/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.test.cosmo.cosmopratiche.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
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
import it.csi.cosmo.cosmopratiche.business.service.RelazionePraticaPraticaService;
import it.csi.cosmo.cosmopratiche.dto.rest.Pratica;
import it.csi.cosmo.cosmopratiche.dto.rest.PraticaInRelazione;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoRelazionePraticaPratica;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class RelazionePraticaPraticaServiceImplTest extends ParentIntegrationTest {

  private static final String DIPENDE_DA = "DIPENDE_DA";
  @Autowired
  private RelazionePraticaPraticaService relazionePraticaPraticaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  @Test
  public void getAllRelations() {
    var tipiRelazioni = relazionePraticaPraticaService.getTipiRelazionePraticaPratica();
    assertNotNull(tipiRelazioni);
    assertTrue("Devono esserci 3 tipi di relazioni", tipiRelazioni.size() == 3);
  }

  @Test
  public void getRelazioniA() {
    Long idPratica = 2L;
    List<PraticaInRelazione> output = relazionePraticaPraticaService.getPraticheInRelazione(idPratica);
    assertNotNull(output);
    assertTrue("Deve esserci una relazione", output.size() == 2);
    assertTrue("La relazione deve essere con la pratica 1", output.get(0).getPratica().getId() == 1);
    assertTrue("La relazione deve essere di tipo 'DIPENDE_DA'",
        DIPENDE_DA.equals(output.get(0).getTipoRelazione().getCodice()));
    assertTrue("La descrizione deve essere 'Dipende da'",
        "Dipende da".equals(output.get(0).getTipoRelazione().getDescrizione()));
  }

  @Test
  public void getRelazioniDA() {
    Long idPratica = 1L;
    List<PraticaInRelazione> output =
        relazionePraticaPraticaService.getPraticheInRelazione(idPratica);
    assertNotNull(output);
    assertTrue("Deve esserci una relazione", output.size() == 3);
    assertTrue("La relazione deve essere con la pratica 2",
        output.get(0).getPratica().getId() == 2);
    assertTrue("La relazione deve essere di tipo 'DIPENDENTE_DA'",
        "DIPENDENTE_DA".equals(output.get(0).getTipoRelazione().getCodice()));
    assertTrue("La descrizione deve essere 'Dipendente da'",
        "Dipendente da".equals(output.get(0).getTipoRelazione().getDescrizione()));
  }

  @Test
  public void creaRelazioneTraPratiche() {

    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(1));
    Pratica risultatoCreazione = relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
    assertNotNull(risultatoCreazione);
  }

  @Test
  public void annullaRelazioneTraPratiche() {

    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(1));

    TipoRelazionePraticaPratica tipoRelazione = new TipoRelazionePraticaPratica();
    tipoRelazione.setCodice(DIPENDE_DA);
    tipoRelazione.setDescrizione("Dipende da");

    Pratica risultatoCreazione = relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        DIPENDE_DA, praticheDaRelazionare);

    assertNotNull(risultatoCreazione);
  }
  
  @Test(expected = BadRequestException.class)
  public void getPraticheInRelazionePraticaNotFound() {
    relazionePraticaPraticaService.getPraticheInRelazione(111L);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniIdPraticaNotNumeric() {
    String idPraticaDa = "prova";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(1));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniListaPraticheVuota() {
    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = new ArrayList<>();
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniPraticaNotFound() {
    String idPraticaDa = "111";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(1));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniTipoRelazionePraticaNotFound() {
    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(1));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICATA", praticheDaRelazionare);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniStessaPratica() {
    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(2));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
  }
  
  @Test(expected = BadRequestException.class)
  public void creaAggiornaRelazioniPraticaDaRelazionareNotFound() {
    String idPraticaDa = "2";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(111));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DUPLICA", praticheDaRelazionare);
  }
  
  @Test
  public void creaAggiornaRelazioniDtFineNotNull() {
    String idPraticaDa = "1";
    List<BigDecimal> praticheDaRelazionare = Arrays.asList(new BigDecimal(5));
    relazionePraticaPraticaService.creaAggiornaRelazioni(idPraticaDa,
        "DIPENDENTE_DA", praticheDaRelazionare);
  }
}
