/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.function.Supplier;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmobusiness.business.rest.impl.FruitoriApiServiceImpl;
import it.csi.cosmo.cosmobusiness.dto.rest.EliminaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.test.cosmo.cosmobusiness.integration.fruitori.EliminaEventoIntegrationTest.TestConfig;
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
public class EliminaEventoIntegrationTest extends ParentIntegrationTest {

  private static final String TASK_ID = "999999";

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoCmmnFeignClient cosmoCmmnFeignClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
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

    resetMock();
  }

  Supplier<EliminaEventoFruitoreRequest> requestBuilder = () -> {
    var body = new EliminaEventoFruitoreRequest();
    body.setCodiceIpaEnte(ente.getCodiceIpa());
    return body;
  };

  private void resetMock() {
    reset(mockCosmoCmmnFeignClient);

    TaskResponse mresponse = new TaskResponse();
    mresponse.setId(TASK_ID);
    mresponse.setAssignee(utente.getCodiceFiscale());
    mresponse.setName("Task name " + UUID.randomUUID().toString());
    mresponse.setDescription("Task description " + UUID.randomUUID().toString());
    mresponse.setDueDate(Date.from(Instant.now().plusSeconds((long) (Math.random() * 10000))));

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenReturn(mresponse);
  }

  @Test
  public void shouldMarkAsCompleted() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    var response = api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null);
    assertEquals(Integer.valueOf(204), Integer.valueOf(response.getStatus()));

    verify(mockCosmoCmmnFeignClient, times(1)).postTaskId(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith -> calledWith.getAction().equals("complete"))));
  }

  @Test
  public void shouldRequireAllFields() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setCodiceIpaEnte(value);
      api.deleteFruitoriEvento(TASK_ID, null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("codiceIpaEnte"))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldFailOnMissingCodiceEnte() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setCodiceIpaEnte("MISSINGENTE");

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        ForbiddenException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("ente".toUpperCase())));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldFailOnMissingTask() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento("3333", null, null, requestBody, null),
        NotFoundException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("task".toUpperCase())));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldFailWhenTaskHasNoAssignee() {
    resetMock();

    // prepare mocks
    TaskResponse mresponse = new TaskResponse();
    mresponse.setId(TASK_ID);
    mresponse.setAssignee(null);

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenReturn(mresponse);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        BadRequestException.class,
        e -> assertTrue(
            e.getMessage().toUpperCase().contains("Nessun assegnatario".toUpperCase())));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldHandleFlowableNotFoundOnGetTask() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientClientErrorException(null,
        new HttpClientErrorException(HttpStatus.NOT_FOUND), null);

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        NotFoundException.class, e -> Arrays.asList("task", "non", "trovato")
        .forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldHandleFlowableHttpExceptionOnGetTask() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientServerErrorException(null,
        new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "chiamata", "interna", "ricerca")
        .forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldHandleClientExceptionOnGetTask() {
    resetMock();

    // prepare mocks

    Throwable feignExc = new InvalidParameterException();

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class, e -> Arrays.asList("errore", "imprevisto", "ricerca")
        .forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldHandleFlowableConflictExceptionOnMarkTaskAsCompleted() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientClientErrorException(null,
        new HttpClientErrorException(HttpStatus.CONFLICT), null);

    when(mockCosmoCmmnFeignClient.postTaskId(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        ConflictException.class,
        e -> Arrays.asList("Operazione non consentita", "stato", "evento").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleClientExceptionOnMarkTaskAsCompleted() {
    resetMock();

    // prepare mocks

    Throwable feignExc = new InvalidParameterException();

    when(mockCosmoCmmnFeignClient.postTaskId(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "imprevisto", "marcatura", "task", "completato").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableServerExceptionOnMarkTaskAsCompleted() {
    resetMock();

    Exception feignExc = new FeignClientServerErrorException(null,
        new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.postTaskId(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.deleteFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "interna", "marcatura", "task", "completato").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

}
