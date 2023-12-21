/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.stardas;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.flowable.rest.service.api.runtime.process.ExecutionResponse;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientStatusCodeException;
import it.csi.cosmo.cosmobusiness.business.service.StardasCallbackService;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitiStepType;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumento;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoSmistaDocumentoType;
import it.csi.cosmo.cosmobusiness.dto.rest.EsitoStep;
import it.csi.cosmo.cosmobusiness.dto.rest.ExecutionWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioneType;
import it.csi.cosmo.cosmobusiness.dto.rest.InformazioniAggiuntiveType;
import it.csi.cosmo.cosmobusiness.dto.rest.ProcessInstanceWrapper;
import it.csi.cosmo.cosmobusiness.dto.rest.ResultType;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmoecm.dto.rest.Esito;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoSmistamentoResponse;
import it.csi.test.cosmo.cosmobusiness.integration.stardas.CallbackTest.TestConfig;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;
/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, TestConfig.class})
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class CallbackTest extends ParentIntegrationTest {

  @Autowired
  private StardasCallbackService callbackService;

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient() {
      return Mockito.mock(CosmoEcmDocumentiFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoCmmnFeignClient cmmnClient() {
      return Mockito.mock(CosmoCmmnFeignClient.class);
    }
  }

  private void resetMock() {
    reset(mockCosmoEcmDocumentiFeignClient);

    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any()))
    .thenReturn(null);
  }

  @Autowired
  private CosmoEcmDocumentiFeignClient mockCosmoEcmDocumentiFeignClient;

  @Autowired
  private CosmoCmmnFeignClient cmmnClient;
  
  private static final String EVENTO = "evento";
 
  @Test
  public void inserisciCallback() {
    resetMock();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test(expected = InternalServerException.class)
  public void inserisciEsitoSmistaDocumentoIdentificativoNull() {
    setupMockEcmIdentificativoNull();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test(expected = InternalServerException.class)
  public void inserisciEsitoSmistaDocumentoProcessoMultiplo() {
    setupMockEcmProcessoMultiplo();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test(expected = InternalServerException.class)
  public void inserisciEsitoSmistaDocumentoExecutionsNull() {
    setupMockEcmExecutionsNull();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test
  public void inserisciEsitoSmistaDocumento() {
    setupMockEcmOk();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test(expected = InternalServerException.class)
  public void inserisciEsitoSmistaDocumentoSenzaProcesso() {
    setupMockEcmBusinessKeyNull();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test(expected = FeignClientServerErrorException.class)
  public void inserisciEsitoSmistaDocumentoFeignClientServerErrorException() {
    setupMockEcmFeignClientServerErrorException();
    callbackService.inserisciEsitoSmistaDocumento(setParametri());
  }
  
  @Test
  public void aggiornaEsitoSmistaDocumento() {
    setupMockEcmPutOk();
    callbackService.aggiornaEsitoSmistaDocumento(setParametri());
  }

  @Test(expected = FeignClientServerErrorException.class)
  public void aggiornaEsitoSmistaDocumentoFeignClientServerErrorException() {
    setupMockEcmPutFeignClientServerErrorException();
    callbackService.aggiornaEsitoSmistaDocumento(setParametri());
  }
  
  private void setupMockEcmPutFeignClientServerErrorException() {
    reset(mockCosmoEcmDocumentiFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientServerErrorException exception = new FeignClientServerErrorException(null, new HttpServerErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(mockCosmoEcmDocumentiFeignClient.putDocumentiIdDocumentoSmistamento(any(), any())).thenThrow(exception);
    
  }
  
  private void setupMockEcmPutOk() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    response.setIdentificativoEvento(EVENTO);
    when(mockCosmoEcmDocumentiFeignClient.putDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response );
    
    reset(cmmnClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();
    instanceResponse.setId("1");
    data.add(instanceResponse);
    wrapper.setData(data);
    wrapper.setTotal(1L);
    when(cmmnClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    ExecutionWrapper wrapperExecutions = new ExecutionWrapper();
    List<ExecutionResponse> executionInstanceResponse = new ArrayList<>();
    ExecutionResponse executionResponse = new ExecutionResponse();
    executionResponse.setId("1");
    executionInstanceResponse.add(executionResponse );
    wrapperExecutions.setData(executionInstanceResponse);
    when(cmmnClient.getExecutions(any(), any())).thenReturn(wrapperExecutions);
    ExecutionWrapper putWrapper = new ExecutionWrapper();
    when(cmmnClient.putExecution(any(), any())).thenReturn(putWrapper);
    
  }

  private void setupMockEcmFeignClientServerErrorException() {
    reset(mockCosmoEcmDocumentiFeignClient);
    ErrorMessageDTO errorMessage = new ErrorMessageDTO();
    FeignClientServerErrorException exception = new FeignClientServerErrorException(null, new HttpServerErrorException(HttpStatus.NOT_FOUND), errorMessage);
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenThrow(exception);
  }

  private void setupMockEcmBusinessKeyNull() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    response.setIdentificativoEvento(EVENTO);
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response);
    
    reset(cmmnClient);
    when(cmmnClient.getProcessInstancesByBusinessKey(any())).thenReturn(null);
  }

  private void setupMockEcmOk() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    response.setIdentificativoEvento(EVENTO);
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response );
    
    reset(cmmnClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();
    instanceResponse.setId("1");
    data.add(instanceResponse);
    wrapper.setData(data);
    wrapper.setTotal(1L);
    when(cmmnClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    ExecutionWrapper wrapperExecutions = new ExecutionWrapper();
    List<ExecutionResponse> executionInstanceResponse = new ArrayList<>();
    ExecutionResponse executionResponse = new ExecutionResponse();
    executionResponse.setId("1");
    executionInstanceResponse.add(executionResponse );
    wrapperExecutions.setData(executionInstanceResponse);
    when(cmmnClient.getExecutions(any(), any())).thenReturn(wrapperExecutions);
    ExecutionWrapper putWrapper = new ExecutionWrapper();
    when(cmmnClient.putExecution(any(), any())).thenReturn(putWrapper);
  }

  private void setupMockEcmExecutionsNull() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    response.setIdentificativoEvento(EVENTO);
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response );
    
    reset(cmmnClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    ProcessInstanceResponse instanceResponse = new ProcessInstanceResponse();
    instanceResponse.setId("1");
    data.add(instanceResponse );
    wrapper.setData(data);
    wrapper.setTotal(1L);
    when(cmmnClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper);
    when(cmmnClient.getExecutions(any(), any())).thenReturn(null);
  }

  private void setupMockEcmProcessoMultiplo() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    response.setIdentificativoEvento(EVENTO);
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response );
    
    reset(cmmnClient);
    ProcessInstanceWrapper wrapper = new ProcessInstanceWrapper();
    List<ProcessInstanceResponse> data = new ArrayList<>();
    wrapper.setData(data);
    wrapper.setTotal(2L);
    when(cmmnClient.getProcessInstancesByBusinessKey(any())).thenReturn(wrapper );
  }
  
  private void setupMockEcmIdentificativoNull() {
    reset(mockCosmoEcmDocumentiFeignClient);
    EsitoSmistamentoResponse response = new EsitoSmistamentoResponse();
    Esito esito = new Esito();
    esito.setCode("000");
    response.setEsito(esito);
    response.setNumDocumentiDaSmistare(1);
    response.setNumDocumentiSmistatiCorrettamente(1);
    response.setNumDocumentiSmistatiInErrore(0);
    response.setIdPratica("1");
    when(mockCosmoEcmDocumentiFeignClient.postDocumentiIdDocumentoSmistamento(any(), any())).thenReturn(response );
  }
  
  private EsitoSmistaDocumentoRequest setParametri() {
    EsitoSmistaDocumentoRequest request = new EsitoSmistaDocumentoRequest();
    var esitoSmistaDocumento = new EsitoSmistaDocumento();
    EsitoSmistaDocumentoType esitoType = new EsitoSmistaDocumentoType();

    InformazioniAggiuntiveType infoAggType = new InformazioniAggiuntiveType();

    InformazioneType info1 = new InformazioneType();
    info1.setNome("Nome info 1");
    info1.setValore("Valore info 1");

    var infoList = new ArrayList<InformazioneType>();
    infoList.add(info1);
    infoAggType.setInformazione(infoList);

    esitoType.setInformazioniAggiuntive(infoAggType);

    esitoType.setIdDocumentoFruitore("378");
    esitoType.setMessageUUID("TEST_UUID");
    esitoType.setTipoTrattamento("TIPO_TRATTAMENTO");

    ResultType esitoTrattamento = new ResultType();
    esitoTrattamento.setCodice("000");
    esitoTrattamento.setMessaggio("Operazione completata con esito positivo");
    esitoType.setEsitoTrattamento(esitoTrattamento);

    EsitiStepType esitiStep = new EsitiStepType();
    var esitoStepList = new ArrayList<EsitoStep>();

    var esitoStep1 = new EsitoStep();
    esitoStep1.setNome("STEP 1");
    var resultType1 = new ResultType();
    resultType1.setCodice("000");
    resultType1.setMessaggio("OK 1");
    esitoStep1.setEsito(resultType1);
    esitoStepList.add(esitoStep1);

    var esitoStep2 = new EsitoStep();
    esitoStep2.setNome("STEP 2");
    var resultType2 = new ResultType();
    resultType2.setCodice("000");
    resultType2.setMessaggio("OK 2");
    esitoStep2.setEsito(resultType2);
    esitoStepList.add(esitoStep2);

    esitiStep.setEsitoStep(esitoStepList);
    esitoType.setEsitiStep(esitiStep);
    esitoSmistaDocumento.setEsito(esitoType);

    request.setEsitoSmistaDocumento(esitoSmistaDocumento);
    return request;
  }
}
