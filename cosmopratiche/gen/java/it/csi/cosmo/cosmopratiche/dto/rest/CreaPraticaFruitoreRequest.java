/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmopratiche.dto.rest.TagRidotto;
import it.csi.cosmo.cosmopratiche.dto.rest.TemplateFirmaFea;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaPraticaFruitoreRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idPratica = null;
  private String codiceIpaEnte = null;
  private String codiceTipologia = null;
  private String oggetto = null;
  private String riassunto = null;
  private String utenteCreazionePratica = null;
  private String metadati = null;
  private List<TagRidotto> tags = new ArrayList<>();
  private List<TemplateFirmaFea> templateFirmaFea = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  @NotNull
  @Size(max=255)
  public String getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(String idPratica) {
    this.idPratica = idPratica;
  }

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
  


  // nome originario nello yaml: codiceTipologia 
  @NotNull
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
  @Size(max=255)
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
  @Size(max=50)
  public String getUtenteCreazionePratica() {
    return utenteCreazionePratica;
  }
  public void setUtenteCreazionePratica(String utenteCreazionePratica) {
    this.utenteCreazionePratica = utenteCreazionePratica;
  }

  /**
   **/
  


  // nome originario nello yaml: metadati 
  public String getMetadati() {
    return metadati;
  }
  public void setMetadati(String metadati) {
    this.metadati = metadati;
  }

  /**
   **/
  


  // nome originario nello yaml: tags 
  public List<TagRidotto> getTags() {
    return tags;
  }
  public void setTags(List<TagRidotto> tags) {
    this.tags = tags;
  }

  /**
   **/
  


  // nome originario nello yaml: templateFirmaFea 
  public List<TemplateFirmaFea> getTemplateFirmaFea() {
    return templateFirmaFea;
  }
  public void setTemplateFirmaFea(List<TemplateFirmaFea> templateFirmaFea) {
    this.templateFirmaFea = templateFirmaFea;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaPraticaFruitoreRequest creaPraticaFruitoreRequest = (CreaPraticaFruitoreRequest) o;
    return Objects.equals(idPratica, creaPraticaFruitoreRequest.idPratica) &&
        Objects.equals(codiceIpaEnte, creaPraticaFruitoreRequest.codiceIpaEnte) &&
        Objects.equals(codiceTipologia, creaPraticaFruitoreRequest.codiceTipologia) &&
        Objects.equals(oggetto, creaPraticaFruitoreRequest.oggetto) &&
        Objects.equals(riassunto, creaPraticaFruitoreRequest.riassunto) &&
        Objects.equals(utenteCreazionePratica, creaPraticaFruitoreRequest.utenteCreazionePratica) &&
        Objects.equals(metadati, creaPraticaFruitoreRequest.metadati) &&
        Objects.equals(tags, creaPraticaFruitoreRequest.tags) &&
        Objects.equals(templateFirmaFea, creaPraticaFruitoreRequest.templateFirmaFea);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, codiceIpaEnte, codiceTipologia, oggetto, riassunto, utenteCreazionePratica, metadati, tags, templateFirmaFea);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaPraticaFruitoreRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    codiceIpaEnte: ").append(toIndentedString(codiceIpaEnte)).append("\n");
    sb.append("    codiceTipologia: ").append(toIndentedString(codiceTipologia)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    riassunto: ").append(toIndentedString(riassunto)).append("\n");
    sb.append("    utenteCreazionePratica: ").append(toIndentedString(utenteCreazionePratica)).append("\n");
    sb.append("    metadati: ").append(toIndentedString(metadati)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    templateFirmaFea: ").append(toIndentedString(templateFirmaFea)).append("\n");
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

