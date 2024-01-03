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

public class CreaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipo = null;
  private String titolo = null;
  private String descrizione = null;
  private String autore = null;
  private String uuidFile = null;
  private String parentId = null;

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

  /**
   **/
  


  // nome originario nello yaml: uuidFile 
  public String getUuidFile() {
    return uuidFile;
  }
  public void setUuidFile(String uuidFile) {
    this.uuidFile = uuidFile;
  }

  /**
   **/
  


  // nome originario nello yaml: parentId 
  public String getParentId() {
    return parentId;
  }
  public void setParentId(String parentId) {
    this.parentId = parentId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentoRequest creaDocumentoRequest = (CreaDocumentoRequest) o;
    return Objects.equals(codiceTipo, creaDocumentoRequest.codiceTipo) &&
        Objects.equals(titolo, creaDocumentoRequest.titolo) &&
        Objects.equals(descrizione, creaDocumentoRequest.descrizione) &&
        Objects.equals(autore, creaDocumentoRequest.autore) &&
        Objects.equals(uuidFile, creaDocumentoRequest.uuidFile) &&
        Objects.equals(parentId, creaDocumentoRequest.parentId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipo, titolo, descrizione, autore, uuidFile, parentId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentoRequest {\n");
    
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    uuidFile: ").append(toIndentedString(uuidFile)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
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

