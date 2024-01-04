/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmosoap.business.service.Api2IndexService;
import it.csi.cosmo.cosmosoap.business.service.DoSignService;
import it.csi.cosmo.cosmosoap.business.service.EventService;
import it.csi.cosmo.cosmosoap.dto.rest.Attivita;
import it.csi.cosmo.cosmosoap.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumento;
import it.csi.cosmo.cosmosoap.dto.rest.ContinueTransaction;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaMassivaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignFirmaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DoSignSigilloRequest;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiPayload;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiTask;
import it.csi.cosmo.cosmosoap.dto.rest.Documento;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;
import it.csi.cosmo.cosmosoap.dto.rest.FormatoFile;
import it.csi.cosmo.cosmosoap.dto.rest.FunzionalitaEseguibileMassivamente;
import it.csi.cosmo.cosmosoap.dto.rest.Pratica;
import it.csi.cosmo.cosmosoap.dto.rest.RichiediOTPRequest;
import it.csi.cosmo.cosmosoap.dto.rest.RiferimentoAttivita;
import it.csi.cosmo.cosmosoap.dto.rest.StartTransaction;
import it.csi.cosmo.cosmosoap.dto.rest.TipoContenutoDocumento;
import it.csi.test.cosmo.cosmosoap.business.service.impl.DoSignServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentIntegrationTest;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteEndTransactionDto;
import it.doqui.dosign.dosign.business.session.dosign.remotev2.RemoteStartTransactionDto;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class, TestConfig.class})
@Transactional
public class DoSignServiceImplTest extends ParentIntegrationTest {

  protected static final Logger log = LoggerFactory.getLogger(DoSignServiceImplTest.class);

  @Autowired
  private DoSignService doSignService;

  List<Documento> multiPayloads = new LinkedList<>();

  List<DocumentiPayload> singleAddSignPayload = new LinkedList<>();

  List<DocumentiPayload> multiAddSignPayload = new LinkedList<>();

  List<DocumentiPayload> firmaMassivaPayloads = new LinkedList<>();

  protected static final String MY_DIRECTORY_PATH = "test/resources/dosign/input/firmaMassiva";

  private static final String ALIAS = "DdfT+sEjeGSSzwTCxObg2elU2mNF6Rz8eSNhkaAdXog=";

  private static final String PIN = "2mNxgENrdlyf6AmhymMdZg==";

  private static final String OTP = "OTP_HERE";

  private static final String CODICE_CA = "1";

  private static final String CODICE_TSA = "codice_tsa 1";

  private static final String CODICE_ENTE_CERTIFICATORE = "ARUBA";

  private static final String PROFILO_FEQ = "PROFILOFEQ1";

  private static final String ID_TRANSIZIONE = "idTransazione";

  private static final String PASSWORD = "password";

  private static final String UANATACA = "UANATACA";

  private static final String PDF_CODE = "application/pdf";


  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Before
  public void testServiceInit() {
    Assert.notNull(doSignService, "Servizio non inizializzato");
  }

  @Before
  public void initResources() throws IOException {

    InputStream singlePDFToSign = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/single/dosign_test_document.pdf");

    assertNotNull(singlePDFToSign);

    InputStream multiPDFToSign1 = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/multi/dosign_multi_test_document_1.pdf");

    assertNotNull(multiPDFToSign1);

    InputStream multiPDFToSign2 = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/multi/dosign_multi_test_document_2.pdf");

    assertNotNull(multiPDFToSign2);

    InputStream singleAddSign = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/addsingle/singolo_documento_prima_firma.pdf.p7m");

    DocumentiPayload singoloDocumentoPrimaFirma = new DocumentiPayload();
    singoloDocumentoPrimaFirma.setOriginalFilename("singolo_documento_prima_firma.pdf.p7m");
    singoloDocumentoPrimaFirma
    .setOriginalContent(singleAddSign.readAllBytes());
    singleAddSignPayload.add(singoloDocumentoPrimaFirma);

    InputStream multiAddSign1 = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/addmulti/dosign_multi_test_document_1.pdf.p7m");
    DocumentiPayload dosignMultiTestDocument1p7m = new DocumentiPayload();
    dosignMultiTestDocument1p7m.setOriginalFilename("dosign_multi_test_document_1.pdf.p7m");
    dosignMultiTestDocument1p7m
    .setOriginalContent(multiAddSign1.readAllBytes());
    multiAddSignPayload.add(dosignMultiTestDocument1p7m);

    InputStream multiAddSign2 = DoSignServiceImplTest.class
        .getResourceAsStream("/dosign/input/addmulti/dosign_multi_test_document_2.pdf.p7m");
    DocumentiPayload dosignMultiTestDocument2p7m = new DocumentiPayload();
    dosignMultiTestDocument2p7m.setOriginalFilename("dosign_multi_test_document_2.pdf.p7m");
    dosignMultiTestDocument2p7m
    .setOriginalContent(multiAddSign2.readAllBytes());
    multiAddSignPayload.add(dosignMultiTestDocument2p7m);

    firmaMassivaInitResources();
  }

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public Api2IndexService api2IndexService() {
      return Mockito.mock(Api2IndexService.class);
    }

    @Bean
    @Primary
    public EventService eventService() {
      return Mockito.mock(EventService.class);
    }

  }

  @Autowired
  private Api2IndexService mockApi2IndexService;

  @Autowired
  private EventService mockEventService;

  private void setUpMock() {
    reset(mockApi2IndexService);
    when(mockApi2IndexService.find(any(), any())).thenReturn(null);
    reset(mockEventService);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());

  }

  private void firmaMassivaInitResources() throws IOException {
    String cwd = Path.of(MY_DIRECTORY_PATH).toAbsolutePath().toString();

    File dir = new File(cwd);

    File[] directoryListing = dir.listFiles();

    for (File file : directoryListing) {
      InputStream input = DoSignServiceImplTest.class
          .getResourceAsStream("/dosign/input/firmaMassiva/" + file.getName());
      assertNotNull(input);
      DocumentiPayload documento = new DocumentiPayload();
      documento.setOriginalFilename(file.getName());
      documento.setOriginalContent(input.readAllBytes());
      firmaMassivaPayloads.add(documento);
    }
  }

  @Test(expected = BadRequestException.class)
  public void testFirmaErroreDocumentoInesistente() {

    var request = getFirmaPayload();
    request.setDocumentiDaFirmare(new ArrayList<>());
    doSignService.firma(request);
  }

  @Test
  public void testFirmaRemoteSingoloDocumento() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("CERT1");
    doSignService.firma(request);
  }

  @Test
  public void testFirmaRemoteSingoloDocumentoProfiloPADES() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("CERT1");
    request.setProfiloFEQ("PADES");
    doSignService.firma(request);
  }

  @Test
  public void testFirmaRemoteMultiDocumento() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("CERT1");
    var documenti =
        List.of(request.getDocumentiDaFirmare().get(0), request.getDocumentiDaFirmare().get(0));
    request.setDocumentiDaFirmare(documenti);
    doSignService.firma(request);
  }

  @Test
  public void testFirmaRemoteMultiDocumentoProfiloPADES() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("CERT1");
    var documenti =
        List.of(request.getDocumentiDaFirmare().get(0), request.getDocumentiDaFirmare().get(0));
    request.setDocumentiDaFirmare(documenti);
    request.setProfiloFEQ("PADES");
    doSignService.firma(request);
  }

  @Test
  public void testFirmaUanatacaSingoloDocumento() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("Uanataca");
    request.setProvider(UANATACA);
    doSignService.firma(request);
  }

  @Test
  public void testFirmaUanatacaMultiDocumento() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("Uanataca");
    request.setProvider(UANATACA);
    var documenti =
        List.of(request.getDocumentiDaFirmare().get(0), request.getDocumentiDaFirmare().get(0));
    request.setDocumentiDaFirmare(documenti);
    doSignService.firma(request);
  }

  @Test
  public void testFirmaUanatacaMultiDocumentoProfiloPADES() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    docIndex.setUid("Uid");
    when(mockApi2IndexService.create(any(), any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var request = getFirmaPayload();
    request.setCodiceEnteCertificatore("Uanataca");
    request.setCodiceEnteCertificatore("CERT1");
    request.setProvider(UANATACA);
    var documenti =
        List.of(request.getDocumentiDaFirmare().get(0), request.getDocumentiDaFirmare().get(0));
    request.setDocumentiDaFirmare(documenti);
    request.setProfiloFEQ("PADES");
    doSignService.firma(request);
  }


  @Test(expected = BadRequestException.class)
  public void fimaMassivaTestDocumentiTaskNonPresenti() {
    var documentiTask = new ArrayList<DocumentiTask>();
    var request = getFirmaMassivaPayload();
    request.setDocumentiDaFirmare(documentiTask);
    doSignService.firmaMassiva(request);
  }

  @Test
  public void fimaMassivaTestDocIndexNonPresente() {
    var documentiTask = new ArrayList<DocumentiTask>();
    var request = getFirmaMassivaPayload();
    var documento = new Documento();
    var contenuto = new ContenutoDocumento();
    var formatoFile = new FormatoFile();
    var tipoContenuto = new TipoContenutoDocumento();
    tipoContenuto
        .setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    formatoFile.setCodice(PDF_CODE);
    contenuto.setFormatoFile(formatoFile);
    contenuto.setId(1L);
    contenuto.setTipo(tipoContenuto);
    documento.setContenuti(List.of(contenuto));
    var documentoTask = new DocumentiTask();
    documentoTask.setDocumenti(List.of(documento));
    var attivita = new Attivita();
    attivita.setId(1);
    documentoTask.setAttivita(attivita);
    documentiTask.add(documentoTask);
    request.setDocumentiDaFirmare(documentiTask);
    var tasks = new ArrayList<AttivitaEseguibileMassivamente>();
    var aem = new AttivitaEseguibileMassivamente();
    RiferimentoAttivita refAttivita = new RiferimentoAttivita();
    refAttivita.setId(1);
    aem.setAttivita(refAttivita);
    aem.setFunzionalita(null);
    var pratica = new Pratica();
    aem.setPratica(pratica);
    tasks.add(aem);
    request.setTasks(tasks);
    doSignService.firmaMassiva(request);
  }

  @Test
  public void fimaMassivaTestDocIndexMockato() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());
    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    doNothing().when(mockEventService).broadcastEvent(any(), any(), any());
    var documentiTask = new ArrayList<DocumentiTask>();
    var request = getFirmaMassivaPayload();
    var documento = new Documento();
    var contenuto = new ContenutoDocumento();
    var formatoFile = new FormatoFile();
    var tipoContenuto = new TipoContenutoDocumento();
    tipoContenuto
        .setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    formatoFile.setCodice(PDF_CODE);
    contenuto.setFormatoFile(formatoFile);
    contenuto.setId(1L);
    contenuto.setTipo(tipoContenuto);
    documento.setContenuti(List.of(contenuto));
    var documentoTask = new DocumentiTask();
    documentoTask.setDocumenti(List.of(documento));
    var attivita = new Attivita();
    attivita.setId(1);
    documentoTask.setAttivita(attivita);
    documentiTask.add(documentoTask);
    request.setDocumentiDaFirmare(documentiTask);
    var tasks = new ArrayList<AttivitaEseguibileMassivamente>();
    var aem = new AttivitaEseguibileMassivamente();
    RiferimentoAttivita refAttivita = new RiferimentoAttivita();
    refAttivita.setId(1);
    aem.setAttivita(refAttivita);
    var funzionalita = new FunzionalitaEseguibileMassivamente();
    funzionalita.setId(1L);
    aem.setFunzionalita(funzionalita);
    var pratica = new Pratica();
    pratica.setId(1);
    aem.setPratica(pratica);
    tasks.add(aem);
    request.setTasks(tasks);
    request.setCodiceEnteCertificatore("Aruba");
    request.setProfiloFEQ(PROFILO_FEQ);
    doSignService.firmaMassiva(request);
  }

  @Test
  public void startTransactionRemote() {
    var st = getStartTransactionPayload();
    st.setAlias(ALIAS);
    st.setOtp(OTP);
    st.setPin(PIN);
    st.setCustomerCa(1);
    doSignService.executeStartSession(st, ID_TRANSIZIONE);
  }

  @Test
  public void startTransactionUanataca() {
    var payload = getStartTransactionUanatacaPayload();
    doSignService.uanatacaExecuteStartSession(payload, ID_TRANSIZIONE);
  }

  @Test
  public void endTransactionRemote() {
    var ct = getContinueTransactionPayload();
    doSignService.executeEndSession(ct, ID_TRANSIZIONE);
  }

  @Test
  public void endTransactionUanataca() {
    var payload = getEndTransactionUanatacaPayload();
    doSignService.uanatacaExecuteEndSession(payload, ID_TRANSIZIONE);
  }

  @Test(expected = InternalServerException.class)
  public void testOTPRemote() {
    var request = getOtpRequestPayload();
    request.setCodiceCa("2");
    request.setCodiceEnteCertificatore(CODICE_ENTE_CERTIFICATORE);
    doSignService.richiediOTP(request);
  }

  @Test(expected = InternalServerException.class)
  public void testOTPUanataca() {
    var request = getOtpRequestPayload();
    request.setPassword(PASSWORD);
    request.setProvider(UANATACA);
    request.setCodiceEnteCertificatore(UANATACA);
    doSignService.richiediOTP(request);
  }

  @Test(expected = BadRequestException.class)
  public void apponiSigilloContenutoErrato() {
    setUpMock();
    var request = getApponiSigilloRequestPayload();
    request.setDocumento(new Documento());
    doSignService.apponiSigillo(request);
  }

  @Test(expected = InternalServerException.class)
  public void apponiSigilloContenutoEsistenteDocumentoIndexInesistente() {
    setUpMock();
    var request = getApponiSigilloRequestPayload();
    var documento = new Documento();
    var contenuto = new ContenutoDocumento();
    var formatoFile = new FormatoFile();
    var tipoContenuto = new TipoContenutoDocumento();
    tipoContenuto
        .setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    formatoFile.setCodice(PDF_CODE);
    contenuto.setFormatoFile(formatoFile);
    contenuto.setId(1L);
    contenuto.setTipo(tipoContenuto);
    documento.setContenuti(List.of(contenuto));
    request.setDocumento(documento);
    doSignService.apponiSigillo(request);
  }

  @Test
  public void apponiSigillo() {
    reset(mockApi2IndexService);
    var docIndex = new Entity();
    docIndex.setContent(singleAddSignPayload.get(0).getOriginalContent());
    docIndex.setFilename(singleAddSignPayload.get(0).getOriginalFilename());

    when(mockApi2IndexService.find(any(), any())).thenReturn(docIndex);
    var request = getApponiSigilloRequestPayload();
    var documento = new Documento();
    var contenuto = new ContenutoDocumento();
    var formatoFile = new FormatoFile();
    var tipoContenuto = new TipoContenutoDocumento();
    tipoContenuto
        .setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    formatoFile.setCodice(PDF_CODE);
    contenuto.setFormatoFile(formatoFile);
    contenuto.setId(1L);
    contenuto.setTipo(tipoContenuto);
    documento.setContenuti(List.of(contenuto));
    request.setDocumento(documento);
    doSignService.apponiSigillo(request);
  }

  private DoSignSigilloRequest getApponiSigilloRequestPayload() {
    var request = new DoSignSigilloRequest();
    request.setDelegatedDomain("delegateDomain");
    request.setDelegatedPassword("delegatedPassword");
    request.setDelegatedUser("delegatedUser");
    request.setOtpPwd("otpPwd");
    request.setTipoHsm("tipoHsm");
    request.setTipoOtpAuth("tipoOtpAuth");
    request.setUser("user");
    return request;
  }

  private StartTransaction getStartTransactionPayload() {
    var st = new StartTransaction();
    st.setAlias(ALIAS);
    st.setOtp(OTP);
    st.setPin(PIN);
    return st;
  }

  private RemoteStartTransactionDto getStartTransactionUanatacaPayload() {
    var ret = new RemoteStartTransactionDto();
    ret.setOtp(OTP);
    ret.setPassword(PASSWORD);
    ret.setPin(PIN);
    ret.setProvider(UANATACA);
    ret.setUsername("username");
    return ret;
  }

  private ContinueTransaction getContinueTransactionPayload() {
    var ret = new ContinueTransaction();
    ret.setAlias(ALIAS);
    ret.setAuthData("authData");
    ret.setCustomerCa(1);
    ret.setPin(PIN);
    return ret;
  }

  private RemoteEndTransactionDto getEndTransactionUanatacaPayload() {
    var ret = new RemoteEndTransactionDto();
    ret.setPassword(PASSWORD);
    ret.setPin(PIN);
    ret.setProvider(UANATACA);
    ret.setUsername("username");
    ret.setSessionId("sessionId");
    return ret;
  }

  private RichiediOTPRequest getOtpRequestPayload() {
    var payload = new RichiediOTPRequest();
    payload.setAlias(ALIAS);
    payload.setPin(PIN);
    return payload;
  }

  private DoSignFirmaRequest getFirmaPayload() {
    var request = new DoSignFirmaRequest();
    request.setAlias(ALIAS);
    request.setPin(PIN);
    request.setOtp(OTP);
    request.setCodiceCa(CODICE_CA);
    request.setProfiloFEQ(PROFILO_FEQ);
    request.setMarcaTemporale(true);
    request.setCodiceTsa(CODICE_TSA);
    request.setNotificaFirma(true);
    var documento = new Documento();
    var contenuto = new ContenutoDocumento();
    var formatoFile = new FormatoFile();
    var tipoContenuto = new TipoContenutoDocumento();
    tipoContenuto
        .setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    formatoFile.setCodice(PDF_CODE);
    contenuto.setFormatoFile(formatoFile);
    contenuto.setId(2L);
    contenuto.setTipo(tipoContenuto);
    documento.setContenuti(List.of(contenuto));
    request.setDocumentiDaFirmare(List.of(documento));
    return request;
  }

  private DoSignFirmaMassivaRequest getFirmaMassivaPayload() {
    var request = new DoSignFirmaMassivaRequest();
    request.setAlias(ALIAS);
    request.setPin(PIN);
    request.setOtp(OTP);
    request.setUuidTransaction(ID_TRANSIZIONE);
    request.setCodiceCa("0");
    request.setCodiceTsa(CODICE_TSA);
    request.setProfiloFEQ(PROFILO_FEQ);
    request.setMarcaTemporale(false);
    request.setNotificaFirma(true);
    return request;
  }
}
