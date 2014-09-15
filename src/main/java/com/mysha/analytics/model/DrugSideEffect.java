package com.mysha.analytics.model;

import java.io.Serializable;

public class DrugSideEffect implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String description;

  private String timestamp;

  public DrugSideEffect() {
  }

  public DrugSideEffect(String id, String name, String description, String timestamp) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "DrugSideEffect [id=" + id + ", name=" + name + ", description=" + description
        + ", timestamp=" + timestamp + "]";
  }

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
}
