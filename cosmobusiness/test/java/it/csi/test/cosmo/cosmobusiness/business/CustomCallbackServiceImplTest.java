/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import javax.ws.rs.NotFoundException;
import org.flowable.rest.service.api.engine.variable.RestVariable;
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
import it.csi.cosmo.cosmobusiness.business.service.CustomCallbackService;
import it.csi.cosmo.cosmobusiness.dto.rest.CustomCallbackResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.business.CustomCallbackServiceImplTest.CustomCallbackIntegrationTConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
    classes = {CosmoBusinessUnitTestInMemory.class, CustomCallbackIntegrationTConfig.class})
@Transactional()
public class CustomCallbackServiceImplTest extends ParentIntegrationTest {

  @Configuration
  public static class CustomCallbackIntegrationTConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }

  @Autowired
  private CosmoCmmnFeignClient mockCosmoCmmnFeignClient;

  @Autowired
  private CustomCallbackService customCallbackService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
    reset(mockCosmoCmmnFeignClient);
  }

  @Test
  public void getCustomCallback() {
    CustomCallbackResponse response =
        customCallbackService.getCustomEndopoint("FruitoreTest1", "Codice descrittivo", null);
    assertNotNull(response);
    assertTrue("Codice descrittivo".equals(response.getCodiceDescrittivo()));
    assertTrue("http://www.fruitore1.it/endpoint".equals(response.getUrl()));
  }

  @Test
  public void getCustomCallbackPlaceHolder() {

    RestVariable variabile = new RestVariable();
    variabile.setName("esempio");
    variabile.setValue("valore");

    when(mockCosmoCmmnFeignClient.getProcessInstanceVariable(any(), any())).thenReturn(variabile);

    CustomCallbackResponse response = customCallbackService.getCustomEndopoint("FruitoreTest1",
        "Codice descrittivo - placeholder", "0000001");
    assertNotNull(response);
    assertTrue("Codice descrittivo - placeholder".equals(response.getCodiceDescrittivo()));
    assertTrue("http://www.fruitore1.it/endpoint/valore".equals(response.getUrl()));
  }

  @Test(expected = NotFoundException.class)
  public void getCustomEndpointFruitoreNotFound() {
    customCallbackService.getCustomEndopoint("FruitoreTest111", "codice", null);
  }

  @Test(expected = InternalServerException.class)
  public void getCustomEndpointFruitoreUrlNull() {
    customCallbackService.getCustomEndopoint("FruitoreTest5", "codice", null);
  }

  @Test(expected = NotFoundException.class)
  public void getCustomEndpointNotFound() {
    setupMockNull();
    customCallbackService.getCustomEndopoint("FruitoreTest1", "Codice descrittivo - placeholder", "1");
  }

  private void setupMockNull() {
    reset(mockCosmoCmmnFeignClient);
    when(mockCosmoCmmnFeignClient.getProcessInstanceVariable(any(), any())).thenReturn(null);
  }
}
