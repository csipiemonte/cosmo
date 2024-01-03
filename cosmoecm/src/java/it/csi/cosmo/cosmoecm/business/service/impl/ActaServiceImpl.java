/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.TikaInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ActaService;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.FilesystemToIndexService;
import it.csi.cosmo.cosmoecm.business.service.LockService;
import it.csi.cosmo.cosmoecm.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.FiltroRicercaDocumentiActaDTO;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoecm.dto.index2.RisultatoMigrazioneDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.ClassificazioneActa;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiSempliciActaResponse;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFisicoActa;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoSempliceActa;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtenteResponse;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaDocumentoFisicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.ProtocolloActa;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.TitolarioActa;
import it.csi.cosmo.cosmoecm.dto.rest.VociTitolarioActa;
import it.csi.cosmo.cosmoecm.dto.tika.TikaDTO;
import it.csi.cosmo.cosmoecm.integration.mapper.ActaMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRFormatoFileTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapActaFeignClient;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;

/**
 *
 */


@Service
public class ActaServiceImpl implements ActaService {

  private static final String ID_IDENTITA = "idIdentita";

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private LockService lockService;

  @Autowired
  private FilesystemToIndexService filesystemToIndexService;

  @Autowired
  private DocumentoService documentoService;

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private ActaMapper actaMapper;

  @Autowired
  private CosmoSoapActaFeignClient actaFeignClient;


  @Autowired
  private CosmoRFormatoFileTipoDocumentoRepository cosmoRFormatoFileTipoDocumentoRepository;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Override
  public IdentitaUtenteResponse findIdentitaDisponibili() {
    var identitaDisponibili = actaFeignClient.getIdentitaDisponibili();
    IdentitaUtenteResponse response = new IdentitaUtenteResponse();
    response.addAll(actaMapper.acta2DTOs(identitaDisponibili.getIdentitaActa()));
    return response;
  }

  @Override
  public List<DocumentoFisicoActa> findDocumentiFisiciByIdDocumentoSemplice(String idIdentita,
      String id) {
    var docFisici = actaFeignClient.getDocumentiFisiciByidDocumentoSemplice(id, idIdentita);
    return actaMapper.toDocumentiFisiciActa(docFisici.getDocumentiFisici());
  }

  @Override
  public RiferimentoOperazioneAsincrona importaDocumentiActa(String identita,
      ImportaDocumentiActaRequest body) {
    ValidationUtils.require(body, "body");

    var auditPrincipal = AuditServiceImpl.getPrincipalCode();

    CosmoTPratica pratica = cosmoTPraticaRepository.findOneNotDeleted(body.getIdPratica())
        .orElseThrow(NotFoundException::new);

    var async = asyncTaskService.start("Importazione documenti da ACTA", task -> {
      Map<String, DocumentoSempliceActa> documentiSemplici = new ConcurrentHashMap<>();
      Map<String, DocumentoFisicoActa> documentiFisici = new ConcurrentHashMap<>();
      Map<String, RetrievedContent> contenutiScaricati = new ConcurrentHashMap<>();
      Map<String, CosmoTDocumento> documentiPratica = new ConcurrentHashMap<>();
      Map<String, RisultatoMigrazioneDocumento> risultatiMigrazione = new ConcurrentHashMap<>();

      task.step("Recupero informazioni sui documenti", step -> {

        var request = actaMapper.toImportaDocumentiRequest(body);
        DocumentiSempliciMap documentiSempliciMap =
            actaFeignClient.getDocumentiSempliciMap(identita, request);

        documentiSempliciMap.getDocumentiSempliciMap().forEach(doc ->
        documentiSemplici.put(doc.getId(),
            actaMapper.toDocumentoSempliceActa(doc.getDocumentoSemplice())));

        DocumentiFisiciMap documentiFisiciMap =
            actaFeignClient.getDocumentiFisiciMap(identita, request);

        documentiFisiciMap.getDocumentiFisiciMap().forEach(doc -> documentiFisici.put(doc.getId(),
            actaMapper.toDocumentoFisicoActa(doc.getDocumento())));
      });

      task.step("Download dei contenuti fisici", step -> {

        for (var documentoSempliceInput : body.getDocumenti()) {
          var documentoSemplice = documentiSemplici.get(documentoSempliceInput.getId());
          for (var documentoFisicoInput : documentoSempliceInput.getDocumentiFisici()) {
            var documentoFisico = documentiFisici.get(documentoFisicoInput.getId());
            step.step(
                "Download del contenuto fisico per il documento " + documentoSemplice.getOggetto(),
                step2 -> {
                  var contenutoFisico =
                      actaFeignClient.getContenutoPrimarioId(documentoFisico.getId(), identita);
                  FileUploadResult downloaded;

                  try {
                    downloaded = fileShareService.saveFromMemory(
                        new ByteArrayInputStream(contenutoFisico.getStream()),
                        contenutoFisico.getFilename(),
                        contenutoFisico.getMimetype(), auditPrincipal);
                  } catch (Exception e) {
                    throw ExceptionUtils.toChecked(e);
                  }

                  step2.debug("scaricati " + downloaded.getMetadata().getContentSize()
                      + " bytes nel contenuto temporaneo locale "
                      + downloaded.getMetadata().getFileUUID());

                  var contenutoGet = fileShareService.get(downloaded.getMetadata().getFileUUID(),
                      downloaded.getMetadata().getUploadedAt().toLocalDate());

                  contenutiScaricati.put(documentoFisicoInput.getId(), contenutoGet);
                });
          }
        }
      });

      task.step("Verifica documenti protocollabili", step -> {
        for (var documentoSempliceInput : body.getDocumenti()) {
          var documentoSemplice = documentiSemplici.get(documentoSempliceInput.getId());
          for (var documentoFisicoInput : documentoSempliceInput.getDocumentiFisici()) {
            step.step("Verifica per il documento " + documentoSemplice.getOggetto(), step2 -> {
              var contenutoScaricato = contenutiScaricati.get(documentoFisicoInput.getId());

              try {
                validaTipoDocumento(contenutoScaricato,
                    documentoFisicoInput.getCodiceTipoDocumento());
              } catch (IOException e) {
                throw ExceptionUtils.toChecked(e);
              }
            });
          }
        }
      });

      task.step("Creazione dei documenti per la pratica", step -> {
        for (var documentoSempliceInput : body.getDocumenti()) {
          var documentoSemplice = documentiSemplici.get(documentoSempliceInput.getId());
          for (var documentoFisicoInput : documentoSempliceInput.getDocumentiFisici()) {
            var documentoFisico = documentiFisici.get(documentoFisicoInput.getId());
            step.step("Creazione del documento " + documentoSemplice.getOggetto(), step2 -> {
              var contenutoScaricato = contenutiScaricati.get(documentoFisicoInput.getId());

              var documento = salvaDocumentoScaricatoInPratica(documentoSemplice, documentoFisico,
                  contenutoScaricato, documentoFisicoInput, pratica, step);

              // ATTENZIONE: SICCOME OGNI STEP STA IN UNA TRANSAZIONE SEPARATA,
              // USANDO L'ISTANZA CosmoTDocumento NEGLI STEP SUCCESSIVI
              // NON SARANNO DISPONIBILI I CAMPI LAZY-LOADED.
              // WORKAROUND: PRECARICARE I CAMPI NECESSARI O PASSARE SOLAMENTE L'ID E RE-FETCHARE IN
              // SEGUITO
              documentiPratica.put(documentoFisicoInput.getId(), documento);

              step2.debug("creato il documento locale " + documento.getDescrizione() + " con ID "
                  + documento.getId());
            });
          }
        }
      });

      CosmoTLock[] lockAcquisiti = new CosmoTLock[] {null};

      task.step("Acquisizione del lock sulla procedura di caricamento", step -> {
        var risultatoAcquisizioneLock = lockService.acquire(LockAcquisitionRequest.builder()
            .withCodiceRisorsa(FilesystemToIndexServiceImpl.JOB_LOCK_RESOURCE_CODE)
            .withRitardoRetry(1000L).withTimeout(2 * 60 * 1000L).withDurata(15 * 60 * 1000L)
            .build());
        if (!risultatoAcquisizioneLock.isAcquired()) {
          step.warn(
              "Non e' stato possibile acquisire il lock sulla procedura di caricamento. I documenti verranno migrati su Index fra qualche minuto.");

          step.sleep(2000);
        } else {
          lockAcquisiti[0] = risultatoAcquisizioneLock.getLock();
        }
      });

      TransactionExecutionResult<Object> risultatoCaricamentoDocumenti = null;

      if (lockAcquisiti[0] != null) {
        risultatoCaricamentoDocumenti = attempt(() -> {
          return task.step("Caricamento dei documenti su Index", step -> {
            for (var documentoSempliceInput : body.getDocumenti()) {
              for (var documentoFisicoInput : documentoSempliceInput.getDocumentiFisici()) {
                var documentoCosmo = documentiPratica.get(documentoFisicoInput.getId());
                step.step(
                    "Migrazione del documento " + documentoCosmo.getDescrizione() + " su Index",
                    step2 -> {
                      var risultati = filesystemToIndexService.migraDocumentiInsideLock(
                          lockAcquisiti[0], Arrays.asList(documentoCosmo), null);

                      var risultato = risultati.isEmpty() ? null : risultati.get(0);

                      risultatiMigrazione.put(documentoFisicoInput.getId(), risultato);

                      if (risultato != null) {
                        if (risultato.isSuccesso()) {
                          step2.debug("documento locale " + documentoCosmo.getDescrizione()
                          + " migrato su Index");
                        } else {
                          step2.debug("errore nella migrazione del documento locale "
                              + documentoCosmo.getDescrizione() + " su Index: "
                              + risultato.getErrore().getMessage()
                              + ". Verra' effettuato un nuovo tentativo fra qualche minuto");
                        }
                      }

                      return null;
                    });
              }
            }
          });
        });

        task.step("Rilascio del lock sulla procedura di caricamento", step -> {
          lockService.release(lockAcquisiti[0]);
        });

      }

      if (!contenutiScaricati.isEmpty()) {
        task.step("Pulizia dei contenuti scaricati", step -> {
          for (var contenutoScaricato : contenutiScaricati.values()) {
            var risultatoPulizia = attempt(() -> {
              fileShareService.delete(contenutoScaricato);
              return null;
            });
            if (risultatoPulizia.success()) {
              step.debug("rimosso file locale " + contenutoScaricato.getUploadUUID());
            } else {
              step.warn(
                  "errore nella rimozione del file locale " + contenutoScaricato.getUploadUUID()
                  + ": " + risultatoPulizia.getError().getMessage());
            }
          }
        });
      }

      if (risultatoCaricamentoDocumenti != null && risultatoCaricamentoDocumenti.failed()) {
        throw ExceptionUtils.toChecked(risultatoCaricamentoDocumenti.getError());
      }

      return null;
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(async.getTaskId());
    return output;
  }

  private String estraiAutore(DocumentoSempliceActa documentoSemplice) {

    Function<Collection<String>, Optional<String>> firstNotBlank =
        coll -> coll.stream().filter(StringUtils::isNotBlank).findFirst();

        var result = firstNotBlank.apply(documentoSemplice.getAutoreFisico());

        if (result.isEmpty()) {
          result = firstNotBlank.apply(documentoSemplice.getAutoreGiuridico());
        }

        if (result.isEmpty()) {
          result = Optional.of(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
        }

        return result.orElse(null);
  }

  private CosmoTDocumento salvaDocumentoScaricatoInPratica(DocumentoSempliceActa documentoSemplice,
      DocumentoFisicoActa documentoFisico, RetrievedContent contenutoScaricato,
      ImportaDocumentiActaDocumentoFisicoRequest documentoFisicoInput, CosmoTPratica pratica,
      LongTask<?> step) {

    final var method = "salvaDocumentoScaricatoInPratica";

    ValidationUtils.require(documentoSemplice, "documentoSemplice");
    ValidationUtils.require(documentoFisico, "documentoFisico");
    ValidationUtils.require(contenutoScaricato, "contenutoScaricato");
    ValidationUtils.require(pratica, "pratica");

    LocalDateTime now = LocalDateTime.now();

    // preparo gli attributi del file da salvare
    var timestampString = now.format(DateTimeFormatter.ISO_DATE) + " "
        + now.withNano(0).format(DateTimeFormatter.ISO_LOCAL_TIME).replace(":", "-");

    var filename = contenutoScaricato.getFilename();

    var titolo = documentoSemplice.getOggetto();
    if (StringUtils.isBlank(titolo)) {
      titolo = documentoFisico.getDescrizione();
    }

    final var titoloOriginale = titolo;

    var autore = estraiAutore(documentoSemplice);

    // check per eventuale gia' esistente
    CosmoTDocumento giaEsistente =
        cosmoTDocumentoRepository.findNotDeletedByField(CosmoTDocumento_.pratica, pratica).stream()
        .filter(d -> d.getTitolo() != null && d.getTitolo().equals(titoloOriginale)).findFirst()
        .orElse(null);

    if (giaEsistente != null) {
      logger.debug(method, "un documento con titolo {} esiste gia'", titoloOriginale, filename);

      // scegli un nuovo nome
      titolo = titoloOriginale + " (" + timestampString + ")";
      int lastDotIndex = filename.lastIndexOf('.');
      filename = filename.substring(0, lastDotIndex) + " (" + timestampString + "-"
          + UUID.randomUUID().toString() + ")" + filename.substring(lastDotIndex);

      logger.debug(method, "rinomino il documento con nuovo titolo {} e nuovo filename {}", titolo,
          filename);

      step.warn(String.format("Un documento con titolo '%s' esiste gia', importo come '%s'",
          titoloOriginale, titolo));
    }

    logger.debug(method, "avvio salvataggio del documento con titolo {}, filename {}", titolo,
        filename);

    // salva il documento fra i documenti della pratica

    return documentoService.creaDocumentoProgrammaticamente(pratica.getId(),
        documentoFisicoInput.getCodiceTipoDocumento(), titolo, autore, filename,
        contenutoScaricato.getContentType(), contenutoScaricato.getContentStream());
  }


  @Override
  public RiferimentoOperazioneAsincrona findDocumentiSemplici(String identita, String filter) {
    final var method = "findDocumentiSemplici";

    logger.info(method, "Inizio ricerca asincrona documenti semplici");
    ValidationUtils.require(filter, "filter");

    var async = asyncTaskService.start("Importazione documenti da ACTA", task -> {
      Page<DocumentoSempliceActa> docs;

      GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> ricercaParametrica =
          SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiActaDTO.class);

      Pageable paging = SearchUtils.getPageRequest(ricercaParametrica, 999);

      try {

        var docSemplici = actaFeignClient.getDocumentiSempliciPageable(filter, identita,
            ricercaParametrica.getFilter() != null
            && Boolean.TRUE.equals(ricercaParametrica.getFilter().getRicercaPerProtocollo()));

        docs = new PageImpl<>(actaMapper.toDocumentiSempliciActa(docSemplici.getDocumenti()),
            paging, docSemplici.getPageInfo().getTotalElements());

      } catch (Exception e) {
        logger.error(method, e.getMessage());
        throw ExceptionUtils.toChecked(e);
      }

      var response = new DocumentiSempliciActaResponse();
      var responseItems = new LinkedList<DocumentoSempliceActa>();
      response.setItems(responseItems);

      responseItems.addAll(doSearchByTerms(docs, ricercaParametrica, identita));

      PageInfo pageInfo = new PageInfo();
      pageInfo.setPage(docs.getNumber());
      pageInfo.setPageSize(docs.getSize());
      pageInfo.setTotalElements(Math.toIntExact(docs.getTotalElements()));
      pageInfo.setTotalPages(docs.getTotalPages());
      response.setPageInfo(pageInfo);

      return ObjectUtils.toJson(response);
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(async.getTaskId());
    logger.info(method, "Fine ricerca asincrona documenti semplici");
    return output;
  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

  private void validaTipoDocumento(RetrievedContent file, String codiceTipo) throws IOException {

    var tipoDoc = cosmoDTipoDocumentoRepository.findOneActiveByField(CosmoDTipoDocumento_.codice, codiceTipo).orElseThrow();
    if (!StringUtils.isEmpty(tipoDoc.getCodiceStardas())) {
      var relTipoDocFormatiFile = cosmoRFormatoFileTipoDocumentoRepository
          .findActiveByField(CosmoRFormatoFileTipoDocumento_.cosmoDTipoDocumento, tipoDoc);
      if (!relTipoDocFormatiFile.isEmpty()) {
        TikaInputStream tikaIS = TikaInputStream.get(file.getContentStream());
        var tikaDetector = TikaDTO.getInstance().detect(tikaIS);
        boolean mimeTypeMatched = relTipoDocFormatiFile
            .stream()
            .anyMatch(ff -> ff.getCosmoDFormatoFile().getMimeType().equalsIgnoreCase(tikaDetector));
        if (!mimeTypeMatched) {
          throw new NotFoundException(ErrorMessages.D_TIPO_DOC_PROTO_MODIF);
        }
      }
    }
  }

  @Override
  public String findIdByIndiceClassificazioneEstesaAggregazione(String identita,
      String indiceClassificazioneEsteso) {
    final var methodName = "findIdByIndiceClassificazioneEstesaAggregazione";
    ValidationUtils.require(identita, "identita");
    ValidationUtils.require(indiceClassificazioneEsteso, "indiceClassificazioneEsteso");
    logger.info(methodName, "Inizio ricerca per indice classificazione estesa");
    var objectId = actaFeignClient.getRicercaPerIndiceClassificazioneEstesa(identita, indiceClassificazioneEsteso);
    logger.info(methodName, "Fine ricerca per indice classificazione estesa");
    return objectId;
  }

  @Override
  public TitolarioActa getTitolarioActa(String idIdentita, String codice) {
    final var methodName = "getTitolarioActa";
    ValidationUtils.require(idIdentita, "idIdentita");
    ValidationUtils.require(codice, "codice");
    logger.info(methodName, "Inizio ricerca Titolario");
    var titolario = actaFeignClient.getRicercaTitolario(codice, idIdentita);
    logger.info(methodName, "Fine ricerca Titolario");
    return actaMapper.acta2TitolarioActa(titolario);
  }

  @Override
  public VociTitolarioActa ricercaAlberaturaVociTitolario(String idIdentita, String chiaveTitolario,
      String chiavePadre, String filter) {
    final var methodName = "ricercaAlberaturaVociTitolario";
    ValidationUtils.require(idIdentita, ID_IDENTITA);
    ValidationUtils.require(chiaveTitolario, "chiaveTitolario");
    ValidationUtils.require(filter, "filter");


    logger.info(methodName, "Inizio ricerca nell'alberatura del titolario");
    var vociTitolario = actaFeignClient.ricercaAlberaturaVociPageable(idIdentita, chiaveTitolario, filter, chiavePadre);

    VociTitolarioActa ret = new VociTitolarioActa();
    ret.setVociTitolario(actaMapper.acta2VociTitolario(vociTitolario.getVociTitolario()));
    PageInfo pageInfo = new PageInfo();
    if (null != vociTitolario.getPageInfo()) {
      pageInfo.setPage(vociTitolario.getPageInfo().getPage());
      pageInfo.setPageSize(vociTitolario.getPageInfo().getPageSize());
      pageInfo.setTotalElements(vociTitolario.getPageInfo().getTotalElements());
      pageInfo.setTotalPages(vociTitolario.getPageInfo().getTotalPages());
    }
    ret.setPageInfo(pageInfo);
    logger.info(methodName, "Fine ricerca nell'alberatura del titolario");

    return ret;
  }

  private LinkedList<DocumentoSempliceActa> doSearchByTerms(Page<DocumentoSempliceActa> docs,
      GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> ricercaParametrica, String identita) {

    LinkedList<DocumentoSempliceActa> ret = new LinkedList<>();

    docs.forEach(d -> {
      var mapped = d;
      mapped = mapTerms(ricercaParametrica, d, identita);
      ret.add(mapped);
    });

    return ret;
  }

  private boolean canExpandTerm(GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> ricercaParametrica, String term) {
    return (ricercaParametrica != null
        && ricercaParametrica.getExpand() != null
        && ricercaParametrica.getExpand().contains(term));
  }

  private DocumentoSempliceActa mapTerms(
      GenericRicercaParametricaDTO<FiltroRicercaDocumentiActaDTO> ricercaParametrica,
      DocumentoSempliceActa d, String identita) {

    final var methodName = "mapTerms";
    DocumentoSempliceActa mapped = new DocumentoSempliceActa();
    if (canExpandTerm(ricercaParametrica, "documentiFisici")) {
      try {
        mapped.setDocumentiFisici(getDocumentiFisiciMapped(d, identita));
      } catch (Exception e) {
        logger.error(methodName,
            "errore nella ricerca dei documenti fisici per il documento semplice " + d.getId()
                + ": " + e.getMessage());
      }
    }
    if (canExpandTerm(ricercaParametrica, "classificazioni")) {
      try {
        mapped.setClassificazioni(getClassificazioniIdDocumentoSempliceMapped(d, identita));
      } catch (Exception e) {
        logger.error(methodName,
            "errore nella ricerca delle classificazioni per il documento semplice " + d.getId()
                + ": " + e.getMessage());
      }
    }
    if (canExpandTerm(ricercaParametrica, "protocolli") && d.getIdProtocolloList() != null) {
      try {
        mapped.setProtocolli(getProtocolliIdMapped(d, identita));
      } catch (Exception e) {
        logger.error(methodName, "errore nella ricerca dei protocolli per il documento semplice "
            + d.getId() + ": " + e.getMessage());
      }
    }
    return mapped;
  }

  private List<DocumentoFisicoActa> getDocumentiFisiciMapped(DocumentoSempliceActa doc, String identita) {
    var docFisici = actaFeignClient.getDocumentiFisiciByidDocumentoSemplice(doc.getId(), identita);
    return actaMapper.toDocumentiFisiciActa(docFisici.getDocumentiFisici());
  }

  private List<ClassificazioneActa> getClassificazioniIdDocumentoSempliceMapped(
      DocumentoSempliceActa doc, String identita) {
    var classificazioni =
        actaFeignClient.getClassificazioniIdDocumentoSemplice(doc.getId(), identita);
    return actaMapper.acta2ClassificazioneActaDTOList(classificazioni.getClassificazioni());
  }

  private List<ProtocolloActa> getProtocolliIdMapped(DocumentoSempliceActa doc, String identita) {
    var protocolli = doc.getIdProtocolloList().stream()
        .map(idProtocollo -> actaFeignClient.getProtocolloId(idProtocollo, identita))
        .filter(Objects::nonNull).collect(Collectors.toList());
    return actaMapper.acta2ProtocolloActaDTOList(protocolli);
  }

}
