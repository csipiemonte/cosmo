/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmonotifications.business.batch.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.batch.model.BatchReportedEvent;
import it.csi.cosmo.common.batch.model.BatchReportedEventLevel;
import it.csi.cosmo.common.entities.CosmoLEsecuzioneBatch;
import it.csi.cosmo.common.entities.CosmoLEsecuzioneBatch_;
import it.csi.cosmo.common.entities.CosmoLSegnalazioneBatch;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmonotifications.business.batch.BatchInterface;
import it.csi.cosmo.cosmonotifications.business.batch.InvioNotificheMailBatch;
import it.csi.cosmo.cosmonotifications.business.service.ClusterService;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmonotifications.business.service.TransactionService;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoLEsecuzioneBatchRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoLSegnalazioneBatchRepository;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;


@Service
@PropertySource("classpath:config.properties")
public class BatchSchedulerServiceImpl
implements InitializingBean, DisposableBean {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMONOTIFICATIONS_BATCH_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private ClusterService clusterService;

  @Autowired
  protected ConfigurazioneService configurazioneService;

  @Autowired
  private InvioNotificheMailBatch invioNotificheMailBatch;

  @Autowired
  protected TransactionService transactionService;

  @Autowired
  private CosmoLEsecuzioneBatchRepository cosmoLEsecuzioneBatchRepository;

  @Autowired
  private CosmoLSegnalazioneBatchRepository cosmoLSegnalazioneBatchRepository;

  private Map<BatchInterface, Boolean> runningMap = new ConcurrentHashMap<>();

  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  @Override
  public void destroy() throws Exception {
    // NOP
  }

  @Scheduled(cron = "${batch.invioNotificheMail.timeInterval}")
  public void launchInvioNotificheMailBatch() {
    launch(invioNotificheMailBatch, "InvioNotificheMail");
  }

  private void launch(BatchInterface executor, String batchName) {
    String method = batchName;

    if (!executor.isEnabled()) {
      logger.debug(method, "skip dell'esecuzione in quanto disabilitata da configurazione");
      return;
    }

    if (clusterService == null || !clusterService.isMaster()) {
      logger.debug(method, "skip dell'esecuzione in quanto istanza non master");
      return;
    }

    if (runningMap.containsKey(executor)) {
      logger.warn(method, "skip dell'esecuzione in quanto un altra esecuzione e' ancora in corso");
      return;
    }

    runningMap.put(executor, Boolean.TRUE);
    logger.info(method, "START");

    BatchExecutionContext context = buildContext(batchName);

    try {
      executor.execute(context);
    } catch (Throwable t) { // NOSONAR
      logger.error(method, "errore nell'esecuzione del batch: " + t.getMessage(), t);

      context.reportError(
          String.format("L'esecuzione del batch iniziata alle %s e' fallita: %s",
              context.getStartedAt().format(DateTimeFormatter.ofPattern("HH:mm")), t.getMessage()),
          t);

    } finally {
      logger.info(method, "END");
      runningMap.remove(executor);

      transactionService.inTransaction(() -> logEsecuzione(context));
      transactionService.inTransaction(() -> eliminaLogObsoleti(context));

    }
  }

  protected void logEsecuzione(BatchExecutionContext context) {
    Instant now = Instant.now();
    CosmoLEsecuzioneBatch entity = new CosmoLEsecuzioneBatch();
    entity.setDtEvento(Timestamp.from(now));
    entity.setCodiceBatch(context.getBatchName());
    entity.setDataAvvio(Timestamp.valueOf(context.getStartedAt()));
    entity.setDataFine(Timestamp.from(now));

    if (context.getEvents().stream().anyMatch(e -> BatchReportedEventLevel.ERROR == e.getLevel())) {
      entity.setCodiceEsito("ERRORE");
    } else if (context.getEvents().stream()
        .anyMatch(e -> BatchReportedEventLevel.WARNING == e.getLevel())) {
      entity.setCodiceEsito("WARNING");
    } else {
      entity.setCodiceEsito("OK");
    }

    entity = cosmoLEsecuzioneBatchRepository.insert(entity);

    for (BatchReportedEvent event : context.getEvents()) {
      CosmoLSegnalazioneBatch entitySegnalazione = new CosmoLSegnalazioneBatch();
      entitySegnalazione.setEsecuzione(entity);
      entitySegnalazione.setDtEvento(Timestamp.valueOf(event.getTimestamp()));
      entitySegnalazione.setLivello(event.getLevel());
      entitySegnalazione.setMessaggio(event.getMessage());
      entitySegnalazione.setDettagli(
          event.getError() != null ? ExceptionUtils.exceptionToString(event.getError()) : null);

      cosmoLSegnalazioneBatchRepository.insert(entitySegnalazione);
    }
  }

  protected void eliminaLogObsoleti(BatchExecutionContext context) {

    Timestamp treshold = Timestamp.from(Instant.now().minus(31, ChronoUnit.DAYS));

    //@formatter:off
    cosmoLEsecuzioneBatchRepository.purge((root, cq, cb) -> cb.and(
        cb.equal(root.get(CosmoLEsecuzioneBatch_.codiceBatch), context.getBatchName()),
        cb.equal(root.get(CosmoLEsecuzioneBatch_.codiceEsito), "OK"),
        cb.isNotNull(root.get(CosmoLEsecuzioneBatch_.dataFine)),
        cb.isNotNull(root.get(CosmoLEsecuzioneBatch_.dtEvento)),
        cb.lessThan(root.get(CosmoLEsecuzioneBatch_.dtEvento), treshold)
    ), 10, 10);
    //@formatter:on
  }



  protected BatchExecutionContext buildContext(String batchName) {
    return BatchExecutionContext.builder()
        .withBatchName(batchName)
        .withStartedAt(LocalDateTime.now())
        .build();
  }

}
