/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.util;

import java.security.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */

public enum FlowableVariableType {

  BOOLEANTYPE("boolean"),
  BYTEARRAYTYPE("bytes"),
  DATETYPE("date"), DOUBLETYPE(
      "double"),
  JPAENTITYLISTVARIABLETYPE("jpa-entity-list"), JPAENTITYVARIABLETYPE("jpa-entity"), INSTANTTYPE(
      "instant"), INTEGERTYPE("integer"), JODADATETIMETYPE("jodadatetime"), JODADATETYPE(
          "jodadate"), JSONTYPE("json"), LOCALDATETIMETYPE("localdatetime"), LOCALDATETYPE(
              "localdate"), LONGJSONTYPE(
                  "longJson"), LONGSTRINGTYPE("longString"), LONGTYPE("long"), NULLTYPE(
                      "null"), SERIALIZABLETYPE(
                          "serializable"), SHORTTYPE("short"), STRINGTYPE("string"), UUIDTYPE("uuid");

  private String codice;


  private FlowableVariableType(String codice) {
    this.codice = codice;
  }

  public String getCodice() {
    return codice;
  }

  public static FlowableVariableType fromCodice(String codice) {
    if (StringUtils.isBlank(codice)) {
      return null;
    }
    for (FlowableVariableType candidate : FlowableVariableType.values()) {
      if (candidate.codice.equals(codice)) {
        return candidate;
      }
    }
    throw new InvalidParameterException(
        "Unknown " + FlowableVariableType.class.getSimpleName() + " [" + codice + "]");
  }


}
