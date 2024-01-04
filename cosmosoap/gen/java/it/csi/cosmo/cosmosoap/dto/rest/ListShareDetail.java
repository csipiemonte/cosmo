/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.ShareDetail;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ListShareDetail  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private List<ShareDetail> listShareDetail = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: listShareDetail 
  public List<ShareDetail> getListShareDetail() {
    return listShareDetail;
  }
  public void setListShareDetail(List<ShareDetail> listShareDetail) {
    this.listShareDetail = listShareDetail;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ListShareDetail listShareDetail = (ListShareDetail) o;
    return Objects.equals(listShareDetail, listShareDetail.listShareDetail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(listShareDetail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ListShareDetail {\n");
    
    sb.append("    listShareDetail: ").append(toIndentedString(listShareDetail)).append("\n");
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

