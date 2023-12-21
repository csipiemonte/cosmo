/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.proto;

import java.util.Base64;
import org.springframework.http.HttpRequest;
import it.csi.cosmo.common.feignclient.model.FeignClientBeforeRequestInterceptor;
import it.csi.cosmo.common.feignclient.model.FeignClientContext;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;

/**
 *
 */

public abstract class BasicAuthenticationInterceptor
implements FeignClientBeforeRequestInterceptor {

  protected abstract String getUsername();

  protected abstract String getPassword();

  protected void addAdditionalHeaders(HttpRequest request, FeignClientContext context) {
    // NOP
  }

  @Override
  public void beforeRequest(HttpRequest request, FeignClientContext context) {

    String clientName = getUsername();
    String clientPassword = getPassword();

    // TODO CHECK ENCODING
    String authHeader = "Basic " + Base64.getEncoder().encodeToString(
        (clientName + ":" + clientPassword).getBytes(CosmoAuthenticationConfig.ENCODING));

    request.getHeaders().add("Authorization", authHeader);
    addAdditionalHeaders(request, context);
  }
}
