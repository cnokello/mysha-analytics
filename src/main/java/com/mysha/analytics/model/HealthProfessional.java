package com.mysha.analytics.model;

import java.io.Serializable;

public class HealthProfessional implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String name;

  private String qualification;

  private String specialty;

  private String subSpecialty;

  private String address;

  private String profession;

  private String localForeign;

  private String type;

  public HealthProfessional() {
  }

  public HealthProfessional(String id, String name, String qualification, String specialty,
      String subSpecialty, String address, String profession, String localForeign, String type) {
    super();
    this.id = id;
    this.name = name;
    this.qualification = qualification;
    this.specialty = specialty;
    this.subSpecialty = subSpecialty;
    this.address = address;
    this.profession = profession;
    this.localForeign = localForeign;
    this.type = type;
  }

  @Override
  public String toString() {
    return "HealthProfessional [id=" + id + ", name=" + name + ", qualification=" + qualification
        + ", specialty=" + specialty + ", subSpecialty=" + subSpecialty + ", address=" + address
        + ", profession=" + profession + ", localForeign=" + localForeign + ", type=" + type + "]";
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

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getSubSpecialty() {
    return subSpecialty;
  }

  public void setSubSpecialty(String subSpecialty) {
    this.subSpecialty = subSpecialty;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getProfession() {
    return profession;
  }

  public void setProfession(String profession) {
    this.profession = profession;
  }

  public String getLocalForeign() {
    return localForeign;
  }

  public void setLocalForeign(String localForeign) {
    this.localForeign = localForeign;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
