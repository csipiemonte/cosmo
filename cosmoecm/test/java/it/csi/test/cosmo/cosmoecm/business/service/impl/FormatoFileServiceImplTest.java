/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.cosmoecm.business.service.FormatoFileService;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
public class FormatoFileServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private FormatoFileService formatoFileService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void ricercaFormatiFile() {
    String filtro = "{\"fields\": \"codice, descrizione\" }";
    var result = formatoFileService.getFormatiFile(filtro);
    assertNotNull(result);
    assertNotNull(result.getFormatiFile());
    assertTrue(!result.getFormatiFile().isEmpty());
  }

  @Test
  public void ricercaFormatiFileRaggruppati() {
    String filtro = "{\"fields\": \"codice, descrizione\" }";
    var result = formatoFileService.getFormatiFileRaggruppati(filtro);
    assertNotNull(result);
    assertNotNull(result.getFormatiFile());
    assertTrue(!result.getFormatiFile().isEmpty());
  }

  @Test
  public void ricercaFormatiFileRaggruppatiFullText() {
    String filtro =
        "{\"filter\":{\"fullText\":{\"ci\":\"application\"}},\"fields\": \"codice, descrizione\" }";
    var result = formatoFileService.getFormatiFileRaggruppati(filtro);
    assertNotNull(result);
    assertNotNull(result.getFormatiFile());
    assertTrue(!result.getFormatiFile().isEmpty());
  }

}
