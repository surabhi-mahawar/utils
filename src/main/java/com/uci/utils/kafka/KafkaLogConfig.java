package com.uci.utils.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaLogConfig {
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String BOOTSTRAP_SERVERS;
	
	@Value("${kafka.logs.topic}")
	private String KAFKA_LOGS_TOPIC;
	
	/* Create kafka log topic */
	public void createLogTopic() {
		Properties properties = new Properties();
//		properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
////		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
////		properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//		properties.put("key.deserializer", "org.springframework.kafka.support.serializer.JsonSerializer.class");
//		properties.put("value.deserializer", "org.springframework.kafka.support.serializer.JsonSerializer.class");
//		properties.put("group.id", "logs");
		
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "logs");
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

		KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
		List topics = new ArrayList();
		topics.add(KAFKA_LOGS_TOPIC);
		kafkaConsumer.subscribe(topics);
	}
}

