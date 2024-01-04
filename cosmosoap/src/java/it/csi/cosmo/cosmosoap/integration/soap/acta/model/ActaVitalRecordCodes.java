/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model;

/**
 *
 */

public enum ActaVitalRecordCodes {

  BASSO("BASSO"), MEDIO("MEDIO"), ALTO("ALTO");

  private String descrizione;

  private ActaVitalRecordCodes(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public static ActaVitalRecordCodes fromDescription(String d) {
    for (ActaVitalRecordCodes candidate : ActaVitalRecordCodes.values()) {
      if (candidate.getDescrizione().equals(d)) {
        return candidate;
      }
    }
    return null;
  }

}
