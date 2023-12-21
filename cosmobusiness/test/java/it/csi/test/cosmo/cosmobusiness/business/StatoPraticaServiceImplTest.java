/*
 * Copyright CSI-Piemonte - 2023 SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.junit.Ignore;
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
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, StatoPraticaServiceImplTest.StatoPraticaServiceTestConfig.class})
@Transactional()
public class StatoPraticaServiceImplTest {

  @Configuration
  public static class StatoPraticaServiceTestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient chiamataEsternaService() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnFeignClient;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Test
  public void getPratica() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    statoPraticaService.getPratica(pratica);
  }

  @Test
  public void getAttivita() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    statoPraticaService.getAttivita(pratica);
  }

  @Ignore("da rivedere")
  @Test
  public void getDocumenti() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(2L);
    statoPraticaService.getDocumenti(pratica);
  }

  @Test
  public void getMessaggi() {
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    statoPraticaService.getMessaggi(pratica);
  }

  @Test
  public void getVariabiliProcesso() {
    setupMock();
    CosmoTPratica pratica = cosmoTPraticaRepository.findOne(1L);
    statoPraticaService.getVariabiliProcesso(pratica);
  }

  private void setupMock() {
    reset(cosmoCmmnFeignClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse response = new ProcessInstanceResponse();
    List<RestVariable> variabili = new ArrayList<>();
    RestVariable variabile = new RestVariable();
    variabile.setName("name");
    variabile.setValue("value");
    variabili.add(variabile);
    response.setVariables(variabili);
    data.add(response);
    wrapper.setData(data);
    when(cosmoCmmnFeignClient.getHistoricProcessInstances(any(), any())).thenReturn(wrapper);
  }
}