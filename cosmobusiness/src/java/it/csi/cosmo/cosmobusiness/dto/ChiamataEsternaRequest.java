/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.dto;

import java.util.Collections;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;

/**
 *
 */


public class ChiamataEsternaRequest<T> {

  private CosmoTEndpointFruitore endpoint;

  private String url;

  private String method;

  private Map<String, Object> pathParams;

  private Map<String, Object> queryParams;

  private Object payload;

  private Map<String, Object> headers;

  private ParameterizedTypeReference<T> returnType;

  private MediaType contentType;

  private ChiamataEsternaRequest(Builder<T> builder) {
    this.endpoint = builder.endpoint;
    this.url = builder.url;
    this.method = builder.method;
    this.pathParams = builder.pathParams;
    this.queryParams = builder.queryParams;
    this.payload = builder.payload;
    this.headers = builder.headers;
    this.returnType = builder.returnType;
    this.contentType = builder.contentType;
  }

  private ChiamataEsternaRequest() {
    // NOP
  }

  public MediaType getContentType() {
    return contentType;
  }

  public String getUrl() {
    return url;
  }

  public ParameterizedTypeReference<T> getReturnType() {
    return returnType;
  }

  public CosmoTEndpointFruitore getEndpoint() {
    return endpoint;
  }

  public String getMethod() {
    return method;
  }

  public Map<String, Object> getPathParams() {
    return pathParams;
  }

  public Map<String, Object> getQueryParams() {
    return queryParams;
  }

  public Object getPayload() {
    return payload;
  }

  public Map<String, Object> getHeaders() {
    return headers;
  }

  public void setEndpoint(CosmoTEndpointFruitore endpoint) {
    this.endpoint = endpoint;
  }

  /**
   * Creates builder to build {@link ChiamataEsternaRequest}.
   * 
   * @return created builder
   */
  public static <T> Builder<T> builder(ParameterizedTypeReference<T> returnType) {
    return new Builder<T>().withReturnType(returnType);
  }

  public static <T> Builder<T> builder(Class<T> returnType) {
    return new Builder<T>().withReturnType(returnType);
  }

  /**
   * Builder to build {@link ChiamataEsternaRequest}.
   */
  public static final class Builder<T> {
    private CosmoTEndpointFruitore endpoint;
    private String url;
    private String method;
    private Map<String, Object> pathParams = Collections.emptyMap();
    private Map<String, Object> queryParams = Collections.emptyMap();
    private Object payload;
    private Map<String, Object> headers = Collections.emptyMap();
    private ParameterizedTypeReference<T> returnType;
    private MediaType contentType;

    private Builder() {}

    public Builder<T> withContentType(MediaType contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder<T> withEndpoint(CosmoTEndpointFruitore endpoint) {
      this.endpoint = endpoint;
      return this;
    }

    public Builder<T> withUrl(String url) {
      this.url = url;
      return this;
    }

    public Builder<T> withMethod(String method) {
      this.method = method;
      return this;
    }

    public Builder<T> withPathParams(Map<String, Object> pathParams) {
      this.pathParams = pathParams;
      return this;
    }

    public Builder<T> withQueryParams(Map<String, Object> queryParams) {
      this.queryParams = queryParams;
      return this;
    }

    public Builder<T> withPayload(Object payload) {
      this.payload = payload;
      return this;
    }

    public Builder<T> withHeaders(Map<String, Object> headers) {
      this.headers = headers;
      return this;
    }

    public Builder<T> withReturnType(ParameterizedTypeReference<T> returnType) {
      this.returnType = returnType;
      return this;
    }

    public Builder<T> withReturnType(Class<T> returnType) {
      this.returnType = ParameterizedTypeReference.forType(returnType);
      return this;
    }

    public ChiamataEsternaRequest<T> build() {
      return new ChiamataEsternaRequest<>(this);
    }
  }


}
