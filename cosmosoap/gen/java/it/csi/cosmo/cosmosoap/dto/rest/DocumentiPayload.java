/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiPayload  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String originalFilename = null;
  private byte[] originalContent = null;
  private String signedFilename = null;
  private byte[] signedContent = null;
  private Integer id = null;
  private Boolean primaFirma = false;

  /**
   **/
  


  // nome originario nello yaml: originalFilename 
  @NotNull
  public String getOriginalFilename() {
    return originalFilename;
  }
  public void setOriginalFilename(String originalFilename) {
    this.originalFilename = originalFilename;
  }

  /**
   **/
  


  // nome originario nello yaml: originalContent 
  @NotNull
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getOriginalContent() {
    return originalContent;
  }
  public void setOriginalContent(byte[] originalContent) {
    this.originalContent = originalContent;
  }

  /**
   **/
  


  // nome originario nello yaml: signedFilename 
  public String getSignedFilename() {
    return signedFilename;
  }
  public void setSignedFilename(String signedFilename) {
    this.signedFilename = signedFilename;
  }

  /**
   **/
  


  // nome originario nello yaml: signedContent 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getSignedContent() {
    return signedContent;
  }
  public void setSignedContent(byte[] signedContent) {
    this.signedContent = signedContent;
  }

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: primaFirma 
  @NotNull
  public Boolean isPrimaFirma() {
    return primaFirma;
  }
  public void setPrimaFirma(Boolean primaFirma) {
    this.primaFirma = primaFirma;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiPayload documentiPayload = (DocumentiPayload) o;
    return Objects.equals(originalFilename, documentiPayload.originalFilename) &&
        Objects.equals(originalContent, documentiPayload.originalContent) &&
        Objects.equals(signedFilename, documentiPayload.signedFilename) &&
        Objects.equals(signedContent, documentiPayload.signedContent) &&
        Objects.equals(id, documentiPayload.id) &&
        Objects.equals(primaFirma, documentiPayload.primaFirma);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originalFilename, originalContent, signedFilename, signedContent, id, primaFirma);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiPayload {\n");
    
    sb.append("    originalFilename: ").append(toIndentedString(originalFilename)).append("\n");
    sb.append("    originalContent: ").append(toIndentedString(originalContent)).append("\n");
    sb.append("    signedFilename: ").append(toIndentedString(signedFilename)).append("\n");
    sb.append("    signedContent: ").append(toIndentedString(signedContent)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    primaFirma: ").append(toIndentedString(primaFirma)).append("\n");
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

