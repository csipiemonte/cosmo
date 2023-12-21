/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest.process;

/**
 *
 */

public class ProcessInstanceVariableEventDTO {

  private String messageType;
  private String businessKey;
  private String variableName;
  private String variableType;
  private String textVariableValue;
  private Long longVariableValue;
  private Double doubleVariableValue;
  private byte[] bytearrayVariableValue;

  public ProcessInstanceVariableEventDTO() {
    // Auto-generated constructor stub
  }

  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public String getBusinessKey() {
    return businessKey;
  }

  public void setBusinessKey(String businessKey) {
    this.businessKey = businessKey;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  public String getVariableType() {
    return variableType;
  }

  public void setVariableType(String variableType) {
    this.variableType = variableType;
  }

  public String getTextVariableValue() {
    return textVariableValue;
  }

  public void setTextVariableValue(String textVariableValue) {
    this.textVariableValue = textVariableValue;
  }

  public Long getLongVariableValue() {
    return longVariableValue;
  }

  public void setLongVariableValue(Long longVariableValue) {
    this.longVariableValue = longVariableValue;
  }

  public Double getDoubleVariableValue() {
    return doubleVariableValue;
  }

  public void setDoubleVariableValue(Double doubleVariableValue) {
    this.doubleVariableValue = doubleVariableValue;
  }

  public byte[] getBytearrayVariableValue() {
    return bytearrayVariableValue;
  }

  public void setBytearrayVariableValue(byte[] bytearrayVariableValue) {
    this.bytearrayVariableValue = bytearrayVariableValue;
  }

  @Override
  public String toString() {
    return "VariableEvent [businessKey=" + businessKey + ", messageType=" + messageType
        + ", variableName=" + variableName + ", variableType=" + variableType
        + ", textVariableValue=" + textVariableValue + "," + ", longVariableValue="
        + longVariableValue + ", doubleVariableValue=" + doubleVariableValue
        + "]";
  }



}
