package com.tinhvan.hd.base;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

public class MQ {

	private Connection connection;
	private Map<UUID, Map<String, Channel>> channelList;

	private static MQ instance;

	private MQ() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			Config config = HDConfig.getInstance();
			String rabbitMQURI = config.get("RABBITMQ_URI");
			factory.setUri(rabbitMQURI);
			connection = factory.newConnection();
			channelList = new HashMap<>();
		} catch (Exception ex) {
			throw new InternalServerErrorException(ex.getMessage());
		}
	}

	private static MQ getInstance() {
		if (instance == null) {
			instance = new MQ();
		}
		return instance;
	}

	private static Channel createChannel(String queueName, String exchangeName,
			String exchangeType, String routeKey) {

		Channel channel;
		try {
			MQ mq = getInstance();
			Connection con = mq.getConnection();
			Map<UUID, Map<String, Channel>> channelList = mq.getChannelList();
			UUID requestId = HDServletRequest.getRequestId();

			Map<String, Channel> channelInContext;
			if (!channelList.containsKey(requestId)) {
				channelInContext = new HashMap<>();
				channelList.put(requestId, channelInContext);
			} else {
				channelInContext = channelList.get(requestId);
			}
			if (channelInContext == null) {
				throw new Exception("channelInContext is null");
			}
			if (channelInContext.containsKey(queueName)) {
				channel = channelInContext.get(queueName);
			} else {
				channel = con.createChannel();
				// channel.queueBind(queue, exchange, routingKey);
				if (routeKey == null) {
				}
				channel.queueDeclare(queueName, false, false, false, null);
				if (exchangeName != null) {
					if (exchangeType == null) {
						exchangeType = "topic";
					}
					channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);
				}
				channelInContext.put(queueName, channel);
			}
		} catch (Exception ex) {
			throw new InternalServerErrorException(ex.getMessage());
		}
		return channel;
	}


	public static void closeChannel() {
		UUID requestId = HDServletRequest.getRequestId();
		closeChannel(requestId);
	}


	public static void closeChannel(UUID requestId) {
		if (instance == null) {
			return;
		}
		MQ mq = getInstance();
		Map<UUID, Map<String, Channel>> channelList = mq.getChannelList();
		if (channelList == null) {
			return;
		}

		if (channelList.containsKey(requestId)) {
			Map<String, Channel> channelInContext = channelList.get(requestId);
			if (channelInContext != null) {
				channelInContext.forEach((q, c) -> {
					try {
						if (c != null) {
							c.close();
						}
					} catch (Exception ex) {
						Log.system("[MQ][closeChannel]", ex.getMessage());
					}
				});
				channelInContext.clear();
			}
			channelList.remove(requestId);
		}
	}

	private static void closeAllChannel() {
		if (instance == null) {
			return;
		}
		MQ mq = getInstance();
		Map<UUID, Map<String, Channel>> channelList = mq.getChannelList();
		channelList.forEach((r, channelInContext) -> {
			channelInContext.forEach((q, c) -> {
				try {
					if (c != null) {
						c.close();
					}
				} catch (Exception ex) {
					Log.system("[MQ][closeAllChannel]", ex.getMessage());
				}
			});
			channelInContext.clear();
		});
		channelList.clear();
	}

	private static void closeConnection() {
		if (instance == null) {
			return;
		}
		MQ mq = getInstance();
		Connection connection = mq.getConnection();
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception ex) {
			Log.system("[MQ][closeConnection]", ex.getMessage());
		}
	}

	public static void disposeAll() {
//		Log.info("[MQ][shutdown]", "START");
		//Log.system("[MQ][shutdown]", "START");
		closeAllChannel();
		closeConnection();
	}

	public static <T> void publish(String queueName, String exchangeName, String exchangeType, String routeKey, T payload) {
		try {
			Channel channel = createChannel(queueName, exchangeName, exchangeType, routeKey);
			if (channel == null) {
				return;
			}
			Gson gson = HDUtil.gson();
			String message = gson.toJson(payload);
			AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
				.contentType("application/json")
				.build();

			channel.basicPublish("", queueName, properties, message.getBytes());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new InternalServerErrorException(ex.getMessage());
		}
	}

	public static <T> void publish(String queueName, String exchangeName, String exchangeType, T payload) {
		publish(queueName, exchangeName, exchangeType, null, payload);
	}

	public static <T> void publish(String queueName, String exchangeName, T payload) {
		publish(queueName, exchangeName, null, null, payload);
	}

	public static <T> void publish(String queueName, T payload) {
		publish(queueName, null, null, null, payload);
	}

	private Connection getConnection() {
		return this.connection;
	}

	private Map<UUID, Map<String, Channel>> getChannelList() {
		return this.channelList;
	}


}
