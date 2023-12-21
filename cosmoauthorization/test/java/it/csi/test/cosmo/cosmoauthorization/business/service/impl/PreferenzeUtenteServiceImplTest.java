/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.PreferenzeUtenteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class PreferenzeUtenteServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private PreferenzeUtenteService preferenzeUtenteService;

  private String valore = "{\"maxPageSize\" : 20}";

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getPreferenzeUtente() {

    Preferenza preferenzeUtente = preferenzeUtenteService.getPreferenzeUtente();
    assertNotNull(preferenzeUtente);
  }

  @Test
  public void getPreferenzeUtenteId() {

    Preferenza preferenzeUtente = preferenzeUtenteService.getPreferenzeUtente("1");
    assertNotNull(preferenzeUtente);
  }

  @Test
  public void createPreferenzeUtente() {

    Preferenza preferenzeUtenteDaCreare = new Preferenza();
    preferenzeUtenteDaCreare.setValore(valore);
    preferenzeUtenteDaCreare.setVersione("2.0");

    Preferenza preferenzeUtenteCreate =
        preferenzeUtenteService.createPreferenzeUtente(preferenzeUtenteDaCreare);

    assertNotNull(preferenzeUtenteCreate);
  }

  @Test
  public void createPreferenzeUtenteConVersioneGiaEsistente() {

    try {
      Preferenza preferenzeUtenteDaCreare = new Preferenza();
      preferenzeUtenteDaCreare.setValore(valore);
      preferenzeUtenteDaCreare.setVersione("1.0");

      preferenzeUtenteService.createPreferenzeUtente(preferenzeUtenteDaCreare);

      fail("preferenzeUtenteCreate non deve esistere");
    } catch (ManagedException e) {
      assertTrue(e.getMessage().equals("Preferenze con versione '1.0' gia' esistenti"));
    }

  }

  @Test
  public void updatePreferenzeUtente() {
    Preferenza preferenzeUtenteDaAggiornare = new Preferenza();
    preferenzeUtenteDaAggiornare.setValore(valore);
    preferenzeUtenteDaAggiornare.setVersione("1.0");

    Preferenza preferenzeUtenteAggiornate =
        preferenzeUtenteService.updatePreferenzeUtente(preferenzeUtenteDaAggiornare);

    assertNotNull(preferenzeUtenteAggiornate);
  }

  @Test(expected = NotFoundException.class)
  public void updatePreferenzeUtenteConVersioneNonEsistente() {
    Preferenza preferenzeUtenteDaAggiornare = new Preferenza();
    preferenzeUtenteDaAggiornare.setValore(valore);
    preferenzeUtenteDaAggiornare.setVersione("2.0");

    preferenzeUtenteService.updatePreferenzeUtente(preferenzeUtenteDaAggiornare);
  }
  
  @Test(expected = NotFoundException.class)
  public void getPreferenzeUtenteNotFound() {
    preferenzeUtenteService.getPreferenzeUtente("111");
  }
  
  @Test
  public void getPreferenzaNotFound() {
    Preferenza preferenza = preferenzeUtenteService.getPreferenzeUtente("2");
    assertNull(preferenza);
  }
}
