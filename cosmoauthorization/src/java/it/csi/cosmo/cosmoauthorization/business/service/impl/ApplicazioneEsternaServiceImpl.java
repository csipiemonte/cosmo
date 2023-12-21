/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoREnteApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoREnteFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoREnteFunzionalitaApplicazioneEsternaPK;
import it.csi.cosmo.common.entities.CosmoRUtenteFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoRUtenteFunzionalitaApplicazioneEsternaPK;
import it.csi.cosmo.common.entities.CosmoTApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFunzionalitaApplicazioneEsterna;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTApplicazioneEsternaMapper;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTFunzionalitaApplicazioneEsternaMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoREnteApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoREnteFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoRUtenteFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTFunzionalitaApplicazioneEsternaRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.specifications.CosmoTApplicazioneEsternaSpecifications;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.CommonUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ApplicazioneEsternaServiceImpl implements ApplicazioneEsternaService {

  private static final String CLASS_NAME = ApplicazioneEsternaServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTApplicazioneEsternaRepository cosmoTApplicazioneEsternaRepository;

  @Autowired
  private CosmoTFunzionalitaApplicazioneEsternaRepository cosmoTFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoREnteApplicazioneEsternaRepository cosmoREnteApplicazioneEsternaRepository;

  @Autowired
  private CosmoREnteFunzionalitaApplicazioneEsternaRepository cosmoREnteFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoRUtenteFunzionalitaApplicazioneEsternaRepository cosmoRUtenteFunzionalitaApplicazioneEsternaRepository;

  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTApplicazioneEsternaMapper applicazioneEsternaMapper;

  @Autowired
  private CosmoTFunzionalitaApplicazioneEsternaMapper funzionalitaMapper;

  private static final String ID_APPLICAZIONE = "id applicazione";

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void eliminaApplicazione(String id) {
    final String methodName = "eliminaApplicazione";

    CommonUtils.validaDatiInput(id, ID_APPLICAZIONE);

    CosmoTApplicazioneEsterna applicazioneDaEliminare =
        cosmoTApplicazioneEsternaRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(() -> {
          final String message =
              String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, id);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    applicazioneDaEliminare.getCosmoREnteApplicazioneEsternas().stream()
    .forEach(applicazioneEsterna -> invalidaEnteCancellaFunzionalita(applicazioneEsterna));

    applicazioneDaEliminare.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    applicazioneDaEliminare
    .setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    applicazioneDaEliminare = cosmoTApplicazioneEsternaRepository.save(applicazioneDaEliminare);

    logger.info(methodName, "Applicazione con id {} eliminata", applicazioneDaEliminare.getId());

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void eliminaApplicazioneAssociata(String id) {
    final String methodName = "eliminaApplicazioneAssociata";

    CommonUtils.validaDatiInput(id, ID_APPLICAZIONE);

    CosmoTApplicazioneEsterna applicazioneDaEliminare =
        cosmoTApplicazioneEsternaRepository.findOneNotDeleted(Long.valueOf(id)).orElseThrow(() -> {
          final String message =
              String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, id);
          logger.error(methodName, message);
          throw new NotFoundException(message);
        });

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    applicazioneDaEliminare.getCosmoREnteApplicazioneEsternas().stream()
    .filter(applicazioneEsterna -> SecurityUtils.getUtenteCorrente().getEnte().getId()
        .equals(applicazioneEsterna.getCosmoTEnte().getId()))
    .forEach(applicazioneEsterna -> invalidaEnteCancellaFunzionalita(applicazioneEsterna));

    logger.info(methodName, "Applicazione con id {} eliminata", applicazioneDaEliminare.getId());

  }

  private void invalidaEnteCancellaFunzionalita(CosmoREnteApplicazioneEsterna applicazioneEsterna) {

    if (null == applicazioneEsterna.getDtFineVal()
        || applicazioneEsterna.getDtFineVal().after(Timestamp.valueOf(LocalDateTime.now()))) {
      applicazioneEsterna.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
      cosmoREnteApplicazioneEsternaRepository.save(applicazioneEsterna);
    }

    applicazioneEsterna.getCosmoTFunzionalitaApplicazioneEsternas().forEach(funzionalita -> {

      if (funzionalita.nonCancellato()) {
        funzionalita.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
        funzionalita.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

        cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalita);
      }

      funzionalita.getCosmoREnteFunzionalitaApplicazioneEsternas()
      .forEach(enteFunzionalitaAssociata -> {
        if (null == enteFunzionalitaAssociata.getDtFineVal() || enteFunzionalitaAssociata
            .getDtFineVal().after(Timestamp.valueOf(LocalDateTime.now()))) {
          enteFunzionalitaAssociata.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
          cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalitaAssociata);
        }
      });

      funzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
      .forEach(funzionalitaAssociata -> {
        if (null == funzionalitaAssociata.getDtFineVal() || funzionalitaAssociata.getDtFineVal()
            .after(Timestamp.valueOf(LocalDateTime.now()))) {
          funzionalitaAssociata.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
          cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(funzionalitaAssociata);
        }
      });

    });
  }

  @Override
  public List<ApplicazioneEsternaConValidita> getTutteApplicazioni() {

    List<ApplicazioneEsternaConValidita> output = new LinkedList<>();

    List<CosmoTApplicazioneEsterna> applicazioni =
        cosmoTApplicazioneEsternaRepository.findAllByDtCancellazioneNullOrderByDescrizione();

    if (null != applicazioni && !applicazioni.isEmpty()) {

      applicazioni.forEach(applicazione -> {
        ApplicazioneEsternaConValidita outputSingolo =
            applicazioneEsternaMapper.toDTOconValidita(applicazione);

        if (null == applicazione.getCosmoREnteApplicazioneEsternas()
            || applicazione.getCosmoREnteApplicazioneEsternas().isEmpty()) {

          outputSingolo.setAssociataEnti(false);
          outputSingolo.setNumEntiAssociati(0l);

        } else {

          long dimensione = applicazione.getCosmoREnteApplicazioneEsternas().stream()
              .filter(CosmoREntity::valido).count();

          outputSingolo.setAssociataEnti(dimensione > 0);
          outputSingolo.setNumEntiAssociati(dimensione);
        }

        output.add(outputSingolo);
      });

    }

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ApplicazioneEsterna> getApplicazioni(Boolean configurata) {
    String methodName = "getApplicazioni";

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }


    List<CosmoTApplicazioneEsterna> applicazioniValide =
        cosmoTApplicazioneEsternaRepository.findAll(CosmoTApplicazioneEsternaSpecifications
            .findAppValideOrderByDescrizione(SecurityUtils.getUtenteCorrente().getEnte().getId()));
    if (null == applicazioniValide || applicazioniValide.isEmpty()) {
      return new LinkedList<>();
    }

    applicazioniValide.forEach(applicazione -> applicazione.getCosmoREnteApplicazioneEsternas()
        .removeIf(applicazioneEsterna -> applicazioneEsterna.nonValido()
            || !SecurityUtils.getUtenteCorrente().getEnte().getId()
            .equals(applicazioneEsterna.getCosmoTEnte().getId())));

    applicazioniValide = applicazioniValide.stream()
        .filter(applicazione -> null != applicazione.getCosmoREnteApplicazioneEsternas()
        && !applicazione.getCosmoREnteApplicazioneEsternas().isEmpty())
        .collect(Collectors.toList());

    configurazioneFunzionalitaValide(applicazioniValide);

    if (null == configurata || Boolean.TRUE.equals(configurata)) {

      List<ApplicazioneEsterna> applicazioniEsterne = new LinkedList<>();
      applicazioniValide.forEach(applicazione -> {

        List<CosmoREnteApplicazioneEsterna> applicazioniAssociate =
            applicazione.getCosmoREnteApplicazioneEsternas().stream().filter(applicazioneEnte -> {

              applicazioneEnte.getCosmoTFunzionalitaApplicazioneEsternas()
              .removeIf(funzionalita -> {

                List<CosmoRUtenteFunzionalitaApplicazioneEsterna> funzionalitaAssociate =
                    funzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas().stream()
                    .filter(utenteFunzionalita -> utenteFunzionalita.valido()
                        && SecurityUtils.getUtenteCorrente().getCodiceFiscale().equals(
                            utenteFunzionalita.getCosmoTUtente().getCodiceFiscale()))
                    .collect(Collectors.toList());

                return funzionalitaAssociate.isEmpty();
              });

              return applicazioneEnte.getCosmoTFunzionalitaApplicazioneEsternas() != null
                  && !applicazioneEnte.getCosmoTFunzionalitaApplicazioneEsternas().isEmpty();

            }).collect(Collectors.toList());


        if (null != applicazioniAssociate && !applicazioniAssociate.isEmpty()) {
          var utenteApp = applicazioniAssociate.stream()
              .map(elem -> elem.getCosmoTFunzionalitaApplicazioneEsternas().get(0)
                  .getCosmoRUtenteFunzionalitaApplicazioneEsternas().stream()
                  .filter(valore -> valore.valido() && valore.getCosmoTUtente().getCodiceFiscale()
                      .equals(SecurityUtils.getUtenteCorrente().getCodiceFiscale()))
                  .findFirst().orElse(null))
              .findFirst().orElse(null);
          if (utenteApp != null) {
            var app = applicazioneEsternaMapper.toDTO(applicazione);
            app.setPosizione(utenteApp.getPosizione());
            applicazioniEsterne.add(app);
          }


        }
      });

      return applicazioniEsterne;

    }

    return applicazioneEsternaMapper.toDTOList(applicazioniValide);

  }


  @Override
  @Transactional(readOnly = true)
  public List<ApplicazioneEsternaConValidita> getApplicazioniAssociateEnte(Boolean enteAssociato) {
    String methodName = "getApplicazioniEsterneAssociateEnte";

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    if (Boolean.TRUE.equals(enteAssociato)) {
      return getAppAssociate();
    } else {
      return getAppNonAssociate();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public ApplicazioneEsterna getApplicazione(String id) {
    final String methodName = "getApplicazione";

    CommonUtils.validaDatiInput(id, ID_APPLICAZIONE);

    CosmoTApplicazioneEsterna applicazione =
        cosmoTApplicazioneEsternaRepository.findOne(Long.valueOf(id));

    if (null == applicazione) {
      final String message = String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, id);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    ApplicazioneEsterna output = applicazioneEsternaMapper.toDTO(applicazione);
    output.setFunzionalita(null);
    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public ApplicazioneEsternaConValidita getApplicazioneAssociataEnte(String id) {
    final String methodName = "getApplicazioneAssociataEnte";

    CommonUtils.validaDatiInput(id, ID_APPLICAZIONE);

    CosmoTApplicazioneEsterna applicazione =
        cosmoTApplicazioneEsternaRepository.findOne(Long.valueOf(id));

    if (null == applicazione) {
      final String message = String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, id);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    applicazione.getCosmoREnteApplicazioneEsternas().removeIf(applicazioneEsterna -> !SecurityUtils
        .getUtenteCorrente().getEnte().getId().equals(applicazioneEsterna.getCosmoTEnte().getId()));

    Optional<CosmoREnteApplicazioneEsterna> enteFunzionalita =
        applicazione.getCosmoREnteApplicazioneEsternas().stream()
        .filter(enteFunzionalitaSingola -> SecurityUtils.getUtenteCorrente().getEnte().getId()
            .equals(enteFunzionalitaSingola.getCosmoTEnte().getId()))
        .findFirst();

    if (enteFunzionalita.isPresent()) {

      Optional<CosmoTFunzionalitaApplicazioneEsterna> funzionalitaPrincipale =
          enteFunzionalita.get().getCosmoTFunzionalitaApplicazioneEsternas().stream()
          .filter(funzionalita -> funzionalita.nonCancellato()
              && Boolean.TRUE.equals(funzionalita.getPrincipale()))
          .findFirst();

      if (funzionalitaPrincipale.isPresent()) {

        return creazioneDTO(applicazione, enteFunzionalita.get(), funzionalitaPrincipale.get());
      } else {
        return creazioneDTO(applicazione, null, null);
      }

    } else {
      return creazioneDTO(applicazione, null, null);
    }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApplicazioneEsterna salvaApplicazione(ApplicazioneEsterna body) {
    final String methodName = "salvaApplicazione";

    ValidationUtils.validaAnnotations(body);

    if (body.getId() != null) {
      logger.error(methodName, ErrorMessages.AE_APPLICAZIONE_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_APPLICAZIONE_NON_CORRETTA);
    }

    if (body.getIcona() == null || body.getIcona().isBlank()) {
      logger.error(methodName, ErrorMessages.AE_APPLICAZIONE_NON_CORRETTA);
      throw new BadRequestException(ErrorMessages.AE_APPLICAZIONE_NON_CORRETTA);
    }

    CosmoTApplicazioneEsterna applicazioneDaSalvare = applicazioneEsternaMapper.toRecord(body);

    applicazioneDaSalvare = cosmoTApplicazioneEsternaRepository.save(applicazioneDaSalvare);

    return applicazioneEsternaMapper.toDTO(applicazioneDaSalvare);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApplicazioneEsterna aggiornaApplicazione(String id, ApplicazioneEsterna body) {
    final String methodName = "aggiornaApplicazione";

    CommonUtils.validaDatiInput(id, ID_APPLICAZIONE);

    ValidationUtils.validaAnnotations(body);

    CosmoTApplicazioneEsterna applicazioneDaAggiornare =
        cosmoTApplicazioneEsternaRepository.findOne(Long.valueOf(id));

    if (null == applicazioneDaAggiornare) {
      final String message = String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, id);
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    applicazioneDaAggiornare.setDescrizione(body.getDescrizione());
    applicazioneDaAggiornare
    .setIcona(StringUtils.isBlank(body.getIcona()) ? null : body.getIcona().getBytes());

    applicazioneDaAggiornare = cosmoTApplicazioneEsternaRepository.save(applicazioneDaAggiornare);

    return applicazioneEsternaMapper.toDTO(applicazioneDaAggiornare);
  }


  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<ApplicazioneEsterna> associaDissociaAppUtente(
      List<ApplicazioneEsterna> applicazioni) {
    String methodName = "associaDissociaAppUtente";

    if (null == applicazioni) {
      logger.error(methodName, ErrorMessages.AE_NESSUNA_APPLICAZIONE_DA_ASSOCIARE);
      throw new NotFoundException(ErrorMessages.AE_NESSUNA_APPLICAZIONE_DA_ASSOCIARE);
    }

    CosmoTUtente utente = cosmoTUtenteRepository
        .findByCodiceFiscale(SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    if (null == utente) {
      logger.error(methodName, String.format(ErrorMessages.U_UTENTE_CON_CODICE_FISCALE_NON_TROVATO,
          SecurityUtils.getUtenteCorrente().getCodiceFiscale()));
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }

    if (applicazioni.isEmpty()) {
      dissociaTutteApp(utente);
    }

    List<CosmoRUtenteFunzionalitaApplicazioneEsterna> utenteFunzionalita =
        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository
        .findAllByCosmoTUtenteCodiceFiscaleAndDtFineValIsNull(utente.getCodiceFiscale())
        .stream()
        .filter(elem -> SecurityUtils.getUtenteCorrente().getEnte().getId()
            .equals(elem.getCosmoTFunzionalitaApplicazioneEsterna()
                .getCosmoREnteApplicazioneEsterna().getCosmoTEnte().getId()))
        .collect(Collectors.toList());

    List<CosmoTFunzionalitaApplicazioneEsterna> funzionalitaSalvate = new ArrayList<>();

    applicazioni.forEach(applicazione -> {

      ValidationUtils.validaAnnotations(applicazione);
      ValidationUtils.require(applicazione.getPosizione(), "posizione");

      if (null == applicazione.getId()) {
        logger.error(methodName,
            String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, ID_APPLICAZIONE));
        throw new BadRequestException(
            String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, ID_APPLICAZIONE));
      }

      CosmoTApplicazioneEsterna applicazioneDaAssociare =
          cosmoTApplicazioneEsternaRepository.findOne(applicazione.getId());

      if (null == applicazioneDaAssociare) {
        final String message =
            String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, applicazione.getId());
        logger.error(methodName, message);
        throw new NotFoundException(message);
      }


      if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
        logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
        throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      }

      CosmoREnteApplicazioneEsterna enteFunzionalitaDaAssociare =
          applicazioneDaAssociare.getCosmoREnteApplicazioneEsternas().stream()
          .filter(enteFunzionalitaSingola -> SecurityUtils.getUtenteCorrente().getEnte().getId()
              .equals(enteFunzionalitaSingola.getCosmoTEnte().getId()))
          .findFirst().orElseThrow(() -> {
            logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
            throw new NotFoundException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
          });



      applicazione.getFunzionalita().forEach(funzionalitaDaAggiornare -> {

        CosmoTFunzionalitaApplicazioneEsterna funzionalita =
            enteFunzionalitaDaAssociare.getCosmoTFunzionalitaApplicazioneEsternas().stream()
            .filter(funzionalitaSuDB -> funzionalitaDaAggiornare.getId()
                .equals(funzionalitaSuDB.getId()) && funzionalitaSuDB.nonCancellato())
            .findFirst().orElseThrow(() -> {
              logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
              throw new NotFoundException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
            });

        funzionalitaSalvate.add(funzionalita);

        aggiornaUtenteFunzionalita(funzionalita, utente, applicazione.getPosizione());
      });
    });

    utenteFunzionalita.forEach(
        elem-> {
          if (!funzionalitaSalvate.contains(elem.getCosmoTFunzionalitaApplicazioneEsterna())) {
            elem.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
            cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(elem);
          }
        }
        );

    return getApplicazioni(true);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApplicazioneEsternaConValidita associaModificaAssociazioneAppEnte(
      ApplicazioneEsternaConValidita applicazione) {
    String methodName = "associaModificaAssociazioneAppEnte";

    ValidationUtils.validaAnnotations(applicazione);

    if (null == applicazione.getId()) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, ID_APPLICAZIONE));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, ID_APPLICAZIONE));
    }

    if (null == applicazione.getCampiTecnici()) {
      logger.error(methodName, String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "Validita'"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "Validita'"));
    }
    ValidationUtils.validaAnnotations(applicazione.getCampiTecnici());

    if (null == applicazione.getFunzionalitaPrincipale()) {
      logger.error(methodName,
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "Funzionalita' principale"));
      throw new BadRequestException(
          String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "Funzionalita' principale"));
    }

    ValidationUtils.validaAnnotations(applicazione.getFunzionalitaPrincipale());

    CosmoTApplicazioneEsterna applicazioneDaAssociare =
        cosmoTApplicazioneEsternaRepository.findOne(applicazione.getId());

    if (null == applicazioneDaAssociare) {
      final String message =
          String.format(ErrorMessages.AE_APPLICAZIONE_ESTERNA_NON_TROVATA, applicazione.getId());
      logger.error(methodName, message);
      throw new NotFoundException(message);
    }

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    CosmoTFunzionalitaApplicazioneEsterna funzionalita = null;

    CosmoREnteApplicazioneEsterna enteApplicazioneDaAssociare =
        cosmoREnteApplicazioneEsternaRepository.findOneByCosmoTApplicazioneEsternaIdAndCosmoTEnteId(
            applicazioneDaAssociare.getId(), SecurityUtils.getUtenteCorrente().getEnte().getId());

    // bisogna associare l'applicazione e l'ente per la prima volta e creare la funzionalita'
    // principale con i suoi valori di validita'
    if (null == enteApplicazioneDaAssociare) {
      enteApplicazioneDaAssociare = salvaEnteApplicazione(applicazione, applicazioneDaAssociare);

      funzionalita = salvaFunzionalita(applicazione, enteApplicazioneDaAssociare);

    } else {
      enteApplicazioneDaAssociare.setDtInizioVal(Timestamp.valueOf(applicazione.getCampiTecnici()
          .getDtIniVal().atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      enteApplicazioneDaAssociare
      .setDtFineVal(null == applicazione.getCampiTecnici().getDtFineVal() ? null
          : Timestamp.valueOf(applicazione.getCampiTecnici().getDtFineVal()
              .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      enteApplicazioneDaAssociare =
          cosmoREnteApplicazioneEsternaRepository.save(enteApplicazioneDaAssociare);

      Optional<CosmoTFunzionalitaApplicazioneEsterna> funzionalitaPrincipale =
          enteApplicazioneDaAssociare.getCosmoTFunzionalitaApplicazioneEsternas().stream()
          .filter(funzionalitaPrinc -> Boolean.TRUE.equals(funzionalitaPrinc.getPrincipale())
              && funzionalitaPrinc.nonCancellato())
          .findFirst();

      if (funzionalitaPrincipale.isPresent()) {
        funzionalita = aggiornaAssociazioneEnteFunzionalita(applicazione,
            funzionalitaPrincipale.get(), enteApplicazioneDaAssociare.getCosmoTEnte());
      } else {
        funzionalita = salvaFunzionalita(applicazione, enteApplicazioneDaAssociare);
      }

      aggiornaValiditaUtenti(enteApplicazioneDaAssociare);
    }

    return creazioneDTO(applicazioneDaAssociare, enteApplicazioneDaAssociare, funzionalita);

  }

  private ApplicazioneEsternaConValidita creazioneDTO(CosmoTApplicazioneEsterna applicazione,
      CosmoREnteApplicazioneEsterna enteApplicazione,
      CosmoTFunzionalitaApplicazioneEsterna funzionalita) {
    ApplicazioneEsternaConValidita output = new ApplicazioneEsternaConValidita();
    output.setId(applicazione.getId());
    output.setDescrizione(applicazione.getDescrizione());
    output.setIcona(applicazione.getIcona() == null ? null : new String(applicazione.getIcona()));

    output.setAssociataUtenti(null == enteApplicazione
        || null == enteApplicazione.getCosmoTFunzionalitaApplicazioneEsternas() ? null
            : utentiAssociatiApp(enteApplicazione));

    if (null == enteApplicazione) {
      output.setCampiTecnici(null);
    } else {
      output.setCampiTecnici(CommonUtils.creaCampiTecnici(enteApplicazione));
    }

    output.setFunzionalitaPrincipale(
        null == funzionalita ? null : funzionalitaMapper.toDTO(funzionalita));

    return output;
  }

  private Boolean utentiAssociatiApp(CosmoREnteApplicazioneEsterna enteFunzionalita) {
    Boolean associataUtente = null;

    associataUtente =
        enteFunzionalita.getCosmoTFunzionalitaApplicazioneEsternas().stream().anyMatch(funz ->

        funz.nonCancellato() && !funz.getCosmoRUtenteFunzionalitaApplicazioneEsternas().isEmpty()
        && funz.getCosmoRUtenteFunzionalitaApplicazioneEsternas().stream()
        .anyMatch(CosmoRUtenteFunzionalitaApplicazioneEsterna::valido));

    return associataUtente;
  }

  private CosmoREnteApplicazioneEsterna salvaEnteApplicazione(
      ApplicazioneEsternaConValidita applicazioneDTO, CosmoTApplicazioneEsterna applicazione) {
    final String methodName = "salvaEnteApplicazione";

    if (null == SecurityUtils.getUtenteCorrente().getEnte()) {
      logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
      throw new BadRequestException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
    }

    CosmoREnteApplicazioneEsterna applicazioneEnte = new CosmoREnteApplicazioneEsterna();

    CosmoTEnte ente = cosmoTEnteRepository
        .findOneNotDeleted(SecurityUtils.getUtenteCorrente().getEnte().getId()).orElseThrow(() -> {
          logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
          throw new NotFoundException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
        });

    applicazioneEnte.setCosmoTApplicazioneEsterna(applicazione);
    applicazioneEnte.setCosmoTEnte(ente);

    applicazioneEnte.setDtInizioVal(null == applicazioneDTO.getCampiTecnici()
        || null == applicazioneDTO.getCampiTecnici().getDtIniVal() ? null
            : Timestamp.valueOf(applicazioneDTO.getCampiTecnici().getDtIniVal()
                .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    applicazioneEnte.setDtFineVal(null == applicazioneDTO.getCampiTecnici()
        || null == applicazioneDTO.getCampiTecnici().getDtFineVal() ? null
            : Timestamp.valueOf(applicazioneDTO.getCampiTecnici().getDtFineVal()
                .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    applicazioneEnte = cosmoREnteApplicazioneEsternaRepository.save(applicazioneEnte);

    return applicazioneEnte;
  }

  private CosmoTFunzionalitaApplicazioneEsterna salvaFunzionalita(
      ApplicazioneEsternaConValidita applicazioneDTO,
      CosmoREnteApplicazioneEsterna applicazioneEnte) {

    CosmoTFunzionalitaApplicazioneEsterna funzionalitaPrincipale =
        funzionalitaMapper.toRecord(applicazioneDTO.getFunzionalitaPrincipale());
    funzionalitaPrincipale.setCosmoREnteApplicazioneEsterna(applicazioneEnte);

    funzionalitaPrincipale =
        cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalitaPrincipale);

    CosmoREnteFunzionalitaApplicazioneEsternaPK pk =
        new CosmoREnteFunzionalitaApplicazioneEsternaPK();
    pk.setIdFunzionalitaApplicazioneEsterna(funzionalitaPrincipale.getId());
    pk.setIdEnte(applicazioneEnte.getCosmoTEnte().getId());

    CosmoREnteFunzionalitaApplicazioneEsterna enteFunzionalitaPrincipale =
        new CosmoREnteFunzionalitaApplicazioneEsterna();
    enteFunzionalitaPrincipale.setId(pk);
    enteFunzionalitaPrincipale.setCosmoTEnte(applicazioneEnte.getCosmoTEnte());
    enteFunzionalitaPrincipale.setCosmoTFunzionalitaApplicazioneEsterna(funzionalitaPrincipale);

    enteFunzionalitaPrincipale.setDtInizioVal(null == applicazioneDTO.getCampiTecnici()
        || null == applicazioneDTO.getCampiTecnici().getDtIniVal() ? null
            : Timestamp.valueOf(applicazioneDTO.getCampiTecnici().getDtIniVal()
                .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    enteFunzionalitaPrincipale.setDtFineVal(null == applicazioneDTO.getCampiTecnici()
        || null == applicazioneDTO.getCampiTecnici().getDtFineVal() ? null
            : Timestamp.valueOf(applicazioneDTO.getCampiTecnici().getDtFineVal()
                .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

    cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalitaPrincipale);

    return funzionalitaPrincipale;
  }

  private CosmoTFunzionalitaApplicazioneEsterna aggiornaAssociazioneEnteFunzionalita(
      ApplicazioneEsternaConValidita applicazione,
      CosmoTFunzionalitaApplicazioneEsterna funzionalitaDaAggiornare, CosmoTEnte ente) {

    funzionalitaDaAggiornare
    .setDescrizione(applicazione.getFunzionalitaPrincipale().getDescrizione());
    funzionalitaDaAggiornare.setUrl(applicazione.getFunzionalitaPrincipale().getUrl());

    funzionalitaDaAggiornare =
        cosmoTFunzionalitaApplicazioneEsternaRepository.save(funzionalitaDaAggiornare);

    Long funzionalitaId = funzionalitaDaAggiornare.getId();
    Optional<CosmoREnteFunzionalitaApplicazioneEsterna> enteFunzionalitaDaAggiornare =
        funzionalitaDaAggiornare.getCosmoREnteFunzionalitaApplicazioneEsternas().stream()
        .filter(enteFunzionalita -> funzionalitaId
            .equals(enteFunzionalita.getCosmoTFunzionalitaApplicazioneEsterna().getId())
            && ente.equals(enteFunzionalita.getCosmoTEnte()))
        .findFirst();

    if (enteFunzionalitaDaAggiornare.isPresent()) {
      enteFunzionalitaDaAggiornare.get()
      .setDtInizioVal(Timestamp.valueOf(applicazione.getCampiTecnici().getDtIniVal()
          .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      enteFunzionalitaDaAggiornare.get()
      .setDtFineVal(null == applicazione.getCampiTecnici().getDtFineVal() ? null
          : Timestamp.valueOf(applicazione.getCampiTecnici().getDtFineVal()
              .atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()));

      cosmoREnteFunzionalitaApplicazioneEsternaRepository.save(enteFunzionalitaDaAggiornare.get());

    }
    return funzionalitaDaAggiornare;

  }

  private CosmoRUtenteFunzionalitaApplicazioneEsterna salvaNuovaAssociazioneUtenteFunzionalita(
      CosmoTFunzionalitaApplicazioneEsterna funzionalita, CosmoTUtente utente, Integer posizione) {

    CosmoRUtenteFunzionalitaApplicazioneEsternaPK pk =
        new CosmoRUtenteFunzionalitaApplicazioneEsternaPK();
    pk.setIdFunzionalitaApplicazioneEsterna(funzionalita.getId());
    pk.setIdUtente(utente.getId());

    CosmoRUtenteFunzionalitaApplicazioneEsterna utenteFunzionalita =
        new CosmoRUtenteFunzionalitaApplicazioneEsterna();
    utenteFunzionalita.setId(pk);
    utenteFunzionalita.setCosmoTFunzionalitaApplicazioneEsterna(funzionalita);
    utenteFunzionalita.setCosmoTUtente(utente);
    utenteFunzionalita.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    utenteFunzionalita.setPosizione(posizione);
    utenteFunzionalita =
        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(utenteFunzionalita);

    return utenteFunzionalita;
  }

  private void aggiornaUtenteFunzionalita(
      CosmoTFunzionalitaApplicazioneEsterna funzionalitaDaAssociare, CosmoTUtente utente,
      Integer posizione) {
    String methodName = "aggiornaUtenteFunzionalita";

    if (null == funzionalitaDaAssociare.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
        || funzionalitaDaAssociare.getCosmoRUtenteFunzionalitaApplicazioneEsternas().isEmpty()) {

      funzionalitaDaAssociare.setCosmoRUtenteFunzionalitaApplicazioneEsternas(Arrays.asList(
          salvaNuovaAssociazioneUtenteFunzionalita(funzionalitaDaAssociare, utente, posizione)));
    } else {

      List<CosmoRUtenteFunzionalitaApplicazioneEsterna> utenteFunzionalitaUtenteCorrente =
          funzionalitaDaAssociare.getCosmoRUtenteFunzionalitaApplicazioneEsternas().stream()
          .filter(uf -> utente.getId().equals(uf.getCosmoTUtente().getId()))
          .collect(Collectors.toList());


      if (null == utenteFunzionalitaUtenteCorrente || utenteFunzionalitaUtenteCorrente.isEmpty()) {
        funzionalitaDaAssociare.setCosmoRUtenteFunzionalitaApplicazioneEsternas(Arrays
            .asList(salvaNuovaAssociazioneUtenteFunzionalita(funzionalitaDaAssociare, utente,
                posizione)));

      } else if (utenteFunzionalitaUtenteCorrente.size() > 1) {
        logger.error(methodName, ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);
        throw new BadRequestException(ErrorMessages.AE_FUNZIONALITA_NON_CORRETTA);

      } else {


        utenteFunzionalitaUtenteCorrente.get(0)
        .setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
        utenteFunzionalitaUtenteCorrente.get(0).setPosizione(posizione);
        utenteFunzionalitaUtenteCorrente.get(0).setDtFineVal(null);

        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository
        .save(utenteFunzionalitaUtenteCorrente.get(0));


      }

    }
  }

  private void dissociaTutteApp(CosmoTUtente utente) {
    List<CosmoRUtenteFunzionalitaApplicazioneEsterna> utenteFunzionalita =
        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository
        .findAllByCosmoTUtenteCodiceFiscaleAndDtFineValIsNull(utente.getCodiceFiscale());

    utenteFunzionalita.forEach(singolaUtenteFunzionalita -> {
      if (SecurityUtils.getUtenteCorrente().getEnte().getId()
          .equals(singolaUtenteFunzionalita.getCosmoTFunzionalitaApplicazioneEsterna()
              .getCosmoREnteApplicazioneEsterna().getCosmoTEnte().getId())) {

        singolaUtenteFunzionalita.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        cosmoRUtenteFunzionalitaApplicazioneEsternaRepository.save(singolaUtenteFunzionalita);
      }
    });
  }

  private List<ApplicazioneEsternaConValidita> getAppNonAssociate() {
    List<ApplicazioneEsternaConValidita> output = new LinkedList<>();

    List<CosmoTApplicazioneEsterna> applicazioni =
        cosmoTApplicazioneEsternaRepository.findAll(CosmoTApplicazioneEsternaSpecifications
            .findAllByCosmoREnteApplicazioneEsternasCosmoTEnteIdNotEqualAndDtCancellazioneNullOrderByDescrizione(
                SecurityUtils.getUtenteCorrente().getEnte().getId()));

    if (null != applicazioni && !applicazioni.isEmpty()) {
      applicazioni.forEach(applicazione -> output.add(creazioneDTO(applicazione, null, null)));
    }
    return output;
  }

  private List<ApplicazioneEsternaConValidita> getAppAssociate() {
    String methodName = "getAppAssociate";

    List<ApplicazioneEsternaConValidita> output = new LinkedList<>();

    List<CosmoTApplicazioneEsterna> applicazioni = cosmoTApplicazioneEsternaRepository
        .findAllByCosmoREnteApplicazioneEsternasCosmoTEnteIdOrderByDescrizione(
            SecurityUtils.getUtenteCorrente().getEnte().getId());

    if (null != applicazioni && !applicazioni.isEmpty()) {

      applicazioni.forEach(applicazione -> {

        CosmoREnteApplicazioneEsterna enteFunzionalita =
            applicazione.getCosmoREnteApplicazioneEsternas().stream()
            .filter(enteFunzionalitaSingola -> SecurityUtils.getUtenteCorrente().getEnte()
                .getId().equals(enteFunzionalitaSingola.getCosmoTEnte().getId()))
            .findFirst().orElseThrow(() -> {
              logger.error(methodName, ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
              throw new NotFoundException(ErrorMessages.AE_ENTE_ASSOCIATO_NON_VALIDO);
            });

        Optional<CosmoTFunzionalitaApplicazioneEsterna> funzionalitaPrincipale =
            enteFunzionalita.getCosmoTFunzionalitaApplicazioneEsternas().stream()
            .filter(funzionalita -> funzionalita.nonCancellato()
                && Boolean.TRUE.equals(funzionalita.getPrincipale()))
            .findFirst();

        if (funzionalitaPrincipale.isPresent()) {
          output.add(creazioneDTO(applicazione, enteFunzionalita, funzionalitaPrincipale.get()));
        }

      });
    }
    return output;
  }

  private void aggiornaValiditaUtenti(CosmoREnteApplicazioneEsterna enteApplicazione) {

    if (null != enteApplicazione.getDtFineVal()) {
      enteApplicazione.getCosmoTFunzionalitaApplicazioneEsternas()
      .forEach(funzionalita -> funzionalita.getCosmoRUtenteFunzionalitaApplicazioneEsternas()
          .forEach(utenteFunzionalita -> {
            if (null == utenteFunzionalita.getDtFineVal() || Timestamp
                .valueOf(LocalDateTime.now()).before(utenteFunzionalita.getDtFineVal())) {
              utenteFunzionalita.setDtFineVal(enteApplicazione.getDtFineVal());
              utenteFunzionalita = cosmoRUtenteFunzionalitaApplicazioneEsternaRepository
                  .save(utenteFunzionalita);
            }

          }));
    }
  }

  private void configurazioneFunzionalitaValide(
      List<CosmoTApplicazioneEsterna> applicazioniValide) {
    applicazioniValide.stream().filter(applicazioneValida -> {
      List<CosmoREnteApplicazioneEsterna> lista =
          applicazioneValida.getCosmoREnteApplicazioneEsternas().stream().filter(appEnte -> {

            appEnte.getCosmoTFunzionalitaApplicazioneEsternas()
            .removeIf(appEnteSingola -> appEnteSingola.cancellato()
                || appEnteSingola.getCosmoREnteFunzionalitaApplicazioneEsternas().stream()
                .anyMatch(CosmoREnteFunzionalitaApplicazioneEsterna::nonValido)

                );
            return null != appEnte.getCosmoTFunzionalitaApplicazioneEsternas()
                && !appEnte.getCosmoTFunzionalitaApplicazioneEsternas().isEmpty();

          }).collect(Collectors.toList());

      return null != lista && !lista.isEmpty();
    }).collect(Collectors.toList());
  }

}
