package com.uci.utils.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.uci.utils.ApplicationConfiguration;
import com.uci.utils.kafka.SimpleProducer;

@SpringBootTest(classes=ApplicationConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class SimpleProducerTest {
	@Autowired
    public SimpleProducer kafkaProducer;
	
	@Value("${kafka.test.topic}")
	public String kafkaTopic;
	
	@Test
	public void sendTest() {
		kafkaProducer.send(kafkaTopic, "Utils test");
	}
}
