package com.uci.utils.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.fusionauth.jwt.domain.Header;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

@Service
@Slf4j
public class SimpleProducer1 {

    private final KafkaTemplate<String, String> producer;

    public SimpleProducer1(KafkaTemplate<String, String> producer) {
    	this.producer = producer;
    }

    public void send(String topic, String message, Context currentContext) {
    	List<RecordHeader> headers = Arrays.asList(new RecordHeader("header_key", "header_value".getBytes()));
    	GlobalOpenTelemetry.getPropagators().getTextMapPropagator().inject(currentContext, headers, null);
    	log.info("headers:"+headers);
    	Context extracted = GlobalOpenTelemetry.getPropagators().getTextMapPropagator().extract(Context.current(), headers, null);
        log.info("extracted: "+extracted);
    	ProducerRecord<String, String> record = new ProducerRecord(topic, null, "", message, headers);
        
    	producer
                .send(record)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(@NotNull Throwable throwable) {
                        log.error("Unable to push {} to {} topic due to {}", message, topic, throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> stringStringSendResult) {
                        log.info("Pushed to topic {}", topic);
                    }
                });
    }
    
    public void send(String topic, String message) {
    	List<RecordHeader> headers = Arrays.asList(new RecordHeader("header_key", "header_value".getBytes()));
    	ProducerRecord<String, String> record = new ProducerRecord(topic, null, "", message, headers);
        
    	producer
                .send(record)
                .addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
                    @Override
                    public void onFailure(@NotNull Throwable throwable) {
                        log.error("Unable to push {} to {} topic due to {}", message, topic, throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, String> stringStringSendResult) {
                        log.info("Pushed to topic {}", topic);
                    }
                });
    }
}