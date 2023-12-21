/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import javax.ws.rs.BadRequestException;

/**
 *
 */

public enum DiscoveredInstanceStatus {

  //@formatter:off
  STARTING(true, 2), // "starting" status reported from instance
  UP(true, 10), // "up" status reported from instance
  TROUBLE(true, 8), // "trouble" status reported from instance
  DOWN(true, 1), // "down" status reported from instance (for instance on healthcheck failure)
  SHUTDOWN, // "shutdown" reported from instance
  NOT_REPORTING(true, 5), // the instance is not beating (in grace period) but was UP when latest seen
  FAILURE; // the instance is not beating (after grace period) and has been declared as failed
  //@formatter:on

  public static DiscoveredInstanceStatus fromReported(DiscoveryInstanceReportedStatus status) {
    if (status == null) {
      return null;
    }
    switch (status) {
      case STARTING:
        return DiscoveredInstanceStatus.STARTING;
      case UP:
        return DiscoveredInstanceStatus.UP;
      case TROUBLE:
        return DiscoveredInstanceStatus.TROUBLE;
      case DOWN:
        return DiscoveredInstanceStatus.DOWN;
      case SHUTDOWN:
        return DiscoveredInstanceStatus.SHUTDOWN;
      default:
        throw new BadRequestException("Unrecognized status " + status);
    }
  }

  private int servePriority = 1;
  private boolean canServeRequests = false;

  private DiscoveredInstanceStatus() {}

  private DiscoveredInstanceStatus(boolean canServeRequests, int servePriority) {
    this.servePriority = servePriority;
    this.canServeRequests = canServeRequests;
  }

  public int getServePriority() {
    return servePriority;
  }

  public boolean canServeRequests() {
    return canServeRequests;
  }

}
