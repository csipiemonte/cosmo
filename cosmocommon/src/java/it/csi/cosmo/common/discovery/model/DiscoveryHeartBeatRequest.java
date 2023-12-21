/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;


/**
 * Continene il payload per un singolo heartbeat
 */
public class DiscoveryHeartBeatRequest extends DiscoveryHeartBeatPayload {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108335L;

  public DiscoveryHeartBeatRequest() {
    // NOP
  }

  private DiscoveryHeartBeatRequest(Builder builder) {
    this.setInstance(builder.instance);
    this.setService(builder.service);
    this.setStatus(builder.status);
  }

  @Override
  public String toString() {
    return "DiscoveryHeartBeatRequest [payload=" + super.toString() + "]";
  }

  /**
   * Creates builder to build {@link DiscoveryHeartBeatRequest}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryHeartBeatRequest}.
   */
  public static final class Builder {
    private DiscoveryInstanceConfiguration instance;
    private DiscoveryServiceConfiguration service;
    private DiscoveryInstanceReportedStatus status;

    private Builder() {}

    public Builder withInstance(DiscoveryInstanceConfiguration instance) {
      this.instance = instance;
      return this;
    }

    public Builder withService(DiscoveryServiceConfiguration service) {
      this.service = service;
      return this;
    }

    public Builder withStatus(DiscoveryInstanceReportedStatus status) {
      this.status = status;
      return this;
    }

    public DiscoveryHeartBeatRequest build() {
      return new DiscoveryHeartBeatRequest(this);
    }
  }
}
