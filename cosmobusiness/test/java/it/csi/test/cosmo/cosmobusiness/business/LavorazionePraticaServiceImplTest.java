/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
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
import it.csi.cosmo.cosmobusiness.business.service.LavorazionePraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaRequestAssegnazione;
import it.csi.cosmo.cosmobusiness.dto.rest.AssegnaAttivitaResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnTaskFeignClient;
import it.csi.cosmo.cosmocmmn.dto.rest.AssegnaTaskResponse;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, LavorazionePraticaServiceImplTest.LavorazionePraticaServiceTestConfig.class})
@Transactional
public class LavorazionePraticaServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class LavorazionePraticaServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoCmmnTaskFeignClient cosmoCmmnTaskFeignClient() {
      return Mockito.mock(CosmoCmmnTaskFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;
  
  @Autowired
  private LavorazionePraticaService lavorazionePraticaService;
  
  @Autowired
  private CosmoCmmnTaskFeignClient cosmoCmmnTaskFeignClient;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest());
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAttivitaConfermaNotFound() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(111L, 111L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentPraticaCancellata() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(14L, 9L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentDataFinePratica() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(15L, 10L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentLinkPraticaNull() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(10L, 11L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentAttivitaCancellata() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 12L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentLinkAttivitaNull() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 13L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaConParentLockNonFornito() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 3L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaTaskNotFound() {
    setupMockNull();
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 1L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaFormKeyNotBlank() {
    setupMock();
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 1L, body);
  }
  
  @Test
  public void postPraticaAttivitaConfermaFormKeyBlank() {
    setupMock();
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaConferma(1L, 14L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAttivitaSalvaNotFound() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaSalva(111L, 111L, body);
  }
  
  @Test
  public void postPraticaAttivitaSalvaConParentLockNonFornito() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaSalva(1L, 3L, body);
  }
  
  @Test
  public void postPraticaAttivitaSalvaLockNonFornito() {
    Task body = new Task();
    lavorazionePraticaService.postPraticaAttivitaSalva(1L, 1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAttivitaAssegnaNotFound() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAttivitaAssegna(111L, 111L, body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticaAttivitaAssegnaNonCoerente() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAttivitaAssegna(111L, 1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAttivitaAssegnaAttivitaCancellata() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAttivitaAssegna(111L, 12L, body);
  }
  
  @Test(expected = ConflictException.class)
  public void postPraticaAttivitaAssegnaPraticaCancellata() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAttivitaAssegna(111L, 9L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAttivitaAssegnaNonConsentito() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 14L, body);
  }
  
  @Test
  public void postPraticaAttivitaAssegnaAssegnazioniNullMantieniFalse() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setAssegnazioni(null);
    body.setMantieniAssegnazione(Boolean.FALSE);
    body.setEsclusivo(Boolean.TRUE);
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticaAttivitaAssegnaAssegnazioniNonSpecificate() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazioni.add(assegnazione);
    body.setAssegnazioni(assegnazioni);
    
    body.setMantieniAssegnazione(Boolean.FALSE);
    body.setEsclusivo(Boolean.TRUE);
    
    lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticaAttivitaAssegnaAssegnazioniUtenteErrato() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazione.setIdUtente(111L);
    assegnazioni.add(assegnazione);
    body.setAssegnazioni(assegnazioni);
    
    body.setMantieniAssegnazione(Boolean.FALSE);
    body.setEsclusivo(Boolean.TRUE);
    
    lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
  }
  
  @Test(expected = BadRequestException.class)
  public void postPraticaAttivitaAssegnaAssegnazioniGruppoErrato() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazione.setIdUtente(null);
    assegnazione.setIdGruppo(111L);
    assegnazioni.add(assegnazione);
    body.setAssegnazioni(assegnazioni);
    
    body.setMantieniAssegnazione(Boolean.FALSE);
    body.setEsclusivo(Boolean.TRUE);
    
    lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
  }
  
  @Test
  public void postPraticaAttivitaAssegnaConUtente() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazione.setIdUtente(1L);
    assegnazione.setIdGruppo(null);
    assegnazioni.add(assegnazione);
    body.setAssegnazioni(assegnazioni);
    
    body.setMantieniAssegnazione(Boolean.TRUE);
    body.setEsclusivo(Boolean.TRUE);
    
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test
  public void postPraticaAttivitaAssegnaConGruppo() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    List<AssegnaAttivitaRequestAssegnazione> assegnazioni = new ArrayList<>();
    AssegnaAttivitaRequestAssegnazione assegnazione = new AssegnaAttivitaRequestAssegnazione();
    assegnazione.setIdUtente(null);
    assegnazione.setIdGruppo(1L);
    assegnazioni.add(assegnazione);
    body.setAssegnazioni(assegnazioni);
    
    body.setMantieniAssegnazione(null);
    body.setEsclusivo(Boolean.TRUE);
    
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAttivitaAssegna(1L, 1L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test
  public void postPraticaAttivitaAssegnaAMe() {
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAttivitaAssegnaAMe(1L, 1L);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test(expected = NotFoundException.class)
  public void postPraticaAssegnaNotFound() {
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    lavorazionePraticaService.postPraticaAssegna(111L, body);
  }
  
  @Test
  public void postPraticaAssegnaConAssegnazioneTrue() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setMantieniAssegnazione(Boolean.TRUE);
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAssegna(3L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test
  public void postPraticaAssegnaConAssegnazioneFalse() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setMantieniAssegnazione(Boolean.FALSE);
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAssegna(3L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test
  public void postPraticaAssegnaConAssegnazioneNull() {
    setupMockTask();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setMantieniAssegnazione(null);
    AssegnaAttivitaResponse response = lavorazionePraticaService.postPraticaAssegna(3L, body);
    assertNotNull(response);
    assertNotNull(response.getMessaggio());
    assertEquals(response.getMessaggio(), "OK");
  }
  
  @Test(expected = Exception.class)
  public void postPraticaAssegnaErroreInvio() {
    setupMockTaskThrowException();
    AssegnaAttivitaRequest body = new AssegnaAttivitaRequest();
    body.setMantieniAssegnazione(Boolean.FALSE);
    lavorazionePraticaService.postPraticaAssegna(3L, body);
  }

  private void setupMockNull() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(null);
  }
  
  private void setupMock() {
    reset(cosmoCmmnFeignClient);
    TaskResponse response = new TaskResponse();
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(response);
  }
  
  private void setupMockTask() {
    reset(cosmoCmmnTaskFeignClient);
    AssegnaTaskResponse response = new AssegnaTaskResponse();
    when(cosmoCmmnTaskFeignClient.postTaskAssegna(any(), any())).thenReturn(response);
  }

  @SuppressWarnings("unchecked")
  private void setupMockTaskThrowException() {
    reset(cosmoCmmnTaskFeignClient);
    when(cosmoCmmnTaskFeignClient.postTaskAssegna(any(), any())).thenThrow(Exception.class);
  }
}