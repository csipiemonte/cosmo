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
import java.io.InputStream;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.fileshare.model.UploadedFileMetadata;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.business.service.FruitoriService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoFruitoreContenutoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.ListShareDetail;
import it.csi.cosmo.cosmosoap.dto.rest.SharedLink;
import it.csi.test.cosmo.cosmoecm.business.service.impl.FruitoriServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class FruitoriServiceImplTest extends ParentIntegrationTest {

  private static final String UNPROCESSABLE_ENTITY = "UNPROCESSABLE_ENTITY";
  private static final String NOT_FOUND = "NOT_FOUND";
  private static final String CONFLICT = "CONFLICT";
  private static final Integer CODICE_422 = 422;
  private static final Integer CONFLICT_CODE = 409;
  private static final Integer NOT_FOUND_CODE = 404;
  private static final String MORE_THAN_A_BYTE = "aaaaaaaaaaaa";
  private static final String MIME_TYPE_PDF = "application/pdf";
  private static final String UPLOAD_UUID = "uploadUUID";
  private static final String EXT_KEY = "extKey";
  private static final String DIGEST = "DIGEST";
  private static final String LINK_REGIONE =
      "http://www.sistemapiemonte.it/eXoRisorse/dwd/servizi/OperePubbliche/prezzario/2023/Allegato_A_Nota_metodologica_2023.pdf";

  @Autowired
  private FruitoriService fruitoriService;

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

  @Test(expected = UnprocessableEntityException.class)
  public void inserisciDocumentiErroreNessunDocumentoFornito() {
    this.autentica(TestConstants.buildUtenteAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    payload.setDocumenti(new ArrayList<>());
    fruitoriService.inserisciDocumenti(payload);
  }

  @Test(expected = UnauthorizedException.class)
  public void inserisciDocumentiErroreFruitoreNonAutorizzato() {
    this.autentica(TestConstants.buildUtenteAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    fruitoriService.inserisciDocumenti(payload);
  }

  @Test(expected = BadRequestException.class)
  public void inserisciDocumentiErroreCodiceIpaEnteErrato() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    payload.setCodiceIpaEnte("fake");
    fruitoriService.inserisciDocumenti(payload);
  }

  @Test(expected = NotFoundException.class)
  public void inserisciDocumentiErrorePraticaErrata() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    payload.setIdPratica("0");
    fruitoriService.inserisciDocumenti(payload);
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErroreNessunContenutoFornito() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(CODICE_422));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(UNPROCESSABLE_ENTITY));
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErrorePiuDiUnContenutoFornito() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    var contenuto = getPayloadCreaDocumentoFruitoreContenutoRequest();
    payload.getDocumenti().get(0).setContenuto(contenuto);
    payload.getDocumenti().get(0).setUploadUUID(UPLOAD_UUID);
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(CODICE_422));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(UNPROCESSABLE_ENTITY));
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErroreContenutoFisicoNullo() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    var contenuto = getPayloadCreaDocumentoFruitoreContenutoRequest();
    payload.getDocumenti().get(0).setContenuto(contenuto);
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(CODICE_422));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(UNPROCESSABLE_ENTITY));
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErroreLunghezzaContenutoFisicoMaggioreDellaCfg() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    var contenuto = getPayloadCreaDocumentoFruitoreContenutoRequest();
    contenuto.setContenutoFisico(MORE_THAN_A_BYTE);
    payload.getDocumenti().get(0).setContenuto(contenuto);
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(CODICE_422));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(UNPROCESSABLE_ENTITY));
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErroreDocumentoFruitoreEsistente() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    payload.getDocumenti().get(0).setContenuto(null);
    payload.getDocumenti().get(0).setUploadUUID(UPLOAD_UUID);
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(CONFLICT_CODE));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(CONFLICT));
  }

  @Test
  public void inserisciDocumentiValidaDocumentoErroreDocumentoPadreFruitoreEsistente() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    payload.getDocumenti().get(0).setContenuto(null);
    payload.getDocumenti().get(0).setUploadUUID(UPLOAD_UUID);
    payload.getDocumenti().get(0).setIdPadre("notPresent");
    payload.getDocumenti().get(0).setId(null);
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(NOT_FOUND_CODE));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals(NOT_FOUND));
  }

  @Test
  public void inserisciDocumentioOk() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiFruitoriRequest();
    var contenuto = getPayloadCreaDocumentoFruitoreContenutoRequest();
    contenuto.setContenutoFisico("aaaa");
    payload.getDocumenti().get(0).setContenuto(contenuto);
    payload.getDocumenti().get(0).setId("nuovo");
    var result = fruitoriService.inserisciDocumenti(payload);
    assertNotNull(result);
    assertNotNull(result.getEsiti());
    assertTrue(result.getEsiti().size() == 1);
    assertNotNull(result.getEsiti().get(0).getEsito());
    assertTrue(result.getEsiti().get(0).getEsito().getStatus().equals(201));
    assertTrue(result.getEsiti().get(0).getEsito().getCode().equals("CREATED"));
  }

  @Test
  public void ricercaContenutoFruitore() {
    setUpMockIndex();
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var result = fruitoriService.getContenutoFruitore(EXT_KEY);
    assertNotNull(result);
    assertNotNull(result.getLinkDownloadDiretto());
  }


  @Test(expected = UnauthorizedException.class)
  public void ricercaContenutoFruitoreSignedErroreChiavePubblicaNulla() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getContenutoFruitoreSigned(EXT_KEY, null, DIGEST);
  }

  @Test(expected = UnauthorizedException.class)
  public void ricercaContenutoFruitoreSignedErroreDigestNullo() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getContenutoFruitoreSigned(EXT_KEY, "publicKey", null);
  }

  @Test(expected = UnauthorizedException.class)
  public void ricercaContenutoFruitoreSignedErroreChiaveFornitaNonRiconosciuta() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getContenutoFruitoreSigned(EXT_KEY, "testX", DIGEST);
  }

  @Test(expected = ForbiddenException.class)
  public void ricercaContenutoFruitoreSignedErroreInvalidDigest() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getContenutoFruitoreSigned(EXT_KEY, "2", DIGEST);
  }

  @Test
  public void ricercaContenutoFruitoreSignedOK() {
    setUpMockIndex();
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getContenutoFruitoreSigned(EXT_KEY, "2",
        "b6b5efaaf5b616acbb6bc7bccafcfc0a4a3158380529eca453412b39c7017810");
  }

  @Test(expected = UnprocessableEntityException.class)
  public void inserisciDocumentiLinkErroreNessunDocumentoFornito() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiLinkFruitoriRequest();
    payload.setDocumenti(new ArrayList<>());
    fruitoriService.inserisciDocumentiLink(payload);
  }

  @Test(expected = BadRequestException.class)
  public void inserisciDocumentiLinkErroreValidazioneLink() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiLinkFruitoriRequest();
    fruitoriService.inserisciDocumentiLink(payload);
  }

  @Test
  public void inserisciDocumentiLinkOKNoCallback() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiLinkFruitoriRequest();
    payload.getDocumenti().get(0).setLink(LINK_REGIONE);
    fruitoriService.inserisciDocumentiLink(payload);
  }

  @Test
  public void inserisciDocumentiLinkOKSiCallback() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    var payload = getPayloadCreaDocumentiLinkFruitoriRequest();
    payload.getDocumenti().get(0).setLink(LINK_REGIONE);
    payload.setRichiediCallback(true);
    fruitoriService.inserisciDocumentiLink(payload);
  }

  @Test
  public void ricercaEndpoint() {
    this.autentica(null, TestConstants.buildFruitoreAutenticato());
    fruitoriService.getEndpoint(OperazioneFruitore.CUSTOM, 1L, "Codice descrittivo");
  }

  private CreaDocumentiFruitoreRequest getPayloadCreaDocumentiFruitoriRequest() {
    setUpMock();
    var payload = new CreaDocumentiFruitoreRequest();
    var documento = new CreaDocumentoFruitoreRequest();
    documento.setCodiceTipo("codice 1");
    documento.setId(EXT_KEY);
    payload.setCodiceIpaEnte("r_piemon");
    payload.setIdPratica("1");
    payload.setDocumenti(List.of(documento));
    return payload;
  }

  private CreaDocumentoFruitoreContenutoRequest getPayloadCreaDocumentoFruitoreContenutoRequest() {
    var payload = new CreaDocumentoFruitoreContenutoRequest();
    payload.setContenutoFisico("");
    payload.setNomeFile("nomeFile");
    return payload;
  }

  private CreaDocumentiLinkFruitoreRequest getPayloadCreaDocumentiLinkFruitoriRequest() {
    setUpMock();
    var payload = new CreaDocumentiLinkFruitoreRequest();
    var documento = new CreaDocumentoLinkFruitoreRequest();
    documento.setCodiceTipo("codice 1");
    documento.setId("extKey1");
    documento.setNomeFile("nomeFile");
    payload.setCodiceIpaEnte("r_piemon");
    payload.setIdPratica("11");
    payload.setDocumenti(List.of(documento));
    return payload;
  }

  private void setUpMock() {
    var f = new FileUploadResult();
    var u = new UploadedFileMetadata();
    RetrievedContent retrievedContent = new RetrievedContentTest();
    u.setFileUUID("fileUUID");
    f.setMetadata(u);
    reset(mockFileShareService);
    when(mockFileShareService.saveFromMemory(any(), any(), any(), any())).thenReturn(f);
    when(mockFileShareService.get(any(), any())).thenReturn(retrievedContent);
  }

  private void setUpMockIndex() {
    var sl = new ListShareDetail();
    var sharedLink = new SharedLink();
    sharedLink.setDownloadUri(LINK_REGIONE);
    reset(mockCosmoSoapIndexFeignClient);
    when(mockCosmoSoapIndexFeignClient.shareId(any())).thenReturn(sl);
    when(mockCosmoSoapIndexFeignClient.share(any())).thenReturn(sharedLink);
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
