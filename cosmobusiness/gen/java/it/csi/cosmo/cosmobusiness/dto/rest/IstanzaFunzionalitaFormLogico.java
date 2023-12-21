/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaParametroFormLogico;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class IstanzaFunzionalitaFormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codice = null;
  private String descrizione = null;
  private Integer ordine = null;
  private Boolean eseguibileMassivamente = null;
  private List<IstanzaParametroFormLogico> parametri = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

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
  


  // nome originario nello yaml: ordine 
  public Integer getOrdine() {
    return ordine;
  }
  public void setOrdine(Integer ordine) {
    this.ordine = ordine;
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
  public List<IstanzaParametroFormLogico> getParametri() {
    return parametri;
  }
  public void setParametri(List<IstanzaParametroFormLogico> parametri) {
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
    IstanzaFunzionalitaFormLogico istanzaFunzionalitaFormLogico = (IstanzaFunzionalitaFormLogico) o;
    return Objects.equals(id, istanzaFunzionalitaFormLogico.id) &&
        Objects.equals(codice, istanzaFunzionalitaFormLogico.codice) &&
        Objects.equals(descrizione, istanzaFunzionalitaFormLogico.descrizione) &&
        Objects.equals(ordine, istanzaFunzionalitaFormLogico.ordine) &&
        Objects.equals(eseguibileMassivamente, istanzaFunzionalitaFormLogico.eseguibileMassivamente) &&
        Objects.equals(parametri, istanzaFunzionalitaFormLogico.parametri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codice, descrizione, ordine, eseguibileMassivamente, parametri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IstanzaFunzionalitaFormLogico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    ordine: ").append(toIndentedString(ordine)).append("\n");
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

