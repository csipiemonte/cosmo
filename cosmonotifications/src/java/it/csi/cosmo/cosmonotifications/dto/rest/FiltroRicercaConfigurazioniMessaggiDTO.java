/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaConfigurazioniMessaggiDTO {

  private LongFilter id;
  private LongFilter idEnte;
  private StringFilter codiceTipoMessaggio;
  private StringFilter codiceTipoPratica;
  private StringFilter fullText;

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  public StringFilter getCodiceTipoMessaggio() {
    return codiceTipoMessaggio;
  }

  public void setCodiceTipoMessaggio(StringFilter codiceTipoMessaggio) {
    this.codiceTipoMessaggio = codiceTipoMessaggio;
  }

  public StringFilter getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(StringFilter codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codiceTipoMessaggio == null) ? 0 : codiceTipoMessaggio.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    FiltroRicercaConfigurazioniMessaggiDTO other = (FiltroRicercaConfigurazioniMessaggiDTO) obj;
    if (codiceTipoMessaggio == null) {
      if (other.codiceTipoMessaggio != null) {
        return false;
      }
    } else if (!codiceTipoMessaggio.equals(other.codiceTipoMessaggio)) {
      return false;
    }
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null) {
        return false;
      }
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica)) {
      return false;
    }
    if (fullText == null) {
      if (other.fullText != null) {
        return false;
      }
    } else if (!fullText.equals(other.fullText)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (idEnte == null) {
      if (other.idEnte != null) {
        return false;
      }
    } else if (!idEnte.equals(other.idEnte)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaConfigurazioniMessaggiDTO [" + (id != null ? "id=" + id + ", " : "")
        + (idEnte != null ? "idEnte=" + idEnte + ", " : "")
        + (codiceTipoMessaggio != null ? "codiceTipoMessaggio=" + codiceTipoMessaggio + ", " : "")
        + (codiceTipoPratica != null ? "codiceTipoPratica=" + codiceTipoPratica + ", " : "")
        + (fullText != null ? "fullText=" + fullText : "") + "]";
  }



}
