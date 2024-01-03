/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ApprovazioneDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private String nomeUtente = null;
  private String cognomeUtente = null;
  private OffsetDateTime dataApprovazione = null;
  private String nomeAttivita = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeUtente 
  public String getNomeUtente() {
    return nomeUtente;
  }
  public void setNomeUtente(String nomeUtente) {
    this.nomeUtente = nomeUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: cognomeUtente 
  public String getCognomeUtente() {
    return cognomeUtente;
  }
  public void setCognomeUtente(String cognomeUtente) {
    this.cognomeUtente = cognomeUtente;
  }

  /**
   **/
  


  // nome originario nello yaml: dataApprovazione 
  public OffsetDateTime getDataApprovazione() {
    return dataApprovazione;
  }
  public void setDataApprovazione(OffsetDateTime dataApprovazione) {
    this.dataApprovazione = dataApprovazione;
  }

  /**
   **/
  


  // nome originario nello yaml: nomeAttivita 
  public String getNomeAttivita() {
    return nomeAttivita;
  }
  public void setNomeAttivita(String nomeAttivita) {
    this.nomeAttivita = nomeAttivita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApprovazioneDocumento approvazioneDocumento = (ApprovazioneDocumento) o;
    return Objects.equals(id, approvazioneDocumento.id) &&
        Objects.equals(nomeUtente, approvazioneDocumento.nomeUtente) &&
        Objects.equals(cognomeUtente, approvazioneDocumento.cognomeUtente) &&
        Objects.equals(dataApprovazione, approvazioneDocumento.dataApprovazione) &&
        Objects.equals(nomeAttivita, approvazioneDocumento.nomeAttivita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nomeUtente, cognomeUtente, dataApprovazione, nomeAttivita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApprovazioneDocumento {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nomeUtente: ").append(toIndentedString(nomeUtente)).append("\n");
    sb.append("    cognomeUtente: ").append(toIndentedString(cognomeUtente)).append("\n");
    sb.append("    dataApprovazione: ").append(toIndentedString(dataApprovazione)).append("\n");
    sb.append("    nomeAttivita: ").append(toIndentedString(nomeAttivita)).append("\n");
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

