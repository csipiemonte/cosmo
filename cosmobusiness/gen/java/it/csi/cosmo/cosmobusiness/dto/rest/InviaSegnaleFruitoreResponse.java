/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InviaSegnaleFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceInvio = null;

  /**
   **/
  


  // nome originario nello yaml: codiceInvio 
  @NotNull
  public String getCodiceInvio() {
    return codiceInvio;
  }
  public void setCodiceInvio(String codiceInvio) {
    this.codiceInvio = codiceInvio;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviaSegnaleFruitoreResponse inviaSegnaleFruitoreResponse = (InviaSegnaleFruitoreResponse) o;
    return Objects.equals(codiceInvio, inviaSegnaleFruitoreResponse.codiceInvio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceInvio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviaSegnaleFruitoreResponse {\n");
    
    sb.append("    codiceInvio: ").append(toIndentedString(codiceInvio)).append("\n");
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

