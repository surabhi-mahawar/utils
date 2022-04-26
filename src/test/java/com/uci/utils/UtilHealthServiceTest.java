package com.uci.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.uci.utils.kafka.KafkaConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UtilsTestConfig.class)
//@ExtendWith(MockitoExtension.class)
class UtilHealthServiceTest {

    @Autowired
    UtilHealthService utilHealthService;

    @MockBean
    KafkaConfig kafkaConfig;

    @MockBean
    BotService botService;

    @AfterAll
    static void teardown(){
        System.out.println("teardown");
    }

    @Test
    void getKafkaHealthNode() throws JsonProcessingException {
        Mockito.when(kafkaConfig.kafkaHealthIndicator()).thenReturn(new HealthIndicator() {
            @Override
            public Health health() {
                return Health.up().build();
            }
        });
        JsonNode result = utilHealthService.getKafkaHealthNode();
        assertEquals(true, result.get("healthy").asBoolean());
    }

    @Test
    void getCampaignUrlHealthNode() throws IOException {
        utilHealthService.campaignUrl = "campaignUrl";
        JsonNode result = utilHealthService.getCampaignUrlHealthNode();
        assertNotNull(result);
    }
}