/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaTipoPraticaStatoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String classe = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  @Size(min=1,max=100)
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: classe 
  public String getClasse() {
    return classe;
  }
  public void setClasse(String classe) {
    this.classe = classe;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaTipoPraticaStatoRequest aggiornaTipoPraticaStatoRequest = (AggiornaTipoPraticaStatoRequest) o;
    return Objects.equals(codice, aggiornaTipoPraticaStatoRequest.codice) &&
        Objects.equals(descrizione, aggiornaTipoPraticaStatoRequest.descrizione) &&
        Objects.equals(classe, aggiornaTipoPraticaStatoRequest.classe);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, classe);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaTipoPraticaStatoRequest {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    classe: ").append(toIndentedString(classe)).append("\n");
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

