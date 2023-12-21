/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaEventoFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private OffsetDateTime nuovaDataScadenza = null;
  private String nuovoTitolo = null;
  private String nuovaDescrizione = null;
  private Boolean eseguito = null;

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: nuovaDataScadenza 
  public OffsetDateTime getNuovaDataScadenza() {
    return nuovaDataScadenza;
  }
  public void setNuovaDataScadenza(OffsetDateTime nuovaDataScadenza) {
    this.nuovaDataScadenza = nuovaDataScadenza;
  }

  /**
   **/
  


  // nome originario nello yaml: nuovoTitolo 
  @Size(min=1,max=255)
  public String getNuovoTitolo() {
    return nuovoTitolo;
  }
  public void setNuovoTitolo(String nuovoTitolo) {
    this.nuovoTitolo = nuovoTitolo;
  }

  /**
   **/
  


  // nome originario nello yaml: nuovaDescrizione 
  @Size(min=1)
  public String getNuovaDescrizione() {
    return nuovaDescrizione;
  }
  public void setNuovaDescrizione(String nuovaDescrizione) {
    this.nuovaDescrizione = nuovaDescrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: eseguito 
  public Boolean isEseguito() {
    return eseguito;
  }
  public void setEseguito(Boolean eseguito) {
    this.eseguito = eseguito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaEventoFruitoreRequest aggiornaEventoFruitoreRequest = (AggiornaEventoFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, aggiornaEventoFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(nuovaDataScadenza, aggiornaEventoFruitoreRequest.nuovaDataScadenza) &&
        Objects.equals(nuovoTitolo, aggiornaEventoFruitoreRequest.nuovoTitolo) &&
        Objects.equals(nuovaDescrizione, aggiornaEventoFruitoreRequest.nuovaDescrizione) &&
        Objects.equals(eseguito, aggiornaEventoFruitoreRequest.eseguito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, nuovaDataScadenza, nuovoTitolo, nuovaDescrizione, eseguito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaEventoFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    nuovaDataScadenza: ").append(toIndentedString(nuovaDataScadenza)).append("\n");
    sb.append("    nuovoTitolo: ").append(toIndentedString(nuovoTitolo)).append("\n");
    sb.append("    nuovaDescrizione: ").append(toIndentedString(nuovaDescrizione)).append("\n");
    sb.append("    eseguito: ").append(toIndentedString(eseguito)).append("\n");
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

