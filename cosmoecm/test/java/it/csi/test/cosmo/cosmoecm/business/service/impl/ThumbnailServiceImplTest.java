/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.cosmoecm.business.service.ThumbnailService;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class ThumbnailServiceImplTest extends ParentIntegrationTest {

  private static final String MIME_TYPE_PDF = "application/pdf";

  private static final String MIME_IMAGE = "image/jpg";

  private static final String MIME_DOCX =
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

  @Autowired
  ThumbnailService service;

  @Test
  public void possibileGenerarePdf() {
    var result = service.possibileGenerazioneThumbnail(MIME_TYPE_PDF);
    assertTrue(result);
  }

  @Test
  public void possibileGenerareImg() {
    var result = service.possibileGenerazioneThumbnail(MIME_IMAGE);
    assertTrue(result);
  }

  @Test
  public void possibileGenerareWord() {
    var result = service.possibileGenerazioneThumbnail(MIME_DOCX);
    assertTrue(result);
  }

  @Test
  public void possibileGenerareQualsiasiAltroFormato() {
    var result = service.possibileGenerazioneThumbnail("application/octet-stream");
    assertFalse(result);
  }

  @Test
  public void generaThumbnail() {
    final InputStream singlePDF = DocumentoServiceImplTest.class
        .getResourceAsStream("/samplefiles/test_pdf-be23cf38-e75b-4a08-9ecd-e19d67f380c9.pdf");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    var result = service.generaThumbnail(singlePDF, MIME_TYPE_PDF, outputStream);
    assertTrue(result);
  }

  @Test
  public void generaThumbnailImage() {
    final InputStream singlePDF = DocumentoServiceImplTest.class
        .getResourceAsStream("/samplefiles/image.jpg");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    var result = service.generaThumbnail(singlePDF, MIME_IMAGE, outputStream);
    assertTrue(result);
  }

  @Test
  public void generaThumbnailWord() {
    final InputStream singlePDF =
        DocumentoServiceImplTest.class.getResourceAsStream("/samplefiles/complex-docx.docx");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    var result = service.generaThumbnail(singlePDF, MIME_DOCX, outputStream);
    assertTrue(result);
  }

  @Test
  public void tentaGenerazioneThumbnailXml() {
    final InputStream singlePDF =
        DocumentoServiceImplTest.class.getResourceAsStream("/samplefiles/t1.xml");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    var result = service.generaThumbnail(singlePDF, "text/xml", outputStream);
    assertFalse(result);
  }

}
