/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import java.io.Serializable;

/**
 *
 */

public class FormDeploymentWrapper implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 388687439723199404L;
  private String id;
  private String name;
  private String parentDeploymentId;
  private String tenantId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getParentDeploymentId() {
    return parentDeploymentId;
  }

  public void setParentDeploymentId(String parentDeploymentId) {
    this.parentDeploymentId = parentDeploymentId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }


}
