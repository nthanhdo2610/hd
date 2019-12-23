package com.tinhvan.hd.config;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author satish sharma
 * <pre>
 *   Periodically poll the service instaces and update the in memory store as key value pair	
 * </pre>
 */
@Component
public class ServiceDescriptionUpdater {
	//implements InitializingBean
	private static final Logger logger = LoggerFactory.getLogger(ServiceDescriptionUpdater.class);

	private static final String IPV4_REGEX =
			"^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
					"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);
	
	private static final String DEFAULT_SWAGGER_URL="/v2/api-docs";
	private static final String KEY_SWAGGER_URL="swagger_url";
	private static final String IP_ADDRESS="http://192.168.75.205:";
	
	@Autowired
	private DiscoveryClient discoveryClient;

	private final RestTemplate template;
	
	public ServiceDescriptionUpdater(){
		this.template = new RestTemplate();
	}
	
	@Autowired
	private ServiceDefinitionsContext definitionContext;
	
	@Scheduled(fixedDelayString= "${swagger.config.refreshrate}")
	public void refreshSwaggerConfigurations(){
		logger.debug("Starting Service Definition Context refresh");

		discoveryClient.getServices().stream().forEach(serviceId -> {
			logger.debug("Attempting service definition refresh for Service : {} ", serviceId);
			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
			if(serviceInstances == null || serviceInstances.isEmpty()){ //Should not be the case kept for failsafe
				logger.info("No instances available for service : {} ",serviceId);
			}else{
				ServiceInstance instance = serviceInstances.get(0);
				String swaggerURL =  getSwaggerURL( instance);

				Optional<Object> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL);

				if(jsonData.isPresent()){
					String content = getJSON(serviceId, jsonData.get());
					definitionContext.addServiceDefinition(serviceId, content);
				}else{
					logger.error("Skipping service id : {} Error : Could not get Swagegr definition from API ",serviceId);
				}

				logger.info("Service Definition Context Refreshed at :  {}",LocalDate.now());
			}
		});
	}
	
	private String getSwaggerURL(ServiceInstance instance){
				String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
				String urlHostIp = IP_ADDRESS;
				String ipVal = instance.getHost();
				if (isValidInet4Address(ipVal)) {
					urlHostIp = "http://" + ipVal + ":";
				}
				return swaggerURL != null ? instance.getUri()+swaggerURL : urlHostIp +instance.getPort()+DEFAULT_SWAGGER_URL;
			}

			private Optional<Object> getSwaggerDefinitionForAPI(String serviceName, String url){
				logger.debug("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);
				try{
					Object jsonData = template.getForObject(url, Object.class);
					return Optional.of(jsonData);
		}catch(RestClientException ex){
			logger.error("Error while getting service definition for service : {} Error : {} ", serviceName, ex.getMessage());
			return Optional.empty();
		}
		 
	}

	public static boolean isValidInet4Address(String ip) {
		if (ip == null) {
			return false;
		}

		Matcher matcher = IPv4_PATTERN.matcher(ip);

		return matcher.matches();
	}

	public String getJSON(String serviceId, Object jsonData){
		try {
			return new ObjectMapper().writeValueAsString(jsonData);
		} catch (JsonProcessingException e) {
			logger.error("Error : {} ", e.getMessage());
			return "";
		}
	}

//	@Override
//	public void afterPropertiesSet() throws Exception {
//		logger.debug("Starting Service Definition Context refresh");
//
//		discoveryClient.getServices().stream().forEach(serviceId -> {
//			logger.debug("Attempting service definition refresh for Service : {} ", serviceId);
//			List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
//			if(serviceInstances == null || serviceInstances.isEmpty()){ //Should not be the case kept for failsafe
//				logger.info("No instances available for service : {} ",serviceId);
//			}else{
//				ServiceInstance instance = serviceInstances.get(0);
//				String swaggerURL =  getSwaggerURL( instance);
//
//				Optional<Object> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL);
//
//				if(jsonData.isPresent()){
//					String content = getJSON(serviceId, jsonData.get());
//					definitionContext.addServiceDefinition(serviceId, content);
//				}else{
//					logger.error("Skipping service id : {} Error : Could not get Swagegr definition from API ",serviceId);
//				}
//
//				logger.info("Service Definition Context Refreshed at :  {}",LocalDate.now());
//			}
//		});
//	}
}
