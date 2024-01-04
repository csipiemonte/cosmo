/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDEsitoVerificaFirma;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoFirmato;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.entities.enums.EsitoVerificaFirma;
import it.csi.cosmo.common.entities.enums.TipoContenutoDocumento;
import it.csi.cosmo.common.entities.enums.TipoContenutoFirmato;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.Signature;
import it.csi.cosmo.cosmosoap.dto.rest.SignatureVerificationParameters;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTContenutoDocumentoRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTInfoVerificaFirmaRepository;
import it.csi.cosmo.cosmosoap.security.SecurityUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ContenutoDocumentoServiceImpl implements ContenutoDocumentoService {

  private static final String CLASS_NAME = ContenutoDocumentoServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private Api2IndexService api2IndexService;

  @Autowired
  private CosmoTInfoVerificaFirmaRepository cosmoTInfoVerificaFirmaRepository;

  @Autowired
  private CosmoTContenutoDocumentoRepository cosmoTContenutoDocumentoRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  public CosmoTContenutoDocumento creaContenutoFirmato(
      CosmoTContenutoDocumento contenutoOriginale,
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

    CosmoDTipoContenutoFirmato tipoContenutoFirmato = new CosmoDTipoContenutoFirmato();
    tipoContenutoFirmato.setCodice(TipoContenutoFirmato.FIRMA_DIGITALE.getCodice());
    output.setTipoContenutoFirmato(tipoContenutoFirmato);

    logger.info(method, "creato contenuto firmato {} a partire da contenuto originale {} ",
        indexEntity.getUid(), contenutoOriginale.getUuidNodo());

    return output;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public boolean verificaFirma(CosmoTContenutoDocumento contenuto) {
    final var method = "verificaFirma";

    var testMode = configurazioneService
        .requireConfig(ParametriApplicativo.REMOTESIGN_DOSIGN_TESTMODE_ENABLED).asBoolean();

    if (contenuto == null || StringUtils.isBlank(contenuto.getUuidNodo())) {
      throw new InvalidParameterException();
    }

    logger.debug(method, "avvio verifica firma contenuto {} con index UID {}", contenuto.getId(),
        contenuto.getUuidNodo());

    var dataInizioVerifica = LocalDateTime.now();

    SignatureVerificationParameters verificaParams = new SignatureVerificationParameters();
    verificaParams.setVerifyCertificateList(true);
    var risultatoVerifica = api2IndexService.verificaFirma(contenuto.getUuidNodo(), verificaParams);

    if (logger.isDebugEnabled()) {

      logger.debug(method,
          "esito verifica firma contenuto {} con index UID {} da Index: "
              + ObjectUtils.represent(risultatoVerifica),
          contenuto.getId(), contenuto.getUuidNodo());
    }

    // cancello informazioni precedenti
    cosmoTInfoVerificaFirmaRepository.deleteByContenutoDocumentoPadre(contenuto);

    boolean complessivamenteValida = true;
    boolean singolaFirmaComplessivamenteValida;

    if (Boolean.TRUE.equals(testMode)) {
      UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();
      var record = new CosmoTInfoVerificaFirma();

      record.setContenutoDocumentoPadre(contenuto);
      record.setInfoVerificaFirmaPadre(null);
      record.setCfFirmatario(userInfo.getCodiceFiscale());
      record.setDtVerificaFirma(Timestamp.valueOf(LocalDateTime.now()));
      record.setDtApposizione(Timestamp.valueOf(LocalDateTime.now()));
      // in fase di test con firma fake si parte dall'assunto che la marca temporale non sia mai
      // apposta
      // questo solamente sulle informazioni di firma!!!
      record.setDtApposizioneMarcaturaTemporale(null);
      record.setFirmatario(userInfo.getCognome() + " " + userInfo.getNome());
      record.setOrganizzazione(null);
      record.setCodiceErrore(null);

      var esito = new CosmoDEsitoVerificaFirma();
      esito.setCodice(EsitoVerificaFirma.VALIDA.name());

      record.setEsito(esito);
      cosmoTInfoVerificaFirmaRepository.save(record);
    } else {
      while (risultatoVerifica != null) {
        if (!Boolean.TRUE.equals(risultatoVerifica.isValida())) {
          complessivamenteValida = false;
        }

        singolaFirmaComplessivamenteValida =
            verificaFirmaSingoloOutput(contenuto, risultatoVerifica);
        if (!singolaFirmaComplessivamenteValida) {
          complessivamenteValida = false;
        }

        risultatoVerifica = risultatoVerifica.getChild();
      }
    }

    var esito = new CosmoDEsitoVerificaFirma();
    esito.setCodice(complessivamenteValida ? EsitoVerificaFirma.VALIDA.name()
        : EsitoVerificaFirma.NON_VALIDA.name());

    contenuto.setDataVerificaFirma(Timestamp.valueOf(dataInizioVerifica));
    contenuto.setEsitoVerificaFirma(esito);

    cosmoTContenutoDocumentoRepository.save(contenuto);

    logger.debug(method, "terminata verifica firma contenuto {} con esito: {}", contenuto.getId(),
        esito);

    return complessivamenteValida;
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
    record = cosmoTInfoVerificaFirmaRepository.save(record);

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
  public CosmoTContenutoDocumento creaContenutoSigilloElettronico(
      CosmoTContenutoDocumento contenutoOriginale, Entity indexEntity) {
    final var method = "creaContenutoSigilloElettronico";

    CosmoTContenutoDocumento output = new CosmoTContenutoDocumento();
    output.setDocumentoPadre(contenutoOriginale.getDocumentoPadre());
    output.setContenutoSorgente(contenutoOriginale);
    output.setUuidNodo(indexEntity.getUid());
    output.setDimensione(contenutoOriginale.getDimensione());
    output.setNomeFile(contenutoOriginale.getNomeFile());
    CosmoDTipoContenutoFirmato tipoContenutoFirmato = new CosmoDTipoContenutoFirmato();
    tipoContenutoFirmato.setCodice(TipoContenutoFirmato.SIGILLO_ELETTRONICO.getCodice());
    output.setTipoContenutoFirmato(tipoContenutoFirmato);
    output.setFormatoFile(contenutoOriginale.getFormatoFile());
    output.setUuidNodo(indexEntity.getUid());

    CosmoDTipoContenutoDocumento tipo = new CosmoDTipoContenutoDocumento();
    tipo.setCodice(TipoContenutoDocumento.FIRMATO.name());
    output.setTipo(tipo);

    logger.info(method, "creato contenuto con apposizione sigillo elettronico {} a partire da contenuto originale {} ",
        indexEntity.getUid(), contenutoOriginale.getUuidNodo());

    return output;

  }



}
