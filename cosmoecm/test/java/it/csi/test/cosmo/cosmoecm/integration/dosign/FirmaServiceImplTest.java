/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.dosign;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import java.time.OffsetDateTime;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmoecm.business.service.FirmaService;
import it.csi.cosmo.cosmoecm.config.Constants;
import it.csi.cosmo.cosmoecm.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmoecm.dto.rest.RichiestaOTPRequest;
import it.csi.cosmo.cosmoecm.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapDosignFeignClient;
import it.csi.cosmo.cosmosoap.dto.rest.SigillaDocumentoResponse;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class,
    it.csi.test.cosmo.cosmoecm.integration.dosign.FirmaServiceImplTest.TestConfig.class})
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class FirmaServiceImplTest extends ParentIntegrationTest {

  protected static final Logger log = LoggerFactory.getLogger ( FirmaServiceImplTest.class );

  private static final String ALIAS = "Test1";

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapDosignFeignClient cosmoSoapDosignFeignClient() {
      return Mockito.mock(CosmoSoapDosignFeignClient.class);
    }
  }

  @Autowired
  private CosmoSoapDosignFeignClient mockCosmoSoapDosignFeignClient;

  @Autowired
  private FirmaService firmaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  private void setUpMock() {
    var sigilloResponse = new SigillaDocumentoResponse();
    sigilloResponse.setCodice(Constants.ESITO_SIGILLO_OK);
    reset(mockCosmoSoapDosignFeignClient);

    doNothing().when(mockCosmoSoapDosignFeignClient).richiediOTP(any());

    doNothing().when(mockCosmoSoapDosignFeignClient).firma(any());

    when(mockCosmoSoapDosignFeignClient.postDosignSigillo(any())).thenReturn(sigilloResponse);
  }

  @Test()
  public void richiestaOTP() {
    setUpMock();

    RichiestaOTPRequest request = new RichiestaOTPRequest();
    request.setAlias("username");
    request.setPin("password");
    request.setCodiceEnteCertificatore("CERT1");
    firmaService.richiediOTP(request);
  }

  @Test(expected = BadRequestException.class)
  public void richiestaOTPErroreEnteCertificatoreNonEsistente() {
    setUpMock();
    RichiestaOTPRequest request = new RichiestaOTPRequest();
    request.setAlias("username");
    request.setPin("password");
    request.setCodiceEnteCertificatore("CERTFAKE");
    firmaService.richiediOTP(request);
  }

  @Test(expected = BadRequestException.class)
  public void firmaCertificatoScaduto() {

    setUpMock();

    FirmaRequest firmaRequest = new FirmaRequest();
    Documento doc = new Documento();
    doc.setId(1L);
    List<Documento> listaDocs = new LinkedList<>();
    listaDocs.add(doc);
    CertificatoFirma certificato = new CertificatoFirma();
    certificato.setDataScadenza(OffsetDateTime.now().minusHours(2));
    firmaRequest.setDocumenti(listaDocs);
    firmaRequest.setCertificato(certificato);
    firmaRequest.setOtp("111");
    firmaService.firma(firmaRequest);
  }

  @Test
  public void firma() {
    setUpMock();
    FirmaRequest firmaRequest = new FirmaRequest();
    Documento doc = new Documento();
    doc.setId(1L);
    List<Documento> listaDocs = new LinkedList<>();
    listaDocs.add(doc);
    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice("CADES");
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice("CERT1");
    SceltaMarcaTemporale scelta = new SceltaMarcaTemporale();
    scelta.setCodice("NO");
    CertificatoFirma certificato = new CertificatoFirma();
    certificato.setDataScadenza(OffsetDateTime.now().plusDays(2));
    certificato.setProfiloFEQ(profiloFEQ);
    certificato.setEnteCertificatore(enteCertificatore);
    certificato.setSceltaMarcaTemporale(scelta);
    firmaRequest.setDocumenti(listaDocs);
    firmaRequest.setCertificato(certificato);
    firmaRequest.setOtp("111");
    firmaService.firma(firmaRequest);
  }

  @Test
  public void apponiSigilloElettronicoErroreAliasNonPresente() {
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var result = firmaService.apponiSigilloElettronico(documento, "alias");
    assertNotNull(result);
    assertNotNull(result.getCodice());
    assertTrue(result.getCodice().equals(Constants.ERR_CREDENZIALI_SIGILLO));
  }

  @Test(expected = InternalServerException.class)
  public void apponiSigilloElettronicoErroreDocumentoNullo() {
    var documento = new CosmoTDocumento();
    documento.setId(123L);
    firmaService.apponiSigilloElettronico(documento, ALIAS);
  }

  @Test
  public void apponiSigilloElettronicoErroreContenutiNonPresenti() {
    var documento = new CosmoTDocumento();
    documento.setId(4L);
    var result = firmaService.apponiSigilloElettronico(documento, ALIAS);
    assertNotNull(result);
    assertNotNull(result.getCodice());
    assertTrue(result.getCodice().equals(Constants.ESITO_RICHIESTA_APPOSIZIONE_SIGILLO_TMP));
  }

  @Test
  public void apponiSigilloElettronico() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var result = firmaService.apponiSigilloElettronico(documento, ALIAS);
    assertNotNull(result);
    assertNotNull(result.getCodice());
    assertNotNull(result.getCodice().equals(Constants.ESITO_SIGILLO_OK));
  }


}
