/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoDEsitoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTAnteprimaContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.MailService;
import it.csi.cosmo.cosmoecm.business.service.StreamDataToIndexService;
import it.csi.cosmo.cosmoecm.business.service.ThumbnailService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.dto.exception.UnknownSignatureException;
import it.csi.cosmo.cosmoecm.dto.index2.IndexContentDisposition;
import it.csi.cosmo.cosmoecm.dto.index2.IndexShareScope;
import it.csi.cosmo.cosmoecm.dto.index2.RisultatoMigrazioneDocumento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoFirmaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTAnteprimaContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;

/**
 *
 */

@Service
public class StreamDataToIndexServiceImpl implements StreamDataToIndexService {

  private static final long MAX_CONTENT_SIZE_FOR_ANTEPRIME = 50 * 1024 * 1024L;

  protected static final String PATH_SEPARATOR = "/";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoDTipoFirmaRepository cosmoDTipoFirmaRepository;

  @Autowired
  private ThumbnailService thumbnailService;

  @Autowired
  protected MailService mailService;

  @Autowired
  private CosmoTAnteprimaContenutoDocumentoRepository anteprimaContenutoDocumentoRepository;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;


  @Override
  public RisultatoMigrazioneDocumento migraDocumento(CosmoTDocumento daMigrare, String link) {
    StreamDataToIndexTransferContext streamDataContext = new StreamDataToIndexTransferContext();

    streamDataContext.idDocumento = daMigrare.getId();

    var res = elaboraDocumento(streamDataContext, link);

    RisultatoMigrazioneDocumento output = new RisultatoMigrazioneDocumento();

    var mapped = RisultatoMigrazioneDocumento.builder()
        .withDocumento(cosmoTDocumentoRepository.findOne(daMigrare.getId()))
        .withSuccesso(res.success()).withErrore(res.getError()).build();

    output = mapped;

    if (!streamDataContext.errori.isEmpty()) {
      notificaErrori(streamDataContext);
    }

    return output;
  }

  private TransactionExecutionResult<?> elaboraDocumento(
      StreamDataToIndexTransferContext streamDataContext, String link) {

    Long idDocumentoDaMigrare = streamDataContext.idDocumento;

    final var method = "elaboraDocumento";
    logger.info(method, "elaborazione documento {} avviata", idDocumentoDaMigrare);

    // in caso di fallimento al primo tentativo, viene chiamata la rollback
    // su
    // tutta la procedura

    logger.info(method, "Inizio tentaVerifichePreliminari");
    var result = tentaVerifichePreliminari(streamDataContext);
    if (result.failed()) {
      return result;
    }
    logger.info(method, "Fine tentaVerifichePreliminari");

    logger.info(method, "Inizio tentaMigrazioneDocumento");
    result = tentaMigrazioneDocumento(streamDataContext, link);
    if (result.failed()) {
      return result;
    }
    logger.info(method, "Fine tentaMigrazioneDocumento");

    logger.info(method, "Inizio tentaAnalisiContenuto");
    result = tentaAnalisiContenuto(streamDataContext);
    if (result.failed()) {
      return result;
    }
    logger.info(method, "Fine tentaAnalisiContenuto");

    logger.info(method, "Inizio tentaVerificaFirma");
    result = tentaVerificaFirma(streamDataContext);
    if (result.failed()) {
      return result;
    }
    logger.info(method, "Fine tentaVerificaFirma");

    logger.info(method, "Inizio tentaElaborazioneContenuto");
    result = tentaElaborazioneContenuto(streamDataContext);
    if (result.failed()) {
      return result;
    }
    logger.info(method, "Fine tentaElaborazioneContenuto");

    tentaGenerazioneAnteprime(streamDataContext, link);


    logger.info(method, "Inizio terminaElaborazioneDocumento");
    terminaElaborazioneDocumento(streamDataContext);
    logger.info(method, "Fine terminaElaborazioneDocumento");

    logger.info(method, "Inizio tentaGenerazioneUrlDownload");
    tentaGenerazioneUrlDownload(streamDataContext);
    logger.info(method, "Fine tentaGenerazioneUrlDownload");

    return TransactionExecutionResult.<Void>forSuccess(null);
  }

  private TransactionExecutionResult<?> tentaVerifichePreliminari(
      StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaVerifichePreliminari";
    logger.info(method, "elaborazione documento {}: avvio verifiche preliminari", idDocumento);

    var result = inTransaction(() -> verifichePreliminari(streamDataContext)

        );

    if (result.failed()) {
      logger.error(method, "errore nelle verifiche preliminari per il documento " + idDocumento
          + ": " + result.getError().getMessage(), result.getError());

      streamDataContext.errori.put(idDocumento, result.getError());

    }

    logger.info(method, "elaborazione documento {}: terminate verifiche preliminari", idDocumento);
    return result;
  }

  private CosmoTPratica verifichePreliminari(StreamDataToIndexTransferContext streamDataContext) {

    verificaEsistenzaNodoPratica(streamDataContext);

    return verificaEsistenzaNodoAnteprime(streamDataContext);
  }

  private synchronized CosmoTPratica verificaEsistenzaNodoPratica(
      StreamDataToIndexTransferContext streamDataContext) {
    final var method = "verificaEsistenzaNodoPratica";

    Long idDocumento = streamDataContext.idDocumento;

    var documento = cosmoTDocumentoRepository.findOne(idDocumento);
    if (null == documento) {
      var msgFormatted = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, idDocumento);
      logger.error(method, msgFormatted);
      throw new NotFoundException(msgFormatted);
    }
    var praticaDoc = documento.getPratica();

    if (StringUtils.isBlank(praticaDoc.getUuidNodo())) {
      logger.info(method, "il nodo della pratica {} non esiste su index, lo creo",
          praticaDoc.getId());

      try {
        String path =
            require(praticaDoc.getEnte().getCodiceIpa(), "pratica.codiceIpaEnte") + PATH_SEPARATOR
            + require(praticaDoc.getDataCreazionePratica(), "pratica.dataCreazionePratica")
            .toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
            + PATH_SEPARATOR + praticaDoc.getId();

        logger.info(method,
            "il nodo della pratica {} non esiste su index, richiedo creazione del percorso {}",
            praticaDoc.getId(), path);

        var praticaFolderUUID = indexFeignClient.createFolder(path);

        logger.info(method, "il nodo della pratica {} e' stato creato al percorso {} con uuid {}",
            praticaDoc.getId(), path, praticaFolderUUID);

        var pratica =
            cosmoTPraticaRepository.findWithLockingByIdAndDtCancellazioneIsNull(praticaDoc.getId());
        pratica.setUuidNodo(praticaFolderUUID);
        praticaDoc = cosmoTPraticaRepository.save(pratica);
      } catch (Exception e) {
        String message = "Errore nella creazione del nodo su index per la pratica "
            + praticaDoc.getId() + ": " + e.getMessage();
        logger.error(method, message, e);
        throw new UnexpectedResponseException(message, e);
      }
    }
    return praticaDoc;
  }

  private synchronized CosmoTPratica verificaEsistenzaNodoAnteprime(
      StreamDataToIndexTransferContext streamDataContext) {
    final var method = "verificaEsistenzaNodoAnteprime";

    Long idDocumento = streamDataContext.idDocumento;

    var documento = cosmoTDocumentoRepository.findOne(idDocumento);
    var pratica = documento.getPratica();

    if (streamDataContext.nodiAnteprime.containsKey(pratica.getId())) {
      return pratica;
    }

    if (StringUtils.isBlank(pratica.getUuidNodo())) {
      throw new UnexpectedResponseException("il nodo della pratica non esiste su index");
    }

    var nodoPratica = indexFeignClient.findFolder(pratica.getUuidNodo());
    String path = nodoPratica.getEffectivePath();
    String nodoPath = nodoPratica.getEffectivePath() + "/anteprime";
    var nodoAnteprime = indexFeignClient.findFolder(nodoPath);

    if (nodoAnteprime != null) {
      streamDataContext.nodiAnteprime.put(pratica.getId(), nodoAnteprime.getUid());

    } else {
      try {
        logger.info(method,
            "il nodo delle anteprime per la pratica {} non esiste su index, richiedo creazione del percorso {}",
            pratica.getId(), nodoPath);

        var folderUUID = indexFeignClient.createFolder(nodoPath);
        streamDataContext.nodiAnteprime.put(pratica.getId(), folderUUID);

        logger.info(method,
            "il nodo delle anteprime per la pratica e' {} stato creato al percorso {} con uuid {}",
            pratica.getId(), path, folderUUID);

      } catch (Exception e) {
        String message = "Errore nella creazione del nodo su index per le anteprime per la pratica "
            + pratica.getId() + ": " + e.getMessage();
        logger.error(method, message, e);
        throw new UnexpectedResponseException(message, e);
      }
    }
    return pratica;
  }

  private TransactionExecutionResult<?> tentaMigrazioneDocumento(
      StreamDataToIndexTransferContext streamDataContext, String link) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaMigrazioneDocumento";
    logger.info(method, "elaborazione documento {}: avvio migrazione su index", idDocumento);

    var result = inTransaction(() -> migraDocumento(streamDataContext, link));

    if (result.failed()) {
      logger.error(method, "errore nella migrazione del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      streamDataContext.errori.put(idDocumento, result.getError());

    }

    logger.info(method, "elaborazione documento {}: terminata migrazione su index", idDocumento);
    return result;
  }

  private CosmoTDocumento migraDocumento(StreamDataToIndexTransferContext streamDataContext,
      String link) {

    Long idDocumento = streamDataContext.idDocumento;

    String methodName = "migraDocumento";
    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    if (documentoDB.getContenuti().isEmpty()) {
      throw new UnexpectedResponseException(
          "Impossibile verificare la locazione del contenuto del documento (nessun contenuto)");
    }


    if (documentoDB.hasContenuto(TipoContenutoDocumento.ORIGINALE)) {

      logger.info(methodName, "salto la migrazione del documento " + documentoDB.getId()
      + " perche' si trova gia' su index");

    } else {

      var contenutoTemporaneo = documentoDB.findContenuto(TipoContenutoDocumento.TEMPORANEO);
      if (contenutoTemporaneo == null) {
        throw new UnexpectedResponseException(
            "Impossibile localizzare il contenuto temporaneo per il documento "
                + documentoDB.getId());
      }

      logger.info(methodName, "avvio migrazione del documento {} dal contenuto temporaneo {}",
          documentoDB.getId(), contenutoTemporaneo.getId());

      CosmoTPratica pratica = contenutoTemporaneo.getDocumentoPadre().getPratica();

      CreaFileRequest cfr = new CreaFileRequest();
      cfr.setUuidPratica(pratica.getUuidNodo());
      cfr.setUuidContenutoTemporaneo(contenutoTemporaneo.getUuidNodo());
      cfr.setDtInserimento(documentoDB.getDtInserimento().toLocalDateTime().toLocalDate());
      cfr.setDescrizione(contenutoTemporaneo.getDocumentoPadre().getDescrizione());
      cfr.setId(contenutoTemporaneo.getDocumentoPadre().getId());
      cfr.setLink(link);
      cfr.setCodiceTipo(null != contenutoTemporaneo.getDocumentoPadre().getTipo()
          ? contenutoTemporaneo.getDocumentoPadre().getTipo().getCodice()
              : null);

      Entity createdOnIndex = indexFeignClient.creaFileIndex(cfr);

      logger.info(methodName, "documento su index creato");

      var creato = contenutoDocumentoService.creaContenutoOriginaleDaStreaming(contenutoTemporaneo,
          createdOnIndex);

      creato.setDocumentoPadre(documentoDB);
      creato.setShaFile(contenutoDocumentoService.generaSha256PerFile(createdOnIndex.getContent()));
      creato = cosmoTContenutoDocumentoRepository.save(creato);

      logger.info(methodName,
          "migrato documento da stream di dati a index -> " + creato.getUuidNodo());
    }

    return documentoDB;
  }

  private TransactionExecutionResult<?> tentaAnalisiContenuto(
      StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaAnalisiContenuto";
    logger.info(method, "elaborazione documento {}: avvio analisi contenuto", idDocumento);

    var result = inTransaction(() -> analizzaContenuto(streamDataContext));

    if (result.failed()) {
      logger.error(method, "errore nella analisi del contenuto del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      streamDataContext.errori.put(idDocumento, result.getError());

    }

    logger.info(method, "elaborazione documento {}: terminata analisi contenuto", idDocumento);
    return result;
  }

  private CosmoTDocumento analizzaContenuto(StreamDataToIndexTransferContext streamDataContext) {
    final var method = "analizzaContenuto";

    Long idDocumento = streamDataContext.idDocumento;

    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var msgFormatted = String.format(
          ErrorMessages.SD2IDX_IMPOSSIBILE_RECUPERARE_CONTENUTO_SU_INDEX, documentoDB.getId());
      logger.error(method, msgFormatted);
      throw new UnexpectedResponseException(msgFormatted);
    }

    logger.debug(method, "avvio analisi contenuto del documento {} con index UID {}",
        documentoDB.getId(), contenutoIndex.getUuidNodo());

    var fileInfo = indexFeignClient.getInfoFormatoFile(contenutoIndex.getUuidNodo());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato analisi contenuto documento {}: {}", documentoDB.getId(),
          ObjectUtils.represent(fileInfo));
    }

    if (fileInfo.getMimeType() != null && fileInfo.getMimeType().isEmpty()) {
      String validMime = fileInfo.getMimeType().get(0);
      logger.info(method, "individuato content-type del documento {} come {}", documentoDB.getId(),
          validMime);
      var nuovoFormato = contenutoDocumentoService.findFormatoByMime(validMime);
      logger.info(method, "il nuovo formato del documento {} e' {}", documentoDB.getId(),
          nuovoFormato);
      contenutoIndex.setFormatoFile(nuovoFormato);
    }

    if (fileInfo.isSigned()) {
      logger.info(method, "individuata firma del documento {} di tipo {}", documentoDB.getId(),
          fileInfo.getSignatureType());

      if (StringUtils.isBlank(fileInfo.getSignatureType())) {
        throw new UnexpectedResponseException(
            "Documento marcato come firmato ma tipo di firma non rilevato");
      }

      // lettura del tipo firma dalla tabella OR UnknownSignatureException
      CosmoDTipoFirma tipoFirma = cosmoDTipoFirmaRepository.findOne(fileInfo.getSignatureType());
      if (tipoFirma == null) {
        throw new UnknownSignatureException(
            "Tipologia di firma " + fileInfo.getSignatureType() + " non gestita");
      }

      contenutoIndex.setTipoFirma(tipoFirma);

      var esitoVerificaFirma = new CosmoDEsitoVerificaFirma();
      esitoVerificaFirma.setCodice(EsitoVerificaFirma.NON_VERIFICATA.name());
      contenutoIndex.setEsitoVerificaFirma(esitoVerificaFirma);

    } else {
      logger.debug(method, "il documento {} non risulta firmato", documentoDB.getId());
    }

    cosmoTContenutoDocumentoRepository.save(contenutoIndex);
    cosmoTDocumentoRepository.save(documentoDB);

    return documentoDB;
  }

  private TransactionExecutionResult<?> tentaVerificaFirma(
      StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaVerificaFirma";
    logger.info(method, "elaborazione documento {}: avvio verifica della firma", idDocumento);

    var result = inTransaction(() -> verificaFirma(streamDataContext));

    if (result.failed()) {
      logger.error(method, "errore nella verifica della firma del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      streamDataContext.errori.put(idDocumento, result.getError());

    }

    logger.info(method, "elaborazione documento {}: terminata verifica della firma", idDocumento);
    return result;
  }

  private CosmoTDocumento verificaFirma(StreamDataToIndexTransferContext streamDataContext) {
    final var method = "verificaFirma";

    Long idDocumento = streamDataContext.idDocumento;

    // ri-leggo da DB perche' probabilmente e' cambiato qualcosa dallo step precedente
    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var msgFormatted = String.format(
          ErrorMessages.SD2IDX_IMPOSSIBILE_RECUPERARE_CONTENUTO_SU_INDEX, documentoDB.getId());
      logger.error(method, msgFormatted);
      throw new UnexpectedResponseException(msgFormatted);
    }

    if (contenutoIndex.getTipoFirma() == null) {
      logger.debug(method,
          "nessuna firma da verificare per il contenuto del documento {} con index UID {}",
          documentoDB.getId(), contenutoIndex.getUuidNodo());
      return documentoDB;
    }

    logger.debug(method, "avvio verifica firma contenuto del documento {} con index UID {}",
        documentoDB.getId(), contenutoIndex.getUuidNodo());

    contenutoDocumentoService.verificaFirma(contenutoIndex);

    logger.debug(method, "terminata verifica firma contenuto del documento {} con index UID {}",
        documentoDB.getId(), contenutoIndex.getUuidNodo());
    return documentoDB;
  }

  private TransactionExecutionResult<?> tentaElaborazioneContenuto(
      StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaElaborazioneContenuto";
    logger.info(method, "elaborazione documento {}: avvio elaborazione del contenuto", idDocumento);

    var result = inTransaction(() -> elaboraContenuto(streamDataContext));

    if (result.failed()) {
      logger.error(method, "errore nell'elaborazione del contenuto del documento " + idDocumento
          + ": " + result.getError().getMessage(), result.getError());

      streamDataContext.errori.put(idDocumento, result.getError());

    }

    logger.info(method, "elaborazione documento {}: terminata elaborazione del contenuto",
        idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaGenerazioneAnteprime(
      StreamDataToIndexTransferContext streamDataContext, String link) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaGenerazioneAnteprime";
    logger.info(method, "elaborazione documento {}: avvio generazione anteprime", idDocumento);

    var result = inTransaction(() -> generaAnteprime(streamDataContext, link));

    if (result.failed()) {
      logger.error(method, "errore nella generazione anteprime del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());
    }

    logger.info(method, "elaborazione documento {}: terminata generazione anteprime", idDocumento);
    return result;
  }

  private CosmoTDocumento generaAnteprime(StreamDataToIndexTransferContext streamDataContext,
      String link) {
    final var method = "generaAnteprime";

    Long idDocumento = streamDataContext.idDocumento;

    var documentoDB = this.cosmoTDocumentoRepository.findOne(idDocumento);
    if (documentoDB == null) {
      throw new UnexpectedResponseException(
          "Impossibile ottenere il documento originale da cui generare le anteprime");
    }

    logger.info(method, "documento {} - avvio generazione anteprime per contenuto originale",
        idDocumento);
    CosmoTContenutoDocumento contenutoOriginale =
        documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoOriginale == null) {
      throw new UnexpectedResponseException("Impossibile ottenere il contenuto documento su index");
    }


    generaAnteprimaPerContenuto(contenutoOriginale, link, streamDataContext);

    CosmoTContenutoDocumento contenutoSbustato =
        documentoDB.findContenuto(TipoContenutoDocumento.SBUSTATO);
    if (contenutoSbustato != null) {
      generaAnteprimaPerContenuto(contenutoSbustato, null, streamDataContext);
    }

    return documentoDB;
  }

  private boolean troppoGrossoPerGenerareAnteprima(CosmoTContenutoDocumento contenuto,
      long linkFileSize) {
    return (linkFileSize > MAX_CONTENT_SIZE_FOR_ANTEPRIME)
        || (contenuto.getDimensione() != null
        && contenuto.getDimensione() > MAX_CONTENT_SIZE_FOR_ANTEPRIME);
  }

  private void generaAnteprimaPerContenuto(CosmoTContenutoDocumento contenuto, String link,
      StreamDataToIndexTransferContext streamDataContext) {

    final var method = "generaAnteprimaPerContenuto";
    if (contenuto == null) {
      throw new UnexpectedResponseException("Impossibile ottenere il contenuto");
    }

    logger.info(method,
        "contenuto documento {} {} - avvio generazione anteprime per contenuto originale",
        contenuto.getId(), contenuto.getTipo().getCodice());

    String mimeType = null;

    URL obj = null;
    HttpURLConnection conn = null;
    try {
      obj = new URL(link);
      conn = (HttpURLConnection) obj.openConnection();
    } catch (Exception e) {
      throw new UnexpectedResponseException(
          "Impossibile ottenere il documento originale da cui generare le anteprime");
    }

    if (troppoGrossoPerGenerareAnteprima(contenuto, conn.getContentLengthLong())) {
      logger.warn(method,
          "contenuto documento {} {} - salto generazione anteprime per contenuto originale perche' roppo grosso",
          contenuto.getId(), contenuto.getTipo().getCodice());
      return;
    }

    if (contenuto.getFormatoFile() != null) {
      mimeType = contenuto.getFormatoFile().getMimeType();
    } else if (conn.getContentType() != null) {
      mimeType = conn.getContentType();
    } else {
      throw new UnexpectedResponseException(
          "Nessuna sorgente di content type per il contenuto " + contenuto.getId());
    }

    final String finalMimeType = mimeType;

    if (thumbnailService.possibileGenerazioneThumbnail(mimeType)) {
      InputStream arbInput = null;
      String filename = conn.getHeaderField("Content-Disposition");
      if (filename == null) {
        filename = UUID.randomUUID().toString();
      }

      Entity entity = null;
      if (!StringUtils.isBlank(contenuto.getUuidNodo())) {
        entity = indexFeignClient.getFile(contenuto.getUuidNodo(), true);
        contenuto.setShaFile(contenutoDocumentoService.generaSha256PerFile(entity.getContent()));
        filename = entity.getFilename();
      }

      try {
        if (conn.getInputStream() != null) {
          arbInput = conn.getInputStream();
        }
      } catch (IOException e) {
        logger.error(method, e.getMessage());
      }
      if (arbInput == null) {
        if (entity != null) {
          arbInput = new ByteArrayInputStream(entity.getContent());

        } else {
          throw new UnexpectedResponseException("Nessun contenuto per l'anteprima");
        }
      }

      final String finalFilename = filename;
      final InputStream finalInput = arbInput;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      TransactionExecutionResult<Boolean> risultatoGenerazione =
          attempt(() -> thumbnailService.generaThumbnail(finalInput, finalMimeType, outputStream));

      if (risultatoGenerazione.failed()) {
        logger.error(method,
            "contenuto documento " + contenuto.getId()
            + " - errore nella generazione anteprima per contenuto originale",
            risultatoGenerazione.getError());
      } else if (risultatoGenerazione.getResult().booleanValue()) {
        logger.info(method, "contenuto documento {} - generata anteprima per contenuto originale",
            contenuto.getId());
        salvaAnteprimaContenuto(contenuto, finalFilename, outputStream, streamDataContext);
      } else {
        logger.debug(method,
            "contenuto documento {} - nessuna anteprima generata per contenuto originale",
            contenuto.getId());
      }
    }

    // salvo flag di anteprima generata in ogni caso
    this.cosmoTContenutoDocumentoRepository.save(contenuto);
  }


  private CosmoTAnteprimaContenutoDocumento salvaAnteprimaContenuto(
      CosmoTContenutoDocumento contenuto, String filenameIndex, ByteArrayOutputStream outputStream,
      StreamDataToIndexTransferContext streamDataContext) {


    final var method = "salvaAnteprimaContenuto";
    logger.info(method, "salvo l'anteprima generata per il contenuto {}", contenuto.getId());

    byte[] bytes = outputStream.toByteArray();

    String filename = contenuto.getNomeFile() + "_preview.jpg";

    // create index entity
    Entity indexEntity = buildDocumentoIndexAnteprima(contenuto, filenameIndex);

    // upload entity on index
    String container =
        streamDataContext.nodiAnteprime.get(contenuto.getDocumentoPadre().getPratica().getId());

    ContenutoEntity contenutoEntity = new ContenutoEntity();
    contenutoEntity.setContent(bytes);
    contenutoEntity.setEntity(indexEntity);
    Entity createdOnIndex = indexFeignClient.creaFile(container, contenutoEntity);

    ShareOptions options = new ShareOptions();
    options.setFilename(filename);
    options.setSource(IndexShareScope.INTERNET.name());
    CondivisioniRequest request = new CondivisioniRequest();
    request.setEntity(createdOnIndex);
    request.setOptions(options);

    var share = indexFeignClient.share(request);

    CosmoTAnteprimaContenutoDocumento entity = new CosmoTAnteprimaContenutoDocumento();

    entity.setContenuto(contenuto);
    entity.setDimensione(Long.valueOf(bytes.length));
    entity.setNomeFile(filename);
    entity.setUuidNodo(createdOnIndex.getUid());
    entity.setFormatoFile(
        cosmoDFormatoFileRepository.findByMimeType(ThumbnailServiceImpl.THUMBNAIL_CONTENT_TYPE));
    entity.setShareUrl(share.getDownloadUri());

    entity = anteprimaContenutoDocumentoRepository.save(entity);

    logger.info(method, "migrata anteprima contenuto documento " + contenuto.getId() + " -> index "
        + entity.getUuidNodo());

    return entity;
  }

  private Entity buildDocumentoIndexAnteprima(CosmoTContenutoDocumento contenuto,
      String filenameIndex) {
    Entity documentoIndex = new Entity();

    documentoIndex.setFilename(filenameIndex != null ? "thumb-" + filenameIndex + ".jpg"
        : "thumb-" + contenuto.getNomeFile() + ".jpg");
    documentoIndex.setMimeType(ThumbnailServiceImpl.THUMBNAIL_CONTENT_TYPE);

    return documentoIndex;
  }

  private CosmoTDocumento elaboraContenuto(StreamDataToIndexTransferContext streamDataContext) {
    final var method = "elaboraContenuto";

    Long idDocumento = streamDataContext.idDocumento;

    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var msgFormatted = String.format(
          ErrorMessages.SD2IDX_IMPOSSIBILE_RECUPERARE_CONTENUTO_SU_INDEX, documentoDB.getId());
      logger.error(method, msgFormatted);
      throw new UnexpectedResponseException(msgFormatted);
    }

    logger.debug(method, "avvio elaborazione del contenuto del documento {} con index UID {}",
        documentoDB.getId(), contenutoIndex.getUuidNodo());

    if (contenutoIndex.getTipoFirma() == null) {
      logger.debug(method,
          "il documento {} non risulta firmato, quindi non sono necessarie elaborazioni",
          documentoDB.getId());
      return documentoDB;

    } else if (!Boolean.TRUE.equals(contenutoIndex.getTipoFirma().getEstraibile())) {
      logger.debug(method,
          "il documento {} risulta firmato in un formato non estraibile ({}), quindi non sono necessarie elaborazioni",
          documentoDB.getId(), contenutoIndex.getTipoFirma().getCodice());
      return documentoDB;

    }

    logger.info(method,
        "il documento {} risulta firmato in formato estraibile ({}), avvio sbustamento su Index",
        documentoDB.getId(), contenutoIndex.getTipoFirma().getCodice());

    String cartellaDestinazioneContenutoEstratto = documentoDB.getPratica().getUuidNodo();
    if (StringUtils.isBlank(cartellaDestinazioneContenutoEstratto)) {
      throw new InvalidParameterException("Nodo della pratica non specificato");
    }

    Entity documentoIndex = indexFeignClient.getFile(contenutoIndex.getUuidNodo(), false);
    if (documentoIndex == null) {
      throw new UnexpectedResponseException("Impossibile reperire il documento "
          + contenutoIndex.getUuidNodo() + " caricato su index");
    }

    String targetContainerUUID = documentoDB.getPratica().getUuidNodo();

    logger.debug(method, "avvio sbustamento dal documento {} nella cartella {}",
        documentoIndex.getUid(), targetContainerUUID);

    var targetContent = indexFeignClient.estraiBusta(targetContainerUUID, documentoIndex);

    logger.info(method, "sbustamento dal documento {} al documento {} completato con successo",
        documentoIndex.getUid(), targetContent.getUid());

    var contenutoSbustato =
        contenutoDocumentoService.creaContenutoSbustato(contenutoIndex, targetContent);
    contenutoSbustato.setDocumentoPadre(documentoDB);

    analizzaFileSbustato(documentoDB, contenutoIndex, contenutoSbustato, targetContent);
    contenutoSbustato.setShaFile(contenutoDocumentoService.generaSha256PerFile(targetContent.getContent()));
    cosmoTContenutoDocumentoRepository.save(contenutoSbustato);

    return documentoDB;
  }

  private void analizzaFileSbustato(CosmoTDocumento documentoDB,
      CosmoTContenutoDocumento contenutoOriginale, CosmoTContenutoDocumento contenutoSbustato,
      Entity fileSbustato) {
    final var method = "analizzaFileSbustato";
    logger.debug(method, "avvio analisi del documento sbustato {} per il documento {}",
        fileSbustato.getUid(), documentoDB.getId());

    var fileInfo = indexFeignClient.getInfoFormatoFile(fileSbustato.getUid());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato analisi contenuto file sbustato {}: {}", documentoDB.getId(),
          ObjectUtils.represent(fileInfo));
    }

    if (fileInfo.getMimeType() != null && fileInfo.getMimeType().isEmpty()) {
      String validMime = fileInfo.getMimeType().get(0);
      logger.info(method, "individuato content-type del documento sbustato {} come {}",
          documentoDB.getId(), validMime);
      contenutoSbustato.setFormatoFile(contenutoDocumentoService.findFormatoByMime(validMime));
    } else {
      logger.warn(method,
          "non e' stato possibile individuare il content-type del documento sbustato {}",
          documentoDB.getId());
      contenutoSbustato
      .setFormatoFile(contenutoDocumentoService.findFormatoByMime("application/octet-stream"));
    }

    String nomeFileEstratto = contenutoOriginale.getNomeFile();

    nomeFileEstratto = nomeFileEstratto + "-estratto";

    if (!StringUtils.isBlank(contenutoSbustato.getFormatoFile().getEstensioneDefault())) {
      nomeFileEstratto =
          nomeFileEstratto + "." + contenutoSbustato.getFormatoFile().getEstensioneDefault();
    } else if (!StringUtils.isBlank(fileInfo.getTypeExtension())) {
      nomeFileEstratto = nomeFileEstratto + "." + fileInfo.getTypeExtension();
    }

    contenutoSbustato.setNomeFile(nomeFileEstratto);
  }

  private TransactionExecutionResult<?> tentaGenerazioneUrlDownload(
      StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "tentaGenerazioneAnteprime";
    logger.info(method, "elaborazione documento {}: avvio generazione url di download pubblico",
        idDocumento);

    var result = inTransaction(() -> generaUrlDownloadPubblico(streamDataContext));

    if (result.failed()) {
      logger.error(method, "errore nella generazione url di download pubblico del documento "
          + idDocumento + ": " + result.getError().getMessage(), result.getError());
    }

    logger.info(method, "elaborazione documento {}: terminata generazione url di download pubblico",
        idDocumento);
    return result;
  }

  private CosmoTDocumento generaUrlDownloadPubblico(
      StreamDataToIndexTransferContext streamDataContext) {
    final var method = "generaUrlDownloadPubblico";

    Long idDocumento = streamDataContext.idDocumento;

    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    documentoDB.getContenuti().stream()
    .filter(c -> c.nonCancellato() && c.getTipo() != null
    && !c.getTipo().getCodice().equals(TipoContenutoDocumento.TEMPORANEO.name())
    && c.getUuidNodo() != null && StringUtils.isBlank(c.getUrlDownload()))
    .forEach(contenuto -> {

      ShareOptions options = new ShareOptions();
      options.setContentDisposition(IndexContentDisposition.INLINE.name());
      options.setFilename(contenuto.getNomeFile());
      options.setSource(IndexShareScope.INTERNET.name());

      CondivisioniRequest request = new CondivisioniRequest();
      request.setOptions(options);
      request.setSourceIdentifier(contenuto.getUuidNodo());

      logger.debug(method, "genero url di download pubblico per il contenuto {}",
          contenuto.getId());
      var share = indexFeignClient.share(request);
      contenuto.setUrlDownload(share.getDownloadUri());

      logger.debug(method, "generato url di download pubblico per il contenuto {}",
          contenuto.getId());
      cosmoTContenutoDocumentoRepository.save(contenuto);
    });

    return documentoDB;
  }

  private void terminaElaborazioneDocumento(StreamDataToIndexTransferContext streamDataContext) {

    Long idDocumento = streamDataContext.idDocumento;

    final var method = "terminaElaborazioneDocumento";
    logger.info(method, "elaborazione documento {}: marco come ELABORATO", idDocumento);

    updateStatoInTransazioneSeparata(streamDataContext, StatoDocumento.ELABORATO);

    logger.info(method, "elaborazione documento {}: terminata", idDocumento);
  }


  private TransactionExecutionResult<?> updateStatoInTransazioneSeparata(
      StreamDataToIndexTransferContext streamDataContext, StatoDocumento statoDaSettare) {
    final var method = "updateStato";

    Long idDocumento = streamDataContext.idDocumento;

    final var result = inTransaction(() -> updateStato(idDocumento, statoDaSettare));

    if (result.failed()) {
      logger.error(method, "errore nella marcatura del documento come " + statoDaSettare,
          result.getError());
    }

    return result;
  }

  public CosmoTDocumento updateStato(Long idDocumento, StatoDocumento statoDaSettare) {
    final var method = "updateStato";

    // ri-leggo da DB perche' probabilmente e' cambiato qualcosa dallo step precedente
    var documentoDB = cosmoTDocumentoRepository.findOne(idDocumento);

    if (documentoDB.getStato() != null
        && documentoDB.getStato().getCodice().equals(statoDaSettare.name())) {
      logger.info(method,
          "richiesto aggiornamento stato del documento {} che e' gia' nello stato {}",
          documentoDB.getId(), documentoDB.getStato().getCodice());

      return documentoDB;
    }

    CosmoDStatoDocumento stato = new CosmoDStatoDocumento();
    stato.setCodice(statoDaSettare.name());

    logger.info(method, "aggiorno stato del documento {} da {} a {}", documentoDB.getId(),
        documentoDB.getStato().getCodice(), stato.getCodice());

    documentoDB.setStato(stato);
    documentoDB.setDtUltimaModifica(Timestamp.from(Instant.now()));
    cosmoTDocumentoRepository.save(documentoDB);

    return documentoDB;
  }

  protected void notificaErrori(StreamDataToIndexTransferContext context) {
    String method = "notificaErrori";
    Map<Long, Throwable> errori = context.errori;

    logger.info(method,
        "invio notifica per " + errori.size() + " errori avvenuti durante la migrazione");

    StringBuilder builder = new StringBuilder();
    builder.append(
        "Si sono verificati degli errori durante il processo di migrazione da streaming ad Index.<br/>")
    .append("La migrazione e' fallita per i documenti seguenti: <br/><br/>");

    for (Long key : errori.keySet()) {
      builder.append(" - #" + key + "<br/>");
    }

    builder.append("<br/>Dettaglio degli errori: <br/>");

    for (Entry<Long, Throwable> entry : errori.entrySet()) {
      builder.append("Errore per #" + entry.getKey() + ":<br/>"
          + ExceptionUtils.exceptionToString(entry.getValue()).replaceAll("(\r\n|\n)", "<br />")
          + "<br/><br/>");
    }

    mailService.inviaMailAssistenza(
        "Errori nel processo di migrazione documento via streaming su Index", builder.toString());
  }

  protected <T> T require(T raw, String descrizione) {
    if (raw == null) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' nullo");
    }
    if (raw instanceof String && StringUtils.isBlank((String) raw)) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' vuoto");
    }
    return raw;
  }


  protected TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return transactionService.inTransaction(task);
  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

  // CONTEXT CLASSES

  protected static class StreamDataToIndexTransferContext {
    Map<Long, Throwable> errori = new HashMap<>();
    Map<Long, String> nodiAnteprime = new HashMap<>();
    Long idDocumento;
  }


}
