/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto.rest;

import java.io.Serializable;
import java.util.List;
import org.flowable.rest.service.api.runtime.task.TaskResponse;

/**
 *
 */

public class TaskResponseWrapper implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 388687439723199404L;
  private List<TaskResponse> data;
  private Long total;
  private Long start;
  private String sort;
  private String order;
  private Long size;
  public List<TaskResponse> getData() {
    return data;
  }
  public void setData(List<TaskResponse> data) {
    this.data = data;
  }
  public Long getTotal() {
    return total;
  }
  public void setTotal(Long total) {
    this.total = total;
  }
  public Long getStart() {
    return start;
  }
  public void setStart(Long start) {
    this.start = start;
  }
  public String getSort() {
    return sort;
  }
  public void setSort(String sort) {
    this.sort = sort;
  }
  public String getOrder() {
    return order;
  }
  public void setOrder(String order) {
    this.order = order;
  }
  public Long getSize() {
    return size;
  }
  public void setSize(Long size) {
    this.size = size;
  }

}
