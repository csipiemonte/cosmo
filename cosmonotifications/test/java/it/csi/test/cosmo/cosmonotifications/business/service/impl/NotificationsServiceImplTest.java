/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
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
import it.csi.cosmo.common.entities.CosmoTNotifica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmonotifications.business.service.NotificationsService;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheResponse;
import it.csi.cosmo.cosmonotifications.dto.rest.Notifica;
import it.csi.cosmo.cosmonotifications.dto.rest.NotificheGlobaliRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.PaginaNotifiche;
import it.csi.cosmo.cosmonotifications.integration.repository.CosmoNotificationRepository;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoAuthorizationPreferenzeUtentiFeignClient;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoWebsocketFeignClient;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;
import it.csi.test.cosmo.cosmonotifications.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmonotifications.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class, NotificationsServiceImplTest.NotificationsServiceTConfig.class})
@Transactional
public class NotificationsServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class NotificationsServiceTConfig {

    @Bean
    @Primary
    public CosmoAuthorizationPreferenzeUtentiFeignClient cosmoAuthorizationPreferenzeUtentiFeignClient() {
      return Mockito.mock(CosmoAuthorizationPreferenzeUtentiFeignClient.class);
    }
    
    @Bean
    @Primary
    public CosmoWebsocketFeignClient cosmoWebsocketFeignClient() {
      return Mockito.mock(CosmoWebsocketFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoWebsocketFeignClient cosmoWebsocketFeignClient;
  
  @Autowired
  private CosmoAuthorizationPreferenzeUtentiFeignClient cosmoAuthorizationPreferenzeUtentiFeignClient;
  
  @Autowired
  private NotificationsService notificationService;
  
  @Autowired
  private CosmoNotificationRepository cosmoTNotificaRepository;
  
  private static final String CODICE_GRUPPO = "DEMO2021";
  private static final String PROVA = "prova";
  private static final String CF = "AAAAAA00B77B000F";
  private static final String CODICE_ENTE = "r_piemon";
  
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test(expected = BadRequestException.class)
  public void getNotificationsOffsetBadRequest() {
    notificationService.getNotifications(-1, 10);
  }
  
  @Test(expected = BadRequestException.class)
  public void getNotificationsLimitBadRequest() {
    notificationService.getNotifications(0, -1);
  }
  
  @Test
  public void getNotifications() {
    PaginaNotifiche notifiche = notificationService.getNotifications(0, 10);
    assertNotNull(notifiche);
    assertNotNull(notifiche.getElementi());
  }
  
  @Test
  public void getNotificationsContentEmpty() {
    PaginaNotifiche notifiche = notificationService.getNotifications(10, 10);
    assertNotNull(notifiche);
    assertNotNull(notifiche.getElementi());
    assertTrue(notifiche.getElementi().isEmpty());
  }
  
  @Test(expected = BadRequestException.class)
  public void getNotificationsIdNotNumeric() {
    notificationService.getNotificationsId(PROVA);
  }
  
  @Test(expected = NotFoundException.class)
  public void getNotificationsIdNull() {
    notificationService.getNotificationsId(null);
  }
  
  @Test
  public void getNotificationsId() {
    Notifica notifica = notificationService.getNotificationsId("1");
    assertNotNull(notifica);
    assertNotNull(notifica.getId());
    assertTrue(notifica.getId() == 1);
    assertTrue(notifica.getDescrizione().equals("notifica1"));
  }
  
  @Test
  public void postNotificationsResultFailed() {
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifiche.add(notifica);
    
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotificationsConGruppoNotFound() {
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add("prova111");
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotificationsConEnteNull() {
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertNull(response.getNotifiche().get(0).getId());
  }
  
  @Test
  public void postNotificationsConUtentiNull() {
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add("prova111");
    notifica.setGruppiDestinatari(gruppi);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotificationsConGruppi() {
    setupMockNull();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
  }
  
  @Test
  public void postNotificationsEmailFalse() {
    setupMock();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    notifica.setTipoNotifica("email");
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotificationsCosmoFalse() {
    setupMock();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    notifica.setTipoNotifica("cosmo");
    notifica.setArrivo(OffsetDateTime.now());
    notifica.setScadenza(OffsetDateTime.now());
    notifica.setIdPratica(111L);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotificationsFruitoreNotFound() {
    setupMock();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    notifica.setTipoNotifica("cosmo");
    notifica.setArrivo(OffsetDateTime.now());
    notifica.setScadenza(OffsetDateTime.now());
    notifica.setIdPratica(1L);
    notifica.setIdFruitore(111L);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertTrue(response.getNotifiche().isEmpty());
  }
  
  @Test
  public void postNotifications() {
    setupMock();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    notifica.setTipoNotifica("info");
    notifica.setArrivo(OffsetDateTime.now());
    notifica.setScadenza(OffsetDateTime.now());
    notifica.setIdPratica(1L);
    notifica.setIdFruitore(1L);
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertNotNull(response.getNotifiche().get(0));
    assertNotNull(response.getNotifiche().get(0).getId());
  }
  
  @Test
  public void postNotificationsConClasseEdEvento() {
    setupMock();
    CreaNotificheRequest body = new CreaNotificheRequest();
    
    List<CreaNotificaRequest> notifiche = new ArrayList<>();
    CreaNotificaRequest notifica = new CreaNotificaRequest();
    notifica.setMessaggio(PROVA);
    notifica.setTipoNotifica("info");
    notifica.setArrivo(OffsetDateTime.now());
    notifica.setScadenza(OffsetDateTime.now());
    notifica.setIdPratica(1L);
    notifica.setIdFruitore(1L);
    notifica.setUrl(PROVA);
    notifica.setDescrizioneUrl(PROVA);
    notifica.setClasse("INFO");
    notifica.setEvento("notifica.ultime.lavorate");
    
    List<String> utenti = new ArrayList<>();
    utenti.add(CF);
    notifica.setUtentiDestinatari(utenti);
    
    List<String> gruppi = new ArrayList<>();
    gruppi.add(CODICE_GRUPPO);
    notifica.setGruppiDestinatari(gruppi);
    
    notifica.setCodiceIpaEnte(CODICE_ENTE);
    
    notifiche.add(notifica);
    body.setNotifiche(notifiche);
    
    CreaNotificheResponse response = notificationService.postNotifications(body);
    assertNotNull(response);
    assertNotNull(response.getNotifiche());
    assertNotNull(response.getNotifiche().get(0));
    assertNotNull(response.getNotifiche().get(0).getId());
  }
  
  @Test(expected = NotFoundException.class)
  public void putNotificationsIdNotNumeric() {
    Notifica body = new Notifica();
    notificationService.putNotificationsId(CF, body);
  }
  
  @Test
  public void putNotifications() {
    Notifica body = new Notifica();
    Notifica response = notificationService.putNotificationsId("1", body);
    assertNotNull(response);
    assertNotNull(response.getId());
    assertTrue(response.getId() == 1);
  }
  
  @Test(expected = NotFoundException.class)
  public void putNotificationsNotFound() {
    Notifica body = new Notifica();
    notificationService.putNotificationsId("122", body);
  }
  
  @Test
  public void sendNotification() {
    CosmoTNotifica notifica = cosmoTNotificaRepository.findOne(1L);
    notificationService.sendNotification(notifica, PROVA, PROVA);
  }
  
  @Test
  public void sendNotificationConClasseEdEvento() {
    CosmoTNotifica notifica = cosmoTNotificaRepository.findOne(2L);
    notificationService.sendNotification(notifica, PROVA, "notifica.ultime.lavorate");
  }
  
  @Test
  public void sendNotificationConTitoloEdEventoBlank() {
    CosmoTNotifica notifica = cosmoTNotificaRepository.findOne(2L);
    notificationService.sendNotification(notifica, "", "");
  }
  
  @Test
  public void sendNotificationsPraticaNotFound() {
    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(111L);
    request.setTipoNotifica(PROVA);
    notificationService.sendNotifications(request);
  }
  
  @Test
  public void sendNotificationsUtenteCreatoreDiverso() {
    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(13L);
    request.setTipoNotifica("INFO");
    notificationService.sendNotifications(request);
  }
  
  @Test
  public void sendNotifications() {
    NotificheGlobaliRequest request = new NotificheGlobaliRequest();
    request.setIdPratica(1L);
    request.setTipoNotifica("INFO");
    notificationService.sendNotifications(request);
  }
  
  @Test
  public void putNotificationsAll() {
    notificationService.putNotificationsAll();
  }
  
  private void setupMock() {
    reset(cosmoAuthorizationPreferenzeUtentiFeignClient);
    Preferenza preferenza = new Preferenza();
    preferenza.setValore("{\"ricezioneNotifiche\": {\"cosmo\": {\"cosmo\": \"false\"},\"email\": {\"email\": \"false\"}}}");
    when(cosmoAuthorizationPreferenzeUtentiFeignClient.getPreferenzeUtenteId(any())).thenReturn(preferenza);
    
    reset(cosmoWebsocketFeignClient);
  }
  
  private void setupMockNull() {
    reset(cosmoAuthorizationPreferenzeUtentiFeignClient);
    Preferenza preferenza = new Preferenza();
    when(cosmoAuthorizationPreferenzeUtentiFeignClient.getPreferenzeUtenteId(any())).thenReturn(preferenza);
    
    reset(cosmoWebsocketFeignClient);
  }
}
