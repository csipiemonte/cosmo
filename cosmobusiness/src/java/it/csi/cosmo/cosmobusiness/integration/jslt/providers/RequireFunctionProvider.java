/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.jslt.providers;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Function;
import it.csi.cosmo.common.exception.BadRequestException;

/**
 *
 */

public class RequireFunctionProvider implements Function {

  @Override
  public JsonNode call(JsonNode input, JsonNode[] arguments) {

    String fieldName = arguments[1].textValue();

    String msg = "Parametro \"" + fieldName + "\" necessario ma non fornito.";
    if (arguments.length >= 3) {
      msg = arguments[2].textValue();
    }

    var field = arguments[0] != null && !arguments[0].isNull() && arguments[0].isObject()
        && arguments[0].has(fieldName)
        ? arguments[0].get(fieldName)
        : null;

    if (field == null || field.isNull()
        || (field.isTextual() && StringUtils.isBlank(field.textValue()))) {
      throw new BadRequestException(msg);
    }

    return field;
  }

  @Override
  public int getMaxArguments() {
    return 3;
  }

  @Override
  public int getMinArguments() {
    return 2;
  }

  @Override
  public String getName() {
    return "require";
  }

}
