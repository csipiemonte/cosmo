/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmo.dto;

import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AmbienteLogoutEnum {

  INTRANET("intranet"),
  INTRACOM("intracom"),
  INTERNET("internet"), 
  DEFAULT("default");

  private String value;

  AmbienteLogoutEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static AmbienteLogoutEnum fromValue(String value) {
    if (StringUtils.isBlank(value)) {
      return null;
    }
    for (AmbienteLogoutEnum candidate : AmbienteLogoutEnum.values()) {
      if (candidate.value.equals(value)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + AmbienteLogoutEnum.class.getSimpleName() + " [" + value + "]");
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }
}
