/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTFruitore_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoLStoricoPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

@Service
public class StoricoPraticaServiceImpl implements StoricoPraticaService {

  /*
   * utile per prevenire lock incrociati coi listener e altre chiamate automatiche sincrone
   */
  private static boolean POLICY_IMMEDIATE_FLUSH = true;
  
  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoLStoricoPraticaRepository cosmoLStoricoPraticaRepository;

  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;

  @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
  @Override
  public void logEvent(CosmoLStoricoPratica entry) {
    final var method = "logEvent";
    logger.debug(method, "registro evento {} per pratica {}", entry.getCodiceTipoEvento(), entry.getPratica() != null ?
        entry.getPratica().getId() : null);
    
    preProcess(entry);
    
    cosmoLStoricoPraticaRepository.insert(entry);
    if (POLICY_IMMEDIATE_FLUSH) {
      cosmoLStoricoPraticaRepository.flush();
    }
  }

  protected void preProcess(CosmoLStoricoPratica entry) {
    if (entry.getDtEvento() == null) {
      entry.setDtEvento(Timestamp.from(Instant.now()));
    }

    /*
     * popolo automaticamente utente e/o fruitore solo se 
     * nessuno dei due e' stato popolato precedentemente.
     */
    if (entry.getUtente() == null && entry.getFruitore() == null) {

      if (entry.getUtente() == null && !Boolean.FALSE.equals(entry.detectUtenteCorrente())) {
        UserInfoDTO utenteCorrente = SecurityUtils.getUtenteCorrente();
        if (utenteCorrente != null && Boolean.FALSE.equals(utenteCorrente.getAnonimo())) {
          entry.setUtente(cosmoLStoricoPraticaRepository.reference(CosmoTUtente.class, utenteCorrente.getId()));
        }
      }
      
      if (entry.getFruitore() != null && !Boolean.FALSE.equals(entry.detectFruitoreCorrente())) {
        ClientInfoDTO client = SecurityUtils.getClientCorrente();
        CosmoTFruitore fruitoreEntity = Optional.ofNullable(client)
            .filter(c -> Boolean.FALSE.equals(c.getAnonimo()))
            .map(c -> cosmoTFruitoreRepository
            .findOneByField(CosmoTFruitore_.apiManagerId, c.getCodice()).orElse(null)).orElse(null);

        if (fruitoreEntity != null) {
          entry.setFruitore(cosmoLStoricoPraticaRepository.reference(CosmoTFruitore.class, fruitoreEntity.getId()));
        }
      }
    }
  }
}
