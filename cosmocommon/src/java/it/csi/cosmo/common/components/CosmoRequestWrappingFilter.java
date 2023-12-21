/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.components;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;
import it.csi.cosmo.common.logger.CosmoLogger;

public abstract class CosmoRequestWrappingFilter extends OncePerRequestFilter {

  private CosmoLogger log;

  public CosmoRequestWrappingFilter(String loggingCategory) {
    super();
    log = new CosmoLogger(loggingCategory, "CosmoRequestWrappingFilter");
  }

  private boolean wrapRequestContent(HttpServletRequest r) {
    return log.isDebugEnabled();
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    boolean isFirstRequest = !isAsyncDispatch(request);
    HttpServletRequest requestToUse = request;

    if (wrapRequestContent(request) && isFirstRequest
        && !(request instanceof CosmoContentCachingRequestWrapper)) {
      requestToUse = new CosmoContentCachingRequestWrapper(request);

      ServletRequestAttributes sra = new ServletRequestAttributes(requestToUse);
      RequestContextHolder.setRequestAttributes(sra);
    }

    filterChain.doFilter(requestToUse, response);
  }

}
