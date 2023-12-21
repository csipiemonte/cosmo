/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ProfiloResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Profilo profilo = null;

  /**
   **/
  


  // nome originario nello yaml: profilo 
  public Profilo getProfilo() {
    return profilo;
  }
  public void setProfilo(Profilo profilo) {
    this.profilo = profilo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfiloResponse profiloResponse = (ProfiloResponse) o;
    return Objects.equals(profilo, profiloResponse.profilo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profilo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfiloResponse {\n");
    
    sb.append("    profilo: ").append(toIndentedString(profilo)).append("\n");
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

