/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security.proxy.model;

/**
 *
 */

public interface ProxyConfigurationProvider {

  void configure(ProxyConfiguration config);

  ProxyConfiguration getConfiguration();
}
