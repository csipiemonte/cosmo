/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmopratiche.business.service.PracticeService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.security.SecurityUtils;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmopratiche.testbed.providers.TestDataProvider;
import it.csi.test.cosmo.cosmopratiche.testbed.providers.TestDataProvider.TestDataEnvironment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class RicercaPraticheServiceImplTest extends ParentIntegrationTest {

  private static final long BASE_ID = 100L;

  private static final long DEFAULT_NUMBER = 10;

  @Autowired
  private PracticeService service;

  @Autowired
  private CosmoTPraticaRepository repository;

  @Autowired
  private TestDataProvider testDataProvider;

  private TestDataEnvironment testData;

  private List<CosmoTPratica> pratiche;

  @Override
  @Before
  public void autentica() {
    testData = testDataProvider.givenTestData();

    this.autentica(testData.getPrincipalAmministratore());
    populatePratiche();
  }

  protected void populatePratiche() {
    this.pratiche = new ArrayList<>();
    CosmoDTipoPratica tipoPratica = testData.getTipiPratica().get(0);

    for (long i = 1L; i <= DEFAULT_NUMBER; i++) {
      final var thisCnt = i;
      this.pratiche.add(testDataProvider.givenPratica(p -> {
        p.setId(BASE_ID + thisCnt);
        p.setEnte(testData.getEnte());
        p.setTipo(tipoPratica);
        p.setStato(tipoPratica.getCosmoRStatoTipoPraticas().get(0).getCosmoDStatoPratica());
        p.setUtenteCreazionePratica(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      }));
    }

    this.repository.flush();
  }

  protected String parse(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.replace("'", "\"");
  }

  @Test
  public void shouldFilterById() {
    assertTrue(!this.pratiche.isEmpty());

    PraticheResponse result1 =
        service.getPratiche(parse("{'filter':{'id':{'defined': true}}}"), false);
    assertTrue(result1.getPageInfo().getTotalElements() > 1);

    PraticheResponse result2 =
        service.getPratiche(parse("{'filter':{'id':{'defined': false}}}"), false);
    assertEquals(0, result2.getPageInfo().getTotalElements().intValue());

    this.pratiche.forEach(p -> {
      PraticheResponse result =
          service.getPratiche(parse("{'filter':{'id':{'eq': " + p.getId() + "}}}"), false);
      assertEquals(1, result.getPageInfo().getTotalElements().intValue());
      assertEquals(p.getId().longValue(), result.getPratiche().get(0).getId().longValue());

      result =
          service.getPratiche(parse("{'filter':{'id':{'ne': " + p.getId() + "}}, 'size': 50}"),
              null);

      assertSame(1, result.getPageInfo().getTotalPages());
      assertTrue(result.getPageInfo().getTotalElements() > 1);
      assertTrue(result.getPratiche().stream()
          .noneMatch(pr -> pr.getId().longValue() == p.getId().longValue()));

      result =
          service.getPratiche(parse("{'filter':{'id':{'in': [-1, -2, " + p.getId() + "]}}}"),
              false);
      assertEquals(1, result.getPageInfo().getTotalElements().intValue());
      assertEquals(p.getId().longValue(), result.getPratiche().get(0).getId().longValue());

      result = service.getPratiche(
          parse("{'filter':{'id':{'nin': [-1, -2, " + p.getId() + "]}}, 'size': 50}"), false);

      assertSame(1, result.getPageInfo().getTotalPages());
      assertTrue(result.getPageInfo().getTotalElements() > 1);
      assertTrue(result.getPratiche().stream()
          .noneMatch(pr -> pr.getId().longValue() == p.getId().longValue()));
    });

  }

  @Test
  public void testGetPracticesOKAllPresent() {
    PraticheResponse p = service.getPratiche("{}", false);
    assertNotNull(p);
    assertNotNull(p.getPageInfo());
    assertNotNull(p.getPageInfo().getTotalPages());
    assertNotNull(p.getPageInfo().getPageSize());
    assertNotNull(p.getPageInfo().getPage());
    assertNotNull(p.getPageInfo().getTotalElements());
    assertNotNull(p.getPratiche());

    assertEquals(DEFAULT_NUMBER, p.getPageInfo().getTotalElements().longValue());
    assertEquals(Integer.valueOf(0), p.getPageInfo().getPage());
  }

  @Test
  public void getPratiche() {
    String filter = parse(
        "{'filter':{'groups':{  'daLavorare':{'inCorso': true}}},'page': 0,'size':10, 'sort': 'dataCreazionePratica DESC'}");
    service.getPratiche(filter, false);
  }

  @Test()
  public void ricercaPraticaFiltroOggetto() {
    String filter = parse(
        "{'filter':{'oggetto':{'ci':'pratica'}, 'groups':{'tutte':{'inCorso': true}}}, 'page': 0,'size':10}");
    PraticheResponse result = service.getPratiche(filter, false);
    assertNotNull(result);
  }

  @Test()
  public void ricercaPraticaFiltroOggetti() {
    String filter = parse(
        "{'filter':{'oggetto':{'ci':'pratica parte'}, 'groups':{'tutte':{'inCorso': true}}}, 'page': 0,'size':10}");
    PraticheResponse result = service.getPratiche(filter, false);
    assertNotNull(result);
    assertTrue(result.getPageInfo().getPageSize() > 1);
  }

  @Test()
  public void ricercaPraticaFiltroTipologia() {
    String filter = parse(
        "{'filter':{'tipologia':{'eq':'1'}, 'groups':{'tutte':{'inCorso': true}}}, 'page': 0,'size':10}");
    PraticheResponse result = service.getPratiche(filter, null);
    assertNotNull(result);
    assertNotNull(result.getPratiche());
    assertTrue(result.getPageInfo().getPageSize() > 1);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticheOggettoNonAlfanumerico() {
    String filter = parse("{'filter':{'oggetto':{'ci':'pratic@'}},'page': 0,'size':0}");
    service.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticheOggettoDimensioneMassimaSuperata() {
    int length = Constants.PRATICHE.MAX_LENGTH_OGGETTO + 1;
    StringBuilder outputBuffer = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      outputBuffer.append("c");
    }
    String filter =
        parse("{'filter':{'oggetto':{'ci':'" + outputBuffer.toString() + "'}},'page': 0,'size':0}");
    service.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataAperturaPratica() {
    String filter = parse(
        "{'filter':{'dataAperturaPraticaDa':{'gte':'2020-12-11'}, 'dataAperturaPraticaA':{'lte': '2020-11-11'}},'page': 0,'size':0}");
    service.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataUltimaModifica() {
    String filter = parse(
        "{'filter':{'dataUltimaModificaDa':{'gte':'2020-12-11'}, 'dataUltimaModificaA':{'lte': '2020-11-11'}},'page': 0,'size':0}");
    service.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataUltimoCambioDiStato() {
    String filter = parse(
        "{'filter':{'dataUltimoCambioStatoDa':{'gte':'2020-12-11'}, 'dataUltimoCambioStatoA':{'lte': '2020-11-11'}},'page': 0,'size':0}");
    service.getPratiche(filter, null);
  }
}
