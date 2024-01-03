/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTUtenteRepository;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class StoricoPraticaServiceImplTest extends ParentIntegrationTest {
  
  @Autowired
  StoricoPraticaService storicoPraticaService;
  
  @Autowired
  CosmoTFruitoreRepository cosmoFruitoreRepository;
  
  @Autowired
  CosmoTUtenteRepository cosmoUtenteRepository;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test(expected = NotFoundException.class)
  public void getStoricoAttivitaNotFound() {
    storicoPraticaService.getStoricoAttivita(64L);
  }
  
  @Test
  public void getStoricoAttivita() {
   StoricoPratica storico = storicoPraticaService.getStoricoAttivita(1L);
   assertNotNull(storico);
   assertTrue(storico.getAssegnazioni().size() == 7);
   assertTrue(storico.getAttivita().size() == 1);
   assertTrue(storico.getEventi().size() == 4);
  }
  
  @Test
  public void logEventUtenteEFruitoreNull() {
    CosmoLStoricoPratica entry = new CosmoLStoricoPratica();
    entry.setDtEvento(null);
    entry.setUtente(null);
    entry.setFruitore(null);
    entry.setCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_ANNULLATA);
    CosmoTPratica pratica = new CosmoTPratica();
    pratica.setId(1L);
    entry.setPratica(pratica);
    storicoPraticaService.logEvent(entry);
  }
  
  @Test(expected = NotFoundException.class)
  public void logEventStoricoPraticaNotFound() {
    StoricoPraticaRequest request = new StoricoPraticaRequest();
   request.setIdPratica(111L);
  storicoPraticaService.logEvent(request);
  }
  
  @Test
  public void logEventStoricoPratica() {
    StoricoPraticaRequest request = new StoricoPraticaRequest();
   request.setIdPratica(1L);
   request.setCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_ANNULLATA.name());
   request.setDescrizioneEvento("annullata");
   storicoPraticaService.logEvent(request);
  }
  
  @Test
  public void getStoricoAttivitaConAttivitaInCorso() {
   StoricoPratica storico = storicoPraticaService.getStoricoAttivita(2L);
   assertNotNull(storico);
   assertTrue(storico.getAttivita().size() == 1);
   assertTrue(storico.getEventi().size() == 1);
   assertTrue(storico.getEventi().get(0).getTipo().equals(TipoEventoStoricoPratica.ATTIVITA_CREATA.name()));
  }
}
