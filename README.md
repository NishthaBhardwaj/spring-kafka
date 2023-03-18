# spring-kafka

## Setting Up Kafka 3.0.0
-- Start up the Zookeeper.
- ./zookeeper-server-start.sh ../config/zookeeper.properties

- Add the below properties in the server.properties

listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false

## How to create a topic ?

./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic

## How to instantiate a Console Producer?

## Without Key

- ./kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic --property "key.separator=-" --property "parse.key=true"

### With Key

./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning -property "key.separator= - " --property "print.key=true"

### With Consumer Group

    ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --group <group-name>
  
### Consume messages With Kafka Headers
  
     ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic library-events.DLT --from-beginning --property print.headers=true --property print.timestamp=true
  

  
  
