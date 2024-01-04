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

public class ServiceStatus  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String component = null;
  private Object details = null;
  private String enviroment = null;
  private String message = null;
  private String name = null;
  private String product = null;
  private Integer responseTime = null;
  private String status = null;
  private String version = null;

  /**
   **/
  


  // nome originario nello yaml: component 
  public String getComponent() {
    return component;
  }
  public void setComponent(String component) {
    this.component = component;
  }

  /**
   **/
  


  // nome originario nello yaml: details 
  public Object getDetails() {
    return details;
  }
  public void setDetails(Object details) {
    this.details = details;
  }

  /**
   **/
  


  // nome originario nello yaml: enviroment 
  public String getEnviroment() {
    return enviroment;
  }
  public void setEnviroment(String enviroment) {
    this.enviroment = enviroment;
  }

  /**
   **/
  


  // nome originario nello yaml: message 
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   **/
  


  // nome originario nello yaml: name 
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  


  // nome originario nello yaml: product 
  public String getProduct() {
    return product;
  }
  public void setProduct(String product) {
    this.product = product;
  }

  /**
   **/
  


  // nome originario nello yaml: responseTime 
  public Integer getResponseTime() {
    return responseTime;
  }
  public void setResponseTime(Integer responseTime) {
    this.responseTime = responseTime;
  }

  /**
   **/
  


  // nome originario nello yaml: status 
  @NotNull
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   **/
  


  // nome originario nello yaml: version 
  public String getVersion() {
    return version;
  }
  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServiceStatus serviceStatus = (ServiceStatus) o;
    return Objects.equals(component, serviceStatus.component) &&
        Objects.equals(details, serviceStatus.details) &&
        Objects.equals(enviroment, serviceStatus.enviroment) &&
        Objects.equals(message, serviceStatus.message) &&
        Objects.equals(name, serviceStatus.name) &&
        Objects.equals(product, serviceStatus.product) &&
        Objects.equals(responseTime, serviceStatus.responseTime) &&
        Objects.equals(status, serviceStatus.status) &&
        Objects.equals(version, serviceStatus.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(component, details, enviroment, message, name, product, responseTime, status, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ServiceStatus {\n");
    
    sb.append("    component: ").append(toIndentedString(component)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("    enviroment: ").append(toIndentedString(enviroment)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    product: ").append(toIndentedString(product)).append("\n");
    sb.append("    responseTime: ").append(toIndentedString(responseTime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

