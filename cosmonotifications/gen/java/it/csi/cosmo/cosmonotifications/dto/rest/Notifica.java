/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmonotifications.dto.rest.Fruitore;
import it.csi.cosmo.cosmonotifications.dto.rest.Pratica;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Notifica  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer id = null;
  private Fruitore fruitore = null;
  private String descrizione = null;
  private OffsetDateTime arrivo = null;
  private OffsetDateTime scadenza = null;
  private OffsetDateTime lettura = null;
  private String codiceFiscale = null;
  private Pratica pratica = null;
  private List<String> destinatari = new ArrayList<>();
  private String url = null;
  private String urlDescrizione = null;

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
  


  // nome originario nello yaml: fruitore 
  public Fruitore getFruitore() {
    return fruitore;
  }
  public void setFruitore(Fruitore fruitore) {
    this.fruitore = fruitore;
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
  


  // nome originario nello yaml: arrivo 
  public OffsetDateTime getArrivo() {
    return arrivo;
  }
  public void setArrivo(OffsetDateTime arrivo) {
    this.arrivo = arrivo;
  }

  /**
   **/
  


  // nome originario nello yaml: scadenza 
  public OffsetDateTime getScadenza() {
    return scadenza;
  }
  public void setScadenza(OffsetDateTime scadenza) {
    this.scadenza = scadenza;
  }

  /**
   **/
  


  // nome originario nello yaml: lettura 
  public OffsetDateTime getLettura() {
    return lettura;
  }
  public void setLettura(OffsetDateTime lettura) {
    this.lettura = lettura;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFiscale 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   **/
  


  // nome originario nello yaml: pratica 
  public Pratica getPratica() {
    return pratica;
  }
  public void setPratica(Pratica pratica) {
    this.pratica = pratica;
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
  


  // nome originario nello yaml: url 
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   **/
  


  // nome originario nello yaml: urlDescrizione 
  public String getUrlDescrizione() {
    return urlDescrizione;
  }
  public void setUrlDescrizione(String urlDescrizione) {
    this.urlDescrizione = urlDescrizione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Notifica notifica = (Notifica) o;
    return Objects.equals(id, notifica.id) &&
        Objects.equals(fruitore, notifica.fruitore) &&
        Objects.equals(descrizione, notifica.descrizione) &&
        Objects.equals(arrivo, notifica.arrivo) &&
        Objects.equals(scadenza, notifica.scadenza) &&
        Objects.equals(lettura, notifica.lettura) &&
        Objects.equals(codiceFiscale, notifica.codiceFiscale) &&
        Objects.equals(pratica, notifica.pratica) &&
        Objects.equals(destinatari, notifica.destinatari) &&
        Objects.equals(url, notifica.url) &&
        Objects.equals(urlDescrizione, notifica.urlDescrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fruitore, descrizione, arrivo, scadenza, lettura, codiceFiscale, pratica, destinatari, url, urlDescrizione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Notifica {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fruitore: ").append(toIndentedString(fruitore)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    arrivo: ").append(toIndentedString(arrivo)).append("\n");
    sb.append("    scadenza: ").append(toIndentedString(scadenza)).append("\n");
    sb.append("    lettura: ").append(toIndentedString(lettura)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    pratica: ").append(toIndentedString(pratica)).append("\n");
    sb.append("    destinatari: ").append(toIndentedString(destinatari)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    urlDescrizione: ").append(toIndentedString(urlDescrizione)).append("\n");
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

