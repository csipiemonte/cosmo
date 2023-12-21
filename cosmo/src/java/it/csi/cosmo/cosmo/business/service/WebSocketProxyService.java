/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import it.csi.cosmo.cosmo.business.service.impl.ProxyServiceImpl;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEventPost;
import it.csi.cosmo.cosmo.dto.ws.WebSocketPost;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;

public interface WebSocketProxyService {

  /**
   *
   * @return
   */
  Response post(HttpServletRequest request, String channel, WebSocketPost payload);

  Response postEvent(HttpServletRequest request, String channel, WebSocketEventPost payload);

  static WebSocketProxyService getInstance() {
    return (WebSocketProxyService) SpringApplicationContextHelper
        .getBean(ProxyServiceImpl.class.getSimpleName());
  }

}
