/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security.proxy.model;

/**
 *
 */

public abstract class AbstractProxyConfigurationProvider implements ProxyConfigurationProvider {

  private ProxyConfiguration cached = null;

  @Override
  public ProxyConfiguration getConfiguration() {
    if (this.cached == null) {
      ProxyConfiguration conf = ProxyConfiguration.configure();
      this.configure(conf);
      this.cached = conf;
    }
    return this.cached;
  }
}
