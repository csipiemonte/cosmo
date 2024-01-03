/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.dto.rest;

import java.util.List;
import it.csi.cosmo.common.dto.search.DateFilter;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaPraticheDTO {

  private GroupsDTO groups;
  private LongFilter id;
  private StringFilter oggetto;
  private StringFilter riassunto;
  private StringFilter stato;
  private StringFilter tipologia;
  private DateFilter dataUltimaModifica;
  private LongFilter daAssociareA;
  private List<TipologiaStatiPraticaDaAssociareDTO> tipologieStatiDaAssociare;
  private Boolean esecuzioneMultipla;
  private String taskMassivo;
  private boolean tuttePratiche;
  private List<VariabileProcessoDTO> variabiliProcesso;

  public boolean isTuttePratiche() {
    return tuttePratiche;
  }

  public void setTuttePratiche(boolean tuttePratiche) {
    this.tuttePratiche = tuttePratiche;
  }

  @Deprecated
  private DateFilter dataUltimaModificaDa;

  @Deprecated
  private DateFilter dataUltimaModificaA;

  private DateFilter dataAperturaPratica;

  @Deprecated
  private DateFilter dataAperturaPraticaDa;

  @Deprecated
  private DateFilter dataAperturaPraticaA;

  private DateFilter dataUltimoCambioStato;

  @Deprecated
  private DateFilter dataUltimoCambioStatoDa;

  @Deprecated
  private DateFilter dataUltimoCambioStatoA;

  public StringFilter getRiassunto() {
    return riassunto;
  }

  public void setRiassunto(StringFilter riassunto) {
    this.riassunto = riassunto;
  }

  public String getTaskMassivo() {
    return taskMassivo;
  }

  public void setTaskMassivo(String taskMassivo) {
    this.taskMassivo = taskMassivo;
  }

  public Boolean getEsecuzioneMultipla() {
    return esecuzioneMultipla;
  }

  public void setEsecuzioneMultipla(Boolean esecuzioneMultipla) {
    this.esecuzioneMultipla = esecuzioneMultipla;
  }

  public GroupsDTO getGroups() {
    return groups;
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public void setGroups(GroupsDTO groups) {
    this.groups = groups;
  }

  public StringFilter getOggetto() {
    return oggetto;
  }

  public void setOggetto(StringFilter oggetto) {
    this.oggetto = oggetto;
  }

  public StringFilter getStato() {
    return stato;
  }

  public void setStato(StringFilter stato) {
    this.stato = stato;
  }

  public StringFilter getTipologia() {
    return tipologia;
  }

  public void setTipologia(StringFilter tipologia) {
    this.tipologia = tipologia;
  }

  public DateFilter getDataUltimaModifica() {
    return dataUltimaModifica;
  }

  public void setDataUltimaModifica(DateFilter dataUltimaModifica) {
    this.dataUltimaModifica = dataUltimaModifica;
  }

  public DateFilter getDataAperturaPratica() {
    return dataAperturaPratica;
  }

  public void setDataAperturaPratica(DateFilter dataAperturaPratica) {
    this.dataAperturaPratica = dataAperturaPratica;
  }

  public DateFilter getDataUltimoCambioStato() {
    return dataUltimoCambioStato;
  }

  public void setDataUltimoCambioStato(DateFilter dataUltimoCambioStato) {
    this.dataUltimoCambioStato = dataUltimoCambioStato;
  }

  public LongFilter getDaAssociareA() {
    return daAssociareA;
  }

  public void setDaAssociareA(LongFilter daAssociareA) {
    this.daAssociareA = daAssociareA;
  }

  public List<TipologiaStatiPraticaDaAssociareDTO> getTipologieStatiDaAssociare() {
    return tipologieStatiDaAssociare;
  }

  public void setTipologieStatiDaAssociare(
      List<TipologiaStatiPraticaDaAssociareDTO> tipologieStatiDaAssociare) {
    this.tipologieStatiDaAssociare = tipologieStatiDaAssociare;
  }

  public List<VariabileProcessoDTO> getVariabiliProcesso() {
    return variabiliProcesso;
  }

  public void setVariabiliProcesso(List<VariabileProcessoDTO> variabiliProcesso) {
    this.variabiliProcesso = variabiliProcesso;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataUltimaModificaDa() {
    return dataUltimaModificaDa;
  }

  @Deprecated(forRemoval = true)
  public void setDataUltimaModificaDa(DateFilter dataUltimaModificaDa) {
    this.dataUltimaModificaDa = dataUltimaModificaDa;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataUltimaModificaA() {
    return dataUltimaModificaA;
  }

  @Deprecated(forRemoval = true)
  public void setDataUltimaModificaA(DateFilter dataUltimaModificaA) {
    this.dataUltimaModificaA = dataUltimaModificaA;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataAperturaPraticaDa() {
    return dataAperturaPraticaDa;
  }

  @Deprecated(forRemoval = true)
  public void setDataAperturaPraticaDa(DateFilter dataAperturaPraticaDa) {
    this.dataAperturaPraticaDa = dataAperturaPraticaDa;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataAperturaPraticaA() {
    return dataAperturaPraticaA;
  }

  @Deprecated(forRemoval = true)
  public void setDataAperturaPraticaA(DateFilter dataAperturaPraticaA) {
    this.dataAperturaPraticaA = dataAperturaPraticaA;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataUltimoCambioStatoDa() {
    return dataUltimoCambioStatoDa;
  }

  @Deprecated(forRemoval = true)
  public void setDataUltimoCambioStatoDa(DateFilter dataUltimoCambioStatoDa) {
    this.dataUltimoCambioStatoDa = dataUltimoCambioStatoDa;
  }

  @Deprecated(forRemoval = true)
  public DateFilter getDataUltimoCambioStatoA() {
    return dataUltimoCambioStatoA;
  }

  @Deprecated(forRemoval = true)
  public void setDataUltimoCambioStatoA(DateFilter dataUltimoCambioStatoA) {
    this.dataUltimoCambioStatoA = dataUltimoCambioStatoA;
  }

  @Override
  public String toString() {
    return "FiltroRicercaPraticheDTO [oggetto=" + oggetto + ", stato=" + stato + ", tipologia="
        + tipologia + ", daAssociareA=" + daAssociareA + ", dataUltimaModifica="
        + dataUltimaModifica + ", dataUltimaModificaDa=" + dataUltimaModificaDa
        + ", dataUltimaModificaA=" + dataUltimaModificaA + ", dataAperturaPratica="
        + dataAperturaPratica + ", dataAperturaPraticaDa=" + dataAperturaPraticaDa
        + ", dataAperturaPraticaA=" + dataAperturaPraticaA + ", dataUltimoCambioStato="
        + dataUltimoCambioStato + ", dataUltimoCambioStatoDa=" + dataUltimoCambioStatoDa
        + ", dataUltimoCambioStatoA=" + dataUltimoCambioStatoA + "]";
  }

}
