/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmopratiche.business.service.PracticeService;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class RicercaPraticheConVariabiliProcessoServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private PracticeService service;


  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  protected String parse(String raw) {
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.replace("'", "\"");
  }

  @Test()
  @Ignore("VariabileString non funziona con h2: function encode usata per cercare stringhe nella colonna butearray_value "
      + "non e' definita, attivare il test usando CosmoPraticheUnitTestDB al posto di CosmoPraticheUnitTestInMemory")
  public void ricercaPraticaFiltroVariabiliProcessoSingoloStringFilter() {
    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'oggetto',"
        + "'singolo':{'variabileString':{'c': 'Test'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 3);
  }


  @Test()
  @Ignore("VariabileString non funziona con h2: function encode usata per cercare stringhe nella colonna butearray_value "
      + "non e' definita, attivare il test usando CosmoPraticheUnitTestDB al posto di CosmoPraticheUnitTestInMemory")
  public void ricercaPraticaFiltroVariabiliProcessoSingoloStringFilterForByteArrayValue() {
    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'oggetto',"
        + "'singolo':{'variabileString':{'eq': 'Test3'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);
  }



  @Test()
  public void ricercaPraticaFiltroVariabiliProcessoSingoloDoubleFilter() {
    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'numerovariabile',"
        + "'singolo':{'variabileNumerica':{'gte': '2.0'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);
  }


  @Test()
  public void ricercaPraticaFiltroVariabiliProcessoSingoloDateFilter() {
    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'testdata',"
        + "'singolo':{'variabileData':{'gte': '2023-03-09'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);
  }


  @Test()
  public void ricercaPraticaFiltroVariabiliProcessoRangeDateFilter() {
    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'testdata',"
        + "'rangeDa':{'variabileData':{'gte': '2023-03-09'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);

    filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'testdata',"
        + "'rangeDa':{'variabileData':{'gt': '2023-03-09'}},'rangeA':{'variabileData':{'lte': '2023-03-12'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");

    praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().isEmpty());


    filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'testdata',"
        + "'rangeDa':{'variabileData':{'gte': '2023-03-01'}},'rangeA':{'variabileData':{'lte': '2023-03-12'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");

    praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);


  }


  @Test()
  public void ricercaPraticaFiltroVariabiliProcessoRangeDoubleFilter() {


    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'numerovariabile',"
        + "'rangeDa':{'variabileNumerica':{'gte': '1.5'}},'rangeA':{'variabileNumerica':{'lte': '3.0'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");

    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);


    filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'numerovariabile',"
        + "'rangeDa':{'variabileNumerica':{'gte': '1.99'}},'rangeA':{'variabileNumerica':{'lt': '2.0'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");

    praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().isEmpty());

  }


  @Test(expected = BadRequestException.class)
  public void ricercaPraticaFiltroVariabiliProcessoTestException() {


    String filter = parse("{'filter':{'tipologia':{'eq':'TP1'},"
        + "'variabiliProcesso': [{'nomeVariabile': 'testdata',"
        + "'rangeDa':{'variabileData':{'gte': '2023-03-01'}},'rangeA':{'variabileData':{'lte': '2023-02-12'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");

    service.getPratiche(filter, null);


  }

  @Test()
  @Ignore("VariabileString non funziona con h2: function encode usata per cercare stringhe nella colonna butearray_value "
      + "non e' definita, attivare il test usando CosmoPraticheUnitTestDB al posto di CosmoPraticheUnitTestInMemory")
  public void ricercaPraticaFiltroVariabiliProcessoSingoloJsonString() {
    String filter = parse("{'filter':{'tipologia':{'eq':'testMarco'}, 'groups':{'daLavorare' : {'inCorso' : true}}, "
        + "'variabiliProcesso': [{'nomeVariabile': 'luoghi_aulici', 'alberaturaJson': 'descrizione',"
        + "'singolo':{'variabileString':{'eq': 'Piazza Carignano'}}}],'tuttePratiche':true},'page':0,'size':10,'sort':'dataCreazionePratica DESC'}");
    PraticheResponse praticheResponse = service.getPratiche(filter, null);
    assertNotNull(praticheResponse);
    assertNotNull(praticheResponse.getPratiche());
    assertTrue(praticheResponse.getPratiche().size() == 1);
  }
}
