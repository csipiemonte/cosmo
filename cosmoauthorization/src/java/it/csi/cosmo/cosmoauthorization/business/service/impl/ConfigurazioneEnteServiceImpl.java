/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK_;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoCConfigurazioneEnteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ConfigurazioneEnteServiceImpl implements ConfigurazioneEnteService {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoCConfigurazioneEnteRepository configurazioneEnteRepository;

  @Autowired
  private CosmoTEnteRepository enteRepository;

  @Autowired
  private CosmoCConfigurazioneEnteMapper configurazioneEnteMapper;

  private static final String CODICE_PROFILO_UTENTE_DEFAULT = "profilo.utente.default";

  @Override
  @Transactional(readOnly = true)
  public ConfigurazioneEnte getConfigurazioneEnte(BigDecimal idEnte, String chiave) {
    String methodName = "getConfigurazioneEnte";

    if (StringUtils.isBlank(chiave)) {
      String message = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "chiave");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    CosmoCConfigurazioneEnte configurazione = configurazioneEnteRepository.findOne(
        (root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                chiave),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                getIdEnte(idEnte)),
            cb.isNull(root.get(CosmoCEntity_.dtFineVal))));

    if (configurazione == null || StringUtils.isBlank(configurazione.getValore())) {
      logger.error(methodName, ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
      throw new BadRequestException(ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
    }

    return configurazioneEnteMapper.toDTO(configurazione);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ConfigurazioneEnte> getConfigurazioniEnte(BigDecimal idEnte) {

    List<CosmoCConfigurazioneEnte> configurazioni =
        configurazioneEnteRepository.findAllActive((root, cq, cb) -> cb.and(
            cb.notEqual(
                root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                CODICE_PROFILO_UTENTE_DEFAULT),
            cb.equal(
                root.get(CosmoCConfigurazioneEnte_.cosmoTEnte).get(CosmoTEnte_.id),
                getIdEnte(idEnte))));

    return configurazioneEnteMapper.toDTOs(configurazioni);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ConfigurazioneEnte postConfigurazioneEnte(BigDecimal idEnte,
      ConfigurazioneEnte configurazione) {
    String methodName = "postConfigurazioneEnte";

    ValidationUtils.validaAnnotations(configurazione);

    CosmoTEnte ente = enteRepository.getOne(getIdEnte(idEnte));

    if (ente == null || ente.getId() == null) {
      logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
    }
    if (configurazione.getChiave().equals(CODICE_PROFILO_UTENTE_DEFAULT)) {
      logger.error(methodName, "Valore chiave non valido");
      throw new BadRequestException("Valore chiave non valido");
    }

    CosmoCConfigurazioneEnte configurazioneEsistente =
        configurazioneEnteRepository.findOne((root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                configurazione.getChiave()),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                ente.getId())));

    if (configurazioneEsistente == null) {
      CosmoCConfigurazioneEnte configurazioneDaSalvare =
          configurazioneEnteMapper.toRecord(configurazione);
      CosmoCConfigurazioneEntePK pk = new CosmoCConfigurazioneEntePK();
      pk.setChiave(configurazione.getChiave());
      pk.setIdEnte(ente.getId());
      configurazioneDaSalvare.setId(pk);
      configurazioneDaSalvare.setCosmoTEnte(ente);
      configurazioneDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

      configurazioneEnteRepository.save(configurazioneDaSalvare);

      return configurazioneEnteMapper.toDTO(configurazioneDaSalvare);
    }

    if (configurazioneEsistente.getDtFineVal() == null) {
      String message =
          String.format(ErrorMessages.CONFIGURAZIONE_ENTE_ATTIVA, configurazione.getChiave());
      logger.error(methodName, message);
      throw new BadRequestException(message);

    } else {
      configurazioneEsistente.setValore(configurazione.getValore());
      configurazioneEsistente.setDescrizione(configurazione.getDescrizione());
      configurazioneEsistente.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
      configurazioneEsistente.setDtFineVal(null);

      configurazioneEsistente = configurazioneEnteRepository.save(configurazioneEsistente);

      return configurazioneEnteMapper.toDTO(configurazioneEsistente);
    }

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ConfigurazioneEnte putConfigurazioneEnte(BigDecimal idEnte, String chiave,
      ConfigurazioneEnte configurazione) {
    String methodName = "putConfigurazioneEnte";

    if (StringUtils.isBlank(chiave)) {
      String message = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "chiave");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    if (chiave.equals(CODICE_PROFILO_UTENTE_DEFAULT)) {
      logger.error(methodName, "Valore chiave non valido");
      throw new BadRequestException("Valore chiave non valido");
    }

    ValidationUtils.validaAnnotations(configurazione);

    CosmoCConfigurazioneEnte configurazioneDaAggiornare = configurazioneEnteRepository.findOne(
        (root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                chiave),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                getIdEnte(idEnte))));

    if (configurazioneDaAggiornare == null
        || StringUtils.isBlank(configurazioneDaAggiornare.getValore())) {
      logger.error(methodName, ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
      throw new BadRequestException(ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
    }

    configurazioneDaAggiornare.setValore(configurazione.getValore());
    configurazioneDaAggiornare.setDescrizione(configurazione.getDescrizione());

    configurazioneDaAggiornare = configurazioneEnteRepository.save(configurazioneDaAggiornare);

    return configurazioneEnteMapper.toDTO(configurazioneDaAggiornare);

  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteConfigurazioneEnte(BigDecimal idEnte, String chiave) {
    String methodName = "deleteConfigurazioneEnte";

    CosmoCConfigurazioneEnte configurazione = configurazioneEnteRepository.findOne(
        (root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                chiave),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                getIdEnte(idEnte)),
            cb.isNull(root.get(CosmoCEntity_.dtFineVal))));

    if (configurazione == null || StringUtils.isBlank(configurazione.getValore())) {
      logger.error(methodName, ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
      throw new BadRequestException(ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
    }

    configurazioneEnteRepository.deactivate(configurazione);
  }

  private Long getIdEnte(BigDecimal idEnte) {
    String methodName = "getIdEnte";


    Long output = null;
    if (idEnte == null) {
      EnteDTO ente = SecurityUtils.getUtenteCorrente().getEnte();

      if (ente == null || ente.getId() == null) {
        logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
        throw new BadRequestException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
      }
      output = ente.getId();
    } else {
      output = idEnte.longValue();
    }

    if (output == null) {
      String message = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "id ente");
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    return output;
  }
}
