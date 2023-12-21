/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class MessaggioOperazioneAsincrona  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String livello = null;
  private String testo = null;
  private OffsetDateTime timestamp = null;

  /**
   **/
  


  // nome originario nello yaml: livello 
  @NotNull
  public String getLivello() {
    return livello;
  }
  public void setLivello(String livello) {
    this.livello = livello;
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

  /**
   **/
  


  // nome originario nello yaml: timestamp 
  @NotNull
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
    MessaggioOperazioneAsincrona messaggioOperazioneAsincrona = (MessaggioOperazioneAsincrona) o;
    return Objects.equals(livello, messaggioOperazioneAsincrona.livello) &&
        Objects.equals(testo, messaggioOperazioneAsincrona.testo) &&
        Objects.equals(timestamp, messaggioOperazioneAsincrona.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(livello, testo, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessaggioOperazioneAsincrona {\n");
    
    sb.append("    livello: ").append(toIndentedString(livello)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
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

