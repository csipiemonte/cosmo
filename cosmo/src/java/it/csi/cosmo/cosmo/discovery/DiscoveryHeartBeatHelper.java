/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.discovery;

import javax.ws.rs.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatPayload;
import it.csi.cosmo.common.discovery.model.DiscoveryInstanceConfiguration;
import it.csi.cosmo.common.discovery.model.DiscoveryServiceConfiguration;

/**
 *
 */

public interface DiscoveryHeartBeatHelper {

  public static void validateHeartBeatPayload(DiscoveryHeartBeatPayload payload) {
    if (payload == null) {
      throw new BadRequestException("Payload is required");
    }
    if (payload.getStatus() == null) {
      throw new BadRequestException("Instance status is required");
    }

    validateHeartBeatServiceConfiguration(payload.getService());
    validateHeartBeatInstanceConfiguration(payload.getInstance());
  }

  private static void validateHeartBeatServiceConfiguration(DiscoveryServiceConfiguration payload) {
    if (payload == null) {
      throw new BadRequestException("Service configuration is required");
    }
    if (StringUtils.isEmpty(payload.getName())) {
      throw new BadRequestException("Service configuration name is required");
    }
    if (StringUtils.isEmpty(payload.getRoute())) {
      throw new BadRequestException("Service configuration route is required");
    }
  }

  private static void validateHeartBeatInstanceConfiguration(
      DiscoveryInstanceConfiguration payload) {
    if (payload == null) {
      throw new BadRequestException("Instance configuration is required");
    }
    if (StringUtils.isEmpty(payload.getInstanceId())) {
      throw new BadRequestException("Instance ID is required");
    }
    if (payload.getLocation() == null) {
      throw new BadRequestException("Instance location is required");
    }
  }

}
