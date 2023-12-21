/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.ApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsterna;
import it.csi.cosmo.cosmoauthorization.dto.rest.ApplicazioneEsternaConValidita;
import it.csi.cosmo.cosmoauthorization.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsterna;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ApplicazioneEsternaServiceImplTest extends ParentIntegrationTest {

  private static final String APPLIC_NOT_NULL = "L'applicazione deve esistere";
  private static final String CAMPI_TECNICI_NOT_NULL = "Devono esserci i campi tecnici";
  private static final String FUNZIONALITA_PRINCIPALE_NOT_NULL =
      "Deve esserci la funzionalita' principale";
  private static final String ASSOCIAZIONE_UTENTI = "Devono esserci utenti associati";

  private static final String DESCRIZIONE_APPLICAZIONE = "Descrizione applicazione";
  private static final String ICONA =
      "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";
  private static final String DESCRIZIONE_FUNZIONALITA = "Descrizione funzionalita'";
  private static final String URL = "www.google.com";
  @Autowired
  private ApplicazioneEsternaService applicazioneEsternaService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());
  }

  @Test
  public void eliminaApplicazione() {
    applicazioneEsternaService.eliminaApplicazione("1");
  }

  @Test
  public void eliminaApplicazioneAssociata() {
    applicazioneEsternaService.eliminaApplicazioneAssociata("1");
  }

  @Test(expected = NotFoundException.class)
  public void deleteApplicazioneNonEsistente() {
    applicazioneEsternaService.eliminaApplicazione("10");
  }

  @Test
  public void getApplicazioni() {
    List<ApplicazioneEsterna> applicazioni = applicazioneEsternaService.getApplicazioni(false);

    assertNotNull(APPLIC_NOT_NULL, applicazioni);
    assertTrue("Ci sono due applicazioni", applicazioni.size() == 2);
    assertTrue("La prima applicazione ha 3 funzionalita",
        applicazioni.get(0).getFunzionalita().size() == 3);
    assertTrue("La seconda applicazione ha 1 funzionalita",
        applicazioni.get(1).getFunzionalita().size() == 1);
  }

  @Test
  public void getApplicazioniConfigurate() {
    List<ApplicazioneEsterna> applicazioni = applicazioneEsternaService.getApplicazioni(true);

    assertNotNull(APPLIC_NOT_NULL, applicazioni);
    assertTrue("Devono esserci un'applicazione", applicazioni.size() == 1);
    assertTrue("La prima applicazione ha 2 funzionalita associata all'utente",
        applicazioni.get(0).getFunzionalita().size() == 2);
  }

  @Test
  public void getApplicazioniAssociateEnte() {
    List<ApplicazioneEsternaConValidita> applicazioni =
        applicazioneEsternaService.getApplicazioniAssociateEnte(true);

    assertNotNull(APPLIC_NOT_NULL, applicazioni);
    assertTrue("Devono esserci due applicazioni", applicazioni.size() == 2);
    assertTrue("La prima applicazione deve avere utenti associati",
        Boolean.TRUE.equals(applicazioni.get(0).isAssociataUtenti()));
    assertTrue("La seconda applicazione non deve avere utenti associati",
        Boolean.FALSE.equals(applicazioni.get(1).isAssociataUtenti()));
    assertNotNull(FUNZIONALITA_PRINCIPALE_NOT_NULL,
        applicazioni.get(0).getFunzionalitaPrincipale());
    assertNotNull(FUNZIONALITA_PRINCIPALE_NOT_NULL,
        applicazioni.get(1).getFunzionalitaPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, applicazioni.get(0).getCampiTecnici());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, applicazioni.get(1).getCampiTecnici());

  }

  @Test
  public void getApplicazioniNonAssociateEnte() {
    List<ApplicazioneEsternaConValidita> applicazioni =
        applicazioneEsternaService.getApplicazioniAssociateEnte(false);

    assertNotNull(APPLIC_NOT_NULL, applicazioni);
    assertNotNull("Deve esserci una applicazione", applicazioni.size() == 1);
    assertNull("Non ha utenti associati", applicazioni.get(0).isAssociataUtenti());
    assertNull("Non deve esserci la funzionalita' principale",
        applicazioni.get(0).getFunzionalitaPrincipale());
    assertNull("Non devono esserci i campi tecnici", applicazioni.get(0).getCampiTecnici());
  }


  @Test
  public void getApplicazione() {
    ApplicazioneEsterna applicazione = applicazioneEsternaService.getApplicazione("1");

    assertNotNull(APPLIC_NOT_NULL, applicazione);
  }

  @Test(expected = NotFoundException.class)
  public void getApplicazioneNonEsistente() {
    applicazioneEsternaService.getApplicazione("10");
  }

  @Test
  public void getApplicazioneAssociateEnte() {
    ApplicazioneEsternaConValidita applicazione =
        applicazioneEsternaService.getApplicazioneAssociataEnte("1");

    assertNotNull(APPLIC_NOT_NULL, applicazione);
    assertNotNull(FUNZIONALITA_PRINCIPALE_NOT_NULL, applicazione.getFunzionalitaPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, applicazione.getCampiTecnici());
    assertTrue(ASSOCIAZIONE_UTENTI, Boolean.TRUE.equals(applicazione.isAssociataUtenti()));
  }

  @Test
  public void getApplicazioneNonAssociateEnte() {
    ApplicazioneEsternaConValidita applicazione =
        applicazioneEsternaService.getApplicazioneAssociataEnte("3");

    assertNotNull(APPLIC_NOT_NULL, applicazione);
    assertNull("Non deve esserci la funzionalita' principale",
        applicazione.getFunzionalitaPrincipale());
    assertNull("Non devono esserci i campi tecnici", applicazione.getCampiTecnici());
    assertNull("Non deve avere utenti associati", applicazione.isAssociataUtenti());
  }

  @Test
  public void salvaApplicazione() {

    ApplicazioneEsterna applicazioneDaSalvare = new ApplicazioneEsterna();
    applicazioneDaSalvare.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneDaSalvare.setIcona(ICONA);

    ApplicazioneEsterna applicazioneSalvata =
        applicazioneEsternaService.salvaApplicazione(applicazioneDaSalvare);

    assertNotNull(APPLIC_NOT_NULL, applicazioneSalvata);
    assertNotNull("Deve esserci un nuovo id", applicazioneSalvata.getId());
  }

  @Test(expected = BadRequestException.class)
  public void salvaApplicazioneSenzaIcona() {

    ApplicazioneEsterna applicazioneDaSalvare = new ApplicazioneEsterna();
    applicazioneDaSalvare.setDescrizione(DESCRIZIONE_APPLICAZIONE);

    applicazioneEsternaService.salvaApplicazione(applicazioneDaSalvare);
  }

  @Test
  public void aggiornaApplicazioneEsistente() {
    final String nuovaDescrizioneApplicazione = "Nuova descrizione per l'applicazione";

    ApplicazioneEsterna applicazioneDaAggiornare = new ApplicazioneEsterna();
    applicazioneDaAggiornare.setDescrizione(nuovaDescrizioneApplicazione);
    applicazioneDaAggiornare.setIcona(ICONA);

    ApplicazioneEsterna applicazioneAggiornata =
        applicazioneEsternaService.aggiornaApplicazione("1", applicazioneDaAggiornare);

    assertNotNull(APPLIC_NOT_NULL, applicazioneAggiornata);
    assertTrue(!DESCRIZIONE_APPLICAZIONE.equals(applicazioneAggiornata.getDescrizione()));
    assertTrue(nuovaDescrizioneApplicazione.equals(applicazioneAggiornata.getDescrizione()));
  }

  @Test
  public void nuovaAssociazioneUtente() {
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(2L);
    applicazioneEsterna.setDescrizione("Applicazione test 1");
    applicazioneEsterna.setPosizione(0);
    FunzionalitaApplicazioneEsterna funzionalita = new FunzionalitaApplicazioneEsterna();
    funzionalita.setId(5l);
    applicazioneEsterna.setFunzionalita(Arrays.asList(funzionalita));


    List<ApplicazioneEsterna> nuovaRelazione =
        applicazioneEsternaService
        .associaDissociaAppUtente(Arrays.asList(applicazioneEsterna));

    assertNotNull(APPLIC_NOT_NULL, nuovaRelazione);
    assertTrue("Deve esserci un' applicazione", nuovaRelazione.size() == 1);
    assertTrue("L' applicazione ha 1 funzionalita associata all'utente",
        nuovaRelazione.get(0).getFunzionalita().size() == 1);

  }


  @Test
  public void rimuoviTutteAssociazioniUtente() {

    List<ApplicazioneEsterna> fineRelazione =
        applicazioneEsternaService.associaDissociaAppUtente(Arrays.asList());

    assertNotNull(APPLIC_NOT_NULL, fineRelazione);
    assertTrue("Non devono esserci applicazioni", fineRelazione.isEmpty());
  }

  @Test
  public void nuovaAssociazioneEnte() {
    ApplicazioneEsternaConValidita applicazioneEsterna = new ApplicazioneEsternaConValidita();
    applicazioneEsterna.setId(3L);
    applicazioneEsterna.setDescrizione("Applicazione test 3");
    applicazioneEsterna.setIcona(ICONA);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    applicazioneEsterna.setCampiTecnici(campiTecnici);

    FunzionalitaApplicazioneEsterna funzionalitaPrincipale = new FunzionalitaApplicazioneEsterna();
    funzionalitaPrincipale.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalitaPrincipale.setUrl(URL);
    funzionalitaPrincipale.setPrincipale(true);

    applicazioneEsterna.setFunzionalitaPrincipale(funzionalitaPrincipale);

    ApplicazioneEsternaConValidita nuovaRelazione =
        applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazioneEsterna);

    assertNotNull(APPLIC_NOT_NULL, nuovaRelazione);
    assertNotNull(FUNZIONALITA_PRINCIPALE_NOT_NULL, nuovaRelazione.getFunzionalitaPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, nuovaRelazione.getCampiTecnici());
    assertNull("Non devono esserci utenti associati", nuovaRelazione.isAssociataUtenti());
  }

  @Test
  public void modificaAssociazioneEnte() {
    ApplicazioneEsternaConValidita applicazioneEsterna = new ApplicazioneEsternaConValidita();
    applicazioneEsterna.setId(1L);
    applicazioneEsterna.setDescrizione("Applicazione test 1");
    applicazioneEsterna.setIcona(ICONA);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    applicazioneEsterna.setCampiTecnici(campiTecnici);

    String nuovaDescrizioneFunzionalita = "Nuova descrizione della funzionalita";
    String nuovoUrl = "www.google.it";
    FunzionalitaApplicazioneEsterna funzionalitaPrincipale = new FunzionalitaApplicazioneEsterna();
    funzionalitaPrincipale.setDescrizione(nuovaDescrizioneFunzionalita);
    funzionalitaPrincipale.setUrl(nuovoUrl);
    funzionalitaPrincipale.setPrincipale(true);

    applicazioneEsterna.setFunzionalitaPrincipale(funzionalitaPrincipale);

    ApplicazioneEsternaConValidita modificaRelazione =
        applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazioneEsterna);

    assertNotNull(APPLIC_NOT_NULL, modificaRelazione);
    assertNotNull(FUNZIONALITA_PRINCIPALE_NOT_NULL, modificaRelazione.getFunzionalitaPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, modificaRelazione.getCampiTecnici());
    assertTrue(ASSOCIAZIONE_UTENTI, Boolean.TRUE.equals(modificaRelazione.isAssociataUtenti()));
    assertTrue(
        !"Funzionalita test 1 di app 1 per ente 1".equals(modificaRelazione.getFunzionalitaPrincipale().getDescrizione()));
    assertTrue(nuovaDescrizioneFunzionalita.equals(modificaRelazione.getFunzionalitaPrincipale().getDescrizione()));
    assertTrue(!URL.equals(modificaRelazione.getFunzionalitaPrincipale().getUrl()));
    assertTrue(nuovoUrl.equals(modificaRelazione.getFunzionalitaPrincipale().getUrl()));
  }

  @Test
  public void getTutteApplicazioni() {
    List<ApplicazioneEsternaConValidita> applicazioni =
        applicazioneEsternaService.getTutteApplicazioni();

    assertNotNull(APPLIC_NOT_NULL, applicazioni);
    assertTrue("Devono esserci cinque applicazioni", applicazioni.size() == 5);
    assertTrue("La prima applicazione deve avere 2 enti associati",
        applicazioni.get(0).getNumEntiAssociati() == 2);
    assertTrue("La seconda applicazione deve avere 1 ente associato",
        applicazioni.get(1).getNumEntiAssociati() == 1);
    assertTrue("La terza applicazione deve avere 1 ente associato",
        applicazioni.get(2).getNumEntiAssociati() == 1);
    assertTrue("La quarta applicazione non ha enti associati",
        applicazioni.get(3).getNumEntiAssociati() == 0);
    assertTrue("La quinta applicazione deve avere 1 ente associato",
        applicazioni.get(4).getNumEntiAssociati() == 1);
  }
  
  @Test(expected = NotFoundException.class)
  public void eliminaApplicazioneAssociataNotFound() {
    applicazioneEsternaService.eliminaApplicazioneAssociata("111");
  }
  
  @Test(expected = NotFoundException.class)
  public void getApplicazioneAssociataEnteNotFound() {
    applicazioneEsternaService.getApplicazioneAssociataEnte("111");
  }
  
  @Test
  public void getApplicazioneAssociataSenzaFunzionalita() {
    ApplicazioneEsternaConValidita response = applicazioneEsternaService.getApplicazioneAssociataEnte("5");
    assertNotNull(response);
    assertNull(response.getFunzionalitaPrincipale());
  }
  
  @Test(expected = BadRequestException.class)
  public void salvaApplicazioneConId() {
    ApplicazioneEsterna body = new ApplicazioneEsterna();
    body.setId(1L);
    body.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsternaService.salvaApplicazione(body);
  }
  
  @Test(expected = BadRequestException.class)
  public void salvaApplicazioneConIconaBlank() {
    ApplicazioneEsterna body = new ApplicazioneEsterna();
    body.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    body.setIcona("");
    applicazioneEsternaService.salvaApplicazione(body);
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaApplicazioneNotFound() {
    ApplicazioneEsterna body = new ApplicazioneEsterna();
    body.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsternaService.aggiornaApplicazione("111", body);
  }
  
  @Test
  public void aggiornaApplicazioneConIconaBlank() {
    ApplicazioneEsterna body = new ApplicazioneEsterna();
    body.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    body.setIcona("");
    applicazioneEsternaService.aggiornaApplicazione("1", body);
  }
  
  @Test(expected = NotFoundException.class)
  public void associaDissociaAppUtenteConApplicazioniNull() {
    applicazioneEsternaService.associaDissociaAppUtente(null);
  }
  
  @Test(expected = BadRequestException.class)
  public void associaDissociaAppUtenteConIdApplicazioneNull() {
    List<ApplicazioneEsterna> applicazioni = new ArrayList<>();
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(null);
    applicazioneEsterna.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsterna.setPosizione(1);
    applicazioni.add(applicazioneEsterna);
    applicazioneEsternaService.associaDissociaAppUtente(applicazioni);
  }
  
  @Test(expected = NotFoundException.class)
  public void associaDissociaAppUtenteConApplicazioneDaAssociareNotFound() {
    List<ApplicazioneEsterna> applicazioni = new ArrayList<>();
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(111L);
    applicazioneEsterna.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsterna.setPosizione(1);
    applicazioni.add(applicazioneEsterna);
    applicazioneEsternaService.associaDissociaAppUtente(applicazioni);
  }
  
  @Test(expected = NotFoundException.class)
  public void associaDissociaAppUtenteConAssociazioneEnteNotFound() {
    List<ApplicazioneEsterna> applicazioni = new ArrayList<>();
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(4L);
    applicazioneEsterna.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsterna.setPosizione(1);
    applicazioni.add(applicazioneEsterna);
    applicazioneEsternaService.associaDissociaAppUtente(applicazioni);
  }
  
  @Test(expected = BadRequestException.class)
  public void associaModificaAssociazioneAppEnteConIdAppNull() {
    ApplicazioneEsternaConValidita applicazione = new ApplicazioneEsternaConValidita();
    applicazione.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazione.setIcona(ICONA);
    applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazione );
  }
  
  @Test(expected = BadRequestException.class)
  public void associaModificaAssociazioneAppEnteConCampiTecniciNull() {
    ApplicazioneEsternaConValidita applicazione = new ApplicazioneEsternaConValidita();
    applicazione.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazione.setIcona(ICONA);
    applicazione.setId(1L);
    applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazione );
  }
  
  @Test(expected = BadRequestException.class)
  public void associaModificaAssociazioneAppEnteConFunzionalitaNull() {
    ApplicazioneEsternaConValidita applicazione = new ApplicazioneEsternaConValidita();
    applicazione.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazione.setIcona(ICONA);
    applicazione.setId(1L);
    CampiTecnici campiTecnici = new CampiTecnici();
    applicazione.setCampiTecnici(campiTecnici);
    applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazione );
  }
  
  @Test(expected = NotFoundException.class)
  public void associaModificaAssociazioneAppEnteConApplicazioneNotFound() {
    ApplicazioneEsternaConValidita applicazione = new ApplicazioneEsternaConValidita();
    applicazione.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazione.setIcona(ICONA);
    applicazione.setId(111L);
    FunzionalitaApplicazioneEsterna funzionalita = new FunzionalitaApplicazioneEsterna();
    funzionalita.setPrincipale(true);
    funzionalita.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    funzionalita.setUrl(URL);
    applicazione.setFunzionalitaPrincipale(funzionalita);
    CampiTecnici campiTecnici = new CampiTecnici();
    applicazione.setCampiTecnici(campiTecnici);
    applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazione );
  }
  
  @Test
  public void associaModificaAssociazioneAppEnteSenzaFunzionalita() {
    ApplicazioneEsternaConValidita applicazione = new ApplicazioneEsternaConValidita();
    applicazione.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazione.setIcona(ICONA);
    applicazione.setId(5L);
    FunzionalitaApplicazioneEsterna funzionalita = new FunzionalitaApplicazioneEsterna();
    funzionalita.setPrincipale(true);
    funzionalita.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalita.setUrl(URL);
    applicazione.setFunzionalitaPrincipale(funzionalita);
    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtFineVal(null);
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    applicazione.setCampiTecnici(campiTecnici);
    applicazioneEsternaService.associaModificaAssociazioneAppEnte(applicazione );
  }
  
  @Test(expected = NotFoundException.class)
  public void associaDissociaAppUtenteFunzionalitaNonCorretta() {
    List<ApplicazioneEsterna> applicazioni = new ArrayList<>();
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(5L);
    applicazioneEsterna.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsterna.setPosizione(1);
    List<FunzionalitaApplicazioneEsterna> listaFunzionalita = new ArrayList<>();
    FunzionalitaApplicazioneEsterna funzionalita = new FunzionalitaApplicazioneEsterna();
    funzionalita.setId(1L);
    listaFunzionalita.add(funzionalita);
    applicazioneEsterna.setFunzionalita(listaFunzionalita);
    applicazioni.add(applicazioneEsterna);
    applicazioneEsternaService.associaDissociaAppUtente(applicazioni);
  }
  
  @Test
  public void associaDissociaAppUtente() {
    List<ApplicazioneEsterna> applicazioni = new ArrayList<>();
    ApplicazioneEsterna applicazioneEsterna = new ApplicazioneEsterna();
    applicazioneEsterna.setId(1L);
    applicazioneEsterna.setDescrizione(DESCRIZIONE_APPLICAZIONE);
    applicazioneEsterna.setPosizione(1);
    List<FunzionalitaApplicazioneEsterna> listaFunzionalita = new ArrayList<>();
    FunzionalitaApplicazioneEsterna funzionalita = new FunzionalitaApplicazioneEsterna();
    funzionalita.setId(1L);
    listaFunzionalita.add(funzionalita);
    applicazioneEsterna.setFunzionalita(listaFunzionalita);
    applicazioni.add(applicazioneEsterna);
    applicazioneEsternaService.associaDissociaAppUtente(applicazioni);
  }
}
