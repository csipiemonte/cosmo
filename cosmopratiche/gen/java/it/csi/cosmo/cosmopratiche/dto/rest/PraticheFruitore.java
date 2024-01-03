/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaFruitore;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PraticheFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idPraticaExt = null;
  private String apiManager = null;
  private String oggetto = null;
  private String statoPratica = null;
  private String tipoPratica = null;
  private String riassunto = null;
  private String dataCreazione = null;
  private String utenteCreazione = null;
  private List<AttivitaFruitore> attivita = new ArrayList<>();
  private List<TagRidotto> tag = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idPraticaExt 
  @NotNull
  public String getIdPraticaExt() {
    return idPraticaExt;
  }
  public void setIdPraticaExt(String idPraticaExt) {
    this.idPraticaExt = idPraticaExt;
  }

  /**
   **/
  


  // nome originario nello yaml: apiManager 
  @NotNull
  public String getApiManager() {
    return apiManager;
  }
  public void setApiManager(String apiManager) {
    this.apiManager = apiManager;
  }

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  @NotNull
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   **/
  


  // nome originario nello yaml: statoPratica 
  public String getStatoPratica() {
    return statoPratica;
  }
  public void setStatoPratica(String statoPratica) {
    this.statoPratica = statoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  @NotNull
  public String getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(String tipoPratica) {
    this.tipoPratica = tipoPratica;
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
  


  // nome originario nello yaml: dataCreazione 
  public String getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(String dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteCreazione 
  public String getUtenteCreazione() {
    return utenteCreazione;
  }
  public void setUtenteCreazione(String utenteCreazione) {
    this.utenteCreazione = utenteCreazione;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  public List<AttivitaFruitore> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<AttivitaFruitore> attivita) {
    this.attivita = attivita;
  }

  /**
   **/
  


  // nome originario nello yaml: tag 
  public List<TagRidotto> getTag() {
    return tag;
  }
  public void setTag(List<TagRidotto> tag) {
    this.tag = tag;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PraticheFruitore praticheFruitore = (PraticheFruitore) o;
    return Objects.equals(idPraticaExt, praticheFruitore.idPraticaExt) &&
        Objects.equals(apiManager, praticheFruitore.apiManager) &&
        Objects.equals(oggetto, praticheFruitore.oggetto) &&
        Objects.equals(statoPratica, praticheFruitore.statoPratica) &&
        Objects.equals(tipoPratica, praticheFruitore.tipoPratica) &&
        Objects.equals(riassunto, praticheFruitore.riassunto) &&
        Objects.equals(dataCreazione, praticheFruitore.dataCreazione) &&
        Objects.equals(utenteCreazione, praticheFruitore.utenteCreazione) &&
        Objects.equals(attivita, praticheFruitore.attivita) &&
        Objects.equals(tag, praticheFruitore.tag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPraticaExt, apiManager, oggetto, statoPratica, tipoPratica, riassunto, dataCreazione, utenteCreazione, attivita, tag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PraticheFruitore {\n");
    
    sb.append("    idPraticaExt: ").append(toIndentedString(idPraticaExt)).append("\n");
    sb.append("    apiManager: ").append(toIndentedString(apiManager)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    statoPratica: ").append(toIndentedString(statoPratica)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    utenteCreazione: ").append(toIndentedString(utenteCreazione)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
    sb.append("    tag: ").append(toIndentedString(tag)).append("\n");
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

