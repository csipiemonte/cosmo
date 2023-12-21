/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
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
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmobusiness.business.rest.impl.FruitoriApiServiceImpl;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.integration.fruitori.CreaEventoIntegrationTest.TestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmobusiness.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.FruitoreTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.UtenteTestbedService;

/**
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, TestConfig.class})
@Transactional()
public class CreaEventoIntegrationTest extends ParentIntegrationTest {

  private static final String TASK_RETURN_ID = "999999";

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      var mock = Mockito.mock(CosmoCmmnFeignClient.class);

      TaskResponse mresponse = new TaskResponse();
      mresponse.setId(TASK_RETURN_ID);
      when(mock.postTask(anyObject())).thenReturn(mresponse);

      return mock;
    }
  }

  @Autowired
  private FruitoreTestbedService fruitoreTestbedService;

  @Autowired
  private EnteTestbedService enteTestbedService;

  @Autowired
  private UtenteTestbedService utenteTestbedService;

  @Autowired
  private CosmoCmmnFeignClient mockCosmoCmmnFeignClient;

  private CosmoTEnte ente;

  private CosmoTUtente utente;

  @Override
  protected void autentica() {

    var fruitore = fruitoreTestbedService.creaFruitore();

    var client = ClientInfoDTO.builder().withCodice(fruitore.getApiManagerId())
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();

    ente = enteTestbedService.creaEnte();
    fruitoreTestbedService.associaAdEnte(fruitore, ente);

    utente = utenteTestbedService.creaUtente();
    utenteTestbedService.associaAdEnte(utente, ente);

    autentica(null, client);
  }

  Supplier<CreaEventoFruitoreRequest> requestBuilder = () -> {
    var body = new CreaEventoFruitoreRequest();
    body.setCodiceIpaEnte(ente.getCodiceIpa());
    body.setDescrizione("Descrizione lunga dell'evento creato da integration test");
    body.setDestinatario(utente.getCodiceFiscale());
    body.setScadenza(OffsetDateTime.now().plusYears(10));
    body.setTitolo("Titolo evento creato da integration test");
    return body;
  };

  private void resetMock() {
    reset(mockCosmoCmmnFeignClient);

    TaskResponse mresponse = new TaskResponse();
    mresponse.setId(TASK_RETURN_ID);
    when(mockCosmoCmmnFeignClient.postTask(anyObject())).thenReturn(mresponse);
  }

  @Test
  public void shouldExecute() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var requestBody = requestBuilder.get();

    var response = api.postFruitoriEvento(null, null, requestBody, null);
    assertNotNull(response);

    var responseEntity = (CreaEventoFruitoreResponse) response.getEntity();
    assertFalse(StringUtils.isBlank(responseEntity.getIdEvento()));

    assertEquals(TASK_RETURN_ID, responseEntity.getIdEvento());
  }

  @Test
  public void shouldRequireAllFields() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setCodiceIpaEnte(value);
      api.postFruitoriEvento(null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("codiceIpaEnte"))));

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setDescrizione(value);
      api.postFruitoriEvento(null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("descrizione"))));

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setDestinatario(value);
      api.postFruitoriEvento(null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("destinatario"))));

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setTitolo(value);
      api.postFruitoriEvento(null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("titolo"))));

    expect(() -> {
      var body = requestBuilder.get();
      body.setScadenza(null);
      api.postFruitoriEvento(null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("scadenza")));
  }

  @Test
  public void shouldWrapFeignCallExceptions() {
    resetMock();

    Exception feignExc =
        new FeignClientServerErrorException(null,
            new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.postTask(anyObject()))
    .thenThrow(feignExc);

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var requestBody = requestBuilder.get();
    requestBody.setTitolo("error");

    expect(() -> api.postFruitoriEvento(null, null, requestBody, null),
        InternalServerException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("imprevisto".toUpperCase())));
  }

  @Test
  public void shouldFailOnMissingCodiceEnte() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var requestBody = requestBuilder.get();
    requestBody.setCodiceIpaEnte("MISSINGENTE");

    expect(() -> api.postFruitoriEvento(null, null, requestBody, null), ForbiddenException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("ente".toUpperCase())));
  }

  @Test
  public void shouldFailOnMissingUser() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var requestBody = requestBuilder.get();
    requestBody.setDestinatario("MISSINGUSER");

    expect(() -> api.postFruitoriEvento(null, null, requestBody, null), NotFoundException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("utente".toUpperCase())));
  }

  @Test
  public void shouldFailOnUserNotAssociated() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);
    assertNotNull(api);

    var altroUtente = utenteTestbedService.creaUtente();

    var requestBody = requestBuilder.get();
    requestBody.setDestinatario(altroUtente.getCodiceFiscale());

    expect(() -> api.postFruitoriEvento(null, null, requestBody, null), BadRequestException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("assoc".toUpperCase())));

    // should work when associated without other changes
    utenteTestbedService.associaAdEnte(altroUtente, ente);

    var response = api.postFruitoriEvento(null, null, requestBody, null);
    assertNotNull(response);
    var responseEntity = (CreaEventoFruitoreResponse) response.getEntity();
    assertFalse(StringUtils.isBlank(responseEntity.getIdEvento()));
    assertEquals(TASK_RETURN_ID, responseEntity.getIdEvento());
  }

}
