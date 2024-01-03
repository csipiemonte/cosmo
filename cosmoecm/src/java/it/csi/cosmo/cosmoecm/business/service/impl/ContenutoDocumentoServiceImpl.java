/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.entities.CosmoDEsitoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDFormatoFile_;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoFirmato;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoFirmato;
import it.csi.cosmo.common.entities.proto.CampiTecniciEntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.PayloadTooLargeException;
import it.csi.cosmo.common.fileshare.exceptions.FileShareRetrievalException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.config.ErrorMessages;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.dto.index2.IndexContentDisposition;
import it.csi.cosmo.cosmoecm.dto.index2.IndexShareScope;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutiDocumento;
import it.csi.cosmo.cosmoecm.integration.mapper.CosmoTContenutoDocumentoMapper;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoDFormatoFileRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTDocumentoRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTInfoVerificaFirmaRepository;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmoecm.util.CommonUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.CondivisioniRequest;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareOptions;
import it.csi.cosmo.cosmosoap.dto.rest.Signature;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;

/**
 *
 */
@Service
@Transactional
public class ContenutoDocumentoServiceImpl implements ContenutoDocumentoService {

  private static final String CLASS_NAME = ContenutoDocumentoServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String ID_DOCUMENTO = "idDocumento";

  private static final String ID_CONTENUTO = "idContenuto";

  @Autowired
  private CosmoTDocumentoRepository documentoRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private CosmoDFormatoFileRepository formatoFileRepository;

  @Autowired
  private CosmoTInfoVerificaFirmaRepository infoVerificaFirmaRepository;

  @Autowired
  private CosmoSoapIndexFeignClient indexFeignClient;

  @Autowired
  private FileShareService fileShareService;

  @Autowired
  private CosmoTContenutoDocumentoMapper cosmoTContenutoDocumentoMapper;

  @Override
  public CosmoTContenutoDocumento getById(Long idDocumento, Long idContenuto) {
    final var methodName = "getById";
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    var doc = documentoRepository.findOneNotDeleted(idDocumento).orElseThrow(
        () -> {
          var msgFormatted = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, idDocumento);
              logger.error(methodName, msgFormatted);
              return new NotFoundException(msgFormatted);
        });

    return doc.getContenuti().stream()
        .filter(o -> o.getId().equals(idContenuto))
        .findFirst()
        .map(d -> {
          d.getTipo().getCodice(); // forza loading del tipo se necessario
          return d;
        })
        .orElseThrow(() -> new NotFoundException(
            "Contenuto documento con id " + idContenuto + " non trovato"));

  }

  @Override
  public ContenutiDocumento getByIdDocumento(Long idDocumento) {
    final var methodName = "getByIdDocumento";
    CommonUtils.require(idDocumento, ID_DOCUMENTO);

    var doc = documentoRepository.findOneNotDeleted(idDocumento).orElseThrow(
        () -> {
          var msgFormatted = String.format(ErrorMessages.D_DOCUMENTO_NON_TROVATO, idDocumento);
          logger.error(methodName, msgFormatted);
          return new NotFoundException(msgFormatted);
        });

    var output = new ContenutiDocumento();
    output.setContenuti(

        doc.getContenuti().stream().filter(CampiTecniciEntity::nonCancellato)
        .map(cosmoTContenutoDocumentoMapper::toDTO).collect(Collectors.toList()));

    return output;
  }

  @Override
  public void cancellaById(Long idDocumento, Long idContenuto, boolean cancellaContenutoFisico) {
    var contenuto = getById(idDocumento, idContenuto);

    cancella(contenuto, cancellaContenutoFisico);
  }

  @Override
  public FileContent getContenutoFisico(Long idDocumento, Long idContenuto) {
    var contenuto = getById(idDocumento, idContenuto);

    return fromContenuto(contenuto);
  }

  @Override
  public URI getLinkDownloadDiretto(Long idDocumento, Long idContenuto, boolean preview) {
    final var methodName = "getLinkDownloadDiretto";
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    var contenuto = this.getById(idDocumento, idContenuto);
    if (TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {
      // disponibile solo come contenuto temporaneo
      logger.error(methodName, ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
      throw new UnsupportedOperationException(
          ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
    }

    if (StringUtils.isBlank(contenuto.getUuidNodo())) {
      logger.error(methodName, ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
      throw new InternalServerException(ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
    }

    // controlla se esistono share valide
    ShareOptions options = new ShareOptions();
    options.setFilename(contenuto.getNomeFile());
    options.setSource(IndexShareScope.INTERNET.name());
    options.setToDate(OffsetDateTime.now().plusMinutes(30));

    if (preview) {
      options.setContentDisposition(IndexContentDisposition.INLINE.name());
    } else {
      options.setContentDisposition(IndexContentDisposition.ATTACHMENT.name());
    }

    var response = getExistingValidShare(contenuto.getUuidNodo(), options)
        .map(ShareDetail::getDownloadUri).orElseGet(() -> {
          CondivisioniRequest request = new CondivisioniRequest();
          request.setSourceIdentifier(contenuto.getUuidNodo());
          request.setOptions(options);
          return indexFeignClient.share(request).getDownloadUri();
        });

    try {
      var test = new URI(response);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers
      .setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
      HttpEntity<String> entity = new HttpEntity<>(headers);

      restTemplate.exchange(test, HttpMethod.GET, entity, byte[].class);
      return new URI(response);
    } catch (URISyntaxException e) {
      var msgFormatted = String.format(ErrorMessages.D_ERRORE_CREAZIONE_URI_DA_URL, response);
      logger.error(methodName, msgFormatted);
      throw new InternalServerException(msgFormatted);
    }
  }

  @Override
  public URI getLinkEsposizionePermanente(Long idDocumento, Long idContenuto, Long durataMinima,
      boolean preview) {
    final var methodName = "getLinkEsposizionePermanente";
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    var contenuto = this.getById(idDocumento, idContenuto);
    if (TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {
      // disponibile solo come contenuto temporaneo
      logger.error(methodName,
          ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
      throw new UnsupportedOperationException(
          ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
    }

    if (StringUtils.isBlank(contenuto.getUuidNodo())) {
      logger.error(methodName, ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
      throw new InternalServerException(ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
    }

    OffsetDateTime toDate = null;
    if (durataMinima != null && durataMinima > 0L) {
      toDate = OffsetDateTime.now().plusSeconds(durataMinima);
    }

    ShareOptions options = new ShareOptions();
    options.setFilename(contenuto.getNomeFile());
    options.setSource(IndexShareScope.INTERNET.name());
    options.setToDate(toDate);

    if (preview) {
      options.setContentDisposition(IndexContentDisposition.ATTACHMENT.name());
    } else {
      options.setContentDisposition(IndexContentDisposition.INLINE.name());
    }

    var response = getExistingValidShare(contenuto.getUuidNodo(), options)
        .map(ShareDetail::getDownloadUri).orElseGet(() -> {
          CondivisioniRequest request = new CondivisioniRequest();
          request.setSourceIdentifier(contenuto.getUuidNodo());
          request.setOptions(options);
          return indexFeignClient.share(request).getDownloadUri();
        });

    try {
      return new URI(response);
    } catch (URISyntaxException e) {
      var msgFormatted = String.format(ErrorMessages.D_ERRORE_CREAZIONE_URI_DA_URL, response);
      logger.error(methodName, msgFormatted);
      throw new InternalServerException(msgFormatted);
    }
  }

  @Override
  public void cancella(CosmoTContenutoDocumento contenuto, boolean cancellaContenutoFisico) {
    final var method = "cancella";
    logger.debug(method, "cancello contenuto documento con id {}", contenuto.getId());

    if (cancellaContenutoFisico) {
      cancellaContenutoFisico(contenuto);
    }

    if (contenuto.cancellato()) {
      logger.warn(method, "il contenuto documento con id {} era gia' stato cancellato",
          contenuto.getId());
      return;
    }

    contenuto.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    contenuto.setUtenteCancellazione(AuditServiceImpl.getPrincipalCode());
    cosmoTContenutoDocumentoRepository.save(contenuto);
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoTemporaneo(RetrievedContent file) {
    final var method = "creaContenutoTemporaneo";
    logger.debug(method, "creazione di contenuto temporaneo a partire dal file {}",
        file.getUploadUUID());

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDimensione(file.getContentSize());
    output.setNomeFile(file.getFilename());
    output.setUuidNodo(file.getUploadUUID());

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.TEMPORANEO.name());
    output.setTipo(tipo);

    output.setFormatoFile(findFormatoByMime(file.getContentType()));
    return output;
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoOriginale(
      CosmoTContenutoDocumento contenutoTemporaneo, Entity indexEntity) {
    final var method = "creaContenutoOriginale";

    logger.info(method,
        "avvio migrazione del contenuto temporaneo {} a contenuto originale su index",
        contenutoTemporaneo.getId());

    RetrievedContent retrievedFile = fileShareService.get(contenutoTemporaneo.getUuidNodo(),
        contenutoTemporaneo.getDtInserimento().toLocalDateTime().toLocalDate());

    if (logger.isDebugEnabled()) {
      logger.debug(method, "dettagli del file da migrare: " + ObjectUtils.represent(retrievedFile));
    }

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoTemporaneo.getDocumentoPadre());
    output.setContenutoSorgente(contenutoTemporaneo);
    output.setDimensione(contenutoTemporaneo.getDimensione());
    output.setNomeFile(contenutoTemporaneo.getNomeFile());
    output.setFormatoFile(contenutoTemporaneo.getFormatoFile());
    output.setUuidNodo(indexEntity.getUid());

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.ORIGINALE.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto originale {} a partire da contenuto temporaneo {} ",
        indexEntity.getUid(), retrievedFile.getUploadUUID());

    return output;
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoOriginaleDaStreaming(
      CosmoTContenutoDocumento contenutoTemporaneo, Entity indexEntity) {
    final var method = "creaContenutoOriginaleDaStreaming";

    logger.debug(method,
        "avvio migrazione del contenuto temporaneo {} a contenuto originale su index",
        contenutoTemporaneo.getId());

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoTemporaneo.getDocumentoPadre());
    output.setContenutoSorgente(contenutoTemporaneo);
    output.setDimensione(contenutoTemporaneo.getDimensione());
    output.setNomeFile(contenutoTemporaneo.getNomeFile());
    output.setFormatoFile(contenutoTemporaneo.getFormatoFile());
    output.setUuidNodo(indexEntity.getUid());

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.ORIGINALE.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto originale {} a partire da contenuto temporaneo ",
        indexEntity.getUid());

    return output;
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoSbustato(CosmoTContenutoDocumento contenutoOriginale,
      Entity indexEntity) {
    final var method = "creaContenutoSbustato";
    logger.info(method,
        "avvio creazione del contenuto sbustato a partire dal contenuto originale {}",
        contenutoOriginale.getId());

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoOriginale.getDocumentoPadre());
    output.setContenutoSorgente(contenutoOriginale);
    output.setUuidNodo(indexEntity.getUid());

    output.setDimensione(null);
    output.setNomeFile(null);
    output.setFormatoFile(null);

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.SBUSTATO.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto sbustato {} a partire da contenuto originale {} ",
        indexEntity.getUid(), contenutoOriginale.getUuidNodo());

    return output;
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoFirmato(CosmoTContenutoDocumento contenutoOriginale,
      Entity indexEntity, CosmoDTipoFirma tipoFirma, String nomeFileFirmato,
      CosmoDFormatoFile formatoFile) {
    final var method = "creaContenutoFirmato";

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoOriginale.getDocumentoPadre());
    output.setContenutoSorgente(contenutoOriginale);
    output.setUuidNodo(indexEntity.getUid());
    output.setDimensione(contenutoOriginale.getDimensione());
    output.setNomeFile(nomeFileFirmato);
    output.setFormatoFile(formatoFile);
    output.setUuidNodo(indexEntity.getUid());
    output.setTipoFirma(tipoFirma);

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.FIRMATO.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto firmato {} a partire da contenuto originale {} ",
        indexEntity.getUid(), contenutoOriginale.getUuidNodo());

    return output;
  }

  @Override
  public CosmoDFormatoFile findFormatoByMime(String mime) {
    final var method = "findFormatoByMime";
    logger.debug(method, "ricerco del formato file a partire dal mimetype {}", mime);

    // fallback if missing
    if (StringUtils.isBlank(mime)) {
      mime = "application/octet-stream";
    }
    // cleanup
    if (mime.contains(";")) {
      mime = mime.substring(0, mime.indexOf(';'));
    }
    final var queryMime = mime.strip();

    var formati = formatoFileRepository
        .findAllActive((Root<CosmoDFormatoFile> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> cb
            .equal(cb.upper(root.get(CosmoDFormatoFile_.mimeType)), queryMime.toUpperCase()));

    if (formati.isEmpty()) {
      logger.error(method,
          "Il mimetype " + queryMime + " del documento fornito non e' presente nel sistema");
      throw new BadRequestException("Il tipo del documento fornito non e' gestito dal sistema.");
    }

    var result = formati.get(0);

    logger.debug(method, "il mimetype {} corrisponde al formato file {}", mime, result);
    return result;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean verificaFirma(CosmoTContenutoDocumento contenuto) {
    final var method = "verificaFirma";
    if (contenuto == null || StringUtils.isBlank(contenuto.getUuidNodo())) {
      logger.error(method, "Contenuto non presente");
      throw new InvalidParameterException();
    }

    logger.debug(method, "avvio verifica firma contenuto {} con index UID {}", contenuto.getId(),
        contenuto.getUuidNodo());

    var dataInizioVerifica = LocalDateTime.now();

    SignatureVerificationParameters verificaParams = new SignatureVerificationParameters();
    verificaParams.setVerifyCertificateList(true);
    var risultatoVerifica = indexFeignClient.verificaFirma(contenuto.getUuidNodo(), verificaParams);

    if (logger.isDebugEnabled()) {

      logger.debug(method,
          "esito verifica firma contenuto {} con index UID {} da Index: "
              + ObjectUtils.represent(risultatoVerifica),
              contenuto.getId(), contenuto.getUuidNodo());
    }

    // cancello informazioni precedenti
    infoVerificaFirmaRepository.deleteByContenutoDocumentoPadre(contenuto);

    boolean complessivamenteValida = true;
    boolean singolaFirmaComplessivamenteValida;

    while (risultatoVerifica != null) {
      if (!Boolean.TRUE.equals(risultatoVerifica.isValida())) {
        complessivamenteValida = false;
      }

      singolaFirmaComplessivamenteValida = verificaFirmaSingoloOutput(contenuto, risultatoVerifica);
      if (!singolaFirmaComplessivamenteValida) {
        complessivamenteValida = false;
      }

      risultatoVerifica = risultatoVerifica.getChild();
    }

    var esito = new CosmoDEsitoVerificaFirma();
    esito.setCodice(complessivamenteValida ? EsitoVerificaFirma.VALIDA.name()
        : EsitoVerificaFirma.NON_VALIDA.name());

    contenuto.setDataVerificaFirma(Timestamp.valueOf(dataInizioVerifica));
    contenuto.setEsitoVerificaFirma(esito);
    CosmoDTipoContenutoFirmato tipoContenutoFirmato = new CosmoDTipoContenutoFirmato();
    tipoContenutoFirmato.setCodice(TipoContenutoFirmato.FIRMA_DIGITALE.getCodice());
    contenuto.setTipoContenutoFirmato(tipoContenutoFirmato);

    cosmoTContenutoDocumentoRepository.save(contenuto);

    logger.debug(method, "terminata verifica firma contenuto {} con esito: {}", contenuto.getId(),
        esito);

    return complessivamenteValida;
  }

  @Override
  public boolean isTemporaneo(Long idDocumento, Long idContenuto) {
    var contenuto = getById(idDocumento, idContenuto);
    return contenuto.getTipo() != null
        && TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice());
  }

  @Override
  public void validaDimensioneDocumento(CosmoDTipoDocumento tipoDocumento,
      Long dimensioneDocumento) {
    CommonUtils.require(dimensioneDocumento, "dimensioneDocumento");
    if (dimensioneDocumento == 0) {
      logger.error("validaDimensioneDocumento", ErrorMessages.DOCUMENTO_VUOTO);
      throw new BadRequestException(ErrorMessages.DOCUMENTO_VUOTO);
    }
    if (tipoDocumento != null && tipoDocumento.getDimensioneMassima() != null
        && dimensioneDocumento != null
        && tipoDocumento.getDimensioneMassima() < (dimensioneDocumento / (1024L * 1024L))) {
      String message =
          String.format(ErrorMessages.DIMENSIONE_DOCUMENTO_SUPERIORE_AL_MASSIMO_CONFIGURATO,
              tipoDocumento.getDescrizione());
      logger.error("validaDimensioneDocumento", message);
      throw new PayloadTooLargeException(message);
    }
  }

  @Override
  public byte[] getContenutoIndex(Long idDocumento, Long idContenuto) {
    String methodName = "getContenutoIndex";
    CommonUtils.require(idDocumento, ID_DOCUMENTO);
    CommonUtils.require(idContenuto, ID_CONTENUTO);

    var contenuto = this.getById(idDocumento, idContenuto);
    if (TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {
      // disponibile solo come contenuto temporaneo
      logger.error(methodName,
          ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
      throw new UnsupportedOperationException(
          ErrorMessages.D_DOWNLOAD_DIRETTO_NON_DISPONIBILE_CONTENUTI_TEMPORANEI);
    }

    if (StringUtils.isBlank(contenuto.getUuidNodo())) {
      logger.error(methodName, ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
      throw new InternalServerException(ErrorMessages.D_CONTENUTO_FISICO_NON_LOCALIZZABILE);
    }

    // controlla se esistono share valide
    ShareOptions options = new ShareOptions();
    options.setFilename(contenuto.getNomeFile());
    options.setSource(IndexShareScope.INTERNET.name());
    options.setToDate(OffsetDateTime.now().plusMinutes(30));
    options.setContentDisposition(IndexContentDisposition.INLINE.name());

    var response = getExistingValidShare(contenuto.getUuidNodo(), options)
        .map(ShareDetail::getDownloadUri).orElseGet(() -> {
          CondivisioniRequest request = new CondivisioniRequest();
          request.setSourceIdentifier(contenuto.getUuidNodo());
          request.setOptions(options);
          return indexFeignClient.share(request).getDownloadUri();
        });

    try {
      var endpoint = new URI(response);
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers
      .setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
      HttpEntity<String> entity = new HttpEntity<>(headers);

      ResponseEntity<byte[]> result =
          restTemplate.exchange(endpoint, HttpMethod.GET, entity, byte[].class);

      return result.getBody();
    } catch (URISyntaxException e) {
      logger.error(methodName,
          "Errore nella creazione dell'URI dell'url: " + response);
      throw new InternalServerException("Errore nella creazione dell'URI dell'url: " + response);
    }
  }

  @Override
  public String generaSha256PerFile(RetrievedContent file) {
    String methodName = "generaSha256PerFile";
    String sha256hex = "";
    try {
      sha256hex = DigestUtils.sha256Hex(file.getContentStream());
    } catch (IOException e) {
      logger.error(methodName,
          "Errore nella generazione dello sha per il file: " + file.getFilename());
      throw new InternalServerException("Errore nella generazione dello sha per il file: " + file.getFilename());
    }
    return sha256hex;
  }

  @Override
  public String generaSha256PerFile(byte[] file) {
    return DigestUtils.sha256Hex(file);
  }

  @Override
  public CosmoTContenutoDocumento creaContenutoFirmatoFea(
      CosmoTContenutoDocumento contenutoOriginale, Entity indexEntity, String nomeFileFirmato) {
    final var method = "creaContenutoFirmatoFea";

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoOriginale.getDocumentoPadre());
    output.setContenutoSorgente(contenutoOriginale);
    output.setUuidNodo(indexEntity.getUid());
    output.setDimensione(contenutoOriginale.getDimensione());
    output.setNomeFile(nomeFileFirmato);
    CosmoDTipoContenutoFirmato tipoContenutoFirmato = new CosmoDTipoContenutoFirmato();
    tipoContenutoFirmato.setCodice(TipoContenutoFirmato.FIRMA_ELETTRONICA_AVANZATA.getCodice());
    output.setTipoContenutoFirmato(tipoContenutoFirmato);
    var formatoFilePDF = new CosmoDFormatoFile();
    formatoFilePDF.setCodice("application/pdf");
    output.setFormatoFile(formatoFilePDF);
    output.setUuidNodo(indexEntity.getUid());

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.FIRMATO.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto firmato fea {} a partire da contenuto originale {} ",
        indexEntity.getUid(), contenutoOriginale.getUuidNodo());

    return output;
  }

  public boolean verificaFirmaSingoloOutput(CosmoTContenutoDocumento contenuto,
      VerifyReport risultato) {

    boolean complessivamenteValida = true;

    if (!Boolean.TRUE.equals(risultato.isValida())) {
      complessivamenteValida = false;
    }

    boolean singolaFirmaComplessivamenteValida;

    if (risultato.getSignature() != null) {
      for (Signature signature2 : risultato.getSignature()) {
        singolaFirmaComplessivamenteValida =
            verificaFirmaSingoloOutput(contenuto, signature2, null);
        if (!singolaFirmaComplessivamenteValida) {
          complessivamenteValida = false;
        }
      }
    }

    return complessivamenteValida;
  }

  public boolean verificaFirmaSingoloOutput(CosmoTContenutoDocumento contenuto, Signature signature,
      CosmoTInfoVerificaFirma parent) {

    boolean complessivamenteValida = true;

    if (!Boolean.TRUE.equals(signature.isValida())) {
      complessivamenteValida = false;
    }

    var record = new CosmoTInfoVerificaFirma();

    record.setContenutoDocumentoPadre(parent == null ? contenuto : null);
    record.setInfoVerificaFirmaPadre(parent);
    record.setCfFirmatario(signature.getCodiceFiscale());
    record.setDtVerificaFirma(signature.getDataOraVerifica() != null
        ? Timestamp.valueOf(signature.getDataOraVerifica().toLocalDateTime())
        : null);
    record.setDtApposizione(!(Boolean.TRUE.equals(signature.isTimestamped()))
        ? Timestamp.valueOf(signature.getDataOra().toLocalDateTime())
        : null);
    record.setDtApposizioneMarcaturaTemporale(Boolean.TRUE.equals(signature.isTimestamped())
        ? Timestamp.valueOf(signature.getDataOra().toLocalDateTime())
        : null);
    record.setFirmatario(signature.getFirmatario());
    record.setOrganizzazione(signature.getOrganizzazione());
    record.setCodiceErrore(signature.getErrorCode() != null ? signature.getErrorCode() : null);

    var esito = new CosmoDEsitoVerificaFirma();
    esito.setCodice(Boolean.TRUE.equals(signature.isValida()) ? EsitoVerificaFirma.VALIDA.name()
        : EsitoVerificaFirma.NON_VALIDA.name());

    record.setEsito(esito);
    record = infoVerificaFirmaRepository.save(record);

    boolean singolaFirmaComplessivamenteValida;

    if (signature.getSignature() != null) {
      for (Signature signature2 : signature.getSignature()) {
        singolaFirmaComplessivamenteValida =
            verificaFirmaSingoloOutput(contenuto, signature2, record);
        if (!singolaFirmaComplessivamenteValida) {
          complessivamenteValida = false;
        }
      }
    }

    return complessivamenteValida;
  }

  private Optional<ShareDetail> getExistingValidShare(String nodeUUID, ShareOptions options) {
    ListShareDetail shares = indexFeignClient.shareId(nodeUUID);
    OffsetDateTime now = OffsetDateTime.now();

    if (shares == null) {
      shares = new ListShareDetail();
    }

    if (shares.getListShareDetail() == null) {
      shares.setListShareDetail(new LinkedList<>());
    }

    //@formatter:off
    return shares.getListShareDetail().stream()
        .filter(s ->
        (options.getSource() == null || options.getSource().equals(s.getSource())) &&
        (s.getFromDate() == null || s.getFromDate().atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime().isBefore(now)) &&
        (options.getContentDisposition() == null || (s.getContentDisposition() != null &&
        s.getContentDisposition().toUpperCase().startsWith(options.getContentDisposition().toUpperCase() + ";")))
            )
        .filter(s -> {
          // se vogliamo esporre il link fino ad una certa data
          // possiamo riutilizzare uno share esistente che scada dopo quella data
          if (options.getToDate() != null && s.getToDate() != null
              && s.getToDate().atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime().isBefore(options.getToDate())) {
            return false;
          }

          // se vogliamo esporre il link a tempo indefinito
          // possimamo riutilizzare solo uno share che non abbia data di scadenza
          if (options.getToDate() == null && s.getToDate() != null) {
            return false;
          }

          return true;
        })
        .findAny();
    //@formatter:on
  }

  private FileContent fromContenuto(CosmoTContenutoDocumento contenuto) {
    final var methodName = "fromContenuto";
    FileContent output = new FileContent();

    if (!TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {

      Entity indexEntity = indexFeignClient.getFile(contenuto.getUuidNodo(), true);

      if (indexEntity == null || indexEntity.getContent() == null) {
        logger.error(methodName, ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
        throw new NotFoundException(ErrorMessages.I_NESSUN_CONTENUTO_DA_SCARICARE);
      }

      output.setContent(indexEntity.getContent());

      if (contenuto.getFormatoFile() == null
          || StringUtils.isBlank(contenuto.getFormatoFile().getMimeType())) {
        output.setMimeType(indexEntity.getMimeType());
      } else {
        output.setMimeType(contenuto.getFormatoFile().getMimeType());
      }

      if (StringUtils.isBlank(contenuto.getNomeFile())) {
        output.setFileName(indexEntity.getFilename());
      } else {
        output.setFileName(contenuto.getNomeFile());
      }

    } else {
      return fromContenutoTemporaneo(contenuto);
    }

    return output;
  }

  private FileContent fromContenutoTemporaneo(CosmoTContenutoDocumento contenuto) {
    final var methodName = "fromContenutoTemporaneo";
    FileContent output = new FileContent();

    try {
      RetrievedContent file = fileShareService.get(contenuto.getUuidNodo(),
          contenuto.getDtInserimento().toLocalDateTime().toLocalDate());

      output.setContent(file.getContentStream().readAllBytes());
      output.setMimeType(file.getContentType());

      if (StringUtils.isBlank(contenuto.getNomeFile())) {
        output.setFileName(file.getFilename());
      } else {
        output.setFileName(contenuto.getNomeFile());
      }
    } catch (IOException e) {
      logger.error(methodName,
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, contenuto.getUuidNodo()));
      throw new NotFoundException(
          String.format(ErrorMessages.FS_UUID_FILE_NON_TROVATO, contenuto.getUuidNodo()));
    }

    return output;
  }

  private void cancellaContenutoFisico(CosmoTContenutoDocumento contenuto) {
    final var method = "cancellaContenutoFisico";

    if (TipoContenutoDocumento.TEMPORANEO.name().equals(contenuto.getTipo().getCodice())) {
      // cancello da filesystem
      try {
        RetrievedContent fileDaCancellare = fileShareService.get(contenuto.getUuidNodo(),
            contenuto.getDtInserimento().toLocalDateTime().toLocalDate());
        if (fileDaCancellare != null) {
          logger.debug(method, "elimino da fileshare il file {}", fileDaCancellare.getUploadUUID());
          fileShareService.delete(fileDaCancellare);
        }
      } catch (FileShareRetrievalException e) {
        logger.warn(method, "impossibile eliminare da fileshare il file {} in quanto mancante",
            contenuto.getUuidNodo());
      } catch (Exception e) {
        logger.warn(method, "impossibile eliminare da fileshare il file " + contenuto.getUuidNodo(),
            e);
      }

    } else {
      // cancello da index
      try {
        logger.debug(method, "elimino da index il nodo {}", contenuto.getUuidNodo());
        indexFeignClient.deleteIdentifier(contenuto.getUuidNodo());

      } catch (Exception e) {
        logger.error(method, "impossibile eliminare da index il file " + contenuto.getUuidNodo(),
            e);
      }
    }
  }
}
