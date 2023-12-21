/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;

/**
 * Continene la response per un singolo heartbeat
 */
public class DiscoveryHeartBeatResponse implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;

  private DiscoveryInstanceReportedStatus status;

  public DiscoveryHeartBeatResponse() {
    // NOP
  }

  private DiscoveryHeartBeatResponse(Builder builder) {
    this.status = builder.status;
  }

  public DiscoveryInstanceReportedStatus getStatus() {
    return status;
  }

  public void setStatus(DiscoveryInstanceReportedStatus status) {
    this.status = status;
  }

  /**
   * Creates builder to build {@link DiscoveryHeartBeatResponse}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryHeartBeatResponse}.
   */
  public static final class Builder {
    private DiscoveryInstanceReportedStatus status;

    private Builder() {}

    public Builder withStatus(DiscoveryInstanceReportedStatus status) {
      this.status = status;
      return this;
    }

    public DiscoveryHeartBeatResponse build() {
      return new DiscoveryHeartBeatResponse(this);
    }
  }

}
