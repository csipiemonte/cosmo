/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.JoinType;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.async.exception.LongTaskFailedException;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRFormLogicoIstanzaFunzionalita;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.process.model.AfterCompleteHookContext;
import it.csi.cosmo.cosmobusiness.business.process.model.BeforeCompleteHookContext;
import it.csi.cosmo.cosmobusiness.business.process.model.FunzionalitaFormLogicoHooks;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.LavorazionePraticaService;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.business.service.MailService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessRecoveryService;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequestAssegnazione;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.specifications.CosmoRAttivitaAssociazioneSpecifications;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnAsyncFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnTaskFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequest;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskRequestAssegnazione;

@Service
public class LavorazionePraticaServiceImpl implements LavorazionePraticaService {

  /**
   *
   */
  private static final String FLOWABLE_ACTION_COMPLETE = "complete";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoCmmnAsyncFeignClient cosmoCmmnAsyncFeignClient;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private LockService lockService;

  @Autowired
  private MailService mailService;

  @Autowired
  private CosmoCmmnTaskFeignClient cosmoCmmnTaskFeignClient;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private ProcessRecoveryService processRecoveryService;

  /**
   * chiamato quando da frontend si clicca su 'conferma'
   *
   * gestisce la conferma di un task come operazione asincrona
   *
   * salva le variabili sul task, aggiorna la data modifica e completa il task su flowable
   */
  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public RiferimentoOperazioneAsincrona postPraticaAttivitaConferma(Long idPratica, Long idAttivita,
      Task body) {
    logger.debug("postPraticaAttivitaConferma", "confermo lavorazione per attivita {}", idAttivita);

    // valida dati di input
    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getVariables(), "variables");

    // fetch with parent
    var attivita = Optional.ofNullable(cosmoTAttivitaRepository.findOne((root, cq, cb) -> {
      root.fetch(CosmoTAttivita_.parent, JoinType.LEFT);
      root.fetch(CosmoTAttivita_.cosmoTPratica, JoinType.INNER);

      //@formatter:off
      return cb.and(
          cb.equal(root.get(CosmoTAttivita_.id), idAttivita),
          cb.equal(root.get(CosmoTAttivita_.cosmoTPratica).get(CosmoTPratica_.id), idPratica)
          );
      //@formatter:on
    })).orElseThrow(NotFoundException::new);

    LongTaskFuture<?> asyncTask = asyncTaskService.start("Conferma del task", task -> {

      eseguiConfermaAttivita(idPratica, attivita, body, true, task);
      return null;
    });

    return riferimentoOperazioneAsincrona(asyncTask);
  }

  @Override
  public void eseguiConfermaAttivita(Long idPratica, CosmoTAttivita attivitaInput, Task body,
      boolean verificaLock, LongTask<Serializable> task) {

    if (attivitaInput.getParent() != null) {
      eseguiConfermaAttivitaCollaborazione(idPratica, attivitaInput, body, verificaLock, task);
      return;
    }

    Timestamp now = Timestamp.from(Instant.now());

    var actor = SecurityUtils.getUtenteCorrente();
    var utente = getUtenteCorrente();
    var fruitore = getFruitoreCorrente();

    Long idAttivita = attivitaInput.getId();
    String taskId = attivitaInput.getTaskId();

    // ottengo informazioni sul task da flowable
    var flowableTask = cosmoCmmnFeignClient.getTaskId(taskId);
    if (flowableTask == null) {
      throw new NotFoundException("Task non trovato");
    }

    task.step(null, step -> {
      CosmoTAttivita found =
          resolveAttivita(idPratica, idAttivita).orElseThrow(NotFoundException::new);

      // verifica che sia consentita la lavorazione dell'attivita'
      requireUtenteNonOspite();
      autorizzaLavorazionePratica(found.getCosmoTPratica());
      autorizzaLavorazioneAttivita(found);

      if (verificaLock) {
        // verifico il lock se richiesto/presente
        transactionService.inTransactionOrThrow(
            () -> lockService.checkLock("@task(" + found.getTaskId() + ")", true));
      }
    });

    HookPointList hooks = task.step(null, step -> {
      // eseguo il touch per aggiornare la data modifica
      CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      this.touchAttivita(attivita);
      return retrieveHooks(attivita);
    });

    if (!hooks.isEmpty()) {

      flowableTask.setVariables(Arrays.asList(cosmoCmmnFeignClient.getTaskVariables(taskId)));
      task.step("Validazione dei dati", step -> {
        // chiamo hook points beforeConfirm
        hooks.forEach(hook -> callHook(hook,
            () -> hook.handler.beforeComplete(
                buildBeforeCompleteHookContext(cosmoTAttivitaRepository.findOne(idAttivita),
                    flowableTask, body, hook.formLogico, hook.istanza))));
      });
    }

    RiferimentoOperazioneAsincrona asyncRef = task.step("Salvataggio dei dati", step -> {
      // eseguo la post su cmmn per aggiornare le variabili
      List<Object> variables = body.getVariables();
      if (variables == null) {
        variables = new ArrayList<>();
      }

      Task payload = new Task();
      payload.setId(taskId);
      payload.setVariables(variables);
      payload.setAction(FLOWABLE_ACTION_COMPLETE);

      return cosmoCmmnAsyncFeignClient.completeTaskById(taskId, payload);
    });

    Exception completeException = null;
    try {
      task.step("Completamento del task", step -> {
        // attendi il completamento del task
        try {
          asyncTaskService.wait(asyncRef.getUuid(), 5 * 60L);
        } catch (LongTaskFailedException e) { // NOSONAR
          step.error("Errore nel completamento del task: " + e.getTask().getErrorMessage());
          throw e;
        } catch (Exception e) {
          step.error("Errore nel completamento del task: " + e.getMessage());
          throw e;
        }
      });
    } catch (Exception e) {
      completeException = e;
    }

    if (completeException != null) {
      task.step("Ripristino della pratica", step -> {
        RecoveryAttemptResult recoveryResult = attemptRecovery(idPratica, actor);

        if (recoveryResult.success && recoveryResult.changed) {
          step.info("Il ripristino del processo associato alla pratica e' riuscito");
          step.sleep(5000);
        } else if (!recoveryResult.success) {
          step.warn("Il ripristino del processo associato alla pratica e' fallito ("
              + recoveryResult.error.getMessage() + ")");
          step.sleep(5000);
        }
      });
      throw ExceptionUtils.toChecked(completeException);
    }

    if (!hooks.isEmpty()) {
      task.step("Esecuzione logiche di processo aggiuntive", step -> {
        // chiamo hook points beforeConfirm

        TaskResponse flowableTaskAfter = cosmoCmmnFeignClient.getHistoricTaskId(taskId);
        flowableTaskAfter
        .setVariables(getProcessInstanceVariables(flowableTaskAfter.getProcessInstanceId()));

        hooks
        .forEach(
            hook -> callHook(hook,
                () -> hook.handler.afterComplete(buildAfterCompleteHookContext(
                    cosmoTAttivitaRepository.findOne(idAttivita), flowableTask,
                    flowableTaskAfter, body, hook.formLogico, hook.istanza))));
      });
    }

    task.step(null, step -> {
      // inserisco il log dell'evento su DB
      var attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      //@formatter:off
      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_COMPLETATA)
          .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata completata.", attivita.getNome()))
          .withPratica(attivita.getCosmoTPratica())
          .withAttivita(attivita)
          .withDtEvento(now)
          .withUtente(utente)
          .withFruitore(fruitore)
          .build());
      //@formatter:on
    });

  }

  protected void eseguiConfermaAttivitaCollaborazione(Long idPratica, CosmoTAttivita attivitaInput,
      Task body, boolean verificaLock, LongTask<Serializable> task) {

    Timestamp now = Timestamp.from(Instant.now());

    var utente = getUtenteCorrente();
    var fruitore = getFruitoreCorrente();

    Long idAttivita = attivitaInput.getId();
    String taskId = attivitaInput.getTaskId();

    CosmoTAttivita attivitaPadre = attivitaInput.getParent();
    String parentTaskId = attivitaPadre.getTaskId();

    task.step(null, step -> {
      CosmoTAttivita found =
          resolveAttivita(idPratica, idAttivita).orElseThrow(NotFoundException::new);

      // verifica che sia consentita la lavorazione dell'attivita'
      requireUtenteNonOspite();
      autorizzaLavorazionePratica(found.getCosmoTPratica());
      autorizzaLavorazioneAttivita(found);

      // lazy load del parent se presente
      CosmoTAttivita parent = found.getParent();
      if (parent != null) {
        parent.getId();
        parent.getCosmoTPratica().getId();
      }

      if (verificaLock) {
        // verifico il lock se richiesto/presente
        transactionService.inTransactionOrThrow(() -> lockService.checkLock(
            "@task(" + (parent != null ? parent.getTaskId() : found.getTaskId()) + ")", true));
      }
    });

    task.step(null, step -> {
      // eseguo il touch per aggiornare la data modifica
      this.touchAttivita(cosmoTAttivitaRepository.findOne(idAttivita));
    });

    task.step("Salvataggio dei dati inseriti", step -> {
      // eseguo la post su cmmn per aggiornare le variabili
      List<Object> variables = body.getVariables();
      if (variables == null) {
        variables = new ArrayList<>();
      }

      Task payload = new Task();
      payload.setId(parentTaskId);
      payload.setVariables(variables);
      payload.setAction(null);

      RiferimentoOperazioneAsincrona asyncRef1 =
          cosmoCmmnAsyncFeignClient.updateTaskById(parentTaskId, payload);

      asyncTaskService.wait(asyncRef1.getUuid(), 5 * 60L);
    });

    task.step("Chiusura della collaborazione", step -> {

      // eseguo la post su cmmn
      Task payload2 = new Task();
      payload2.setId(taskId);
      payload2.setAction(FLOWABLE_ACTION_COMPLETE);

      var asyncRef = cosmoCmmnAsyncFeignClient.completeTaskById(taskId, payload2);

      // attendi il completamento del task
      asyncTaskService.wait(asyncRef.getUuid(), 5 * 60L);
    });

    task.step(null, step -> {
      // inserisco il log dell'evento su DB
      var attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      var attivitaParent = attivita.getParent();
      //@formatter:off
      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_LAVORATA)
          .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata lavorata in collaborazione.", attivitaParent.getNome()))
          .withPratica(attivitaParent.getCosmoTPratica())
          .withAttivita(attivitaParent)
          .withDtEvento(now)
          .withUtente(utente)
          .withFruitore(fruitore)
          .build());

      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_COMPLETATA)
          .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata completata.", attivita.getNome()))
          .withPratica(attivita.getCosmoTPratica())
          .withAttivita(attivita)
          .withDtEvento(now)
          .withUtente(utente)
          .withFruitore(fruitore)
          .build());
      //@formatter:on
    });
  }

  /**
   * chiamato quando da frontend si clicca su 'salva'
   *
   * gestisce il salvataggio di un task come operazione asincrona
   *
   * salva le variabili sul task e aggiorna la data modifica
   */
  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public RiferimentoOperazioneAsincrona postPraticaAttivitaSalva(Long idPratica, Long idAttivita,
      Task body) {

    // valida dati di input
    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getVariables(), "variables");

    // fetch with parent
    var attivita = Optional.ofNullable(cosmoTAttivitaRepository.findOne((root, cq, cb) -> {
      root.fetch(CosmoTAttivita_.parent, JoinType.LEFT);
      root.fetch(CosmoTAttivita_.cosmoTPratica, JoinType.INNER);

      //@formatter:off
      return cb.and(
          cb.equal(root.get(CosmoTAttivita_.id), idAttivita),
          cb.equal(root.get(CosmoTAttivita_.cosmoTPratica).get(CosmoTPratica_.id), idPratica)
          );
      //@formatter:on
    })).orElseThrow(NotFoundException::new);

    LongTaskFuture<?> asyncTask = asyncTaskService.start("Salvataggio del task", task -> {

      eseguiLavorazioneAttivita(idPratica, attivita, body, true, task);
      return null;
    });

    return riferimentoOperazioneAsincrona(asyncTask);
  }

  @Override
  public void eseguiLavorazioneAttivita(Long idPratica, CosmoTAttivita attivitaInput, Task body,
      boolean verificaLock, LongTask<Serializable> task) {

    if (attivitaInput.getParent() != null) {
      eseguiLavorazioneAttivitaCollaborazione(idPratica, attivitaInput, body, verificaLock, task);
      return;
    }

    Timestamp now = Timestamp.from(Instant.now());

    var utente = getUtenteCorrente();
    var fruitore = getFruitoreCorrente();

    Long idAttivita = attivitaInput.getId();
    String taskId = attivitaInput.getTaskId();

    task.step("Verifica dello stato del task", step -> {
      CosmoTAttivita found =
          resolveAttivita(idPratica, idAttivita).orElseThrow(NotFoundException::new);

      // verifica che sia consentita la lavorazione dell'attivita'
      autorizzaLavorazionePratica(found.getCosmoTPratica());
      autorizzaLavorazioneAttivita(found);
      requireUtenteNonOspite();

      if (verificaLock) {
        // verifico il lock se richiesto/presente
        lockService.checkLock("@task(" + found.getTaskId() + ")", true);
      }
    });

    task.step("Salvataggio delle informazioni di audit", step -> {
      // eseguo il touch per aggiornare la data modifica
      this.touchAttivita(cosmoTAttivitaRepository.findOne(idAttivita));
    });

    task.step("Salvataggio dei dati inseriti", step -> {
      // eseguo la post su cmmn per aggiornare le variabili
      List<Object> variables = body.getVariables();
      if (variables == null) {
        variables = new ArrayList<>();
      }

      Task payload = new Task();
      payload.setId(taskId);
      payload.setVariables(variables);
      payload.setAction(null);

      RiferimentoOperazioneAsincrona asyncRef =
          cosmoCmmnAsyncFeignClient.updateTaskById(taskId, payload);

      asyncTaskService.wait(asyncRef.getUuid(), 5 * 60L);
    });

    task.step("Salvataggio delle informazioni di storico", step -> {
      // inserisco il log dell'evento su DB
      var attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      //@formatter:off
      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_LAVORATA)
          .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata lavorata.", attivita.getNome()))
          .withPratica(attivita.getCosmoTPratica())
          .withAttivita(attivita)
          .withDtEvento(now)
          .withUtente(utente)
          .withFruitore(fruitore)
          .build());
      //@formatter:on

      // eseguo il touch per aggiornare la data modifica
      this.touchAttivita(attivita);
    });
  }

  private void eseguiLavorazioneAttivitaCollaborazione(Long idPratica, CosmoTAttivita attivitaInput,
      Task body, boolean verificaLock, LongTask<Serializable> task) {

    Timestamp now = Timestamp.from(Instant.now());

    CosmoTAttivita attivitaPadreInput = attivitaInput.getParent();

    var utente = getUtenteCorrente();
    var fruitore = getFruitoreCorrente();

    Long idAttivitaPadre = attivitaPadreInput.getId();
    Long idAttivita = attivitaInput.getId();
    String parentTaskId = attivitaInput.getParent().getTaskId();

    task.step("Verifica dello stato del task", step -> {
      CosmoTAttivita found =
          resolveAttivita(idPratica, idAttivita).orElseThrow(NotFoundException::new);

      // verifica che sia consentita la lavorazione dell'attivita'
      autorizzaLavorazionePratica(found.getCosmoTPratica());
      autorizzaLavorazioneAttivita(found);
      requireUtenteNonOspite();

      // lazy load del parent se presente
      CosmoTAttivita parent = found.getParent();
      if (parent != null) {
        parent.getId();
        parent.getCosmoTPratica().getId();
      }

      if (verificaLock) {
        // verifico il lock se richiesto/presente
        transactionService.inTransactionOrThrow(() -> lockService.checkLock(
            "@task(" + (parent != null ? parent.getTaskId() : found.getTaskId()) + ")", true));
      }

    });

    task.step("Salvataggio delle informazioni di audit", step -> {
      // eseguo il touch per aggiornare la data modifica
      var attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      var attivitaPadre = cosmoTAttivitaRepository.findOne(idAttivitaPadre);
      this.touchAttivita(attivita);
      this.touchAttivita(attivitaPadre);
    });

    task.step("Salvataggio dei dati inseriti", step -> {
      // eseguo la post su cmmn per aggiornare le variabili
      List<Object> variables = body.getVariables();
      if (variables == null) {
        variables = new ArrayList<>();
      }

      Task payload = new Task();
      payload.setId(parentTaskId);
      payload.setVariables(variables);
      payload.setAction(null);

      RiferimentoOperazioneAsincrona asyncRef =
          cosmoCmmnAsyncFeignClient.updateTaskById(parentTaskId, payload);

      asyncTaskService.wait(asyncRef.getUuid(), 5 * 60L);
    });

    task.step("Salvataggio delle informazioni di storico", step -> {
      // inserisco il log dell'evento su DB
      var attivita = cosmoTAttivitaRepository.findOne(idAttivita);
      var attivitaPadre = cosmoTAttivitaRepository.findOne(idAttivitaPadre);
      //@formatter:off
      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_LAVORATA)
          .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata lavorata.", attivita.getNome()))
          .withPratica(attivita.getCosmoTPratica())
          .withAttivita(attivita)
          .withDtEvento(now)
          .withUtente(utente)
          .withFruitore(fruitore)
          .build());
      //@formatter:on

      // eseguo il touch per aggiornare la data modifica
      this.touchAttivita(attivita);
      this.touchAttivita(attivitaPadre);
    });
  }

  private List<RestVariable> getProcessInstanceVariables(String id) {
    var response = cosmoCmmnFeignClient.getHistoricProcessInstances(id, true);
    return response.getData().isEmpty() ? null : response.getData().get(0).getVariables();
  }

  private void callHook(HookPoint hook, Runnable task) {
    final var method = "callHook";
    try {
      task.run();
    } catch (Exception e) {
      logger.error(method,
          "errore nella chiamata hook all'handler " + hook.handler.getClass().getSimpleName(), e);
      if (!hook.handler.failSafe()) {
        throw e;
      }
    }
  }

  private HookPointList retrieveHooks(CosmoTAttivita attivita) {
    HookPointList hooks = new HookPointList();
    if (attivita != null) {

      CosmoTFormLogico formLogico = attivita.getFormLogico();
      if (formLogico == null || formLogico.getDtCancellazione() != null) {
        throw new InternalServerException("Nessun form logico collegato al task");
      }
      formLogico.getCosmoRFormLogicoIstanzaFunzionalitas().stream().filter(CosmoREntity::valido)
      .map(CosmoRFormLogicoIstanzaFunzionalita::getCosmoTIstanzaFunzionalitaFormLogico)
      .filter(istanza -> istanza.getCosmoDFunzionalitaFormLogico() != null
      && !StringUtils.isBlank(istanza.getCosmoDFunzionalitaFormLogico().getHandler()))
      .map(istanza -> {
        HookPoint h = new HookPoint();
        h.formLogico = formLogico;
        h.istanza = istanza;
        h.handler = retrieveHandler(istanza.getCosmoDFunzionalitaFormLogico().getHandler());

        // lazy load
        istanza.getCosmoRIstanzaFormLogicoParametroValores().forEach(i -> {
          i.getCosmoDChiaveParametroFunzionalitaFormLogico().getCodice();
        });
        istanza.getCosmoRFormLogicoIstanzaFunzionalitas().forEach(i -> {
          i.getCosmoTIstanzaFunzionalitaFormLogico().getId();
        });
        return h;
      }).forEach(hooks::add);
    }
    return hooks;
  }

  private FunzionalitaFormLogicoHooks retrieveHandler(String name) {
    if (StringUtils.isBlank(name)) {
      throw new InvalidParameterException();
    }

    Object handler = SpringApplicationContextHelper.getBean(name.trim());

    if (handler == null) {
      /*
       * @formatter:off
       *
       * se stai debuggando qui vuol dire che il tuo handler non e' stato trovato.
       * assicurati che:
       *
       * - la classe sia annotata con @Component
       * - la classe sia correttamente referenziata per nome
       * - se hai dato un nome al bean (tipo @Component("name")) allora deve essere referenziata con lo stesso nome
       *
       * @formatter:on
       */
      throw new InternalServerException("Handler \"" + name + "\" non trovato");
    }

    if (!(handler instanceof FunzionalitaFormLogicoHooks)) {
      /*
       * @formatter:off
       *
       * se stai debuggando qui vuol dire che il tuo handler non e' configurato bene.
       * assicurati che la classe implementi FunzionalitaFormLogicoHooks o, ancora meglio, estenda AbstractFormLogicoHookHandler
       *
       * @formatter:on
       */
      throw new InternalServerException("Handler \"" + name + "\" non configurato correttamente.");
    }

    return (FunzionalitaFormLogicoHooks) handler;
  }

  private BeforeCompleteHookContext buildBeforeCompleteHookContext(CosmoTAttivita attivita,
      TaskResponse task, Task body, CosmoTFormLogico formLogico,
      CosmoTIstanzaFunzionalitaFormLogico istanza) {
    //@formatter:off
    return BeforeCompleteHookContext.builder()
        .withAttivita(attivita)
        .withPratica(attivita.getCosmoTPratica())
        .withFormLogico(formLogico)
        .withTaskId(task.getId())
        .withTask(task)
        .withRequest(body)
        .withUtente(getUtenteCorrente())
        .withIstanza(istanza)
        .withParametriIstanza(buildParametriIstanza(istanza))
        .build();
    //@formatter:on
  }

  private AfterCompleteHookContext buildAfterCompleteHookContext(CosmoTAttivita attivita,
      TaskResponse taskBefore, TaskResponse taskAfter, Task body, CosmoTFormLogico formLogico,
      CosmoTIstanzaFunzionalitaFormLogico istanza) {
    //@formatter:off
    return AfterCompleteHookContext.builder()
        .withAttivita(attivita)
        .withPratica(attivita.getCosmoTPratica())
        .withFormLogico(formLogico)
        .withTaskId(taskBefore.getId())
        .withTaskBefore(taskBefore)
        .withTask(taskAfter)
        .withRequest(body)
        .withUtente(getUtenteCorrente())
        .withIstanza(istanza)
        .withParametriIstanza(buildParametriIstanza(istanza))
        .build();
    //@formatter:on
  }

  private Map<String, Object> buildParametriIstanza(CosmoTIstanzaFunzionalitaFormLogico istanza) {
    Map<String, Object> output = new HashMap<>();
    if (istanza != null && istanza.getCosmoRIstanzaFormLogicoParametroValores() != null) {
      for (var entry : istanza.getCosmoRIstanzaFormLogicoParametroValores()) {
        output.put(entry.getId().getCodiceChiaveParametro(), entry.getValoreParametro());
      }
    }

    return output;
  }

  /**
   * aggiorna le assegnazioni di una attivita
   */
  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public AssegnaAttivitaResponse postPraticaAttivitaAssegna(Long idPratica, Long idAttivita,
      AssegnaAttivitaRequest body) {

    // valida dati di input
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(idAttivita, "idAttivita");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    requireUtenteNonOspite();

    class PreparedRequest {
      CosmoTAttivita attivita;
      String taskId;
      AssegnaTaskRequest feignRequest;
    }
    final PreparedRequest prepared = new PreparedRequest();

    // recupera l'attivita' e verifica che sia coerente con le ID
    transactionService.inTransactionOrThrow(() -> {
      prepared.attivita =
          resolveAttivita(idPratica, idAttivita).orElseThrow(NotFoundException::new);

      // verifica che sia consentita la lavorazione dell'attivita'
      autorizzaLavorazionePratica(prepared.attivita.getCosmoTPratica());
      autorizzaLavorazioneAttivita(prepared.attivita);
      autorizzaLavorazioneAssegnazione(prepared.attivita);

      // crea la request
      prepared.taskId = prepared.attivita.getTaskId();

      AssegnaTaskRequest r = buildRichiestaCmmn(body);
      prepared.feignRequest = r;
      return r;
    });

    cosmoCmmnTaskFeignClient.postTaskAssegna(prepared.taskId, prepared.feignRequest);

    if (body.isMantieniAssegnazione() != null && Boolean.TRUE.equals(body.isMantieniAssegnazione())) {
      keepAssignmentCurrentUser(prepared.taskId);
    }

    AssegnaAttivitaResponse response = new AssegnaAttivitaResponse();
    response.setMessaggio("OK");
    return response;
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public AssegnaAttivitaResponse postPraticaAttivitaAssegnaAMe(Long idPratica, Long idAttivita) {
    requireUtenteNonOspite();

    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setEsclusivo(true);
    body.setAssegnazioni(new ArrayList<>());
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazione.setIdUtente(SecurityUtils.getUtenteCorrente().getId());
    assegnazione.setTipologia("assignee");
    body.getAssegnazioni().add(assegnazione);

    return postPraticaAttivitaAssegna(idPratica, idAttivita, body);
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public AssegnaAttivitaResponse postPraticaAssegna(Long idPratica, AssegnaAttivitaRequest body) {
    final var method = "postPraticaAssegna";
    logger.debug(method, "assegno tutte le attivita' della pratica {}", idPratica);

    // valida dati di input
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    requireUtenteNonOspite();

    class PreparedRequest {
      CosmoTPratica pratica;
      List<CosmoTAttivita> attivita;
      List<String> taskIds;
      Map<String, AssegnaTaskRequest> feignRequests;
    }
    final PreparedRequest prepared = new PreparedRequest();

    // recupera l'attivita' e verifica che sia coerente con le ID
    transactionService.inTransactionOrThrow(() -> {
      prepared.pratica = cosmoTPraticaRepository.findOne(idPratica);
      if (prepared.pratica == null) {
        throw new NotFoundException("Pratica non trovata");
      }

      // verifica che sia consentita la lavorazione della pratica
      autorizzaLavorazionePratica(prepared.pratica);

      prepared.attivita =
          prepared.pratica.getAttivita().stream().filter(CosmoTEntity::nonCancellato).map(a -> {
            // verifica che sia consentita la lavorazione dell'attivita'
            autorizzaLavorazioneAttivita(a);
            return a;
          }).collect(Collectors.toList());

      // crea la request
      prepared.taskIds = prepared.attivita.stream().map(CosmoTAttivita::getTaskId)
          .filter(Objects::nonNull).collect(Collectors.toList());

      // preparo le feign request per cmmn
      prepared.feignRequests = new HashMap<>();

      prepared.taskIds.stream().forEach(taskId -> {

        AssegnaTaskRequest r = buildRichiestaCmmn(body);
        prepared.feignRequests.put(taskId, r);
      });

      return null;
    });

    // invio tutte le feign request
    logger.debug(method, "invio {} richieste per assegnare le attivita' della pratica {}",
        prepared.feignRequests.size(), idPratica);

    boolean inconsistente = false;
    boolean canRollback = true;
    for (Entry<String, AssegnaTaskRequest> request : prepared.feignRequests.entrySet()) {
      try {
        cosmoCmmnTaskFeignClient.postTaskAssegna(request.getKey(), request.getValue());
        canRollback = false;
        if (body.isMantieniAssegnazione() != null && Boolean.TRUE.equals(body.isMantieniAssegnazione())) {
          keepAssignmentCurrentUser(request.getKey());
        }
      } catch (Exception e) {
        logger.error(method, "errore nell'invio di una singola assegnazione", e);
        if (canRollback) {
          throw e;
        } else {
          logger.warn(method,
              "non posso fare rollback perche' ho gia' assegnato altre attivita' per la stessa pratica");
          inconsistente = true;
        }
      }
    }

    if (inconsistente) {
      // situazione inconsistente
      segnalaInconsistenzaAssegnazione(prepared.pratica, body);
    }

    AssegnaAttivitaResponse response = new AssegnaAttivitaResponse();
    response.setMessaggio("OK");


    return response;
  }

  protected AssegnaTaskRequest buildRichiestaCmmn(AssegnaAttivitaRequest body) {
    AssegnaTaskRequest r = new AssegnaTaskRequest();
    r.setEsclusivo(body.isEsclusivo());
    if (body.getAssegnazioni() != null) {
      r.setAssegnazioni(body.getAssegnazioni().stream().map(a -> {
        AssegnaTaskRequestAssegnazione mapped = new AssegnaTaskRequestAssegnazione();
        if (a.getIdUtente() != null) {
          mapped.setCodiceFiscaleUtente(requireUtenteFromId(a.getIdUtente()).getCodiceFiscale());
        } else if (a.getIdGruppo() != null) {
          mapped
          .setCodiceGruppo(Optional.ofNullable(cosmoTGruppoRepository.findOne(a.getIdGruppo()))
              .map(CosmoTGruppo::getCodice)
              .orElseThrow(() -> new BadRequestException("Gruppo non trovato")));
        } else {
          throw new BadRequestException(
              "Nessun idGruppo o idUtente specificato per l'assegnazione.");
        }
        mapped.setTipologia(a.getTipologia());
        return mapped;
      }).collect(Collectors.toList()));
    }

    return r;
  }

  protected void segnalaInconsistenzaAssegnazione(CosmoTPratica pratica,
      AssegnaAttivitaRequest request) {
    String subject = "Errore di inconsistenza in assegnazione pratica";
    String text = new StringBuilder()
        .append("L'assegnazione delle attivita' relative alla pratica \"")
        .append(pratica.getOggetto()).append("\" con id ").append(pratica.getId())
        .append(
            " e' riuscita solo per alcune attivita' ma e' fallita per altre.<br/>La situazione dei dati potrebbe essere inconsistente.")
        .append("<br/><br/>").append("Richiesta originale: ").append(ObjectUtils.represent(request))
        .toString();

    mailService.inviaMailAssistenza(subject, text);
  }

  protected CosmoTUtente requireUtenteFromId(Long id) {
    return Optional
        .ofNullable(cosmoTUtenteRepository
            .findOne(id.equals(0L) ? SecurityUtils.getUtenteCorrente().getId() : id))
        .orElseThrow(() -> new BadRequestException("Utente non trovato"));
  }

  protected Optional<CosmoTUtente> identify(RiferimentoUtente riferimento) {
    if (riferimento == null) {
      return Optional.empty();
    }
    if (riferimento.getId() != null) {
      return Optional.ofNullable(cosmoTUtenteRepository.findOne(riferimento.getId()));
    }
    if (!StringUtils.isBlank(riferimento.getCodiceFiscale())) {
      return cosmoTUtenteRepository.findOneByField(CosmoTUtente_.codiceFiscale,
          riferimento.getCodiceFiscale());
    }
    return Optional.empty();
  }

  protected RiferimentoOperazioneAsincrona riferimentoOperazioneAsincrona(
      LongTaskFuture<?> asyncTask) {
    RiferimentoOperazioneAsincrona output = new RiferimentoOperazioneAsincrona();
    output.setNome(asyncTask.getTask().getName());
    output.setStato(asyncTask.getTask().getStatus().name());
    output.setUuid(asyncTask.getTaskId());
    return output;
  }

  protected RestVariable variable(String name, String value) {
    RestVariable output = new RestVariable();
    output.setName(name);
    output.setValue(value);
    return output;
  }

  void touchAttivita(CosmoTAttivita attivita) {
    cosmoTAttivitaRepository.touch(attivita, AuditServiceImpl.getPrincipalCode());
    if (attivita.getParent() != null) {
      cosmoTAttivitaRepository.touch(attivita.getParent(), AuditServiceImpl.getPrincipalCode());
    }
  }

  private Optional<CosmoTAttivita> resolveAttivita(Long idPratica, Long idAttivita) {
    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(idAttivita, "idAttivita");

    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(idAttivita);
    if (attivita == null) {
      return Optional.empty();
    }

    if (attivita.getCosmoTPratica().getId().equals(idPratica)) {
      return Optional.of(attivita);
    }

    if (attivita.cancellato()) {
      throw new NotFoundException("Attivita' non trovata");
    }
    if (attivita.getCosmoTPratica().cancellato()) {
      throw new ConflictException("Pratica non trovata");
    }

    throw new BadRequestException("Pratica non coerente con l'attivita' fornita");
  }

  private void autorizzaLavorazioneAttivita(CosmoTAttivita attivita) {
    ValidationUtils.require(attivita, "attivita");

    if (attivita.cancellato()) {
      throw new NotFoundException("Lavorazione non consentita per attivita' terminata");
    }
    if (attivita.getCosmoTPratica().cancellato()
        || attivita.getCosmoTPratica().getDataFinePratica() != null) {
      throw new ConflictException("Lavorazione non consentita per pratica terminata");
    }

    if (StringUtils.isEmpty(attivita.getLinkAttivita())) {
      throw new InternalServerException("Nessun link attivita");
    }
  }

  private void autorizzaLavorazionePratica(CosmoTPratica pratica) {
    ValidationUtils.require(pratica, "pratica");

    if (pratica.cancellato() || pratica.getDataFinePratica() != null) {
      throw new ConflictException("Lavorazione non consentita per pratica terminata");
    }

    if (StringUtils.isEmpty(pratica.getLinkPratica())) {
      throw new InternalServerException("Nessun link pratica");
    }
  }

  private void requireUtenteNonOspite() {

    UserInfoDTO user = SecurityUtils.getUtenteCorrente();
    if (!Boolean.FALSE.equals(user.getAnonimo())) {
      throw new UnauthorizedException("Lavorazione non consentita per utenti ospiti");
    }
  }

  private CosmoTUtente getUtenteCorrente() {
    UserInfoDTO user = SecurityUtils.getUtenteCorrente();
    if (Boolean.FALSE.equals(user.getAnonimo())) {
      return cosmoTUtenteRepository.findByCodiceFiscale(user.getCodiceFiscale());
    }
    return null;
  }

  private CosmoTFruitore getFruitoreCorrente() {

    ClientInfoDTO client = SecurityUtils.getClientCorrente();
    if (Boolean.FALSE.equals(client.getAnonimo())) {
      return cosmoTFruitoreRepository
          .findOneByField(CosmoTFruitore_.apiManagerId, client.getCodice()).orElse(null);
    }
    return null;
  }

  private class HookPointList extends ArrayList<HookPoint> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
  }

  private class HookPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    FunzionalitaFormLogicoHooks handler;
    CosmoTFormLogico formLogico;
    CosmoTIstanzaFunzionalitaFormLogico istanza;
  }

  private void autorizzaLavorazioneAssegnazione(CosmoTAttivita attivita) {
    ValidationUtils.require(attivita, "attivita");
    var utente = getUtenteCorrente();

    if (utente == null) {
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    CosmoRAttivitaAssegnazione assegnazione = null;

    if (utente.getCosmoTGruppos() == null || utente.getCosmoTGruppos().isEmpty()) {
      assegnazione = cosmoRAttivitaAssegnazioneRepository
          .findOneByCosmoTAttivitaIdAndDtFineValIsNullAndIdUtente(attivita.getId(),
              utente.getId().intValue());
    } else {
      assegnazione =
          cosmoRAttivitaAssegnazioneRepository.findOne(CosmoRAttivitaAssociazioneSpecifications
              .findByAssociazioneMeOMioGruppo(attivita.getId(), utente.getId().intValue(),
                  utente.getCosmoTGruppos().stream().filter(CampiTecniciEntity::nonCancellato)
                  .map(g -> g.getId().intValue()).collect(Collectors.toList())));
    }

    if (assegnazione == null) {
      throw new NotFoundException("Lavorazione non consentita: task in carico ad un altro utente");
    }
  }


  private RecoveryAttemptResult attemptRecovery(Long idPratica, UserInfoDTO actor) {
    final var method = "attemptRecovery";
    RecoveryAttemptResult output = new RecoveryAttemptResult();
    logger.info(method, "avvio sequenza di recovery per la pratica {}", idPratica);
    try {
      output.changed = processRecoveryService.recover(idPratica, actor);
      logger.info(method, "sequenza di recovery per la pratica {} completata con successo",
          idPratica);
      output.success = true;
    } catch (Exception e) {
      logger.info(method,
          "errore nel tentativo di esecuzione della sequenza di recovery per la pratica "
              + idPratica,
              e);
      output.changed = false;
      output.success = false;
      output.error = e;
    }
    return output;
  }

  private class RecoveryAttemptResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    boolean success = false;
    boolean changed = false;
    Exception error;
  }

  private void keepAssignmentCurrentUser(String taskId) {
    var list = cosmoRAttivitaAssegnazioneRepository
        .findAllByCosmoTAttivitaLinkAttivitaAndDtFineValIsNotNullAndIdUtenteOrderByDtFineValDesc(
            "tasks/" + taskId, SecurityUtils.getUtenteCorrente().getId().intValue());
    if (!list.isEmpty()) {
      var updateRow = list.get(0);
      updateRow.setDtFineVal(null);
      cosmoRAttivitaAssegnazioneRepository.save(updateRow);
    }
  }
}
