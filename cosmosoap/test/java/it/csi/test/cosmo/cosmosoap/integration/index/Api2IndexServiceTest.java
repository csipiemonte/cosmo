/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
@Transactional
public class Api2IndexServiceTest extends ParentUnitTest {

  protected static CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "Api2IndexServiceTest");

  private static final String TEST_ROOT = "test/Api2IndexServiceTest";

  private static final String TEXT_WORLD = "text world";
  @Autowired
  private Api2IndexService service;

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

  @Test
  public void creaTrovaDocumento() {

    var docCreato = service.create(TEST_ROOT, creaTXTEntity(), null);

    assertNotNull(docCreato.getUid());
    assertNotNull(docCreato.getFilename());
    assertNotNull(docCreato.getDescrizione());
    assertNotNull(docCreato.getIdDocumento());
    assertNotNull(docCreato.getTipoDocumento());
    assertNotNull(docCreato.getMimeType());
    assertNotNull(docCreato.getVersionable());
    assertNotNull(docCreato.getContent());

    var docCercato = service.find(docCreato.getUid(), true);

    assertTrue(docCreato.getFilename().equals(docCercato.getFilename()));
    assertTrue(TEXT_WORLD.equals(new String(docCercato.getContent())));

  }

  @Test
  public void createDocumentoConContenuto() {

    Entity documento = new Entity();
    documento.setFilename("testfile.pdf");
    documento.setMimeType("application/pdf");
    documento.setDescrizione("descrizione documento (prima versione)");
    documento.setIdDocumento(1L);
    documento.setTipoDocumento("P");

    var result = service.create(TEST_ROOT, documento, getContent());

    assertNotNull(result.getUid());
    assertNotNull(result.getFilename());
    assertNotNull(result.getDescrizione());
    assertNotNull(result.getIdDocumento());
    assertNotNull(result.getTipoDocumento());
    assertNotNull(result.getMimeType());
    assertNotNull(result.getVersionable());
  }

  @Test
  public void aggiornaEntity() {

    var creato = service.create(TEST_ROOT, creaTXTEntity(), null);
    creato.setFilename("Nuovo filename");

    var result = service.aggiorna(creato);
    assertTrue("Nuovo filename".equals(result.getFilename()));
  }

  @Test
  public void creaTrovaCartella() {
    String crea = service.createFolder(TEST_ROOT + "/prova_folder");
    assertNotNull(crea);

    Folder trova = service.findFolder(crea);
    assertNotNull(trova.getFoldername());
    assertNotNull(trova.getUid());

  }

  private Entity creaTXTEntity() {
    Entity documento = new Entity();
    documento.setFilename("testfile.txt");
    documento.setMimeType("application/txt");
    documento.setContent(TEXT_WORLD.getBytes());
    documento.setDescrizione("descrizione documento (prima versione)");
    documento.setIdDocumento(1L);
    documento.setTipoDocumento("P");
    return documento;
  }

  private byte[] getContent() {
    try {
      return this.getClass().getClassLoader().getResourceAsStream("samplefiles/cosmo-extracted.pdf")
          .readAllBytes();
    } catch (IOException e) {
      throw new BadConfigurationException();
    }
  }

}
