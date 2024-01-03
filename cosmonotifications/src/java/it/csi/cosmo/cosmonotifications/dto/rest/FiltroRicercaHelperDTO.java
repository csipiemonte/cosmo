/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.dto.rest;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaHelperDTO
    implements RecursiveFilterSpecification<FiltroRicercaHelperDTO> {

  private LongFilter id;
  private StringFilter codicePagina;
  private StringFilter codiceTab;
  private StringFilter codiceForm;
  private StringFilter codiceModale;

  private FiltroRicercaHelperDTO[] and;
  private FiltroRicercaHelperDTO[] or;

  @Override
  public FiltroRicercaHelperDTO[] getAnd() {
    return and;
  }

  public void setAnd(FiltroRicercaHelperDTO[] and) {
    this.and = and;
  }

  @Override
  public FiltroRicercaHelperDTO[] getOr() {
    return or;
  }

  public void setOr(FiltroRicercaHelperDTO[] or) {
    this.or = or;
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getCodicePagina() {
    return codicePagina;
  }

  public void setCodicePagina(StringFilter codicePagina) {
    this.codicePagina = codicePagina;
  }
  
  public StringFilter getCodiceModale() {
    return codiceModale;
  }

  public void setCodiceModale(StringFilter codiceModale) {
    this.codiceModale = codiceModale;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codicePagina == null) ? 0 : codicePagina.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    FiltroRicercaHelperDTO other = (FiltroRicercaHelperDTO) obj;
    if (codicePagina == null) {
      if (other.codicePagina != null)
        return false;
    } else if (!codicePagina.equals(other.codicePagina))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "FiltroRicercaCustomFormDTO [id=" + id + ", codicePagina=" + codicePagina + "]";
  }

  public StringFilter getCodiceTab() {
    return codiceTab;
  }

  public void setCodiceTab(StringFilter codiceTab) {
    this.codiceTab = codiceTab;
  }

  public StringFilter getCodiceForm() {
    return codiceForm;
  }

  public void setCodiceForm(StringFilter codiceForm) {
    this.codiceForm = codiceForm;
  }
}
