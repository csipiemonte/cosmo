/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.dto.discovery;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import it.csi.cosmo.common.discovery.model.DiscoveredServices;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;

/**
 *
 */

public class CachedDiscoveryServices {

  private OffsetDateTime lastFetchTime;

  private OffsetDateTime lastUpdateTime;

  private DiscoveryRegistry registry;

  private DiscoveredServices analyzedResults;

  public CachedDiscoveryServices() {
    // nop
  }

  public Long getAgeInSeconds() {
    if (!hasData() || lastFetchTime == null) {
      return null;
    }

    return lastFetchTime.until(OffsetDateTime.now(), ChronoUnit.SECONDS);
  }

  public boolean hasData() {
    return registry != null && registry.getFullId() != null && registry.getFullId().length() >= 3
        && analyzedResults != null;
  }

  public OffsetDateTime getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(OffsetDateTime lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  public OffsetDateTime getLastFetchTime() {
    return lastFetchTime;
  }

  public void setLastFetchTime(OffsetDateTime lastFetchTime) {
    this.lastFetchTime = lastFetchTime;
  }

  public DiscoveryRegistry getRegistry() {
    return registry;
  }

  public void setRegistry(DiscoveryRegistry registry) {
    this.registry = registry;
  }

  public DiscoveredServices getAnalyzedResults() {
    return analyzedResults;
  }

  public void setAnalyzedResults(DiscoveredServices analyzedResults) {
    this.analyzedResults = analyzedResults;
  }

}
