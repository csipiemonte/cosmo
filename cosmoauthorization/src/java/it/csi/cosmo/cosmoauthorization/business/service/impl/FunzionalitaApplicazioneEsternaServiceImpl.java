/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoREnteFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoREnteFunzionalitaApplicazioneEsternaPK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.FunzionalitaApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTFunzionalitaApplicazioneEsternaMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoREnteApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoREnteFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.CommonUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class FunzionalitaApplicazioneEsternaServiceImpl
implements FunzionalitaApplicazioneEsternaService {

  private static final String CLASS_NAME =
      FunzionalitaApplicazioneEsternaService.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  private static final String ID_APPLICAZIONE = "Id applicazione";
  private static final String ID_FUNZIONALITA = "Id funzionalita";

  @Autowired
  private CosmoTFunzionalitaApplicazioneEsternaRepository cosmoTFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoRUtenteFunzionalitaApplicazioneEsternaRepository cosmoRUtenteFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoREnteFunzionalitaApplicazioneEsternaRepository cosmoREnteFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoREnteApplicazioneEsternaRepository cosmoREnteApplicazioneEsternaRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;


  @Autowired
  private CosmoTFunzionalitaApplicazioneEsternaMapper funzionalitaMapper;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void eliminaFunzionalita(String idApplicazione, String idFunzionalita) {
    final String methodName = "eliminaFunzionalita";

    CommonUtils.validaDatiInput(idApplicazione, ID_APPLICAZIONE);

    CommonUtils.validaDatiInput(idFunzionalita, ID_FUNZIONALITA);

    CosmoTFunzionalitaApplicazioneEsterna funzionalita =
        cosmoTFunzionalitaApplicazioneEsternaRepository
        .findOneNotDeleted(Long.valueOf(idFunzionalita)).orElseThrow(() -> {
          final String message =
              String.format(ErrorMessages.AE_FUNZIONALITA_NON_TROVATA, idFunzionalita);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    if (!Long.valueOf(idApplicazione).equals(
        funzionalita.getCosmoREnteApplicazioneEsterna().getCosmoTApplicazioneEsterna().getId())) {
      logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
    }

    if (funzionalita.nonCancellato()) {
      funzionalita.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
      funzionalita.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

      cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalita);
    }

    funzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
    .forEach(funzionalitaAssociata -> {
      if (null == funzionalitaAssociata.getDtFineVal()
          || funzionalitaAssociata.getDtFineVal()
          .after(Timestamp.valueOf(LocalDateTime.now()))) {
        funzionalitaAssociata.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(funzionalitaAssociata);
      }
    });

    funzionalita.getCosmoREnteFunzionalitaApplicazioneEsternas()
    .forEach(enteFunzionalitaAssociata -> {
      if (null == enteFunzionalitaAssociata.getDtFineVal()
          || enteFunzionalitaAssociata.getDtFineVal()
          .after(Timestamp.valueOf(LocalDateTime.now()))) {
        enteFunzionalitaAssociata.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalitaAssociata);
      }
    });
  }

  @Override
  @Transactional(readOnly = true)
  public List<FunzionalitaApplicazioneEsternaConValidita> getFunzionalita(String idApplicazione) {
    final String methodName = "getSingolaFunzionalita";

    CommonUtils.validaDatiInput(idApplicazione, ID_APPLICAZIONE);

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }
    List<FunzionalitaApplicazioneEsternaConValidita> output = new LinkedList<>();

    List<CosmoTFunzionalitaApplicazioneEsterna> funzionalita =
        cosmoTFunzionalitaApplicazioneEsternaRepository
        .findAllByCosmoREnteApplicazioneEsternaCosmoTApplicazioneEsternaIdAndCosmoREnteApplicazioneEsternaCosmoTEnteIdAndDtCancellazioneNullOrderByDescrizione(
            Long.valueOf(idApplicazione), SecurityUtils.getUtenteCorrente().getEnte().getId());

    funzionalita.stream()
    .filter(singolaFunzionalita -> Boolean.FALSE.equals(singolaFunzionalita.getPrincipale()))
    .forEach(singolaFunzionalita -> {

      Optional<CosmoREnteFunzionalitaApplicazioneEsterna> singolaEnteFunzionalita =
          singolaFunzionalita.getCosmoREnteFunzionalitaApplicazioneEsternas().stream()
          .filter(enteFunzionalita -> singolaFunzionalita.getId()
              .equals(enteFunzionalita.getCosmoTFunzionalitaApplicazioneEsterna().getId())
              && SecurityUtils.getUtenteCorrente().getEnte().getId()
              .equals(enteFunzionalita.getCosmoTEnte().getId()))
          .findFirst();

      if (singolaEnteFunzionalita.isPresent()) {

        FunzionalitaApplicazioneEsternaConValidita singola =
            funzionalitaMapper.toDTOconValidita(singolaFunzionalita);
        singola.setCampiTecnici(CommonUtils.creaCampiTecnici(singolaEnteFunzionalita.get()));
        if (null == singolaFunzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
            && singolaFunzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
            .isEmpty()) {
          singola.setAssociataUtenti(false);
        } else {
          singola
          .setAssociataUtenti(
              singolaFunzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas().stream()
              .anyMatch(utenteFunzionalita -> SecurityUtils.getUtenteCorrente()
                  .getCodiceFiscale()
                  .equals(utenteFunzionalita.getCosmoTUtente().getCodiceFiscale())
                  && (null == utenteFunzionalita.getDtFineVal() || utenteFunzionalita
                  .getDtFineVal().after(Timestamp.valueOf(LocalDateTime.now())))));
        }


        output.add(singola);
      }
    });

    return output;

  }

  @Override
  @Transactional(readOnly = true)
  public FunzionalitaApplicazioneEsternaConValidita getSingolaFunzionalita(String idApplicazione,
      String idFunzionalita) {
    final String methodName = "getSingolaFunzionalita";

    CommonUtils.validaDatiInput(idApplicazione, ID_APPLICAZIONE);

    CommonUtils.validaDatiInput(idFunzionalita, ID_FUNZIONALITA);

    CosmoTFunzionalitaApplicazioneEsterna funzionalita =
        cosmoTFunzionalitaApplicazioneEsternaRepository.findOne(Long.valueOf(idFunzionalita));

    if (null == funzionalita) {
      final String message =
          String.format(ErrorMessages.AE_FUNZIONALITA_NON_TROVATA, idFunzionalita);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    if (!Long.valueOf(idApplicazione).equals(
        funzionalita.getCosmoREnteApplicazioneEsterna().getCosmoTApplicazioneEsterna().getId())) {
      logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
    }

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    CosmoTEnte cosmoTEnte = cosmoTEnteRepository
        .findOneNotDeleted(SecurityUtils.getUtenteCorrente().getEnte().getId()).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
          throw new NotFoundException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
        });

    Optional<CosmoREnteFunzionalitaApplicazioneEsterna> enteFunzionalita =
        funzionalita.getCosmoREnteFunzionalitaApplicazioneEsternas().stream()
        .filter(enteFunzionalitaSingola -> funzionalita
            .equals(enteFunzionalitaSingola.getCosmoTFunzionalitaApplicazioneEsterna())
            && cosmoTEnte.equals(enteFunzionalitaSingola.getCosmoTEnte()))
        .findFirst();

    FunzionalitaApplicazioneEsternaConValidita output =
        funzionalitaMapper.toDTOconValidita(funzionalita);

    if (enteFunzionalita.isPresent()) {
      output.setCampiTecnici(CommonUtils.creaCampiTecnici(enteFunzionalita.get()));
    }

    return output;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public FunzionalitaApplicazioneEsternaConValidita salvaSingolaFunzionalita(String idApplicazione,
      FunzionalitaApplicazioneEsternaConValidita body) {
    final String methodName = "salvaSingolaFunzionalita";

    CommonUtils.validaDatiInput(idApplicazione, ID_APPLICAZIONE);

    ValidationUtils.validaAnnotations(body);

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    if (Boolean.TRUE.equals(body.isPrincipale())) {
      logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
    }

    CosmoREnteApplicazioneEsterna enteApplicazione =
        cosmoREnteApplicazioneEsternaRepository.findOneByCosmoTApplicazioneEsternaIdAndCosmoTEnteId(
            Long.valueOf(idApplicazione), SecurityUtils.getUtenteCorrente().getEnte().getId());

    if (null == enteApplicazione) {
      logger.error(methodName, ErrorMessages.AE_NESSUNA_APPLICAZIONE_ASSOCIATA_ENTE);
      throw new BadRequestException(ErrorMessages.AE_NESSUNA_APPLICAZIONE_ASSOCIATA_ENTE);

    }

    CosmoTFunzionalitaApplicazioneEsterna funzionalita = funzionalitaMapper.toRecord(body);

    funzionalita.setCosmoREnteApplicazioneEsterna(enteApplicazione);
    funzionalita = cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalita);

    CosmoTEnte cosmoTEnte = cosmoTEnteRepository
        .findOneNotDeleted(SecurityUtils.getUtenteCorrente().getEnte().getId()).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
          throw new NotFoundException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
        });


    CosmoREnteFunzionalitaApplicazioneEsternaPK pk =
        new CosmoREnteFunzionalitaApplicazioneEsternaPK();
    pk.setIdFunzionalitaApplicazioneEsterna(funzionalita.getId());
    pk.setIdEnte(cosmoTEnte.getId());

    CosmoREnteFunzionalitaApplicazioneEsterna enteFunzionalita =
        new CosmoREnteFunzionalitaApplicazioneEsterna();
    enteFunzionalita.setId(pk);
    enteFunzionalita.setCosmoTEnte(cosmoTEnte);
    enteFunzionalita.setCosmoTFunzionalitaApplicazioneEsterna(funzionalita);

    enteFunzionalita.setDtInizioVal(null == body.getCampiTecnici().getDtIniVal() ? null
        : Timestamp.valueOf(body.getCampiTecnici().getDtIniVal()
            .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    enteFunzionalita.setDtFineVal(null == body.getCampiTecnici().getDtFineVal() ? null
        : Timestamp.valueOf(body.getCampiTecnici().getDtFineVal()
            .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalita);

    FunzionalitaApplicazioneEsternaConValidita output =
        funzionalitaMapper.toDTOconValidita(funzionalita);

    output.setCampiTecnici(CommonUtils.creaCampiTecnici(enteFunzionalita));

    return output;

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public FunzionalitaApplicazioneEsternaConValidita aggiornaSingolaFunzionalita(
      String idApplicazione, String idFunzionalita,
      FunzionalitaApplicazioneEsternaConValidita body) {
    final String methodName = "aggiornaSingolaFunzionalita";

    CommonUtils.validaDatiInput(idApplicazione, ID_APPLICAZIONE);

    CommonUtils.validaDatiInput(idFunzionalita, ID_FUNZIONALITA);

    ValidationUtils.validaAnnotations(body);

    if (Boolean.TRUE.equals(body.isPrincipale())) {
      logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
    }

    CosmoTFunzionalitaApplicazioneEsterna funzionalita =
        cosmoTFunzionalitaApplicazioneEsternaRepository
        .findOneNotDeleted(Long.valueOf(idFunzionalita)).orElseThrow(() -> {
          final String message =
              String.format(ErrorMessages.AE_FUNZIONALITA_NON_TROVATA, idFunzionalita);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    if (!Long.valueOf(idApplicazione).equals(
        funzionalita.getCosmoREnteApplicazioneEsterna().getCosmoTApplicazioneEsterna().getId())) {
      logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
    }

    funzionalita.setDescrizione(body.getDescrizione());
    funzionalita.setUrl(body.getUrl());

    funzionalita = cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalita);

    CosmoTEnte cosmoTEnte = cosmoTEnteRepository
        .findOneNotDeleted(SecurityUtils.getUtenteCorrente().getEnte().getId()).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
          throw new NotFoundException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
        });

    Long funzionalitaId = funzionalita.getId();
    Optional<CosmoREnteFunzionalitaApplicazioneEsterna> enteFunzionalitaDaAggiornare =
        funzionalita.getCosmoREnteFunzionalitaApplicazioneEsternas().stream()
        .filter(enteFunzionalita -> funzionalitaId
            .equals(enteFunzionalita.getCosmoTFunzionalitaApplicazioneEsterna().getId())
            && cosmoTEnte.equals(enteFunzionalita.getCosmoTEnte()))
        .findFirst();

    if (enteFunzionalitaDaAggiornare.isPresent()) {
      enteFunzionalitaDaAggiornare.get().setDtInizioVal(Timestamp.valueOf(body.getCampiTecnici()
          .getDtIniVal().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      enteFunzionalitaDaAggiornare.get()
      .setDtFineVal(null == body.getCampiTecnici().getDtFineVal() ? null
          : Timestamp.valueOf(body.getCampiTecnici().getDtFineVal()
              .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalitaDaAggiornare.get());

    }

    if (null != body.getCampiTecnici().getDtFineVal()) {

      funzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas().forEach(utenteFunzionalita -> {
        if (null == utenteFunzionalita.getDtFineVal()
            || Timestamp.valueOf(LocalDateTime.now()).after(utenteFunzionalita.getDtFineVal())) {
          utenteFunzionalita.setDtFineVal(Timestamp.valueOf(body.getCampiTecnici().getDtFineVal()
              .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

          cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(utenteFunzionalita);
        }

      });

    }
    FunzionalitaApplicazioneEsternaConValidita output =
        funzionalitaMapper.toDTOconValidita(funzionalita);

    if (enteFunzionalitaDaAggiornare.isPresent()) {
      output.setCampiTecnici(CommonUtils.creaCampiTecnici(enteFunzionalitaDaAggiornare.get()));
    }

    return output;
  }

}
