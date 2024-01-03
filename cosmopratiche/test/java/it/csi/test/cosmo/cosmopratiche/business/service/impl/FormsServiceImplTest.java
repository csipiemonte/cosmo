/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import org.flowable.rest.service.api.repository.FormDefinitionResponse;
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
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.FormsService;
import it.csi.cosmo.cosmopratiche.dto.rest.FormDefinitionsResponseWrapper;
import it.csi.cosmo.cosmopratiche.dto.rest.SimpleForm;
import it.csi.cosmo.cosmopratiche.dto.rest.StrutturaFormLogico;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, FormsServiceImplTest.FormsIntegrationTConfig.class})
@Transactional
public class FormsServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class FormsIntegrationTConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private FormsService formsService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  @Test
  public void recuperaStrutturaDaNome() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaNome("form1");
    assertNotNull(strutturaFormLogico);
  }

  @Test
  public void recuperaStrutturaByIdAttivita() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaIdAttivita("1");
    assertNotNull(strutturaFormLogico);
  }
  
  @Test
  public void recuperaStrutturaDaNomeNull() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaNome("form12");
    assertNull(strutturaFormLogico);
  }
  
  @Test
  public void recuperaStrutturaDaNomeConRFormLogicoNonValido() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaNome("form4");
    assertNull(strutturaFormLogico);
  }
  
  @Test
  public void recuperaStrutturaDaNomeConRFormLogicoValidoEIstanzaNonCancellato() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaNome("form3");
    assertNotNull(strutturaFormLogico);
  }
  
  @Test(expected = NotFoundException.class)
  public void recuperaStrutturaByIdAttivitaNull() {
    formsService.recuperaStrutturaDaIdAttivita("57");
  }
  
  @Test
  public void recuperaStrutturaByIdAttivitaConRFormLogicoValido() {
    StrutturaFormLogico strutturaFormLogico = formsService.recuperaStrutturaDaIdAttivita("5");
    assertNotNull(strutturaFormLogico);
  }
  
  @Test(expected = NotFoundException.class)
  public void getPraticheTaskIdTaskFormAttivitaNotFound() {
    formsService.getPraticheTaskIdTaskForm("877503");
  }
  
  @Test(expected = NotFoundException.class)
  public void getPraticheTaskIdTaskFormConTaskNull() {
    setupMockNull();
    formsService.getPraticheTaskIdTaskForm("877504");
  }
  
  @Test
  public void getPraticheTaskIdTaskFormConTaskEmpty() {
    setupMockWithFormKeyEmpty();
    SimpleForm form = formsService.getPraticheTaskIdTaskForm("877504");
    assertNull(form);
  }
  
  @Test(expected = NotFoundException.class)
  public void getPraticheTaskIdTaskFormConFormAssenti() {
    setupMockWithResultMinoreDiUno();
    formsService.getPraticheTaskIdTaskForm("877504");
  }
  
  @Test(expected = InternalServerException.class)
  public void getPraticheTaskIdTaskFormConFormMultipli() {
    setupMockWithResultMaggioreDiUno();
    formsService.getPraticheTaskIdTaskForm("877504");
  }
  
  @Test(expected = NotFoundException.class)
  public void getPraticheTaskIdTaskFormConFormPresenteMaNull() {
    setupMockWithResultNull();
    formsService.getPraticheTaskIdTaskForm("877504");
  }
  
  @Test
  public void getPraticheTaskIdTask() {
    setupMockWithResult();
    SimpleForm form = formsService.getPraticheTaskIdTaskForm("877504");
    assertNotNull(form);
    assertEquals(form.getId(), "1");
  }
  
  @Test
  public void getFormDefinitionFormKey() {
    setupMockWithResult();
    SimpleForm form = formsService.getFormDefinitionFormKey("prova");
    assertNotNull(form);
    assertEquals(form.getId(), "1");
  }
  
  private void setupMockNull() {
    reset(cosmoCmmnFeignClient);
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(null);
  }
  
  private void setupMockWithFormKeyEmpty() {
    reset(cosmoCmmnFeignClient);
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setFormKey("");
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(taskResponse);
  }
  
  private void setupMockWithResultMinoreDiUno() {
    reset(cosmoCmmnFeignClient);
    
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setFormKey("prova");
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(taskResponse);
    
    FormDefinitionsResponseWrapper form = new FormDefinitionsResponseWrapper();
    form.setTotal(0L);
    when(cosmoCmmnFeignClient.queryFormDefinitions(any(), any(), any())).thenReturn(form);
  }
  
  private void setupMockWithResultMaggioreDiUno() {
    reset(cosmoCmmnFeignClient);
    
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setFormKey("prova");
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(taskResponse);
    
    FormDefinitionsResponseWrapper form = new FormDefinitionsResponseWrapper();
    form.setTotal(2L);
    when(cosmoCmmnFeignClient.queryFormDefinitions(any(), any(), any())).thenReturn(form);
  }
  
  private void setupMockWithResultNull() {
    reset(cosmoCmmnFeignClient);
    
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setFormKey("prova");
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(taskResponse);
    
    FormDefinitionsResponseWrapper form = new FormDefinitionsResponseWrapper();
    form.setTotal(1L);
    FormDefinitionResponse response = new FormDefinitionResponse();
    response.setId("1");
    FormDefinitionResponse[] data = new FormDefinitionResponse[1];
    data[0] = response;
    form.setData(data);
    when(cosmoCmmnFeignClient.queryFormDefinitions(any(), any(), any())).thenReturn(form);
  
    when(cosmoCmmnFeignClient.getFormDefinitionModel(any())).thenReturn(null);
  }
  
  private void setupMockWithResult() {
    reset(cosmoCmmnFeignClient);
    
    TaskResponse taskResponse = new TaskResponse();
    taskResponse.setFormKey("prova");
    when(cosmoCmmnFeignClient.getTaskId(any())).thenReturn(taskResponse);
    
    FormDefinitionsResponseWrapper form = new FormDefinitionsResponseWrapper();
    form.setTotal(1L);
    FormDefinitionResponse response = new FormDefinitionResponse();
    response.setId("1");
    FormDefinitionResponse[] data = new FormDefinitionResponse[1];
    data[0] = response;
    form.setData(data);
    when(cosmoCmmnFeignClient.queryFormDefinitions(any(), any(), any())).thenReturn(form);
  
    SimpleForm simpleForm = new SimpleForm();
    simpleForm.setId("1");
    when(cosmoCmmnFeignClient.getFormDefinitionModel(any())).thenReturn(simpleForm);
  }
}
