/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.VoceTitolarioActa;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VociTitolarioActa  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<VoceTitolarioActa> vociTitolario = new ArrayList<>();
  private PageInfo pageInfo = null;

  /**
   **/
  


  // nome originario nello yaml: vociTitolario 
  public List<VoceTitolarioActa> getVociTitolario() {
    return vociTitolario;
  }
  public void setVociTitolario(List<VoceTitolarioActa> vociTitolario) {
    this.vociTitolario = vociTitolario;
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
    VociTitolarioActa vociTitolarioActa = (VociTitolarioActa) o;
    return Objects.equals(vociTitolario, vociTitolarioActa.vociTitolario) &&
        Objects.equals(pageInfo, vociTitolarioActa.pageInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vociTitolario, pageInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VociTitolarioActa {\n");
    
    sb.append("    vociTitolario: ").append(toIndentedString(vociTitolario)).append("\n");
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

