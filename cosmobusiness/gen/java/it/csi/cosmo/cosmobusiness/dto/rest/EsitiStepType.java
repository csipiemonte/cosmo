/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoStep;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsitiStepType  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<EsitoStep> esitoStep = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: esitoStep 
  @NotNull
  public List<EsitoStep> getEsitoStep() {
    return esitoStep;
  }
  public void setEsitoStep(List<EsitoStep> esitoStep) {
    this.esitoStep = esitoStep;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsitiStepType esitiStepType = (EsitiStepType) o;
    return Objects.equals(esitoStep, esitiStepType.esitoStep);
  }

  @Override
  public int hashCode() {
    return Objects.hash(esitoStep);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsitiStepType {\n");
    
    sb.append("    esitoStep: ").append(toIndentedString(esitoStep)).append("\n");
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

