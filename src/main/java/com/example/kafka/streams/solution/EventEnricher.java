package com.example.kafka.streams.solution;

import io.vavr.collection.HashMap;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.ValueMapper;

public class EventEnricher {

    private static final String KAFKA_TOPIC = "classic-10";
    private static final String KAFKA_BROKERS = "localhost:32811";
    private static final Serde<String> KEY_SERDE = Serdes.String();
    private static final Serde<String> VALUE_SERDE = Serdes.String();

    public static void main(String[] args) {
        final StreamsConfig streamConfiguration = createStreamConfiguration();
        final Topology streamTopology = buildStreamTopology();
        final KafkaStreams streams = new KafkaStreams(streamTopology, streamConfiguration);
        streams.cleanUp();
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    private static Topology buildStreamTopology() {
        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, String> stream = builder.stream(KAFKA_TOPIC, Consumed.with(KEY_SERDE, VALUE_SERDE))
            .mapValues((ValueMapper<String, String>) String::toUpperCase)
            .through("upper-classic-10")
            .map((k, v) -> new KeyValue<>("FIXED_KEY", v + "_withFixedKey"));
        stream.to("fixed-upper-classic-10");
        return builder.build();
    }

    private static StreamsConfig createStreamConfiguration() {
        return new StreamsConfig(HashMap.<String, Object>of(
            StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS,
            StreamsConfig.APPLICATION_ID_CONFIG, "stream-sample",
            StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, KEY_SERDE.getClass(),
            StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, VALUE_SERDE.getClass()
        ).toJavaMap());
    }
}
