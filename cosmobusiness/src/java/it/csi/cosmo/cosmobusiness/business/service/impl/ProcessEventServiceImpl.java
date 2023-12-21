/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ProcessEventService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

@Service
public class ProcessEventServiceImpl implements ProcessEventService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private ProcessService processService;

  private static final String ACTIVITY_CANCELLED = "activityCancelled";
  private static final String TASK_CREATED = "taskCreated";
  private static final String TASK_COMPLETED = "taskCompleted";
  private static final String TASK_ASSIGNED = "taskAssigned";
  private static final String PROCESS_COMPLETED = "processCompleted";
  private static final String PROCESS_CREATED = "processCreated";
  private static final String PROCESS_COMPLETED_WITH_TERMINATE_END =
      "processCompletedWithTerminateEnd";
  private static final String PROCESS_COMPLETED_WITH_ERROR_END = "processCompletedWithErrorEnd";
  private static final String STATE_CHANGE = "stateChange";

  private Map<String, Consumer<ProcessEngineEventDTO>> handlers;

  public ProcessEventServiceImpl() {
    handlers = new HashMap<>();
    handlers.put(ACTIVITY_CANCELLED, this::activityCancelled);
    handlers.put(TASK_CREATED, this::taskCreated);
    handlers.put(TASK_COMPLETED, this::taskCompleted);
    handlers.put(TASK_ASSIGNED, this::taskAssigned);
    handlers.put(PROCESS_CREATED, this::processCreated);
    handlers.put(PROCESS_COMPLETED, this::processCompleted);
    handlers.put(STATE_CHANGE, this::stateChange);
    handlers.put(PROCESS_COMPLETED_WITH_TERMINATE_END, this::processCompleted);
    handlers.put(PROCESS_COMPLETED_WITH_ERROR_END, this::processCompleted);

  }

  @Transactional(readOnly = false, rollbackFor = Exception.class)
  @Override
  public void process(ProcessEngineEventDTO body) {
    final var method = "process";

    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.getMessageType(), "body.messageType");
    ValidationUtils.require(body.getTimestamp(), "timestamp");

    var client = SecurityUtils.getClientCorrente();

    if (logger.isDebugEnabled()) {
      logger.debug(method, "received process event: {}", ObjectUtils.represent(body));
      logger.debug(method, "received process event from client: {}", ObjectUtils.represent(client));
    } else {
      logger.info(method, "received process event: {}", body.getMessageType());
    }

    if (StringUtils.isBlank(body.getMessageType())) {
      throw new BadRequestException("MessageType non specificato");
    }

    if (!handlers.containsKey(body.getMessageType())) {
      throw new BadRequestException("Tipologia di messaggio non gestita: " + body.getMessageType());
    }

    try {
      handlers.get(body.getMessageType()).accept(body);
      logger.debug(method, "Evento del processo gestito");

    } catch (Throwable t) { // NOSONAR perche' la rilancio
      logger.error(method, "errore nella ricezione di un evento di processo: " + t.getMessage());
      throw t;
    }
  }

  /*
   * un processo e' stato creato. scatta all'avvio di un processo
   *
   * a questo punto la pratica deve gia' esistere nel database e il suo ID viene passato come
   * businessKey del processo
   */
  private void processCreated(ProcessEngineEventDTO body) {
    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");

    // recupero pratica
    CosmoTPratica pratica = getPraticaWithLock(body);

    // aggiorno dati di esecuzione della pratica
    pratica.setLinkPratica(linkPratica(body.getProcess().getId()));

    logger.debug(body.getMessageType(), "Link pratica da salvare {}", pratica.getLinkPratica());

    pratica.setDataCambioStato(Timestamp.from(body.getTimestamp().toInstant()));

    registraCambioStato(pratica, body, false);

    cosmoTPraticaRepository.save(pratica);

    logger.info(body.getMessageType(), "aggiornata pratica con id {} e link pratica {}",
        pratica.getId(), pratica.getLinkPratica());
  }

  private void taskCreated(ProcessEngineEventDTO body) {
    if (body.getProcess() == null) {
      return;
    }

    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getTask(), "task");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");
    var task = body.getTask();

    // recupero pratica
    CosmoTPratica pratica = getPratica(body);

    registraCambioStato(pratica, body, false);

    var attivita = processService.importaNuovoTask(pratica, task, body.getProcess().getVariables());

    logger.info(body.getMessageType(), "inserita attivita {} ({}) per pratica {} ({})",
        attivita.getId(), task.getId(), pratica.getId(), body.getProcess().getId());
  }

  private void taskAssigned(ProcessEngineEventDTO body) {
    if (body.getProcess() == null) {
      return;
    }

    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getTask(), "task");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");
    var task = body.getTask();

    // recupero pratica
    CosmoTPratica pratica = getPratica(body);

    registraCambioStato(pratica, body, false);

    // recupero attivita
    Optional<CosmoTAttivita> attivita = fetchAttivitaByPraticaAndIdTask(pratica, task.getId());
    if (attivita.isEmpty()) {
      return;
    }
    // gestisco assegnazioni
    processService.aggiornaAssegnazioni(attivita.get(), task, pratica.getEnte(),
        body.getProcess().getVariables());
  }

  private void taskCompleted(ProcessEngineEventDTO body) {
    if (body.getProcess() == null) {
      return;
    }

    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getTask(), "task");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");
    var task = body.getTask();

    // recupero pratica
    CosmoTPratica pratica = getPratica(body);

    registraCambioStato(pratica, body, false);

    // recupero attivita
    Optional<CosmoTAttivita> attivita = fetchAttivitaByPraticaAndIdTask(pratica, task.getId());
    if (attivita.isEmpty()) {
      return;
    }

    processService.aggiornaAttivitaTerminata(attivita.get());
  }

  private void taskCancelled(ProcessEngineEventDTO body) {
    if (body.getProcess() == null) {
      return;
    }

    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getTask(), "task");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");
    var task = body.getTask();

    // recupero pratica
    CosmoTPratica pratica = getPratica(body);

    registraCambioStato(pratica, body, false);

    // recupero attivita
    Optional<CosmoTAttivita> attivita = fetchAttivitaByPraticaAndIdTask(pratica, task.getId());
    if (attivita.isEmpty()) {
      return;
    }

    processService.aggiornaAttivitaAnnullata(attivita.get());
  }

  private void activityCancelled(ProcessEngineEventDTO body) {
    this.taskCancelled(body);
  }

  private void processCompleted(ProcessEngineEventDTO body) {
    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");

    // recupero pratica
    CosmoTPratica pratica = getPratica(body);

    pratica.setDataCambioStato(Timestamp.from(body.getTimestamp().toInstant()));
    registraCambioStato(pratica, body, false);

    processService.terminaPratica(pratica, body.getTimestamp());
  }

  private void stateChange(ProcessEngineEventDTO body) {
    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");

    // recupero pratica
    CosmoTPratica pratica = getPraticaWithLock(body);

    pratica.setDataCambioStato(Timestamp.from(body.getTimestamp().toInstant()));

    registraCambioStato(pratica, body, true);

    logger.info(body.getMessageType(), "aggiornato stato della pratica con id {}", pratica.getId());
  }

  // HELPERS

  private void registraCambioStato(CosmoTPratica pratica, ProcessEngineEventDTO body,
      boolean explicit) {
    processService.registraCambioStato(pratica,
        (String) body.getProcess().getVariables().getOrDefault("stato", null),
        body.getTimestamp().toInstant(), explicit);
  }

  private CosmoTPratica getPratica(ProcessEngineEventDTO body) {
    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");

    // recupero id pratica
    Long idPratica = Long.valueOf(body.getProcess().getBusinessKey());

    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      throw new InternalServerException("Pratica con id " + idPratica + " non trovata");
    }

    return pratica;
  }

  private CosmoTPratica getPraticaWithLock(ProcessEngineEventDTO body) {
    ValidationUtils.require(body.getProcess(), "process");
    ValidationUtils.require(body.getProcess().getBusinessKey(), "process.businessKey");

    // recupero id pratica
    Long idPratica = Long.valueOf(body.getProcess().getBusinessKey());

    CosmoTPratica pratica =
        cosmoTPraticaRepository.findWithLockingByIdAndDtCancellazioneIsNull(idPratica);
    if (pratica == null) {
      throw new InternalServerException("Pratica con id " + idPratica + " non trovata");
    }

    return pratica;
  }

  private Optional<CosmoTAttivita> fetchAttivitaByPraticaAndIdTask(CosmoTPratica pratica,
      String taskId) {
    return Optional.ofNullable(cosmoTAttivitaRepository
        .findByCosmoTPraticaIdAndLinkAttivita(pratica.getId(), linkAttivita(taskId)));
  }

  private String linkAttivita(String id) {
    return "tasks/" + id;
  }

  private String linkPratica(String id) {
    return "/pratiche/" + id;
  }

}
