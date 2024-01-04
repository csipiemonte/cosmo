/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentoSempliceMap  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private DocumentoSemplice documentoSemplice = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: documentoSemplice 
  public DocumentoSemplice getDocumentoSemplice() {
    return documentoSemplice;
  }
  public void setDocumentoSemplice(DocumentoSemplice documentoSemplice) {
    this.documentoSemplice = documentoSemplice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoSempliceMap documentoSempliceMap = (DocumentoSempliceMap) o;
    return Objects.equals(id, documentoSempliceMap.id) &&
        Objects.equals(documentoSemplice, documentoSempliceMap.documentoSemplice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, documentoSemplice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoSempliceMap {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    documentoSemplice: ").append(toIndentedString(documentoSemplice)).append("\n");
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

