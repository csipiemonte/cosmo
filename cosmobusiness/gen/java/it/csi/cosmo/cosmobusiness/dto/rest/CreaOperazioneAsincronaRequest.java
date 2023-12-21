/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaOperazioneAsincronaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nome = null;
  private String stato = null;
  private Long versione = null;
  private OffsetDateTime dataAvvio = null;

  /**
   **/
  


  // nome originario nello yaml: nome 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  @NotNull
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: versione 
  @NotNull
  public Long getVersione() {
    return versione;
  }
  public void setVersione(Long versione) {
    this.versione = versione;
  }

  /**
   **/
  


  // nome originario nello yaml: dataAvvio 
  public OffsetDateTime getDataAvvio() {
    return dataAvvio;
  }
  public void setDataAvvio(OffsetDateTime dataAvvio) {
    this.dataAvvio = dataAvvio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaOperazioneAsincronaRequest creaOperazioneAsincronaRequest = (CreaOperazioneAsincronaRequest) o;
    return Objects.equals(nome, creaOperazioneAsincronaRequest.nome) &&
        Objects.equals(stato, creaOperazioneAsincronaRequest.stato) &&
        Objects.equals(versione, creaOperazioneAsincronaRequest.versione) &&
        Objects.equals(dataAvvio, creaOperazioneAsincronaRequest.dataAvvio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, stato, versione, dataAvvio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaOperazioneAsincronaRequest {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    versione: ").append(toIndentedString(versione)).append("\n");
    sb.append("    dataAvvio: ").append(toIndentedString(dataAvvio)).append("\n");
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

