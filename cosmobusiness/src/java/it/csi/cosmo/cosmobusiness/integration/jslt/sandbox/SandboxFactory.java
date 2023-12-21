/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.sandbox;

import java.util.Arrays;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.entities.CosmoTCredenzialiAutenticazioneFruitore;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmobusiness.business.service.StatoPraticaService;
import it.csi.cosmo.cosmobusiness.integration.jslt.JsltProviderContext;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.CloneFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.GetGruppoFromCodiceFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.GetUtenteFromCodiceFiscaleFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.GetUtentiFromSelezioneFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.JsonFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.providers.RequireFunctionProvider;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.httpresponse.RootHttpResponseNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.statopratica.RootSandboxedNode;
import it.csi.cosmo.cosmobusiness.integration.jslt.sandbox.tokenrequest.RootTokenRequestNode;

/**
 *
 */

public interface SandboxFactory {

  public static RootSandboxedNode buildStatoPraticaSandbox(CosmoTPratica pratica,
      StatoPraticaService statoPraticaService) {

    //@formatter:off
    var ctx = JsltProviderContext.builder()
        .withPratica(pratica)
        .withStatoPraticaProvider(statoPraticaService)
        .build();
    //@formatter:on

    return new RootSandboxedNode(ctx);
  }

  public static Collection<Function> buildExtensionFunctions() {
    return Arrays.asList(new JsonFunctionProvider(), new CloneFunctionProvider(),
        new GetGruppoFromCodiceFunctionProvider(),
        new GetUtenteFromCodiceFiscaleFunctionProvider(), new GetUtentiFromSelezioneFunctionProvider(),
        new RequireFunctionProvider());
  }

  public static <T> RootHttpResponseNode<T> fromHttpResponse(ResponseEntity<T> response) {
    return new RootHttpResponseNode<>(response);
  }

  public static RootTokenRequestNode forTokenRequest(
      CosmoTCredenzialiAutenticazioneFruitore credenziali) {
    return new RootTokenRequestNode(credenziali);
  }
}
