/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreateUploadSessionRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String fileName = null;
  private String mimeType = null;
  private Long size = null;

  /**
   **/
  


  // nome originario nello yaml: fileName 
  @NotNull
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
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
  


  // nome originario nello yaml: size 
  @NotNull
  public Long getSize() {
    return size;
  }
  public void setSize(Long size) {
    this.size = size;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUploadSessionRequest createUploadSessionRequest = (CreateUploadSessionRequest) o;
    return Objects.equals(fileName, createUploadSessionRequest.fileName) &&
        Objects.equals(mimeType, createUploadSessionRequest.mimeType) &&
        Objects.equals(size, createUploadSessionRequest.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileName, mimeType, size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUploadSessionRequest {\n");
    
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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

