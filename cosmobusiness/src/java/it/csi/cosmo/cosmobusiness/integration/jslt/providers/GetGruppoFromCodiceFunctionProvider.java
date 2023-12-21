/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoAuthorizationGruppiFeignClient;
import it.csi.cosmo.cosmobusiness.util.listener.SpringApplicationContextHelper;

/**
 *
 */

public class GetGruppoFromCodiceFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {

    if (arguments == null || arguments.length == 0) {
      throw new BadRequestException("Utente non indicato");
    }

    String codice = arguments[0].asText();

    CosmoAuthorizationGruppiFeignClient client =
        SpringApplicationContextHelper.getBean(CosmoAuthorizationGruppiFeignClient.class);

    var resp = client.getGruppoCodice(codice);

    return ObjectUtils.getDataMapper().convertValue(resp.getGruppo(), JsonNode.class);
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
    return "getGruppoFromCodice";
  }

}
