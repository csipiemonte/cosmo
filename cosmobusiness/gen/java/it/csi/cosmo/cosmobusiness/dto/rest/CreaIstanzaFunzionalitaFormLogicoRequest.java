/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaParametroFormLogico;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaIstanzaFunzionalitaFormLogicoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private Boolean eseguibileMassivamente = null;
  private List<AggiornaIstanzaParametroFormLogico> parametri = new ArrayList<>();

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
  


  // nome originario nello yaml: eseguibileMassivamente 
  public Boolean isEseguibileMassivamente() {
    return eseguibileMassivamente;
  }
  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
  }

  /**
   **/
  


  // nome originario nello yaml: parametri 
  public List<AggiornaIstanzaParametroFormLogico> getParametri() {
    return parametri;
  }
  public void setParametri(List<AggiornaIstanzaParametroFormLogico> parametri) {
    this.parametri = parametri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaIstanzaFunzionalitaFormLogicoRequest creaIstanzaFunzionalitaFormLogicoRequest = (CreaIstanzaFunzionalitaFormLogicoRequest) o;
    return Objects.equals(codice, creaIstanzaFunzionalitaFormLogicoRequest.codice) &&
        Objects.equals(descrizione, creaIstanzaFunzionalitaFormLogicoRequest.descrizione) &&
        Objects.equals(eseguibileMassivamente, creaIstanzaFunzionalitaFormLogicoRequest.eseguibileMassivamente) &&
        Objects.equals(parametri, creaIstanzaFunzionalitaFormLogicoRequest.parametri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, eseguibileMassivamente, parametri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaIstanzaFunzionalitaFormLogicoRequest {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    eseguibileMassivamente: ").append(toIndentedString(eseguibileMassivamente)).append("\n");
    sb.append("    parametri: ").append(toIndentedString(parametri)).append("\n");
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

