/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InviaChiamataEsternaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceFruitore = null;
  private String codiceEndpoint = null;
  private String url = null;
  private String metodo = null;
  private String mappaturaQuery = null;
  private String mappaturaUrl = null;
  private String mappaturaRequestBody = null;
  private String mappaturaHeaders = null;
  private String mappaturaOutput = null;
  private Boolean restituisceJson = null;

  /**
   **/
  


  // nome originario nello yaml: codiceFruitore 
  public String getCodiceFruitore() {
    return codiceFruitore;
  }
  public void setCodiceFruitore(String codiceFruitore) {
    this.codiceFruitore = codiceFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEndpoint 
  public String getCodiceEndpoint() {
    return codiceEndpoint;
  }
  public void setCodiceEndpoint(String codiceEndpoint) {
    this.codiceEndpoint = codiceEndpoint;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: metodo 
  public String getMetodo() {
    return metodo;
  }
  public void setMetodo(String metodo) {
    this.metodo = metodo;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaQuery 
  public String getMappaturaQuery() {
    return mappaturaQuery;
  }
  public void setMappaturaQuery(String mappaturaQuery) {
    this.mappaturaQuery = mappaturaQuery;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaUrl 
  public String getMappaturaUrl() {
    return mappaturaUrl;
  }
  public void setMappaturaUrl(String mappaturaUrl) {
    this.mappaturaUrl = mappaturaUrl;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaRequestBody 
  public String getMappaturaRequestBody() {
    return mappaturaRequestBody;
  }
  public void setMappaturaRequestBody(String mappaturaRequestBody) {
    this.mappaturaRequestBody = mappaturaRequestBody;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaHeaders 
  public String getMappaturaHeaders() {
    return mappaturaHeaders;
  }
  public void setMappaturaHeaders(String mappaturaHeaders) {
    this.mappaturaHeaders = mappaturaHeaders;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaOutput 
  public String getMappaturaOutput() {
    return mappaturaOutput;
  }
  public void setMappaturaOutput(String mappaturaOutput) {
    this.mappaturaOutput = mappaturaOutput;
  }

  /**
   **/
  


  // nome originario nello yaml: restituisceJson 
  public Boolean isRestituisceJson() {
    return restituisceJson;
  }
  public void setRestituisceJson(Boolean restituisceJson) {
    this.restituisceJson = restituisceJson;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviaChiamataEsternaRequest inviaChiamataEsternaRequest = (InviaChiamataEsternaRequest) o;
    return Objects.equals(codiceFruitore, inviaChiamataEsternaRequest.codiceFruitore) &&
        Objects.equals(codiceEndpoint, inviaChiamataEsternaRequest.codiceEndpoint) &&
        Objects.equals(url, inviaChiamataEsternaRequest.url) &&
        Objects.equals(metodo, inviaChiamataEsternaRequest.metodo) &&
        Objects.equals(mappaturaQuery, inviaChiamataEsternaRequest.mappaturaQuery) &&
        Objects.equals(mappaturaUrl, inviaChiamataEsternaRequest.mappaturaUrl) &&
        Objects.equals(mappaturaRequestBody, inviaChiamataEsternaRequest.mappaturaRequestBody) &&
        Objects.equals(mappaturaHeaders, inviaChiamataEsternaRequest.mappaturaHeaders) &&
        Objects.equals(mappaturaOutput, inviaChiamataEsternaRequest.mappaturaOutput) &&
        Objects.equals(restituisceJson, inviaChiamataEsternaRequest.restituisceJson);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFruitore, codiceEndpoint, url, metodo, mappaturaQuery, mappaturaUrl, mappaturaRequestBody, mappaturaHeaders, mappaturaOutput, restituisceJson);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviaChiamataEsternaRequest {\n");
    
    sb.append("    codiceFruitore: ").append(toIndentedString(codiceFruitore)).append("\n");
    sb.append("    codiceEndpoint: ").append(toIndentedString(codiceEndpoint)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    metodo: ").append(toIndentedString(metodo)).append("\n");
    sb.append("    mappaturaQuery: ").append(toIndentedString(mappaturaQuery)).append("\n");
    sb.append("    mappaturaUrl: ").append(toIndentedString(mappaturaUrl)).append("\n");
    sb.append("    mappaturaRequestBody: ").append(toIndentedString(mappaturaRequestBody)).append("\n");
    sb.append("    mappaturaHeaders: ").append(toIndentedString(mappaturaHeaders)).append("\n");
    sb.append("    mappaturaOutput: ").append(toIndentedString(mappaturaOutput)).append("\n");
    sb.append("    restituisceJson: ").append(toIndentedString(restituisceJson)).append("\n");
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

