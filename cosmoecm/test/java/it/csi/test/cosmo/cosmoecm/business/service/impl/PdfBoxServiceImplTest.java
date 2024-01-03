/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class PdfBoxServiceImplTest extends ParentIntegrationTest {

  protected static final String myDirectoryPath = "test/resources/samplefiles/pdf-extracted.pdf";

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }


  @Test
  public void writeSomeTextToCoordinates() throws IOException {
    String cwd = Path.of(myDirectoryPath).toAbsolutePath().toString();
    File dir = new File(cwd);
    var doc = PDDocument.load(dir);
    PDPage firstPage = doc.getPage(0);
    PDFont pdfFont= PDType1Font.HELVETICA_BOLD;
    int fontSize = 14;
    PDPageContentStream contentStream = new PDPageContentStream(doc, firstPage, PDPageContentStream.AppendMode.APPEND,true,true);
    contentStream.setFont(pdfFont, fontSize);
    contentStream.beginText();
    contentStream.newLineAtOffset(200,685);
    contentStream.showText("John");
    contentStream.endText();
    contentStream.close(); // don't forget that one!
    doc.save(cwd);
  }

}
