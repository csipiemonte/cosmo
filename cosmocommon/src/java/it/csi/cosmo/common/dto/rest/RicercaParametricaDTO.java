/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */

public class RicercaParametricaDTO implements Serializable {

  private static final long serialVersionUID = -6707774343049892677L;

  private Map<String, Map<String, String>> filter;
  private String sort;
  private String fields;
  private Integer page;
  private Integer size;
  private Integer limit;
  private Integer offset;

  public Map<String, Map<String, String>> getFilter() {
    return filter;
  }

  public void setFilter(Map<String, Map<String, String>> filter) {
    this.filter = filter;
  }

  public String getSort() {
    return sort;
  }

  public void setSort(String sort) {
    this.sort = sort;
  }

  public String getFields() {
    return fields;
  }

  public void setFields(String fields) {
    this.fields = fields;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }


}
