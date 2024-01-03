/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.InformazioneAggiuntivaSmistamento;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoSmistaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String messageUUID = null;
  private String tipoTrattamento = null;
  private List<Esito> esito = new ArrayList<>();
  private List<InformazioneAggiuntivaSmistamento> informazioniAggiuntive = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: messageUUID 
  public String getMessageUUID() {
    return messageUUID;
  }
  public void setMessageUUID(String messageUUID) {
    this.messageUUID = messageUUID;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoTrattamento 
  public String getTipoTrattamento() {
    return tipoTrattamento;
  }
  public void setTipoTrattamento(String tipoTrattamento) {
    this.tipoTrattamento = tipoTrattamento;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  public List<Esito> getEsito() {
    return esito;
  }
  public void setEsito(List<Esito> esito) {
    this.esito = esito;
  }

  /**
   **/
  


  // nome originario nello yaml: informazioniAggiuntive 
  public List<InformazioneAggiuntivaSmistamento> getInformazioniAggiuntive() {
    return informazioniAggiuntive;
  }
  public void setInformazioniAggiuntive(List<InformazioneAggiuntivaSmistamento> informazioniAggiuntive) {
    this.informazioniAggiuntive = informazioniAggiuntive;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoSmistaDocumentoRequest esitoSmistaDocumentoRequest = (EsitoSmistaDocumentoRequest) o;
    return Objects.equals(messageUUID, esitoSmistaDocumentoRequest.messageUUID) &&
        Objects.equals(tipoTrattamento, esitoSmistaDocumentoRequest.tipoTrattamento) &&
        Objects.equals(esito, esitoSmistaDocumentoRequest.esito) &&
        Objects.equals(informazioniAggiuntive, esitoSmistaDocumentoRequest.informazioniAggiuntive);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageUUID, tipoTrattamento, esito, informazioniAggiuntive);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoSmistaDocumentoRequest {\n");
    
    sb.append("    messageUUID: ").append(toIndentedString(messageUUID)).append("\n");
    sb.append("    tipoTrattamento: ").append(toIndentedString(tipoTrattamento)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    informazioniAggiuntive: ").append(toIndentedString(informazioniAggiuntive)).append("\n");
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

