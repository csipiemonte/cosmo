/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmopratiche.business.service.impl;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.UnprocessableEntityException;
import it.csi.cosmo.cosmopratiche.business.service.TipiPraticheService;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaDocumentoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaStatoRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.AggiornaTipoPraticaTrasformazioneDatiRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.CreaTipoPraticaRequest;
import it.csi.cosmo.cosmopratiche.dto.rest.FormatoFile;
import it.csi.cosmo.cosmopratiche.dto.rest.TabsDettaglio;
import it.csi.cosmo.cosmopratiche.dto.rest.TipiPraticheResponse;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;
import it.csi.test.cosmo.cosmopratiche.testbed.config.CosmoPraticheUnitTestInMemory;
import it.csi.test.cosmo.cosmopratiche.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmopratiche.testbed.model.ParentIntegrationTest;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoPraticheUnitTestInMemory.class})
@Transactional
public class TipiPraticheServiceImplTest extends ParentIntegrationTest {

  @Autowired
  TipiPraticheService tipiPraticheService;

  @Override
  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticatoTest);
  }

  @Test
  public void getAllTipiPratiche() {
    String filter = "{}";
    TipiPraticheResponse praticheResponse = tipiPraticheService.getTipiPratica(filter);
    assertNotNull("TipiPratiche non nullo", praticheResponse.getTipiPratiche());
  }

  @Test()
  public void getTipiPraticaPerEnte() {
    List<TipoPratica> tipiPratica = tipiPraticheService.getTipiPraticaPerEnte(1, true, false);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }

  @Test
  public void postTipoPratica() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    request.setCodice("test");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);

    TipoPratica response = tipiPraticheService.postTipoPratica(request);
    assertTrue("Tipo pratica creato con codice 'test'", response.getCodice().equals("test"));
  }

  @Test
  public void postTipoPraticaPreferenzeFirma() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    request.setCodice("test");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setCodiceEnteCertificatore("CERT1");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    TipoPratica response = tipiPraticheService.postTipoPratica(request);
    assertTrue("Tipo pratica creato con codice 'test'", response.getCodice().equals("test"));
  }

  @Test
  public void putTipoPratica() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);

    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }

  @Test
  public void putTipoPraticaPreferenzeFirma() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void getTipiPraticaPerEnteConCreazionePraticaFalse() {
    List<TipoPratica> tipiPratica = tipiPraticheService.getTipiPraticaPerEnte(1, false, false);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }
  
  @Test
  public void getTipiPraticaPerEnteConIdEnteNullECreazionePraticaNull() {
    List<TipoPratica> tipiPratica = tipiPraticheService.getTipiPraticaPerEnte(null, null, false);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }
  
  @Test
  public void getTipiPraticaPerEnteConIdEnteZeroECreazionePraticaNull() {
    List<TipoPratica> tipiPratica = tipiPraticheService.getTipiPraticaPerEnte(0, null, false);
    assertNotNull(tipiPratica);
    assertTrue(!tipiPratica.isEmpty());
  }
  
  @Test
  public void getTipoPratica() {
    TipoPratica tipoPratica = tipiPraticheService.getTipoPratica("TP1");
    assertNotNull(tipoPratica);
  }
  
  @Test
  public void postTipoPraticaConGruppoCreatoreEGruppoSupervisore() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    request.setCodice("test");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setIdGruppoCreatore(1L);
    request.setIdGruppoSupervisore(1L);

    TipoPratica response = tipiPraticheService.postTipoPratica(request);
    assertTrue("Tipo pratica creato con codice 'test'", response.getCodice().equals("test"));
  }
  
  @Test
  public void putTipoPraticaConGruppoCreatoreEGruppoSupervisore() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setIdGruppoCreatore(1L);
    request.setIdGruppoSupervisore(1L);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP3", request);
    assertTrue("Tipo pratica creato con codice 'TP3'", response.getCodice().equals("TP3"));
  }
  
  @Test(expected = UnprocessableEntityException.class)
  public void putTipoPraticaCASNonValorizzatoEOverrideResponsabileTrue() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(true);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    tipiPraticheService.putTipoPratica("TP1", request);
  }
  
  @Test(expected = UnprocessableEntityException.class)
  public void putTipoPraticaCASNonValorizzatoeRTSValorizzato() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setResponsabileTrattamentoStardas("test1");
    tipiPraticheService.putTipoPratica("TP1", request);
  }
  
  @Test(expected = UnprocessableEntityException.class)
  public void putTipoPraticaPDKeCDKuguali() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setCaseDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    tipiPraticheService.putTipoPratica("TP1", request);
  }
  
  @Test
  public void getTipoPraticaConCustomFormSingolo() {
    TipoPratica tipoPratica = tipiPraticheService.getTipoPratica("TP4");
    assertNotNull(tipoPratica);
  }
  
  @Test(expected = ConflictException.class)
  public void getTipoPraticaConCustomFormMultiplo() {
    TipoPratica tipoPratica = tipiPraticheService.getTipoPratica("TP5");
    assertNotNull(tipoPratica);
  }
  
  @Test
  public void getTipoPraticaConGruppoTipoPratica() {
    TipoPratica tipoPratica = tipiPraticheService.getTipoPratica("TP3");
    assertNotNull(tipoPratica);
  }
  
  @Test
  public void putTipoPraticaConGruppoCreatoreNull() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setIdGruppoCreatore(null);
    request.setIdGruppoSupervisore(null);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP3", request);
    assertTrue("Tipo pratica creato con codice 'TP3'", response.getCodice().equals("TP3"));
  }
  
  @Test
  public void deleteTipoPratica() {
    tipiPraticheService.deleteTipoPratica("TP1");
  }
  
  @Test
  public void putTipoPraticaConStatoeDettaglioPresentiDaAssociare() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaStatoRequest> listaStati = new ArrayList<>();
    AggiornaTipoPraticaStatoRequest statoRequest = new AggiornaTipoPraticaStatoRequest();
    statoRequest.setCodice("PROVA");
    statoRequest.setDescrizione("PROVA");
    listaStati.add(statoRequest);
    
    List<TabsDettaglio> listaDettaglio = new ArrayList<>();
    TabsDettaglio tabsDettaglio = new TabsDettaglio();
    tabsDettaglio.setCodice("documenti");
    tabsDettaglio.setDescrizione("documenti");
    listaDettaglio.add(tabsDettaglio );
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setStati(listaStati);
    request.setTabsDettaglio(listaDettaglio);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaConStatoeDettaglioDaAssociare() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaStatoRequest> listaStati = new ArrayList<>();
    AggiornaTipoPraticaStatoRequest statoRequest = new AggiornaTipoPraticaStatoRequest();
    statoRequest.setCodice("PROVA2");
    statoRequest.setDescrizione("PROVA2");
    listaStati.add(statoRequest);
    
    List<TabsDettaglio> listaDettaglio = new ArrayList<>();
    TabsDettaglio tabsDettaglio = new TabsDettaglio();
    tabsDettaglio.setCodice("documenti");
    tabsDettaglio.setDescrizione("documenti");
    TabsDettaglio tabsDettaglio2 = new TabsDettaglio();
    tabsDettaglio2.setCodice("riassunto");
    tabsDettaglio2.setDescrizione("riassunto");
    listaDettaglio.add(tabsDettaglio2);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setStati(listaStati);
    request.setTabsDettaglio(listaDettaglio);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTipiDocumento() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaDocRequest = new AggiornaTipoPraticaDocumentoRequest();
    
    aggiornaDocRequest.setCodice("prova");
    aggiornaDocRequest.setDescrizione("prova");
    aggiornaDocRequest.setFirmabile(true);
    aggiornaDocRequest.setDimensioneMassima(1L);
    tipiDocumento.add(aggiornaDocRequest);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTipiDocumento(tipiDocumento);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTipiDocumentoEsistente() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaDocRequest = new AggiornaTipoPraticaDocumentoRequest();
    aggiornaDocRequest.setCodice("codice 1");
    aggiornaDocRequest.setDescrizione("codice 1");
    aggiornaDocRequest.setFirmabile(true);
    aggiornaDocRequest.setDimensioneMassima(1L);
    tipiDocumento.add(aggiornaDocRequest);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTipiDocumento(tipiDocumento);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTrasformazioni() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaTrasformazioneDatiRequest> aggiornaTrasformazioni = new ArrayList<>();
    AggiornaTipoPraticaTrasformazioneDatiRequest trasformazione = new AggiornaTipoPraticaTrasformazioneDatiRequest();
    trasformazione.setId(2L);
    trasformazione.setCodiceFase("beforeProcessStart");
    trasformazione.setObbligatoria(true);
    trasformazione.setDefinizione("prova2");
    aggiornaTrasformazioni.add(trasformazione);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTrasformazioni(aggiornaTrasformazioni);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTrasformazioniEsistenti() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaTrasformazioneDatiRequest> aggiornaTrasformazioni = new ArrayList<>();
    AggiornaTipoPraticaTrasformazioneDatiRequest trasformazione = new AggiornaTipoPraticaTrasformazioneDatiRequest();
    trasformazione.setId(1L);
    trasformazione.setCodiceFase("beforeProcessStart");
    trasformazione.setObbligatoria(true);
    trasformazione.setDefinizione("prova1");
    aggiornaTrasformazioni.add(trasformazione);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTrasformazioni(aggiornaTrasformazioni);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test(expected = ConflictException.class)
  public void postTipoPraticaConCodiceEsistente() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    request.setCodice("TP1");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setCodiceEnteCertificatore("CERT1");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    tipiPraticheService.postTipoPratica(request);
  }
  
  @Test
  public void putTipoPraticaAggiornaCustomForm() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setCodiceCustomForm("custom-6");
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void postTipoPraticaConAggiornamentoTipiDocumentoPratica() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> listaTipi = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaTipo = new AggiornaTipoPraticaDocumentoRequest();
    aggiornaTipo.setCodice("prova");
    listaTipi.add(aggiornaTipo);
    
    request.setCodice("test");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setIdGruppoCreatore(1L);
    request.setIdGruppoSupervisore(1L);
    request.setTipiDocumento(listaTipi);

    TipoPratica response = tipiPraticheService.postTipoPratica(request);
    assertTrue("Tipo pratica creato con codice 'test'", response.getCodice().equals("test"));
  }
  
  @Test
  public void postTipoPraticaConAggiornamentoTipiDocumentoPraticaEMimeType() {
    CreaTipoPraticaRequest request = new CreaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> listaTipi = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaTipo = new AggiornaTipoPraticaDocumentoRequest();
    aggiornaTipo.setCodice("prova");
    List<FormatoFile> formati = new ArrayList<>();
    FormatoFile formato = new FormatoFile();
    formato.setCodice("prova formato");
    formato.setMimeType("no supporto");
    formati.add(formato );
    FormatoFile formato2 = new FormatoFile();
    formato2.setCodice("prova formato");
    formato2.setMimeType("raggruppato");
    formati.add(formato);
    aggiornaTipo.setFormatiFile(formati );
    listaTipi.add(aggiornaTipo);
    
    request.setCodice("test");
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setIdGruppoCreatore(1L);
    request.setIdGruppoSupervisore(1L);
    request.setTipiDocumento(listaTipi);

    TipoPratica response = tipiPraticheService.postTipoPratica(request);
    assertTrue("Tipo pratica creato con codice 'test'", response.getCodice().equals("test"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTipiDocumentoConAllegati() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaDocRequest = new AggiornaTipoPraticaDocumentoRequest();
    aggiornaDocRequest.setCodice("prova");
    aggiornaDocRequest.setDescrizione("prova");
    aggiornaDocRequest.setFirmabile(true);
    aggiornaDocRequest.setDimensioneMassima(1L);
    
    List<AggiornaTipoPraticaDocumentoRequest> allegati = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest allegato = new AggiornaTipoPraticaDocumentoRequest();
    allegato.setCodice("prova 2");
    allegato.setDescrizione("prova2");
    allegato.setFirmabile(true);
    allegato.setDimensioneMassima(1L);
    allegati.add(allegato);
    AggiornaTipoPraticaDocumentoRequest allegato2 = new AggiornaTipoPraticaDocumentoRequest();
    allegato2.setCodice("codice 1");
    allegato2.setDescrizione("prova2");
    allegato2.setFirmabile(true);
    allegato2.setDimensioneMassima(1L);
    allegati.add(allegato);
    aggiornaDocRequest.setAllegati(allegati);
    tipiDocumento.add(aggiornaDocRequest);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTipiDocumento(tipiDocumento);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
  
  @Test
  public void putTipoPraticaAggiornaTipiDocumentoConAllegatiEPadreEsistente() {
    AggiornaTipoPraticaRequest request = new AggiornaTipoPraticaRequest();
    
    List<AggiornaTipoPraticaDocumentoRequest> tipiDocumento = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest aggiornaDocRequest = new AggiornaTipoPraticaDocumentoRequest();
    aggiornaDocRequest.setCodice("codice 1");
    aggiornaDocRequest.setDescrizione("prova");
    aggiornaDocRequest.setFirmabile(true);
    aggiornaDocRequest.setDimensioneMassima(1L);
    
    List<AggiornaTipoPraticaDocumentoRequest> allegati = new ArrayList<>();
    AggiornaTipoPraticaDocumentoRequest allegato = new AggiornaTipoPraticaDocumentoRequest();
    allegato.setCodice("codice 1");
    allegato.setDescrizione("prova2");
    allegato.setFirmabile(true);
    allegato.setDimensioneMassima(1L);
    allegati.add(allegato);
    AggiornaTipoPraticaDocumentoRequest allegato2 = new AggiornaTipoPraticaDocumentoRequest();
    allegato2.setCodice("codice 2");
    allegato2.setDescrizione("prova2");
    allegato2.setFirmabile(true);
    allegato2.setDimensioneMassima(1L);
    allegati.add(allegato2);
    aggiornaDocRequest.setAllegati(allegati);
    tipiDocumento.add(aggiornaDocRequest);
    
    request.setIdEnte(1L);
    request.setDescrizione("test");
    request.setCreabileDaInterfaccia(true);
    request.setCreabileDaServizio(true);
    request.setProcessDefinitionKey("test");
    request.setOverrideResponsabileTrattamento(false);
    request.setAnnullabile(true);
    request.setCondivisibile(true);
    request.setAssegnabile(true);
    request.setOverrideFruitoreDefault(false);
    request.setCodiceEnteCertificatore("CERT2");
    request.setCodiceTipoOtp("TIPOOTP1");
    request.setCodiceSceltaMarcaTemporale("SI");
    request.setCodiceProfiloFEQ("PROFILOFEQ1");
    request.setCodiceTipoCredenziale("TIPOCREDFIRMA1");
    request.setTipiDocumento(tipiDocumento);
    TipoPratica response = tipiPraticheService.putTipoPratica("TP1", request);
    assertTrue("Tipo pratica creato con codice 'TP1'", response.getCodice().equals("TP1"));
  }
}
