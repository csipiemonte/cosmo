/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaFormLogicoIstanzaFunzionalitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoEnte;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaFormLogicoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private Boolean eseguibileMassivamente = null;
  private Boolean wizard = null;
  private List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanzeFunzionalita = new ArrayList<>();
  private RiferimentoEnte riferimentoEnte = null;

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
  public List<AggiornaFormLogicoIstanzaFunzionalitaRequest> getIstanzeFunzionalita() {
    return istanzeFunzionalita;
  }
  public void setIstanzeFunzionalita(List<AggiornaFormLogicoIstanzaFunzionalitaRequest> istanzeFunzionalita) {
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
    CreaFormLogicoRequest creaFormLogicoRequest = (CreaFormLogicoRequest) o;
    return Objects.equals(codice, creaFormLogicoRequest.codice) &&
        Objects.equals(descrizione, creaFormLogicoRequest.descrizione) &&
        Objects.equals(eseguibileMassivamente, creaFormLogicoRequest.eseguibileMassivamente) &&
        Objects.equals(wizard, creaFormLogicoRequest.wizard) &&
        Objects.equals(istanzeFunzionalita, creaFormLogicoRequest.istanzeFunzionalita) &&
        Objects.equals(riferimentoEnte, creaFormLogicoRequest.riferimentoEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, eseguibileMassivamente, wizard, istanzeFunzionalita, riferimentoEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaFormLogicoRequest {\n");
    
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

