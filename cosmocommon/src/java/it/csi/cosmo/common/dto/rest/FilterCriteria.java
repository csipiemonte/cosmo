/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.dto.rest;


/**
 *
 */

public enum FilterCriteria {

  EQUALS("eq"),
  NOT_EQUALS("ne"),
  LESS_THAN("lt"),
  LESS_THAN_EQUALS("lte"),
  GREATER_THAN("gt"),
  GREATER_THAN_EQUALS("gte"),
  IN("in"),
  NOT_IN("nin"),
  CONTAINS("c"),
  CONTAINS_IGNORE_CASE("ci"),
  NOT_CONTAINS("nc"), 
  NOT_CONTAINS_IGNORE_CASE("nci"),
  STARTS_WITH("s"),
  STARTS_WITH_IGNORE_CASE("si"),
  ENDS_WITH("e"),
  ENDS_WITH_IGNORE_CASE("ei"),
  DEFINED("defined");

  private String codice;

  public String getCodice() {
    return codice;
  }

  private FilterCriteria(String codice) {
    this.codice = codice;
  }

  public static FilterCriteria fromCodice(String codice) {
    for (FilterCriteria candidate : FilterCriteria.values()) {
      if (candidate.getCodice().equals(codice)) {
        return candidate;
      }
    }
    return null;
  }
}
