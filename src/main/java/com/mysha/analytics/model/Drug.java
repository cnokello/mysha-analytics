package com.mysha.analytics.model;

import java.io.Serializable;

public class Drug implements Serializable {

  // private UUID id = UUID.randomUUID();

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String description;

  private String indication;

  private String pharmacoDynamics;

  private String actionMechanism;

  private String toxicity;

  private String metabolism;

  private String absorption;

  private String eliminationRoute;

  private String halfLife;

  private String distributionVolume;

  public Drug() {
  }

  public Drug(String id, String name, String description, String indication,
      String pharmacoDynamics, String actionMechanism, String toxicity, String metabolism,
      String absorption, String eliminationRoute, String halfLife, String distributionVolume) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.indication = indication;
    this.pharmacoDynamics = pharmacoDynamics;
    this.actionMechanism = actionMechanism;
    this.toxicity = toxicity;
    this.metabolism = metabolism;
    this.absorption = absorption;
    this.eliminationRoute = eliminationRoute;
    this.halfLife = halfLife;
    this.distributionVolume = distributionVolume;
  }

  @Override
  public String toString() {
    return "Drug [id=" + id + ", name=" + name + ", description=" + description + ", indication="
        + indication + ", pharmacoDynamics=" + pharmacoDynamics + ", actionMechanism="
        + actionMechanism + ", toxicity=" + toxicity + ", metabolism=" + metabolism
        + ", absorption=" + absorption + ", eliminationRoute=" + eliminationRoute + ", halfLife="
        + halfLife + ", distributionVolume=" + distributionVolume + "]";
  }

  /*
   * public UUID getId() { return id; }
   * 
   * public void setId(UUID id) { this.id = id; }
   */

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getIndication() {
    return indication;
  }

  public void setIndication(String indication) {
    this.indication = indication;
  }

  public String getPharmacoDynamics() {
    return pharmacoDynamics;
  }

  public void setPharmacoDynamics(String pharmacoDynamics) {
    this.pharmacoDynamics = pharmacoDynamics;
  }

  public String getActionMechanism() {
    return actionMechanism;
  }

  public void setActionMechanism(String actionMechanism) {
    this.actionMechanism = actionMechanism;
  }

  public String getToxicity() {
    return toxicity;
  }

  public void setToxicity(String toxicity) {
    this.toxicity = toxicity;
  }

  public String getMetabolism() {
    return metabolism;
  }

  public void setMetabolism(String metabolism) {
    this.metabolism = metabolism;
  }

  public String getAbsorption() {
    return absorption;
  }

  public void setAbsorption(String absorption) {
    this.absorption = absorption;
  }

  public String getEliminationRoute() {
    return eliminationRoute;
  }

  public void setEliminationRoute(String eliminationRoute) {
    this.eliminationRoute = eliminationRoute;
  }

  public String getHalfLife() {
    return halfLife;
  }

  public void setHalfLife(String halfLife) {
    this.halfLife = halfLife;
  }

  public String getDistributionVolume() {
    return distributionVolume;
  }

  public void setDistributionVolume(String distributionVolume) {
    this.distributionVolume = distributionVolume;
  }

}
