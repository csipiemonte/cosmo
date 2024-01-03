/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoRelazionePratica;
import it.csi.cosmo.common.entities.CosmoRPraticaPratica;
import it.csi.cosmo.common.entities.CosmoRPraticaPraticaPK;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.proto.CosmoREntity;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmopratiche.business.service.RelazionePraticaPraticaService;
import it.csi.cosmo.cosmopratiche.config.ErrorMessages;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoDTipoRelazionePraticaMapper;
import it.csi.cosmo.cosmopratiche.integration.mapper.CosmoTPraticheMapper;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoRPraticaPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTipoRelazionePraticaRepository;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.cosmo.cosmopratiche.util.logger.LogCategory;
import it.csi.cosmo.cosmopratiche.util.logger.LoggerFactory;

/**
 *
 */
@Service
@Transactional
public class RelazionePraticaPraticaServiceImpl implements RelazionePraticaPraticaService {

  private static final String CLASS_NAME = RelazionePraticaPraticaServiceImpl.class.getSimpleName();

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMOPRATICHE_BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @Autowired
  private CosmoTipoRelazionePraticaRepository tipoRelazionePraticaRepository;

  @Autowired
  private CosmoRPraticaPraticaRepository relazionePraticaPraticaRepository;

  @Autowired
  private CosmoTPraticaRepository praticaRepository;

  @Autowired
  private CosmoDTipoRelazionePraticaMapper cosmoDTipoRelazionePraticaMapper;

  @Autowired
  private CosmoTPraticheMapper praticaMapper;

  @Override
  @Transactional(readOnly = true)
  public List<TipoRelazionePraticaPratica> getTipiRelazionePraticaPratica() {

    var tipiRelazionePratiche = tipoRelazionePraticaRepository.findAllActive();

    List<TipoRelazionePraticaPratica> output = new LinkedList<>();
    tipiRelazionePratiche
    .forEach(single -> output.add(cosmoDTipoRelazionePraticaMapper.toDTOA(single)));

    return output;
  }

  @Override
  @Transactional(readOnly = true)
  public List<PraticaInRelazione> getPraticheInRelazione(Long idPratica) {
    final String methodName = "getPraticheInRelazione";

    ValidationUtils.validaAnnotations(idPratica);

    CosmoTPratica pratica = praticaRepository.findOneNotDeleted(idPratica).orElseThrow(() -> {
      final String message = String.format(ErrorMessages.PRATICA_CON_ID_NON_TROVATA, idPratica);
      logger.error(methodName, message);
      throw new BadRequestException(message);
    });

    UserInfoDTO userInfo = SecurityUtils.getUtenteCorrente();

    List<PraticaInRelazione> output = new LinkedList<>();

    pratica.getCosmoRPraticaPraticasA().stream().filter(CosmoREntity::valido).forEach(p -> {
      PraticaInRelazione pirA = new PraticaInRelazione();
      pirA.setPratica(praticaMapper.toPractice(p.getCosmoTPraticaA(), null, userInfo));
      pirA.setTipoRelazione(
          cosmoDTipoRelazionePraticaMapper.toDTOA(p.getCosmoDTipoRelazionePratica()));
      output.add(pirA);
    });

    pratica.getCosmoRPraticaPraticasDa().stream().filter(CosmoREntity::valido).forEach(p -> {
      PraticaInRelazione pirDa = new PraticaInRelazione();
      pirDa.setPratica(praticaMapper.toPractice(p.getCosmoTPraticaDa(), null, userInfo));
      pirDa.setTipoRelazione(
          cosmoDTipoRelazionePraticaMapper.toDTODa(p.getCosmoDTipoRelazionePratica()));
      output.add(pirDa);
    });

    return output;
  }

  @Override
  public Pratica creaAggiornaRelazioni(String idPratica, String codiceTipoRelazione,
      List<BigDecimal> idPraticheDaRelazionare) {
    final String methodName = "creaAggiornaRelazioni";

    ValidationUtils.validaAnnotations(idPratica);
    ValidationUtils.validaAnnotations(codiceTipoRelazione);

    if (!StringUtils.isNumeric(idPratica)) {
      final String message = "Id Pratica " + ErrorMessages.NON_VALIDO;
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    if (idPraticheDaRelazionare.isEmpty()) {
      final String message = "Nessuna pratica da associare a " + idPratica;
      logger.error(methodName, message);
      throw new BadRequestException(message);
    }

    CosmoTPratica praticaDa =
        praticaRepository.findOneNotDeleted(Long.parseLong(idPratica)).orElseThrow(() -> {
          final String message =
              String.format(ErrorMessages.PRATICA_CON_ID_NON_TROVATA, Long.parseLong(idPratica));
          logger.error(methodName, message);
          throw new BadRequestException(message);
        });

    CosmoDTipoRelazionePratica tipoRelazionePratica =
        tipoRelazionePraticaRepository.findOneActive(codiceTipoRelazione).orElseThrow(() -> {
          final String message = String
              .format(ErrorMessages.TIPO_RELAZIONE_PRATICA_CON_ID_NON_TROVATA, codiceTipoRelazione);
          logger.error(methodName, message);
          throw new BadRequestException(message);
        });

    for (var idPraticaA : idPraticheDaRelazionare) {

      if (idPratica.equals("" + idPraticaA)) {
        final String message = "Impossibile mettere in relazione una pratica con se stessa";
        logger.error(methodName, message);
        throw new BadRequestException(message);

      }

      CosmoTPratica praticaA =
          praticaRepository.findOneNotDeleted(idPraticaA.longValue()).orElseThrow(() -> {
            final String message =
                String.format(ErrorMessages.PRATICA_CON_ID_NON_TROVATA, idPraticaA.longValue());
            logger.error(methodName, message);
            throw new BadRequestException(message);
          });

      CosmoRPraticaPratica relazionePraticaPratica = relazionePraticaPraticaRepository
          .findOneByCosmoTPraticaDaIdAndCosmoTPraticaAIdAndCosmoDTipoRelazionePraticaCodice(
              praticaDa.getId(), praticaA.getId(), tipoRelazionePratica.getCodice());

      if (relazionePraticaPratica == null) {

        CosmoRPraticaPraticaPK relazionePraticaPraticaPK = new CosmoRPraticaPraticaPK();
        relazionePraticaPraticaPK.setIdPraticaDa(praticaDa.getId());
        relazionePraticaPraticaPK.setIdPraticaA(praticaA.getId());
        relazionePraticaPraticaPK.setCodiceTipoRelazione(tipoRelazionePratica.getCodice());

        relazionePraticaPratica = new CosmoRPraticaPratica();
        relazionePraticaPratica.setId(relazionePraticaPraticaPK);
        relazionePraticaPratica.setCosmoTPraticaDa(praticaDa);
        relazionePraticaPratica.setCosmoTPraticaA(praticaA);
        relazionePraticaPratica.setCosmoDTipoRelazionePratica(tipoRelazionePratica);
        relazionePraticaPratica.setDtInizioVal(Timestamp.valueOf(LocalDateTime.now()));

      } else {
        if (relazionePraticaPratica.getDtFineVal() == null) {
          relazionePraticaPratica.setDtFineVal(Timestamp.valueOf(LocalDateTime.now()));
        } else {
          relazionePraticaPratica.setDtFineVal(null);
        }
      }
      relazionePraticaPraticaRepository.save(relazionePraticaPratica);

    }

    return praticaMapper.toPractice(praticaDa, null, SecurityUtils.getUtenteCorrente());

  }
}
