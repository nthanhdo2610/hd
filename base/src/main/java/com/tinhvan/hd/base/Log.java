package com.tinhvan.hd.base;

import com.tinhvan.hd.base.enities.LogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.StringJoiner;

@Component
public class Log {

    private static final Logger LOG = LoggerFactory.getLogger(Log.class);
    private static RabbitTemplate rabbitTemplate;

    public Log(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void system(String label, Object... arg) {
        //insertLog(4, label, arg);
        publishLog(4, label, arg);
        String format = createLogString(label, arg);
        LOG.error(format, arg);
    }

    public static void debug(String label, Object... arg) {
        publishLog(0, label, arg);
        // insertLog(0, label, arg);
        String format = createLogString(label, arg);
        LOG.info(format, arg);
    }

    public static void print(String label, Object... arg) {
        String format = createLogString(label, arg);
        LOG.info(format, arg);
    }

    public static void info(String label, Object... arg) {
        publishLog(1, label, arg);
        String format = createLogString(label, arg);
        LOG.info(format, arg);
    }

    public static void warn(String label, Object... arg) {
        publishLog(2, label, arg);
        String format = createLogString(label, arg);
        LOG.warn(format, arg);
    }

    public static void error(String label, Object... arg) {
        publishLog(3, label, arg);
        String format = createLogString(label, arg);
        LOG.error(format, arg);
    }

    private static String createLogString(String label, Object... arg) {
        StringJoiner format = new StringJoiner(" ");
        format.add(label);
        for (int i = 0; i < arg.length; i++) {
            format.add("{}");
        }
        return format.toString();
    }

    private static LogEntity insertLog(int level, String label, Object... arg) {
        //LogDAO dao = getInstance();
        LogEntity entity = new LogEntity();
        entity.setLabel(label);
        entity.setContent(arg);
        entity.setCreateDate(new Date());
        //dao.insert(entity);
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE, entity);
        return entity;
    }

    private static LogEntity publishLog(int level, String label, Object... arg) {
        LogEntity entity = new LogEntity();
        entity.setLabel(label);
        entity.setContent(arg);
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE, entity);
        return entity;
    }

}
