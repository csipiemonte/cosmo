/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.client;

import java.io.Serializable;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryServiceConfiguration;

/**
 *
 */

public class DiscoveryClientConfiguration implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = -47132840084545497L;

  private static final String DEFAULT_HEARTBEAT_ENDPOINT = "/api/discovery/heartbeat";

  private static final int DEFAULT_HEARTBEAT_INTERVAL = 10;

  private URI discoveryServerEndpoint;

  private DiscoveryInstanceConfiguration instanceConfiguration;

  private DiscoveryServiceConfiguration serviceConfiguration;

  private String username;

  private String password;

  public String getCombinedId() {
    if (this.instanceConfiguration != null && this.serviceConfiguration != null
        && !StringUtils.isEmpty(this.instanceConfiguration.getInstanceId())
        && !StringUtils.isEmpty(this.serviceConfiguration.getName())) {
      return this.serviceConfiguration.getName() + ":" + this.instanceConfiguration.getInstanceId();
    } else {
      return null;
    }
  }

  public DiscoveryClientConfiguration() {
    // NOP
  }

  private DiscoveryClientConfiguration(Builder builder) {
    this.discoveryServerEndpoint = builder.discoveryServerEndpoint;
    this.instanceConfiguration = builder.instanceConfiguration;
    this.serviceConfiguration = builder.serviceConfiguration;
    this.username = builder.username;
    this.password = builder.password;
  }

  public String getHeartbeatEndpoint() {
    return DEFAULT_HEARTBEAT_ENDPOINT;
  }

  public int getHeartbeatInterval() {
    return DEFAULT_HEARTBEAT_INTERVAL;
  }

  public URI getDiscoveryServerEndpoint() {
    return discoveryServerEndpoint;
  }

  public void setDiscoveryServerEndpoint(URI discoveryServerEndpoint) {
    this.discoveryServerEndpoint = discoveryServerEndpoint;
  }

  public DiscoveryInstanceConfiguration getInstanceConfiguration() {
    return instanceConfiguration;
  }

  public void setInstanceConfiguration(DiscoveryInstanceConfiguration instanceConfiguration) {
    this.instanceConfiguration = instanceConfiguration;
  }

  public DiscoveryServiceConfiguration getServiceConfiguration() {
    return serviceConfiguration;
  }

  public void setServiceConfiguration(DiscoveryServiceConfiguration serviceConfiguration) {
    this.serviceConfiguration = serviceConfiguration;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Creates builder to build {@link DiscoveryClientConfiguration}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryClientConfiguration}.
   */
  public static final class Builder {
    private URI discoveryServerEndpoint;
    private DiscoveryInstanceConfiguration instanceConfiguration;
    private DiscoveryServiceConfiguration serviceConfiguration;
    private String username;
    private String password;

    private Builder() {}

    public Builder withDiscoveryServerEndpoint(URI discoveryServerEndpoint) {
      this.discoveryServerEndpoint = discoveryServerEndpoint;
      return this;
    }

    public Builder withInstanceConfiguration(DiscoveryInstanceConfiguration instanceConfiguration) {
      this.instanceConfiguration = instanceConfiguration;
      return this;
    }

    public Builder withServiceConfiguration(DiscoveryServiceConfiguration serviceConfiguration) {
      this.serviceConfiguration = serviceConfiguration;
      return this;
    }

    public Builder withUsername(String username) {
      this.username = username;
      return this;
    }

    public Builder withPassword(String password) {
      this.password = password;
      return this;
    }

    public DiscoveryClientConfiguration build() {
      return new DiscoveryClientConfiguration(this);
    }
  }

}
