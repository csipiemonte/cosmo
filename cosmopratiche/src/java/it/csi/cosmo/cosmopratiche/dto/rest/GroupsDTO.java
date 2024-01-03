/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import java.io.Serializable;
import java.util.Map;

/**
 *
 */

public class GroupsDTO implements Serializable {

  private static final long serialVersionUID = 1650629852186968753L;

  private Map<String, String> tutte;

  private Map<String, String> preferite;

  private Map<String, String> inEvidenza;

  private Map<String, String> daLavorare;

  public Map<String, String> getTutte() {
    return tutte;
  }

  public void setTutte(Map<String, String> tutte) {
    this.tutte = tutte;
  }

  public Map<String, String> getPreferite() {
    return preferite;
  }

  public void setPreferite(Map<String, String> preferite) {
    this.preferite = preferite;
  }

  public Map<String, String> getInEvidenza() {
    return inEvidenza;
  }

  public void setInEvidenza(Map<String, String> inEvidenza) {
    this.inEvidenza = inEvidenza;
  }

  public Map<String, String> getDaLavorare() {
    return daLavorare;
  }

  public void setDaLavorare(Map<String, String> daLavorare) {
    this.daLavorare = daLavorare;
  }

}
