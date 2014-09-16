package com.mysha.analytics.model;

import java.io.Serializable;

public class Disease implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String class0;

  private String class1;

  private String class2;

  private String name;

  private String description;

  private String type;

  public Disease() {
  }

  @Override
  public String toString() {
    return "Disease [id=" + id + ", class0=" + class0 + ", class1=" + class1 + ", class2=" + class2
        + ", name=" + name + ", description=" + description + ", type=" + type + "]";
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getClass0() {
    return class0;
  }

  public void setClass0(String class0) {
    this.class0 = class0;
  }

  public String getClass1() {
    return class1;
  }

  public void setClass1(String class1) {
    this.class1 = class1;
  }

  public String getClass2() {
    return class2;
  }

  public void setClass2(String class2) {
    this.class2 = class2;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
