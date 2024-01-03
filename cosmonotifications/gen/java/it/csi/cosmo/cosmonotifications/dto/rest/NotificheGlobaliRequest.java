/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class NotificheGlobaliRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private String tipoNotifica = null;
  private String messaggio = null;
  private String classe = null;
  private String evento = null;
  private String codiceIpaEnte = null;
  private String url = null;
  private String descrizioneUrl = null;

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoNotifica 
  @NotNull
  public String getTipoNotifica() {
    return tipoNotifica;
  }
  public void setTipoNotifica(String tipoNotifica) {
    this.tipoNotifica = tipoNotifica;
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
  


  // nome originario nello yaml: classe 
  public String getClasse() {
    return classe;
  }
  public void setClasse(String classe) {
    this.classe = classe;
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
  


  // nome originario nello yaml: codiceIpaEnte 
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
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
  


  // nome originario nello yaml: descrizioneUrl 
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
    NotificheGlobaliRequest notificheGlobaliRequest = (NotificheGlobaliRequest) o;
    return Objects.equals(idPratica, notificheGlobaliRequest.idPratica) &&
        Objects.equals(tipoNotifica, notificheGlobaliRequest.tipoNotifica) &&
        Objects.equals(messaggio, notificheGlobaliRequest.messaggio) &&
        Objects.equals(classe, notificheGlobaliRequest.classe) &&
        Objects.equals(evento, notificheGlobaliRequest.evento) &&
        Objects.equals(codiceIpaEnte, notificheGlobaliRequest.codiceIpaEnte) &&
        Objects.equals(url, notificheGlobaliRequest.url) &&
        Objects.equals(descrizioneUrl, notificheGlobaliRequest.descrizioneUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, tipoNotifica, messaggio, classe, evento, codiceIpaEnte, url, descrizioneUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificheGlobaliRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    tipoNotifica: ").append(toIndentedString(tipoNotifica)).append("\n");
    sb.append("    messaggio: ").append(toIndentedString(messaggio)).append("\n");
    sb.append("    classe: ").append(toIndentedString(classe)).append("\n");
    sb.append("    evento: ").append(toIndentedString(evento)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
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

