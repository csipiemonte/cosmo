/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import javax.ws.rs.BadRequestException;

/**
 *
 */

public enum DiscoveredServiceStatus {

  //@formatter:off
  UNAVAILABLE, // no instances available (all removed, for instance)
  STARTING,
  UP(true), // at least one instance in "up" status
  TROUBLE(true),
  DOWN,
  SHUTDOWN,
  NOT_REPORTING(true),
  FAILURE;
  //@formatter:on

  public static DiscoveredServiceStatus fromInstanceStatus(DiscoveredInstanceStatus status) {
    if (status == null) {
      return null;
    }
    switch (status) {
      case STARTING:
        return DiscoveredServiceStatus.STARTING;
      case UP:
        return DiscoveredServiceStatus.UP;
      case TROUBLE:
        return DiscoveredServiceStatus.TROUBLE;
      case DOWN:
        return DiscoveredServiceStatus.DOWN;
      case SHUTDOWN:
        return DiscoveredServiceStatus.SHUTDOWN;
      case NOT_REPORTING:
        return DiscoveredServiceStatus.NOT_REPORTING;
      case FAILURE:
        return DiscoveredServiceStatus.FAILURE;
      default:
        throw new BadRequestException("Unrecognized status " + status);
    }
  }

  private boolean canServeRequests = false;

  private DiscoveredServiceStatus() {}

  private DiscoveredServiceStatus(boolean canServeRequests) {
    this.canServeRequests = canServeRequests;
  }

  public boolean canServeRequests() {
    return canServeRequests;
  }

}
