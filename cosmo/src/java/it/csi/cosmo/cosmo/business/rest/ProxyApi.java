/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmo.config.ProxyConfig;


/**
 * Risorsa RestEasy per il reperimento di informazioni sullo stato
 */
@Path("/")
@Consumes({"application/json", "multipart/form-data"})
@Produces({"application/json"})
public interface ProxyApi {


  @Secured(permitAll = true)
  @GET
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyGet(@Context HttpServletRequest request, @Context UriInfo uriInfo);

  @Secured(permitAll = true)
  @POST
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyPost(@Context HttpServletRequest request, @Context UriInfo uriInfo);

  @Secured(permitAll = true)
  @PUT
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyPut(@Context HttpServletRequest request, @Context UriInfo uriInfo);

  @Secured(permitAll = true)
  @DELETE
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyDelete(@Context HttpServletRequest request, @Context UriInfo uriInfo);

  @Secured(permitAll = true)
  @OPTIONS
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyOptions(@Context HttpServletRequest request, @Context UriInfo uriInfo);

  @Secured(permitAll = true)
  @HEAD
  @Path(ProxyConfig.PROXY_CATCHALL_URL)
  public Response proxyHead(@Context HttpServletRequest request, @Context UriInfo uriInfo);

}
