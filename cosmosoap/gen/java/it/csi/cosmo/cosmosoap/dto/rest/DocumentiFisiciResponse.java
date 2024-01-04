/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiFisiciResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DocumentoFisico> documentiFisici = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documentiFisici 
  public List<DocumentoFisico> getDocumentiFisici() {
    return documentiFisici;
  }
  public void setDocumentiFisici(List<DocumentoFisico> documentiFisici) {
    this.documentiFisici = documentiFisici;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiFisiciResponse documentiFisiciResponse = (DocumentiFisiciResponse) o;
    return Objects.equals(documentiFisici, documentiFisiciResponse.documentiFisici);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentiFisici);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiFisiciResponse {\n");
    
    sb.append("    documentiFisici: ").append(toIndentedString(documentiFisici)).append("\n");
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

