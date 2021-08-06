package com.uci.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uci.utils.kafka.KafkaConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UtilHealthService {
	@Value("${campaign.url}")
	String campaignUrl;
	
	@Autowired
	private KafkaConfig kafkaConfig;
	
	@Autowired
	private BotService botService;
	
	/**
	 * Returns kafka health node with kafka health & details
	 * 
	 * @return JsonNode
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public JsonNode getKafkaHealthNode() throws JsonMappingException, JsonProcessingException {
		HealthIndicator kafkaHealthIndicator = kafkaConfig.kafkaHealthIndicator();
		Boolean kafkaHealthy = getIsKafkaHealthy(kafkaHealthIndicator);
    	
		/* Result node */
		ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"healthy\":false}");
        
        /* Add data in result node */
        ((ObjectNode) jsonNode).put("healthy", kafkaHealthy);
        if(kafkaHealthy) {
        	JsonNode kafkaDetailNode = getKafkaHealthDetailsNode(kafkaHealthIndicator);
        	((ObjectNode) jsonNode).put("details", kafkaDetailNode);
        }
        return jsonNode;
	}
	
	/**
	 * Returns Campaign url health 
	 * 
	 * @return JsonNode
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public JsonNode getCampaignUrlHealthNode() throws IOException, JsonProcessingException {
		Boolean campaignStatus = getIsCampaignHealthy();
    	
		/* Result node */
    	ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"healthy\":false}");
        
        /* Add data in result node */
        ((ObjectNode) jsonNode).put("healthy", campaignStatus);
        
        return jsonNode;
	}
	
	/**
	 * Returns kafka health details node
	 * 
	 * @param kafkaHealthIndicator
	 * @return JsonNode
	 */
	private JsonNode getKafkaHealthDetailsNode(HealthIndicator kafkaHealthIndicator) {
		Map<String, Object> kafkaDetails = kafkaHealthIndicator.health().getDetails();
		
		ObjectMapper mapper = new ObjectMapper();
        ObjectNode kafkaDetailNode = mapper.createObjectNode();
        for(Map.Entry<String, Object> entry : kafkaDetails.entrySet()){
        	kafkaDetailNode.put(entry.getKey(), entry.getValue().toString());
        };
        
        return kafkaDetailNode;
	}
	
	/**
	 * Returns kafka health in boolean
	 * 
	 * @param kafkaHealthIndicator
	 * @return Boolean
	 */
	private Boolean getIsKafkaHealthy(HealthIndicator kafkaHealthIndicator) {
		return kafkaHealthIndicator.getHealth(false).getStatus().toString().equals("UP");
	}
	
	/**
	 * Get is campaign url healthy or not
	 * 
	 * @return Boolean
	 */
	private Boolean getIsCampaignHealthy() {
    	ObjectMapper mapper = new ObjectMapper();
    	try {
	    	WebClient webClient = WebClient.create(campaignUrl);
			
	    	String responseBody = webClient.get()
	    			.uri(builder -> builder.path("admin/v1/health").build())
	    			.retrieve()
	                .bodyToMono(String.class)
	                .block();
	    	
			JsonNode root = mapper.readTree(responseBody);
			return root.path("result").get("healty").asBoolean();
		} catch (Exception e) {
			log.info(e.getMessage());
//			System.out.println(e.getMessage());
		}
    	return false;
    }
}
