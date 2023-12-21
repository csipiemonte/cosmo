/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.business.service.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.mail.CosmoMailTransportDelegate;
import it.csi.cosmo.common.mail.CosmoMailerFactory;
import it.csi.cosmo.common.mail.ICosmoMailer;
import it.csi.cosmo.common.mail.model.CosmoMail;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmo.business.service.MailService;
import it.csi.cosmo.cosmo.config.Constants;
import it.csi.cosmo.cosmo.config.ParametriApplicativo;
import it.csi.cosmo.cosmo.util.logger.LogCategory;
import it.csi.cosmo.cosmo.util.logger.LoggerConstants;
import it.csi.cosmo.cosmo.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per l'invio delle mail
 */
@Service
@Transactional
public class MailServiceImpl implements MailService, DisposableBean {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.MAIL_LOG_CATEGORY, "MailServiceImpl");

  private ICosmoMailer cosmoMailer;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired(required = false)
  private CosmoMailTransportDelegate transportDelegate;

  public MailServiceImpl() {
    // NOP
  }
  
  private boolean isEnabledMailAssistenza() {
    return configurazioneService.requireConfig(ParametriApplicativo.MAIL_ASSISTENZA_ENABLE).asBool();
  }

  private synchronized ICosmoMailer getMailer() {
    if (this.cosmoMailer == null) {

      //@formatter:off
      this.cosmoMailer = CosmoMailerFactory.defaultMailer()
          .withServer(configurazioneService.requireConfig(ParametriApplicativo.MAIL_SERVER).asString())
          .withEnableAuthentication(configurazioneService.requireConfig(ParametriApplicativo.MAIL_ENABLE_AUTHENTICATION).asBoolean())
          .withEnableStartTLS(configurazioneService.requireConfig(ParametriApplicativo.MAIL_ENABLE_START_TLS).asBoolean())
          .withMaxRetries(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_MAX_RETRIES).asInteger())
          .withPassword(configurazioneService.getConfig(ParametriApplicativo.MAIL_PASSWORD).asString())
          .withPort(configurazioneService.requireConfig(ParametriApplicativo.MAIL_PORT).asInteger())
          .withRetryLinearBackoffFactor(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_LINEAR_BACKOFF_FACTOR).asDouble())
          .withRetryExponentialBackoffFactor(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_EXPONENTIAL_BACKOFF_FACTOR).asDouble())
          .withRetryDelay(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_DELAY).asInteger())
          .withTransportProtocol(configurazioneService.requireConfig(ParametriApplicativo.MAIL_TRANSPORT_PROTOCOL).asString())
          .withUsername(configurazioneService.getConfig(ParametriApplicativo.MAIL_USERNAME).asString())
          .withDebouncePeriod(configurazioneService.getConfig(ParametriApplicativo.MAIL_DEBOUNCE_PERIOD).asInteger())
          .withLoggingPrefix(LoggerConstants.ROOT_LOG_CATEGORY)
          .withTransportDelegate(transportDelegate)
          .build();
      //@formatter:on
    }

    return this.cosmoMailer;
  }

  @Override
  public Future<CosmoMailDeliveryResult> inviaMailAssistenza(String subject, String text) {
    String method = "inviaMailAssistenza";
    logger.debug(method, "invio della mail " + subject);

    subject = "[" + Constants.COMPONENT_DESCRIPTION +
        " " + (ConfigurazioneServiceImpl.getCurrentEnvironment()) +
        "] " + subject;

    //@formatter:off
    CosmoMail mail = CosmoMail.builder()
        .withSubject(subject)
        .withText(text)
        .withFrom(configurazioneService.requireConfig(ParametriApplicativo.MAIL_DEFAULT_FROM).asString())
        .withFromName(configurazioneService.requireConfig(ParametriApplicativo.MAIL_DEFAULT_FROM_NAME).asString())
        .withTo(configurazioneService.requireConfig(ParametriApplicativo.MAIL_DEFAULT_DESTINATION).asStringList(","))
        .withCc(configurazioneService.getConfig(ParametriApplicativo.MAIL_DEFAULT_CC).asStringList(","))
        .withBcc(configurazioneService.getConfig(ParametriApplicativo.MAIL_DEFAULT_BCC).asStringList(","))
        .build();
    //@formatter:on

    if (isEnabledMailAssistenza()) {
      return getMailer().send(mail);
    } else {
      logger.info(method, "invio delle email disabilitato. richiesto l'invio della mail seguente: "
          + ObjectUtils.represent(mail));

      return CompletableFuture.completedFuture(null);
    }
  }

  @Override
  public void destroy() throws Exception {
    String method = "destroy";
    logger.info(method, "chiusura del mail service");
    if (cosmoMailer != null) {
      cosmoMailer.close();
    }
  }
}
