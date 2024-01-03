/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CaricamentoPraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeFile = null;
  private String pathFile = null;
  private String identificativoPratica = null;
  private String statoCaricamentoPratica = null;
  private Long idPratica = null;
  private String descrizioneEvento = null;
  private String errore = null;
  private Long idEnte = null;

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
  public String getStatoCaricamentoPratica() {
    return statoCaricamentoPratica;
  }
  public void setStatoCaricamentoPratica(String statoCaricamentoPratica) {
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
  


  // nome originario nello yaml: idEnte 
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CaricamentoPraticaRequest caricamentoPraticaRequest = (CaricamentoPraticaRequest) o;
    return Objects.equals(nomeFile, caricamentoPraticaRequest.nomeFile) &&
        Objects.equals(pathFile, caricamentoPraticaRequest.pathFile) &&
        Objects.equals(identificativoPratica, caricamentoPraticaRequest.identificativoPratica) &&
        Objects.equals(statoCaricamentoPratica, caricamentoPraticaRequest.statoCaricamentoPratica) &&
        Objects.equals(idPratica, caricamentoPraticaRequest.idPratica) &&
        Objects.equals(descrizioneEvento, caricamentoPraticaRequest.descrizioneEvento) &&
        Objects.equals(errore, caricamentoPraticaRequest.errore) &&
        Objects.equals(idEnte, caricamentoPraticaRequest.idEnte);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, pathFile, identificativoPratica, statoCaricamentoPratica, idPratica, descrizioneEvento, errore, idEnte);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CaricamentoPraticaRequest {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    pathFile: ").append(toIndentedString(pathFile)).append("\n");
    sb.append("    identificativoPratica: ").append(toIndentedString(identificativoPratica)).append("\n");
    sb.append("    statoCaricamentoPratica: ").append(toIndentedString(statoCaricamentoPratica)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    descrizioneEvento: ").append(toIndentedString(descrizioneEvento)).append("\n");
    sb.append("    errore: ").append(toIndentedString(errore)).append("\n");
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
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

