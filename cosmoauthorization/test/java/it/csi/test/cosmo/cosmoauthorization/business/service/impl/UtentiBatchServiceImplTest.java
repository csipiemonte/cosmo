/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte;
import it.csi.cosmo.common.entities.CosmoCConfigurazioneEnte_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTProfilo;
import it.csi.cosmo.common.entities.CosmoTProfilo_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtente_;
import it.csi.cosmo.cosmoauthorization.business.service.UtentiBatchService;
import it.csi.cosmo.cosmoauthorization.dto.exception.UtentiBatchException;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoCConfigurazioneEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTEnteRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTProfiloRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class UtentiBatchServiceImplTest {

  @Autowired
  private UtentiBatchService utentiBatchService;
  
  @Autowired
  private CosmoTEnteRepository cosmoTEnteRepository;
  
  @Autowired
  private CosmoCConfigurazioneEnteRepository cosmoCConfigurazioneEnteRepository;
  
  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;
  
  @Autowired
  private CosmoTProfiloRepository cosmoTProfiloRepository;
  
  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;
  
  private static final String CF = "AAAAAA00B77B000F";
  private static final String ADMIN = "ADMIN";
  private static final String COGNOME = "cognome";
  private static final String VALORE1 = "valore 1";

  @Test
  public void findEnteByCodiceFiscale() {
    Optional<CosmoTEnte> ente =
        utentiBatchService.findEnteByCodiceFiscaleOrCodiceIpa("80087670016");
    assertNotNull(ente);
    if (!ente.isEmpty()) {
      assertNotNull(ente.get());
      assertNotNull(ente.get().getCodiceFiscale());
      assertTrue(ente.get().getCodiceFiscale().equals("80087670016"));
    }
  }

  @Test
  public void findEnteByCodiceIpa() {
    Optional<CosmoTEnte> ente = utentiBatchService.findEnteByCodiceFiscaleOrCodiceIpa("r_piemon");
    assertNotNull(ente);
    if (!ente.isEmpty()) {
      assertNotNull(ente.get());
      assertNotNull(ente.get().getCodiceIpa());
      assertTrue(ente.get().getCodiceIpa().equals("r_piemon"));
    }
  }

  @Test
  public void findConfigurazioneEnteByIdEnteAndChiave() {
    Optional<CosmoCConfigurazioneEnte> conf =
        utentiBatchService.findConfigurazioneEnteByIdEnteAndChiave(1L, "chiave 1");
    assertNotNull(conf);
    if (!conf.isEmpty()) {
      assertNotNull(conf.get());
      assertNotNull(conf.get().getValore());
      assertTrue(conf.get().getValore().equals(VALORE1));
    }
  }

  @Test
  public void findUtenteByFieldEqualsIgnoreCase() {
    Optional<CosmoTUtente> utente = utentiBatchService
        .findUtenteByFieldEqualsIgnoreCase(CosmoTUtente_.codiceFiscale, CF, 2L);
    assertNotNull(utente);
    if (!utente.isEmpty()) {
      assertNotNull(utente.get());
      assertNotNull(utente.get().getCodiceFiscale());
      assertTrue(utente.get().getCodiceFiscale().equals(CF));
    }
  }
  
  @Test
  public void findUtenteByFieldEqualsIgnoreCaseConExcluedIdNull() {
    Optional<CosmoTUtente> utente = utentiBatchService
        .findUtenteByFieldEqualsIgnoreCase(CosmoTUtente_.codiceFiscale, CF, null);
    assertNotNull(utente);
    if (!utente.isEmpty()) {
      assertNotNull(utente.get());
      assertNotNull(utente.get().getCodiceFiscale());
      assertTrue(utente.get().getCodiceFiscale().equals(CF));
    }
  }
  
  @Test
  public void findProfiloByFieldEqualsIgnoreCase() {
    Optional<CosmoTProfilo> profilo = utentiBatchService.findProfiloByFieldEqualsIgnoreCase(CosmoTProfilo_.codice, ADMIN, 2L);
    assertNotNull(profilo);
    if (!profilo.isEmpty()) {
      assertNotNull(profilo.get());
      assertNotNull(profilo.get().getCodice());
      assertTrue(profilo.get().getCodice().equals(ADMIN));
    }
  }
  
  @Test
  public void findProfiloByFieldEqualsIgnoreCaseConExcluedIdNull() {
    Optional<CosmoTProfilo> profilo = utentiBatchService.findProfiloByFieldEqualsIgnoreCase(CosmoTProfilo_.codice, ADMIN, null);
    assertNotNull(profilo);
    if (!profilo.isEmpty()) {
      assertNotNull(profilo.get());
      assertNotNull(profilo.get().getCodice());
      assertTrue(profilo.get().getCodice().equals(ADMIN));
    }
  }
  
  @Test
  public void findGruppoByCodiceAndEnte() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    Optional<CosmoTGruppo> gruppo = utentiBatchService.findGruppoByCodiceAndEnte("DEMO2021", ente);
    assertNotNull(gruppo);
    if (!gruppo.isEmpty()) {
      assertNotNull(gruppo.get());
      assertNotNull(gruppo.get().getCodice());
      assertTrue(gruppo.get().getCodice().equals("DEMO2021"));
    }
  }
  
  @Test(expected = UtentiBatchException.class)
  public void saveUtenteBatchErroreParsingData() {
    utentiBatchService.saveUtenteBatch(CF, "nome", COGNOME, "tel", "mail", "22-10-2021", "22-10-2021", null, null);
  }
  
  @Test
  public void saveUtenteBatchProfiloNonRegistrato() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, VALORE1);
    if(!conf.isEmpty()) {
    utentiBatchService.saveUtenteBatch(CF, "nome", COGNOME, "tel", "mail", null, "22/10/2024", ente, conf.get());
    }
  }
  
  @Test(expected = UtentiBatchException.class)
  public void saveUtenteBatch() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, "valore 3");
    if(!conf.isEmpty()) {
    utentiBatchService.saveUtenteBatch(CF, "nome", COGNOME, "tel", "mail", "22/11/2021", null, ente, conf.get());
    }
  }
  
  @Test
  public void saveUtenteBatchProfiloConEnteDiverso() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(2L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, VALORE1);
    if(!conf.isEmpty()) {
    utentiBatchService.saveUtenteBatch(CF, "nome", COGNOME, "tel", "mail", null, "22/10/2024", ente, conf.get());
    }
  }
  
  @Test
  public void saveProfiliUtente() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(1L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, VALORE1);
    CosmoTProfilo profilo = cosmoTProfiloRepository.findOne(1L);
    List<CosmoTProfilo> profili = new ArrayList<>();
    profili.add(profilo);
    if(!conf.isEmpty()) {
    utentiBatchService.saveProfiliUtente(utente, ente, conf.get(), profili);
    }
  }
  
  @Test
  public void saveProfiliUtenteConProfiliDiversi() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(1L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, VALORE1);
    CosmoTProfilo profilo = cosmoTProfiloRepository.findOne(2L);
    List<CosmoTProfilo> profili = new ArrayList<>();
    profili.add(profilo);
    if(!conf.isEmpty()) {
    utentiBatchService.saveProfiliUtente(utente, ente, conf.get(), profili);
    }
  }
  
  @Test
  public void saveProfiliUtenteConListaProfiliVuota() {
    CosmoTEnte ente = cosmoTEnteRepository.findOne(1L);
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(1L);
    Optional<CosmoCConfigurazioneEnte> conf = cosmoCConfigurazioneEnteRepository.findOneActiveByField(CosmoCConfigurazioneEnte_.valore, VALORE1);
    List<CosmoTProfilo> profili = new ArrayList<>();
    if(!conf.isEmpty()) {
    utentiBatchService.saveProfiliUtente(utente, ente, conf.get(), profili);
    }
  }
  
  @Test
  public void saveGruppiUtente () {
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(1L);
    CosmoTGruppo gruppo = cosmoTGruppoRepository.findOne(1L);
    List<CosmoTGruppo> gruppi = new ArrayList<>();
    gruppi.add(gruppo);
    utentiBatchService.saveGruppiUtente(utente, gruppi);
  }
  
  @Test
  public void saveGruppiUtenteConGruppoDiverso() {
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(1L);
    CosmoTGruppo gruppo = cosmoTGruppoRepository.findOne(2L);
    List<CosmoTGruppo> gruppi = new ArrayList<>();
    gruppi.add(gruppo);
    utentiBatchService.saveGruppiUtente(utente, gruppi);
  }
}
