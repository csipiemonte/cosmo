/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.service;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.test.cosmo.cosmobusiness.testbed.repository.CosmoTEnteTestbedRepository;

/**
 *
 */

@Service
@Transactional
public class EnteTestbedService {

  @Autowired
  private CosmoTEnteTestbedRepository cosmoTEnteTestbedRepository;

  @PersistenceContext
  private EntityManager em;

  /**
   * @param codice
   */
  @Transactional
  public CosmoTEnte creaEnte() {
    String randomUUID = UUID.randomUUID().toString();

    CosmoTEnte entity = new CosmoTEnte();
    entity.setCodiceFiscale(randomUUID.substring(0, 16));
    entity.setCodiceIpa(randomUUID.substring(0, 10));
    entity.setNome("Ente " + randomUUID);

    entity = cosmoTEnteTestbedRepository.save(entity);
    cosmoTEnteTestbedRepository.flush();
    em.refresh(entity);
    return entity;
  }

}
