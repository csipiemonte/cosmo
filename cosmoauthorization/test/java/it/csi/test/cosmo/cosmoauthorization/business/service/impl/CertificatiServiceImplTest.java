/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.CertificatiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmoauthorization.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoCredenzialiFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoOTP;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class CertificatiServiceImplTest extends ParentIntegrationTest {


  private static final String DESCRIZIONE = "Descrizione 2 ";
  private static final String USERNAME = "Username 2";
  private static final String PASSWORD = "password 2";
  private static final String ENTE_CREDITORE = "CERT2";
  private static final String PROFILO_FEQ = "PROFILOFEQ2";
  private static final String SCELTA_MARCA_TEMPORALE = "NO";
  private static final String TIPO_CREDENZIALI_FIRMA = "TIPOCREDFIRMA2";
  private static final String TIPO_OTP = "TIPOOTP2";
  private static final String PIN = "1234";
  private static final String PROVA = "prova";
  private static final String CERT = "CERT1";

  @Autowired
  private CertificatiService certificatiService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoId1());
  }

  @Test
  public void getCertificato() {
    CertificatoFirma certificato = certificatiService.getCertificato("1");

    assertNotNull("Esiste un certificato con id 1", certificato);

    assertTrue("La descrizione deve essere 'Certificato 1'",
        "Certificato 1".equals(certificato.getDescrizione()));
    assertTrue("Lo username deve essere 'username'", "username".equals(certificato.getUsername()));
    assertTrue("La password deve essere 'password'", "password".equals(certificato.getPassword()));
  }

  @Test(expected = BadRequestException.class)
  public void getCertificatoNullo() {
    certificatiService.getCertificato(null);
  }

  @Test(expected = NotFoundException.class)
  public void getCertificatoIdNonEsistente() {
    certificatiService.getCertificato("111");
  }

  @Test
  public void getCertificati() {
    List<CertificatoFirma> certificati = certificatiService.getCertificati();

    assertNotNull("Non ci sono certificati per l'utente", certificati);
    assertTrue("Deve esserci solo certificato", certificati.size() == 2);

    assertTrue("La descrizione deve essere 'Certificato 1'",
        "Certificato 1".equals(certificati.get(0).getDescrizione()));
    assertTrue("Lo username deve essere 'username'",
        "username".equals(certificati.get(0).getUsername()));
    assertTrue("La password deve essere 'password'",
        "password".equals(certificati.get(0).getPassword()));
  }

  @Test
  public void salvaCertificato() {
    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setUsername(USERNAME);
    certificatoFirma.setPassword(PASSWORD);
    certificatoFirma.setPin(PIN);


    OffsetDateTime dataScadenza = OffsetDateTime.now().plusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);
    certificatoFirma.setUltimoUtilizzato(false);

    CertificatoFirma certificatoFirmaSalvate = certificatiService.postCertificato(certificatoFirma);

    assertTrue("La descrizione deve essere '" + DESCRIZIONE + "'",
        DESCRIZIONE.equals(certificatoFirmaSalvate.getDescrizione()));
    assertTrue("Lo username deve essere '" + USERNAME + "'",
        USERNAME.equals(certificatoFirmaSalvate.getUsername()));
    assertTrue("La password deve essere '" + PASSWORD + "'",
        PASSWORD.equals(certificatoFirmaSalvate.getPassword()));
    assertTrue("La data di scadenza deve essere '" + dataScadenza + "'", dataScadenza
        .truncatedTo(ChronoUnit.MILLIS).isEqual(certificatoFirmaSalvate.getDataScadenza()));
    assertTrue(
        "Il codice dell'ente certificatore deve essere '" + enteCertificatore.getCodice() + "'",
        enteCertificatore.getCodice()
            .equals(certificatoFirmaSalvate.getEnteCertificatore().getCodice()));
    assertTrue("Il codice del profiloFEQ deve essere '" + profiloFEQ.getCodice() + "'",
        profiloFEQ.getCodice().equals(certificatoFirmaSalvate.getProfiloFEQ().getCodice()));
    assertTrue(
        "Il codice della scelta marca temporale deve essere '" + sceltaMarcaTemporale.getCodice()
            + "'",
        sceltaMarcaTemporale.getCodice()
            .equals(certificatoFirmaSalvate.getSceltaMarcaTemporale().getCodice()));
    assertTrue(
        "Il codice del tipo di certificato firma deve essere '" + tipoCredenzialiFirma.getCodice()
            + "'",
        tipoCredenzialiFirma.getCodice()
            .equals(certificatoFirmaSalvate.getTipoCredenzialiFirma().getCodice()));
    assertTrue("Il codice del tipo di otp deve essere '" + tipoOTP.getCodice() + "'",
        tipoOTP.getCodice().equals(certificatoFirmaSalvate.getTipoOTP().getCodice()));
    assertTrue("Il pin deve essere '" + PIN + "'", PIN.equals(certificatoFirmaSalvate.getPin()));
  }

  @Test(expected = BadRequestException.class)
  public void salvaCertificatoSenzaUsername() {
    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setPassword(PASSWORD);
    certificatoFirma.setPin(PIN);

    OffsetDateTime dataScadenza = OffsetDateTime.now().plusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);

    certificatiService.postCertificato(certificatoFirma);
  }

  @Test(expected = NotFoundException.class)
  public void salvaCertificatoConImpostazioneFirmaSbagliata() {
    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setUsername(USERNAME);
    certificatoFirma.setPassword(PASSWORD);
    certificatoFirma.setPin(PIN);

    OffsetDateTime dataScadenza = OffsetDateTime.now().plusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice("CERT2x");
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);
    certificatoFirma.setUltimoUtilizzato(true);

    certificatiService.postCertificato(certificatoFirma);
  }

  @Test
  public void aggiornaCertificato() {

    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setId(1L);
    certificatoFirma.setDescrizione("Nuova descrizione");
    certificatoFirma.setUsername("nuova username");
    certificatoFirma.setPassword("nuova password");
    certificatoFirma.setPin("0000");

    OffsetDateTime dataScadenza = OffsetDateTime.now().plusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);
    certificatoFirma.setUltimoUtilizzato(false);

    CertificatoFirma certificatoFirmaAggiornate =
        certificatiService.putCertificato("1", certificatoFirma);

    assertTrue("La descrizione deve essere 'Nuova descrizione'",
        "Nuova descrizione".equals(certificatoFirmaAggiornate.getDescrizione()));
    assertTrue("Lo username deve essere 'nuova username'",
        "nuova username".equals(certificatoFirmaAggiornate.getUsername()));
    assertTrue("La password deve essere 'nuova password'",
        "nuova password".equals(certificatoFirmaAggiornate.getPassword()));
    assertTrue("La data di scadenza deve essere '" + dataScadenza + "'", dataScadenza
        .truncatedTo(ChronoUnit.MILLIS).isEqual(certificatoFirmaAggiornate.getDataScadenza()));
    assertTrue(
        "Il codice dell'ente certificatore deve essere '" + enteCertificatore.getCodice() + "'",
        enteCertificatore.getCodice()
            .equals(certificatoFirmaAggiornate.getEnteCertificatore().getCodice()));
    assertTrue("Il codice del profiloFEQ deve essere '" + profiloFEQ.getCodice() + "'",
        profiloFEQ.getCodice().equals(certificatoFirmaAggiornate.getProfiloFEQ().getCodice()));
    assertTrue(
        "Il codice della scelta marca temporale deve essere '" + sceltaMarcaTemporale.getCodice()
            + "'",
        sceltaMarcaTemporale.getCodice()
            .equals(certificatoFirmaAggiornate.getSceltaMarcaTemporale().getCodice()));
    assertTrue(
        "Il codice del tipo di credenziali firma deve essere '" + tipoCredenzialiFirma.getCodice()
            + "'",
        tipoCredenzialiFirma.getCodice()
            .equals(certificatoFirmaAggiornate.getTipoCredenzialiFirma().getCodice()));
    assertTrue("Il codice del tipo di otp deve essere '" + tipoOTP.getCodice() + "'",
        tipoOTP.getCodice().equals(certificatoFirmaAggiornate.getTipoOTP().getCodice()));
    assertTrue("Il pin deve essere '0000'", "0000".equals(certificatoFirmaAggiornate.getPin()));

  }

  @Test(expected = BadRequestException.class)
  public void aggiornaCertificatoSenzaID() {

    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setUsername(USERNAME);
    certificatoFirma.setPassword(PASSWORD);
    certificatoFirma.setPin(PIN);

    OffsetDateTime dataScadenza = OffsetDateTime.now().plusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);

    certificatiService.putCertificato(null, certificatoFirma);
  }

  @Test
  public void aggiornaCertificatoScadenzaAntecedente() {

    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setId(1L);
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setUsername(USERNAME);
    certificatoFirma.setPassword(PASSWORD);
    certificatoFirma.setPin(PIN);

    OffsetDateTime dataScadenza = OffsetDateTime.now().minusHours(2);
    certificatoFirma.setDataScadenza(dataScadenza);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);
    certificatoFirma.setUltimoUtilizzato(false);

    CertificatoFirma aggiornato = certificatiService.putCertificato("1", certificatoFirma);

    assertNotNull("Deve esserci un certificato", aggiornato);
    assertTrue("La data di scadenza deve essere '" + dataScadenza + "'",
        dataScadenza.truncatedTo(ChronoUnit.MILLIS).isEqual(aggiornato.getDataScadenza()));
  }


  @Test
  public void deleteCertificato() {

    CertificatoFirma certificato = certificatiService.deleteCertificato("1");

    assertNotNull("Deve esserci il certificato", certificato);
    assertTrue("Il certificato deve avere id 1", certificato.getId() == 1L);

  }

  @Test(expected = NotFoundException.class)
  public void deleteCertificatoNonEsistente() {
    certificatiService.getCertificato("10");
  }

  @Test
  public void updateUltimoCertificato() {



    CertificatoFirma certificatoFirmaAggiornate = certificatiService.putUltimoCertificatoUsato("1");

    assertNotNull("Deve esserci il certificato", certificatoFirmaAggiornate);
    assertTrue("Il certificato deve avere ultimoUtilizzato true",
        certificatoFirmaAggiornate.isUltimoUtilizzato() == Boolean.TRUE);

  }

  @Test(expected = NotFoundException.class)
  public void updateUltimoCertificatoConIdNonTrovato() {

    certificatiService.putUltimoCertificatoUsato("10");
  }

  @Test(expected = BadRequestException.class)
  public void getCertificatoIdNotNumeric() {
    certificatiService.getCertificato(PROVA);
  }

  @Test(expected = BadRequestException.class)
  public void postCertificatoConIdValorizzato() {
    CertificatoFirma body = new CertificatoFirma();
    body.setId(1L);
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.postCertificato(body);
  }

  @Test(expected = BadRequestException.class)
  public void putCertificatoIdNotNumeric() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato(PROVA, body);
  }

  @Test(expected = NotFoundException.class)
  public void putCertificatoNotFound() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("111", body);
  }

  @Test(expected = BadRequestException.class)
  public void putCertificatoUtenteDiverso() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("2", body);
  }

  @Test(expected = BadRequestException.class)
  public void deleteCertificatoIdNotNumeric() {
    certificatiService.deleteCertificato(PROVA);
  }

  @Test(expected = NotFoundException.class)
  public void deleteCertificatoNotFound() {
    certificatiService.deleteCertificato("222");
  }

  @Test(expected = NotFoundException.class)
  public void putCertificatoConProfiloFEQNotFound() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    profiloFeq.setCodice(DESCRIZIONE);
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(CERT);
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("1", body);
  }

  @Test(expected = NotFoundException.class)
  public void putCertificatoConSceltaMarcaTemporaleNotFound() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    profiloFeq.setCodice(PROFILO_FEQ);
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(CERT);
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(DESCRIZIONE);
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("1", body);
  }

  @Test(expected = NotFoundException.class)
  public void putCertificatoConCredenzialiFirmaNotFound() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    profiloFeq.setCodice("PROFILOFEQ1");
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(DESCRIZIONE);
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(CERT);
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice("SI");
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("1", body);
  }

  @Test(expected = NotFoundException.class)
  public void putCertificatoConTipoOTPNotFound() {
    CertificatoFirma body = new CertificatoFirma();
    ProfiloFEQ profiloFeq = new ProfiloFEQ();
    profiloFeq.setCodice("PROFILOFEQ1");
    body.setProfiloFEQ(profiloFeq);
    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice("TIPOCREDFIRMA1");
    body.setTipoCredenzialiFirma(tipoCredenzialiFirma);
    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(CERT);
    body.setEnteCertificatore(enteCertificatore);
    body.setUsername(USERNAME);
    body.setPin("pin");
    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(DESCRIZIONE);
    body.setTipoOTP(tipoOTP);
    body.setDescrizione(DESCRIZIONE);
    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice("SI");
    body.setSceltaMarcaTemporale(sceltaMarcaTemporale);
    certificatiService.putCertificato("1", body);
  }

  @Test
  public void salvaCertificatoNoPasswordEDtScadenza() {
    CertificatoFirma certificatoFirma = new CertificatoFirma();
    certificatoFirma.setDescrizione(DESCRIZIONE);
    certificatoFirma.setUsername(USERNAME);
    certificatoFirma.setPassword("");
    certificatoFirma.setPin(PIN);

    EnteCertificatore enteCertificatore = new EnteCertificatore();
    enteCertificatore.setCodice(ENTE_CREDITORE);
    certificatoFirma.setEnteCertificatore(enteCertificatore);

    ProfiloFEQ profiloFEQ = new ProfiloFEQ();
    profiloFEQ.setCodice(PROFILO_FEQ);
    certificatoFirma.setProfiloFEQ(profiloFEQ);

    SceltaMarcaTemporale sceltaMarcaTemporale = new SceltaMarcaTemporale();
    sceltaMarcaTemporale.setCodice(SCELTA_MARCA_TEMPORALE);
    certificatoFirma.setSceltaMarcaTemporale(sceltaMarcaTemporale);

    TipoCredenzialiFirma tipoCredenzialiFirma = new TipoCredenzialiFirma();
    tipoCredenzialiFirma.setCodice(TIPO_CREDENZIALI_FIRMA);
    certificatoFirma.setTipoCredenzialiFirma(tipoCredenzialiFirma);

    TipoOTP tipoOTP = new TipoOTP();
    tipoOTP.setCodice(TIPO_OTP);
    certificatoFirma.setTipoOTP(tipoOTP);
    certificatoFirma.setUltimoUtilizzato(false);

    CertificatoFirma certificatoFirmaSalvate = certificatiService.postCertificato(certificatoFirma);
    assertNotNull(certificatoFirmaSalvate);
    assertNull(certificatoFirmaSalvate.getDataScadenza());
    assertNull(certificatoFirmaSalvate.getPassword());
  }
  
  @Test(expected = BadRequestException.class)
  public void putUltimoCertificatoUsatoNotNumeric() {
    certificatiService.putUltimoCertificatoUsato(DESCRIZIONE);
  }
}
