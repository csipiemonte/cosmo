/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.filter;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.HeaderDecoratorPrecedence;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobe.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobe.config.ParametriApplicativo;
import it.csi.cosmo.cosmobe.util.logger.LogCategory;
import it.csi.cosmo.cosmobe.util.logger.LoggerFactory;


/**
 * Filtro delegato alla gestione delle chiamate CORS
 * 
 * @author CSIPiemonte
 */
@Provider
@HeaderDecoratorPrecedence
@Priority(11000)
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

  protected static final CosmoLogger LOG =
      LoggerFactory.getLogger(LogCategory.FILTER_LOG_CATEGORY, "CorsFilter");

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    // NOP
  }

  @Override
  public void filter(ContainerRequestContext requestContext,
      ContainerResponseContext responseContext) throws IOException {

    if (isEnabled()) {
      responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
      responseContext.getHeaders().add("Access-Control-Allow-Methods", "*");
      responseContext.getHeaders().add("Access-Control-Allow-Headers", "*");
    }
  }

  private boolean isEnabled() {
    ConfigurazioneService configurazioneService = ConfigurazioneService.getInstance();
    return configurazioneService.getConfig(ParametriApplicativo.CORS_ENABLE).asBool();
  }

}
