/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.Metadato;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Metadati  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Metadato> metadato = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: metadato 
  @NotNull
  @Size(min=1)
  public List<Metadato> getMetadato() {
    return metadato;
  }
  public void setMetadato(List<Metadato> metadato) {
    this.metadato = metadato;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metadati metadati = (Metadati) o;
    return Objects.equals(metadato, metadati.metadato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Metadati {\n");
    
    sb.append("    metadato: ").append(toIndentedString(metadato)).append("\n");
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

