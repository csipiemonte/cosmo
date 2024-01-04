/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class IdentitaActaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<IdentitaActa> identitaActa = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: identitaActa 
  public List<IdentitaActa> getIdentitaActa() {
    return identitaActa;
  }
  public void setIdentitaActa(List<IdentitaActa> identitaActa) {
    this.identitaActa = identitaActa;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IdentitaActaResponse identitaActaResponse = (IdentitaActaResponse) o;
    return Objects.equals(identitaActa, identitaActaResponse.identitaActa);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identitaActa);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class IdentitaActaResponse {\n");
    
    sb.append("    identitaActa: ").append(toIndentedString(identitaActa)).append("\n");
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

