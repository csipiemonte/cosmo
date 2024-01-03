/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.DoubleFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class ValoreVariabile {


  private StringFilter variabileString;
  private DoubleFilter variabileNumerica;
  private DateFilter variabileData;
  private BooleanFilter variabileBoolean;

  public StringFilter getVariabileString() {
    return variabileString;
  }

  public void setVariabileString(StringFilter variabileString) {
    this.variabileString = variabileString;
  }

  public DoubleFilter getVariabileNumerica() {
    return variabileNumerica;
  }

  public void setVariabileNumerica(DoubleFilter variabileNumerica) {
    this.variabileNumerica = variabileNumerica;
  }

  public DateFilter getVariabileData() {
    return variabileData;
  }

  public void setVariabileData(DateFilter variabileData) {
    this.variabileData = variabileData;
  }

  public BooleanFilter getVariabileBoolean() {
    return variabileBoolean;
  }

  public void setVariabileBoolean(BooleanFilter variabileBoolean) {
    this.variabileBoolean = variabileBoolean;
  }

}
