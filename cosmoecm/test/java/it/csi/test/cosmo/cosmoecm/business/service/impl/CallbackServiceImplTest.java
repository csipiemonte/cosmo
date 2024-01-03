/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.StatoCallbackFruitore;
import it.csi.cosmo.cosmoecm.business.service.CallbackService;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentiLinkFruitore;
import it.csi.cosmo.cosmoecm.dto.rest.EsitoCreazioneDocumentoLinkFruitore;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class CallbackServiceImplTest extends ParentIntegrationTest {

  @Autowired
  private CallbackService callbackService;

  @Test
  public void test() {
    Long idFruitore = 2L;
    Map<String, Object> parametri = new HashMap<>();
    parametri.put("idPratica", 1L);
    parametri.put("idDocumento", 1L);
    EsitoCreazioneDocumentiLinkFruitore payload = new EsitoCreazioneDocumentiLinkFruitore();
    var esito = new EsitoCreazioneDocumentoLinkFruitore();
    payload.setEsiti(List.of(esito));
    var result =
        callbackService.schedulaInvioAsincrono(
            OperazioneFruitore.CUSTOM, idFruitore, payload, parametri, "");
    assertNotNull(result);
    assertNotNull(result.getStato());
    assertNotNull(result.getStato().getCodice());
    assertTrue(result.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.toString()));
  }
}
