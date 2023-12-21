/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CompleteUploadDocumentiZipSessionRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String hashes = null;
  private String folderName = null;

  /**
   **/
  


  // nome originario nello yaml: hashes 
  public String getHashes() {
    return hashes;
  }
  public void setHashes(String hashes) {
    this.hashes = hashes;
  }

  /**
   **/
  


  // nome originario nello yaml: folderName 
  @NotNull
  public String getFolderName() {
    return folderName;
  }
  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteUploadDocumentiZipSessionRequest completeUploadDocumentiZipSessionRequest = (CompleteUploadDocumentiZipSessionRequest) o;
    return Objects.equals(hashes, completeUploadDocumentiZipSessionRequest.hashes) &&
        Objects.equals(folderName, completeUploadDocumentiZipSessionRequest.folderName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashes, folderName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteUploadDocumentiZipSessionRequest {\n");
    
    sb.append("    hashes: ").append(toIndentedString(hashes)).append("\n");
    sb.append("    folderName: ").append(toIndentedString(folderName)).append("\n");
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

