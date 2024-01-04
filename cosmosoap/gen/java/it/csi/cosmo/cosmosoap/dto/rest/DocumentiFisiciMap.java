/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisicoMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiFisiciMap  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DocumentoFisicoMap> documentiFisiciMap = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documentiFisiciMap 
  public List<DocumentoFisicoMap> getDocumentiFisiciMap() {
    return documentiFisiciMap;
  }
  public void setDocumentiFisiciMap(List<DocumentoFisicoMap> documentiFisiciMap) {
    this.documentiFisiciMap = documentiFisiciMap;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiFisiciMap documentiFisiciMap = (DocumentiFisiciMap) o;
    return Objects.equals(documentiFisiciMap, documentiFisiciMap.documentiFisiciMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentiFisiciMap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiFisiciMap {\n");
    
    sb.append("    documentiFisiciMap: ").append(toIndentedString(documentiFisiciMap)).append("\n");
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

