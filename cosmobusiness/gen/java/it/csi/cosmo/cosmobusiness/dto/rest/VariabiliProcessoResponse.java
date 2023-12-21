/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VariabiliProcessoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String processInstanceId = null;
  private List<VariabileProcesso> variabili = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: processInstanceId 
  public String getProcessInstanceId() {
    return processInstanceId;
  }
  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  /**
   **/
  


  // nome originario nello yaml: variabili 
  public List<VariabileProcesso> getVariabili() {
    return variabili;
  }
  public void setVariabili(List<VariabileProcesso> variabili) {
    this.variabili = variabili;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VariabiliProcessoResponse variabiliProcessoResponse = (VariabiliProcessoResponse) o;
    return Objects.equals(processInstanceId, variabiliProcessoResponse.processInstanceId) &&
        Objects.equals(variabili, variabiliProcessoResponse.variabili);
  }

  @Override
  public int hashCode() {
    return Objects.hash(processInstanceId, variabili);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VariabiliProcessoResponse {\n");
    
    sb.append("    processInstanceId: ").append(toIndentedString(processInstanceId)).append("\n");
    sb.append("    variabili: ").append(toIndentedString(variabili)).append("\n");
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

