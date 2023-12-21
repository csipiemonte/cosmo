/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.security.proxy.model;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

/**
 *
 */

public abstract class RouteMatchingHelper {

  private static AntPathMatcher matcher = new AntPathMatcher();

  private RouteMatchingHelper() {
    // NOP
  }

  public static ProxyRouteConfigurationMatch findMatchingRoute(ProxyConfiguration config,
      String path,
      HttpMethod method) {
    for (ProxyRouteConfiguration routeConfig : config.getRoutes()) {
      String matching = matches(routeConfig, path, method);
      if (matching != null) {
        return new ProxyRouteConfigurationMatch(routeConfig, matching);
      }
    }
    return null;
  }

  private static String matches(ProxyRouteConfiguration routeConfig, String path,
      HttpMethod method) {

    if (!(routeConfig.getMethods().isEmpty()
        || routeConfig.getMethods().stream().anyMatch(m -> m == method))) {
      return null;
    }

    return routeConfig.getMatchers().stream().filter(m -> matcher.match(m, path)).findFirst()
        .orElse(null);
  }

  public static class ProxyRouteConfigurationMatch {
    private ProxyRouteConfiguration routeConfig;
    private String matcher;

    public ProxyRouteConfigurationMatch(ProxyRouteConfiguration routeConfig, String matcher) {
      super();
      this.routeConfig = routeConfig;
      this.matcher = matcher;
    }

    public ProxyRouteConfiguration getRouteConfig() {
      return routeConfig;
    }

    public String getMatcher() {
      return matcher;
    }

  }

}
