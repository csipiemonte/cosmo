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

public class GetElaborazionePraticaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String mappatura = null;

  /**
   **/
  


  // nome originario nello yaml: mappatura 
  public String getMappatura() {
    return mappatura;
  }
  public void setMappatura(String mappatura) {
    this.mappatura = mappatura;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetElaborazionePraticaRequest getElaborazionePraticaRequest = (GetElaborazionePraticaRequest) o;
    return Objects.equals(mappatura, getElaborazionePraticaRequest.mappatura);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mappatura);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetElaborazionePraticaRequest {\n");
    
    sb.append("    mappatura: ").append(toIndentedString(mappatura)).append("\n");
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

