/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.business.service;

import java.net.URI;

public interface DiscoveryDeclarativeFallbackService {

  URI getLocationWithFallbackDiscovery(String urlSegment);

  /**
   * @return
   */
  boolean isEnabled();

}
