package com.example.kafka.classic.solution;

import io.vavr.collection.HashMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Map;

public class ClassicConsumer {

    private static final String KAFKA_TOPIC = "classic-10";
    private static final String KAFKA_BROKERS = "localhost:32811";

    public static void main(String[] args) {
        final Map<String, Object> consumerConfiguration = createConsumerConfiguration();
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfiguration)){
            consumer.subscribe(Collections.singleton(KAFKA_TOPIC));
            while (true) {
                final ConsumerRecords<String, String> records = consumer.poll(1000);
                records.forEach(it -> System.out.println(String.format("%d:%d - [%s; %s]", it.partition(), it.offset(), it.key(), it.value())));
            }
        }
    }

    private static Map<String, Object> createConsumerConfiguration() {
        return HashMap.<String, Object>of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS,
            ConsumerConfig.GROUP_ID_CONFIG, "classic-group-id",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
        ).toJavaMap();
    }
}
