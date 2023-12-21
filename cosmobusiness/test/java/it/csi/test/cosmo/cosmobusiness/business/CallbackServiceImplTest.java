/*
* Copyright CSI-Piemonte - 2023
* SPDX-License-Identifier: GPL-3.0-or-later
*/
 
package it.csi.test.cosmo.cosmobusiness.business;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.business.service.ChiamataEsternaService;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, CallbackServiceImplTest.CallbackServiceTestConfig.class})
@Transactional
public class CallbackServiceImplTest {
  
  @Configuration
  public static class CallbackServiceTestConfig {
    
    @Bean
    @Primary
    public ChiamataEsternaService chiamataEsternaService() {
      return Mockito.mock(ChiamataEsternaService.class);
    }
  }

  @Autowired
  private CallbackService callbackService;
  
  @Autowired
  private ChiamataEsternaService chiamataEsternaService;
  
  @Test
  public void inviaSincrono() {
    setupMockChiamataEsterna();
    callbackService.inviaSincrono(OperazioneFruitore.CUSTOM, 2L, null, null, null);
  }
  
  @Test(expected = InternalServerException.class)
  public void inviaSincronoConEndpointNonRest() {
    callbackService.inviaSincrono(OperazioneFruitore.CUSTOM, 3L, "{\"esiti\":[]}", null, "segnale");
  }
  
  @Test(expected = HttpStatusCodeException.class)
  public void inviaSincronoConErroreChiamataEsterna() {
    setupMockChiamataEsternaInErrore();
    callbackService.inviaSincrono(OperazioneFruitore.CUSTOM, 2L, null, null, null);
  }
  
  @Test
  public void schedulaInvioAsincrono() {
    callbackService.schedulaInvioAsincrono(OperazioneFruitore.CUSTOM, 2L, null, null, null);
  }
  
  @Test(expected = NotFoundException.class)
  public void annullaCallbackSchedulatoNotFound() {
    callbackService.annullaCallbackSchedulato(111L);
  }
  
  @Test
  public void annullaCallbackRischedulato() {
    callbackService.annullaCallbackSchedulato(1L);
  }
  
  @Test
  public void annullaCallbackSchedulato() {
    callbackService.annullaCallbackSchedulato(2L);
  }
  
  @Test
  public void annullaCallbackInCorso() {
    callbackService.annullaCallbackSchedulato(3L);
  }
  
  @Test(expected = ConflictException.class)
  public void annullaCallbackAnnullato() {
    callbackService.annullaCallbackSchedulato(4L);
  }
  
  @Test(expected = NotFoundException.class)
  public void tentaInvioCallbackSchedulatoNotFound() {
    callbackService.tentaInvioCallbackSchedulato(111L);
  }
  
  @Test
  public void tentaInvioCallbackSchedulatoRischedulato() {
    callbackService.tentaInvioCallbackSchedulato(1L);
  }
  
  @Test
  public void tentaInvioCallbackSchedulato() {
    callbackService.tentaInvioCallbackSchedulato(2L);
  }
  
  @Test
  public void tentaInvioCallbackSchedulatoInCorso() {
    callbackService.tentaInvioCallbackSchedulato(3L);
  }
  
  @Test(expected = ConflictException.class)
  public void tentaInvioCallbackSchedulatoAnnullato() {
    callbackService.tentaInvioCallbackSchedulato(4L);
  }
  
  private void setupMockChiamataEsternaInErrore() {
    reset(chiamataEsternaService);
    HttpStatusCodeException exception = new HttpStatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR) {};
    when(chiamataEsternaService.inviaChiamataEsterna(any())).thenThrow(exception);
  }
  
  private void setupMockChiamataEsterna() {
    reset(chiamataEsternaService);
    ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
    when(chiamataEsternaService.inviaChiamataEsterna(any())).thenReturn(response);
  }
  
}