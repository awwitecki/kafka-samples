# Fail over
1. Create new topic with 2 partitions
    ```
    kafka-topics --create --topic fail-over --partitions 2 --replication-factor 3 --zookeeper localhost:2181
    ```
2. Create and start concurrently 3 consumers, where each of them will fail after consuming first 30 events
    * every event should be printed out with the information about partition, offset, consumer and consumption time in ms
    * configure consumers to poll only one record at once using `MAX_POLL_RECORDS_CONFIG`
3. Create and start producer which will fed the topic with at least 90 events
4. See the results
    * what partitions have been consumed by which consumers?
    * when the third consumer started its consumption?
    * have you noticed any delay in overall consumption at any point of time? why?
