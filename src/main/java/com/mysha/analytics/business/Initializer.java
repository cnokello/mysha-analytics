package com.mysha.analytics.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.model.Drug;
import com.mysha.analytics.utils.ConfigLoader;

@Service(value = "initializer")
public class Initializer {

  private static final Logger LOGGER = Logger.getLogger(Initializer.class);

  private @Autowired
  ConfigLoader cfg;

  private @Autowired
  DrugProcessor drugProcessor;

  private @Autowired
  CassandraAPI cassandra;

  private JavaPairReceiverInputDStream<String, String> kafkaInputStream;

  private volatile JavaStreamingContext streamingCtx;

  public JavaStreamingContext getStreamingCtx() {
    return streamingCtx;
  }

  /**
   * Performs initialization activities: connection to DB, Kafka Input Stream
   * 
   * @throws Exception
   */
  public synchronized void setup() throws Exception {

    // Setup Cassandra
    // cassandra.createSchema();
    // cassandra.close();

    // Setup Spark
    SparkConf sparkCfg = new SparkConf(true).setAppName(cfg.getEnv().getProperty("spark.appname"))
        .setMaster(cfg.getEnv().getProperty("spark.master"))
        .set("spark.executor.memory", cfg.getEnv().getProperty("spark.memory"))
        .set("spark.cassandra.connection.host", cfg.getEnv().getProperty("cassandra.host"))
        .set("spark.cassandra.username", "cassandra").set("spark.cassandra.password", "cassandra");

    streamingCtx = new JavaStreamingContext(sparkCfg, new Duration(Integer.parseInt(cfg.getEnv()
        .getProperty("kafka.duration"))));

    String[] topics = (cfg.getEnv().getProperty("kafka.topics.drugs")).split(",");
    Map<String, Integer> topicMap = new HashMap<String, Integer>();
    for (String topic : topics) {
      topicMap.put(topic, Integer.parseInt(cfg.getEnv().getProperty("kafka.threads")));
    }

    kafkaInputStream = KafkaUtils.createStream(
        streamingCtx,
        cfg.getEnv().getProperty("zookeeper.host") + ":"
            + cfg.getEnv().getProperty("zookeeper.port"),
        cfg.getEnv().getProperty("kafka.consumer.group"), topicMap);

    LOGGER.info("Kafka input stream initialized");
  }

  /**
   * 
   * @return Returns a Kafka input stream
   */
  public JavaPairReceiverInputDStream<String, String> getKafkaInputStream() {
    LOGGER.info("Retrieving kafka input stream");
    return kafkaInputStream;
  }

  /**
   * Starts services
   */
  public void start() {
    LOGGER.info("Streaming Spark streaming context");
    if (streamingCtx != null) {
      streamingCtx.start();
    }

    LOGGER.info("Spark streaming context started");
  }

  /**
   * Releases resources when done
   */
  public void tear() {
    LOGGER.info("Releasing resources");

    if (streamingCtx != null) {
      streamingCtx.awaitTermination();
    }

    LOGGER.info("Resources released");
  }

  public DrugProcessor getDrugProcessor() {
    return drugProcessor;
  }

  public void processDrug(final Drug drug) throws Exception {
    LOGGER.info("\n\nOKELLO [[[[[[" + drug.toString() + "\n\n");
  }
}
