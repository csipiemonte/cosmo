/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.fruitori;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
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
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.AggiornaEventoFruitoreResponse;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.util.CommonUtils;
import it.csi.test.cosmo.cosmobusiness.integration.fruitori.AggiornaEventoIntegrationTest.TestConfig;
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
public class AggiornaEventoIntegrationTest extends ParentIntegrationTest {

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

  private TaskResponse existingTask;

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

  Supplier<AggiornaEventoFruitoreRequest> requestBuilder = () -> {
    var body = new AggiornaEventoFruitoreRequest();
    body.setCodiceIpaEnte(ente.getCodiceIpa());
    body.setNuovaDescrizione("Descrizione lunga dell'evento creato da integration test");
    body.setNuovaDataScadenza(OffsetDateTime.now().plusYears(10));
    body.setNuovoTitolo("Titolo evento creato da integration test");
    body.setEseguito(false);
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

    existingTask = mresponse;

    when(mockCosmoCmmnFeignClient.getTaskId(TASK_ID)).thenReturn(mresponse);
  }

  @Test
  public void shouldUpdateEventData() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setNuovaDescrizione("Descrizione lunga dell'evento creato da integration test - "
        + UUID.randomUUID().toString());
    requestBody.setNuovaDataScadenza(OffsetDateTime.now().plusYears(3));
    requestBody.setNuovoTitolo(
        "Titolo evento creato da integration test - " + UUID.randomUUID().toString());

    var response = api.putFruitoriEvento(TASK_ID, null, null, requestBody, null);
    assertNotNull(response);

    var responseEntity = (AggiornaEventoFruitoreResponse) response.getEntity();
    assertFalse(StringUtils.isBlank(responseEntity.getMessaggio()));

    assertTrue(responseEntity.getMessaggio().contains("aggiorna"));

    verify(mockCosmoCmmnFeignClient, times(1)).putTask(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith ->
        StringUtils.isBlank(calledWith.getAction()) &&
        Objects.equals(calledWith.getName(), requestBody.getNuovoTitolo()) &&
        Objects.equals(calledWith.getDescription(), requestBody.getNuovaDescrizione()) &&
        Objects.equals(calledWith.getDueDate(), requestBody.getNuovaDataScadenza().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
            )));
  }

  @Test
  public void shouldReuseCachedDataWhenNotProvided() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody1 = requestBuilder.get();
    requestBody1.setNuovoTitolo(null);
    requestBody1.setNuovaDataScadenza(null);
    requestBody1.setNuovaDescrizione("Descrizione lunga dell'evento creato da integration test - "
        + UUID.randomUUID().toString());

    api.putFruitoriEvento(TASK_ID, null, null, requestBody1, null);

    verify(mockCosmoCmmnFeignClient, times(1)).putTask(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith -> StringUtils.isBlank(calledWith.getAction())
            && Objects.equals(calledWith.getName(), existingTask.getName())
            && Objects.equals(calledWith.getDescription(), requestBody1.getNuovaDescrizione())
            && Objects.equals(calledWith.getDueDate(),
                CommonUtils.dateToISO(existingTask.getDueDate())))));

    resetMock();

    var requestBody2 = requestBuilder.get();
    requestBody2.setNuovaDescrizione(null);
    requestBody2.setNuovaDataScadenza(null);
    requestBody2.setNuovoTitolo(
        "Titolo evento creato da integration test - " + UUID.randomUUID().toString());
    api.putFruitoriEvento(TASK_ID, null, null, requestBody2, null);

    verify(mockCosmoCmmnFeignClient, times(1)).putTask(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith -> StringUtils.isBlank(calledWith.getAction())
            && Objects.equals(calledWith.getName(), requestBody2.getNuovoTitolo())
            && Objects.equals(calledWith.getDescription(), existingTask.getDescription())
            && Objects.equals(calledWith.getDueDate(),
                CommonUtils.dateToISO(existingTask.getDueDate())))));

    resetMock();

    var requestBody3 = requestBuilder.get();
    requestBody3.setNuovoTitolo(null);
    requestBody3.setNuovaDescrizione(null);
    requestBody3.setNuovaDataScadenza(OffsetDateTime.now().plusYears(3));
    api.putFruitoriEvento(TASK_ID, null, null, requestBody3, null);

    verify(mockCosmoCmmnFeignClient, times(1)).putTask(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith -> StringUtils.isBlank(calledWith.getAction())
            && Objects.equals(calledWith.getName(), existingTask.getName())
            && Objects.equals(calledWith.getDescription(), existingTask.getDescription())
            && Objects.equals(calledWith.getDueDate(), requestBody3.getNuovaDataScadenza()
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))));

  }

  @Test
  public void shouldMarkAsExecuted() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setEseguito(true);
    requestBody.setNuovaDataScadenza(null);
    requestBody.setNuovaDescrizione(null);
    requestBody.setNuovoTitolo(null);

    var response = api.putFruitoriEvento(TASK_ID, null, null, requestBody, null);
    assertNotNull(response);

    var responseEntity = (AggiornaEventoFruitoreResponse) response.getEntity();
    assertFalse(StringUtils.isBlank(responseEntity.getMessaggio()));

    assertTrue(responseEntity.getMessaggio().contains("marcato"));

    verify(mockCosmoCmmnFeignClient, times(1)).postTaskVariables(eq(TASK_ID),
        argThat(new LambdaMatcher<>(calledWith -> calledWith.size() == 1
        && calledWith.get(0).getName().equals("eseguito"))));
  }

  @Test
  public void shouldDoNothing() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setEseguito(null);
    requestBody.setNuovaDataScadenza(null);
    requestBody.setNuovaDescrizione(null);
    requestBody.setNuovoTitolo(null);

    var response = api.putFruitoriEvento(TASK_ID, null, null, requestBody, null);
    assertNotNull(response);

    var responseEntity = (AggiornaEventoFruitoreResponse) response.getEntity();
    assertFalse(StringUtils.isBlank(responseEntity.getMessaggio()));

    assertTrue(responseEntity.getMessaggio().contains("Nessuna operazione"));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldRequireAllFields() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    Arrays.asList(null, "").forEach(value -> expect(() -> {
      var body = requestBuilder.get();
      body.setCodiceIpaEnte(value);
      api.putFruitoriEvento(TASK_ID, null, null, body, null);
    }, BadRequestException.class, e -> assertTrue(e.getMessage().contains("codiceIpaEnte"))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldFailOnMissingCodiceEnte() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setCodiceIpaEnte("MISSINGENTE");

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        ForbiddenException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("ente".toUpperCase())));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldFailOnMissingTask() {
    resetMock();

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.putFruitoriEvento("3333", null, null, requestBody, null),
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

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
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

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
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

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
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

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class, e -> Arrays.asList("errore", "imprevisto", "ricerca")
        .forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));

    verify(mockCosmoCmmnFeignClient, never()).putTask(any(), any());
  }

  @Test
  public void shouldHandleFlowableConflictExceptionOnUpdateTask() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientClientErrorException(null,
        new HttpClientErrorException(HttpStatus.CONFLICT), null);

    when(mockCosmoCmmnFeignClient.putTask(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        ConflictException.class, e -> Arrays.asList("Operazione non consentita", "stato", "evento")
        .forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableServerExceptionOnUpdateTask() {
    resetMock();

    Exception feignExc = new FeignClientServerErrorException(null,
        new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.putTask(anyString(), anyObject())).thenThrow(feignExc);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setNuovoTitolo("error");

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> assertTrue(e.getMessage().toUpperCase().contains("interna".toUpperCase())));
  }

  @Test
  public void shouldHandleClientExceptionOnUpdateTask() {
    resetMock();

    // prepare mocks

    Throwable feignExc = new InvalidParameterException();

    when(mockCosmoCmmnFeignClient.putTask(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "imprevisto", "aggiornamento", "task").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableConflictExceptionOnMarkTaskAsExecuted() {
    resetMock();

    // prepare mocks

    Exception feignExc = new FeignClientClientErrorException(null,
        new HttpClientErrorException(HttpStatus.CONFLICT), null);

    when(mockCosmoCmmnFeignClient.postTaskVariables(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setEseguito(true);
    requestBody.setNuovaDataScadenza(null);
    requestBody.setNuovaDescrizione(null);
    requestBody.setNuovoTitolo(null);

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        ConflictException.class,
        e -> Arrays.asList("Operazione non consentita", "stato", "evento").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleClientExceptionOnMarkTaskAsExecuted() {
    resetMock();

    // prepare mocks

    Throwable feignExc = new InvalidParameterException();

    when(mockCosmoCmmnFeignClient.postTaskVariables(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    // run test
    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setEseguito(true);
    requestBody.setNuovaDataScadenza(null);
    requestBody.setNuovaDescrizione(null);
    requestBody.setNuovoTitolo(null);

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "imprevisto", "marcatura", "task").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

  @Test
  public void shouldHandleFlowableServerExceptionOnMarkTaskAsExecuted() {
    resetMock();

    Exception feignExc = new FeignClientServerErrorException(null,
        new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE), null);

    when(mockCosmoCmmnFeignClient.postTaskVariables(eq(TASK_ID), anyObject())).thenThrow(feignExc);

    var api = buildResource(FruitoriApiServiceImpl.class);

    var requestBody = requestBuilder.get();
    requestBody.setEseguito(true);
    requestBody.setNuovaDataScadenza(null);
    requestBody.setNuovaDescrizione(null);
    requestBody.setNuovoTitolo(null);

    expect(() -> api.putFruitoriEvento(TASK_ID, null, null, requestBody, null),
        InternalServerException.class,
        e -> Arrays.asList("errore", "interna", "marcatura", "task").forEach(
            token -> assertTrue(e.getMessage().toUpperCase().contains(token.toUpperCase()))));
  }

}
