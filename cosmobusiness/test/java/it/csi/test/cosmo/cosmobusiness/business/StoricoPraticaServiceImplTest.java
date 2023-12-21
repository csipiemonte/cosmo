/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

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
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class StoricoPraticaServiceImplTest extends ParentIntegrationTest {
  
  @Autowired
  private StoricoPraticaService storicoPraticaService;
  
  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest());
  }
  
  @Test
  public void logEvent() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    CosmoLStoricoPratica cosmoLStoricoPratica = CosmoLStoricoPratica.builder()
        .withUtenteCorrente(true)
        .withPratica(pratica)
        .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_ANNULLATA)
        .build();
    storicoPraticaService.logEvent(cosmoLStoricoPratica);
  }
}