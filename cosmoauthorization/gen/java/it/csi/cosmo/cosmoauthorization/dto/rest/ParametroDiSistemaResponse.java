/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ParametroDiSistemaResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<ParametroDiSistema> parametriDiSistema = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: parametriDiSistema 
  @NotNull
  public List<ParametroDiSistema> getParametriDiSistema() {
    return parametriDiSistema;
  }
  public void setParametriDiSistema(List<ParametroDiSistema> parametriDiSistema) {
    this.parametriDiSistema = parametriDiSistema;
  }

  /**
   **/
  


  // nome originario nello yaml: pageInfo 
  @NotNull
  public PageInfo getPageInfo() {
    return pageInfo;
  }
  public void setPageInfo(PageInfo pageInfo) {
    this.pageInfo = pageInfo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParametroDiSistemaResponse parametroDiSistemaResponse = (ParametroDiSistemaResponse) o;
    return Objects.equals(parametriDiSistema, parametroDiSistemaResponse.parametriDiSistema) &&
        Objects.equals(pageInfo, parametroDiSistemaResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parametriDiSistema, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParametroDiSistemaResponse {\n");
    
    sb.append("    parametriDiSistema: ").append(toIndentedString(parametriDiSistema)).append("\n");
    sb.append("    pageInfo: ").append(toIndentedString(pageInfo)).append("\n");
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

