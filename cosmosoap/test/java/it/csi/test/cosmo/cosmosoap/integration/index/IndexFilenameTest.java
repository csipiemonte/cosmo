/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.IOException;
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
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexFolder;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class IndexFilenameTest extends ParentUnitTest {

  public static final String TEST_ROOT = "test/IndexFilenameTest";

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
  public void testFilenameConflitto() throws Exception {
    CosmoDocumentoIndex entity = createEntity();
    dump("ENTITY CREATA #1", entity);

    CosmoDocumentoIndex entity2 = createEntity();
    dump("ENTITY CREATA #2", entity2);

    CosmoDocumentoIndex entity3 = createEntity();
    dump("ENTITY CREATA #3", entity3);
  }

  @Test
  public void testFilenameCreazioneModifica() throws Exception {
    CosmoDocumentoIndex entity = createEntity();
    dump("ENTITY CREATA", entity);

    String remoteName1 = entity.getRemoteName();
    assertNotNull(remoteName1);

    entity.setFilename(entity.getFilename() + " (mod 1)");
    entity.setDescrizione("entity modificata passo 1");
    entity.setIdDocumentoCosmo(111L);
    entity = index2Service.save(entity);

    dump("ENTITY MODIFICATA", entity);

    String remoteName2 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(remoteName1, remoteName2);
  }

  @Test
  public void testFilenameSenzaContentModelCreazioneModifica() throws Exception {
    var input = getSample("prova-dosign.p7m");
    GenericIndexContent entity = upload(input);
    dump("ENTITY CREATA", entity);

    String remoteName1 = entity.getRemoteName();
    assertNotNull(remoteName1);

    entity.setFilename(entity.getFilename() + " (raw mod 1)");
    entity = index2Service.save(entity);

    dump("ENTITY MODIFICATA", entity);

    String remoteName2 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(remoteName1, remoteName2);
  }

  @Test
  public void testFoldernameCreazioneModifica() throws Exception {
    String folderName = "prova_folder";
    var entity = new GenericIndexFolder();
    entity.setFoldername(folderName);

    entity = index2Service.createFolder(TEST_ROOT, entity);
    dump("FOLDER CREATA", entity);

    String remoteName1 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(folderName, entity.getFoldername());

    String folderNameAfter = entity.getFoldername() + " (mod 1)";

    entity.setFoldername(folderNameAfter);
    entity = index2Service.saveFolder(entity);

    dump("FOLDER MODIFICATA", entity);

    String remoteName2 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(remoteName1, remoteName2);
    assertEquals(folderNameAfter, entity.getFoldername());

    // cerca by path originale
    var found = index2Service.findFolder(TEST_ROOT + "/" + folderName, GenericIndexFolder.class);
    assertNotNull(found);
    assertEquals(folderNameAfter, found.getFoldername());
  }

  @Test
  public void testFoldernameWithoutContentModeCreazioneModifica() throws Exception {
    String folderName = "prova_folder";
    String entityUUID = index2Service.createFolder(TEST_ROOT + "/" + folderName);
    GenericIndexFolder entity = index2Service.findFolder(entityUUID, GenericIndexFolder.class);
    dump("FOLDER CREATA", entity);

    String remoteName1 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(folderName, entity.getFoldername());

    String folderNameAfter = entity.getFoldername() + " (mod 1)";

    entity.setFoldername(folderNameAfter);
    entity = index2Service.saveFolder(entity);

    dump("FOLDER MODIFICATA", entity);

    String remoteName2 = entity.getRemoteName();
    assertNotNull(remoteName1);
    assertEquals(remoteName1, remoteName2);
    assertEquals(folderNameAfter, entity.getFoldername());

    // cerca by path originale
    var found = index2Service.findFolder(TEST_ROOT + "/" + folderName, GenericIndexFolder.class);
    assertNotNull(found);
    assertEquals(folderNameAfter, found.getFoldername());
  }

  private CosmoDocumentoIndex createEntity() {
    CosmoDocumentoIndex documento = buildEntity();

    documento = index2Service.create(TEST_ROOT, documento);

    assertNotNull(documento.getUid());
    assertNotNull(documento.getFilename());
    assertNotNull(documento.getDescrizione());
    assertNotNull(documento.getIdDocumentoCosmo());
    assertNotNull(documento.getTipoDocumento());
    assertNotNull(documento.getMimeType());
    assertNotNull(documento.getVersionable());

    return documento;
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
