/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
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
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmopratiche.business.service.CondivisioniPraticheService;
import it.csi.cosmo.cosmopratiche.dto.rest.CondivisionePratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaCondivisionePraticaRequest;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, CondivisioniPraticheServiceImplTest.CondivisioniPraticheServiceIntegrationTConfig.class})
@Transactional
public class CondivisioniPraticheServiceImplTest extends ParentIntegrationTest{

  @Configuration
  public static class CondivisioniPraticheServiceIntegrationTConfig {

    @Bean
    @Primary
    public CosmoNotificationsNotificheFeignClient cosmoNotificationsNotificheFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoNotificationsNotificheFeignClient cosmoNotificationsNotificheFeignClient;
  
  @Autowired
  private CondivisioniPraticheService condivisioniPraticheService;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaCondivisioneSenzaUtenteOGruppo() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    condivisioniPraticheService.creaCondivisione(1L, body);
  }
  
  @Test(expected = ConflictException.class)
  public void creaCondivisioneEsistente() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdGruppo(1L);
    condivisioniPraticheService.creaCondivisione(1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void creaCondivisionePraticaNotFound() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdUtente(1L);
    condivisioniPraticheService.creaCondivisione(123L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void creaCondivisionePraticaConUtenteNotFound() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdUtente(111L);
    condivisioniPraticheService.creaCondivisione(1L, body);
  }
  
  @Test(expected = NotFoundException.class)
  public void creaCondivisionePraticaConGruppoNotFound() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdGruppo(111L);
    condivisioniPraticheService.creaCondivisione(1L, body);
  }
  
  @Test
  public void creaCondivisioneConGruppo() {
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdGruppo(1L);
    CondivisionePratica condivisionePratica = condivisioniPraticheService.creaCondivisione(2L, body);
    assertNotNull(condivisionePratica);
    assertNotNull(condivisionePratica.getCondivisaAGruppo());
    assertNotNull(condivisionePratica.getCondivisaAGruppo().getCodice());
    assertTrue(condivisionePratica.getCondivisaAGruppo().getCodice().equals("DEMO2021"));
    
  }
  
  @Test
  public void creaCondivisioneConUtente() {
    setupMock();
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdUtente(1L);
    CondivisionePratica condivisionePratica =condivisioniPraticheService.creaCondivisione(2L, body);
    assertNotNull(condivisionePratica);
    assertNotNull(condivisionePratica.getCondivisaAUtente());
    assertNotNull(condivisionePratica.getCondivisaAUtente().getCodiceFiscale());
    assertTrue(condivisionePratica.getCondivisaAUtente().getCodiceFiscale().equals("AAAAAA00B77B000F"));
  }
  
  @Test
  public void creaCondivisioneConUtenteConNotificaFallita() {
    setupMockThrowException();
    CreaCondivisionePraticaRequest body = new CreaCondivisionePraticaRequest();
    body.setIdUtente(1L);
    CondivisionePratica condivisionePratica =condivisioniPraticheService.creaCondivisione(2L, body);
    assertNotNull(condivisionePratica);
    assertNotNull(condivisionePratica.getCondivisaAUtente());
    assertNotNull(condivisionePratica.getCondivisaAUtente().getCodiceFiscale());
    assertTrue(condivisionePratica.getCondivisaAUtente().getCodiceFiscale().equals("AAAAAA00B77B000F"));
  }
  
  @Test(expected = NotFoundException.class)
  public void rimuoviCondivisioneNotFound() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 123L);
  }
  
  @Test(expected = BadRequestException.class)
  public void rimuoviCondivisioneTipoNonCondivisa() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 1L);
  }
  
  @Test(expected = NotFoundException.class)
  public void rimuoviCondivisionePraticaDiversa() {
    condivisioniPraticheService.rimuoviCondivisione(12L, 2L);
  }
  
  @Test
  public void rimuoviCondivisioneConUtente() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 2L);
  }
  
  @Test
  public void rimuoviCondivisioneConGruppo() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 3L);
  }
  
  @Test
  public void rimuoviCondivisioneConUtenteCondivisioneDiverso() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 4L);
  }
  
  @Test
  public void rimuoviCondivisioneUtenteCondivisioneDiversoConGruppo() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 5L);
  }
  
  @Test(expected = ForbiddenException.class)
  public void rimuoviCondivisioneNonRiguardanteUtenteCorrente() {
    condivisioniPraticheService.rimuoviCondivisione(1L, 6L);
  }
  
  private void setupMock() {
    reset(cosmoNotificationsNotificheFeignClient);
    when(cosmoNotificationsNotificheFeignClient.postNotifications(any())).thenReturn(null);
  }
  
  @SuppressWarnings("unchecked")
  private void setupMockThrowException() {
    reset(cosmoNotificationsNotificheFeignClient);
    when(cosmoNotificationsNotificheFeignClient.postNotifications(any())).thenThrow(Exception.class);
  }
}
