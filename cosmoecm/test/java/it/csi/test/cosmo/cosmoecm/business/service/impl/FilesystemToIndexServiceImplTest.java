/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmoecm.business.service.EventService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.FilesystemToIndexService;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.test.cosmo.cosmoecm.business.service.impl.FilesystemToIndexServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class FilesystemToIndexServiceImplTest extends ParentIntegrationTest {


  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient cosmoSoapIndexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

    @Bean
    @Primary
    public FileShareService fileShareService() {
      return Mockito.mock(FileShareService.class);
    }

    @Bean
    @Primary
    public CosmoNotificationsNotificheGlobaliFeignClient cosmoNotificationsNotificheGlobaliFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheGlobaliFeignClient.class);
    }

    @Bean
    @Primary
    public EventService eventService() {
      return Mockito.mock(EventService.class);
    }

  }

  @Autowired
  private FilesystemToIndexService filesystemToIndexService;

  @Autowired
  private CosmoSoapIndexFeignClient mockCosmoSoapIndexFeignClient;

  @Autowired
  private FileShareService mockFileShareService;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient mockCosmoNotificationsNotificheGlobaliFeignClient;

  @Autowired
  private EventService mockEventService;

  private static final String MIME_TYPE_PDF = "application/pdf";

  private static final String EFFECTIVE_PATH = "effectivePath";

  private static final String FILENAME = "filename";

  private static final String DOWNLOAD_URI = "downloadUri";

  private static final String PRATICA_FOLDER_UUID = "praticaFolderUUID";


  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  private void setUpMockMigrazione() {
    var folder = new Folder();
    folder.setEffectivePath(EFFECTIVE_PATH);
    var entity = new Entity();
    entity.setUid("uid");
    entity.setFilename(FILENAME);
    var ffi = new FileFormatInfo();
    var sharedLink = new SharedLink();
    sharedLink.setDownloadUri(DOWNLOAD_URI);
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService, mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.creaFileIndex(any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(ffi);
    when(mockCosmoSoapIndexFeignClient.creaFile(any(), any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(sharedLink);
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(entity);
    when(mockFileShareService.get(any(), any())).thenReturn(new RetrievedContentTest());
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
  }

  @Test
  @Commit
  public void migraDocumento() {
    this.setUpMockMigrazione();
    var documento = filesystemToIndexService.findDaMigrare(1, 1);
    assertNotNull(documento);
    assertTrue(documento.size() == 1);
    var risultatoMigrazione = filesystemToIndexService.migraDocumento(documento.get(0));
    assertNotNull(risultatoMigrazione);
    assertTrue(risultatoMigrazione.isSuccesso());
    var doc = filesystemToIndexService.findDaMigrare(1, 1);
    assertTrue(!doc.isEmpty());
  }

  @Test(expected = ConflictException.class)
  public void migraDocumentoInsideLockLockNullo() {
    filesystemToIndexService.migraDocumentiInsideLock(null, null, null);
  }

  @Test(expected = ConflictException.class)
  public void migraDocumentoInsideLockLockCancellato() {
    var lockCancellato = new CosmoTLock();
    lockCancellato.setDtCancellazione(Timestamp.from(Instant.now()));
    filesystemToIndexService.migraDocumentiInsideLock(lockCancellato, null, null);
  }

  @Test(expected = ConflictException.class)
  public void migraDocumentoInsideLockLockScaduto() {
    var lockScaduto = new CosmoTLock();
    lockScaduto.setDtScadenza(Timestamp.from(Instant.now().minusSeconds(1L)));
    filesystemToIndexService.migraDocumentiInsideLock(lockScaduto, null, null);
  }

  @Test(expected = NullPointerException.class)
  @Commit
  public void tentaMigrazioneDocumentoInesistente() {
    var documento = new CosmoTDocumento();
    documento.setId(123L);
    filesystemToIndexService.migraDocumento(documento);
  }

  @Test
  @Commit
  public void tentaMigrazioneDocumentoErroreCreazioneCartellaIndex() {
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService,
        mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenThrow(new UnexpectedResponseException());
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var risultato = filesystemToIndexService.migraDocumento(documento);
    assertTrue(!risultato.isSuccesso());
  }

  @Test
  @Commit
  public void tentaMigrazioneDocumentoErroreControlloEsistenzaNodoAnteprime() {
    var folder = new Folder();
    folder.setEffectivePath(EFFECTIVE_PATH);
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService,
        mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID).thenThrow(new UnexpectedResponseException());
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder).thenReturn(null);
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var risultato = filesystemToIndexService.migraDocumento(documento);
    assertTrue(!risultato.isSuccesso());
  }

  @Test
  @Commit
  public void tentaMigrazioneDocumentoErroreContenutoDocumentoVuoto() {
    var folder = new Folder();
    folder.setEffectivePath(EFFECTIVE_PATH);
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService,
        mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documento = new CosmoTDocumento();
    documento.setId(4L);
    var risultato = filesystemToIndexService.migraDocumento(documento);
    assertTrue(!risultato.isSuccesso());
  }

  @Test
  @Commit
  public void migraDocumentoFormatoFileInfoFirmato() {
    var folder = new Folder();
    folder.setEffectivePath(EFFECTIVE_PATH);
    var entity = new Entity();
    entity.setUid("uid");
    entity.setFilename(FILENAME);
    var ffi = new FileFormatInfo();
    ffi.setMimeType(List.of(MIME_TYPE_PDF));
    ffi.setSigned(true);
    ffi.setSignatureType("PDF");
    var sharedLink = new SharedLink();
    sharedLink.setDownloadUri(DOWNLOAD_URI);
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService,
        mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.creaFileIndex(any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.creaFile(any(), any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(ffi);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(sharedLink);
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(entity);
    when(mockFileShareService.get(any(), any())).thenReturn(new RetrievedContentTest());
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documento = new CosmoTDocumento();
    documento.setId(10L);
    var risultato = filesystemToIndexService.migraDocumento(documento);
    assertTrue(risultato.isSuccesso());
  }

  @Test
  @Commit
  public void migraDocumentoFormatoFileInfoBustato() {
    var folder = new Folder();
    folder.setEffectivePath(EFFECTIVE_PATH);
    var entity = new Entity();
    entity.setUid("uid");
    entity.setFilename(FILENAME);
    var ffi = new FileFormatInfo();
    ffi.setMimeType(List.of(MIME_TYPE_PDF));
    ffi.setSigned(true);
    ffi.setSignatureType("PCKS");
    var sharedLink = new SharedLink();
    sharedLink.setDownloadUri(DOWNLOAD_URI);
    reset(mockCosmoSoapIndexFeignClient, mockFileShareService,
        mockCosmoNotificationsNotificheGlobaliFeignClient, mockEventService);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.creaFileIndex(any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.creaFile(any(), any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(ffi);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(sharedLink);
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.estraiBusta(any(), any())).thenReturn(entity);
    when(mockFileShareService.get(any(), any())).thenReturn(new RetrievedContentTest());
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documento = new CosmoTDocumento();
    documento.setId(11L);
    var risultato = filesystemToIndexService.migraDocumento(documento);
    assertTrue(!risultato.isSuccesso());
  }

  private class RetrievedContentTest implements RetrievedContent {

    @Override
    public Path getWorkingFolder() {
      return null;
    }

    @Override
    public InputStream getContentStream() {
      InputStream singlePDF = DocumentoServiceImplTest.class
          .getResourceAsStream("/samplefiles/test_pdf-be23cf38-e75b-4a08-9ecd-e19d67f380c9.pdf");

      assertNotNull(singlePDF);
      return singlePDF;
    }

    @Override
    public String getUploadUUID() {
      return null;
    }

    @Override
    public String getFilename() {
      return "test_pdf-be23cf38-e75b-4a08-9ecd-e19d67f380c9.pdf";
    }

    @Override
    public String getContentType() {
      return MIME_TYPE_PDF;
    }

    @Override
    public Long getContentSize() {
      return 1L;
    }

    @Override
    public ZonedDateTime getUploadedAt() {
      return ZonedDateTime.now();
    }
  }
}
