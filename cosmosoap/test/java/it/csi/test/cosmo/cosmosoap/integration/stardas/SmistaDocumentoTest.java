/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmosoap.integration.stardas;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import it.csi.cosmo.cosmosoap.business.service.Index2Service;
import it.csi.cosmo.cosmosoap.business.service.StardasService;
import it.csi.cosmo.cosmosoap.config.Constants;
import it.csi.cosmo.cosmosoap.dto.index2.CosmoDocumentoIndex;
import it.csi.cosmo.cosmosoap.dto.rest.ConfigurazioneChiamante;
import it.csi.cosmo.cosmosoap.dto.rest.DatiSmistaDocumento;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentoElettronico;
import it.csi.cosmo.cosmosoap.dto.rest.EmbeddedBinary;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.test.cosmo.cosmosoap.testbed.config.CosmoSoapUnitTestInMemory;
import it.csi.test.cosmo.cosmosoap.testbed.model.ParentUnitTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoSoapUnitTestInMemory.class})
public class SmistaDocumentoTest extends ParentUnitTest {

  public static final String TEST_ROOT = "test/SmistaDocumentoTest";

  @Autowired
  private StardasService stardasService;

  @Autowired
  private Index2Service index2Service;

  @Before
  public void cleanupBefore() {
    cleanup();
  }

  @After
  public void cleanupAfter() {
    cleanup();
  }

  private void cleanup() {
    getLogger().info("DELETING TEST FOLDER");
    index2Service.delete(TEST_ROOT);
  }

  @Test
  public void testSmistaDocumento() throws IOException {
    SmistaDocumentoRequest smistamento = setDocumento();
    var esito = stardasService.smistaDocumento(smistamento);
    assertNotNull(esito);
    assertTrue((esito.getResult().getCodice().equalsIgnoreCase(Constants.ESITO_SMISTAMENTO_OK)) || esito.getResult().getCodice().matches(Constants.REGEX_CODICE_ESITO_WARNING));
  }

  @Test
  public void statoRichiesta() throws IOException {

    SmistaDocumentoRequest smistamento = setDocumento();

    var esitoSmistamento = stardasService.smistaDocumento(smistamento);

    GetStatoRichiestaRequest statoRichiestaRequest = new GetStatoRichiestaRequest();
    statoRichiestaRequest.setConfigurazioneChiamante(smistamento.getConfigurazioneChiamante());
    statoRichiestaRequest.setMessageUUID(esitoSmistamento.getMessageUUID());

    var esito = stardasService.getStatoRichiesta(statoRichiestaRequest);
    assertNotNull(esito);
    assertTrue((esito.getResult().getCodice().equalsIgnoreCase(Constants.ESITO_SMISTAMENTO_OK)) || esito.getResult().getCodice().matches(Constants.REGEX_CODICE_ESITO_WARNING));
  }


  private SmistaDocumentoRequest setDocumento() throws IOException {

    CosmoDocumentoIndex documentoIndex = createEntity();

    var configurazione = new ConfigurazioneChiamante();
    configurazione.setCodiceApplicazione("ITER_APPFIR_CRP_COSMO_TEST");
    configurazione.setCodiceFiscaleEnte("97603810017");
    configurazione.setCodiceFruitore("COSMO_DEV");
    configurazione.setCodiceTipoDocumento("MOD_DIP");

    var contenutoBinario = new EmbeddedBinary();
    contenutoBinario.setContent(documentoIndex.getContent());

    var documentoElettronico = new DocumentoElettronico();
    documentoElettronico.setDocumentoFirmato(false);
    documentoElettronico.setMimeType("application/pdf");
    documentoElettronico.setNomeFile("cosmo-extracted.pdf");
    documentoElettronico.setContenutoBinario(contenutoBinario);

    var datiSmistamento = new DatiSmistaDocumento();
    datiSmistamento.setResponsabileTrattamento("DVNLRD99A01L219J");
    datiSmistamento.setIdDocumentoFruitore("1106");
    datiSmistamento.setDocumentoElettronico(documentoElettronico);
    datiSmistamento.setUuidNodo(documentoIndex.getNodeUUID());

    SmistaDocumentoRequest request = new SmistaDocumentoRequest();
    request.setConfigurazioneChiamante(configurazione);
    request.setDatiSmistaDocumento(datiSmistamento);

    return request;
  }


  private CosmoDocumentoIndex buildEntity() throws IOException {
    CosmoDocumentoIndex documento = new CosmoDocumentoIndex();

    documento.setFilename("cosmo-extracted.pdf");
    documento
    .setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    InputStream is = new ClassPathResource("/samplefiles/cosmo-extracted.pdf").getInputStream();
    documento.setContent(is.readAllBytes());
    documento.setDescrizione("smistamento a stardas - documento 1");
    documento.setIdDocumentoCosmo(1L);
    documento.setTipoDocumento("P");

    return documento;
  }

  private CosmoDocumentoIndex createEntity() throws IOException {
    CosmoDocumentoIndex documento = buildEntity();

    documento = index2Service.create(TEST_ROOT, documento);

    assertNotNull(documento.getUid());
    assertNotNull(documento.getFilename());
    assertNotNull(documento.getDescrizione());
    assertNotNull(documento.getIdDocumentoCosmo());
    assertNotNull(documento.getTipoDocumento());
    assertNotNull(documento.getMimeType());
    assertNotNull(documento.getVersionable());

    return documento;
  }

}
