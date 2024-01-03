/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.business.rest.impl;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;
import it.csi.cosmo.common.components.CosmoErrorHandler;
import it.csi.cosmo.cosmonotifications.business.service.impl.ConfigurazioneServiceImpl;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;

/**
 * Classe per gestire gli errori
 */
@Provider
@Produces("application/json")
public class ErrorHandlerApiImpl extends CosmoErrorHandler {

  public ErrorHandlerApiImpl() {
    super(LogCategory.COSMONOTIFICATIONS_REST_LOG_CATEGORY.getCategory());
  }

  @Override
  protected boolean exposeExceptions() {
    return ConfigurazioneServiceImpl.isLocal();
  }
}



