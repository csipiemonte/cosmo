/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.List;
import java.util.Objects;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaDocumentiInvioStiloDTO {

  private LongFilter idPratica;
  private LongFilter idUd;
  private StringFilter tipologiaDocumento;
  private List<CodiceTipologiaDocumento> codici;

  public LongFilter getIdPratica() {
    return idPratica;
  }

  public void setIdPratica(LongFilter idPratica) {
    this.idPratica = idPratica;
  }

  public LongFilter getIdUd() {
    return idUd;
  }

  public void setIdUd(LongFilter idUd) {
    this.idUd = idUd;
  }

  public StringFilter getTipologiaDocumento() {
    return tipologiaDocumento;
  }

  public void setTipologiaDocumento(StringFilter tipologiaDocumento) {
    this.tipologiaDocumento = tipologiaDocumento;
  }

  public List<CodiceTipologiaDocumento> getCodici() {
    return codici;
  }

  public void setCodici(List<CodiceTipologiaDocumento> codici) {
    this.codici = codici;
  }

  @Override
  public int hashCode() {
    return Objects.hash(idPratica, idUd, tipologiaDocumento);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FiltroRicercaDocumentiInvioStiloDTO other = (FiltroRicercaDocumentiInvioStiloDTO) obj;
    return Objects.equals(idPratica, other.idPratica) && Objects.equals(idUd, other.idUd)
        && Objects.equals(tipologiaDocumento, other.tipologiaDocumento);
  }

  @Override
  public String toString() {
    return "FiltroRicercaDocumentiInvioStiloDTO [idPratica=" + idPratica + ", idUd=" + idUd
        + ", tipologiaDocumento=" + tipologiaDocumento + "]";
  }



}
