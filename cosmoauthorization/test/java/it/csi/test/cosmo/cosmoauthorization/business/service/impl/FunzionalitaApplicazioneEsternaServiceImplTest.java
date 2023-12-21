/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoauthorization.business.service.impl;

import static org.junit.Assert.assertNotNull;
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
import it.csi.cosmo.cosmoauthorization.business.service.FunzionalitaApplicazioneEsternaService;
import it.csi.cosmo.cosmoauthorization.dto.rest.CampiTecnici;
import it.csi.cosmo.cosmoauthorization.dto.rest.FunzionalitaApplicazioneEsternaConValidita;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.CosmoAuthorizationUnitTestInMemory;
import it.csi.test.cosmo.cosmoauthorization.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoauthorization.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoAuthorizationUnitTestInMemory.class})
@Transactional
public class FunzionalitaApplicazioneEsternaServiceImplTest extends ParentIntegrationTest {

  private static final String FUNZIONALITA_NOT_NULL = "La funzionalita' deve esistere";

  private static final String CAMPI_TECNICI_NOT_NULL = "Devono esserci i campi tecnici";

  private static final String FUNZIONALITA_NON_PRINCIPALE =
      "La funzionalita' non deve essere principale";

  private static final String DESCRIZIONE_FUNZIONALITA = "Descrizione funzionalita'";
  private static final String URL = "www.google.com";

  @Autowired
  private FunzionalitaApplicazioneEsternaService funzionalitaApplicazioneEsternaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildEnteUtenteAutenticato());
  }

  @Test
  public void eliminaFunzionalita() {
    funzionalitaApplicazioneEsternaService.eliminaFunzionalita("1", "1");
  }

  @Test(expected = NotFoundException.class)
  public void eliminaFunzionalitaNonEsistente() {
    funzionalitaApplicazioneEsternaService.eliminaFunzionalita("1", "10");
  }

  @Test(expected = BadRequestException.class)
  public void eliminaFunzionalitaNonCorrispondenteAllApplicazione() {
    funzionalitaApplicazioneEsternaService.eliminaFunzionalita("2", "4");
  }

  @Test
  public void getFunzionalita() {
    List<FunzionalitaApplicazioneEsternaConValidita> funzionalita =
        funzionalitaApplicazioneEsternaService.getFunzionalita("1");

    assertNotNull(FUNZIONALITA_NOT_NULL, funzionalita);
    assertTrue("Ci sono due funzionalita non principali", funzionalita.size() == 2);
    assertTrue(FUNZIONALITA_NON_PRINCIPALE, !funzionalita.get(0).isPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, funzionalita.get(0).getCampiTecnici());
    assertTrue(FUNZIONALITA_NON_PRINCIPALE, !funzionalita.get(1).isPrincipale());
    assertNotNull(CAMPI_TECNICI_NOT_NULL, funzionalita.get(1).getCampiTecnici());
  }

  @Test
  public void getSingolaFunzionalita() {
    FunzionalitaApplicazioneEsternaConValidita singolaFunzionalita =
        funzionalitaApplicazioneEsternaService.getSingolaFunzionalita("1", "2");

    assertNotNull(FUNZIONALITA_NOT_NULL, singolaFunzionalita);
    assertNotNull(CAMPI_TECNICI_NOT_NULL, singolaFunzionalita.getCampiTecnici());
  }

  @Test(expected = BadRequestException.class)
  public void getSingolaFunzionalitaNonCorrispondenteAllApplicazione() {
    funzionalitaApplicazioneEsternaService.getSingolaFunzionalita("2", "4");
  }

  @Test
  public void salvaSingolaFunzionalita() {
    FunzionalitaApplicazioneEsternaConValidita funzionalitaDaSalvare =
        new FunzionalitaApplicazioneEsternaConValidita();
    funzionalitaDaSalvare.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalitaDaSalvare.setUrl(URL);
    funzionalitaDaSalvare.setPrincipale(Boolean.FALSE);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    funzionalitaDaSalvare.setCampiTecnici(campiTecnici);

    FunzionalitaApplicazioneEsternaConValidita funzionalitaSalvata =
        funzionalitaApplicazioneEsternaService.salvaSingolaFunzionalita("1", funzionalitaDaSalvare);

    assertNotNull(FUNZIONALITA_NOT_NULL, funzionalitaSalvata);
    assertNotNull(CAMPI_TECNICI_NOT_NULL, funzionalitaSalvata.getCampiTecnici());
    assertNotNull("L'id della funzionalita e' stato associato", funzionalitaSalvata.getId());

  }

  @Test(expected = BadRequestException.class)
  public void salvaFunzionalitaSuAppNonEsistente() {
    FunzionalitaApplicazioneEsternaConValidita funzionalitaDaSalvare =
        new FunzionalitaApplicazioneEsternaConValidita();
    funzionalitaDaSalvare.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalitaDaSalvare.setUrl(URL);
    funzionalitaDaSalvare.setPrincipale(Boolean.FALSE);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    funzionalitaDaSalvare.setCampiTecnici(campiTecnici);

    funzionalitaApplicazioneEsternaService.salvaSingolaFunzionalita("10", funzionalitaDaSalvare);
  }

  @Test
  public void aggiornaSingolaFunzionalita() {
    FunzionalitaApplicazioneEsternaConValidita funzionalitaDaAggiornare =
        new FunzionalitaApplicazioneEsternaConValidita();
    funzionalitaDaAggiornare.setId(2l);
    funzionalitaDaAggiornare.setDescrizione("Nuova descrizione");
    funzionalitaDaAggiornare.setUrl("Nuovo url");
    funzionalitaDaAggiornare.setPrincipale(Boolean.FALSE);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    funzionalitaDaAggiornare.setCampiTecnici(campiTecnici);

    FunzionalitaApplicazioneEsternaConValidita funzionalitaAggiornata =
        funzionalitaApplicazioneEsternaService.aggiornaSingolaFunzionalita("1", "2",
            funzionalitaDaAggiornare);

    assertNotNull(FUNZIONALITA_NOT_NULL, funzionalitaAggiornata);
    assertNotNull(CAMPI_TECNICI_NOT_NULL, funzionalitaAggiornata.getCampiTecnici());
    assertTrue("La descrizione deve essere diversa",
        !"Funzionalita test 2 di app 1 per ente 1".equals(funzionalitaAggiornata.getDescrizione()));
    assertTrue("L'url deve essere diversa", !URL.equals(funzionalitaAggiornata.getUrl()));
    assertTrue("La data di fine validita deve essere valorizzata",
        null != funzionalitaAggiornata.getCampiTecnici().getDtFineVal()
            && campiTecnici.getDtFineVal().truncatedTo(ChronoUnit.MILLIS)
                .equals(funzionalitaAggiornata.getCampiTecnici().getDtFineVal()));

  }

  @Test(expected = BadRequestException.class)
  public void aggiornaFunzionalitaSuAppNonEsistente() {
    FunzionalitaApplicazioneEsternaConValidita funzionalitaDaAggiornare =
        new FunzionalitaApplicazioneEsternaConValidita();
    funzionalitaDaAggiornare.setId(2l);
    funzionalitaDaAggiornare.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalitaDaAggiornare.setUrl(URL);
    funzionalitaDaAggiornare.setPrincipale(Boolean.FALSE);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    funzionalitaDaAggiornare.setCampiTecnici(campiTecnici);

    funzionalitaApplicazioneEsternaService.aggiornaSingolaFunzionalita("10", "2",
        funzionalitaDaAggiornare);
  }

  @Test(expected = BadRequestException.class)
  public void aggiornaFunzionalitaPrincipale() {
    FunzionalitaApplicazioneEsternaConValidita funzionalitaDaAggiornare =
        new FunzionalitaApplicazioneEsternaConValidita();
    funzionalitaDaAggiornare.setId(2l);
    funzionalitaDaAggiornare.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    funzionalitaDaAggiornare.setUrl(URL);
    funzionalitaDaAggiornare.setPrincipale(Boolean.TRUE);

    CampiTecnici campiTecnici = new CampiTecnici();
    campiTecnici.setDtIniVal(OffsetDateTime.now());
    campiTecnici.setDtFineVal(OffsetDateTime.now().plusHours(6));

    funzionalitaDaAggiornare.setCampiTecnici(campiTecnici);

    funzionalitaApplicazioneEsternaService.aggiornaSingolaFunzionalita("1", "2",
        funzionalitaDaAggiornare);
  }

  @Test(expected = NotFoundException.class)
  public void getSingolaFunzionalitaNotFound() {
    funzionalitaApplicazioneEsternaService.getSingolaFunzionalita("111", "111");
  }

  @Test(expected = BadRequestException.class)
  public void salvaSingolaFunzionalitaNotPrincipale() {
    FunzionalitaApplicazioneEsternaConValidita body =
        new FunzionalitaApplicazioneEsternaConValidita();
    body.setPrincipale(Boolean.TRUE);
    body.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    body.setUrl(URL);
    CampiTecnici campiTecnici = new CampiTecnici();
    body.setCampiTecnici(campiTecnici);
    funzionalitaApplicazioneEsternaService.salvaSingolaFunzionalita("1", body);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornaSingolaFunzionalitaNotFound() {
    FunzionalitaApplicazioneEsternaConValidita body = new FunzionalitaApplicazioneEsternaConValidita();
    body.setPrincipale(Boolean.FALSE);
    body.setDescrizione(DESCRIZIONE_FUNZIONALITA);
    body.setUrl(URL);
    CampiTecnici campiTecnici = new CampiTecnici();
    body.setCampiTecnici(campiTecnici);
    funzionalitaApplicazioneEsternaService.aggiornaSingolaFunzionalita("1", "111", body );
  }

}
