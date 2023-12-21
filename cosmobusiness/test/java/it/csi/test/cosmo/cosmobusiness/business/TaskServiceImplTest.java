/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.BadRequestException;
import org.flowable.rest.service.api.engine.variable.RestVariable;
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
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.TaskService;
import it.csi.cosmo.cosmobusiness.dto.rest.Commento;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, TaskServiceImplTest.TaskServiceTestConfig.class})
@Transactional
public class TaskServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class TaskServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoNotificationsNotificheFeignClient notificheFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;
  
  @Autowired
  private CosmoNotificationsNotificheFeignClient notificheFeignClient;
  
  @Autowired
  private TaskService taskService;
  
  private static final String TASK_ID = "877504";
  private static final String TENANT_ID = "r_piemon";
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest());
  }
  
  @Test
  public void getTaskId() {
    setupMockGetTaskId();
    taskService.getTaskId("1");
  }
  
  @Test
  public void getTask() {
    setupMockGetTask();
    taskService.getTask(null, null, null, null, null);
  }
  
  @Test(expected = NotFoundException.class)
  public void getTaskIdtaskCommentsAttivitaNotFound() {
    taskService.getTaskIdtaskComments("1");
  }
  
  @Test
  public void getTaskIdtaskComments() {
    taskService.getTaskIdtaskComments(TASK_ID);
  }
  
  @Test
  public void postTaskConParentTaskNull() {
    setupMockPostTask();
    Task task = new Task();
    taskService.postTask(task);
  }
  
  @Test
  public void postTask() {
    setupMockPostTask();
    Task task = new Task();
    task.setTenantId(TENANT_ID);
    task.setParentTaskId(TASK_ID);
    taskService.postTask(task);
  }
  
  @Test
  public void putPraticheVariabiliProcessInstanceIdNull() {
    setupMockPutProcessVariables();
    List<VariabileProcesso> body = new ArrayList<>();
    VariabileProcesso variabile = new VariabileProcesso();
    body.add(variabile);
    taskService.putPraticheVariabiliProcessInstanceId(null, body);
  }
  
  @Test
  public void putPraticheVariabiliProcessInstanceId() {
    setupMockPutProcessVariables();
    List<VariabileProcesso> body = new ArrayList<>();
    VariabileProcesso variabile = new VariabileProcesso();
    body.add(variabile);
    taskService.putPraticheVariabiliProcessInstanceId("1", body);
  }
  
  @Test
  public void postTaskIdAttivitaNotPresent() {
    setupMockPostTaskId();
    Task body = new Task();
    taskService.postTaskId("1", body);
  }
  
  @Test
  public void postTaskId() {
    setupMockPostTaskId();
    Task body = new Task();
    body.setTenantId(TENANT_ID);
    taskService.postTaskId(TASK_ID, body);
  }
  
  @Test
  public void postTaskIdtaskCommentsSenzaNotifica() {
    Commento body = new Commento();
    taskService.postTaskIdtaskComments(TASK_ID, body, false);
  }
  
  @Test
  public void postTaskIdtaskCommentsConNotifica() {
    setupMockPostNotifications();
    Commento body = new Commento();
    body.setTimestamp(OffsetDateTime.now());
    body.setCfAutore("AAAAAA00B77B000F");
    taskService.postTaskIdtaskComments(TASK_ID, body, true);
  }
  
  @Test(expected = BadRequestException.class)
  public void putTaskConAssigneeNull() {
    Task body = new Task();
    taskService.putTask("1", body);
  }
  
  @Test
  public void putTask() {
    setupMockPutTask();
    Task body = new Task();
    body.setAssignee("assignee");
    taskService.putTask("1", body);
  }
  
  private void setupMockPutTask() {
    reset(cosmoCmmnFeignClient);
    TaskResponse response = new TaskResponse();
    when(cosmoCmmnFeignClient.putTask(any(), any())).thenReturn(response);
  }
  
  private void setupMockPostNotifications() {
    reset(notificheFeignClient);
    CreaNotificheResponse response = new CreaNotificheResponse();
    when(notificheFeignClient.postNotifications(any())).thenReturn(response);
  }
  
  @SuppressWarnings("removal")
  private void setupMockPostTaskId() {
    reset(cosmoCmmnFeignClient);
    Map<String, Object> value = new HashMap<>();
    when(cosmoCmmnFeignClient.postTaskId(any(), any())).thenReturn(value );
  }
  
  private void setupMockPutProcessVariables() {
    reset(cosmoCmmnFeignClient);
    RestVariable[] response = new RestVariable[1];
    when(cosmoCmmnFeignClient.putProcessInstanceVariables(any(), any())).thenReturn(response );
  }
  
  private void setupMockPostTask() {
    reset(cosmoCmmnFeignClient);
    TaskResponse response = new TaskResponse();
    when(cosmoCmmnFeignClient.postTask(any())).thenReturn(response);
  }
  
  private void setupMockGetTask() {
    reset(cosmoCmmnFeignClient);
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    wrapper.setTotal(1L);
    wrapper.setSize(1L);
    
    List<TaskResponse> taskResponses = new ArrayList<>();
    TaskResponse task = new TaskResponse();
    task.setTenantId(TENANT_ID);
    taskResponses.add(task);
    wrapper.setData(taskResponses);
    
    when(cosmoCmmnFeignClient.getTasks(any(), any(), any(), any(), any(), any(), any())).thenReturn(wrapper );
  }
  
  private void setupMockGetTaskId() {
    reset(cosmoCmmnFeignClient);
    TaskResponse response = new TaskResponse();
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(response);
  }

}