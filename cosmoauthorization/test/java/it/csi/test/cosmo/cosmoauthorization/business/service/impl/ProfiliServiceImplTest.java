/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoauthorization.business.service.ProfiliService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CategoriaUseCase;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiliResponse;
import it.csi.cosmo.cosmoauthorization.dto.rest.Profilo;
import it.csi.cosmo.cosmoauthorization.dto.rest.UseCase;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class ProfiliServiceImplTest extends ParentIntegrationTest {

  private static final String PROFILO_NON_NULLO = "profilo non nullo";
  @Autowired
  private ProfiliService profiliService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getProfilo() {
    Profilo profilo = profiliService.getProfilo("1");

    assertNotNull(PROFILO_NON_NULLO, profilo);
  }

  @Test
  public void getProfili() {
    ProfiliResponse profili = profiliService.getProfili("");

    assertNotNull(PROFILO_NON_NULLO, profili.getProfili());
  }

  @Test
  public void createProfilo() {
    Profilo profiloDaCreare = new Profilo();
    profiloDaCreare.setCodice("PROFILO");
    profiloDaCreare.setDescrizione("Descrizione nuovo profilo");

    UseCase useCase = new UseCase();
    useCase.setCodice("ADMIN PRIN");

    profiloDaCreare.setUseCases(Arrays.asList(useCase));
    Profilo profiloCreato = profiliService.createProfilo(profiloDaCreare);

    assertNotNull(PROFILO_NON_NULLO, profiloCreato);
  }

  @Test
  public void updateProfilo() {

    Profilo profiloSulDB = profiliService.getProfilo("1");

    Profilo profiloDaAggiornare = new Profilo();
    profiloDaAggiornare.setId(1l);
    profiloDaAggiornare.setCodice("PROF");

    UseCase useCase = new UseCase();

    useCase.setCodice("ADMIN SEC");

    CategoriaUseCase categoriaUseCase = new CategoriaUseCase();
    categoriaUseCase.setCodice("ADMIN");
    categoriaUseCase.setDescrizione("Amministrazione");
    useCase.setCodiceCategoria(categoriaUseCase);

    useCase.setDescrizione("Amministrazione Secondaria");

    profiloDaAggiornare.setUseCases(Arrays.asList(useCase));

    Profilo profiloAggiornato =
        profiliService.updateProfilo(profiloDaAggiornare.getId() + "", profiloDaAggiornare);

    assertNotNull(PROFILO_NON_NULLO, profiloAggiornato);
    assertEquals(profiloSulDB.getCodice(), profiloAggiornato.getCodice());
  }

  @Test
  public void rimuoviTuttiUseCaseDaProfilo() {

    Profilo profilo = profiliService.getProfilo("1");
    profilo.setUseCases(new LinkedList<>());

    Profilo profiloAggiornato = profiliService.updateProfilo(profilo.getId() + "", profilo);

    assertEquals(profiloAggiornato.getUseCases().size(), 0);

  }

  @Test
  public void aggiungiUseCaseDaProfilo() {

    Profilo profilo = profiliService.getProfilo("1");

    List<UseCase> useCases = new LinkedList<>();
    useCases.addAll(profilo.getUseCases());
    UseCase useCase = new UseCase();
    useCase.setCodice("ADMIN SEC");
    useCase.setDescrizione("Amministrazione Secondaria");
    useCases.add(useCase);
    profilo.setUseCases(useCases);

    Profilo profiloAggiornato = profiliService.updateProfilo(profilo.getId() + "", profilo);

    assertEquals(profiloAggiornato.getUseCases().size(), profilo.getUseCases().size());

  }
  
  @Test(expected = NotFoundException.class)
  public void getProfiloNotFound() {
    profiliService.getProfilo("111");
  }
  
  @Test(expected = ConflictException.class)
  public void createProfiloEsistente() {
    Profilo profilo = new Profilo();
    profilo.setCodice("ADMIN");
    profiliService.createProfilo(profilo);
  }
  
  @Test(expected = NotFoundException.class)
  public void createProfiloConUseCaseNotFound() {
    Profilo profilo = new Profilo();
    profilo.setCodice("ADMIN2");
    List<UseCase> useCases = new ArrayList<>();
    UseCase useCase = new UseCase();
    useCase.setCodice(PROFILO_NON_NULLO);
    useCases.add(useCase);
    profilo.setUseCases(useCases);
    profiliService.createProfilo(profilo);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateProfiloNotFound() {
    Profilo profilo = new Profilo();
    profiliService.updateProfilo("111", profilo);
  }
  
  @Test(expected = NotFoundException.class)
  public void updateProfiloConUseCaseNotFound() {
    Profilo profilo = new Profilo();
    List<UseCase> useCases = new ArrayList<>();
    UseCase useCase = new UseCase();
    useCase.setCodice(PROFILO_NON_NULLO);
    useCases.add(useCase);
    profilo.setUseCases(useCases);
    profiliService.updateProfilo("1", profilo);
  }
  
  @Test(expected = NotFoundException.class)
  public void deleteProfiloNotFound() {
    profiliService.deleteProfilo("111");
  }
  
  @Test
  public void deleteProfilo() {
    profiliService.deleteProfilo("1");
  }
}
