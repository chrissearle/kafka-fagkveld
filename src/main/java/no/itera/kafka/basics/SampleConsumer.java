package no.itera.kafka.basics;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleConsumer {
  private static final Logger LOG = LoggerFactory.getLogger(SampleConsumer.class);

  private static Consumer<Long, String> createConsumer() {
    // Configure the consumer
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.SERVER);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "SampleConsumer");
    // Uncomment this to run from the earliest data rather than from "now"
    // props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    final Consumer<Long, String> consumer = new KafkaConsumer<>(props);

    // Subscribe the consumer to the topic
    consumer.subscribe(Collections.singletonList(Config.TOPIC_NAME));

    return consumer;
  }

  static void runConsumer() {
    // We want to poll for 30 seconds
    final long runUntil = System.currentTimeMillis() + Config.POLL_DURATION;

    // Get the consumer already subscribed to the topic
    try (Consumer<Long, String> consumer = createConsumer()) {
      while (System.currentTimeMillis() < runUntil) {
        LOG.info("Consumer: Polling");
        final ConsumerRecords<Long, String> consumerRecords = consumer.poll(Config.POLL_INTERVAL);

        // If we didn't see anything new - we will get a count of 0 so just loop.
        if (consumerRecords.count() > 0) {
          LOG.info("Consumer: Polled {} records", consumerRecords.count());
          consumerRecords.forEach(record -> {
            LOG.info("Record[{} -> {}] Meta[Partition[{}] Offset[{}]]\n", record.key(), record.value(),
                record.partition(), record.offset()
            );
          });

          // Tell kafka we have read these messages
          consumer.commitAsync();
        }
      }
    }
  }

  public static void main(String[] args) {
    LOG.info("Consumer: Start");
    runConsumer();
    LOG.info("Consumer: Done");
  }
}
