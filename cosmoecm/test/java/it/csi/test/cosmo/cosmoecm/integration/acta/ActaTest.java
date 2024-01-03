/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.integration.acta;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
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
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.ActaService;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaDocumentoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.integration.rest.CosmoSoapActaFeignClient;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;
import it.csi.cosmo.cosmosoap.dto.rest.ContenutoDocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiFisiciResponse;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSemplici;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiSempliciMap;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoFisico;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSemplice;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoSempliceMap;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActa;
import it.csi.cosmo.cosmosoap.dto.rest.IdentitaActaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.Titolario;
import it.csi.cosmo.cosmosoap.dto.rest.VociTitolario;
import it.csi.test.cosmo.cosmoecm.integration.acta.ActaTest.TestConfig;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class, TestConfig.class})
public class ActaTest extends ParentIntegrationTest {

  protected static CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.TEST_LOG_CATEGORY, "SmistaDocumentoTest");

  @Configuration
  public static class TestConfig {

    @Bean
    @Primary
    public CosmoSoapActaFeignClient cosmoSoapActaFeignClient() {
      return Mockito.mock(CosmoSoapActaFeignClient.class);
    }

  }

  @Autowired
  private CosmoSoapActaFeignClient mockCosmoSoapActaFeignClient;



  @Autowired
  private ActaService actaService;

  private void setUpMock() {
    reset(mockCosmoSoapActaFeignClient);
    when(mockCosmoSoapActaFeignClient.getIdentitaDisponibili()).thenReturn(getListaIdentitaActa());
    when(mockCosmoSoapActaFeignClient.getDocumentiFisiciByidDocumentoSemplice(any(), any())).thenReturn(getDocumentiFisici());
    when(mockCosmoSoapActaFeignClient.getRicercaPerIndiceClassificazioneEstesa(any(), any())).thenReturn("objectID");
    when(mockCosmoSoapActaFeignClient.getDocumentiSempliciPageable(any(), any(), any())).thenReturn(getDocumentiSemplici());
    when(mockCosmoSoapActaFeignClient.getDocumentiSempliciMap(any(), any())).thenReturn(getDocumentiSempliciMap());
    when(mockCosmoSoapActaFeignClient.getDocumentiFisiciMap(any(),  any())).thenReturn(new DocumentiFisiciMap());
    when(mockCosmoSoapActaFeignClient.getContenutoPrimarioId(any(),  any())).thenReturn(new ContenutoDocumentoFisico());
    when(mockCosmoSoapActaFeignClient.getRicercaTitolario(any(),  any())).thenReturn(new Titolario());
    when(mockCosmoSoapActaFeignClient.ricercaAlberaturaVociPageable(any(),  any(), any(), any())).thenReturn(new VociTitolario());

  }

  @Test
  public void ricercaIdentitaDisponibili() {
    setUpMock();
    var esito = actaService.findIdentitaDisponibili();
    assertNotNull(esito);
  }

  @Test
  public void ricercaDocumentiFisiciByIdDocumentoSemplice() {
    setUpMock();
    var esito = actaService.findDocumentiFisiciByIdDocumentoSemplice("identita", "id");
    assertNotNull(esito);
  }

  @Test
  public void ricercaIdByIndiceClassificazioneEstesaAggregazione() {
    setUpMock();
    var esito = actaService.findIdByIndiceClassificazioneEstesaAggregazione("identita", "indiceClassificazioneEsteso");
    assertNotNull(esito);
  }

  @Test
  public void ricercaTitolario() {
    setUpMock();
    var esito = actaService.getTitolarioActa("identita", "indiceClassificazioneEsteso");
    assertNotNull(esito);
  }

  @Test
  public void ricercaAlberaturaVociTitolario() {
    setUpMock();
    var esito = actaService.ricercaAlberaturaVociTitolario("idIdentita", "chiaveTitolario",
        "chiavePadre", "filter");
    assertNotNull(esito);
  }

  @Test
  public void ricercaDocumentiSemplici() {
    setUpMock();
    var esito = actaService.findDocumentiSemplici("identita", "filtro");
    assertNotNull(esito);
  }

  @Test
  public void importaDocumenti() {
    setUpMock();
    ImportaDocumentiActaRequest importaDocumenti = new ImportaDocumentiActaRequest();
    importaDocumenti.setIdPratica(1L);
    List<ImportaDocumentiActaDocumentoRequest> listaDocReq = new ArrayList<>();
    importaDocumenti.setDocumenti(listaDocReq);
    var esito = actaService.importaDocumentiActa("identita", importaDocumenti);
    assertNotNull(esito);
  }

  private IdentitaActaResponse getListaIdentitaActa() {
    IdentitaActaResponse iar = new IdentitaActaResponse();
    List<IdentitaActa> listaIdentita = new ArrayList<>();
    IdentitaActa identitaSingola = new IdentitaActa();
    identitaSingola.setCodiceAOO("codiceAOO");
    identitaSingola.setCodiceNodo("codiceNodo");
    identitaSingola.setCodiceStruttura("codiceStruttura");
    identitaSingola.setDescrizioneAOO("descrizioneAOO");
    identitaSingola.setDescrizioneNodo("descrizioneNodo");
    identitaSingola.setDescrizioneStruttura("descrizioneStruttura");
    identitaSingola.setIdentificativoAOO("identificativoAOO");
    identitaSingola.setIdentificativoNodo("identificativoNodo");
    identitaSingola.setIdentificativoStruttura("identificativoStruttura");
    listaIdentita.add(identitaSingola);
    iar.setIdentitaActa(listaIdentita);
    return iar;
  }

  private DocumentiFisiciResponse getDocumentiFisici() {
    DocumentiFisiciResponse dfr = new DocumentiFisiciResponse();
    List<DocumentoFisico> listDocumentiFisici = new ArrayList<>();
    DocumentoFisico documentoSingolo = new DocumentoFisico();
    documentoSingolo.setDbKey("dbKey");
    documentoSingolo.setDescrizione("descrizione");
    documentoSingolo.setProgressivo(1);
    listDocumentiFisici.add(documentoSingolo);
    dfr.setDocumentiFisici(listDocumentiFisici);
    return dfr;
  }

  private DocumentiSemplici getDocumentiSemplici() {
    DocumentiSemplici docSemplici = new DocumentiSemplici();
    List<DocumentoSemplice> listDocSemplice = new ArrayList<>();
    DocumentoSemplice docSemplice = new DocumentoSemplice();
    listDocSemplice.add(docSemplice);
    docSemplici.setDocumenti(listDocSemplice);
    return docSemplici;
  }

  private DocumentiSempliciMap getDocumentiSempliciMap() {
    DocumentiSempliciMap docSempliciMap = new DocumentiSempliciMap();
    List<DocumentoSempliceMap> listDocSempliceMap = new ArrayList<>();
    docSempliciMap.setDocumentiSempliciMap(listDocSempliceMap);
    return docSempliciMap;
  }



}
