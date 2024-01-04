/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertArrayEquals;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexFileFormatInfo;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class FileFormatDetectionTest extends ParentUnitTest {

  @Autowired
  private Index2Service index2Service;

  @Test
  public void testWithUploadedNode() {

    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename("testfile-content.txt");
    documento.setMimeType("application/text");
    documento.setContent(getSample("signed-pcks.pdf"));

    try {
      documento = index2Service.create(Index2ServiceImplTest.TEST_ROOT, documento);

      assertArrayEquals(
          new String[] {"true", "PCKS", "PKCS #7 Cryptographic Message File", null, "p7m"},
          getMeta(documento.getUid()));

    } finally {
      index2Service.delete(Index2ServiceImplTest.TEST_ROOT);
    }
  }

  @Test
  public void testJfifRenamed() {

    assertArrayEquals(
        new String[] {"false", null, "JPEG File Interchange Format", "image/jpeg", "jpe"},
        getMeta(getSample("jfif-disguised.docx")));

  }

  @Test
  public void testXmlUnsigned() {

    assertArrayEquals(
        new String[] {"false", null, "Extensible Markup Language", "application/xml",
        "xml"},
        getMeta(getSample("actually-an-xml.xml")));

  }

  @Test
  public void testP7mRenamed() {

    assertArrayEquals(
        new String[] {"true", "PCKS", "PKCS #7 Cryptographic Message File", null, "p7m"},
        getMeta(getSample("cosmo.p7m")));

  }

  @Test
  public void testXmlSigned1() {

    assertArrayEquals(
        new String[] {"true", "XML", "Extensible Markup Language", "application/xml",
        "xml"},
        getMeta(getSample("t1.xml")));

  }

  @Test
  public void testXmlSigned2() {

    assertArrayEquals(
        new String[] {"true", "XML", "Extensible Markup Language", "application/xml",
        "xml"},
        getMeta(getSample("t2.xml")));
  }

  private byte[] getSample(String name) {
    try {
      return this.getClass().getClassLoader().getResourceAsStream("samplefiles/" + name)
          .readAllBytes();
    } catch (IOException e) {
      throw new BadConfigurationException();
    }
  }

  private String[] getMeta(String identifier) {
    return map(index2Service.getFileFormatInfo(identifier));
  }

  private String[] getMeta(byte[] content) {
    return map(index2Service.getFileFormatInfo(content));
  }

  private String[] map(IndexFileFormatInfo result) {

    String[] output = new String[] {null, null, null, null, null};

    output[0] = result.isSigned() ? "true" : "false";
    output[1] = result.getSignatureType();
    output[2] = result.getDescription();
    output[3] = result.getMimeType().length > 0 ? result.getMimeType()[0] : null;
    output[4] = result.getTypeExtension();

    return output;
  }
}
