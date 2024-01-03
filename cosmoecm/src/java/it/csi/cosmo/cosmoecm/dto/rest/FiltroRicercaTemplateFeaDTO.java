/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.List;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaTemplateFeaDTO
implements RecursiveFilterSpecification<FiltroRicercaTemplateFeaDTO> {

  private StringFilter descrizione;
  private LongFilter idEnte;
  private LongFilter idPratica;
  private StringFilter codiceTipoPratica;
  private StringFilter codiceTipoDocumento;
  private List<String> codiciTipoDocumento;
  private FiltroRicercaTemplateFeaDTO[] and;
  private FiltroRicercaTemplateFeaDTO[] or;

  public StringFilter getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(StringFilter descrizione) {
    this.descrizione = descrizione;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  public StringFilter getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(StringFilter codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  public StringFilter getCodiceTipoDocumento() {
    return codiceTipoDocumento;
  }

  public void setCodiceTipoDocumento(StringFilter codiceTipoDocumento) {
    this.codiceTipoDocumento = codiceTipoDocumento;
  }

  @Override
  public FiltroRicercaTemplateFeaDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaTemplateFeaDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaTemplateFeaDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaTemplateFeaDTO[] or) {
    this.or = or;
  }

  public LongFilter getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(LongFilter idPratica) {
    this.idPratica = idPratica;
  }

  public List<String> getCodiciTipoDocumento() {
    return codiciTipoDocumento;
  }

  public void setCodiciTipoDocumento(List<String> codiciTipoDocumento) {
    this.codiciTipoDocumento = codiciTipoDocumento;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((idEnte == null) ? 0 : idEnte.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
    result = prime * result + ((codiceTipoDocumento == null) ? 0 : codiceTipoDocumento.hashCode());
    result = prime * result + ((idPratica == null) ? 0 : idPratica.hashCode());
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
    FiltroRicercaTemplateFeaDTO other = (FiltroRicercaTemplateFeaDTO) obj;
    if (idEnte == null) {
      if (other.idEnte != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    }
    if (idPratica == null) {
      if (other.idPratica != null)
        return false;
    } else if (!codiceTipoDocumento.equals(other.codiceTipoDocumento))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaEntiDTO [idEnte=" + idEnte + ", descrizione=" + descrizione
        + ", codiceTipoPratica=" + codiceTipoPratica + ", codiceTipoDocumento="
        + codiceTipoDocumento + ", idPratica=" + idPratica + "]";
  }



}
