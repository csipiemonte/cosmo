/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.ParametroFormLogico;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FunzionalitaFormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codice = null;
  private String descrizione = null;
  private List<ParametroFormLogico> parametri = new ArrayList<>();
  private Boolean multiIstanza = false;

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
  


  // nome originario nello yaml: parametri 
  public List<ParametroFormLogico> getParametri() {
    return parametri;
  }
  public void setParametri(List<ParametroFormLogico> parametri) {
    this.parametri = parametri;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FunzionalitaFormLogico funzionalitaFormLogico = (FunzionalitaFormLogico) o;
    return Objects.equals(id, funzionalitaFormLogico.id) &&
        Objects.equals(codice, funzionalitaFormLogico.codice) &&
        Objects.equals(descrizione, funzionalitaFormLogico.descrizione) &&
        Objects.equals(parametri, funzionalitaFormLogico.parametri) &&
        Objects.equals(multiIstanza, funzionalitaFormLogico.multiIstanza);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codice, descrizione, parametri, multiIstanza);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FunzionalitaFormLogico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    parametri: ").append(toIndentedString(parametri)).append("\n");
    sb.append("    multiIstanza: ").append(toIndentedString(multiIstanza)).append("\n");
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

