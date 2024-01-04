/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;


/**
 *
 */
@ActaModel(className = "VocePropertiesType")
public abstract class VoceTitolarioActa extends EntitaActa {

  @ActaProperty
  protected String codice;

  @ActaProperty(propertyName = "presenzaFascTemp")
  protected Boolean presenzaFascicoloTemporaneo;

  @ActaProperty
  protected LocalDate dataAggiornamento;

  @ActaProperty(propertyName = "idProvvedimentoAutorizzatList", itemType = Long.class,
      className = "ProvvedimentoAutorizzativoPropertiesType")
  protected List<Long> idProvvedimentiAutorizzativi;

  @ActaProperty(propertyName = "descrBreve")
  protected String descrizioneBreve;

  @ActaProperty(propertyName = "presenzaFascRealeAnnualeNV")
  protected Boolean presenzaFascicoloRealeAnnuale;

  @ActaProperty(propertyName = "insertSottoVociGestCont")
  protected Boolean insertSottoVociGestCont;

  @ActaProperty(propertyName = "presenzaSerieFasc")
  protected Boolean presenzaSerieFascicoli;

  @ActaProperty(propertyName = "conservazioneGenerale")
  protected Integer durataConservazioneGenerale;

  @ActaProperty(propertyName = "presenzaFascRealeLiberoNV")
  protected Boolean presenzaFascicoloRealeLibero;

  @ActaProperty(propertyName = "presenzaFascRealeLegislaturaNV")
  protected Boolean presenzaFascicoloRealeLegislatura;

  @ActaProperty(propertyName = "presenzaFascRealeEreditatoNV")
  protected Boolean presenzaFascRealeEreditatoNV;

  @ActaProperty
  protected String indiceClassificazioneEstesa;

  @ActaProperty
  protected String paroleChiave;

  @ActaProperty(propertyName = "presenzaSerieDoss")
  protected Boolean presenzaSerieDoss;

  @ActaProperty(propertyName = "presenzaSerieDoc")
  protected Boolean presenzaSerieDoc;

  @ActaProperty(propertyName = "presenzaFascStand")
  protected Boolean presenzaFascStand;

  @ActaProperty(propertyName = "presenzaFascRealeContinuoNV")
  protected Boolean presenzaFascicoloRealeContinuo;

  @ActaProperty
  protected String descrizione;

  @ActaProperty
  protected LocalDate dataInizio;

  @ActaProperty
  protected String stato;

  @ActaProperty
  protected LocalDate dataFine;

  @ActaProperty(propertyName = "conservazioneCorrente")
  protected Integer durataConservazioneCorrente;

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public Boolean getPresenzaFascicoloTemporaneo() {
    return presenzaFascicoloTemporaneo;
  }

  public void setPresenzaFascicoloTemporaneo(Boolean presenzaFascicoloTemporaneo) {
    this.presenzaFascicoloTemporaneo = presenzaFascicoloTemporaneo;
  }

  public LocalDate getDataAggiornamento() {
    return dataAggiornamento;
  }

  public void setDataAggiornamento(LocalDate dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  public List<Long> getIdProvvedimentiAutorizzativi() {
    return idProvvedimentiAutorizzativi;
  }

  public void setIdProvvedimentiAutorizzativi(List<Long> idProvvedimentiAutorizzativi) {
    this.idProvvedimentiAutorizzativi = idProvvedimentiAutorizzativi;
  }

  public String getDescrizioneBreve() {
    return descrizioneBreve;
  }

  public void setDescrizioneBreve(String descrizioneBreve) {
    this.descrizioneBreve = descrizioneBreve;
  }

  public Boolean getPresenzaFascicoloRealeAnnuale() {
    return presenzaFascicoloRealeAnnuale;
  }

  public void setPresenzaFascicoloRealeAnnuale(Boolean presenzaFascicoloRealeAnnuale) {
    this.presenzaFascicoloRealeAnnuale = presenzaFascicoloRealeAnnuale;
  }

  public Boolean getInsertSottoVociGestCont() {
    return insertSottoVociGestCont;
  }

  public void setInsertSottoVociGestCont(Boolean insertSottoVociGestCont) {
    this.insertSottoVociGestCont = insertSottoVociGestCont;
  }

  public Boolean getPresenzaSerieFascicoli() {
    return presenzaSerieFascicoli;
  }

  public void setPresenzaSerieFascicoli(Boolean presenzaSerieFascicoli) {
    this.presenzaSerieFascicoli = presenzaSerieFascicoli;
  }

  public Integer getDurataConservazioneGenerale() {
    return durataConservazioneGenerale;
  }

  public void setDurataConservazioneGenerale(Integer durataConservazioneGenerale) {
    this.durataConservazioneGenerale = durataConservazioneGenerale;
  }

  public Boolean getPresenzaFascicoloRealeLibero() {
    return presenzaFascicoloRealeLibero;
  }

  public void setPresenzaFascicoloRealeLibero(Boolean presenzaFascicoloRealeLibero) {
    this.presenzaFascicoloRealeLibero = presenzaFascicoloRealeLibero;
  }

  public Boolean getPresenzaFascicoloRealeLegislatura() {
    return presenzaFascicoloRealeLegislatura;
  }

  public void setPresenzaFascicoloRealeLegislatura(Boolean presenzaFascicoloRealeLegislatura) {
    this.presenzaFascicoloRealeLegislatura = presenzaFascicoloRealeLegislatura;
  }

  public String getIndiceClassificazioneEstesa() {
    return indiceClassificazioneEstesa;
  }

  public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
    this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
  }

  public Boolean getPresenzaSerieDoss() {
    return presenzaSerieDoss;
  }

  public void setPresenzaSerieDoss(Boolean presenzaSerieDoss) {
    this.presenzaSerieDoss = presenzaSerieDoss;
  }

  public Boolean getPresenzaFascicoloRealeContinuo() {
    return presenzaFascicoloRealeContinuo;
  }

  public void setPresenzaFascicoloRealeContinuo(Boolean presenzaFascicoloRealeContinuo) {
    this.presenzaFascicoloRealeContinuo = presenzaFascicoloRealeContinuo;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public LocalDate getDataInizio() {
    return dataInizio;
  }

  public void setDataInizio(LocalDate dataInizio) {
    this.dataInizio = dataInizio;
  }

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public LocalDate getDataFine() {
    return dataFine;
  }

  public void setDataFine(LocalDate dataFine) {
    this.dataFine = dataFine;
  }

  public Integer getDurataConservazioneCorrente() {
    return durataConservazioneCorrente;
  }

  public void setDurataConservazioneCorrente(Integer durataConservazioneCorrente) {
    this.durataConservazioneCorrente = durataConservazioneCorrente;
  }

  public Boolean getPresenzaSerieDoc() {
    return presenzaSerieDoc;
  }

  public void setPresenzaSerieDoc(Boolean presenzaSerieDoc) {
    this.presenzaSerieDoc = presenzaSerieDoc;
  }

  public String getParoleChiave() {
    return paroleChiave;
  }

  public void setParoleChiave(String paroleChiave) {
    this.paroleChiave = paroleChiave;
  }

  public Boolean getPresenzaFascStand() {
    return presenzaFascStand;
  }

  public void setPresenzaFascStand(Boolean presenzaFascStand) {
    this.presenzaFascStand = presenzaFascStand;
  }

  public Boolean getPresenzaFascRealeEreditatoNV() {
    return presenzaFascRealeEreditatoNV;
  }

  public void setPresenzaFascRealeEreditatoNV(Boolean presenzaFascRealeEreditatoNV) {
    this.presenzaFascRealeEreditatoNV = presenzaFascRealeEreditatoNV;
  }

}
