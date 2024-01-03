/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RicezioneNotifiche  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Boolean all = null;
  private Boolean creaTask = null;
  private Boolean annullaTask = null;
  private Boolean assegnaTask = null;
  private Boolean commento = null;
  private Boolean condividiPratica = null;
  private Boolean smistamentoDocumenti = null;
  private Boolean elaborazioneDocumenti = null;

  /**
   **/
  


  // nome originario nello yaml: all 
  public Boolean isAll() {
    return all;
  }
  public void setAll(Boolean all) {
    this.all = all;
  }

  /**
   **/
  


  // nome originario nello yaml: creaTask 
  public Boolean isCreaTask() {
    return creaTask;
  }
  public void setCreaTask(Boolean creaTask) {
    this.creaTask = creaTask;
  }

  /**
   **/
  


  // nome originario nello yaml: annullaTask 
  public Boolean isAnnullaTask() {
    return annullaTask;
  }
  public void setAnnullaTask(Boolean annullaTask) {
    this.annullaTask = annullaTask;
  }

  /**
   **/
  


  // nome originario nello yaml: assegnaTask 
  public Boolean isAssegnaTask() {
    return assegnaTask;
  }
  public void setAssegnaTask(Boolean assegnaTask) {
    this.assegnaTask = assegnaTask;
  }

  /**
   **/
  


  // nome originario nello yaml: commento 
  public Boolean isCommento() {
    return commento;
  }
  public void setCommento(Boolean commento) {
    this.commento = commento;
  }

  /**
   **/
  


  // nome originario nello yaml: condividiPratica 
  public Boolean isCondividiPratica() {
    return condividiPratica;
  }
  public void setCondividiPratica(Boolean condividiPratica) {
    this.condividiPratica = condividiPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: smistamentoDocumenti 
  public Boolean isSmistamentoDocumenti() {
    return smistamentoDocumenti;
  }
  public void setSmistamentoDocumenti(Boolean smistamentoDocumenti) {
    this.smistamentoDocumenti = smistamentoDocumenti;
  }

  /**
   **/
  


  // nome originario nello yaml: elaborazioneDocumenti 
  public Boolean isElaborazioneDocumenti() {
    return elaborazioneDocumenti;
  }
  public void setElaborazioneDocumenti(Boolean elaborazioneDocumenti) {
    this.elaborazioneDocumenti = elaborazioneDocumenti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RicezioneNotifiche ricezioneNotifiche = (RicezioneNotifiche) o;
    return Objects.equals(all, ricezioneNotifiche.all) &&
        Objects.equals(creaTask, ricezioneNotifiche.creaTask) &&
        Objects.equals(annullaTask, ricezioneNotifiche.annullaTask) &&
        Objects.equals(assegnaTask, ricezioneNotifiche.assegnaTask) &&
        Objects.equals(commento, ricezioneNotifiche.commento) &&
        Objects.equals(condividiPratica, ricezioneNotifiche.condividiPratica) &&
        Objects.equals(smistamentoDocumenti, ricezioneNotifiche.smistamentoDocumenti) &&
        Objects.equals(elaborazioneDocumenti, ricezioneNotifiche.elaborazioneDocumenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(all, creaTask, annullaTask, assegnaTask, commento, condividiPratica, smistamentoDocumenti, elaborazioneDocumenti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RicezioneNotifiche {\n");
    
    sb.append("    all: ").append(toIndentedString(all)).append("\n");
    sb.append("    creaTask: ").append(toIndentedString(creaTask)).append("\n");
    sb.append("    annullaTask: ").append(toIndentedString(annullaTask)).append("\n");
    sb.append("    assegnaTask: ").append(toIndentedString(assegnaTask)).append("\n");
    sb.append("    commento: ").append(toIndentedString(commento)).append("\n");
    sb.append("    condividiPratica: ").append(toIndentedString(condividiPratica)).append("\n");
    sb.append("    smistamentoDocumenti: ").append(toIndentedString(smistamentoDocumenti)).append("\n");
    sb.append("    elaborazioneDocumenti: ").append(toIndentedString(elaborazioneDocumenti)).append("\n");
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

