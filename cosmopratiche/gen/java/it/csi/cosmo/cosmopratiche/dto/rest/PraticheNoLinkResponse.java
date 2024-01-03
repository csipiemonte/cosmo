/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PraticheNoLinkResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Pratica> pratiche = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: pratiche 
  public List<Pratica> getPratiche() {
    return pratiche;
  }
  public void setPratiche(List<Pratica> pratiche) {
    this.pratiche = pratiche;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PraticheNoLinkResponse praticheNoLinkResponse = (PraticheNoLinkResponse) o;
    return Objects.equals(pratiche, praticheNoLinkResponse.pratiche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pratiche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PraticheNoLinkResponse {\n");
    
    sb.append("    pratiche: ").append(toIndentedString(pratiche)).append("\n");
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

