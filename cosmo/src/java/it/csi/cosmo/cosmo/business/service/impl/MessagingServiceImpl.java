/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.MessagingService;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.dto.exception.NoJmsHandlerException;
import it.csi.cosmo.cosmo.dto.messaging.ParentJMSMessage;
import it.csi.cosmo.cosmo.integration.messaging.MessageHandler;
import it.csi.cosmo.cosmo.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;

/**
 *
 */

@Service
public class MessagingServiceImpl implements MessagingService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.MESSAGING_LOG_CATEGORY, "MessagingServiceImpl");

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired(required = false)
  private JmsTemplate jmsTemplate;

  @Autowired(required = false)
  private MessageConverter messageConverter;

  public MessagingServiceImpl() {
    // NOP
  }

  private Map<String, MessageHandler<ParentJMSMessage>> handlersCache = new HashMap<>();

  @Override
  public boolean isEnabled() {
    return configurazioneService != null
        && configurazioneService.requireConfig(ParametriApplicativo.MESSAGING_ENABLE).asBool();
  }

  @Override
  public void onMessage(Message message) {
    String method = "onMessage";

    ParentJMSMessage response;

    try {
      response = (ParentJMSMessage) messageConverter.fromMessage(message);
    } catch (JMSException e) {
      logger.error(method, "error reading incoming JMS message", e);
      return;
    }

    if (logger.isDebugEnabled()) {
      logger.debug(method, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
      logger.info(method,
          String.format("[MESSAGING] <== MESSAGE RECEIVED : %s", response.getClass().getName()));
      logger.debug(method, "incoming message content: " + ObjectUtils.represent(response));
      logger.debug(method, "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    } else {

      logger.info(method,
          String.format("[MESSAGING] <== MESSAGE RECEIVED : %s", response.getClass().getName()));
    }

    try {
      MessageHandler<ParentJMSMessage> dispatchTarget =
          getMessageHandlerService(response.getClass());
      dispatchTarget.onMessage(response);

    } catch (Exception e) {
      logger.error(method, "error handling incoming JMS message", e);
    }

  }

  @Override
  public void sendMessage(final ParentJMSMessage payload) {
    String method = "sendMessage";

    if (logger.isDebugEnabled()) {
      logger.debug(method, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
      logger.info(method,
          "[MESSAGING] ==> SENDING OUTBOUND MESSAGE " + payload.getClass().getName());
      logger.debug(method, "outbound message content: " + ObjectUtils.represent(payload));
      logger.debug(method, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    } else {
      logger.info(method,
          "[MESSAGING] ==> SENDING OUTBOUND MESSAGE " + payload.getClass().getName());
    }

    try {
      jmsTemplate.send(new MessageCreator() {
        @Override
        public Message createMessage(Session session) throws JMSException {
          return session.createObjectMessage(payload);
        }
      });
    } catch (Exception e) {
      logger.error(method, "error sending message to JMS topic", e);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private MessageHandler<ParentJMSMessage> getMessageHandlerService(Class<?> messageClass) {
    return handlersCache.computeIfAbsent(messageClass.getName(), c -> {

      ApplicationContext appContext = SpringApplicationContextHelper.getAppContext();

      Map<String, MessageHandler> handlers = appContext.getBeansOfType(MessageHandler.class);

      //@formatter:off
      return (MessageHandler<ParentJMSMessage>) handlers.entrySet().stream()
          .filter(entry -> entry.getValue().canHandle(messageClass))
          .reduce((a, b) -> {
            throw new BadConfigurationException("Multiple handlers for message type: " + messageClass.getName());
          })
          .map(Entry::getValue)
          .orElseThrow(() -> new NoJmsHandlerException("No handler found for message type: " + messageClass.getName()));
      //@formatter:on

    });
  }

}
