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
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.fileshare.model.FileUploadResult;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.common.fileshare.model.UploadedFileMetadata;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.business.service.FileShareService;
import it.csi.cosmo.cosmoecm.dto.rest.AggiornaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneDocumentiRequest;
import it.csi.cosmo.cosmoecm.dto.rest.PreparaEsposizioneTipologiaDocumentiRequest;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapIndexFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.test.cosmo.cosmoecm.business.service.impl.DocumentoServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class DocumentoServiceImplTest extends ParentIntegrationTest {

  private static final String DOCUMENTI_NON_NULLO = "documenti non nullo";

  private static final String IL_DOCUMENTO_NON_DEVE_ESSERE_NULLO = "il documento non deve essere nullo";

  private static final String DOCUMENTO_DEVE_ESSERE_UNO = "Documento deve essere 1";

  private static final String FILTER_PRATICA = "{\"filter\":{\"idPratica\":{\"eq\":\"1\"}}, \"page\":0,\"size\":10}";

  private static final String FILE_UUID = "fileUUID";

  private static final String MIME_TYPE_PDF = "application/pdf";

  private static final String CODICE_2 = "codice 2";

  private static final String FILTER_DA_FIRMARE =
      "{\"tutti\": \"false\", \"creationTime\": \"" + LocalDateTime.now() + "\" }";

  @Autowired
  private DocumentoService documentoService;

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

  @Autowired
  private FileShareService mockFileShareService;

  @Autowired
  private CosmoSoapIndexFeignClient mockIndexFeignClient;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getDocumento() {
    Documento documento = documentoService.getDocumento(2);
    assertNotNull(IL_DOCUMENTO_NON_DEVE_ESSERE_NULLO, documento);
  }

  @Test
  public void getTuttiDocumenti() {
    String filtro = "{}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);
    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertNotNull(documenti);
    assertNotNull(documenti.getDocumenti());
    assertNotNull(documenti.getPageInfo());
    assertNotNull(documenti.getPageInfo().getTotalElements());
    assertNotNull(documenti.getPageInfo().getPage());
    assertNotNull(documenti.getPageInfo().getPageSize());
    assertNotNull(documenti.getPageInfo().getTotalPages());
  }

  @Test
  public void getTuttiDocumentiConExport() {
    String filtro = "{\"fields\": \"id, tipo\" }";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, true);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertNotNull(documenti);
    assertNotNull(documenti.getDocumenti());
    assertNotNull(documenti.getPageInfo());
    assertNotNull(documenti.getPageInfo().getTotalElements());
    assertNotNull(documenti.getPageInfo().getPage());
    assertNotNull(documenti.getPageInfo().getPageSize());
    assertNotNull(documenti.getPageInfo().getTotalPages());
    assertTrue("documenti devono essere 9", documenti.getDocumenti().size() == 9);
  }

  @Test
  public void getDocumentiPerPratica() {
    String filtro = "{\"filter\":{\"idPratica\":{\"eq\":\"1\"} }, \"page\": 0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertTrue("documenti devono essere 3", documenti.getDocumenti().size() == 3);
  }

  @Test
  public void getTuttiDocumentiPerTipo() {
    String filtro =
        "{\"filter\":{\"tipo\":{\"eq\":\"codice 2\"} }, \"page\": 0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertTrue(DOCUMENTO_DEVE_ESSERE_UNO, documenti.getDocumenti().size() == 1);
  }

  @Test
  public void getTuttiDocumentiPerTitolo() {
    String filtro =
        "{\"filter\":{\"titoloNomeFile\":{\"ci\":\"1\"} }, \"page\": 0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertTrue(DOCUMENTO_DEVE_ESSERE_UNO, documenti.getDocumenti().size() == 1);
  }

  @Test
  public void getTuttiDocumentiPerFormato() {
    String filtro =
        "{\"filter\":{\"formato\":{\"ci\":\"X\"} },  \"page\": 0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertTrue(DOCUMENTO_DEVE_ESSERE_UNO, documenti.getDocumenti().size() == 1);
  }

  @Test
  public void getTuttiGliAllegati() {
    String filtro = "{\"filter\":{\"idParent\":{\"eq\":\"1\"} }, \"page\": 0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertFalse(DOCUMENTI_NON_NULLO, CollectionUtils.isEmpty(documenti.getDocumenti()));
    assertTrue("gli allegati devono essere 1", documenti.getDocumenti().size() == 1);
  }

  @Test
  public void getTuttiGliAllegatiConIdPratica() {

    String filtro =
        "{\"filter\":{\"idPratica\":{\"eq\":\"2\"},\"idParent\":{\"eq\":\"1\"} }, \"page\":0,\"size\":10}";
    DocumentiResponse documenti = documentoService.getDocumenti(filtro, false);

    assertTrue("non devono esserci documenti", CollectionUtils.isEmpty(documenti.getDocumenti()));
  }

  @Test
  public void modificaDocumentoTitolo() {

    Documento documento = documentoService.getDocumento(1);

    AggiornaDocumentoRequest documentoDaAggiornare = new AggiornaDocumentoRequest();
    documentoDaAggiornare.setTitolo("Nuovo titolo");

    Documento documentoAggiornato =
        documentoService.modificaDocumento(documentoDaAggiornare, documento.getId().intValue());

    assertNotNull(IL_DOCUMENTO_NON_DEVE_ESSERE_NULLO, documentoAggiornato);
    assertTrue("Titolo non aggiornato",
        !documento.getTitolo().equals(documentoDaAggiornare.getTitolo()));
  }

  @Test(expected = BadRequestException.class)
  public void ricercaDocumentiDaFirmareFiltroVuoto() {
    documentoService.getDocumentiDaFirmare("", "", false);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaDocumentiDaFirmareNessunaPraticaIndicata() {
    String filter = "{\"filter\":{ }, \"page\":0,\"size\":10}";
    documentoService.getDocumentiDaFirmare(filter, "", false);
  }

  @Test(expected = BadRequestException.class)
  public void ricercaDocumentiDaFirmareFiltroDaFirmareVuoto() {
    String filter = "{\"filter\":{\"idPratica\":{\"eq\":\"2\"}}, \"page\":0,\"size\":10}";
    String filterDaFirmare = "";
    documentoService.getDocumentiDaFirmare(filter, filterDaFirmare, false);
  }

  @Test
  public void ricercaDocumentiDaFirmareTuttiTrue() {
    String filterDaFirmare =
        "{\"tutti\": \"true\", \"creationTime\": \"" + LocalDateTime.now() + "\" }";
    var documentiDaFirmare =
        documentoService.getDocumentiDaFirmare(FILTER_PRATICA, filterDaFirmare, false);
    assertTrue(IL_DOCUMENTO_NON_DEVE_ESSERE_NULLO, !documentiDaFirmare.getDocumenti().isEmpty());
  }

  @Test(expected = BadRequestException.class)
  public void ricercaDocumentiDaFirmareTuttiFalseSenzaTipologieDichiarate() {
    String filterDaFirmare =
        "{\"tutti\": \"false\", \"creationTime\": \"" + LocalDateTime.now() + "\" }";
    documentoService.getDocumentiDaFirmare(FILTER_PRATICA, filterDaFirmare, false);
  }

  @Test
  public void ricercaDocumentiDaFirmareTuttiFalseTipologieDichiarateNoExport() {
    var tipologieDocumenti = new ArrayList<String>();
    tipologieDocumenti.add("fake");
    String filterDaFirmare =
        "{\"tutti\": \"false\", \"creationTime\": \"" + LocalDateTime.now()
            + "\", \"tipologieDocumenti\":[ \"codice 1\"] }";
    documentoService.getDocumentiDaFirmare(FILTER_PRATICA, filterDaFirmare, false);
  }

  @Test
  public void ricercaDocumentiDaFirmareTuttiFalseTipologieDichiarateConExport() {
    String filter =
        "{\"filter\":{\"idPratica\":{\"eq\":\"1\"}}, \"fields\": \"id, titolo\", \"page\":0,\"size\":10}";
    var tipologieDocumenti = new ArrayList<String>();
    tipologieDocumenti.add("fake");
    String filterDaFirmare =
        "{\"tutti\": \"false\", \"creationTime\": \"" + LocalDateTime.now() + "\", \"tipologieDocumenti\":[ \"codice 1\"] }";
    documentoService.getDocumentiDaFirmare(filter, filterDaFirmare, true);
  }

  @Test(expected = BadRequestException.class)
  public void inserimentoDocumentoSenzaPratica() {
    var cdr = new CreaDocumentiRequest();
    var documento = new CreaDocumentoRequest();
    cdr.setDocumenti(List.of(documento));
    documentoService.inserisciDocumenti(null, cdr);
  }

  @Test(expected = BadRequestException.class)
  public void inserimentoDocumentoSenzaTipoDocumento() {
    var cdr = new CreaDocumentiRequest();
    var documento = new CreaDocumentoRequest();
    cdr.setDocumenti(List.of(documento));
    documentoService.inserisciDocumenti(1L, cdr);
  }

  @Test(expected = BadRequestException.class)
  public void inserimentoDocumentoSenzaUuidFile() {
    var cdr = new CreaDocumentiRequest();
    var documento = new CreaDocumentoRequest();
    documento.setCodiceTipo(CODICE_2);
    cdr.setDocumenti(List.of(documento));
    documentoService.inserisciDocumenti(1L, cdr);
  }

  @Test
  public void inserimentoDocumento() {
    reset(mockFileShareService);
    RetrievedContentTest rc = new RetrievedContentTest();
    when(mockFileShareService.get(any(), any())).thenReturn(rc);
    var cdr = new CreaDocumentiRequest();
    var documento = new CreaDocumentoRequest();
    documento.setCodiceTipo(CODICE_2);
    documento.setUuidFile("uuidFile");
    cdr.setDocumenti(List.of(documento));
    var documenti = documentoService.inserisciDocumenti(1L, cdr);
    assertTrue("Documento inserito correttamente", documenti.getDocumenti().size() == 1);
  }

  @Test
  public void inserimentoDocumentoConParent() {
    reset(mockFileShareService);
    RetrievedContentTest rc = new RetrievedContentTest();
    when(mockFileShareService.get(any(), any())).thenReturn(rc);
    var cdr = new CreaDocumentiRequest();
    var documento = new CreaDocumentoRequest();
    documento.setCodiceTipo(CODICE_2);
    documento.setParentId("1");
    documento.setUuidFile("uuidFile");
    cdr.setDocumenti(List.of(documento));
    var documenti = documentoService.inserisciDocumenti(1L, cdr);
    assertTrue("Documento inserito correttamente", documenti.getDocumenti().size() == 1);
  }

  @Test(expected = BadRequestException.class)
  public void modificaDocumentoIdNullo() {
    documentoService.modificaDocumento(null, null);
  }

  @Test(expected = BadRequestException.class)
  public void cancellaDocumentoIdNullo() {
    documentoService.cancellaDocumento(null);
  }

  @Test(expected = NotFoundException.class)
  public void cancellaDocumentoIdNonEsistente() {
    documentoService.cancellaDocumento(-1);
  }

  @Test
  public void cancellaDocumentoEAllegati() {
    var documentoCancellato = documentoService.cancellaDocumento(7);
    assertNotNull(documentoCancellato);
  }

  @Test
  public void eliminaDocumentoSuIndex() {
    reset(mockIndexFeignClient);
    doNothing().when(mockIndexFeignClient).deleteIdentifier(any());
    Entity documento = createEntity();
    documentoService.cancellaDocumentoIndex(documento.getUid());
  }

  @Test
  public void cancellaDaFileShareService() {
    reset(mockFileShareService);
    doNothing().when(mockFileShareService).delete(any());
    RetrievedContentTest rc = new RetrievedContentTest();
    documentoService.delete(rc);
    log("cancellazione avvenuta senza eccezioni");
  }

  @Test(expected = NotFoundException.class)
  public void creaDocumentoProgrammaticamentePraticaInesistente() {
    RetrievedContentTest rc = new RetrievedContentTest();
    documentoService.creaDocumentoProgrammaticamente(123L, null, null, null, "pdfFileName", MIME_TYPE_PDF, rc.getContentStream());
  }

  @Test(expected = NotFoundException.class)
  public void creaDocumentoProgrammaticamenteTipoDocumentoNonPresente() {
    var fur = new FileUploadResult();
    var ufm = new UploadedFileMetadata();
    ufm.setFileUUID(FILE_UUID);
    fur.setMetadata(ufm);
    var rc = new RetrievedContentTest();
    reset(mockFileShareService);
    when(mockFileShareService.saveFromMemory(any(), any(), any(), any())).thenReturn(fur);
    when(mockFileShareService.get(any(), any())).thenReturn(rc);
    documentoService.creaDocumentoProgrammaticamente(1L, "fake", "titolo", "autore", "pdfFile",
        MIME_TYPE_PDF, rc.getContentStream());
  }

  @Test
  public void creaDocumentoProgrammaticamente() {
    var fur = new FileUploadResult();
    var ufm = new UploadedFileMetadata();
    ufm.setFileUUID(FILE_UUID);
    ufm.setContentSize(1L);
    fur.setMetadata(ufm);
    var rc = new RetrievedContentTest();
    reset(mockFileShareService);
    when(mockFileShareService.saveFromMemory(any(), any(), any(), any())).thenReturn(fur);
    when(mockFileShareService.get(any(), any())).thenReturn(rc);
    var documento = documentoService.creaDocumentoProgrammaticamente(1L, CODICE_2, "titolo", "autore", "pdfFile",
        MIME_TYPE_PDF, rc.getContentStream());
    assertNotNull(documento);
  }

  @Test
  public void richiediDaFileShareService() {
    RetrievedContentTest rc = new RetrievedContentTest();
    reset(mockFileShareService);
    when(mockFileShareService.get(any(), any())).thenReturn(rc);
    documentoService.get(FILE_UUID);
  }

  @Test(expected = NotFoundException.class)
  public void preparaEsposizioneDocumentiIdPraticaErrato() {
    var pedr = new PreparaEsposizioneDocumentiRequest();
    documentoService.preparaEsposizioneDocumenti(123L, pedr);
  }

  @Test
  public void preparaEsposizioneDocumenti() {
    var pedr = new PreparaEsposizioneDocumentiRequest();
    var petdrList = new ArrayList<PreparaEsposizioneTipologiaDocumentiRequest>();
    var petdr = new PreparaEsposizioneTipologiaDocumentiRequest();
    petdr.setCodiceTipoDocumento("codice 1");
    petdr.setDurata(3L);
    petdrList.add(petdr);
    pedr.setTipologieDaEsporre(petdrList);
    var result = documentoService.preparaEsposizioneDocumenti(1L, pedr);
    assertNotNull(result);
    assertNotNull(result.getDocumentiEsposti());
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiBodyVuoto() {
    documentoService.getTipologieDocumentiSalvati("");
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiIdPraticaNullo() {
    String body = "{\"daFirmare\":\"true\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiTipologieDocumentiNonEsistenti() {
    String body = "{\"idPratica\":\"1\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiCreationTimeNonValorizzato() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice 1\"}], \"daFirmare\":\"true\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiVerificaDataDocObbligatorioCreationTimeNullo() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice 1\"}], \"verificaDataDocObbligatori\":\"true\", \"creationTime\": \"\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiBodyErrato() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice 1\"}], \"verificaDataDocObbligatori\":\"true\", \"creationTime\": \"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiTipologieDocumentiErrate() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice fake\"}], \"daFirmare\":\"true\", \"creationTime\": \""
            + LocalDateTime.now() + "\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test(expected = BadRequestException.class)
  public void getTipologieDocumentiSalvatiTipologieDocumentiVuote() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"\"}], \"daFirmare\":\"true\", \"creationTime\": \""
            + LocalDateTime.now() + "\"}";
    documentoService.getTipologieDocumentiSalvati(body);
  }

  @Test
  public void getTipologieDocumentiSalvati() {
    String body =
        "{\"idPratica\":\"1\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice 1\"}], \"daFirmare\":\"true\", \"creationTime\": \""
            + LocalDateTime.now() + "\"}";
    var list = documentoService.getTipologieDocumentiSalvati(body);
    assertTrue(!list.isEmpty());
  }

  @Test
  public void getTipologieDocumentiSalvatiNonDaFirmare() {
    String body =
        "{\"idPratica\":\"13\", \"tipologieDocumenti\": [{\"codiceTipologiaDocumento\": \"codice 1\", \"codiceTipologiaDocumentoPadre\": \"codice 1\"}],\"daFirmare\":\"false\", \"creationTime\": \""
            + LocalDateTime.now() + "\"}";
    var list = documentoService.getTipologieDocumentiSalvati(body);
    assertTrue(!list.isEmpty());
  }

  private Entity createEntity() {
    Entity documento = new Entity();

    documento.setFilename("testfile.txt");
    documento.setMimeType("application/text");
    documento.setContent("hello world".getBytes());
    documento.setDescrizione("descrizione documento (prima versione)");
    documento.setIdDocumento(1L);
    documento.setTipoDocumento("DOC");

    return documento;
  }

  private class RetrievedContentTest implements RetrievedContent {

    @Override
    public Path getWorkingFolder() {
      return null;
    }

    @Override
    public InputStream getContentStream() {
      InputStream singlePDF =
          DocumentoServiceImplTest.class
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
