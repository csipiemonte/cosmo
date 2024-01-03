/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmopratiche.dto.rest.PageInfo;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitore;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PraticheFruitoreResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<PraticheFruitore> praticheFruitore = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: praticheFruitore 
  public List<PraticheFruitore> getPraticheFruitore() {
    return praticheFruitore;
  }
  public void setPraticheFruitore(List<PraticheFruitore> praticheFruitore) {
    this.praticheFruitore = praticheFruitore;
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
    PraticheFruitoreResponse praticheFruitoreResponse = (PraticheFruitoreResponse) o;
    return Objects.equals(praticheFruitore, praticheFruitoreResponse.praticheFruitore) &&
        Objects.equals(pageInfo, praticheFruitoreResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(praticheFruitore, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PraticheFruitoreResponse {\n");
    
    sb.append("    praticheFruitore: ").append(toIndentedString(praticheFruitore)).append("\n");
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

