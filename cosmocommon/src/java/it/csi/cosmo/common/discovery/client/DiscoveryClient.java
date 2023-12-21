/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.discovery.client;

import java.io.Serializable;
import java.net.URI;
import java.security.InvalidParameterException;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceReportedStatus;
import it.csi.cosmo.common.discovery.model.DiscoveryServiceConfiguration;

/**
 *
 */

public class DiscoveryClient implements Serializable, AutoCloseable {

  /**
   *
   */
  private static final long serialVersionUID = 9090726473741200184L;

  private transient Logger logger;

  private DiscoveryClientHeartbeatHandler heartbeatHandler;

  private DiscoveryClientConfiguration configuration;

  private DiscoveryClient(Builder builder) {
    logger = Logger.getLogger(
        (builder != null ? builder.loggingPrefix : Constants.PRODUCT)
        + ".discovery.DiscoveryClient");
    logger.debug("initializing discovery client");

    if (builder != null) {
      if (null == builder.discoveryServerEndpoint) {
        throw new InvalidParameterException("discoveryServerEndpoint is required");
      }
      if (StringUtils.isEmpty(builder.username)) {
        throw new InvalidParameterException("username is required");
      }
      if (StringUtils.isEmpty(builder.password)) {
        throw new InvalidParameterException("password is required");
      }
      if (builder.serviceConfiguration == null) {
        throw new InvalidParameterException("serviceConfiguration is required");
      }
      if (builder.instanceConfiguration == null) {
        throw new InvalidParameterException("instanceConfiguration is required");
      }

      this.configuration = DiscoveryClientConfiguration.builder()
          .withDiscoveryServerEndpoint(builder.discoveryServerEndpoint)
          .withInstanceConfiguration(builder.instanceConfiguration)
          .withServiceConfiguration(builder.serviceConfiguration).withUsername(builder.username)
          .withPassword(builder.password).build();
    } else {
      this.configuration = DiscoveryClientConfiguration.builder().build();
    }

    this.heartbeatHandler = new DiscoveryClientHeartbeatHandler(this.configuration,
        builder != null ? builder.loggingPrefix : Constants.PRODUCT);
  }

  public DiscoveryClient() {
    this(null);
  }

  @Override
  public void close() throws Exception {
    if (this.heartbeatHandler != null) {
      heartbeatHandler.close();
    }
  }

  public boolean getHeartbeatStatus() {
    return heartbeatHandler.getHeartbeatStatus();
  }

  public DiscoveryClientHeartbeatHandler enableHeartbeat(
      Supplier<DiscoveryInstanceReportedStatus> statusProvider) {
    heartbeatHandler.enableHeartbeat(statusProvider);
    return heartbeatHandler;
  }

  public DiscoveryClientHeartbeatHandler disableHeartbeat() {
    heartbeatHandler.disableHeartbeat();
    return heartbeatHandler;
  }

  public Future<ResponseEntity<DiscoveryHeartBeatResponse>> sendHeartbeat(
      DiscoveryInstanceReportedStatus status) {
    return heartbeatHandler.scheduleHeartbeatWithShortDelay(status);
  }

  @Override
  public String toString() {
    return "DiscoveryClient [configuration=" + configuration + "]";
  }

  /**
   * Creates builder to build {@link DiscoveryClient}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DiscoveryClient}.
   */
  public static final class Builder {
    private URI discoveryServerEndpoint;
    private DiscoveryInstanceConfiguration instanceConfiguration;
    private DiscoveryServiceConfiguration serviceConfiguration;
    private String username;
    private String password;
    private String loggingPrefix;

    private Builder() {}

    public Builder withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

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

    public DiscoveryClient build() {
      return new DiscoveryClient(this);
    }
  }


}
