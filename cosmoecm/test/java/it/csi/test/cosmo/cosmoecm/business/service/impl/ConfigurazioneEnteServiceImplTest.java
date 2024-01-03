/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.dto.common.ConfigurazioneDTO;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmoecm.business.service.ConfigurazioneEnteService;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class ConfigurazioneEnteServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ConfigurazioneEnteService configurazioneEnteService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getConfigurazioneEnteValido() {
    ConfigurazioneDTO result = configurazioneEnteService.getConfigEnte("chiave 1");

    assertNotNull("Deve esserci una configurazione valida", result);
  }

  @Test(expected = BadRequestException.class)
  public void getConfigurazioneEnteNonValido() {
    configurazioneEnteService.getConfigEnte("chiave 2");
  }

  @Test(expected = BadRequestException.class)
  public void getConfigurazioneEnteChiaveVuota() {
    configurazioneEnteService.getConfigEnte("");
  }

}
