/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RichiediGenerazioneReportInputUtenteRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String variabile = null;
  private String codiceParametro = null;
  private String valore = null;

  /**
   **/
  


  // nome originario nello yaml: variabile 
  public String getVariabile() {
    return variabile;
  }
  public void setVariabile(String variabile) {
    this.variabile = variabile;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceParametro 
  @NotNull
  public String getCodiceParametro() {
    return codiceParametro;
  }
  public void setCodiceParametro(String codiceParametro) {
    this.codiceParametro = codiceParametro;
  }

  /**
   **/
  


  // nome originario nello yaml: valore 
  @NotNull
  public String getValore() {
    return valore;
  }
  public void setValore(String valore) {
    this.valore = valore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichiediGenerazioneReportInputUtenteRequest richiediGenerazioneReportInputUtenteRequest = (RichiediGenerazioneReportInputUtenteRequest) o;
    return Objects.equals(variabile, richiediGenerazioneReportInputUtenteRequest.variabile) &&
        Objects.equals(codiceParametro, richiediGenerazioneReportInputUtenteRequest.codiceParametro) &&
        Objects.equals(valore, richiediGenerazioneReportInputUtenteRequest.valore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(variabile, codiceParametro, valore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichiediGenerazioneReportInputUtenteRequest {\n");
    
    sb.append("    variabile: ").append(toIndentedString(variabile)).append("\n");
    sb.append("    codiceParametro: ").append(toIndentedString(codiceParametro)).append("\n");
    sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
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

