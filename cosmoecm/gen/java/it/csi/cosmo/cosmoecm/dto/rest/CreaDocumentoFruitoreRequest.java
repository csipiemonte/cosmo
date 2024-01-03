/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoFruitoreContenutoRequest;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaDocumentoFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String idPadre = null;
  private String codiceTipo = null;
  private String titolo = null;
  private String descrizione = null;
  private String autore = null;
  private String uploadUUID = null;
  private CreaDocumentoFruitoreContenutoRequest contenuto = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @Size(min=1,max=255)
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: idPadre 
  @Size(min=1,max=255)
  public String getIdPadre() {
    return idPadre;
  }
  public void setIdPadre(String idPadre) {
    this.idPadre = idPadre;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipo 
  @NotNull
  @Size(min=1,max=100)
  public String getCodiceTipo() {
    return codiceTipo;
  }
  public void setCodiceTipo(String codiceTipo) {
    this.codiceTipo = codiceTipo;
  }

  /**
   **/
  


  // nome originario nello yaml: titolo 
  @Size(min=1,max=255)
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
  @Size(min=0,max=100)
  public String getAutore() {
    return autore;
  }
  public void setAutore(String autore) {
    this.autore = autore;
  }

  /**
   **/
  


  // nome originario nello yaml: uploadUUID 
  @Size(min=1,max=100)
  public String getUploadUUID() {
    return uploadUUID;
  }
  public void setUploadUUID(String uploadUUID) {
    this.uploadUUID = uploadUUID;
  }

  /**
   **/
  


  // nome originario nello yaml: contenuto 
  public CreaDocumentoFruitoreContenutoRequest getContenuto() {
    return contenuto;
  }
  public void setContenuto(CreaDocumentoFruitoreContenutoRequest contenuto) {
    this.contenuto = contenuto;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentoFruitoreRequest creaDocumentoFruitoreRequest = (CreaDocumentoFruitoreRequest) o;
    return Objects.equals(id, creaDocumentoFruitoreRequest.id) &&
        Objects.equals(idPadre, creaDocumentoFruitoreRequest.idPadre) &&
        Objects.equals(codiceTipo, creaDocumentoFruitoreRequest.codiceTipo) &&
        Objects.equals(titolo, creaDocumentoFruitoreRequest.titolo) &&
        Objects.equals(descrizione, creaDocumentoFruitoreRequest.descrizione) &&
        Objects.equals(autore, creaDocumentoFruitoreRequest.autore) &&
        Objects.equals(uploadUUID, creaDocumentoFruitoreRequest.uploadUUID) &&
        Objects.equals(contenuto, creaDocumentoFruitoreRequest.contenuto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idPadre, codiceTipo, titolo, descrizione, autore, uploadUUID, contenuto);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentoFruitoreRequest {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idPadre: ").append(toIndentedString(idPadre)).append("\n");
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    uploadUUID: ").append(toIndentedString(uploadUUID)).append("\n");
    sb.append("    contenuto: ").append(toIndentedString(contenuto)).append("\n");
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

