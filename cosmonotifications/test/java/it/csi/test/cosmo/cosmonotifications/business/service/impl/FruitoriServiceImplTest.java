/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmonotifications.business.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
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
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.security.model.ClientInfoDTO;
import it.csi.cosmo.common.security.model.ScopeDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.Preferenza;
import it.csi.cosmo.cosmonotifications.business.service.FruitoriService;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaFruitoreRequest;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoAuthorizationPreferenzeUtentiFeignClient;
import it.csi.cosmo.cosmonotifications.integration.rest.CosmoWebsocketFeignClient;
import it.csi.test.cosmo.cosmonotifications.testbed.config.CwnotificationsUnitTestInMemory;
import it.csi.test.cosmo.cosmonotifications.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CwnotificationsUnitTestInMemory.class, FruitoriServiceImplTest.FruitoriServiceTConfig.class})
@Transactional
public class FruitoriServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class FruitoriServiceTConfig {

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
  
  @Before
  public void autentica() {

    var client = ClientInfoDTO.builder().withCodice("FruitoreTest1")
        .withScopes(Arrays.asList(ScopeDTO.builder().withCodice("UNKNOWN").build()))
        .withAnonimo(false).build();

    autentica(null, client);
  }
  
  @Autowired
  private FruitoriService fruitoriService;
  
  private static final String CF = "AAAAAA00B77B000F";
  private static final String DESC = "descrizione";
  private static final String CODICE_IPA = "r_piemon";
  
  @Test(expected = BadRequestException.class)
  public void creaNotificaFruitoreSenzaDestinatari() {
    CreaNotificaFruitoreRequest notifica = new CreaNotificaFruitoreRequest();
    notifica.setCodiceIpaEnte(CODICE_IPA);
    notifica.setDescrizione(DESC);
    fruitoriService.creaNotificaFruitore(notifica);
  }
  
  @Test(expected = NotFoundException.class)
  public void creaNotificaFruitorePraticaNotFound() {
    CreaNotificaFruitoreRequest notifica = new CreaNotificaFruitoreRequest();
    notifica.setCodiceIpaEnte(CODICE_IPA);
    notifica.setDescrizione(DESC);
    notifica.setUrl("url");
    notifica.setDescrizioneUrl(DESC);
    notifica.setIdPratica("1111");
    
    List<String> destinatari = new ArrayList<>();
    destinatari.add(CF);
    notifica.setDestinatari(destinatari);
    
    List<String> utentiDestinatari = new ArrayList<>();
    utentiDestinatari.add(CF);
    notifica.setUtentiDestinatari(utentiDestinatari);
    
    fruitoriService.creaNotificaFruitore(notifica);
  }
  
  @Test
  public void creaNotificaFruitore() {
    setupMock();
    CreaNotificaFruitoreRequest notifica = new CreaNotificaFruitoreRequest();
    notifica.setCodiceIpaEnte(CODICE_IPA);
    notifica.setDescrizione(DESC);
    notifica.setUrl("url");
    notifica.setDescrizioneUrl(DESC);
    notifica.setIdPratica("1");
    
    List<String> destinatari = new ArrayList<>();
    destinatari.add(CF);
    notifica.setDestinatari(destinatari);
    
    List<String> utentiDestinatari = new ArrayList<>();
    utentiDestinatari.add(CF);
    notifica.setUtentiDestinatari(utentiDestinatari);
    
    fruitoriService.creaNotificaFruitore(notifica);
  }
  
  private void setupMock() {
    reset(cosmoAuthorizationPreferenzeUtentiFeignClient);
    Preferenza preferenza = new Preferenza();
    preferenza.setValore("{\"ricezioneNotifiche\": {\"cosmo\": {\"cosmo\": \"false\"},\"email\": {\"email\": \"false\"}}}");
    when(cosmoAuthorizationPreferenzeUtentiFeignClient.getPreferenzeUtenteId(any())).thenReturn(preferenza);
    
    reset(cosmoWebsocketFeignClient);
  }
}
