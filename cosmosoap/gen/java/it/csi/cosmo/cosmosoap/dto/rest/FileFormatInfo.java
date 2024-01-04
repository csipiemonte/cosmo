/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FileFormatInfo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String description = null;
  private String formatVersion = null;
  private List<String> mimeType = new ArrayList<>();
  private String puid = null;
  private String typeExtension = null;
  private Boolean signed = false;
  private String signatureType = null;

  /**
   **/
  


  // nome originario nello yaml: description 
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  


  // nome originario nello yaml: formatVersion 
  public String getFormatVersion() {
    return formatVersion;
  }
  public void setFormatVersion(String formatVersion) {
    this.formatVersion = formatVersion;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  public List<String> getMimeType() {
    return mimeType;
  }
  public void setMimeType(List<String> mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: puid 
  public String getPuid() {
    return puid;
  }
  public void setPuid(String puid) {
    this.puid = puid;
  }

  /**
   **/
  


  // nome originario nello yaml: typeExtension 
  public String getTypeExtension() {
    return typeExtension;
  }
  public void setTypeExtension(String typeExtension) {
    this.typeExtension = typeExtension;
  }

  /**
   **/
  


  // nome originario nello yaml: signed 
  @NotNull
  public Boolean isSigned() {
    return signed;
  }
  public void setSigned(Boolean signed) {
    this.signed = signed;
  }

  /**
   **/
  


  // nome originario nello yaml: signatureType 
  public String getSignatureType() {
    return signatureType;
  }
  public void setSignatureType(String signatureType) {
    this.signatureType = signatureType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FileFormatInfo fileFormatInfo = (FileFormatInfo) o;
    return Objects.equals(description, fileFormatInfo.description) &&
        Objects.equals(formatVersion, fileFormatInfo.formatVersion) &&
        Objects.equals(mimeType, fileFormatInfo.mimeType) &&
        Objects.equals(puid, fileFormatInfo.puid) &&
        Objects.equals(typeExtension, fileFormatInfo.typeExtension) &&
        Objects.equals(signed, fileFormatInfo.signed) &&
        Objects.equals(signatureType, fileFormatInfo.signatureType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, formatVersion, mimeType, puid, typeExtension, signed, signatureType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FileFormatInfo {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    formatVersion: ").append(toIndentedString(formatVersion)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    puid: ").append(toIndentedString(puid)).append("\n");
    sb.append("    typeExtension: ").append(toIndentedString(typeExtension)).append("\n");
    sb.append("    signed: ").append(toIndentedString(signed)).append("\n");
    sb.append("    signatureType: ").append(toIndentedString(signatureType)).append("\n");
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

