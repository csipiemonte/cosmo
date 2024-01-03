/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabileProcesso;
import it.csi.cosmo.cosmobusiness.dto.rest.VariabiliProcessoResponse;
import it.csi.cosmo.cosmopratiche.business.service.MetadatiService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class, MetadatiServiceImplTest.MetadatiServiceIntegrationTConfig.class})
@Transactional
public class MetadatiServiceImplTest {
  
  @Configuration
  public static class MetadatiServiceIntegrationTConfig {

    @Bean
    @Primary
    public CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient() {
      return Mockito.mock(CosmoBusinessPraticheFeignClient.class);
    }
  }
  
  @Autowired
  private CosmoBusinessPraticheFeignClient cosmoBusinessPraticheFeignClient;
  
  @Autowired
  private MetadatiService metadatiService;
  
  @Test(expected = BadRequestException.class)
  public void aggiornaMetadatiPraticaIdNull() {
    metadatiService.aggiornaMetadatiPratica(null);
  }
  
  @Test(expected = BadRequestException.class)
  public void aggiornaMetadatiPraticaIdNonNumerico() {
    metadatiService.aggiornaMetadatiPratica("prova");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaIdNotFound() {
    metadatiService.aggiornaMetadatiPratica("56");
  }
  
  @Test
  public void aggiornaMetadatiPraticaBlank() {
    Pratica pratica = metadatiService.aggiornaMetadatiPratica("3");
    assertNotNull(pratica);
    assertNull(pratica.getMetadati());
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaConTipoPraticaNull() {
    metadatiService.aggiornaMetadatiPratica("7");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaSenzaConfigurazioneMetadati() {
    metadatiService.aggiornaMetadatiPratica("8");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaProcessoNotFound() {
    setupMockNull();
    metadatiService.aggiornaMetadatiPratica("1");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaProcessoNull() {
    setupMockNull();
    metadatiService.aggiornaMetadatiPratica("9");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaProcessoBlank() {
    setupMockNull();
    metadatiService.aggiornaMetadatiPratica("10");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaMetadatiPraticaProcessoReplaceBlank() {
    setupMockNull();
    metadatiService.aggiornaMetadatiPratica("11");
  }
  
  @Test
  public void aggiornaMetadatiPratica() {
    setupMockWithValue();
    Pratica pratica = metadatiService.aggiornaMetadatiPratica("1");
    assertNotNull(pratica);
  }
  
  @Test
  public void aggiornaMetadatiPraticaConNodo() {
    setupMockWithValue();
    Pratica pratica = metadatiService.aggiornaMetadatiPratica("5");
    assertNotNull(pratica);
  }
  
  @Test(expected = InternalServerException.class)
  public void aggiornaMetadatiPraticaProblemaLetturaMetadati() {
    setupMockWithValue();
    Pratica pratica = metadatiService.aggiornaMetadatiPratica("4");
    assertNotNull(pratica);
  }
  
  @Test
  public void aggiornaMetadatiPraticaConArray() {
    setupMockWithValue();
    Pratica pratica = metadatiService.aggiornaMetadatiPratica("12");
    assertNotNull(pratica);
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaVariabiliProcessoMetadatiNull() {
    metadatiService.aggiornaVariabiliProcesso("3");
  }
  
  @Test(expected = ConflictException.class)
  public void aggiornaVariabiliProcessoLinkPraticaNull() {
    metadatiService.aggiornaVariabiliProcesso("10");
  }
  
  @Test(expected = ConflictException.class)
  public void aggiornaVariabiliProcessoLinkPraticaBlank() {
    metadatiService.aggiornaVariabiliProcesso("9");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaVariabiliProcessoTipoPraticaNotFound() {
    metadatiService.aggiornaVariabiliProcesso("7");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaVariabiliProcessoSenzaConfigurazioneMetadati() {
    metadatiService.aggiornaVariabiliProcesso("8");
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaVariabiliProcessoProcessoNotFound() {
    setupMockNull();
    metadatiService.aggiornaVariabiliProcesso("1");
  }
  
  @Test
  public void aggiornaVariabiliProcesso() {
    setupMockWithValueVariabili();
    Pratica pratica = metadatiService.aggiornaVariabiliProcesso("1");
    assertNotNull(pratica);
  }
  
  @Test
  public void aggiornaVariabiliProcessoConNodo() {
    setupMockWithValueVariabili();
    Pratica pratica = metadatiService.aggiornaVariabiliProcesso("5");
    assertNotNull(pratica);
  }
  
  @Test(expected = InternalServerException.class)
  public void aggiornaVariabiliProcessoProblemaLetturaMetadati() {
    setupMockWithValueVariabili();
    Pratica pratica = metadatiService.aggiornaVariabiliProcesso("4");
    assertNotNull(pratica);
  }
  
  @Test
  public void aggiornaVariabiliProcessoConArray() {
    setupMockWithValueVariabili();
    Pratica pratica = metadatiService.aggiornaVariabiliProcesso("12");
    assertNotNull(pratica);
  }
  
  private void setupMockNull() {
    reset(cosmoBusinessPraticheFeignClient);
    when(cosmoBusinessPraticheFeignClient.getPraticheVariabiliProcessInstanceId(any())).thenReturn(null);
  }
  
  private void setupMockWithValue() {
    reset(cosmoBusinessPraticheFeignClient);
    VariabiliProcessoResponse response = new VariabiliProcessoResponse();
    List<VariabileProcesso> variabili = new ArrayList<>();
    VariabileProcesso variabile = new VariabileProcesso();
    variabile.setName("emailRichiedente");
    VariabileProcesso variabile1 = new VariabileProcesso();
    variabile1.setName("coloreOggetto");
    VariabileProcesso variabile2 = new VariabileProcesso();
    variabile2.setName("formaOggetto");
    VariabileProcesso variabile3 = new VariabileProcesso();
    variabile3.setName("valoreEmail");
    variabili.add(variabile);
    variabili.add(variabile1);
    variabili.add(variabile2);
    variabili.add(variabile3);
    response.setVariabili(variabili);
    when(cosmoBusinessPraticheFeignClient.getPraticheVariabiliProcessInstanceId(any())).thenReturn(response );
  }
  
  private void setupMockWithValueVariabili() {
    reset(cosmoBusinessPraticheFeignClient);
    VariabiliProcessoResponse response = new VariabiliProcessoResponse();
    List<VariabileProcesso> variabili = new ArrayList<>();
    VariabileProcesso variabile = new VariabileProcesso();
    variabile.setName("emailRichiedente");
    VariabileProcesso variabile1 = new VariabileProcesso();
    variabile1.setName("coloreOggetto");
    VariabileProcesso variabile2 = new VariabileProcesso();
    variabile2.setName("formaOggetto");
    VariabileProcesso variabile3 = new VariabileProcesso();
    variabile3.setName("valoreEmail");
    variabili.add(variabile);
    variabili.add(variabile1);
    variabili.add(variabile2);
    variabili.add(variabile3);
    response.setVariabili(variabili);
    when(cosmoBusinessPraticheFeignClient.getPraticheVariabiliProcessInstanceId(any())).thenReturn(response);
    doNothing().when(cosmoBusinessPraticheFeignClient).putPraticheVariabiliProcessInstanceId(any(), any());
  }
}
