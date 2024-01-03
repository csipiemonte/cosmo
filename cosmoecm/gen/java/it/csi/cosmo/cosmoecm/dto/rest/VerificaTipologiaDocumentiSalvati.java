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

public class VerificaTipologiaDocumentiSalvati  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipologiaDocumento = null;
  private String codiceTipologiaDocumentoPadre = null;
  private String descrizioneTipologiaDocumento = null;
  private Boolean presente = null;

  /**
   **/
  


  // nome originario nello yaml: codiceTipologiaDocumento 
  @NotNull
  public String getCodiceTipologiaDocumento() {
    return codiceTipologiaDocumento;
  }
  public void setCodiceTipologiaDocumento(String codiceTipologiaDocumento) {
    this.codiceTipologiaDocumento = codiceTipologiaDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTipologiaDocumentoPadre 
  public String getCodiceTipologiaDocumentoPadre() {
    return codiceTipologiaDocumentoPadre;
  }
  public void setCodiceTipologiaDocumentoPadre(String codiceTipologiaDocumentoPadre) {
    this.codiceTipologiaDocumentoPadre = codiceTipologiaDocumentoPadre;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizioneTipologiaDocumento 
  public String getDescrizioneTipologiaDocumento() {
    return descrizioneTipologiaDocumento;
  }
  public void setDescrizioneTipologiaDocumento(String descrizioneTipologiaDocumento) {
    this.descrizioneTipologiaDocumento = descrizioneTipologiaDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: presente 
  public Boolean isPresente() {
    return presente;
  }
  public void setPresente(Boolean presente) {
    this.presente = presente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerificaTipologiaDocumentiSalvati verificaTipologiaDocumentiSalvati = (VerificaTipologiaDocumentiSalvati) o;
    return Objects.equals(codiceTipologiaDocumento, verificaTipologiaDocumentiSalvati.codiceTipologiaDocumento) &&
        Objects.equals(codiceTipologiaDocumentoPadre, verificaTipologiaDocumentiSalvati.codiceTipologiaDocumentoPadre) &&
        Objects.equals(descrizioneTipologiaDocumento, verificaTipologiaDocumentiSalvati.descrizioneTipologiaDocumento) &&
        Objects.equals(presente, verificaTipologiaDocumentiSalvati.presente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipologiaDocumento, codiceTipologiaDocumentoPadre, descrizioneTipologiaDocumento, presente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerificaTipologiaDocumentiSalvati {\n");
    
    sb.append("    codiceTipologiaDocumento: ").append(toIndentedString(codiceTipologiaDocumento)).append("\n");
    sb.append("    codiceTipologiaDocumentoPadre: ").append(toIndentedString(codiceTipologiaDocumentoPadre)).append("\n");
    sb.append("    descrizioneTipologiaDocumento: ").append(toIndentedString(descrizioneTipologiaDocumento)).append("\n");
    sb.append("    presente: ").append(toIndentedString(presente)).append("\n");
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

