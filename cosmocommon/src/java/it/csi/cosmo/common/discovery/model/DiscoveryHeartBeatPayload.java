/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;

/**
 * Continene il payload per un singolo heartbeat
 */
public abstract class DiscoveryHeartBeatPayload implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;

  private DiscoveryInstanceConfiguration instance;

  private DiscoveryServiceConfiguration service;

  private DiscoveryInstanceReportedStatus status;

  public String getCombinedId() {
    if (this.instance != null && this.service != null
        && !StringUtils.isEmpty(this.instance.getInstanceId())
        && !StringUtils.isEmpty(this.service.getName())) {
      return this.service.getName() + ":" + this.instance.getInstanceId();
    } else {
      return null;
    }
  }

  public DiscoveryInstanceReportedStatus getStatus() {
    return status;
  }

  public void setStatus(DiscoveryInstanceReportedStatus status) {
    this.status = status;
  }

  public DiscoveryInstanceConfiguration getInstance() {
    return instance;
  }

  public void setInstance(DiscoveryInstanceConfiguration instance) {
    this.instance = instance;
  }

  public DiscoveryServiceConfiguration getService() {
    return service;
  }

  public void setService(DiscoveryServiceConfiguration service) {
    this.service = service;
  }

  @Override
  public String toString() {
    return "DiscoveryHeartBeat [instance=" + instance + ", service=" + service + ", status="
        + status + "]";
  }


}
