/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.poi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
public class ApachePOITest extends ParentUnitTest {

  @Before
  public void cleanupBefore() {
    cleanup();
  }

  @After
  public void cleanupAfter() {
    cleanup();
  }

  private void cleanup() {
    getLogger().info("cleanup");
  }

  @Test
  public void testDOCX() throws Exception {
    byte[] input = getSample("complex-docx.docx");

    InputStream doc = new ByteArrayInputStream(input);
    XWPFDocument document = new XWPFDocument(doc);
    PdfOptions options = PdfOptions.create();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PdfConverter.getInstance().convert(document, out, options);
    byte[] outBytes = out.toByteArray();


    PDDocument documentPdf = PDDocument.load(new ByteArrayInputStream(outBytes));
    PDFRenderer pdfRenderer = new PDFRenderer(documentPdf);
    for (int page = 0; page < documentPdf.getNumberOfPages(); ++page) {
      BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
      bim.toString();

    }
    documentPdf.close();
  }

  @Test
  public void testPDF() throws Exception {
    byte[] input = getSample("pdf-extracted.pdf");

    PDDocument document = PDDocument.load(input);
    PDFRenderer pdfRenderer = new PDFRenderer(document);
    for (int page = 0; page < document.getNumberOfPages(); ++page) {
      BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 150, ImageType.RGB);
      bim.toString();



    }
    document.close();
  }

  @Test
  public void testJPEG() throws Exception {
    byte[] input = getSample("image.jpg");

    BufferedImage bimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    bimg.createGraphics().drawImage(ImageIO.read(new ByteArrayInputStream(input))
        .getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);


  }

  @Test
  public void testPNG() throws Exception {
    byte[] input = getSample("image.png");

    BufferedImage bimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    bimg.createGraphics().drawImage(ImageIO.read(new ByteArrayInputStream(input))
        .getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);

  }

  private byte[] getSample(String name) {
    try {
      return this.getClass().getClassLoader().getResourceAsStream("samplefiles/" + name)
          .readAllBytes();
    } catch (IOException e) {
      throw new BadConfigurationException();
    }
  }

}
