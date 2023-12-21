/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.ForbiddenException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.exception.UnauthorizedException;
import it.csi.cosmo.cosmoauthorization.business.rest.impl.EntiApiImpl;
import it.csi.cosmo.cosmoauthorization.business.service.EntiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequestNuoviUtentiAmministratori;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaEnteRequestUtentiAmministratori;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.EntiResponse;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class EntiServiceImplTest extends ParentIntegrationTest {

  private static final String ENTE_NON_NULLO = "ente non nullo";
  private static final String CODICE_IPA = "codiceIpa";
  private static final String CF_ENTE_ESISTENTE = "80087670016";
  private static final String CF_UTENTE_NUOVO = "AAAAAA00B77B000G";
  private static final String REGIONE = "Regione Basilicata";
  private static final String CF_ENTE_NUOVO = "80087670017";
  private static final String ADMIN = "ADMIN";
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAmministratoreSistema());
  }
  
  @Autowired
  private EntiService entiService;

  @Test
  public void getEnte() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    EnteResponse ente = (EnteResponse) api.getEntiId(1L, null).getEntity();

    assertNotNull(ENTE_NON_NULLO, ente.getEnte());
  }

  @Test(expected = BadRequestException.class)
  public void getEnteConIdNullo() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);
    api.getEntiId(null, null);
  }

  @Test(expected = NotFoundException.class)
  public void getEnteNonPresenteNelDb() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);
    api.getEntiId(1234567L, null);
  }

  @Test
  public void getEnti() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    EntiResponse enti = (EntiResponse) api.getEnti("", null).getEntity();

    assertNotNull(ENTE_NON_NULLO, enti.getEnti());
  }

  @Test
  public void createEnte() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    var enteDaCreare = new CreaEnteRequest();

    enteDaCreare.setCodiceIpa("codice_1");
    enteDaCreare.setNome("Test");
    enteDaCreare.setCodiceFiscale("0000001");

    List<CreaEnteRequestUtentiAmministratori> admins = new ArrayList<>();

    CreaEnteRequestUtentiAmministratori admin = new CreaEnteRequestUtentiAmministratori();
    admin.setId(1L);
    admin.setEmail("admin@admin.admin");
    admin.setTelefono("330 123 123 45");
    admins.add(admin);
    enteDaCreare.setUtentiAmministratori(admins);

    EnteResponse enteCreato = (EnteResponse) api.postEnti(enteDaCreare, null).getEntity();

    assertNotNull(ENTE_NON_NULLO, enteCreato.getEnte());
  }

  @Test
  public void updateEnte() {
    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    Long id = 1L;

    var request = new AggiornaEnteRequest();
    request.setNome("Nuovo nome");

    EnteResponse updated = (EnteResponse) api.putEnti(id, request, null).getEntity();
    assertNotNull(ENTE_NON_NULLO, updated.getEnte());

    assertEquals(request.getNome(), updated.getEnte().getNome());
  }

  @Test
  public void deleteEnte() {

    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    api.deleteEntiId(1L, null);
  }

  @Ignore("controllo UC non supportato negli IT al momento")
  @Test(expected = UnauthorizedException.class)
  public void deleteEnteGuest() {
    this.autentica(null);

    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    api.deleteEntiId(1L, null);
  }

  @Ignore("controllo UC non supportato negli IT al momento")
  @Test(expected = ForbiddenException.class)
  public void deleteEnteSenzaAutorizzazioni() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());

    var api = buildResource(EntiApiImpl.class);
    assertNotNull(api);

    api.deleteEntiId(1L, null);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteEnteNotFound() {
    entiService.deleteEnte(111L);
  }
  
  @Test
  public void getEnteConConfigurazione() {
    Ente ente = entiService.getEnte(1L);
    assertNotNull(ente);
    assertNotNull(ente.getId());
    assertTrue(ente.getId() == 1);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConUtentiEmpty() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("nome");
    ente.setCodiceFiscale(CF_ENTE_ESISTENTE);
    entiService.createEnte(ente);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConUtentiNull() {
    CreaEnteRequest ente = new CreaEnteRequest();
    ente.setUtentiAmministratori(null);
    ente.setNuoviUtentiAmministratori(null);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("nome");
    ente.setCodiceFiscale(CF_ENTE_ESISTENTE);
    entiService.createEnte(ente);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConUtentiEmptyENuoviUtentiNull() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(null);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("nome");
    ente.setCodiceFiscale(CF_ENTE_ESISTENTE);
    entiService.createEnte(ente);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConUtentiNullENuoviUtentiEmpty() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    ente.setUtentiAmministratori(null);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("nome");
    ente.setCodiceFiscale(CF_ENTE_ESISTENTE);
    entiService.createEnte(ente);
  }
  
  @Test(expected = ConflictException.class)
  public void createEnteConCfEsistente() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_ESISTENTE);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("nome");
    entiService.createEnte(ente);
  }
  
  @Test(expected = ConflictException.class)
  public void createEnteConCodiceIpaEsistente() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa("r_piemon");
    ente.setNome("nome");
    entiService.createEnte(ente);
  }
  
  @Test(expected = ConflictException.class)
  public void createEnteConNomeEsistente() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome("Regione Piemonte");
    entiService.createEnte(ente);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConUtenteNotFound() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(111L);
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome(REGIONE);
    entiService.createEnte(ente);
  }
  
  @Test(expected = BadRequestException.class)
  public void createEnteConNuovoUtenteEsistente() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuovoUtente.setCodiceFiscale("AAAAAA00B77B000F");
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome(REGIONE);
    entiService.createEnte(ente);
  }
  
  @Test
  public void createEnteConNuovoUtente() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuovoUtente.setCodiceFiscale(CF_UTENTE_NUOVO);
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome(REGIONE);
    Ente response = entiService.createEnte(ente);
    assertNotNull(response);
    assertNotNull(response.getNome());
    assertTrue(response.getNome().equals(REGIONE));
  }
  
  @Test
  public void createEnteConNuovoUtenteECodiceProfiloDefault() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuovoUtente.setCodiceFiscale(CF_UTENTE_NUOVO);
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome(REGIONE);
    ente.setCodiceProfiloDefault(ADMIN);
    Ente response = entiService.createEnte(ente);
    assertNotNull(response);
    assertNotNull(response.getNome());
    assertTrue(response.getNome().equals(REGIONE));
  }
  
  @Test
  public void createEnteConNuovoUtenteECodiceProfiloDefaultNotFound() {
    CreaEnteRequest ente = new CreaEnteRequest();
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuovoUtente.setCodiceFiscale(CF_UTENTE_NUOVO);
    nuoviUtenti.add(nuovoUtente);
    ente.setUtentiAmministratori(utenti);
    ente.setNuoviUtentiAmministratori(nuoviUtenti);
    ente.setCodiceFiscale(CF_ENTE_NUOVO);
    ente.setCodiceIpa(CODICE_IPA);
    ente.setNome(REGIONE);
    ente.setCodiceProfiloDefault(CODICE_IPA);
    Ente response = entiService.createEnte(ente);
    assertNotNull(response);
    assertNotNull(response.getNome());
    assertTrue(response.getNome().equals(REGIONE));
  }
  
  @Test(expected = NotFoundException.class)
  public void updateEnteNotFound() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(CODICE_IPA);
    entiService.updateEnte(111L, request);
  }
  
  @Test(expected = ConflictException.class)
  public void updateEnteConNomeEsistente() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome("Regione Piemonte");
    entiService.updateEnte(2L, request);
  }
  
  @Test(expected = BadRequestException.class)
  public void updateEnteConUtenteAmministratoreNotFound() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(REGIONE);
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(111L);
    utenti.add(utente);
    request.setUtentiAmministratori(utenti);
    entiService.updateEnte(1L, request);
  }
  
  @Test
  public void updateEnteConCodiceProfilo() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(REGIONE);
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    request.setUtentiAmministratori(utenti);
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    request.setNuoviUtentiAmministratori(nuoviUtenti);
    request.setCodiceProfiloDefault(ADMIN);
    entiService.updateEnte(1L, request);
  }
  
  @Test
  public void updateEnteConCodiceProfiloNuovo() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(REGIONE);
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    request.setUtentiAmministratori(utenti);
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    request.setNuoviUtentiAmministratori(nuoviUtenti);
    request.setCodiceProfiloDefault(ADMIN);
    entiService.updateEnte(2L, request);
  }
  
  @Test
  public void updateEnteConCodiceProfiloNotFound() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(REGIONE);
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    request.setUtentiAmministratori(utenti);
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    request.setNuoviUtentiAmministratori(nuoviUtenti);
    request.setCodiceProfiloDefault(REGIONE);
    entiService.updateEnte(2L, request);
  }
  
  @Test
  public void updateEnteConCodiceProfiloBlank() {
    AggiornaEnteRequest request = new AggiornaEnteRequest();
    request.setNome(REGIONE);
    List<CreaEnteRequestUtentiAmministratori> utenti = new ArrayList<>();
    CreaEnteRequestUtentiAmministratori utente = new CreaEnteRequestUtentiAmministratori();
    utente.setId(1L);
    utenti.add(utente);
    request.setUtentiAmministratori(utenti);
    List<CreaEnteRequestNuoviUtentiAmministratori> nuoviUtenti = new ArrayList<>();
    CreaEnteRequestNuoviUtentiAmministratori nuovoUtente = new CreaEnteRequestNuoviUtentiAmministratori();
    nuoviUtenti.add(nuovoUtente);
    request.setNuoviUtentiAmministratori(nuoviUtenti);
    entiService.updateEnte(2L, request);
  }
}
