/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobe.security.proxy.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import javax.ws.rs.core.Response;
import org.springframework.http.HttpMethod;

/**
 *
 */

public class ProxyRouteConfiguration extends ACLSpecification<ProxyRouteConfiguration>
implements RouteMatcher<ProxyRouteConfiguration> {

  private ProxyConfiguration parent;

  private List<String> matchers;

  private List<HttpMethod> methods;

  private String rewrite;

  private Supplier<Response> handler;

  public ProxyRouteConfiguration(ProxyConfiguration parent, List<String> matchers) {
    this.methods = new ArrayList<>();
    this.parent = parent;
    this.matchers = matchers;
  }

  public Supplier<Response> getHandler() {
    return handler;
  }

  public List<String> getMatchers() {
    return matchers;
  }

  public List<HttpMethod> getMethods() {
    return methods;
  }

  public String getRewrite() {
    return rewrite;
  }

  protected void setRewrite(String rewrite) {
    this.rewrite = rewrite;
  }

  public ProxyRouteConfiguration handle(Supplier<Response> handler) {
    this.handler = handler;
    return this;
  }

  private ProxyRouteConfiguration clone(HttpMethod method) {
    parent.getRoutes().remove(this);
    ProxyRouteConfiguration output = parent.antMatchers(this.getMatchers());
    output.getMethods().add(method);
    return output;
  }

  public ProxyRouteConfiguration get() {
    return clone(HttpMethod.GET);
  }

  public ProxyRouteConfiguration post() {
    return clone(HttpMethod.POST);
  }

  public ProxyRouteConfiguration put() {
    return clone(HttpMethod.PUT);
  }

  public ProxyRouteConfiguration delete() {
    return clone(HttpMethod.DELETE);
  }

  public ProxyRouteConfiguration method(HttpMethod httpMethod) {
    return clone(httpMethod);
  }

  public ProxyRouteConfiguration rewrite(String target) {
    this.rewrite = target;
    return this;
  }

  @Override
  public ProxyRouteConfiguration antMatchers(String matcher) {
    return parent.antMatchers(matcher);
  }

  @Override
  public ProxyRouteConfiguration antMatchers(Collection<String> matcher) {
    return parent.antMatchers(matcher);
  }

  @Override
  public ProxyRouteConfiguration antMatchers(String... matcher) {
    return parent.antMatchers(matcher);
  }

  public ProxyConfiguration build() {
    return parent;
  }
}
