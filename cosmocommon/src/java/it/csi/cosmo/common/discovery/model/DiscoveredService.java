/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Continene informazioni su un singolo service
 */
public class DiscoveredService implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115135334L;

  private DiscoveryRegistryServiceEntry registryEntry;

  private DiscoveredServiceStatus status;

  private Set<DiscoveredInstance> instances;

  public DiscoveredService() {
    this.instances = new HashSet<>();
  }

  public Set<DiscoveredInstance> getInstances() {
    return instances;
  }

  public void setInstances(Set<DiscoveredInstance> instances) {
    this.instances = instances;
  }

  public DiscoveredServiceStatus getStatus() {
    return status;
  }

  public void setStatus(DiscoveredServiceStatus status) {
    this.status = status;
  }

  public DiscoveryRegistryServiceEntry getRegistryEntry() {
    return registryEntry;
  }

  public void setRegistryEntry(DiscoveryRegistryServiceEntry registryEntry) {
    this.registryEntry = registryEntry;
  }

  @Override
  public String toString() {
    return "DiscoveredService [registryEntry=" + registryEntry + ", status=" + status + "]";
  }

}
