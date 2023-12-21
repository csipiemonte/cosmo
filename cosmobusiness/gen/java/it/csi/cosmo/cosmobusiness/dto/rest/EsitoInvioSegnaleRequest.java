/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoInvioSegnaleRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceInvio = null;
  private String codiceSegnale = null;
  private Boolean successo = null;
  private String errore = null;
  private Object dettagliErrore = null;

  /**
   **/
  


  // nome originario nello yaml: codiceInvio 
  @NotNull
  public String getCodiceInvio() {
    return codiceInvio;
  }
  public void setCodiceInvio(String codiceInvio) {
    this.codiceInvio = codiceInvio;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceSegnale 
  @NotNull
  public String getCodiceSegnale() {
    return codiceSegnale;
  }
  public void setCodiceSegnale(String codiceSegnale) {
    this.codiceSegnale = codiceSegnale;
  }

  /**
   **/
  


  // nome originario nello yaml: successo 
  @NotNull
  public Boolean isSuccesso() {
    return successo;
  }
  public void setSuccesso(Boolean successo) {
    this.successo = successo;
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
   * dettagli aggiuntivi sull&#39;errore
   **/
  


  // nome originario nello yaml: dettagliErrore 
  public Object getDettagliErrore() {
    return dettagliErrore;
  }
  public void setDettagliErrore(Object dettagliErrore) {
    this.dettagliErrore = dettagliErrore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoInvioSegnaleRequest esitoInvioSegnaleRequest = (EsitoInvioSegnaleRequest) o;
    return Objects.equals(codiceInvio, esitoInvioSegnaleRequest.codiceInvio) &&
        Objects.equals(codiceSegnale, esitoInvioSegnaleRequest.codiceSegnale) &&
        Objects.equals(successo, esitoInvioSegnaleRequest.successo) &&
        Objects.equals(errore, esitoInvioSegnaleRequest.errore) &&
        Objects.equals(dettagliErrore, esitoInvioSegnaleRequest.dettagliErrore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceInvio, codiceSegnale, successo, errore, dettagliErrore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoInvioSegnaleRequest {\n");
    
    sb.append("    codiceInvio: ").append(toIndentedString(codiceInvio)).append("\n");
    sb.append("    codiceSegnale: ").append(toIndentedString(codiceSegnale)).append("\n");
    sb.append("    successo: ").append(toIndentedString(successo)).append("\n");
    sb.append("    errore: ").append(toIndentedString(errore)).append("\n");
    sb.append("    dettagliErrore: ").append(toIndentedString(dettagliErrore)).append("\n");
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

