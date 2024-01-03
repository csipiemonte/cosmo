/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

/**
 *
 */

public class VariabileProcessoDTO {

  private String nomeVariabile;
  private String alberaturaJson;
  private ValoreVariabile singolo;
  private ValoreVariabile rangeDa;
  private ValoreVariabile rangeA;

  public String getNomeVariabile() {
    return nomeVariabile;
  }

  public void setNomeVariabile(String nomeVariabile) {
    this.nomeVariabile = nomeVariabile;
  }

  public ValoreVariabile getSingolo() {
    return singolo;
  }

  public void setSingolo(ValoreVariabile singolo) {
    this.singolo = singolo;
  }

  public ValoreVariabile getRangeDa() {
    return rangeDa;
  }

  public void setRangeDa(ValoreVariabile rangeDa) {
    this.rangeDa = rangeDa;
  }

  public ValoreVariabile getRangeA() {
    return rangeA;
  }

  public void setRangeA(ValoreVariabile rangeA) {
    this.rangeA = rangeA;
  }

  public String getAlberaturaJson() {
    return alberaturaJson;
  }

  public void setAlberaturaJson(String alberaturaJson) {
    this.alberaturaJson = alberaturaJson;
  }
}
