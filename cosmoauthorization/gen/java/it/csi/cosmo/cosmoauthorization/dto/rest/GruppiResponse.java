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
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class GruppiResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Gruppo> gruppi = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: gruppi 
  public List<Gruppo> getGruppi() {
    return gruppi;
  }
  public void setGruppi(List<Gruppo> gruppi) {
    this.gruppi = gruppi;
  }

  /**
   **/
  


  // nome originario nello yaml: pageInfo 
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
    GruppiResponse gruppiResponse = (GruppiResponse) o;
    return Objects.equals(gruppi, gruppiResponse.gruppi) &&
        Objects.equals(pageInfo, gruppiResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gruppi, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GruppiResponse {\n");
    
    sb.append("    gruppi: ").append(toIndentedString(gruppi)).append("\n");
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

