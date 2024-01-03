/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CaricamentoPratica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String nomeFile = null;
  private String pathFile = null;
  private String identificativoPratica = null;
  private StatoCaricamentoPratica statoCaricamentoPratica = null;
  private Long idPratica = null;
  private String descrizioneEvento = null;
  private String errore = null;
  private RiferimentoEnte ente = null;
  private RiferimentoUtente utente = null;
  private Boolean hasElaborazioni = null;
  private List<CaricamentoPratica> elaborazioni = new ArrayList<>();
  private OffsetDateTime dtInserimento = null;
  private OffsetDateTime dtUltimaModifica = null;
  private OffsetDateTime dtCancellazione = null;
  private Boolean visualizzaDettaglio = null;

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
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: pathFile 
  public String getPathFile() {
    return pathFile;
  }
  public void setPathFile(String pathFile) {
    this.pathFile = pathFile;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoPratica 
  public String getIdentificativoPratica() {
    return identificativoPratica;
  }
  public void setIdentificativoPratica(String identificativoPratica) {
    this.identificativoPratica = identificativoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: statoCaricamentoPratica 
  public StatoCaricamentoPratica getStatoCaricamentoPratica() {
    return statoCaricamentoPratica;
  }
  public void setStatoCaricamentoPratica(StatoCaricamentoPratica statoCaricamentoPratica) {
    this.statoCaricamentoPratica = statoCaricamentoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneEvento 
  public String getDescrizioneEvento() {
    return descrizioneEvento;
  }
  public void setDescrizioneEvento(String descrizioneEvento) {
    this.descrizioneEvento = descrizioneEvento;
  }

  /**
   **/
  


  // nome originario nello yaml: errore 
  public String getErrore() {
    return errore;
  }
  public void setErrore(String errore) {
    this.errore = errore;
  }

  /**
   **/
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: utente 
  public RiferimentoUtente getUtente() {
    return utente;
  }
  public void setUtente(RiferimentoUtente utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: hasElaborazioni 
  public Boolean isHasElaborazioni() {
    return hasElaborazioni;
  }
  public void setHasElaborazioni(Boolean hasElaborazioni) {
    this.hasElaborazioni = hasElaborazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: elaborazioni 
  public List<CaricamentoPratica> getElaborazioni() {
    return elaborazioni;
  }
  public void setElaborazioni(List<CaricamentoPratica> elaborazioni) {
    this.elaborazioni = elaborazioni;
  }

  /**
   **/
  


  // nome originario nello yaml: dtInserimento 
  public OffsetDateTime getDtInserimento() {
    return dtInserimento;
  }
  public void setDtInserimento(OffsetDateTime dtInserimento) {
    this.dtInserimento = dtInserimento;
  }

  /**
   **/
  


  // nome originario nello yaml: dtUltimaModifica 
  public OffsetDateTime getDtUltimaModifica() {
    return dtUltimaModifica;
  }
  public void setDtUltimaModifica(OffsetDateTime dtUltimaModifica) {
    this.dtUltimaModifica = dtUltimaModifica;
  }

  /**
   **/
  


  // nome originario nello yaml: dtCancellazione 
  public OffsetDateTime getDtCancellazione() {
    return dtCancellazione;
  }
  public void setDtCancellazione(OffsetDateTime dtCancellazione) {
    this.dtCancellazione = dtCancellazione;
  }

  /**
   **/
  


  // nome originario nello yaml: visualizzaDettaglio 
  public Boolean isVisualizzaDettaglio() {
    return visualizzaDettaglio;
  }
  public void setVisualizzaDettaglio(Boolean visualizzaDettaglio) {
    this.visualizzaDettaglio = visualizzaDettaglio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaricamentoPratica caricamentoPratica = (CaricamentoPratica) o;
    return Objects.equals(id, caricamentoPratica.id) &&
        Objects.equals(nomeFile, caricamentoPratica.nomeFile) &&
        Objects.equals(pathFile, caricamentoPratica.pathFile) &&
        Objects.equals(identificativoPratica, caricamentoPratica.identificativoPratica) &&
        Objects.equals(statoCaricamentoPratica, caricamentoPratica.statoCaricamentoPratica) &&
        Objects.equals(idPratica, caricamentoPratica.idPratica) &&
        Objects.equals(descrizioneEvento, caricamentoPratica.descrizioneEvento) &&
        Objects.equals(errore, caricamentoPratica.errore) &&
        Objects.equals(ente, caricamentoPratica.ente) &&
        Objects.equals(utente, caricamentoPratica.utente) &&
        Objects.equals(hasElaborazioni, caricamentoPratica.hasElaborazioni) &&
        Objects.equals(elaborazioni, caricamentoPratica.elaborazioni) &&
        Objects.equals(dtInserimento, caricamentoPratica.dtInserimento) &&
        Objects.equals(dtUltimaModifica, caricamentoPratica.dtUltimaModifica) &&
        Objects.equals(dtCancellazione, caricamentoPratica.dtCancellazione) &&
        Objects.equals(visualizzaDettaglio, caricamentoPratica.visualizzaDettaglio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nomeFile, pathFile, identificativoPratica, statoCaricamentoPratica, idPratica, descrizioneEvento, errore, ente, utente, hasElaborazioni, elaborazioni, dtInserimento, dtUltimaModifica, dtCancellazione, visualizzaDettaglio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaricamentoPratica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    pathFile: ").append(toIndentedString(pathFile)).append("\n");
    sb.append("    identificativoPratica: ").append(toIndentedString(identificativoPratica)).append("\n");
    sb.append("    statoCaricamentoPratica: ").append(toIndentedString(statoCaricamentoPratica)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    descrizioneEvento: ").append(toIndentedString(descrizioneEvento)).append("\n");
    sb.append("    errore: ").append(toIndentedString(errore)).append("\n");
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    hasElaborazioni: ").append(toIndentedString(hasElaborazioni)).append("\n");
    sb.append("    elaborazioni: ").append(toIndentedString(elaborazioni)).append("\n");
    sb.append("    dtInserimento: ").append(toIndentedString(dtInserimento)).append("\n");
    sb.append("    dtUltimaModifica: ").append(toIndentedString(dtUltimaModifica)).append("\n");
    sb.append("    dtCancellazione: ").append(toIndentedString(dtCancellazione)).append("\n");
    sb.append("    visualizzaDettaglio: ").append(toIndentedString(visualizzaDettaglio)).append("\n");
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

