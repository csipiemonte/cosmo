/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;

/**
 * Continene informazioni su una singola istanza
 */
public class DiscoveredInstance implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115135334L;

  private DiscoveryRegistryInstanceEntry registryEntry;

  private DiscoveredInstanceStatus status;

  public DiscoveryRegistryInstanceEntry getRegistryEntry() {
    return registryEntry;
  }

  public void setRegistryEntry(DiscoveryRegistryInstanceEntry registryEntry) {
    this.registryEntry = registryEntry;
  }

  public DiscoveredInstanceStatus getStatus() {
    return status;
  }

  public void setStatus(DiscoveredInstanceStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "DiscoveredInstance [registryEntry=" + registryEntry + ", status=" + status + "]";
  }

}
