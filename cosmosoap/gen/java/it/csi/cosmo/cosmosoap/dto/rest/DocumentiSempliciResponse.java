/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentiSempliciResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<DocumentoSemplice> documentiSemplici = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: documentiSemplici 
  public List<DocumentoSemplice> getDocumentiSemplici() {
    return documentiSemplici;
  }
  public void setDocumentiSemplici(List<DocumentoSemplice> documentiSemplici) {
    this.documentiSemplici = documentiSemplici;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentiSempliciResponse documentiSempliciResponse = (DocumentiSempliciResponse) o;
    return Objects.equals(documentiSemplici, documentiSempliciResponse.documentiSemplici);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentiSemplici);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentiSempliciResponse {\n");
    
    sb.append("    documentiSemplici: ").append(toIndentedString(documentiSemplici)).append("\n");
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

