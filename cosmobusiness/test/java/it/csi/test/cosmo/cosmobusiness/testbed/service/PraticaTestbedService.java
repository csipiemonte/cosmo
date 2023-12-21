/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.test.cosmo.cosmobusiness.testbed.repository.CosmoDTipoPraticaTestbedRepository;
import it.csi.test.cosmo.cosmobusiness.testbed.repository.CosmoTPraticaTestbedRepository;

/**
 *
 */

@Service
@Transactional
public class PraticaTestbedService {

  @Autowired
  private CosmoTPraticaTestbedRepository cosmoTPraticaTestbedRepository;

  @Autowired
  private CosmoDTipoPraticaTestbedRepository cosmoDTipoPraticaTestbedRepository;

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public CosmoDTipoPratica creaTipoPratica(CosmoTEnte ente) {
    String randomUUID = UUID.randomUUID().toString();

    CosmoDTipoPratica entity = new CosmoDTipoPratica();

    entity.setCodice(randomUUID);
    entity.setProcessDefinitionKey(randomUUID);
    entity.setDtInizioVal(Timestamp.from(Instant.now()));
    entity.setDescrizione("Tipo pratica " + randomUUID);
    entity.setCosmoTEnte(ente);
    entity.setCreabileDaInterfaccia(true);
    entity.setCreabileDaServizio(true);
    entity.setAnnullabile(true);
    entity.setCondivisibile(true);
    entity.setAssegnabile(true);
    entity.setOverrideFruitoreDefault(false);

    entity = cosmoDTipoPraticaTestbedRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }

  public CosmoDTipoPratica update(CosmoDTipoPratica entity) {
    entity = cosmoDTipoPraticaTestbedRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }

  /**
   * @param codice
   */
  @Transactional
  public CosmoTPratica creaPratica(CosmoTEnte ente, CosmoTFruitore fruitore,
      CosmoDTipoPratica tipoPratica) {
    String randomUUID = UUID.randomUUID().toString();

    CosmoTPratica entity = new CosmoTPratica();

    entity.setEnte(ente);
    entity.setFruitore(fruitore);
    entity.setIdPraticaExt(randomUUID);
    entity.setLinkPratica("tasks/" + randomUUID);
    entity.setOggetto("Oggetto della pratica " + randomUUID);
    entity.setRiassunto("Riassunto della pratica " + randomUUID);
    entity.setTipo(tipoPratica);
    // entity.setStato(stato);
    entity.setUuidNodo("000-000-000-000");

    entity = cosmoTPraticaTestbedRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }


}
