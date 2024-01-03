/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoEnte;
import it.csi.cosmo.cosmoecm.dto.rest.TipoPratica;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TemplateReport  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private RiferimentoEnte ente = null;
  private TipoPratica tipoPratica = null;
  private String codiceTemplatePadre = null;
  private String codice = null;
  private byte[] sorgenteTemplate = null;
  private byte[] templateCompilato = null;
  private Long id = null;

  /**
   **/
  


  // nome originario nello yaml: ente 
  public RiferimentoEnte getEnte() {
    return ente;
  }
  public void setEnte(RiferimentoEnte ente) {
    this.ente = ente;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoPratica 
  public TipoPratica getTipoPratica() {
    return tipoPratica;
  }
  public void setTipoPratica(TipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
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
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: sorgenteTemplate 
  public byte[] getSorgenteTemplate() {
    return sorgenteTemplate;
  }
  public void setSorgenteTemplate(byte[] sorgenteTemplate) {
    this.sorgenteTemplate = sorgenteTemplate;
  }

  /**
   **/
  


  // nome originario nello yaml: templateCompilato 
  public byte[] getTemplateCompilato() {
    return templateCompilato;
  }
  public void setTemplateCompilato(byte[] templateCompilato) {
    this.templateCompilato = templateCompilato;
  }

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TemplateReport templateReport = (TemplateReport) o;
    return Objects.equals(ente, templateReport.ente) &&
        Objects.equals(tipoPratica, templateReport.tipoPratica) &&
        Objects.equals(codiceTemplatePadre, templateReport.codiceTemplatePadre) &&
        Objects.equals(codice, templateReport.codice) &&
        Objects.equals(sorgenteTemplate, templateReport.sorgenteTemplate) &&
        Objects.equals(templateCompilato, templateReport.templateCompilato) &&
        Objects.equals(id, templateReport.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ente, tipoPratica, codiceTemplatePadre, codice, sorgenteTemplate, templateCompilato, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TemplateReport {\n");
    
    sb.append("    ente: ").append(toIndentedString(ente)).append("\n");
    sb.append("    tipoPratica: ").append(toIndentedString(tipoPratica)).append("\n");
    sb.append("    codiceTemplatePadre: ").append(toIndentedString(codiceTemplatePadre)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    sorgenteTemplate: ").append(toIndentedString(sorgenteTemplate)).append("\n");
    sb.append("    templateCompilato: ").append(toIndentedString(templateCompilato)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

