package com.mysha.analytics;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * A simple word count app written using Spark API
 * 
 * @author nelson.okello
 * 
 */
public class SimpleWordCountApp {

  private static final Logger LOGGER = Logger.getLogger(SimpleWordCountApp.class);

  private static final Pattern SEPARATOR = Pattern.compile("\\|");

  public static void main(String[] args) {

    final String filePath = "/home/nelson.okello/tmp/test-files/equity/KEBNKCRBCA097120140531B068";
    SparkConf cfg = new SparkConf().setAppName("Word count app");
    JavaSparkContext ctx = new JavaSparkContext(cfg);

    // Data
    JavaRDD<String> lines = ctx.textFile(filePath);

    // Extract words
    JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterable<String> call(String s) {
        return Arrays.asList(SEPARATOR.split(s));
      }
    });

    // Ones word-count pairs
    JavaPairRDD<String, Integer> ones = words
        .mapToPair(new PairFunction<String, String, Integer>() {
          @Override
          public Tuple2<String, Integer> call(String s) {
            return new Tuple2<String, Integer>(s, 1);
          }
        });

    // Word counts
    JavaPairRDD<String, Integer> counts = ones
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          @Override
          public Integer call(Integer i1, Integer i2) {
            return i1 + i2;
          }
        });

    // Print out the output
    List<Tuple2<String, Integer>> output = counts.collect();
    for (Tuple2<?, ?> tuple : output) {
      System.out.println("]]]]]]]]] " + tuple._1() + ": " + tuple._2());
    }

    ctx.stop();
  }
}
