/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.TikaInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import it.csi.cosmo.common.async.internal.ContextAwareCallable;
import it.csi.cosmo.common.components.CosmoErrorHandler;
import it.csi.cosmo.common.entities.CosmoDOperazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoSchemaAutenticazione_;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipodocTipopratica_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore;
import it.csi.cosmo.common.entities.CosmoTEndpointFruitore_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTSchemaAutenticazioneFruitore_;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoSchemaAutenticazione;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.entities.proto.IntervalloValiditaEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.CallbackService;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.FruitoriService;
import it.csi.cosmo.cosmoecm.business.service.StreamDataToIndexService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.ContenutoDocumentoResult;
import it.csi.cosmo.cosmoecm.dto.ResponseHeaderDTO;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiLinkFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentoFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentoLinkFruitore;
import it.csi.cosmo.cosmoecm.dto.tika.TikaDTO;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CycleAvoidingMappingContext;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRFormatoFileTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocumentoTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTCredenzialiAutenticazioneFruitoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEndpointFruitoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTPraticaSpecifications;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.FilesUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class FruitoriServiceImpl implements FruitoriService {

  private static final String CLASS_NAME = FruitoriServiceImpl.class.getSimpleName();

  public static final String FIELD_ID_PRATICA = "idPratica";
  public static final String FIELD_ID_PRATICA_EXT = "idPraticaExt";
  public static final String FIELD_ID_DOCUMENTO_EXT = "idDocumentoExt";
  public static final String FIELD_DOCUMENTO = "documento";

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private CosmoTDocumentoMapper cosmoTDocumentoMapper;

  @Autowired
  private CosmoTCredenzialiAutenticazioneFruitoreRepository cosmoTCredenzialiAutenticazioneFruitoreRepository;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  AsyncTaskService asyncTaskService;

  @Autowired
  StreamDataToIndexService streamDataToIndexService;

  @Autowired
  CosmoTEndpointFruitoreRepository cosmoTEndpointFruitoreRepository;

  @Autowired
  CallbackService callbackService;

  @Autowired
  CosmoRTipoDocTipoPraticaRepository cosmoRTipoDocTipoPraticaRepository;

  @Autowired
  CosmoRTipoDocumentoTipoDocumentoRepository cosmoRTipoDocumentoTipoDocumentoRepository;

  @Autowired
  CosmoRFormatoFileTipoDocumentoRepository cosmoRFormatoFileTipoDocumentoRepository;

  private static final int MAX_PARALLEL_EXECUTIONS = 3;

  private CosmoErrorHandler errorMapper =
      new CosmoErrorHandler(LogCategory.BUSINESS_LOG_CATEGORY.getCategory()) {

    @Override
    protected boolean exposeExceptions() {
      return false;
    }

  };

  @Override
  @Transactional(rollbackFor = Exception.class, timeout = 60000)
  public EsitoCreazioneDocumentiFruitore inserisciDocumenti(CreaDocumentiFruitoreRequest request) {
    final var methodName = "inserisciDocumenti";
    ValidationUtils.validaAnnotations(request);
    if (request.getDocumenti().isEmpty()) {
      logger.error(methodName, ErrorMessages.D_NESSUN_DOCUMENTO_FORNITO);
      throw new UnprocessableEntityException(ErrorMessages.D_NESSUN_DOCUMENTO_FORNITO);
    }

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    var enteAssociato = verificaEnteAssociato(fruitoreEntity, request.getCodiceIpaEnte());

    var pratica = this.findPraticaByChiaveEsterna(request.getIdPratica(), request.getCodiceIpaEnte(),
            fruitoreEntity.getId());

    EsitoCreazioneDocumentiFruitore documentiCreati = new EsitoCreazioneDocumentiFruitore();
    List<EsitoCreazioneDocumentoFruitore> documentiSingoli = new LinkedList<>();

    logger.debug(methodName, "inserisco {} documenti", request.getDocumenti().size());

    // inserisco singolarmente i documenti

    for (CreaDocumentoFruitoreRequest documento : request.getDocumenti()) {

      ValidationUtils.require(documento.getCodiceTipo(), "Tipologia documento");

      documentiSingoli
      .add(tentaInserimentoDocumento(documento, pratica, enteAssociato, fruitoreEntity));
    }

    documentiCreati.setEsiti(documentiSingoli);
    return documentiCreati;
  }

  @Override
  public ContenutoDocumentoResult getContenutoFruitore(String idDocumentoExt) {
    ValidationUtils.require(idDocumentoExt, "id");
    ValidationUtils.require(idDocumentoExt, FIELD_ID_DOCUMENTO_EXT);

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    return getContenutoDocumentoPerFruitore(idDocumentoExt, fruitoreEntity.getId(), false);
  }

  @Override
  public ContenutoDocumentoResult getContenutoFruitoreSigned(String idDocumentoExt,
      String idChiavePubblica, String digest) {

    ValidationUtils.require(idDocumentoExt, FIELD_ID_DOCUMENTO_EXT);

    if (StringUtils.isBlank(idChiavePubblica)) {
      throw new UnauthorizedException("Nessuna chiave pubblica fornita");
    }

    if (StringUtils.isBlank(digest)) {
      throw new UnauthorizedException("Nessun digest precalcolato fornito");
    }

    // ricerco la chiave attraverso l'id chiave pubblica
    //@formatter:off
    var chiave = cosmoTCredenzialiAutenticazioneFruitoreRepository.findOneNotDeleted((root, cq, cb) -> {
      var joinSchema = root.join(CosmoTCredenzialiAutenticazioneFruitore_.schemaAutenticazione);
      return cb.and(
          cb.isNull(root.get(CosmoTEntity_.dtCancellazione)),
          cb.isNull(joinSchema.get(CosmoTEntity_.dtCancellazione)),
          cb.equal(joinSchema.get(CosmoTSchemaAutenticazioneFruitore_.inIngresso), true),
          cb.equal(joinSchema.get(CosmoTSchemaAutenticazioneFruitore_.tipo).get(CosmoDTipoSchemaAutenticazione_.codice), TipoSchemaAutenticazione.DIGEST.name()),
          cb.isNotNull(root.get(CosmoTCredenzialiAutenticazioneFruitore_.clientId)),
          cb.equal(root.get(CosmoTCredenzialiAutenticazioneFruitore_.clientId), idChiavePubblica)
          );
    }).orElseThrow(
        () -> new UnauthorizedException("La chiave fornita non e' stata riconosciuta")
        );
    //@formatter:on

    // get schema autenticazione dalla chiave
    var schema = chiave.getSchemaAutenticazione();
    if (schema == null || schema.cancellato()) {
      throw new UnauthorizedException("Schema di autenticazione non valido");
    }

    // get fruitore dallo schema autenticazione
    var fruitore = schema.getFruitore();
    if (fruitore == null || fruitore.cancellato()) {
      throw new UnauthorizedException("Fruitore non valido");
    }

    // valida firma digest
    validaDigestPerDocumento(idDocumentoExt, digest, chiave);

    // get del documento
    return getContenutoDocumentoPerFruitore(idDocumentoExt, fruitore.getId(), true);
  }

  @Override
  public EsitoCreazioneDocumentiLinkFruitore inserisciDocumentiLink(
      CreaDocumentiLinkFruitoreRequest request) {
    String methodName = "inserisciDocumentiLink";
    ValidationUtils.validaAnnotations(request);

    if (request.getDocumenti().isEmpty()) {
      logger.error(methodName, ErrorMessages.D_NESSUN_DOCUMENTO_FORNITO);
      throw new UnprocessableEntityException(ErrorMessages.D_NESSUN_DOCUMENTO_FORNITO);
    }

    request.getDocumenti()
        .forEach(doc -> ValidationUtils.require(doc.getCodiceTipo(), "Tipologia documento"));

    // ottengo la reference al fruitore
    CosmoTFruitore fruitoreEntity = getFruitoreCorrente();

    // valido il codice ipa ente verificando contro le associazioni del fruitore
    var enteAssociato = verificaEnteAssociato(fruitoreEntity, request.getCodiceIpaEnte());

    var pratica =
        this.findPraticaByChiaveEsterna(request.getIdPratica(), request.getCodiceIpaEnte(),
            fruitoreEntity.getId());

    validaDocumentiLink(request.getDocumenti());

    EsitoCreazioneDocumentiLinkFruitore documentiCreati = new EsitoCreazioneDocumentiLinkFruitore();
    EsitoCreazioneDocumentiLinkFruitore docs = new EsitoCreazioneDocumentiLinkFruitore();
    List<EsitoCreazioneDocumentoLinkFruitore> documentiSingoli = new LinkedList<>();
    List<EsitoCreazioneDocumentoLinkFruitore> docSingoli = new LinkedList<>();

    logger.debug(methodName, "inserisco {} documenti", request.getDocumenti().size());

    String taskName = "Inserimento documenti link via streaming";
    logger.debug(methodName,
        "Avvio la chiamata asincrona per l' inserimento dei documenti via streaming su cosmo e su index");

    asyncTaskService.start(taskName, task -> {

      var executor = Executors
          .newFixedThreadPool(Math.min(request.getDocumenti().size(), MAX_PARALLEL_EXECUTIONS));

      // submit di subtask all'executor per l'esecuzione parallela
      for (CreaDocumentoLinkFruitoreRequest documento : request.getDocumenti()) {

        var callable = new ContextAwareCallable<Object>(() ->

        task.step(taskName, step -> {
          try {
            Esito esito = new Esito();
            EsitoCreazioneDocumentoLinkFruitore es = new EsitoCreazioneDocumentoLinkFruitore();
            esito =
                tentaInserimentoDocumentoLink(documento, pratica, enteAssociato, fruitoreEntity);
            es.setEsito(esito);
            docSingoli.add(es);
          } catch (Throwable terr) { // NOSONAR
            Esito esito = new Esito();
            var converted = errorMapper.convertException(terr);
            esito.setCode(converted.getCode());
            esito.setStatus(converted.getStatus());
            esito.setTitle(converted.getTitle());
          }
          return null;
        }), getCurrentRequestAttributes());


        executor.submit(callable);

      }

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(Math.max(3000, 15 * 60), TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        task.warn(
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }

      boolean esisteEndpoint = verificaEsistenzaEndpoint(OperazioneFruitore.CALLBACK_ESITO_DOC_LINK,
          pratica.getFruitore().getId(), null).isPresent();

      logger.debug(methodName,
          "esistenza di endpoint di callback configurato per il fruitore {}: {}",
          pratica.getFruitore().getNomeApp(), esisteEndpoint);

      boolean inviaCallback;

      if (Boolean.TRUE.equals(request.isRichiediCallback()) && esisteEndpoint) {
        inviaCallback = true;
      } else if (request.isRichiediCallback() == null) {
        inviaCallback = esisteEndpoint;
      } else {
        inviaCallback = false;
      }

      if (inviaCallback) {

        logger.debug(methodName,
            "schedulo invio callback di esito creazione documenti tramite link per la pratica {}",
            pratica.getId());

        Map<String, Object> parametri = new HashMap<>();
        parametri.put(FIELD_ID_PRATICA, pratica.getId());
        parametri.put(FIELD_ID_PRATICA_EXT, pratica.getIdPraticaExt());

        docs.setEsiti(docSingoli);
        var payload = docs;
        callbackService.schedulaInvioAsincrono(OperazioneFruitore.CALLBACK_ESITO_DOC_LINK,
            pratica.getFruitore().getId(), payload, parametri, null);
      }

      return null;
    });

    request.getDocumenti().stream().forEach(doc -> {
      EsitoCreazioneDocumentoLinkFruitore e = new EsitoCreazioneDocumentoLinkFruitore();
      Esito esito = new Esito();
      esito.setCode("ACCEPTED");
      esito.setStatus(202);
      esito.setTitle(
          "La richiesta di download da link " + doc.getLink() + " e' stata presa in carico");
      e.setEsito(esito);
      e.setInput(doc);
      documentiSingoli.add(e);
    });


    documentiCreati.setEsiti(documentiSingoli);
    return documentiCreati;
  }

  @Override
  public CosmoTEndpointFruitore getEndpoint(OperazioneFruitore operazione, Long idFruitore,
      String codiceDescrittivo) {
    ValidationUtils.assertNotNull(operazione, "operazione");
    ValidationUtils.assertNotNull(idFruitore, "idFruitore");

    String errorMessage = (codiceDescrittivo != null && !codiceDescrittivo.isBlank())
        ? ("Nessun endpoint per l'operazione con codice descrittivo " + codiceDescrittivo
            + " sul fruitore " + idFruitore)
        : ("Nessun endpoint per l'operazione " + operazione.name() + " sul fruitore " + idFruitore);

    return fetchEndpoint(operazione, idFruitore, codiceDescrittivo)
        .orElseThrow(() -> new InternalServerException(errorMessage));
  }

  private void validaDigestPerDocumento(String idDocumentoExt, String digest,
      CosmoTCredenzialiAutenticazioneFruitore credenziali) {
    final var method = "validaDigestPerDocumento";

    ValidationUtils.require(idDocumentoExt, FIELD_ID_DOCUMENTO_EXT);
    ValidationUtils.require(digest, "digest");
    ValidationUtils.require(credenziali, "credenziali");

    if (StringUtils.isBlank(credenziali.getClientSecret())) {
      throw new InternalServerException("Credenziali non configurate correttamente");
    }

    // ricalcola il digest
    String digestSubject = idDocumentoExt.strip() + ":" + credenziali.getClientSecret().strip();
    String correctDigest = DigestUtils.sha256Hex(digestSubject);

    if (logger.isDebugEnabled()) {
      logger.debug(method, "checking digest signature");
      logger.debug(method, "\tdigest signature digest alg = [sha256hex]");
      logger.debug(method, "\tdigest signature digest subject = [{}]", digestSubject);
      logger.debug(method, "\tdigest signature result = [{}]", correctDigest);
      logger.debug(method, "\tdigest incoming signature = [{}]", digest);
    }

    if (!correctDigest.equalsIgnoreCase(digest)) {
      throw new ForbiddenException("Invalid digest");
    }
  }

  private ContenutoDocumentoResult getContenutoDocumentoPerFruitore(String idDocumentoExt,
      Long idFruitore, boolean anteprima) {
    final var methodName = "getContenutoDocumentoPerFruitore";
    // ricerco il documento tramite idDocumentoExt
    var documento = cosmoTDocumentoRepository.findOneNotDeleted((root, cq, cb) -> {
      var joinPratica = root.join(CosmoTDocumento_.pratica);
      var joinFruitore = joinPratica.join(CosmoTPratica_.fruitore);
      //@formatter:off
      return cb.and(
          cb.isNotNull(joinPratica.get(CosmoTPratica_.fruitore)),
          cb.isNotNull(root.get(CosmoTDocumento_.idDocumentoExt)),
          cb.equal(joinFruitore.get(CosmoTFruitore_.id), idFruitore),
          cb.equal(root.get(CosmoTDocumento_.idDocumentoExt), idDocumentoExt)
          );
      //@formatter:on
    }).orElseThrow(() -> {
      String message =
          String.format(ErrorMessages.D_DOCUMENTO_FRUITORE_NON_TROVATO, idDocumentoExt);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    });

    ContenutoDocumentoResult contentResult = new ContenutoDocumentoResult();
    var contenuto = getContenutoDocumentoFruitore(documento);
    if (contenuto.getTipo() != null
        && TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {
      var localContent =
          contenutoDocumentoService.getContenutoFisico(documento.getId(), contenuto.getId());
      contentResult.setContent(localContent.getContent());
      contentResult.setFileName(localContent.getFileName());
      contentResult.setMimeType(localContent.getMimeType());
    } else {
      contentResult.setLinkDownloadDiretto(contenutoDocumentoService
          .getLinkDownloadDiretto(documento.getId(), contenuto.getId(), anteprima));
    }
    return contentResult;
  }

  private CosmoTContenutoDocumento getContenutoDocumentoFruitore(CosmoTDocumento doc) {
    ValidationUtils.require(doc, "doc");

    String methodName = "getContenutoDocumentoFruitore";

    // verifico la presenza del contenuto di tipo FIRMATO
    var contenuto = doc.getContenuti().stream()
        .filter(c -> c.getDtCancellazione() == null
        && TipoContenutoDocumento.FIRMATO.name().equals(c.getTipo().getCodice()))
        .sorted(Comparator.comparing(CosmoTContenutoDocumento::getDtInserimento).reversed())
        .limit(1).findFirst();

    if (contenuto.isEmpty()) {
      // verifico la presenza del contenuto di tipo ORIGINALE
      contenuto = doc.getContenuti().stream()
          .filter(c -> c.getDtCancellazione() == null
          && TipoContenutoDocumento.ORIGINALE.name().equals(c.getTipo().getCodice()))
          .sorted(Comparator.comparing(CosmoTContenutoDocumento::getDtInserimento).reversed())
          .limit(1).findFirst();
    } else {
      logger.info(methodName, "Reperimento contenuto firmato: OK");
    }

    if (contenuto.isEmpty()) {
      // verifico la presenza del contenuto di tipo TEMPORANEO
      contenuto = doc.getContenuti().stream()
          .filter(c -> c.getDtCancellazione() == null
          && TipoContenutoDocumento.TEMPORANEO.name().equals(c.getTipo().getCodice()))
          .sorted(Comparator.comparing(CosmoTContenutoDocumento::getDtInserimento).reversed())
          .limit(1).findFirst();
      if (contenuto.isPresent()) {
        logger.info(methodName, "Reperimento contenuto temporaneo: OK");
      } else {
        logger.error(methodName, ErrorMessages.D_NESSUN_CONTENUTO_DISPONIBILE_RECUPERO_FRUITORE);
        throw new NotFoundException(ErrorMessages.D_NESSUN_CONTENUTO_DISPONIBILE_RECUPERO_FRUITORE);
      }
    } else {
      logger.info(methodName, "Reperimento contenuto originale: OK");
    }

    return contenuto.get();
  }

  private EsitoCreazioneDocumentoFruitore tentaInserimentoDocumento(
      CreaDocumentoFruitoreRequest documento, CosmoTPratica pratica, CosmoTEnte ente,
      CosmoTFruitore fruitoreEntity) {
    final var method = "tentaInserimentoDocumento";

    var output = new EsitoCreazioneDocumentoFruitore();
    var esito = new Esito();

    var attempt = transactionService.inTransaction(() -> cosmoTDocumentoMapper.toFruitoreDTO(
        inserisciDocumento(documento, pratica, ente, fruitoreEntity),
        new CycleAvoidingMappingContext()));

    if (attempt.success()) {
      esito.setCode("CREATED");
      esito.setStatus(201);
      esito.setTitle("Documento inserito con ID " + attempt.getResult().getId());
      output.setOutput(attempt.getResult());
    } else {
      logger.error(method, "errore nell'inserimento di un documento", attempt.getError());
      var converted = errorMapper.convertException(attempt.getError());
      esito.setCode(converted.getCode());
      esito.setStatus(converted.getStatus());
      esito.setTitle(converted.getTitle());
    }

    documento.setContenuto(null);
    output.setInput(documento);
    output.setEsito(esito);
    return output;
  }

  private void validaDocumento(CreaDocumentoFruitoreRequest documento, CosmoTPratica pratica,
      CosmoTEnte ente, CosmoTFruitore fruitoreEntity) {
    final var methodName = "validaDocumento";
    ValidationUtils.require(documento, FIELD_DOCUMENTO);
    ValidationUtils.require(pratica, "pratica");
    ValidationUtils.require(ente, "ente");

    ValidationUtils.validaAnnotations(documento);

    boolean hasUploadedUUID = !StringUtils.isBlank(documento.getUploadUUID());
    boolean hasEmbeddedContent = documento.getContenuto() != null;

    if (!hasUploadedUUID && !hasEmbeddedContent) {
      logger.error(methodName, ErrorMessages.F_FRUITORE_NESSUN_CONTENUTO_FORNITO);
      throw new UnprocessableEntityException(
          ErrorMessages.F_FRUITORE_NESSUN_CONTENUTO_FORNITO);
    } else if (hasUploadedUUID && hasEmbeddedContent) {
      logger.error(methodName, ErrorMessages.F_FRUITORE_CONTENUTO_FORNITO_NON_UNIVOCO);
      throw new UnprocessableEntityException(
          ErrorMessages.F_FRUITORE_CONTENUTO_FORNITO_NON_UNIVOCO);
    }
    if (hasEmbeddedContent) {
      ValidationUtils.validaAnnotations(documento.getContenuto());
      if (documento.getContenuto().getContenutoFisico().length() < 1) {
        logger.error(methodName, ErrorMessages.F_FRUITORE_CONTENUTO_BINARIO_NON_PRESENTE);
        throw new UnprocessableEntityException(ErrorMessages.F_FRUITORE_CONTENUTO_BINARIO_NON_PRESENTE);
      } else {
        long maxSize = configurazioneService
            .requireConfig(ParametriApplicativo.MAX_DOCUMENT_SIZE_FOR_EMBEDDED_UPLOAD).asLong();
        if (documento.getContenuto().getContenutoFisico().length() > maxSize) {
          var msgFormatted = String.format(ErrorMessages.F_FRUITORE_CONTENUTO_BINARIO_ECCESSIVAMENTE_GRANDE, maxSize);
          logger.error(methodName, msgFormatted);
          throw new UnprocessableEntityException(msgFormatted);
        }
      }
    }
    // se id_documento_ext e' valorizzato, verifico che non sia gia' presente sulla tabella
    if (!StringUtils.isBlank(documento.getId())
        && this.findDocumentoByChiaveEsterna(documento.getId(), pratica.getIdPraticaExt(),
            ente.getCodiceIpa(), fruitoreEntity.getId()).isPresent()) {
      var msgFormatted =
          String.format(ErrorMessages.D_DOCUMENTO_FRUITORE_ESISTENTE, documento.getId());
      logger.error(methodName, msgFormatted);
      throw new ConflictException(msgFormatted);
    }

    // se id_doc_parent_ext e' valorizzato, verifico che sia presente sulla tabella
    // cosmo_t_documento come id_documento_ext (gestione allegato)
    if (!StringUtils.isBlank(documento.getIdPadre())
        && this.findDocumentoByChiaveEsterna(documento.getIdPadre(), pratica.getIdPraticaExt(),
            ente.getCodiceIpa(), fruitoreEntity.getId()).isEmpty()) {
      var msgFormatted =
          String.format(ErrorMessages.D_DOCUMENTO_FRUITORE_NON_TROVATO, documento.getIdPadre());
      logger.error(methodName, msgFormatted);
      throw new NotFoundException(msgFormatted);
    }
  }

  private CosmoTContenutoDocumento creaContenutoDocumentoLink(ResponseHeaderDTO infoFile,
      CreaDocumentoLinkFruitoreRequest documento) {
    final var method = "creaContenutoDocumentoLink";
    logger.debug(method, "creazione del contenuto del documento passato tramite link");

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDimensione(infoFile.getSize());
    output.setNomeFile(documento.getNomeFile());
    output.setUuidNodo(UUID.randomUUID().toString());
    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.TEMPORANEO.name());
    output.setTipo(tipo);
    output.setFormatoFile(contenutoDocumentoService.findFormatoByMime(infoFile.getMimeType()));
    return output;
  }


  private void trasferisciContenutiEmbedded(CreaDocumentoFruitoreRequest documento) {
    final var method = "trasferisciContenutiTemporanei";

    ValidationUtils.require(documento, FIELD_DOCUMENTO);
    ValidationUtils.require(documento.getContenuto(), "documento.contenuto");

    var contenuto = documento.getContenuto();

    logger.debug(method, "trasferisco contenuto embedded su disco");

    byte[] byteArray;
    try {
      byteArray = Base64.getDecoder().decode(contenuto.getContenutoFisico());
    } catch (Exception e) {
      logger.error(method, ErrorMessages.F_FRUITORE_ERRORE_DECODIFICA_CONTENUTO_BASE64);
      throw new BadRequestException(ErrorMessages.F_FRUITORE_ERRORE_DECODIFICA_CONTENUTO_BASE64);
    }

    var saved = fileShareService.saveFromMemory(new ByteArrayInputStream(byteArray),
        contenuto.getNomeFile(),
        StringUtils.isBlank(contenuto.getMimeType()) ? "application/octet-stream"
            : contenuto.getMimeType(),
            SecurityUtils.getClientCorrente().getCodice());

    logger.info(method, "trasferito contenuto embedded su disco come file {}",
        saved.getMetadata().getFileUUID());
    documento.setUploadUUID(saved.getMetadata().getFileUUID());
  }

  private CosmoTDocumento inserisciDocumento(CreaDocumentoFruitoreRequest documento,
      CosmoTPratica pratica, CosmoTEnte ente, CosmoTFruitore fruitoreEntity) {
    String methodName = "inserisciDocumento";

    validaDocumento(documento, pratica, ente, fruitoreEntity);

    if (documento.getContenuto() != null) {
      trasferisciContenutiEmbedded(documento);
    }
    ValidationUtils.require(documento.getUploadUUID(), "documento.uploadUUID");

    // verifico che l'uuid del file sia presente sul file system
    RetrievedContent file = null;

    // creo la entity e salvo su DB
    var documentoDaSalvare = new CosmoTDocumento();

    try {
      file = fileShareService.get(documento.getUploadUUID(), LocalDate.now());
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new NotFoundException(
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, documento.getUploadUUID()));
    }

    var tipoDocumento = findTipoDocumento(documento.getCodiceTipo());

    CosmoTDocumento docPadre = null;
    if (null != documento.getIdPadre()) {
      docPadre = cosmoTDocumentoRepository.findByPraticaIdAndIdDocumentoExt(pratica.getId(),
          documento.getIdPadre());
      if (null == docPadre) {
        throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_PRINCIPALE_NON_TROVATO,
            documento.getIdPadre()));
      }
      documentoDaSalvare.setDocumentoPadre(docPadre);
    }
    validaTipoDocumento(pratica, tipoDocumento, documento.getId(), docPadre, file.getContentSize(), file.getContentStream());

    // valorizzo dati se non passati
    if (StringUtils.isBlank(documento.getId())) {
      documento.setId(UUID.randomUUID().toString());
    }

    documentoDaSalvare.setAutore(documento.getAutore());
    documentoDaSalvare.setDescrizione(documento.getDescrizione());
    documentoDaSalvare.setTitolo(documento.getTitolo());
    documentoDaSalvare.setTipo(tipoDocumento);
    documentoDaSalvare
    .setPratica(cosmoTDocumentoRepository.reference(CosmoTPratica.class, pratica.getId()));
    documentoDaSalvare.setIdDocParentExt(documento.getIdPadre());
    documentoDaSalvare.setIdDocumentoExt(documento.getId());
    documentoDaSalvare.setStato(cosmoTDocumentoRepository.reference(CosmoDStatoDocumento.class,
        StatoDocumento.ACQUISITO.name()));

    var contenuto = contenutoDocumentoService.creaContenutoTemporaneo(file);
    if (contenuto.getFormatoFile() != null
        && Boolean.FALSE.equals(contenuto.getFormatoFile().getUploadConsentito())) {
      throw new BadRequestException("Il tipo del documento fornito non e' consentito.");
    }

    documentoDaSalvare = cosmoTDocumentoRepository.save(documentoDaSalvare);

    contenuto.setDocumentoPadre(documentoDaSalvare);
    contenuto.setShaFile(contenutoDocumentoService.generaSha256PerFile(file));
    cosmoTContenutoDocumentoRepository.save(contenuto);

    logger.info(methodName, "documento con id {} inserito da fruitore esterno",
        documentoDaSalvare.getId());
    return documentoDaSalvare;
  }

  private CosmoDTipoDocumento findTipoDocumento(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    final var methodName = "findTipoDocumento";

    CosmoDTipoDocumento tipoDocumento =
        cosmoDTipoDocumentoRepository.findOneActive(codice).orElse(null);

    var relTipoDocFormatoFile = cosmoRFormatoFileTipoDocumentoRepository.findActiveByField(
        CosmoRFormatoFileTipoDocumento_.cosmoDTipoDocumento, tipoDocumento);

    if (tipoDocumento == null) {
      String message = String.format(ErrorMessages.D_TIPO_DOCUMENTO_NON_ESISTENTE, codice);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    tipoDocumento.setCosmoRFormatoFileTipoDocumentos(relTipoDocFormatoFile);
    return tipoDocumento;
  }

  private CosmoTFruitore getFruitoreCorrente() {
    final var methodName = "getFruitoreCorrente";
    // ottengo il fruitore corrente
    var fruitore = SecurityUtils.getClientCorrente();

    // ottengo la reference al fruitore
    return cosmoTFruitoreRepository
        .findOneNotDeletedByField(CosmoTFruitore_.apiManagerId, fruitore.getCodice()).orElseThrow(
            () -> {
              logger.error(methodName, ErrorMessages.F_FRUITORE_NON_AUTENTICATO_NON_RICONOSCIUTO);
               return new UnauthorizedException(ErrorMessages.F_FRUITORE_NON_AUTENTICATO_NON_RICONOSCIUTO);
              });
  }

  private CosmoTEnte verificaEnteAssociato(CosmoTFruitore fruitoreEntity, String codiceIpaEnte) {
    final var methodName = "verificaEnteAssociato";
    //@formatter:off
    return fruitoreEntity.getCosmoRFruitoreEntes().stream()
        .filter(IntervalloValiditaEntity::valido)
        .map(CosmoRFruitoreEnte::getCosmoTEnte)
        .filter(ente -> !ente.cancellato())
        .filter(ente -> codiceIpaEnte.equalsIgnoreCase(ente.getCodiceIpa()))
        .findFirst()
        .orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.F_FRUITORE_IPA_ENTE_NON_CORRISPONDE_AGLI_ENTI_ASSOCIATI);
          return new BadRequestException(ErrorMessages.F_FRUITORE_IPA_ENTE_NON_CORRISPONDE_AGLI_ENTI_ASSOCIATI);
        });
    //@formatter:on
  }

  private CosmoTPratica findPraticaByChiaveEsterna(String idPraticaExt,
      String codiceIpaEnte, Long idFruitore) {
    final var methodName = "findPraticaByChiaveEsterna";
    return this.cosmoTPraticaRepository.findOneNotDeleted(CosmoTPraticaSpecifications
        .findByChiaveFruitoreEsterno(idPraticaExt, codiceIpaEnte, idFruitore)).orElseThrow(() -> {
          var msgFormatted =
                  String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPraticaExt);
          logger.error(methodName, msgFormatted);
          return new NotFoundException(msgFormatted);
            });

  }

  private Optional<CosmoTDocumento> findDocumentoByChiaveEsterna(String idDocumentoExt,
      String idPraticaExt, String codiceIpaEnte, Long idFruitore) {
    return this.cosmoTDocumentoRepository.findOneNotDeleted(CosmoTDocumentoSpecifications
        .findByChiaveFruitoreEsterno(idDocumentoExt, idPraticaExt, codiceIpaEnte, idFruitore));
  }


  private Esito tentaInserimentoDocumentoLink(CreaDocumentoLinkFruitoreRequest documento,
      CosmoTPratica pratica, CosmoTEnte ente, CosmoTFruitore fruitoreEntity) {
    final var method = "tentaInserimentoDocumentoLink";

    Esito esito = new Esito();


    var attempt = transactionService
        .inTransaction(() -> inserisciDocumentoLink(documento, pratica, ente, fruitoreEntity));

    if (attempt.success()) {

      var attempMigration =
          streamDataToIndexService.migraDocumento(attempt.getResult(), documento.getLink());
      if (attempMigration.isSuccesso()) {
        logger.debug(method,
            "documento con id " + attempt.getResult().getId() + " importato correttamente");
        esito.setCode("CREATED");
        esito.setStatus(201);
        esito.setTitle("Documento inserito con ID " + documento.getId());
      } else {
        logger.error(method, "errore nell'inserimento di un documento su index", attempt.getError());
        attempt.getResult().setStato(cosmoTDocumentoRepository.reference(CosmoDStatoDocumento.class,
            StatoDocumento.ERR_ACQUISIZIONE.name()));
        cosmoTDocumentoRepository.save(attempt.getResult());
        var converted = errorMapper.convertException(attempMigration.getErrore());
        esito.setCode(converted.getCode());
        esito.setStatus(converted.getStatus());
        esito.setTitle(converted.getTitle() + " - id documento: " + documento.getId());
      }
    } else {
      var converted = errorMapper.convertException(attempt.getError());
      esito.setCode(converted.getCode());
      esito.setStatus(converted.getStatus());
      esito.setTitle(converted.getTitle() + " - id documento: " + documento.getId());
      logger.error(method, "errore nell'inserimento di un documento", attempt.getError());
    }

    return esito;
  }

  private void validaDocumentoLink(CreaDocumentoLinkFruitoreRequest documento, CosmoTPratica pratica,
      CosmoTEnte ente, CosmoTFruitore fruitoreEntity) {

    ValidationUtils.require(documento, FIELD_DOCUMENTO);
    ValidationUtils.require(pratica, "pratica");
    ValidationUtils.require(ente, "ente");

    ValidationUtils.validaAnnotations(documento);

    // se id_documento_ext e' valorizzato, verifico che non sia gia' presente sulla tabella
    if (!StringUtils.isBlank(documento.getId())
        && this.findDocumentoByChiaveEsterna(documento.getId(), pratica.getIdPraticaExt(),
            ente.getCodiceIpa(), fruitoreEntity.getId()).isPresent()) {
      throw new ConflictException(
          String.format(ErrorMessages.D_DOCUMENTO_FRUITORE_ESISTENTE, documento.getId()));
    }

    // se id_doc_parent_ext e' valorizzato, verifico che sia presente sulla tabella
    // cosmo_t_documento come id_documento_ext (gestione allegato)
    if (!StringUtils.isBlank(documento.getIdPadre())
        && this.findDocumentoByChiaveEsterna(documento.getIdPadre(), pratica.getIdPraticaExt(),
            ente.getCodiceIpa(), fruitoreEntity.getId()).isEmpty()) {

      throw new NotFoundException(
          String.format(ErrorMessages.D_DOCUMENTO_FRUITORE_NON_TROVATO, documento.getIdPadre()));
    }
  }

  private CosmoTDocumento inserisciDocumentoLink(CreaDocumentoLinkFruitoreRequest documento,
      CosmoTPratica pratica, CosmoTEnte ente, CosmoTFruitore fruitoreEntity) {
    String methodName = "inserisciDocumentoLink";

    validaDocumentoLink(documento, pratica, ente, fruitoreEntity);

    var infoFile = FilesUtils.getResponseHeaderInfo(documento.getLink());
    var tipoDocumento = findTipoDocumento(documento.getCodiceTipo());
    CosmoTDocumento docPadre = null;
    if (null != documento.getIdPadre()) {
      docPadre = cosmoTDocumentoRepository.findByPraticaIdAndIdDocumentoExt(pratica.getId(),
          documento.getIdPadre());
      if (null == docPadre) {
        throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_PRINCIPALE_NON_TROVATO,
            documento.getIdPadre()));
      }
    }

    InputStream linkIS;
    try {
      linkIS = new URL(documento.getLink()).openStream();
    } catch (IOException e) {
      throw new BadRequestException( "Errore nella streaming del documento raggiungibile tramite link: " + e.getMessage());
    }
    validaTipoDocumento(pratica, tipoDocumento, documento.getId(), docPadre, infoFile.getSize(),
        linkIS);

    CosmoTDocumento documentoSalvato = inserisciDocumentoEffettivo(documento, pratica);
    logger.debug(methodName, "documento inserito correttamente");

    return documentoSalvato;
  }

  /*
   * Funzione che effettua lo streaming e il salvataggio del documento su index
   */


  private CosmoTDocumento inserisciDocumentoEffettivo(CreaDocumentoLinkFruitoreRequest documento,
      CosmoTPratica pratica) {

    final var method = "inserisciDocumentoEffettivo";

    ValidationUtils.require(documento, FIELD_DOCUMENTO);
    ValidationUtils.require(documento.getLink(), "documento.link");

    logger.debug(method, "streaming del documento raggiungibile tramite link: " + documento.getLink());

    ResponseHeaderDTO infoFile = FilesUtils.getResponseHeaderInfo(documento.getLink());
    return persistDocInTransazioneSeparata(documento, pratica, infoFile);

  }

  private CosmoTDocumento persistDocInTransazioneSeparata(
      CreaDocumentoLinkFruitoreRequest documento, CosmoTPratica pratica,
      ResponseHeaderDTO infoFile) {
    TransactionExecutionResult<CosmoTDocumento> result = transactionService.inTransaction(() ->
      persistDocByLink(documento, pratica, infoFile)
    );

    return result.getResult();

  }

  private CosmoTDocumento persistDocByLink(CreaDocumentoLinkFruitoreRequest documento, CosmoTPratica
      pratica, ResponseHeaderDTO infoFile) {

    String methodName = "persistDocByLink";
    var documentoDaSalvare = new CosmoTDocumento();


    var tipoDocumento = findTipoDocumento(documento.getCodiceTipo());

    contenutoDocumentoService.validaDimensioneDocumento(tipoDocumento, infoFile.getSize());

    if (null != documento.getIdPadre()) {
      var docPadre = cosmoTDocumentoRepository.findByPraticaIdAndIdDocumentoExt(pratica.getId(), documento.getIdPadre());
      if (null == docPadre) {
        throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_PRINCIPALE_NON_TROVATO,
            documento.getIdPadre()));
      }
      documentoDaSalvare.setDocumentoPadre(docPadre);
    }

    documentoDaSalvare.setAutore(documento.getAutore());
    documentoDaSalvare.setDescrizione(documento.getDescrizione());
    documentoDaSalvare.setTitolo(documento.getTitolo()); documentoDaSalvare.setTipo(tipoDocumento);
    documentoDaSalvare
    .setPratica(cosmoTDocumentoRepository.reference(CosmoTPratica.class, pratica.getId()));
    documentoDaSalvare.setIdDocParentExt(documento.getIdPadre());
    documentoDaSalvare.setIdDocumentoExt(documento.getId());
    documentoDaSalvare.setStato(cosmoTDocumentoRepository.reference(CosmoDStatoDocumento.class,
        StatoDocumento.IN_ELABORAZIONE.name()));

    CosmoTContenutoDocumento contenuto = creaContenutoDocumentoLink(infoFile, documento);
    if (contenuto.getFormatoFile() != null
        && Boolean.FALSE.equals(contenuto.getFormatoFile().getUploadConsentito())) {
      throw new BadRequestException("Il tipo del documento fornito non e' consentito.");
    }

    documentoDaSalvare = cosmoTDocumentoRepository.save(documentoDaSalvare);

    contenuto.setDocumentoPadre(documentoDaSalvare);
    try (var linkIS = new URL(documento.getLink()).openStream()) {
      contenuto.setShaFile(contenutoDocumentoService.generaSha256PerFile(linkIS.readAllBytes()));
    } catch (NotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new BadRequestException( "Errore nel " + methodName + e.getMessage());
    }
    cosmoTContenutoDocumentoRepository.save(contenuto);

    logger.debug(methodName , "documento con id {} inserito da fruitore esterno",
        documentoDaSalvare.getId());

    return documentoDaSalvare;

  }

  /**
   * Esegue la validazione dei documenti da importare tramite link
   *
   *
   * @param documenti rappresenta la lista dei documenti da importare tramite link
   */
  private void validaDocumentiLink(List<CreaDocumentoLinkFruitoreRequest> documenti) {

    String methodName = "validaDocumentiLink";
    documenti.stream().forEach(ldl -> {

      int responseCode = 0;
      try {
        URL url = new URL(ldl.getLink());
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        responseCode = huc.getResponseCode();
      } catch (IOException e) {
        e.printStackTrace();
      }
      if (responseCode != HttpURLConnection.HTTP_OK) {
        String message = "Il link " + ldl.getLink() + " non e' raggiungibile, quindi non verra' effettuata l'importazione";
        logger.debug(methodName, message);
        throw new BadRequestException(message);
      }
    });

  }

  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private Optional<CosmoTEndpointFruitore> verificaEsistenzaEndpoint(OperazioneFruitore operazione,
      Long idFruitore, String codiceDescrittivo) {
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

  private void validaTipoDocumento(CosmoTPratica pratica, CosmoDTipoDocumento tipoDocumento,
      String idDocumento, CosmoTDocumento docPadre, Long fileSize, InputStream content) {
    final var method = "validaTipoDocumento";
    // controllo che il tipo documento sia tra quelli presenti per la tipologia di pratica
    var relTipiDocTipoPratica = cosmoRTipoDocTipoPraticaRepository.findActiveByField(CosmoRTipodocTipopratica_.cosmoDTipoPratica, pratica.getTipo());
    var findTipoDocInTipoPratica = relTipiDocTipoPratica.stream().anyMatch(
        p -> p.getCosmoDTipoDocumento().getCodice().equalsIgnoreCase(tipoDocumento.getCodice()));
    if (!findTipoDocInTipoPratica) {
      throw new NotFoundException(
          String.format(ErrorMessages.D_TIPO_DOCUMENTO_TIPO_PRATICA_NON_ESISTENTE, tipoDocumento.getCodice(), pratica.getTipo().getCodice()));
    }

    // controllo la consistenza del tipo documento passato, sia esso un documento principale o un allegato

    if (null != docPadre && null != docPadre.getId()) {
      CosmoRTipoDocumentoTipoDocumentoPK pkRelTipoDoc = new CosmoRTipoDocumentoTipoDocumentoPK();
      pkRelTipoDoc.setCodiceAllegato(tipoDocumento.getCodice());
      pkRelTipoDoc.setCodicePadre(docPadre.getTipo().getCodice());
      pkRelTipoDoc.setCodiceTipoPratica(pratica.getTipo().getCodice());
      var tipoDocAllegato = cosmoRTipoDocumentoTipoDocumentoRepository
          .findOneActive(pkRelTipoDoc);
      if (tipoDocAllegato.isEmpty()) {
        var msgFormatted = String.format(ErrorMessages.D_RELAZIONE_TIPO_DOC_NON_TROVATA,
            docPadre.getTipo().getCodice(), tipoDocumento.getCodice());
        logger.error(method, msgFormatted);
        throw new NotFoundException(msgFormatted);
      }
    } else {
      var relazioniTipiDocumento = cosmoRTipoDocumentoTipoDocumentoRepository.findActiveByField(
          CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica, pratica.getTipo());
      var findAllegatoInRelazione =
          relazioniTipiDocumento.stream().filter(p -> p.getCosmoDTipoDocumentoAllegato().getCodice()
              .equalsIgnoreCase(tipoDocumento.getCodice())).findFirst();
      if (findAllegatoInRelazione.isPresent()) {
        throw new NotFoundException(String.format(
            ErrorMessages.D_TIPO_DOCUMENTO_ALLEGATO_TIPO_PRATICA, tipoDocumento.getCodice(),
            findAllegatoInRelazione.get().getCosmoDTipoDocumentoPadre().getCodice()));
      }
    }

    // controllo dimensione massima documento per tipologia
    contenutoDocumentoService.validaDimensioneDocumento(tipoDocumento, fileSize);
    // controllo documento per protocollazione
    try {
      verificaTipoDocumentoPerProtocollazione(tipoDocumento, content);
    } catch (IOException e) {
      throw new ConflictException(
          String.format(ErrorMessages.D_LETTURA_MIME_TYPE_ERRATA, idDocumento));
    }
  }

  private void verificaTipoDocumentoPerProtocollazione(CosmoDTipoDocumento tipoDocumento, InputStream fileContent) throws IOException {
    final var method = "verificaTipoDocumentoPerProtocollazione";
    if (null != tipoDocumento.getCodiceStardas() && !tipoDocumento.getCosmoRFormatoFileTipoDocumentos().isEmpty()) {
      TikaInputStream tikaIS = TikaInputStream.get(fileContent);
      var tikaDetector = TikaDTO.getInstance().detect(tikaIS);
      var formatoFileMatch = tipoDocumento.getCosmoRFormatoFileTipoDocumentos().stream()
          .anyMatch(formatoFile -> formatoFile.getCosmoDFormatoFile().getMimeType()
              .equalsIgnoreCase(tikaDetector));

      if (!formatoFileMatch) {
        logger.error(method, ErrorMessages.D_TIPO_DOC_PROTO_MODIF);
        throw new NotFoundException(ErrorMessages.D_TIPO_DOC_PROTO_MODIF);
      }
    }
  }

}
