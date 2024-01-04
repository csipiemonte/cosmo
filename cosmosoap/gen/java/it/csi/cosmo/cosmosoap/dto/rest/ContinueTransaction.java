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

public class ContinueTransaction  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String authData = null;
  private String alias = null;
  private String customerInformation = null;
  private String pin = null;
  private Integer customerCa = null;
  private String sendingType = null;

  /**
   **/
  


  // nome originario nello yaml: authData 
  public String getAuthData() {
    return authData;
  }
  public void setAuthData(String authData) {
    this.authData = authData;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContinueTransaction continueTransaction = (ContinueTransaction) o;
    return Objects.equals(authData, continueTransaction.authData) &&
        Objects.equals(alias, continueTransaction.alias) &&
        Objects.equals(customerInformation, continueTransaction.customerInformation) &&
        Objects.equals(pin, continueTransaction.pin) &&
        Objects.equals(customerCa, continueTransaction.customerCa) &&
        Objects.equals(sendingType, continueTransaction.sendingType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authData, alias, customerInformation, pin, customerCa, sendingType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContinueTransaction {\n");
    
    sb.append("    authData: ").append(toIndentedString(authData)).append("\n");
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    customerInformation: ").append(toIndentedString(customerInformation)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    customerCa: ").append(toIndentedString(customerCa)).append("\n");
    sb.append("    sendingType: ").append(toIndentedString(sendingType)).append("\n");
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

