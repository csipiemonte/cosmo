/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.WSError;
import java.io.Serializable;
import javax.validation.constraints.*;

public class BaseOutputUnitaDocumentaria  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String wsResult = null;
  private WSError wsError = null;
  private String warningMessage = null;

  /**
   **/
  


  // nome originario nello yaml: wsResult 
  public String getWsResult() {
    return wsResult;
  }
  public void setWsResult(String wsResult) {
    this.wsResult = wsResult;
  }

  /**
   **/
  


  // nome originario nello yaml: wsError 
  public WSError getWsError() {
    return wsError;
  }
  public void setWsError(WSError wsError) {
    this.wsError = wsError;
  }

  /**
   **/
  


  // nome originario nello yaml: warningMessage 
  public String getWarningMessage() {
    return warningMessage;
  }
  public void setWarningMessage(String warningMessage) {
    this.warningMessage = warningMessage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseOutputUnitaDocumentaria baseOutputUnitaDocumentaria = (BaseOutputUnitaDocumentaria) o;
    return Objects.equals(wsResult, baseOutputUnitaDocumentaria.wsResult) &&
        Objects.equals(wsError, baseOutputUnitaDocumentaria.wsError) &&
        Objects.equals(warningMessage, baseOutputUnitaDocumentaria.warningMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wsResult, wsError, warningMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BaseOutputUnitaDocumentaria {\n");
    
    sb.append("    wsResult: ").append(toIndentedString(wsResult)).append("\n");
    sb.append("    wsError: ").append(toIndentedString(wsError)).append("\n");
    sb.append("    warningMessage: ").append(toIndentedString(warningMessage)).append("\n");
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

