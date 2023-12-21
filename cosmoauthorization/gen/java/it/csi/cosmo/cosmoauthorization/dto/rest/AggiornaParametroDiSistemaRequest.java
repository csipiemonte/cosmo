/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaParametroDiSistemaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String chiave = null;
  private String valore = null;
  private String descrizione = null;

  /**
   **/
  


  // nome originario nello yaml: chiave 
  @NotNull
  public String getChiave() {
    return chiave;
  }
  public void setChiave(String chiave) {
    this.chiave = chiave;
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

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaParametroDiSistemaRequest aggiornaParametroDiSistemaRequest = (AggiornaParametroDiSistemaRequest) o;
    return Objects.equals(chiave, aggiornaParametroDiSistemaRequest.chiave) &&
        Objects.equals(valore, aggiornaParametroDiSistemaRequest.valore) &&
        Objects.equals(descrizione, aggiornaParametroDiSistemaRequest.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chiave, valore, descrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaParametroDiSistemaRequest {\n");
    
    sb.append("    chiave: ").append(toIndentedString(chiave)).append("\n");
    sb.append("    valore: ").append(toIndentedString(valore)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
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

