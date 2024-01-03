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

public class CreaDocumentoLinkFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String idPadre = null;
  private String codiceTipo = null;
  private String titolo = null;
  private String descrizione = null;
  private String autore = null;
  private String nomeFile = null;
  private String link = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
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
  


  // nome originario nello yaml: nomeFile 
  @NotNull
  @Size(min=1,max=255)
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: link 
  @NotNull
  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentoLinkFruitoreRequest creaDocumentoLinkFruitoreRequest = (CreaDocumentoLinkFruitoreRequest) o;
    return Objects.equals(id, creaDocumentoLinkFruitoreRequest.id) &&
        Objects.equals(idPadre, creaDocumentoLinkFruitoreRequest.idPadre) &&
        Objects.equals(codiceTipo, creaDocumentoLinkFruitoreRequest.codiceTipo) &&
        Objects.equals(titolo, creaDocumentoLinkFruitoreRequest.titolo) &&
        Objects.equals(descrizione, creaDocumentoLinkFruitoreRequest.descrizione) &&
        Objects.equals(autore, creaDocumentoLinkFruitoreRequest.autore) &&
        Objects.equals(nomeFile, creaDocumentoLinkFruitoreRequest.nomeFile) &&
        Objects.equals(link, creaDocumentoLinkFruitoreRequest.link);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, idPadre, codiceTipo, titolo, descrizione, autore, nomeFile, link);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentoLinkFruitoreRequest {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    idPadre: ").append(toIndentedString(idPadre)).append("\n");
    sb.append("    codiceTipo: ").append(toIndentedString(codiceTipo)).append("\n");
    sb.append("    titolo: ").append(toIndentedString(titolo)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

