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
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoSmistamentoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Esito esito = null;
  private Integer numDocumentiDaSmistare = null;
  private Integer numDocumentiSmistatiCorrettamente = null;
  private Integer numDocumentiSmistatiInErrore = null;
  private String idPratica = null;
  private String identificativoEvento = null;

  /**
   **/
  


  // nome originario nello yaml: esito 
  public Esito getEsito() {
    return esito;
  }
  public void setEsito(Esito esito) {
    this.esito = esito;
  }

  /**
   **/
  


  // nome originario nello yaml: numDocumentiDaSmistare 
  public Integer getNumDocumentiDaSmistare() {
    return numDocumentiDaSmistare;
  }
  public void setNumDocumentiDaSmistare(Integer numDocumentiDaSmistare) {
    this.numDocumentiDaSmistare = numDocumentiDaSmistare;
  }

  /**
   **/
  


  // nome originario nello yaml: numDocumentiSmistatiCorrettamente 
  public Integer getNumDocumentiSmistatiCorrettamente() {
    return numDocumentiSmistatiCorrettamente;
  }
  public void setNumDocumentiSmistatiCorrettamente(Integer numDocumentiSmistatiCorrettamente) {
    this.numDocumentiSmistatiCorrettamente = numDocumentiSmistatiCorrettamente;
  }

  /**
   **/
  


  // nome originario nello yaml: numDocumentiSmistatiInErrore 
  public Integer getNumDocumentiSmistatiInErrore() {
    return numDocumentiSmistatiInErrore;
  }
  public void setNumDocumentiSmistatiInErrore(Integer numDocumentiSmistatiInErrore) {
    this.numDocumentiSmistatiInErrore = numDocumentiSmistatiInErrore;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: identificativoEvento 
  public String getIdentificativoEvento() {
    return identificativoEvento;
  }
  public void setIdentificativoEvento(String identificativoEvento) {
    this.identificativoEvento = identificativoEvento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoSmistamentoResponse esitoSmistamentoResponse = (EsitoSmistamentoResponse) o;
    return Objects.equals(esito, esitoSmistamentoResponse.esito) &&
        Objects.equals(numDocumentiDaSmistare, esitoSmistamentoResponse.numDocumentiDaSmistare) &&
        Objects.equals(numDocumentiSmistatiCorrettamente, esitoSmistamentoResponse.numDocumentiSmistatiCorrettamente) &&
        Objects.equals(numDocumentiSmistatiInErrore, esitoSmistamentoResponse.numDocumentiSmistatiInErrore) &&
        Objects.equals(idPratica, esitoSmistamentoResponse.idPratica) &&
        Objects.equals(identificativoEvento, esitoSmistamentoResponse.identificativoEvento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esito, numDocumentiDaSmistare, numDocumentiSmistatiCorrettamente, numDocumentiSmistatiInErrore, idPratica, identificativoEvento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoSmistamentoResponse {\n");
    
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
    sb.append("    numDocumentiDaSmistare: ").append(toIndentedString(numDocumentiDaSmistare)).append("\n");
    sb.append("    numDocumentiSmistatiCorrettamente: ").append(toIndentedString(numDocumentiSmistatiCorrettamente)).append("\n");
    sb.append("    numDocumentiSmistatiInErrore: ").append(toIndentedString(numDocumentiSmistatiInErrore)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    identificativoEvento: ").append(toIndentedString(identificativoEvento)).append("\n");
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

