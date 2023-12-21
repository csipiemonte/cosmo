/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TipologiaFunzionalitaFormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private Boolean multiIstanza = false;
  private Boolean eseguibileMassivamente = null;

  /**
   **/
  


  // nome originario nello yaml: codice 
  @Size(max=30)
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @Size(max=100)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: multiIstanza 
  public Boolean isMultiIstanza() {
    return multiIstanza;
  }
  public void setMultiIstanza(Boolean multiIstanza) {
    this.multiIstanza = multiIstanza;
  }

  /**
   **/
  


  // nome originario nello yaml: eseguibileMassivamente 
  public Boolean isEseguibileMassivamente() {
    return eseguibileMassivamente;
  }
  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipologiaFunzionalitaFormLogico tipologiaFunzionalitaFormLogico = (TipologiaFunzionalitaFormLogico) o;
    return Objects.equals(codice, tipologiaFunzionalitaFormLogico.codice) &&
        Objects.equals(descrizione, tipologiaFunzionalitaFormLogico.descrizione) &&
        Objects.equals(multiIstanza, tipologiaFunzionalitaFormLogico.multiIstanza) &&
        Objects.equals(eseguibileMassivamente, tipologiaFunzionalitaFormLogico.eseguibileMassivamente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, multiIstanza, eseguibileMassivamente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipologiaFunzionalitaFormLogico {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    multiIstanza: ").append(toIndentedString(multiIstanza)).append("\n");
    sb.append("    eseguibileMassivamente: ").append(toIndentedString(eseguibileMassivamente)).append("\n");
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

