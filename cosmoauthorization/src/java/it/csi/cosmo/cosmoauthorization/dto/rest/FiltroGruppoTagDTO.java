/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 */

public class FiltroGruppoTagDTO {
  private String[] nomeTag;
  private String nomeGruppo;

  public String[] getNomeTag() {
    return nomeTag;
  }

  public void setNomeTag(String[] nomeTag) {
    this.nomeTag = nomeTag;
  }

  public String getNomeGruppo() {
    return nomeGruppo;
  }

  public void setNomeGruppo(String nomeGruppo) {
    this.nomeGruppo = nomeGruppo;
  }

  @Override
  public String toString() {
    return "FiltroGruppoTagDTO [nomeTag=" + Arrays.toString(nomeTag) + ", nomeGruppo=" + nomeGruppo
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(nomeTag);
    result = prime * result + Objects.hash(nomeGruppo);
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
    FiltroGruppoTagDTO other = (FiltroGruppoTagDTO) obj;
    return Objects.equals(nomeGruppo, other.nomeGruppo) && Arrays.equals(nomeTag, other.nomeTag);
  }



}
