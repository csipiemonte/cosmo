/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RilascioLockRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String codiceRisorsa = null;
  private String codiceOwner = null;

  /**
   **/
  


  // nome originario nello yaml: codiceRisorsa 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceRisorsa() {
    return codiceRisorsa;
  }
  public void setCodiceRisorsa(String codiceRisorsa) {
    this.codiceRisorsa = codiceRisorsa;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceOwner 
  @NotNull
  @Size(min=1,max=255)
  public String getCodiceOwner() {
    return codiceOwner;
  }
  public void setCodiceOwner(String codiceOwner) {
    this.codiceOwner = codiceOwner;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RilascioLockRequest rilascioLockRequest = (RilascioLockRequest) o;
    return Objects.equals(codiceRisorsa, rilascioLockRequest.codiceRisorsa) &&
        Objects.equals(codiceOwner, rilascioLockRequest.codiceOwner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceRisorsa, codiceOwner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RilascioLockRequest {\n");
    
    sb.append("    codiceRisorsa: ").append(toIndentedString(codiceRisorsa)).append("\n");
    sb.append("    codiceOwner: ").append(toIndentedString(codiceOwner)).append("\n");
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

