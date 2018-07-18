package com.example.kafka.classic.solution;

import io.vavr.collection.HashMap;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClassicProducer {

    private static final String KAFKA_TOPIC = "classic-10";
    private static final String KAFKA_BROKERS = "localhost:32811";

    public static void main(String[] args) throws Exception {
        final Map<String, Object> producerConfiguratiom = createProducerConfiguratiom();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfiguratiom)) {
            for (int i = 0; i < 10; ++i) {
                final ProducerRecord<String, String> record = createKafkaEvent(i);
                producer.send(record).get(2, TimeUnit.SECONDS);
            }
            producer.flush();
        }
    }

    private static ProducerRecord<String, String> createKafkaEvent(int index) {
        return new ProducerRecord<>(KAFKA_TOPIC, null, createEventValue(index));
    }

    private static String createEventValue(int index) {
        return String.format("Event %d", index);
    }

    private static Map<String, Object> createProducerConfiguratiom() {
        return HashMap.<String, Object>of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        ).toJavaMap();
    }
}
