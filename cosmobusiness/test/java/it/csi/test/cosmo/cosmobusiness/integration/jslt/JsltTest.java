/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.integration.jslt;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.CosmoDTipoPratica;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.business.service.MotoreJsonDinamicoService;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.SandboxFactory;
import it.csi.test.cosmo.cosmobusiness.testbed.config.CosmoBusinessUnitTestInMemory;
import it.csi.test.cosmo.cosmobusiness.testbed.model.ParentIntegrationTest;
import it.csi.test.cosmo.cosmobusiness.testbed.service.AttivitaTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.EnteTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.PraticaTestbedService;
import it.csi.test.cosmo.cosmobusiness.testbed.service.UtenteTestbedService;

/**
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CosmoBusinessUnitTestInMemory.class})
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class JsltTest extends ParentIntegrationTest {

  @Autowired
  private EnteTestbedService enteTestbedService;

  @Autowired
  private UtenteTestbedService utenteTestbedService;

  @Autowired
  private PraticaTestbedService praticaTestbedService;

  @Autowired
  private StatoPraticaService statoPraticaService;

  @Autowired
  private AttivitaTestbedService attivitaTestbedService;

  @Autowired
  private MotoreJsonDinamicoService motoreJsonDinamicoService;

  @Test(expected = InternalServerException.class)
  public void testTransform() throws IOException {
    // crea una pratica
    CosmoTUtente utente = utenteTestbedService.creaUtente();
    CosmoTEnte ente = enteTestbedService.creaEnte();
    CosmoDTipoPratica tipoPratica = praticaTestbedService.creaTipoPratica(ente);
    utenteTestbedService.associaAdEnte(utente, ente);
    var pratica = praticaTestbedService.creaPratica(ente, null, tipoPratica);

    attivitaTestbedService.creaAttivita(pratica);
    attivitaTestbedService.creaAttivita(pratica);
    attivitaTestbedService.creaAttivita(pratica);

    // creo sandbox
    var sandboxedVirtualNode =
        SandboxFactory.buildStatoPraticaSandbox(pratica, statoPraticaService);

    //@formatter:off
    String expr = 
      "{  \n"
        + "  \"tenantId\": .pratica.ente.codiceIpa, \n"
        + "  \"variabili\": .variabili, \n"
        + "  \"variabili4\": .variabili.var4, \n"
        + "  \"variabili4_1\": .variabili.var4[1], \n"
        + "  \"variabili5_key2\": .variabili.var5.key2, \n"
        + "  \"oggetto\" : .pratica.oggetto, \n"
        + "  \"att0Nome\": .pratica.attivita[0].nome, \n"
        + "  \"att1Nome\": .pratica.attivita[1].nome, \n"
        + "  \"att1Msg0\": .pratica.attivita[1].messaggiCollaboratori[0].messaggio \n"
      + "}"
    ;
    //@formatter:on
    JsonNode actual = motoreJsonDinamicoService.eseguiMappatura(expr, sandboxedVirtualNode);

    log("TRANSFORM INPUT: \n" + expr);

    dump("TRANSFORM RESULT", actual);

  }
}
