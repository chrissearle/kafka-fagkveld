package no.itera.kafka.basics;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleProducer {
  private static final Logger LOG = LoggerFactory.getLogger(SampleProducer.class);

  private static Producer<Long, String> createProducer() {
    // Configure the producer
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.SERVER);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "SampleProducer");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    return new KafkaProducer<>(props);
  }

  static void runProducer(final int count) throws Exception {
    long time = System.currentTimeMillis();

    try (Producer<Long, String> producer = createProducer()) {
      // Produce count records
      for (long idx = time; idx < time + count; idx++) {
        // Create the data we're going to send
        final ProducerRecord<Long, String> record = new ProducerRecord<>(Config.TOPIC_NAME, idx, "Message: " + idx);

        // Send it and get the metadata back
        RecordMetadata metadata = producer.send(record).get();

        long elapsed = System.currentTimeMillis() - time;

        LOG.info("Time [{}] Record[{} -> {}] Meta[Partition[{}] Offset[{}]]", elapsed, record.key(), record.value()
            , metadata.partition(), metadata.offset());
      }
    }
  }

  public static void main(String[] args) throws Exception {
    LOG.info("Producer: Start");
    runProducer(5);
    LOG.info("Producer: Done");
  }
}
