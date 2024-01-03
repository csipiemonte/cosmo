/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
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
import it.csi.cosmo.cosmopratiche.business.service.CaricamentoPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CaricamentoPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoCaricamentoPratica;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoFileUploadFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, CaricamentoPraticaServiceImplTest.CaricamentoPraticaServiceIntegrationTConfig.class})
@Transactional
public class CaricamentoPraticaServiceImplTest extends ParentIntegrationTest {
  
  @Configuration
  public static class CaricamentoPraticaServiceIntegrationTConfig {

    @Bean
    @Primary
    public CosmoFileUploadFeignClient cosmoFileUploadFeignClient() {
      return Mockito.mock(CosmoFileUploadFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoFileUploadFeignClient cosmoFileUploadFeignClient;
  
  @Autowired
  private CaricamentoPraticaService caricamentoPraticaService;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Test(expected = NotFoundException.class)
  public void creaCaricamentoPraticaNotFoundStato() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROVA");
    caricamentoPraticaService.creaCaricamentoPratica(request);
  }
  
  @Test
  public void creaCaricamentoPratica() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROCESSO_AVVIATO");
    CaricamentoPratica caricamentoPratica = caricamentoPraticaService.creaCaricamentoPratica(request);
    assertNotNull(caricamentoPratica);
    assertNotNull(caricamentoPratica.getStatoCaricamentoPratica());
    assertTrue(caricamentoPratica.getStatoCaricamentoPratica().getCodice().equals("PROCESSO_AVVIATO"));
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaCaricamentoPraticaNotFound() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    caricamentoPraticaService.aggiornaCaricamentoPratica(111L, request);
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaCaricamentoPraticaNotFoundStato() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROVA");
    caricamentoPraticaService.aggiornaCaricamentoPratica(1L, request);
  }
  
  @Test
  public void aggiornaCaricamentoPraticaConIdPraticaNull() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROCESSO_AVVIATO");
    CaricamentoPratica caricamentoPratica = caricamentoPraticaService.aggiornaCaricamentoPratica(1L, request);
    assertNotNull(caricamentoPratica);
    assertNotNull(caricamentoPratica.getStatoCaricamentoPratica());
    assertTrue(caricamentoPratica.getStatoCaricamentoPratica().getCodice().equals("PROCESSO_AVVIATO"));
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaCaricamentoPraticaNotFoundPratica() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROCESSO_AVVIATO");
    request.setIdPratica(111L);
    caricamentoPraticaService.aggiornaCaricamentoPratica(1L, request);
  }
  
  @Test
  public void aggiornaCaricamentoPratica() {
    CaricamentoPraticaRequest request = new CaricamentoPraticaRequest();
    request.setStatoCaricamentoPratica("PROCESSO_AVVIATO");
    request.setIdPratica(1L);
    CaricamentoPratica caricamentoPratica = caricamentoPraticaService.aggiornaCaricamentoPratica(1L, request);
    assertNotNull(caricamentoPratica);
    assertNotNull(caricamentoPratica.getStatoCaricamentoPratica());
    assertTrue(caricamentoPratica.getStatoCaricamentoPratica().getCodice().equals("PROCESSO_AVVIATO"));
  }
  
  @Test
  public void getStatiCaricamento() {
    List<StatoCaricamentoPratica> listaStati = caricamentoPraticaService.getStatiCaricamento();
    assertNotNull(listaStati);
    assertEquals(listaStati.size(), 13);
  }
  
  @Test
  public void getCaricamentoPratiche() {
    CaricamentoPraticheResponse response = caricamentoPraticaService.getCaricamentoPratiche(null);
    assertNotNull(response);
    assertNotNull(response.getCaricamentoPratiche());
    assertEquals(response.getCaricamentoPratiche().size(), 4);
    assertNotNull(response.getCaricamentoPratiche().get(0));
    assertNotNull(response.getCaricamentoPratiche().get(0).getId());
    assertTrue(response.getCaricamentoPratiche().get(0).getId() == 10);
  }
  
  @Test
  public void getCaricamentoPraticheCaricamentoInBozza() {
    CaricamentoPraticheResponse caricamentoPratiche = caricamentoPraticaService.getCaricamentoPraticheCaricamentoInBozza(null);
    assertNotNull(caricamentoPratiche);
    assertNotNull(caricamentoPratiche.getCaricamentoPratiche());
    assertEquals(caricamentoPratiche.getCaricamentoPratiche().size(), 2);
  }
  
  @Test(expected = BadRequestException.class)
  public void deleteCaricamentoPraticaNotFound() {
    caricamentoPraticaService.deleteCaricamentoPratiche(111L);
  }
  
  @Test
  public void deleteCaricamentoPratica() {
    setupMock();
    caricamentoPraticaService.deleteCaricamentoPratiche(1L);
  }
  
  @Test(expected = BadRequestException.class)
  public void deleteCaricamentoPraticaErroreCancellazione() {
    setupMockThrowException();
    caricamentoPraticaService.deleteCaricamentoPratiche(1L);
  }
  
  @Test
  public void getCaricamentoPraticheIdWithExportTrue() {
    CaricamentoPraticheResponse response = caricamentoPraticaService.getCaricamentoPraticheId("1", null, true);
    assertNotNull(response);
    assertNotNull(response.getCaricamentoPratiche());
    assertEquals(response.getCaricamentoPratiche().size(), 3);
    assertNotNull(response.getCaricamentoPratiche().get(0));
    assertNotNull(response.getCaricamentoPratiche().get(0).getId());
    assertTrue(response.getCaricamentoPratiche().get(0).getId() == 3);
  }
  
  @Test
  public void getCaricamentoPraticheIdWithExportFalse() {
    CaricamentoPraticheResponse response = caricamentoPraticaService.getCaricamentoPraticheId("1", null, false);
    assertNotNull(response);
    assertNotNull(response.getCaricamentoPratiche());
    assertEquals(response.getCaricamentoPratiche().size(), 3);
    assertNotNull(response.getCaricamentoPratiche().get(0));
    assertNotNull(response.getCaricamentoPratiche().get(0).getId());
    assertTrue(response.getCaricamentoPratiche().get(0).getId() == 3);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPathElaborazioniDataSbagliata() {
    caricamentoPraticaService.getPathElaborazioni("prova");
  }
  
  @Test
  public void deletePathFile() {
    caricamentoPraticaService.deletePathFile("provafile");
  }
  
  @Test
  public void getPathElaborazioni() {
    List<String> listaPath =caricamentoPraticaService.getPathElaborazioni("2023-11-07");
    assertNotNull(listaPath);
    assertNotNull(listaPath.get(0));
    assertTrue(listaPath.get(0).equals("provafile"));
  }
  
  private void setupMock() {
    reset(cosmoFileUploadFeignClient);
    doNothing().when(cosmoFileUploadFeignClient).deleteFilePratiche(any());
  }
  
  private void setupMockThrowException() {
    reset(cosmoFileUploadFeignClient);
    doThrow(Exception.class).when(cosmoFileUploadFeignClient).deleteFilePratiche(any());
  }
}
