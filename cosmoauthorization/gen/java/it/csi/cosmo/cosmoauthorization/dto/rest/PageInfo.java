/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import javax.validation.constraints.*;

public class PageInfo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Integer page = null;
  private Integer pageSize = null;
  private Integer totalElements = null;
  private Integer totalPages = null;

  /**
   **/
  


  // nome originario nello yaml: page 
  public Integer getPage() {
    return page;
  }
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   **/
  


  // nome originario nello yaml: pageSize 
  public Integer getPageSize() {
    return pageSize;
  }
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   **/
  


  // nome originario nello yaml: totalElements 
  public Integer getTotalElements() {
    return totalElements;
  }
  public void setTotalElements(Integer totalElements) {
    this.totalElements = totalElements;
  }

  /**
   **/
  


  // nome originario nello yaml: totalPages 
  public Integer getTotalPages() {
    return totalPages;
  }
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PageInfo pageInfo = (PageInfo) o;
    return Objects.equals(page, pageInfo.page) &&
        Objects.equals(pageSize, pageInfo.pageSize) &&
        Objects.equals(totalElements, pageInfo.totalElements) &&
        Objects.equals(totalPages, pageInfo.totalPages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, totalElements, totalPages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PageInfo {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
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

