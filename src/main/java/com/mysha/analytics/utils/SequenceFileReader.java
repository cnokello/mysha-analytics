package com.mysha.analytics.utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.log4j.Logger;

public class SequenceFileReader {

  private static final Logger LOGGER = Logger.getLogger(SequenceFileReader.class);

  public static SequenceFile.Reader getReader(String filePath) throws Exception {
    LOGGER.info("Retrieving a sequence file reader...");

    Configuration cfg = new Configuration();
    FileSystem fs = FileSystem.get(cfg);
    Path path = new Path(filePath);

    SequenceFile.Reader reader = null;
    try {
      reader = new SequenceFile.Reader(fs, path, cfg);

      LOGGER.info("Sequence file reader obtained.");

    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    } finally {
      IOUtils.closeStream(reader);
    }

    return reader;
  }

  public static void main(String[] args) {
    try {
      getReader("nelson.okello");
    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }
  }

}
