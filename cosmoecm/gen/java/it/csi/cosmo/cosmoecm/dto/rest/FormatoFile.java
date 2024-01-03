/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileProfiloFeqTipoFirma;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FormatoFile  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codice = null;
  private String descrizione = null;
  private String icona = null;
  private String mimeType = null;
  private Boolean supportaAnteprima = null;
  private List<FormatoFileProfiloFeqTipoFirma> formatoFileProfiloFeqTipoFirma = new ArrayList<>();

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
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: icona 
  public String getIcona() {
    return icona;
  }
  public void setIcona(String icona) {
    this.icona = icona;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: supportaAnteprima 
  public Boolean isSupportaAnteprima() {
    return supportaAnteprima;
  }
  public void setSupportaAnteprima(Boolean supportaAnteprima) {
    this.supportaAnteprima = supportaAnteprima;
  }

  /**
   **/
  


  // nome originario nello yaml: formatoFileProfiloFeqTipoFirma 
  public List<FormatoFileProfiloFeqTipoFirma> getFormatoFileProfiloFeqTipoFirma() {
    return formatoFileProfiloFeqTipoFirma;
  }
  public void setFormatoFileProfiloFeqTipoFirma(List<FormatoFileProfiloFeqTipoFirma> formatoFileProfiloFeqTipoFirma) {
    this.formatoFileProfiloFeqTipoFirma = formatoFileProfiloFeqTipoFirma;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormatoFile formatoFile = (FormatoFile) o;
    return Objects.equals(codice, formatoFile.codice) &&
        Objects.equals(descrizione, formatoFile.descrizione) &&
        Objects.equals(icona, formatoFile.icona) &&
        Objects.equals(mimeType, formatoFile.mimeType) &&
        Objects.equals(supportaAnteprima, formatoFile.supportaAnteprima) &&
        Objects.equals(formatoFileProfiloFeqTipoFirma, formatoFile.formatoFileProfiloFeqTipoFirma);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codice, descrizione, icona, mimeType, supportaAnteprima, formatoFileProfiloFeqTipoFirma);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormatoFile {\n");
    
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    icona: ").append(toIndentedString(icona)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    supportaAnteprima: ").append(toIndentedString(supportaAnteprima)).append("\n");
    sb.append("    formatoFileProfiloFeqTipoFirma: ").append(toIndentedString(formatoFileProfiloFeqTipoFirma)).append("\n");
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

