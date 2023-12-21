/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceDTO;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.business.service.ProcessEventService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, ProcessEventServiceImplTest.ProcessEventServiceTestConfig.class})
@Transactional
public class ProcessEventServiceImplTest {
  
  @Configuration
  public static class ProcessEventServiceTestConfig {
    
    @Bean
    @Primary
    public ProcessService processService() {
      return Mockito.mock(ProcessService.class);
    }
  }
  
  @Autowired
  private ProcessEventService processEventService;
  
  @Autowired
  private ProcessService processService;
  
  private static final String ACTIVITY_CANCELLED = "activityCancelled";
  private static final String PROVA = "PROVA";
  private static final String TASK_ID = "877504";
  private static final String STATO = "stato";
  private static final String TASK_CREATED = "taskCreated";
  private static final String TASK_COMPLETED = "taskCompleted";
  private static final String TASK_ASSIGNED = "taskAssigned";
  
  
  @Test(expected = BadRequestException.class)
  public void processConMessageNonGestito() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType("message");
    body.setTimestamp(OffsetDateTime.now());
    processEventService.process(body);
  }
  
  @Test
  public void processActivityCancelledProcessNull() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(ACTIVITY_CANCELLED);
    body.setTimestamp(OffsetDateTime.now());
    processEventService.process(body);
  }
  
  @Test
  public void processActivityCancelled() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(ACTIVITY_CANCELLED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId(TASK_ID);
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processActivityCancelledAttivitaNotFound() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(ACTIVITY_CANCELLED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId("111");
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void processTaskCreatedPraticaNotFound() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_CREATED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("111");
    body.setProcess(process);
    TaskInstanceDTO task = new TaskInstanceDTO();
    body.setTask(task);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskCreated() {
    setupMockImportaNuovoTask();
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_CREATED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    body.setProcess(process);
    TaskInstanceDTO task = new TaskInstanceDTO();
    body.setTask(task);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskCreatedProcessNull() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_CREATED);
    body.setTimestamp(OffsetDateTime.now());
    processEventService.process(body);
  }
  
  @Test
  public void processTaskCompleted() {
    setupMockAggiornaAttivitaTerminata();
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_COMPLETED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId(TASK_ID);
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskCompletedAttivitaNotFound() {
    setupMockAggiornaAttivitaTerminata();
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_COMPLETED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId("111");
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskCompletedProcessNull() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_COMPLETED);
    body.setTimestamp(OffsetDateTime.now());
    processEventService.process(body);
  }
  
  @Test
  public void processTaskAssigned() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_ASSIGNED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId(TASK_ID);
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskAssignedAttivitaNotFound() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_ASSIGNED);
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    TaskInstanceDTO task = new TaskInstanceDTO();
    task.setId("111");
    body.setTask(task);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processTaskAssignedProcessNull() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType(TASK_ASSIGNED);
    body.setTimestamp(OffsetDateTime.now());
    processEventService.process(body);
  }
  
  @Test(expected = InternalServerException.class)
  public void processCreatedPraticaNotFound() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType("processCreated");
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("111");
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processCreated() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType("processCreated");
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processCompleted() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType("processCompleted");
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  @Test
  public void processStateChange() {
    ProcessEngineEventDTO body = new ProcessEngineEventDTO();
    body.setMessageType("stateChange");
    body.setTimestamp(OffsetDateTime.now());
    ProcessInstanceDTO process = new ProcessInstanceDTO();
    process.setBusinessKey("1");
    Map<String, Object> variables = new HashMap<>();
    variables.put(STATO, PROVA);
    process.setVariables(variables);
    body.setProcess(process);
    processEventService.process(body);
  }
  
  private void setupMockImportaNuovoTask() {
    reset(processService);
    CosmoTAttivita attivita = new CosmoTAttivita();
    when(processService.importaNuovoTask(any(), any(), any())).thenReturn(attivita);
  }
  
  private void setupMockAggiornaAttivitaTerminata() {
    reset(processService);
    doNothing().when(processService).aggiornaAttivitaTerminata(any());
  }
}