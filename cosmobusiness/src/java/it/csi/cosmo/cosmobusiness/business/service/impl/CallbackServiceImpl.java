/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.common.entities.CosmoDEsitoInvioCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoDStatoCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoTCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTInvioCallbackFruitore;
import it.csi.cosmo.common.entities.enums.EsitoInvioCallbackFruitore;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.StatoCallbackFruitore;
import it.csi.cosmo.common.entities.enums.TipoEndpointFruitore;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.FruitoriService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.ChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.EsitoTentativoInvioCallback;
import it.csi.cosmo.cosmobusiness.dto.rest.InvioCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.SchedulazioneCallbackResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.StatoCallback;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTCallbackFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEndpointFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTInvioCallbackFruitoreRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class CallbackServiceImpl implements CallbackService {

  /**
   *
   */
  private static final String FIELD_CALLBACK = "callback";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Autowired
  private CosmoTCallbackFruitoreRepository cosmoTCallbackFruitoreRepository;

  @Autowired
  private CosmoTInvioCallbackFruitoreRepository cosmoTInvioCallbackFruitoreRepository;

  @Autowired
  private ChiamataEsternaService chiamataEsternaService;

  @Autowired
  private FruitoriService fruitoriService;

  protected Integer getMaxRetries() {
    return this.configurazioneService
        .getConfig(ParametriApplicativo.BATCH_INVIO_CALLBACK_MAX_RETRIES).asInteger();
  }

  @Override
  public InvioCallbackResponse inviaSincrono(OperazioneFruitore operazione, Long idFruitore,
      Object payload, Map<String, Object> parametri, String codiceSegnale) {
    final var method = "inviaSincrono";

    CosmoTCallbackFruitore callback = costruisciIstanzaCallback(operazione, idFruitore, payload,
        parametri, StatoCallbackFruitore.IN_CORSO, codiceSegnale);

    try {
      inviaCallback(callback, 0);

    } catch (HttpStatusCodeException e) {
      logger.error(method, "invio diretto callback fallito (errore HTTP)", e);
      try {
        logger.warn(method, "risposta al callback diretto fallito: {}",
            e.getResponseBodyAsString());
      } catch (Exception e2) {
        // best attempt
        logger.warn(method,
            "errore (inoltre) nel salvataggio della risposta HTTP per il callback fallito", e2);
      }
      throw e;

    } catch (Exception e) {
      logger.error(method, "invio diretto callback fallito (errore sconosciuto)", e);
      throw e;
    }

    InvioCallbackResponse response = new InvioCallbackResponse();
    StatoCallback stato = new StatoCallback();
    stato.setCodice(EsitoInvioCallbackFruitore.INVIATO.name());
    response.setUuid(UUID.randomUUID().toString());
    response.setStato(stato);
    return response;
  }

  @Override
  public SchedulazioneCallbackResponse schedulaInvioAsincrono(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, String codiceSegnale) {

    CosmoTCallbackFruitore callback = costruisciIstanzaCallback(operazione, idFruitore, payload,
        parametri, StatoCallbackFruitore.SCHEDULATO, codiceSegnale);

    callback = cosmoTCallbackFruitoreRepository.save(callback);

    SchedulazioneCallbackResponse response = new SchedulazioneCallbackResponse();
    StatoCallback stato = new StatoCallback();
    stato.setCodice(callback.getStato().getCodice());
    response.setUuid("cosmo-cb-" + String.format("%06d", callback.getId()));
    response.setStato(stato);
    return response;
  }

  @Override
  public void annullaCallbackSchedulato(Long idCallback) {
    final var method = "annullaCallbackSchedulato";
    logger.info(method, "richiesto annullamento di callback schedulato {}", idCallback);

    CosmoTCallbackFruitore cb = cosmoTCallbackFruitoreRepository.findOne(idCallback);
    if (cb == null) {
      throw new NotFoundException("Callback specificato non trovato");
    }

    if (!(cb.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
        || cb.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())
        || cb.getStato().getCodice().equals(StatoCallbackFruitore.IN_CORSO.name()))) {

      throw new ConflictException("Callback specificato non annullabile al momento - in stato "
          + cb.getStato().getCodice());
    }

    cb.setStato(cosmoTEndpointFruitoreRepository.reference(CosmoDStatoCallbackFruitore.class,
        StatoCallbackFruitore.ANNULLATO.name()));

    cosmoTCallbackFruitoreRepository.save(cb);

    logger.info(method, "annullato il callback schedulato {}", idCallback);
  }

  @Override
  public EsitoTentativoInvioCallback tentaInvioCallbackSchedulato(Long idCallback) {
    final var method = "tentaInvioCallbackSchedulato";
    logger.info(method, "tento invio di callback schedulato {}", idCallback);

    CosmoTCallbackFruitore cb = cosmoTCallbackFruitoreRepository.findOne(idCallback);
    if (cb == null) {
      throw new NotFoundException("Callback specificato non trovato");
    }

    if (!(cb.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
        || cb.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())
        || cb.getStato().getCodice().equals(StatoCallbackFruitore.IN_CORSO.name()))) {

      throw new ConflictException(
          "Callback specificato non inviabile al momento - in stato " + cb.getStato().getCodice());
    }

    Long numTentativiFalliti = cb.getTentativiInvio().stream()
        .filter(t -> t.getEsito().getCodice().equals(EsitoInvioCallbackFruitore.FALLITO.name()))
        .collect(Collectors.counting());
    if (numTentativiFalliti > 0) {
      logger.debug(method, "il callback schedulato {} ha {} tentativi falliti precedenti",
          idCallback, numTentativiFalliti);
    }

    EsitoTentativoInvioCallback esitoInvio = tentaInvioCallback(cb, numTentativiFalliti.intValue());
    String codiceEsitoInvio = esitoInvio.getEsito().name();
    logger.info(method, "esito tentativo invio di callback schedulato {}: {}", idCallback,
        codiceEsitoInvio);

    String statoPrecedente = cb.getStato().getCodice();
    StatoCallbackFruitore nuovoStato;

    if (codiceEsitoInvio.equals(EsitoInvioCallbackFruitore.INVIATO.name())) {
      // inviato con successo
      nuovoStato = StatoCallbackFruitore.INVIATO;
    } else if (numTentativiFalliti < (getMaxRetries() - 1)) {
      // fallito ma rischedulabile
      nuovoStato = StatoCallbackFruitore.RISCHEDULATO;
    } else {
      // fallito e non rischedulabile
      nuovoStato = StatoCallbackFruitore.FALLITO;
    }

    logger.info(method, "cambio di stato callback schedulato {}: {} => {}", idCallback,
        statoPrecedente, nuovoStato.name());

    cb.setStato(cosmoTEndpointFruitoreRepository.reference(CosmoDStatoCallbackFruitore.class,
        nuovoStato.name()));
    cb.setRisposta(esitoInvio.getRisposta());

    transactionService.inTransactionOrThrow(() -> cosmoTCallbackFruitoreRepository.save(cb));

    return EsitoTentativoInvioCallback.builder().withEsito(esitoInvio.getEsito())
        .withErrore(esitoInvio.getErrore()).withStato(nuovoStato)
        .withRisposta(esitoInvio.getRisposta()).build();
  }

  protected CosmoTCallbackFruitore costruisciIstanzaCallback(OperazioneFruitore operazione,
      Long idFruitore, Object payload, Map<String, Object> parametri, StatoCallbackFruitore stato,
      String codiceSegnale) {

    if (StringUtils.isBlank(codiceSegnale)) {
      codiceSegnale = operazione.name();
    }

    CosmoTCallbackFruitore callback = new CosmoTCallbackFruitore();

    callback.setDtInserimento(Timestamp.from(Instant.now()));
    callback.setEndpoint(fruitoriService.getEndpoint(operazione, idFruitore, null));
    if (payload != null) {
      callback.setPayload(ObjectUtils.getDataMapper().convertValue(payload, JsonNode.class));
    }
    callback.setStato(cosmoTEndpointFruitoreRepository.reference(CosmoDStatoCallbackFruitore.class,
        stato.name()));
    callback.setTentativiInvio(new ArrayList<>());
    callback.setParametri(parametri);
    callback.setCodiceSegnale(codiceSegnale);

    return callback;
  }

  /**
   * tenta l'invio del callback e registra l'esito del tentativo in una nuova entity. la nuova
   * entity viene committata anche se la chiamata fallisce.
   *
   * @param callback
   * @param retryNumber
   * @return
   */
  protected EsitoTentativoInvioCallback tentaInvioCallback(CosmoTCallbackFruitore callback,
      Integer retryNumber) {
    final var method = "tentaInvioCallback";

    ValidationUtils.assertNotNull(callback, FIELD_CALLBACK);
    ValidationUtils.assertNotNull(callback.getId(), "callback.id");

    CosmoTInvioCallbackFruitore entity = new CosmoTInvioCallbackFruitore();
    entity.setCallback(callback);

    logger.info(method, "tento invio callback {}", callback.getId());

    EsitoInvioCallbackFruitore esito;
    Throwable errore = null;

    JsonNode responseBody = null;

    try {
      ResponseEntity<JsonNode> response = inviaCallback(callback, retryNumber);

      esito = EsitoInvioCallbackFruitore.INVIATO;
      responseBody = response.getBody();
      logger.info(method, "callback {} inviato con successo", callback.getId());


    } catch (HttpStatusCodeException e) {
      logger.error(method, "callback fallito (errore HTTP)", e);
      errore = e;
      esito = EsitoInvioCallbackFruitore.FALLITO;
      entity.setDettagliEsito(chiamataEsternaService.getDescrizioneErroreChiamataEsterna(e));

      try {
        responseBody = ObjectUtils.fromJson(e.getResponseBodyAsString(), JsonNode.class);
      } catch (Exception e2) {
        // best attempt
        logger.warn(method,
            "errore (inoltre) nel salvataggio della risposta HTTP per il callback fallito", e2);
      }

    } catch (Exception e) {
      logger.error(method, "callback fallito (errore sconosciuto)", e);
      errore = e;
      esito = EsitoInvioCallbackFruitore.FALLITO;
      entity.setDettagliEsito(chiamataEsternaService.getDescrizioneErroreChiamataEsterna(e));
    }

    entity.setRisposta(responseBody);
    entity.setEsito(cosmoTEndpointFruitoreRepository
        .reference(CosmoDEsitoInvioCallbackFruitore.class, esito.name()));

    // persisto l'esito in una nuova transazione (lo voglio committato anche se il chiamante
    // lancera' eccezione)
    transactionService
    .inTransactionOrThrow(() -> cosmoTInvioCallbackFruitoreRepository.save(entity));

    return EsitoTentativoInvioCallback.builder().withRisposta(responseBody).withEsito(esito)
        .withErrore(errore).build();
  }

  /**
   * tenta l'invio del callback smistando a seconda del tipo di endpoint REST/SOAP
   *
   * @param callback
   * @param retryNumber
   */
  protected ResponseEntity<JsonNode> inviaCallback(CosmoTCallbackFruitore callback,
      Integer retryNumber) {
    final var method = "inviaCallback";

    ValidationUtils.assertNotNull(callback, FIELD_CALLBACK);

    // ottengo e valido l'endpoint associato
    CosmoTEndpointFruitore endpoint = callback.getEndpoint();

    ValidationUtils.assertNotNull(endpoint, "callback.endpoint");
    ValidationUtils.assertNotNull(endpoint.getFruitore(), "callback.endpoint.fruitore");
    logger.debug(method, "invio callback {} al fruitore {}", callback.getId(),
        callback.getEndpoint().getFruitore().getNomeApp());

    // per ora supporto solo REST
    ValidationUtils.assertNotNull(endpoint.getCodiceTipoEndpoint(),
        "callback.endpoint.codiceTipoEndpoint");

    if (!TipoEndpointFruitore.REST.name().equals(endpoint.getCodiceTipoEndpoint())) {
      throw new InternalServerException(
          "Endpoint di tipo " + endpoint.getCodiceTipoEndpoint() + " non supportato al momento.");
    }

    return inviaCallbackREST(callback, retryNumber);
  }

  /**
   * tenta l'invio del callback ad un endpoint REST utilizzando un RestTemplate
   *
   * @param callback
   * @param retryNumber
   */
  protected ResponseEntity<JsonNode> inviaCallbackREST(CosmoTCallbackFruitore callback,
      Integer retryNumber) {
    final var method = "inviaCallbackREST";

    ValidationUtils.assertNotNull(callback, FIELD_CALLBACK);

    // ottengo e valido l'endpoint associato
    CosmoTEndpointFruitore endpoint = callback.getEndpoint();

    ValidationUtils.assertNotNull(endpoint, "callback.endpoint");
    ValidationUtils.assertNotNull(endpoint.getFruitore(), "callback.endpoint.fruitore");

    logger.debug(method, "invio callback REST {} al fruitore {}", callback.getId(),
        endpoint.getFruitore().getNomeApp());

    Map<String, Object> headers = new HashMap<>();

    if (callback.getId() != null) {
      headers.put(Constants.HEADERS_PREFIX + "Callback-Id", callback.getId().toString());
    }
    if (callback.getDtInserimento() != null) {
      headers.put(Constants.HEADERS_PREFIX + "Event-Timestamp", callback.getDtInserimento()
          .toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
    if (retryNumber > 0) {
      headers.put(Constants.HEADERS_PREFIX + "Retry-Number", String.valueOf(retryNumber));
    }

    //@formatter:off
    var requestChiamataEsterna = ChiamataEsternaRequest.<JsonNode>builder(JsonNode.class)
        .withEndpoint(endpoint)
        .withPathParams(callback.getParametri())
        .withHeaders(headers)
        .withPayload(callback.getPayload())
        .build();
    //@formatter:on

    // invio la request
    ResponseEntity<JsonNode> result =
        chiamataEsternaService.inviaChiamataEsterna(requestChiamataEsterna);

    logger.debug(method, "inviato callback REST con risposta {} {}", result.getStatusCodeValue(),
        result.getStatusCode().getReasonPhrase());

    return result;
  }
}