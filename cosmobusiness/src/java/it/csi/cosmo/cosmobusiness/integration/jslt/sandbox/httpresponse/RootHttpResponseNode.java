/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.httpresponse;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 *
 */

public class RootHttpResponseNode<T> {

  private ResponseEntity<T> response;
  private Map<String, Object> cachedHeaders;

  public RootHttpResponseNode(ResponseEntity<T> response) {
    this.response = response;
  }

  public T getBody() {
    return response.getBody();
  }

  public Map<String, Object> getHeaders() {
    if (cachedHeaders == null) {
      Map<String, Object> newCachedHeaders = new HashMap<>();
      response.getHeaders().forEach((key, value) -> {
        newCachedHeaders.put(key.toLowerCase(), value);
      });
      cachedHeaders = newCachedHeaders;
    }
    return cachedHeaders;
  }

  public int getStatus() {
    return response.getStatusCodeValue();
  }

  // alias
  public T getResponseBody() {
    return getBody();
  }

  // alias
  public Map<String, Object> getResponseHeaders() {
    return getHeaders();
  }

  // alias
  public int getResponseStatus() {
    return getStatus();
  }

  // alias
  public int getStatusCode() {
    return getStatus();
  }

  // alias
  public int getResponseStatusCode() {
    return getStatus();
  }

}
