/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmonotifications.dto.rest.Esito;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitoCreazioneNotificaFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String destinatario = null;
  private String utenteDestinatario = null;
  private String gruppoDestinatario = null;
  private Esito esito = null;

  /**
   **/
  


  // nome originario nello yaml: destinatario 
  @Size(min=1,max=255)
  public String getDestinatario() {
    return destinatario;
  }
  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteDestinatario 
  @Size(min=1,max=255)
  public String getUtenteDestinatario() {
    return utenteDestinatario;
  }
  public void setUtenteDestinatario(String utenteDestinatario) {
    this.utenteDestinatario = utenteDestinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppoDestinatario 
  @Size(min=1,max=255)
  public String getGruppoDestinatario() {
    return gruppoDestinatario;
  }
  public void setGruppoDestinatario(String gruppoDestinatario) {
    this.gruppoDestinatario = gruppoDestinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: esito 
  @NotNull
  public Esito getEsito() {
    return esito;
  }
  public void setEsito(Esito esito) {
    this.esito = esito;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitoCreazioneNotificaFruitoreResponse esitoCreazioneNotificaFruitoreResponse = (EsitoCreazioneNotificaFruitoreResponse) o;
    return Objects.equals(destinatario, esitoCreazioneNotificaFruitoreResponse.destinatario) &&
        Objects.equals(utenteDestinatario, esitoCreazioneNotificaFruitoreResponse.utenteDestinatario) &&
        Objects.equals(gruppoDestinatario, esitoCreazioneNotificaFruitoreResponse.gruppoDestinatario) &&
        Objects.equals(esito, esitoCreazioneNotificaFruitoreResponse.esito);
  }

  @Override
  public int hashCode() {
    return Objects.hash(destinatario, utenteDestinatario, gruppoDestinatario, esito);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitoCreazioneNotificaFruitoreResponse {\n");
    
    sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
    sb.append("    utenteDestinatario: ").append(toIndentedString(utenteDestinatario)).append("\n");
    sb.append("    gruppoDestinatario: ").append(toIndentedString(gruppoDestinatario)).append("\n");
    sb.append("    esito: ").append(toIndentedString(esito)).append("\n");
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

