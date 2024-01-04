/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Result;
import java.io.Serializable;
import javax.validation.constraints.*;

public class SmistaDocumentoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Result result = null;
  private String messageUUID = null;

  /**
   **/
  


  // nome originario nello yaml: result 
  public Result getResult() {
    return result;
  }
  public void setResult(Result result) {
    this.result = result;
  }

  /**
   **/
  


  // nome originario nello yaml: messageUUID 
  @NotNull
  @Size(max=50)
  public String getMessageUUID() {
    return messageUUID;
  }
  public void setMessageUUID(String messageUUID) {
    this.messageUUID = messageUUID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmistaDocumentoResponse smistaDocumentoResponse = (SmistaDocumentoResponse) o;
    return Objects.equals(result, smistaDocumentoResponse.result) &&
        Objects.equals(messageUUID, smistaDocumentoResponse.messageUUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, messageUUID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SmistaDocumentoResponse {\n");
    
    sb.append("    result: ").append(toIndentedString(result)).append("\n");
    sb.append("    messageUUID: ").append(toIndentedString(messageUUID)).append("\n");
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

