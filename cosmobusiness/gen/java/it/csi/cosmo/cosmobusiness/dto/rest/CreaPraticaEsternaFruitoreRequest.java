/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaAttivitaFruitoreRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaPraticaEsternaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private String tipoPratica = null;
  private String oggetto = null;
  private String stato = null;
  private String riassunto = null;
  private String utenteCreazionePratica = null;
  private String idPraticaExt = null;
  private String linkPratica = null;
  private List<CreaPraticaEsternaAttivitaFruitoreRequest> attivita = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: codiceIpaEnte 
  @NotNull
  public String getCodiceIpaEnte() {
    return codiceIpaEnte;
  }
  public void setCodiceIpaEnte(String codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  @NotNull
  public String getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(String tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  @NotNull
  @Size(max=255)
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
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
  


  // nome originario nello yaml: riassunto 
  public String getRiassunto() {
    return riassunto;
  }
  public void setRiassunto(String riassunto) {
    this.riassunto = riassunto;
  }

  /**
   **/
  


  // nome originario nello yaml: utenteCreazionePratica 
  @NotNull
  @Size(max=50)
  public String getUtenteCreazionePratica() {
    return utenteCreazionePratica;
  }
  public void setUtenteCreazionePratica(String utenteCreazionePratica) {
    this.utenteCreazionePratica = utenteCreazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: idPraticaExt 
  @NotNull
  @Size(max=255)
  public String getIdPraticaExt() {
    return idPraticaExt;
  }
  public void setIdPraticaExt(String idPraticaExt) {
    this.idPraticaExt = idPraticaExt;
  }

  /**
   **/
  


  // nome originario nello yaml: linkPratica 
  @NotNull
  public String getLinkPratica() {
    return linkPratica;
  }
  public void setLinkPratica(String linkPratica) {
    this.linkPratica = linkPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: attivita 
  @NotNull
  public List<CreaPraticaEsternaAttivitaFruitoreRequest> getAttivita() {
    return attivita;
  }
  public void setAttivita(List<CreaPraticaEsternaAttivitaFruitoreRequest> attivita) {
    this.attivita = attivita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaPraticaEsternaFruitoreRequest creaPraticaEsternaFruitoreRequest = (CreaPraticaEsternaFruitoreRequest) o;
    return Objects.equals(codiceIpaEnte, creaPraticaEsternaFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(tipoPratica, creaPraticaEsternaFruitoreRequest.tipoPratica) &&
        Objects.equals(oggetto, creaPraticaEsternaFruitoreRequest.oggetto) &&
        Objects.equals(stato, creaPraticaEsternaFruitoreRequest.stato) &&
        Objects.equals(riassunto, creaPraticaEsternaFruitoreRequest.riassunto) &&
        Objects.equals(utenteCreazionePratica, creaPraticaEsternaFruitoreRequest.utenteCreazionePratica) &&
        Objects.equals(idPraticaExt, creaPraticaEsternaFruitoreRequest.idPraticaExt) &&
        Objects.equals(linkPratica, creaPraticaEsternaFruitoreRequest.linkPratica) &&
        Objects.equals(attivita, creaPraticaEsternaFruitoreRequest.attivita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, tipoPratica, oggetto, stato, riassunto, utenteCreazionePratica, idPraticaExt, linkPratica, attivita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaPraticaEsternaFruitoreRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    utenteCreazionePratica: ").append(toIndentedString(utenteCreazionePratica)).append("\n");
    sb.append("    idPraticaExt: ").append(toIndentedString(idPraticaExt)).append("\n");
    sb.append("    linkPratica: ").append(toIndentedString(linkPratica)).append("\n");
    sb.append("    attivita: ").append(toIndentedString(attivita)).append("\n");
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

