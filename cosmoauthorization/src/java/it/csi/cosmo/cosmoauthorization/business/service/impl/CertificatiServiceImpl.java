/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.cripto.CryptoConverter;
import it.csi.cosmo.common.cripto.FilesystemConfigurationProvider;
import it.csi.cosmo.common.entities.CosmoDEnteCertificatore;
import it.csi.cosmo.common.entities.CosmoDProfiloFeq;
import it.csi.cosmo.common.entities.CosmoDSceltaMarcaTemporale;
import it.csi.cosmo.common.entities.CosmoDTipoCredenzialiFirma;
import it.csi.cosmo.common.entities.CosmoDTipoOtp;
import it.csi.cosmo.common.entities.CosmoTCertificatoFirma;
import it.csi.cosmo.common.entities.CosmoTCertificatoFirma_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.CertificatiService;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.config.ParametriApplicativo;
import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTCertificatoFirmaMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDEnteCertificatoreRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDProfiloFeqRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDSceltaMarcaTemporaleRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoCredenzialiFirmaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoDTipoOtpRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTCertificatoFirmaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class CertificatiServiceImpl implements CertificatiService {

  private static final String CLASS_NAME = CertificatiServiceImpl.class.getSimpleName();

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS");

  private static final String ID_CERTIFICATO = "id certificato";

  @Autowired
  private CosmoTCertificatoFirmaRepository cosmoTCertificatoFirmaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoDEnteCertificatoreRepository cosmoDEnteCertificatoreRepository;

  @Autowired
  private CosmoDProfiloFeqRepository cosmoDProfiloFeqRepository;

  @Autowired
  private CosmoDSceltaMarcaTemporaleRepository cosmoDSceltaMarcaTemporaleRepository;

  @Autowired
  private CosmoDTipoCredenzialiFirmaRepository cosmoDTipoCredenzialiFirmaRepository;

  @Autowired
  private CosmoDTipoOtpRepository cosmoDTipoOtpRepository;

  @Autowired
  private CosmoTCertificatoFirmaMapper certificatoFirmaMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired(required = false)
  private CryptoConverter cryptoConverter;

  @Override
  @Transactional(readOnly = true)
  public CertificatoFirma getCertificato(String idCertificato) {
    final String methodName = "getCertificato";

    ValidationUtils.require(idCertificato, ID_CERTIFICATO);

    if (!StringUtils.isNumeric(idCertificato)) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, ID_CERTIFICATO));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, ID_CERTIFICATO));
    }

    CosmoTCertificatoFirma certificatoFirma = cosmoTCertificatoFirmaRepository
        .findOneByField(CosmoTCertificatoFirma_.id, Long.valueOf(idCertificato))
        .orElseThrow(() -> {
          String message =
              String.format(ErrorMessages.CF_CERTIFICATO_FIRMA_NON_TROVATO, idCertificato);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    CertificatoFirma output = certificatoFirmaMapper.toDTO(certificatoFirma);
    decriptazione(certificatoFirma, output);

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public List<CertificatoFirma> getCertificati() {

    List<CosmoTCertificatoFirma> certificatiDB =
        cosmoTCertificatoFirmaRepository.findAllByCosmoTUtenteCodiceFiscaleAndDtCancellazioneIsNull(
            SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    List<CertificatoFirma> output = new LinkedList<>();

    certificatiDB.forEach(certificatoDB -> {
      CertificatoFirma certificatoDTO = certificatoFirmaMapper.toDTO(certificatoDB);
      decriptazione(certificatoDB, certificatoDTO);
      output.add(certificatoDTO);
    });

    return output;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificatoFirma postCertificato(CertificatoFirma body) {
    final String methodName = "postCertificato";

    ValidationUtils.require(body, "certificato");
    ValidationUtils.validaAnnotations(body);

    if (null != body.getId()) {
      String message = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, "id");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    CosmoTUtente utente = cosmoTUtenteRepository
        .findByCodiceFiscale(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    if (null == utente) {
      logger.error(methodName, ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    CosmoTCertificatoFirma certificatoDaSalvare = new CosmoTCertificatoFirma();

    certificatoDaSalvare.setDescrizione(body.getDescrizione());

    setImpostazioniFirma(body, certificatoDaSalvare);

    criptazione(body, certificatoDaSalvare);

    certificatoDaSalvare.setCosmoTUtente(utente);
    certificatoDaSalvare.setUltimoUtilizzato(false);

    certificatoDaSalvare = cosmoTCertificatoFirmaRepository.save(certificatoDaSalvare);

    CertificatoFirma output = certificatoFirmaMapper.toDTO(certificatoDaSalvare);

    decriptazione(certificatoDaSalvare, output);

    return output;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificatoFirma putCertificato(String idCertificato, CertificatoFirma body) {
    final String methodName = "putCertificato";

    ValidationUtils.require(idCertificato, ID_CERTIFICATO);
    ValidationUtils.require(body, "certificato");
    ValidationUtils.validaAnnotations(body);

    if (!StringUtils.isNumeric(idCertificato)) {
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, ID_CERTIFICATO));
    }

    CosmoTCertificatoFirma certificatoFirmaDaAggiornare = cosmoTCertificatoFirmaRepository
        .findOneByField(CosmoTCertificatoFirma_.id, Long.valueOf(idCertificato))
        .orElseThrow(() -> {
          String message =
              String.format(ErrorMessages.CF_CERTIFICATO_FIRMA_NON_TROVATO, idCertificato);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    CosmoTUtente utente = cosmoTUtenteRepository
        .findByCodiceFiscale(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    if (null == utente) {
      logger.error(methodName, ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    if (!utente.getCodiceFiscale()
        .equals(certificatoFirmaDaAggiornare.getCosmoTUtente().getCodiceFiscale())) {
      logger.error(methodName, ErrorMessages.CF_UTENTE_CERTIFICATO_FIRMA_NON_CORRISPONDENTE);
      throw new BadRequestException(ErrorMessages.CF_UTENTE_CERTIFICATO_FIRMA_NON_CORRISPONDENTE);
    }

    certificatoFirmaDaAggiornare.setDescrizione(body.getDescrizione());

    setImpostazioniFirma(body, certificatoFirmaDaAggiornare);

    certificatoFirmaDaAggiornare.setCosmoTUtente(utente);

    criptazione(body, certificatoFirmaDaAggiornare);

    certificatoFirmaDaAggiornare =
        cosmoTCertificatoFirmaRepository.save(certificatoFirmaDaAggiornare);

    CertificatoFirma output = certificatoFirmaMapper.toDTO(certificatoFirmaDaAggiornare);

    decriptazione(certificatoFirmaDaAggiornare, output);

    return output;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificatoFirma deleteCertificato(String idCertificato) {
    final String methodName = "deleteCertificato";

    ValidationUtils.require(idCertificato, ID_CERTIFICATO);

    if (!StringUtils.isNumeric(idCertificato)) {
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, ID_CERTIFICATO));
    }

    CosmoTCertificatoFirma certificatoFirma = cosmoTCertificatoFirmaRepository
        .findOneByField(CosmoTCertificatoFirma_.id, Long.valueOf(idCertificato))
        .orElseThrow(() -> {
          String message =
              String.format(ErrorMessages.CF_CERTIFICATO_FIRMA_NON_TROVATO, idCertificato);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    certificatoFirma.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    certificatoFirma.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    certificatoFirma = cosmoTCertificatoFirmaRepository.save(certificatoFirma);

    return certificatoFirmaMapper.toDTO(certificatoFirma);
  }

  private void setImpostazioniFirma(CertificatoFirma dto, CosmoTCertificatoFirma tFirma) {
    String methodName = "validazioneImpostazioniFirma";

    CosmoDEnteCertificatore enteCertificatore = cosmoDEnteCertificatoreRepository
        .findOneActive(dto.getEnteCertificatore().getCodice()).orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.CF_ENTE_CERTIFICATORE_NON_VALIDO,
              dto.getEnteCertificatore().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });
    tFirma.setCosmoDEnteCertificatore(enteCertificatore);

    CosmoDProfiloFeq profiloFEQ = cosmoDProfiloFeqRepository
        .findOneActive(dto.getProfiloFEQ().getCodice()).orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.CF_PROFILO_FEQ_NON_VALIDO,
              dto.getProfiloFEQ().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });
    tFirma.setCosmoDProfiloFeq(profiloFEQ);

    CosmoDSceltaMarcaTemporale sceltaTemporale = cosmoDSceltaMarcaTemporaleRepository
        .findOneActive(dto.getSceltaMarcaTemporale().getCodice()).orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.CF_SCELTA_TEMPORALE_NON_VALIDA,
              dto.getSceltaMarcaTemporale().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });
    tFirma.setCosmoDSceltaMarcaTemporale(sceltaTemporale);

    CosmoDTipoCredenzialiFirma tipoCredenzialiFirma = cosmoDTipoCredenzialiFirmaRepository
        .findOneActive(dto.getTipoCredenzialiFirma().getCodice()).orElseThrow(() -> {
          String notFound = String.format(ErrorMessages.CF_TIPO_CREDENZIALI_FIRMA_NON_VALIDO,
              dto.getTipoCredenzialiFirma().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });
    tFirma.setCosmoDTipoCredenzialiFirma(tipoCredenzialiFirma);

    CosmoDTipoOtp tipoOTP =
        cosmoDTipoOtpRepository.findOneActive(dto.getTipoOTP().getCodice()).orElseThrow(() -> {
          String notFound =
              String.format(ErrorMessages.CF_TIPO_OTP_NON_VALIDO, dto.getTipoOTP().getCodice());
          logger.error(methodName, notFound);
          throw new NotFoundException(notFound);
        });
    tFirma.setCosmoDTipoOtp(tipoOTP);

  }

  private void criptazione(CertificatoFirma dto, CosmoTCertificatoFirma tFirma) {

    CryptoConverter cc = getCryptoConverter();

    tFirma.setUsername(cc.encrypt(dto.getUsername()));
    tFirma.setPin(cc.encrypt(dto.getPin()));
    if (!StringUtils.isEmpty(dto.getPassword())) {
      tFirma.setPassword(cc.encrypt(dto.getPassword()));
    }
    if (null != dto.getDataScadenza()) {

      LocalDateTime converted =
          dto.getDataScadenza().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

      tFirma.setDtScadenza(cc.encrypt(formatter.format(converted)));
    } else {
      tFirma.setDtScadenza(null);
    }
  }

  private void decriptazione(CosmoTCertificatoFirma tFirma, CertificatoFirma dto) {

    CryptoConverter cc = getCryptoConverter();

    dto.setUsername(cc.decrypt(tFirma.getUsername()));
    dto.setPin(cc.decrypt(tFirma.getPin()));
    if (!StringUtils.isEmpty(tFirma.getPassword())) {
      dto.setPassword(cc.decrypt(tFirma.getPassword()));
    }
    if (StringUtils.isNotBlank(tFirma.getDtScadenza())) {
      String decryptedDate = cc.decrypt(tFirma.getDtScadenza());
      OffsetDateTime zoned = LocalDateTime.parse(decryptedDate, formatter)
          .atZone(ZoneId.systemDefault()).toOffsetDateTime();
      dto.setDataScadenza(zoned);

    }
  }

  private synchronized CryptoConverter getCryptoConverter() {
    if (cryptoConverter == null) {
      //@formatter:off
      cryptoConverter = CryptoConverter.builder()
          .withLoggingPrefix(LogCategory.BUSINESS_LOG_CATEGORY.getCategory())
          .withConfigurationProvider(FilesystemConfigurationProvider.builder()
              .withLoggingPrefix(LogCategory.BUSINESS_LOG_CATEGORY.getCategory())
              .withFile(configurazioneService.requireConfig(ParametriApplicativo.CRYPTO_CONFIGURATION_FILE).asString())
              .build()
              )
          .build();
      //@formatter:on
    }

    return cryptoConverter;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CertificatoFirma putUltimoCertificatoUsato(String idCertificato) {
    final String methodName = "putUltimoCertificatoUsato";

    if (!StringUtils.isNumeric(idCertificato)) {
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO_CORRETTAMENTE, ID_CERTIFICATO));
    }

    CosmoTCertificatoFirma certificatoFirmaDaAggiornare = cosmoTCertificatoFirmaRepository
        .findOneNotDeletedByField(CosmoTCertificatoFirma_.id, Long.valueOf(idCertificato))
        .orElseThrow(() -> {
          String message =
              String.format(ErrorMessages.CF_CERTIFICATO_FIRMA_NON_TROVATO, idCertificato);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    var utente = SecurityUtils.getUtenteCorrente();

    if(utente==null) {
      throw new BadRequestException("Utente non autenticato");
    }

    List<CosmoTCertificatoFirma> listaCertificatiUsati = cosmoTCertificatoFirmaRepository.findAllNotDeleted((root,cq,cb)->{
      var predicate =
          cb.and(cb.equal(root.get(CosmoTCertificatoFirma_.ultimoUtilizzato), Boolean.TRUE),
              cb.equal(root.get(CosmoTCertificatoFirma_.cosmoTUtente).get(CosmoTUtente_.id),
                  utente.getId()));
      return cq.where(predicate).getRestriction();
    });

    listaCertificatiUsati.forEach(certificato -> {
      certificato.setUltimoUtilizzato(false);
      cosmoTCertificatoFirmaRepository.save(certificato);
    });

    certificatoFirmaDaAggiornare.setUltimoUtilizzato(Boolean.TRUE);

    certificatoFirmaDaAggiornare =
        cosmoTCertificatoFirmaRepository.save(certificatoFirmaDaAggiornare);

     return certificatoFirmaMapper.toDTO(certificatoFirmaDaAggiornare);
  }
}
