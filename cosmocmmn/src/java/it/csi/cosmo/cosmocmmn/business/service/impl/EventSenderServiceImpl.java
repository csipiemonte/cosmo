/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmocmmn.business.service.EventSenderService;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessProcessEventFeignClient;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per la gestione dell'utente, della profilazione e della sessione
 */
@Service
@Transactional
public class EventSenderServiceImpl implements EventSenderService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "EventSenderServiceImpl");

  @Autowired
  private CosmoBusinessProcessEventFeignClient client;

  @Override
  public void sendEventToCosmo(ProcessEngineEventDTO event) {
    final var method = "sendEventToCosmo";
    if (logger.isDebugEnabled()) {
      logger.debug(method, "invio messaggio {} da listener flowable: {}", event.getMessageType(),
          ObjectUtils.represent(event));
    }

    try {
      this.client.postProcessEvent(event);
    } catch (FeignClientClientErrorException e) {
      logger.error(method, "Errore HTTP 4xx nell'invio dell'evento a cosmobusiness: "
          + ObjectUtils.represent(e.getResponse()), e);
      throw e;
    } catch (FeignClientServerErrorException e) {
      logger.error(method, "Errore HTTP 5xx nell'invio dell'evento a cosmobusiness: "
          + ObjectUtils.represent(e.getResponse()), e);
      throw e;
    } catch (Exception e) {
      logger.error(method, "Errore nell'invio dell'evento a cosmobusiness: " + e.getMessage(), e);
      throw e;
    } finally {
      if (logger.isDebugEnabled()) {
        logger.debug(method, "inviato messaggio {} da listener flowable", event.getMessageType());
      }
    }
  }

  @Override
  public void sendVariableEventToCosmo(ProcessInstanceVariableEventDTO event) {
    final var method = "sendVariableEventToCosmo";
    if (logger.isDebugEnabled()) {
      logger.debug(method, "invio messaggio {} da listener flowable: {}", event.getMessageType(),
          ObjectUtils.represent(event));
    }

    try {
      this.client.postProcessVariableEvent(event);
    } catch (FeignClientClientErrorException e) {
      logger.error(method, "Errore HTTP 4xx nell'invio delle variabili a cosmobusiness: "
          + ObjectUtils.represent(e.getResponse()), e);
      throw e;
    } catch (FeignClientServerErrorException e) {
      logger.error(method, "Errore HTTP 5xx nell'invio delle variabili a cosmobusiness: "
          + ObjectUtils.represent(e.getResponse()), e);
      throw e;
    } catch (Exception e) {
      logger.error(method, "Errore nell'invio delle variabili a cosmobusiness: " + e.getMessage(),
          e);
      throw e;
    } finally {
      if (logger.isDebugEnabled()) {
        logger.debug(method, "inviato messaggio {} da listener flowable", event.getMessageType());
      }
    }

  }
}
