/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import it.csi.cosmo.common.async.internal.ContextAwareCallable;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.entities.CosmoTAnteprimaContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTOtp;
import it.csi.cosmo.common.entities.CosmoTOtp_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FeaService;
import it.csi.cosmo.cosmoecm.business.service.ThumbnailService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.dto.index2.IndexContentDisposition;
import it.csi.cosmo.cosmoecm.dto.index2.IndexShareScope;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateDocumento;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTAnteprimaContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTOtpRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoEntity;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;

/**
 *
 */

@Service
@Transactional
public class FeaServiceImpl implements FeaService {

  private static final long MAX_CONTENT_SIZE_FOR_ANTEPRIME = 50 * 1024 * 1024L;

  private static final String MIME_APPLICATION_PDF = "application/pdf";

  @Autowired
  private CosmoTOtpRepository cosmoTOtpRepository;

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private ThumbnailService thumbnailService;

  @Autowired
  private CosmoTAnteprimaContenutoDocumentoRepository anteprimaContenutoDocumentoRepository;

  @Autowired
  private CosmoDFormatoFileRepository cosmoDFormatoFileRepository;


  private static final String CLASS_NAME = FeaServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final int MAX_PARALLEL_EXECUTIONS = 3;

  @Override
  public RiferimentoOperazioneAsincrona firma(FirmaFeaRequest request) {
    final String methodName = "firma";

    logger.info(methodName, "Inizio firma fea");

    ValidationUtils.require(request, "request");
    ValidationUtils.require(request.getOtp(), "otp");
    ValidationUtils.require(request.getTemplateDocumenti(), "templateDocumenti");
    var otp = validaOtp(request.getOtp());

    var resultCollector = new ConcurrentHashMap<Long, EsitoAttivitaEseguibileMassivamente>();


    final String taskName = "esecuzione firma elettronica avanzata";
    LongTaskFuture<?> asyncTask = asyncTaskService.start(taskName, task -> {

      var executor = Executors
          .newFixedThreadPool(Math.min(request.getTemplateDocumenti().size(), MAX_PARALLEL_EXECUTIONS));


      // submit di subtask all'executor per l'esecuzione parallela
      for (TemplateDocumento td : request.getTemplateDocumenti()) {

        String docInfo = getInfoDocumento(td.getDocumento().getContenuti());

        var callable = new ContextAwareCallable<Object>(() -> task
            .step("Tentativo di firma per il documento: " + " ("
                + docInfo
                + ")",
                step -> {
                  EsitoAttivitaEseguibileMassivamente esito = new EsitoAttivitaEseguibileMassivamente();
                  esito.templateDocumento = td;
                  List<Documento> documenti = request.getTemplateDocumenti().stream().map(TemplateDocumento::getDocumento)
                      .collect(Collectors.toList());
                  var contenuti = getListaContenutiDaFirmare(documenti);

                  var contenuto = contenuti.stream().filter(
                      x -> x.getDocumentoPadre().getId().equals(td.getDocumento().getId()))
                      .findFirst();
                  try {
                    eseguiFirmaFea( td, contenuto.get());
                    esito.status = "COMPLETED";
                    esito.successo = true;
                  } catch (Throwable terr) { // NOSONAR
                    esito.successo = false;
                    esito.errore = terr;
                    esito.status = "FAILED";
                  } finally {
                    resultCollector.put(td.getDocumento().getId(), esito);
                  }
                  return null;
                }), getCurrentRequestAttributes());
        executor.submit(callable);
      }

      executor.shutdown();
      try {
        if (!executor.awaitTermination(Math.max(300, request.getTemplateDocumenti().size() * 60),
            TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        task.warn(
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }
      invalidaOTP(otp);
      // verifica risultati da resultCollector
      return ObjectUtils.toJson(resultCollector.values());
    });

    var output = new RiferimentoOperazioneAsincrona();
    logger.info(methodName, "Fine firma fea");
    output.setUuid(asyncTask.getTaskId());
    return output;
  }

  private void invalidaOTP(CosmoTOtp otp) {
    otp.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    otp.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    otp.setUtilizzato(Boolean.TRUE);
    cosmoTOtpRepository.save(otp);
  }


  /*
   * Verifica la bonta' del codice otp passato
   *
   */
  private CosmoTOtp validaOtp(String otp) {
    final String methodName = "validaOtp";

    var utente = SecurityUtils.getUtenteCorrente();
    if (utente == null) {
      logger.error(methodName, ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    var ente = utente.getEnte();
    if (ente == null) {
      logger.error(methodName, ErrorMessages.FEA_UTENTE_NON_AUTENTICATO_PRESSO_ENTE);
      throw new BadRequestException(ErrorMessages.FEA_UTENTE_NON_AUTENTICATO_PRESSO_ENTE);
    }

    var optOtp = cosmoTOtpRepository.findOneNotDeleted((root, cq, cb) ->

          cb.and(cb.equal(root.get(CosmoTOtp_.utente).get(CosmoTUtente_.id), utente.getId()),
          cb.equal(root.get(CosmoTOtp_.cosmoTEnte).get(CosmoTEnte_.id), ente.getId()),
          cb.isFalse(root.get(CosmoTOtp_.utilizzato)),
          cb.equal(root.get(CosmoTOtp_.valore), otp),
          cb.greaterThan(root.get(CosmoTOtp_.dtScadenza), Timestamp.valueOf(LocalDateTime.now())))
    );

    if (optOtp.isEmpty()) {
      logger.error(methodName, String.format(ErrorMessages.FEA_CODICE_OTP_NON_TROVATO, otp));
      throw new NotFoundException(String.format(ErrorMessages.FEA_CODICE_OTP_NON_TROVATO, otp));
    }

    return optOtp.get();
  }

  private void eseguiFirmaFea(TemplateDocumento templateDocumento,
      CosmoTContenutoDocumento contenuto) throws IOException {

    byte[] signedPdf = addFeaSignature(templateDocumento, contenuto);
    var contenutoFirmato =
        transactionService.inTransaction(() -> creaDocumentoFirmatoFea(contenuto, signedPdf));
    if (contenutoFirmato.getResult() != null) {
      generaAnteprimaFea(contenutoFirmato.getResult());
    }
  }

  private byte[] addFeaSignature(TemplateDocumento td, CosmoTContenutoDocumento contenuto) throws IOException {
    String methodName = "createSignature";

    var currentUser = SecurityUtils.getUtenteCorrente();
    if (currentUser == null) {
      logger.error(methodName, "Utente non autenticato");
      throw new BadRequestException("Utente non autenticato");
    }

    var ente = cosmoTEnteRepository.findOneNotDeletedByField(CosmoTEnte_.id, currentUser.getEnte().getId());
    if (ente.isEmpty()) {
      logger.error(methodName, "Utente non autenticato presso un ente");
      throw new BadRequestException("Utente non autenticato presso un ente");
    }

    var preferenzaEnte = ente.get().getCosmoTPreferenzeEntes().stream().filter(CosmoTEntity::nonCancellato).findAny().orElseThrow();
    var logoFea = preferenzaEnte.getValore().getIconaFea();
    if (StringUtils.isEmpty(logoFea)) {
      logger.error(methodName, "Il logo per la firma elettronica avanzata non e' presente");
      throw new BadRequestException("Il logo per la firma elettronica avanzata non e' presente");
    }

    logger.info(methodName, String.format(
        "Tentativo di firma elettronica avanzata avviato. %nId contenuto documento da firmare: %d.%n",
        contenuto.getId()));
    String uriIndex = contenuto.getUrlDownload();
    if (null == uriIndex) {
      var parametroNonValorizzato =
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "documentoIndex");
      logger.error(methodName, parametroNonValorizzato);
      throw new InternalServerException(parametroNonValorizzato);
    }
    InputStream linkIS;
    try {
      linkIS = new URL(uriIndex).openStream();
    } catch (IOException e) {
      throw new BadRequestException(
          "Errore nella creazione dello stream di input " + e.getMessage());
    }
    logger.info(methodName, String.format(
        "Numero pagina: %d%ncoordinata X: %f coordinata Y: %f%n",
        td.getPagina(), td.getCoordinataX(), td.getCoordinataY()));

    var doc = PDDocument.load(linkIS);
    int pdfNumberOfPages = doc.getNumberOfPages();
    int numeroPaginaDaFirmare = td.getPagina().intValue() - 1;
    byte[] image = Base64.getDecoder().decode(logoFea);
    ByteArrayInputStream bais = new ByteArrayInputStream(image);
    BufferedImage bim = ImageIO.read(bais);
    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bim);
    PDFont font = PDType1Font.TIMES_ROMAN;

    PDPage pagina = doc.getPage(numeroPaginaDaFirmare);
    if (pdfNumberOfPages < numeroPaginaDaFirmare) {
      throw new BadRequestException(
          "Il numero della pagina su cui apporre la firma e' maggiore del numero totale delle pagine del documento");
    }
    PDPageContentStream contents = new PDPageContentStream(doc, pagina,
        PDPageContentStream.AppendMode.APPEND, true, true);
    var firmatario =
        currentUser.getCognome().toUpperCase() + " " + currentUser.getNome().toUpperCase();
    var grandezzaFirmaDaScalare = 0;
    if (td.getCoordinataX().floatValue() > 480) {
      grandezzaFirmaDaScalare = (firmatario.length() * 7) + pdImage.getWidth();
    }
    var x = (td.getCoordinataX().floatValue() + pagina.getCropBox().getLowerLeftX()) - grandezzaFirmaDaScalare;
    var y = (-td.getCoordinataY().floatValue() + pagina.getCropBox().getUpperRightY()) - pdImage.getHeight();
    contents.drawImage(pdImage, x, y);

    contents.beginText();
    contents.setFont(font, 10);

    contents.newLineAtOffset(x + pdImage.getWidth(), y + (pdImage.getHeight() / 2f - 5f));
    var formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(Timestamp.valueOf(LocalDateTime.now()));
    contents.showText(formatDate);
    contents.endText();
    contents.beginText();
    contents.newLine();
    contents.endText();
    contents.beginText();
    contents.newLineAtOffset(x + pdImage.getWidth(), y + (pdImage.getHeight() / 2f + 5f));
    contents.showText(currentUser.getCognome().toUpperCase() + " " + currentUser.getNome().toUpperCase());
    contents.endText();
    contents.close();
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    doc.save(outStream);
    return outStream.toByteArray();
  }

  private CosmoTContenutoDocumento creaDocumentoFirmatoFea(CosmoTContenutoDocumento contenuto,
      byte[] pdfData) {
    final String methodName = "creaDocumentoFirmatoFea";
    Entity documentoIndex = null;

    if (TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())) {
      documentoIndex = indexFeignClient.getFile(contenuto.getUuidNodo(), false);

      if (documentoIndex == null) {
        logger.error(methodName, ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
        throw new InternalServerException(ErrorMessages.REPERIMENTO_DOCUMENTO_INDEX);
      }
      documentoIndex.setContent(pdfData);
      indexFeignClient.aggiorna(documentoIndex);

    } else {

      CosmoTPratica pratica = contenuto.getDocumentoPadre().getPratica();

      Entity documentoDaCreareSuIndex =
          buildDocumentoIndex(contenuto.getDocumentoPadre(), contenuto.getNomeFile());

      ContenutoEntity contenutoEntity = new ContenutoEntity();
      contenutoEntity.setEntity(documentoDaCreareSuIndex);
      contenutoEntity.setContent(pdfData);

      documentoIndex = indexFeignClient.creaFile(pratica.getUuidNodo(), contenutoEntity);
    }

    var creato = contenutoDocumentoService.creaContenutoFirmatoFea(contenuto, documentoIndex, contenuto.getNomeFile());

    ShareOptions options = new ShareOptions();
    options.setContentDisposition(IndexContentDisposition.INLINE.name());
    options.setFilename(creato.getNomeFile());
    options.setSource(IndexShareScope.INTERNET.name());

    CondivisioniRequest request = new CondivisioniRequest();
    request.setOptions(options);
    request.setSourceIdentifier(creato.getUuidNodo());

    logger.debug(methodName, "genero url di download pubblico per il contenuto {}",
        creato.getId());
    var share = indexFeignClient.share(request);
    creato.setUrlDownload(share.getDownloadUri());


    if (TipoContenutoDocumento.FIRMATO.toString()
        .equals(contenuto.getTipo().getCodice())) {
      contenuto.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
      contenuto
      .setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      cosmoTContenutoDocumentoRepository.save(contenuto);
    }
    creato.setShaFile(contenutoDocumentoService.generaSha256PerFile(documentoIndex.getContent()));
    creato = cosmoTContenutoDocumentoRepository.saveAndFlush(creato);

    return creato;

  }

  private List<CosmoTContenutoDocumento> getListaContenutiDaFirmare(List<Documento> documenti) {
    String methodName = "getListaContenutiDaFirmare";
    List<CosmoTContenutoDocumento> listaContenuti = new LinkedList<>();

    documenti.forEach(documento -> {

      List<ContenutoDocumento> contenuti = documento.getContenuti().stream()
          .filter( contenuto -> contenuto.getDtCancellazione() == null &&
          (TipoContenutoDocumento.ORIGINALE.toString().equals(contenuto.getTipo().getCodice())||
              TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())))
          .sorted(Comparator.comparing(ContenutoDocumento::getDtInserimento).reversed())
          .limit(1)
          .filter(contenuto -> (TipoContenutoDocumento.ORIGINALE.toString().equals(contenuto.getTipo().getCodice())) ||
              (TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())))
          .collect(Collectors.toList());

      // verifica esistenza ultimo contenuto
      if (contenuti == null || contenuti.isEmpty()) {
        logger.error(methodName, ErrorMessages.FIRMA_ERRORE);
        throw new BadRequestException("Contenuto documenti errato");
      }

      listaContenuti.add(cosmoTContenutoDocumentoRepository.findOne(contenuti.get(0).getId()));
    });

    return listaContenuti;
  }



  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private Entity buildDocumentoIndex(CosmoTDocumento documentoDB, String nomeOriginale) {
    Entity documentoIndex = new Entity();
    documentoIndex.setDescrizione(documentoDB.getDescrizione());
    documentoIndex.setFilename(addNamePostfix(nomeOriginale, "-firmato"));
    documentoIndex.setIdDocumento(documentoDB.getId());
    documentoIndex.setMimeType(MIME_APPLICATION_PDF);
    documentoIndex.setTipoDocumento(documentoDB.getTipo() != null ? documentoDB.getTipo().getCodice() : null);

    return documentoIndex;
  }

  private String addNamePostfix(String name, String postfix) {
    if (!name.contains(".")) {
      return name + postfix + "";
    }
    int position = name.lastIndexOf(".");
    String onlyName = name.substring(0, position);
    String onlyExt = name.substring(position + 1);
    return onlyName + postfix + "." + onlyExt;
  }


  public static class EsitoAttivitaEseguibileMassivamente {
    TemplateDocumento templateDocumento;
    Boolean successo;
    Throwable errore;
    String status;

    public TemplateDocumento getTemplateDocumento() {
      return templateDocumento;
    }

    public Boolean getSuccesso() {
      return successo;
    }

    public Throwable getErrore() {
      return errore;
    }

    public String getStatus() {
      return status;
    }
  }

  private String getInfoDocumento(List<ContenutoDocumento> cd) {
    String ret = "Documento generico";
    var optContenuto = cd.stream()
        .filter(x -> TipoContenutoDocumento.ORIGINALE.toString().equals(x.getTipo().getCodice()))
        .findFirst();

    if (optContenuto.isPresent()) {
      ret = optContenuto.get().getNomeFile();
    }

    return ret;
  }


  private void generaAnteprimaFea(CosmoTContenutoDocumento contenuto) {

    final String methodName = "generaAnteprimaFea";

    if (contenuto.getDimensione() != null
        && contenuto.getDimensione() > MAX_CONTENT_SIZE_FOR_ANTEPRIME) {
      logger.warn(methodName,
          "contenuto documento {} {} - salto generazione anteprime perche' troppo grosso",
          contenuto.getId(), contenuto.getTipo().getCodice());
      return;
    }

    if (thumbnailService.possibileGenerazioneThumbnail(MIME_APPLICATION_PDF)) {
      InputStream arbInput = null;
      String filename = contenuto.getNomeFile();

      Entity entity = null;
      if (!StringUtils.isBlank(contenuto.getUuidNodo())) {
        entity = indexFeignClient.getFile(contenuto.getUuidNodo(), true);
        filename = entity.getFilename();
      }

      if (entity != null) {
        arbInput = new ByteArrayInputStream(entity.getContent());
      } else {
        throw new UnexpectedResponseException("Nessun contenuto per l'anteprima");
      }

      final InputStream finalInput = arbInput;
      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

      TransactionExecutionResult<Boolean> risultatoGenerazione = attempt(
          () -> thumbnailService.generaThumbnail(finalInput, MIME_APPLICATION_PDF, outputStream));

      if (risultatoGenerazione.failed()) {
        logger.error(methodName,
            "contenuto documento " + contenuto.getId()
            + " - errore nella generazione anteprima",
            risultatoGenerazione.getError());
      } else if (risultatoGenerazione.getResult().booleanValue()) {
        logger.info(methodName,
            "contenuto documento {} - generata anteprima",
            contenuto.getId());
        byte[] bytes = outputStream.toByteArray();

        String nomeFile = contenuto.getNomeFile() + "_preview.jpg";

        // create index entity
        Entity indexEntity = buildDocumentoIndexAnteprima(contenuto, nomeFile);

        // upload entity on index
        var nodoPratica =
            indexFeignClient.findFolder(contenuto.getDocumentoPadre().getPratica().getUuidNodo());
        String nodoPath = nodoPratica.getEffectivePath() + "/anteprime";
        var nodoAnteprime = indexFeignClient.findFolder(nodoPath);

        ContenutoEntity contenutoEntity = new ContenutoEntity();
        contenutoEntity.setContent(bytes);
        contenutoEntity.setEntity(indexEntity);
        Entity createdOnIndex = indexFeignClient.creaFile(nodoAnteprime.getUid(), contenutoEntity);

        ShareOptions options = new ShareOptions();
        options.setFilename(filename);
        options.setSource(IndexShareScope.INTERNET.name());
        CondivisioniRequest request = new CondivisioniRequest();
        request.setEntity(createdOnIndex);
        request.setOptions(options);

        var share = indexFeignClient.share(request);

        CosmoTAnteprimaContenutoDocumento anteprima = new CosmoTAnteprimaContenutoDocumento();

        anteprima.setContenuto(contenuto);
        anteprima.setDimensione(Long.valueOf(bytes.length));
        anteprima.setNomeFile(filename);
        anteprima.setUuidNodo(createdOnIndex.getUid());
        anteprima.setFormatoFile(cosmoDFormatoFileRepository
            .findByMimeType(ThumbnailServiceImpl.THUMBNAIL_CONTENT_TYPE));
        anteprima.setShareUrl(share.getDownloadUri());

        anteprimaContenutoDocumentoRepository.save(anteprima);

        logger.info(methodName, "generata anteprima contenuto documento " + contenuto.getId()
        + " -> index " + anteprima.getUuidNodo());

      } else {
        logger.debug(methodName,
            "contenuto documento {} - nessuna anteprima generata",
            contenuto.getId());
      }
    }

  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

  private Entity buildDocumentoIndexAnteprima(CosmoTContenutoDocumento contenuto,
      String filenameIndex) {
    Entity documentoIndex = new Entity();

    documentoIndex.setFilename(filenameIndex != null ? "thumb-" + filenameIndex + ".jpg"
        : "thumb-" + contenuto.getNomeFile() + ".jpg");
    documentoIndex.setMimeType(ThumbnailServiceImpl.THUMBNAIL_CONTENT_TYPE);

    return documentoIndex;
  }
}
