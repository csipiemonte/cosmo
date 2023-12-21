/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaCredenzialiAutenticazioneFruitoreRequest;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaSchemaAutenticazioneFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipo = null;
  private CreaCredenzialiAutenticazioneFruitoreRequest credenziali = null;
  private Boolean inIngresso = null;
  private String tokenEndpoint = null;
  private String mappaturaRichiestaToken = null;
  private String mappaturaOutputToken = null;
  private String metodoRichiestaToken = null;
  private String contentTypeRichiestaToken = null;
  private String nomeHeader = null;
  private String formatoHeader = null;

  /**
   **/
  


  // nome originario nello yaml: codiceTipo 
  @NotNull
  public String getCodiceTipo() {
    return codiceTipo;
  }
  public void setCodiceTipo(String codiceTipo) {
    this.codiceTipo = codiceTipo;
  }

  /**
   **/
  


  // nome originario nello yaml: credenziali 
  @NotNull
  public CreaCredenzialiAutenticazioneFruitoreRequest getCredenziali() {
    return credenziali;
  }
  public void setCredenziali(CreaCredenzialiAutenticazioneFruitoreRequest credenziali) {
    this.credenziali = credenziali;
  }

  /**
   **/
  


  // nome originario nello yaml: inIngresso 
  public Boolean isInIngresso() {
    return inIngresso;
  }
  public void setInIngresso(Boolean inIngresso) {
    this.inIngresso = inIngresso;
  }

  /**
   **/
  


  // nome originario nello yaml: tokenEndpoint 
  public String getTokenEndpoint() {
    return tokenEndpoint;
  }
  public void setTokenEndpoint(String tokenEndpoint) {
    this.tokenEndpoint = tokenEndpoint;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaRichiestaToken 
  public String getMappaturaRichiestaToken() {
    return mappaturaRichiestaToken;
  }
  public void setMappaturaRichiestaToken(String mappaturaRichiestaToken) {
    this.mappaturaRichiestaToken = mappaturaRichiestaToken;
  }

  /**
   **/
  


  // nome originario nello yaml: mappaturaOutputToken 
  public String getMappaturaOutputToken() {
    return mappaturaOutputToken;
  }
  public void setMappaturaOutputToken(String mappaturaOutputToken) {
    this.mappaturaOutputToken = mappaturaOutputToken;
  }

  /**
   **/
  


  // nome originario nello yaml: metodoRichiestaToken 
  public String getMetodoRichiestaToken() {
    return metodoRichiestaToken;
  }
  public void setMetodoRichiestaToken(String metodoRichiestaToken) {
    this.metodoRichiestaToken = metodoRichiestaToken;
  }

  /**
   **/
  


  // nome originario nello yaml: contentTypeRichiestaToken 
  public String getContentTypeRichiestaToken() {
    return contentTypeRichiestaToken;
  }
  public void setContentTypeRichiestaToken(String contentTypeRichiestaToken) {
    this.contentTypeRichiestaToken = contentTypeRichiestaToken;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeHeader 
  public String getNomeHeader() {
    return nomeHeader;
  }
  public void setNomeHeader(String nomeHeader) {
    this.nomeHeader = nomeHeader;
  }

  /**
   **/
  


  // nome originario nello yaml: formatoHeader 
  public String getFormatoHeader() {
    return formatoHeader;
  }
  public void setFormatoHeader(String formatoHeader) {
    this.formatoHeader = formatoHeader;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaSchemaAutenticazioneFruitoreRequest creaSchemaAutenticazioneFruitoreRequest = (CreaSchemaAutenticazioneFruitoreRequest) o;
    return Objects.equals(codiceTipo, creaSchemaAutenticazioneFruitoreRequest.codiceTipo) &&
        Objects.equals(credenziali, creaSchemaAutenticazioneFruitoreRequest.credenziali) &&
        Objects.equals(inIngresso, creaSchemaAutenticazioneFruitoreRequest.inIngresso) &&
        Objects.equals(tokenEndpoint, creaSchemaAutenticazioneFruitoreRequest.tokenEndpoint) &&
        Objects.equals(mappaturaRichiestaToken, creaSchemaAutenticazioneFruitoreRequest.mappaturaRichiestaToken) &&
        Objects.equals(mappaturaOutputToken, creaSchemaAutenticazioneFruitoreRequest.mappaturaOutputToken) &&
        Objects.equals(metodoRichiestaToken, creaSchemaAutenticazioneFruitoreRequest.metodoRichiestaToken) &&
        Objects.equals(contentTypeRichiestaToken, creaSchemaAutenticazioneFruitoreRequest.contentTypeRichiestaToken) &&
        Objects.equals(nomeHeader, creaSchemaAutenticazioneFruitoreRequest.nomeHeader) &&
        Objects.equals(formatoHeader, creaSchemaAutenticazioneFruitoreRequest.formatoHeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipo, credenziali, inIngresso, tokenEndpoint, mappaturaRichiestaToken, mappaturaOutputToken, metodoRichiestaToken, contentTypeRichiestaToken, nomeHeader, formatoHeader);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaSchemaAutenticazioneFruitoreRequest {\n");
    
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    credenziali: ").append(toIndentedString(credenziali)).append("\n");
    sb.append("    inIngresso: ").append(toIndentedString(inIngresso)).append("\n");
    sb.append("    tokenEndpoint: ").append(toIndentedString(tokenEndpoint)).append("\n");
    sb.append("    mappaturaRichiestaToken: ").append(toIndentedString(mappaturaRichiestaToken)).append("\n");
    sb.append("    mappaturaOutputToken: ").append(toIndentedString(mappaturaOutputToken)).append("\n");
    sb.append("    metodoRichiestaToken: ").append(toIndentedString(metodoRichiestaToken)).append("\n");
    sb.append("    contentTypeRichiestaToken: ").append(toIndentedString(contentTypeRichiestaToken)).append("\n");
    sb.append("    nomeHeader: ").append(toIndentedString(nomeHeader)).append("\n");
    sb.append("    formatoHeader: ").append(toIndentedString(formatoHeader)).append("\n");
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

