/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ws.rs.BadRequestException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.csi.cosmo.common.entities.CosmoDTipoCommento;
import it.csi.cosmo.common.entities.CosmoDTipoCommento_;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTCommento;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.TipoCommento;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.integration.mapper.CommentoMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.TaskMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDTipoCommentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTCommentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;

@Service
public class TaskServiceImpl implements TaskService {
  
  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoCmmnFeignClient cmmnClient;

  @Autowired
  private CosmoNotificationsNotificheFeignClient notificheFeignClient;

  @Autowired
  private CommentoMapper commentoMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTCommentoRepository cosmoTCommentoRepository;

  @Autowired
  private CosmoDTipoCommentoRepository cosmoDTipoCommentoRepository;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;

  @Autowired
  private LockService lockService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  private ObjectMapper getMapper() {
    ObjectMapper mapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  /*
   * chiamato da frontend quando 'entro' su un task
   */
  @Override
  public Task getTaskId(String id) {

    TaskResponse response = cmmnClient.getTaskId(id);
    return taskMapper.toTask(response);
  }

  @Override
  public PaginaTask getTask(String dueAfter, String sort, String size, String start,
      String dueBefore) {
    String tenantId = null != SecurityUtils.getUtenteCorrente().getEnte()
        ? SecurityUtils.getUtenteCorrente().getEnte().getTenantId()
            : null;

    TaskResponseWrapper tasks =
        this.cmmnClient.getTasks(SecurityUtils.getUtenteCorrente().getCodiceFiscale(), dueBefore,
            dueAfter, sort, size, start, tenantId);

    PaginaTask p = new PaginaTask();

    p.setTotale(BigDecimal.valueOf(tasks.getTotal()));
    p.setDimensionePagina(BigDecimal.valueOf(tasks.getSize()));

    final ObjectMapper mapper = getMapper();


    p.setElementi(tasks.getData().stream().filter(d -> tenantId.equals(d.getTenantId()))
        .map(d -> mapper.convertValue(d, Task.class)).collect(Collectors.toList()));

    return p;
  }

  @Override
  @Transactional(readOnly = true)
  public PaginaCommenti getTaskIdtaskComments(String idtask) {


    CosmoTAttivita att = this.getCosmoTAttivitaFromIdTask(idtask);

    List<CosmoTAttivita> subtasks =
        cosmoTAttivitaRepository.findByField(CosmoTAttivita_.parent, att);

    List<CosmoRAttivitaAssegnazione> assegnazioni =
        cosmoRAttivitaAssegnazioneRepository.findByCosmoTAttivitaIn(subtasks);

    List<CosmoTCommento> commenti = att.getCommenti().stream()
        .filter(temp -> temp.getDtCancellazione() == null && temp.getUtenteCancellazione() == null)
        .sorted(Comparator.comparing(CosmoTCommento::getDataCreazione).reversed())
        .collect(Collectors.toList());
    PaginaCommenti p = new PaginaCommenti();
    p.setElementi(
        commenti.stream().map(c -> commentoMapper.toCommento(c)).collect(Collectors.toList()));

    p.getElementi().forEach(commento -> {
      Optional<CosmoRAttivitaAssegnazione> find =
          assegnazioni.stream()
          .filter(assegnazione -> (assegnazione.getDtFineVal() == null
          && OffsetDateTime
          .ofInstant(Instant.ofEpochMilli(assegnazione.getDtInizioVal().getTime()),
              ZoneId.systemDefault())
          .minusSeconds(10).isBefore(commento.getTimestamp()))
              || (assegnazione.getDtFineVal() != null
              && OffsetDateTime
              .ofInstant(Instant.ofEpochMilli(assegnazione.getDtInizioVal().getTime()),
                  ZoneId.systemDefault())
              .minusSeconds(10).isBefore(commento.getTimestamp())
              && OffsetDateTime
              .ofInstant(Instant.ofEpochMilli(assegnazione.getDtFineVal().getTime()),
                  ZoneId.systemDefault())
              .minusSeconds(10).isAfter(commento.getTimestamp())))
          .findFirst();
      if (find.isPresent()) {
        var utente = cosmoTUtenteRepository.findOneByField(CosmoTUtente_.id,
            Long.valueOf(find.get().getIdUtente()));
        if (!utente.get().getCodiceFiscale().equals(commento.getCfAutore())) {
          commento.setCfDestinatario(utente.get().getCodiceFiscale());
          commento.setCognomeDestinatario(utente.get().getCognome());
          commento.setNomeDestinatario(utente.get().getNome());
        }
      }
    });
    return p;
  }

  @Override
  @Transactional
  public TaskResponse postTask(Task body) {

    if (StringUtils.isEmpty(body.getTenantId())) {
      UserInfoDTO utente = SecurityUtils.getUtenteCorrente();
      if (utente != null && utente.getEnte() != null && utente.getEnte().getTenantId() != null) {
        body.setTenantId(utente.getEnte().getTenantId());
      }
    }

    if (!StringUtils.isBlank(body.getParentTaskId())) {
      /*
       * marco come 'touched' l'attivita'
       */
      findAttivitaByTaskId(body.getParentTaskId()).ifPresent(attivita -> cosmoTAttivitaRepository
          .touch(attivita, AuditServiceImpl.getPrincipalCode()));
    }

    return cmmnClient.postTask(body);
  }

  /*
   * chiamato quando clicco 'SALVA' sulla lavorazione task
   */
  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public RestVariable[] putPraticheVariabiliProcessInstanceId(String processInstanceId,
      List<VariabileProcesso> body) {

    /*
     * aggiorno le variabili su flowable
     */
    RestVariable[] response = null;
    if (body != null && !body.isEmpty()) {
      var putVariables = new ArrayList<RestVariable>();
      body.forEach(variabile -> {
        var restVariable = new RestVariable();
        restVariable.setName(variabile.getName());
        restVariable.setScope(variabile.getScope());
        restVariable.setType(variabile.getType());
        Object value = null;
        value = variabile.getValue();
        restVariable.setValue(value);
        putVariables.add(restVariable);
      });
      response = this.cmmnClient.putProcessInstanceVariables(processInstanceId, putVariables);
    }

    /*
     * marco come 'touched' sia la pratica che le attivita' e loggo la modifica
     */
    transactionService.inTransaction(() -> {
      Optional<CosmoTPratica> praticaOpt = findPraticaByProcessInstanceId(processInstanceId);
      String principalCode = AuditServiceImpl.getPrincipalCode();
      praticaOpt.ifPresent(entity -> {
        cosmoTPraticaRepository.touch(entity, principalCode);
        entity.getAttivita().forEach(this::touchAttivita);
      });

    });

    return response;
  }

  /*
   * chiamato quando clicco 'CONFERMA' sulla lavorazione task
   *
   * o quando chiudo un subtask di collaborazione
   */
  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Task postTaskId(String id, Task body) {
    Timestamp now = Timestamp.from(Instant.now());

    /*
     * verifico il lock se richiesto/presente
     */
    var lockCheckResult =
        transactionService.inTransaction(() -> lockService.checkLock("@task(" + id + ")", false));
    if (lockCheckResult.failed()) {
      if (lockCheckResult.getError() instanceof RuntimeException) {
        throw (RuntimeException) lockCheckResult.getError();
      } else {
        throw new InternalServerException("Errore nella verifica del lock",
            lockCheckResult.getError());
      }
    }

    /*
     * eseguo la post per chiudere il task su cosmocmmn
     */
    UserInfoDTO utente = SecurityUtils.getUtenteCorrente();

    if (StringUtils.isEmpty(body.getTenantId())) {
      if (utente != null && utente.getEnte() != null && utente.getEnte().getTenantId() != null) {
        body.setTenantId(utente.getEnte().getTenantId());
      }
    }

    Map<String, Object> response = cmmnClient.postTaskId(id, body);

    /*
     * marco come 'touched' l'attivita' e inserisco il log dell'evento su DB
     */
    transactionService.inTransaction(() -> {
      findAttivitaByTaskId(id).ifPresent(attivita -> {
        this.touchAttivita(attivita);
        // inserisco il log dell'operazione su db
        //@formatter:off
        storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
            .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_COMPLETATA)
            .withDescrizioneEvento(String.format("L'attivita' '%s' e' stata completata.", attivita.getNome()))
            .withPratica(attivita.getCosmoTPratica())
            .withAttivita(attivita)
            .withDtEvento(now)
            .build());
        //@formatter:on
      });
    });

    final ObjectMapper mapper = getMapper();
    return mapper.convertValue(response, Task.class);
  }

  private void touchAttivita(CosmoTAttivita attivita) {
    cosmoTAttivitaRepository.touch(attivita, AuditServiceImpl.getPrincipalCode());
    if (attivita.getParent() != null) {
      cosmoTAttivitaRepository.touch(attivita.getParent(), AuditServiceImpl.getPrincipalCode());
    }
  }

  @Override
  public Commento postTaskIdtaskComments(String idtask, Commento body, Boolean reply) {

    CosmoTAttivita attivita = getCosmoTAttivitaFromIdTask(idtask);


    CosmoTCommento commento = new CosmoTCommento();
    commento.setAttivita(attivita);
    commento.setDataCreazione(body.getTimestamp() != null
        ? Timestamp.valueOf(
            body.getTimestamp().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime())
            : new Timestamp(System.currentTimeMillis()));

    commento.setUtenteCreatore(StringUtils.isNotEmpty(body.getCfAutore()) ? body.getCfAutore()
        : SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    commento.setMessaggio(body.getMessaggio());

    CosmoDTipoCommento tipoCommento = cosmoDTipoCommentoRepository
        .findOneByField(CosmoDTipoCommento_.codice, TipoCommento.COMMENTO.getCodice())
        .orElseThrow(() -> new NotFoundException(
            "Tipo Commento " + TipoCommento.COMMENTO.getCodice() + " non trovato"));
    commento.setTipo(tipoCommento);

    this.cosmoTCommentoRepository.save(commento);

    if (reply != null && Boolean.TRUE.equals(reply)) {
      inviaNotificaDelCommento(idtask, body.getMessaggio());
    }

    return commentoMapper.toCommento(commento);
  }

  private CosmoTAttivita getCosmoTAttivitaFromIdTask(String idtask) {
    // trovare l'attivita tramite task id
    Optional<CosmoTAttivita> attivita =
        cosmoTAttivitaRepository.findOneByField(CosmoTAttivita_.linkAttivita, "tasks/" + idtask);


    return attivita
        .orElseThrow(() -> new NotFoundException("Attivita con idTask " + idtask + " non trovata"));

  }

  @Override
  public Task putTask(String id, Task body) {
    if (body.getAssignee() == null) {
      throw new BadRequestException("Il campo 'assignee' e' obbligatorio");
    }

    TaskResponse response = this.cmmnClient.putTask(id, body);

    final ObjectMapper mapper = getMapper();
    return mapper.convertValue(response, Task.class);
  }

  private Optional<CosmoTAttivita> findAttivitaByTaskId(String taskId) {
    if (StringUtils.isBlank(taskId)) {
      throw new InvalidParameterException();
    }

    return this.cosmoTAttivitaRepository.findOneByField(CosmoTAttivita_.linkAttivita,
        "tasks/" + taskId.strip());
  }

  private Optional<CosmoTPratica> findPraticaByProcessInstanceId(String processInstanceId) {
    if (StringUtils.isBlank(processInstanceId)) {
      throw new InvalidParameterException();
    }

    return this.cosmoTPraticaRepository.findOneByField(CosmoTPratica_.linkPratica,
        "/pratiche/" + processInstanceId.strip());
  }

  private void inviaNotificaDelCommento(String idtask, String commento) {
    
    final var method = "inviaNotificaDelCommento";
    
    Optional<CosmoTAttivita> attivita = cosmoTAttivitaRepository
        .findOneNotDeletedByField(CosmoTAttivita_.linkAttivita, "tasks/" + idtask);

    attivita.ifPresent(att -> {

      List<Long> idUtenti = new LinkedList<>();

      List<CosmoRAttivitaAssegnazione> assegnazioni = cosmoRAttivitaAssegnazioneRepository
          .findByCosmoTAttivitaIdAndDtFineValIsNull(att.getId());

      assegnazioni.stream()
      .filter(
          ass -> ass.getIdUtente() != null && !idUtenti.contains(ass.getIdUtente().longValue()))
      .forEach(ass -> idUtenti.add(ass.getIdUtente().longValue()));

      List<CosmoTUtente> utenti = cosmoTUtenteRepository.findByIdIn(idUtenti);

      List<String> codiciFiscali =
          utenti.stream()
          .map(CosmoTUtente::getCodiceFiscale).collect(Collectors.toList());

      CosmoTPratica pratica =
          cosmoTPraticaRepository.findOneByAttivitaIdAndDtCancellazioneIsNull(att.getId());

      if (codiciFiscali != null && !codiciFiscali.isEmpty() && pratica != null) {

        try {
        CreaNotificaRequest notifica = new CreaNotificaRequest();
        notifica.setIdPratica(pratica.getId());
        notifica.setUtentiDestinatari(codiciFiscali);
        notifica.setTipoNotifica(TipoNotifica.COMMENTO.getAzione());

        if (SecurityUtils.getUtenteCorrente().getEnte() != null) {
          notifica.setCodiceIpaEnte(SecurityUtils.getUtenteCorrente().getEnte().getTenantId());
        }
        notifica.setPush(Boolean.TRUE);

        notifica.setMessaggio(String.format(Constants.INSERIMENTO_COMMENTO_AL_TASK, commento,
            att.getNome(), pratica.getOggetto()));

        CreaNotificheRequest notifiche = new CreaNotificheRequest();
        notifiche.setNotifiche(Arrays.asList(notifica));

        notificheFeignClient.postNotifications(notifiche);
        } catch (Exception e) {
          logger.error(method, "error sending notifications batch", e);
        }
      }

    });
  }

}
