/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Classificazione;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Classificazioni  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Classificazione> classificazioni = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: classificazioni 
  public List<Classificazione> getClassificazioni() {
    return classificazioni;
  }
  public void setClassificazioni(List<Classificazione> classificazioni) {
    this.classificazioni = classificazioni;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Classificazioni classificazioni = (Classificazioni) o;
    return Objects.equals(classificazioni, classificazioni.classificazioni);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classificazioni);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Classificazioni {\n");
    
    sb.append("    classificazioni: ").append(toIndentedString(classificazioni)).append("\n");
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

