package com.mysha.analytics.business;

import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.model.Disease;

public class DiseaseProcessor implements Callable<Long> {

  private static final Logger LOGGER = Logger.getLogger(DiseaseProcessor.class);

  private CassandraAPI cassandra;

  private KafkaStream dataStream;

  private int threadNumber;

  private Gson gson = new Gson();

  public DiseaseProcessor(KafkaStream dataStream, int threadNumber, CassandraAPI cassandra) {
    this.dataStream = dataStream;
    this.threadNumber = threadNumber;
    this.cassandra = cassandra;
  }

  @Override
  public Long call() throws Exception {
    ConsumerIterator<byte[], byte[]> it = dataStream.iterator();

    while (it.hasNext()) {
      final String diseaseJson = new String(it.next().message());
      Disease _disease = gson.fromJson(diseaseJson, Disease.class);

      cassandra.persistDisease(_disease);

      LOGGER.info(_disease.toString());

    }

    return 1l;
  }

}
