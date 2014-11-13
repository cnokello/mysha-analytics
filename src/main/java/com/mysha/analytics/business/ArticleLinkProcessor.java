package com.mysha.analytics.business;

import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.ml.BayesClassifier;
import com.mysha.analytics.model.ArticleLink;
import com.mysha.analytics.utils.ConfigLoader;

public class ArticleLinkProcessor implements Callable<Long> {

  private Logger LOGGER = Logger.getLogger(ArticleLinkProcessor.class);

  private CassandraAPI cassandra;

  private @Autowired
  ConfigLoader cfg;

  private KafkaStream dataStream;

  private int threadNumber;

  private BayesClassifier classifier;

  private Gson gson = new Gson();

  public ArticleLinkProcessor() {
  }

  public ArticleLinkProcessor(KafkaStream dataStream, int threadNumber, CassandraAPI cassandra,
      BayesClassifier classifier) {
    this.dataStream = dataStream;
    this.threadNumber = threadNumber;
    this.cassandra = cassandra;
    this.classifier = classifier;
  }

  @Override
  public Long call() throws Exception {
    ConsumerIterator<byte[], byte[]> it = dataStream.iterator();

    while (it.hasNext()) {
      final String drugJson = new String(it.next().message());
      final ArticleLink articleLink = gson.fromJson(drugJson, ArticleLink.class);
      final String category = classifier.classify(articleLink.getId(), articleLink.getTitle());
      LOGGER.info(category);

      cassandra.persistArticleLink(articleLink);

      LOGGER.info(articleLink.toString());

    }

    return 1l;
  }
}
