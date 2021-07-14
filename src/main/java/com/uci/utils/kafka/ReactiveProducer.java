/*
 * Copyright (c) 2016-2021 VMware Inc. or its affiliates, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.uci.utils.kafka;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Service
@Slf4j
public class ReactiveProducer {

    public KafkaSender<Integer, String> sender;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

    public void sendMessages(String topic, String message) {
        Long timestamp = Instant.now().getEpochSecond();

        Mono<SenderRecord<Integer, String, Integer>> outboundFlux = Mono.just(
                SenderRecord.create(topic, 0, timestamp, null, message, null)
        );
        sender.<Integer>send(outboundFlux)
                .doOnError(e -> log.error("Send failed", e))
                .subscribe(r -> {
                    RecordMetadata metadata = r.recordMetadata();
                    log.debug(String.format("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n", r.correlationMetadata(),
                            metadata.topic(),
                            metadata.partition(),
                            metadata.offset(),
                            dateFormat.format(new Date(metadata.timestamp())))
                    );
                });
    }

    public void close() {
        sender.close();
    }
}