/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStream;
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
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.MailService;
import it.csi.cosmo.cosmoecm.business.service.StreamDataToIndexService;
import it.csi.cosmo.cosmoecm.dto.exception.UnexpectedResponseException;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FileFormatInfo;
import it.csi.cosmo.cosmosoap.dto.rest.Folder;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.test.cosmo.cosmoecm.business.service.impl.StreamDataToIndexServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class StreamDataToIndexServiceImplTest extends ParentIntegrationTest {


  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient cosmoSoapIndexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

    @Bean
    @Primary
    public MailService mailService() {
      return Mockito.mock(MailService.class);
    }

  }

  @Autowired
  private StreamDataToIndexService streamDataToIndexService;

  @Autowired
  private CosmoSoapIndexFeignClient mockCosmoSoapIndexFeignClient;


  @Autowired
  private MailService mockMailService;

  private static final String FAKE_LINK = "fakeLink";

  private static final String FILENAME = "filename";

  private static final String LINK = "https://www.regione.piemonte.it/web/media/37773/download";

  private static final String DOWNLOAD_URI = "downloadUri";

  private static final String PRATICA_FOLDER_UUID = "praticaFolderUUID";

  private Folder folder = new Folder();

  private FileFormatInfo fileFormatInfo = new FileFormatInfo();

  private Entity entity = new Entity();

  private SharedLink sharedLink = new SharedLink();

  private InputStream singlePDF = DocumentoServiceImplTest.class
      .getResourceAsStream("/samplefiles/test_pdf-be23cf38-e75b-4a08-9ecd-e19d67f380c9.pdf");


  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  @Commit
  public void migraDocumentoErroreVerifichePreliminariDocumentoNullo() {
    reset(mockMailService);
    when(mockMailService.inviaMailAssistenza(any(), any())).thenReturn(null);
    var documento = new CosmoTDocumento();
    documento.setId(0L);
    var res = streamDataToIndexService.migraDocumento(documento, FAKE_LINK);
    assertNotNull(res);
    assertTrue(!res.isSuccesso());
    assertTrue(res.getErrore() instanceof NotFoundException);
  }

  @Test
  @Commit
  public void migraDocumentoErroreVerifichePreliminariCreazioneCartellaSuIndex() {
    reset(mockMailService, mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder).thenThrow(new UnexpectedResponseException());
    when(mockMailService.inviaMailAssistenza(any(), any())).thenReturn(null);
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var res = streamDataToIndexService.migraDocumento(documento, FAKE_LINK);
    assertNotNull(res);
    assertTrue(!res.isSuccesso());
    assertTrue(res.getErrore() instanceof UnexpectedResponseException);
  }

  @Test
  public void migraDocumento() {
    this.fileFormatInfo.setMimeType(List.of("PDF"));
    reset(mockMailService, mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(this.fileFormatInfo);
    when(mockMailService.inviaMailAssistenza(any(), any())).thenReturn(null);
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var res = streamDataToIndexService.migraDocumento(documento, FAKE_LINK);
    assertNotNull(res);
  }

  @Test
  public void migraDocumentoGenerazioneAnteprimeInErroreMigrazioneEffettuata() throws IOException {
    this.fileFormatInfo.setMimeType(List.of("PDF"));
    this.entity.setUid("uid");
    this.entity.setContent(this.singlePDF.readAllBytes());
    this.sharedLink.setDownloadUri(DOWNLOAD_URI);
    reset(mockMailService, mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(this.fileFormatInfo);
    when(mockCosmoSoapIndexFeignClient.creaFileIndex(any())).thenReturn(this.entity);
    when(mockMailService.inviaMailAssistenza(any(), any())).thenReturn(null);
    var documento = new CosmoTDocumento();
    documento.setId(11L);
    var res = streamDataToIndexService.migraDocumento(documento, FAKE_LINK);
    assertNotNull(res);
    assertTrue(res.isSuccesso());
  }

  @Test
  public void migraDocumento1() throws IOException {
    this.fileFormatInfo.setMimeType(List.of("PDF"));
    this.entity.setUid("uid");
    this.entity.setContent(this.singlePDF.readAllBytes());
    this.entity.setFilename(FILENAME);
    reset(mockMailService, mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.createFolder(any())).thenReturn(PRATICA_FOLDER_UUID);
    when(mockCosmoSoapIndexFeignClient.findFolder(any())).thenReturn(folder);
    when(mockCosmoSoapIndexFeignClient.getInfoFormatoFile(any())).thenReturn(this.fileFormatInfo);
    when(mockCosmoSoapIndexFeignClient.creaFileIndex(any())).thenReturn(this.entity);
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(this.entity);
    when(mockCosmoSoapIndexFeignClient.creaFile(any(), any())).thenReturn(this.entity);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(this.sharedLink);
    when(mockMailService.inviaMailAssistenza(any(), any())).thenReturn(null);
    var documento = new CosmoTDocumento();
    documento.setId(11L);
    var res = streamDataToIndexService.migraDocumento(documento, LINK);
    assertNotNull(res);
    assertTrue(res.isSuccesso());
  }


}
