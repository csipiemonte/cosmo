/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipo = null;
  private String titolo = null;
  private String descrizione = null;
  private String autore = null;

  /**
   **/
  


  // nome originario nello yaml: codiceTipo 
  @NotNull
  public String getCodiceTipo() {
    return codiceTipo;
  }
  public void setCodiceTipo(String codiceTipo) {
    this.codiceTipo = codiceTipo;
  }

  /**
   **/
  


  // nome originario nello yaml: titolo 
  public String getTitolo() {
    return titolo;
  }
  public void setTitolo(String titolo) {
    this.titolo = titolo;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: autore 
  public String getAutore() {
    return autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaDocumentoRequest aggiornaDocumentoRequest = (AggiornaDocumentoRequest) o;
    return Objects.equals(codiceTipo, aggiornaDocumentoRequest.codiceTipo) &&
        Objects.equals(titolo, aggiornaDocumentoRequest.titolo) &&
        Objects.equals(descrizione, aggiornaDocumentoRequest.descrizione) &&
        Objects.equals(autore, aggiornaDocumentoRequest.autore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipo, titolo, descrizione, autore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaDocumentoRequest {\n");
    
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
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

