/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketEventPostRequest;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmosoap.business.service.EventService;
import it.csi.cosmo.cosmosoap.integration.rest.CosmoWebsocketFeignClient;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class EventServiceImpl implements EventService {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "EventServiceImpl");

  @Autowired
  public CosmoWebsocketFeignClient cosmoWebsocketFeignClient;

  public EventServiceImpl() {
    // NOP
  }

  protected boolean isEnabled() {
    return true; // per override opzionale
  }

  @Override
  public void broadcastEvent(String eventType, Object payload, WebSocketTargetSelector... targets) {
    final var method = "broadcastEvent";
    logger.info(method, "broadcasting event {}", eventType);
    if (logger.isDebugEnabled()) {
      logger.info(method, "broadcasting event {} with payload {} to targets {}", eventType, payload,
          Arrays.asList(targets));
    }

    WebSocketEventPostRequest request = new WebSocketEventPostRequest();

    request.setEvent(eventType);
    request.setPayload(payload);
    request.setTargets(Arrays.asList(targets));

    try {
      cosmoWebsocketFeignClient.postEvent(request);
    } catch (Exception e) {
      logger.error(method, "error event " + eventType, e);
      throw e;
    }
  }
}
