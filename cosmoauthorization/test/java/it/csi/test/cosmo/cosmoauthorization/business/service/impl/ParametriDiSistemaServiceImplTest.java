/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.ParametriDiSistemaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaParametroDiSistemaRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistema;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametroDiSistemaResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ParametriDiSistemaServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ParametriDiSistemaService parametriDiSistemaService;

  private static final String PARAMETRO_DI_SISTEMA_NON_NULLO = "parametro di sistema non nullo";
  private static final String VALORE = "10000";
  private static final String DESCRIZIONE = "Grandezza massima del file caricato per il logo";
  private static final String NEW_CHIAVE = "sono.una.nuova.chiave";
  private static final String CHIAVE_DELETE = "max.page.size";
  private static final String CHIAVE = "user.pref.version";

  @Test
  public void getParamtriDiSistema() {
    String filter =
        "{\"filter\":{\"fullText\": {\"ci\":\"max.page.size\"}},\"page\": 0,\"size\":10}";
    ParametroDiSistemaResponse parametroDiSistemaResponse =
        parametriDiSistemaService.getParamtriDiSistema(filter);

    assertFalse(PARAMETRO_DI_SISTEMA_NON_NULLO,
        CollectionUtils.isEmpty(parametroDiSistemaResponse.getParametriDiSistema()));
    assertTrue("il parametro di sistema deve essere unico, con codice max.page.size",
        parametroDiSistemaResponse.getParametriDiSistema().size() == 1 && parametroDiSistemaResponse
            .getParametriDiSistema().get(0).getChiave().equals(CHIAVE_DELETE));
  }

  @Test
  public void deleteParametroDiSistema() {

    ParametroDiSistema parametroDiSistema =
        parametriDiSistemaService.deleteParametroDiSistemaByChiave(CHIAVE_DELETE);
    assertNotNull(PARAMETRO_DI_SISTEMA_NON_NULLO, parametroDiSistema);
  }

  @Test
  public void getParametroDiSistemaByChiave() {
    ParametroDiSistema parametroDiSistema =
        parametriDiSistemaService.getParamtroDiSistemaByChiave("ente.pref.maxsize");

    assertNotNull(PARAMETRO_DI_SISTEMA_NON_NULLO, parametroDiSistema);
    assertTrue("il valore deve essere 10000", VALORE.equals(parametroDiSistema.getValore()));
    assertTrue("Grandezza massima del file caricato per il logo",
        DESCRIZIONE.equals(parametroDiSistema.getDescrizione()));
  }

  @Test
  public void postParametroDiSistema() {

    CreaParametroDiSistemaRequest parametroDiSistemaDaCreare = new CreaParametroDiSistemaRequest();

    parametroDiSistemaDaCreare.setChiave(NEW_CHIAVE);
    parametroDiSistemaDaCreare.setValore("JUnitSuperTest");
    parametroDiSistemaDaCreare.setDescrizione("Sono una nuova descrizione");

    ParametroDiSistema parametroDiSistemaCreato =
        parametriDiSistemaService.postParametroDiSistema(parametroDiSistemaDaCreare);

    assertNotNull("Deve esserci un parametro di sistema", parametroDiSistemaCreato);
    assertEquals(NEW_CHIAVE, parametroDiSistemaCreato.getChiave());

  }

  @Test
  public void putParametroDiSistema() {

    AggiornaParametroDiSistemaRequest psAgg = new AggiornaParametroDiSistemaRequest();

    psAgg.setChiave(CHIAVE);
    psAgg.setValore("3erre3");
    psAgg.setDescrizione("Nuova Descrizione");

    ParametroDiSistema psAggiornato =
        parametriDiSistemaService.putParametroDiSistemaByChiave(CHIAVE, psAgg);
    assertNotNull("Deve esserci un parametro di sistema", psAggiornato);
    assertEquals(CHIAVE, psAggiornato.getChiave());
    assertEquals("3erre3", psAggiornato.getValore());
    assertEquals("Nuova Descrizione", psAggiornato.getDescrizione());

  }

  @Test(expected = NotFoundException.class)
  public void deleteParametroDiSistemaByChiaveNotFound() {
    parametriDiSistemaService.deleteParametroDiSistemaByChiave(VALORE);
  }

  @Test(expected = NotFoundException.class)
  public void getParametroDiSistemaByChiaveNotFound() {
    parametriDiSistemaService.getParamtroDiSistemaByChiave(VALORE);
  }

  @Test(expected = NotFoundException.class)
  public void putParametroDiSistemaByChiave() {
    AggiornaParametroDiSistemaRequest request = new AggiornaParametroDiSistemaRequest();
    request.setChiave(CHIAVE);
    request.setValore(VALORE);
    parametriDiSistemaService.putParametroDiSistemaByChiave(VALORE, request);
  }

  @Test(expected = ConflictException.class)
  public void postParametroDiSistemaConNomeEsistente() {
    CreaParametroDiSistemaRequest request = new CreaParametroDiSistemaRequest();
    request.setChiave(CHIAVE_DELETE);
    request.setValore(VALORE);
    parametriDiSistemaService.postParametroDiSistema(request);
  }

  @Test
  public void putParametroDiSistemaByChiaveConDescrizioneBlank() {
    AggiornaParametroDiSistemaRequest request = new AggiornaParametroDiSistemaRequest();
    request.setChiave(CHIAVE);
    request.setValore(VALORE);
    request.setDescrizione("");
    ParametroDiSistema parametro = parametriDiSistemaService.putParametroDiSistemaByChiave(CHIAVE, request);
    assertNotNull(parametro);
    assertNull(parametro.getDescrizione());
  }

}
