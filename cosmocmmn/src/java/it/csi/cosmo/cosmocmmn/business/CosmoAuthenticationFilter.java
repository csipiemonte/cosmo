/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.flowable.common.engine.impl.identity.Authentication;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmocmmn.security.SecurityUtils;

/**
 *
 */

public class CosmoAuthenticationFilter implements Filter {

  @Override
  public void destroy() {
    // nothing to do
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    UserInfoDTO user = SecurityUtils.getUtenteCorrente();

    Authentication.setAuthenticatedUserId(user.getCodiceFiscale());
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // nothing to do
  }

}
