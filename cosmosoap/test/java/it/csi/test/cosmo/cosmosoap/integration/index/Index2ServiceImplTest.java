/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoPraticaIndex;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2NodeLockedException;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexFolder;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntityVersion;
import it.csi.cosmo.cosmosoap.integration.soap.index2.mtom.Index2MtomDownloadHelper;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
@Transactional
public class Index2ServiceImplTest extends ParentUnitTest {

  protected static CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "Index2ServiceImplTest");

  public static final String TEST_ROOT = "test/Index2ServiceImplTest";

  @Autowired
  private Index2Service service;

  @Before
  public void cleanupBefore() {
    cleanup();
  }

  @After
  public void cleanupAfter() {
    cleanup();
  }

  private void cleanup() {
    logger.info("cleanup", "DELETING TEST FOLDER");
    service.delete(TEST_ROOT);
  }

  @Ignore("test temporaneo per verificare il download di un file particolare")
  @Test
  public void testDownloadFileGrosso()
      throws InterruptedException, ExecutionException {
    String uuid = "7d320e2a-1dc4-11eb-b35e-93d341e49ce9";

    dump("READING PHASE 0 START");
    var entity = service.find(uuid);
    assertNotNull("il nodo deve esistere", entity);

    var contentData = entity.getContentData();
    dump("READING PHASE 0 END");

    assertNotNull("contentData deve esistere", contentData);

    assertTrue(StringUtils.isNotBlank(contentData.getContentType()));
    assertTrue(StringUtils.isNotBlank(contentData.getFileName()));
    assertTrue(contentData.getFileSize() > 90 * 1024 * 1024L);
    assertNotNull(contentData.getContentStream());

    dump("READING PHASE 1 START");
    byte[] content = entity.getContent();
    dump("READING PHASE 1 END");

    assertTrue("deve essere maggiore di 90 MB", content.length > 90 * 1024 * 1024);

    dump("READING PHASE 2 START");

    byte[] content2 = Index2MtomDownloadHelper.downloadWithTimeout(contentData.getContentStream(),
        300, TimeUnit.SECONDS);

    dump("READING PHASE 2 END");

    assertTrue("deve essere maggiore di 90 MB", content2.length > 90 * 1024 * 1024);
  }

  @Test
  public void testFindFolder() {
    createEntity();

    CosmoPraticaIndex folder = service.findFolder(TEST_ROOT, CosmoPraticaIndex.class);

    assertNotNull(folder);
  }

  @Test
  public void testCaseNamingForFolders() {
    CosmoDocumentoIndex entity = buildEntity();

    String pathOne = TEST_ROOT + "/naming-test/folder-one";
    String pathTwo = TEST_ROOT + "/naming-test/Folder-one/test2";

    service.createFolder(pathOne);
    service.create(pathTwo, entity);
  }

  @Test
  public void testGenericPojos() {

    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename("testfile-generic.txt");
    documento.setMimeType("application/text");
    documento.setContent("hello world".getBytes());

    documento = service.create(TEST_ROOT, documento);

    assertNotNull(documento.getUid());

    GenericIndexContent documentoSalvato =
        service.find(documento.getUid(), GenericIndexContent.class);

    assertEquals(documento.getUid(), documentoSalvato.getUid());
    assertNotNull(documentoSalvato.getUid());

    GenericIndexFolder genericFolder = service.findFolder(TEST_ROOT, GenericIndexFolder.class);

    assertNotNull(genericFolder.getUid());
  }

  @Test
  public void testEntityCreate() {

    CosmoDocumentoIndex documento = createEntity();

    assertNotNull(documento.getUid());

    CosmoDocumentoIndex documentoSalvato =
        service.find(documento.getUid(), CosmoDocumentoIndex.class);

    assertEquals(documento.getUid(), documentoSalvato.getUid());
    assertNotNull(documentoSalvato.getUid());
  }

  private CosmoDocumentoIndex buildEntity() {
    CosmoDocumentoIndex documento = new CosmoDocumentoIndex();

    documento.setFilename("testfile.txt");
    documento.setMimeType("application/text");
    documento.setContent("hello world".getBytes());
    documento.setDescrizione("descrizione documento (prima versione)");
    documento.setIdDocumentoCosmo(1L);
    documento.setTipoDocumento("P");

    return documento;
  }

  private CosmoDocumentoIndex createEntity() {
    CosmoDocumentoIndex documento = buildEntity();

    documento = service.create(TEST_ROOT, documento);

    assertNotNull(documento.getUid());
    assertNotNull(documento.getFilename());
    assertNotNull(documento.getDescrizione());
    assertNotNull(documento.getIdDocumentoCosmo());
    assertNotNull(documento.getTipoDocumento());
    assertNotNull(documento.getMimeType());
    assertNotNull(documento.getVersionable());

    return documento;
  }

  @Test
  public void testStreamingUpload() {
    CosmoDocumentoIndex documento = buildEntity();

    byte[] contentBA = documento.getContent();
    documento.setContent(null);

    documento = service.create(TEST_ROOT, documento, new ByteArrayInputStream(contentBA));

    assertNotNull(documento.getUid());
    assertNotNull(documento.getFilename());
    assertNotNull(documento.getDescrizione());
    assertNotNull(documento.getIdDocumentoCosmo());
    assertNotNull(documento.getTipoDocumento());
    assertNotNull(documento.getMimeType());
    assertNotNull(documento.getVersionable());

    var downloaded = service.find(documento.getUid()).getContent();
    assertTrue(sameBytes(contentBA, downloaded));
  }

  @Test
  public void testEntityCheckOutCheckIn() {

    CosmoDocumentoIndex created = createEntity();
    assertFalse(created.getWorkingCopy().isWorkingCopy());

    CosmoDocumentoIndex checkedOut = service.checkOut(created);

    assertTrue(checkedOut.getWorkingCopy().isWorkingCopy());
    assertNotEquals(created.getUid(), checkedOut.getUid());

    CosmoDocumentoIndex checkedIn = service.checkIn(checkedOut);
    assertFalse(checkedIn.getWorkingCopy().isWorkingCopy());

    assertEquals(created.getUid(), checkedIn.getUid());
  }

  @Test(expected = Index2NodeLockedException.class)
  public void testEntityWriteFailOnCheckedOut() {

    CosmoDocumentoIndex created = createEntity();

    CosmoDocumentoIndex checkedOut = service.checkOut(created);

    created.setDescrizione("tentativo di scrittura su elemento checked out");

    try {
      service.save(created);
    } finally {
      service.cancelCheckout(checkedOut);
    }
  }

  @Test
  public void testEntityWriteOnCancelledCheckedOut() {

    CosmoDocumentoIndex created = createEntity();

    CosmoDocumentoIndex checkedOut = service.checkOut(created);
    service.cancelCheckout(checkedOut);

    String newDesc = "tentativo di scrittura su elemento checked out";

    created.setDescrizione(newDesc);
    CosmoDocumentoIndex updated = service.save(created);

    assertEquals(updated.getDescrizione(), newDesc);

    assertEquals(service.find(updated.getUid(), CosmoDocumentoIndex.class).getDescrizione(),
        newDesc);
  }

  @Test
  public void testEntityVersioning() {

    CosmoDocumentoIndex created = createEntity();
    CosmoDocumentoIndex checkedOut;
    CosmoDocumentoIndex checkedIn;

    assertEquals(created.getVersionable().getVersionLabel(), "1.0");

    checkedOut = service.checkOut(created);
    checkedOut.setDescrizione("versione 1.1");
    service.save(checkedOut);
    checkedIn = service.checkIn(checkedOut);

    assertEquals(checkedIn.getDescrizione(), checkedOut.getDescrizione());
    assertEquals(checkedIn.getVersionable().getVersionLabel(), "1.1");

    checkedOut = service.checkOut(created);
    checkedOut.setDescrizione("versione 1.2");
    service.save(checkedOut);
    checkedIn = service.checkIn(checkedOut);

    assertEquals(checkedIn.getDescrizione(), checkedOut.getDescrizione());
    assertEquals(checkedIn.getVersionable().getVersionLabel(), "1.2");
  }

  @Test
  public void testEntityPreviousVersionRetrieve() {
    String methodName = "testEntityPreviousVersionRetrieve";

    CosmoDocumentoIndex version10 = createEntity();
    CosmoDocumentoIndex checkedOut;

    assertEquals(version10.getVersionable().getVersionLabel(), "1.0");

    byte[] v10content = version10.getContent();

    checkedOut = service.checkOut(version10);
    checkedOut.setDescrizione("versione 1.1");
    service.save(checkedOut);
    CosmoDocumentoIndex version11 = service.checkIn(checkedOut);

    assertEquals(version11.getDescrizione(), checkedOut.getDescrizione());
    assertEquals(version11.getVersionable().getVersionLabel(), "1.1");

    byte[] v11content = version11.getContent();

    checkedOut = service.checkOut(version11);
    checkedOut.setDescrizione("versione 1.2");
    checkedOut.setContent("content version 2".getBytes());
    service.save(checkedOut);
    CosmoDocumentoIndex version12 = service.checkIn(checkedOut);

    assertEquals(version12.getDescrizione(), checkedOut.getDescrizione());
    assertEquals(version12.getVersionable().getVersionLabel(), "1.2");

    byte[] v12content = version12.getContent();

    List<IndexEntityVersion<CosmoDocumentoIndex>> previousVersions =
        service.getPreviousVersions(version12);

    assertEquals(previousVersions.size(), 3);

    CosmoDocumentoIndex retrievedVersion10 = previousVersions.get(0).getEntity();

    CosmoDocumentoIndex retrievedVersion11 = previousVersions.get(1).getEntity();

    CosmoDocumentoIndex retrievedVersion12 = previousVersions.get(2).getEntity();

    byte[] rv10content = retrievedVersion10.getContent();

    logger.debug(methodName, "v10 content: " + new String(v10content));
    logger.debug(methodName, "rv10 content: " + new String(rv10content));

    assertEquals(version10.getDescrizione(), retrievedVersion10.getDescrizione());
    assertEquals(contentToString(v10content), contentToString(retrievedVersion10.getContent()));
    assertEquals(version10.getVersionable().getVersionLabel(),
        retrievedVersion10.getVersionable().getVersionLabel());

    byte[] rv11content = retrievedVersion11.getContent();

    logger.debug(methodName, "v11 content: " + new String(v11content));
    logger.debug(methodName, "rv11 content: " + new String(rv11content));

    assertEquals(version11.getDescrizione(), retrievedVersion11.getDescrizione());
    assertEquals(contentToString(version11.getContent()),
        contentToString(retrievedVersion11.getContent()));
    assertEquals(version11.getVersionable().getVersionLabel(),
        retrievedVersion11.getVersionable().getVersionLabel());

    byte[] rv12content = retrievedVersion12.getContent();

    logger.debug(methodName, "v12 content: " + new String(v12content));
    logger.debug(methodName, "rv12 content: " + new String(rv12content));

    assertEquals(version12.getDescrizione(), retrievedVersion12.getDescrizione());
    assertEquals(contentToString(version12.getContent()),
        contentToString(retrievedVersion12.getContent()));
    assertEquals(version12.getVersionable().getVersionLabel(),
        retrievedVersion12.getVersionable().getVersionLabel());
  }

  private String contentToString(byte[] raw) {
    return new String(raw, StandardCharsets.UTF_8);
  }

  private boolean sameBytes(byte[] a, byte[] b) {
    return a != null && b != null
        && Base64.getEncoder().encodeToString(a).equals(Base64.getEncoder().encodeToString(b));
  }
}
