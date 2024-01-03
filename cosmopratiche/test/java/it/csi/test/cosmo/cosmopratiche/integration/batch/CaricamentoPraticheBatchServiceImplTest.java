/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.integration.batch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
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
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmo.dto.rest.FileUploadResult;
import it.csi.cosmo.cosmobusiness.dto.rest.Processo;
import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticheBatchService;
import it.csi.cosmo.cosmopratiche.integration.repository.CosmoTCaricamentoPraticaRepository;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoEcmDocumentiFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoFileUploadFeignClient;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoProcessoFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, CaricamentoPraticheBatchServiceImplTest.CaricamentoPraticheBatchServiceIntegrationTConfig.class})
@Transactional
public class CaricamentoPraticheBatchServiceImplTest {
  
  @Configuration
  public static class CaricamentoPraticheBatchServiceIntegrationTConfig {

    @Bean
    @Primary
    public CosmoProcessoFeignClient cosmoProcessoFeignClient() {
      return Mockito.mock(CosmoProcessoFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient() {
      return Mockito.mock(CosmoBusinessPraticheFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoFileUploadFeignClient cosmoFileUploadFeignClient() {
      return Mockito.mock(CosmoFileUploadFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient() {
      return Mockito.mock(CosmoEcmDocumentiFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoProcessoFeignClient cosmoProcessoFeignClient;
  
  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;
  
  @Autowired
  private CosmoFileUploadFeignClient cosmoFileUploadFeignClient;
  
  @Autowired
  private CosmoEcmDocumentiFeignClient cosmoEcmDocumentiFeignClient;
  
  @Autowired
  private CaricamentoPraticheBatchService caricamentoPraticheBatchService;
  
  @Autowired
  private CosmoTCaricamentoPraticaRepository caricamentoPraticaRepository;
  
  @Test
  public void getCaricamentoPraticheWithCaricamentoCompletato() {
    List<CosmoTCaricamentoPratica> listaCaricamento = caricamentoPraticheBatchService.getCaricamentoPraticheWithCaricamentoCompletato();
    assertNotNull(listaCaricamento);
    assertNotNull(listaCaricamento.get(0));
    assertTrue(listaCaricamento.get(0).getId() == 6);
  }
  
  @Test(expected = BadRequestException.class)
  public void elaboraCosmoTCaricamentoPraticaFileNotFound() {
    CosmoTCaricamentoPratica caricamentoPratica = new CosmoTCaricamentoPratica();
    caricamentoPratica.setPathFile("prova");
    caricamentoPratica.setNomeFile("nomefile");
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica );
  }
  
  @Test(expected = BadRequestException.class)
  public void elaboraCosmoTCaricamentoPraticaFileVuoto() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(8L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test(expected = IllegalStateException.class)
  public void elaboraCosmoTCaricamentoPraticaIdentificativoPraticaErrato() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(10L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaValidazioneRigheFallita() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(9L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErroreTag() {
    setupMock();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(11L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErroreTagErrorEcmGetDocumento() {
    setupMockEcmGetDocumentoThrowException();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(11L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErroreTagErrorEcmGetDocumentoNull() {
    setupMockEcmGetDocumentoNull();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(11L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErroreTagErrorEcmPostDocumento() {
    setupMockEcmPostDocumentoThrowException();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(11L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica);
  }

  @Test
  public void registraErroreGenerico() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(9L);
    caricamentoPraticheBatchService.registraErroreGenerico(caricamentoPratica, "error");
  }
  
  @Test
  public void iniziaElaborazione() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(9L);
    caricamentoPraticheBatchService.iniziaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void completaElaborazionePraticaInErrore() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(7L);
    caricamentoPraticheBatchService.completaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void completaElaborazionePraticaCreataConErrore() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(8L);
    caricamentoPraticheBatchService.completaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void completaElaborazionePraticaProcessoInErrore() {
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(9L);
    caricamentoPraticheBatchService.completaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void completaElaborazionePraticaDeleteSuccess() {
    setupMockCompletaElaborazione();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(4L);
    caricamentoPraticheBatchService.completaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void completaElaborazionePraticaDeleteInErrore() {
    setupMockCompletaElaborazioneThrowException();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(4L);
    caricamentoPraticheBatchService.completaElaborazione(caricamentoPratica);
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPratica() {
    setupMock();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(7L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica );
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErroreProcesso() {
    setupMockWithExceptionProcessoFeignClient();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(7L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica );
  }
  
  @Test
  public void elaboraCosmoTCaricamentoPraticaErrorePutVariabili() {
    setupMockWithExceptionBusinessFeignClient();
    CosmoTCaricamentoPratica caricamentoPratica = caricamentoPraticaRepository.findOne(7L);
    caricamentoPraticheBatchService.elaboraCosmoTCaricamentoPratica(caricamentoPratica );
  }
  
  @SuppressWarnings("unchecked")
  private void setupMockEcmPostDocumentoThrowException() {
    reset(cosmoProcessoFeignClient);
    Processo processo = new Processo();
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenReturn(processo);
  
    reset(cosmoBusinessPraticheFeignClient);
    doNothing().when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
    
    reset(cosmoFileUploadFeignClient);
    FileUploadResult fileUploadResult = new FileUploadResult();
    fileUploadResult.setUploadUUID("provaUUID");
    when(cosmoFileUploadFeignClient.postFilePraticheUnzipFile(any())).thenReturn(fileUploadResult);
    
    reset(cosmoEcmDocumentiFeignClient);
    DocumentiResponse documentiResponse = new DocumentiResponse();
    List<Documento> listaDoc = new ArrayList<>();
    Documento doc = new Documento();
    doc.setId(2L);
    listaDoc.add(doc);
    documentiResponse.setDocumenti(listaDoc);
    when(cosmoEcmDocumentiFeignClient.getDocumento(any(), any())).thenReturn(documentiResponse);
    when(cosmoEcmDocumentiFeignClient.postDocumento(any(), any())).thenThrow(Exception.class);
  }
  
  private void setupMockEcmGetDocumentoNull() {
    reset(cosmoProcessoFeignClient);
    Processo processo = new Processo();
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenReturn(processo);
  
    reset(cosmoBusinessPraticheFeignClient);
    doNothing().when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
    
    reset(cosmoFileUploadFeignClient);
    FileUploadResult fileUploadResult = new FileUploadResult();
    fileUploadResult.setUploadUUID("provaUUID");
    when(cosmoFileUploadFeignClient.postFilePraticheUnzipFile(any())).thenReturn(fileUploadResult);
    
    reset(cosmoEcmDocumentiFeignClient);
    when(cosmoEcmDocumentiFeignClient.getDocumento(any(), any())).thenReturn(null);
    
  }
  
  @SuppressWarnings("unchecked")
  private void setupMockEcmGetDocumentoThrowException() {
    reset(cosmoProcessoFeignClient);
    Processo processo = new Processo();
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenReturn(processo);
  
    reset(cosmoBusinessPraticheFeignClient);
    doNothing().when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
    
    reset(cosmoFileUploadFeignClient);
    FileUploadResult fileUploadResult = new FileUploadResult();
    fileUploadResult.setUploadUUID("provaUUID");
    when(cosmoFileUploadFeignClient.postFilePraticheUnzipFile(any())).thenReturn(fileUploadResult);
    
    reset(cosmoEcmDocumentiFeignClient);
    when(cosmoEcmDocumentiFeignClient.getDocumento(any(), any())).thenThrow(Exception.class);
    
  }
  
  private void setupMockWithExceptionBusinessFeignClient() {
    reset(cosmoProcessoFeignClient);
    Processo processo = new Processo();
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenReturn(processo);
  
    reset(cosmoBusinessPraticheFeignClient);
    doThrow(Exception.class).when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
  }

  @SuppressWarnings("unchecked")
  private void setupMockWithExceptionProcessoFeignClient() {
    reset(cosmoProcessoFeignClient);
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenThrow(Exception.class);
  }

  private void setupMock() {
    reset(cosmoProcessoFeignClient);
    Processo processo = new Processo();
    when(cosmoProcessoFeignClient.postAvviaProcessoIdPratica(any())).thenReturn(processo);
  
    reset(cosmoBusinessPraticheFeignClient);
    doNothing().when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
    
    reset(cosmoFileUploadFeignClient);
    FileUploadResult fileUploadResult = new FileUploadResult();
    fileUploadResult.setUploadUUID("provaUUID");
    when(cosmoFileUploadFeignClient.postFilePraticheUnzipFile(any())).thenReturn(fileUploadResult);
    
    reset(cosmoEcmDocumentiFeignClient);
    DocumentiResponse documentiResponse = new DocumentiResponse();
    List<Documento> listaDoc = new ArrayList<>();
    Documento doc = new Documento();
    doc.setId(2L);
    listaDoc.add(doc);
    documentiResponse.setDocumenti(listaDoc);
    when(cosmoEcmDocumentiFeignClient.getDocumento(any(), any())).thenReturn(documentiResponse);
    Documenti documenti = new Documenti();
    List<Documento> listaDocumenti = new ArrayList<>();
    Documento documento = new Documento();
    documento.setId(1L);
    listaDocumenti.add(documento);
    documenti.setDocumenti(listaDocumenti);
    when(cosmoEcmDocumentiFeignClient.postDocumento(any(), any())).thenReturn(documenti);
  }
  
  private void setupMockCompletaElaborazione() {
    reset(cosmoFileUploadFeignClient);
    doNothing().when(cosmoFileUploadFeignClient).deleteFilePratiche(any());
  }
  
  private void setupMockCompletaElaborazioneThrowException() {
    reset(cosmoFileUploadFeignClient);
    doThrow(Exception.class).when(cosmoFileUploadFeignClient).deleteFilePratiche(any());
  }
}
