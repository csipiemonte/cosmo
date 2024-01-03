/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

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
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.TipoDocumentoService;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class TipoDocumentoServiceImplTest extends ParentIntegrationTest {

  private static final String IL_TIPO_DOCUMENTO_NON_DEVE_ESSERE_NULLO =
      "il tipo documento non deve essere nullo";

  @Autowired
  private TipoDocumentoService tipoDocumentoService;

  protected static final String CODICE_1 = "codice 1";
  protected static final String CODICE_2 = "codice 2";

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test
  public void getTipiDocumenti() {
    List<TipoDocumento> result = tipoDocumentoService.getTipiDocumenti("1", null);

    assertNotNull(IL_TIPO_DOCUMENTO_NON_DEVE_ESSERE_NULLO, result);
    assertTrue("1 tipo di documento deve essere associato alla pratica", result.size() == 1);
  }

  @Test(expected = BadRequestException.class)
  public void getTipiDocumentiConIdPraticaNullo() {
    tipoDocumentoService.getTipiDocumenti("", "");
  }

  @Test(expected = BadRequestException.class)
  public void getTipiDocumentiConIdPraticaNonNumerico() {
    tipoDocumentoService.getTipiDocumenti("uno", "");
  }

  @Test
  public void getAllTipiDocumenti() {
    List<TipoDocumento> result = tipoDocumentoService.getTipiDocumentiAll("TP1");

    assertNotNull(IL_TIPO_DOCUMENTO_NON_DEVE_ESSERE_NULLO, result);
    assertTrue("2 tipi di documenti devono essere associati alla pratica", result.size() == 2);
  }

  @Test(expected = BadRequestException.class)
  public void getTipiDocumentiConTipoPraticaNullo() {
    tipoDocumentoService.getTipiDocumentiAll("");
  }

  @Test(expected = NotFoundException.class)
  public void getTipiDocumentiConTipoPraticaInesistente() {
    tipoDocumentoService.getTipiDocumentiAll("fake");
  }

  @Test
  public void getTipiDocumentoByListaCodici() {
    List<String> tipiDocumenti = List.of(CODICE_1, CODICE_2);
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti);
    assertTrue("2 tipi di documenti devono essere estratti", res.size() == 2);
  }

  @Test
  public void getTipiDocumentoByListaCodiciVuota() {
    List<String> tipiDocumenti = new ArrayList<>();
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti);
    assertTrue("3 tipi di documenti devono essere estratti", res.size() == 3);
  }

  @Test
  public void ricercaTipiDocumentoParametrizzataVuota() {
    List<String> tipiDocumenti = new ArrayList<>();
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti, false, "", "");
    assertTrue(res.size() == 2);
  }

  @Test
  public void ricercaTipiDocumentoParametrizzataTipiDocumento() {
    List<String> tipiDocumenti = List.of(CODICE_1, CODICE_2);
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti, false, "", "");
    assertTrue(res.size() == 2);
  }

  @Test
  public void ricercaTipiDocumentoParametrizzataTipiDocumentoFormatoFile() {
    List<String> tipiDocumenti = List.of(CODICE_1, CODICE_2);
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti, true, "", "");
    assertTrue(res.size() == 2);
  }

  @Test
  public void ricercaTipiDocumentoParametrizzataTipiDocumentoFormatoFileCodicePadre() {
    List<String> tipiDocumenti = new ArrayList<>();
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti, true, CODICE_2, "");
    assertTrue(res.size() == 1);
  }

  @Test
  public void ricercaTipiDocumentoParametrizzataTipiDocumentoFormatoFileCodicePadreCodiceTipoPratica() {
    List<String> tipiDocumenti = new ArrayList<>();
    var res = tipoDocumentoService.getTipiDocumento(tipiDocumenti, true, CODICE_1, "TP1");
    assertTrue(res.size() == 1);
  }

}
