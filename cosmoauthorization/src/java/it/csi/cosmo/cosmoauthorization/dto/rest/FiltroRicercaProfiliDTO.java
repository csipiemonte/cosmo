/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaProfiliDTO {

  private BooleanFilter assegnabile;
  private StringFilter codice;

  public BooleanFilter getAssegnabile() {
    return assegnabile;
  }

  public void setAssegnabile(BooleanFilter assegnabile) {
    this.assegnabile = assegnabile;
  }

  public StringFilter getCodice() {
    return codice;
  }

  public void setCodice(StringFilter codice) {
    this.codice = codice;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((assegnabile == null) ? 0 : assegnabile.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FiltroRicercaProfiliDTO other = (FiltroRicercaProfiliDTO) obj;
    if (assegnabile == null) {
      if (other.assegnabile != null)
        return false;
    } else if (!assegnabile.equals(other.assegnabile))
      return false;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaProfiliDTO [assegnabile=" + assegnabile + ", codice=" + codice + "]";
  }

}
