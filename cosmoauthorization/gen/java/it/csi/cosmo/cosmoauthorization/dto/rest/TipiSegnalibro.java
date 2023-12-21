/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoSegnalibro;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class TipiSegnalibro  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<TipoSegnalibro> tipiSegnalibro = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: tipiSegnalibro 
  public List<TipoSegnalibro> getTipiSegnalibro() {
    return tipiSegnalibro;
  }
  public void setTipiSegnalibro(List<TipoSegnalibro> tipiSegnalibro) {
    this.tipiSegnalibro = tipiSegnalibro;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipiSegnalibro tipiSegnalibro = (TipiSegnalibro) o;
    return Objects.equals(tipiSegnalibro, tipiSegnalibro.tipiSegnalibro);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipiSegnalibro);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipiSegnalibro {\n");
    
    sb.append("    tipiSegnalibro: ").append(toIndentedString(tipiSegnalibro)).append("\n");
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

