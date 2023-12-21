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

public class Commento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String cfAutore = null;
  private String cognomeAutore = null;
  private String nomeAutore = null;
  private String messaggio = null;
  private OffsetDateTime timestamp = null;
  private String cfDestinatario = null;
  private String cognomeDestinatario = null;
  private String nomeDestinatario = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: cfAutore 
  public String getCfAutore() {
    return cfAutore;
  }
  public void setCfAutore(String cfAutore) {
    this.cfAutore = cfAutore;
  }

  /**
   **/
  


  // nome originario nello yaml: cognomeAutore 
  public String getCognomeAutore() {
    return cognomeAutore;
  }
  public void setCognomeAutore(String cognomeAutore) {
    this.cognomeAutore = cognomeAutore;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeAutore 
  public String getNomeAutore() {
    return nomeAutore;
  }
  public void setNomeAutore(String nomeAutore) {
    this.nomeAutore = nomeAutore;
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

  /**
   **/
  


  // nome originario nello yaml: cfDestinatario 
  public String getCfDestinatario() {
    return cfDestinatario;
  }
  public void setCfDestinatario(String cfDestinatario) {
    this.cfDestinatario = cfDestinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: cognomeDestinatario 
  public String getCognomeDestinatario() {
    return cognomeDestinatario;
  }
  public void setCognomeDestinatario(String cognomeDestinatario) {
    this.cognomeDestinatario = cognomeDestinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeDestinatario 
  public String getNomeDestinatario() {
    return nomeDestinatario;
  }
  public void setNomeDestinatario(String nomeDestinatario) {
    this.nomeDestinatario = nomeDestinatario;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Commento commento = (Commento) o;
    return Objects.equals(id, commento.id) &&
        Objects.equals(cfAutore, commento.cfAutore) &&
        Objects.equals(cognomeAutore, commento.cognomeAutore) &&
        Objects.equals(nomeAutore, commento.nomeAutore) &&
        Objects.equals(messaggio, commento.messaggio) &&
        Objects.equals(timestamp, commento.timestamp) &&
        Objects.equals(cfDestinatario, commento.cfDestinatario) &&
        Objects.equals(cognomeDestinatario, commento.cognomeDestinatario) &&
        Objects.equals(nomeDestinatario, commento.nomeDestinatario);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, cfAutore, cognomeAutore, nomeAutore, messaggio, timestamp, cfDestinatario, cognomeDestinatario, nomeDestinatario);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Commento {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    cfAutore: ").append(toIndentedString(cfAutore)).append("\n");
    sb.append("    cognomeAutore: ").append(toIndentedString(cognomeAutore)).append("\n");
    sb.append("    nomeAutore: ").append(toIndentedString(nomeAutore)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    cfDestinatario: ").append(toIndentedString(cfDestinatario)).append("\n");
    sb.append("    cognomeDestinatario: ").append(toIndentedString(cognomeDestinatario)).append("\n");
    sb.append("    nomeDestinatario: ").append(toIndentedString(nomeDestinatario)).append("\n");
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

