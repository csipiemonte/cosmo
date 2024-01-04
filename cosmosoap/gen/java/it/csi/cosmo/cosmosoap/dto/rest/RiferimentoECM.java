/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RiferimentoECM  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String ecmUuid = null;

  /**
   **/
  


  // nome originario nello yaml: ecmUuid 
  @NotNull
  @Size(min=1,max=400)
  public String getEcmUuid() {
    return ecmUuid;
  }
  public void setEcmUuid(String ecmUuid) {
    this.ecmUuid = ecmUuid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiferimentoECM riferimentoECM = (RiferimentoECM) o;
    return Objects.equals(ecmUuid, riferimentoECM.ecmUuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ecmUuid);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiferimentoECM {\n");
    
    sb.append("    ecmUuid: ").append(toIndentedString(ecmUuid)).append("\n");
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

