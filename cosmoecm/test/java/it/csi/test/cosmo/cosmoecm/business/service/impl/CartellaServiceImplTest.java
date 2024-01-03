/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmoecm.business.service.CartellaService;
import it.csi.cosmo.cosmoecm.dto.rest.Cartella;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.test.cosmo.cosmoecm.business.service.impl.CartellaServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
public class CartellaServiceImplTest extends ParentIntegrationTest {

  private static final String PATH = "path";

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient indexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

  }

  @Autowired
  private CartellaService cartellaService;

  @Autowired
  private CosmoSoapIndexFeignClient mockIndexFeignClient;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test(expected = BadRequestException.class)
  public void creaCartellaSuIndexErroreCartellaDaCreareNonValorizzata() {
    var cartella = new Cartella();
    cartellaService.creaCartellaIndex(cartella);
  }

  @Test(expected = InternalServerException.class)
  public void creaCartellaSuIndexErroreChiamataIndexUuidCartellaNulla() {
    setUpMockCreazioneCartellaVuota();
    var cartella = new Cartella();
    cartella.setPath(PATH);
    cartellaService.creaCartellaIndex(cartella);
  }

  @Test
  public void creaCartellaSuIndex() {
    setUpMockCreazioneCartella();
    var cartella = new Cartella();
    cartella.setPath(PATH);
    var result = cartellaService.creaCartellaIndex(cartella);
    assertNotNull(result);
  }

  @Test(expected = BadRequestException.class)
  public void cancellaCartellaSuIndexErroreUuidCartellaNonValorizzato() {
    var cartella = new Cartella();
    cartellaService.cancellaCartellaIndex(cartella);
  }

  @Test
  public void cancellaCartellaSuIndex() {
    setUpMockCancellazioneCartella();
    var cartella = new Cartella();
    cartella.setUuid("uuid");
    cartellaService.cancellaCartellaIndex(cartella);
  }

  private void setUpMockCreazioneCartellaVuota() {
    reset(mockIndexFeignClient);
    when(mockIndexFeignClient.createFolder(any())).thenReturn(null);
  }

  private void setUpMockCreazioneCartella() {
    reset(mockIndexFeignClient);
    when(mockIndexFeignClient.createFolder(any())).thenReturn("UuidCartella");
  }

  private void setUpMockCancellazioneCartella() {
    reset(mockIndexFeignClient);
    doNothing().when(mockIndexFeignClient).deleteIdentifier(any());
  }


}
