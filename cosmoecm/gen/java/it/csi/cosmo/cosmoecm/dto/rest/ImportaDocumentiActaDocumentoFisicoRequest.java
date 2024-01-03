/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ImportaDocumentiActaDocumentoFisicoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String codiceTipoDocumento = null;

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
  


  // nome originario nello yaml: codiceTipoDocumento 
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImportaDocumentiActaDocumentoFisicoRequest importaDocumentiActaDocumentoFisicoRequest = (ImportaDocumentiActaDocumentoFisicoRequest) o;
    return Objects.equals(id, importaDocumentiActaDocumentoFisicoRequest.id) &&
        Objects.equals(codiceTipoDocumento, importaDocumentiActaDocumentoFisicoRequest.codiceTipoDocumento);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, codiceTipoDocumento);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportaDocumentiActaDocumentoFisicoRequest {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
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

