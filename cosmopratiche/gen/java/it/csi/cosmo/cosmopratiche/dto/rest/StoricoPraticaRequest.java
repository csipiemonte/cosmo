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

public class StoricoPraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private Long idAttivita = null;
  private String codiceTipoEvento = null;
  private String descrizioneEvento = null;
  private Long idUtente = null;
  private Long idFruitore = null;
  private Long idUtenteCoinvolto = null;
  private Long idGruppoCoinvolto = null;

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: idAttivita 
  public Long getIdAttivita() {
    return idAttivita;
  }
  public void setIdAttivita(Long idAttivita) {
    this.idAttivita = idAttivita;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoEvento 
  @NotNull
  public String getCodiceTipoEvento() {
    return codiceTipoEvento;
  }
  public void setCodiceTipoEvento(String codiceTipoEvento) {
    this.codiceTipoEvento = codiceTipoEvento;
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
  


  // nome originario nello yaml: idUtente 
  public Long getIdUtente() {
    return idUtente;
  }
  public void setIdUtente(Long idUtente) {
    this.idUtente = idUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: idFruitore 
  public Long getIdFruitore() {
    return idFruitore;
  }
  public void setIdFruitore(Long idFruitore) {
    this.idFruitore = idFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: idUtenteCoinvolto 
  public Long getIdUtenteCoinvolto() {
    return idUtenteCoinvolto;
  }
  public void setIdUtenteCoinvolto(Long idUtenteCoinvolto) {
    this.idUtenteCoinvolto = idUtenteCoinvolto;
  }

  /**
   **/
  


  // nome originario nello yaml: idGruppoCoinvolto 
  public Long getIdGruppoCoinvolto() {
    return idGruppoCoinvolto;
  }
  public void setIdGruppoCoinvolto(Long idGruppoCoinvolto) {
    this.idGruppoCoinvolto = idGruppoCoinvolto;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoricoPraticaRequest storicoPraticaRequest = (StoricoPraticaRequest) o;
    return Objects.equals(idPratica, storicoPraticaRequest.idPratica) &&
        Objects.equals(idAttivita, storicoPraticaRequest.idAttivita) &&
        Objects.equals(codiceTipoEvento, storicoPraticaRequest.codiceTipoEvento) &&
        Objects.equals(descrizioneEvento, storicoPraticaRequest.descrizioneEvento) &&
        Objects.equals(idUtente, storicoPraticaRequest.idUtente) &&
        Objects.equals(idFruitore, storicoPraticaRequest.idFruitore) &&
        Objects.equals(idUtenteCoinvolto, storicoPraticaRequest.idUtenteCoinvolto) &&
        Objects.equals(idGruppoCoinvolto, storicoPraticaRequest.idGruppoCoinvolto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, idAttivita, codiceTipoEvento, descrizioneEvento, idUtente, idFruitore, idUtenteCoinvolto, idGruppoCoinvolto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StoricoPraticaRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    idAttivita: ").append(toIndentedString(idAttivita)).append("\n");
    sb.append("    codiceTipoEvento: ").append(toIndentedString(codiceTipoEvento)).append("\n");
    sb.append("    descrizioneEvento: ").append(toIndentedString(descrizioneEvento)).append("\n");
    sb.append("    idUtente: ").append(toIndentedString(idUtente)).append("\n");
    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
    sb.append("    idUtenteCoinvolto: ").append(toIndentedString(idUtenteCoinvolto)).append("\n");
    sb.append("    idGruppoCoinvolto: ").append(toIndentedString(idGruppoCoinvolto)).append("\n");
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

