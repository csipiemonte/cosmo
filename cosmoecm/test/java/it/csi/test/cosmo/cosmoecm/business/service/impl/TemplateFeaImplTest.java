/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.TemplateFeaService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFea;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateFeaResponse;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class TemplateFeaImplTest extends ParentIntegrationTest {

  @Autowired
  private TemplateFeaService templateFeaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getTemplateFeaByFilters() {
    String filtro = "";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getAllTemplateFea() {
    String filtro = "";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, true);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByIdEnte() {
    String filtro = "{\"filter\":{\"idEnte\":{\"eq\":\"1\"}}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByIdEnteConCampoDescrizione() {
    String filtro =
        "{\"filter\":{\"idEnte\":{\"eq\":\"1\"}}, \"page\": 0,\"size\":10, \"fields\":\"descrizione\"}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByTipoPratica() {
    String filtro = "{\"filter\":{\"codiceTipoPratica\":{\"eq\":\"TP2\"}}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByTipoDocumento() {
    String filtro = "{\"filter\":{\"codiceTipoDocumento\":{\"eq\":\"codice 1\"}}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByDescrizione() {
    String filtro = "{\"filter\":{\"descrizione\":{\"eq\":\"Maggio\"}}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateById() {
    TemplateFea tf = templateFeaService.getTemplateFeaId(1L);
    assertNotNull(tf);
  }

  @Test
  public void getTemplateFeaByIdPratica() {
    String filtro = "{\"filter\":{\"idPratica\":{\"eq\":\"1\"}}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void getTemplateFeaByCodiciTipoDocumento() {
    String filtro =
        "{\"filter\":{\"codiciTipoDocumento\":[\"codice 1\", \"codice 2\"]}, \"page\": 0,\"size\":10}";
    TemplateFeaResponse tf = templateFeaService.getTemplateFea(filtro, null);
    assertNotNull(tf.getTemplateFea());
  }

  @Test
  public void postTemplateFea() {
    var nuovoTemplateFea = templateFeaService.creaTemplateFea(getCommonTemplateFeaRequest());
    assertNotNull(nuovoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void postTemplateFeaEnteInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setIdEnte(100L);
    var nuovoTemplateFea = templateFeaService.creaTemplateFea(request);
    assertNotNull(nuovoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void postTemplateFeaTipoPraticaInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setCodiceTipoPratica("fake");
    var nuovoTemplateFea = templateFeaService.creaTemplateFea(request);
    assertNotNull(nuovoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void postTemplateFeaTipoDocumentoInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setCodiceTipoDocumento("fake");
    var nuovoTemplateFea = templateFeaService.creaTemplateFea(request);
    assertNotNull(nuovoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void deleteTemplateFeaIdInesistente() {
    templateFeaService.deleteTemplateFea(4L);
  }

  @Test
  public void deleteTemplateFea() {
    templateFeaService.deleteTemplateFea(1L);
  }

  @Test
  public void updateTemplateFea() {
    var aggiornamentoTemplateFea = templateFeaService.updateTemplateFea(1L, getCommonTemplateFeaRequest());
    assertNotNull(aggiornamentoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void updateTemplateFeaIdInesistente() {
    var aggiornamentoTemplateFea = templateFeaService.updateTemplateFea(4L, getCommonTemplateFeaRequest());
    assertNotNull(aggiornamentoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void updateTemplateFeaEnteInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setIdEnte(100L);
    var aggiornamentoTemplateFea = templateFeaService.updateTemplateFea(1L, request);
    assertNotNull(aggiornamentoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void updateTemplateFeaTipoPraticaInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setCodiceTipoPratica("fake");
    var aggiornamentoTemplateFea = templateFeaService.updateTemplateFea(1L, request);
    assertNotNull(aggiornamentoTemplateFea);
  }

  @Test(expected = NotFoundException.class)
  public void updateTemplateFeaTipoDocumentoInesistente() {
    var request = getCommonTemplateFeaRequest();
    request.setCodiceTipoDocumento("fake");
    var aggiornamentoTemplateFea = templateFeaService.updateTemplateFea(1L, request);
    assertNotNull(aggiornamentoTemplateFea);
  }

  private CreaTemplateFeaRequest getCommonTemplateFeaRequest() {
    CreaTemplateFeaRequest request = new CreaTemplateFeaRequest();
    request.setCodiceTipoDocumento("codice 1");
    request.setCodiceTipoPratica("TP1");
    request.setIdEnte(1L);
    request.setCoordinataX(1.1);
    request.setCoordinataY(1.1);
    request.setPagina(13L);
    request.setDescrizione("Nuova descrizione");
    request.setCaricatoDaTemplate(Boolean.TRUE);
    return request;
  }

}
