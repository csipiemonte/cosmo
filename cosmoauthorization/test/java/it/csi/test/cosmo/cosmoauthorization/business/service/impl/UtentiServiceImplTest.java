/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.ManagedException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.cosmoauthorization.business.batch.impl.BatchSchedulerServiceImpl;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiService;
import it.csi.cosmo.cosmoauthorization.config.ErrorMessages;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneUtenteProfilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtenteCampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.UtentiResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class UtentiServiceImplTest extends ParentIntegrationTest {

  private static final String L_UTENTE_DEVE_ESSERE_NULLO = "L'utente deve essere nullo";
  private static final String UTENTE_NON_NULLO = "utente non nullo";

  @Autowired
  private UtentiService utentiService;

  @Autowired
  BatchSchedulerServiceImpl batchSchedulerServiceImpl;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());
  }


  @Test
  public void provaBatchUtenti() {
    this.batchSchedulerServiceImpl.launchBatchUtenti();
  }

  @Test
  public void getUtenti() {
    UtentiResponse utenti = utentiService.getUtenti(
        "{\"filter\":{\"email\":{\"in\":[\"test@test.it\"]},\"nome\":{\"ci\":\"Utente\"} },\"sort\":\"+email, -cognome\",\"fields\":\"email, cognome\", \"page\": 0,\"size\":10}");

    assertNotNull(UTENTE_NON_NULLO, utenti.getUtenti());
  }

  @Test
  public void getUtentiEnte() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());

    UtentiResponse utenti = utentiService
        .getUtentiEnte("{\"page\": 0,\"size\":10}");

    assertNotNull(UTENTE_NON_NULLO, utenti.getUtenti());
  }


  @Test
  public void getUtente() {
    Utente utente = utentiService.getUtenteDaId("1");

    assertNotNull(UTENTE_NON_NULLO, utente);
  }

  @Test
  public void getUtenteDaCodiceFiscale() {
    Utente utente = utentiService.getUtenteDaCodiceFiscale("AAAAAA00B77B000F");

    assertNotNull(UTENTE_NON_NULLO, utente);
  }

  @Test
  public void getUtenteConIdNullo() {
    try {

      utentiService.getUtenteDaId("");

      fail(L_UTENTE_DEVE_ESSERE_NULLO);
    } catch (ManagedException e) {
      assertTrue(e.getMessage()
          .equals(String.format(ErrorMessages.PARAMETRO_NON_VALORIZZATO, "id utente")));
    }
  }

  @Test
  public void getUtenteNonPresenteNelDb() {
    try {

      utentiService.getUtenteDaId("1000");

      fail(L_UTENTE_DEVE_ESSERE_NULLO);
    } catch (ManagedException e) {
      assertTrue(e.getMessage().equals(String.format(ErrorMessages.U_UTENTE_NON_TROVATO, "1000")));
    }
  }

  @Test
  public void getUtenteCorrente() {

    Utente utenteCorrente = utentiService.getUtenteCorrente();

    assertNotNull("utente corrente non puo' essere mai nullo", utenteCorrente);
  }

  @Test
  public void createUtente() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());
    UtenteCampiTecnici utenteCampiTecniciDaCreare = new UtenteCampiTecnici();
    Utente utenteDaCreare = new Utente();
    utenteDaCreare.setCodiceFiscale("AAAAAA00A00A000N");
    utenteDaCreare.setNome("Test");
    utenteDaCreare.setCognome("Test");

    Ente enteDaCreare = new Ente();
    enteDaCreare.setId(1L);
    enteDaCreare.setNome("Test ente");

    AssociazioneEnteUtente associazioneEnteUtente = new AssociazioneEnteUtente();
    associazioneEnteUtente.setEnte(enteDaCreare);

    associazioneEnteUtente.setEmail("test1@test.it");
    associazioneEnteUtente.setTelefono("0123456789");

    utenteDaCreare.setEnti(Arrays.asList(associazioneEnteUtente));

    Profilo profilo = new Profilo();
    profilo.setId(1L);
    profilo.setCodice("ADMIN");
    profilo.setDescrizione("descrizione profilo");

    AssociazioneUtenteProfilo utenteProfilo = new AssociazioneUtenteProfilo();
    utenteProfilo.setEnte(enteDaCreare);
    utenteProfilo.setProfilo(profilo);
    utenteDaCreare.setProfili(Arrays.asList(utenteProfilo));

    utenteCampiTecniciDaCreare.setUtente(utenteDaCreare);
    utenteCampiTecniciDaCreare.setDtInizioValidita(OffsetDateTime.now());

    Utente utenteCreato = utentiService.createUtente(utenteCampiTecniciDaCreare);

    assertNotNull(UTENTE_NON_NULLO, utenteCreato);
  }

  @Test
  public void updateUtente() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());

    UtenteCampiTecnici utenteCampiTecniciDaAggiornare = new UtenteCampiTecnici();

    Utente utenteSulDB = utentiService.getUtenteDaId("1");

    Utente utenteDaAggiornare = new Utente();
    utenteDaAggiornare.setId(1l);
    utenteDaAggiornare.setCodiceFiscale("AAAAAA00A00A000N");
    utenteDaAggiornare.setNome("Test");
    utenteDaAggiornare.setCognome("Cambio cognome");

    Profilo profilo = new Profilo();
    profilo.setCodice("cod nuovo");
    profilo.setDescrizione("descrizione profilo nuovo");

    Ente enteDaAggiornare = new Ente();
    enteDaAggiornare.setId(1l);
    enteDaAggiornare.setNome("Ente Di Prova");

    AssociazioneEnteUtente associazioneEnteUtente = new AssociazioneEnteUtente();
    associazioneEnteUtente.setEnte(enteDaAggiornare);

    associazioneEnteUtente.setEmail("test1@test.it");
    associazioneEnteUtente.setTelefono("0123456789");

    utenteDaAggiornare.setEnti(Arrays.asList(associazioneEnteUtente));

    AssociazioneUtenteProfilo utenteProfilo = new AssociazioneUtenteProfilo();
    utenteProfilo.setEnte(enteDaAggiornare);
    utenteProfilo.setProfilo(profilo);

    utenteCampiTecniciDaAggiornare.setUtente(utenteDaAggiornare);
    utenteCampiTecniciDaAggiornare.setDtInizioValidita(OffsetDateTime.now());
    Utente utenteAggiornato = utentiService.updateUtente(utenteCampiTecniciDaAggiornare);

    assertNotNull(UTENTE_NON_NULLO, utenteAggiornato);
    assertEquals(utenteDaAggiornare.getCognome(), utenteAggiornato.getCognome());

    assertNotEquals(utenteSulDB.getCognome(), utenteAggiornato.getCognome());
  }

  @Test
  public void deleteUtente() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());

    Utente utente = utentiService.deleteUtente("1");

    assertNotNull(UTENTE_NON_NULLO, utente);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteUtenteNotFound() {
    utentiService.deleteUtente("111");
  }
  
  @Test
  public void getUtentiConEnte() {
    UtentiResponse utenti = utentiService.getUtenti(
        "{\"filter\":{ \"idEnte\":{\"eq\":\"1\"} }, \"page\": 0,\"size\":10}");

    assertNotNull(UTENTE_NON_NULLO, utenti.getUtenti());
  }
  
  @Test(expected = NotFoundException.class)
  public void getUtenteConValiditaUtenteNotFound() {
    utentiService.getUtenteConValidita("111", "1");
  }
  
  @Test
  public void getUtenteConValidita() {
    UtenteCampiTecnici utente = utentiService.getUtenteConValidita("1", "1");
    assertNotNull(utente);
    assertNotNull(utente.getUtente());
    assertNotNull(utente.getUtente().getCodiceFiscale());
    assertTrue(utente.getUtente().getCodiceFiscale().equals("AAAAAA00B77B000F"));
  }
  
  @Test
  public void getUtenteConValiditaDataFineNotNull() {
    UtenteCampiTecnici utente = utentiService.getUtenteConValidita("1", "2");
    assertNull(utente);
  }
  
  @Test(expected = BadRequestException.class)
  public void getUtenteDaCodiceFiscaleBlank() {
    utentiService.getUtenteDaCodiceFiscale("");
  }
  
  @Test(expected = NotFoundException.class)
  public void getUtenteDaCodiceFiscaleErrato() {
    utentiService.getUtenteDaCodiceFiscale("prova111");
  }
  
  @Test(expected = BadRequestException.class)
  public void createUtenteNull() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    utentiService.createUtente(utenteCampiTecnici );
  }
  
  @Test(expected = BadRequestException.class)
  public void createUtenteConNomeNull() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utenteCampiTecnici.setUtente(utente);
    utentiService.createUtente(utenteCampiTecnici );
  }
  
  @Test(expected = BadRequestException.class)
  public void createUtenteConCognomeNull() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setNome("nome");
    utenteCampiTecnici.setUtente(utente);
    utentiService.createUtente(utenteCampiTecnici );
  }
  
  @Test(expected = BadRequestException.class)
  public void createUtenteConCFNull() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setNome("nome");
    utente.setCognome("cognome");
    utenteCampiTecnici.setUtente(utente);
    utentiService.createUtente(utenteCampiTecnici );
  }
  
  @Test
  public void createUtenteEsistente() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setNome("nome");
    utente.setCognome("cognome");
    utente.setCodiceFiscale("AAAAAA00B77B000F");
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    Utente response = utentiService.createUtente(utenteCampiTecnici);
    assertNotNull(response);
    assertNotNull(response.getCodiceFiscale());
    assertEquals(response.getCodiceFiscale(), "AAAAAA00B77B000F");
  }
  
  @Test(expected = UnprocessableEntityException.class)
  public void createUtenteSenzaAssociazioni() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setNome("nome");
    utente.setCognome("cognome");
    utente.setCodiceFiscale("AAAAAA00B77B000F");
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utentiService.createUtente(utenteCampiTecnici);
  }
  
  @Test(expected = BadRequestException.class)
  public void updateUtenteNull() {
    utentiService.updateUtente(null);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateUtenteNotFound() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(111L);
    utenteCampiTecnici.setUtente(utente);
    utentiService.updateUtente(utenteCampiTecnici);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateUtenteProfiloNotFound() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(2L);
    List<AssociazioneUtenteProfilo> listaProfili = new ArrayList<>();
    AssociazioneUtenteProfilo associazioneProfilo = new AssociazioneUtenteProfilo();
    Profilo profilo = new Profilo();
    associazioneProfilo.setProfilo(profilo);
    listaProfili.add(associazioneProfilo);
    utente.setProfili(listaProfili);
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    utentiService.updateUtente(utenteCampiTecnici);
  }
  
  @Test(expected = ForbiddenException.class)
  public void updateUtenteProfiloNonAssegnabile() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(2L);
    List<AssociazioneUtenteProfilo> listaProfili = new ArrayList<>();
    AssociazioneUtenteProfilo associazioneProfilo = new AssociazioneUtenteProfilo();
    Profilo profilo = new Profilo();
    profilo.setId(4L);
    associazioneProfilo.setProfilo(profilo);
    listaProfili.add(associazioneProfilo);
    utente.setProfili(listaProfili);
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    utentiService.updateUtente(utenteCampiTecnici);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateUtenteEnteNotFound() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(2L);
    List<AssociazioneUtenteProfilo> listaProfili = new ArrayList<>();
    AssociazioneUtenteProfilo associazioneProfilo = new AssociazioneUtenteProfilo();
    Profilo profilo = new Profilo();
    profilo.setId(1L);
    associazioneProfilo.setProfilo(profilo);
    Ente ente = new Ente();
    ente.setId(111L);
    associazioneProfilo.setEnte(ente);
    listaProfili.add(associazioneProfilo);
    utente.setProfili(listaProfili);
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    utentiService.updateUtente(utenteCampiTecnici);
  }
  
  @Test
  public void updateUtenteSenzaProfiliAssociati() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(2L);
    List<AssociazioneUtenteProfilo> listaProfili = new ArrayList<>();
    AssociazioneUtenteProfilo associazioneProfilo = new AssociazioneUtenteProfilo();
    Profilo profilo = new Profilo();
    profilo.setId(1L);
    associazioneProfilo.setProfilo(profilo);
    Ente ente = new Ente();
    ente.setId(1L);
    associazioneProfilo.setEnte(ente);
    listaProfili.add(associazioneProfilo);
    utente.setProfili(listaProfili);
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    Utente response = utentiService.updateUtente(utenteCampiTecnici);
    assertNotNull(response);
    assertNotNull(response.getCodiceFiscale());
    assertEquals(response.getCodiceFiscale(), "AAAAAA00A11B000J");
  }
  
  @Test
  public void updateUtenteConProfiliAssociati() {
    UtenteCampiTecnici utenteCampiTecnici = new UtenteCampiTecnici();
    Utente utente = new Utente();
    utente.setId(1L);
    List<AssociazioneUtenteProfilo> listaProfili = new ArrayList<>();
    AssociazioneUtenteProfilo associazioneProfilo = new AssociazioneUtenteProfilo();
    Profilo profilo = new Profilo();
    profilo.setId(2L);
    associazioneProfilo.setProfilo(profilo);
    Ente ente = new Ente();
    ente.setId(2L);
    associazioneProfilo.setEnte(ente);
    listaProfili.add(associazioneProfilo);
    utente.setProfili(listaProfili);
    List<AssociazioneEnteUtente> enti = new ArrayList<>();
    AssociazioneEnteUtente associazioneEnte = new AssociazioneEnteUtente();
    enti.add(associazioneEnte);
    utente.setEnti(enti);
    utenteCampiTecnici.setUtente(utente);
    utenteCampiTecnici.setDtInizioValidita(OffsetDateTime.now().minusYears(2));
    utenteCampiTecnici.setDtFineValidita(OffsetDateTime.now().plusYears(2));
    Utente response = utentiService.updateUtente(utenteCampiTecnici);
    assertNotNull(response);
    assertNotNull(response.getCodiceFiscale());
    assertEquals(response.getCodiceFiscale(), "AAAAAA00B77B000F");
  }
}
