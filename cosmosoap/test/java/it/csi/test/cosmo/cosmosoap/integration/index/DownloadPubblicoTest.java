/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexContentDisposition;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class DownloadPubblicoTest extends ParentUnitTest {

  private static final String TEST_PDF = "cosmo-extracted.pdf";

  public static final String TEST_ROOT = "test/DownloadPubblicoTest";

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
  public void testShareWithDatesExpiration() throws Exception {
    var input = getSample(TEST_PDF);
    GenericIndexContent sourceEntity = upload(input, "application/pdf", "testfile.pdf");
    assertNull(sourceEntity.getShared().getSharedLinks());

    //@formatter:off
    IndexShareOptions options = IndexShareOptions.builder()
        .withFromDate(OffsetDateTime.now().minusDays(1))
        .withToDate(OffsetDateTime.now().plusSeconds(4))
        .withSource(IndexShareScope.INTERNET)
        .build();
    //@formatter:on

    CreatedSharedLink shareResult = index2Service.share(sourceEntity, options);
    assertNotNull(shareResult);
    assertNotNull(shareResult.getDownloadUri());
    assertEquals(options.getFromDate(), shareResult.getFromDate());
    assertEquals(options.getToDate(), shareResult.getToDate());
    assertEquals(options.getSource(), shareResult.getSource());

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);
    assertEquals(options.getFromDate().truncatedTo(ChronoUnit.SECONDS),
        found.getShared().getSharedLinks()[0].getFromDate());
    assertEquals(options.getToDate().truncatedTo(ChronoUnit.SECONDS),
        found.getShared().getSharedLinks()[0].getToDate());
    assertEquals(options.getSource(), found.getShared().getSharedLinks()[0].getSource());

    // prova il download, mi aspetto che fallisca
    sleep(5000);
    expect(() -> download(shareResult.getDownloadUri()), HttpStatusCodeException.class);
  }

  @Test
  public void testUnShare() throws Exception {
    var input = getSample(TEST_PDF);
    GenericIndexContent sourceEntity = upload(input, "application/pdf", "testfile.pdf");
    assertNull(sourceEntity.getShared().getSharedLinks());

    byte[] uploadedContent = input;

    // creo tre link di share
    CreatedSharedLink shareResult1 = index2Service.share(sourceEntity);
    CreatedSharedLink shareResult2 = index2Service.share(sourceEntity);
    CreatedSharedLink shareResult3 = index2Service.share(sourceEntity);

    assertNotEquals(shareResult1.getDownloadUri(), shareResult2.getDownloadUri());
    assertNotEquals(shareResult1.getDownloadUri(), shareResult3.getDownloadUri());
    assertNotEquals(shareResult2.getDownloadUri(), shareResult3.getDownloadUri());

    // find del documento per verificare che ci siano tre attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(3, found.getShared().getSharedLinks().length);

    // prova il download

    ResponseEntity<byte[]> downloadResult1 = download(shareResult1.getDownloadUri());
    ResponseEntity<byte[]> downloadResult2 = download(shareResult2.getDownloadUri());
    ResponseEntity<byte[]> downloadResult3 = download(shareResult3.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult1.getBody()));
    assertTrue(sameBytes(uploadedContent, downloadResult2.getBody()));
    assertTrue(sameBytes(uploadedContent, downloadResult3.getBody()));

    // unshare link one
    index2Service.unshare(found, shareResult1.getDownloadUri());

    // mi aspetto due voci ora
    found = index2Service.find(sourceEntity.getUid());
    assertEquals(2, found.getShared().getSharedLinks().length);

    // mi aspetto che fallisca il download numero uno ma che gli altri riescano di nuovo

    expect(() -> download(shareResult1.getDownloadUri()), HttpStatusCodeException.class);
    downloadResult2 = download(shareResult2.getDownloadUri());
    downloadResult3 = download(shareResult3.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult2.getBody()));
    assertTrue(sameBytes(uploadedContent, downloadResult3.getBody()));

    // unshare dall'oggetto
    index2Service.unshare(found, found.getShared().getSharedLinks()[0]);

    expect(() -> download(shareResult1.getDownloadUri()), HttpStatusCodeException.class);
    expect(() -> download(shareResult2.getDownloadUri()), HttpStatusCodeException.class);
    downloadResult3 = download(shareResult3.getDownloadUri());

    // unshare di tutto il doc
    index2Service.unshare(found);

    // mi aspetto zero voci ora
    found = index2Service.find(sourceEntity.getUid());
    assertNull(found.getShared().getSharedLinks());

    // mi aspetto che falliscano anche gli altri due ora

    expect(() -> download(shareResult2.getDownloadUri()), HttpStatusCodeException.class);
    expect(() -> download(shareResult3.getDownloadUri()), HttpStatusCodeException.class);
  }

  @Test
  public void testDownloadFilename() throws Exception {
    var input = getSample(TEST_PDF);
    GenericIndexContent sourceEntity = upload(input, "application/pdf", "testfile.pdf");
    assertNull(sourceEntity.getShared().getSharedLinks());

    byte[] uploadedContent = input;
    var requestedDownloadFilename = "custom-name.pdf";

    CreatedSharedLink shareResult = index2Service.share(sourceEntity, IndexShareOptions.builder()
        .withContentDisposition(IndexContentDisposition.INLINE)
        .withFilename(requestedDownloadFilename)
        .build()
        );
    assertNotNull(shareResult);
    assertNotNull(shareResult.getDownloadUri());
    assertNull(shareResult.getFromDate());
    assertNull(shareResult.getToDate());

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);

    assertEquals(sourceEntity.getFilename(), found.getFilename());

    // prova il download
    ResponseEntity<byte[]> downloadResult =
        download(shareResult.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult.getBody()));

    assertEquals("inline; filename=\"" + requestedDownloadFilename + "\"",
        downloadResult.getHeaders().get("Content-Disposition").get(0));
  }

  @Test
  public void testShareWithGenericContent() throws Exception {
    var input = getSample(TEST_PDF);
    GenericIndexContent sourceEntity = upload(input, "application/pdf", "testfile.pdf");
    assertNull(sourceEntity.getShared().getSharedLinks());

    byte[] uploadedContent = input;

    CreatedSharedLink shareResult = index2Service.share(sourceEntity);
    assertNotNull(shareResult);
    assertNotNull(shareResult.getDownloadUri());
    assertNull(shareResult.getFromDate());
    assertNull(shareResult.getToDate());

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);

    // prova il download
    ResponseEntity<byte[]> downloadResult =
        download(shareResult.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult.getBody()));
  }

  @Test
  public void testShareWithContentModel() throws Exception {
    String name = TEST_PDF;
    CosmoDocumentoIndex sourceEntity = createEntity(name);
    assertNull(sourceEntity.getShared().getSharedLinks());

    byte[] uploadedContent = getSample(name);

    CreatedSharedLink shareResult = index2Service.share(sourceEntity);
    assertNotNull(shareResult);
    assertNotNull(shareResult.getDownloadUri());
    assertNull(shareResult.getFromDate());
    assertNull(shareResult.getToDate());

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);

    // prova il download
    ResponseEntity<byte[]> downloadResult =
        download(shareResult.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult.getBody()));
  }

  @Test
  public void testShareWithDatesOnInternet() throws Exception {
    var input = getSample(TEST_PDF);
    GenericIndexContent sourceEntity = upload(input, "application/pdf", "testfile.pdf");
    assertNull(sourceEntity.getShared().getSharedLinks());

    byte[] uploadedContent = input;

    //@formatter:off
    IndexShareOptions options = IndexShareOptions.builder()
        .withFromDate(OffsetDateTime.now().minusDays(1))
        .withToDate(OffsetDateTime.now().plusHours(1))
        .withSource(IndexShareScope.INTERNET)
        .build();
    //@formatter:on

    CreatedSharedLink shareResult = index2Service.share(sourceEntity, options);
    assertNotNull(shareResult);
    assertNotNull(shareResult.getDownloadUri());
    assertEquals(options.getFromDate(), shareResult.getFromDate());
    assertEquals(options.getToDate(), shareResult.getToDate());
    assertEquals(options.getSource(), shareResult.getSource());

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);
    assertEquals(options.getFromDate().truncatedTo(ChronoUnit.SECONDS),
        found.getShared().getSharedLinks()[0].getFromDate());
    assertEquals(options.getToDate().truncatedTo(ChronoUnit.SECONDS),
        found.getShared().getSharedLinks()[0].getToDate());
    assertEquals(options.getSource(), found.getShared().getSharedLinks()[0].getSource());

    // prova il download
    ResponseEntity<byte[]> downloadResult =
        download(shareResult.getDownloadUri());

    assertTrue(sameBytes(uploadedContent, downloadResult.getBody()));
  }

  private CosmoDocumentoIndex createEntity(String file) {
    CosmoDocumentoIndex documento = buildEntity(file);

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

  private CosmoDocumentoIndex buildEntity(String file) {
    CosmoDocumentoIndex documento = new CosmoDocumentoIndex();

    documento.setFilename("testfile.pdf");
    documento.setMimeType("application/pdf");
    documento.setContent(getSample(file));
    documento.setDescrizione("descrizione documento (prima versione)");
    documento.setIdDocumentoCosmo(1L);
    documento.setTipoDocumento("P");

    return documento;
  }

  private ResponseEntity<byte[]> download(URI uri) {
    var rt = new RestTemplate();
    ResponseEntity<byte[]> downloadResult = rt.getForEntity(uri, byte[].class);

    dump("DONWLOAD HEADERS", downloadResult.getHeaders());

    return downloadResult;
  }

  private GenericIndexContent upload(byte[] content, String contentType, String filename) {
    GenericIndexContent documento = new GenericIndexContent();
    documento.setFilename(filename);
    documento.setMimeType(contentType);
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

  private boolean sameBytes(byte[] a, byte[] b) {
    return a != null && b != null
        && Base64.getEncoder().encodeToString(a).equals(Base64.getEncoder().encodeToString(b));
  }
}
