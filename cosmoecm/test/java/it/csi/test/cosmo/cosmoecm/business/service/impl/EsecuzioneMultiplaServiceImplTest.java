/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
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
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmoecm.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmoecm.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiTask;
import it.csi.cosmo.cosmoecm.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmoecm.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapDosignFeignClient;
import it.csi.test.cosmo.cosmoecm.business.service.impl.EsecuzioneMultiplaServiceImplTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
@Transactional
public class EsecuzioneMultiplaServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private EsecuzioneMultiplaService esecuzioneMultiplaService;

  @Autowired
  private CosmoSoapDosignFeignClient mockCosmoSoapDosignFeignClient;

  private static final String OTP = "000000";

  private static final String FAKE = "fake";

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapDosignFeignClient cosmoSoapDosignFeignClient() {
      return Mockito.mock(CosmoSoapDosignFeignClient.class);
    }

  }

  @Test(expected = NotFoundException.class)
  public void eseguiFirmaMultiplaErroreEnteCertificatoreNonPresente() {
    var request = getPayloadEsecuzioneMultiplaFirma();
    request.getCertificato().getEnteCertificatore().setCodice(FAKE);
    esecuzioneMultiplaService.postEsecuzioneMultiplaFirma(request);
  }

  @Test(expected = InternalServerException.class)
  public void eseguiFirmaMultiplaErroreInFaseDiFirma() {
    var request = getPayloadEsecuzioneMultiplaFirma();
    request.getCertificato().setSceltaMarcaTemporale(null);
    esecuzioneMultiplaService.postEsecuzioneMultiplaFirma(request);
  }

  @Test
  public void eseguiFirmaMultipla() {
    setUpMock();
    var request = getPayloadEsecuzioneMultiplaFirma();
    esecuzioneMultiplaService.postEsecuzioneMultiplaFirma(request);
  }

  @Test
  public void eseguiRifiutoFirmaMultipla() {
    setUpMock();
    var request = getPayloadEsecuzioneMultiplaRifiutoFirma();
    esecuzioneMultiplaService.postEsecuzioneMultiplaRifiutoFirma(request);
  }

  private void setUpMock() {
    reset(mockCosmoSoapDosignFeignClient);
    doNothing().when(mockCosmoSoapDosignFeignClient).firmaMassiva(any());
  }

  private EsecuzioneMultiplaRifiutoFirmaRequest getPayloadEsecuzioneMultiplaRifiutoFirma() {
    var payload = new EsecuzioneMultiplaRifiutoFirmaRequest();
    payload.setMandareAvantiProcesso(true);
    var tasks = new ArrayList<AttivitaEseguibileMassivamente>();
    var e = new AttivitaEseguibileMassivamente();

    tasks.add(e);
    payload.setTasks(tasks);
    payload.setNote("Note");
    return payload;
  }


  private EsecuzioneMultiplaFirmaRequest getPayloadEsecuzioneMultiplaFirma() {
    var payload = new EsecuzioneMultiplaFirmaRequest();
    var certificato = new CertificatoFirma();
    var documentiTask = new ArrayList<DocumentiTask>();
    var tasks = new ArrayList<AttivitaEseguibileMassivamente>();
    var profiloFEQ = new ProfiloFEQ();
    var enteCertificatore = new EnteCertificatore();
    var sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale
        .setCodice(it.csi.cosmo.common.entities.enums.SceltaMarcaTemporale.SI.toString());
    enteCertificatore.setCodice("CERT3");
    profiloFEQ.setCodice(it.csi.cosmo.common.entities.enums.ProfiloFEQ.CADES.toString());
    certificato.setProfiloFEQ(profiloFEQ);
    certificato.setEnteCertificatore(enteCertificatore);
    certificato.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    payload.setOtp(OTP);
    payload.setCertificato(certificato);
    payload.setDocumentiTask(documentiTask);
    payload.setLockMgmt(false);
    payload.setMandareAvantiProcesso(false);
    payload.setTasks(tasks);
    return payload;
  }
}
