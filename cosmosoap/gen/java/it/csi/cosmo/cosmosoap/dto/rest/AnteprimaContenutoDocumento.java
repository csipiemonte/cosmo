/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.FormatoFile;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AnteprimaContenutoDocumento  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeFile = null;
  private String shareUrl = null;
  private Long dimensione = null;
  private FormatoFile formatoFile = null;
  private Long id = null;

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: shareUrl 
  public String getShareUrl() {
    return shareUrl;
  }
  public void setShareUrl(String shareUrl) {
    this.shareUrl = shareUrl;
  }

  /**
   **/
  


  // nome originario nello yaml: dimensione 
  public Long getDimensione() {
    return dimensione;
  }
  public void setDimensione(Long dimensione) {
    this.dimensione = dimensione;
  }

  /**
   **/
  


  // nome originario nello yaml: formatoFile 
  public FormatoFile getFormatoFile() {
    return formatoFile;
  }
  public void setFormatoFile(FormatoFile formatoFile) {
    this.formatoFile = formatoFile;
  }

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
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
    AnteprimaContenutoDocumento anteprimaContenutoDocumento = (AnteprimaContenutoDocumento) o;
    return Objects.equals(nomeFile, anteprimaContenutoDocumento.nomeFile) &&
        Objects.equals(shareUrl, anteprimaContenutoDocumento.shareUrl) &&
        Objects.equals(dimensione, anteprimaContenutoDocumento.dimensione) &&
        Objects.equals(formatoFile, anteprimaContenutoDocumento.formatoFile) &&
        Objects.equals(id, anteprimaContenutoDocumento.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, shareUrl, dimensione, formatoFile, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnteprimaContenutoDocumento {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    shareUrl: ").append(toIndentedString(shareUrl)).append("\n");
    sb.append("    dimensione: ").append(toIndentedString(dimensione)).append("\n");
    sb.append("    formatoFile: ").append(toIndentedString(formatoFile)).append("\n");
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

