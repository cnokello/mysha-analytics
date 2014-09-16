package com.mysha.analytics.dao;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.mysha.analytics.Init;
import com.mysha.analytics.model.Disease;
import com.mysha.analytics.model.Drug;
import com.mysha.analytics.model.DrugClass;
import com.mysha.analytics.model.DrugSideEffect;
import com.mysha.analytics.utils.ConfigLoader;

@Service(value = "cassandraAPI")
public class CassandraAPI {

  private @Autowired
  ConfigLoader cfg;

  private @Value("${log.basedir}")
  String logBaseDir;

  private Cluster cluster;

  private Session session;

  private ObjectMapper objectMapper = new ObjectMapper();

  private static final Logger LOGGER = Logger.getLogger(Init.class);

  private PreparedStatement DRUG_PSTMT;

  private PreparedStatement DRUG_SIDE_EFFECT_PSTMT;

  private PreparedStatement DISEASE_PSTMT;

  private PreparedStatement DRUG_CLASS_PSTMT;

  private BoundStatement boundStatement;

  public synchronized void connect() {
    if (session == null) {
      try {
        LOGGER.info("Connecting to Cassandra cluster....");

        final String host = cfg.getEnv().getProperty("cassandra.host");
        cluster = Cluster.builder().addContactPoint(host).build();
        session = cluster.connect();

        createSchema();

        DRUG_PSTMT = session
            .prepare("INSERT INTO my_health.drugs (id, name, description, indication, pharmacoDynamics, "
                + "actionMechanism, toxicity, metabolism, absorption, eliminationRoute, "
                + "halfLife, distributionVolume) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");

        DRUG_SIDE_EFFECT_PSTMT = session
            .prepare("INSERT INTO my_health.drug_side_effects(id, name, description) VALUES(?,?,?)");

        DISEASE_PSTMT = session
            .prepare("INSERT INTO my_health.diseases(id, class0, class1, class2, name, description) VALUES(?,?,?,?,?,?)");

        DRUG_CLASS_PSTMT = session
            .prepare("INSERT INTO my_health.drug_classes(id, class0, class1, class2, name, description) VALUES(?,?,?,?,?,?)");

        LOGGER.info("Connected to Cassandra cluster");

      } catch (Exception e) {
        try {
          FileUtils.writeStringToFile(
              new File(logBaseDir + "/mysha-analytics.error"),
              String.format("Message: %s\nTrace:%s\n\n", e.getMessage(),
                  ExceptionUtils.getStackTrace(e)), true);
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        LOGGER.error(String.format("Message: %s\nTrace:%s\n\n", e.getMessage(),
            ExceptionUtils.getStackTrace(e)));
      }
    }

  }

  public synchronized void close() {
    if (cluster != null) {
      cluster.close();
    }
  }

  /**
   * Creates the database schema
   */
  public synchronized void createSchema() {
    connect();

    LOGGER.info("Creating database schema...");

    try {
      session.execute("CREATE KEYSPACE IF NOT EXISTS my_health WITH replication = "
          + "{ 'class': 'SimpleStrategy', 'replication_factor': 3  };");

      session.execute("CREATE TABLE IF NOT EXISTS my_health.drugs (" + "id text PRIMARY KEY, "
          + "name text, " + "description text, " + "indication text, " + "pharmacoDynamics text, "
          + "actionMechanism text, " + "toxicity text, " + "metabolism text, "
          + "absorption text, " + "eliminationRoute text, " + "halfLife text, "
          + "distributionVolume text" + ");");

      session
          .execute("CREATE TABLE IF NOT EXISTS my_health.drug_side_effects(id text PRIMARY KEY, name text, description text)");

      session
          .execute("CREATE TABLE IF NOT EXISTS my_health.diseases(id text PRIMARY KEY, class0 text, class1 text, class2 text, name text, description text)");

      session
          .execute("CREATE TABLE IF NOT EXISTS my_health.drug_classes(id text PRIMARY KEY, class0 text, class1 text, class2 text, name text, description text)");

    } catch (Exception e) {
      LOGGER.error(String.format("Message: %s\nTrace: %s\n\n", e.getMessage(),
          ExceptionUtils.getStackTrace(e)));
    }

    LOGGER.info("Database schema created");
  }

  /**
   * Persists drug object into the database
   * 
   * @param drug
   * @throws Exception
   */
  public synchronized void persistDrug(final Drug drug) throws Exception {
    connect();

    LOGGER.info("Saving to DB ... " + drug.toString());

    boundStatement = new BoundStatement(DRUG_PSTMT);
    session.execute(boundStatement.bind(drug.getId(), drug.getName(), drug.getDescription(),
        drug.getIndication(), drug.getPharmacoDynamics(), drug.getActionMechanism(),
        drug.getToxicity(), drug.getMetabolism(), drug.getAbsorption(), drug.getEliminationRoute(),
        drug.getHalfLife(), drug.getDistributionVolume()));

    LOGGER.info("Saved to DB: " + drug.toString());
  }

  /**
   * Persists drug side effect object into the database
   * 
   * @param effect
   * @throws Exception
   */
  public synchronized void persistDrugSideEffect(final DrugSideEffect effect) throws Exception {
    connect();

    LOGGER.info("Saving to DB ... " + effect.toString());

    boundStatement = new BoundStatement(DRUG_SIDE_EFFECT_PSTMT);
    session.execute(boundStatement.bind(effect.getId(), effect.getName(), effect.getDescription()));

    LOGGER.info("Saved to DB: " + effect.toString());

  }

  /**
   * Persists disease into the database
   * 
   * @param disease
   * @throws Exception
   */
  public synchronized void persistDisease(final Disease disease) throws Exception {
    connect();

    LOGGER.info("Saving to DB ... " + disease.toString());

    boundStatement = new BoundStatement(DISEASE_PSTMT);
    session.execute(boundStatement.bind(disease.getId(), disease.getClass0(), disease.getClass1(),
        disease.getClass2(), disease.getName(), disease.getDescription()));

    LOGGER.info("Saved to DB ... " + disease.toString());
  }

  /**
   * Persists drug class to DB
   * 
   * @param drugClass
   * @throws Exception
   */
  public synchronized void persistDrugClass(final DrugClass drugClass) throws Exception {
    connect();

    LOGGER.info("Saving to DB ... " + drugClass.toString());

    boundStatement = new BoundStatement(DRUG_CLASS_PSTMT);
    session.execute(boundStatement.bind(drugClass.getId(), drugClass.getClass0(),
        drugClass.getClass1(), drugClass.getClass2(), drugClass.getName(),
        drugClass.getDescription()));

    LOGGER.info("Saved to DB ... " + drugClass.toString());
  }
}
