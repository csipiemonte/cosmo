/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.testbed.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoRFruitoreEnte;
import it.csi.cosmo.common.entities.CosmoRFruitoreEntePK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoRFruitoreEnteTestRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoTFruitoreTestRepository;

/**
 *
 */

@Service
@Transactional
public class FruitoreTestbedService {

  @Autowired
  private CosmoTFruitoreTestRepository cosmoTFruitoreRepository;

  @Autowired
  private CosmoRFruitoreEnteTestRepository cosmoRFruitoreEnteRepository;

  @PersistenceContext
  private EntityManager em;

  /**
   * @param codice
   */
  @Transactional
  public CosmoTFruitore creaFruitore() {
    String randomUUID = UUID.randomUUID().toString();

    CosmoTFruitore entity = new CosmoTFruitore();
    entity.setApiManagerId(randomUUID);
    entity.setNomeApp("Fruitore " + randomUUID);
    entity.setUrl("http://fruitore-test/" + randomUUID);

    entity = cosmoTFruitoreRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }

  @Transactional
  public CosmoRFruitoreEnte associaAdEnte(CosmoTFruitore fruitore, CosmoTEnte ente) {
    var associazione = new CosmoRFruitoreEnte();
    associazione.setCosmoTEnte(ente);
    associazione.setCosmoTFruitore(fruitore);
    associazione.setDtInizioVal(Timestamp.from(Instant.now()));
    var id = new CosmoRFruitoreEntePK();
    id.setIdEnte(ente.getId());
    id.setIdFruitore(fruitore.getId());
    associazione.setId(id);

    associazione = cosmoRFruitoreEnteRepository.saveAndFlush(associazione);
    em.refresh(associazione);
    em.refresh(fruitore);
    em.refresh(ente);
    return associazione;
  }


}
