/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.flowable.rest.service.api.runtime.process.ExecutionResponse;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
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
import it.csi.cosmo.common.dto.rest.process.TaskIdentityLinkDTO;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.dto.rest.AvanzamentoProcessoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, ProcessServiceImplTest.ProcessServiceTestConfig.class})
@Transactional
public class ProcessServiceImplTest {
  
  @Configuration
  public static class ProcessServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;
  
  @Autowired
  private ProcessService processService;
  
  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;
  
  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;
  
  @Autowired
  private CosmoTFruitoreRepository cosmoTFruitoreRepository;
  
  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;
  
  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;
  
  private static final String PROVA = "prova111";
  private static final String CODICE_TAG = "DIR-ACQ";
  private static final String CODICE_GRUPPO = "DEMO2021";
  private static final String FORM_KEY = "form1";
  private static final String TAG_CONCATENATI = "tag.DIR-ACQ.gruppi.tag.DIR-ACQ";
  private static final String BOZZA = "BOZZA";
  
  @Test(expected = NotFoundException.class)
  public void importaNuovoTaskFormLogicoNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(PROVA);
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test(expected = NotFoundException.class)
  public void importaNuovoTaskAttivitaNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey("form3");
    task.setParentTaskId(PROVA);
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test(expected = NotFoundException.class)
  public void importaNuovoTaskTagNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    task.setParentTaskId("877504");
    task.setAssignee("AAAAAA00B77B000F");
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId("tag.");
    identityLinks.add(taskIdentityDto );
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test(expected = NotFoundException.class)
  public void importaNuovoTaskVariabileProcessoNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(TAG_CONCATENATI);
    identityLinks.add(taskIdentityDto );
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test(expected = BadRequestException.class)
  public void importaNuovoTaskFormatoVariabileNonCorretto() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(TAG_CONCATENATI);
    identityLinks.add(taskIdentityDto );
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    variabili.put(CODICE_TAG, CODICE_TAG);
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test(expected = NotFoundException.class)
  public void importaNuovoTaskTagConUtenteNonPresente() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(TAG_CONCATENATI);
    identityLinks.add(taskIdentityDto );
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    List<String> tags = new ArrayList<>();
    tags.add(CODICE_TAG);
    variabili.put(CODICE_TAG, tags);
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test
  public void importaNuovoTaskConAssegnazioniViaTag() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId("tag.DIR-ACQ");
    identityLinks.add(taskIdentityDto);
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    CosmoTAttivita attivita = processService.importaNuovoTask(pratica, task, variabili);
    assertNotNull(attivita);
    assertNotNull(attivita.getCosmoTPratica());
    assertNotNull(attivita.getCosmoTPratica().getId());
    assertTrue(attivita.getCosmoTPratica().getId() == 1);
    assertNotNull(attivita.getFormKey());
    assertEquals(attivita.getFormKey(), FORM_KEY);
  }
  
  @Test(expected = InternalServerException.class)
  public void importaNuovoTaskConGruppoNonTrovato() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(PROVA);
    identityLinks.add(taskIdentityDto);
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test
  public void importaNuovoTaskConGruppoSingolo() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(CODICE_GRUPPO);
    identityLinks.add(taskIdentityDto);
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    CosmoTAttivita attivita = processService.importaNuovoTask(pratica, task, variabili);
    assertNotNull(attivita);
    assertNotNull(attivita.getCosmoTPratica());
    assertNotNull(attivita.getCosmoTPratica().getId());
    assertTrue(attivita.getCosmoTPratica().getId() == 1);
    assertNotNull(attivita.getFormKey());
    assertEquals(attivita.getFormKey(), FORM_KEY);
  }
  
  @Test(expected = InternalServerException.class)
  public void importaNuovoTaskUtenteNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(CODICE_GRUPPO);
    identityLinks.add(taskIdentityDto);
    
    TaskIdentityLinkDTO taskIdentityDto2 = new TaskIdentityLinkDTO();
    taskIdentityDto2.setGroupId("DEMO2122");
    taskIdentityDto2.setUserId("1");
    identityLinks.add(taskIdentityDto2);
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    processService.importaNuovoTask(pratica, task, variabili);
  }
  
  @Test
  public void importaNuovoTaskConNuoveAssegnazioni() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setFormKey(FORM_KEY);
    
    Set<TaskIdentityLinkDTO> identityLinks = new HashSet<>();
    TaskIdentityLinkDTO taskIdentityDto = new TaskIdentityLinkDTO();
    taskIdentityDto.setGroupId(CODICE_GRUPPO);
    identityLinks.add(taskIdentityDto);
    
    TaskIdentityLinkDTO taskIdentityDto2 = new TaskIdentityLinkDTO();
    taskIdentityDto2.setGroupId("DEMO2122");
    taskIdentityDto2.setUserId("AAAAAA00B77B000F");
    identityLinks.add(taskIdentityDto2);
    task.setIdentityLinks(identityLinks);
    
    Map<String, Object> variabili = new HashMap<>();
    CosmoTAttivita attivita = processService.importaNuovoTask(pratica, task, variabili);
    assertNotNull(attivita);
    assertNotNull(attivita.getCosmoTPratica());
    assertNotNull(attivita.getCosmoTPratica().getId());
    assertTrue(attivita.getCosmoTPratica().getId() == 1);
    assertNotNull(attivita.getFormKey());
    assertEquals(attivita.getFormKey(), FORM_KEY);
  }
  
  @Test
  public void importaNuovaAttivita() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(1L);
    CosmoTFruitore fruitore = cosmoTFruitoreRepository.findOne(1L);
    processService.importaNuovaAttivita(pratica, attivita, fruitore);
  }
  
  @Test
  public void tentaNotificaCreazioneTaskErroreUtente() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(3L);
    processService.tentaNotificaCreazioneTask(attivita);
  }
  
  @Test
  public void tentaNotificaCreazioneTaskErroreGruppo() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(5L);
    processService.tentaNotificaCreazioneTask(attivita);
  }
  
  @Test
  public void registraCambioStatoConStatoBlank() {
    boolean result = processService.registraCambioStato(null, null, null, false);
    assertFalse(result);
  }
  
  @Test(expected = InternalServerException.class)
  public void registraCambioStatoNotFound() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    processService.registraCambioStato(pratica, PROVA, null, false);
  }
  
  @Test(expected = InternalServerException.class)
  public void registraCambioStatoNonCoerente() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(8L);
    processService.registraCambioStato(pratica, BOZZA, null, false);
  }
  
  @Test
  public void registraCambioStatoExplicitTrue() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    boolean result = processService.registraCambioStato(pratica, BOZZA, OffsetDateTime.now().toInstant(), true);
    assertTrue(result);
  }
  
  @Test
  public void registraCambioStatoExplicitFalse() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    boolean result = processService.registraCambioStato(pratica, BOZZA, OffsetDateTime.now().toInstant(), false);
    assertTrue(result);
  }
  
  @Test
  public void aggiornaAttivitaTerminata() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(1L);
    processService.aggiornaAttivitaTerminata(attivita);    
  }
  
  @Test
  public void aggiornaAttivitaAnnullataNull() {
    processService.aggiornaAttivitaAnnullata(null);    
  }
  
  @Test
  public void aggiornaAttivitaAnnullata() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(1L);
    processService.aggiornaAttivitaAnnullata(attivita);    
  }
  
  @Test
  public void aggiornaAttivitaAnnullataErroreUtente() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(3L);
    processService.aggiornaAttivitaAnnullata(attivita);
  }
  
  @Test
  public void aggiornaAttivitaAnnullataErroreGruppi() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(5L);
    processService.aggiornaAttivitaAnnullata(attivita);
  }
  
  @Test
  public void aggiornaAttivitaAnnullataConGruppi() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(4L);
    processService.aggiornaAttivitaAnnullata(attivita);
  }
  
  @Test
  public void aggiornaAssegnazioniDaEliminare() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(3L);
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    List<CosmoRAttivitaAssegnazione> assegnazioni = cosmoRAttivitaAssegnazioneRepository.findByCosmoTAttivitaIdAndDtFineValIsNull(1L);
    processService.aggiornaAssegnazioni(attivita, assegnazioni, ente);
  }
  
  @Test
  public void accodaNotifica() {
    CreaNotificaRequest request = new CreaNotificaRequest();
    processService.accodaNotifica(request);
  }
  
  @Test(expected = InternalServerException.class)
  public void buildAssegnazioneGruppoConIdEnteNull() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(1L);
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    ente.setId(null);
    processService.buildAssegnazioneGruppo(ente, attivita, CODICE_GRUPPO);
  }
  
  @Test
  public void buildAssegnazioneGruppo() {
    CosmoTAttivita attivita = cosmoTAttivitaRepository.findOne(1L);
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    CosmoRAttivitaAssegnazione assegnazione = processService.buildAssegnazioneGruppo(ente, attivita, CODICE_GRUPPO);
    assertNotNull(assegnazione);
    assertNotNull(assegnazione.getCosmoTAttivita());
    assertNotNull(assegnazione.getCosmoTAttivita().getId());
    assertTrue(assegnazione.getCosmoTAttivita().getId() == 1);
  }
  
  @Test
  public void terminaPratica() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    processService.terminaPratica(pratica, OffsetDateTime.now());
  }
  
  @Test
  public void annullaPratica() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    processService.annullaPratica(pratica, OffsetDateTime.now());
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoIdentificativoEventoBlank() {
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("");
    processService.avanzaProcesso(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoIdentificativoEventoNull() {
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    processService.avanzaProcesso(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoNonEsistente() {
    setupMockNull();
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("evento");
    processService.avanzaProcesso(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoConProcessiMultipli() {
    setupMockConProcessiMultipli();
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("evento");
    processService.avanzaProcesso(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoConExecutionNull() {
    setupMockConExecutionNull();
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("evento");
    processService.avanzaProcesso(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void avanzaProcessoConExecutionDataNull() {
    setupMockConExecutionDataNull();
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("evento");
    processService.avanzaProcesso(body);
  }
  
  @Test
  public void avanzaProcesso() {
    setupMock();
    AvanzamentoProcessoRequest body = new AvanzamentoProcessoRequest();
    body.setIdPratica(1L);
    body.setIdentificativoEvento("evento");
    processService.avanzaProcesso(body);
  }
  
  private void setupMockNull() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(null);
  }
  
  private void setupMockConProcessiMultipli() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> listResponse = new ArrayList<>();
    wrapper.setData(listResponse);
    wrapper.setTotal(2L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  }
  
  private void setupMockConExecutionNull() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> listResponse = new ArrayList<>();
    ProcessInstanceResponse processResponse = new ProcessInstanceResponse();
    processResponse.setId("1");
    listResponse.add(processResponse);
    wrapper.setData(listResponse);
    wrapper.setTotal(1L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(null);
  }
  
  private void setupMockConExecutionDataNull() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> listResponse = new ArrayList<>();
    ProcessInstanceResponse processResponse = new ProcessInstanceResponse();
    processResponse.setId("1");
    listResponse.add(processResponse);
    wrapper.setData(listResponse);
    wrapper.setTotal(1L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  
    ExecutionWrapper execution = new ExecutionWrapper();
    execution.setData(null);
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(execution );
  }
  
  private void setupMock() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> listResponse = new ArrayList<>();
    ProcessInstanceResponse processResponse = new ProcessInstanceResponse();
    processResponse.setId("1");
    listResponse.add(processResponse);
    wrapper.setData(listResponse);
    wrapper.setTotal(1L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  
    ExecutionWrapper execution = new ExecutionWrapper();
    List<ExecutionResponse> executionResponses = new ArrayList<>();
    ExecutionResponse executionResponse = new ExecutionResponse();
    executionResponses.add(executionResponse );
    execution.setData(executionResponses);
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(execution);
    
    ExecutionWrapper executionWrapper = new ExecutionWrapper();
    when(cosmoCmmnFeignClient.putExecution(any(), any())).thenReturn(executionWrapper );
  }
}