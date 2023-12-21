/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.model;

import java.io.Serializable;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

/**
 * Continene la configurazione di una singola istanza
 */
public class DiscoveryInstanceConfiguration implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 8646345928115108334L;

  public static final int MINIMUM_HEARTBEAT_INTERVAL = 3000;

  public static final int STANDARD_HEARTBEAT_INTERVAL = 10000;

  private String instanceId = UUID.randomUUID().toString();

  private URI location;

  private Integer heartBeatInterval;

  public DiscoveryInstanceConfiguration() {
    // NOP
  }

  private DiscoveryInstanceConfiguration(Builder builder) {

    if (StringUtils.isEmpty(builder.instanceId)) {
      throw new InvalidParameterException("instance id is required");
    }
    if (builder.location == null) {
      throw new InvalidParameterException("instance location is required");
    }
    if (builder.heartBeatInterval == null) {
      throw new InvalidParameterException("instance heartBeatInterval is required");
    }

    if (builder.heartBeatInterval < MINIMUM_HEARTBEAT_INTERVAL) {
      throw new InvalidParameterException(
          "instance heartBeatInterval can't be less than " + MINIMUM_HEARTBEAT_INTERVAL);
    }

    this.instanceId = builder.instanceId;
    this.location = builder.location;
    this.heartBeatInterval = builder.heartBeatInterval;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public Integer getHeartBeatInterval() {
    return heartBeatInterval;
  }

  public void setHeartBeatInterval(Integer heartBeatInterval) {
    this.heartBeatInterval = heartBeatInterval;
  }


  public URI getLocation() {
    return location;
  }

  public void setLocation(URI location) {
    this.location = location;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((instanceId == null) ? 0 : instanceId.hashCode());
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
    DiscoveryInstanceConfiguration other = (DiscoveryInstanceConfiguration) obj;
    if (instanceId == null) {
      if (other.instanceId != null) {
        return false;
      }
    } else if (!instanceId.equals(other.instanceId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DiscoveryInstanceConfiguration [instanceId=" + instanceId + ", location=" + location
        + ", heartBeatInterval=" + heartBeatInterval + "]";
  }

  /**
   * Creates builder to build {@link DiscoveryInstanceConfiguration}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryInstanceConfiguration}.
   */
  public static final class Builder {
    private String instanceId = UUID.randomUUID().toString();
    private URI location;
    private Integer heartBeatInterval = STANDARD_HEARTBEAT_INTERVAL;

    private Builder() {}

    public Builder withInstanceId(String instanceId) {
      this.instanceId = instanceId;
      return this;
    }

    public Builder withLocation(URI location) {
      this.location = location;
      return this;
    }

    public Builder withHeartBeatInterval(Integer heartBeatInterval) {
      this.heartBeatInterval = heartBeatInterval;
      return this;
    }

    public DiscoveryInstanceConfiguration build() {
      return new DiscoveryInstanceConfiguration(this);
    }
  }

}
