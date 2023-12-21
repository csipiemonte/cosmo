/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
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
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.security.model.EnteDTO;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.cosmobusiness.business.service.ProcessRecoveryService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.dto.rest.TaskResponseWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, ProcessRecoveryServiceImplTest.ProcessRecoveryServiceTestConfig.class})
@Transactional
public class ProcessRecoveryServiceImplTest {
  
  @Configuration
  public static class ProcessRecoveryServiceTestConfig {
    
    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
    
    @Bean
    @Primary
    public ProcessService processService() {
      return Mockito.mock(ProcessService.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnClient;
  
  @Autowired
  private ProcessRecoveryService processRecoveryService;
  
  @Autowired
  private ProcessService processService;

  @Test(expected = NotFoundException.class)
  public void recoverPraticaNotFound() {
    processRecoveryService.recover(111L, null);
  }
  
  @Test(expected = NotFoundException.class)
  public void recoverPraticaConLinkPraticaNull() {
    processRecoveryService.recover(6L, null);
  }
  
  @Test
  public void recoverConDataFinePratica() {
    setupMockEndedFalse();
    UserInfoDTO user = new UserInfoDTO();
    user.setAnonimo(true);
    processRecoveryService.recover(15L, user);
  }
  
  @Test
  public void recover() {
    setupMockEndedTrue();
    UserInfoDTO user = new UserInfoDTO();
    user.setAnonimo(false);
    EnteDTO ente = new EnteDTO();
    ente.setTenantId("r_piemon");
    user.setEnte(ente );
    processRecoveryService.recover(1L, user);
  }
  
  private void setupMockEndedTrue() {
    reset(cosmoCmmnClient);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    response.setEnded(true);
    when(cosmoCmmnClient.getProcessInstance(any())).thenReturn(response);
    
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    List<TaskResponse> data = new ArrayList<>();
    TaskResponse taskResponse1 = new TaskResponse();
    taskResponse1.setId("295018");
    TaskResponse taskResponse2 = new TaskResponse();
    taskResponse2.setId("877504");
    data.add(taskResponse1);
    data.add(taskResponse2);
    wrapper.setData(data);
    when(cosmoCmmnClient.getTasksByProcessInstanceId(any())).thenReturn(wrapper);
    
    RestVariable[] value = new RestVariable[1];
    RestVariable variable = new RestVariable();
    variable.setName("stato");
    variable.setValue("PROVA");
    value[0] = variable;
    when(cosmoCmmnClient.getProcessInstanceVariables(any())).thenReturn(value);
  }

  private void setupMockEndedFalse() {
    reset(cosmoCmmnClient);
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    response.setEnded(false);
    when(cosmoCmmnClient.getProcessInstance(any())).thenReturn(response);
    
    TaskResponseWrapper wrapper = new TaskResponseWrapper();
    List<TaskResponse> data = new ArrayList<>();
    wrapper.setData(data);
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setId("1");
    data.add(taskResponse );
    when(cosmoCmmnClient.getTasksByProcessInstanceId(any())).thenReturn(wrapper);
    
    RestVariable[] value = new RestVariable[1];
    RestVariable variable = new RestVariable();
    variable.setName("stato");
    variable.setValue("PROVA");
    value[0] = variable;
    when(cosmoCmmnClient.getProcessInstanceVariables(any())).thenReturn(value );
  
    reset(processService);
    CosmoTAttivita attivita = new CosmoTAttivita();
    when(processService.importaNuovoTask(any(), any(), any())).thenReturn(attivita );
  }
}