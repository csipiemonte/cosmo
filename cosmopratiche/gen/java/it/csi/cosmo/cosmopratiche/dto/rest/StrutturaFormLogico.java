/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaFormLogico;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class StrutturaFormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codice = null;
  private String descrizione = null;
  private Boolean wizard = null;
  private List<FunzionalitaFormLogico> funzionalita = new ArrayList<>();

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
  


  // nome originario nello yaml: wizard 
  public Boolean isWizard() {
    return wizard;
  }
  public void setWizard(Boolean wizard) {
    this.wizard = wizard;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalita 
  public List<FunzionalitaFormLogico> getFunzionalita() {
    return funzionalita;
  }
  public void setFunzionalita(List<FunzionalitaFormLogico> funzionalita) {
    this.funzionalita = funzionalita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StrutturaFormLogico strutturaFormLogico = (StrutturaFormLogico) o;
    return Objects.equals(id, strutturaFormLogico.id) &&
        Objects.equals(codice, strutturaFormLogico.codice) &&
        Objects.equals(descrizione, strutturaFormLogico.descrizione) &&
        Objects.equals(wizard, strutturaFormLogico.wizard) &&
        Objects.equals(funzionalita, strutturaFormLogico.funzionalita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codice, descrizione, wizard, funzionalita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StrutturaFormLogico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    wizard: ").append(toIndentedString(wizard)).append("\n");
    sb.append("    funzionalita: ").append(toIndentedString(funzionalita)).append("\n");
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

