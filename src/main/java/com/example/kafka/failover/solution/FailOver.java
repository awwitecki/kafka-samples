package com.example.kafka.failover.solution;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FailOver {

    private static final String KAFKA_TOPIC = "fail-over";
    private static final String KAFKA_BROKERS = "localhost:32816";

    private final Seq<Integer> consumptionDistribution = List.of(30, 30, 30);
    private final Integer totalConsumption = consumptionDistribution.sum().intValue();
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws Exception {
        final FailOver failOver = new FailOver();
        failOver.
            consumptionDistribution
            .map(FailOver::createConsumptionJob)
            .forEach(failOver.executor::submit);
        Thread.sleep(500);
        failOver.produceEvents();
    }

    private void produceEvents() throws Exception {
        final Map<String, Object> producerConfiguratiom = createProducerConfiguratiom();
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfiguratiom)) {
            for (int i = 0; i < totalConsumption; ++i) {
                final ProducerRecord<String, String> record = createKafkaEvent(i);
                producer.send(record).get(5, SECONDS);
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

    private static Runnable createConsumptionJob(int failOnCount) {
        return () -> {
            System.out.println(String.format("%d - failOnCount: %d", Thread.currentThread().getId(), failOnCount));
            final Map<String, Object> consumerConfiguration = createConsumerConfiguration();
            final KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfiguration);
            consumer.subscribe(Collections.singleton(KAFKA_TOPIC));
            int consumptionCount = 0;
            while (true) {
                for (ConsumerRecord<String, String> record : consumer.poll(100)) {
                    System.out.println(formatKafkaEvent(record));
                    consumptionCount++;
                }
                if (consumptionCount >= failOnCount) {
                    System.out.println(String.format("%s - STOPPED", Thread.currentThread().getId()));
                    consumer.close();
                    break;
                }
            }
        };
    }

    private static String formatKafkaEvent(ConsumerRecord<String, String> record) {
        return String.format("%s:%s - %d:%d [%s; %s]",
            System.nanoTime() / 1e6,
            Thread.currentThread().getId(),
            record.partition(),
            record.offset(),
            record.key(),
            record.value());
    }

    private static Map<String, Object> createConsumerConfiguration() {
        return HashMap.<String, Object>of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS,
            ConsumerConfig.GROUP_ID_CONFIG, "fail-over-group-id",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
            ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"
        ).toJavaMap();
    }
}
