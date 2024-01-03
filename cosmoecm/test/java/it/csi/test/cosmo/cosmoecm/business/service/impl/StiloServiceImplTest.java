/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.cosmoecm.business.service.StiloService;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoInvioStiloRequest;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class StiloServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private StiloService stiloService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void testPratica1Codice1WithoutIdUd() {

    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"1\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 1\", \"prova\"]}, \"codici\": [{\"codice\": \"codice 1\"}, {\"codice\": \"prova\"}]}}";

    List<Documento> documentiInvioStilo =
        stiloService.getDocumentiInvioStilo(filter);
    assertNotNull(documentiInvioStilo);
    assertEquals(2, documentiInvioStilo.size());
  }


  @Test
  public void testPratica1Codice1WithIdUd() {
    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"1\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 1\"]},"
        + "\"idUd\":{\"eq\": \"2967913\"}, \"codici\": [{\"codice\": \"codice 1\"}]}}";

    List<Documento> documentiInvioStiloIdUd1 =
        stiloService.getDocumentiInvioStilo(filter);

    filter = "{\"filter\":{\"idPratica\":{\"eq\": \"1\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 1\"]},"
        + "\"idUd\":{\"eq\": \"2\"}, \"codici\": [{\"codice\": \"codice 1\"}]}}";

    List<Documento> documentiInvioStiloIdUd2 =
        stiloService.getDocumentiInvioStilo(filter);

    assertNotNull(documentiInvioStiloIdUd1);
    assertNotNull(documentiInvioStiloIdUd2);
    assertEquals(2, documentiInvioStiloIdUd1.size());
    assertEquals(1, documentiInvioStiloIdUd2.size());
  }

  @Test
  public void testPratica2Codice2WithoutIdUd() {
    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"2\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 2\"]}, \"codici\": [{\"codice\": \"codice 2\"}] }}";

    List<Documento> documentiInvioStilo =
        stiloService.getDocumentiInvioStilo(filter);

    assertNotNull(documentiInvioStilo);
    assertEquals(1, documentiInvioStilo.size());
  }


  @Test
  public void testPratica2Codice2WitIdUd() {

    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"1\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 2\"]}," + "\"idUd\":{\"eq\": \"4\"}}}";

    List<Documento> documentiInvioStilo = stiloService.getDocumentiInvioStilo(filter);

    assertNotNull(documentiInvioStilo);
    assertEquals(0, documentiInvioStilo.size());
  }

  @Test
  public void testPratica2Codice1WithoutIdUd() {
    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"2\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 1\"]}}}";

    List<Documento> documentiInvioStilo =
        stiloService.getDocumentiInvioStilo(filter);

    assertNotNull(documentiInvioStilo);
    assertEquals(0, documentiInvioStilo.size());
  }

  @Test
  public void testIdUd3() {
    String filter = "{\"filter\":{\"idPratica\":{\"eq\": \"1\"},"
        + "\"tipologiaDocumento\":{\"in\": [\"codice 1\"]},"
        + "\"idUd\":{\"eq\": \"3\"}, \"codici\": [{\"codice\": \"codice 1\"}]}}";

    List<Documento> documentiInvioStilo = stiloService.getDocumentiInvioStilo(filter);

    assertNotNull(documentiInvioStilo);
    assertEquals(2, documentiInvioStilo.size());
  }

  @Test
  public void impostaEsitoInvioStiloDocumentoEsistente() {
    EsitoInvioStiloRequest request = new EsitoInvioStiloRequest();
    request.setCodiceEsitoInvioStilo(555);
    request.setIdUd(1L);
    request.setMessaggioEsitoInvioStilo("Documento Inviato");
    request.setRisultato(0);
    stiloService.impostaEsitoInvioStilo("3", request);
  }

  @Test(expected = BadRequestException.class)
  public void impostaEsitoInvioStiloDocumentoEsistenteGiaInviato() {
    EsitoInvioStiloRequest request = new EsitoInvioStiloRequest();
    request.setCodiceEsitoInvioStilo(555);
    request.setIdUd(1L);
    request.setMessaggioEsitoInvioStilo("Documento Inviato");
    request.setRisultato(0);
    stiloService.impostaEsitoInvioStilo("1", request);
  }

  @Test
  public void impostaEsitoInvioStiloNuovoDocumento() {
    EsitoInvioStiloRequest request = new EsitoInvioStiloRequest();
    request.setCodiceEsitoInvioStilo(555);
    request.setIdUd(5L);
    request.setMessaggioEsitoInvioStilo("Errore invio");
    request.setRisultato(1);
    stiloService.impostaEsitoInvioStilo("3", request);
  }

  @Test
  public void impostaInvioDocumentoStiloDocumentoEsistente() {
    stiloService.impostaInvioDocumentoStilo("2", 2L);
  }

  @Test(expected = BadRequestException.class)
  public void impostaInvioDocumentoStiloDocumentoEsistenteGiaInviato() {
    stiloService.impostaInvioDocumentoStilo("1", 1L);
  }

  @Test(expected = BadRequestException.class)
  public void impostaInvioDocumentoStiloDocumentoEsistenteInFaseDiInvio() {
    stiloService.impostaInvioDocumentoStilo("1", 2L);
  }

  @Test
  public void impostaInvioDocumentoStiloNuovoDocumento() {
    stiloService.impostaInvioDocumentoStilo("2", 6L);
  }

  @Test
  public void aggiornamentoStatoInvioStiloDocPresenteEsitoDiversoDaInviato() {
    EsitoInvioStiloRequest request = new EsitoInvioStiloRequest();
    request.setCodiceEsitoInvioStilo(555);
    request.setIdUd(2L);
    request.setMessaggioEsitoInvioStilo("Errore invio");
    request.setRisultato(1);
    stiloService.impostaEsitoInvioStilo("2", request);
  }


  @Test
  public void aggiornamentoStatoInvioStiloDocNonPresente() {
    EsitoInvioStiloRequest request = new EsitoInvioStiloRequest();
    request.setCodiceEsitoInvioStilo(555);
    request.setIdUd(2L);
    request.setMessaggioEsitoInvioStilo("Errore invio");
    request.setRisultato(1);
    stiloService.impostaEsitoInvioStilo("1", request);
  }
}
