/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaTemplateRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idEnte = null;
  private String codiceTipoPratica = null;
  private String codiceTemplatePadre = null;
  private String codice = null;
  private String sorgenteTemplate = null;

  /**
   **/
  


  // nome originario nello yaml: idEnte 
  public Long getIdEnte() {
    return idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipoPratica 
  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }
  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTemplatePadre 
  public String getCodiceTemplatePadre() {
    return codiceTemplatePadre;
  }
  public void setCodiceTemplatePadre(String codiceTemplatePadre) {
    this.codiceTemplatePadre = codiceTemplatePadre;
  }

  /**
   **/
  


  // nome originario nello yaml: codice 
  @NotNull
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: sorgenteTemplate 
  @NotNull
  public String getSorgenteTemplate() {
    return sorgenteTemplate;
  }
  public void setSorgenteTemplate(String sorgenteTemplate) {
    this.sorgenteTemplate = sorgenteTemplate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaTemplateRequest creaTemplateRequest = (CreaTemplateRequest) o;
    return Objects.equals(idEnte, creaTemplateRequest.idEnte) &&
        Objects.equals(codiceTipoPratica, creaTemplateRequest.codiceTipoPratica) &&
        Objects.equals(codiceTemplatePadre, creaTemplateRequest.codiceTemplatePadre) &&
        Objects.equals(codice, creaTemplateRequest.codice) &&
        Objects.equals(sorgenteTemplate, creaTemplateRequest.sorgenteTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idEnte, codiceTipoPratica, codiceTemplatePadre, codice, sorgenteTemplate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaTemplateRequest {\n");
    
    sb.append("    idEnte: ").append(toIndentedString(idEnte)).append("\n");
    sb.append("    codiceTipoPratica: ").append(toIndentedString(codiceTipoPratica)).append("\n");
    sb.append("    codiceTemplatePadre: ").append(toIndentedString(codiceTemplatePadre)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    sorgenteTemplate: ").append(toIndentedString(sorgenteTemplate)).append("\n");
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

