/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmopratiche.business.service.FruitoriService;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaRelazionePraticaResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheFruitoreResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RelazionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TemplateFirmaFea;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class FruitoriServiceImplTest extends ParentIntegrationTest {


  @Autowired
  private FruitoriService fruitoriService;

  @Override
  protected void autentica() {

    var client = ClientInfoDTO.builder().withCodice("FruitoreTest1")
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();

    autentica(null, client);
  }

  @Test
  public void creaAggiornaPraticheInRelazione() {
    OffsetDateTime dateTime = OffsetDateTime.now();
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDE_DA");
    relazioni.add(rel1);
    RelazionePratica rel2 = new RelazionePratica();
    rel2.setIdPraticaExtA("3");
    rel2.setTipoRelazione("DIPENDE_DA");
    relazioni.add(rel2);
    RelazionePratica rel3 = new RelazionePratica();
    rel3.setIdPraticaExtA("5");
    rel3.setTipoRelazione("DUPLICA");
    rel3.setDtFineValidita(dateTime);
    relazioni.add(rel3);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    var result = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertEquals(result.getRelazioniPratica().size(), 3);
    assertEquals(result.getRelazioniPratica().get(0).getRelazionePratica().getIdPraticaExtA(), "2");
    assertEquals(result.getRelazioniPratica().get(0).getEsito().getCode(), "OK");
    assertEquals(result.getRelazioniPratica().get(1).getRelazionePratica().getIdPraticaExtA(), "3");
    assertEquals(result.getRelazioniPratica().get(1).getEsito().getCode(), "ERRORE");
    assertEquals(result.getRelazioniPratica().get(2).getRelazionePratica().getIdPraticaExtA(), "5");
    assertEquals(result.getRelazioniPratica().get(2).getRelazionePratica().getDtFineValidita(),
        dateTime);
    assertEquals(result.getRelazioniPratica().get(2).getEsito().getCode(), "OK");
  }

  @Test
  public void creaAggiornaPraticheInRelazioneSoloErrore() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel2 = new RelazionePratica();
    rel2.setIdPraticaExtA("3");
    rel2.setTipoRelazione("DUPLICA");
    relazioni.add(rel2);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    var result = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertEquals(result.getRelazioniPratica().size(), 1);
    assertEquals(result.getRelazioniPratica().get(0).getRelazionePratica().getIdPraticaExtA(), "3");
    assertEquals(result.getRelazioniPratica().get(0).getEsito().getCode(), "ERRORE");
  }

  @Test
  public void creaAggiornaPraticheInRelazioneTuttiCorretti() {
    OffsetDateTime dateTime = OffsetDateTime.now();
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DUPLICA");
    rel1.setDtInizioValidita(dateTime);
    relazioni.add(rel1);
    RelazionePratica rel3 = new RelazionePratica();
    rel3.setIdPraticaExtA("5");
    rel3.setTipoRelazione("DIPENDE_DA");
    relazioni.add(rel3);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    var result = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertEquals(result.getRelazioniPratica().size(), 2);
    assertEquals(result.getRelazioniPratica().get(0).getRelazionePratica().getIdPraticaExtA(), "2");
    assertEquals(result.getRelazioniPratica().get(0).getRelazionePratica().getDtInizioValidita(),
        dateTime);
    assertEquals(result.getRelazioniPratica().get(0).getEsito().getCode(), "OK");
    assertEquals(result.getRelazioniPratica().get(1).getRelazionePratica().getIdPraticaExtA(), "5");
    assertEquals(result.getRelazioniPratica().get(1).getEsito().getCode(), "OK");
  }

  @Test(expected = BadRequestException.class)
  public void creaAggiornaPraticheInRelazioneErrorePraticaDa() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDE_DA");
    relazioni.add(rel1);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    fruitoriService.creaAggiornaPraticheInRelazione("3", request);
  }

  @Test(expected = BadRequestException.class)
  public void creaAggiornaPraticheInRelazioneErroreAssociazioneFruitoreEnte() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDE_DA");
    relazioni.add(rel1);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("cmto");
    request.setRelazioniPratica(relazioni);
    fruitoriService.creaAggiornaPraticheInRelazione("1", request);
  }
  
  @Test(expected = ConflictException.class)
  public void postPratichePraticaIsPresent() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setIdPratica("1");
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("prova");
    request.setOggetto("prova");
    fruitoriService.postPratiche(request);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheCosmoDPraticaNotFound() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setIdPratica("6");
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("prova");
    request.setOggetto("prova");
    fruitoriService.postPratiche(request);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheNonCreabileDaServizio() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setIdPratica("6");
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("TP6");
    request.setOggetto("prova");
    fruitoriService.postPratiche(request);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticheTipoPraticaNonAssociataAdEnte() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setIdPratica("6");
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("TP7");
    request.setOggetto("prova");
    fruitoriService.postPratiche(request);
  }
  
  @Test
  public void postPratiche() {
    CreaPraticaFruitoreRequest request = new CreaPraticaFruitoreRequest();
    request.setIdPratica("66");
    request.setCodiceIpaEnte("r_piemon");
    request.setCodiceTipologia("TP1");
    request.setOggetto("prova");
    request.setMetadati("prova");
    request.setRiassunto("prova");
    List<TemplateFirmaFea> templateFirmaFea = new ArrayList<>();
    TemplateFirmaFea firmaFea = new TemplateFirmaFea();
    firmaFea.setCodiceTipoDocumento("codice 1");
    firmaFea.setCoordinataX(1.0);
    firmaFea.setCoordinataY(1.0);
    firmaFea.setPagina(1L);
    firmaFea.setDescrizione("prova");
    templateFirmaFea.add(firmaFea);
    request.setTemplateFirmaFea(templateFirmaFea);
    CreaPraticaFruitoreResponse response = fruitoriService.postPratiche(request);
    assertNotNull(response);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaPraticheInRelazioneConRelazioniNull() {
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(null);
    fruitoriService.creaAggiornaPraticheInRelazione("1", request);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaPraticheInRelazioneConRelazioniVuote() {
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    List<RelazionePratica> relazioni = new ArrayList<>();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    fruitoriService.creaAggiornaPraticheInRelazione("1", request);
  }
  
  @Test
  public void creaAggiornaPraticheInRelazioneConRelazioneNotFound() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDE_DAL");
    relazioni.add(rel1);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    AggiornaRelazionePraticaResponse response = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertNotNull(response);
    assertNotNull(response.getRelazioniPratica());
    assertNotNull(response.getRelazioniPratica().get(0));
    assertTrue(response.getRelazioniPratica().get(0).getEsito().getCode().equals("ERRORE"));
    assertTrue(response.getRelazioniPratica().get(0).getEsito().getStatus().equals(400));
  }
  
  @Test
  public void creaAggiornaPraticheConDtFineValiditaRelazioneNull() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDENTE_DA");
    relazioni.add(rel1);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    AggiornaRelazionePraticaResponse response = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertNotNull(response);
    assertNotNull(response.getRelazioniPratica());
    assertNotNull(response.getRelazioniPratica().get(0));
    assertTrue(response.getRelazioniPratica().get(0).getEsito().getCode().equals("ERRORE"));
    assertTrue(response.getRelazioniPratica().get(0).getEsito().getStatus().equals(400));
  }
  
  @Test
  public void creaAggiornaPraticheConRelazionePraticaEsistente() {
    List<RelazionePratica> relazioni = new ArrayList<>();
    RelazionePratica rel1 = new RelazionePratica();
    rel1.setIdPraticaExtA("2");
    rel1.setTipoRelazione("DIPENDENTE_DA");
    rel1.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    rel1.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    relazioni.add(rel1);
    RelazionePratica rel2 = new RelazionePratica();
    rel2.setIdPraticaExtA("5");
    rel2.setTipoRelazione("DIPENDENTE_DA");
    rel2.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    rel2.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    relazioni.add(rel2);
    AggiornaRelazionePraticaRequest request = new AggiornaRelazionePraticaRequest();
    request.setCodiceIpaEnte("r_piemon");
    request.setRelazioniPratica(relazioni);
    AggiornaRelazionePraticaResponse response = fruitoriService.creaAggiornaPraticheInRelazione("1", request);
    assertNotNull(response);
    assertNotNull(response.getRelazioniPratica()); 
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheFilterNull() {
    fruitoriService.getPratiche(null);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheFilterEmpty() {
    fruitoriService.getPratiche("");
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCFUtenteEmpty() {
    String filter = "{\"codiceFiscaleUtente\":\"\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCFUtenteNotFound() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000G\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCodiceIpaEnteEmpty() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCodiceIpaEnteNotFound() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"prova\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCodiceTipoPraticaNotFound() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"r_piemon\", \"codiceTipoPratica\":\"prova\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheCodiceTagNotFound() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"r_piemon\", \"codiceTipoPratica\":\"TP1\", \"codiceTag\":\"prova\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheApiManagerIdFruitoreNotFound() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"r_piemon\", \"codiceTipoPratica\":\"TP1\", \"codiceTag\":\"DIR-ACQ\", \"apiManagerIdFruitore\":\"prova\", \"page\": 0, \"size\": 50}";
    fruitoriService.getPratiche(filter);
  }
  
  @Test
  public void getPraticheWithAllPresent() {
    String filter = "{\"codiceFiscaleUtente\":\"AAAAAA00B77B000F\", \"codiceIpaEnte\":\"r_piemon\", \"codiceTipoPratica\":\"TP1\", \"codiceTag\":\"DIR-ACQ\", \"apiManagerIdFruitore\":\"FruitoreTest1\", \"page\": 0, \"size\": 50}";
    PraticheFruitoreResponse response = fruitoriService.getPratiche(filter);
    assertNotNull(response);
  }
}
