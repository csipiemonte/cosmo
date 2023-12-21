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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.util.ValoreParametroFormLogicoWrapper;
import it.csi.cosmo.cosmobusiness.business.service.AttivazioneSistemaEsternoService;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;

/**
*
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class, AttivazioneSistemaEsternoServiceImplTest.AttivazioneSistemaEsternoServiceTestConfig.class})
@Transactional
public class AttivazioneSistemaEsternoServiceImplTest {
  
  @Configuration
  public static class AttivazioneSistemaEsternoServiceTestConfig {
    
    @Bean
    @Primary
    public IstanzaFormLogiciService istanzaFormLogiciService() {
      return Mockito.mock(IstanzaFormLogiciService.class);
    }
  }
  
  @Autowired
  private AttivazioneSistemaEsternoService attivazioneSistemaEsternoService;
  
  @Autowired
  private IstanzaFormLogiciService istanzaFormLogiciService;
  
  @Test(expected = NotFoundException.class)
  public void inviaAttivazioneSistemaEsternoPraticaNotFound() {
    attivazioneSistemaEsternoService.inviaAttivazioneSistemaEsterno(111L, 1L);
  }
  
  @Test(expected = NotFoundException.class)
  public void inviaAttivazioneSistemaEsternoAttivitaNotFound() {
    attivazioneSistemaEsternoService.inviaAttivazioneSistemaEsterno(1L, 111L);
  }
  
  @Test(expected = NullPointerException.class)
  public void inviaAttivazioneSistemaEsterno() {
    setupMock();
    attivazioneSistemaEsternoService.inviaAttivazioneSistemaEsterno(1L, 1L);
  }
  
  @Test(expected = NullPointerException.class)
  public void getPayloadAttivazioneSistemaEsterno() {
    attivazioneSistemaEsternoService.getPayloadAttivazioneSistemaEsterno(1L, 1L);
  }

  private void setupMock() {
    reset(istanzaFormLogiciService);
    CosmoTIstanzaFunzionalitaFormLogico value = new CosmoTIstanzaFunzionalitaFormLogico();
    when(istanzaFormLogiciService.ricercaIstanzaAttiva(any(), any())).thenReturn(value);
    ValoreParametroFormLogicoWrapper wrapper = null;
    when(istanzaFormLogiciService.requireValoreParametro(any(), any())).thenReturn(wrapper);
    CosmoTIstanzaFunzionalitaFormLogico istanza = new CosmoTIstanzaFunzionalitaFormLogico();
    when(istanzaFormLogiciService.ricercaIstanzaAttiva(any(), any())).thenReturn(istanza);
  }
}