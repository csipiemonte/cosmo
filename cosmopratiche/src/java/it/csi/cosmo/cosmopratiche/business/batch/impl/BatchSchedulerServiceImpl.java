/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmopratiche.business.batch.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.batch.model.BatchReportedEvent;
import it.csi.cosmo.common.batch.model.BatchReportedEvent.Builder;
import it.csi.cosmo.common.batch.model.BatchReportedEventLevel;
import it.csi.cosmo.common.entities.CosmoLEsecuzioneBatch;
import it.csi.cosmo.common.entities.CosmoLEsecuzioneBatch_;
import it.csi.cosmo.common.entities.CosmoLSegnalazioneBatch;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.cosmopratiche.business.batch.BatchInterface;
import it.csi.cosmo.cosmopratiche.business.batch.CaricamentoPraticheBatch;
import it.csi.cosmo.cosmopratiche.business.service.ClusterService;
import it.csi.cosmo.cosmopratiche.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmopratiche.business.service.MailService;
import it.csi.cosmo.cosmopratiche.business.service.TransactionService;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoLEsecuzioneBatchRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoLSegnalazioneBatchRepository;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;


@Service
@PropertySource("classpath:config.properties")
public class BatchSchedulerServiceImpl
implements InitializingBean, DisposableBean {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BATCH_LOG_CATEGORY,
          this.getClass().getSimpleName());

  @Autowired
  private ClusterService clusterService;

  @Autowired
  protected ConfigurazioneService configurazioneService;

  @Autowired
  protected MailService mailService;

  @Autowired
  private CosmoLEsecuzioneBatchRepository cosmoLEsecuzioneBatchRepository;

  @Autowired
  private CosmoLSegnalazioneBatchRepository cosmoLSegnalazioneBatchRepository;

  @Autowired
  protected TransactionService transactionService;

  private Map<BatchInterface, Boolean> runningMap = new ConcurrentHashMap<>();
  
  private String result = "";


  @Override
  public void afterPropertiesSet() throws Exception {
    // NOP
  }

  @Override
  public void destroy() throws Exception {
    // NOP
  }

  @Autowired
  private CaricamentoPraticheBatch caricamentoPraticheBatch;


  @Scheduled(cron = "${batch.caricamentopratiche.timeInterval}")
  public void launchBatchCaricamentoPratiche() {
    launch(caricamentoPraticheBatch, "caricamentoPraticheBatch");
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
      result = executor.execute(context);
    } catch (Throwable t) { // NOSONAR
      logger.error(method, "errore nell'esecuzione del batch: " + t.getMessage(), t);

      context.reportError(
          String.format("L'esecuzione del batch iniziata alle %s e' fallita: %s",
              context.getStartedAt().format(DateTimeFormatter.ofPattern("HH:mm")), t.getMessage()),
          t);

    } finally {
      logger.info(method, "END");
      runningMap.remove(executor);

      transactionService.inTransaction(() -> logEsecuzione(context, result));
      transactionService.inTransaction(() -> eliminaLogObsoleti(context));

      segnalaAssistenza(context);
    }
  }

  protected void logEsecuzione(BatchExecutionContext context, String result) {
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
      Builder builder = BatchReportedEvent.builder();
      if("ERROR".equals(result)) {
        builder.withLevel(BatchReportedEventLevel.ERROR);
      }else {
        builder.withLevel(BatchReportedEventLevel.WARNING);
      }
      
      BatchReportedEvent batchErrorOrWarning = builder.build();
      
      entitySegnalazione.setEsecuzione(entity);
      entitySegnalazione.setDtEvento(Timestamp.valueOf(event.getTimestamp()));
      entitySegnalazione.setLivello(batchErrorOrWarning.getLevel());
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

  protected void segnalaAssistenza(BatchExecutionContext context) {
    final var method = "segnalaAssistenza";

    if (context.getEvents().isEmpty()) {
      return;
    }

    Collection<BatchReportedEvent> errori = context.getEvents().stream().filter(
        e -> BatchReportedEventLevel.ERROR == e.getLevel())
        .collect(Collectors.toCollection(LinkedList::new));

    if (errori.isEmpty()) {
      return;
    }

    logger.info(method,
        "sono presenti segnalazioni di warning o errore, invio segnalazione all'assistenza");

    String builtSubject =
        String.format("Batch %s - ERRORE nell'esecuzione delle %s",
            context.getBatchName(),
            context.getStartedAt().format(DateTimeFormatter.ofPattern("HH:mm")));

    StringBuilder mailBody = new StringBuilder();
    mailBody
    .append("L'esecuzione del batch ").append(context.getBatchName()).append(" iniziata alle ")
    .append(context.getStartedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
    .append(" ha riportato degli ERRORI.")
    .append("<br/><br/>");

    for (BatchReportedEvent error : errori) {
      mailBody
      .append(" - [")
      .append(error.getLevel().name())
      .append("] ")
      .append(error.getMessage())
      .append("<br/>");
    }

    List<BatchReportedEvent> withStackTraces = errori.stream()
        .filter(e -> e.getError() != null)
        .collect(Collectors.toCollection(LinkedList::new));

    if (!withStackTraces.isEmpty()) {
      mailBody.append("<br/>Dettaglio degli errori: <br/>");

      for (BatchReportedEvent entry : withStackTraces) {
        mailBody
        .append(" - [")
        .append(entry.getLevel().name())
        .append("] ")
        .append(entry.getMessage())
        .append("<br/>")
        .append(ExceptionUtils.exceptionToString(entry.getError()).replaceAll("(\r\n|\n)", "<br />"))
        .append("<br/><br/>");
      }
    }

    mailService.inviaMailAssistenza(builtSubject, mailBody.toString());
  }

  protected BatchExecutionContext buildContext(String batchName) {
    return BatchExecutionContext.builder()
        .withBatchName(batchName)
        .withStartedAt(LocalDateTime.now())
        .build();
  }

}
