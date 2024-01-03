/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class InfoFirmaFea  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String firmatario = null;
  private OffsetDateTime data = null;

  /**
   **/
  


  // nome originario nello yaml: firmatario 
  public String getFirmatario() {
    return firmatario;
  }
  public void setFirmatario(String firmatario) {
    this.firmatario = firmatario;
  }

  /**
   **/
  


  // nome originario nello yaml: data 
  public OffsetDateTime getData() {
    return data;
  }
  public void setData(OffsetDateTime data) {
    this.data = data;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfoFirmaFea infoFirmaFea = (InfoFirmaFea) o;
    return Objects.equals(firmatario, infoFirmaFea.firmatario) &&
        Objects.equals(data, infoFirmaFea.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firmatario, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfoFirmaFea {\n");
    
    sb.append("    firmatario: ").append(toIndentedString(firmatario)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

