# kafka-samples

## Prerequisities
* Java
* Docker
    * On Mac: `https://docs.docker.com/docker-for-mac/install/`
* Kafka
    * On Mac: `brew install kafka`
* Zookeeper
    * On Mac: `brew install zookeeper`

## Set up Kafka cluster with Zookeeper
Everything is already prepared in docker-compose.yml. Just run in project root directory (replacing `<local_ip>` with your own IP address - cannot be localhost):
```
# Start 3 Kafka nodes and Zookeeper node
export HOST_IP=<local_ip> && docker-compose up --scale kafka=3 -d

# Check ports under which Kafka is available
docker port kafka-samples_kafka_1 && \
docker port kafka-samples_kafka_2 && \
docker port kafka-samples_kafka_3
```
This will start up **1 Zookeeper** node and **3 Kafka** nodes, which will be available under:
* Zookeeper: `localhost:2181`
* Kafka: `localhost:<port_1>`, `localhost:<port_2>`, `localhost:<port_3>`

When you're done playing you can shutdown everything with `docker-compose down`.

## Exercises
You may find them in the related Java packages, i.e.
1. Classic approach - `com.example.kafka.classic`
2. Kafka Streams - `com.example.kafka.streams`
