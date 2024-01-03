/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceModale;
import it.csi.cosmo.cosmonotifications.dto.rest.CodicePagina;
import it.csi.cosmo.cosmonotifications.dto.rest.CodiceTab;
import it.csi.cosmo.cosmonotifications.dto.rest.CustomForm;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Helper  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private CodicePagina codicePagina = null;
  private String html = null;
  private Long id = null;
  private CodiceTab codiceTab = null;
  private CustomForm codiceForm = null;
  private CodiceModale codiceModale = null;

  /**
   **/
  


  // nome originario nello yaml: codicePagina 
  public CodicePagina getCodicePagina() {
    return codicePagina;
  }
  public void setCodicePagina(CodicePagina codicePagina) {
    this.codicePagina = codicePagina;
  }

  /**
   **/
  


  // nome originario nello yaml: html 
  public String getHtml() {
    return html;
  }
  public void setHtml(String html) {
    this.html = html;
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

  /**
   **/
  


  // nome originario nello yaml: codiceTab 
  public CodiceTab getCodiceTab() {
    return codiceTab;
  }
  public void setCodiceTab(CodiceTab codiceTab) {
    this.codiceTab = codiceTab;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceForm 
  public CustomForm getCodiceForm() {
    return codiceForm;
  }
  public void setCodiceForm(CustomForm codiceForm) {
    this.codiceForm = codiceForm;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceModale 
  public CodiceModale getCodiceModale() {
    return codiceModale;
  }
  public void setCodiceModale(CodiceModale codiceModale) {
    this.codiceModale = codiceModale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Helper helper = (Helper) o;
    return Objects.equals(codicePagina, helper.codicePagina) &&
        Objects.equals(html, helper.html) &&
        Objects.equals(id, helper.id) &&
        Objects.equals(codiceTab, helper.codiceTab) &&
        Objects.equals(codiceForm, helper.codiceForm) &&
        Objects.equals(codiceModale, helper.codiceModale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codicePagina, html, id, codiceTab, codiceForm, codiceModale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Helper {\n");
    
    sb.append("    codicePagina: ").append(toIndentedString(codicePagina)).append("\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceTab: ").append(toIndentedString(codiceTab)).append("\n");
    sb.append("    codiceForm: ").append(toIndentedString(codiceForm)).append("\n");
    sb.append("    codiceModale: ").append(toIndentedString(codiceModale)).append("\n");
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

