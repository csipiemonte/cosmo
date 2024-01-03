/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.entities.CosmoDTipoNotifica_;
import it.csi.cosmo.common.entities.CosmoDTipoPratica_;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTMessaggioNotifica;
import it.csi.cosmo.common.entities.CosmoTMessaggioNotifica_;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.SearchUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmonotifications.business.service.ConfigurazioniMessaggiNotificheService;
import it.csi.cosmo.cosmonotifications.config.ErrorMessages;
import it.csi.cosmo.cosmonotifications.config.ParametriApplicativo;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotifica;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioneMessaggioNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.ConfigurazioniMessaggiNotificheResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.FiltroRicercaConfigurazioniMessaggiDTO;
import it.csi.cosmo.cosmonotifications.dto.rest.PageInfo;
import it.csi.cosmo.cosmonotifications.integration.mapper.CosmoTMessaggioNotificaMapper;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoEnteRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTMessaggioNotificaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTipoNotificaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoTipoPraticaRepository;
import it.csi.cosmo.cosmonotifications.integration.repository.specifications.CosmoTMessaggioNotificaSpecifications;
import it.csi.cosmo.cosmonotifications.security.SecurityUtils;
import it.csi.cosmo.cosmonotifications.util.logger.LogCategory;
import it.csi.cosmo.cosmonotifications.util.logger.LoggerFactory;

/**
 *
 */

@Service
@Transactional
public class ConfigurazioniMessaggiNotificheServiceImpl
implements ConfigurazioniMessaggiNotificheService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.COSMONOTIFICATIONS_BUSINESS_LOG_CATEGORY,
          "ConfigurazioniMessaggiNotificheServiceImpl");

  @Autowired
  private CosmoTMessaggioNotificaRepository cosmoTMessaggioNotificaRepository;

  @Autowired
  private CosmoEnteRepository cosmoTEnteRepository;

  @Autowired
  private CosmoTipoNotificaRepository cosmoDTipoNotificaRepository;

  @Autowired
  private CosmoTipoPraticaRepository cosmoDTipoPraticaRepository;

  @Autowired
  private CosmoTMessaggioNotificaMapper cosmoTMessaggioNotificaMapper;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  public ConfigurazioneMessaggioNotifica creaConfigurazioneMessaggio(
      ConfigurazioneMessaggioNotificaRequest body) {
    final String methodName = "creaConfigurazioneMessaggio";

    ValidationUtils.require(body.getCodiceTipoMessaggio(), "body.getCodiceTipoMessaggio");
    ValidationUtils.require(body.getIdEnte(), "body.getIdEnte");

    var ente = cosmoTEnteRepository.findOneNotDeleted(body.getIdEnte()).orElse(null);

    if (ente == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_ENTE_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_ENTE_NON_TROVATO);
    }

    var tipoMessaggio =
        cosmoDTipoNotificaRepository.findOneActive(body.getCodiceTipoMessaggio())
        .orElse(null);

    if (tipoMessaggio == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_MESSAGGIO_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_MESSAGGIO_NON_TROVATO);
    }

    var messaggioNotifica = new CosmoTMessaggioNotifica();

    messaggioNotifica.setCosmoDTipoNotifica(tipoMessaggio);
    messaggioNotifica.setCosmoTEnte(ente);
    messaggioNotifica.setTesto(body.getTesto());


    if (body.getCodiceTipoPratica() != null) {
      var tipoPratica =
          cosmoDTipoPraticaRepository.findOneActive(body.getCodiceTipoPratica()).orElse(null);

      if (tipoPratica == null || !tipoPratica.getCosmoTEnte().getId().equals(ente.getId())) {
        logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_PRATICA_NON_TROVATO);
        throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_PRATICA_NON_TROVATO);
      }

      messaggioNotifica.setCosmoDTipoPratica(tipoPratica);
    }

    checkUniqueConflict(body, null);

    return cosmoTMessaggioNotificaMapper
        .toDTO(cosmoTMessaggioNotificaRepository.save(messaggioNotifica));
  }

  @Override
  public ConfigurazioneMessaggioNotifica eliminaConfigurazioneMessaggio(
      Long idConfigurazioneMessaggio) {
    final String methodName = "eliminaConfigurazioneMessaggio";
    ValidationUtils.require(idConfigurazioneMessaggio, "idConfigurazioneMessaggio");

    if (SecurityUtils.getUtenteCorrente() == null
        || SecurityUtils.getUtenteCorrente().getCodiceFiscale() == null) {
      logger.error(methodName, ErrorMessages.UTENTE_NON_AUTENTICATO);
      throw new BadRequestException(ErrorMessages.UTENTE_NON_AUTENTICATO);
    }

    var messaggio =
        cosmoTMessaggioNotificaRepository.findOneNotDeleted(idConfigurazioneMessaggio).orElse(null);
    if (messaggio == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
    }
    messaggio.setDtCancellazione(Timestamp.valueOf(LocalDateTime.now()));
    messaggio.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    return cosmoTMessaggioNotificaMapper.toDTO(cosmoTMessaggioNotificaRepository.save(messaggio));
  }

  @Override
  public ConfigurazioneMessaggioNotifica modificaConfigurazioneMessaggio(
      Long idConfigurazioneMessaggio, ConfigurazioneMessaggioNotificaRequest body) {

    final String methodName = "modificaConfigurazioneMessaggio";
    ValidationUtils.require(idConfigurazioneMessaggio, "idConfigurazioneMessaggio");
    ValidationUtils.require(body.getCodiceTipoMessaggio(), "body.getCodiceTipoMessaggio");
    ValidationUtils.require(body.getIdEnte(), "body.getIdEnte");

    var messaggioNotifica =
        cosmoTMessaggioNotificaRepository.findOneNotDeleted(idConfigurazioneMessaggio).orElse(null);
    if (messaggioNotifica == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
    }

    var ente = cosmoTEnteRepository.findOneNotDeleted(body.getIdEnte()).orElse(null);

    if (ente == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_ENTE_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_ENTE_NON_TROVATO);
    }

    var tipoMessaggio =
        cosmoDTipoNotificaRepository.findOneActive(body.getCodiceTipoMessaggio())
        .orElse(null);

    if (tipoMessaggio == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_MESSAGGIO_NON_TROVATO);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_MESSAGGIO_NON_TROVATO);
    }

    messaggioNotifica.setCosmoDTipoNotifica(tipoMessaggio);
    messaggioNotifica.setCosmoTEnte(ente);
    messaggioNotifica.setTesto(body.getTesto());


    if (body.getCodiceTipoPratica() != null) {
      var tipoPratica =
          cosmoDTipoPraticaRepository.findOneActive(body.getCodiceTipoPratica()).orElse(null);

      if (tipoPratica == null || !tipoPratica.getCosmoTEnte().getId().equals(ente.getId())) {
        logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_PRATICA_NON_TROVATO);
        throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_TIPO_PRATICA_NON_TROVATO);
      }

      messaggioNotifica.setCosmoDTipoPratica(tipoPratica);
    } else {
      messaggioNotifica.setCosmoDTipoPratica(null);
    }

    checkUniqueConflict(body, idConfigurazioneMessaggio);

    return cosmoTMessaggioNotificaMapper
        .toDTO(cosmoTMessaggioNotificaRepository.save(messaggioNotifica));
  }

  @Override
  public ConfigurazioneMessaggioNotifica getConfigurazioneMessaggioId(
      Long idConfigurazioneMessaggio) {
    ValidationUtils.require(idConfigurazioneMessaggio, "idConfigurazioneMessaggio");
    final String methodName = "getConfigurazioneMessaggioId";
    var messaggioNotifica =
        cosmoTMessaggioNotificaRepository.findOneNotDeleted(idConfigurazioneMessaggio).orElse(null);
    if (messaggioNotifica == null) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_NON_TROVATA);
    }
    return cosmoTMessaggioNotificaMapper.toDTO(messaggioNotifica);
  }

  @Override
  public ConfigurazioniMessaggiNotificheResponse getConfigurazioniMessaggi(String filter) {

    ConfigurazioniMessaggiNotificheResponse output = new ConfigurazioniMessaggiNotificheResponse();

    GenericRicercaParametricaDTO<FiltroRicercaConfigurazioniMessaggiDTO> ricercaParametrica =
        SearchUtils.getRicercaParametrica(filter, FiltroRicercaConfigurazioniMessaggiDTO.class);

    Pageable paging = SearchUtils.getPageRequest(ricercaParametrica,
        configurazioneService.requireConfig(ParametriApplicativo.MAX_PAGE_SIZE).asInteger());

    Page<CosmoTMessaggioNotifica> pageMessaggi =
        cosmoTMessaggioNotificaRepository.findAllNotDeleted(CosmoTMessaggioNotificaSpecifications
            .findByFilters(ricercaParametrica.getFilter(), ricercaParametrica.getSort()), paging);

    List<CosmoTMessaggioNotifica> messaggiDB = pageMessaggi.getContent();

    List<ConfigurazioneMessaggioNotifica> configurazioniMessaggi = new LinkedList<>();
    messaggiDB.forEach(messaggioDB -> configurazioniMessaggi
        .add(cosmoTMessaggioNotificaMapper.toLightDTO(messaggioDB)));
    output.setConfigurazioniMessaggi(configurazioniMessaggi);

    if (!StringUtils.isBlank(ricercaParametrica.getFields())) {
      SearchUtils.filterFields(configurazioniMessaggi,
          Arrays.asList(ricercaParametrica.getFields().split(",")));
    }

    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(pageMessaggi.getNumber());
    pageInfo.setPageSize(pageMessaggi.getSize());
    pageInfo.setTotalElements(Math.toIntExact(pageMessaggi.getTotalElements()));
    pageInfo.setTotalPages(pageMessaggi.getTotalPages());
    output.setPageInfo(pageInfo);
    return output;
  }

  private void checkUniqueConflict(ConfigurazioneMessaggioNotificaRequest messaggio,
      Long excludeId) {

    final String methodName = "checkUniqueConflict";

    var messaggioNotifica = cosmoTMessaggioNotificaRepository.findAll((root, cq, cb) -> {
      var predicate =
          cb.and(cb.equal(root.get(CosmoTMessaggioNotifica_.cosmoTEnte).get(CosmoTEnte_.id),
              messaggio.getIdEnte()),
              cb.equal(root.get(CosmoTMessaggioNotifica_.cosmoDTipoNotifica)
                  .get(CosmoDTipoNotifica_.codice), messaggio.getCodiceTipoMessaggio()),
              cb.isNull(root.get(CosmoTEntity_.dtCancellazione)));
      if (messaggio.getCodiceTipoPratica() != null) {
        predicate = cb.and(predicate,
            cb.equal(
                root.get(CosmoTMessaggioNotifica_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice),
                messaggio.getCodiceTipoPratica()));
      }else {
        predicate = cb.and(predicate, cb.isNull(
            root.get(CosmoTMessaggioNotifica_.cosmoDTipoPratica).get(CosmoDTipoPratica_.codice)));
      }
      if (excludeId != null) {
        predicate =
            cb.and(predicate, cb.notEqual(root.get(CosmoTMessaggioNotifica_.id), excludeId));
      }
      return cq
          .where(predicate)
          .getRestriction();
    });

    if (!messaggioNotifica.isEmpty()) {
      logger.error(methodName, ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_PRESENTE);
      throw new BadRequestException(ErrorMessages.MESSAGGI_NOTIFICHE_CONFIGURAZIONE_PRESENTE);
    }
  }

}
