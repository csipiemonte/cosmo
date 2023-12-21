/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.mail.model.CosmoMail;
import it.csi.cosmo.common.mail.model.CosmoMailAttachment;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryCache;
import it.csi.cosmo.common.mail.model.CosmoMailDeliveryResult;
import it.csi.cosmo.common.mail.model.CosmoMailQueueEntry;
import it.csi.cosmo.common.mail.model.MailStatus;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class DefaultCosmoMailerImpl implements ICosmoMailer {

  private static final String MAIL_CONTENT_TYPE = "text/html; charset=utf-8";

  private static final String PROPERTY_MAIL_SMTP_SSL_TRUST = "mail.smtp.ssl.trust";

  private static final String PROPERTY_MAIL_SMTP_PORT = "mail.smtp.port";

  private static final String PROPERTY_MAIL_SMTP_HOST = "mail.smtp.host";

  private static final String PROPERTY_MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

  private static final String PROPERTY_MAIL_SMTP_AUTH = "mail.smtp.auth";

  private static final String PROPERTY_MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

  private Logger logger;
  private String loggingPrefix;

  private String server;
  private Integer port;
  private String username;
  private String password;
  private Boolean enableAuthentication;
  private String transportProtocol;
  private Boolean enableStartTLS;
  private Integer maxRetries;
  private Integer retryDelay;
  private Double retryLinearBackoffFactor;
  private Double retryExponentialBackoffFactor;

  private Integer debouncePeriod;
  private CosmoMailDeliveryCache deliveryCache;
  private CosmoMailTransportDelegate transportDelegate;

  private ScheduledExecutorService mailQueueProcessor;

  private DefaultCosmoMailerImpl(Builder builder) {
    this.loggingPrefix =
        StringUtils.isBlank(builder.loggingPrefix) ? Constants.PRODUCT : builder.loggingPrefix;
    logger = Logger.getLogger(this.loggingPrefix + ".mail.DefaultCosmoMailerImpl");

    this.server = builder.server;
    this.port = builder.port;
    this.username = builder.username;
    this.password = builder.password;
    this.enableAuthentication = builder.enableAuthentication;
    this.transportProtocol = builder.transportProtocol;
    this.enableStartTLS = builder.enableStartTLS;
    this.maxRetries = builder.maxRetries;
    this.retryDelay = builder.retryDelay;
    this.retryLinearBackoffFactor = builder.retryLinearBackoffFactor;
    this.retryExponentialBackoffFactor = builder.retryExponentialBackoffFactor;
    this.debouncePeriod = builder.debouncePeriod;
    this.transportDelegate = builder.transportDelegate;

    mailQueueProcessor = Executors.newScheduledThreadPool(1);

    if (this.isDebounceEnabled()) {
      this.deliveryCache = new CosmoMailDeliveryCache();
    }
  }

  @Override
  public void close() throws IOException {
    logger.info("destroying mailer service");
    if (mailQueueProcessor != null) {
      mailQueueProcessor.shutdownNow();
    }
  }

  @Override
  public Future<CosmoMailDeliveryResult> send(CosmoMail mail) {
    CosmoMailQueueEntry queueEntry = new CosmoMailQueueEntry(mail);

    if (this.isDebounceEnabled() && !debounceFilter(mail)) {
      logger.warn("suppressed duplicated mail in debounce period - " + mail.getSubject());

      CosmoMailDeliveryResult result = new CosmoMailDeliveryResult();
      result.setFailedAttempts(queueEntry.getFailedAttempts());
      result.setMail(queueEntry.getMail());
      result.setStatus(MailStatus.SUPPRESSED_BY_DEBOUNCE);
      result.setUuid(queueEntry.getUuid());

      return CompletableFuture.completedFuture(result);
    }

    logger.debug("created mail queue entry " + queueEntry.getUuid());

    if (logger.isDebugEnabled()) {
      logger.debug(
          "mail queue entry " + queueEntry.getUuid() + " mail: " + ObjectUtils.represent(mail));
    }

    mailQueueProcessor.schedule(() -> {
      logger.debug("attempting first delivery of mail " + queueEntry.getUuid());
      attemptDeliveryOrRequeue(queueEntry);
    }, 100, TimeUnit.MILLISECONDS);

    return queueEntry.getResolver();
  }

  private boolean isCacheable(CosmoMail mail) {
    return CollectionUtils.isEmpty(mail.getAttachments());
  }

  private boolean debounceFilter(CosmoMail mail) {
    if (!isCacheable(mail)) {
      return true;
    }

    if (this.deliveryCache.isCached(mail, this.debouncePeriod)) {
      return false;
    }
    this.deliveryCache.add(mail);
    return true;
  }

  private boolean isDebounceEnabled() {
    return this.debouncePeriod != null && this.debouncePeriod > 0;
  }

  private void attemptDeliveryOrRequeue(CosmoMailQueueEntry queueEntry) {

    try {
      attemptDelivery(queueEntry);

      logger.info("correctly delivered mail " + queueEntry.getUuid() + " - "
          + queueEntry.getMail().getSubject());

      queueEntry.setStatus(MailStatus.SENT);

      CosmoMailDeliveryResult result = new CosmoMailDeliveryResult();
      result.setFailedAttempts(queueEntry.getFailedAttempts());
      result.setMail(queueEntry.getMail());
      result.setStatus(MailStatus.SENT);
      result.setUuid(queueEntry.getUuid());

      queueEntry.getResolver().complete(result);

    } catch (Throwable e) { // NOSONAR
      handleDeliveryFailure(queueEntry, e);
    }
  }

  private void handleDeliveryFailure(CosmoMailQueueEntry queueEntry, Throwable e) {
    logger.error("delivery failed for mail " + queueEntry.getUuid() + ": " + e.getMessage(), e);

    queueEntry.setFailedAttempts(queueEntry.getFailedAttempts() + 1);

    logger.debug("delivery failed for mail " + queueEntry.getUuid() + " for "
        + queueEntry.getFailedAttempts() + " times");

    if (queueEntry.getFailedAttempts() <= maxRetries) {
      // schedule with delay

      long delay = retryDelay != null ? retryDelay : 0;

      if (retryLinearBackoffFactor != null && retryLinearBackoffFactor > 0.0) {
        delay += Math
            .round(retryDelay * (queueEntry.getFailedAttempts() - 1) * retryLinearBackoffFactor);
      }

      if (retryExponentialBackoffFactor != null && retryExponentialBackoffFactor > 0.0) {
        delay += Math.round(retryDelay
            * (Math.pow(retryExponentialBackoffFactor, queueEntry.getFailedAttempts() - 1d) - 1));
      }

      logger.debug(
          "delivery for mail " + queueEntry.getUuid() + " will be reattempted in " + delay + " ms");

      queueEntry.setStatus(MailStatus.REQUEUED);

      mailQueueProcessor.schedule(() -> {
        logger.debug("attempting scheduled re-delivery #" + (queueEntry.getFailedAttempts())
            + " of mail " + queueEntry.getUuid());
        attemptDeliveryOrRequeue(queueEntry);
      }, delay, TimeUnit.MILLISECONDS);

    } else {
      // failed completely

      queueEntry.setStatus(MailStatus.FAILED);

      logger.error("delivery for mail " + queueEntry.getUuid()
      + " failed and max retries reached. Dropping message now.");

      queueEntry.getResolver().completeExceptionally(e);


    }
  }

  private void attemptDelivery(CosmoMailQueueEntry queueEntry)
      throws UnsupportedEncodingException, MessagingException {

    CosmoMail mail = queueEntry.getMail();

    logger.debug("attempting delivery of mail " + queueEntry.getUuid() + " - " + mail.getSubject());

    StringBuilder textBuilder = new StringBuilder();
    textBuilder
    .append(mail.getText() != null ? mail.getText() : "");

    StringBuilder htmlBuilder = new StringBuilder();
    htmlBuilder
    .append(mail.getText() != null ? mail.getText().replaceAll("(\r\n|\n)", "<br />") : "");

    MimeMessage message = buildMimeMessage();

    message.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));

    message.setSubject(mail.getSubject());
    message.setContent(mail.getText(), MAIL_CONTENT_TYPE);

    for (String addr : mail.getTo()) {
      if (!StringUtils.isBlank(addr)) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
      }
    }

    for (String addr : mail.getCc()) {
      if (!StringUtils.isBlank(addr)) {
        message.addRecipient(Message.RecipientType.CC, new InternetAddress(addr));
      }
    }

    for (String addr : mail.getBcc()) {
      if (!StringUtils.isBlank(addr)) {
        message.addRecipient(Message.RecipientType.BCC, new InternetAddress(addr));
      }
    }

    List<CosmoMailAttachment> inlineAttachments =
        CollectionUtils.isEmpty(mail.getAttachments()) ? Collections.emptyList()
            : mail.getAttachments().stream().filter(a -> Boolean.TRUE.equals(a.getAsLink()))
            .collect(Collectors.toList());

    List<CosmoMailAttachment> embeddedAttachments =
        CollectionUtils.isEmpty(mail.getAttachments()) ? Collections.emptyList()
            : mail.getAttachments().stream().filter(a -> !(Boolean.TRUE.equals(a.getAsLink())))
            .collect(Collectors.toList());

    if (!inlineAttachments.isEmpty()) {
      htmlBuilder.append("<br /><br /><b>File allegati:</b>");
      textBuilder.append("\r\n\r\nFile allegati:");

      for (CosmoMailAttachment attachment : inlineAttachments) {
        //@formatter:off
        htmlBuilder
        .append("<br /><a href=\"")
        .append(attachment.getUrl())
        .append("\" target=\"_blank\">")
        .append(attachment.getFileName())
        .append("</a>");
        //@formatter:on

        //@formatter:off
        textBuilder
        .append("\r\n")
        .append(attachment.getFileName())
        .append(": ")
        .append(attachment.getUrl());
        //@formatter:on
      }
    }

    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    if (!embeddedAttachments.isEmpty()) {
      for (CosmoMailAttachment attachment : embeddedAttachments) {
        try {
          helper.addAttachment(attachment.getFileName(), new UrlResource(attachment.getUrl()));
        } catch (MalformedURLException | MessagingException e) {
          throw ExceptionUtils.toChecked(e);
        }
      }
    }

    helper.setText(textBuilder.toString(), htmlBuilder.toString());

    if (this.transportDelegate != null) {
      logger.debug("delivering mail " + queueEntry.getUuid() + " via provided transport delegate");
      this.transportDelegate.send(message);
    } else {
      Transport.send(message);
    }
  }

  private MimeMessage buildMimeMessage() {

    Properties properties = System.getProperties();

    properties.put(PROPERTY_MAIL_TRANSPORT_PROTOCOL, transportProtocol);
    properties.put(PROPERTY_MAIL_SMTP_AUTH, enableAuthentication);
    properties.put(PROPERTY_MAIL_SMTP_STARTTLS_ENABLE, enableStartTLS);
    properties.put(PROPERTY_MAIL_SMTP_HOST, server);
    properties.put(PROPERTY_MAIL_SMTP_PORT, port != null ? port.toString() : 25);
    properties.put(PROPERTY_MAIL_SMTP_SSL_TRUST, "*");

    Session session = null;

    if (!StringUtils.isBlank(username)) {
      properties.put(PROPERTY_MAIL_SMTP_AUTH, "true");
      session = Session.getInstance(properties, new CosmoMailAuthenticator(username, password));
    } else {
      session = Session.getInstance(properties);
    }

    return new MimeMessage(session);
  }

  /**
   * Creates builder to build {@link DefaultCosmoMailerImpl}.
   *
   * @return created builder
   */
  public static IServerStage builder() {
    return new Builder();
  }

  public interface IServerStage {
    public IBuildStage withServer(String server);
  }

  public interface IBuildStage {
    public IBuildStage withPort(Integer port);

    public IBuildStage withUsername(String username);

    public IBuildStage withPassword(String password);

    public IBuildStage withEnableAuthentication(Boolean enableAuthentication);

    public IBuildStage withTransportProtocol(String transportProtocol);

    public IBuildStage withEnableStartTLS(Boolean enableStartTLS);

    public IBuildStage withMaxRetries(Integer maxRetries);

    public IBuildStage withRetryDelay(Integer retryDelay);

    public IBuildStage withRetryLinearBackoffFactor(Double retryLinearBackoffFactor);

    public IBuildStage withRetryExponentialBackoffFactor(Double retryExponentialBackoffFactor);

    public IBuildStage withLoggingPrefix(String loggingPrefix);

    public IBuildStage withDebouncePeriod(Integer debouncePeriod);

    public IBuildStage withTransportDelegate(CosmoMailTransportDelegate transportDelegate);

    public DefaultCosmoMailerImpl build();
  }

  /**
   * Builder to build {@link DefaultCosmoMailerImpl}.
   */
  public static final class Builder implements IServerStage, IBuildStage {
    private String server;
    private Integer port = 25;
    private String username;
    private String password;
    private Boolean enableAuthentication = false;
    private String transportProtocol = "smtp";
    private Boolean enableStartTLS = false;
    private Integer maxRetries = 5;
    private Integer retryDelay = 500;
    private Double retryLinearBackoffFactor = 0.0;
    private Double retryExponentialBackoffFactor = 0.0;
    private String loggingPrefix;
    private Integer debouncePeriod = 30;
    private CosmoMailTransportDelegate transportDelegate;

    private Builder() {}

    @Override
    public IBuildStage withServer(String server) {
      this.server = server;
      return this;
    }

    @Override
    public IBuildStage withPort(Integer port) {
      this.port = port;
      return this;
    }

    @Override
    public IBuildStage withUsername(String username) {
      this.username = username;
      return this;
    }

    @Override
    public IBuildStage withPassword(String password) {
      this.password = password;
      return this;
    }

    @Override
    public IBuildStage withEnableAuthentication(Boolean enableAuthentication) {
      this.enableAuthentication = enableAuthentication;
      return this;
    }

    @Override
    public IBuildStage withTransportProtocol(String transportProtocol) {
      this.transportProtocol = transportProtocol;
      return this;
    }

    @Override
    public IBuildStage withEnableStartTLS(Boolean enableStartTLS) {
      this.enableStartTLS = enableStartTLS;
      return this;
    }

    @Override
    public IBuildStage withMaxRetries(Integer maxRetries) {
      this.maxRetries = maxRetries;
      return this;
    }

    @Override
    public IBuildStage withRetryDelay(Integer retryDelay) {
      this.retryDelay = retryDelay;
      return this;
    }

    @Override
    public IBuildStage withRetryLinearBackoffFactor(Double retryLinearBackoffFactor) {
      this.retryLinearBackoffFactor = retryLinearBackoffFactor;
      return this;
    }

    @Override
    public IBuildStage withRetryExponentialBackoffFactor(Double retryExponentialBackoffFactor) {
      this.retryExponentialBackoffFactor = retryExponentialBackoffFactor;
      return this;
    }

    @Override
    public IBuildStage withLoggingPrefix(String loggingPrefix) {
      this.loggingPrefix = loggingPrefix;
      return this;
    }

    @Override
    public IBuildStage withDebouncePeriod(Integer debouncePeriod) {
      this.debouncePeriod = debouncePeriod;
      return this;
    }

    @Override
    public IBuildStage withTransportDelegate(CosmoMailTransportDelegate transportDelegate) {
      this.transportDelegate = transportDelegate;
      return this;
    }

    @Override
    public DefaultCosmoMailerImpl build() {
      return new DefaultCosmoMailerImpl(this);
    }
  }


}
