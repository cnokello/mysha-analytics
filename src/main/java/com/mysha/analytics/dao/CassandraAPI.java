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
import com.mysha.analytics.model.Drug;
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

  private BoundStatement boundStatement;

  public void connect() {
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

  public void close() {
    if (cluster != null) {
      cluster.close();
    }
  }

  /**
   * Creates the database schema
   */
  public void createSchema() {
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
  public void persistDrug(final Drug drug) throws Exception {
    connect();

    boundStatement = new BoundStatement(DRUG_PSTMT);
    session.execute(boundStatement.bind(drug.getId(), drug.getName(), drug.getDescription(),
        drug.getIndication(), drug.getPharmacoDynamics(), drug.getActionMechanism(),
        drug.getToxicity(), drug.getMetabolism(), drug.getAbsorption(), drug.getEliminationRoute(),
        drug.getHalfLife(), drug.getDistributionVolume()));

  }

  /**
   * Persists drug side effect object into the database
   * 
   * @param effect
   * @throws Exception
   */
  public void persistDrugSideEffect(final DrugSideEffect effect) throws Exception {
    connect();

    LOGGER.info("Saving to DB ... " + effect.toString());

    boundStatement = new BoundStatement(DRUG_SIDE_EFFECT_PSTMT);
    session.execute(boundStatement.bind(effect.getId(), effect.getName(), effect.getDescription()));

    LOGGER.info("Saved to DB: " + effect.toString());

  }
}
