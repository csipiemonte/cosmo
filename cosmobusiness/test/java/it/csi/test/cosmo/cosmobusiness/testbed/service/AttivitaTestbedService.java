/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.service;

import java.util.ArrayList;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.test.cosmo.cosmobusiness.testbed.repository.CosmoTAttivitaTestbedRepository;

/**
 *
 */

@Service
@Transactional
public class AttivitaTestbedService {

  @Autowired
  private CosmoTAttivitaTestbedRepository cosmoTAttivitaTestbedRepository;

  @PersistenceContext
  private EntityManager em;

  @Transactional
  public CosmoTAttivita creaAttivita(CosmoTPratica pratica) {
    String randomUUID = UUID.randomUUID().toString();

    CosmoTAttivita entity = new CosmoTAttivita();

    entity.setCosmoRAttivitaAssegnaziones(new ArrayList<>());
    entity.setCosmoTPratica(pratica);
    entity.setDescrizione(
        "Descrizione attivita " + randomUUID + " della pratica " + pratica.getOggetto());
    entity.setDtInserimento(pratica.getDtInserimento());
    entity.setDtUltimaModifica(pratica.getDtInserimento());
    entity.setLinkAttivita("/pratiche/" + randomUUID);
    entity.setNome("Attivita " + randomUUID);
    entity.setSubtasks(new ArrayList<>());
    entity.setUtenteInserimento(pratica.getUtenteCreazionePratica());
    entity.setUtenteUltimaModifica(pratica.getUtenteCreazionePratica());

    entity = cosmoTAttivitaTestbedRepository.saveAndFlush(entity);
    em.refresh(entity);
    return entity;
  }


}
