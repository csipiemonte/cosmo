/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.service;

import java.util.Set;
import it.csi.cosmo.common.discovery.model.DiscoveredService;

public interface DiscoveryFetchService {

  Set<DiscoveredService> getServices();
}
