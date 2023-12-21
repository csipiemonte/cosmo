/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.http.HttpMethod;
import it.csi.cosmo.common.discovery.model.DiscoveredInstance;
import it.csi.cosmo.cosmo.business.service.impl.ProxyServiceImpl;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;

public interface ProxyService {

  /**
   *
   * @return
   */
  Response doProxy(HttpServletRequest request, UriInfo uriInfo, HttpMethod method);

  Optional<DiscoveredInstance> pickInstanceForCall(String serviceName);

  static ProxyService getInstance() {
    return (ProxyService) SpringApplicationContextHelper
        .getBean(ProxyServiceImpl.class.getSimpleName());
  }

}
