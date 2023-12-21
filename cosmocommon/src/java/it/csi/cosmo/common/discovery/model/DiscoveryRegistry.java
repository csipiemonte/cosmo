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
import java.util.UUID;

/**
 * Continene informazioni sulle istanze correntemente registrate
 */
public class DiscoveryRegistry implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646348472115108334L;

  private String registryId;

  private Long version;

  private ZonedDateTime creationTime;

  private ZonedDateTime lastUpdateTime;

  private Set<DiscoveryRegistryServiceEntry> services;

  public DiscoveryRegistry() {
    this.registryId = UUID.randomUUID().toString();
    this.services = new HashSet<>();
    this.version = 0L;
    this.creationTime = ZonedDateTime.now();
    this.lastUpdateTime = this.creationTime;
  }

  public String getFullId() {
    return this.registryId + ":" + this.version;
  }

  public void markUpdated() {
    this.version++;
    this.lastUpdateTime = ZonedDateTime.now();
  }

  public Optional<DiscoveryRegistryServiceEntry> findService(String code) {
    return services.stream().filter(o -> code.equals(o.getConfiguration().getName())).findAny();
  }

  public String getRegistryId() {
    return registryId;
  }

  public void setRegistryId(String registryId) {
    this.registryId = registryId;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public ZonedDateTime getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(ZonedDateTime creationTime) {
    this.creationTime = creationTime;
  }

  public ZonedDateTime getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(ZonedDateTime lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Set<DiscoveryRegistryServiceEntry> getServices() {
    return services;
  }

  public void setServices(Set<DiscoveryRegistryServiceEntry> services) {
    this.services = services;
  }

  @Override
  public String toString() {
    return "DiscoveryRegistry [services=" + services + "]";
  }

}
