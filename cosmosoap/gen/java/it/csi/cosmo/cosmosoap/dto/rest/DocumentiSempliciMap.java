/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSempliceMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiSempliciMap  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DocumentoSempliceMap> documentiSempliciMap = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documentiSempliciMap 
  public List<DocumentoSempliceMap> getDocumentiSempliciMap() {
    return documentiSempliciMap;
  }
  public void setDocumentiSempliciMap(List<DocumentoSempliceMap> documentiSempliciMap) {
    this.documentiSempliciMap = documentiSempliciMap;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiSempliciMap documentiSempliciMap = (DocumentiSempliciMap) o;
    return Objects.equals(documentiSempliciMap, documentiSempliciMap.documentiSempliciMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentiSempliciMap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiSempliciMap {\n");
    
    sb.append("    documentiSempliciMap: ").append(toIndentedString(documentiSempliciMap)).append("\n");
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

