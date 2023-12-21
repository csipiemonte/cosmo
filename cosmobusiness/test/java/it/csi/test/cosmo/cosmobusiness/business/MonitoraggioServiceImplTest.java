/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.cosmobusiness.business.service.MonitoraggioService;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoPraticheFeignClient;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticheNoLinkResponse;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class,
    MonitoraggioServiceImplTest.MonitoraggioIntegrationTConfig.class})
@Transactional
public class MonitoraggioServiceImplTest {

  @Configuration
  public static class MonitoraggioIntegrationTConfig {

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
  }

  @Autowired
  private MonitoraggioService monitoraggioService;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private CosmoPraticheFeignClient cosmoPraticheFeignClient;

  @Test
  @Commit
  public void testMonitoraggioPratiche() {
    setUpMockWithResult();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(!lista.isEmpty());
    assertTrue(lista.size() == 2);
  }

  @Test
  @Commit
  public void testMonitoraggioPraticheListaPraticheVuota() {
    setupMockWithNoResult();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(lista.isEmpty());
  }

  @Test
  @Commit
  public void testMonitoraggioPraticheConProcessInstanceNull() {
    setupMockWithWrapperNull();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(!lista.isEmpty());
    assertTrue(lista.size() == 1);
  }

  @Test
  @Commit
  public void testMonitoraggioPraticheConProcessInstanceDataNull() {
    setupMockWithWrapperDataNull();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(!lista.isEmpty());
    assertTrue(lista.size() == 1);
  }

  @Test
  @Commit
  public void testMonitoraggioPraticheConProcessInstanceDataVuoto() {
    setupMockWithWrapperDataVuoto();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(!lista.isEmpty());
    assertTrue(lista.size() == 1);
  }

  @Test
  @Commit
  public void testMonitoraggioPraticheExcpetionProcessInstance() {
    setupMockCmmnWithException();
    BatchExecutionContext context = new BatchExecutionContext();
    List<Pratica> lista = monitoraggioService.monitoraggioPratiche(context);
    assertTrue(!lista.isEmpty());
    assertTrue(lista.size() == 1);
  }

  private void setupMockCmmnWithException() {
    preSetupMockWithWrapper();
    RuntimeException exc = new RuntimeException();
    when(cosmoCmmnFeignClient.getHistoricProcessInstancesByBusinessKey(any())).thenThrow(exc);
  }

  private void setupMockWithWrapperDataVuoto() {
    preSetupMockWithWrapper();
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> listProcessInstance = new ArrayList<>();
    wrapper.setData(listProcessInstance);

    when(cosmoCmmnFeignClient.getHistoricProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  }

  private void setupMockWithWrapperDataNull() {
    preSetupMockWithWrapper();
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    when(cosmoCmmnFeignClient.getHistoricProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  }

  private void setupMockWithWrapperNull() {
    preSetupMockWithWrapper();
    when(cosmoCmmnFeignClient.getHistoricProcessInstancesByBusinessKey(any())).thenReturn(null);
  }

  private void preSetupMockWithWrapper() {
    reset(cosmoPraticheFeignClient);

    PraticheNoLinkResponse response = new PraticheNoLinkResponse();

    List<Pratica> listaPratiche = new ArrayList<>();
    Pratica pratica1 = new Pratica();
    pratica1.setId(1);
    pratica1.setLinkPratica(null);
    listaPratiche.add(pratica1);
    response.setPratiche(listaPratiche);

    when(cosmoPraticheFeignClient.getPraticheNoLink()).thenReturn(response);

    reset(cosmoCmmnFeignClient);
  }

  private void setupMockWithNoResult() {
    reset(cosmoPraticheFeignClient);

    PraticheNoLinkResponse response = new PraticheNoLinkResponse();
    List<Pratica> listaPratiche = new ArrayList<>();
    response.setPratiche(listaPratiche);
  }

  private void setUpMockWithResult() {
    reset(cosmoPraticheFeignClient);

    PraticheNoLinkResponse response = new PraticheNoLinkResponse();

    List<Pratica> listaPratiche = new ArrayList<>();
    Pratica pratica1 = new Pratica();
    pratica1.setId(1);
    pratica1.setLinkPratica(null);
    listaPratiche.add(pratica1);
    Pratica pratica2 = new Pratica();
    pratica2.setId(2);
    pratica2.setLinkPratica(null);
    listaPratiche.add(pratica2);
    response.setPratiche(listaPratiche);

    when(cosmoPraticheFeignClient.getPraticheNoLink()).thenReturn(response);

    reset(cosmoCmmnFeignClient);

    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    ProcessInstanceResponse process1 = new ProcessInstanceResponse();
    process1.setId("1230");
    ProcessInstanceResponse process2 = new ProcessInstanceResponse();
    process2.setId("1231");
    List<ProcessInstanceResponse> processList = new ArrayList<>();
    processList.add(process1);
    processList.add(process2);

    wrapper.setData(processList);

    when(cosmoCmmnFeignClient.getHistoricProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
  }
}
