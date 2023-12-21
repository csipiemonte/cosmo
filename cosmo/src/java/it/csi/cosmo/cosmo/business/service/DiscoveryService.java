/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.service;

import it.csi.cosmo.common.discovery.model.DiscoveredServices;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatRequest;
import it.csi.cosmo.common.discovery.model.DiscoveryHeartBeatResponse;
import it.csi.cosmo.common.discovery.model.DiscoveryRegistry;

/**
 * Servizio per la gestione del service discovery
 */
public interface DiscoveryService {

  DiscoveredServices getDiscoveredServices();

  DiscoveryHeartBeatResponse processHeartBeat(DiscoveryHeartBeatRequest request);

  DiscoveryRegistry getRegistry();

}
