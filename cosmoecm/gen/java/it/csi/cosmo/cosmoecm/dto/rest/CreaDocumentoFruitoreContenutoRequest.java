/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaDocumentoFruitoreContenutoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String nomeFile = null;
  private String mimeType = null;
  private String contenutoFisico = null;

  /**
   **/
  


  // nome originario nello yaml: nomeFile 
  @NotNull
  @Size(min=1,max=255)
  public String getNomeFile() {
    return nomeFile;
  }
  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  @Size(min=1,max=255)
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: contenutoFisico 
  @NotNull
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public String getContenutoFisico() {
    return contenutoFisico;
  }
  public void setContenutoFisico(String contenutoFisico) {
    this.contenutoFisico = contenutoFisico;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreaDocumentoFruitoreContenutoRequest creaDocumentoFruitoreContenutoRequest = (CreaDocumentoFruitoreContenutoRequest) o;
    return Objects.equals(nomeFile, creaDocumentoFruitoreContenutoRequest.nomeFile) &&
        Objects.equals(mimeType, creaDocumentoFruitoreContenutoRequest.mimeType) &&
        Objects.equals(contenutoFisico, creaDocumentoFruitoreContenutoRequest.contenutoFisico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nomeFile, mimeType, contenutoFisico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentoFruitoreContenutoRequest {\n");
    
    sb.append("    nomeFile: ").append(toIndentedString(nomeFile)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    contenutoFisico: ").append(toIndentedString(contenutoFisico)).append("\n");
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

