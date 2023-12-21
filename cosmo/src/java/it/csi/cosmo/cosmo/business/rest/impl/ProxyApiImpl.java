/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.business.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import it.csi.cosmo.cosmo.business.rest.ProxyApi;
import it.csi.cosmo.cosmo.business.rest.proto.ParentApiImpl;
import it.csi.cosmo.cosmo.business.service.ProxyService;


public class ProxyApiImpl extends ParentApiImpl implements ProxyApi {

  @Autowired
  public ProxyService proxyService;

  @Override
  public Response proxyGet(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.GET);
  }

  @Override
  public Response proxyPost(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.POST);
  }

  @Override
  public Response proxyPut(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.PUT);
  }

  @Override
  public Response proxyDelete(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.DELETE);
  }

  @Override
  public Response proxyOptions(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.OPTIONS);
  }

  @Override
  public Response proxyHead(HttpServletRequest request, UriInfo uriInfo) {
    return proxyService.doProxy(request, uriInfo, HttpMethod.HEAD);
  }

}
