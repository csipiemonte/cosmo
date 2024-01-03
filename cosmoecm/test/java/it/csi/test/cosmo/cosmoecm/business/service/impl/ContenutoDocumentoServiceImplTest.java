/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDTipoContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.PayloadTooLargeException;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmoecm.business.service.ContenutoDocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.ShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.cosmo.cosmosoap.dto.rest.VerifyReport;
import it.csi.test.cosmo.cosmoecm.business.service.impl.ContenutoDocumentoServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class ContenutoDocumentoServiceImplTest extends ParentIntegrationTest {

  private static final String MIME_TYPE_PDF = "application/pdf";

  @Autowired
  private ContenutoDocumentoService contenutoDocumentoService;

  @Autowired
  private FileShareService mockFileShareService;

  @Autowired
  private CosmoSoapIndexFeignClient mockCosmoSoapIndexFeignClient;

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public FileShareService fileShareService() {
      return Mockito.mock(FileShareService.class);
    }

    @Bean
    @Primary
    public CosmoSoapIndexFeignClient cosmoSoapIndexFeignClient() {
      return Mockito.mock(CosmoSoapIndexFeignClient.class);
    }

  }

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void creaContenutoTemporaneo() {
    var result = contenutoDocumentoService.creaContenutoTemporaneo(new RetrievedContentTest());
    assertNotNull(result);
  }

  @Test
  public void creaContenutoOriginale() {
    setUpMock();
    var contenuto = getContenuto();
    var indexEntity = new Entity();
    var result = contenutoDocumentoService.creaContenutoOriginale(contenuto, indexEntity);
    assertNotNull(result);
  }

  @Test
  public void creaContenutoSbustato() {
    var contenuto = getContenuto();
    var indexEntity = new Entity();
    var result = contenutoDocumentoService.creaContenutoSbustato(contenuto, indexEntity);
    assertNotNull(result);
  }

  @Test
  public void creaContenutoFirmato() {
    var contenuto = getContenuto();
    var indexEntity = new Entity();
    var result = contenutoDocumentoService.creaContenutoFirmato(contenuto, indexEntity,
        getTipoFirma(), "nomeFileFirmato", getFormatoFile());
    assertNotNull(result);
  }

  @Test
  public void cercaFormatoByMimeNullo() {
    var result = contenutoDocumentoService.findFormatoByMime("");
    assertNotNull(result);
  }

  @Test(expected = BadRequestException.class)
  public void cercaFormatoByMimeErroreNonPresenteASistema() {
    contenutoDocumentoService.findFormatoByMime("application/fake;fake");
  }

  @Test
  public void cancellaContenutoTemporaneoFisicamente() {
    setUpMock();
    var contenuto = getContenuto();
    contenuto.setTipo(getTipoContenutoDocumento(
        it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.TEMPORANEO.toString()));
    contenutoDocumentoService.cancella(contenuto, true);
  }

  @Test
  public void cancellaContenutoNonTemporaneoFisicamente() {
    setUpMock();
    var contenuto = getContenuto();
    contenuto.setTipo(getTipoContenutoDocumento(
        it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString()));
    contenutoDocumentoService.cancella(contenuto, true);
  }

  @Test
  public void cancellaContenutoWarningCancellato() {
    var contenuto = getContenuto();
    contenuto.setDtCancellazione(Timestamp.from(Instant.now()));
    contenutoDocumentoService.cancella(contenuto, false);
  }

  @Test(expected = NotFoundException.class)
  public void getContenutoByIdErroreDocumentoNonPresente() {
    contenutoDocumentoService.getById(0L, 0L);
  }

  @Test(expected = NotFoundException.class)
  public void getContenutoByIdErroreContenutoDocumentoNonPresente() {
    contenutoDocumentoService.getById(1L, 0L);
  }

  @Test
  public void getContenutoById() {
    var result = contenutoDocumentoService.getById(1L, 1L);
    assertNotNull(result);
  }

  @Test
  public void cancellaContenutoById() {
    contenutoDocumentoService.cancellaById(1L, 1L, true);
  }

  @Test
  public void getContenutoFisico() {
    setUpMock();
    var result = contenutoDocumentoService.getContenutoFisico(1L, 1L);
    assertNotNull(result);
  }

  @Test(expected = NotFoundException.class)
  public void cercaContenutoTramiteIdDocumentoErroreDocumentoNonPresente() {
    contenutoDocumentoService.getByIdDocumento(0L);
  }

  @Test
  public void cercaContenutoTramiteIdDocumento() {
    var result = contenutoDocumentoService.getByIdDocumento(1L);
    assertNotNull(result);
  }

  @Test(expected = InvalidParameterException.class)
  public void verificaFirmaErroreContenutoNullo() {
    contenutoDocumentoService.verificaFirma(new CosmoTContenutoDocumento());
  }

  @Test
  public void verificaFirma() {
    setUpMock();
    contenutoDocumentoService.verificaFirma(getContenuto());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getLinkDownloadDirettoErroreDownloadNonDisponibile() {
    contenutoDocumentoService.getLinkDownloadDiretto(10L, 9L, false);
  }

  @Test(expected = InternalServerException.class)
  public void getLinkDownloadDirettoErroreContenutoFisicoNonLocalizzabile() {
    contenutoDocumentoService.getLinkDownloadDiretto(3L, 8L, false);
  }

  @Test
  public void getLinkDownloadDirettoErrore() {
    setUpMock();
    var result = contenutoDocumentoService.getLinkDownloadDiretto(7L, 5L, false);
    assertNotNull(result);
  }

  @Test
  public void isTemporaneo() {
    var result = contenutoDocumentoService.isTemporaneo(7L, 5L);
    assertFalse(result);
  }

  @Test(expected = BadRequestException.class)
  public void validazioneDimensioneDocumentoErroreDocumentoVuoto() {
    contenutoDocumentoService.validaDimensioneDocumento(null, 0L);
  }

  @Test(expected = PayloadTooLargeException.class)
  public void validazioneDimensioneDocumentoErroreDimensioneDocumentoSuperioreAlMassimoConfigurato() {
    contenutoDocumentoService.validaDimensioneDocumento(getTipoDocumento(), 5000000L);
  }

  @Test
  public void validazioneDimensioneDocumento() {
    contenutoDocumentoService.validaDimensioneDocumento(getTipoDocumento(), 1L);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getLinkEsposizionePermenenteErroreContenutoTemporaneo() {
    contenutoDocumentoService.getLinkEsposizionePermanente(11L, 10L, 1L, true);
  }

  @Test(expected = InternalServerException.class)
  public void getLinkEsposizionePermenenteErroreContenutoFisicoNonLocalizzabile() {
    contenutoDocumentoService.getLinkEsposizionePermanente(3L, 8L, 1L, true);
  }

  @Test
  public void getLinkEsposizionePermenente() {
    var result = contenutoDocumentoService.getLinkEsposizionePermanente(7L, 5L, 1L, true);
    assertNotNull(result);
  }

  @Test
  public void creaContenutoOriginaleDaStreaming() {
    var indexEntity = new Entity();
    indexEntity.setUid("uid");
    var result =
        contenutoDocumentoService.creaContenutoOriginaleDaStreaming(getContenuto(), indexEntity);
    assertNotNull(result);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getContenutoIndexErroreContenutoTemporaneo() {
    contenutoDocumentoService.getContenutoIndex(11L, 10L);
  }

  @Test(expected = InternalServerException.class)
  public void getContenutoIndexErroreContenutoFisicoNonLocalizzabile() {
    contenutoDocumentoService.getContenutoIndex(3L, 8L);
  }

  @Test
  public void getContenutoIndex() {
    setUpMock();
    var result = contenutoDocumentoService.getContenutoIndex(7L, 5L);
    assertNotNull(result);
    assertTrue(result.length > 0);
  }

  @Test
  public void generaSha256PerFileDaFile() {
    contenutoDocumentoService.generaSha256PerFile(new RetrievedContentTest());
  }

  @Test
  public void generaSha256PerFileDaByteArray() {
    contenutoDocumentoService.generaSha256PerFile(new byte[1]);
  }

  @Test
  public void creaContenutoFirmatoFea() {
    var indexEntity = new Entity();
    indexEntity.setUid("uid");
    var result = contenutoDocumentoService.creaContenutoFirmatoFea(getContenuto(), indexEntity,
        "nomeFileFirmato");
    assertNotNull(result);
  }

  private void setUpMock() {
    var entity = new Entity();
    var verifyReport = new VerifyReport();
    var sd = new ListShareDetail();
    var listShareDetail = new ArrayList<ShareDetail>();
    var shareDetail = new ShareDetail();
    var sharedLink = new SharedLink();
    listShareDetail.add(shareDetail);
    sd.setListShareDetail(listShareDetail);
    entity.setContent(new byte[5]);
    sharedLink.setDownloadUri("https://www.regione.piemonte.it/web/");
    reset(mockFileShareService, mockCosmoSoapIndexFeignClient);
    when(mockFileShareService.get(any(), any())).thenReturn(new RetrievedContentTest());
    when(mockCosmoSoapIndexFeignClient.getFile(any(), any())).thenReturn(entity);
    when(mockCosmoSoapIndexFeignClient.verificaFirma(any(), any())).thenReturn(verifyReport);
    when(mockCosmoSoapIndexFeignClient.shareId(any())).thenReturn(sd);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(sharedLink);
    doNothing().when(mockFileShareService).delete(any());
    doNothing().when(mockCosmoSoapIndexFeignClient).deleteIdentifier(any());
  }

  private CosmoDTipoDocumento getTipoDocumento() {
    var tipoDocumento = new CosmoDTipoDocumento();
    tipoDocumento.setDimensioneMassima(1L);
    return tipoDocumento;
  }

  private CosmoTContenutoDocumento getContenuto() {
    var contenuto = new CosmoTContenutoDocumento();
    contenuto.setDtInserimento(Timestamp.from(Instant.now()));
    contenuto.setDimensione(1L);
    contenuto.setNomeFile("nomeFile");
    contenuto.setId(1L);
    contenuto.setUuidNodo("uuidNodo");
    contenuto.setFormatoFile(new CosmoDFormatoFile());
    return contenuto;
  }

  private CosmoDTipoFirma getTipoFirma() {
    var tipoFirma = new CosmoDTipoFirma();
    tipoFirma.setCodice("PDF");
    return tipoFirma;
  }

  private CosmoDFormatoFile getFormatoFile() {
    var formatoFile = new CosmoDFormatoFile();
    formatoFile.setCodice(MIME_TYPE_PDF);
    return formatoFile;
  }

  private CosmoDTipoContenutoDocumento getTipoContenutoDocumento(String tipoContenuto) {
    var tipoContenutoDocumento = new CosmoDTipoContenutoDocumento();
    tipoContenutoDocumento.setCodice(tipoContenuto);
    return tipoContenutoDocumento;
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
