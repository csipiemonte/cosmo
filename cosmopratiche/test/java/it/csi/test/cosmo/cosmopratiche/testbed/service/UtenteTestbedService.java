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
import it.csi.cosmo.common.entities.CosmoRUtenteEnte;
import it.csi.cosmo.common.entities.CosmoRUtenteEntePK;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoRUtenteEnteTestbedRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.repository.CosmoTUtenteTestbedRepository;

/**
 *
 */

@Service
@Transactional
public class UtenteTestbedService {

  @Autowired
  private CosmoTUtenteTestbedRepository cosmoTUtenteTestbedRepository;

  @Autowired
  private CosmoRUtenteEnteTestbedRepository cosmoRUtenteEnteTestbedRepository;

  @PersistenceContext
  private EntityManager em;

  /**
   * @param codice
   */
  @Transactional
  public CosmoTUtente creaUtente() {
    String randomUUID = UUID.randomUUID().toString();

    CosmoTUtente entity = new CosmoTUtente();
    entity.setCodiceFiscale(randomUUID.replace('-', '0').substring(0, 16));
    entity.setCognome(randomUUID);
    entity.setNome("Utente");

    entity = cosmoTUtenteTestbedRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }

  @Transactional
  public CosmoRUtenteEnte associaAdEnte(CosmoTUtente utente, CosmoTEnte ente) {
    var associazione = new CosmoRUtenteEnte();
    associazione.setCosmoTEnte(ente);
    associazione.setCosmoTUtente(utente);
    associazione.setDtInizioVal(Timestamp.from(Instant.now()));
    var id = new CosmoRUtenteEntePK();
    id.setIdEnte(ente.getId());
    id.setIdUtente(utente.getId());
    associazione.setId(id);
    associazione.setEmail(utente.getCodiceFiscale() + "@cosmo.it");
    associazione.setTelefono("123-000" + utente.getId());

    associazione = cosmoRUtenteEnteTestbedRepository.saveAndFlush(associazione);
    em.refresh(associazione);
    em.refresh(utente);
    em.refresh(ente);
    return associazione;
  }


}
