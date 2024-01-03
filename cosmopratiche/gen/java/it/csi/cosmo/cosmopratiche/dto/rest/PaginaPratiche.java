/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PaginaPratiche  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private PageInfo info = null;
  private List<Pratica> elementi = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: info 
  public PageInfo getInfo() {
    return info;
  }
  public void setInfo(PageInfo info) {
    this.info = info;
  }

  /**
   **/
  


  // nome originario nello yaml: elementi 
  public List<Pratica> getElementi() {
    return elementi;
  }
  public void setElementi(List<Pratica> elementi) {
    this.elementi = elementi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginaPratiche paginaPratiche = (PaginaPratiche) o;
    return Objects.equals(info, paginaPratiche.info) &&
        Objects.equals(elementi, paginaPratiche.elementi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(info, elementi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginaPratiche {\n");
    
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    elementi: ").append(toIndentedString(elementi)).append("\n");
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

