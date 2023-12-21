/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InviaSegnaleProcessoRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceSegnale = null;

  /**
   **/
  


  // nome originario nello yaml: codiceSegnale 
  @NotNull
  public String getCodiceSegnale() {
    return codiceSegnale;
  }
  public void setCodiceSegnale(String codiceSegnale) {
    this.codiceSegnale = codiceSegnale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviaSegnaleProcessoRequest inviaSegnaleProcessoRequest = (InviaSegnaleProcessoRequest) o;
    return Objects.equals(codiceSegnale, inviaSegnaleProcessoRequest.codiceSegnale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceSegnale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviaSegnaleProcessoRequest {\n");
    
    sb.append("    codiceSegnale: ").append(toIndentedString(codiceSegnale)).append("\n");
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

