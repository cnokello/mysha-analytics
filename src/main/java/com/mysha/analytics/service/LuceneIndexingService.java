package com.mysha.analytics.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysha.analytics.utils.ConfigLoader;

@Service(value = "luceneIndexingService")
public class LuceneIndexingService {

  private static final Logger LOGGER = Logger.getLogger(LuceneIndexingService.class);

  private @Autowired
  ConfigLoader cfg;

  private Directory indexDir;

  private Analyzer analyzer;

  private IndexWriter indexWriter;

  @SuppressWarnings("deprecation")
  private Field id = new Field("id", "", Field.Store.YES, Field.Index.NOT_ANALYZED,
      Field.TermVector.NO);

  @SuppressWarnings("deprecation")
  private Field categoryField = new Field("category", "", Field.Store.YES,
      Field.Index.NOT_ANALYZED, Field.TermVector.NO);

  @SuppressWarnings("deprecation")
  private Field contentField = new Field("content", "", Field.Store.NO, Field.Index.ANALYZED,
      Field.TermVector.WITH_POSITIONS_OFFSETS);

  public synchronized void init(final String docType) throws Exception {

    if (indexDir == null || analyzer == null) {
      LOGGER.info("Initializing indexing...");

      indexDir = FSDirectory.open(new File(cfg.getEnv().getProperty("index.basedir") + "/"
          + docType)); //
      analyzer = new EnglishAnalyzer();
      analyzer.setVersion(Version.LUCENE_4_10_0);
      IndexWriterConfig indexWriterCfg = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
      indexWriter = new IndexWriter(indexDir, indexWriterCfg);

      LOGGER.info("Indexing initialization complete.");
    }

  }

  /**
   * Creates an index for use with the K-Nearest Neighbor algorithm
   * 
   * @throws Exception
   */
  public void createKNNIndex(FileInputStream in) throws Exception {

  }

  /**
   * Creates an index for use with the TF-IDF algorithm
   * 
   * @throws Exception
   */
  public void createTFIDFIndex() throws Exception {

  }

  public void index(final String docType, final String key, final String content) {
    try {
      init(docType);

      LOGGER.info("Adding document to the lucene index...");
      Document doc = new Document();
      doc.add(new TextField("key", key, Field.Store.YES));
      doc.add(new StringField("content", content, Field.Store.YES));
      indexWriter.addDocument(doc);

      LOGGER.info("Document successfully added to the index");

    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }

  }

  public void commit() throws Exception {
    try {
      LOGGER.info("Committing indexing...");
      if (indexWriter != null) {
        indexWriter.commit();
      }

      LOGGER.info("Indexing committed.");

    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }
  }

  public void close() throws Exception {
    LOGGER.info("Attempting to close index writer...");

    try {
      if (indexWriter != null) {
        indexWriter.commit();
        indexWriter.close();
      }
    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }
  }

  public static void main(String[] args) {
    LuceneIndexingService indexingService = new LuceneIndexingService();
    indexingService.index("DOCTOR", "1234444", "Okello is a nice guy");
  }
}
