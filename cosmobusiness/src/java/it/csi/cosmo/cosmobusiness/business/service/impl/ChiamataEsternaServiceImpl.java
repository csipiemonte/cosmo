/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.TipoSchemaAutenticazione;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.handler.CosmoJWTHelper;
import it.csi.cosmo.common.security.handler.CosmoJWTTokenDecoder;
import it.csi.cosmo.common.security.model.StandardAuthenticationToken;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.RestTemplateUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.config.Constants;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.ChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEndpointFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTSchemaAutenticazioneFruitoreRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerConstants;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class ChiamataEsternaServiceImpl implements ChiamataEsternaService {

  /**
   *
   */
  private static final String CODICE_CONTENT_TYPE_FORM_ENCODED = "FORM";

  private static final String PARAM_REQUEST = "request";

  private static final String FIELD_ID_PRATICA = "idPratica";

  private static final String HEADER_AUTHORIZATION = "Authorization";

  private static final String HEADER_AUTHORIZATION_FORMAT = "Bearer ${token}";

  private static final String DEFAULT_TOKEN_ENDPOINT_RELATIVE = "/token";

  private static final long MINIMUM_CACHED_TOKEN_TTL_FOR_REUSE = 120;

  private static final String DEFAULT_MAPPATURA_RICHIESTA_TOKEN =
      "{\"grant_type\": \"client_credentials\", \"client_id\": .credenziali.clientId, \"client_secret\": .credenziali.clientSecret}";

  private static final String DEFAULT_MAPPATURA_OUTPUT_TOKEN =
      "{\"token\": .body.access_token, \"expiresIn\": .body.expires_in}";

  private static final String MAPPATURA_RICHIESTA_TOKEN_APIMGR =
      "{\"grant_type\": \"client_credentials\", \"client_id\": .credenziali.clientId, \"client_secret\": .credenziali.clientSecret}";

  private static final String MAPPATURA_OUTPUT_TOKEN_APIMGR =
      "{\"token\": .body.access_token, \"expiresIn\": .body.expires_in}";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private MotoreJsonDinamicoService motoreJsonDinamicoService;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Autowired
  private CosmoTSchemaAutenticazioneFruitoreRepository cosmoTSchemaAutenticazioneFruitoreRepository;

  private Map<String, CachedTokenEntry> tokenCache = new ConcurrentHashMap<>();

  @Override
  public RiferimentoOperazioneAsincrona inviaChiamataEsternaDaProcesso(Long idPratica,
      InviaChiamataEsternaRequest body) {

    ValidationUtils.require(idPratica, FIELD_ID_PRATICA);
    ValidationUtils.require(body, "body");

    // cerca la pratica
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    // ottieni il fruitore
    var fruitore = pratica.getFruitore();

    if (!StringUtils.isBlank(body.getCodiceFruitore())) {
      fruitore = cosmoTFruitoreRepository
          .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, body.getCodiceFruitore())
          .orElseThrow(() -> new NotFoundException(
              "Fruitore con codice " + body.getCodiceFruitore() + " non trovato"));
    }

    CosmoTEndpointFruitore endpoint = null;

    if (!StringUtils.isBlank(body.getCodiceEndpoint())) {
      // un fruitore associato e' richiesto solo nel caso sia specificato l'endpoint via codice
      if (fruitore == null) {
        throw new InternalServerException("Nessun fruitore associato alla pratica " + idPratica);
      }

      // ricerco l'endpoint se specificato. se non lo trovo do errore
      //@formatter:off
      final var fruitoreFinal = fruitore;
      endpoint = fruitore.getEndpoints().stream()
          .filter(CosmoTEntity::nonCancellato)
          .filter(e ->
          (
              e.getOperazione() != null &&
              e.getOperazione().valido() &&
              e.getOperazione().getCodice().equals(body.getCodiceEndpoint().trim())
              )
          ||
          (
              e.getOperazione() != null &&
              e.getOperazione().getCodice().equals(OperazioneFruitore.CUSTOM.name()) &&
              !StringUtils.isBlank(e.getCodiceDescrittivo()) &&
              e.getCodiceDescrittivo().equals(body.getCodiceEndpoint())
              )
              )
          .findFirst().orElseThrow(() -> new InternalServerException("Nessun endpoint di tipo "
              + body.getCodiceEndpoint() + " attivo per il fruitore "
              + fruitoreFinal.getNomeApp()));
      //@formatter:on
    }


    // costruisco la sandbox per la valutazione delle espressioni
    var sandboxContext = SandboxFactory.buildStatoPraticaSandbox(pratica, statoPraticaService);

    // compilo le varie espressioni
    Map<String, Object> effectivePathParams = new HashMap<>();
    Map<String, Object> effectiveQueryParams = new HashMap<>();
    Map<String, Object> effectiveHeaders = new HashMap<>();
    Object payload = null;

    eseguiMappatura(body.getMappaturaQuery(), sandboxContext).forEach(effectiveQueryParams::put);
    eseguiMappatura(body.getMappaturaUrl(), sandboxContext).forEach(effectivePathParams::put);
    eseguiMappatura(body.getMappaturaHeaders(), sandboxContext).forEach(effectiveHeaders::put);

    if (!StringUtils.isBlank(body.getMappaturaRequestBody())) {
      payload =
          motoreJsonDinamicoService.eseguiMappatura(body.getMappaturaRequestBody(), sandboxContext);
    }

    boolean isJson =
        body.isRestituisceJson() == null || Boolean.TRUE.equals(body.isRestituisceJson());
    //@formatter:off
    var request = isJson ?
        ChiamataEsternaRequest.<JsonNode>builder(JsonNode.class)
        .withEndpoint(endpoint)
        .withMethod(body.getMetodo())
        .withUrl(body.getUrl())
        .withHeaders(effectiveHeaders)
        .withPathParams(effectivePathParams)
        .withQueryParams(effectiveQueryParams)
        .withPayload(payload)
        .build() :
          ChiamataEsternaRequest.<String>builder(String.class)
          .withEndpoint(endpoint)
          .withMethod(body.getMetodo())
          .withUrl(body.getUrl())
          .withHeaders(effectiveHeaders)
          .withPathParams(effectivePathParams)
          .withQueryParams(effectiveQueryParams)
          .withPayload(payload)
          .build();
    //@formatter:on

    var operazioneAsincrona = asyncTaskService.start("Invio chiamata esterna", task -> {
      Object[] outputHolder = new Object[] {null};

      task.step("invio della chiamata", step -> {
        // esegui la request
        var risultatoChiamata = inviaChiamataEsterna(request);

        // salva il risultato
        outputHolder[0] = risultatoChiamata;
      });

      // mappa l'output
      Map<String, Object> mappedOutput = new HashMap<>();

      @SuppressWarnings("unchecked")
      var risultatoChiamata = isJson ? (ResponseEntity<JsonNode>) outputHolder[0]
          : (ResponseEntity<String>) outputHolder[0];

      mappedOutput.put("rawOutput",
          isJson ? risultatoChiamata.getBody().toString()
              : risultatoChiamata.getBody());

      if (!StringUtils.isBlank(body.getMappaturaOutput())) {

        task.step("mappatura dell'output", step -> {
          // mappa l'output della request
          var risultatoMappaturaOutput = motoreJsonDinamicoService.eseguiMappatura(
              body.getMappaturaOutput(), SandboxFactory.fromHttpResponse(risultatoChiamata));

          // salva il risultato
          mappedOutput.put("mappedOutput", risultatoMappaturaOutput);
        });
      }

      return ObjectUtils.toJson(mappedOutput);
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(operazioneAsincrona.getTaskId());
    output.setStato(LongTaskStatus.STARTED.name());
    return output;
  }

  private Map<String, Object> eseguiMappatura(String specifica, Object input) {
    Map<String, Object> output = new HashMap<>();
    if (StringUtils.isBlank(specifica)) {
      return output;
    }

    var mapped = motoreJsonDinamicoService.eseguiMappatura(specifica, input);
    if (mapped == null) {
      return output;
    }

    if (mapped.getNodeType() != JsonNodeType.OBJECT || !(mapped instanceof ObjectNode)) {
      throw new InternalServerException("La specifica di mappatura deve costruire un oggetto");
    }
    var objectNode = (ObjectNode) mapped;

    objectNode.fields().forEachRemaining(field -> {
      output.put(field.getKey(), field.getValue().asText());
    });

    return output;
  }

  @Override
  public String getDescrizioneErroreChiamataEsterna(Exception e) {

    StringBuilder errorDetails = new StringBuilder(e.getMessage());

    if (e instanceof FeignClientClientErrorException) {
      errorDetails.append("\r\n").append("Response body: ")
      .append(ObjectUtils.toJson(((FeignClientClientErrorException) e).getResponse()));
    } else if (e instanceof FeignClientServerErrorException) {
      errorDetails.append("\r\n").append("Response body: ")
      .append(ObjectUtils.toJson(((FeignClientServerErrorException) e).getResponse()));
    } else if (e instanceof HttpStatusCodeException) {
      errorDetails.append("\r\n").append("Response body: ")
      .append(((HttpStatusCodeException) e).getResponseBodyAsString());
    }
    errorDetails.append("\r\n").append(ExceptionUtils.exceptionToString(e)).toString();

    return errorDetails.toString();
  }

  /**
   * applica gli header di autenticazione a seconda dello schema di autenticazione a cui afferisce
   * l'endpoint
   *
   * @param endpoint
   * @param headers
   *
   */
  private void applicaAutenticazione(CosmoTSchemaAutenticazioneFruitore schema,
      HttpHeaders headers) {

    ValidationUtils.assertNotNull(schema, "schemaAutenticazione");
    ValidationUtils.assertNotNull(schema.getTipo(), "schemaAutenticazione.tipo");
    ValidationUtils.assertNotNull(schema.getTipo().getCodice(), "schemaAutenticazione.tipo.codice");

    TipoSchemaAutenticazione tipoSchema =
        TipoSchemaAutenticazione.valueOf(schema.getTipo().getCodice());

    switch (tipoSchema) {
      case NONE:
        return;
      case BASIC:
        applicaAutenticazioneBasic(schema, headers);
        return;
      case API_MANAGER:
      case TOKEN:
        applicaAutenticazioneToken(schema, headers);
        return;
      default:
        throw new InternalServerException(
            "Schema di autenticazione " + tipoSchema.name() + " non supportato");
    }
  }

  /**
   * applica gli header per la basic authentication
   *
   * @param endpoint
   * @param headers
   */
  private void applicaAutenticazioneBasic(CosmoTSchemaAutenticazioneFruitore schema,
      HttpHeaders headers) {

    ValidationUtils.assertNotNull(schema.getCredenziali(), "schemaAutenticazione.credenziali");

    List<CosmoTCredenzialiAutenticazioneFruitore> basicSchemas = schema.getCredenziali().stream()
        .filter(CosmoTEntity::nonCancellato).collect(Collectors.toList());

    if (basicSchemas.isEmpty()) {
      throw new InternalServerException(
          "Nessuna credenziale valida per lo schema di autenticazione " + schema.getId());
    } else if (basicSchemas.size() > 1) {
      throw new InternalServerException(
          "Credenziali valide non univoche per lo schema di autenticazione " + schema.getId());
    }

    CosmoTCredenzialiAutenticazioneFruitore basicSchema = basicSchemas.get(0);

    if (StringUtils.isBlank(basicSchema.getUsername())) {
      throw new InternalServerException("Credenziali non valide per lo schema di autenticazione "
          + schema.getId() + ": username non fornito");
    }
    if (StringUtils.isBlank(basicSchema.getPassword())) {
      throw new InternalServerException("Credenziali non valide per lo schema di autenticazione "
          + schema.getId() + ": password non fornita");
    }

    String headerValue =
        new StringBuilder("Basic ")
        .append(Base64.getEncoder().encodeToString(
            (basicSchema.getUsername() + ":" + basicSchema.getPassword()).getBytes()))
        .toString();

    headers.add(HEADER_AUTHORIZATION, headerValue);
  }

  /**
   * istanzia un resttemplate ad-hoc per il tentativo di callback
   *
   * @return un istanza di RestTemplate configurata correttamente
   */
  private RestTemplate costruisciRestTemplate() {

    //@formatter:off
    return RestTemplateUtils.builder()
        .withAllowConnectionReuse(false)
        .withConnectionRequestTimeout(10000)
        .withConnectTimeout(10000)
        .withReadTimeout(60000)
        .build();
    //@formatter:on
  }

  @Override
  public <T> ResponseEntity<T> inviaChiamataEsterna(ChiamataEsternaRequest<T> request) {
    final var method = "inviaChiamataEsterna";
    validaRequest(request);

    // reload endpoint - transaction is different
    var endpoint = request.getEndpoint() == null ? null
        : cosmoTEndpointFruitoreRepository.findOne(request.getEndpoint().getId());
    request.setEndpoint(endpoint);

    if (logger.isDebugEnabled()) {
      logger.debug(method, "invio chiamata esterna con parametri: {}",
          ObjectUtils.represent(request));
    }

    // ottieni URL completo
    URI uri = costruisciRestURI(request);
    HttpMethod requestMethod = getMethod(request);
    HttpHeaders headers = costruisciHeaders(request);

    // applico autenticazione
    applicaAutenticazione(request, headers);

    HttpEntity<Object> requestEntity = new HttpEntity<>(request.getPayload(), headers);

    // costruisco rest template per l'endpoint
    RestTemplate rt = costruisciRestTemplate();

    // invio la request
    ResponseEntity<T> result =
        logAndSend(rt, uri, requestMethod, requestEntity, request.getReturnType());

    logger.debug(method, "inviato callback REST alla URI {} con risposta {} {}", uri,
        result.getStatusCodeValue(), result.getStatusCode().getReasonPhrase());

    return result;
  }

  private void applicaAutenticazione(ChiamataEsternaRequest<?> request, HttpHeaders headers) {
    final var method = "applicaAutenticazione";
    if (request == null || request.getEndpoint() == null
        || request.getEndpoint().getSchemaAutenticazione() == null) {
      logger.debug(method, "autenticazione non necessaria per questa chiamata");
      return;
    }

    var schema = request.getEndpoint().getSchemaAutenticazione();

    logger.debug(method, "applico schema di autenticazione {} associato all'endpoint {}",
        schema.getId(), request.getEndpoint().getId());

    this.applicaAutenticazione(schema, headers);
  }

  private void validaRequest(ChiamataEsternaRequest<?> request) {
    ValidationUtils.assertNotNull(request, PARAM_REQUEST);
    boolean hasEndpoint = request.getEndpoint() != null;
    boolean hasUrl = !StringUtils.isBlank(request.getUrl());
    boolean hasMethod = !StringUtils.isBlank(request.getMethod());

    if (!hasEndpoint && (!hasUrl || !hasMethod)) {
      throw new BadRequestException("Un endpoint oppure una coppia URL/metodo sono richiesti");
    }
  }

  private URI costruisciRestURI(ChiamataEsternaRequest<?> request) {
    ValidationUtils.assertNotNull(request, PARAM_REQUEST);

    CosmoTFruitore fruitore =
        request.getEndpoint() != null ? request.getEndpoint().getFruitore() : null;

    String url = !StringUtils.isBlank(request.getUrl()) ? request.getUrl()
        : request.getEndpoint().getEndpoint();

    ValidationUtils.assertNotNull(url, "url");

    return costruisciRestURI(fruitore, url, request.getPathParams(), request.getQueryParams());
  }

  private URI costruisciRestURI(CosmoTFruitore fruitore, String endpoint,
      Map<String, Object> pathParams, Map<String, Object> queryParams) {

    ValidationUtils.assertNotNull(endpoint, "endpoint");

    boolean fruitoreURLIsBlank = fruitore == null || StringUtils.isBlank(fruitore.getUrl());
    boolean endpointURLIsAbsolute = endpoint.strip().startsWith("http");

    UriBuilder builder;
    if (endpointURLIsAbsolute) {
      builder = UriBuilder.fromUri(endpoint.strip());
    } else if (fruitore == null) {
      throw new InternalServerException("URL relativa non valida: " + endpoint);
    } else if (fruitoreURLIsBlank) {
      throw new InternalServerException("Nessuna URL di base configurata per il fruitore "
          + fruitore.getId() + " con cui risolvere l'URL relativo: " + endpoint);
    } else {
      builder = UriBuilder.fromUri(fruitore.getUrl().strip()).path(endpoint.strip());
    }

    if (queryParams != null && !queryParams.isEmpty()) {
      for (var qp : queryParams.entrySet()) {
        builder = builder.queryParam(qp.getKey(), qp.getValue());
      }
    }

    if (pathParams != null && !pathParams.isEmpty()) {
      // replace with ext parameters
      // Map<String, Object> extParameters = new HashMap<>();
      // pathParams.forEach(extParameters::put);
      // if (extParameters.containsKey(FIELD_ID_PRATICA_EXT)) {
      // extParameters.put(FIELD_ID_PRATICA, extParameters.get(FIELD_ID_PRATICA_EXT));
      // }

      return builder.buildFromMap(pathParams);
    }

    return builder.build();
  }

  private HttpMethod getMethod(ChiamataEsternaRequest<?> request) {
    ValidationUtils.assertNotNull(request, PARAM_REQUEST);

    String raw = !StringUtils.isBlank(request.getMethod()) ? request.getMethod()
        : request.getEndpoint().getMetodoHttp();

    ValidationUtils.assertNotNull(raw, "method");

    try {
      return HttpMethod.valueOf(raw.toUpperCase());
    } catch (Exception e) {
      throw new NoSuchElementException("Metodo HTTP non valido: " + raw);
    }
  }

  private HttpHeaders costruisciHeaders(ChiamataEsternaRequest<?> request) {
    ValidationUtils.assertNotNull(request, PARAM_REQUEST);
    HttpHeaders headers = new HttpHeaders();
    Set<String> added = new HashSet<>();

    if (request.getHeaders() != null && !request.getHeaders().isEmpty()) {
      for (var entry : request.getHeaders().entrySet()) {
        if (entry.getValue() != null) {
          added.add(entry.getKey().toUpperCase());
          headers.add(entry.getKey(), entry.getValue().toString());
        }
      }
    }

    if (!added.contains("User-Agent".toUpperCase())) {
      headers.add("User-Agent",
          Constants.PRODUCT + "/" + Constants.COMPONENT_DESCRIPTION + " M2M Client");
    }

    if (!added.contains("Content-Type".toUpperCase()) && request.getPayload() != null) {
      MediaType effectiveContentType = request.getContentType();
      if (effectiveContentType == null) {
        effectiveContentType = MediaType.APPLICATION_JSON;
      }

      headers.setContentType(effectiveContentType);
    }

    if (!added.contains("Accept".toUpperCase()) && request.getPayload() != null) {
      headers.add("Accept", "application/json");
    }

    return headers;
  }

  private void logBefore(URI uri, HttpMethod requestMethod, HttpEntity<?> requestEntity) {

    final var method = "inviaChiamataEsterna";
    final var prefix = "[before-request] ";

    logger.info(method, prefix + "request uri = {}", uri);
    logger.info(method, prefix + "request method = {}", requestMethod);
    if (requestEntity != null) {
      for (var header : requestEntity.getHeaders().entrySet()) {
        logger.info(method, prefix + "request header {} = {}", header.getKey(), header.getValue());
      }
      if (requestEntity.getBody() != null) {
        logger.info(method, prefix + "request request body = {}",
            ObjectUtils.represent(requestEntity.getBody()));
      }
    }
  }

  private void logSuccess(ResponseEntity<?> result) {
    final var method = "inviaChiamataEsterna";
    final var prefix = "[request-success] ";

    logger.info(method, prefix + "response status = {}", result.getStatusCode());
    logger.info(method, prefix + "response entity = {}", result.getBody());
    for (var header : result.getHeaders().entrySet()) {
      logger.info(method, prefix + "response header {} = {}", header.getKey(), header.getValue());
    }
  }

  private void logError(HttpStatusCodeException e) {
    final var method = "inviaChiamataEsterna";
    final var prefix = "[request-failed] ";

    logger.info(method, prefix + "response status = {}", e.getStatusCode());
    logger.info(method, prefix + "response entity = {}", e.getResponseBodyAsString());
    for (var header : e.getResponseHeaders().entrySet()) {
      logger.info(method, prefix + "response header {} = {}", header.getKey(), header.getValue());
    }
  }

  private <T> ResponseEntity<T> logAndSend(RestTemplate rt, URI uri, HttpMethod requestMethod,
      HttpEntity<?> requestEntity, ParameterizedTypeReference<T> returnType) {
    final var method = "sendCallback";
    var doLog = logger.isDebugEnabled();

    logger.debug(method, "tento invio chiamata esterna via {} alla URI {}", requestMethod.name(),
        uri);

    if (doLog) {
      logBefore(uri, requestMethod, requestEntity);
    }

    try {
      ResponseEntity<T> result = rt.exchange(uri, requestMethod, requestEntity, returnType);
      logger.debug(method, "risultato invio chiamata esterna: {}", result.getStatusCode());

      if (doLog) {
        logSuccess(result);
      }

      return result;

    } catch (HttpStatusCodeException e) {
      logger.error(method, "errore HTTP " + e.getStatusCode() + " nella chiamata esterna", e);

      if (doLog) {
        logError(e);
      }

      throw e;

    } catch (Exception e) {
      logger.error(method, "errore generico nella chiamata esterna", e);
      throw e;
    }
  }

  /**
   * applica gli header per l'autenticazione via token
   *
   * @param endpoint
   * @param headers
   */
  private void applicaAutenticazioneToken(CosmoTSchemaAutenticazioneFruitore schema,
      HttpHeaders headers) {

    ValidationUtils.assertNotNull(schema, "schemaAutenticazione");
    ValidationUtils.assertNotNull(schema.getId(), "schemaAutenticazione.id");
    ValidationUtils.assertNotNull(schema.getCredenziali(), "schemaAutenticazione.credenziali");

    final var methodName = "applicaAutenticazioneToken";

    logger.debug(methodName, "applico autenticazione token da schema {}", schema.getId());

    String token = getTokenValido(schema, false);

    logger.debug(methodName, "ottenuto token valido per autenticazione da schema {}",
        schema.getId());

    String nomeHeader = HEADER_AUTHORIZATION;
    if (!StringUtils.isBlank(schema.getNomeHeader())) {
      nomeHeader = schema.getNomeHeader().strip();
    }

    String formatoHeader = HEADER_AUTHORIZATION_FORMAT;
    if (!StringUtils.isBlank(schema.getFormatoHeader())) {
      formatoHeader = schema.getFormatoHeader().strip();
    }

    Map<String, String> valuesMap = new HashMap<>();
    valuesMap.put("token", token);


    @SuppressWarnings("deprecation")
    StrSubstitutor sub = new StrSubstitutor(valuesMap);
    @SuppressWarnings("deprecation")
    String headerValue = sub.replace(formatoHeader);

    headers.add(nomeHeader, headerValue);
  }

  private synchronized String getTokenValido(CosmoTSchemaAutenticazioneFruitore schema,
      boolean forceNew) {
    final var method = "getTokenValido";

    String cacheKey = schema.getId().toString();

    if (!forceNew) {
      var cached = this.tokenCache.getOrDefault(cacheKey, null);

      logger.debug(method, "verifico presenza token valido per autenticazione con chiave {}",
          cacheKey);

      if (cached != null && cached.ttl() >= MINIMUM_CACHED_TOKEN_TTL_FOR_REUSE) {
        logger.debug(method, "trovato token valido per autenticazione con chiave {} in cache",
            cacheKey);
        return cached.token;
      }
    }

    logger.debug(method,
        "nessun token valido per autenticazione con chiave {} in cache, richiedo nuovo token",
        cacheKey);
    var nuovoToken = ottieniToken(schema);

    logger.debug(method, "ottenuto nuovo token valido per autenticazione con chiave {}", cacheKey);

    if (!forceNew) {
      boolean canCache = false;
      Long effectiveTTL = nuovoToken.ttl();

      if (effectiveTTL == null) {
        logger.warn(method, "nessuna scadenza del token nella response o nel contenuto del token.");

      } else if (effectiveTTL < 60) {
        logger.warn(method,
            "scadenza del token indicata gia' passata o troppo breve (TTL {}). "
                + "Probabilmente uno scarto nelle ore o timezone non correttamente gestito.",
                effectiveTTL);

      } else {
        logger.debug(method, "il token scade: {} (fra {} secondi)", nuovoToken.expiresAt.toString(),
            effectiveTTL);
        canCache = true;
      }

      if (!canCache) {
        logger.warn(method, "il token non puo' essere inserito in cache per il riutilizzo.");
      } else {
        logger.warn(method, "inserisco il nuovo token con chiave {} in cache.", cacheKey);
        this.tokenCache.put(cacheKey, nuovoToken);
      }
    }

    return nuovoToken.token;
  }

  private CachedTokenEntry ottieniToken(CosmoTSchemaAutenticazioneFruitore schema) {
    final var methodName = "ottieniToken";

    ValidationUtils.assertNotNull(schema, "schemaAutenticazione");
    ValidationUtils.assertNotNull(schema.getId(), "schemaAutenticazione.id");
    ValidationUtils.assertNotNull(schema.getCredenziali(), "schemaAutenticazione.credenziali");

    boolean isApiManager = schema.getTipo() != null
        && TipoSchemaAutenticazione.API_MANAGER.toString().equals(schema.getTipo().getCodice());

    logger.debug(methodName, "avvio chiamata per ottenimento token per autenticazione da schema {}",
        schema.getId());

    List<CosmoTCredenzialiAutenticazioneFruitore> basicSchemas = schema.getCredenziali().stream()
        .filter(CosmoTEntity::nonCancellato).collect(Collectors.toList());

    if (basicSchemas.isEmpty()) {
      throw new InternalServerException(
          "Nessuna credenziale valida per lo schema di autenticazione " + schema.getId());
    } else if (basicSchemas.size() > 1) {
      throw new InternalServerException(
          "Credenziali valide non univoche per lo schema di autenticazione " + schema.getId());
    }

    CosmoTCredenzialiAutenticazioneFruitore credenzialiAttive = basicSchemas.get(0);

    if (StringUtils.isBlank(credenzialiAttive.getClientId())) {
      throw new InternalServerException("Credenziali non valide per lo schema di autenticazione "
          + schema.getId() + ": ClientID non fornito");
    }
    if (StringUtils.isBlank(credenzialiAttive.getClientSecret())) {
      throw new InternalServerException("Credenziali non valide per lo schema di autenticazione "
          + schema.getId() + ": ClientSecret non fornito");
    }

    // costruisci indirizzo per la richiesta
    String tokenEndpointForSchema = schema.getTokenEndpoint();
    if (StringUtils.isBlank(tokenEndpointForSchema)) {
      tokenEndpointForSchema =
          isApiManager
          ? configurazioneService.requireConfig(ParametriApplicativo.APIMGR_TOKEN_ENDPOINT)
              .asString()
              : DEFAULT_TOKEN_ENDPOINT_RELATIVE;
    }

    URI tokenURI = costruisciRestURI(schema.getFruitore(), tokenEndpointForSchema, null, null);

    logger.debug(methodName, "la chiamata per ottenimento token avra' endpoint: {}", tokenURI);

    // costruisci metodo
    String method = schema.getMetodoRichiestaToken();
    if (StringUtils.isBlank(method)) {
      method = HttpMethod.POST.name();
    }

    logger.debug(methodName, "la chiamata per ottenimento token avra' metodo: {}", method);

    // compilo le varie espressioni
    Map<String, Object> effectiveQueryParams = new HashMap<>();
    Object payload = null;

    var sandboxContext = SandboxFactory.forTokenRequest(credenzialiAttive);

    String mappaturaRichiestaToken = schema.getMappaturaRichiestaToken();
    if (StringUtils.isBlank(mappaturaRichiestaToken)) {
      mappaturaRichiestaToken =
          isApiManager ? MAPPATURA_RICHIESTA_TOKEN_APIMGR : DEFAULT_MAPPATURA_RICHIESTA_TOKEN;
    }

    if (!StringUtils.isBlank(mappaturaRichiestaToken)) {
      logger.debug(methodName, "avvio mappatura per payload");

      if (method.equalsIgnoreCase(HttpMethod.GET.name())) {
        logger.debug(methodName,
            "avvio mappatura per payload come parametri query (il metodo e' GET)");
        eseguiMappatura(mappaturaRichiestaToken, sandboxContext).forEach(effectiveQueryParams::put);
      } else {
        logger.debug(methodName, "avvio mappatura per payload come parametri request body");
        payload =
            motoreJsonDinamicoService.eseguiMappatura(mappaturaRichiestaToken, sandboxContext);
      }
    }

    MediaType contentType = isApiManager
        || CODICE_CONTENT_TYPE_FORM_ENCODED.equals(schema.getContentTypeRichiestaToken())
        ? MediaType.APPLICATION_FORM_URLENCODED
            : MediaType.APPLICATION_JSON;

    if (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)) {
      Map<String, Object> mapped = ObjectUtils.getDataMapper().convertValue(payload,
          new TypeReference<Map<String, Object>>() {});
      var payloadMVM = new LinkedMultiValueMap<String, Object>();
      for (Entry<String, Object> entry : mapped.entrySet()) {
        payloadMVM.put(entry.getKey(), Arrays.asList(entry.getValue()));
      }
      payload = payloadMVM;
    }

    //@formatter:off
    var chiamataRequest = ChiamataEsternaRequest.builder(JsonNode.class)
        .withUrl(tokenURI.toString())
        .withMethod(method)
        .withQueryParams(effectiveQueryParams)
        .withPayload(payload)
        .withContentType(contentType)
        .build();
    //@formatter:on

    logger.debug(methodName, "avvio la chiamata per il token");

    ResponseEntity<JsonNode> risultatoChiamata;

    try {
      risultatoChiamata = inviaChiamataEsterna(chiamataRequest);
    } catch (Exception e) {
      logger.error(methodName,
          "errore nella chiamata per l'ottenimento di un token di autenticazione", e);
      throw e;
    }

    logger.debug(methodName, "ottenuta response alla chiamata per il token: {}",
        risultatoChiamata.getStatusCode());

    JsonNode risultatoMappaturaOutput = null;
    final var extractVarToken = "token";
    final var extractVarExpiresIn = "expiresIn";

    String mappaturaOutputToken = schema.getMappaturaOutputToken();
    if (StringUtils.isBlank(mappaturaOutputToken)) {
      mappaturaOutputToken =
          isApiManager ? MAPPATURA_OUTPUT_TOKEN_APIMGR : DEFAULT_MAPPATURA_OUTPUT_TOKEN;
    }

    if (!StringUtils.isBlank(mappaturaOutputToken)) {
      // mappa l'output della request
      logger.debug(methodName,
          "avvio mappatura dell'output della richiesta per estrarre il risultato");
      risultatoMappaturaOutput = motoreJsonDinamicoService.eseguiMappatura(mappaturaOutputToken,
          SandboxFactory.fromHttpResponse(risultatoChiamata));
    }

    if (risultatoMappaturaOutput == null || !risultatoMappaturaOutput.isObject()
        || !risultatoMappaturaOutput.has(extractVarToken)) {
      throw new InternalServerException(
          "Nessun token estratto dall'output della chiamata al token endpoint");
    }

    String token = risultatoMappaturaOutput.get(extractVarToken).asText();

    if (StringUtils.isBlank(token)) {
      throw new InternalServerException(
          "Token non valido estratto dall'output della chiamata al token endpoint (vuoto)");
    }

    LocalDateTime expiresAtResponse = null;
    LocalDateTime expiresAtJWT = null;

    logger.debug(methodName, "verifica della scadenza del token ottenuto");

    if (risultatoMappaturaOutput.has(extractVarExpiresIn)) {
      var ttlNode = risultatoMappaturaOutput.get(extractVarExpiresIn);
      if (!ttlNode.isNull() && ttlNode.isNumber()) {
        var ttl = ttlNode.asLong();
        if (ttl > 0) {
          expiresAtResponse = LocalDateTime.now().plusSeconds(ttl);
        }
      }
    }

    // controlla se il formato e' JWT
    boolean isJWT = CosmoJWTHelper.isJwtFormat(token);
    StandardAuthenticationToken jwtDecoded = null;

    if (isJWT) {
      logger.debug(methodName,
          "il token ottenuto e' stato riconosciuto come formato JWT. Provo a decodificarlo per estrarre informazioni.");
      jwtDecoded =
          new CosmoJWTTokenDecoder("nope", LoggerConstants.ROOT_LOG_CATEGORY).decode(token);
    }

    if (jwtDecoded != null && jwtDecoded.getPayload() != null
        && jwtDecoded.getPayload().getExpiresAtEpoch() != null) {
      // controlla se il JWT contiene un campo 'exp'
      var expiresAtInJWT = jwtDecoded.getPayload().getExpiresAtEpoch();
      if (expiresAtInJWT > 0) {
        expiresAtJWT = LocalDateTime.ofInstant(Instant.ofEpochMilli(expiresAtInJWT * 1000),
            ZoneId.systemDefault());
      }
    }

    LocalDateTime expiresAt = null;
    if (expiresAtResponse != null && expiresAtJWT != null
        && !expiresAtResponse.equals(expiresAtJWT)) {
      logger.warn(methodName,
          "Doppia scadenza non coerente del token indicata sia nella response che nel contenuto del token. Verra' usata la scadenza piu' stringente.");
      expiresAt = expiresAtResponse;
      if (expiresAtJWT.isBefore(expiresAt)) {
        expiresAt = expiresAtJWT;
      }
    } else if (expiresAtResponse != null) {
      expiresAt = expiresAtResponse;
    } else if (expiresAtJWT != null) {
      expiresAt = expiresAtJWT;
    }

    CachedTokenEntry cacheEntry = new CachedTokenEntry();
    cacheEntry.token = token;
    cacheEntry.expiresAt = expiresAt;
    return cacheEntry;
  }

  private class CachedTokenEntry {
    String token;
    LocalDateTime expiresAt;

    public Long ttl() {
      if (expiresAt == null) {
        return null;
      }
      return LocalDateTime.now().until(expiresAt, ChronoUnit.SECONDS);
    }
  }

  @Override
  public Object testSchemaAutenticazione(Long idSchemaAutenticazione) {
    ValidationUtils.require(idSchemaAutenticazione, "idSchemaAutenticazione");

    var schema = this.cosmoTSchemaAutenticazioneFruitoreRepository.findOne(idSchemaAutenticazione);
    if (schema == null) {
      throw new NotFoundException("Schema di autenticazione non trovato");
    }

    if (!TipoSchemaAutenticazione.TOKEN.name().equals(schema.getTipo().getCodice())) {
      throw new BadRequestException(
          "Impossibile testare uno schema di autenticazione di questo tipo");
    }

    Map<String, Object> output = new HashMap<>();

    try {
      var token = getTokenValido(schema, true);

      output.put("success", true);
      output.put("token", token);

    } catch (HttpStatusCodeException e) {
      output.put("success", false);
      output.put("errorCode", e.getStatusCode());
      output.put("error", e.getMessage());
      output.put("errorResponse", e.getResponseBodyAsString());

    } catch (Exception e) {
      output.put("success", false);
      output.put("error", e.getMessage());

    }

    return output;
  }
}
