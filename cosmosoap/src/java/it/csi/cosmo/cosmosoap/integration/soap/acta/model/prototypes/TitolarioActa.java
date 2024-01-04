/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.TitolarioActaDefaultImpl;


/**
 *
 */
@ActaModel(className = "TitolarioPropertiesType")
public abstract class TitolarioActa extends EntitaActa {

  public static TitolarioActaDefaultImpl.Builder builder() {
    return TitolarioActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "dataInizio")
  protected LocalDate dataInizio;

  @ActaProperty(propertyName = "dataFine")
  protected LocalDate dataFine;

  @ActaProperty(propertyName = "stato")
  protected String stato;

  @ActaProperty(propertyName = "codice")
  protected String codice;

  @ActaProperty(propertyName = "descrizione")
  protected String descrizione;

  @ActaProperty(propertyName = "idProvvedimentoAutorizzatList")
  protected List<Long> idProvvedimentoAutorizzatList;

  @ActaProperty(propertyName = "numeroMaxLivelli")
  protected Integer numeroMaxLivelli;

  @ActaProperty(propertyName = "insertContenutiInVociConSottovoci")
  protected Boolean insertContenutiInVociConSottovoci;

  @ActaProperty(propertyName = "insertDocInFascConSottofasc")
  protected Boolean insertDocInFascConSottofasc;

  @ActaProperty(propertyName = "insertFuoriVolumi")
  protected Boolean insertFuoriVolumi;

  @ActaProperty(propertyName = "serieMultipleVoce")
  protected Boolean serieMultipleVoce;


  @ActaProperty(propertyName = "creaAutomaPrimaSerie")
  protected Boolean creaAutomaPrimaSerie;


  @ActaProperty(propertyName = "scartoSoloConVolumi")
  protected Boolean scartoSoloConVolumi;

  public LocalDate getDataInizio() {
    return dataInizio;
  }

  public void setDataInizio(LocalDate dataInizio) {
    this.dataInizio = dataInizio;
  }

  public LocalDate getDataFine() {
    return dataFine;
  }

  public void setDataFine(LocalDate dataFine) {
    this.dataFine = dataFine;
  }

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public List<Long> getIdProvvedimentoAutorizzatList() {
    return idProvvedimentoAutorizzatList;
  }

  public void setIdProvvedimentoAutorizzatList(List<Long> idProvvedimentoAutorizzatList) {
    this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
  }

  public Integer getNumeroMaxLivelli() {
    return numeroMaxLivelli;
  }

  public void setNumeroMaxLivelli(Integer numeroMaxLivelli) {
    this.numeroMaxLivelli = numeroMaxLivelli;
  }

  public Boolean getInsertContenutiInVociConSottovoci() {
    return insertContenutiInVociConSottovoci;
  }

  public void setInsertContenutiInVociConSottovoci(Boolean insertContenutiInVociConSottovoci) {
    this.insertContenutiInVociConSottovoci = insertContenutiInVociConSottovoci;
  }

  public Boolean getInsertDocInFascConSottofasc() {
    return insertDocInFascConSottofasc;
  }

  public void setInsertDocInFascConSottofasc(Boolean insertDocInFascConSottofasc) {
    this.insertDocInFascConSottofasc = insertDocInFascConSottofasc;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Boolean getInsertFuoriVolumi() {
    return insertFuoriVolumi;
  }

  public void setInsertFuoriVolumi(Boolean insertFuoriVolumi) {
    this.insertFuoriVolumi = insertFuoriVolumi;
  }

  public Boolean getSerieMultipleVoce() {
    return serieMultipleVoce;
  }

  public void setSerieMultipleVoce(Boolean serieMultipleVoce) {
    this.serieMultipleVoce = serieMultipleVoce;
  }

  public Boolean getCreaAutomaPrimaSerie() {
    return creaAutomaPrimaSerie;
  }

  public void setCreaAutomaPrimaSerie(Boolean creaAutomaPrimaSerie) {
    this.creaAutomaPrimaSerie = creaAutomaPrimaSerie;
  }

  public Boolean getScartoSoloConVolumi() {
    return scartoSoloConVolumi;
  }

  public void setScartoSoloConVolumi(Boolean scartoSoloConVolumi) {
    this.scartoSoloConVolumi = scartoSoloConVolumi;
  }

}
