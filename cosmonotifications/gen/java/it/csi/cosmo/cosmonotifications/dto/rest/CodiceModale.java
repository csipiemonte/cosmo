/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CodiceModale  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String codicePagina = null;
  private String codiceTab = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
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
  


  // nome originario nello yaml: codicePagina 
  public String getCodicePagina() {
    return codicePagina;
  }
  public void setCodicePagina(String codicePagina) {
    this.codicePagina = codicePagina;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTab 
  public String getCodiceTab() {
    return codiceTab;
  }
  public void setCodiceTab(String codiceTab) {
    this.codiceTab = codiceTab;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CodiceModale codiceModale = (CodiceModale) o;
    return Objects.equals(codice, codiceModale.codice) &&
        Objects.equals(descrizione, codiceModale.descrizione) &&
        Objects.equals(codicePagina, codiceModale.codicePagina) &&
        Objects.equals(codiceTab, codiceModale.codiceTab);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, codicePagina, codiceTab);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CodiceModale {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    codicePagina: ").append(toIndentedString(codicePagina)).append("\n");
    sb.append("    codiceTab: ").append(toIndentedString(codiceTab)).append("\n");
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

