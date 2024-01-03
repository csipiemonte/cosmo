/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.business.service.impl;

import static org.junit.Assert.assertNotNull;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.cosmoecm.business.service.FeaService;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutoDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.Documento;
import it.csi.cosmo.cosmoecm.dto.rest.FirmaFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.TemplateDocumento;
import it.csi.cosmo.cosmoecm.dto.rest.TipoContenutoDocumento;
import it.csi.test.cosmo.cosmoecm.testbed.config.CosmoEcmUnitTestInMemory;
import it.csi.test.cosmo.cosmoecm.testbed.config.TestConstants;
import it.csi.test.cosmo.cosmoecm.testbed.model.ParentIntegrationTest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoEcmUnitTestInMemory.class})
@Transactional
public class FeaServiceImplTest extends ParentIntegrationTest {

  public static final String OTP = "00000";

  @Autowired
  private FeaService feaService;

  @Before
  public void autentica() {
    this.autentica(TestConstants.buildUtenteAutenticato());
  }

  @Test(expected = NotFoundException.class)
  public void eseguiFirmaElettronicaAvanzataErroreValidaOtpInesistente() {
    var payload = getPayload();
    payload.setOtp("fake");
    feaService.firma(payload);
  }

  @Ignore("lanciato con tutti gli unit test va in errore. Da risolvere utilizzando junit sulle funzioni asincrone")
  @Test
  public void eseguiFirmaElettronicaAvanzata() {
    var payload = getPayload();
    var contenuto = new ContenutoDocumento();
    var tipo = new TipoContenutoDocumento();
    tipo.setCodice(it.csi.cosmo.common.entities.enums.TipoContenutoDocumento.ORIGINALE.toString());
    contenuto.setId(2L);
    contenuto.setTipo(tipo);
    contenuto.setDtInserimento(OffsetDateTime.now());
    payload.getTemplateDocumenti().get(0).getDocumento().setContenuti(List.of(contenuto));
    var result = feaService.firma(payload);
    assertNotNull(result);
  }

  private FirmaFeaRequest getPayload() {
    var templateDocumento = new TemplateDocumento();
    var documento = new Documento();
    templateDocumento.setCoordinataX(10.0);
    templateDocumento.setCoordinataY(10.0);
    templateDocumento.setPagina(0L);
    templateDocumento.setDocumento(documento);
    var payload = new FirmaFeaRequest();
    payload.setIconaFea("icona");
    payload.setOtp(OTP);
    payload.setTemplateDocumenti(List.of(templateDocumento));
    return payload;
  }


}
