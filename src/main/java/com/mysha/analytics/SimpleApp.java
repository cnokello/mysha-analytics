package com.mysha.analytics;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * A simple application that demos file ingestion capability of Spark
 * 
 * @author nelson.okello
 * 
 */
public class SimpleApp {

  public static void main(String[] args) {
    String logFile = "/home/nelson.okello/sws/spark-1.0.2-bin-hadoop2/README.md";
    SparkConf conf = new SparkConf().setAppName("Simple Application");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> logData = sc.textFile(logFile).cache();

    long numAs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) {
        return s.contains("a");
      }
    }).count();

    long numBs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) {
        return s.contains("b");
      }
    }).count();

    System.out.println(">>>>>>>>>>>>>>> Lines with a: " + numAs + ", lines with b: " + numBs);

    // Parallelized processing
    List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    JavaRDD<Integer> distData = sc.parallelize(data);

    // RDDs basics
    JavaRDD<String> lines = sc
        .textFile("/home/nelson.okello/sws/spark-1.0.2-bin-hadoop2/README.md");
    JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
      public Integer call(String s) {
        return s.length();
      }
    });

    int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
      public Integer call(Integer a, Integer b) {
        return a + b;
      }
    });

    System.out.println(">>>>>>>> Total Length: " + totalLength);

  }
}
