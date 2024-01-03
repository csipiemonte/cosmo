/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoRequest;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CreaDocumentiRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<CreaDocumentoRequest> documenti = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documenti 
  public List<CreaDocumentoRequest> getDocumenti() {
    return documenti;
  }
  public void setDocumenti(List<CreaDocumentoRequest> documenti) {
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
    CreaDocumentiRequest creaDocumentiRequest = (CreaDocumentiRequest) o;
    return Objects.equals(documenti, creaDocumentiRequest.documenti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documenti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreaDocumentiRequest {\n");
    
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

