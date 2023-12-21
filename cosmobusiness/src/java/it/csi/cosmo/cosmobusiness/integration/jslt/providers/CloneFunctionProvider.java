/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public class CloneFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {
    try {
      var serialized = input.toString();
      return ObjectUtils.getDataMapper().readTree(serialized);

    } catch (Exception e) {
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
    return "clone";
  }

}
