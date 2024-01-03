/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK_;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.CosmoTOtp;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.business.service.MailService;
import it.csi.cosmo.cosmoecm.business.service.OtpService;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoRUtenteEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTOtpRepository;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoecm.integration.repository.specifications.CosmoTOtpSpecifications;
import it.csi.cosmo.cosmoecm.security.SecurityUtils;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class OtpServiceImpl implements OtpService {

  private static final int OTP_LENGTH = 5;

  private static final String CLASS_NAME = OtpServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String DURATA_OTP_FEA = "durata.otp.fea";

  private static final String ERRORE_INVIO_MAIL = "Errore invio email";

  @Autowired
  private CosmoTOtpRepository cosmoTOtpRepository;


  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoRUtenteEnteRepository cosmoRUtenteEnteRepository;

  @Autowired
  private CosmoCConfigurazioneEnteRepository configurazioneEnteRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private MailService mailService;


  private Random random = new Random();

  @Override
  public String createOtp() {


    String values = "0123456789";


    char[] otp = new char[OTP_LENGTH];

    for (int i = 0; i < OTP_LENGTH; i++) {
      otp[i] = values.charAt(random.nextInt(values.length()));
    }

    return new String(otp);
  }

  @Transactional
  @Override
  public void richiediOtp() {
    final String methodName = "richiediOtp";

    var utente = SecurityUtils.getUtenteCorrente();
    if (utente == null) {
      logger.error(methodName, "Utente non autenticato");
      throw new BadRequestException("Utente non autenticato");
    }

    var ente = utente.getEnte();
    if (ente == null) {
      logger.error(methodName, "Utente non autenticato presso un ente");
      throw new BadRequestException("Utente non autenticato presso un ente");
    }



    int limit = getDurataOtp(ente.getId());


    var otps = cosmoTOtpRepository
        .findAll(CosmoTOtpSpecifications.findByUtenteEnte(utente.getId(), ente.getId()));
    var utenteDB = cosmoTUtenteRepository.findOneNotDeleted(utente.getId()).orElse(null);

    if (utenteDB == null) {
      logger.error(methodName, "Utente non presente");
      throw new BadRequestException("Utente non presente");
    }

    var enteDB = cosmoTEnteRepository.findOneNotDeleted(ente.getId()).orElse(null);
    if (enteDB == null) {
      logger.error(methodName, "Ente non presente");
      throw new BadRequestException("Ente non presente");
    }

    if (otps == null || otps.isEmpty()) {

      CosmoTOtp otp = new CosmoTOtp();
      otp.setIdUtente(utenteDB);
      otp.setCosmoTEnte(enteDB);
      otp.setValore(createOtp());
      otp.setUtilizzato(false);
      otp.setDtScadenza(Timestamp.valueOf(LocalDateTime.now().plusMinutes(limit)));

      otp = this.cosmoTOtpRepository.saveAndFlush(otp);
      inviaOtp(otp);

    } else {

      var otp = otps.get(0);

      String valore = null;

      do {
        valore = createOtp();
      } while (valore.equals(otp.getValore()));

      if (Boolean.FALSE.equals(otp.getUtilizzato())) {

        otp.setValore(valore);
        otp.setDtCancellazione(null);
        otp.setDtScadenza(Timestamp.valueOf(LocalDateTime.now().plusMinutes(limit)));

        otp = this.cosmoTOtpRepository.saveAndFlush(otp);
        inviaOtp(otp);

      } else {


        otp.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
        otp.setUtenteCancellazione(utente.getCodiceFiscale());
        this.cosmoTOtpRepository.saveAndFlush(otp);

        CosmoTOtp otpDaSalvare = new CosmoTOtp();
        otpDaSalvare.setCosmoTEnte(enteDB);
        otpDaSalvare.setIdUtente(utenteDB);
        otpDaSalvare.setValore(valore);
        otpDaSalvare.setUtilizzato(false);
        otpDaSalvare.setDtScadenza(Timestamp.valueOf(LocalDateTime.now().plusMinutes(limit)));

        otpDaSalvare = this.cosmoTOtpRepository.saveAndFlush(otpDaSalvare);
        inviaOtp(otpDaSalvare);

      }

    }

  }

  private int getDurataOtp(Long idEnte) {
    final String methodName = "getDurataOtp";
    int limit = 0;

    CosmoCConfigurazioneEnte scadenza = configurazioneEnteRepository
        .findOne((root, cq, cb) -> cb.and(cb.isNull(root.get(CosmoCEntity_.dtFineVal)),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                idEnte),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                DURATA_OTP_FEA)));
    if (scadenza == null || scadenza.getValore() == null || scadenza.getValore().isBlank()) {

      try {

        limit = configurazioneService.requireConfig(ParametriApplicativo.DURATA_OTP_FEA_DEFAULT)
            .asInteger();

      } catch (Exception e) {
        logger.error(methodName, "Durata Otp FEA non configurata");
        throw new BadRequestException("Durata Otp FEA non configurata");
      }

    } else {
      limit = Integer.valueOf(scadenza.getValore());
    }

    return limit;


  }

  private void inviaOtp(CosmoTOtp otp) {

    final String methodName = "inviaOtp";


    var utenteEnte = cosmoRUtenteEnteRepository.findByCosmoTEnteAndCosmoTUtente(otp.getCosmoTEnte(),
        otp.getIdUtente());

    if(utenteEnte==null || utenteEnte.nonValido(Timestamp.valueOf(LocalDateTime.now()))) {
      logger.error(methodName, "Associazione utente ente non valida");
      throw new BadRequestException("Associazione utente ente non valida");
    }

    if(utenteEnte.getEmail()==null) {
      logger.error(methodName, "Indirizzo email non presente");
      throw new BadRequestException("Indirizzo email non presente");
    }


    var mail = mailService.inviaMailOtp(otp.getValore(), utenteEnte.getEmail());

    try {
      if (mail.get() == null) {
        logger.error(methodName, ERRORE_INVIO_MAIL);
        throw new BadRequestException(ERRORE_INVIO_MAIL);
      }
    } catch (InterruptedException | ExecutionException e) {
      logger.error(methodName, "Errore invio email: " + e.getMessage());
      throw new BadRequestException(ERRORE_INVIO_MAIL);
    }
  }

}
