/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEntePK_;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.proto.CosmoCEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmosoap.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmosoap.config.ErrorMessages;
import it.csi.cosmo.cosmosoap.config.ParametriApplicativo;
import it.csi.cosmo.cosmosoap.dto.acta.InformazioniActaEnte;
import it.csi.cosmo.cosmosoap.integration.mapper.CosmoCConfigurazioneEnteMapper;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmosoap.security.SecurityUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class ConfigurazioneEnteServiceImpl implements ConfigurazioneEnteService {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Autowired
  private CosmoCConfigurazioneEnteRepository configurazioneEnteRepository;

  @Autowired
  private CosmoCConfigurazioneEnteMapper configurazioneEnteMapper;

  @Override
  @Transactional(readOnly = true)
  public ConfigurazioneDTO getConfigEnte(String chiave) {
    String methodName = "getConfigEnte";

    if (StringUtils.isBlank(chiave)) {
      String message = String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "chiave");
      logger.error(methodName, message);
      throw new BadRequestException(message);

    }

    EnteDTO ente = SecurityUtils.getUtenteCorrente().getEnte();

    if (ente == null || ente.getId() == null) {
      logger.error(methodName, ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.E_ENTE_CORRENTE_NON_TROVATO);
    }

    CosmoCConfigurazioneEnte configurazione = configurazioneEnteRepository.findOne(
        (root, cq, cb) -> cb.and(
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.chiave),
                chiave),
            cb.equal(root.get(CosmoCConfigurazioneEnte_.id).get(CosmoCConfigurazioneEntePK_.idEnte),
                ente.getId()),
            cb.isNull(root.get(CosmoCEntity_.dtFineVal))));

    if (configurazione == null || StringUtils.isBlank(configurazione.getValore())) {
      logger.error(methodName, ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);
      throw new BadRequestException(ErrorMessages.PARAMETRO_NON_DEFINITO_IN_CONFIGURAZIONE);

    }

    return configurazioneEnteMapper.toDTO(configurazione);
  }

  @Override
  public InformazioniActaEnte getConfigurazioneActaEnte() {

    return InformazioniActaEnte.builder()
        .withAppKey(getConfigEnte(
            configurazioneService.requireConfig(ParametriApplicativo.ACTA_APP_KEY).asString())
            .getValue())
        .withRepositoryName(getConfigEnte(configurazioneService
            .requireConfig(ParametriApplicativo.ACTA_REPOSITORY_NAME).asString()).getValue())
        .build();
  }

}
