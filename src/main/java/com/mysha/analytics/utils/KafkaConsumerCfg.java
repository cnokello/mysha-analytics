package com.mysha.analytics.utils;

import java.util.Properties;

import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "kafkaConsumerCfg")
public class KafkaConsumerCfg {

  private @Autowired
  ConfigLoader cfg;

  public ConsumerConnector init(final String topic) throws Exception {
    Properties props = new Properties();
    props.put("zookeeper.connect", cfg.getEnv().getProperty("zookeeper.host.port"));
    props.put("group.id", cfg.getEnv().getProperty("kafka.consumer.group"));
    props.put("zookeeper.session.timeout.ms", cfg.getEnv().getProperty("zookeeper.timeout.ms"));
    props.put("zookeeper.sync.time.ms", cfg.getEnv().getProperty("zookeeper.sync.time.ms"));
    props
        .put("auto.commit.interval.ms", cfg.getEnv().getProperty("transformer.commit.interval.ms"));

    return kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
  }
}
