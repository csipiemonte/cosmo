/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmoauthorization.business.service.ImpostazioniFirmaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ImpostazioniFirma;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ImpostazioniFirmaServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private ImpostazioniFirmaService impostazioniFirmaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getImpostazioniFirma() {
    ImpostazioniFirma impostazioniFirma = impostazioniFirmaService.getImpostazioniFirma();
    assertNotNull(impostazioniFirma);
    assertNotNull(impostazioniFirma.getEntiCertificatori());
    assertNotNull(impostazioniFirma.getProfiliFEQ());
    assertNotNull(impostazioniFirma.getScelteMarcaTemporale());
    assertNotNull(impostazioniFirma.getTipiCredenzialiFirma());
    assertNotNull(impostazioniFirma.getTipiOTP());
  }

}
