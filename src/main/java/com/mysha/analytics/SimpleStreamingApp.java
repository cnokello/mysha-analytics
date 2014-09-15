package com.mysha.analytics;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import scala.Tuple2;

/**
 * A simple streaming application that receives text from a data server using a TCP socket
 * 
 * @author nelson.okello
 * 
 */
public class SimpleStreamingApp {

  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) {
    if (args.length < 2) {
      System.err.println("Usage: SimpleStreamingApp <hostname> <port>");
      System.exit(1);
    }

    // StreamingExamples.setStreamingLogLevels();

    // Streaming context
    SparkConf cfg = new SparkConf().setAppName("Simple Streaming App");
    JavaStreamingContext ctx = new JavaStreamingContext(cfg, new Duration(5000));

    // A DStream that connects to the data server, host:port
    JavaReceiverInputDStream<String> lines = ctx.socketTextStream(args[0],
        Integer.parseInt(args[1]), StorageLevels.MEMORY_AND_DISK_SER);

    // Process the received line of text
    JavaDStream<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
      @Override
      public Iterable<String> call(String x) throws Exception {
        return Arrays.asList(SPACE.split(x));
      }
    });

    // Count each waord in each batch
    JavaPairDStream<String, Integer> pairs = words
        .mapToPair(new PairFunction<String, String, Integer>() {
          @Override
          public Tuple2<String, Integer> call(String s) throws Exception {
            return new Tuple2<String, Integer>(s, 1);
          }

        });

    JavaPairDStream<String, Integer> wordCounts = pairs
        .reduceByKey(new Function2<Integer, Integer, Integer>() {
          @Override
          public Integer call(Integer i1, Integer i2) throws Exception {
            return i1 + i2;
          }

        });

    wordCounts.print();

    // Start processing and wait until its done
    ctx.start();
    ctx.awaitTermination();
  }
}
