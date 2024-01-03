/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmoecm.business.service.FileService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.test.cosmo.cosmoecm.business.service.impl.FileServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
public class FileServiceImplTest extends ParentIntegrationTest {

  private static final String MIME_TYPE_PDF = "application/pdf";

  @Autowired
  private FileService fileService;

  @Autowired
  private FileShareService mockFileShareService;

  @Autowired
  private CosmoSoapIndexFeignClient mockIndexFeignClient;

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public FileShareService fileShareService() {
      return Mockito.mock(FileShareService.class);
    }

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient indexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

  }

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test(expected = BadRequestException.class)
  public void downloadDocumentoErroreParametroNonValorizzatoCorrettamente() {
    fileService.downloadDocumento("");
  }

  @Test(expected = NotFoundException.class)
  public void downloadDocumentoErroreDocumentoNonTrovato() {
    fileService.downloadDocumento("123");
  }

  @Test(expected = NotFoundException.class)
  public void downloadDocumentoErroreNessunContenutoDaScaricare() {
    fileService.downloadDocumento("3");
  }

  @Test(expected = NotFoundException.class)
  public void downloadDocumentoContenutoOriginaleErroreNessunContenutoDaScaricare() {
    setUpMockError();
    fileService.downloadDocumento("1");
  }

  @Test
  public void downloadDocumentoContenutoOriginaleOK() {
    setUpMock();
    var result = fileService.downloadDocumento("1");
    assertNotNull(result);
    assertNotNull(result.getContent());
  }

  @Test
  public void downloadDocumentoContenutoTemporaneoOK() {
    setUpMock();
    var result = fileService.downloadDocumento("11");
    assertNotNull(result);
    assertNotNull(result.getContent());
  }

  @Test(expected = BadRequestException.class)
  public void cancellaSuFileSystemErroreParametroNonValorizzatoCorrettamente() {
    fileService.deleteFileOnFileSystem("");
  }

  @Test
  public void cancellaSuFileSystem() {
    setUpMock();
    fileService.deleteFileOnFileSystem("uuid");
  }

  @Test(expected = BadRequestException.class)
  public void previewDocumentoErroreParametroNonValorizzatoCorrettamente() {
    fileService.previewDocumento("");
  }

  @Test(expected = NotFoundException.class)
  public void previewDocumentoErroreDocumentoNullo() {
    fileService.previewDocumento("123");
  }

  @Test(expected = NotFoundException.class)
  public void previewDocumentoErroreNessunContenutoDaScaricare() {
    setUpMockError();
    fileService.previewDocumento("1");
  }

  private void setUpMock() {
    var entity = new Entity();
    var content = new byte[10];
    entity.setContent(content);
    reset(mockFileShareService, mockIndexFeignClient);
    when(mockFileShareService.get(any(), any())).thenReturn(new RetrievedContentTest());
    doNothing().when(mockFileShareService).delete(any());
    when(mockIndexFeignClient.getFile(any(), any())).thenReturn(entity);
  }

  private void setUpMockError() {
    var entity = new Entity();
    reset(mockFileShareService, mockIndexFeignClient);
    when(mockIndexFeignClient.getFile(any(), any())).thenReturn(entity);
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
      return null;
    }
  }

}
