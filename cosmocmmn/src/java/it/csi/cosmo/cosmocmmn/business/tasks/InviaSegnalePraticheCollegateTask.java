/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.engine.runtime.Execution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoPratichePraticheFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;


public class InviaSegnalePraticheCollegateTask extends ParentFlowableTask {

  private static final boolean DEFAULT_SEQUENZIALE = false;

  private static final int MAX_CONCURRENT_PROCESSES = 4;

  private static final int MAX_EXECUTION_TIME_MS = 30 * 60 * 1000;

  /**
   * parametri forniti in input da process design
   */
  private Expression sequenziale;

  private Expression codiceSegnale;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "invio segnale alle pratiche collegate per businessKey {}", businessKey);
    Long idPratica = Long.valueOf(businessKey);

    boolean isSequenziale = getSequenziale(execution);
    var praticheCollegate = getPraticheCollegate(idPratica);

    if (praticheCollegate.isEmpty()) {
      logger.warn(method, "nessuna pratica collegata a cui inviare il segnale per businessKey {}",
          businessKey);
      return;
    }

    var runtimeService = Context.getProcessEngineConfiguration().getRuntimeService();

    if (isSequenziale) {
      inviaSequenziale(execution, idPratica, praticheCollegate, runtimeService);
    } else {
      inviaParallelo(execution, idPratica, praticheCollegate, runtimeService);
    }
  }

  private List<PraticaInRelazione> getPraticheCollegate(Long idPratica) {
    ValidationUtils.require(idPratica, "idPratica");
    CosmoPratichePraticheFeignClient client = this.getBean(CosmoPratichePraticheFeignClient.class);

    var result = client.getPraticheIdPraticaRelazioni(idPratica.intValue());
    if (result == null) {
      return Collections.emptyList();
    }
    return Arrays.asList(result);
  }

  protected void inviaSequenziale(DelegateExecution execution, Long idPratica,
      List<PraticaInRelazione> praticheCollegate, RuntimeService runtimeService) {
    final var method = "inviaSincronoSequenziale";

    String codiceSegnale = getCodiceSegnale(execution);

    for (var praticaCollegata : praticheCollegate) {
      try {
        logger.info(method, "invio segnale pratica collegata {}",
            praticaCollegata.getPratica().getId());

        inviaSingolaPratica(codiceSegnale, praticaCollegata, runtimeService);

        logger.info(method, "invio segnale completato per pratica collegata {}",
            praticaCollegata.getPratica().getId());
      } catch (Exception e) {
        logger.error(method,
            "invio segnale fallito per pratica collegata " + praticaCollegata.getPratica().getId(),
            e);
        throw e;
      }
    }
  }

  protected void inviaParallelo(DelegateExecution execution, Long idPratica,
      List<PraticaInRelazione> praticheCollegate, RuntimeService runtimeService) {
    final var method = "inviaSincronoParallelo";

    String codiceSegnale = getCodiceSegnale(execution);

    // al massimo avanzo MAX_CONCURRENT_PROCESSES pratiche alla volta
    ExecutorService executor =
        Executors.newFixedThreadPool(Math.min(praticheCollegate.size(), MAX_CONCURRENT_PROCESSES));

    List<Future<?>> tasks = new LinkedList<>();
    List<PraticaInRelazione> inviate = new LinkedList<>();

    for (var praticaCollegata : praticheCollegate) {
      inviate.add(praticaCollegata);
      var future = executor.submit(() -> {
        try {
          logger.info(method, "invio segnale pratica collegata {}",
              praticaCollegata.getPratica().getId());

          inviaSingolaPratica(codiceSegnale, praticaCollegata, runtimeService);

          logger.info(method, "invio segnale completato per pratica collegata {}",
              praticaCollegata.getPratica().getId());
        } catch (Exception e) {
          logger.error(method, "invio segnale fallito per pratica collegata "
              + praticaCollegata.getPratica().getId(), e);
          throw e;
        }
      });
      tasks.add(future);
    }

    executor.shutdown();

    logger.info(method, "attendo fine avanzamento di tutte le pratiche collegate a {}", idPratica);
    try {
      if (!executor.awaitTermination(MAX_EXECUTION_TIME_MS, TimeUnit.MILLISECONDS)) {
        logger.warn(method,
            "attesa fine avanzamento di tutte le pratiche collegate a {} in timeout, eseguo shutdown immediato",
            idPratica);
        executor.shutdownNow();
        throw new InternalServerException(
            "Timeout nell'attesa fine avanzamento di tutte le pratiche collegate");
      } else {
        logger.info(method, "fine avanzamento di tutte le pratiche collegate a {}", idPratica);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.warn(method,
          "executor did not complete in MAX_EXECUTION_TIME_MS, got interruption signal (maybe server is shutting down?)");
    }

    // get di tutti i task - se almeno uno e' fallito rilancio eccezione
    logger.info(method, "verifico risultato avanzamento delle pratiche collegate a {}", idPratica);
    for (int i = 0; i < tasks.size(); i++) {
      var task = tasks.get(i);
      var inviata = inviate.get(i);
      try {
        task.get();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.warn(method,
            "a single task did not complete, got interruption signal (maybe server is shutting down?)");
      } catch (ExecutionException e) {
        if (e.getCause() instanceof ManagedException) {
          throw new InternalServerException("Errore nell'invio del segnale alla pratica "
              + inviata.getPratica().getId() + ": " + e.getCause().getMessage(), e.getCause());
        }
        if (e.getCause() instanceof RuntimeException) {
          throw (RuntimeException) e.getCause();
        }
        throw ExceptionUtils.toChecked(e.getCause());
      }
    }
    logger.info(method,
        "verificato con successo risultato avanzamento delle pratiche collegate a {}", idPratica);
  }

  void inviaSingolaPratica(String codiceSegnale, PraticaInRelazione praticaCollegata,
      RuntimeService runtimeService) {
    final var methodName = "inviaSincronoPratica";

    String processId = ObjectUtils.getIdFromLink(praticaCollegata.getPratica().getLinkPratica());

    var processInstances =
        runtimeService.createProcessInstanceQuery().processInstanceId(processId).active().list();

    if (processInstances.isEmpty()) {
      String msg = "Nessun processo attivo corrispondente all'identificativo fornito";
      logger.warn(methodName, msg);
      return;
    }

    if (processInstances.size() > 1) {
      String msg = "Troppi processi (" + processInstances.size() + ") corrispondenti al processId "
          + processId;
      logger.warn(methodName, msg);
      throw new InternalServerException(msg);
    }

    var processInstance = processInstances.get(0);
    var waitingExecutions =
        runtimeService.createExecutionQuery().processInstanceId(processInstance.getId())
            .messageEventSubscriptionName(codiceSegnale).list();

    logger.info(methodName, "iniziata elaborazione asincrona del segnale {} per il processo {}",
        codiceSegnale, processId);

    if (waitingExecutions.isEmpty()) {
      logger.info(methodName, "nessuna esecuzione in attesa nel processo {} per il segnale {}",
          processId, codiceSegnale);
      return;
    }

    logger.info(methodName, "{} esecuzioni in attesa nel processo {} per il segnale {}",
        waitingExecutions.size(), processId, codiceSegnale);

    for (Execution execution : waitingExecutions) {
      logger.info(methodName, "invio del segnale {} all'esecuzione {} nel processo {}",
          codiceSegnale, execution.getId(), processId);

      runtimeService.messageEventReceived(codiceSegnale, execution.getId());

      logger.info(methodName, "inviato il segnale {} all'esecuzione {} nel processo {}",
          codiceSegnale, execution.getId(), processId);

      logger.info(methodName, "terminata elaborazione asincrona del segnale {} per il processo {}",
          codiceSegnale, processId);
    }
  }

  public boolean getSequenziale(DelegateExecution execution) {
    if (sequenziale == null) {
      return DEFAULT_SEQUENZIALE;
    }
    String raw = (String) sequenziale.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return DEFAULT_SEQUENZIALE;
    }
    return Boolean.valueOf(raw.strip());
  }

  private String getCodiceSegnale(DelegateExecution execution) {
    if (codiceSegnale == null) {
      return null;
    }
    String raw = (String) codiceSegnale.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  public void setSequenziale(Expression sequenziale) {
    this.sequenziale = sequenziale;
  }
}
