/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaDocumentoRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ImportaDocumentiActaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long idPratica = null;
  private List<ImportaDocumentiActaDocumentoRequest> documenti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idPratica 
  public Long getIdPratica() {
    return idPratica;
  }
  public void setIdPratica(Long idPratica) {
    this.idPratica = idPratica;
  }

  /**
   **/
  


  // nome originario nello yaml: documenti 
  @NotNull
  public List<ImportaDocumentiActaDocumentoRequest> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<ImportaDocumentiActaDocumentoRequest> documenti) {
    this.documenti = documenti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImportaDocumentiActaRequest importaDocumentiActaRequest = (ImportaDocumentiActaRequest) o;
    return Objects.equals(idPratica, importaDocumentiActaRequest.idPratica) &&
        Objects.equals(documenti, importaDocumentiActaRequest.documenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, documenti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImportaDocumentiActaRequest {\n");
    
    sb.append("    idPratica: ").append(toIndentedString(idPratica)).append("\n");
    sb.append("    documenti: ").append(toIndentedString(documenti)).append("\n");
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

