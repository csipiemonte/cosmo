/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.mail.CosmoMailerFactory;
import it.csi.cosmo.common.mail.ICosmoMailer;
import it.csi.cosmo.common.mail.model.CosmoMail;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.MailService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.config.ParametriApplicativo;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerConstants;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;


/**
 * Implementazione del servizio per l'invio delle mail
 */
@Service
@Transactional
public class MailServiceImpl implements MailService, DisposableBean {

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_MAIL_LOG_CATEGORY, "MailServiceImpl");

  private ICosmoMailer cosmoMailer;

  @Autowired
  private ConfigurazioneService configurazioneService;

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
          .withEnableAuthentication(configurazioneService.getConfig(ParametriApplicativo.MAIL_ENABLE_AUTHENTICATION).asBoolean(false))
          .withEnableStartTLS(configurazioneService.getConfig(ParametriApplicativo.MAIL_ENABLE_START_TLS).asBoolean(false))
          .withMaxRetries(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_MAX_RETRIES).asInteger())
          .withPassword(configurazioneService.getConfig(ParametriApplicativo.MAIL_PASSWORD).asString(null))
          .withPort(configurazioneService.getConfig(ParametriApplicativo.MAIL_PORT).asInteger(25))
          .withRetryLinearBackoffFactor(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_LINEAR_BACKOFF_FACTOR).asDouble())
          .withRetryExponentialBackoffFactor(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_EXPONENTIAL_BACKOFF_FACTOR).asDouble())
          .withRetryDelay(configurazioneService.requireConfig(ParametriApplicativo.MAIL_RETRY_DELAY).asInteger())
          .withTransportProtocol(configurazioneService.requireConfig(ParametriApplicativo.MAIL_TRANSPORT_PROTOCOL).asString())
          .withUsername(configurazioneService.getConfig(ParametriApplicativo.MAIL_USERNAME).asString(null))
          .withDebouncePeriod(configurazioneService.getConfig(ParametriApplicativo.MAIL_DEBOUNCE_PERIOD).asInteger())
          .withLoggingPrefix(LoggerConstants.COSMOPRATICHE_ROOT_LOG_CATEGORY)
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
