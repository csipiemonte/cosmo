/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneEnteService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ConfigurazioneEnteServiceImplTest extends ParentIntegrationTest {

  private static final String CONFIGURAZIONE_NULLA = "Configurazione nulla";
  private static final String LA_CHIAVE_NON_CORRISPONDE = "La chiave non corrisponde";
  private static final String IL_VALORE_NON_CORRISPONDE = "Il valore non corrisponde";
  private static final String LA_DESCRIZIONE_NON_CORRISPONDE = "La descrizione non corrisponde";
  private static final String CHIAVE_1 = "chiave 1";
  private static final String CHIAVE_2 = "chiave 2";
  private static final String CHIAVE_111 = "chiave 111";
  @Autowired
  private ConfigurazioneEnteService configurazioneEnteService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());
  }

  @Test
  public void getConfigurazione() {
    ConfigurazioneEnte result = configurazioneEnteService.getConfigurazioneEnte(null, CHIAVE_1);
    assertNotNull(CONFIGURAZIONE_NULLA, result);
  }

  @Test
  public void getConfigurazioneConEnte() {
    ConfigurazioneEnte result =
        configurazioneEnteService.getConfigurazioneEnte(new BigDecimal(1L), CHIAVE_1);
    assertNotNull(CONFIGURAZIONE_NULLA, result);
  }

  @Test(expected = BadRequestException.class)
  public void getConfigurazioneConEnteSenzaConfigurazione() {
    configurazioneEnteService.getConfigurazioneEnte(new BigDecimal(2L), CHIAVE_1);
  }

  @Test(expected = BadRequestException.class)
  public void getConfigurazioneNonValida() {
    configurazioneEnteService.getConfigurazioneEnte(null, CHIAVE_2);
  }

  @Test
  public void getConfigurazioni() {
    List<ConfigurazioneEnte> result = configurazioneEnteService.getConfigurazioniEnte(null);

    assertNotNull("Deve esserci una lista di configurazioni", result);
    assertEquals("Deve esserci una configurazione valida", 2, result.size());
  }

  @Test
  public void createConfigurazioneNuova() {

    ConfigurazioneEnte configurazioneDaCreare = new ConfigurazioneEnte();

    configurazioneDaCreare.setChiave(CHIAVE_111);
    configurazioneDaCreare.setValore("valore 111");
    configurazioneDaCreare.setDescrizione("Test");

    ConfigurazioneEnte result =
        configurazioneEnteService.postConfigurazioneEnte(null, configurazioneDaCreare);

    assertNotNull(CONFIGURAZIONE_NULLA, result);
    assertTrue(LA_CHIAVE_NON_CORRISPONDE,
        configurazioneDaCreare.getChiave().equals(result.getChiave()));
    assertTrue(IL_VALORE_NON_CORRISPONDE,
        configurazioneDaCreare.getValore().equals(result.getValore()));
    assertTrue(LA_DESCRIZIONE_NON_CORRISPONDE,
        configurazioneDaCreare.getDescrizione().equals(result.getDescrizione()));
  }

  @Test(expected = BadRequestException.class)
  public void createConfigurazioneChiaveValida() {

    ConfigurazioneEnte configurazioneDaCreare = new ConfigurazioneEnte();

    configurazioneDaCreare.setChiave(CHIAVE_1);
    configurazioneDaCreare.setValore("valore 1");
    configurazioneDaCreare.setDescrizione("Test");

    configurazioneEnteService.postConfigurazioneEnte(null, configurazioneDaCreare);
  }

  @Test
  public void createConfigurazioneChiaveNonValida() {

    ConfigurazioneEnte configurazioneDaCreare = new ConfigurazioneEnte();

    configurazioneDaCreare.setChiave(CHIAVE_2);
    configurazioneDaCreare.setValore("valore 2 nuovo");
    configurazioneDaCreare.setDescrizione("Test");

    ConfigurazioneEnte result =
        configurazioneEnteService.postConfigurazioneEnte(null, configurazioneDaCreare);

    assertNotNull(CONFIGURAZIONE_NULLA, result);
    assertTrue(LA_CHIAVE_NON_CORRISPONDE,
        configurazioneDaCreare.getChiave().equals(result.getChiave()));
    assertTrue(IL_VALORE_NON_CORRISPONDE,
        configurazioneDaCreare.getValore().equals(result.getValore()));
    assertTrue(LA_DESCRIZIONE_NON_CORRISPONDE,
        configurazioneDaCreare.getDescrizione().equals(result.getDescrizione()));
  }

  @Test
  public void updateConfigurazioneEnteValida() {

    ConfigurazioneEnte configurazioneDaAggiornare = new ConfigurazioneEnte();
    configurazioneDaAggiornare.setChiave(CHIAVE_1);
    configurazioneDaAggiornare.setValore("valore 1 nuovo");
    configurazioneDaAggiornare.setDescrizione("Test");

    ConfigurazioneEnte result =
        configurazioneEnteService.putConfigurazioneEnte(null, CHIAVE_1, configurazioneDaAggiornare);

    assertNotNull(CONFIGURAZIONE_NULLA, result);
    assertTrue(LA_CHIAVE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getChiave().equals(result.getChiave()));
    assertTrue(IL_VALORE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getValore().equals(result.getValore()));
    assertTrue(LA_DESCRIZIONE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getDescrizione().equals(result.getDescrizione()));
  }

  @Test
  public void updateConfigurazioneEnteNonValida() {

    ConfigurazioneEnte configurazioneDaAggiornare = new ConfigurazioneEnte();
    configurazioneDaAggiornare.setChiave(CHIAVE_2);
    configurazioneDaAggiornare.setValore("valore 2 nuovo");
    configurazioneDaAggiornare.setDescrizione("Test");

    ConfigurazioneEnte result =
        configurazioneEnteService.putConfigurazioneEnte(null, CHIAVE_2, configurazioneDaAggiornare);

    assertNotNull(CONFIGURAZIONE_NULLA, result);
    assertTrue(LA_CHIAVE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getChiave().equals(result.getChiave()));
    assertTrue(IL_VALORE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getValore().equals(result.getValore()));
    assertTrue(LA_DESCRIZIONE_NON_CORRISPONDE,
        configurazioneDaAggiornare.getDescrizione().equals(result.getDescrizione()));
  }

  @Test(expected = BadRequestException.class)
  public void updateConfigurazioneEnteNonEsistente() {

    ConfigurazioneEnte configurazioneDaAggiornare = new ConfigurazioneEnte();
    configurazioneDaAggiornare.setChiave(CHIAVE_111);
    configurazioneDaAggiornare.setValore("valore 111 nuovo");
    configurazioneDaAggiornare.setDescrizione("Test");

    configurazioneEnteService.putConfigurazioneEnte(null, CHIAVE_111, configurazioneDaAggiornare);
  }

  @Test
  public void deleteConfigurazioneEnteValida() {
    configurazioneEnteService.deleteConfigurazioneEnte(null, CHIAVE_1);
  }

  @Test(expected = BadRequestException.class)
  public void deleteConfigurazioneEnteNonValida() {
    configurazioneEnteService.deleteConfigurazioneEnte(null, CHIAVE_2);
  }

  @Test(expected = BadRequestException.class)
  public void deleteConfigurazioneEnteNonEsistente() {
    configurazioneEnteService.deleteConfigurazioneEnte(null, CHIAVE_111);
  }
  
  @Test(expected = BadRequestException.class)
  public void getConfigurazioneEnteChiaveBlank() {
    configurazioneEnteService.getConfigurazioneEnte(null, "");
  }
  
  @Test(expected = BadRequestException.class)
  public void postConfigurazioneEnteChiaveConfigurazioneNonValida() {
    ConfigurazioneEnte configurazioneDaCreare = new ConfigurazioneEnte();

    configurazioneDaCreare.setChiave("profilo.utente.default");
    configurazioneDaCreare.setValore("valore 1");
    configurazioneDaCreare.setDescrizione("Test");
    
    configurazioneEnteService.postConfigurazioneEnte(new BigDecimal(1), configurazioneDaCreare);
  }
  
  @Test(expected = BadRequestException.class)
  public void putConfigurazioneEnteChiaveBlank() {
    configurazioneEnteService.putConfigurazioneEnte(null, "", null);
  }
  
  @Test(expected = BadRequestException.class)
  public void putConfigurazioneEnteChiaveNonValida() {
    configurazioneEnteService.putConfigurazioneEnte(null, "profilo.utente.default", null);
  }
}
