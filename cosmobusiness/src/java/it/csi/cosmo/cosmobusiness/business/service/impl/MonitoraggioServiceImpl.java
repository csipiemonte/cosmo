/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.business.service.MailService;
import it.csi.cosmo.cosmobusiness.business.service.MonitoraggioService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmobusiness.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;

@Service
@Transactional
public class MonitoraggioServiceImpl implements MonitoraggioService {

  private static final String JOB_LOCK_RESOURCE_CODE = "MONITORAGGIO_JOB_LOCK";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  LockService lockService;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheFeignClient;

  @Autowired
  protected TransactionService transactionService;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private MailService mailService;

  public List<Pratica> monitoraggioPratiche(BatchExecutionContext context) {
    return this.lockService.executeLocking(lock -> monitoraggioPraticheLock(lock, context),
        LockAcquisitionRequest.builder().withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
            .withRitardoRetry(500L).withTimeout(2000L).withDurata(5 * 60 * 1000L).build());
  }

  private List<Pratica> monitoraggioPraticheLock(CosmoTLock lock, BatchExecutionContext context) {
    String method = "monitoraggioPraticheLock";
    logger.info(method, "start lock per monitoraggio pratiche");

    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di monitoraggio senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di monitoraggio con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }

    MonitoraggioContext monitoraggioContext = newTransferContext();
    monitoraggioContext.batchContext = context;

    monitoraggio(monitoraggioContext);

    logger.info(method, "end lock per monitoraggio pratiche");

    return monitoraggioContext.pratiche;
  }

  private void monitoraggio(MonitoraggioContext context) {
    final var methodName = "monitoraggio";
    logger.info(methodName, "start monitoraggio");

    controlloPraticheSenzaProcesso(context);

    invioMailMonitoraggio(context);

    logger.info(methodName, "end monitoraggio");
  }

  private void controlloPraticheSenzaProcesso(MonitoraggioContext context) {
    final var methodName = "controlloPraticheSenzaProcesso";
    logger.info(methodName, "start controllo delle pratiche senza processo");

    try {
      getPraticheNoLink(context);
    } catch (Exception e) {
      logger.error(methodName,
          "errore nel recupero delle pratiche con link null " + e.getMessage());
    }
    if (!context.pratiche.isEmpty()) {
      inTransaction(() -> recuperoProcessiPraticheSenzaLink(context));
    }

    logger.info(methodName, "end controllo delle pratiche senza processo");
  }

  private void recuperoProcessiPraticheSenzaLink(MonitoraggioContext context) {
    final var method = "recuperoProcessiPraticheSenzaLink";
    logger.info(method, "recupero processi pratiche senza link");

    StringBuilder builder = new StringBuilder();
    builder.append("<b>Pratiche senza link:</b> " + "\n");
    context.pratiche.forEach(pratica -> {
      try {
        ProcessInstanceWrapper wrapper = cosmoCmmnFeignClient
            .getHistoricProcessInstancesByBusinessKey(String.valueOf(pratica.getId()));
        if (wrapper != null && wrapper.getData() != null && !wrapper.getData().isEmpty()) {
          builder.append("La pratica " + pratica.getOggetto() + " ");
          builder.append("con id " + "<b>" + pratica.getId() + "</b>" + " ");
          builder.append("non ha un processo associato. Il processo associato dovrebbe essere: "
              + "<b>" + wrapper.getData().get(0).getId() + "</b>" + "\n");
          context.bodyMail = builder.toString();
        }
      } catch (Exception e) {
        logger.error(method, "errore nel recupero del processo per la pratica con id: "
            + pratica.getId() + e.getMessage());
      }
    });
    logger.info(method, "end recupero processi pratiche senza link");
  }

  private List<Pratica> getPraticheNoLink(MonitoraggioContext context) {
    final var method = "getPraticheNoLink";
    logger.info(method, "start get lista pratiche con link null");

    PraticheNoLinkResponse response = cosmoPraticheFeignClient.getPraticheNoLink();
    context.pratiche = response.getPratiche();

    logger.info(method, "end metodo get lista pratiche con link null");
    return response.getPratiche();
  }

  protected static class MonitoraggioContext {
    BatchExecutionContext batchContext = null;
    Map<Long, Throwable> errori = new HashMap<>();
    List<Pratica> pratiche = new ArrayList<>();
    String bodyMail = "";
  }

  public MonitoraggioContext newTransferContext() {
    return new MonitoraggioContext();
  }

  protected TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return transactionService.inTransaction(task);
  }

  private void invioMailMonitoraggio(MonitoraggioContext context) {
    final var method = "invioMailMonitoraggio";
    logger.info(method, "start invio mail pratiche monitorate");

    if (!StringUtils.isBlank(context.bodyMail)) {
      String subject = "Batch Monitoraggio - Resoconto";
      mailService.inviaMailAssistenza(subject, context.bodyMail);
    } else {
      logger.info(method, "nessun pratica da monitorare");
    }
    logger.info(method, "end invio mail pratiche monitorate");
  }
}
