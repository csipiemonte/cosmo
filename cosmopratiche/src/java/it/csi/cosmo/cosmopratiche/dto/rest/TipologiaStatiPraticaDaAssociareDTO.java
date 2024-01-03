/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import java.io.Serializable;
import java.util.List;

/**
 *
 */

public class TipologiaStatiPraticaDaAssociareDTO implements Serializable {

  private static final long serialVersionUID = 1650629852186968753L;

  private String tipologia;

  private List<String> stati;

  public String getTipologia() {
    return tipologia;
  }

  public void setTipologia(String tipologia) {
    this.tipologia = tipologia;
  }

  public List<String> getStati() {
    return stati;
  }

  public void setStati(List<String> stati) {
    this.stati = stati;
  }

}
