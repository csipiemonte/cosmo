/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities.enums;

import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */

public enum InfoSmistamento {

  INDICE_CLASS_ESTESO("Indice_class_esteso"),
  DATA_REG_PROTOCOLLO("Data_reg_protocollo"),
  NUMERO_REG_PROTOCOLLO("Numero_reg_protocollo");

  private String codice;

  private InfoSmistamento(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static InfoSmistamento fromCode(String code) {
    if (StringUtils.isBlank(code)) {
      return null;
    }
    for (InfoSmistamento candidate : InfoSmistamento.values()) {
      if (candidate.codice.equals(code)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + InfoSmistamento.class.getSimpleName() + " [" + code + "]");
  }
}
