/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class JsonFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {
    try {
      return ObjectUtils.getDataMapper().readTree(arguments[0].asText());
    } catch (IOException e) {
      throw new InternalServerException("Invalid or unreadable JSON: " + arguments[0], e);
    }
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
    return "parseJson";
  }

}
