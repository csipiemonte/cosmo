/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.business.service.impl.Index2ServiceImpl;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2NodeNotFoundException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;
import it.doqui.index.ecmengine.client.webservices.dto.Node;
import it.doqui.index.ecmengine.client.webservices.dto.OperationContext;
import it.doqui.index.ecmengine.client.webservices.dto.engine.management.Property;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.Document;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.DocumentOperation;
import it.doqui.index.ecmengine.client.webservices.dto.engine.security.EnvelopedContent;
import it.doqui.index.ecmengine.client.webservices.engine.EcmEngineWebServiceDelegate;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class SbustamentoTest extends ParentUnitTest {

  private static final String RESULT = "RESULT";

  private static final String EXPECTED_OUTPUT_FILE = "signed-pcks-extracted.pdf";

  private static final String INPUT_FILE = "signed-pcks.pdf";

  public static final String TEST_ROOT = "test/SbustamentoTest";

  @Autowired
  private Index2Service index2Service;

  @Before
  public void cleanupBefore() {
    cleanup();
  }

  @After
  public void cleanupAfter() {
    // cleanup();
  }

  private void cleanup() {
    getLogger().info("DELETING TEST FOLDER");
    index2Service.delete(TEST_ROOT);
  }

  @Test
  public void testWithSourceEntityAndTargetIdentifierAsPathWithTargetEntityContentModel() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    GenericIndexContent sourceEntity = upload(input);
    CosmoDocumentoIndex targetEntity = new CosmoDocumentoIndex();
    targetEntity.setDescrizione("descrizione");
    targetEntity.setIdDocumentoCosmo(999999L);
    targetEntity.setTipoDocumento("T");

    var result = index2Service.estraiBusta(sourceEntity, TEST_ROOT, targetEntity);

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result.getContent()));

    assertTrue(result.getFilename().contains(sourceEntity.getFilename()));
    assertTrue(result.getFilename().endsWith(".pdf"));
    assertEquals("application/pdf", result.getMimeType());

    assertEquals(targetEntity.getDescrizione(), result.getDescrizione());
    assertEquals(targetEntity.getIdDocumentoCosmo(), result.getIdDocumentoCosmo());
    assertEquals(targetEntity.getTipoDocumento(), result.getTipoDocumento());
  }

  @Test
  public void testWithSourceEntityAndTargetIdentifierAsPathWithTargetEntityGeneric() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    GenericIndexContent sourceEntity = upload(input);
    GenericIndexContent targetEntity = new GenericIndexContent();

    targetEntity.setMimeType("application/octet-stream");
    targetEntity.setFilename("estratto-nome-custom.pdf");

    var result = index2Service.estraiBusta(sourceEntity, TEST_ROOT, targetEntity);

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result.getContent()));

    assertEquals(targetEntity.getFilename(), result.getFilename());
    assertEquals("application/pdf", result.getMimeType());
  }

  @Test
  public void testWithSourceEntityAndTargetIdentifierAsPath() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    GenericIndexContent sourceEntity = upload(input);

    var result = index2Service.estraiBusta(sourceEntity, TEST_ROOT);

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result.getContent()));

    assertTrue(result.getFilename().contains(sourceEntity.getFilename()));
    assertTrue(result.getFilename().endsWith(".pdf"));
    assertEquals("application/pdf", result.getMimeType());
  }

  @Test(expected = Index2NodeNotFoundException.class)
  public void testWithSourceIdentifierAsMissingPath() {

    index2Service.estraiBusta(TEST_ROOT + "/notfound-" + UUID.randomUUID().toString());
  }

  @Test(expected = Index2NodeNotFoundException.class)
  public void testWithSourceIdentifierAsMissingUID() {

    index2Service.estraiBusta(UUID.randomUUID().toString());
  }

  @Test
  public void testWithSourceIdentifierAsUUID() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    GenericIndexContent sourceEntity = upload(input);

    var result = index2Service.estraiBusta(sourceEntity.getUid());

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result));
  }

  @Test
  public void testWithSourceEntityToBytes() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    GenericIndexContent sourceEntity = upload(input);

    var result = index2Service.estraiBusta(sourceEntity);

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result));
  }

  @Test
  public void testWithBytesToBytes() {
    var input = getSample(INPUT_FILE);
    var expectedOutput = getSample(EXPECTED_OUTPUT_FILE);

    var result = index2Service.estraiBusta(input);

    dump(RESULT, result);
    assertTrue(sameBytes(expectedOutput, result));
  }

  private GenericIndexContent upload(byte[] content) {
    return upload(content, "application/octet-stream");
  }

  private GenericIndexContent upload(byte[] content, String mimeType) {
    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename(UUID.randomUUID().toString());
    documento.setMimeType(mimeType);
    documento.setContent(content);

    return index2Service.create(TEST_ROOT, documento);
  }

  @Test
  public void testLowLevel() throws IOException {
    Index2WrapperFacadeImpl wrapper =
        (Index2WrapperFacadeImpl) ((Index2ServiceImpl) index2Service).getFacade();

    OperationContext oc = wrapper.getOperationContext();
    EcmEngineWebServiceDelegate facade = wrapper.getExecutor();

    final var thisInput = "cosmo.p7m";
    final var thisOutput = "cosmo-extracted.pdf";

    var bytes = getSample(thisInput);

    final var testFolder = "test/sbustamento";
    index2Service.delete(testFolder);

    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename("testfile-content.xml");
    documento.setMimeType("application/text");
    documento.setContent(bytes);

    documento = index2Service.create(testFolder, documento);

    // nota: funziona passando EnvelopedContent ma bisogna trasferire i bytes
    EnvelopedContent ec = new EnvelopedContent();
    ec.setData(bytes);
    ec.setStore(true);


    Node nodo = new Node();
    nodo.setUid(documento.getUid());


    Document doc = new Document();

    var operation = new DocumentOperation();
    operation.setReturnData(false);
    operation.setTempStore(true);

    doc.setOperation(operation);
    doc.setUid(documento.getUid());
    doc.setContentPropertyPrefixedName("cm:content");

    var result = facade.extractDocumentFromEnvelope(doc, oc);
    dump(RESULT, result);

    GenericIndexContent documentoEstratto = new GenericIndexContent();
    documentoEstratto.setFilename("testfile-content-estratto.xml");
    documentoEstratto.setMimeType("application/text");
    documentoEstratto.setContent(new byte[] {});
    documentoEstratto = index2Service.create(testFolder, documentoEstratto);

    var nodeTempSource = new Node(result.getUid());
    var nodeTempDest = new Node(documentoEstratto.getUid());
    var property = new Property();

    property.setPrefixedName("cm:content");

    facade.moveContentFromTemp(nodeTempSource, nodeTempDest, property, oc);
    dump("MOVE RESULT", "done");

    var extractedFile = index2Service.find(documentoEstratto.getUid());

    dump("EXTRACTED FILE", extractedFile);

    assertNotNull(extractedFile.getContent());
    assertTrue(extractedFile.getContent().length > 10);
    assertTrue(sameBytes(extractedFile.getContent(), getSample(thisOutput)));

  }

  private byte[] getSample(String name) {
    try {
      return this.getClass().getClassLoader().getResourceAsStream("samplefiles/" + name)
          .readAllBytes();
    } catch (IOException e) {
      throw new BadConfigurationException();
    }
  }

  private boolean sameBytes(byte[] a, byte[] b) {
    return a != null && b != null
        && Base64.getEncoder().encodeToString(a).equals(Base64.getEncoder().encodeToString(b));
  }


}
