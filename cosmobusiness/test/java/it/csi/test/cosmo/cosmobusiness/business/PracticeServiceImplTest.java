package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ExecutionResponse;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoDTrasformazioneDatiPratica;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.cosmobusiness.business.service.PracticeService;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.dto.exception.FlowableVariableHandlingException;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaPraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.FormTask;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaCommenti;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaTask;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.RiassuntoStatoPratica;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnSyncFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFormsApiFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.FunzionalitaFormLogico;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/*
 * Copyright CSI-Piemonte - 2023 SPDX-License-Identifier: GPL-3.0-or-later
 */


/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class,
    PracticeServiceImplTest.PracticeServiceTestConfig.class})
@Transactional
public class PracticeServiceImplTest extends ParentIntegrationTest {

  @Configuration
  public static class PracticeServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoPraticheFeignClient cosmoPraticheFeignClient() {
      return Mockito.mock(CosmoPraticheFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoPraticheFormsApiFeignClient formsClient() {
      return Mockito.mock(CosmoPraticheFormsApiFeignClient.class);
    }

    @Bean
    @Primary
    public TaskService taskService() {
      return Mockito.mock(TaskService.class);
    }

    @Bean
    @Primary
    public CosmoNotificationsNotificheGlobaliFeignClient notificheGlobaliFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheGlobaliFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoCmmnSyncFeignClient cosmoCmmnSyncFeignClient() {
      return Mockito.mock(CosmoCmmnSyncFeignClient.class);
    }
  }

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest());
  }

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheFeignClient;

  @Autowired
  private CosmoPraticheFormsApiFeignClient formsClient;

  @Autowired
  private TaskService taskService;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient notificheGlobaliFeignClient;

  @Autowired
  private PracticeService practiceService;
  
  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;
  
  @Autowired
  private CosmoCmmnSyncFeignClient cosmoCmmnSyncFeignClient;

  @Test
  public void getPraticheId() {
    setupMockCmmn();
    practiceService.getPraticheId("1");
  }

  @Test(expected = NotFoundException.class)
  public void getCommentiPraticaNotFound() {
    practiceService.getCommenti("process");
  }

  @Test
  public void getCommenti() {
    PaginaCommenti commenti = practiceService.getCommenti("1");
    assertNotNull(commenti);
    assertNotNull(commenti.getElementi());
    assertTrue(commenti.getElementi().size() == 2);
  }

  @Test(expected = NotFoundException.class)
  public void getPraticheIdCommentiIdCommentoNotFound() {
    practiceService.getPraticheIdCommentiIdCommento("111", "1");
  }

  @Test
  public void getPraticheIdCommentiIdCommento() {
    Commento commento = practiceService.getPraticheIdCommentiIdCommento("1", "1");
    assertNotNull(commento);
    assertNotNull(commento.getId());
    assertEquals(commento.getId(), "1");
  }

  @Test
  public void getPraticheStatoIdPraticaExt() {
    setupMockPratiche();
    RiassuntoStatoPratica riassunto = practiceService.getPraticheStatoIdPraticaExt("1");
    assertNotNull(riassunto);
    assertNotNull(riassunto.getAttivita());
    assertNotNull(riassunto.getCommenti());
  }

  @Test
  public void getPraticheIdTasks() {
    setupMockCmmnGetProcess();
    PaginaTask task = practiceService.getPraticheIdTasks("1");
    assertNotNull(task);
  }

  @Test
  public void getPraticheTaskIdTaskSubtasks() {
    setupMockPraticheTask();
    PaginaTask task = practiceService.getPraticheTaskIdTaskSubtasks("1");
    assertNotNull(task);
  }

  @Test
  public void getPraticheTaskIdTaskConFormsNull() {
    setupMockTaskId();
    setupMockCmmn();
    setupMockPraticheIdPratica();
    setupMockFormsNull();
    FormTask task = practiceService.getPraticheTaskIdTask("1");
    assertNotNull(task);
    assertNotNull(task.getFunzionalita());
    assertTrue(task.getFunzionalita().isEmpty());
  }

  @Test
  public void getPraticheTaskIdTaskConFunzionalitaFormsNull() {
    setupMockTaskId();
    setupMockCmmn();
    setupMockPraticheIdPratica();
    setupMockFunzionalitaFormsNull();
    FormTask task = practiceService.getPraticheTaskIdTask("1");
    assertNotNull(task);
    assertNotNull(task.getFunzionalita());
    assertTrue(task.getFunzionalita().isEmpty());
  }

  @Test
  public void getPraticheTaskIdTask() {
    setupMockTaskId();
    setupMockCmmn();
    setupMockPraticheIdPratica();
    setupMockForms();
    FormTask task = practiceService.getPraticheTaskIdTask("1");
    assertNotNull(task);
    assertNotNull(task.getFunzionalita());
    assertFalse(task.getFunzionalita().isEmpty());
  }

  @Test
  public void postCommentiErroreNotificaCreazioneCommento() {
    setupMockNotificheThrowException();
    Commento body = new Commento();
    body.setCfAutore("cf");
    practiceService.postCommenti("1", body);
  }

  @Test
  public void postCommenti() {
    setupMockNotifiche();
    Commento body = new Commento();
    body.setTimestamp(OffsetDateTime.now());
    practiceService.postCommenti("1", body);
  }

  @Test(expected = BadRequestException.class)
  public void creaVistoGraficoAttivitaNotFound() {
    practiceService.creaVistoGrafico(111L);
  }

  @Test
  public void creaVistoGrafico() {
    practiceService.creaVistoGrafico(1L);
  }

  @Test(expected = BadRequestException.class)
  public void creaVistoGraficoApprovazioneEsistente() {
    practiceService.creaVistoGrafico(2L);
  }

  @Test
  public void postPraticheFruitoreSenzaPermessi() {
    setupMockPostPratiche();
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setOggetto("oggetto");
    body.setCodiceTipo("TP1");
    body.setCodiceFruitore("r_piemon");
    practiceService.postPratiche(body);
  }

  @Test
  public void postPraticheFruitoreConPermessi() {
    setupMockPostPratiche();
    CreaPraticaRequest body = new CreaPraticaRequest();
    body.setOggetto("oggetto");
    body.setCodiceTipo("TP1");
    body.setCodiceFruitore("FruitoreTest1");
    body.setRiassunto("riassunto");
    practiceService.postPratiche(body);
  }

  @Test
  public void putPratiche() {
    setupMockPutPratiche();
    it.csi.cosmo.cosmobusiness.dto.rest.Pratica body =
        new it.csi.cosmo.cosmobusiness.dto.rest.Pratica();
    it.csi.cosmo.cosmobusiness.dto.rest.Pratica pratica =
        practiceService.putPratiche("1", body, true);
    assertNotNull(pratica);
    assertNotNull(pratica.getOggetto());
    assertTrue(pratica.getOggetto().equals("putOggetto"));
  }

  @Test
  public void deletePraticheIdCommentiIdCommento() {
    practiceService.deletePraticheIdCommentiIdCommento("1", "1");
  }

  @Test(expected = ForbiddenException.class)
  public void deletePraticheIdCommentiIdCommentoUtenteDiverso() {
    practiceService.deletePraticheIdCommentiIdCommento("2", "1");
  }

  @Test
  public void deletePraticheIdConIdLinkNull() {
    setupMockDeletePraticheIdFromLinkNull();
    practiceService.deletePraticheId("1");
  }

  @Test
  public void deletePraticheIdWithWrapper() {
    setupMockDeletePraticheWithWrapper();
    practiceService.deletePraticheId("1");
  }

  @Test
  public void deletePraticheIdWithWrapperNull() {
    setupMockDeletePraticheWrapperNull();
    practiceService.deletePraticheId("1");
  }

  @Test
  public void deletePraticheIdWithWrapperDataEmpty() {
    setupMockDeletePraticheWrapperDataEmpty();
    practiceService.deletePraticheId("1");
  }

  @Test(expected = InvalidParameterException.class)
  public void avviaProcessoConPraticaNull() {
    CosmoTPratica pratica = null;
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = InternalServerException.class)
  public void avviaProcessoConTipoPraticaNull() {
    CosmoTPratica pratica = new CosmoTPratica();
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = InternalServerException.class)
  public void avviaProcessoConProcessKeyNull() {
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoDTipoPratica tipo = new CosmoDTipoPratica();
    pratica.setTipo(tipo);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = InvalidParameterException.class)
  public void avviaProcessoPraticaIdNull() {
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoDTipoPratica tipo = new CosmoDTipoPratica();
    tipo.setProcessDefinitionKey("1");
    pratica.setTipo(tipo);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = ConflictException.class)
  public void avviaProcessoConProcessoGiaEsistente() {
    setupMockAvviaProcessoGiaEsistente();
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void avviaProcessoRecuperoProcessoInFeignClientClientError() {
    setupMockRecuperoProcessoInFeignClientClientError();
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = InternalServerException.class)
  public void avviaProcessoRecuperoProcessoInErrore() {
    setupMockRecuperoProcessoInErrore();
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = InternalServerException.class) 
  public void avviaProcessoErrorePraticaNonTrovata() {
    setupMockErroreAvvioProcessoPraticaNonTrovata();
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoDTipoPratica tipo = new CosmoDTipoPratica();
    tipo.setProcessDefinitionKey("1");
    List<CosmoDTrasformazioneDatiPratica> trasformazioni = new ArrayList<>();
    tipo.setTrasformazioni(trasformazioni);
    pratica.setTipo(tipo);
    pratica.setId(111L);
    CosmoTEnte ente = new CosmoTEnte();
    ente.setCodiceIpa("r_piemon");
    pratica.setEnte(ente);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = ConflictException.class)
  public void avviaProcessoPerOperazioneNonConsentita() {
    setupMockPerOperazioneNonConsentita();
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoDTipoPratica tipo = new CosmoDTipoPratica();
    tipo.setProcessDefinitionKey("1");
    List<CosmoDTrasformazioneDatiPratica> trasformazioni = new ArrayList<>();
    tipo.setTrasformazioni(trasformazioni);
    pratica.setTipo(tipo);
    pratica.setId(4L);
    CosmoTEnte ente = new CosmoTEnte();
    ente.setCodiceIpa("r_piemon");
    pratica.setEnte(ente);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void avviaProcessoErrorePostProcessInstance() {
    setupMockErrorePostProcessInstance();
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoDTipoPratica tipo = new CosmoDTipoPratica();
    tipo.setProcessDefinitionKey("1");
    List<CosmoDTrasformazioneDatiPratica> trasformazioni = new ArrayList<>();
    tipo.setTrasformazioni(trasformazioni);
    pratica.setTipo(tipo);
    pratica.setId(5L);
    CosmoTEnte ente = new CosmoTEnte();
    ente.setCodiceIpa("r_piemon");
    pratica.setEnte(ente);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test
  public void avviaProcesso() {
    setupMockAvviaProcesso();
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(13L);
    practiceService.avviaProcesso(pratica);
  }
  
  @Test(expected = FlowableVariableHandlingException.class)
  public void getVariablesFromHistoryProcessError() {
    setupMockVariablesHistoryError();
    practiceService.getVariablesFromHistoryProcess("1", Boolean.TRUE);
  }
  
  @Test
  public void getVariablesFromHistory() {
    setupMockVariablesHistory();
    practiceService.getVariablesFromHistoryProcess("1", Boolean.TRUE);
  }
  
  @Test(expected = NotFoundException.class)
  public void avviaProcessoPraticaNotFound() {
    practiceService.avviaProcesso("111");
  }
  
  @Test(expected = InternalServerException.class)
  public void avviaProcessoConIdPraticaErroreImprevisto() {
    practiceService.avviaProcesso("13");
  }

  private void setupMockCmmn() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    response.setBusinessKey("1");
    when(cosmoCmmnFeignClient.getProcessInstance(any())).thenReturn(response);
  }

  private void setupMockPratiche() {
    reset(cosmoPraticheFeignClient);
    it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica riassunto =
        new it.csi.cosmo.cosmopratiche.dto.rest.RiassuntoStatoPratica();
    when(cosmoPraticheFeignClient.getPraticheStatoIdPraticaExt(any())).thenReturn(riassunto);

    reset(cosmoCmmnFeignClient);
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    List<TaskResponse> taskResponse = new ArrayList<>();
    TaskResponse response = new TaskResponse();
    taskResponse.add(response);
    wrapper.setData(taskResponse);
    when(cosmoCmmnFeignClient.getPraticheIdStoricoAttivitaTask(any(), any())).thenReturn(wrapper);
  }

  private void setupMockCmmnGetProcess() {
    reset(cosmoCmmnFeignClient);
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    List<TaskResponse> taskResponse = new ArrayList<>();
    TaskResponse response = new TaskResponse();
    taskResponse.add(response);
    wrapper.setData(taskResponse);
    when(cosmoCmmnFeignClient.getPraticheTasksProcessInstanceId(any())).thenReturn(wrapper);
  }

  private void setupMockPraticheTask() {
    reset(cosmoPraticheFeignClient);
    it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask task =
        new it.csi.cosmo.cosmopratiche.dto.rest.PaginaTask();
    when(cosmoPraticheFeignClient.getPraticheTaskIdTaskSubtasks(any())).thenReturn(task);
  }

  private void setupMockPraticheIdPratica() {
    reset(cosmoPraticheFeignClient);
    Pratica pratica = new Pratica();
    when(cosmoPraticheFeignClient.getPraticheIdPratica(any(), any())).thenReturn(pratica);
  }

  private void setupMockFormsNull() {
    reset(formsClient);
    when(formsClient.getFormsNome(any())).thenReturn(null);
  }

  private void setupMockFunzionalitaFormsNull() {
    reset(formsClient);
    StrutturaFormLogico formLogico = new StrutturaFormLogico();
    formLogico.setFunzionalita(null);
    when(formsClient.getFormsNome(any())).thenReturn(formLogico);
  }

  private void setupMockForms() {
    reset(formsClient);
    StrutturaFormLogico formLogico = new StrutturaFormLogico();
    List<FunzionalitaFormLogico> funzionalita = new ArrayList<>();
    FunzionalitaFormLogico funzionalitaForm = new FunzionalitaFormLogico();
    funzionalita.add(funzionalitaForm);
    formLogico.setFunzionalita(funzionalita);
    when(formsClient.getFormsNome(any())).thenReturn(formLogico);
  }

  private void setupMockTaskId() {
    reset(taskService);
    Task task = new Task();
    task.setProcessInstanceId("1");
    task.setName("task");
    when(taskService.getTaskId(any())).thenReturn(task);
  }

  private void setupMockNotificheThrowException() {
    reset(notificheGlobaliFeignClient);
    doThrow(Exception.class).when(notificheGlobaliFeignClient).postNotificheGlobali(any());
  }

  private void setupMockNotifiche() {
    reset(notificheGlobaliFeignClient);
    doNothing().when(notificheGlobaliFeignClient).postNotificheGlobali(any());
  }

  private void setupMockPostPratiche() {
    reset(cosmoPraticheFeignClient);
    Pratica pratica = new Pratica();
    pratica.setId(1);
    when(cosmoPraticheFeignClient.postPratiche(any())).thenReturn(pratica);
  }

  private void setupMockPutPratiche() {
    reset(cosmoPraticheFeignClient);
    Pratica pratica = new Pratica();
    pratica.setOggetto("putOggetto");
    when(cosmoPraticheFeignClient.putPraticheIdPratica(any(), any(), any())).thenReturn(pratica);
  }

  private void setupMockDeletePraticheIdFromLinkNull() {
    setupMockPraticheIdPratica();
    doNothing().when(cosmoPraticheFeignClient).deletePraticheIdPratica(any());
  }

  private void setupMockDeletePraticheWithWrapper() {
    setupMockPraticheIdPraticaLinkPraticaNotNull();
    setupMockExecutionsNotNull();
  }

  private void setupMockPraticheIdPraticaLinkPraticaNotNull() {
    reset(cosmoPraticheFeignClient);
    Pratica pratica = new Pratica();
    pratica.setLinkPratica("/1/");
    when(cosmoPraticheFeignClient.getPraticheIdPratica(any(), any())).thenReturn(pratica);
    doNothing().when(cosmoPraticheFeignClient).deletePraticheIdPratica(any());
  }

  @SuppressWarnings("removal")
  private void setupMockExecutionsNotNull() {
    reset(cosmoCmmnFeignClient);
    ExecutionWrapper wrapper = new ExecutionWrapper();
    List<ExecutionResponse> data = new ArrayList<>();
    ExecutionResponse response = new ExecutionResponse();
    data.add(response);
    wrapper.setData(data);
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(wrapper);
    ExecutionWrapper wrapperPut = new ExecutionWrapper();
    when(cosmoCmmnFeignClient.putExecution(any(), any())).thenReturn(wrapperPut);
    doNothing().when(cosmoCmmnFeignClient).putPraticaIdPraticaExt(any(), any());
  }

  @SuppressWarnings("removal")
  private void setupMockDeletePraticheWrapperNull() {
    setupMockPraticheIdPraticaLinkPraticaNotNull();
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(null);
    doThrow(Exception.class).when(cosmoCmmnFeignClient).putPraticaIdPraticaExt(any(), any());
  }

  @SuppressWarnings("removal")
  private void setupMockDeletePraticheWrapperDataEmpty() {
    setupMockPraticheIdPraticaLinkPraticaNotNull();
    reset(cosmoCmmnFeignClient);
    ExecutionWrapper wrapper = new ExecutionWrapper();
    List<ExecutionResponse> data = new ArrayList<>();
    wrapper.setData(data);
    when(cosmoCmmnFeignClient.getExecutions(any(), any())).thenReturn(wrapper);
    doThrow(Exception.class).when(cosmoCmmnFeignClient).putPraticaIdPraticaExt(any(), any());
  }
  
  private void setupMockAvviaProcessoGiaEsistente() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    wrapper.setSize(1L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  }
  
  private void setupMockRecuperoProcessoInFeignClientClientError() {
    reset(cosmoCmmnFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    errorMessage.setTitle("error");
    FeignClientClientErrorException exception = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenThrow(exception);
  }
  
  private void setupMockRecuperoProcessoInErrore() {
    reset(cosmoCmmnFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientClientErrorException exception = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenThrow(exception);
  }
  
  private void setupMockErroreAvvioProcessoPraticaNonTrovata() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    wrapper.setSize(0L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.postProcessInstance(any())).thenReturn(response);
    
    reset(cosmoCmmnSyncFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientClientErrorException feignException = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnSyncFeignClient.putProcessInstanceVariables(any(), any())).thenThrow(feignException);
  }
  
  private void setupMockPerOperazioneNonConsentita() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    wrapper.setSize(0L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.postProcessInstance(any())).thenReturn(response);
    
    reset(cosmoCmmnSyncFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientClientErrorException feignException = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.CONFLICT), errorMessage);
    when(cosmoCmmnSyncFeignClient.putProcessInstanceVariables(any(), any())).thenThrow(feignException);
  }
  
  private void setupMockErrorePostProcessInstance() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    wrapper.setSize(0L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.postProcessInstance(any())).thenReturn(response);
    
    reset(cosmoCmmnSyncFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    errorMessage.setTitle("error");
    FeignClientClientErrorException feignException = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(cosmoCmmnSyncFeignClient.putProcessInstanceVariables(any(), any())).thenThrow(feignException);
  }
  
  private void setupMockAvviaProcesso() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    wrapper.setSize(0L);
    when(cosmoCmmnFeignClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    when(cosmoCmmnFeignClient.postProcessInstance(any())).thenReturn(response);
    
    reset(cosmoCmmnSyncFeignClient);
    Map<String, Object> value = new HashMap<>();
    when(cosmoCmmnSyncFeignClient.putProcessInstanceVariables(any(), any())).thenReturn(value);
  }
  
  private void setupMockVariablesHistoryError() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse response1 = new ProcessInstanceResponse();
    ProcessInstanceResponse response2 = new ProcessInstanceResponse();
    response1.setId("1");
    List<RestVariable> variabili = new ArrayList<>();
    RestVariable variabile1 = new RestVariable();
    variabile1.setValueUrl("url");
    RestVariable variabile2 = new RestVariable();
    RestVariable variabile3 = new RestVariable();
    variabile3.setValue("value");
    variabili.add(variabile1);
    variabili.add(variabile2);
    variabili.add(variabile3);
    response1.setVariables(variabili);
    response2.setId("2");
    data.add(response1);
    data.add(response2);
    wrapper.setData(data);
    when(cosmoCmmnFeignClient.getHistoricProcessInstances(any(), any())).thenReturn(wrapper);
    byte[] raw = new byte[1];
    when(cosmoCmmnFeignClient.getSerializedHistoryVariable(any(), any())).thenReturn(raw);
  }
  
  private void setupMockVariablesHistory() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse response1 = new ProcessInstanceResponse();
    response1.setId("1");
    List<RestVariable> variabili = new ArrayList<>();
    RestVariable variabile1 = new RestVariable();
    variabile1.setValueUrl("url");
    variabili.add(variabile1);
    response1.setVariables(variabili);
    data.add(response1);
    wrapper.setData(data);
    when(cosmoCmmnFeignClient.getHistoricProcessInstances(any(), any())).thenReturn(wrapper);
    when(cosmoCmmnFeignClient.getSerializedHistoryVariable(any(), any())).thenReturn(null);
  }
}