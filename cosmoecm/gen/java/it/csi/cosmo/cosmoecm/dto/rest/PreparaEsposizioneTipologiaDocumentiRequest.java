/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PreparaEsposizioneTipologiaDocumentiRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceTipoDocumento = null;
  private Long durata = null;

  /**
   **/
  


  // nome originario nello yaml: codiceTipoDocumento 
  public String getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }
  public void setCodiceTipoDocumento(String codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: durata 
  public Long getDurata() {
    return durata;
  }
  public void setDurata(Long durata) {
    this.durata = durata;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreparaEsposizioneTipologiaDocumentiRequest preparaEsposizioneTipologiaDocumentiRequest = (PreparaEsposizioneTipologiaDocumentiRequest) o;
    return Objects.equals(codiceTipoDocumento, preparaEsposizioneTipologiaDocumentiRequest.codiceTipoDocumento) &&
        Objects.equals(durata, preparaEsposizioneTipologiaDocumentiRequest.durata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceTipoDocumento, durata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PreparaEsposizioneTipologiaDocumentiRequest {\n");
    
    sb.append("    codiceTipoDocumento: ").append(toIndentedString(codiceTipoDocumento)).append("\n");
    sb.append("    durata: ").append(toIndentedString(durata)).append("\n");
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

