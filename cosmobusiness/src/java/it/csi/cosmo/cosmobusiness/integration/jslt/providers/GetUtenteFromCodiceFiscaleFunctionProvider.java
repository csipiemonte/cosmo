/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoAuthorizationUtentiFeignClient;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public class GetUtenteFromCodiceFiscaleFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {

    if (arguments == null || arguments.length == 0) {
      throw new BadRequestException("Utente non indicato");
    }

    String cf = arguments[0].asText();

    CosmoAuthorizationUtentiFeignClient client =
        SpringApplicationContextHelper.getBean(CosmoAuthorizationUtentiFeignClient.class);

    var resp = client.getUtentiCodiceFiscale(cf);

    return ObjectUtils.getDataMapper().convertValue(resp.getUtente(), JsonNode.class);
  }

  @Override
  public int getMaxArguments() {
    return 1;
  }

  @Override
  public int getMinArguments() {
    return 1;
  }

  @Override
  public String getName() {
    return "getUtenteFromCodiceFiscale";
  }

}
