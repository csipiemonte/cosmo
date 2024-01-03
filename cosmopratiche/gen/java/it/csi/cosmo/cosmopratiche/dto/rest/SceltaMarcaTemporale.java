/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SceltaMarcaTemporale  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private Boolean nonValidoInApposizione = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
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

  /**
   **/
  


  // nome originario nello yaml: nonValidoInApposizione 
  public Boolean isNonValidoInApposizione() {
    return nonValidoInApposizione;
  }
  public void setNonValidoInApposizione(Boolean nonValidoInApposizione) {
    this.nonValidoInApposizione = nonValidoInApposizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SceltaMarcaTemporale sceltaMarcaTemporale = (SceltaMarcaTemporale) o;
    return Objects.equals(codice, sceltaMarcaTemporale.codice) &&
        Objects.equals(descrizione, sceltaMarcaTemporale.descrizione) &&
        Objects.equals(nonValidoInApposizione, sceltaMarcaTemporale.nonValidoInApposizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, nonValidoInApposizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SceltaMarcaTemporale {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    nonValidoInApposizione: ").append(toIndentedString(nonValidoInApposizione)).append("\n");
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

