/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PaginaCommenti  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Commento> elementi = new ArrayList<>();
  private PageInfo info = null;

  /**
   **/
  


  // nome originario nello yaml: elementi 
  public List<Commento> getElementi() {
    return elementi;
  }
  public void setElementi(List<Commento> elementi) {
    this.elementi = elementi;
  }

  /**
   **/
  


  // nome originario nello yaml: info 
  public PageInfo getInfo() {
    return info;
  }
  public void setInfo(PageInfo info) {
    this.info = info;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginaCommenti paginaCommenti = (PaginaCommenti) o;
    return Objects.equals(elementi, paginaCommenti.elementi) &&
        Objects.equals(info, paginaCommenti.info);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementi, info);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginaCommenti {\n");
    
    sb.append("    elementi: ").append(toIndentedString(elementi)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
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

