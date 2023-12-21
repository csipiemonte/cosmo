/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.business.rest.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.cosmobe.business.rest.ProxyApi;
import it.csi.cosmo.cosmobe.business.service.ProxyService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;


public class ProxyApiImpl extends ParentApiImpl implements ProxyApi {

  @Autowired
  public ProxyService proxyService;

  private boolean isCorsEnabled() {
    return configurazioneService.getConfig(ParametriApplicativo.CORS_ENABLE).asBool();
  }

  @Override
  public Response proxyGet(HttpServletRequest request, UriInfo uriInfo) {
    return doProxy(request, uriInfo, HttpMethod.GET);
  }

  @Override
  public Response proxyPost(HttpServletRequest request, UriInfo uriInfo) {
    return doProxy(request, uriInfo, HttpMethod.POST);
  }

  @Override
  public Response proxyPut(HttpServletRequest request, UriInfo uriInfo) {
    return doProxy(request, uriInfo, HttpMethod.PUT);
  }

  @Override
  public Response proxyDelete(HttpServletRequest request, UriInfo uriInfo) {
    return doProxy(request, uriInfo, HttpMethod.DELETE);
  }

  @Override
  public Response proxyOptions(HttpServletRequest request, UriInfo uriInfo) {
    if (isCorsEnabled()) {
      return Response.status(200).build();
    } else {
      throw new ForbiddenException("CORS requests are not allowed");
    }
  }

  @Override
  public Response proxyHead(HttpServletRequest request, UriInfo uriInfo) {
    return doProxy(request, uriInfo, HttpMethod.HEAD);
  }

  private Response doProxy(HttpServletRequest request, UriInfo uriInfo, HttpMethod method) {
    return proxyService.doProxy(request, uriInfo, method);
  }

}
