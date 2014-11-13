package com.mysha.analytics.model;

import java.io.Serializable;

public class HealthFacility implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String regNumber;

  private String address;

  private String facilityType;

  private String bedCapacity;

  private String type;

  public HealthFacility() {
  }

  public HealthFacility(String id, String name, String regNumber, String address,
      String facilityType, String bedCapacity, String type) {
    super();
    this.id = id;
    this.name = name;
    this.regNumber = regNumber;
    this.address = address;
    this.facilityType = facilityType;
    this.bedCapacity = bedCapacity;
    this.type = type;
  }

  @Override
  public String toString() {
    return "HealthFacility [id=" + id + ", name=" + name + ", regNumber=" + regNumber
        + ", address=" + address + ", facilityType=" + facilityType + ", bedCapacity="
        + bedCapacity + ", type=" + type + "]";
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

  public String getRegNumber() {
    return regNumber;
  }

  public void setRegNumber(String regNumber) {
    this.regNumber = regNumber;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getFacilityType() {
    return facilityType;
  }

  public void setFacilityType(String facilityType) {
    this.facilityType = facilityType;
  }

  public String getBedCapacity() {
    return bedCapacity;
  }

  public void setBedCapacity(String bedCapacity) {
    this.bedCapacity = bedCapacity;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
