package com.mysha.analytics;

import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

/**
 * A simple logistic regression app
 * 
 * @author nelson.okello
 * 
 */
public final class SimpleLogisticRegressionApp {

  static class ParsePoint implements Function<String, LabeledPoint> {
    private static final Pattern COMMA = Pattern.compile(",");
    private static final Pattern SPACE = Pattern.compile(" ");

    @Override
    public LabeledPoint call(String line) throws Exception {
      String[] parts = COMMA.split(line);
      double y = Double.parseDouble(parts[0]);
      String[] tok = SPACE.split(parts[1]);
      double[] x = new double[tok.length];
      for (int i = 0; i < tok.length; i++) {
        x[i] = Double.parseDouble(tok[i]);
      }

      return new LabeledPoint(y, Vectors.dense(x));
    }
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.err
          .println("]]]]]]]] Usage: SimpleLogisticRegressionApp <input_dir> <step_size> <niters>");
      System.exit(1);
    }

    // Setup
    SparkConf cfg = new SparkConf().setAppName("Simple Logistic Regression App");
    JavaSparkContext ctx = new JavaSparkContext(cfg);
    JavaRDD<String> lines = ctx.textFile(args[0]);
    JavaRDD<LabeledPoint> points = lines.map(new ParsePoint()).cache();
    double stepSize = Double.parseDouble(args[1]);
    int iterations = Integer.parseInt(args[2]);

    // Logistic Regression
    LogisticRegressionModel model = LogisticRegressionWithSGD.train(points.rdd(), iterations,
        stepSize);

    System.out.println("]]]]]]]] Final Hyperplane: " + model.weights());

    ctx.stop();
  }
}
