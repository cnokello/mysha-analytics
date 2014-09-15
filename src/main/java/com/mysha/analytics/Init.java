package com.mysha.analytics;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mysha.analytics.transformer.DrugSideEffectTransformer;
import com.mysha.analytics.transformer.DrugTransformer;

/**
 * This class demonstrates streaming from Kafka
 * 
 * @author nelson.okello
 * 
 */
public class Init {

  private static final Logger LOGGER = Logger.getLogger(Init.class);

  private static final ApplicationContext ctx = new ClassPathXmlApplicationContext(
      "META-INF/applicationContext.xml");

  public Init() {
  }

  public static void main(String[] args) throws Exception {
    final DrugTransformer drugTransformer = (DrugTransformer) ctx.getBean("drugTransformer");
    final DrugSideEffectTransformer sideEffectTransformer = (DrugSideEffectTransformer) ctx
        .getBean("drugSideEffectTransformer");

    drugTransformer.transform();
    sideEffectTransformer.transform();

  }
}
