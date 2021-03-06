package com.mysha.analytics.transformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysha.analytics.business.ArticleLinkProcessor;
import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.ml.BayesClassifier;
import com.mysha.analytics.utils.ConfigLoader;
import com.mysha.analytics.utils.KafkaConsumerCfg;

/**
 * Processes drugs
 * 
 * @author nelson.okello
 * 
 */
@Service(value = "articleLinkTransformer")
public class ArticleLinkTransformer {

  private static final Logger LOGGER = Logger.getLogger(ArticleLinkTransformer.class);

  private @Autowired
  ConfigLoader cfg;

  private @Autowired
  KafkaConsumerCfg kafkaCfg;

  private @Autowired
  CassandraAPI cassandra;

  private @Autowired
  BayesClassifier classifier;

  private ConsumerConnector consumer;

  private String topic;

  private ExecutorService executor;

  /**
   * Establishes connection to Kafka
   */
  public synchronized void initKafkaConsumer() {
    if (consumer == null) {
      try {
        LOGGER.info("Connecting to Kafka...");

        topic = cfg.getEnv().getProperty("kafka.topics.article.link");
        consumer = kafkaCfg.init(topic);

        LOGGER.info("Connected to Kafka topic " + topic);

      } catch (Exception e) {
        LOGGER.error(String.format("Message: %s\nTrace: %s\n\n", e.getMessage(),
            ExceptionUtils.getStackTrace(e)));
      }
    }
  }

  public void transform() {
    initKafkaConsumer();

    // Perform initialization
    try {
      int numThreads = new Integer(cfg.getEnv().getProperty("kafka.threads"));
      Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
      topicCountMap.put(topic, numThreads);
      Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer
          .createMessageStreams(topicCountMap);
      List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

      // Execute all threads
      executor = Executors.newFixedThreadPool(numThreads);

      // Consume the messages
      int threadNum = 0;
      for (final KafkaStream stream : streams) {
        LOGGER.info("#### A new stream message....");
        executor.submit(new ArticleLinkProcessor(stream, threadNum, cassandra, classifier));
        threadNum++;
      }

    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }

  }
}
