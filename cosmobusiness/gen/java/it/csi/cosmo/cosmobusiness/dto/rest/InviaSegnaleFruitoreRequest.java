/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreVariabileRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InviaSegnaleFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceSegnale = null;
  private Boolean richiediCallback = null;
  private List<InviaSegnaleFruitoreVariabileRequest> variabili = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: codiceSegnale 
  @NotNull
  public String getCodiceSegnale() {
    return codiceSegnale;
  }
  public void setCodiceSegnale(String codiceSegnale) {
    this.codiceSegnale = codiceSegnale;
  }

  /**
   **/
  


  // nome originario nello yaml: richiediCallback 
  public Boolean isRichiediCallback() {
    return richiediCallback;
  }
  public void setRichiediCallback(Boolean richiediCallback) {
    this.richiediCallback = richiediCallback;
  }

  /**
   **/
  


  // nome originario nello yaml: variabili 
  public List<InviaSegnaleFruitoreVariabileRequest> getVariabili() {
    return variabili;
  }
  public void setVariabili(List<InviaSegnaleFruitoreVariabileRequest> variabili) {
    this.variabili = variabili;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviaSegnaleFruitoreRequest inviaSegnaleFruitoreRequest = (InviaSegnaleFruitoreRequest) o;
    return Objects.equals(codiceSegnale, inviaSegnaleFruitoreRequest.codiceSegnale) &&
        Objects.equals(richiediCallback, inviaSegnaleFruitoreRequest.richiediCallback) &&
        Objects.equals(variabili, inviaSegnaleFruitoreRequest.variabili);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceSegnale, richiediCallback, variabili);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviaSegnaleFruitoreRequest {\n");
    
    sb.append("    codiceSegnale: ").append(toIndentedString(codiceSegnale)).append("\n");
    sb.append("    richiediCallback: ").append(toIndentedString(richiediCallback)).append("\n");
    sb.append("    variabili: ").append(toIndentedString(variabili)).append("\n");
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

