/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitiStepType;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniAggiuntiveType;
import it.csi.cosmo.cosmobusiness.dto.rest.ResultType;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoSmistaDocumentoType  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String messageUUID = null;
  private String idDocumentoFruitore = null;
  private String tipoTrattamento = null;
  private EsitiStepType esitiStep = null;
  private InformazioniAggiuntiveType informazioniAggiuntive = null;
  private ResultType esitoTrattamento = null;

  /**
   **/
  


  // nome originario nello yaml: messageUUID 
  @Size(min=1,max=50)
  public String getMessageUUID() {
    return messageUUID;
  }
  public void setMessageUUID(String messageUUID) {
    this.messageUUID = messageUUID;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumentoFruitore 
  @Size(min=1,max=200)
  public String getIdDocumentoFruitore() {
    return idDocumentoFruitore;
  }
  public void setIdDocumentoFruitore(String idDocumentoFruitore) {
    this.idDocumentoFruitore = idDocumentoFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoTrattamento 
  @NotNull
  @Size(min=1,max=50)
  public String getTipoTrattamento() {
    return tipoTrattamento;
  }
  public void setTipoTrattamento(String tipoTrattamento) {
    this.tipoTrattamento = tipoTrattamento;
  }

  /**
   **/
  


  // nome originario nello yaml: esitiStep 
  public EsitiStepType getEsitiStep() {
    return esitiStep;
  }
  public void setEsitiStep(EsitiStepType esitiStep) {
    this.esitiStep = esitiStep;
  }

  /**
   **/
  


  // nome originario nello yaml: informazioniAggiuntive 
  public InformazioniAggiuntiveType getInformazioniAggiuntive() {
    return informazioniAggiuntive;
  }
  public void setInformazioniAggiuntive(InformazioniAggiuntiveType informazioniAggiuntive) {
    this.informazioniAggiuntive = informazioniAggiuntive;
  }

  /**
   **/
  


  // nome originario nello yaml: esitoTrattamento 
  public ResultType getEsitoTrattamento() {
    return esitoTrattamento;
  }
  public void setEsitoTrattamento(ResultType esitoTrattamento) {
    this.esitoTrattamento = esitoTrattamento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoSmistaDocumentoType esitoSmistaDocumentoType = (EsitoSmistaDocumentoType) o;
    return Objects.equals(messageUUID, esitoSmistaDocumentoType.messageUUID) &&
        Objects.equals(idDocumentoFruitore, esitoSmistaDocumentoType.idDocumentoFruitore) &&
        Objects.equals(tipoTrattamento, esitoSmistaDocumentoType.tipoTrattamento) &&
        Objects.equals(esitiStep, esitoSmistaDocumentoType.esitiStep) &&
        Objects.equals(informazioniAggiuntive, esitoSmistaDocumentoType.informazioniAggiuntive) &&
        Objects.equals(esitoTrattamento, esitoSmistaDocumentoType.esitoTrattamento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageUUID, idDocumentoFruitore, tipoTrattamento, esitiStep, informazioniAggiuntive, esitoTrattamento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoSmistaDocumentoType {\n");
    
    sb.append("    messageUUID: ").append(toIndentedString(messageUUID)).append("\n");
    sb.append("    idDocumentoFruitore: ").append(toIndentedString(idDocumentoFruitore)).append("\n");
    sb.append("    tipoTrattamento: ").append(toIndentedString(tipoTrattamento)).append("\n");
    sb.append("    esitiStep: ").append(toIndentedString(esitiStep)).append("\n");
    sb.append("    informazioniAggiuntive: ").append(toIndentedString(informazioniAggiuntive)).append("\n");
    sb.append("    esitoTrattamento: ").append(toIndentedString(esitoTrattamento)).append("\n");
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

