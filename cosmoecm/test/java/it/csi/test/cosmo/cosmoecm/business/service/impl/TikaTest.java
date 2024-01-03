/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class TikaTest extends ParentIntegrationTest {

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void controlliMimeTypeTikaCore() throws IOException {
    InputStream fileTxt =
        TikaTest.class.getResourceAsStream("/tika/input/provaTXT.doc");
    InputStream filePDF =
        TikaTest.class.getResourceAsStream("/tika/input/prova.pdf");
    InputStream fileFirmato =
        TikaTest.class.getResourceAsStream("/tika/input/prova.p7m");
    InputStream fileDOCX =
        TikaTest.class.getResourceAsStream("/tika/input/prova.jpeg");
    Tika tika = new Tika();
    assertTrue("myme type: text/plain", tika.detect(fileTxt).equalsIgnoreCase("text/plain"));
    assertTrue("myme type: application/pdf", tika.detect(filePDF).equalsIgnoreCase("application/pdf"));
    assertTrue("myme type: application/pkcs7-signature", tika.detect(fileFirmato).equalsIgnoreCase("application/pkcs7-signature"));
    TikaInputStream tikaDocx = TikaInputStream.get(fileDOCX);
    assertTrue("myme type: application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        tika.detect(tikaDocx).equalsIgnoreCase(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
  }

}
