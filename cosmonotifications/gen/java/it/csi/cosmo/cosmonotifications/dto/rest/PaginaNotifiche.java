/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PaginaNotifiche  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Notifica> elementi = new ArrayList<>();
  private PageInfo info = null;
  private Integer totaleNonLette = null;

  /**
   **/
  


  // nome originario nello yaml: elementi 
  public List<Notifica> getElementi() {
    return elementi;
  }
  public void setElementi(List<Notifica> elementi) {
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

  /**
   **/
  


  // nome originario nello yaml: totaleNonLette 
  public Integer getTotaleNonLette() {
    return totaleNonLette;
  }
  public void setTotaleNonLette(Integer totaleNonLette) {
    this.totaleNonLette = totaleNonLette;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginaNotifiche paginaNotifiche = (PaginaNotifiche) o;
    return Objects.equals(elementi, paginaNotifiche.elementi) &&
        Objects.equals(info, paginaNotifiche.info) &&
        Objects.equals(totaleNonLette, paginaNotifiche.totaleNonLette);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elementi, info, totaleNonLette);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginaNotifiche {\n");
    
    sb.append("    elementi: ").append(toIndentedString(elementi)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
    sb.append("    totaleNonLette: ").append(toIndentedString(totaleNonLette)).append("\n");
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

