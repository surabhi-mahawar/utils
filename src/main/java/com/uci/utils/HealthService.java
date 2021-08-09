package com.uci.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.cassandra.CassandraHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.uci.utils.kafka.KafkaConfig;

@Service
public class HealthService {
	@Value("${campaign.url}")
	String campaignUrl;
	
	@Autowired
	private CassandraOperations cassandraOperations;
	
	@Autowired
	private KafkaConfig kafkaConfig;
	
	@Autowired
	private BotService botService;
	
	/**
	 * Returns health json node for kafka, campaign url and cassandra
	 * 
	 * @return JsonNode
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public JsonNode getAllHealthNode() throws JsonMappingException, JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"checks\":[{\"name\":\"cassandra db\",\"healthy\":false},{\"name\":\"kafka\",\"healthy\":false},{\"name\":\"campaign\",\"healthy\":false}],\"healthy\":true}");
        
        /* Cassandra health info */
        JsonNode cassandraHealthNode = getCassandraHealthNode();
        JsonNode cassandraNode = mapper.createObjectNode();
        ((ObjectNode) cassandraNode).put("name", "Cassandra db");
        ((ObjectNode) cassandraNode).put("healthy", cassandraHealthNode.get("healthy").asBoolean());
        
        /* Kafka health info */
        JsonNode kafkaHealthNode = getKafkaHealthNode();
        JsonNode kafkaNode = mapper.createObjectNode();
        ((ObjectNode) kafkaNode).put("name", "Kafka");
        ((ObjectNode) kafkaNode).put("healthy", kafkaHealthNode.get("healthy").asBoolean());
        ((ObjectNode) kafkaNode).put("details", kafkaHealthNode.get("details"));
        
        /* Campaign url health info */
        JsonNode campaignHealthNode = getCampaignUrlHealthNode();
        JsonNode campaignNode = mapper.createObjectNode();
        ((ObjectNode) campaignNode).put("name", "Campaign");
        ((ObjectNode) campaignNode).put("healthy", campaignHealthNode.get("healthy").asBoolean());
        
        /* create `ArrayNode` object */
        ArrayNode arrayNode = mapper.createArrayNode();
        
        /* add JSON users to array */
        arrayNode.addAll(Arrays.asList(cassandraNode, kafkaNode, campaignNode));
        
        ((ObjectNode) jsonNode).putArray("checks").addAll(arrayNode);
        
        /* System overall health */
        if(kafkaHealthNode.get("healthy").booleanValue() 
        		&& campaignHealthNode.get("healthy").booleanValue()) {
        	((ObjectNode) jsonNode).put("healthy", true);
        } else {
        	((ObjectNode) jsonNode).put("healthy", false);
        }
        
        return jsonNode;
	}
	
	/**
	 * Returns cassandra health node
	 * 
	 * @return
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("deprecation")
	public JsonNode getCassandraHealthNode() throws JsonMappingException, JsonProcessingException {
		CassandraHealthIndicator indicator = new CassandraHealthIndicator(cassandraOperations);
  		Boolean cassandraHealth = indicator.getHealth(false).getStatus().toString().equals("UP");
		
		/* Result node */
		ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"healthy\":false}");
        /* Add data in result node */
        ((ObjectNode) jsonNode).put("healthy", cassandraHealth);
        
        return jsonNode;
	}
	
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
		Boolean campaignStatus = botService.statusUrlCheck(campaignUrl);
    	
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
}
