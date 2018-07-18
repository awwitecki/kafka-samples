# Kafka streams
1. Create Kafka stream which will consume events from our classic producer, uppercase their values,
store them in another topic, then assign them fixed keys and some changed values and once again 
store them in another topic
    1. e.g.<br> <topic_1>: `[null, "event 1"]` -> <br><topic_2>: `[null, "EVENT 1"]` -><br> <topic_3>: `["FIXED_KEY", "EVENT 1_withFixedKey"]`
    2. if you want to, you can find working solution in the `solution` sub-package
    3. Streams can be defined using `StreamsBuilder` (see it #stream and #build methods)
    4. We can manipulate data using #map, #mapValues, and so on and so forth
    5. We can route data using #through and #to
    6. We can create ready-to-run stream with `KafkaStreams` - there we need to pass our configured streams and configuration (see `StreamsConfig`)
    7. Remember to call #cleanUp before #start
2. Start consuming events on target topics
    * you can do this with console using
        ```
        kafka-console-consumer --bootstrap-server <kafka_brokers> --topic <topic_name> --from-beginning --property key.separator=, --property print.key=true
        ```
3. Start your Kafka stream and observe the results on target topics
