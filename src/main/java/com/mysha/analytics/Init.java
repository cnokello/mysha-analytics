package com.mysha.analytics;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mysha.analytics.transformer.ArticleLinkTransformer;
import com.mysha.analytics.transformer.DiseaseTransformer;
import com.mysha.analytics.transformer.DrugClassTransformer;
import com.mysha.analytics.transformer.DrugSideEffectTransformer;
import com.mysha.analytics.transformer.DrugTransformer;
import com.mysha.analytics.transformer.HealthFacilityTransformer;
import com.mysha.analytics.transformer.HealthProfessionalTransformer;

/**
 * This class is the entry point for streams processing
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

    // Drug streams process
    final DrugTransformer drugTransformer = (DrugTransformer) ctx.getBean("drugTransformer");

    // Drug side effects streams processing
    final DrugSideEffectTransformer sideEffectTransformer = (DrugSideEffectTransformer) ctx
        .getBean("drugSideEffectTransformer");

    // Disease streams processing
    final DiseaseTransformer diseaseTransformer = (DiseaseTransformer) ctx
        .getBean("diseaseTransformer");

    // Drug classes streams processing
    final DrugClassTransformer drugClassTransformer = (DrugClassTransformer) ctx
        .getBean("drugClassTransformer");

    // Health professional streams processing
    final HealthProfessionalTransformer healthProfessionalTransformer = (HealthProfessionalTransformer) ctx
        .getBean("healthProfessionalTransformer");

    // Health facility streams processing
    final HealthFacilityTransformer healthFacilityTransformer = (HealthFacilityTransformer) ctx
        .getBean("healthFacilityTransformer");

    // Article link streams processing
    final ArticleLinkTransformer articleLinkTransformer = (ArticleLinkTransformer) ctx
        .getBean("articleLinkTransformer");

    // Run the streams
    drugTransformer.transform();
    sideEffectTransformer.transform();
    diseaseTransformer.transform();
    drugClassTransformer.transform();
    healthProfessionalTransformer.transform();
    healthFacilityTransformer.transform();
    articleLinkTransformer.transform();
  }
}
