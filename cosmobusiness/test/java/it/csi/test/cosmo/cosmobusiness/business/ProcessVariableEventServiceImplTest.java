/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceVariableEventDTO;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.ProcessVariableEventService;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class ProcessVariableEventServiceImplTest {
  
  @Autowired
  private ProcessVariableEventService processVariableEventService;
  
  private static final String VARIABLE_CREATED = "variableCreated";
  private static final String VARIABILE = "variabile";
  private static final String OGGETTO = "oggetto";
  
  @Test(expected = BadRequestException.class)
  public void processVariableCreatedMessaggioNonGestito() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType("prova");
    processVariableEventService.process(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void processVariableCreatedVariabileNotFound() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType(VARIABLE_CREATED);
    body.setBusinessKey("111");
    body.setVariableName(VARIABLE_CREATED);
    body.setVariableType("type");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableCreatedVariabileNull() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType(VARIABLE_CREATED);
    body.setBusinessKey("1");
    body.setVariableName(VARIABILE);
    body.setVariableType("json");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableCreatedVariabileEsistente() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType(VARIABLE_CREATED);
    body.setBusinessKey("1");
    body.setVariableName(OGGETTO);
    body.setVariableType("json");
    body.setLongVariableValue(1L);
    body.setBytearrayVariableValue(new byte[1]);
    body.setDoubleVariableValue(2.0);
    body.setTextVariableValue("text");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableUpdatedVariabileNull() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType("variableUpdated");
    body.setBusinessKey("1");
    body.setVariableName(VARIABILE);
    body.setVariableType("text");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableUpdatedVariabileEsistente() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType("variableUpdated");
    body.setBusinessKey("1");
    body.setVariableName(OGGETTO);
    body.setVariableType("text");
    body.setTextVariableValue("text");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableDeletedVariabileNull() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType("variableDeleted");
    body.setBusinessKey("1");
    body.setVariableName(VARIABILE);
    body.setVariableType("text");
    processVariableEventService.process(body);
  }
  
  @Test
  public void processVariableDeletedVariabileEsistente() {
    ProcessInstanceVariableEventDTO body = new ProcessInstanceVariableEventDTO();
    body.setMessageType("variableDeleted");
    body.setBusinessKey("1");
    body.setVariableName(OGGETTO);
    body.setVariableType("text");
    body.setTextVariableValue("text");
    processVariableEventService.process(body);
  }
}