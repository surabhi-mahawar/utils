package com.uci.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.uci.utils.kafka.KafkaLogConfig;

@SpringBootApplication
public class UtilsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UtilsApplication.class, args);
        
        /* Create kafka log topic on application run */
        KafkaLogConfig kafkaLogConfig = new KafkaLogConfig();
        kafkaLogConfig.createLogTopic();
    }

}
