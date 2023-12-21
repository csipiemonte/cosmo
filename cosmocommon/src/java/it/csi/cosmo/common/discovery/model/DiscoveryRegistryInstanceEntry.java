/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Continene informazioni sulle istanze correntemente registrate
 */
public class DiscoveryRegistryInstanceEntry implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;

  private DiscoveryInstanceConfiguration configuration;

  private DiscoveryInstanceReportedStatus status;

  private ZonedDateTime firstDiscoveryTime;

  private ZonedDateTime lastHeartBeatTime;

  private DiscoveryHeartBeatRequest lastHeartBeat;

  public DiscoveryInstanceReportedStatus getStatus() {
    return status;
  }

  public void setStatus(DiscoveryInstanceReportedStatus status) {
    this.status = status;
  }

  public DiscoveryHeartBeatRequest getLastHeartBeat() {
    return lastHeartBeat;
  }

  public void setLastHeartBeat(DiscoveryHeartBeatRequest lastHeartBeat) {
    this.lastHeartBeat = lastHeartBeat;
  }

  public DiscoveryInstanceConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(DiscoveryInstanceConfiguration configuration) {
    this.configuration = configuration;
  }

  public ZonedDateTime getFirstDiscoveryTime() {
    return firstDiscoveryTime;
  }

  public void setFirstDiscoveryTime(ZonedDateTime firstDiscoveryTime) {
    this.firstDiscoveryTime = firstDiscoveryTime;
  }

  public ZonedDateTime getLastHeartBeatTime() {
    return lastHeartBeatTime;
  }

  public void setLastHeartBeatTime(ZonedDateTime lastHeartBeatTime) {
    this.lastHeartBeatTime = lastHeartBeatTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DiscoveryRegistryInstanceEntry other = (DiscoveryRegistryInstanceEntry) obj;
    if (configuration == null) {
      if (other.configuration != null) {
        return false;
      }
    } else if (!configuration.equals(other.configuration)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DiscoveryRegistryInstanceEntry [configuration=" + configuration + ", status=" + status + "]";
  }

}
