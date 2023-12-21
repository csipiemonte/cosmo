/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoEnte;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FormLogico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String codice = null;
  private String descrizione = null;
  private Boolean eseguibileMassivamente = null;
  private Boolean wizard = null;
  private List<IstanzaFunzionalitaFormLogico> istanzeFunzionalita = new ArrayList<>();
  private RiferimentoEnte riferimentoEnte = null;

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
  


  // nome originario nello yaml: eseguibileMassivamente 
  public Boolean isEseguibileMassivamente() {
    return eseguibileMassivamente;
  }
  public void setEseguibileMassivamente(Boolean eseguibileMassivamente) {
    this.eseguibileMassivamente = eseguibileMassivamente;
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
  


  // nome originario nello yaml: istanzeFunzionalita 
  public List<IstanzaFunzionalitaFormLogico> getIstanzeFunzionalita() {
    return istanzeFunzionalita;
  }
  public void setIstanzeFunzionalita(List<IstanzaFunzionalitaFormLogico> istanzeFunzionalita) {
    this.istanzeFunzionalita = istanzeFunzionalita;
  }

  /**
   **/
  


  // nome originario nello yaml: riferimentoEnte 
  public RiferimentoEnte getRiferimentoEnte() {
    return riferimentoEnte;
  }
  public void setRiferimentoEnte(RiferimentoEnte riferimentoEnte) {
    this.riferimentoEnte = riferimentoEnte;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormLogico formLogico = (FormLogico) o;
    return Objects.equals(id, formLogico.id) &&
        Objects.equals(codice, formLogico.codice) &&
        Objects.equals(descrizione, formLogico.descrizione) &&
        Objects.equals(eseguibileMassivamente, formLogico.eseguibileMassivamente) &&
        Objects.equals(wizard, formLogico.wizard) &&
        Objects.equals(istanzeFunzionalita, formLogico.istanzeFunzionalita) &&
        Objects.equals(riferimentoEnte, formLogico.riferimentoEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codice, descrizione, eseguibileMassivamente, wizard, istanzeFunzionalita, riferimentoEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormLogico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    eseguibileMassivamente: ").append(toIndentedString(eseguibileMassivamente)).append("\n");
    sb.append("    wizard: ").append(toIndentedString(wizard)).append("\n");
    sb.append("    istanzeFunzionalita: ").append(toIndentedString(istanzeFunzionalita)).append("\n");
    sb.append("    riferimentoEnte: ").append(toIndentedString(riferimentoEnte)).append("\n");
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

