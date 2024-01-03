/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaNotificaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private String idPratica = null;
  private String descrizione = null;
  private List<String> destinatari = new ArrayList<>();
  private List<String> utentiDestinatari = new ArrayList<>();
  private List<String> gruppiDestinatari = new ArrayList<>();
  private String url = null;
  private String descrizioneUrl = null;

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @Size(min=1,max=255)
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  @Size(min=1)
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: destinatari 
  public List<String> getDestinatari() {
    return destinatari;
  }
  public void setDestinatari(List<String> destinatari) {
    this.destinatari = destinatari;
  }

  /**
   **/
  


  // nome originario nello yaml: utentiDestinatari 
  public List<String> getUtentiDestinatari() {
    return utentiDestinatari;
  }
  public void setUtentiDestinatari(List<String> utentiDestinatari) {
    this.utentiDestinatari = utentiDestinatari;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppiDestinatari 
  public List<String> getGruppiDestinatari() {
    return gruppiDestinatari;
  }
  public void setGruppiDestinatari(List<String> gruppiDestinatari) {
    this.gruppiDestinatari = gruppiDestinatari;
  }

  /**
   **/
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione_url 
  public String getDescrizioneUrl() {
    return descrizioneUrl;
  }
  public void setDescrizioneUrl(String descrizioneUrl) {
    this.descrizioneUrl = descrizioneUrl;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaNotificaFruitoreRequest creaNotificaFruitoreRequest = (CreaNotificaFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, creaNotificaFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(idPratica, creaNotificaFruitoreRequest.idPratica) &&
        Objects.equals(descrizione, creaNotificaFruitoreRequest.descrizione) &&
        Objects.equals(destinatari, creaNotificaFruitoreRequest.destinatari) &&
        Objects.equals(utentiDestinatari, creaNotificaFruitoreRequest.utentiDestinatari) &&
        Objects.equals(gruppiDestinatari, creaNotificaFruitoreRequest.gruppiDestinatari) &&
        Objects.equals(url, creaNotificaFruitoreRequest.url) &&
        Objects.equals(descrizioneUrl, creaNotificaFruitoreRequest.descrizioneUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, idPratica, descrizione, destinatari, utentiDestinatari, gruppiDestinatari, url, descrizioneUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaNotificaFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    destinatari: ").append(toIndentedString(destinatari)).append("\n");
    sb.append("    utentiDestinatari: ").append(toIndentedString(utentiDestinatari)).append("\n");
    sb.append("    gruppiDestinatari: ").append(toIndentedString(gruppiDestinatari)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    descrizioneUrl: ").append(toIndentedString(descrizioneUrl)).append("\n");
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

