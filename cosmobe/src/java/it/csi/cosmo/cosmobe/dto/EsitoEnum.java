/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.dto;

import com.fasterxml.jackson.annotation.JsonValue;

  public enum EsitoEnum {
    CON_VALIDAZIONE("File validato e salvato. In attesa di essere processato"),

    SENZA_VALIDAZIONE("File salvato. La validazione ed il processamento avverranno in seguito");
    private String value;

    EsitoEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
}

