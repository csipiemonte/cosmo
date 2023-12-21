/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import it.csi.cosmo.common.util.RequestUtils;


public class CosmoRequestLoggingInterceptor
    extends AbstractCosmoRequestLoggingInterceptor
    implements ContainerRequestFilter, ContainerResponseFilter {

  public CosmoRequestLoggingInterceptor(String loggingCategory, boolean doRemoveChunkedHeader) {
    super(loggingCategory, doRemoveChunkedHeader);
  }

  @Context
  private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    super.before(getAdapterBeforeRequest(requestContext));
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {

    super.after(getAdapterAfterRequest(requestContext, responseContext));
  }

  private RequestFilterRequestAdapter getAdapterBeforeRequest(
      ContainerRequestContext requestContext) {

    ResourceInfo resourceInfoLocal = this.resourceInfo;

    return new RequestFilterRequestAdapter() {

      @Override
      public HttpServletRequest getRequest() {
        return RequestUtils.getCurrentRequest().orElse(null);
      }

      @Override
      public String getTargetClassName() {
        return resourceInfoLocal != null && resourceInfoLocal.getResourceClass() != null
            ? resourceInfoLocal.getResourceClass().getSimpleName()
            : "<no class>";
      }

      @Override
      public String getTargetMethodName() {
        return resourceInfoLocal != null && resourceInfoLocal.getResourceMethod() != null
            ? resourceInfoLocal.getResourceMethod().getName()
            : "<no class>";
      }

    };
  }


  private RequestFilterResponseAdapter getAdapterAfterRequest(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext) {

    var b4 = getAdapterBeforeRequest(requestContext);

    return new RequestFilterResponseAdapter() {

      @Override
      public HttpServletRequest getRequest() {
        return b4.getRequest();
      }

      @Override
      public String getTargetClassName() {
        return b4.getTargetClassName();
      }

      @Override
      public String getTargetMethodName() {
        return b4.getTargetMethodName();
      }

      @Override
      public Object getResponseEntity() {
        return responseContext.getEntity();
      }

      @Override
      public Integer getResponseStatus() {
        return responseContext.getStatus();
      }

      @Override
      public Map<String, List<Object>> getResponseHeaders() {
        return responseContext.getHeaders();
      }

      @Override
      public void addResponseHeaders(String key, String value) {
        responseContext.getHeaders().add(key, value);
      }

      @Override
      public void removeResponseHeader(String key) {
        responseContext.getHeaders().remove(key);
      }
    };
  }
}
