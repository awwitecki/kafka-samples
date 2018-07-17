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
Everything is already prepared in docker-compose.yml. Just run in project root directory:
```
docker-compose up -d
```
This will start up **1 Zookeeper** node and **3 Kafka** nodes, which will be available under:
* Zookeeper: `localhost:2181`
* Kafka: `localhost:9092`, `localhost:9093`, `localhost:9094`

When you're done playing you can shutdown everything with `docker-compose down`.
