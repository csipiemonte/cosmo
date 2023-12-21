/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.business.service.CallbackStatoPraticaService;
import it.csi.cosmo.cosmobusiness.business.service.FruitoriService;
import it.csi.cosmo.cosmobusiness.business.service.MailService;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaEsitoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaAttivitaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaEsitoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaEsitoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaPraticaEsternaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoInvioSegnaleRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.GetPraticaFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaSegnaleFruitoreResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDTipoPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEndpointFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnAsyncFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.CommonUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmocmmn.dto.rest.InviaSegnaleProcessoRequest;


@Service
@Transactional
public class FruitoriServiceImpl implements FruitoriService {

  private static final String DUMP_OUTPUT = "output = {}";

  private static final String DUMP_INPUT = "input = {}";

  private static final String VARIABLE_TYPE_DATE = "date";

  private static final String VARIABLE_SCOPE_LOCAL = "local";

  private static final String ERR_CONFLICT =
      "Operazione non consentita per lo stato corrente dell'evento";

  private static final String ACTION_COMPLETE = "complete";

  private static final String VARIABLE_ESEGUITO = "eseguito";

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, "FruitoriServiceImpl");

  @Autowired
  private PracticeService praticaService;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Autowired
  private CosmoDTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoCmmnAsyncFeignClient cosmoCmmnAsyncFeignClient;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CallbackStatoPraticaService fruitoriCallbackService;

  @Autowired
  private CallbackService callbackService;

  @Autowired
  private MailService mailService;

  @Autowired
  private ProcessService processService;

  @Override
  public GetPraticaFruitoreResponse getPratica(String idPratica) {

    final var method = "getPratica";
    logger.debug(method, "richiesta getPratica da parte di fruitore esterno per la pratica {}",
        idPratica);

    var contesto = getContestoChiamataCorrente(null, idPratica);

    return fruitoriCallbackService.getStatoPratica(contesto.pratica.getId());
  }

  @Override
  public AvviaProcessoFruitoreResponse avviaProcesso(AvviaProcessoFruitoreRequest body) {
    final var method = "postProcesso";
    logger.debug(method,
        "richiesta di avvio processo da parte di fruitore esterno per la pratica {}/{}",
        body.getCodiceIpaEnte(), body.getIdPratica());

    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_INPUT, ObjectUtils.represent(body));
    }

    // Validazione campi obbligatori
    ValidationUtils.require(body, "request body (AvviaProcessoFruitoreRequest)");
    ValidationUtils.validaAnnotations(body);

    // ottengo info su fruitore, ente e pratica
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), body.getIdPratica());

    // recupero la processDefinitionKey dal tipo pratica e la businessKey dalla pratica
    ProcessInstanceResponse result = praticaService.avviaProcesso(contesto.pratica);

    logger.debug(method, "avviato il processo {} relativo alla pratica {} da parte del fruitore {}",
        result.getId(), contesto.pratica.getId(), contesto.fruitore.getApiManagerId());

    // inserisco il log dell'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_AVVIATA)
        .withDescrizioneEvento("La lavorazione della pratica e' stata avviata.")
        .withPratica(contesto.pratica)
        .build());
    //@formatter:on

    // costruisco output
    AvviaProcessoFruitoreResponse output = new AvviaProcessoFruitoreResponse();
    if (contesto.pratica.getEnte() != null) {
      output.setCodiceIpaEnte(contesto.pratica.getEnte().getCodiceIpa());
    }

    output.setIdPratica(contesto.pratica.getIdPraticaExt());
    output.setIdPraticaCosmo(contesto.pratica.getId().intValue());
    output.setIdProcesso(result.getId());

    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_OUTPUT, ObjectUtils.represent(output));
    }

    return output;
  }

  @Override
  public CreaEventoFruitoreResponse creaEvento(CreaEventoFruitoreRequest body) {
    final var method = "creaEvento";
    logger.debug(method, "richiesta di creazione evento da parte di fruitore esterno");
    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_INPUT, ObjectUtils.represent(body));
    }

    ValidationUtils.require(body, "creaEvento request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo info su fruitore, ente e pratica
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), null);

    // verifico l'utente destinatario
    var utenteDestinatario = verificaUtentePerEnte(body.getDestinatario(), contesto.ente);

    // eseguo la POST su cmmn
    Task createTaskRequest = new Task();
    createTaskRequest.setAssignee(utenteDestinatario.getCodiceFiscale());
    createTaskRequest.setName(CommonUtils.clean(body.getTitolo()));
    createTaskRequest.setDescription(CommonUtils.clean(body.getDescrizione()));
    createTaskRequest.setDueDate(body.getScadenza().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

    TaskResponse createTaskResponse;

    try {
      createTaskResponse = cosmoCmmnFeignClient.postTask(createTaskRequest);

    } catch (Exception e) {
      logger.error(method, "errore nella creazione del task per un evento", e);
      throw new InternalServerException(
          "Si e' verificato un errore imprevisto nella creazione del task. Contattare l'assistenza.",
          e);
    }

    var output = new CreaEventoFruitoreResponse();
    output.setIdEvento(createTaskResponse.getId());

    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_OUTPUT, ObjectUtils.represent(output));
    }

    return output;
  }

  @Override
  public AggiornaEventoFruitoreResponse aggiornaEvento(String idEvento,
      AggiornaEventoFruitoreRequest body) {
    final var method = "aggiornaEvento";
    logger.debug(method, "richiesta di aggiornamento evento da parte di fruitore esterno");
    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_INPUT, ObjectUtils.represent(body));
    }

    ValidationUtils.require(idEvento, "idEvento");
    ValidationUtils.require(body, "aggiornaEvento request body");
    ValidationUtils.validaAnnotations(body);

    List<String> actions = new LinkedList<>();

    // ottengo info su fruitore, ente e pratica
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), null);

    /*
     * Per verificare la correttezza della modifica dell'evento e' necessario verificare
     * preliminarmente, tramite una chiamata al task, l'assegnatario dell'evento che stiamo per
     * modificare.
     */
    TaskResponse task = getTaskById(idEvento).orElseThrow(() -> new NotFoundException(
        "Il task corrispondente all'id specificato non e' stato trovato."));

    /*
     * restituisce tra i dati l'assignee che, in cosmo_r_utente_ente deve essere associato all'ente
     * indicato in codiceIpaEnte. In caso contrario servizio restituisce un errore.
     */
    if (StringUtils.isBlank(task.getAssignee())) {
      throw new BadRequestException(
          "Nessun assegnatario per il task corrente. Impossibile verificare l'associazione con l'ente.");
    }
    verificaUtentePerEnte(task.getAssignee(), contesto.ente);

    /*
     * cosomobe dopo aver verificato la correttezza dei dati in input veicola la chiamata su
     * cosmocmmn/api/process/runtime/tasks/:idEvento(PUT) con i dati di input valorizzati nella
     * chiamata a cosmobe
     */
    var hasNewTitolo = body.getNuovoTitolo() != null;
    var hasNewDescrizione = body.getNuovaDescrizione() != null;
    var hasNewScadenza = body.getNuovaDataScadenza() != null;

    if (hasNewTitolo || hasNewDescrizione || hasNewScadenza) {
      Task updateTaskRequest = new Task();
      updateTaskRequest.setAssignee(task.getAssignee());
      updateTaskRequest
      .setName(hasNewTitolo ? CommonUtils.clean(body.getNuovoTitolo()) : task.getName());
      updateTaskRequest
      .setDescription(hasNewDescrizione ? CommonUtils.clean(body.getNuovaDescrizione())
          : task.getDescription());
      updateTaskRequest.setDueDate(hasNewScadenza
          ? body.getNuovaDataScadenza().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
              : CommonUtils.dateToISO(task.getDueDate()));

      updateTaskById(idEvento, updateTaskRequest);
      actions.add("Dati dell'evento aggiornati.");
    }

    /*
     * il sistema esterno puo' comunicare l'avvenuta esecuzione dell'evento valorizzando a true il
     * dato in input 'eseguito'. La chiamata viene veicolata su
     * cosmocmmn/api/process/runtime/tasks/:idEvento /variables(POST) per valorizzare una variabile
     * di task che registri l'informazione di esecuzione.
     */
    if (Boolean.TRUE.equals(body.isEseguito())) {
      updateTaskExecution(idEvento);
      actions.add("Evento marcato come 'eseguito'.");
    }

    AggiornaEventoFruitoreResponse output = new AggiornaEventoFruitoreResponse();
    output.setMessaggio(actions.isEmpty() ? "Nessuna operazione eseguita"
        : actions.stream().collect(Collectors.joining("; ")));

    if (logger.isDebugEnabled()) {
      logger.debug(method, DUMP_OUTPUT, ObjectUtils.represent(output));
    }
    return output;
  }

  @Override
  public void eliminaEvento(String idEvento, EliminaEventoFruitoreRequest body) {
    final var method = "eliminaEvento";
    logger.debug(method, "richiesta di eliminazione evento da parte di fruitore esterno");


    ValidationUtils.require(idEvento, "idEvento");
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo info su fruitore, ente e pratica
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), null);

    /*
     * Per verificare la correttezza della modifica dell'evento e' necessario verificare
     * preliminarmente, tramite una chiamata al task, l'assegnatario dell'evento che stiamo per
     * modificare.
     */
    TaskResponse task = getTaskById(idEvento).orElseThrow(() -> new NotFoundException(
        "Il task corrispondente all'id specificato non e' stato trovato."));

    /*
     * restituisce tra i dati l'assignee che, in cosmo_r_utente_ente deve essere associato all'ente
     * indicato in codiceIpaEnte. In caso contrario servizio restituisce un errore.
     */
    if (StringUtils.isBlank(task.getAssignee())) {
      throw new BadRequestException(
          "Nessun assegnatario per il task corrente. Impossibile verificare l'associazione con l'ente.");
    }
    verificaUtentePerEnte(task.getAssignee(), contesto.ente);

    updateTaskCompletion(idEvento);
  }

  @Override
  public InviaSegnaleFruitoreResponse inviaSegnaleProcesso(String idPratica,
      InviaSegnaleFruitoreRequest body) {
    final var method = "inviaSegnaleProcesso";
    logger.debug(method, "richiesta di invio segnale {} a processo da parte di fruitore esterno {}",
        body.getCodiceSegnale(), (logger.isDebugEnabled() ? ObjectUtils.represent(body) : ""));

    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo info su fruitore, ente e pratica
    var contesto = getContestoChiamataCorrente(null, idPratica);

    var processInstanceId = contesto.pratica.getProcessInstanceId();
    ValidationUtils.require(processInstanceId, "pratica.processInstanceId");

    // se e' richiesto l'aggiornamento delle variabili lo faccio prima di inviare il segnale
    if (body.getVariabili() != null && !body.getVariabili().isEmpty()) {
      var payloadVariabili = body.getVariabili().stream().map(reqVariabile -> {
        RestVariable mapped = new RestVariable();
        mapped.setName(reqVariabile.getNome());
        mapped.setValue(reqVariabile.getValore());
        return mapped;
      }).collect(Collectors.toList());

      logger.debug(method,
          "invio richiesta di aggiornamento variabili per la pratica {} (processo {}) {}",
          contesto.pratica.getId(), processInstanceId,
          (logger.isDebugEnabled() ? ObjectUtils.represent(payloadVariabili) : ""));

      this.cosmoCmmnFeignClient.putProcessInstanceVariables(processInstanceId, payloadVariabili);

      logger.debug(method, "effettuato aggiornamento variabili per la pratica {} (processo {})",
          contesto.pratica.getId(), processInstanceId);
    }

    // invia segnale al processo
    RiferimentoOperazioneAsincrona rispostaAsync;

    try {
      var reqPayload = new InviaSegnaleProcessoRequest();
      reqPayload.setCodiceSegnale(body.getCodiceSegnale());

      logger.debug(method, "invio richiesta di invio segnale per la pratica {} (processo {}) {}",
          contesto.pratica.getId(), processInstanceId,
          (logger.isDebugEnabled() ? ObjectUtils.represent(reqPayload) : ""));

      rispostaAsync =
          this.cosmoCmmnAsyncFeignClient.inviaSegnaleProcesso(processInstanceId, reqPayload);

      logger.debug(method, "inviato segnale per la pratica {} (processo {}) con codice invio {}",
          contesto.pratica.getId(), processInstanceId, rispostaAsync.getUuid());

    } catch (FeignClientClientErrorException | FeignClientServerErrorException e) {
      // let error handler convert the exception
      throw e;

    } catch (Exception e) {
      // signal send failed synchronously
      throw new InternalServerException("Errore nella preparazione dell'invio del segnale", e);
    }

    // spawn di un task asincrono per "seguire" il task asincrono remoto e schedulare la callback al
    // termine
    asyncTaskService.start("Attesa dell'elaborazione del segnale al processo", task -> {

      task.step("attesa dell'elaborazione su Flowable", step -> {
        try {

          logger.debug(method,
              "attesa di fine elaborazione segnale per la pratica {} (processo {}) con codice invio {}",
              contesto.pratica.getId(), processInstanceId, rispostaAsync.getUuid());

          var remoteAsyncTaskResult = asyncTaskService.wait(rispostaAsync.getUuid(), 15 * 60L);

          logger.debug(method,
              "elaborazione segnale per la pratica {} (processo {}) terminata in stato {}",
              contesto.pratica.getId(), processInstanceId, remoteAsyncTaskResult.getStatus());

          if (remoteAsyncTaskResult.getStatus().equals(LongTaskStatus.COMPLETED)) {

            processEsitoInvioSegnale(contesto.pratica, rispostaAsync.getUuid(), body);

          } else {

            Map<String, Object> dettagliErrore = new HashMap<>();
            dettagliErrore.put("class", "processError");
            dettagliErrore.put("message", remoteAsyncTaskResult.getErrorMessage());
            dettagliErrore.put("details", remoteAsyncTaskResult.getErrorDetails());

            processEsitoInvioSegnale(contesto.pratica, rispostaAsync.getUuid(), body,
                remoteAsyncTaskResult.getErrorMessage(), dettagliErrore);
          }

        } catch (Exception e) {

          logger.debug(method, "elaborazione segnale fallita", e);

          Map<String, Object> dettagliErrore = new HashMap<>();
          dettagliErrore.put("class", e.getClass().getSimpleName());
          dettagliErrore.put("message", e.getMessage());

          processEsitoInvioSegnale(contesto.pratica, rispostaAsync.getUuid(), body, e.getMessage(),
              dettagliErrore);
        }
      });

      return null;
    });

    InviaSegnaleFruitoreResponse output = new InviaSegnaleFruitoreResponse();
    output.setCodiceInvio(rispostaAsync.getUuid());
    return output;
  }

  private void processEsitoInvioSegnale(CosmoTPratica pratica, String codiceInvio,
      InviaSegnaleFruitoreRequest body) {
    processEsitoInvioSegnale(pratica, codiceInvio, body, null, null);
  }

  private void processEsitoInvioSegnale(CosmoTPratica pratica, String codiceInvio,
      InviaSegnaleFruitoreRequest body, String messaggioErrore, Object dettagliErrore) {
    final var method = "processEsitoInvioSegnale";
    final var processInstanceId = pratica.getProcessInstanceId();

    boolean successo = StringUtils.isBlank(messaggioErrore) && dettagliErrore == null;

    logger.debug(method,
        "elaborazione segnale per la pratica {} (processo {}) terminata con risultato: {}",
        pratica.getId(), processInstanceId, successo);

    boolean esisteEndpoint = verificaEsistenzaEndpoint(OperazioneFruitore.CALLBACK_INVIO_SEGNALE,
        pratica.getFruitore().getId(), null).isPresent();

    logger.debug(method, "esistenza di endpoint di callback configurato per il fruitore {}: {}",
        pratica.getFruitore().getNomeApp(), esisteEndpoint);

    boolean inviaCallback;

    if (Boolean.TRUE.equals(body.isRichiediCallback()) && esisteEndpoint) {
      inviaCallback = true;
    } else if (body.isRichiediCallback() == null) {
      inviaCallback = esisteEndpoint;
    } else {
      inviaCallback = false;
    }

    if (inviaCallback) {
      logger.debug(method,
          "schedulo invio callback di esito segnale per la pratica {} (processo {})",
          pratica.getId(), processInstanceId, successo);

      Map<String, Object> parametri = new HashMap<>();
      parametri.put(CallbackStatoPraticaServiceImpl.FIELD_ID_PRATICA, pratica.getId());
      parametri.put(CallbackStatoPraticaServiceImpl.FIELD_ID_PRATICA_EXT,
          pratica.getIdPraticaExt());

      var payload = new EsitoInvioSegnaleRequest();
      payload.setCodiceInvio(codiceInvio);
      payload.setCodiceSegnale(body.getCodiceSegnale());
      payload.setDettagliErrore(dettagliErrore);
      payload.setErrore(messaggioErrore);
      payload.setSuccesso(successo);

      callbackService.schedulaInvioAsincrono(OperazioneFruitore.CALLBACK_INVIO_SEGNALE,
          pratica.getFruitore().getId(), payload, parametri, null);

    } else if (!successo) {
      logger.debug(method,
          "invio segnalazione all'assistenza perche' l'avanzamento del processo e' fallito ma il fruitore non ha nessun callback configurato",
          pratica.getId(), processInstanceId, successo);

      // invio fallito ma nessun callback
      this.mailService.inviaMailAssistenza("Invio di segnale da parte di fruitore esterno fallito",
          String.format(
              "L'invio del segnale %s da parte del fruitore esterno %s per la pratica con ID %d e' fallito.\n\n"
                  + "Codice invio: %s\n" + "Errore: %s\n" + "Dettagli: %s",
                  body.getCodiceSegnale(), pratica.getFruitore().getNomeApp(), pratica.getId(),
                  codiceInvio, messaggioErrore, ObjectUtils.represent(dettagliErrore)));
    }
  }

  @Override
  public CreaPraticaEsternaFruitoreResponse postPraticheEsterne(
      CreaPraticaEsternaFruitoreRequest body) {

    Timestamp adesso = new Timestamp(System.currentTimeMillis());

    // Validazione campi obbligatori
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo la reference al fruitore
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), null);

    // verifico che non collida la chiave composta idPratica - codiceIpaEnte
    if (this.findPraticaByChiaveEsterna(body.getIdPraticaExt(), body.getCodiceIpaEnte(),
        contesto.fruitore.getId()).isPresent()) {
      throw new ConflictException("Esiste gia' una pratica con ID " + body.getIdPraticaExt()
      + " e codice ente " + body.getCodiceIpaEnte());
    }

    // verifico il codice tipo pratica e ottengo la reference
    CosmoDTipoPratica tipo = cosmoDTipoPraticaRepository.findOneActive(body.getTipoPratica())
        .orElseThrow(() -> new BadRequestException(
            String.format("Tipo pratica %s non trovata", body.getTipoPratica())));

    // verifico che il tipo pratica sia creabile da servizio
    if (Boolean.FALSE.equals(tipo.getCreabileDaServizio())) {
      throw new BadRequestException(
          String.format("Pratica di tipo %s non creabile da servizio", body.getTipoPratica()));
    }

    // verifico che il tipo pratica sia associato all'ente
    if (tipo.getCosmoTEnte() != null
        && !contesto.ente.getId().equals(tipo.getCosmoTEnte().getId())) {
      throw new BadRequestException("Il tipo di pratica non e' disponibile per l'ente");
    }

    // verifico lo stato e ottengo la reference
    CosmoDStatoPratica stato = null;

    if (!StringUtils.isBlank(body.getStato())) {
      // verifico che lo stato pratica sia associato al tipo pratica
      stato = processService.getStatoPratica(body.getStato(), tipo.getCodice());
    }

    CosmoTPratica pratica = new CosmoTPratica();

    pratica.setEsterna(Boolean.TRUE);
    pratica.setLinkPraticaEsterna(body.getLinkPratica());
    pratica.setIdPraticaExt(body.getIdPraticaExt());
    pratica.setEnte(cosmoTPraticaRepository.reference(CosmoTEnte.class, contesto.ente.getId()));
    pratica.setFruitore(
        cosmoTPraticaRepository.reference(CosmoTFruitore.class, contesto.fruitore.getId()));
    pratica.setOggetto(body.getOggetto());
    pratica.setRiassunto(body.getRiassunto());
    pratica.setUtenteCreazionePratica(body.getUtenteCreazionePratica());
    pratica.setTipo(tipo);
    pratica.setStato(stato);
    pratica.setDataCreazionePratica(adesso);
    pratica.setAttivita(new ArrayList<>());
    pratica.setDataCambioStato(adesso);

    if (!StringUtils.isBlank(body.getRiassunto())) {
      // tenta indicizzazione del campo riassunto come best-attempt
      try {
        pratica.setRiassuntoTestuale(Jsoup.parse(body.getRiassunto()).text());
      } catch (Exception e) {
        logger.error("postPraticheEsterne",
            "errore nell'indicizzazione del campo riassunto a versione testuale", e);
      }
    }

    pratica = cosmoTPraticaRepository.save(pratica);

    // inserisco il log dell'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_CREATA)
        .withDescrizioneEvento(String.format("La pratica '%s' e' stata creata.", pratica.getOggetto()))
        .withPratica(pratica)
        .withFruitore(contesto.fruitore)
        .build());
    //@formatter:on

    // aggiorno le attivita
    aggiornaAttivitaEsterne(contesto, pratica, body.getAttivita());

    CreaPraticaEsternaFruitoreResponse output = new CreaPraticaEsternaFruitoreResponse();
    var esito = new CreaPraticaEsternaEsitoFruitoreResponse();
    esito.setCode("OK");
    esito.setStatus(201);
    esito.setIdPratica(pratica.getId().toString());
    output.setEsito(esito);

    return output;
  }

  @Override
  public AggiornaPraticaEsternaFruitoreResponse putPraticheEsterne(String idPraticaExt,
      AggiornaPraticaEsternaFruitoreRequest body) {

    Timestamp adesso = new Timestamp(System.currentTimeMillis());

    // Validazione campi obbligatori
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo la reference al fruitore
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), idPraticaExt);

    if (contesto.pratica == null || contesto.pratica.getDataFinePratica() != null) {
      throw new NotFoundException("Nessuna pratica trovata con ID " + idPraticaExt
          + " e codice ente " + body.getCodiceIpaEnte());
    }

    var pratica = contesto.pratica;

    if (!StringUtils.isBlank(body.getRiassunto())) {
      pratica.setRiassunto(body.getRiassunto());
      // tenta indicizzazione del campo riassunto come best-attempt
      try {
        pratica.setRiassuntoTestuale(Jsoup.parse(body.getRiassunto()).text());
      } catch (Exception e) {
        logger.error("putPraticheEsterne",
            "errore nell'indicizzazione del campo riassunto a versione testuale", e);
      }
    }

    if (!StringUtils.isBlank(body.getLinkPratica())) {
      pratica.setLinkPraticaEsterna(body.getLinkPratica());
    }

    if (body.getTipoPratica() != null) {
      // verifico il codice tipo pratica e ottengo la reference
      CosmoDTipoPratica tipo = cosmoDTipoPraticaRepository.findOneActive(body.getTipoPratica())
          .orElseThrow(() -> new BadRequestException(
              String.format("Tipo pratica %s non trovata", body.getTipoPratica())));

      // verifico che il tipo pratica sia creabile da servizio
      if (Boolean.FALSE.equals(tipo.getCreabileDaServizio())) {
        throw new BadRequestException(
            String.format("Pratica di tipo %s non creabile da servizio", body.getTipoPratica()));
      }

      // verifico che il tipo pratica sia associato all'ente
      if (tipo.getCosmoTEnte() != null
          && !contesto.ente.getId().equals(tipo.getCosmoTEnte().getId())) {
        throw new BadRequestException("Il tipo di pratica non e' disponibile per l'ente");
      }

      pratica.setTipo(tipo);
    }

    if (body.getStato() != null) {
      pratica
      .setStato(processService.getStatoPratica(body.getStato(), pratica.getTipo().getCodice()));
      pratica.setDataCambioStato(adesso);
    }

    // verifico coerenza stato e tipo visto che possono variare in modo indipendente
    if (pratica.getStato() != null && pratica.getTipo() != null) {

      pratica.setStato(processService.getStatoPratica(pratica.getStato().getCodice(),
          pratica.getTipo().getCodice()));
    }

    cosmoTPraticaRepository.saveAndFlush(pratica);

    if (body.getDataFinePratica() != null) {
      processService.terminaPratica(pratica, OffsetDateTime.now(), contesto.fruitore);

    } else {
      var attivitaMapped = body.getAttivita().stream().map(a -> {
        var out = new CreaPraticaEsternaAttivitaFruitoreRequest();
        try {
          PropertyUtils.copyProperties(out, a);
        } catch (Exception e) {
          throw ExceptionUtils.toChecked(e);
        }

        out.setAssegnazione(new ArrayList<>());
        if (a.getAssegnazione() != null) {
          for (var ass : a.getAssegnazione()) {
            var assMapped = new CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest();
            try {
              PropertyUtils.copyProperties(assMapped, ass);
            } catch (Exception e) {
              throw ExceptionUtils.toChecked(e);
            }
            out.getAssegnazione().add(assMapped);
          }
        }

        return out;
      }).collect(Collectors.toList());

      aggiornaAttivitaEsterne(contesto, pratica, attivitaMapped);
      cosmoTPraticaRepository.saveAndFlush(pratica);
    }

    AggiornaPraticaEsternaFruitoreResponse output = new AggiornaPraticaEsternaFruitoreResponse();
    var esito = new AggiornaPraticaEsternaEsitoFruitoreResponse();
    esito.setCode("OK");
    esito.setStatus(200);
    esito.setIdPratica(pratica.getId().toString());
    output.setEsito(esito);

    return output;
  }

  @Override
  public EliminaPraticaEsternaFruitoreResponse deletePraticheEsterne(String idPraticaExt,
      EliminaPraticaEsternaFruitoreRequest body) {

    // Validazione campi obbligatori
    ValidationUtils.require(body, "request body");
    ValidationUtils.validaAnnotations(body);

    // ottengo la reference al fruitore
    var contesto = getContestoChiamataCorrente(body.getCodiceIpaEnte(), idPraticaExt);

    if (contesto.pratica == null || contesto.pratica.getDataFinePratica() != null) {
      throw new NotFoundException("Nessuna pratica trovata con ID " + idPraticaExt
          + " e codice ente " + body.getCodiceIpaEnte());
    }

    var pratica = contesto.pratica;

    processService.annullaPratica(pratica, OffsetDateTime.now(), contesto.fruitore);

    EliminaPraticaEsternaFruitoreResponse output = new EliminaPraticaEsternaFruitoreResponse();
    var esito = new EliminaPraticaEsternaEsitoFruitoreResponse();
    esito.setCode("OK");
    esito.setStatus(200);
    esito.setIdPratica(pratica.getId().toString());
    output.setEsito(esito);

    return output;
  }

  private void aggiornaAttivitaEsterne(ContestoChiamataFruitore ctx, CosmoTPratica pratica,
      List<CreaPraticaEsternaAttivitaFruitoreRequest> attivitaInputList) {

    var difference = ComplexListComparator.compareLists(pratica.getAttivita(), attivitaInputList,
        (CosmoTAttivita attivitaLocale,
            CreaPraticaEsternaAttivitaFruitoreRequest attivitaInput) -> {
              return attivitaLocale.nonCancellato()
                  && !StringUtils.isBlank(attivitaLocale.getLinkAttivitaEsterna())
                  && !StringUtils.isBlank(attivitaInput.getLinkAttivita())
                  && Objects.equals(attivitaLocale.getLinkAttivitaEsterna(),
                      attivitaInput.getLinkAttivita())
                  && Objects.equals(attivitaLocale.getNome(), attivitaInput.getNome());
            });

    difference.onElementsInBoth((CosmoTAttivita attivitaLocale,
        CreaPraticaEsternaAttivitaFruitoreRequest attivitaInput) -> {

          attivitaLocale.setNome(attivitaInput.getNome());
          attivitaLocale.setDescrizione(attivitaInput.getDescrizione());

          aggiornaAssegnazioniAttivitaEsterna(ctx, pratica,
              attivitaLocale,
              attivitaInput.getAssegnazione());
        });

    difference.onElementsInFirstNotInSecond(attivitaLocale -> {
      if (attivitaLocale.cancellato()) {
        return;
      }

      processService.aggiornaAttivitaTerminata(attivitaLocale);

      // inserisco il log dell'operazione su db
      //@formatter:off
      storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
          .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_COMPLETATA)
          .withDescrizioneEvento(String.format(
              "L'attivita' \"%s\" e' stata completata.",
              attivitaLocale.getNome(),
              attivitaLocale.getCosmoTPratica().getOggetto()
              ))
          .withPratica(attivitaLocale.getCosmoTPratica())
          .withAttivita(attivitaLocale)
          .withFruitore(ctx.fruitore)
          .build());
      //@formatter:on
    });

    difference.onElementsInSecondNotInFirst(attivitaInput -> {

      creaAttivitaEsterna(ctx, pratica, attivitaInput);
    });
  }

  private void creaAttivitaEsterna(ContestoChiamataFruitore ctx, CosmoTPratica pratica,
      CreaPraticaEsternaAttivitaFruitoreRequest attivitaInput) {

    var method = "creaAttivitaEsterna";

    CosmoTAttivita attivitaDaSalvare = new CosmoTAttivita();

    attivitaDaSalvare.setCosmoTPratica(pratica);
    attivitaDaSalvare.setDescrizione(attivitaInput.getDescrizione());
    attivitaDaSalvare.setLinkAttivitaEsterna(attivitaInput.getLinkAttivita());
    attivitaDaSalvare.setNome(attivitaInput.getNome());

    var attivitaAssegnaziones = new ArrayList<CosmoRAttivitaAssegnazione>();
    if (null != attivitaInput.getAssegnazione()) {
      for (CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest attivitaAssegnazioneReq : attivitaInput
          .getAssegnazione()) {
        if (null != attivitaAssegnazioneReq.getUtente()
            && !attivitaAssegnazioneReq.getUtente().isBlank()) {
          var utente = cosmoTUtenteRepository.findByCodiceFiscale(attivitaAssegnazioneReq.getUtente());
          if (null != utente) {
            attivitaAssegnaziones.add(mappaAssegnazioneDaAssegnazioneEsterna(pratica, attivitaDaSalvare,
                attivitaAssegnazioneReq));
          } else {
            logger.warn(method,
                String.format("utente con cf %s non trovato, l'attivita' non verra' inserita",
                    attivitaAssegnazioneReq.getUtente()));
          }
        }
      }
    }
    attivitaDaSalvare
    .setCosmoRAttivitaAssegnaziones(attivitaAssegnaziones);
    // attivitaDaSalvare
    // .setCosmoRAttivitaAssegnaziones(attivitaInput.getAssegnazione() == null ? new ArrayList<>()
    // : attivitaInput.getAssegnazione().stream()
    // .map(a -> mappaAssegnazioneDaAssegnazioneEsterna(pratica, attivitaDaSalvare, a))
    // .collect(Collectors.toList()));

    processService.importaNuovaAttivita(pratica, attivitaDaSalvare, ctx.fruitore);
  }

  private CosmoRAttivitaAssegnazione mappaAssegnazioneDaAssegnazioneEsterna(CosmoTPratica pratica,
      CosmoTAttivita attivita,
      CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest assegnazioneInput) {

    if (!StringUtils.isBlank(assegnazioneInput.getUtente())) {
      if (!StringUtils.isBlank(assegnazioneInput.getGruppo())) {
        throw new BadRequestException(
            "una assegnazione deve contenere solo un utente o un gruppo, non tutti e due");
      }
      return processService.buildAssegnazioneDiretta(attivita, assegnazioneInput.getUtente());
    }
    if (!StringUtils.isBlank(assegnazioneInput.getGruppo())) {
      return processService.buildAssegnazioneGruppo(pratica.getEnte(), attivita,
          assegnazioneInput.getGruppo());
    }
    throw new BadRequestException("una assegnazione deve contenere almeno un utente o un gruppo");
  }

  private void aggiornaAssegnazioniAttivitaEsterna(ContestoChiamataFruitore ctx,
      CosmoTPratica pratica, CosmoTAttivita attivita,
      List<CreaPraticaEsternaAssegnazioneAttivitaFruitoreRequest> assegnazioniInput) {

    List<CosmoRAttivitaAssegnazione> assegnazioniInputMapped = assegnazioniInput == null
        ? new ArrayList<>()
            : assegnazioniInput.stream()
            .map(a -> mappaAssegnazioneDaAssegnazioneEsterna(pratica, attivita, a))
            .collect(Collectors.toList());

    processService.aggiornaAssegnazioni(attivita, assegnazioniInputMapped, pratica.getEnte(),
        ctx.fruitore);
  }

  private Map<String, Object> updateTaskCompletion(String idEvento) {
    var payload = new Task();
    payload.setAction(ACTION_COMPLETE);

    try {
      return cosmoCmmnFeignClient.postTaskId(idEvento, payload);

    } catch (HttpStatusCodeException e) {
      if (e.getRawStatusCode() == 409) {
        throw new ConflictException(ERR_CONFLICT, e);
      }
      throw new InternalServerException(
          "Errore nella chiamata interna per la marcatura del task come 'completato'", e);
    } catch (Exception e) {
      throw new InternalServerException(
          "Errore imprevisto nella marcatura del task come 'completato'", e);
    }
  }

  private RestVariable[] updateTaskExecution(String idEvento) {
    var variables = Arrays.asList(CommonUtils.variable(VARIABLE_ESEGUITO,
        OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME), VARIABLE_SCOPE_LOCAL,
        VARIABLE_TYPE_DATE));

    try {
      return cosmoCmmnFeignClient.postTaskVariables(idEvento, variables);

    } catch (HttpStatusCodeException e) {
      if (e.getRawStatusCode() == 409) {
        throw new ConflictException(ERR_CONFLICT, e);
      }
      throw new InternalServerException(
          "Errore nella chiamata interna per la marcatura del task come 'eseguito'", e);
    } catch (Exception e) {
      throw new InternalServerException(
          "Errore imprevisto nella marcatura del task come 'eseguito'", e);
    }
  }

  private void updateTaskById(String idEvento, Task updateTaskRequest) {
    try {
      cosmoCmmnFeignClient.putTask(idEvento, updateTaskRequest);

    } catch (HttpStatusCodeException e) {
      if (e.getRawStatusCode() == 409) {
        throw new ConflictException(ERR_CONFLICT, e);
      }
      throw new InternalServerException(
          "Errore nella chiamata interna per l'aggiornamento del task", e);
    } catch (Exception e) {
      throw new InternalServerException("Errore imprevisto nell'aggiornamento del task", e);
    }
  }

  private Optional<TaskResponse> getTaskById(String idEvento) {
    TaskResponse task;
    try {
      task = cosmoCmmnFeignClient.getTaskId(idEvento);
    } catch (HttpStatusCodeException e) {
      if (e.getRawStatusCode() == 404) {
        task = null;
      } else {
        throw new InternalServerException(
            "Errore nella chiamata interna per la ricerca del task specificato", e);
      }
    } catch (Exception e) {
      throw new InternalServerException("Errore imprevisto nella ricerca del task specificato", e);
    }

    return Optional.ofNullable(task);
  }

  private ContestoChiamataFruitore getContestoChiamataCorrente(String codiceIpaEnte,
      String idPraticaExt) {

    // ottengo il fruitore corrente
    var client = SecurityUtils.getClientCorrente();

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = cosmoTFruitoreRepository
        .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, client.getCodice()).orElseThrow(
            () -> new UnauthorizedException("Fruitore non autenticato o non riconosciuto"));

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    var enteAssociato = StringUtils.isBlank(codiceIpaEnte) ? null
        : verificaAssociazioneFruitoreEnte(fruitoreEntity, codiceIpaEnte);

    // verifico che esista la pratica corrispondente agli ID forniti
    CosmoTPratica pratica = StringUtils.isBlank(idPraticaExt) ? null
        : this
        .findPraticaByChiaveEsterna(idPraticaExt,
            enteAssociato != null ? enteAssociato.getCodiceIpa() : null, fruitoreEntity.getId())
        .orElseThrow(
            () -> new NotFoundException("Nessun pratica trovata avente ID " + idPraticaExt));

    var output = new ContestoChiamataFruitore();
    output.client = client;
    output.fruitore = fruitoreEntity;
    output.ente = enteAssociato;
    output.pratica = pratica;
    return output;
  }

  private CosmoTUtente verificaUtentePerEnte(String codiceFiscale, CosmoTEnte ente) {
    // verifica che l'utente esista
    var utente =
        cosmoTUtenteRepository.findOneNotDeletedByField(CosmoTUtente_.codiceFiscale, codiceFiscale);

    if (utente.isEmpty()) {
      throw new NotFoundException("Utente non trovato");
    }

    // verifica che l'utente sia associato all'ente
    verificaAssociazioneUtenteEnte(utente.get(), ente);

    return utente.get();
  }

  private void verificaAssociazioneUtenteEnte(CosmoTUtente utente, CosmoTEnte ente) {

    // verifica che l'utente sia associato all'ente
    if (utente.getCosmoRUtenteEntes().stream()
        .noneMatch(assoc -> assoc.valido() && assoc.getId().getIdEnte().equals(ente.getId()))) {
      throw new BadRequestException("Utente non associato all'ente specificato");
    }
  }

  private CosmoTEnte verificaAssociazioneFruitoreEnte(CosmoTFruitore fruitoreEntity,
      String codiceIpaEnte) {
    //@formatter:off
    return fruitoreEntity.getCosmoRFruitoreEntes().stream()
        .filter(CosmoREntity::valido)
        .map(CosmoRFruitoreEnte::getCosmoTEnte)
        .filter(ente -> !ente.cancellato())
        .filter(ente -> codiceIpaEnte.equalsIgnoreCase(ente.getCodiceIpa()))
        .findFirst()
        .orElseThrow(() -> new ForbiddenException("Il Codice IPA ente non coincide con nessuno degli enti associati al fruitore"));
    //@formatter:on
  }

  private Optional<CosmoTPratica> findPraticaByChiaveEsterna(String idPraticaExt,
      String codiceIpaEnte, Long idFruitore) {
    return this.cosmoTPraticaRepository.findOneNotDeleted(CosmoTPraticaSpecifications
        .findByChiaveFruitoreEsterno(idPraticaExt, codiceIpaEnte, idFruitore));
  }

  @Override
  public CosmoTEndpointFruitore getEndpoint(OperazioneFruitore operazione, Long idFruitore,
      String codiceDescrittivo) {
    ValidationUtils.assertNotNull(operazione, "operazione");
    ValidationUtils.assertNotNull(idFruitore, "idFruitore");

    String errorMessage = (codiceDescrittivo != null && !codiceDescrittivo.isBlank())
        ? ("Nessun endpoint per l'operazione con codice descrittivo" + codiceDescrittivo + " sul fruitore " + idFruitore)
            :  ("Nessun endpoint per l'operazione " + operazione.name() + " sul fruitore " + idFruitore);

    return fetchEndpoint(operazione, idFruitore, codiceDescrittivo)
        .orElseThrow(() -> new InternalServerException(errorMessage));
  }

  @Override
  public Optional<CosmoTEndpointFruitore> verificaEsistenzaEndpoint(OperazioneFruitore operazione,
      Long idFruitore, String codiceDescrittivo) {

    ValidationUtils.assertNotNull(operazione, "operazione");
    ValidationUtils.assertNotNull(idFruitore, "idFruitore");

    return fetchEndpoint(operazione, idFruitore, codiceDescrittivo);
  }

  private Optional<CosmoTEndpointFruitore> fetchEndpoint(OperazioneFruitore operazione,
      Long idFruitore, String codiceDescrittivo) {

    ValidationUtils.assertNotNull(operazione, "operazione");
    ValidationUtils.assertNotNull(idFruitore, "idFruitore");

    if (codiceDescrittivo != null && !codiceDescrittivo.isBlank()) {
      return cosmoTEndpointFruitoreRepository.findOneNotDeleted(
          (Root<CosmoTEndpointFruitore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            var joinFruitore = root.join(CosmoTEndpointFruitore_.fruitore, JoinType.LEFT);
            var joinOperazione = root.join(CosmoTEndpointFruitore_.operazione, JoinType.LEFT);
            return cb.and(cb.equal(joinFruitore.get(CosmoTFruitore_.id), idFruitore),
                cb.equal(joinOperazione.get(CosmoDOperazioneFruitore_.codice), operazione.name()),
                cb.isTrue(joinOperazione.get(CosmoDOperazioneFruitore_.personalizzabile)),
                cb.equal(root.get(CosmoTEndpointFruitore_.codiceDescrittivo), codiceDescrittivo));
          });
    } else {
      return cosmoTEndpointFruitoreRepository.findOneNotDeleted(
          (Root<CosmoTEndpointFruitore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            var joinFruitore = root.join(CosmoTEndpointFruitore_.fruitore, JoinType.LEFT);
            var joinOperazione = root.join(CosmoTEndpointFruitore_.operazione, JoinType.LEFT);
            return cb.and(cb.equal(joinFruitore.get(CosmoTFruitore_.id), idFruitore),
                cb.equal(joinOperazione.get(CosmoDOperazioneFruitore_.codice), operazione.name()));
          });
    }
  }

  public static class ContestoChiamataFruitore {
    ClientInfoDTO client;
    CosmoTFruitore fruitore;
    CosmoTEnte ente;
    CosmoTPratica pratica;
  }

}
