/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.UtentiService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoPraticheUnitTestInMemory.class } )
@Transactional
public class UtentiServiceImplTest extends ParentIntegrationTest {
  
  @Autowired
  private UtentiService utentiService;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test(expected = NotFoundException.class)
  public void putUtentiPratichePreferiteIdPraticaNotFound() {
    utentiService.putUtentiPratichePreferiteIdPratica("64");
  }
  
  @Test
  public void putUtentiPratichePreferiteIdPraticaListaVuota() {
    Pratica pratica = utentiService.putUtentiPratichePreferiteIdPratica("2");
    assertNotNull(pratica);
    assertEquals(pratica.isPreferita(), Boolean.FALSE);
  }
  
  @Test
  public void putUtentiPratichePreferiteIdPratica() {
    Pratica pratica = utentiService.putUtentiPratichePreferiteIdPratica("1");
    assertNotNull(pratica);
    assertEquals(pratica.isPreferita(), Boolean.TRUE);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteUtentiPratichePreferiteIdPraticaNotFound() {
    utentiService.deleteUtentiPratichePreferiteIdPratica("64");
  }
  
  @Test
  public void deleteUtentiPratichePreferiteIdPraticaListaVuota() {
    utentiService.deleteUtentiPratichePreferiteIdPratica("2");
  }
  
  @Test
  public void deleteUtentiPratichePreferiteIdPratica() {
    utentiService.deleteUtentiPratichePreferiteIdPratica("1");
  }
}
