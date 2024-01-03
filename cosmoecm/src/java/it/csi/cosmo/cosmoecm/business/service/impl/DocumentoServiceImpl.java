/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.io.TikaInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.FilterCriteria;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDEsitoVerificaFirma_;
import it.csi.cosmo.common.entities.CosmoDStatoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoRFormatoFileTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumentoPK;
import it.csi.cosmo.common.entities.CosmoRTipoDocumentoTipoDocumento_;
import it.csi.cosmo.common.entities.CosmoTApprovazione;
import it.csi.cosmo.common.entities.CosmoTApprovazione_;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento_;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTPratica_;
import it.csi.cosmo.common.entities.CosmoTPreferenzeEnte_;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.StatoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoFirmato;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.dto.DocumentiDaFirmareDTO;
import it.csi.cosmo.cosmoecm.dto.TipologieDocumentiSalvatiDTO;
import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ApprovazioneDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.FiltroRicercaDocumentiDTO;
import it.csi.cosmo.cosmoecm.dto.rest.InfoFirmaFea;
import it.csi.cosmo.cosmoecm.dto.rest.PageInfo;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentoResponse;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneTipologiaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.SigilloDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.SmistamentoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.VerificaTipologiaDocumentiSalvati;
import it.csi.cosmo.cosmoecm.dto.tika.TikaDTO;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoDTipoDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTApprovazioneMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTInfoAggiuntiveSmistamentoMapper;
import it.csi.cosmo.cosmoecm.integration.mapper.CycleAvoidingMappingContext;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRFormatoFileTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocTipoPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRTipoDocumentoTipoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTApprovazioneRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTPreferenzeEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTDocumentoSpecifications;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.CommonUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class DocumentoServiceImpl implements DocumentoService {

  private static final String CLASS_NAME = DocumentoServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String ID_DOCUMENTO = "id documento";

  @Autowired
  private CosmoTDocumentoRepository cosmoTDocumentoRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoRTipoDocTipoPraticaRepository cosmoRTipoDocTipoPraticaRepository;

  @Autowired
  private CosmoTDocumentoMapper cosmoTDocumentoMapper;

  @Autowired
  private CosmoTInfoAggiuntiveSmistamentoMapper cosmoTInfoAggiuntiveSmistamentoMapper;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private CosmoDTipoDocumentoRepository cosmoDTipoDocumentoRepository;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTPreferenzeEnteRepository cosmoTPreferenzeEnteRepository;

  @Autowired
  private CosmoRFormatoFileTipoDocumentoRepository cosmoRFormatoFileTipoDocumentoRepository;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private CosmoRTipoDocumentoTipoDocumentoRepository cosmoRTipoDocumentoTipoDocumentoRepository;

  @Autowired
  private CosmoDTipoDocumentoMapper cosmoDTipoDocumentoMapper;

  @Autowired
  private CosmoTApprovazioneRepository cosmoTApprovazioneRepository;

  @Autowired
  private CosmoTApprovazioneMapper cosmoTApprovazioneMapper;

  @Override
  @Transactional(readOnly = true)
  public Documento getDocumento(Integer id) {
    CommonUtils.require(id, ID_DOCUMENTO);

    CosmoTDocumento documentoSuDB =
        cosmoTDocumentoRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id)));

    Documento documento = cosmoTDocumentoMapper.toDTO(documentoSuDB, new CycleAvoidingMappingContext());

    List<ApprovazioneDocumento> listaApprovazioni = new ArrayList<>();

    List<CosmoTApprovazione> listaCosmoTApprovazioni = cosmoTApprovazioneRepository.findNotDeletedByField(CosmoTApprovazione_.cosmoTDocumento, documentoSuDB);
    listaCosmoTApprovazioni.forEach(approvazione -> listaApprovazioni.add(cosmoTApprovazioneMapper.toDto(approvazione)));
    documento.setApprovazioni(listaApprovazioni);

    return documento;
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentiResponse getDocumenti(String filter, Boolean export) {
    DocumentiResponse output = new DocumentiResponse();

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiDTO.class);

    var size = Boolean.TRUE.equals(export) ? ParametriApplicativo.EXPORT_ROW_MAX_SIZE
        : ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    Page<CosmoTDocumento> pageDocumenti =
        cosmoTDocumentoRepository.findAll(CosmoTDocumentoSpecifications
            .findByFiltri(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTDocumento> documentiSuDB = pageDocumenti.getContent();

    List<Documento> documenti = new LinkedList<>();

    if (Boolean.TRUE.equals(export)) {
      documentiSuDB.forEach(documentoSuDB -> documenti
          .add(cosmoTDocumentoMapper.toDTOLight(documentoSuDB, new CycleAvoidingMappingContext())));
    } else {
      documentiSuDB.forEach(documentoSuDB ->{
        List<CosmoTApprovazione> listaCosmoTApprovazioni =
            cosmoTApprovazioneRepository.findAllNotDeleted((root, cq, cb) -> {

              var predicate = cb.and(
                  cb.equal(root.get(CosmoTApprovazione_.cosmoTDocumento).get(CosmoTDocumento_.id),
                      documentoSuDB.getId()));

              return cq.where(predicate)
                  .orderBy(cb.desc(root.get(CosmoTApprovazione_.dtApprovazione)))
                  .getRestriction();
            });
        var listaApprovazioni = listaCosmoTApprovazioni.stream()
            .map(approvazione -> cosmoTApprovazioneMapper.toDto(approvazione))
            .collect(Collectors.toList());
        Documento documento = mapDocumentoConSmistamento(documentoSuDB);
        documento.setApprovazioni(listaApprovazioni);
        documenti.add(documento);
      });
    }

    var codiceTipoPratica =

        // aggancio delle relazioni allegati/principali per le tipologie di documento sia sulla chiave
        // allegati
        !documenti.isEmpty()
        ? documentiSuDB.get(0).getPratica().getTipo().getCodice()
            : "";

    for (var documento : documenti) {

      CosmoDTipoDocumento tipoDocDB = cosmoDTipoDocumentoMapper.toRecord(documento.getTipo());
      var relazioniDocPrincipaliAllegati = getRelazioniTipoDocumentoPrincipale(tipoDocDB, codiceTipoPratica);

      List<TipoDocumento> allegati = new ArrayList<>();
      for (var relazione : relazioniDocPrincipaliAllegati) {

        var allegato = cosmoDTipoDocumentoMapper.toDTO(relazione.getCosmoDTipoDocumentoAllegato());
        allegato.setPrincipali(Arrays
            .asList(cosmoDTipoDocumentoMapper.toDTO(relazione.getCosmoDTipoDocumentoPadre())));
        documento.getAllegati().stream().forEach(docAll ->
          docAll.getTipo().setPrincipali(Arrays
            .asList(cosmoDTipoDocumentoMapper.toDTO(relazione.getCosmoDTipoDocumentoPadre())))
        );
        allegati.add(allegato);

      }
      if (null != documento.getTipo()) {
        documento.getTipo().setAllegati(allegati);
      }
    }

    output.setDocumenti(documenti);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(documenti, Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageDocumenti.getNumber());
    pageInfo.setPageSize(pageDocumenti.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageDocumenti.getTotalElements()));
    pageInfo.setTotalPages(pageDocumenti.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentiResponse getDocumentiDaFirmare(String filter, String filterDaFirmare,
      Boolean export) {
    final String methodName = "getDocumentiDaFirmare";

    ValidationUtils.validaAnnotations(filter);

    GenericRicercaParametricaDTO<FiltroRicercaDocumentiDTO> ricercaParametrica =

        SearchUtils.getRicercaParametrica(filter, FiltroRicercaDocumentiDTO.class);
    if (ricercaParametrica.getFilter() == null) {
      logger.error(methodName, "Nessun filtro inserito");
      throw new BadRequestException("Nessun filtro inserito");
    }

    if (ricercaParametrica.getFilter().getIdPratica() == null
        || ricercaParametrica.getFilter().getIdPratica().isEmpty() || !ricercaParametrica
        .getFilter().getIdPratica().containsKey(FilterCriteria.EQUALS.getCodice())) {
      logger.error(methodName, "Nessuna pratica indicata");
      throw new BadRequestException("Nessuna pratica indicata");
    }

    DocumentiDaFirmareDTO input = null;

    try {
      input = ObjectUtils.getDataMapper().readValue(filterDaFirmare, DocumentiDaFirmareDTO.class);
    } catch (IOException e) {
      logger.error(methodName, ErrorMessages.D_DA_FIRMARE_INFO_NON_CORRETTE, e);
      throw new BadRequestException(ErrorMessages.D_DA_FIRMARE_INFO_NON_CORRETTE, e);
    }

    if (input.getTutti() != null && Boolean.TRUE.equals(input.getTutti())) {

      List<CosmoTDocumento> documentiSuDB = getDocumentiFirmati(
          cosmoTDocumentoRepository.findAll(CosmoTDocumentoSpecifications
              .findDocumentiDaFirmare(ricercaParametrica.getFilter(), input)),
          input.getCreationTime());

      DocumentiResponse output = new DocumentiResponse();

      List<Documento> documenti = new LinkedList<>();
      documentiSuDB
      .forEach(documentoSuDB -> documenti.add(
          mapDocumentoConSmistamento(documentoSuDB)));
      output.setDocumenti(documenti);

      return output;
    }

    logger.info(methodName,
        "Verifico la presenza nei filtri delle tipologie documento, obbligatorie in caso di filtro tutti a false");
    ValidationUtils.require(input.getTipologieDocumenti(), "tipologieDocumenti");

    DocumentiResponse output = new DocumentiResponse();

    var size = Boolean.TRUE.equals(export) ? ParametriApplicativo.EXPORT_ROW_MAX_SIZE
        : ParametriApplicativo.MAX_PAGE_SIZE;

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(size).asInteger());

    Page<CosmoTDocumento> pageDocumenti = cosmoTDocumentoRepository.findAll(
        CosmoTDocumentoSpecifications.findDocumentiDaFirmare(ricercaParametrica.getFilter(), input),
        paging);

    var documentiSuDB = pageDocumenti.getContent();
    List<CosmoTDocumento> docs = getDocumentiFirmati(documentiSuDB, input.getCreationTime());

    List<Documento> documenti = new LinkedList<>();
    if (Boolean.TRUE.equals(export)) {
      docs.forEach(documentoSuDB -> documenti
          .add(cosmoTDocumentoMapper.toDTOLight(documentoSuDB, new CycleAvoidingMappingContext())));
    } else {
      docs.forEach(documentoSuDB -> {
        List<CosmoTApprovazione> listaCosmoTApprovazioni =
            cosmoTApprovazioneRepository.findAllNotDeleted((root, cq, cb) -> {

              var predicate = cb.and(
                  cb.equal(root.get(CosmoTApprovazione_.cosmoTDocumento).get(CosmoTDocumento_.id),
                      documentoSuDB.getId()));

              return cq.where(predicate)
                  .orderBy(cb.desc(root.get(CosmoTApprovazione_.dtApprovazione))).getRestriction();
            });
        var listaApprovazioni = listaCosmoTApprovazioni.stream()
            .map(approvazione -> cosmoTApprovazioneMapper.toDto(approvazione))
            .collect(Collectors.toList());
        Documento documento = mapDocumentoConSmistamento(documentoSuDB);
        documento.setApprovazioni(listaApprovazioni);
        documenti.add(documento);

      });
    }

    output.setDocumenti(documenti);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(documenti, Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageDocumenti.getNumber());
    pageInfo.setPageSize(pageDocumenti.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageDocumenti.getTotalElements()));
    pageInfo.setTotalPages(pageDocumenti.getTotalPages());
    output.setPageInfo(pageInfo);

    return output;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Documenti inserisciDocumenti(Long idPratica, CreaDocumentiRequest documenti) {
    String methodName = "inserisciDocumento";
    Documenti documentiCreati = new Documenti();
    List<Documento> documentiSingoli = new LinkedList<>();

    if (null == idPratica) {
      logger.error(methodName, ErrorMessages.P_ID_PRATICA_NON_VALORIZZATO);
      throw new BadRequestException(ErrorMessages.P_ID_PRATICA_NON_VALORIZZATO);
    }

    for (CreaDocumentoRequest documento : documenti.getDocumenti()) {
      if (null == documento || StringUtils.isBlank(documento.getCodiceTipo())) {
        logger.error(methodName, ErrorMessages.D_TIPO_DOCUMENTO_NON_VALORIZZATO);
        throw new BadRequestException(ErrorMessages.D_TIPO_DOCUMENTO_NON_VALORIZZATO);
      }
      documentiSingoli.add(cosmoTDocumentoMapper.toDTO(inserisciDocumento(documento, idPratica),
          new CycleAvoidingMappingContext()));
    }

    documentiCreati.setDocumenti(documentiSingoli);
    return documentiCreati;
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public Documento modificaDocumento(AggiornaDocumentoRequest documento, Integer id) {
    String methodName = "modificaDocumento";

    if (id == null) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "id del documento"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "id del documento"));
    }

    CosmoTDocumento documentoDaAggiornare =
        cosmoTDocumentoRepository.findOneNotDeleted(id.longValue()).orElseThrow(
            () -> new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id)));

    documentoDaAggiornare.setAutore(documento.getAutore());
    documentoDaAggiornare.setDescrizione(documento.getDescrizione());
    documentoDaAggiornare.setTitolo(documento.getTitolo());

    documentoDaAggiornare = cosmoTDocumentoRepository.save(documentoDaAggiornare);
    logger.info(methodName, "Documento con id {} aggiornato", documentoDaAggiornare.getId());

    return cosmoTDocumentoMapper.toDTO(documentoDaAggiornare, new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Documento cancellaDocumento(Integer id) {
    String methodName = "cancellaDocumento";
    CommonUtils.require(id, ID_DOCUMENTO);

    transactionService.inTransactionOrThrow(() -> cancellaDocumentoLogicamente(id));

    try {
      cancellaDocumentoFisicamente(id);
    } catch (Exception e) {
      logger.warn(methodName,
          "Errore nella cancellazione dei contenuti fisici del documento con id " + id, e);
    }

    logger.info(methodName, "Documento con id {} eliminato", id);

    // refetch (altra transazione)
    CosmoTDocumento documentoSuDB = cosmoTDocumentoRepository.findOne(Long.valueOf(id));
    return cosmoTDocumentoMapper.toDTO(documentoSuDB, new CycleAvoidingMappingContext());
  }

  @Override
  public CosmoTDocumento cancellaDocumentoLogicamente(Integer id) {
    String methodName = "cancellaDocumentoLogicamente";
    CommonUtils.require(id, ID_DOCUMENTO);

    CosmoTDocumento documentoSuDB = cosmoTDocumentoRepository.findOne(Long.valueOf(id));

    if (documentoSuDB == null) {
      logger.error(methodName, String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id));
    }

    var allegatiDaCancellare = cosmoTDocumentoRepository.findAllByDocumentoPadre(documentoSuDB);

    if (null != allegatiDaCancellare && !allegatiDaCancellare.isEmpty()) {
      logger.info(methodName,
          String.format("Cancellazione dei %d allegati relativi al documento con id %d",
              allegatiDaCancellare.size(), documentoSuDB.getId()));

      allegatiDaCancellare
      .forEach(allegato -> cancellaDocumentoLogicamente(allegato.getId().intValue()));
    }

    documentoSuDB.getContenuti().forEach(c -> contenutoDocumentoService.cancella(c, false));

    documentoSuDB.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    documentoSuDB.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    return cosmoTDocumentoRepository.save(documentoSuDB);
  }

  @Override
  public void cancellaDocumentoIndex(String uuid) {
    indexFeignClient.deleteIdentifier(uuid);
  }

  @Override
  public RetrievedContent get(String fileUUID) {
    return fileShareService.get(fileUUID, LocalDate.now());
  }

  @Override
  public void delete(RetrievedContent file) {
    fileShareService.delete(file);
  }

  @Transactional
  @Override
  public CosmoTDocumento creaDocumentoProgrammaticamente(Long idPratica, String codiceTipo,
      String titolo, String autore, String filename, String mimeType, InputStream content) {
    final var method = "creaDocumentoProgrammaticamente";

    ValidationUtils.require(idPratica, "idPratica");
    ValidationUtils.require(filename, "filename");
    ValidationUtils.require(mimeType, "mimeType");
    ValidationUtils.require(content, "content");

    var now = Instant.now();
    var utente = SecurityUtils.getUtenteCorrente();
    var auditPrincipal = AuditServiceImpl.getPrincipalCode();

    // cerca la pratica
    var pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      var formatted = String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPratica);
      logger.error(method, formatted);
      throw new NotFoundException(formatted);
    }

    // trasferisco il contenuto su un file temporaneo
    var saved =
        fileShareService.saveFromMemory(content, UUID.randomUUID().toString() + "-" + filename,
            StringUtils.isBlank(mimeType) ? "application/octet-stream" : mimeType, auditPrincipal);

    var retrieved = fileShareService.get(saved.getMetadata().getFileUUID(), LocalDate.now());

    logger.info(method, "trasferito contenuto embedded su disco come file {}",
        saved.getMetadata().getFileUUID());

    // verifico il tipo documento se necessario
    CosmoDTipoDocumento tipoDocumento = null;
    if (!StringUtils.isBlank(codiceTipo)) {
      tipoDocumento = this.cosmoDTipoDocumentoRepository
          .findOneActiveByField(CosmoDTipoDocumento_.codice, codiceTipo.strip())
          .orElseThrow(() ->{
            logger.error(method, ErrorMessages.D_CODICE_TIPO_DOCUMENTO);
            throw new NotFoundException(ErrorMessages.D_CODICE_TIPO_DOCUMENTO);
              });
    }

    contenutoDocumentoService.validaDimensioneDocumento(tipoDocumento,
        saved.getMetadata().getContentSize());

    // creo il nuovo documento
    CosmoTDocumento entity = new CosmoTDocumento();
    entity.setTitolo(titolo);
    entity.setDescrizione(titolo);
    if (!StringUtils.isBlank(autore)) {
      entity.setAutore(autore);
    } else if (Boolean.FALSE.equals(utente.getAnonimo())) {
      entity.setAutore(utente.getNome() + " " + utente.getCognome());
    }
    entity.setDtInserimento(Timestamp.from(now));
    entity.setUtenteInserimento(auditPrincipal);
    entity.setDtUltimaModifica(Timestamp.from(now));
    entity.setUtenteUltimaModifica(auditPrincipal);
    entity.setIdDocumentoExt(UUID.randomUUID().toString());
    entity.setPratica(pratica);
    entity.setStato(cosmoTDocumentoRepository.reference(CosmoDStatoDocumento.class,
        StatoDocumento.ACQUISITO.name()));
    entity.setTipo(null);
    entity.setContenuti(new ArrayList<>());
    entity.setTipo(tipoDocumento);

    entity = cosmoTDocumentoRepository.save(entity);

    // creo il nuovo contenuto del documento
    var contenuto = contenutoDocumentoService.creaContenutoTemporaneo(retrieved);
    contenuto.setDocumentoPadre(entity);
    contenuto.setDtInserimento(Timestamp.from(now));
    contenuto.setUtenteInserimento(auditPrincipal);
    contenuto.setDtUltimaModifica(Timestamp.from(now));
    contenuto.setUtenteUltimaModifica(auditPrincipal);
    String shaFile = contenutoDocumentoService.generaSha256PerFile(retrieved);
    contenuto.setShaFile(shaFile);
    contenuto = cosmoTContenutoDocumentoRepository.save(contenuto);

    entity.getContenuti().add(contenuto);

    return entity;
  }

  @Override
  public PreparaEsposizioneDocumentiResponse preparaEsposizioneDocumenti(Long idPratica,
      PreparaEsposizioneDocumentiRequest body) {

    final var method = "preparaEsposizioneDocumenti";

    // get dei documenti associati alla pratica
    var pratica = cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      logger.error(method, String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPratica));
      throw new NotFoundException(String.format(ErrorMessages.P_PRATICA_NON_TROVATA, idPratica));
    }

    // forza fetch immediatamente se lazy -
    pratica.getDocumenti();

    PreparaEsposizioneDocumentiResponse output = new PreparaEsposizioneDocumentiResponse();
    output.setDocumentiEsposti(new ArrayList<>());

    final Long[] dimensioneTotale = new Long[] {0L};

    // verifico che sia tutto ok senza generare i link
    for (PreparaEsposizioneTipologiaDocumentiRequest tipologiaDaEsporre : body
        .getTipologieDaEsporre()) {

      documentiDaEsporre(pratica.getDocumenti(), tipologiaDaEsporre, body.isUltimoDocumento())
      .forEach(d -> {
        var contenutoEsponibile = getContenutoEsponibile(d);
        if (contenutoEsponibile != null) {
          dimensioneTotale[0] += contenutoEsponibile.getDimensione();
        }
      });

    }

    // verifico dimensione totale contro hard limit da configurazione applicativa
    var maxSizeHardLimit = configurazioneService
        .requireConfig(ParametriApplicativo.MAX_DOCUMENT_SIZE_FOR_EMAIL).asLong();
    if (dimensioneTotale[0] > maxSizeHardLimit) {
      throw new BadRequestException(
          "La dimensione complessiva dei documenti richiesti, pari a " + dimensioneTotale[0]
              + " bytes, e' maggiore del massimo consentito pari " + maxSizeHardLimit + " bytes");
    }

    // verifico dimensione totale contro soft limit da configurazione ente
    var preferenzeEnteOpt = cosmoTPreferenzeEnteRepository.findAllNotDeleted((root, cq, cb) -> {
      //@formatter:off
      return cb.and(
          cb.equal(root.get(CosmoTPreferenzeEnte_.cosmoTEnte).get(CosmoTEnte_.id), pratica.getEnte().getId())
          );
      //@formatter:on
    }, new Sort(Direction.DESC, CosmoTPreferenzeEnte_.versione.getName()));

    if (!preferenzeEnteOpt.isEmpty() && preferenzeEnteOpt.get(0).getValore() != null) {
      var preferenzeEnte = preferenzeEnteOpt.get(0).getValore();
      if (preferenzeEnte.getDimensioneMassimaAllegatiEmail() != null
          && preferenzeEnte.getDimensioneMassimaAllegatiEmail() > 0L
          && dimensioneTotale[0] > preferenzeEnte.getDimensioneMassimaAllegatiEmail()) {
        throw new BadRequestException(
            "La dimensione complessiva dei documenti richiesti, pari a " + dimensioneTotale[0]
                + " bytes, e' maggiore del massimo consentito per l'ente corrente pari a "
                + preferenzeEnte.getDimensioneMassimaAllegatiEmail() + " bytes");
      }
    }

    // genero i link solo se e' tutto corretto
    for (PreparaEsposizioneTipologiaDocumentiRequest tipologiaDaEsporre : body
        .getTipologieDaEsporre()) {

      documentiDaEsporre(pratica.getDocumenti(), tipologiaDaEsporre, body.isUltimoDocumento())
      .forEach(d -> esponiDocumenti(output, d, tipologiaDaEsporre));

    }

    return output;
  }


  @Override
  public List<VerificaTipologiaDocumentiSalvati> getTipologieDocumentiSalvati(String body) {
    String methodName = "getTipologieDocumentiSalvati";

    ValidationUtils.validaAnnotations(body);

    try {
      var input = ObjectUtils.getDataMapper().readValue(body, TipologieDocumentiSalvatiDTO.class);

      if (input.getIdPratica() == null) {
        String message = "Nessun id della pratica indicato";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }

      if (input.getTipologieDocumenti() == null || input.getTipologieDocumenti().isEmpty()) {
        String message = "Nessuna tipologia di documento indicata";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }

      if (Boolean.TRUE.equals(input.getDaFirmare()) && input.getCreationTime() == null) {
        String message = "Data creazione task non valida";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }

      if (Boolean.TRUE.equals(input.getVerificaDataDocObbligatori())
          && input.getCreationTime() == null) {
        String message = "Data creazione task non valida";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }

      List<String> codiciTipologieDocumenti = input.getTipologieDocumenti().stream()
          .map(VerificaTipologiaDocumentiSalvati::getCodiceTipologiaDocumento)
          .filter(string -> !string.isBlank()).collect(Collectors.toList());

      if (codiciTipologieDocumenti == null || codiciTipologieDocumenti.isEmpty()) {
        String message = "Nessuna tipologia di documento indicata";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }

      var tipiDocumenti = cosmoDTipoDocumentoRepository.findAll(codiciTipologieDocumenti).stream()
          .filter(CosmoDEntity::valido).collect(Collectors.toList());

      if (tipiDocumenti == null || tipiDocumenti.isEmpty()) {
        String message = "Nessuna tipologia inviata valida";
        logger.error(methodName, message);
        throw new BadRequestException(message);
      }
      var docs = getDocs(input, tipiDocumenti);

      return getListaVerificaTipologiaDocumentiSalvati(input, tipiDocumenti, docs);

    } catch (IOException e) {
      logger.error(methodName, ErrorMessages.D_DA_FIRMARE_INFO_NON_CORRETTE, e);
      throw new BadRequestException(ErrorMessages.D_DA_FIRMARE_INFO_NON_CORRETTE, e);
    }
  }

  private List<VerificaTipologiaDocumentiSalvati> getListaVerificaTipologiaDocumentiSalvati(
      TipologieDocumentiSalvatiDTO input, List<CosmoDTipoDocumento> tipiDocumenti, List<CosmoTDocumento> docs ) {
    var output = new ArrayList<VerificaTipologiaDocumentiSalvati>();
    input.getTipologieDocumenti().forEach(tipologiaDocumento -> {
      VerificaTipologiaDocumentiSalvati single = new VerificaTipologiaDocumentiSalvati();
      single.setCodiceTipologiaDocumento(tipologiaDocumento.getCodiceTipologiaDocumento());
      single
          .setCodiceTipologiaDocumentoPadre(tipologiaDocumento.getCodiceTipologiaDocumentoPadre());

      var tipoDocumento = tipiDocumenti.stream().filter(
          tipoDoc -> tipologiaDocumento.getCodiceTipologiaDocumento().equals(tipoDoc.getCodice()))
          .findFirst().orElse(null);
      single.setDescrizioneTipologiaDocumento(
          tipoDocumento == null ? null : tipoDocumento.getDescrizione());

      if (docs.isEmpty()) {
        single.setPresente(false);
      } else {
        single.setPresente(docs.stream()
            .anyMatch(doc -> tipologiaDocumento.getCodiceTipologiaDocumento()
                .equals(doc.getTipo().getCodice())
                && (tipologiaDocumento.getCodiceTipologiaDocumentoPadre() == null
                    || (null != doc.getDocumentoPadre() && null != doc.getDocumentoPadre().getTipo() && tipologiaDocumento
                        .getCodiceTipologiaDocumentoPadre()
                        .equals(doc.getDocumentoPadre().getTipo().getCodice()))
                    
                    
                    )));
      }

      output.add(single);
    });
    return output;
  }

  private List<CosmoTDocumento> getDocs(TipologieDocumentiSalvatiDTO input,
      List<CosmoDTipoDocumento> tipiDocumenti) {
    return cosmoTDocumentoRepository.findAllNotDeleted((root, cq, cb) -> {

      Predicate predicate = null;
      if (input.getDaFirmare() == null || Boolean.FALSE.equals(input.getDaFirmare())) {
        predicate = cb.and(
            cb.equal(root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id),
                input.getIdPratica()),
            root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.codice).in(tipiDocumenti
                .stream().map(CosmoDTipoDocumento::getCodice).collect(Collectors.toList())));
      } else {
        ListJoin<CosmoTDocumento, CosmoTContenutoDocumento> joinDocumentoContenuti =
            root.join(CosmoTDocumento_.contenuti, JoinType.LEFT);

        ListJoin<CosmoTContenutoDocumento, CosmoTInfoVerificaFirma> joinContenutiFirme =
            joinDocumentoContenuti.join(CosmoTContenutoDocumento_.infoVerificaFirme, JoinType.LEFT);

        predicate = cb.and(
            cb.equal(root.get(CosmoTDocumento_.pratica).get(CosmoTPratica_.id),
                input.getIdPratica()),

            root.get(CosmoTDocumento_.tipo).get(CosmoDTipoDocumento_.codice)
            .in(tipiDocumenti.stream().map(CosmoDTipoDocumento::getCodice)
                .collect(Collectors.toList())),

            cb.equal(
                joinDocumentoContenuti.get(CosmoTContenutoDocumento_.tipo)
                .get(CosmoDTipoContenutoDocumento_.codice),
                TipoContenutoDocumento.FIRMATO.toString()),
            cb.isNull(joinDocumentoContenuti.get(CosmoTEntity_.dtCancellazione)),

            cb.equal(joinContenutiFirme.get(CosmoTInfoVerificaFirma_.cfFirmatario),
                SecurityUtils.getUtenteCorrente().getCodiceFiscale()),
            cb.isNotNull(joinContenutiFirme.get(CosmoTInfoVerificaFirma_.dtApposizione)),
            cb.greaterThan(joinContenutiFirme.get(CosmoTInfoVerificaFirma_.dtApposizione),
                Timestamp.valueOf(input.getCreationTime())),
            cb.equal(joinContenutiFirme.get(CosmoTInfoVerificaFirma_.esito)
                .get(CosmoDEsitoVerificaFirma_.codice), EsitoVerificaFirma.VALIDA.toString()),
            cb.isNull(joinContenutiFirme.get(CosmoTEntity_.dtCancellazione)));
      }

      return Boolean.TRUE.equals(input.getVerificaDataDocObbligatori())
          ? cb.and(predicate, cb.greaterThan(root.get(CosmoTEntity_.dtInserimento),
              Timestamp.valueOf(input.getCreationTime())))
              : predicate;
    });
  }

  private List<CosmoRTipoDocumentoTipoDocumento> getRelazioniTipoDocumentoPrincipale(
      CosmoDTipoDocumento tipoDoc, String codiceTipoPratica) {
    return cosmoRTipoDocumentoTipoDocumentoRepository.findAllActive((root, cq, cb) -> {

      Join<CosmoRTipoDocumentoTipoDocumento, CosmoDTipoPratica> joinRTipoDocTipoPratica =
          root.join(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoPratica);

      return cb.and(
          cb.equal(joinRTipoDocTipoPratica.get(CosmoDTipoPratica_.codice), codiceTipoPratica),
          cb.equal(root.get(CosmoRTipoDocumentoTipoDocumento_.cosmoDTipoDocumentoPadre), tipoDoc));
    });
  }


  private String getFirmatario(String cfUtente) {

    final String methodName = "getFirmatario";
    var cf = cfUtente.split("@");
    if (cf == null || cf.length == 0) {
      logger.error(methodName, (ErrorMessages.FEA_UTENTE_NON_TROVATO));
      throw new BadRequestException(ErrorMessages.FEA_UTENTE_NON_TROVATO);
    }
    var utente = cosmoTUtenteRepository.findByCodiceFiscale(cf[0]);
    if (utente == null) {
      logger.error(methodName, String.format(ErrorMessages.FEA_FIRMATARIO_NON_TROVATO, cf[0]));
      throw new BadRequestException(String.format(ErrorMessages.FEA_FIRMATARIO_NON_TROVATO, cf[0]));
    }

    return utente.getNome() + " " + utente.getCognome();
  }

  private Documento mapDocumentoConSmistamento(CosmoTDocumento documentoSuDB) {
    var documento = cosmoTDocumentoMapper.toDTO(documentoSuDB, new CycleAvoidingMappingContext());

    if (documentoSuDB.getContenuti() != null && !documentoSuDB.getContenuti().isEmpty()
        && documento.getContenuti() != null && !documento.getContenuti().isEmpty()) {
      documentoSuDB.getContenuti().stream()
      .filter(contenuto -> contenuto.getTipo().getCodice()
          .equals(TipoContenutoDocumento.FIRMATO.toString())
          && contenuto.getTipoContenutoFirmato() != null && contenuto.getTipoContenutoFirmato()
          .getCodice().equals(TipoContenutoFirmato.FIRMA_ELETTRONICA_AVANZATA.getCodice()))
      .forEach(contenutoFirmato -> {
        var contenutoDTO = documento.getContenuti().stream()
            .filter(cont -> cont.getId().equals(contenutoFirmato.getId())).findAny()
            .orElseThrow(() -> new BadRequestException("Contenuto non trovato"));
        InfoFirmaFea infoFea = new InfoFirmaFea();
        infoFea.setFirmatario(getFirmatario(contenutoFirmato.getUtenteInserimento()));
        infoFea.setData(OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(contenutoFirmato.getDtInserimento().getTime()),
            ZoneId.systemDefault()));
        contenutoDTO.setInfoFirmaFea(infoFea);

      });
    }

    if (null != documentoSuDB.getDocumentiFigli() && !documentoSuDB.getDocumentiFigli().isEmpty()) {
      documento.setAllegati(
          documentoSuDB.getDocumentiFigli().stream().filter(CosmoTEntity::nonCancellato)
          .map(this::mapDocumentoConSmistamento).collect(Collectors.toList()));
    }

    if (documentoSuDB.getCosmoRSmistamentoDocumentos() != null) {
      var relazioneSmistamento = documentoSuDB.getCosmoRSmistamentoDocumentos().stream()
          .filter(CosmoREntity::valido).findFirst().orElse(null);

      if (null != relazioneSmistamento
          && null != relazioneSmistamento.getCosmoDStatoSmistamento()) {
        var smistamentoDocumento = new SmistamentoDocumento();
        smistamentoDocumento
        .setCodiceStato(relazioneSmistamento.getCosmoDStatoSmistamento().getCodice());
        smistamentoDocumento
        .setDescrizioneStato(relazioneSmistamento.getCosmoDStatoSmistamento().getDescrizione());
        smistamentoDocumento.setCodiceEsito(relazioneSmistamento.getCodiceEsitoSmistamento());
        smistamentoDocumento
        .setDescrizioneEsito(relazioneSmistamento.getMessaggioEsitoSmistamento());
        if (null != relazioneSmistamento.getCosmoTInfoAggiuntiveSmistamentos()) {
          smistamentoDocumento
          .setInfoAggiuntive(relazioneSmistamento.getCosmoTInfoAggiuntiveSmistamentos().stream()
              .map(
                  infoAggiuntiva -> cosmoTInfoAggiuntiveSmistamentoMapper.toDTO(infoAggiuntiva))
              .collect(Collectors.toList()));
        }
        documento.setSmistamento(smistamentoDocumento);
      }
    }

    // info sigillo documento
    var sigilliDocumento = new ArrayList<SigilloDocumento>();
    documentoSuDB.getCosmoRSigilloDocumentos().stream().forEach(sigilloDoc -> {
      var sigilloDocumento = new SigilloDocumento();
      sigilloDocumento.setCodiceStato(sigilloDoc.getCosmoDStatoSigilloElettronico().getCodice());
      sigilloDocumento.setDescrizioneStato(sigilloDoc.getCosmoDStatoSigilloElettronico().getDescrizione());
      sigilloDocumento.setCodiceEsito(sigilloDoc.getCodiceEsitoSigillo());
      sigilloDocumento.setDescrizioneEsito(sigilloDoc.getMessaggioEsitoSigillo());
      ZoneOffset zoneOffSet = ZoneId.of("Europe/Berlin").getRules().getOffset(
          LocalDateTime.ofInstant(sigilloDoc.getDtInizioVal().toInstant(), ZoneOffset.UTC));
      sigilloDocumento.setDtInserimento(sigilloDoc.getDtInizioVal().toInstant().atOffset(zoneOffSet));
      sigilliDocumento.add(sigilloDocumento);
    });

    documento.setSigillo(sigilliDocumento);

    return documento;
  }

  private List<CosmoTDocumento> getDocumentiFirmati(List<CosmoTDocumento> documenti,
      LocalDateTime creationTime) {

    List<CosmoTDocumento> output = new LinkedList<>();

    documenti.forEach(documento -> {

      if (documento.findContenuto(TipoContenutoDocumento.FIRMATO) == null || documento
          .getContenuti().stream()
          .noneMatch(contenuto -> contenuto.nonCancellato()
              && TipoContenutoDocumento.FIRMATO.toString().equals(contenuto.getTipo().getCodice())
              && contenuto.getTipoContenutoFirmato() != null
              && contenuto.getTipoContenutoFirmato().getCodice()
                  .equals(TipoContenutoFirmato.FIRMA_DIGITALE.getCodice())
              && contenuto.getInfoVerificaFirme().stream()
                  .anyMatch(infoFirma -> infoFirma.nonCancellato()
                      && infoFirma.getEsito().getCodice()
                          .equals(EsitoVerificaFirma.VALIDA.toString())
                      && infoFirma.getCfFirmatario()
                          .equals(SecurityUtils.getUtenteCorrente().getCodiceFiscale())
                      && creationTime != null
                      && infoFirma.getDtApposizione().before(Timestamp.valueOf(creationTime))))) {

        output.add(documento);
      }
    });

    return output;
  }

  private void validaTipoDocumento(RetrievedContent file, CosmoDTipoDocumento tipoDoc,
      String parentId) throws IOException {
    if (!StringUtils.isEmpty(parentId)) {
      CosmoTDocumento documentoPadre = cosmoTDocumentoRepository
          .findOneNotDeleted(Long.parseLong(parentId)).orElseThrow(() -> new NotFoundException(
              String.format(ErrorMessages.D_DOCUMENTO_PADRE_NON_TROVATO, parentId)));

      var codiceTipoDocumentoPadre = documentoPadre.getTipo().getCodice();
      if (StringUtils.isEmpty(documentoPadre.getTipo().getCodiceStardas())
          && !StringUtils.isEmpty(tipoDoc.getCodiceStardas())) {
        throw new ConflictException(ErrorMessages.D_PADRE_NON_PROTOCOLLABILE);
      }
      var pkRelazione = new CosmoRTipoDocumentoTipoDocumentoPK();
      pkRelazione.setCodicePadre(codiceTipoDocumentoPadre);
      pkRelazione.setCodiceAllegato(tipoDoc.getCodice());
      pkRelazione.setCodiceTipoPratica(documentoPadre.getPratica().getTipo().getCodice());
      var relTipoDocAllegato = cosmoRTipoDocumentoTipoDocumentoRepository
          .findOneActiveByField(CosmoRTipoDocumentoTipoDocumento_.id, pkRelazione)
          .orElseThrow(() -> new NotFoundException(
              String.format(ErrorMessages.D_RELAZIONE_TIPO_DOC_NON_TROVATA,
                  codiceTipoDocumentoPadre, tipoDoc.getCodice())));

      if (!StringUtils.isEmpty(relTipoDocAllegato.getCodiceStardasAllegato())) {
        validaTipoDocumentoInternal(file, tipoDoc);
      }
    } else {
      if (!StringUtils.isEmpty(tipoDoc.getCodiceStardas())) {
        validaTipoDocumentoInternal(file, tipoDoc);
      }
    }
  }

  private void validaTipoDocumentoInternal(RetrievedContent file, CosmoDTipoDocumento tipoDoc)
      throws IOException {
    final var methodName = "validaTipoDocumentoInternal";
    var relTipoDocFormatiFile = cosmoRFormatoFileTipoDocumentoRepository
        .findActiveByField(CosmoRFormatoFileTipoDocumento_.cosmoDTipoDocumento, tipoDoc);
    if (!relTipoDocFormatiFile.isEmpty()) {
      TikaInputStream tikaIS = TikaInputStream.get(file.getContentStream());
      var tikaDetector = TikaDTO.getInstance().detect(tikaIS);
      boolean mimeTypeMatched = relTipoDocFormatiFile.stream()
          .anyMatch(ff -> ff.getCosmoDFormatoFile().getMimeType().equalsIgnoreCase(tikaDetector));
      if (!mimeTypeMatched) {
        logger.error(methodName, ErrorMessages.D_TIPO_DOC_PROTO_MODIF);
        throw new NotFoundException(ErrorMessages.D_TIPO_DOC_PROTO_MODIF);
      }
    }
  }



  private CosmoTDocumento inserisciDocumento(CreaDocumentoRequest documento, Long idPratica) {
    String methodName = "inserisciDocumento";
    CommonUtils.require(idPratica, "idPratica");

    validaDocumento(documento);

    // verifico che l'uuid del file sia presente sul file system
    RetrievedContent file = null;

    try {
      file = get(documento.getUuidFile());
    } catch (Exception e) {
      logger.error(methodName,
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, documento.getUuidFile()));
      throw new NotFoundException(
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, documento.getUuidFile()));
    }

    var tipoDocumento = findTipoDocumento(documento.getCodiceTipo());
    contenutoDocumentoService.validaDimensioneDocumento(tipoDocumento, file.getContentSize());

    try {
      validaTipoDocumento(file, tipoDocumento, documento.getParentId());
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }

    var documentoDaSalvare = new CosmoTDocumento();

    documentoDaSalvare.setAutore(documento.getAutore());
    documentoDaSalvare.setDescrizione(documento.getDescrizione());
    documentoDaSalvare.setTitolo(documento.getTitolo());

    if (!StringUtils.isBlank(documento.getParentId())) {
      var documentoPadre =
          cosmoTDocumentoRepository.findOne(Long.parseLong(documento.getParentId()));
      if (null == documentoPadre) {
        throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_PRINCIPALE_NON_TROVATO,
            documento.getParentId()));
      }

      var pkRelazione = new CosmoRTipoDocumentoTipoDocumentoPK();
      pkRelazione.setCodiceAllegato(documento.getCodiceTipo());
      pkRelazione.setCodicePadre(documentoPadre.getTipo().getCodice());
      pkRelazione.setCodiceTipoPratica(documentoPadre.getPratica().getTipo().getCodice());

      var relazionePrincipaleAllegato = cosmoRTipoDocumentoTipoDocumentoRepository
          .findActiveByField(CosmoRTipoDocumentoTipoDocumento_.id, pkRelazione);

      if (CollectionUtils.isEmpty(relazionePrincipaleAllegato)) {
        throw new ConflictException(String.format(ErrorMessages.D_TIPO_DOC_NON_AMMISSIBILE,
            documento.getCodiceTipo(), documento.getParentId()));
      }

      documentoDaSalvare.setDocumentoPadre(documentoPadre);
      documentoDaSalvare.setIdDocParentExt(documentoPadre.getIdDocumentoExt());
    }

    documentoDaSalvare.setTipo(tipoDocumento);
    documentoDaSalvare
        .setPratica(cosmoTDocumentoRepository.reference(CosmoTPratica.class, idPratica));

    documentoDaSalvare.setIdDocumentoExt(UUID.randomUUID().toString());
    documentoDaSalvare.setStato(cosmoTDocumentoRepository.reference(CosmoDStatoDocumento.class,
        StatoDocumento.ACQUISITO.name()));

    var contenuto = contenutoDocumentoService.creaContenutoTemporaneo(file);
    if (contenuto.getFormatoFile() != null
        && Boolean.FALSE.equals(contenuto.getFormatoFile().getUploadConsentito())) {
      throw new BadRequestException("Il tipo del documento fornito non e' consentito.");
    }

    documentoDaSalvare = cosmoTDocumentoRepository.save(documentoDaSalvare);

    contenuto.setDocumentoPadre(documentoDaSalvare);
    String shaFile = contenutoDocumentoService.generaSha256PerFile(file);
    contenuto.setShaFile(shaFile);
    cosmoTContenutoDocumentoRepository.save(contenuto);

    logger.info(methodName, "Documento con id {} inserito", documentoDaSalvare.getId());
    return documentoDaSalvare;
  }

  private CosmoDTipoDocumento findTipoDocumento(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    final var methodName = "findTipoDocumento";

    CosmoDTipoDocumento tipoDocumento = cosmoDTipoDocumentoRepository.findOne(codice);

    if (tipoDocumento == null) {
      String message = String.format(ErrorMessages.D_TIPO_DOCUMENTO_NON_ESISTENTE, codice);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }
    return tipoDocumento;
  }

  private void cancellaDocumentoFisicamente(Integer id) {
    String methodName = "cancellaDocumentoFisicamente";
    CommonUtils.require(id, ID_DOCUMENTO);

    CosmoTDocumento documentoSuDB = cosmoTDocumentoRepository.findOne(Long.valueOf(id));

    if (documentoSuDB == null) {
      logger.error(methodName, String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id));
      throw new NotFoundException(String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, id));
    }

    var allegatiDaCancellare = cosmoTDocumentoRepository.findAllByDocumentoPadre(documentoSuDB);

    if (null != allegatiDaCancellare && !allegatiDaCancellare.isEmpty()) {
      logger.info(methodName,
          String.format("Cancellazione dei %d allegati relativi al documento con id %d",
              allegatiDaCancellare.size(), documentoSuDB.getId()));

      allegatiDaCancellare
          .forEach(allegato -> cancellaDocumentoFisicamente(allegato.getId().intValue()));
    }

    documentoSuDB.getContenuti().forEach(c -> contenutoDocumentoService.cancella(c, true));
  }

  private List<CosmoTDocumento> documentiDaEsporre(List<CosmoTDocumento> documenti,
      PreparaEsposizioneTipologiaDocumentiRequest tipologiaDaEsporre, Boolean ultimoDocumento) {
    List<CosmoTDocumento> result = null;
    if (ultimoDocumento != null && ultimoDocumento.equals(true)) {
      var ultimoDoc = documenti.stream()
          .filter(d -> d.nonCancellato() && d.getTipo() != null && d.getTipo().getCodice() != null
              && d.getTipo().getCodice().equals(tipologiaDaEsporre.getCodiceTipoDocumento()))
          .sorted(Comparator.comparing(CosmoTDocumento::getDtInserimento).reversed()).limit(1)
          .findFirst();
      result = ultimoDoc.isPresent() ? Arrays.asList(ultimoDoc.get()) : new ArrayList<>();
    } else {
      result = documenti.stream()
          .filter(d -> d.nonCancellato() && d.getTipo() != null && d.getTipo().getCodice() != null
              && d.getTipo().getCodice().equals(tipologiaDaEsporre.getCodiceTipoDocumento()))
          .collect(Collectors.toList());
    }
    return result;
  }

  private void esponiDocumenti(PreparaEsposizioneDocumentiResponse output,
      CosmoTDocumento documento, PreparaEsposizioneTipologiaDocumentiRequest tipologiaDaEsporre) {
    String method = "esponiDocumenti";
    Esito esito = new Esito();
    esito.setStatus(HttpStatus.OK.value());
    esito.setCode("OK");
    esito.setTitle("Ok");

    var risultatoEsposizioneDocumento = new PreparaEsposizioneDocumentoResponse();
    output.getDocumentiEsposti().add(risultatoEsposizioneDocumento);

    risultatoEsposizioneDocumento.setEsito(esito);

    risultatoEsposizioneDocumento.setIdDocumento(documento.getId());
    risultatoEsposizioneDocumento.setCodiceTipoDocumento(documento.getTipo().getCodice());
    risultatoEsposizioneDocumento.setNomeFile(documento.getTitolo());

    if (documento.getStato() == null
        || !StatoDocumento.ELABORATO.name().equals(documento.getStato().getCodice())) {
      esito.setStatus(HttpStatus.CONFLICT.value());
      esito.setCode("DocumentoNonPronto");
      esito.setTitle("Documento non pronto"
          + (documento.getStato() != null ? ", stato: " + documento.getStato().getCodice() : ""));
      return;
    }

    try {
      CosmoTContenutoDocumento contenutoEsponibile = getContenutoEsponibile(documento);

      if (contenutoEsponibile != null) {
        risultatoEsposizioneDocumento.setNomeFile(contenutoEsponibile.getNomeFile());
        risultatoEsposizioneDocumento.setDimensione(contenutoEsponibile.getDimensione());
        risultatoEsposizioneDocumento
            .setUrl(contenutoDocumentoService.getLinkEsposizionePermanente(documento.getId(),
                contenutoEsponibile.getId(), tipologiaDaEsporre.getDurata(), true).toString());
        risultatoEsposizioneDocumento.setShaFile(contenutoEsponibile.getShaFile());

      } else {
        esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        esito.setCode("NessunContenutoDisponibile");
        esito.setTitle("Nessun contenuto disponibile per il documento " + documento.getId());
      }
    } catch (Exception e) {
      logger.error(method, "errore nella preparazione del documento " + documento.getId(), e);
      esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      esito.setCode(e.getClass().getSimpleName());
      esito.setTitle(e.getMessage());
    }
  }

  private CosmoTContenutoDocumento getContenutoEsponibile(CosmoTDocumento documento) {
    var c = documento.findContenuto(TipoContenutoDocumento.FIRMATO);
    if (c != null) {
      return c;
    }

    c = documento.findContenuto(TipoContenutoDocumento.SBUSTATO);
    if (c != null) {
      return c;
    }

    c = documento.findContenuto(TipoContenutoDocumento.ORIGINALE);
    if (c != null) {
      return c;
    }

    return null;
  }

  private void validaDocumento(CreaDocumentoRequest documento) {

    CommonUtils.requireString(documento.getUuidFile(), "uuidFile");

  }

}
