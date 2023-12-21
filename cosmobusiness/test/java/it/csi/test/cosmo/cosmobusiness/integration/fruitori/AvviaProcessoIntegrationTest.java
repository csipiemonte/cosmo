/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
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
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmobusiness.business.rest.impl.FruitoriApiServiceImpl;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AvviaProcessoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.integration.fruitori.AvviaProcessoIntegrationTest.TestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmobusiness.testbed.rest.CosmoCmmnSyncTestClient;
import it.csi.test.cosmo.cosmobusiness.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.FruitoreTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.PraticaTestbedService;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, TestConfig.class})
@Transactional()
public class AvviaProcessoIntegrationTest extends ParentIntegrationTest {

  private static final String ID_PROCESSO = "345678";

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }

    @Bean
    @Primary
    public CosmoCmmnSyncTestClient cosmoCmmnSyncTestClient() {
      return Mockito.mock(CosmoCmmnSyncTestClient.class);
    }
  }

  @Autowired
  private PraticaTestbedService praticaTestbedService;

  @Autowired
  private FruitoreTestbedService fruitoreTestbedService;

  @Autowired
  private EnteTestbedService enteTestbedService;

  @Autowired
  private CosmoCmmnFeignClient mockCosmoCmmnFeignClient;

  @Autowired
  private CosmoCmmnSyncTestClient mockCosmoCmmnSyncTestClient;

  private CosmoTEnte ente;

  private CosmoTPratica pratica;

  @Override
  protected void autentica() {

    var fruitore = fruitoreTestbedService.creaFruitore();

    var client = ClientInfoDTO.builder().withCodice(fruitore.getApiManagerId())
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();

    ente = enteTestbedService.creaEnte();
    fruitoreTestbedService.associaAdEnte(fruitore, ente);

    pratica =
        praticaTestbedService.creaPratica(ente, fruitore,
            praticaTestbedService.creaTipoPratica(ente));

    autentica(null, client);
    resetMock();
  }

  private void resetMock() {
    reset(mockCosmoCmmnFeignClient);
    reset(mockCosmoCmmnSyncTestClient);

    var mresponse = new ProcessInstanceResponse();
    mresponse.setId(ID_PROCESSO);

    Map<String, Object> response = new HashMap<>();

    when(mockCosmoCmmnFeignClient.postProcessInstance(anyObject())).thenReturn(mresponse);
    when(mockCosmoCmmnSyncTestClient.putProcessInstanceVariables(any(), any()))
    .thenReturn(response);
  }

  Supplier<AvviaProcessoFruitoreRequest> requestBuilder = () -> {
    var body = new AvviaProcessoFruitoreRequest();
    body.setCodiceIpaEnte(ente.getCodiceIpa());
    body.setIdPratica(pratica.getIdPraticaExt());
    return body;
  };

  @Test
  public void shouldExecute() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var requestBody = requestBuilder.get();

    var response = api.postProcessoFruitore(null, null, requestBody, null);
    assertNotNull(response);

    var responseEntity = (AvviaProcessoFruitoreResponse) response.getEntity();

    assertFalse(StringUtils.isBlank(responseEntity.getIdProcesso()));
    assertEquals(ID_PROCESSO, responseEntity.getIdProcesso());
    assertEquals(ente.getCodiceIpa(), responseEntity.getCodiceIpaEnte());
    assertEquals(pratica.getId().longValue(), responseEntity.getIdPraticaCosmo().longValue());
  }

  @Test
  public void shouldHandlePraticaNotFound() {
    resetMock();

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setIdPratica("123MISSING");

    expect(() -> api.postProcessoFruitore(null, null, requestBody, null), NotFoundException.class,
        e -> Arrays.asList("pratica", "trovata").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandlePraticaWithoutTipoPratica() {
    resetMock();

    CosmoTPratica anotherPratica =
        praticaTestbedService.creaPratica(ente, pratica.getFruitore(), null);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setIdPratica(anotherPratica.getIdPraticaExt());

    expect(() -> api.postProcessoFruitore(null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("nessuna", "tipologia", "pratica").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableConflictExceptionOnProcessPost() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientClientErrorException(null,
        new HttpClientErrorException(HttpStatus.CONFLICT), null);

    when(mockCosmoCmmnFeignClient.postProcessInstance(anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.postProcessoFruitore(null, null, requestBody, null), ConflictException.class,
        e -> Arrays.asList("Operazione non consentita", "stato", "pratica").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleClientExceptionOnProcessPost() {
    resetMock();

    // prepare mocks

    Throwable feignExc = new InvalidParameterException();

    when(mockCosmoCmmnFeignClient.postProcessInstance(anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.postProcessoFruitore(null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "imprevisto", "avvio", "processo").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableServerExceptionOnProcessPost() {
    resetMock();

    Exception feignExc = new FeignClientServerErrorException(null,
        new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.postProcessInstance(anyObject())).thenThrow(feignExc);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.postProcessoFruitore(null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "interna", "avvio", "processo").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

}
