package com.mysha.analytics.business;

import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.model.DrugSideEffect;
import com.mysha.analytics.utils.ConfigLoader;

public class DrugSideEffectProcessor implements Callable<Long> {

  private Logger LOGGER = Logger.getLogger(DrugSideEffectProcessor.class);

  private CassandraAPI cassandra;

  private @Autowired
  ConfigLoader cfg;

  private KafkaStream dataStream;

  private int threadNumber;

  private Gson gson = new Gson();

  public DrugSideEffectProcessor() {
  }

  public DrugSideEffectProcessor(KafkaStream dataStream, int threadNumber, CassandraAPI cassandra) {
    this.dataStream = dataStream;
    this.threadNumber = threadNumber;
    this.cassandra = cassandra;
  }

  @Override
  public Long call() throws Exception {
    ConsumerIterator<byte[], byte[]> it = dataStream.iterator();
    while (it.hasNext()) {
      final String drugJson = new String(it.next().message());
      DrugSideEffect sideEffect = gson.fromJson(drugJson, DrugSideEffect.class);

      cassandra.persistDrugSideEffect(sideEffect);

      LOGGER.info(sideEffect.toString());

    }

    return 1l;
  }
}
