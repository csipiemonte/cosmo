/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaIstanzaFunzionalitaFormLogicoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzaFunzionalitaFormLogico;
import it.csi.cosmo.cosmobusiness.dto.rest.IstanzeFormLogiciResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.TipologiaFunzionalitaFormLogico;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional()
public class IstanzaFormLogiciServiceImplTest extends ParentIntegrationTest {

  private static final String CODICE_COMMENTI = "COMMENTI";
  private static final String CHECK_CODICE = "Il codice deve essere '%s'";
  private static final String DESCRIZIONE = "Descrizione";
  private static final String CHECK_DESCRIZIONE = "La descrizione deve essere '%s'";
  private static final String ISTANZA_NOT_NULL = "L'istanza deve essere valorizzata";

  @Autowired
  private IstanzaFormLogiciService istanzaFormLogiciService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void deleteIstanza() {
    istanzaFormLogiciService.deleteFormLogiciIstanzeId(1L);
  }

  @Test
  public void getIstanzePerCodiceOrderByCodice() {
    String filter =
        "{\"filter\":{\"codice\":{\"ci\":\"doc\"}},\"sort\":\"+descrizione\"}";
    IstanzeFormLogiciResponse result = istanzaFormLogiciService.getFormLogiciIstanze(filter);
    assertNotNull(ISTANZA_NOT_NULL, result);
    assertTrue("Ci sono in totale tre elementi", result.getPageInfo().getTotalElements() == 3);
    assertTrue("Sono ordinati alfabeticamente",
        result.getIstanze().get(0).getDescrizione()
        .compareTo(result.getIstanze().get(1).getDescrizione()) == 0);

    filter = "{\"filter\":{\"codice\":{\"ci\":\"doc\"}},\"sort\":\"descrizione DESC\"}";
    result = istanzaFormLogiciService.getFormLogiciIstanze(filter);
    assertNotNull(ISTANZA_NOT_NULL, result);
    assertTrue("Ci sono in totale tre elementi", result.getPageInfo().getTotalElements() == 3);
    assertTrue("Sono ordinati alfabeticamente",
        result.getIstanze().get(0).getDescrizione()
        .compareTo(result.getIstanze().get(1).getDescrizione()) > 0);
  }

  @Test
  public void getIstanzePerDescrizioneConSoloCodiceComeCampo() {
    String filter = "{\"filter\":{\"descrizione\":{\"ci\":\"tab\"}},\"fields\":\"codice\"}";
    IstanzeFormLogiciResponse result = istanzaFormLogiciService.getFormLogiciIstanze(filter);
    assertNotNull(ISTANZA_NOT_NULL, result);
    assertTrue("Ci sono in totale cinque elementi", result.getPageInfo().getTotalElements() == 5);
    assertNull(result.getIstanze().get(0).getId());
    assertNull(result.getIstanze().get(0).getDescrizione());
  }

  @Test
  public void getIstanza() {
    IstanzaFunzionalitaFormLogico result = istanzaFormLogiciService.getFormLogiciIstanzeId(1L);
    assertNotNull(ISTANZA_NOT_NULL, result);
    assertNotNull("Deve esserci il codice", result.getCodice());
    assertNotNull("Deve esserci la descrizione", result.getDescrizione());
    assertTrue(String.format(CHECK_CODICE, CODICE_COMMENTI),
        CODICE_COMMENTI.equals(result.getCodice()));
  }

  @Test
  public void salvaIstanza() {
    CreaIstanzaFunzionalitaFormLogicoRequest req = new CreaIstanzaFunzionalitaFormLogicoRequest();
    req.setCodice(CODICE_COMMENTI);
    req.setDescrizione(DESCRIZIONE);
    IstanzaFunzionalitaFormLogico funz = istanzaFormLogiciService.postFormLogiciIstanze(req);

    assertNotNull(ISTANZA_NOT_NULL, funz);
    assertTrue(String.format(CHECK_CODICE, CODICE_COMMENTI),
        CODICE_COMMENTI.equals(funz.getCodice()));
    assertTrue(String.format(CHECK_DESCRIZIONE, DESCRIZIONE),
        DESCRIZIONE.equals(funz.getDescrizione()));

  }

  @Test(expected = NotFoundException.class)
  public void salvaIstanzaConCodiceErrato() {
    CreaIstanzaFunzionalitaFormLogicoRequest funz = new CreaIstanzaFunzionalitaFormLogicoRequest();
    funz.setCodice("COMMENTO_ASDASDASD");
    funz.setDescrizione(DESCRIZIONE);
    istanzaFormLogiciService.postFormLogiciIstanze(funz);
  }

  @Test
  public void aggiornaIstanza() {

    AggiornaIstanzaFunzionalitaFormLogicoRequest req =
        new AggiornaIstanzaFunzionalitaFormLogicoRequest();
    req.setCodice(CODICE_COMMENTI);
    req.setDescrizione(DESCRIZIONE);
    IstanzaFunzionalitaFormLogico funz = istanzaFormLogiciService.putFormLogiciIstanzeId(1L, req);

    assertNotNull(ISTANZA_NOT_NULL, funz);
    assertTrue(String.format(CHECK_CODICE, CODICE_COMMENTI),
        CODICE_COMMENTI.equals(funz.getCodice()));
    assertTrue(String.format(CHECK_DESCRIZIONE, DESCRIZIONE),
        DESCRIZIONE.equals(funz.getDescrizione()));
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaIstanzaConCodiceErrato() {
    AggiornaIstanzaFunzionalitaFormLogicoRequest funz =
        new AggiornaIstanzaFunzionalitaFormLogicoRequest();
    funz.setCodice("COMMENTO_ASDASDASD");
    funz.setDescrizione(DESCRIZIONE);
    istanzaFormLogiciService.putFormLogiciIstanzeId(1L, funz);
  }

  @Test
  public void getAllTipologie() {
    List<TipologiaFunzionalitaFormLogico> response = istanzaFormLogiciService.getAllTipologie();
    assertNotNull(ISTANZA_NOT_NULL, response);
    assertTrue("Devono esserci 7 elementi", response.size() == 7);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteFormLogiciIstanzeIdNotFound() {
    istanzaFormLogiciService.deleteFormLogiciIstanzeId(111L);
  }
  
  @Test
  public void deleteFormLogiciIstanzeId() {
    istanzaFormLogiciService.deleteFormLogiciIstanzeId(1L);
  }
}
