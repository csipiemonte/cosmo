/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.ReportTemplateService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaTemplateRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateReportResponse;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class ReportTemplateImplTest extends ParentIntegrationTest {

  @Autowired
  private ReportTemplateService reportTemplateService;

  private static final String UPDATED = "updated";

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getAllTemplate() {
    String filtro = "";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertNotNull(tr.getTemplateReport());
  }

  @Test
  public void getTemplateByIdEnte() {
    String filtro =
        "{\"filter\":{\"idEnte\":{\"eq\":\"1\", \"defined\":\"false\"}}, \"page\": 0,\"size\":10}";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertNotNull(tr.getTemplateReport());
  }

  @Test
  public void getTemplateByCodiceTipoPraticaErrato() {
    String filtro = "{\"filter\":{\"codiceTipoPratica\":{\"eq\":\"TP3\"}}, \"page\": 0,\"size\":10}";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertTrue(tr.getTemplateReport().isEmpty());
  }

  @Test
  public void getTemplateByCodiceTipoPraticaCorretto() {
    String filtro =
        "{\"filter\":{\"codiceTipoPratica\":{\"eq\":\"TP2\"}}, \"page\": 0,\"size\":10}";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertTrue(tr.getTemplateReport().size() == 1);
  }

  @Test
  public void getTemplateByCodice() {
    String filtro = "{\"filter\":{\"codice\":{\"eq\":\"codice2\"}}, \"page\": 0,\"size\":10}";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertNotNull(tr.getTemplateReport());
  }

  @Test
  public void getTemplateByCodiceIn() {
    String filtro = "{\"filter\":{\"codice\":{\"ci\":\"codice\"}}, \"fields\": \"id_ente,codice_tipo_pratica\", \"page\": 0,\"size\":10}";
    TemplateReportResponse tr = reportTemplateService.getReportTemplate(filtro);
    assertTrue(tr.getTemplateReport().size() == 1);
  }

  @Test(expected = NotFoundException.class)
  public void erroreEnteCreazioneTemplate() {
    var ctr = payloadCreazioneTemplate();
    ctr.setIdEnte(123L);
    assertTrue(null != reportTemplateService.creaReportTemplate(ctr));
  }

  @Test(expected = NotFoundException.class)
  public void erroreTipoPraticaCreazioneTemplate() {
    var ctr = payloadCreazioneTemplate();
    ctr.setIdEnte(1L);
    ctr.setCodiceTipoPratica("FAKE");
    assertTrue(null != reportTemplateService.creaReportTemplate(ctr));
  }

  @Test
  public void creazioneTemplate() {
    var ctr = payloadCreazioneTemplate();
    assertTrue(null != reportTemplateService.creaReportTemplate(ctr));
  }

  @Test
  public void creazioneTemplateConCodicePadre() {
    var ctr = payloadCreazioneTemplate();
    ctr.setCodiceTemplatePadre("codiceTemplatePadre");
    assertTrue(null != reportTemplateService.creaReportTemplate(ctr));
  }

  @Test(expected = NotFoundException.class)
  public void deleteTemplateInesistente() {
    reportTemplateService.deleteReportTemplate(123L);
  }

  @Test
  public void deleteTemplate() {
    reportTemplateService.deleteReportTemplate(1L);
  }

  @Test(expected = NotFoundException.class)
  public void ricercaTemplateIdErrato() {
    reportTemplateService.getReportTemplateId(123L);
  }

  @Test
  public void ricercaTemplateId() {
    reportTemplateService.getReportTemplateId(1L);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornamentoTemplateIdErrato() {
    reportTemplateService.updateReportTemplate(123L, payloadCreazioneTemplate());
  }

  @Test(expected = NotFoundException.class)
  public void aggiornamentoTemplateIdEnteErrato() {
    var payloadAggiornamento = payloadCreazioneTemplate();
    payloadAggiornamento.setIdEnte(123L);
    reportTemplateService.updateReportTemplate(1L, payloadAggiornamento);
  }

  @Test(expected = NotFoundException.class)
  public void aggiornamentoTemplateTipoPraticaErrato() {
    var payloadAggiornamento = payloadCreazioneTemplate();
    payloadAggiornamento.setCodiceTipoPratica("FAKE");
    reportTemplateService.updateReportTemplate(1L, payloadAggiornamento);
  }

  @Test
  public void aggiornamentoTemplate() {
    var payloadAggiornamento = payloadCreazioneTemplate();
    payloadAggiornamento.setCodice(UPDATED);
    var aggiornamentoTemplate = reportTemplateService.updateReportTemplate(1L, payloadAggiornamento);
    assertTrue(aggiornamentoTemplate.getCodice().equalsIgnoreCase(UPDATED));
  }

  @Test
  public void aggiornamentoTemplateConCodicePadre() {
    var payloadAggiornamento = payloadCreazioneTemplate();
    payloadAggiornamento.setCodice(UPDATED);
    payloadAggiornamento.setCodiceTemplatePadre(UPDATED);
    var aggiornamentoTemplate = reportTemplateService.updateReportTemplate(1L, payloadAggiornamento);
    assertTrue(aggiornamentoTemplate.getCodice().equalsIgnoreCase(UPDATED)
        && aggiornamentoTemplate.getCodiceTemplatePadre().equalsIgnoreCase(UPDATED));
  }

  private CreaTemplateRequest payloadCreazioneTemplate() {
    var payload = new CreaTemplateRequest();
    payload.setCodice("codice");
    payload.setCodiceTipoPratica("TP1");
    payload.setSorgenteTemplate("sorgente");
    payload.setIdEnte(1L);
    return payload;
  }


}
