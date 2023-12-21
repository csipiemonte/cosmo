/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.business.service.MessagingService;
import it.csi.cosmo.cosmo.business.service.WebSocketProxyService;
import it.csi.cosmo.cosmo.business.service.proto.ParentWebSocketService;
import it.csi.cosmo.cosmo.dto.exception.NoJmsHandlerException;
import it.csi.cosmo.cosmo.dto.messaging.WebSocketPostJMSMessage;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmo.dto.ws.WebSocketChannel;
import it.csi.cosmo.cosmo.dto.ws.WebSocketEventPost;
import it.csi.cosmo.cosmo.dto.ws.WebSocketPost;
import it.csi.cosmo.cosmo.integration.messaging.MessageHandler;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class WebSocketProxyServiceImpl
implements WebSocketProxyService, MessageHandler<WebSocketPostJMSMessage> {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.WEBSOCKET_LOG_CATEGORY, "WebSocketProxyServiceImpl");

  private Map<String, ParentWebSocketService<Object>> handlersCache = new HashMap<>();

  @Autowired
  private MessagingService messagingService;

  public WebSocketProxyServiceImpl() {
    // NOP
  }

  @Override
  public boolean canHandle(Class<WebSocketPostJMSMessage> clazz) {
    return WebSocketPostJMSMessage.class.isAssignableFrom(clazz);
  }

  @Override
  public Response postEvent(HttpServletRequest request, String channel,
      WebSocketEventPost inputPayload) {
    String method = "postEvent";

    WebSocketEventPost message = new WebSocketEventPost();
    message.setEvent(inputPayload.getEvent());
    message.setPayload(inputPayload.getPayload());
    message.setTargets(inputPayload.getTargets());

    if (logger.isDebugEnabled()) {
      logger.debug(method,
          "received websocket event POST HTTP request for channel {} with payload {}", channel,
          message.getPayload());
    }

    if (messagingService.isEnabled()) {
      logger.debug(method, "forwarding event POST HTTP request to JMS message topic");
      dispatchToJMSTopic(message, channel);

    } else {
      logger.debug(method, "JMS messaging is disabled, dispatching event locally");
      dispatch(message, channel);
    }

    return Response.ok().build();
  }

  @Override
  public Response post(HttpServletRequest request, String channel, WebSocketPost inputPayload) {
    String method = "post";

    WebSocketEventPost message = new WebSocketEventPost();
    message.setEvent(null);
    message.setPayload(inputPayload.getPayload());
    message.setTargets(inputPayload.getTargets());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "received websocket POST HTTP request for channel {} with payload {}",
          channel, message.getPayload());
    }

    if (messagingService.isEnabled()) {
      logger.debug(method, "forwarding POST HTTP request to JMS message topic");
      dispatchToJMSTopic(message, channel);

    } else {
      logger.debug(method, "JMS messaging is disabled, dispatching locally");
      dispatch(message, channel);
    }

    return Response.ok().build();
  }

  private void dispatchToJMSTopic(WebSocketEventPost payload, String channel) {
    WebSocketPostJMSMessage serializable = new WebSocketPostJMSMessage();
    serializable.setChannel(channel);
    serializable.setEvent(payload.getEvent());
    try {
      serializable
      .setJsonPayload(ObjectUtils.getDataMapper().writeValueAsString(payload.getPayload()));
    } catch (JsonProcessingException e) {
      throw new InternalServerException("Invalid payload (not json-serializable)", e);
    }

    serializable.setTargets(payload.getTargets().toArray(new WebSocketTargetSelector[] {}));

    messagingService.sendMessage(serializable);
  }

  @Override
  public void onMessage(WebSocketPostJMSMessage payload) {
    String method = "onMessage";

    String channel = payload.getChannel();

    if (logger.isDebugEnabled()) {
      logger.debug(method, "received websocket POST JMS message for channel " + channel
          + " with payload: " + payload.getJsonPayload());
    }

    WebSocketEventPost original = new WebSocketEventPost();
    try {
      original.setPayload(ObjectUtils.getDataMapper().readTree(payload.getJsonPayload()));
    } catch (IOException e) {
      throw new InternalServerException("Invalid recovered payload (not json-readable)", e);
    }
    original.setEvent(payload.getEvent());
    original.setTargets(Arrays.asList(payload.getTargets()));

    dispatch(original, channel);
  }

  private void dispatch(WebSocketEventPost payload, String channel) {
    String method = "dispatch";

    if (logger.isDebugEnabled()) {
      logger.debug(method, "dispatching websocket POST message for channel " + channel
          + " with payload: " + payload.getPayload().toString());
    }

    ParentWebSocketService<Object> service = getWebSocketHandlerService(channel);

    if (logger.isDebugEnabled()) {
      try {
        logger.debug(method, "dispatching websocket POST message to service "
            + ObjectUtils.getTargetObject(service).getName());
      } catch (Exception e) {
        logger.debug(method,
            "dispatching websocket POST message to service " + service.getClass().getName());
      }
    }

    JsonNode payloadNode = payload.getPayload();

    Predicate<UserInfoDTO> userFilter = toPredicate(payload.getTargets());

    try {
      if (StringUtils.isEmpty(payload.getEvent())) {
        service.broadcast(payloadNode, userFilter);
      } else {
        service.broadcastEvent(payload.getEvent(), payloadNode, userFilter);
      }
    } catch (Exception e) {
      logger.error(method, "error dispatching websocket POST message", e);
    }
  }

  private Predicate<UserInfoDTO> toPredicate(List<WebSocketTargetSelector> filters) {
    if (filters.isEmpty()) {
      return userInfo -> true;
    }
    return userInfo -> filters.stream().anyMatch(filter -> filterBySelector(filter).test(userInfo));
  }

  private Predicate<UserInfoDTO> filterBySelector(WebSocketTargetSelector filter) {
    return filterByCodiceFiscale(filter).and(filterByIdEnte(filter)).and(filterByIdProfilo(filter));
  }

  private Predicate<UserInfoDTO> filterByCodiceFiscale(WebSocketTargetSelector filter) {
    return userInfo -> StringUtils.isBlank(filter.getCodiceFiscale())
        || (userInfo != null && userInfo.getCodiceFiscale() != null
        && userInfo.getCodiceFiscale().equalsIgnoreCase(filter.getCodiceFiscale()));
  }

  private Predicate<UserInfoDTO> filterByIdEnte(WebSocketTargetSelector filter) {
    return userInfo -> filter.getIdEnte() == null
        || (userInfo != null && userInfo.getEnte() != null && userInfo.getEnte().getId() != null
        && userInfo.getEnte().getId().equals(filter.getIdEnte()));
  }

  private Predicate<UserInfoDTO> filterByIdProfilo(WebSocketTargetSelector filter) {
    return userInfo -> filter.getIdProfilo() == null || (userInfo != null
        && userInfo.getProfilo() != null && userInfo.getProfilo().getId() != null
        && userInfo.getProfilo().getId().equals(filter.getIdProfilo()));
  }

  @SuppressWarnings("unchecked")
  private synchronized ParentWebSocketService<Object> getWebSocketHandlerService(String channel) {
    return handlersCache.computeIfAbsent(channel, c -> {

      ApplicationContext appContext = SpringApplicationContextHelper.getAppContext();
      Map<String, Object> handlers = appContext.getBeansWithAnnotation(WebSocketChannel.class);

      //@formatter:off
      return (ParentWebSocketService<Object>) handlers.entrySet().stream()
          .filter(entry -> entry.getValue() instanceof ParentWebSocketService)
          .filter(entry -> {
            WebSocketChannel ann = appContext.findAnnotationOnBean(entry.getKey(), WebSocketChannel.class);
            return ann != null && ann.value().equalsIgnoreCase(c);
          })
          .reduce((a, b) -> {
            throw new BadRequestException("Multiple handlers for websocket channel: " + c);
          })
          .map(Entry::getValue)
          .orElseThrow(() -> new NoJmsHandlerException("No handler found for websocket channel: " + c));
      //@formatter:on

    });


  }

}
