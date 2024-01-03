/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaPraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceIpaEnte = null;
  private String codiceTipologia = null;
  private String oggetto = null;
  private String riassunto = null;
  private String utenteCreazionePratica = null;
  private String codiceFruitore = null;

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
  


  // nome originario nello yaml: codiceTipologia 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceTipologia() {
    return codiceTipologia;
  }
  public void setCodiceTipologia(String codiceTipologia) {
    this.codiceTipologia = codiceTipologia;
  }

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  @NotNull
  @Size(min=1,max=255)
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
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
  @Size(min=1,max=50)
  public String getUtenteCreazionePratica() {
    return utenteCreazionePratica;
  }
  public void setUtenteCreazionePratica(String utenteCreazionePratica) {
    this.utenteCreazionePratica = utenteCreazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceFruitore 
  public String getCodiceFruitore() {
    return codiceFruitore;
  }
  public void setCodiceFruitore(String codiceFruitore) {
    this.codiceFruitore = codiceFruitore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaPraticaRequest creaPraticaRequest = (CreaPraticaRequest) o;
    return Objects.equals(codiceIpaEnte, creaPraticaRequest.codiceIpaEnte) &&
        Objects.equals(codiceTipologia, creaPraticaRequest.codiceTipologia) &&
        Objects.equals(oggetto, creaPraticaRequest.oggetto) &&
        Objects.equals(riassunto, creaPraticaRequest.riassunto) &&
        Objects.equals(utenteCreazionePratica, creaPraticaRequest.utenteCreazionePratica) &&
        Objects.equals(codiceFruitore, creaPraticaRequest.codiceFruitore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceIpaEnte, codiceTipologia, oggetto, riassunto, utenteCreazionePratica, codiceFruitore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaPraticaRequest {\n");
    
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    codiceTipologia: ").append(toIndentedString(codiceTipologia)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    utenteCreazionePratica: ").append(toIndentedString(utenteCreazionePratica)).append("\n");
    sb.append("    codiceFruitore: ").append(toIndentedString(codiceFruitore)).append("\n");
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

