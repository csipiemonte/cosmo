/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest.impl;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.discovery.model.DiscoveredService;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatRequest;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;
import it.csi.cosmo.cosmo.business.rest.DiscoveryApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.DiscoveryService;


public class DiscoveryApiImpl extends ParentApiImpl implements DiscoveryApi {

  @Autowired
  public DiscoveryService discoveryService;

  @Override
  public DiscoveryHeartBeatResponse sendHeartBeat(DiscoveryHeartBeatRequest request) {

    return discoveryService.processHeartBeat(request);
  }

  @Override
  public Set<DiscoveredService> getServices() {

    return discoveryService.getDiscoveredServices().getServices();
  }

  @Override
  public DiscoveryRegistry getRegistry() {

    return discoveryService.getRegistry();
  }

}
