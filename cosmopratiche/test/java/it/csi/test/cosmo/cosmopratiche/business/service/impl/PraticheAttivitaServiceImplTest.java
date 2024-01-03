/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
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
import it.csi.cosmo.cosmopratiche.business.service.PraticaAttivitaService;
import it.csi.cosmo.cosmopratiche.dto.rest.Assegnazione;
import it.csi.cosmo.cosmopratiche.dto.rest.Attivita;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.StatoPratica;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { CosmoPraticheUnitTestInMemory.class} )
@Transactional
public class PraticheAttivitaServiceImplTest extends ParentIntegrationTest {
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }
  
  @Autowired
  PraticaAttivitaService praticaAttivitaService;
  
  @Test(expected = BadRequestException.class)
  public void salvaPraticaWithId() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    praticaAttivitaService.salva(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void salvaPraticaTipoNull() {
    Pratica pratica = new Pratica();
    pratica.setTipo(null);
    praticaAttivitaService.salva(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void salvaPraticaWithTipoPraticaCodiceBlank() {
    Pratica pratica = new Pratica();
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("");
    pratica.setTipo(tipo );
    praticaAttivitaService.salva(pratica);
  }
  
  @Test(expected = NotFoundException.class)
  public void salvaPraticaWithTipoPraticaNotFound() {
    Pratica pratica = new Pratica();
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP12");
    pratica.setTipo(tipo);
    praticaAttivitaService.salva(pratica);
  }
  
  @Test
  public void salvaPratica() {
    Pratica pratica = new Pratica();
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setCodiceIpaEnte("r_piemon");
    Pratica result = praticaAttivitaService.salva(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test(expected = BadRequestException.class)
  public void aggiornaPraticaWithIdNull() {
    Pratica pratica = new Pratica();
    pratica.setId(null);
    praticaAttivitaService.aggiorna(pratica);
  }
  
  @Test(expected = NotFoundException.class)
  public void aggiornaPraticaNotFound() {
    Pratica pratica = new Pratica();
    pratica.setId(123);
    praticaAttivitaService.aggiorna(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void aggiornaPraticaWithTipoNull() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    pratica.setTipo(null);
    praticaAttivitaService.aggiorna(pratica);
  }
  
  @Test(expected = BadRequestException.class)
  public void aggiornaPraticaWithTipoCodiceBlank() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("");
    pratica.setTipo(tipo);
    praticaAttivitaService.aggiorna(pratica);
  }
  
  @Test
  public void aggiornaPraticaWithValuesNull() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test
  public void aggiornaPraticaWithValues() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setDataCambioStato(OffsetDateTime.now());
    StatoPratica stato = new StatoPratica();
    stato.setCodice("PROVA");
    pratica.setStato(stato);
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    attivita.setIdPratica(2);
    attivita.setEvento("TASK_COMPLETATO");
    listaAttivita.add(attivita );
    pratica.setAttivita(listaAttivita);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test
  public void aggiornaPraticaNotFoundWithTaskAggiornato() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setDataCambioStato(OffsetDateTime.now());
    StatoPratica stato = new StatoPratica();
    stato.setCodice("PROVA");
    pratica.setStato(stato);
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    attivita.setIdPratica(2);
    attivita.setEvento("TASK_AGGIORNATO");
    listaAttivita.add(attivita );
    List<Assegnazione> assegnazioni = new ArrayList<>();
    Assegnazione assegnazione = new Assegnazione();
    assegnazioni.add(assegnazione);
    attivita.setAssegnazione(assegnazioni);
    pratica.setAttivita(listaAttivita);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test
  public void aggiornaPraticaTaskCompletato() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setDataCambioStato(OffsetDateTime.now());
    StatoPratica stato = new StatoPratica();
    stato.setCodice("PROVA");
    pratica.setStato(stato);
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    attivita.setIdPratica(1);
    attivita.setEvento("TASK_COMPLETATO");
    attivita.setLinkAttivita("tasks/877504");
    listaAttivita.add(attivita );
    pratica.setAttivita(listaAttivita);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test
  public void aggiornaPraticaTaskAggiornatoSenzaAssegnazioni() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setDataCambioStato(OffsetDateTime.now());
    StatoPratica stato = new StatoPratica();
    stato.setCodice("PROVA");
    pratica.setStato(stato);
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    attivita.setIdPratica(1);
    attivita.setEvento("TASK_AGGIORNATO");
    attivita.setLinkAttivita("tasks/877504");
    listaAttivita.add(attivita );
    pratica.setAttivita(listaAttivita);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  @Test
  public void aggiornaPraticaTaskAggiornatoConAssegnazioni() {
    Pratica pratica = new Pratica();
    pratica.setId(1);
    TipoPratica tipo = new TipoPratica();
    tipo.setCodice("TP1");
    pratica.setTipo(tipo);
    pratica.setDataCambioStato(OffsetDateTime.now());
    StatoPratica stato = new StatoPratica();
    stato.setCodice("PROVA");
    pratica.setStato(stato);
    List<Attivita> listaAttivita = new ArrayList<>();
    Attivita attivita = new Attivita();
    attivita.setIdPratica(1);
    attivita.setEvento("TASK_AGGIORNATO");
    attivita.setLinkAttivita("tasks/295013");
    List<Assegnazione> assegnazioni = new ArrayList<>();
    Assegnazione assegnazione = new Assegnazione();
    assegnazione.setIdGruppo("DEMO2021");
    assegnazione.setIdUtente("AAAAAA00B77B000F");
    assegnazione.setAssegnatario(Boolean.TRUE);
    assegnazioni.add(assegnazione);
    attivita.setAssegnazione(assegnazioni);
    listaAttivita.add(attivita );
    pratica.setAttivita(listaAttivita);
    Pratica result = praticaAttivitaService.aggiorna(pratica);
    assertNotNull(result);
    assertNotNull(result.getTipo());
    assertNotNull(result.getTipo().getCodice());
    assertEquals(result.getTipo().getCodice(), "TP1");
  }
  
  
  @Test(expected = NotFoundException.class)
  public void putAttivitaIdAttivitaNull() {
    praticaAttivitaService.putAttivitaIdAttivita(null);
  }
  
  @Test
  public void putAttivitaIdAttivitaWithAssegnazione() {
    Boolean result = praticaAttivitaService.putAttivitaIdAttivita("877504");
    assertFalse(result);
  }
  
  @Test
  public void putAttivitaIdAttivitaWithAssegnazioneEmpty() {
    Boolean result = praticaAttivitaService.putAttivitaIdAttivita("877507");
    assertTrue(result);
  }
  
  @Test
  public void getPraticheAttivitaIdAttivita() {
    Attivita attivita = praticaAttivitaService.getPraticheAttivitaIdAttivita("877504");
    assertNotNull(attivita);
    assertNotNull(attivita.getId());
    assertTrue(attivita.getId() == 1);
  }
  
  @Test
  public void getPraticheAttivitaIdAttivitaNotFound() {
    Attivita attivita = praticaAttivitaService.getPraticheAttivitaIdAttivita("111111");
    assertNull(attivita);
  }
  
  @Test(expected = BadRequestException.class)
  public void getAttivitaIdPraticaNull () {
    praticaAttivitaService.getAttivitaIdPratica(null);
  }
  
  @Test
  public void getAttivitaIdPratica () {
    List<Attivita> listaAttivita = praticaAttivitaService.getAttivitaIdPratica("1");
    assertNotNull(listaAttivita);
    assertTrue(listaAttivita.size() == 5);
  }
}
