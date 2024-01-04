/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.index;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.exception.BadConfigurationException;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexShareDetail;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.GenericIndexContent;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.CreatedSharedLink;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareOptions;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.sharing.IndexShareScope;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class GetSharesTest extends ParentUnitTest {

  private static final String TEST_PDF = "cosmo-extracted.pdf";

  public static final String TEST_ROOT = "test/GetShareTest";

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

    dump("shareResult", shareResult);

    // find del documento per verificare che ci sia l'attribute
    var found = index2Service.find(sourceEntity.getUid());
    assertNotNull(found.getShared());
    assertNotNull(found.getShared().getSharedLinks());
    assertEquals(1, found.getShared().getSharedLinks().length);

    dump("entity", found);

    var sharedLink = found.getShared().getSharedLinks()[0];

    assertEquals(options.getFromDate().truncatedTo(ChronoUnit.SECONDS),
        sharedLink.getFromDate());
    assertEquals(options.getToDate().truncatedTo(ChronoUnit.SECONDS),
        sharedLink.getToDate());
    assertEquals(options.getSource(), sharedLink.getSource());

    assertEquals(shareResult.getContentDisposition(), sharedLink.getContentDisposition());
    assertTrue(shareResult.getFromDate().until(sharedLink.getFromDate(), ChronoUnit.MILLIS) < 1000);
    assertTrue(shareResult.getToDate().until(sharedLink.getToDate(), ChronoUnit.MILLIS) < 1000);
    assertEquals(shareResult.getSource(), sharedLink.getSource());

    // get delle share
    List<IndexShareDetail> shares = index2Service.getShares(sourceEntity);

    dump("shares", shares);

    assertEquals(1, shares.size());
    var share = shares.get(0);

    assertEquals(shareResult.getContentDisposition(), share.getContentDisposition());
    assertEquals(shareResult.getDownloadUri(), share.getDownloadUri());
    assertEquals(shareResult.getSource(), share.getSource());
    assertTrue(shareResult.getFromDate().until(share.getFromDate(), ChronoUnit.MILLIS) < 1000);
    assertTrue(shareResult.getToDate().until(share.getToDate(), ChronoUnit.MILLIS) < 1000);
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

}
