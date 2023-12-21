/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import it.csi.cosmo.common.dto.common.ErrorMessageDTO;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.feignclient.exception.FeignClientClientErrorException;
import it.csi.cosmo.common.feignclient.exception.FeignClientServerErrorException;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional
public class ChiamataEsternaServiceImplTest {
  
  @Autowired
  private ChiamataEsternaService chiamataEsternaService;
  
  @Test(expected = NotFoundException.class)
  public void inviaChiamataEsternaDaProcessoPraticaNotFound() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    chiamataEsternaService.inviaChiamataEsternaDaProcesso(111L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void inviaChiamataEsternaDaProcessoFruitoreNotFound() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    body.setCodiceFruitore("prova111");
    chiamataEsternaService.inviaChiamataEsternaDaProcesso(1L, body);
  }
  
  @Test(expected = InternalServerException.class)
  public void inviaChiamataEsternaDaProcessoFruitoreNonAssociato() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    body.setCodiceEndpoint("endpoint");
    chiamataEsternaService.inviaChiamataEsternaDaProcesso(4L, body);
  }
  
  @Test(expected = InternalServerException.class)
  public void inviaChiamataEsternaDaProcessoEndpointNotFound() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    body.setCodiceEndpoint("endpoint");
    chiamataEsternaService.inviaChiamataEsternaDaProcesso(1L, body);
  } 
  
  @Test
  public void inviaChiamataEsternaDaProcessoSenzaEndpoint() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    body.setCodiceFruitore("FruitoreTest1");
    chiamataEsternaService.inviaChiamataEsternaDaProcesso(1L, body);
  } 
  
  @Test
  public void inviaChiamataEsternaDaProcessoMappaturaNonJson() {
    InviaChiamataEsternaRequest body = new InviaChiamataEsternaRequest();
    body.setCodiceEndpoint("CUSTOM");
    body.setCodiceFruitore("FruitoreTest1");
    body.setRestituisceJson(Boolean.FALSE);
    body.setUrl("http://www.fruitore1.it");
    body.setMetodo("GET");
    body.setMappaturaRequestBody("{\"datoinviato\": \"Esempio di dato inviato\"}");
    body.setMappaturaQuery("{\"datoinviato\": \"Esempio di dato inviato\"}");
    RiferimentoOperazioneAsincrona response = chiamataEsternaService.inviaChiamataEsternaDaProcesso(2L, body);
    assertNotNull(response);
  } 
  
  @Test
  public void getDescrizioneErroreChiamataEsternaFeignClientClientErrorException() {
    FeignClientClientErrorException exception = new FeignClientClientErrorException(null, new HttpClientErrorException(HttpStatus.HTTP_VERSION_NOT_SUPPORTED), new ErrorMessageDTO());
    chiamataEsternaService.getDescrizioneErroreChiamataEsterna(exception);
  }
  
  @Test
  public void getDescrizioneErroreChiamataEsternaFeignClientServerErrorException() {
    FeignClientServerErrorException exception = new FeignClientServerErrorException(null, new HttpServerErrorException(HttpStatus.CONFLICT), new ErrorMessageDTO());
    chiamataEsternaService.getDescrizioneErroreChiamataEsterna(exception);
  }
  
  @Test
  public void getDescrizioneErroreChiamataEsternaHttpStatusCodeException() {
    HttpStatusCodeException exception = new HttpStatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR) {};
    chiamataEsternaService.getDescrizioneErroreChiamataEsterna(exception);
  }
  
  @Test
  public void getDescrizioneErroreChiamataEsternaBadRequestException() {
    BadRequestException exception = new BadRequestException();
    chiamataEsternaService.getDescrizioneErroreChiamataEsterna(exception);
  }
  
  @Test(expected = NotFoundException.class)
  public void testSchemaAutenticazioneNotFound() {
    chiamataEsternaService.testSchemaAutenticazione(111L);
  }
  
  @Test(expected = BadRequestException.class)
  public void testSchemaAutenticazioneTipoNonToken() {
    chiamataEsternaService.testSchemaAutenticazione(1L);
  }
  
  @Test
  public void testSchemaAutenticazioneSenzaCredenziali() {
    chiamataEsternaService.testSchemaAutenticazione(5L);
  }
  
  @Test
  public void testSchemaAutenticazioneConCredenzialiMultiple() {
    chiamataEsternaService.testSchemaAutenticazione(6L);
  }
  
  @Test
  public void testSchemaAutenticazioneConClientIdNull() {
    chiamataEsternaService.testSchemaAutenticazione(7L);
  }
  
  @Test
  public void testSchemaAutenticazione() {
    chiamataEsternaService.testSchemaAutenticazione(8L);
  }
}