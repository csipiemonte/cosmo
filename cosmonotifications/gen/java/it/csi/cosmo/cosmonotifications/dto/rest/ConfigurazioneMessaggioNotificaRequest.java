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

public class ConfigurazioneMessaggioNotificaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipoMessaggio = null;
  private Long idEnte = null;
  private String codiceTipoPratica = null;
  private String testo = null;

  /**
   **/
  


  // nome originario nello yaml: codiceTipoMessaggio 
  @NotNull
  public String getCodiceTipoMessaggio() {
    return codiceTipoMessaggio;
  }
  public void setCodiceTipoMessaggio(String codiceTipoMessaggio) {
    this.codiceTipoMessaggio = codiceTipoMessaggio;
  }

  /**
   **/
  


  // nome originario nello yaml: idEnte 
  @NotNull
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoPratica 
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: testo 
  @NotNull
  public String getTesto() {
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConfigurazioneMessaggioNotificaRequest configurazioneMessaggioNotificaRequest = (ConfigurazioneMessaggioNotificaRequest) o;
    return Objects.equals(codiceTipoMessaggio, configurazioneMessaggioNotificaRequest.codiceTipoMessaggio) &&
        Objects.equals(idEnte, configurazioneMessaggioNotificaRequest.idEnte) &&
        Objects.equals(codiceTipoPratica, configurazioneMessaggioNotificaRequest.codiceTipoPratica) &&
        Objects.equals(testo, configurazioneMessaggioNotificaRequest.testo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipoMessaggio, idEnte, codiceTipoPratica, testo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConfigurazioneMessaggioNotificaRequest {\n");
    
    sb.append("    codiceTipoMessaggio: ").append(toIndentedString(codiceTipoMessaggio)).append("\n");
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    codiceTipoPratica: ").append(toIndentedString(codiceTipoPratica)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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

