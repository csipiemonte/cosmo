/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StartTransaction  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer customerCa = null;
  private String sendingType = null;
  private String alias = null;
  private String customerInformation = null;
  private String pin = null;
  private Integer maxTranSize = null;
  private String otp = null;

  /**
   **/
  


  // nome originario nello yaml: customerCa 
  @NotNull
  public Integer getCustomerCa() {
    return customerCa;
  }
  public void setCustomerCa(Integer customerCa) {
    this.customerCa = customerCa;
  }

  /**
   **/
  


  // nome originario nello yaml: sendingType 
  public String getSendingType() {
    return sendingType;
  }
  public void setSendingType(String sendingType) {
    this.sendingType = sendingType;
  }

  /**
   **/
  


  // nome originario nello yaml: alias 
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   **/
  


  // nome originario nello yaml: customerInformation 
  public String getCustomerInformation() {
    return customerInformation;
  }
  public void setCustomerInformation(String customerInformation) {
    this.customerInformation = customerInformation;
  }

  /**
   **/
  


  // nome originario nello yaml: pin 
  public String getPin() {
    return pin;
  }
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   **/
  


  // nome originario nello yaml: maxTranSize 
  @NotNull
  public Integer getMaxTranSize() {
    return maxTranSize;
  }
  public void setMaxTranSize(Integer maxTranSize) {
    this.maxTranSize = maxTranSize;
  }

  /**
   **/
  


  // nome originario nello yaml: otp 
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartTransaction startTransaction = (StartTransaction) o;
    return Objects.equals(customerCa, startTransaction.customerCa) &&
        Objects.equals(sendingType, startTransaction.sendingType) &&
        Objects.equals(alias, startTransaction.alias) &&
        Objects.equals(customerInformation, startTransaction.customerInformation) &&
        Objects.equals(pin, startTransaction.pin) &&
        Objects.equals(maxTranSize, startTransaction.maxTranSize) &&
        Objects.equals(otp, startTransaction.otp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerCa, sendingType, alias, customerInformation, pin, maxTranSize, otp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartTransaction {\n");
    
    sb.append("    customerCa: ").append(toIndentedString(customerCa)).append("\n");
    sb.append("    sendingType: ").append(toIndentedString(sendingType)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    customerInformation: ").append(toIndentedString(customerInformation)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    maxTranSize: ").append(toIndentedString(maxTranSize)).append("\n");
    sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

