/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security.proxy.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */

public class ProxyConfiguration implements RouteMatcher<ProxyRouteConfiguration> {

  private List<ProxyRouteConfiguration> routes = new LinkedList<>();

  public static ProxyConfiguration configure() {
    return new ProxyConfiguration();
  }

  private ProxyConfiguration() {
    // NOP
  }

  public List<ProxyRouteConfiguration> getRoutes() {
    return routes;
  }

  @Override
  public ProxyRouteConfiguration antMatchers(String matcher) {

    List<String> matchers = new LinkedList<>();
    matchers.add(matcher);

    ProxyRouteConfiguration route = new ProxyRouteConfiguration(this, matchers);
    this.routes.add(route);
    return route;
  }

  @Override
  public ProxyRouteConfiguration antMatchers(Collection<String> matcher) {
    List<String> matchers = new LinkedList<>();
    matchers.addAll(matcher);

    ProxyRouteConfiguration route = new ProxyRouteConfiguration(this, matchers);
    this.routes.add(route);
    return route;
  }

  @Override
  public ProxyRouteConfiguration antMatchers(String... matcher) {
    List<String> matchers = new LinkedList<>();
    matchers.addAll(Arrays.asList(matcher));

    ProxyRouteConfiguration route = new ProxyRouteConfiguration(this, matchers);
    this.routes.add(route);
    return route;
  }

}
