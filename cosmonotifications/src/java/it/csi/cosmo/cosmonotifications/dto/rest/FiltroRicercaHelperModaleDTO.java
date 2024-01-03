/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.dto.rest;

import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaHelperModaleDTO implements RecursiveFilterSpecification<FiltroRicercaHelperModaleDTO>{
  
  private StringFilter codicePagina;
  private StringFilter codiceTab;
  
  private FiltroRicercaHelperModaleDTO[] and;
  private FiltroRicercaHelperModaleDTO[] or;

  public void setAnd(FiltroRicercaHelperModaleDTO[] and) {
    this.and = and;
  }

  public void setOr(FiltroRicercaHelperModaleDTO[] or) {
    this.or = or;
  }

  public StringFilter getCodicePagina() {
    return codicePagina;
  }

  public void setCodicePagina(StringFilter codicePagina) {
    this.codicePagina = codicePagina;
  }

  public StringFilter getCodiceTab() {
    return codiceTab;
  }

  public void setCodiceTab(StringFilter codiceTab) {
    this.codiceTab = codiceTab;
  }

  @Override
  public FiltroRicercaHelperModaleDTO[] getAnd() {
    return and;
  }

  @Override
  public FiltroRicercaHelperModaleDTO[] getOr() {
    return or;
  }
}
