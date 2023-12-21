/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ParametriResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ConfigurazioneServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getTuttiParametri() {
    ParametriResponse parametriResponse = configurazioneService.getConfigAPI(null);

    assertNotNull("response non nulla", parametriResponse);
    assertNotNull("parametri non nulli", parametriResponse.getParametri());
    assertTrue(parametriResponse.getParametri().size() > 1);
  }

}
