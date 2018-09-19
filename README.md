# Kafka - simple example

## Running kafka and zookeeper

To make things simple - we start a single zookeeper and a single kafka broker in docker.

* docker/clear.sh - stop and delete any running/existing containers
* docker/start.sh - start up zookeeper and kafka
* docker/docker-compose.yml - the docker image configuration used

## Testing with the command line tools

### List all existing topics

    kafka-topics --zookeeper localhost:2181 --list

### Create a topic

    kafka-topics --zookeeper localhost:2181 --create --topic testTopic --partitions 1 --replication-factor 1

### Send data to a topic

    kafka-console-producer --broker-list localhost:9092 --topic testTopic

`kafka-console-producer` will give you a prompt where you can type strings - each line will be sent as a message.

### Consume data from the topic

    kafka-console-consumer --bootstrap-server localhost:9092 --topic testTopic

`kafka-console-consumer` will read and print to screen any messages it sees.

When run like this - it will run from "now" - so will only see new messages. If you add the `--from-beginning` parameter - it will read everything in the topic.

## Java

Topic creation for the java code:

    kafka-topics --zookeeper localhost:2181 --create --topic basics-test-topic --partitions 1 --replication-factor 1

The `SampleProducer` code simply sends in 5 messages. The key is a long, the message is a string.

The `SampleConsumer` code reads and logs what it sees. If you run the code as is - it will only see new messages. You can uncomment the line containing `AUTO_OFFSET_RESET_CONFIG` to see everything in the topic - but - if you have previously run the code - it will likely not show them since the consumer group id is set and kafka knows that this group has already been used. Change the group id and you should then see all records.
