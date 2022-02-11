package com.uci.utils.telemetry.service;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SuppressWarnings("ALL")
@Slf4j
@Service
public class TelemetryService {
	public WebClient getWebClient() {
		return WebClient.builder()
				.baseUrl(System.getenv("POSTHOG_TELEMETRY_URL"))
				.defaultHeader("Content-Type", "application/json")
				.build();
	}
	
	@SuppressWarnings("deprecation")
	public Mono<String> sendEvent(String event, ObjectNode properties) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode node = mapper.createObjectNode();
		node.put("api_key", System.getenv("POSTHOG_TELEMETRY_APIKEY"));
		
		node.put("properties", properties);
		node.put("distinct_id", "7a5ca61f-dc4e-49ee-83f0-ae0f8d5f5818");
		
//		LocalDateTime now = LocalDateTime.now();  
//		node.put("timestamp", now.toString());
		node.put("event", event);
		node.put("type", "alias");
		
		log.info("json node:"+node);
		
		return getWebClient().post()
			.uri(builder -> builder.path("batch/").build())
			.body(BodyInserters.fromObject(node))
			.retrieve()
			.bodyToMono(String.class)
			.map(response -> {
				if (response != null) {
					log.info("Telemetry event sent successfully.");
				}
				return response;
			})
			.doOnError(throwable -> log.info("Error in sending telemetry event: " + throwable.getMessage()))
			.onErrorReturn("");
		
//		return Mono.just("");
		
	}
}
