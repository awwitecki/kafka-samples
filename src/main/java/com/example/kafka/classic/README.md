# Classic approach
1. Create classic Kafka producer in Java which will send 10 events with null keys and random values 
    1. if you want to, you can find working solution in the `solution` sub-package
    2. Kafka producer is represented by class `KafkaProducer`
    3. it may take `Properties` or `Map` instances as arguments with necessary configuration
    4. the minimum configuration should be:
        * ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
        * ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
        * ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
    5. you can send events using #send method and passing there `ProducerRecord` instances 
2. Create classic Kafka consumer in Java which will consume these events and print them with information about their partitions, offsets, keys and values
    1. if you want to, you can find working solution in the `solution` sub-package
    2. Kafka consumer is represented by class `KafkaConsumer`
    3. it may take `Properties` or `Map` instances as arguments with necessary configuration
    4. the minimum configuration should be:
        * ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
        * ConsumerConfig.GROUP_ID_CONFIG
        * ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
        * ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
    5. first of all you have to subscribe on the topic (see #subscribe method)
    6. after that you can consume events using #poll method
3. Run your producer and consumer - get acquainted with results
    * what are the partitions for all events and why?
    * if we run the consumer again without running producer - will we get the same result?
    * did you have to point all Kafka brokers as bootstrap servers?
4. Create topic with 10 partitions manually and use it for your producer and consumer
    1. you can do this with console command e.g. `kafka-topics --create --topic classic-10 --partitions 10 --replication-factor 3 --zookeeper localhost:2181`
    2. run again your producer and consumer using new topic
        * what's the difference?
