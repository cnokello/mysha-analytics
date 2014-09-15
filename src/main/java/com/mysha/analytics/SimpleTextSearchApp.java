package com.mysha.analytics;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * Performs a simple text file search
 * 
 * @author nelson.okello
 * 
 */
public class SimpleTextSearchApp {

  public static void main(String[] args) {
    String fileToSearchPath = "/home/nelson.okello/tmp/test-files/equity";
    SparkConf cfg = new SparkConf().setAppName("Simple Text Search App");
    JavaSparkContext ctx = new JavaSparkContext(cfg);
    JavaRDD<String> fileToSearch = ctx.textFile(fileToSearchPath);

    // Filter those mentioning LIMITED
    JavaRDD<String> limitedCompanies = fileToSearch.filter(new Function<String, Boolean>() {
      public Boolean call(String s) {
        return s.contains("LIMITED");
      }
    });

    // Count those mentioning LIMITED
    System.out.println("]]]]]]]]] Total LIMITED companies: " + limitedCompanies.count());

    // Fetch those mentioning KENYA as an array of strings
    List<String> companies = limitedCompanies.filter(new Function<String, Boolean>() {
      public Boolean call(String s) {
        return s.contains("KENYA");
      }
    }).collect();

    for (String c : companies) {
      System.out.println("]]]]]]]] Company: " + c);
    }

  }
}
