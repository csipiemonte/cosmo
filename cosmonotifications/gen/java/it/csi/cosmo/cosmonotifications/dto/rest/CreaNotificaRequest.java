/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaNotificaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idFruitore = null;
  private Long idPratica = null;
  private String messaggio = null;
  private OffsetDateTime arrivo = null;
  private OffsetDateTime scadenza = null;
  private List<String> utentiDestinatari = new ArrayList<>();
  private List<String> gruppiDestinatari = new ArrayList<>();
  private String classe = null;
  private Boolean push = null;
  private String codiceIpaEnte = null;
  private String tipoNotifica = null;
  private String evento = null;
  private String url = null;
  private String descrizioneUrl = null;

  /**
   **/
  


  // nome originario nello yaml: idFruitore 
  public Long getIdFruitore() {
    return idFruitore;
  }
  public void setIdFruitore(Long idFruitore) {
    this.idFruitore = idFruitore;
  }

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: messaggio 
  @NotNull
  public String getMessaggio() {
    return messaggio;
  }
  public void setMessaggio(String messaggio) {
    this.messaggio = messaggio;
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
  


  // nome originario nello yaml: classe 
  public String getClasse() {
    return classe;
  }
  public void setClasse(String classe) {
    this.classe = classe;
  }

  /**
   **/
  


  // nome originario nello yaml: push 
  public Boolean isPush() {
    return push;
  }
  public void setPush(Boolean push) {
    this.push = push;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoNotifica 
  public String getTipoNotifica() {
    return tipoNotifica;
  }
  public void setTipoNotifica(String tipoNotifica) {
    this.tipoNotifica = tipoNotifica;
  }

  /**
   **/
  


  // nome originario nello yaml: evento 
  public String getEvento() {
    return evento;
  }
  public void setEvento(String evento) {
    this.evento = evento;
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
    CreaNotificaRequest creaNotificaRequest = (CreaNotificaRequest) o;
    return Objects.equals(idFruitore, creaNotificaRequest.idFruitore) &&
        Objects.equals(idPratica, creaNotificaRequest.idPratica) &&
        Objects.equals(messaggio, creaNotificaRequest.messaggio) &&
        Objects.equals(arrivo, creaNotificaRequest.arrivo) &&
        Objects.equals(scadenza, creaNotificaRequest.scadenza) &&
        Objects.equals(utentiDestinatari, creaNotificaRequest.utentiDestinatari) &&
        Objects.equals(gruppiDestinatari, creaNotificaRequest.gruppiDestinatari) &&
        Objects.equals(classe, creaNotificaRequest.classe) &&
        Objects.equals(push, creaNotificaRequest.push) &&
        Objects.equals(codiceIpaEnte, creaNotificaRequest.codiceIpaEnte) &&
        Objects.equals(tipoNotifica, creaNotificaRequest.tipoNotifica) &&
        Objects.equals(evento, creaNotificaRequest.evento) &&
        Objects.equals(url, creaNotificaRequest.url) &&
        Objects.equals(descrizioneUrl, creaNotificaRequest.descrizioneUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idFruitore, idPratica, messaggio, arrivo, scadenza, utentiDestinatari, gruppiDestinatari, classe, push, codiceIpaEnte, tipoNotifica, evento, url, descrizioneUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaNotificaRequest {\n");
    
    sb.append("    idFruitore: ").append(toIndentedString(idFruitore)).append("\n");
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    arrivo: ").append(toIndentedString(arrivo)).append("\n");
    sb.append("    scadenza: ").append(toIndentedString(scadenza)).append("\n");
    sb.append("    utentiDestinatari: ").append(toIndentedString(utentiDestinatari)).append("\n");
    sb.append("    gruppiDestinatari: ").append(toIndentedString(gruppiDestinatari)).append("\n");
    sb.append("    classe: ").append(toIndentedString(classe)).append("\n");
    sb.append("    push: ").append(toIndentedString(push)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    tipoNotifica: ").append(toIndentedString(tipoNotifica)).append("\n");
    sb.append("    evento: ").append(toIndentedString(evento)).append("\n");
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

