/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.Parametro;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ParametriResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Parametro> parametri = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: parametri 
  public List<Parametro> getParametri() {
    return parametri;
  }
  public void setParametri(List<Parametro> parametri) {
    this.parametri = parametri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParametriResponse parametriResponse = (ParametriResponse) o;
    return Objects.equals(parametri, parametriResponse.parametri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parametri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParametriResponse {\n");
    
    sb.append("    parametri: ").append(toIndentedString(parametri)).append("\n");
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

