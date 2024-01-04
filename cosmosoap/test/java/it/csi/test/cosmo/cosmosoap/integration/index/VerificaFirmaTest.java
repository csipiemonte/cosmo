/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import java.io.IOException;
import java.util.Date;
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
import it.csi.cosmo.cosmosoap.integration.soap.index2.Index2WrapperFacadeImpl;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature.IndexSignatureVerificationParameters;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class VerificaFirmaTest extends ParentUnitTest {

  private static final String RESULT = "RESULT";

  public static final String TEST_ROOT = "test/VerificaFirmaTest";

  @Autowired
  private Index2Service index2Service;

  @Before
  public void cleanupBefore() {
    cleanup();
  }

  @After
  public void cleanupAfter() {
    cleanup();
  }

  private void cleanup() {
    getLogger().info("DELETING TEST FOLDER");
    index2Service.delete(TEST_ROOT);
  }

  @Test
  public void testManuale() throws Exception {
    var input = getSample("con marca.pdf");
    GenericIndexContent sourceEntity = upload(input);

    Index2WrapperFacadeImpl wrapper =
        (Index2WrapperFacadeImpl) ((Index2ServiceImpl) index2Service).getFacade();

    var executor = wrapper.getMtomExecutor();
    var oc = wrapper.getMtomOperationContext();

    it.doqui.index.ecmengine.mtom.dto.VerifyParameter param =
        new it.doqui.index.ecmengine.mtom.dto.VerifyParameter();
    param.setVerificationDate(new Date()); // NOSONAR
    param.setVerificationType(1);
    param.setVerificationScope(1);
    param.setProfileType(1);

    it.doqui.index.ecmengine.mtom.dto.Document doc1 =
        new it.doqui.index.ecmengine.mtom.dto.Document();
    doc1.setUid(sourceEntity.getUid());

    it.doqui.index.ecmengine.mtom.dto.DocumentOperation operation =
        new it.doqui.index.ecmengine.mtom.dto.DocumentOperation();
    operation.setReturnData(false);
    operation.setTempStore(true);
    doc1.setOperation(operation);

    doc1.setContentPropertyPrefixedName("cm:content");

    try {

      it.doqui.index.ecmengine.mtom.dto.VerifyReport result = executor
          .verifySignedDocument(doc1, null, param, oc);

      dump(RESULT, result);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Test
  public void testVerificaFirmaMultiplaCatena() throws Exception {
    var input = getSample("prova-dosign.p7m");
    GenericIndexContent sourceEntity = upload(input);

    try {
      var result = index2Service.verificaFirma(sourceEntity,
          IndexSignatureVerificationParameters.builder().withVerifyCertificateList(true).build());

      dump(RESULT, result);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Test
  public void testVerificaFirmaAnnidata() throws Exception {
    var input = getSample("cosmo.p7m");
    GenericIndexContent sourceEntity = upload(input);

    try {
      var result = index2Service.verificaFirma(sourceEntity,
          IndexSignatureVerificationParameters.builder().withVerifyCertificateList(true).build());

      dump(RESULT, result);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private GenericIndexContent upload(byte[] content) {
    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename(UUID.randomUUID().toString());
    documento.setMimeType("application/octet-stream");
    documento.setContent(content);

    return index2Service.create(TEST_ROOT, documento);
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
