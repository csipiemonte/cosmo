/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmo.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoAuthenticationConfig;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.util.RequestUtils;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.ProfilazioneClientService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.security.SecurityUtils;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;


/**
 * Filtro delegato all'inserimento in sessione:
 * <ul>
 * <li>dell'oggetto <code>ClientInfo</code> contenente la profilazione per il client corrente
 * </ul>
 *
 * @author CSIPiemonte
 */

@Provider
@HeaderDecoratorPrecedence
@Priority(10000)
public class ClientAuthenticationFilter implements ContainerRequestFilter, ContainerResponseFilter {

  protected static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.FILTER_LOG_CATEGORY, "ClientAuthenticationFilter");

  public static final String CLIENTINFO_REQUESTATTR = "appDataCurrentClient";

  private boolean isEnabled() {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(ParametriApplicativo.ENABLE_CLIENT_AUTHENTICATION_FILTER)
        .asBoolean();
  }

  private boolean isBypassed() {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(ParametriApplicativo.BYPASS_CLIENT_AUTHENTICATION_FILTER)
        .asBoolean();
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    if (!isEnabled()) {
      return;
    }

    String methodName = "filter";
    HttpServletRequest hreq = RequestUtils.getCurrentRequest().orElse(null);

    if (hreq == null) {
      return;
    }

    ClientInfoDTO clientInfo = null;

    if (isBypassed()) {
      clientInfo = ProfilazioneClientService.getInstance().caricaClientInfoTest(hreq);
    } else {

      clientInfo = ProfilazioneClientService.getInstance().caricaClientInfo(hreq);
    }

    if (clientInfo != null) {
      if (logger.isDebugEnabled()) {
        logger.debug(methodName, "caricato client info da filter " + clientInfo);
      }

      hreq.setAttribute(CLIENTINFO_REQUESTATTR, clientInfo);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {

    if (!isEnabled()) {
      return;
    }

    responseContext.getHeaders().add(CosmoAuthenticationConfig.HEADER_CLIENT_ATTIVO,
        SecurityUtils.getClientCorrente().getCodice());
  }
}
