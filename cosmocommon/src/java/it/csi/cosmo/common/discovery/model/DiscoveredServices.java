/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Continene informazioni sulle istanze correntemente registrate
 */
public class DiscoveredServices implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646348472115108334L;

  private Set<DiscoveredService> services;

  public DiscoveredServices() {
    this.services = new HashSet<>();
  }

  public Set<DiscoveredService> getServices() {
    return services;
  }

  public void setServices(Set<DiscoveredService> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    return "DiscoveredServices [services=" + services + "]";
  }

}
