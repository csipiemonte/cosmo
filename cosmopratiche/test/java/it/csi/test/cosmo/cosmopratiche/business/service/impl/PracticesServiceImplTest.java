/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientNotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.PracticeService;
import it.csi.cosmo.cosmopratiche.config.Constants;
import it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, PracticesServiceImplTest.PracticesServiceIntegrationTConfig.class})
@Transactional
public class PracticesServiceImplTest extends ParentIntegrationTest {

  @Configuration
  public static class PracticesServiceIntegrationTConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private PracticeService s;

  @Autowired
  CosmoTPraticaRepository cosmoPracticesRepository;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  @Test
  public void testGetPracticesOKAllPresent() {
    PraticheResponse p = s.getPratiche("{\"id\":{\"gt\":\"2\"}}", false);
    assertNotNull(p);
    assertNotNull(p.getPageInfo().getTotalPages());
    assertNotNull(p.getPageInfo().getPageSize());
    assertNotNull(p.getPageInfo().getPage());
    assertNotNull(p.getPageInfo().getTotalElements());
    assertNotNull(p.getPratiche());
  }

  @Test()
  public void getTipiPratica() {
    List<TipoPratica> tipiPratica = s.getTipiPratica(null);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }

  @Test()
  public void getTipiPraticaEnteFisso() {
    List<TipoPratica> tipiPratica = s.getTipiPratica(1);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticheOggettoNonAlfanumerico() {
    String filter = "{\"filter\":{\"oggetto\":{\"ci\":\"pratic@\"}},\"page\": 0,\"size\":0}";
    s.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticheOggettoDimensioneMassimaSuperata() {
    int length = Constants.PRATICHE.MAX_LENGTH_OGGETTO + 1;
    StringBuilder outputBuffer = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      outputBuffer.append("c");
    }
    String filter = "{\"filter\":{\"oggetto\":{\"ci\":\"" + outputBuffer.toString() + "\"}},\"page\": 0,\"size\":0}";
    s.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataAperturaPratica() {
    String filter =
        "{\"filter\":{\"dataAperturaPraticaDa\":{\"gte\":\"2020-12-11\"}, \"dataAperturaPraticaA\":{\"lte\": \"2020-11-11\"}},\"page\": 0,\"size\":0}";
    s.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataUltimaModifica() {
    String filter =
        "{\"filter\":{\"dataUltimaModificaDa\":{\"gte\":\"2020-12-11\"}, \"dataUltimaModificaA\":{\"lte\": \"2020-11-11\"}},\"page\": 0,\"size\":0}";
    s.getPratiche(filter, null);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaPraticaErroreTemporaleDataUltimoCambioDiStato() {
    String filter =
        "{\"filter\":{\"dataUltimoCambioStatoDa\":{\"gte\":\"2020-12-11\"}, \"dataUltimoCambioStatoA\":{\"lte\": \"2020-11-11\"}},\"page\": 0,\"size\":0}";
    s.getPratiche(filter, null);
  }

  @Test()
  public void ricercaPraticaFiltroOggetto() {
    String filter =
        "{\"filter\":{\"oggetto\":{\"ci\":\"pratica\"}, \"groups\":{\"tutte\":{\"inCorso\": true}}}, \"page\": 0,\"size\":10}";
    PraticheResponse pratiche = s.getPratiche(filter, null);
    assertNotNull(pratiche);
  }

  @Test()
  public void ricercaPraticaFiltroOggetti() {
    String filter =
        "{\"filter\":{\"oggetto\":{\"ci\":\"pratica parte\"}, \"groups\":{\"tutte\":{\"inCorso\": true}}}, \"page\": 0,\"size\":10}";
    PraticheResponse pratiche = s.getPratiche(filter, false);
    assertNotNull(pratiche);
    assertTrue(pratiche.getPageInfo().getPageSize() > 1);
  }

  @Test()
  public void ricercaPraticaFiltroTipologia() {
    String filter =
        "{\"filter\":{\"tipologia\":{\"eq\":\"1\"}, \"groups\":{\"tutte\":{\"inCorso\": true}}}, \"page\": 0,\"size\":10}";
    PraticheResponse pratiche = s.getPratiche(filter, null);
    assertNotNull(pratiche);
    assertNotNull(pratiche.getPratiche());
    assertTrue(pratiche.getPageInfo().getPageSize() > 1);
  }

  @Test
  public void getPratiche() {
    String filter =
        "{\"filter\":{\"groups\":{  \"daLavorare\":{\"inCorso\": true}}},\"page\": 0,\"size\":10, \"sort\": \"dataCreazionePratica DESC\"}";
    s.getPratiche(filter, false);
  }

  @Test
  public void ricercaPraticheNonCancellateConLinkNull() {
    PraticheNoLinkResponse response = s.getPraticheNoLink();
    assertNotNull(response);
    assertTrue(response.getPratiche().size() > 1);
    assertTrue(response.getPratiche().get(0).getLinkPratica() == null);
  }

  @Test(expected = BadRequestException.class)
  public void postPracticesTipologiaNotFound() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP52");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("1");
    s.postPractices(body);
  }

  @Test(expected = BadRequestException.class)
  public void postPracticesTipologiaNonCreabileDaInterfaccia() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP8");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("1");
    s.postPractices(body);
  }

  @Test(expected = BadRequestException.class)
  public void postPracticesTipologiaConEnteNotFound() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP1");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("1");
    s.postPractices(body);
  }

  @Test(expected = BadRequestException.class)
  public void postPracticesTipologiaConEnteDiverso() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP1");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("cmto");
    s.postPractices(body);
  }

  @Test
  public void postPracticesConRiassuntoECodiceFruitoreBlank() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP1");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("r_piemon");
    body.setRiassunto("");
    body.setCodiceFruitore("");
    Pratica pratica = s.postPractices(body);
    assertNotNull(pratica);
    assertEquals(pratica.getCodiceIpaEnte(), body.getCodiceIpaEnte());
  }

  @Test(expected = NotFoundException.class)
  public void postPracticesConFruitoreNotFound() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP1");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("r_piemon");
    body.setRiassunto("prova");
    body.setCodiceFruitore("FruitoreTest123");
    s.postPractices(body);
  }

  @Test
  public void postPracticesConFruitore() {
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setCodiceTipologia("TP1");
    body.setUtenteCreazionePratica("1");
    body.setOggetto("prova");
    body.setCodiceIpaEnte("r_piemon");
    body.setRiassunto("prova");
    body.setCodiceFruitore("FruitoreTest1");
    Pratica pratica = s.postPractices(body);
    assertNotNull(pratica);
    assertEquals(pratica.getCodiceIpaEnte(), body.getCodiceIpaEnte());
    assertEquals(pratica.getFruitore().getApiManagerId(), body.getCodiceFruitore());
  }

  @Test(expected = NotFoundException.class)
  public void deletePracticesIdNotFound() {
    s.deletePracticesId("123");
  }

  @Test
  public void deletePracticesId() {
    s.deletePracticesId("1");
  }

  @Test
  public void getPracticesIdAnnullataFalse() {
    s.getPracticesId("1", Boolean.FALSE);
  }

  @Test(expected = NotFoundException.class)
  public void getPracticesIdNofFound() {
    s.getPracticesId("123", Boolean.TRUE);
  }

  @Test(expected = NotFoundException.class)
  public void putPracticesId() {
    Pratica body = new Pratica();
    s.putPracticesId("123", body , true);
  }

  @Test(expected = BadRequestException.class)
  public void putPracticesIdBodyNull() {
    s.putPracticesId("123", null , true);
  }

  @Test(expected = NotFoundException.class)
  public void putPracticesIdAggiornaTaskTrueTagNotFound() {
    Pratica body = new Pratica();
    List<Long> tags = new ArrayList<>();
    tags.add(13L);
    body.setTags(tags);
    body.setCodiceIpaEnte("r_piemon");
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    List<Assegnazione> listaAssegnazione = new ArrayList<>();
    Assegnazione assegnazione = new Assegnazione();
    listaAssegnazione.add(assegnazione);
    attivita.setAssegnazione(listaAssegnazione);
    listaAttivita.add(attivita);
    body.setAttivita(listaAttivita );
    s.putPracticesId("1", body , true);
  }

  @Test
  public void putPracticesIdAggiornaTaskTrueWithTag() {
    Pratica body = new Pratica();
    List<Long> tags = new ArrayList<>();
    tags.add(2L);
    tags.add(3L);
    tags.add(4L);
    tags.add(5L);
    body.setTags(tags);
    body.setCodiceIpaEnte("r_piemon");
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    List<Assegnazione> listaAssegnazione = new ArrayList<>();
    Assegnazione assegnazione = new Assegnazione();
    listaAssegnazione.add(assegnazione);
    attivita.setAssegnazione(listaAssegnazione);
    listaAttivita.add(attivita);
    body.setAttivita(listaAttivita );
    s.putPracticesId("1", body , true);
  }

  @Test(expected = NotFoundException.class)
  public void getPraticheStatoIdPraticaExtNotFound() {
    s.getPraticheStatoIdPraticaExt("546");
  }

  @Test
  public void getPraticheStatoIdPraticaExt() {
    RiassuntoStatoPratica pratica = s.getPraticheStatoIdPraticaExt("5");
    assertNotNull(pratica);
    assertNotNull(pratica.getPratica());
    assertNotNull(pratica.getPratica().getId());
    assertTrue(pratica.getPratica().getId() == 5);
  }

  @Test(expected = NotFoundException.class)
  public void getPraticheTaskIdTaskSubtasksNotFound() {
    s.getPraticheTaskIdTaskSubtasks("111111");
  }

  @Test
  public void getPraticheTaskIdTaskSubtasks() {
    PaginaTask task = s.getPraticheTaskIdTaskSubtasks("877501");
    assertNotNull(task);
    assertNotNull(task.getElementi());
    assertNotNull(task.getElementi().get(0));
    assertNotNull(task.getElementi().get(0).getId());
    assertTrue(task.getElementi().get(0).getId().equals("197539"));
  }

  @Test
  public void getStatiPerTipoBlank() {
    List<StatoPratica> result = s.getStatiPerTipo("");
    assertNotNull(result);
    assertTrue(result.size() == 2);
  }

  @Test
  public void getStatiPerTipo() {
    List<StatoPratica> result = s.getStatiPerTipo("TP1");
    assertNotNull(result);
    assertTrue(result.size() == 2);
  }

  @Test
  public void getPraticheExportTrue() {
    String filter =
        "{\"filter\":{\"groups\":{  \"daLavorare\":{\"inCorso\": true}}},\"page\": 0,\"size\":10, \"sort\": \"dataCreazionePratica DESC\"}";
    s.getPratiche(filter, true);
  }

  @Test(expected = NotFoundException.class)
  public void getVisibilitaPraticaByIdNotFound() {
    s.getVisibilitaPraticaById(64L);
  }

  @Test
  public void getVisibilitaPraticaById() {
    Pratica pratica = s.getVisibilitaPraticaById(1L);
    assertNotNull(pratica);
    assertTrue(pratica.getId() == 1);
  }

  @Test(expected = NotFoundException.class)
  public void getVisibilitaPraticaByIdNonAccessibile() {
    s.getVisibilitaPraticaById(6L);
  }

  @Test(expected = NotFoundException.class)
  public void getVisibilitaPraticaByIdTaskNotFound() {
    s.getVisibilitaPraticaByIdTask("111111");
  }

  @Test
  public void getVisibilitaPraticaByIdTask() {
    Pratica pratica = s.getVisibilitaPraticaByIdTask("877504");
    assertNotNull(pratica);
    assertTrue(pratica.getId() == 1);
  }

  @Test(expected = NotFoundException.class)
  public void getVisibilitaPraticaByIdTaskNonVisibile() {
    s.getVisibilitaPraticaByIdTask("887509");
  }

  @Test(expected = NotFoundException.class)
  public void getPraticheIdPraticaDiagrammaNotFound() {
    s.getPraticheIdPraticaDiagramma(57);
  }

  @Test(expected = ConflictException.class)
  public void getPraticheIdPraticaDiagrammaSenzaProcesso() {
    s.getPraticheIdPraticaDiagramma(6);
  }

  @Test(expected = NotFoundException.class)
  public void getPraticheIdPraticaDiagrammaProcessoNonTrovato() {
    setupMockThrowException();
    s.getPraticheIdPraticaDiagramma(1);
  }

  @Test
  public void getPraticheIdPraticaDiagrammaConProcesso() {
    setupMockFoundProcessInstanceId();
    byte[] response = s.getPraticheIdPraticaDiagramma(1);
    assertNotNull(response);
    assertTrue(response.length == 2);
  }

  @Test
  public void getPraticheIdPraticaDiagrammaConHistoricProcess() {
    setupMockFoundHistoricProcessInstanceId();
    byte[] response = s.getPraticheIdPraticaDiagramma(1);
    assertNotNull(response);
    assertTrue(response.length == 2);
  }

  @Test
  public void getPraticheIdPraticaDiagrammaConProcessoEnded() {
    setupMockFoundProcessInstanceIdEnded();
    byte[] response = s.getPraticheIdPraticaDiagramma(1);
    assertNotNull(response);
    assertTrue(response.length == 2);
  }

  @SuppressWarnings("unchecked")
  private void setupMockThrowException() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getProcessInstanceId(any())).thenThrow(FeignClientNotFoundException.class);
    when(cosmoCmmnFeignClient.getHistoricProcessInstanceId(any())).thenThrow(FeignClientNotFoundException.class);
  }

  private void setupMockFoundProcessInstanceId() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.getProcessInstanceId(any())).thenReturn(response);
    byte[] result = new byte[2];
    when(cosmoCmmnFeignClient.getProcessInstanceDiagram(any())).thenReturn(result);
  }

  private void setupMockFoundHistoricProcessInstanceId() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getProcessInstanceId(any())).thenReturn(null);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.getHistoricProcessInstanceId(any())).thenReturn(response);
    byte[] result = new byte[2];
    when(cosmoCmmnFeignClient.getHistoricProcessInstanceDiagram(any())).thenReturn(result);
  }

  private void setupMockFoundProcessInstanceIdEnded() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    response.setEnded(Boolean.TRUE);
    when(cosmoCmmnFeignClient.getProcessInstanceId(any())).thenReturn(response);
    byte[] result = new byte[2];
    when(cosmoCmmnFeignClient.getHistoricProcessInstanceDiagram(any())).thenReturn(result);
  }
}
