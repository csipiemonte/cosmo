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

public class CreateUploadSessionResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String sessionUUID = null;

  /**
   **/
  


  // nome originario nello yaml: sessionUUID 
  @NotNull
  @Size(min=1,max=255)
  public String getSessionUUID() {
    return sessionUUID;
  }
  public void setSessionUUID(String sessionUUID) {
    this.sessionUUID = sessionUUID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUploadSessionResponse createUploadSessionResponse = (CreateUploadSessionResponse) o;
    return Objects.equals(sessionUUID, createUploadSessionResponse.sessionUUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionUUID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUploadSessionResponse {\n");
    
    sb.append("    sessionUUID: ").append(toIndentedString(sessionUUID)).append("\n");
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

