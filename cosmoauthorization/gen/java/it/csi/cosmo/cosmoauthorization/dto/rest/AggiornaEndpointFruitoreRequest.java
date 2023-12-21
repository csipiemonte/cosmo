/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaEndpointFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idSchemaAutenticazione = null;
  private String codiceOperazione = null;
  private String codiceTipo = null;
  private String endpoint = null;
  private String metodoHttp = null;
  private String codiceDescrittivo = null;

  /**
   **/
  


  // nome originario nello yaml: idSchemaAutenticazione 
  public Long getIdSchemaAutenticazione() {
    return idSchemaAutenticazione;
  }
  public void setIdSchemaAutenticazione(Long idSchemaAutenticazione) {
    this.idSchemaAutenticazione = idSchemaAutenticazione;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceOperazione 
  @NotNull
  public String getCodiceOperazione() {
    return codiceOperazione;
  }
  public void setCodiceOperazione(String codiceOperazione) {
    this.codiceOperazione = codiceOperazione;
  }

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
  


  // nome originario nello yaml: endpoint 
  @NotNull
  public String getEndpoint() {
    return endpoint;
  }
  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  /**
   **/
  


  // nome originario nello yaml: metodoHttp 
  public String getMetodoHttp() {
    return metodoHttp;
  }
  public void setMetodoHttp(String metodoHttp) {
    this.metodoHttp = metodoHttp;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceDescrittivo 
  public String getCodiceDescrittivo() {
    return codiceDescrittivo;
  }
  public void setCodiceDescrittivo(String codiceDescrittivo) {
    this.codiceDescrittivo = codiceDescrittivo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaEndpointFruitoreRequest aggiornaEndpointFruitoreRequest = (AggiornaEndpointFruitoreRequest) o;
    return Objects.equals(idSchemaAutenticazione, aggiornaEndpointFruitoreRequest.idSchemaAutenticazione) &&
        Objects.equals(codiceOperazione, aggiornaEndpointFruitoreRequest.codiceOperazione) &&
        Objects.equals(codiceTipo, aggiornaEndpointFruitoreRequest.codiceTipo) &&
        Objects.equals(endpoint, aggiornaEndpointFruitoreRequest.endpoint) &&
        Objects.equals(metodoHttp, aggiornaEndpointFruitoreRequest.metodoHttp) &&
        Objects.equals(codiceDescrittivo, aggiornaEndpointFruitoreRequest.codiceDescrittivo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idSchemaAutenticazione, codiceOperazione, codiceTipo, endpoint, metodoHttp, codiceDescrittivo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaEndpointFruitoreRequest {\n");
    
    sb.append("    idSchemaAutenticazione: ").append(toIndentedString(idSchemaAutenticazione)).append("\n");
    sb.append("    codiceOperazione: ").append(toIndentedString(codiceOperazione)).append("\n");
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    metodoHttp: ").append(toIndentedString(metodoHttp)).append("\n");
    sb.append("    codiceDescrittivo: ").append(toIndentedString(codiceDescrittivo)).append("\n");
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

