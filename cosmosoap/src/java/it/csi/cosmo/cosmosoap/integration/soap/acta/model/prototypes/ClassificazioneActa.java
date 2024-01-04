/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ClassificazioneActaDefaultImpl;


/**
 *
 */
public abstract class ClassificazioneActa extends EntitaActa {

  public static ClassificazioneActaDefaultImpl.Builder builder() {
    return ClassificazioneActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "dataInizio")
  protected LocalDate dataInizio;

  @ActaProperty(propertyName = "dataFine")
  protected LocalDate dataFine;

  @ActaProperty(propertyName = "legislatura")
  protected String legislatura;

  @ActaProperty(propertyName = "numero")
  protected String numero;

  @ActaProperty(propertyName = "stato")
  protected String stato;

  @ActaProperty(propertyName = "codice")
  protected String codice;

  @ActaProperty(propertyName = "cartaceo")
  protected Boolean cartaceo;

  @ActaProperty(propertyName = "docAllegato")
  protected Boolean docAllegato;

  @ActaProperty(propertyName = "docConAllegati")
  protected Boolean docConAllegati;

  @ActaProperty(propertyName = "copiaCartacea")
  protected Boolean copiaCartacea;

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

  public String getLegislatura() {
    return legislatura;
  }

  public void setLegislatura(String legislatura) {
    this.legislatura = legislatura;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
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

  public Boolean getCartaceo() {
    return cartaceo;
  }

  public void setCartaceo(Boolean cartaceo) {
    this.cartaceo = cartaceo;
  }

  public Boolean getDocAllegato() {
    return docAllegato;
  }

  public void setDocAllegato(Boolean docAllegato) {
    this.docAllegato = docAllegato;
  }

  public Boolean getDocConAllegati() {
    return docConAllegati;
  }

  public void setDocConAllegati(Boolean docConAllegati) {
    this.docConAllegati = docConAllegati;
  }

  public Boolean getCopiaCartacea() {
    return copiaCartacea;
  }

  public void setCopiaCartacea(Boolean copiaCartacea) {
    this.copiaCartacea = copiaCartacea;
  }

}
