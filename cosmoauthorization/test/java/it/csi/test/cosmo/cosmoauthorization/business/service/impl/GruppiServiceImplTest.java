/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.CosmoTUtenteGruppo;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.GruppiService;
import it.csi.cosmo.cosmoauthorization.dto.rest.AggiornaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.AssociazioneEnteUtente;
import it.csi.cosmo.cosmoauthorization.dto.rest.CreaGruppoRequest;
import it.csi.cosmo.cosmoauthorization.dto.rest.Ente;
import it.csi.cosmo.cosmoauthorization.dto.rest.GruppiResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Gruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.RiferimentoGruppo;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteGruppoRepository;
import it.csi.cosmo.cosmoauthorization.integration.repository.CosmoTUtenteRepository;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class GruppiServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private GruppiService gruppiService;
  
  private static final String DEMO2021 = "DEMO2021";
  private static final String GRUPPO1 = "gruppo1";
  
  @Autowired
  private CosmoTUtenteGruppoRepository cosmoTUtenteGruppoRepository;
  
  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;
  
  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteEnteAutenticato("AAAAAA00B77B000F"));
  }

  @Test
  public void getGruppo() {
    Gruppo gruppo = gruppiService.getGruppo(1L);

    assertNotNull("gruppo non nullo", gruppo);
  }

  @Test
  public void getGruppi() {
    String filter =
        "{\"filter\":{\"codice\":{\"in\": [\"2\", \"FL-ID\"]} },\"sort\":\"+id\",\"fields\":\"id,codice,nome\", \"page\": 0,\"size\":5}";
    GruppiResponse gruppi = gruppiService.getGruppi(filter);
    assertNotNull("utente non nullo", gruppi.getGruppi());
  }
  
  @Test
  public void updateGruppo() {

    Gruppo gruppoSulDB = gruppiService.getGruppo(1L);

    Long idUtente = 1L;

    AggiornaGruppoRequest gruppoDaAggiornare = new AggiornaGruppoRequest();
    gruppoDaAggiornare.setCodice("DEMO2023");
    gruppoDaAggiornare.setNome("TTTT");
    gruppoDaAggiornare.setIdUtenti(Arrays.asList(idUtente));
    gruppoDaAggiornare.setIdTags(Arrays.asList(1L));

    Gruppo gruppoAggiornato = gruppiService.updateGruppo(gruppoSulDB.getId(), gruppoDaAggiornare);

    assertNotNull("gruppo non nullo", gruppoAggiornato);
  }

  @Test(expected = NotFoundException.class)
  public void getGruppoNotFound() {
    gruppiService.getGruppo(111L);
  }
  
  @Test(expected = NotFoundException.class)
  public void getGruppoPerCodiceNotFound() {
    gruppiService.getGruppoPerCodice("prova");
  }
  
  @Test
  public void getGruppoPerCodice() {
    Gruppo gruppo = gruppiService.getGruppoPerCodice(DEMO2021);
    assertNotNull(gruppo);
    assertNotNull(gruppo.getCodice());
    assertEquals(gruppo.getCodice(), DEMO2021);
  }
  
  @Test
  public void getGruppiUtenteCorrente() {
    List<RiferimentoGruppo> gruppi = gruppiService.getGruppiUtenteCorrente();
    assertNotNull(gruppi);
    assertNotNull(gruppi.get(0));
    assertTrue(gruppi.size() == 1);
    assertTrue(gruppi.get(0).getCodice().equals(DEMO2021));
  }
  
  @Test(expected = ConflictException.class)
  public void createGruppo() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(DEMO2021);
    request.setNome(GRUPPO1);
    gruppiService.createGruppo(request);
  }
  
  @Test
  public void createGruppoConAssociazioniNull() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    request.setIdUtenti(null);
    request.setCodiciTipologiePratiche(null);
    Gruppo gruppo = gruppiService.createGruppo(request);
    assertNotNull(gruppo);
    assertNotNull(gruppo.getCodice());
    assertTrue(gruppo.getCodice().equals(GRUPPO1));
  }
  
  @Test
  public void createGruppoConAssociazioniEmpty() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<Long> utenti = new ArrayList<>();
    request.setIdUtenti(utenti);
    
    List<String> codiciTipologiePratiche = new ArrayList<>();
    request.setCodiciTipologiePratiche(codiciTipologiePratiche);
    
    gruppiService.createGruppo(request);
  }
  
  @Test(expected = NotFoundException.class)
  public void createGruppoConAssociazioniUtenteNotFound() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<Long> utenti = new ArrayList<>();
    utenti.add(111L);
    request.setIdUtenti(utenti);
    
    List<String> codiciTipologiePratiche = new ArrayList<>();
    request.setCodiciTipologiePratiche(codiciTipologiePratiche );
    
    gruppiService.createGruppo(request);
  }
  
  @Test(expected = NotFoundException.class)
  public void createGruppoConTipoPraticaNotFound() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<Long> utenti = new ArrayList<>();
    utenti.add(1L);
    request.setIdUtenti(utenti);
    
    List<String> codiciTipologiePratiche = new ArrayList<>();
    codiciTipologiePratiche.add(GRUPPO1);
    request.setCodiciTipologiePratiche(codiciTipologiePratiche);
    
    gruppiService.createGruppo(request);
  }
  
  @Test
  public void createGruppoConAssociazioni() {
    CreaGruppoRequest request = new CreaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<Long> utenti = new ArrayList<>();
    utenti.add(1L);
    request.setIdUtenti(utenti);
    
    List<String> codiciTipologiePratiche = new ArrayList<>();
    codiciTipologiePratiche.add("TP1");
    request.setCodiciTipologiePratiche(codiciTipologiePratiche);
    
    Gruppo gruppo = gruppiService.createGruppo(request);
    assertNotNull(gruppo);
    assertNotNull(gruppo.getUtenti());
    assertNotNull(gruppo.getUtenti().get(0));
    assertTrue(gruppo.getUtenti().get(0).getId() == 1);
    assertNotNull(gruppo.getTipologiePratiche());
    assertNotNull(gruppo.getTipologiePratiche().get(0));
    assertTrue(gruppo.getTipologiePratiche().get(0).getCodice().equals("TP1"));
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteGruppoNotFound() {
    gruppiService.deleteGruppo(111L);
  }
  
  @Test
  public void deleteGruppo() {
    gruppiService.deleteGruppo(1L);
  }
  
  @Test(expected = ConflictException.class)
  public void updateGruppoConCodiceEsistente() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(DEMO2021);
    request.setNome(GRUPPO1);
    gruppiService.updateGruppo(2L, request);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateGruppoNotFound() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    gruppiService.updateGruppo(111L, request);
  }
  
  @Test
  public void updateGruppoConAssociazioniDaEliminare() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<Long> utenti = new ArrayList<>();
    utenti.add(3L);
    request.setIdUtenti(utenti);
    
    gruppiService.updateGruppo(1L, request);
  }
  
  @Test
  public void updateGruppoConTipologiePratiche() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    
    List<String> codiciTipologiaPratiche = new ArrayList<>();
    codiciTipologiaPratiche.add("TP2");
    request.setCodiciTipologiePratiche(codiciTipologiaPratiche );
    
    gruppiService.updateGruppo(1L, request);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateGruppoConTagUtenteNotFound() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(GRUPPO1);
    request.setNome(GRUPPO1);
    request.setUtenteTag(111L);
    
    gruppiService.updateGruppo(1L, request);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateGruppoConAssociazioneNotFound() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(DEMO2021);
    request.setNome("Demo 20 e 21");
    request.setDescrizione("Gruppo con solo utenti demo 20 e demo 21");
    request.setUtenteTag(1L);
    List<Long> tags = new ArrayList<>();
    tags.add(111L);
    request.setIdTags(tags);
    
    gruppiService.updateGruppo(1L, request);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateGruppoConTagNotFound() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(DEMO2021);
    request.setNome("Demo 20 e 21");
    request.setDescrizione("Gruppo con solo utenti demo 20 e demo 21");
    request.setUtenteTag(1L);
    List<Long> utenti = new ArrayList<>();
    utenti.add(1L);
    request.setIdUtenti(utenti);
    List<Long> tags = new ArrayList<>();
    tags.add(111L);
    request.setIdTags(tags);
    
    gruppiService.updateGruppo(1L, request);
  }
  
  @Test
  public void updateGruppoConTag() {
    AggiornaGruppoRequest request = new AggiornaGruppoRequest();
    request.setCodice(DEMO2021);
    request.setNome("Demo 20 e 21");
    request.setDescrizione("Gruppo con solo utenti demo 20 e demo 21");
    request.setUtenteTag(1L);
    List<Long> utenti = new ArrayList<>();
    utenti.add(1L);
    request.setIdUtenti(utenti);
    List<Long> tags = new ArrayList<>();
    tags.add(2L);
    request.setIdTags(tags);
    
    Gruppo gruppo = gruppiService.updateGruppo(1L, request);
    assertNotNull(gruppo);
    assertNotNull(gruppo.getCodice());
    assertTrue(gruppo.getCodice().equals(DEMO2021));
  }
}
