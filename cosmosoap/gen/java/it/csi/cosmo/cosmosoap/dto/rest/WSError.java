/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class WSError  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String errorContext = null;
  private Integer errorNumber = null;
  private String errorMessage = null;

  /**
   **/
  


  // nome originario nello yaml: errorContext 
  public String getErrorContext() {
    return errorContext;
  }
  public void setErrorContext(String errorContext) {
    this.errorContext = errorContext;
  }

  /**
   **/
  


  // nome originario nello yaml: errorNumber 
  public Integer getErrorNumber() {
    return errorNumber;
  }
  public void setErrorNumber(Integer errorNumber) {
    this.errorNumber = errorNumber;
  }

  /**
   **/
  


  // nome originario nello yaml: errorMessage 
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WSError wsError = (WSError) o;
    return Objects.equals(errorContext, wsError.errorContext) &&
        Objects.equals(errorNumber, wsError.errorNumber) &&
        Objects.equals(errorMessage, wsError.errorMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(errorContext, errorNumber, errorMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WSError {\n");
    
    sb.append("    errorContext: ").append(toIndentedString(errorContext)).append("\n");
    sb.append("    errorNumber: ").append(toIndentedString(errorNumber)).append("\n");
    sb.append("    errorMessage: ").append(toIndentedString(errorMessage)).append("\n");
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

