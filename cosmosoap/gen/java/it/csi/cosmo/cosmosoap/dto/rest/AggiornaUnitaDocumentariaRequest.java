/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmosoap.dto.rest.AttributoUnitaDocumentaria;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class AggiornaUnitaDocumentariaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String idUd = null;
  private String idDocumento = null;
  private List<AttributoUnitaDocumentaria> attributi = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: idUd 
  @NotNull
  public String getIdUd() {
    return idUd;
  }
  public void setIdUd(String idUd) {
    this.idUd = idUd;
  }

  /**
   **/
  


  // nome originario nello yaml: idDocumento 
  public String getIdDocumento() {
    return idDocumento;
  }
  public void setIdDocumento(String idDocumento) {
    this.idDocumento = idDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: attributi 
  public List<AttributoUnitaDocumentaria> getAttributi() {
    return attributi;
  }
  public void setAttributi(List<AttributoUnitaDocumentaria> attributi) {
    this.attributi = attributi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggiornaUnitaDocumentariaRequest aggiornaUnitaDocumentariaRequest = (AggiornaUnitaDocumentariaRequest) o;
    return Objects.equals(idUd, aggiornaUnitaDocumentariaRequest.idUd) &&
        Objects.equals(idDocumento, aggiornaUnitaDocumentariaRequest.idDocumento) &&
        Objects.equals(attributi, aggiornaUnitaDocumentariaRequest.attributi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUd, idDocumento, attributi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggiornaUnitaDocumentariaRequest {\n");
    
    sb.append("    idUd: ").append(toIndentedString(idUd)).append("\n");
    sb.append("    idDocumento: ").append(toIndentedString(idDocumento)).append("\n");
    sb.append("    attributi: ").append(toIndentedString(attributi)).append("\n");
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

