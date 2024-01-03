/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaVariabiliProcessoDTO {

  private StringFilter labelFiltro;
  private StringFilter nomeFiltro;
  private StringFilter nomeEnte;
  private StringFilter descrizioneTipoPratica;




  public StringFilter getLabelFiltro() {
    return labelFiltro;
  }



  public void setLabelFiltro(StringFilter labelFiltro) {
    this.labelFiltro = labelFiltro;
  }



  public StringFilter getNomeEnte() {
    return nomeEnte;
  }



  public void setNomeEnte(StringFilter nomeEnte) {
    this.nomeEnte = nomeEnte;
  }



  public StringFilter getDescrizioneTipoPratica() {
    return descrizioneTipoPratica;
  }



  public void setDescrizioneTipoPratica(StringFilter descrizioneTipoPratica) {
    this.descrizioneTipoPratica = descrizioneTipoPratica;
  }


  public StringFilter getNomeFiltro() {
    return nomeFiltro;
  }



  public void setNomeFiltro(StringFilter nomeFiltro) {
    this.nomeFiltro = nomeFiltro;
  }



  @Override
  public String toString() {
    return "FiltroRicercaVariabiliProcessoDTO [labelFiltro=" + labelFiltro + ", nomeFiltro="
        + nomeFiltro + ", nomeEnte=" + nomeEnte + ", descrizioneTipoPratica="
        + descrizioneTipoPratica + "]";
  }








}
