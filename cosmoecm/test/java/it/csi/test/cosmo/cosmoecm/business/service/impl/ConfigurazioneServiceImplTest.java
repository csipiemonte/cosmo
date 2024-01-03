/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class ConfigurazioneServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void testListaConfigurazioniSenzaParametri() {
    var cfg = configurazioneService.getConfig();
    assertTrue(!cfg.isEmpty());
  }

  @Test
  public void testConfigurazioneParametroApplicativo() {
    var cfg = configurazioneService.getConfig(ParametriApplicativo.TESTMODE_ENABLED);
    assertTrue(!cfg.isEmpty());
  }

  @Test(expected = BadConfigurationException.class)
  public void testConfigurazioneParametroApplicativoNonConfigurato() {
    configurazioneService.getConfig(ParametriApplicativo.BATCH_SIGILLO_ELETTRONICO_ENABLE);
  }

  @Test
  public void testRichiestaConfigurazioneParametroApplicativo() {
    var cfg = configurazioneService.requireConfig(ParametriApplicativo.TESTMODE_ENABLED);
    assertTrue(!cfg.isEmpty());
  }

  @Test(expected = BadConfigurationException.class)
  public void testRichiestaConfigurazioneParametroApplicativoNonConfigurato() {
    configurazioneService.requireConfig(ParametriApplicativo.BATCH_SIGILLO_ELETTRONICO_ENABLE);
  }

  @Test
  public void testRichiestaBuildProperties() {
    var buildProperties = configurazioneService.getBuildProperties();
    assertTrue(!buildProperties.isEmpty());
  }

  @Test
  public void testRichiediIstanza() {
    var instance = ConfigurazioneService.getInstance();
    assertTrue(null != instance);
  }

  @Test
  public void testConfigurazioneParametroApplicativoRaw() {
    var cfg = configurazioneService.getConfig("testmode.enabled");
    assertTrue(null != cfg);
  }

  @Test
  public void testConfigurazioneParametroTestModeAttivo() {
    assertTrue(configurazioneService.isTestModeEnabled());
  }



}
