/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.UtenteFruitore;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class MessaggioFruitore  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private UtenteFruitore utente = null;
  private String messaggio = null;
  private OffsetDateTime timestamp = null;

  /**
   **/
  


  // nome originario nello yaml: utente 
  public UtenteFruitore getUtente() {
    return utente;
  }
  public void setUtente(UtenteFruitore utente) {
    this.utente = utente;
  }

  /**
   **/
  


  // nome originario nello yaml: messaggio 
  public String getMessaggio() {
    return messaggio;
  }
  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
  }

  /**
   **/
  


  // nome originario nello yaml: timestamp 
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessaggioFruitore messaggioFruitore = (MessaggioFruitore) o;
    return Objects.equals(utente, messaggioFruitore.utente) &&
        Objects.equals(messaggio, messaggioFruitore.messaggio) &&
        Objects.equals(timestamp, messaggioFruitore.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(utente, messaggio, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessaggioFruitore {\n");
    
    sb.append("    utente: ").append(toIndentedString(utente)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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

