/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.NotFoundException;
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
import it.csi.cosmo.cosmobusiness.business.service.EventiService;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, EventiServiceImplTest.EventiServiceTestConfig.class})
@Transactional
public class EventiServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class EventiServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cmmClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }
  
  @Autowired
  private EventiService eventiService;
  
  @Autowired
  CosmoCmmnFeignClient cmmClient;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest());
  }
  
  private static final String DT = "2022-01-11 12:12:12";
  
  @Test
  public void getEventi() {
    setupMockPostQueryTask();
    eventiService.getEventi(null, null, DT, DT, DT, DT, null, null, null, null);
  }
  
  @Test
  public void getEventiDataError() {
    setupMockPostQueryTask();
    eventiService.getEventi(null, null, "11-10-2022", DT, DT, DT, true, null, null, null);
  }
  
  @Test
  public void getEventiId() {
    setupMockGetTaskId();
    eventiService.getEventiId("1");
  }
  
  @Test(expected = NotFoundException.class)
  public void getEventiIdError() {
    setupMockGetTaskIdError();
    eventiService.getEventiId("1");
  }
  
  @Test
  public void postEvento() {
    setupMockPostTask();
    Evento body = new Evento();
    body.setDtScadenza("2022-12-12");
    body.setDescrizione("desc");
    body.setNome("nome");
    eventiService.postEvento(body);
  }
  
  @Test
  public void putEvento() {
    setupMockPutTask();
    Evento body = new Evento();
    body.setDtScadenza("2022-12-12");
    body.setDescrizione("desc");
    body.setNome("nome");
    eventiService.putEvento("1", body);
  }
  
  @Test
  public void deleteEvento() {
    setupMockDeleteTask();
    eventiService.deleteEvento("1");
  }
  
  private void setupMockDeleteTask() {
    reset(cmmClient);
    TaskResponse response = new TaskResponse();
    when(cmmClient.deleteTask(any())).thenReturn(response);
  }
  
  private void setupMockPostTask() {
    reset(cmmClient);
    TaskResponse response = new TaskResponse();
    response.setDescription("desc");
    response.setCreateTime(new Date());
    response.setDueDate(new Date());
    response.setId("id");
    response.setName("nome");
    response.setSuspended(true);
    response.setPriority(2);
    when(cmmClient.postTask(any())).thenReturn(response);
  }
  
  private void setupMockPutTask() {
    reset(cmmClient);
    TaskResponse response = new TaskResponse();
    response.setDescription("desc");
    response.setCreateTime(new Date());
    response.setDueDate(new Date());
    response.setId("id");
    response.setName("nome");
    response.setSuspended(true);
    when(cmmClient.putTask(any(), any())).thenReturn(response);
  }
  
  private void setupMockGetTaskId() {
    reset(cmmClient);
    TaskResponse response = new TaskResponse();
    when(cmmClient.getTaskId(any())).thenReturn(response);
  }
  
  private void setupMockGetTaskIdError() {
    reset(cmmClient);
    HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
    when(cmmClient.getTaskId(any())).thenThrow(exception);
  }

  private void setupMockPostQueryTask() {
    reset(cmmClient);
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    List<TaskResponse> responses = new ArrayList<>();
    TaskResponse response = new TaskResponse();
    responses.add(response);
    wrapper.setData(responses);
    when(cmmClient.postQueryTask(any())).thenReturn(wrapper);
  }
}