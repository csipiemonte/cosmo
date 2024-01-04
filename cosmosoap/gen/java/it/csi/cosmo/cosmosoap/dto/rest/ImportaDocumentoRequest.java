/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.ImportaDocumentoFisicoRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ImportaDocumentoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private List<ImportaDocumentoFisicoRequest> documentiFisici = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  @NotNull
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: documentiFisici 
  @NotNull
  public List<ImportaDocumentoFisicoRequest> getDocumentiFisici() {
    return documentiFisici;
  }
  public void setDocumentiFisici(List<ImportaDocumentoFisicoRequest> documentiFisici) {
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
    ImportaDocumentoRequest importaDocumentoRequest = (ImportaDocumentoRequest) o;
    return Objects.equals(id, importaDocumentoRequest.id) &&
        Objects.equals(documentiFisici, importaDocumentoRequest.documentiFisici);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentiFisici);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportaDocumentoRequest {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

