/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Continene informazioni su un singolo service
 */
public class DiscoveryRegistryServiceEntry implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;

  private DiscoveryServiceConfiguration configuration;

  private ZonedDateTime firstDiscoveryTime;

  private ZonedDateTime lastHeartBeatTime;

  private DiscoveryHeartBeatRequest lastHeartBeat;

  private Set<DiscoveryRegistryInstanceEntry> instances;

  public DiscoveryRegistryServiceEntry() {
    this.instances = new HashSet<>();
  }

  public Optional<DiscoveryRegistryInstanceEntry> findInstance(String code) {
    return instances.stream().filter(o -> code.equals(o.getConfiguration().getInstanceId()))
        .findAny();
  }

  public Set<DiscoveryRegistryInstanceEntry> getInstances() {
    return instances;
  }

  public void setInstances(Set<DiscoveryRegistryInstanceEntry> instances) {
    this.instances = instances;
  }

  public DiscoveryServiceConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(DiscoveryServiceConfiguration configuration) {
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

  public DiscoveryHeartBeatRequest getLastHeartBeat() {
    return lastHeartBeat;
  }

  public void setLastHeartBeat(DiscoveryHeartBeatRequest lastHeartBeat) {
    this.lastHeartBeat = lastHeartBeat;
  }

  @Override
  public String toString() {
    return "DiscoveryRegistryServiceEntry [configuration=" + configuration + ", firstDiscoveryTime="
        + firstDiscoveryTime + ", lastHeartBeatTime=" + lastHeartBeatTime + ", lastHeartBeat="
        + lastHeartBeat + ", instances=" + instances + "]";
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
    DiscoveryRegistryServiceEntry other = (DiscoveryRegistryServiceEntry) obj;
    if (configuration == null) {
      if (other.configuration != null) {
        return false;
      }
    } else if (!configuration.equals(other.configuration)) {
      return false;
    }
    return true;
  }

}
