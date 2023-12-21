/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaAttivitaFruitoreRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaPraticaEsternaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private String tipoPratica = null;
  private String stato = null;
  private String riassunto = null;
  private String linkPratica = null;
  private List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();
  private OffsetDateTime dataFinePratica = null;

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  public String getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(String tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: riassunto 
  public String getRiassunto() {
    return riassunto;
  }
  public void setRiassunto(String riassunto) {
    this.riassunto = riassunto;
  }

  /**
   **/
  


  // nome originario nello yaml: linkPratica 
  public String getLinkPratica() {
    return linkPratica;
  }
  public void setLinkPratica(String linkPratica) {
    this.linkPratica = linkPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public List<AggiornaPraticaEsternaAttivitaFruitoreRequest> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<AggiornaPraticaEsternaAttivitaFruitoreRequest> attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFinePratica 
  public OffsetDateTime getDataFinePratica() {
    return dataFinePratica;
  }
  public void setDataFinePratica(OffsetDateTime dataFinePratica) {
    this.dataFinePratica = dataFinePratica;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaPraticaEsternaFruitoreRequest aggiornaPraticaEsternaFruitoreRequest = (AggiornaPraticaEsternaFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, aggiornaPraticaEsternaFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(tipoPratica, aggiornaPraticaEsternaFruitoreRequest.tipoPratica) &&
        Objects.equals(stato, aggiornaPraticaEsternaFruitoreRequest.stato) &&
        Objects.equals(riassunto, aggiornaPraticaEsternaFruitoreRequest.riassunto) &&
        Objects.equals(linkPratica, aggiornaPraticaEsternaFruitoreRequest.linkPratica) &&
        Objects.equals(attivita, aggiornaPraticaEsternaFruitoreRequest.attivita) &&
        Objects.equals(dataFinePratica, aggiornaPraticaEsternaFruitoreRequest.dataFinePratica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, tipoPratica, stato, riassunto, linkPratica, attivita, dataFinePratica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaPraticaEsternaFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    linkPratica: ").append(toIndentedString(linkPratica)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    dataFinePratica: ").append(toIndentedString(dataFinePratica)).append("\n");
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

