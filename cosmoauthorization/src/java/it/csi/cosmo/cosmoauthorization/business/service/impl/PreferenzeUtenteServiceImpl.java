/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.entities.CosmoTPreferenzeUtente;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeUtenteService;
import it.csi.cosmo.cosmoauthorization.config.Constants;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmoauthorization.integration.mapper.CosmoTPreferenzeUtenteMapper;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTPreferenzeUtenteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmoauthorization.security.SecurityUtils;
import it.csi.cosmo.cosmoauthorization.util.CommonUtils;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class PreferenzeUtenteServiceImpl implements PreferenzeUtenteService {

  private static final String CLASS_NAME = PreferenzeUtenteServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTPreferenzeUtenteRepository cosmoTPreferenzeUtenteRepository;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoTPreferenzeUtenteMapper cosmoTPreferenzeUtenteMapper;


  @Override
  @Transactional(readOnly = true)
  public Preferenza getPreferenzeUtente() {

    CosmoTUtente utente = getUtenteCorrente();

    return getPreferenza(utente);

  }


  @Override
  public Preferenza getPreferenzeUtente(String id) {

    CommonUtils.validaDatiInput(id, "id utente");

    CosmoTUtente utente = cosmoTUtenteRepository.findOne(Long.valueOf(id));

    if (utente == null) {
      final String message = String.format(ErrorMessages.U_UTENTE_NON_TROVATO, id);
      logger.error("getPreferenzeUtente", message);
      throw new NotFoundException(message);
    }

    return getPreferenza(utente);
  }

  private Preferenza getPreferenza(CosmoTUtente utente) {
    final String methodName = "getPreferenza";

    ConfigurazioneDTO versione = configurazioneService.getConfig(Constants.USER_PREF_VERSION);

    if (versione == null || StringUtils.isBlank(versione.getValue())) {
      logger.error(methodName, ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA);
      throw new NotFoundException(ErrorMessages.C_CONFIGURAZIONE_NON_TROVATA);
    }

    CosmoTPreferenzeUtente preferenzeUtente =
        cosmoTPreferenzeUtenteRepository.findByVersioneAndCosmoTUtente(versione.getValue(), utente);

    if (preferenzeUtente == null) {
      logger.error(methodName, String
          .format(ErrorMessages.PU_PREFERENZE_VERSIONE_UTENTE_NON_TROVATE, versione));
      return null;
    }

    return cosmoTPreferenzeUtenteMapper.toDTO(preferenzeUtente);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Preferenza createPreferenzeUtente(Preferenza preferenzeUtente) {
    final String methodName = "createPreferenzeUtente";

    CosmoTUtente utente = getUtenteCorrente();

    CosmoTPreferenzeUtente preferenzeUtenteDaSalvare = cosmoTPreferenzeUtenteRepository
        .findByVersioneAndCosmoTUtente(preferenzeUtente.getVersione(), utente);

    if (preferenzeUtenteDaSalvare != null) {
      logger.error(methodName,
          String.format(ErrorMessages.PU_PREFERENZE_VERSIONE_UTENTE_GIA_ESISTENTI,
              preferenzeUtente.getVersione()));
      throw new ConflictException(
          String.format(ErrorMessages.PU_PREFERENZE_VERSIONE_UTENTE_GIA_ESISTENTI,
              preferenzeUtente.getVersione()));
    }

    preferenzeUtenteDaSalvare =
        cosmoTPreferenzeUtenteMapper.toRecord(preferenzeUtente);
    preferenzeUtenteDaSalvare.setCosmoTUtente(utente);
    // preferenzeUtenteDaSalvare.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));
    preferenzeUtenteDaSalvare = cosmoTPreferenzeUtenteRepository.save(preferenzeUtenteDaSalvare);

    return cosmoTPreferenzeUtenteMapper.toDTO(preferenzeUtenteDaSalvare);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Preferenza updatePreferenzeUtente(Preferenza preferenzeUtente) {
    final String methodName = "updatePreferenzeUtente";

    CosmoTUtente utente = getUtenteCorrente();

    CosmoTPreferenzeUtente preferenzeUtenteDaAggiornare = cosmoTPreferenzeUtenteRepository
        .findByVersioneAndCosmoTUtente(preferenzeUtente.getVersione(), utente);

    if (preferenzeUtenteDaAggiornare == null) {
      logger.error(methodName,
          String.format(ErrorMessages.PU_PREFERENZE_VERSIONE_UTENTE_NON_TROVATE,
              preferenzeUtente.getVersione()));
      throw new NotFoundException(
          String.format(ErrorMessages.PU_PREFERENZE_VERSIONE_UTENTE_NON_TROVATE,
              preferenzeUtente.getVersione()));
    }

    preferenzeUtenteDaAggiornare
    .setValore(cosmoTPreferenzeUtenteMapper.toRecord(preferenzeUtente).getValore());
    preferenzeUtenteDaAggiornare =
        cosmoTPreferenzeUtenteRepository.save(preferenzeUtenteDaAggiornare);

    return cosmoTPreferenzeUtenteMapper.toDTO(preferenzeUtenteDaAggiornare);
  }

  private CosmoTUtente getUtenteCorrente() {
    CosmoTUtente utente = cosmoTUtenteRepository
        .findByCodiceFiscale(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    if (utente == null) {
      logger.error("getUtenteCorrente", ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
      throw new NotFoundException(ErrorMessages.U_UTENTE_CORRENTE_NON_TROVATO);
    }
    return utente;
  }

}
