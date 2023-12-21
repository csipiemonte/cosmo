/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.model;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 */

public class FeignClientRequestContext {

  private String explicitContentType;

  private FeignClientContext feignContext;

  private HttpMethod method;

  private Object body;

  private URI uri;

  private Map<String, String> pathParams;

  private Map<String, String> queryParams;

  private MultiValueMap<String, String> headers;

  private String requestId;

  public FeignClientRequestContext(FeignClientContext feignContext) {
    this.feignContext = feignContext;
    this.requestId = UUID.randomUUID().toString();

    pathParams = new HashMap<>();
    queryParams = new HashMap<>();
    headers = new LinkedMultiValueMap<>();
  }

  public String getExplicitContentType() {
    return explicitContentType;
  }

  public void setExplicitContentType(String explicitContentType) {
    this.explicitContentType = explicitContentType;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public FeignClientContext getFeignContext() {
    return feignContext;
  }

  public void setFeignContext(FeignClientContext feignContext) {
    this.feignContext = feignContext;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public URI getUri() {
    return uri;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }

  public Map<String, String> getPathParams() {
    return pathParams;
  }

  public void setPathParams(Map<String, String> pathParams) {
    this.pathParams = pathParams;
  }

  public Map<String, String> getQueryParams() {
    return queryParams;
  }

  public void setQueryParams(Map<String, String> queryParams) {
    this.queryParams = queryParams;
  }

  public MultiValueMap<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(MultiValueMap<String, String> headers) {
    this.headers = headers;
  }

  @Override
  public String toString() {
    return "FeignClientRequestContext [" + (method != null ? "method=" + method + ", " : "")
        + (uri != null ? "uri=" + uri + ", " : "")
        + (requestId != null ? "requestId=" + requestId : "") + "]";
  }

}
