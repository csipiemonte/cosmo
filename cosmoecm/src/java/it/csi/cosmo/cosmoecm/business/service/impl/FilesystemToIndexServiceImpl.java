/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import javax.ws.rs.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoDEsitoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTAnteprimaContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmo.dto.rest.ws.WebSocketTargetSelector;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.EventService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.FilesystemToIndexService;
import it.csi.cosmo.cosmoecm.business.service.LockService;
import it.csi.cosmo.cosmoecm.business.service.MailService;
import it.csi.cosmo.cosmoecm.business.service.ThumbnailService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.config.Constants;
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
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.CreaFileRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;

/**
 *
 */

@Service
public class FilesystemToIndexServiceImpl implements FilesystemToIndexService {

  public static final String JOB_LOCK_RESOURCE_CODE = "FS2INDEX_JOB_LOCK";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final long MAX_CONTENT_SIZE_FOR_ANTEPRIME = 50 * 1024 * 1024L;

  protected static final String PATH_SEPARATOR = "/";

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private ThumbnailService thumbnailService;

  @Autowired
  protected MailService mailService;

  @Autowired
  protected TransactionService transactionService;

  @Autowired
  private EventService eventService;

  @Autowired
  private CosmoTDocumentoRepository documentoRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository contenutoDocumentoRepository;

  @Autowired
  private CosmoTPraticaRepository praticaRepository;

  @Autowired
  private CosmoDTipoFirmaRepository tipoFirmaRepository;

  @Autowired
  private CosmoTAnteprimaContenutoDocumentoRepository anteprimaContenutoDocumentoRepository;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;

  @Autowired
  private CosmoTEnteRepository enteRepository;

  @Autowired
  private LockService lockService;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient notificheFeignClient;

  @Override
  public List<CosmoTDocumento> findDaMigrare(int numMax, int maxRetries) {
    String methodName = "findDaMigrare";
    logger.debug(methodName, "ricerco documenti da migrare...");

    Pageable pageRequest =
        new PageRequest(0, numMax, new Sort(Direction.DESC, CosmoTDocumento_.id.getName()));

    List<CosmoTDocumento> daMigrare = documentoRepository
        .findAllNotDeleted(CosmoTDocumentoSpecifications.findByStato(StatoDocumento.ACQUISITO),
            pageRequest)
        .getContent();

    if (!daMigrare.isEmpty()) {
      logger.info(methodName, "trovati " + daMigrare.size() + " documenti da migrare");
    } else {
      logger.debug(methodName, "nessun documento da migrare");
    }

    if (daMigrare.isEmpty()) {
      // possibile fare retry
      return findDaRitentare(numMax, maxRetries);
    }

    return daMigrare;
  }

  @Override
  public RisultatoMigrazioneDocumento migraDocumento(CosmoTDocumento daMigrare) {
    var res = this.migraDocumenti(Arrays.asList(daMigrare));
    if (res.isEmpty()) {
      return null;
    }
    return res.get(0);
  }


  @Override
  public List<RisultatoMigrazioneDocumento> migraDocumenti(List<CosmoTDocumento> daMigrare) {
    return migraDocumenti(daMigrare, null);
  }

  @Override
  public List<RisultatoMigrazioneDocumento> migraDocumenti(List<CosmoTDocumento> daMigrare,
      BatchExecutionContext batchContext) {

    //@formatter:off
    return this.lockService.executeLocking(lock -> migraDocumentiInsideLock(lock, daMigrare, batchContext),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
  }

  @Override
  public List<RisultatoMigrazioneDocumento> migraDocumentiInsideLock(
      CosmoTLock lock,
      List<CosmoTDocumento> daMigrare, BatchExecutionContext batchContext) {

    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di migrazione senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di migrazione con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }

    FilesystemToIndexTransferContext context = newTransferContext();
    context.batchContext = batchContext;
    return migraDocumentiInternal(daMigrare, batchContext);
  }

  public CosmoTDocumento updateStato(Long idDocumento, StatoDocumento statoDaSettare) {
    final var method = "updateStato";

    // ri-leggo da DB perche' probabilmente e' cambiato qualcosa dallo step precedente
    var documentoDB = documentoRepository.findOne(idDocumento);
    if (null == documentoDB) {
      var msgFormatted = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, idDocumento);
      logger.error(method, msgFormatted);
      throw new NotFoundException(msgFormatted);
    }

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
    documentoRepository.save(documentoDB);

    return documentoDB;
  }

  public CosmoTDocumento registraTentativoAcquisizione(DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;

    final var method = "registraTentativoAcquisizione";
    logger.info(method, "registrazione tentativo di acquisizione per il documento {}", idDocumento);

    // ri-leggo da DB perche' probabilmente e' cambiato qualcosa dallo step precedente
    var documentoDB = documentoRepository.findOne(idDocumento);

    documentoDB.setNumeroTentativiAcquisizione((documentoDB.getNumeroTentativiAcquisizione() != null
        ? documentoDB.getNumeroTentativiAcquisizione()
            : 0) + 1);

    documentoRepository.save(documentoDB);

    return documentoDB;
  }

  public CosmoTPratica verifichePreliminariPratica(DocumentTransferContext documentContext) {

    return verificaEsistenzaNodoPratica(documentContext);

  }

  public CosmoTPratica verifichePreliminariAnteprima(DocumentTransferContext documentContext) {

    var pratica = verificaEsistenzaNodoAnteprime(documentContext);

    documentContext.transferContext.praticheElaborate.add(pratica.getId());

    return pratica;
  }

  public CosmoTDocumento migraDocumento(DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    String methodName = "migraDocumento";
    var documentoDB = documentoRepository.findOne(idDocumento);

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
      cfr.setCodiceTipo(null != contenutoTemporaneo.getDocumentoPadre().getTipo()
          ? contenutoTemporaneo.getDocumentoPadre().getTipo().getCodice()
              : null);


      Entity createdOnIndex = indexFeignClient.creaFileIndex(cfr);

      var creato =
          contenutoDocumentoService.creaContenutoOriginale(contenutoTemporaneo, createdOnIndex);

      RetrievedContent retrievedFile = fileShareService.get(contenutoTemporaneo.getUuidNodo(),
          documentoDB.getDtInserimento().toLocalDateTime().toLocalDate());

      // salvo il doc su filesystem per rimuoverlo in seguito
      context.documentiFilesystem.put(documentoDB.getId(), retrievedFile);

      creato.setDocumentoPadre(documentoDB);
      String shaFile = contenutoDocumentoService.generaSha256PerFile(retrievedFile);
      creato.setShaFile(shaFile);
      creato = contenutoDocumentoRepository.save(creato);

      logger.info(methodName, "migrato documento fileshare "
          + retrievedFile.getUploadUUID() + " -> index" + creato.getUuidNodo());
    }

    CosmoDStatoDocumento stato = new CosmoDStatoDocumento();
    stato.setCodice(StatoDocumento.IN_ELABORAZIONE.name());
    documentoDB.setStato(stato);

    documentoRepository.save(documentoDB);

    return documentoDB;
  }

  public CosmoTDocumento rimuoviContenutoTemporaneo(DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;

    final var method = "rimuoviContenutoTemporaneo";

    var documentoDB = documentoRepository.findOne(idDocumento);

    var contenutoTemporaneo = documentoDB.findContenuto(TipoContenutoDocumento.TEMPORANEO);

    if (contenutoTemporaneo != null) {
      logger.info(method, "cancello il contenuto temporaneo {} del documento {}",
          contenutoTemporaneo.getId(), documentoDB.getId());
      contenutoDocumentoService.cancella(contenutoTemporaneo, true);
    }

    logger.info(method, "terminata rimozione contenuto temporaneo documento {}", idDocumento);
    return documentoDB;
  }

  public CosmoTDocumento analizzaContenuto(DocumentTransferContext documentContext) {
    final var method = "analizzaContenuto";

    Long idDocumento = documentContext.idDocumento;

    var documentoDB = documentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var formatted = String.format(
          ErrorMessages.FS2IDX_IMPOSSIBILE_REPERIRE_RECORD_CORRISPONDENTE_SU_INDEX,
          documentoDB.getId());
      logger.error(method, formatted);
      throw new UnexpectedResponseException(formatted);
    }

    logger.debug(method, "avvio analisi contenuto del documento {} con index UID {}",
        documentoDB.getId(), contenutoIndex.getUuidNodo());

    var fileInfo = indexFeignClient.getInfoFormatoFile(contenutoIndex.getUuidNodo());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "risultato analisi contenuto documento {}: {}", documentoDB.getId(),
          ObjectUtils.represent(fileInfo));
    }

    if (fileInfo.getMimeType() != null && !fileInfo.getMimeType().isEmpty()) {
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
      CosmoDTipoFirma tipoFirma = tipoFirmaRepository.findOne(fileInfo.getSignatureType());
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
    
    contenutoDocumentoRepository.save(contenutoIndex);
    documentoRepository.save(documentoDB);
    return documentoDB;
  }

  public CosmoTDocumento verificaFirma(DocumentTransferContext documentContext) {
    final var method = "verificaFirma";

    Long idDocumento = documentContext.idDocumento;

    // ri-leggo da DB perche' probabilmente e' cambiato qualcosa dallo step precedente
    var documentoDB = documentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var formatted = String.format(
          ErrorMessages.FS2IDX_IMPOSSIBILE_REPERIRE_RECORD_CORRISPONDENTE_SU_INDEX,
          documentoDB.getId());
      logger.error(method, formatted);
      throw new UnexpectedResponseException(formatted);
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

  public CosmoTDocumento elaboraContenuto(DocumentTransferContext documentContext) {
    final var method = "elaboraContenuto";

    Long idDocumento = documentContext.idDocumento;

    var documentoDB = documentoRepository.findOne(idDocumento);

    var contenutoIndex = documentoDB.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (contenutoIndex == null) {
      var formatted = String.format(
          ErrorMessages.FS2IDX_IMPOSSIBILE_REPERIRE_RECORD_CORRISPONDENTE_SU_INDEX,
          documentoDB.getId());
      logger.error(method, formatted);
      throw new UnexpectedResponseException(formatted);
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
    contenutoDocumentoRepository.save(contenutoSbustato);

    return documentoDB;
  }

  public CosmoTDocumento generaAnteprime(DocumentTransferContext documentContext) {
    final var method = "generaAnteprime";

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    var documentoDB = documentoRepository.findOne(idDocumento);
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

    RetrievedContent retrievedFile =
        context.documentiFilesystem.getOrDefault(documentoDB.getId(), null);
    if (retrievedFile == null) {
      throw new UnexpectedResponseException(
          "Impossibile ottenere il file originale da cui generare le anteprime");
    }

    generaAnteprimaPerContenuto(contenutoOriginale, retrievedFile, documentContext);

    CosmoTContenutoDocumento contenutoSbustato =
        documentoDB.findContenuto(TipoContenutoDocumento.SBUSTATO);
    if (contenutoSbustato != null) {
      generaAnteprimaPerContenuto(contenutoSbustato, null, documentContext);
    }

    return documentoDB;
  }

  public FilesystemToIndexTransferContext newTransferContext() {
    return new FilesystemToIndexTransferContext();
  }

  protected void notificaErrori(FilesystemToIndexTransferContext context) {
    String method = "notificaErrori";
    Map<Long, Throwable> errori = context.errori;

    logger.info(method,
        "invio notifica per " + errori.size() + " errori avvenuti durante la migrazione");

    StringBuilder builder = new StringBuilder();
    builder.append(
        "Si sono verificati degli errori durante il processo di migrazione da filesystem ad Index.<br/>")
        .append("La migrazione e' fallita per i documenti seguenti: <br/><br/>");

    for (Long key : errori.keySet()) {
      builder.append(" - #" + key + "<br/>");
    }

    builder.append("<br/>Dettaglio degli errori: <br/>");

    for (Entry<Long, Throwable> entry : errori.entrySet()) {
      builder.append("Errore per #" + entry.getKey() + ":<br/>"
          + ExceptionUtils.exceptionToString(entry.getValue()).replaceAll("(\r\n|\n)", "<br />")
          + "<br/><br/>");

      if (context.batchContext != null) {
        context.batchContext.reportWarning(
            "Errore nella migrazione del documento #" + entry.getKey(), entry.getValue());
      }
    }

    // TODO - sostituire con meccanismo di invio del batch scheduler.
    mailService.inviaMailAssistenza("Errori nel processo di migrazione documenti su Index",
        builder.toString());
  }

  // HELPER METHODS

  protected <T> T require(T raw, String descrizione) {
    if (raw == null) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' nullo");
    }
    if (raw instanceof String && StringUtils.isBlank((String) raw)) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' vuoto");
    }
    return raw;
  }

  protected <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task) {
    return transactionService.inTransaction(task);
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


  private boolean troppoGrossoPerGenerareAnteprima(
      CosmoTContenutoDocumento contenuto,
      RetrievedContent override) {
    return ((override != null && override.getContentSize() != null
        && override.getContentSize() > MAX_CONTENT_SIZE_FOR_ANTEPRIME)
        || (contenuto.getDimensione() != null
        && contenuto.getDimensione() > MAX_CONTENT_SIZE_FOR_ANTEPRIME));
  }

  private void generaAnteprimaPerContenuto(CosmoTContenutoDocumento contenuto,
      RetrievedContent override, DocumentTransferContext documentContext) {

    final var method = "generaAnteprimaPerContenuto";
    if (contenuto == null) {
      logger.error(method, ErrorMessages.FS2IDX_IMPOSSIBILE_OTTENERE_CONTENUTO);
      throw new UnexpectedResponseException(ErrorMessages.FS2IDX_IMPOSSIBILE_OTTENERE_CONTENUTO);
    }

    logger.info(method,
        "contenuto documento {} {} - avvio generazione anteprime per contenuto originale",
        contenuto.getId(), contenuto.getTipo().getCodice());

    String mimeType = null;

    if (troppoGrossoPerGenerareAnteprima(contenuto, override)) {
      logger.warn(method,
          "contenuto documento {} {} - salto generazione anteprime per contenuto originale perche' roppo grosso",
          contenuto.getId(), contenuto.getTipo().getCodice());
      return;
    }

    if (contenuto.getFormatoFile() != null) {
      mimeType = contenuto.getFormatoFile().getMimeType();
    } else if (override != null) {
      mimeType = override.getContentType();
    } else {
      throw new UnexpectedResponseException(
          "Nessuna sorgente di content type per il contenuto " + contenuto.getId());
    }

    final String finalMimeType = mimeType;

    if (thumbnailService.possibileGenerazioneThumbnail(mimeType)) {
      InputStream arbInput = null;
      String filename = override != null ? override.getFilename() : null;

      Entity entity = null;
      if (!StringUtils.isBlank(contenuto.getUuidNodo())) {
        entity = indexFeignClient.getFile(contenuto.getUuidNodo(), true);
        filename = entity.getFilename();
      }

      if (override != null) {
        // devo fare il re-get perche' lo stream e' gia' stato usato
        RetrievedContent reloaded =
            fileShareService.get(override.getUploadUUID(),
                override.getUploadedAt().toLocalDate());

        arbInput = reloaded.getContentStream();
      } else if (entity != null) {
        arbInput = new ByteArrayInputStream(entity.getContent());
        contenuto.setShaFile(contenutoDocumentoService.generaSha256PerFile(entity.getContent()));
      } else {
        throw new UnexpectedResponseException("Nessun contenuto per l'anteprima");
      }

      generaThumbnail(filename, arbInput, finalMimeType, contenuto, documentContext);
    }

    // salvo flag di anteprima generata in ogni caso
    contenutoDocumentoRepository.save(contenuto);
  }

  private TransactionExecutionResult<Boolean> generaThumbnail(String filename, InputStream arbInput,
      String mimetype, CosmoTContenutoDocumento contenuto, DocumentTransferContext documentContext) {
    final var method = "generaThunbnail";
    final String finalFilename = filename;
    final InputStream finalInput = arbInput;
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    TransactionExecutionResult<Boolean> risultatoGenerazione =
        attempt(() -> thumbnailService.generaThumbnail(finalInput, mimetype, outputStream));

    if (risultatoGenerazione.failed()) {
      logger.error(method,
          "contenuto documento " + contenuto.getId()
              + " - errore nella generazione anteprima per contenuto originale",
          risultatoGenerazione.getError());
    } else if (risultatoGenerazione.getResult().booleanValue()) {
      logger.info(method, "contenuto documento {} - generata anteprima per contenuto originale",
          contenuto.getId());
      salvaAnteprimaContenuto(contenuto, finalFilename, outputStream, documentContext);
    } else {
      logger.debug(method,
          "contenuto documento {} - nessuna anteprima generata per contenuto originale",
          contenuto.getId());
    }

    return risultatoGenerazione;

  }

  private CosmoTAnteprimaContenutoDocumento salvaAnteprimaContenuto(
      CosmoTContenutoDocumento contenuto, String filenameIndex, ByteArrayOutputStream outputStream,
      DocumentTransferContext documentContext) {

    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "salvaAnteprimaContenuto";
    logger.info(method, "salvo l'anteprima generata per il contenuto {}", contenuto.getId());

    byte[] bytes = outputStream.toByteArray();

    String filename = contenuto.getNomeFile() + "_preview.jpg";

    // create index entity
    Entity indexEntity = buildDocumentoIndexAnteprima(contenuto, filenameIndex);

    // upload entity on index
    String container =
        context.nodiAnteprime.get(contenuto.getDocumentoPadre().getPratica().getId());

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

  private CosmoTDocumento generaUrlDownloadPubblico(DocumentTransferContext documentContext) {
    final var method = "elaboraContenuto";

    Long idDocumento = documentContext.idDocumento;

    var documentoDB = documentoRepository.findOne(idDocumento);

    documentoDB.getContenuti().stream()
    .filter(c -> c.nonCancellato() && c.getTipo() != null
    && !c.getTipo().getCodice().equals(TipoContenutoDocumento.TEMPORANEO.name())
    && c.getUuidNodo() != null && StringUtils.isBlank(c.getUrlDownload()))
    .forEach(contenuto -> {

      ShareOptions shareOptions = new ShareOptions();
      shareOptions.setContentDisposition(IndexContentDisposition.INLINE.name());
      shareOptions.setFilename(contenuto.getNomeFile());
      shareOptions.setSource(IndexShareScope.INTERNET.name());

      CondivisioniRequest request = new CondivisioniRequest();
      request.setOptions(shareOptions);
      request.setSourceIdentifier(contenuto.getUuidNodo());

      logger.debug(method, "genero url di download pubblico per il contenuto {}",
          contenuto.getId());
      var share = indexFeignClient.share(request);
      contenuto.setUrlDownload(share.getDownloadUri());

      logger.debug(method, "generato url di download pubblico per il contenuto {}",
          contenuto.getId());
      contenutoDocumentoRepository.save(contenuto);
    });

    return documentoDB;
  }

  private void inviaNotificaStatoDocumento(CosmoTDocumento documento, String  messaggio, boolean success) {


    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(documento.getPratica().getId());
    request.setTipoNotifica(TipoNotifica.ELABORAZIONE_DOCUMENTI.getCodice());
    if(success) {
      request.setMessaggio(messaggio);
      request.setClasse("SUCCESS");
    }else {
      request.setMessaggio(messaggio);
      request.setClasse("WARNING");
    }
    request.setEvento(Constants.EVENTS.STATO_DOCUMENTI);
    request.setCodiceIpaEnte(documento.getPratica().getEnte().getCodiceIpa());

    try {
      notificheFeignClient.postNotificheGlobali(request);
    } catch (Exception e) {
      logger.error("inviaNotificaStatoDocumento", "Errore nell'inviare la notifica", e);
    }
  }

  private List<CosmoTDocumento> findDaRitentare(int numMax, int maxRetries) {
    String methodName = "findDaRitentare";
    logger.debug(methodName, "ricerco documenti per cui ritentare la migrazione ...");

    Pageable pageRequest =
        new PageRequest(0, numMax, new Sort(Direction.DESC, CosmoTDocumento_.id.getName()));

    List<CosmoTDocumento> daMigrare = documentoRepository.findAllNotDeleted(
        CosmoTDocumentoSpecifications.findByStato(StatoDocumento.ERR_ACQUISIZIONE,
            LocalDate.now().minusDays(15), maxRetries),
        pageRequest).getContent();

    if (!daMigrare.isEmpty()) {
      logger.info(methodName,
          "trovati " + daMigrare.size() + " documenti per cui ritentare la migrazione");
    } else {
      logger.debug(methodName, "nessun documento per cui ritentare la migrazione");
    }

    return daMigrare;
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

    if (fileInfo.getMimeType() != null && !fileInfo.getMimeType().isEmpty()) {
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

  private synchronized CosmoTPratica verificaEsistenzaNodoPratica(
      DocumentTransferContext documentContext) {
    final var method = "verificaEsistenzaNodoPratica";

    Long idDocumento = documentContext.idDocumento;

    var documento = documentoRepository.findOne(idDocumento);
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
            praticaRepository.findWithLockingByIdAndDtCancellazioneIsNull(praticaDoc.getId());
        pratica.setUuidNodo(praticaFolderUUID);
        praticaDoc = praticaRepository.save(pratica);

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
      DocumentTransferContext documentContext) {
    final var method = "verificaEsistenzaNodoAnteprime";

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    var documento = documentoRepository.findOne(idDocumento);
    var pratica = documento.getPratica();

    if (context.nodiAnteprime.containsKey(pratica.getId())) {
      return pratica;
    }

    if (StringUtils.isBlank(pratica.getUuidNodo())) {
      logger.error(method, ErrorMessages.D_PRATICA_SENZA_NODO);
      throw new UnexpectedResponseException(ErrorMessages.D_PRATICA_SENZA_NODO);
    }

    var nodoPratica = indexFeignClient.findFolder(pratica.getUuidNodo());
    String path = nodoPratica.getEffectivePath();
    String nodoPath = nodoPratica.getEffectivePath() + "/anteprime";
    var nodoAnteprime = indexFeignClient.findFolder(nodoPath);

    if (nodoAnteprime != null) {
      context.nodiAnteprime.put(pratica.getId(), nodoAnteprime.getUid());

    } else {
      try {
        logger.info(method,
            "il nodo delle anteprime per la pratica {} non esiste su index, richiedo creazione del percorso {}",
            pratica.getId(), nodoPath);

        var folderUUID = indexFeignClient.createFolder(nodoPath);
        context.nodiAnteprime.put(pratica.getId(), folderUUID);

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

  private TransactionExecutionResult<?> elaboraDocumento(DocumentTransferContext documentContext) {

    Long idDocumentoDaMigrare = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "elaboraDocumento";
    logger.info(method, "elaborazione documento {} avviata", idDocumentoDaMigrare);

    context.documentiElaborati.add(idDocumentoDaMigrare);

    var result = registraTentativoAcquisizioneInTransazioneSeparata(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaVerifichePreliminariPratica(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaVerifichePreliminariAnteprima(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaMigrazioneDocumento(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaAnalisiContenuto(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaVerificaFirma(documentContext);
    if (result.failed()) {
      return result;
    }

    result = tentaElaborazioneContenuto(documentContext);
    if (result.failed()) {
      return result;
    }

    tentaGenerazioneAnteprime(documentContext);

    terminaElaborazioneDocumento(documentContext);

    tentaGenerazioneUrlDownload(documentContext);

    return TransactionExecutionResult.<Void>forSuccess(null);
  }

  private TransactionExecutionResult<?> registraTentativoAcquisizioneInTransazioneSeparata(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "registraTentativoAcquisizione";
    logger.info(method, "elaborazione documento {}: registrazione tentativo di acquisizione",
        idDocumento);

    var result = inTransaction(() -> registraTentativoAcquisizione(documentContext));

    if (result.failed()) {
      logger.error(method,
          "errore nella registrazione del tentativo di acquisizione per il documento " + idDocumento
              + ": " + result.getError().getMessage(),
          result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_ACQUISIZIONE);
    }

    logger.info(method,
        "elaborazione documento {}: terminata registrazione tentativo di acquisizione",
        idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaVerifichePreliminariPratica(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaVerifichePreliminariPratica";
    logger.info(method, "elaborazione documento {}: avvio verifiche preliminari per folder",
        idDocumento);

    var result = inTransaction(() -> verifichePreliminariPratica(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nelle verifiche preliminari della folder, per il documento "
          + idDocumento + ": " + result.getError().getMessage(), result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_ACQUISIZIONE);
    }

    logger.info(method, "elaborazione documento {}: terminate verifiche preliminari per folder",
        idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaVerifichePreliminariAnteprima(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaVerifichePreliminariAnteprima";
    logger.info(method,
        "elaborazione documento {}: avvio verifiche preliminari per folder anteprime", idDocumento);

    var result = inTransaction(() -> verifichePreliminariAnteprima(documentContext));

    if (result.failed()) {
      logger.error(method,
          "errore nelle verifiche preliminari della folder delle anteprime, per il documento "
              + idDocumento + ": " + result.getError().getMessage(),
          result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_ACQUISIZIONE);
    }

    logger.info(method,
        "elaborazione documento {}: terminate verifiche preliminari per folder delle anteprime",
        idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaMigrazioneDocumento(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaMigrazioneDocumento";
    logger.info(method, "elaborazione documento {}: avvio migrazione su index", idDocumento);

    var result = inTransaction(() -> migraDocumento(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nella migrazione del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_ACQUISIZIONE);
    }

    logger.info(method, "elaborazione documento {}: terminata migrazione su index", idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaAnalisiContenuto(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaAnalisiContenuto";
    logger.info(method, "elaborazione documento {}: avvio analisi contenuto", idDocumento);

    var result = inTransaction(() -> analizzaContenuto(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nella analisi del contenuto del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_ANALISI);
    }

    logger.info(method, "elaborazione documento {}: terminata analisi contenuto", idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaVerificaFirma(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaVerificaFirma";
    logger.info(method, "elaborazione documento {}: avvio verifica della firma", idDocumento);

    var result = inTransaction(() -> verificaFirma(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nella verifica della firma del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_VERIFICA);
    }

    logger.info(method, "elaborazione documento {}: terminata verifica della firma", idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaElaborazioneContenuto(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaElaborazioneContenuto";
    logger.info(method, "elaborazione documento {}: avvio elaborazione del contenuto", idDocumento);

    var result = inTransaction(() -> elaboraContenuto(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nell'elaborazione del contenuto del documento " + idDocumento
          + ": " + result.getError().getMessage(), result.getError());

      context.errori.put(idDocumento, result.getError());

      updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ERR_SBUSTAMENTO);
    }

    logger.info(method, "elaborazione documento {}: terminata elaborazione del contenuto",
        idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaGenerazioneAnteprime(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;

    final var method = "tentaGenerazioneAnteprime";
    logger.info(method, "elaborazione documento {}: avvio generazione anteprime", idDocumento);

    var result = inTransaction(() -> generaAnteprime(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nella generazione anteprime del documento " + idDocumento + ": "
          + result.getError().getMessage(), result.getError());
    }

    logger.info(method, "elaborazione documento {}: terminata generazione anteprime", idDocumento);
    return result;
  }

  private TransactionExecutionResult<?> tentaGenerazioneUrlDownload(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;

    final var method = "tentaGenerazioneAnteprime";
    logger.info(method, "elaborazione documento {}: avvio generazione url di download pubblico",
        idDocumento);

    var result = inTransaction(() -> generaUrlDownloadPubblico(documentContext));

    if (result.failed()) {
      logger.error(method, "errore nella generazione url di download pubblico del documento "
          + idDocumento + ": " + result.getError().getMessage(), result.getError());
    }

    logger.info(method, "elaborazione documento {}: terminata generazione url di download pubblico",
        idDocumento);
    return result;
  }

  private void terminaElaborazioneDocumento(DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;

    final var method = "terminaElaborazioneDocumento";
    logger.info(method, "elaborazione documento {}: marco come ELABORATO", idDocumento);

    updateStatoInTransazioneSeparata(documentContext, StatoDocumento.ELABORATO);

    tentaRimozioneContenutoTemporaneo(documentContext);

    logger.info(method, "elaborazione documento {}: terminata", idDocumento);
  }

  private TransactionExecutionResult<Void> tentaRimozioneContenutoTemporaneo(
      DocumentTransferContext documentContext) {

    Long idDocumento = documentContext.idDocumento;
    FilesystemToIndexTransferContext context = documentContext.transferContext;

    final var method = "tentaRimozioneContenutoTemporaneo";
    logger.info(method, "elaborazione documento {}: avvio rimozione contenuto temporaneo",
        idDocumento);

    var result = inTransaction(() -> {
      if (!context.documentiFilesystem.containsKey(idDocumento)) {
        return;
      }

      rimuoviContenutoTemporaneo(documentContext);
    });

    if (result.failed()) {
      logger.error(method,
          "errore nella cancellazione del contenuto temporaneo del documento" + idDocumento,
          result.getError());
    }

    logger.info(method, "elaborazione documento {}: terminata rimozione contenuto temporaneo",
        idDocumento);

    return result;
  }

  private TransactionExecutionResult<?> updateStatoInTransazioneSeparata(
      DocumentTransferContext documentContext, StatoDocumento statoDaSettare) {
    final var method = "updateStato";

    Long idDocumento = documentContext.idDocumento;

    final var result = inTransaction(() -> updateStato(idDocumento, statoDaSettare));

    if (result.failed()) {
      logger.error(method, "errore nella marcatura del documento come " + statoDaSettare,
          result.getError());
    }

    return result;
  }

  private void notificaDocumentiElaborati(Long idPratica) {
    final var method = "notificaDocumentiElaborati";

    var result = this.inTransaction(() -> {
      Map<String, Object> payload = new HashMap<>();
      payload.put("idPratica", idPratica);

      CosmoTPratica pratica = praticaRepository.findOne(idPratica);

      WebSocketTargetSelector target = new WebSocketTargetSelector();

      if (pratica.getEnte() != null && !StringUtils.isBlank(pratica.getEnte().getCodiceIpa())) {
        // filtro per ente
        CosmoTEnte ente = enteRepository.findByCodiceIpa(pratica.getEnte().getCodiceIpa());
        if (ente != null) {
          target.setIdEnte(ente.getId());
        }
      }

      eventService.broadcastEvent(Constants.EVENTS.DOCUMENTI_ELABORATI, payload, target);
    });

    if (result.failed()) {
      logger.error(method, "Errore durante la notifica dei documenti elaborati", result.getError());
    }
  }

  private List<RisultatoMigrazioneDocumento> migraDocumentiInternal(List<CosmoTDocumento> daMigrare,
      BatchExecutionContext batchContext) {
    var output = new ArrayList<RisultatoMigrazioneDocumento>();
    FilesystemToIndexTransferContext context = newTransferContext();
    context.batchContext = batchContext;
    for (CosmoTDocumento documentoDaMigrare : daMigrare) {
      var documentContext = new DocumentTransferContext();
      documentContext.transferContext = context;
      documentContext.idDocumento = documentoDaMigrare.getId();

      var res = elaboraDocumento(documentContext);

      //@formatter:off
      var mapped = RisultatoMigrazioneDocumento.builder()
          .withDocumento(documentoRepository.findOne(documentoDaMigrare.getId()))
          .withSuccesso(res.success())
          .withErrore(res.getError())
          .build();
      //@formatter:on

      output.add(mapped);

      String titoloDoc = "UNDEFINED";
      if (StringUtils.isNotBlank(mapped.getDocumento().getTitolo())) {
        titoloDoc = mapped.getDocumento().getTitolo();
      } else {
        if (mapped.getDocumento().findContenuto(TipoContenutoDocumento.ORIGINALE) != null
            && StringUtils.isNotBlank(mapped.getDocumento()
                .findContenuto(TipoContenutoDocumento.ORIGINALE).getNomeFile())) {
          titoloDoc =
              mapped.getDocumento().findContenuto(TipoContenutoDocumento.ORIGINALE).getNomeFile();

        } else if (mapped.getDocumento().findContenuto(TipoContenutoDocumento.TEMPORANEO) != null
            && StringUtils.isNotBlank(mapped.getDocumento()
                .findContenuto(TipoContenutoDocumento.TEMPORANEO).getNomeFile())) {
          titoloDoc =
              mapped.getDocumento().findContenuto(TipoContenutoDocumento.TEMPORANEO).getNomeFile();
        }
      }

      if (mapped.isSuccesso()) {
        String messaggioSuccess = String.format(Constants.STATO_DOCUMENTO_SUCCESS, titoloDoc,
            mapped.getDocumento().getPratica().getOggetto());
        inviaNotificaStatoDocumento(mapped.getDocumento(), messaggioSuccess, mapped.isSuccesso());
      } else {
        String messaggioError = String.format(Constants.STATO_DOCUMENTO_ERROR, titoloDoc,
            mapped.getDocumento().getPratica().getOggetto());
        inviaNotificaStatoDocumento(mapped.getDocumento(), messaggioError, mapped.isSuccesso());
      }
    }

    if (!context.errori.isEmpty()) {
      notificaErrori(context);
    }

    if (!context.praticheElaborate.isEmpty()) {
      context.praticheElaborate.forEach(this::notificaDocumentiElaborati);
    }
    return output;
  }

  // CONTEXT CLASSES
  protected static class FilesystemToIndexTransferContext {
    BatchExecutionContext batchContext = null;
    Map<Long, Throwable> errori = new HashMap<>();
    Set<Long> praticheElaborate = new HashSet<>();
    Set<Long> documentiElaborati = new HashSet<>();
    Map<Long, RetrievedContent> documentiFilesystem = new HashMap<>();
    Map<Long, String> nodiAnteprime = new HashMap<>();
  }

  protected static class DocumentTransferContext {
    FilesystemToIndexTransferContext transferContext;
    Long idDocumento;
  }


}
