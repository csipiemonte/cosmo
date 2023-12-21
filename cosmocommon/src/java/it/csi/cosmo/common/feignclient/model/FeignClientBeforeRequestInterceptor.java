/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.feignclient.model;

import org.springframework.http.HttpRequest;

/**
 *
 */

public interface FeignClientBeforeRequestInterceptor {

  void beforeRequest(HttpRequest request, FeignClientContext context);
}
