/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import it.csi.cosmo.common.dto.search.BooleanFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaCustomFormDTO
implements RecursiveFilterSpecification<FiltroRicercaCustomFormDTO> {

  private StringFilter codice;
  private StringFilter descrizione;
  private StringFilter fullText;
  private BooleanFilter senzaAssociazioneConTipoPratica;

  private FiltroRicercaCustomFormDTO[] and;
  private FiltroRicercaCustomFormDTO[] or;

  @Override
  public FiltroRicercaCustomFormDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaCustomFormDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaCustomFormDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaCustomFormDTO[] or) {
    this.or = or;
  }

  public StringFilter getCodice() {
    return codice;
  }

  public void setCodice(StringFilter codice) {
    this.codice = codice;
  }

  public StringFilter getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(StringFilter descrizione) {
    this.descrizione = descrizione;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  public BooleanFilter getSenzaAssociazioneConTipoPratica() {
    return senzaAssociazioneConTipoPratica;
  }

  public void setSenzaAssociazioneConTipoPratica(BooleanFilter senzaAssociazioneConTipoPratica) {
    this.senzaAssociazioneConTipoPratica = senzaAssociazioneConTipoPratica;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codice == null) ? 0 : codice.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
    result = prime * result + ((senzaAssociazioneConTipoPratica == null) ? 0
        : senzaAssociazioneConTipoPratica.hashCode());
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
    FiltroRicercaCustomFormDTO other = (FiltroRicercaCustomFormDTO) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    if (descrizione == null) {
      if (other.descrizione != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    if (fullText == null) {
      if (other.fullText != null)
        return false;
    } else if (!fullText.equals(other.fullText))
      return false;
    if (senzaAssociazioneConTipoPratica == null) {
      if (other.senzaAssociazioneConTipoPratica != null)
        return false;
    } else if (!senzaAssociazioneConTipoPratica.equals(other.senzaAssociazioneConTipoPratica))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaCustomFormDTO [codice=" + codice + ", descrizione="
        + descrizione + ", fullText=" + fullText + ", senzaAssociazioneConTipoPratica="
        + senzaAssociazioneConTipoPratica + "]";
  }

}
