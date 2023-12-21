/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceCreateRequest;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.entities.CosmoDTipoCommento;
import it.csi.cosmo.common.entities.CosmoDTipoCommento_;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoTApprovazione;
import it.csi.cosmo.common.entities.CosmoTApprovazione_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTCommento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.FaseTrasformazioneDatiPratica;
import it.csi.cosmo.common.entities.enums.TipoCommento;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.entities.enums.TipoRelazionePraticaPratica;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.exception.FlowableVariableHandlingException;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.FormTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Funzionalita;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoUtente;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.cosmo.cosmobusiness.integration.mapper.CommentoMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.CosmoPraticheMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.EventoMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.FormLogicoMapper;
import it.csi.cosmo.cosmobusiness.integration.mapper.TaskMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDTipoCommentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTApprovazioneRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTCommentoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnSyncFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFormsApiFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.CommonUtils;
import it.csi.cosmo.cosmobusiness.util.Util;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;

@Service
@Transactional
public class PracticeServiceImpl implements PracticeService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "PracticeServiceImpl");

  @Autowired
  private TaskService ts;

  @Autowired
  private Util util;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheClient;

  @Autowired
  private CosmoPraticheFormsApiFeignClient formsClient;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnClient;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient notificheGlobaliFeignClient;

  @Autowired
  private CosmoEcmDocumentiFeignClient ecmDocumentiFeignClient;

  @Autowired
  private CosmoPraticheMapper praticheMapper;

  @Autowired
  private TaskMapper taskMapper;

  @Autowired
  private FormLogicoMapper funzionalitaMapper;

  @Autowired
  private EventoMapper eventoMapper;

  @Autowired
  private CommentoMapper commentoMapper;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTApprovazioneRepository cosmoTApprovazioneRepository;

  @Autowired
  private CosmoTCommentoRepository cosmoTCommentoRepository;

  @Autowired
  private CosmoDTipoCommentoRepository cosmoDTipoCommentoRepository;


  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Autowired
  private MotoreJsonDinamicoService motoreJsonDinamicoService;

  @Autowired
  private CosmoCmmnSyncFeignClient cosmoCmmnSyncFeignClient;

  @Autowired
  private TransactionService transactionService;

  public static final String SLASH = "/";

  private static final String VARIABLE_INITIATOR = "initiator";

  private static final String VARIABLE_ID_PRATICA = "idPratica";

  private static final String VARIABLE_OGGETTO = "oggetto";

  // ------------------------- AUX ------------------------------------

  private List<Funzionalita> getListaFunzionalita(String nome) {
    it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico m;

    m = formsClient.getFormsNome(nome);
    if (m == null || m.getFunzionalita() == null) {
      return Collections.emptyList();
    }
    return m.getFunzionalita().stream().map(f -> funzionalitaMapper.toFunzionalita(f))
        .collect(Collectors.toList());
  }

  // ------------------------- GET ------------------------------------

  @Override
  public Pratica getPraticheId(String id) {
    ProcessInstanceResponse response = cosmoCmmnClient.getProcessInstance(id);
    Pratica p = new Pratica();

    p.setProcesso(taskMapper.toProcesso(response));
    return p;
  }

  @Override
  @Transactional(readOnly = true)
  public PaginaCommenti getCommenti(String processInstanceId) {

    CosmoTPratica pratica = getCosmoTPraticaFromProcessInstanceId(processInstanceId);

    List<CosmoTCommento> commenti = pratica.getCommenti().stream()
        .filter(temp -> temp.getDtCancellazione() == null && temp.getUtenteCancellazione() == null)
        .sorted(Comparator.comparing(CosmoTCommento::getDataCreazione).reversed())
        .collect(Collectors.toList());
    PaginaCommenti p = new PaginaCommenti();
    p.setElementi(
        commenti.stream().map(c -> commentoMapper.toCommento(c)).collect(Collectors.toList()));
    return p;
  }


  private CosmoTPratica getCosmoTPraticaFromProcessInstanceId(String processInstanceId) {

    // trovare la pratica tramite processInstanceId
    Optional<CosmoTPratica> praticaOptional = cosmoTPraticaRepository
        .findOneByField(CosmoTPratica_.linkPratica, "/pratiche/" + processInstanceId);

    return praticaOptional.orElseThrow(() -> new NotFoundException(
        "Pratica con processInstanceId " + processInstanceId + " non trovata"));

  }

  @Override
  @Transactional(readOnly = true)
  public Commento getPraticheIdCommentiIdCommento(String idCommento, String processInstanceId) {


    CosmoTCommento cosmoTCommento = getCosmoTCommento(idCommento, processInstanceId);

    return commentoMapper.toCommento(cosmoTCommento);
  }

  private CosmoTCommento getCosmoTCommento(String idCommento, String processInstanceId) {

    CosmoTPratica pratica = this.getCosmoTPraticaFromProcessInstanceId(processInstanceId);

    Optional<CosmoTCommento> optionalCommento = pratica.getCommenti().stream()
        .filter(temp -> temp.getId().toString().equalsIgnoreCase(idCommento)
            && temp.getDtCancellazione() == null && temp.getUtenteCancellazione() == null)
        .findFirst();

    return optionalCommento.orElseThrow(() -> new NotFoundException("Commento " + idCommento
        + " riferito alla pratica link_pratica " + processInstanceId + " non trovato"));

  }

  @Override
  public RiassuntoStatoPratica getPraticheStatoIdPraticaExt(String idPraticaExt) {
    RiassuntoStatoPratica p = new RiassuntoStatoPratica();
    it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica pratica =
        cosmoPraticheClient.getPraticheStatoIdPraticaExt(idPraticaExt);

    p.setPratica(praticheMapper.toPratica(pratica.getPratica()));

    PaginaCommenti pc = getCommenti(idPraticaExt);
    p.setCommenti(pc);

    PaginaTask pt = getPraticheIdStoricoTask(idPraticaExt);

    PaginaEventi pe = new PaginaEventi();
    List<Evento> ls = new ArrayList<>();

    pt.getElementi().forEach(t -> ls.add(eventoMapper.toEvento(t)));
    pe.setElementi(ls);
    p.setAttivita(pe);
    return p;
  }

  @Override
  public PaginaTask getPraticheIdStoricoTask(String id) {
    PaginaTask p = new PaginaTask();
    List<Task> lsTask = new ArrayList<>();

    TaskResponseWrapper response = cosmoCmmnClient.getPraticheIdStoricoAttivitaTask(id, "userTask");

    response.getData().stream().forEach(taskRes -> lsTask.add(taskMapper.toTask(taskRes)));

    p.setElementi(lsTask);
    return p;
  }

  @Override
  public PaginaTask getPraticheIdTasks(String id) {
    PaginaTask p = new PaginaTask();
    p.setElementi(cosmoCmmnClient.getPraticheTasksProcessInstanceId(id).getData().stream()
        .map(taskR -> taskMapper.toTask(taskR)).collect(Collectors.toList()));
    return p;
  }

  @Override
  public PaginaTask getPraticheTaskIdTaskSubtasks(String idTask) {
    return praticheMapper.toDTO(cosmoPraticheClient.getPraticheTaskIdTaskSubtasks(idTask));
  }

  @Override
  public FormTask getPraticheTaskIdTask(String idTask) {

    FormTask ft = new FormTask();
    Task task = ts.getTaskId(idTask);

    Pratica praticaProcesso = getPraticheId(task.getProcessInstanceId());
    String idPratica = praticaProcesso.getProcesso().getBusinessKey();

    Pratica pratica =
        praticheMapper.toPratica(cosmoPraticheClient.getPraticheIdPratica(idPratica, null));

    pratica.setProcesso(praticaProcesso.getProcesso());
    ft.setTask(task);
    ft.setPratica(pratica);



    List<Funzionalita> lsFunz = getListaFunzionalita(task.getName());
    ft.setFunzionalita(lsFunz);
    return ft;
  }


  // ------------------------- POST -----------------------------------


  @Override
  public Commento postCommenti(String processInstanceId, Commento body) {
    String methodName = "postCommenti";
    CosmoTCommento c = new CosmoTCommento();
    c.setUtenteCreatore(StringUtils.isNotEmpty(body.getCfAutore()) ? body.getCfAutore()
        : SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    c.setMessaggio(body.getMessaggio());
    CosmoTPratica pratica = this.getCosmoTPraticaFromProcessInstanceId(processInstanceId);
    c.setPratica(pratica);
    c.setDataCreazione(body.getTimestamp() != null
        ? Timestamp.valueOf(
            body.getTimestamp().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime())
            : new Timestamp(System.currentTimeMillis()));

    CosmoDTipoCommento tipoCommento = cosmoDTipoCommentoRepository
        .findOneByField(CosmoDTipoCommento_.codice, TipoCommento.COMMENTO.getCodice())
        .orElseThrow(() -> new NotFoundException(
            "Tipo Commento " + TipoCommento.COMMENTO.getCodice() + " non trovato"));
    c.setTipo(tipoCommento);

    this.cosmoTCommentoRepository.save(c);

    try {
      creaNotificaDelCommento(pratica.getId(),
          String.format(Constants.INSERIMENTO_COMMENTO, body.getMessaggio()));
    } catch (Exception e) {
      logger.error(methodName, "Errore in fase notifica creazione commento: " + e.getMessage());
    }

    return commentoMapper.toCommento(c);
  }

  @Override
  public void creaVistoGrafico(Long idAttivita) {

    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOneNotDeleted(idAttivita)
        .orElseThrow(() -> new BadRequestException(
            "creaVistoGrafico: Attivita con id " + idAttivita + " non trovata"));

    var utente = getUtenteCorrente();

    if (utente == null) {
      throw new BadRequestException("Utente non autenticato");
    }

    if (attivita.getCosmoTPratica().getDocumenti() != null
        && !attivita.getCosmoTPratica().getDocumenti().isEmpty()) {

      checkUniqueAttivitaUtenteDocumento(idAttivita, attivita.getCosmoTPratica().getDocumenti(),
          utente.getId());

      attivita.getCosmoTPratica().getDocumenti().forEach(documento -> {

        CosmoTApprovazione approvazione = new CosmoTApprovazione();
        approvazione.setCosmoTAttivita(attivita);
        approvazione.setCosmoTDocumento(documento);
        approvazione.setCosmoTUtente(utente);
        approvazione.setDtApprovazione(Timestamp.valueOf(LocalDateTime.now()));


        cosmoTApprovazioneRepository.save(approvazione);

      });
  }

  }

  private void checkUniqueAttivitaUtenteDocumento(Long idAttivita,
      List<CosmoTDocumento> documenti, Long idUtente) {
    var approvazioni = cosmoTApprovazioneRepository.findAllNotDeleted((root, cq, cb) -> {

      var predicate = cb.and(
          cb.equal(root.get(CosmoTApprovazione_.cosmoTAttivita).get(CosmoTAttivita_.id),
              idAttivita),
          cb.equal(root.get(CosmoTApprovazione_.cosmoTUtente).get(CosmoTUtente_.id),
              idUtente),
          root.get(CosmoTApprovazione_.cosmoTDocumento).in(documenti));

      return cq.where(predicate).getRestriction();
    });

    if (approvazioni != null && !approvazioni.isEmpty()) {
      throw new BadRequestException(
          String.format(ErrorMessages.APPROVAZIONE_ATTIVITA_UTENTE_DOCUMENTO, idAttivita,
              idUtente));
    }
  }


  private Optional<CosmoTFruitore> getFruitoreSeVisibile(String codiceFruitore,
      UserInfoDTO principal) {
    if (principal.getEnte() == null || principal.getEnte().getId() == null) {
      return Optional.empty();
    }

    var fruitoreOpt = cosmoTFruitoreRepository
        .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, codiceFruitore);

    if (fruitoreOpt.isEmpty()) {
      return Optional.empty();
    }

    var idEnte = principal.getEnte().getId();

    if (fruitoreOpt.get().getCosmoRFruitoreEntes().stream().filter(CosmoREntity::valido)
        .anyMatch(r -> r.getId().getIdEnte().equals(idEnte) && r.getCosmoTEnte().nonCancellato())) {
      return fruitoreOpt;
    }

    return Optional.empty();
  }

  @Override
  @Transactional
  public OperazioneAsincrona postPratiche(CreaPraticaRequest body) {
    final var method = "postPratiche";

    var user = SecurityUtils.getUtenteCorrente();
    Timestamp now = Timestamp.from(Instant.now());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "ricevuta richiesta di creazione pratica da utente {}: {}",
          user.getCodiceFiscale(), ObjectUtils.represent(body));
    } else {
      logger.info(method, "ricevuta richiesta di creazione pratica da utente {}",
          user.getCodiceFiscale());
    }

    var utente = getUtenteCorrente();
    var fruitore = getFruitoreCorrente();

    var future = asyncTaskService.start("Creazione pratica", task -> {

      task.step("Validazione della richiesta", step -> {
        ValidationUtils.require(body, "body");
        ValidationUtils.validaAnnotations(body);
        if (user.getEnte() == null || StringUtils.isBlank(user.getEnte().getTenantId())) {
          throw new ForbiddenException("Utente non associato ad un ente");
        }

        // CHECK VISIBILITA FRUITORE SE RICHIESTO
        if (!StringUtils.isBlank(body.getCodiceFruitore())) {
          getFruitoreSeVisibile(body.getCodiceFruitore(), user)
          .orElseThrow(() -> new ForbiddenException( // NOSONAR
              "Operativita' per conto del fruitore specificato non permessa"));
        }
      });

      Long idPratica = task.step("Registrazione della pratica", step -> {

        var creaPraticaRequest = new it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest();
        creaPraticaRequest.setOggetto(body.getOggetto());
        creaPraticaRequest.setRiassunto(body.getRiassunto());
        creaPraticaRequest.setCodiceIpaEnte(user.getEnte().getTenantId());
        creaPraticaRequest.setCodiceTipologia(body.getCodiceTipo());
        creaPraticaRequest.setUtenteCreazionePratica(user.getCodiceFiscale());
        creaPraticaRequest.setCodiceFruitore(body.getCodiceFruitore());

        return cosmoPraticheClient.postPratiche(creaPraticaRequest).getId().longValue();
      });

      task.step("Avvio del processo", step -> {

        var praticaDB = cosmoTPraticaRepository.findOneNotDeleted(idPratica)
            .orElseThrow(() -> new InternalServerException(
                "Pratica creata ma non reperibile da DB per l'avvio."));

        logger.info(method,
            "creata pratica con id {} dall'utente {}, ora tento di avviare il processo", idPratica,
            user.getCodiceFiscale());

        var processoAvviato = doAvviaProcesso(praticaDB, step);

        if (logger.isDebugEnabled()) {
          logger.debug(method, "avviato il processo {} per la pratica con id {} dall'utente {}: {}",
              processoAvviato.getId(), idPratica, user.getCodiceFiscale(),
              ObjectUtils.represent(processoAvviato));
        } else {
          logger.info(method, "avviato il processo {} per la pratica con id {} dall'utente {}",
              processoAvviato.getId(), idPratica, user.getCodiceFiscale());
        }

        return praticaDB.getId();
      });

      if (body.getIdPraticaEsistente() != null) {

        var praticaA = cosmoTPraticaRepository.findOneNotDeleted(idPratica)
            .orElseThrow(() -> new InternalServerException(
                "Pratica creata ma non reperibile da DB per l'avvio."));

        if (body.isDaAssociare() != null && Boolean.TRUE.equals(body.isDaAssociare())) {
          task.step("Associazione pratiche", step -> {
            cosmoPraticheClient.putPraticheInRelazione(praticaA.getId().toString(),
                TipoRelazionePraticaPratica.DUPLICA.getCodice(),
                Arrays.asList(new BigDecimal(body.getIdPraticaEsistente())));
          });
        }

        if (body.getDocumentiDaDuplicare() != null && !body.getDocumentiDaDuplicare().isEmpty()) {
          task.step("Preparazione duplicazione documenti", step -> {
            ecmDocumentiFeignClient.preparaDuplicazione(body.getIdPraticaEsistente().intValue(),
                praticaA.getId().intValue(), false);
          });

          RelazioniDocumentoDuplicato docDaDuplicareDocNuovo = new RelazioniDocumentoDuplicato();

          for (int i = 0; i < body.getDocumentiDaDuplicare().size(); i++) {
            int count = i;

            task.step("Duplicazione documento " + (count + 1) + " di "
                + body.getDocumentiDaDuplicare().size(), step -> {
                  logger.debug(method, "Duplicazione documento " + (count + 1) + " di "
                      + body.getDocumentiDaDuplicare().size());

                  var response = ecmDocumentiFeignClient.duplicaDocumento(
                      body.getDocumentiDaDuplicare().get(count).intValue(),
                      praticaA.getId().intValue(), docDaDuplicareDocNuovo);

                  response.getRelazioneDocumenti().stream().forEach(rel -> {
                    if (docDaDuplicareDocNuovo.getRelazioneDocumenti().stream().noneMatch(
                        p -> p.getIdDocumentoDaDuplicare().equals(rel.getIdDocumentoDaDuplicare())
                        && p.getIdDocumentoDuplicato().equals(rel.getIdDocumentoDuplicato()))) {
                      docDaDuplicareDocNuovo.getRelazioneDocumenti().add(rel);
                    }
                  });

                });
          }
        }
      }

      task.step("Salvataggio delle informazioni di storico", step -> {
        // inserisco il log dell'evento su DB
        CosmoTPratica pratica = cosmoTPraticaRepository.findOne(idPratica);

        //@formatter:off
        storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
            .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_CREATA)
            .withDescrizioneEvento("La pratica e' stata creata.")
            .withPratica(pratica)
            .withDtEvento(now)
            .withUtente(utente)
            .withFruitore(fruitore)
            .build());
        //@formatter:on

        // inserisco il log dell'evento su DB
        //@formatter:off
        storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
            .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_AVVIATA)
            .withDescrizioneEvento("La lavorazione della pratica e' stata avviata.")
            .withPratica(pratica)
            .withDtEvento(Timestamp.from(now.toInstant().plusSeconds(1)))
            .withUtente(utente)
            .withFruitore(fruitore)
            .build());
        //@formatter:on
      });

      Pratica output = new Pratica();
      output.setId(idPratica.intValue());
      return output;
    });

    OperazioneAsincrona output = new OperazioneAsincrona();
    output.setUuid(future.getTaskId());
    return output;
  }

  // ------------------------- PUT ------------------------------------

  @Override
  public Pratica putPratiche(String id, Pratica body, Boolean assegnaAttivita) {

    return praticheMapper.toPratica(cosmoPraticheClient.putPraticheIdPratica(id, assegnaAttivita,
        praticheMapper.praticaBusinessToPratiche(body)));
  }

  // ------------------------- DELETE ---------------------------------

  @Override
  public Void deletePraticheIdCommentiIdCommento(String idCommento, String processInstanceId) {
    CosmoTCommento cosmoTCommento = getCosmoTCommento(idCommento, processInstanceId);
    if (!cosmoTCommento.getUtenteCreatore()
        .equals(SecurityUtils.getUtenteCorrente().getCodiceFiscale())) {
      throw new ForbiddenException("Impossibile eliminare commenti");
    }

    cosmoTCommento.setDtCancellazione(new Timestamp(System.currentTimeMillis()));
    cosmoTCommento.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    this.cosmoTCommentoRepository.save(cosmoTCommento);
    return null;
  }

  @Override
  public void deletePraticheId(String idPratica) {
    // Eseguo il controllo se l'utente ha l'abilitazione ADMIN_PRAT

    String methodName = "deletePraticheId";

    it.csi.cosmo.cosmopratiche.dto.rest.Pratica pratica =
        cosmoPraticheClient.getPraticheIdPratica(idPratica, null);

    cosmoPraticheClient.deletePraticheIdPratica(idPratica);

    String idFromLink = getIdFromLink(pratica.getLinkPratica());

    if (idFromLink != null) {
      ExecutionWrapper executionWrapper =
          cosmoCmmnClient.getExecutions("ANNULLA_PRATICA", idFromLink);

      if (executionWrapper != null && !executionWrapper.getData().isEmpty()) {
        String executionId = executionWrapper.getData().get(0).getId();
        var executionActionRequest = new ExecutionActionRequest();
        executionActionRequest.setAction("messageEventReceived");
        executionActionRequest.setMessageName("ANNULLA_PRATICA");
        cosmoCmmnClient.putExecution(executionId, executionActionRequest);

        logger.info(methodName, "putExecution ANNULLA_PRATICA eseguita per executionId "
            + executionId + " per la pratica con id " + idPratica);

      } else {
        logger.info(methodName, "putExecution ANNULLA_PRATICA NON eseguita per la pratica con id "
            + idPratica + " : executionId non presente");
      }

      String action = "{\"action\": \"suspend\"}";

      try {
        cosmoCmmnClient.putPraticaIdPraticaExt(idFromLink, util.toJson(action));
        logger.info(methodName, "putPraticaIdPraticaExt suspend eseguita per processInstanceId "
            + idFromLink + " per la pratica con id " + idPratica);
      } catch (Exception e) {
        logger.info(methodName, "putPraticaIdPraticaExt suspend NON eseguita per la pratica con id "
            + idPratica + " : processInstanceId non presente");
      }
    }
  }

  @Override
  public ProcessInstanceResponse avviaProcesso(CosmoTPratica pratica) {
    return doAvviaProcesso(pratica, null);
  }

  private ProcessInstanceResponse doAvviaProcesso(CosmoTPratica pratica, LongTask<?> step) {
    final var method = "avviaProcesso";

    // recupero la processDefinitionKey dal tipo pratica e la businessKey dalla pratica
    String processDefKey = recuperaProcessDefinitionKey(pratica);
    String businessKey = recuperaBusinessKey(pratica);

    // costruisco il payload per flowable
    ProcessInstanceCreateRequest payload = new ProcessInstanceCreateRequest();
    payload.setBusinessKey(businessKey);
    payload.setProcessDefinitionKey(processDefKey);
    payload.setTenantId(pratica.getEnte().getCodiceIpa());

    //@formatter:off
    List<RestVariable> variables = new ArrayList<>();
    payload.setVariables(variables);

    variables.addAll(
        Arrays.asList(
            CommonUtils.variable(VARIABLE_OGGETTO, pratica.getOggetto()),
            CommonUtils.variable(VARIABLE_INITIATOR, pratica.getUtenteCreazionePratica()),
            CommonUtils.variable(VARIABLE_ID_PRATICA, pratica.getId())));
    //@formatter:on

    // eseguo eventuali trasformazioni per la fase beforeProcessStart
    LinkedList<CosmoDTrasformazioneDatiPratica> trasformazioni =
        pratica.getTipo().getTrasformazioni().stream().filter(CosmoDEntity::valido)
        .sorted((e1, e2) -> (e1.getOrdine() != null ? e1.getOrdine() : Integer.valueOf(0))
            .compareTo((e2.getOrdine() != null ? e2.getOrdine() : Integer.valueOf(0))))
        .collect(Collectors.toCollection(LinkedList::new));

    for (var trasformazione : trasformazioni) {
      if (!trasformazione.getCodiceFase()
          .equalsIgnoreCase(FaseTrasformazioneDatiPratica.BEFORE_PROCESS_START.getCodice())) {
        continue;
      }
      if (step != null) {
        step.step(trasformazione.getDescrizione(), tStep -> {
          applicaTrasformazione(pratica, trasformazione, variables, null);
          return null;
        });
      } else {
        applicaTrasformazione(pratica, trasformazione, variables, null);
      }
    }

    try {
      ProcessInstanceWrapper risultato =
          cosmoCmmnClient.getProcessInstancesByBusinessKey(businessKey);
      if (risultato != null && risultato.getSize() > 0) {
        throw new ConflictException("Processo con id " + businessKey + " già esistente");
      }
    } catch (FeignClientClientErrorException e) {
      if (e.getResponse() != null && !StringUtils.isBlank(e.getResponse().getTitle())) {
        throw new BadRequestException(e.getResponse().getTitle(), e);
      } else {
        throw new InternalServerException(
            "Errore nella chiamata interna per l'ottenimento di un dato processo", e);
      }
    }

    // eseguo la POST a cmmn
    if (logger.isDebugEnabled()) {
      logger.debug(method,
          "invio messaggio per avvio del processo relativo alla pratica {} con parametri: {}",
          pratica.getId(), ObjectUtils.toJson(payload));
    }

    ProcessInstanceResponse result;
    try {
      result = cosmoCmmnClient.postProcessInstance(payload);
      cosmoCmmnSyncFeignClient.putProcessInstanceVariables(result.getId(), payload.getVariables());

    } catch (FeignClientClientErrorException e) {

      transactionService.inTransaction(() -> checkProcessInstance(businessKey));

      if (e.getResponse() != null && !StringUtils.isBlank(e.getResponse().getTitle())) {
        throw new BadRequestException(e.getResponse().getTitle(), e);
      } else if (e.getRawStatusCode() == 409) {
        throw new ConflictException("Operazione non consentita per lo stato corrente della pratica",
            e);
      } else {
        throw new InternalServerException("Errore nella chiamata interna per l'avvio del processo",
            e);
      }
    } 
    catch (FeignClientServerErrorException e) {

      transactionService.inTransaction(() -> checkProcessInstance(businessKey));

      if (e.getResponse() != null && !StringUtils.isBlank(e.getResponse().getTitle())) {
        throw new BadRequestException(e.getResponse().getTitle(), e);
      } else {
        throw new InternalServerException("Errore nella chiamata interna per l'avvio del processo",
            e);
      }
    } catch (Exception e) {

      // transactionService.inTransaction(() -> checkProcessInstance(businessKey));

      throw new InternalServerException("Errore imprevisto nell'avvio del processo", e);
    }

    if (logger.isDebugEnabled()) {
      logger.debug(method, "ricevuta risposta per avvio del processo relativo alla pratica {}: {}",
          pratica.getId(), ObjectUtils.toJson(result));
    }

    // eseguo eventuali trasformazioni per la fase afterProcessStart

    for (var trasformazione : trasformazioni) {
      if (!trasformazione.getCodiceFase()
          .equalsIgnoreCase(FaseTrasformazioneDatiPratica.AFTER_PROCESS_START.getCodice())) {
        continue;
      }

      // lazy load
      List<RestVariable> variabiliAttuali = new ArrayList<>();

      if (step != null) {
        step.step(trasformazione.getDescrizione(), tStep -> {
          applicaTrasformazione(pratica, trasformazione, variabiliAttuali, result.getId());
          cosmoCmmnSyncFeignClient.putProcessInstanceVariables(result.getId(), variabiliAttuali);
          return null;
        });
      } else {
        applicaTrasformazione(pratica, trasformazione, variabiliAttuali, result.getId());
        cosmoCmmnSyncFeignClient.putProcessInstanceVariables(result.getId(), variabiliAttuali);
      }
    }

    return result;
  }

  protected void applicaTrasformazione(CosmoTPratica pratica,
      CosmoDTrasformazioneDatiPratica trasformazione, List<RestVariable> variables,
      String processInstanceId) {

    try {
      var sandboxContext = SandboxFactory.buildStatoPraticaSandbox(pratica, statoPraticaService);
      sandboxContext.forceProcessInstanceId(processInstanceId);

      var mapped = motoreJsonDinamicoService.eseguiMappatura(trasformazione.getDefinizione(),
          sandboxContext);
      if (mapped == null) {
        return;
      }

      if (mapped.getNodeType() != JsonNodeType.OBJECT || !(mapped instanceof ObjectNode)) {
        throw new InternalServerException("La specifica di mappatura deve costruire un oggetto");
      }
      var objectNode = (ObjectNode) mapped;

      objectNode.fields().forEachRemaining(field -> {
        variables.removeIf(v -> v.getName().equalsIgnoreCase(field.getKey()));
        var v = field.getValue();
        Object writeValue;
        if (v.isNull()) {
          writeValue = null;
        } else if (v.isArray() || v.isObject()) {
          writeValue = v;
        } else if (v.isTextual()) {
          writeValue = v.textValue();
        } else if (v.isBoolean()) {
          writeValue = v.asBoolean();
        } else if (v.isFloatingPointNumber()) {
          writeValue = v.asDouble();
        } else if (v.isIntegralNumber()) {
          writeValue = v.asLong();
        } else {
          writeValue = v.asText();
        }
        variables.add(CommonUtils.variable(field.getKey(), writeValue));
      });

    } catch (Exception e) {
      logger.error("applicaTrasformazione", "errore nella trasformazione \""
          + trasformazione.getDescrizione() + "\" sulla pratica " + pratica.getId(), e);
      if (trasformazione.getObbligatoria() != null
          && trasformazione.getObbligatoria().booleanValue()) {
        throw new InternalServerException(
            "errore nella trasformazione \"" + trasformazione.getDescrizione() + "\"", e);
      }
    }
  }

  protected String recuperaProcessDefinitionKey(CosmoTPratica pratica) {
    if (pratica == null) {
      throw new InvalidParameterException();
    }

    if (pratica.getTipo() == null) {
      throw new InternalServerException(
          "La pratica specificata non e' associata a nessuna tipologia di pratica");
    } else if (StringUtils.isBlank(pratica.getTipo().getProcessDefinitionKey())) {
      throw new InternalServerException(
          "La ProcessDefinitionKey della pratica specificata non e' codificata");
    }

    return pratica.getTipo().getProcessDefinitionKey();
  }

  protected String recuperaBusinessKey(CosmoTPratica pratica) {
    if (pratica == null || pratica.getId() == null) {
      throw new InvalidParameterException();
    }

    return pratica.getId().toString();
  }

  protected String getIdFromLink(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    raw = raw.strip();
    while (raw.startsWith("/")) {
      raw = raw.substring(1).strip();
    }
    if (raw.contains("/")) {
      return raw.substring(raw.indexOf('/') + 1);
    }
    return null;
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

  @Override
  public void creaNotificaDelCommento(Long idPratica, String commento) {

    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(idPratica);
    request.setTipoNotifica(TipoNotifica.COMMENTO.getCodice());
    request.setMessaggio(commento);
    request.setClasse("SUCCESS");
    request.setEvento(Constants.EVENTS.COMMENTI);

    try {
      notificheGlobaliFeignClient.postNotificheGlobali(request);
    } catch (Exception e) {
      logger.error("creaNotificaDelCommento", "Notifica del commento non creata", e);
    }
  }

  @Override
  public VariabiliProcessoResponse getVariablesFromHistoryProcess(String processInstanceId,
      Boolean includeProcessVariables) {

    var pirs = this.cosmoCmmnClient
        .getHistoricProcessInstances(processInstanceId, includeProcessVariables).getData();

    var process =
        pirs.stream().filter(pir -> processInstanceId.equals(pir.getId())).findFirst().orElse(null);

    var response = new VariabiliProcessoResponse();
    if (process != null) {
      response.setProcessInstanceId(processInstanceId);

      process.getVariables().forEach(variable -> {
        VariabileProcesso variabile = new VariabileProcesso();

        variabile.setName(variable.getName());
        variabile.setScope(variable.getScope());
        variabile.setType(variable.getType());
        variabile.setValue(variable.getValue());
        if (variable.getValue() == null && variable.getValueUrl() != null) {
          variabile.setValue(this.getSerializedHistoryVariabileValue(processInstanceId, variable));
        }
        response.getVariabili().add(variabile);
      });
    }
    return response;
  }

  private Object getSerializedHistoryVariabileValue(String processInstanceId,
      RestVariable restVariable) {
    var methodName = "getSerializedVariabileValue";
    Object value = null;
    byte[] raw = this.cosmoCmmnClient.getSerializedHistoryVariable(processInstanceId,
        restVariable.getName());
    if (raw != null) {
      ByteArrayInputStream bis = new ByteArrayInputStream(raw);
      try (ObjectInput in = new ObjectInputStream(bis)) {
        value = in.readObject();
      } catch (IOException | ClassNotFoundException e) {
        logger.error(methodName, e.getMessage());
        throw new FlowableVariableHandlingException(e);
      }
    }
    return value;
  }

  @Override
  public ProcessInstanceResponse avviaProcesso(String idPratica) {

    CosmoTPratica pratica =
        cosmoTPraticaRepository.findOneByField(CosmoTPratica_.id, Long.valueOf(idPratica))
        .orElseThrow(() -> new NotFoundException("Pratica non trovata"));
    return avviaProcesso(pratica);
  }

  private void checkProcessInstance(String businessKey) {

    final String methodName = "checkProcessInstance";

    ProcessInstanceWrapper risultato = null;

    try {
      risultato = cosmoCmmnClient.getProcessInstancesByBusinessKey(businessKey);
    } catch (FeignClientClientErrorException e) {
      throw new BadRequestException(e.getResponse().getTitle(), e);
    }

    if (risultato == null || risultato.getSize() == 0) {

      var pratica = cosmoTPraticaRepository.findOneNotDeleted(Long.valueOf(businessKey))
          .orElseThrow(() -> new BadRequestException(
              String.format(ErrorMessages.PRATICA_NON_TROVATA, businessKey)));

      pratica.setLinkPratica(null);
      cosmoTPraticaRepository.save(pratica);

      logger.info(methodName, "Rimosso link pratica sulla pratica " + businessKey);
    }
  }
}