/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.DocumentGeneratorService;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FilesystemToIndexService;
import it.csi.cosmo.cosmoecm.business.service.ReportService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoecm.dto.jasper.ContestoCreazioneDocumento;
import it.csi.cosmo.cosmoecm.dto.jasper.RisultatoGenerazioneReport;
import it.csi.cosmo.cosmoecm.dto.rest.GenerazioneReportResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediGenerazioneReportRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

  private static final String CLASSNAME = ReportServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASSNAME);

  @Autowired
  private DocumentoService documentoService;

  @Autowired
  private DocumentGeneratorService documentGeneratorService;

  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  protected TransactionService transactionService;

  @Autowired
  private FilesystemToIndexService filesystemToIndexService;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public RisultatoGenerazioneReport generaReportDaProcessoInMemory(
      RichiediGenerazioneReportRequest body) {
    return generaReportDaProcessoInByteArray(body);
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public RiferimentoOperazioneAsincrona generaReportAsincrono(
      RichiediGenerazioneReportRequest body) {

    var asyncTask = asyncTaskService.start("Generazione report", t -> {

      return t.step("Generazione report", step -> {
        return this.generaReportDaProcesso(body);
      });
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(asyncTask.getTaskId());
    return output;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public GenerazioneReportResponse generaReportDaProcesso(RichiediGenerazioneReportRequest body) {
    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    final var method = "generaReportDaProcesso";

    var risultatoGenerazione = generaReportDaProcessoInByteArray(body);
    var pratica = risultatoGenerazione.getPratica();
    var documentoGenerato = risultatoGenerazione.getCompilato();
    var formatoParsed = risultatoGenerazione.getFormato();

    // salva il documento fra i documenti della pratica in una transazione separata
    var risultatoSalvataggioDocumento = transactionService.inTransaction(
        () -> salvaDocumentoGenerato(body, pratica, formatoParsed, documentoGenerato));

    // se il salvataggio e' fallito interrompo qui
    if (risultatoSalvataggioDocumento.failed()) {
      throw ExceptionUtils.toChecked(risultatoSalvataggioDocumento.getError());
    }

    // eseguo migrazione del documento immediatamente
    var documento = risultatoSalvataggioDocumento.getResult();
    var contenuto = documento.findContenuto(TipoContenutoDocumento.TEMPORANEO);

    final var documentoDaMigrare = documento;
    var risultatoMigrazione =
        attempt(() -> filesystemToIndexService.migraDocumento(documentoDaMigrare));

    if (risultatoMigrazione.failed() || !risultatoMigrazione.getResult().isSuccesso()) {
      logger.warn(method,
          "il tentativo di migrazione immediata su index del report generato e' fallito. Verra' ritentata la migrazione secondo schedulazione.",
          risultatoMigrazione.failed() ? risultatoMigrazione.getError()
              : risultatoMigrazione.getResult().getErrore());

    } else {
      logger.info(method, "il report generato e' stato trasferito su index");
      documento = risultatoMigrazione.getResult().getDocumento();
      var nuovoContenuto = documento.findContenuto(TipoContenutoDocumento.ORIGINALE);
      if (nuovoContenuto != null) {
        contenuto = nuovoContenuto;
      }
    }

    // ritorna in output gli id del documento e del contenuto generati
    var output = new GenerazioneReportResponse();
    output.setIdDocumento(documento.getId());
    output.setIdContenutoDocumento(contenuto.getId());
    output.setUrl(contenuto.getUrlDownload());

    logger.debug(method, "salvato il documento con id {} e contenuto con id {}",
        output.getIdDocumento(), output.getIdContenutoDocumento());

    return output;
  }

  private RisultatoGenerazioneReport generaReportDaProcessoInByteArray(
      RichiediGenerazioneReportRequest body) {
    final var methodName = "generaReportDaProcessoInByteArray";
    var output = new RisultatoGenerazioneReport();

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);

    final var method = "generaReportDaProcesso";

    if (logger.isDebugEnabled()) {
      logger.debug(method, "ricevuta richiesta di generazione report {}", body);
    }

    // cerca la pratica
    var pratica = cosmoTPraticaRepository.findOne(body.getIdPratica());
    if (pratica == null) {
      var msgformatted = String.format(ErrorMessages.P_PRATICA_NON_TROVATA, body.getIdPratica());
      logger.error(methodName, msgformatted);
      throw new NotFoundException(msgformatted);
    }

    output.setPratica(pratica);

    // compila i parametri secondo mappatura
    ContestoCreazioneDocumento context = new ContestoCreazioneDocumento();
    context.setCodiceTipoPratica(pratica.getTipo().getCodice());
    context.setIdEnte(pratica.getEnte().getId());

    logger.debug(method, "carico risorse per template {}", body.getCodiceTemplate());

    Map<String, Object> parameters =
        documentGeneratorService.loadRisorsePerTemplate(null, body.getCodiceTemplate(), context);

    for (var parameter : parameters.entrySet()) {
      logger.debug(method, "caricata risorsa per template {}: {}", body.getCodiceTemplate(),
          parameter.getKey());
    }

    if (!StringUtils.isBlank(body.getMappaturaParametri())) {
      GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
      requestElaborazione.setMappatura(body.getMappaturaParametri());

      logger.debug(method, "richiedo elaborazione mappa parametri per i dati della pratica: {}",
          body.getMappaturaParametri());

      @SuppressWarnings("unchecked")
      Map<String, Object> datiElaborati = (Map<String, Object>) cosmoBusinessPraticheFeignClient
          .postPraticheIdElaborazione(body.getIdPratica(), requestElaborazione);

      logger.debug(method,
          "ricevuto risultato elaborazione mappa parametri per i dati della pratica");

      for (var entry : datiElaborati.entrySet()) {
        parameters.put(entry.getKey(), entry.getValue());
      }
    }

    // importa gli input utente
    if (body.getInputUtente() != null) {
      for (var inputUtente : body.getInputUtente()) {
        if (!StringUtils.isBlank(inputUtente.getCodiceParametro())) {
          parameters.put(inputUtente.getCodiceParametro(), inputUtente.getValore());
        }
      }
    }

    // genera il documento
    var formatoRichiesto = body.getFormato();
    if (StringUtils.isBlank(formatoRichiesto)) {
      formatoRichiesto = ExportFormat.PDF.name();
    }
    final ExportFormat formatoParsed;
    try {
      formatoParsed = ExportFormat.valueOf(formatoRichiesto.toUpperCase());
    } catch (Exception e) {
      var msgFormatted = String.format(ErrorMessages.R_FORMATO_NON_VALIDO, formatoRichiesto);;
      logger.error(methodName, msgFormatted);
      throw new BadRequestException(msgFormatted, e);
    }
    output.setFormato(formatoParsed);

    logger.debug(method, "formato richiesto: '{}', formato effettivo: '{}'", body.getFormato(),
        formatoParsed.name());

    logger.debug(method, "avvio rendering del documento");

    var documentoGenerato = documentGeneratorService.render(body.getCodiceTemplate(), null,
        parameters, formatoParsed, context);

    logger.debug(method, "terminato rendering del documento");

    output.setCompilato(documentoGenerato);
    output.setMimeType(documentGeneratorService.getMimeTypeByFormato(formatoParsed));
    output.setEstensione(formatoParsed.name().toLowerCase());
    return output;
  }

  private CosmoTDocumento salvaDocumentoGenerato(RichiediGenerazioneReportRequest body,
      CosmoTPratica pratica, ExportFormat formatoParsed, byte[] documentoGenerato) {

    final var method = "salvaDocumentoGenerato";

    ValidationUtils.require(body, "body");
    ValidationUtils.validaAnnotations(body);
    LocalDateTime now = LocalDateTime.now();

    // preparo gli attributi del file da salvare
    var timestampString = now.format(DateTimeFormatter.ISO_DATE) + " "
        + now.withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME).replace(":", "-");
    var estensione = formatoParsed.name().toLowerCase();
    var filename = body.getNomeFile();
    if (StringUtils.isBlank(filename)) {
      filename = "Report " + timestampString;
    }
    if (!filename.endsWith("." + estensione)) {
      filename = filename + "." + estensione;
    }

    var titolo = body.getTitolo();
    if (StringUtils.isBlank(titolo)) {
      titolo = "Report " + timestampString;
    }
    final var titoloDefinitivo = titolo;

    var autore = body.getAutore();

    // check per eventuale gia' esistente
    CosmoTDocumento giaEsistente =
        cosmoTDocumentoRepository.findNotDeletedByField(CosmoTDocumento_.pratica, pratica).stream()
            .filter(d -> d.getTitolo() != null && d.getTitolo().equals(titoloDefinitivo))
            .findFirst().orElse(null);

    if (giaEsistente != null) {
      logger.debug(method, "un documento con titolo {} esiste gia'", titoloDefinitivo, filename);

      if (!Boolean.TRUE.equals(body.isSovrascrivi())) {
        // scegli un nuovo nome
        titolo = titoloDefinitivo + " (" + timestampString + ")";
        int lastDotIndex = filename.lastIndexOf('.');
        filename = filename.substring(0, lastDotIndex) + " (" + timestampString + ")"
            + filename.substring(lastDotIndex);

        logger.debug(method, "rinomino il documento con nuovo titolo {} e nuovo filename {}",
            titolo, filename);
      } else {
        logger.debug(method,
            "cancello il documento con id {} per sovrascriverlo col documento corrente",
            giaEsistente.getId());

        // cancella gia' esistente
        documentoService.cancellaDocumentoLogicamente(giaEsistente.getId().intValue());
      }
    }

    logger.debug(method, "avvio salvataggio del documento con titolo {}, filename {}", titolo,
        filename);

    // salva il documento fra i documenti della pratica
    return documentoService.creaDocumentoProgrammaticamente(pratica.getId(),
        body.getCodiceTipoDocumento(), titolo, autore, filename,
        documentGeneratorService.getMimeTypeByFormato(formatoParsed),
        new ByteArrayInputStream(documentoGenerato));
  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

}
