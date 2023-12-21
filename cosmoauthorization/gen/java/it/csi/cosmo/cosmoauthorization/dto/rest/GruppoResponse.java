/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import java.io.Serializable;
import javax.validation.constraints.*;

public class GruppoResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Gruppo gruppo = null;

  /**
   **/
  


  // nome originario nello yaml: gruppo 
  public Gruppo getGruppo() {
    return gruppo;
  }
  public void setGruppo(Gruppo gruppo) {
    this.gruppo = gruppo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GruppoResponse gruppoResponse = (GruppoResponse) o;
    return Objects.equals(gruppo, gruppoResponse.gruppo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gruppo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GruppoResponse {\n");
    
    sb.append("    gruppo: ").append(toIndentedString(gruppo)).append("\n");
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

