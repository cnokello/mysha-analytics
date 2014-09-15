package com.mysha.analytics.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * This class is responsible for loading configuration properties and retrieving specific properties
 * 
 * @author nelson.okello
 * 
 */
@Configuration
@PropertySource({ "classpath:META-INF/cfg.properties" })
@Service(value = "configLoader")
public class ConfigLoader {

  private @Autowired
  Environment env;

  public Environment getEnv() {
    return env;
  }

}
