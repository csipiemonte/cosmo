/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.CosmoDStatoSigilloElettronico;
import it.csi.cosmo.common.entities.CosmoRSigilloDocumento;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTSigilloElettronico;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService;
import it.csi.cosmo.cosmoecm.dto.EsitoRichiestaSigilloElettronicoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CodiceTipologiaDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.RichiediApposizioneSigilloRequest;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoNotificationsNotificheGlobaliFeignClient;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class,
    it.csi.test.cosmo.cosmoecm.business.service.impl.SigilloElettronicoServiceImplTest.TestConfig.class})
@Transactional
public class SigilloElettronicoServiceImplTest extends ParentIntegrationTest {

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoNotificationsNotificheGlobaliFeignClient cosmoNotificationsNotificheGlobaliFeignClient() {
      return Mockito.mock(CosmoNotificationsNotificheGlobaliFeignClient.class);
    }
  }

  @Autowired
  private SigilloElettronicoService sigilloElettronicoService;

  @Autowired
  private CosmoNotificationsNotificheGlobaliFeignClient mockCosmoNotificationsNotificheGlobaliFeignClient;

  private void setUpMock() {
    reset(mockCosmoNotificationsNotificheGlobaliFeignClient);
    doNothing().when(mockCosmoNotificationsNotificheGlobaliFeignClient).postNotificheGlobali(any());
  }

  @Test
  public void getSigilloElettronicoId() {
    CredenzialiSigilloElettronico result = sigilloElettronicoService.getSigilloElettronicoId(1);
    assertNotNull(result);
  }

  @Test(expected = NotFoundException.class)
  public void getSigilloElettronicoIdInesistente() {
    sigilloElettronicoService.getSigilloElettronicoId(-1);
  }

  @Test
  public void creaSigilloElettronico() {
    CredenzialiSigilloElettronico result =
        sigilloElettronicoService.creaSigilloElettronico(getCommonDto());
    assertNotNull(result);
  }

  @Test(expected = BadRequestException.class)
  public void creaSigilloElettronicoAliasEsistente() {
    var request = getCommonDto();
    request.setAlias("Test3");
    sigilloElettronicoService.creaSigilloElettronico(request);
  }

  @Test
  public void updateSigilloElettronico() {
    CredenzialiSigilloElettronico result =
        sigilloElettronicoService.updateSigilloElettronico(1, getCommonDto());
    assertNotNull(result);
  }

  @Test(expected = NotFoundException.class)
  public void updateSigilloElettronicoIdInesistente() {
    sigilloElettronicoService.updateSigilloElettronico(-1, getCommonDto());
  }

  @Test
  public void deleteSigilloElettronico() {
    sigilloElettronicoService.deleteSigilloElettronico(1);
  }

  @Test(expected = NotFoundException.class)
  public void deleteSigilloElettronicoIdInesistente() {
    sigilloElettronicoService.deleteSigilloElettronico(-1);
  }

  @Test
  public void getSigilloElettronicoByFilters() {
    var filter =
        "{\"page\":0,\"size\":10,\"fields\":\"alias, utente\",\"filter\":{\"alias\":{\"ci\":\"Test\"}}}";
    var result = sigilloElettronicoService.getSigilloElettronico(filter);
    assertTrue(!result.getSigilliElettronici().isEmpty() && result.getSigilliElettronici().size() == 3);
  }

  @Test
  public void getSigilloElettronicoByFiltersWithoutFields() {
    var filter =
        "{\"page\":0,\"size\":10,\"filter\":{\"alias\":{\"ci\":\"Test\"}}}";
    var result = sigilloElettronicoService.getSigilloElettronico(filter);
    assertTrue(!result.getSigilliElettronici().isEmpty() && result.getSigilliElettronici().size() == 3);
  }

  @Test
  public void richiediApposizioneSigilloElettronico() {
    sigilloElettronicoService.richiediApposizioneSigillo("2", getCommonRichiediApposizioneSigilloRequest());
  }

  @Test
  public void richiediApposizioneSigilloElettronicoPraticaSenzaDocumenti() {
    sigilloElettronicoService.richiediApposizioneSigillo("5", getCommonRichiediApposizioneSigilloRequest());
  }

  @Test
  public void salvataggioApposizioneSigilloElettronicoContenutoDocumentoSenzaUUIDNodo() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(5L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(
        getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, 2L);
  }

  @Test
  public void salvataggioApposizioneSigilloElettronicoContenutoDocumentoSenzaFormatoFile() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(6L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(
        getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, 2L);
  }

  @Test
  public void salvataggioApposizioneSigilloElettronicoContenuto() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(8L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(
        getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, 2L);
  }

  @Test(expected = BadRequestException.class)
  public void richiediApposizioneSigilloElettronicoIdPraticaNonValorizzato() {
    sigilloElettronicoService.richiediApposizioneSigillo(null, getCommonRichiediApposizioneSigilloRequest());
  }

  @Test(expected = NumberFormatException.class)
  public void richiediApposizioneSigilloElettronicoIdPraticaErrato() {
    sigilloElettronicoService.richiediApposizioneSigillo("fake", getCommonRichiediApposizioneSigilloRequest());
  }

  @Test(expected = BadRequestException.class)
  public void richiediApposizioneSigilloElettronicoBodyVuoto() {
    sigilloElettronicoService.richiediApposizioneSigillo("1", null);
  }

  @Test(expected = BadRequestException.class)
  public void richiediApposizioneSigilloElettronicoIdentificativoMessaggioVuoto() {
    var request = getCommonRichiediApposizioneSigilloRequest();
    request.setIdentificativoMessaggio(null);
    sigilloElettronicoService.richiediApposizioneSigillo("1", request);
  }

  @Test(expected = BadRequestException.class)
  public void richiediApposizioneSigilloElettronicoIdPraticaNonEsistente() {
    sigilloElettronicoService.richiediApposizioneSigillo("100",
        getCommonRichiediApposizioneSigilloRequest());
  }

  @Test
  public void recuperaDocumentiDaSigillare() {
    var documenti = sigilloElettronicoService.recuperaDocumentiDaSigillare();
    assertTrue(documenti.isEmpty());
  }

  @Test(expected = InternalServerException.class)
  public void salvataggioRichiestaApposizioneSigilloParametriNulli() {
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(null, null, null);
  }

  @Test(expected = InternalServerException.class)
  public void salvataggioRichiestaApposizioneSigilloDocumentoNullo() {
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(getCommonEsitoRichiestaSigilloElettronicoDocumento(), null, null);
  }

  @Test(expected = InternalServerException.class)
  public void salvataggioRichiestaApposizioneSigilloIdSigilloNullo() {
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, null);
  }

  @Test(expected = NotFoundException.class)
  public void salvataggioRichiestaApposizioneSigilloDocumentoNonAncoraSigillato() {
    var documento = new CosmoTDocumento();
    documento.setId(2L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, 1L);
  }

  @Test
  public void salvataggioRichiestaApposizioneSigilloOK() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(
        getCommonEsitoRichiestaSigilloElettronicoDocumento(), documento, 1L);
  }

  @Test
  public void salvataggioRichiestaApposizioneSigilloKO() {
    setUpMock();
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    var request = getCommonEsitoRichiestaSigilloElettronicoDocumento();
    request.setCodice("001");
    sigilloElettronicoService.salvaEsitoRichiestaApposizioneSigillo(
        request, documento, 1L);
  }

  @Test
  public void getIdEnteDaDocumento() {
    var documento = new CosmoTDocumento();
    CosmoTPratica pratica = new CosmoTPratica();
    CosmoTEnte ente = new CosmoTEnte();
    ente.setId(1L);
    pratica.setEnte(ente);
    documento.setPratica(pratica);
    documento.setId(1L);
    var ris = sigilloElettronicoService.recuperaIdEnteDaDocumento(documento);
    assertTrue(null != ris);
  }

  @Test
  public void getIdDaDocumento() {
    var documento = new CosmoTDocumento();
    CosmoTPratica pratica = new CosmoTPratica();
    pratica.setId(1L);
    documento.setPratica(pratica);
    documento.setId(1L);
    var ris = sigilloElettronicoService.recuperaIdPraticaDaDocumento(documento);
    assertTrue(null != ris);
  }

  @Test
  public void aggiornaStatoSigilloInWorkInProgress() {
    var relazioneSigilloDocumento = new CosmoRSigilloDocumento();
    var statoSigilloElettronico = new CosmoDStatoSigilloElettronico();
    var documento = new CosmoTDocumento();
    var sigilloElettronico = new CosmoTSigilloElettronico();
    relazioneSigilloDocumento.setCosmoDStatoSigilloElettronico(statoSigilloElettronico);
    relazioneSigilloDocumento.setCosmoTDocumento(documento);
    relazioneSigilloDocumento.setCosmoTSigilloElettronico(sigilloElettronico);
    relazioneSigilloDocumento.setId(1L);
    sigilloElettronicoService.aggiornaStatoSigilloWip(relazioneSigilloDocumento);
  }

  @Test
  public void aggiornaStatoSigilloInErrore() {
    sigilloElettronicoService.aggiornaSigilliInErrore(1L, "SIGILLATO", "Test");
  }

  @Test
  public void checkApposizioneSigilloDocumenti() {
    var documenti = new ArrayList<CosmoTDocumento>();
    var documento = new CosmoTDocumento();
    documento.setId(1L);
    documenti.add(documento);
    sigilloElettronicoService.checkApposizioneSigilloDocumenti(documenti, 1L);
  }

  private CreaCredenzialiSigilloElettronicoRequest getCommonDto() {
    CreaCredenzialiSigilloElettronicoRequest request =
        new CreaCredenzialiSigilloElettronicoRequest();
    request.setAlias("Test4");
    request.setDelegatedDomain("prova4");
    request.setDelegatedPassword("Dct9dFFgAA7D");
    request.setDelegatedUser("prova3");
    request.setOtpPwd("dsign");
    request.setTipoHsm("prova");
    request.setTipoOtpAuth("faCSI2");
    request.setUtente("prova");
    return request;
  }

  private RichiediApposizioneSigilloRequest getCommonRichiediApposizioneSigilloRequest() {
    CodiceTipologiaDocumento ctd = new CodiceTipologiaDocumento();
    ctd.setCodice("codice 1");
    List<CodiceTipologiaDocumento> listTipologiaDoc = new ArrayList<>();
    listTipologiaDoc.add(ctd);
    RichiediApposizioneSigilloRequest rasr = new RichiediApposizioneSigilloRequest();
    rasr.setIdentificativoAlias("Test1");
    rasr.setIdentificativoMessaggio("SIGILLO");
    rasr.setTipiDocumento(listTipologiaDoc);
    return rasr;
  }

  private EsitoRichiestaSigilloElettronicoDocumento getCommonEsitoRichiestaSigilloElettronicoDocumento() {
    EsitoRichiestaSigilloElettronicoDocumento esito =
        new EsitoRichiestaSigilloElettronicoDocumento("SIGILLATO", "OK");
    esito.setCodice("000");
    esito.setMessageUUID("UUID");
    esito.setMessaggio("OK");
    return esito;
  }
}
