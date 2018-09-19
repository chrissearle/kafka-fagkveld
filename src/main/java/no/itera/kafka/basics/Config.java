package no.itera.kafka.basics;

import java.time.Duration;

public class Config {
  public static final String TOPIC_NAME = "basics-test-topic";
  public static final String SERVER = "localhost:9092";
  public static final Duration POLL_INTERVAL = Duration.ofSeconds(1);
  public static final long POLL_DURATION = 30 * 1000;

}
