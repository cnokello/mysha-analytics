package com.mysha.analytics.business;

import java.util.concurrent.Callable;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mysha.analytics.dao.CassandraAPI;
import com.mysha.analytics.model.DrugClass;
import com.mysha.analytics.utils.ConfigLoader;

public class DrugClassProcessor implements Callable<Long> {

  private Logger LOGGER = Logger.getLogger(DrugProcessor.class);

  private CassandraAPI cassandra;

  private @Autowired
  ConfigLoader cfg;

  private KafkaStream dataStream;

  private int threadNumber;

  private Gson gson = new Gson();

  public DrugClassProcessor() {
  }

  public DrugClassProcessor(KafkaStream dataStream, int threadNumber, CassandraAPI cassandra) {
    this.dataStream = dataStream;
    this.threadNumber = threadNumber;
    this.cassandra = cassandra;
  }

  @Override
  public Long call() throws Exception {
    ConsumerIterator<byte[], byte[]> it = dataStream.iterator();
    while (it.hasNext()) {
      final String drugJson = new String(it.next().message());
      DrugClass _drug = gson.fromJson(drugJson, DrugClass.class);

      cassandra.persistDrugClass(_drug);

      LOGGER.info(_drug.toString());

    }

    return 1l;
  }
}
