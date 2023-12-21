/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.PageInfo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ProfiliResponse  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<Profilo> profili = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: profili 
  public List<Profilo> getProfili() {
    return profili;
  }
  public void setProfili(List<Profilo> profili) {
    this.profili = profili;
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
    ProfiliResponse profiliResponse = (ProfiliResponse) o;
    return Objects.equals(profili, profiliResponse.profili) &&
        Objects.equals(pageInfo, profiliResponse.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profili, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfiliResponse {\n");
    
    sb.append("    profili: ").append(toIndentedString(profili)).append("\n");
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

