/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.RegistrazioneClassificazioniActaDefaultImpl;


/**
 *
 */
public abstract class RegistrazioneClassificazioniActa extends EntitaActa {

  public static RegistrazioneClassificazioniActaDefaultImpl.Builder builder() {
    return RegistrazioneClassificazioniActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "dataProtocollo")
  protected LocalDate dataProtocollo;

  @ActaProperty(propertyName = "stato")
  protected String stato;

  @ActaProperty(propertyName = "objectIdRegistrazione")
  protected String objectIdRegistrazione;

  @ActaProperty(propertyName = "annoProtocolloMittente")
  protected Long annoProtocolloMittente;

  @ActaProperty(propertyName = "idAooResponsabile")
  protected String idAooResponsabile;

  @ActaProperty(propertyName = "objectIdAooResponsabile")
  protected String objectIdAooResponsabile;

  @ActaProperty(propertyName = "dataProtocolloMittente")
  protected String dataProtocolloMittente;

  @ActaProperty(propertyName = "idAooProtocollante")
  protected String idAooProtocollante;

  @ActaProperty(propertyName = "objectIdAooProtocollante")
  protected String objectIdAooProtocollante;

  @ActaProperty(propertyName = "codiceProtocolloMittente")
  protected String codiceProtocolloMittente;

  @ActaProperty(propertyName = "oggetto")
  protected String oggetto;

  @ActaProperty(propertyName = "idRegistroProtocollo")
  protected String idRegistroProtocollo;

  @ActaProperty(propertyName = "codice")
  protected String codice;

  @ActaProperty(propertyName = "codiceAooProtocollante")
  protected String codiceAooProtocollante;

  @ActaProperty(propertyName = "codiceAooResponsabile")
  protected String codiceAooResponsabile;

  @ActaProperty(propertyName = "dbKeyTipoRegistrazione")
  protected String dbKeyTipoRegistrazione;

  @ActaProperty(propertyName = "objectIdClassificazione")
  protected String objectIdClassificazione;

  @ActaProperty(propertyName = "anno")
  protected Long anno;

  @ActaProperty(propertyName = "flagRiservato")
  protected Boolean flagRiservato;

  public LocalDate getDataProtocollo() {
    return dataProtocollo;
  }

  public void setDataProtocollo(LocalDate dataProtocollo) {
    this.dataProtocollo = dataProtocollo;
  }

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public String getObjectIdRegistrazione() {
    return objectIdRegistrazione;
  }

  public void setObjectIdRegistrazione(String objectIdRegistrazione) {
    this.objectIdRegistrazione = objectIdRegistrazione;
  }

  public Long getAnnoProtocolloMittente() {
    return annoProtocolloMittente;
  }

  public void setAnnoProtocolloMittente(Long annoProtocolloMittente) {
    this.annoProtocolloMittente = annoProtocolloMittente;
  }

  public String getIdAooResponsabile() {
    return idAooResponsabile;
  }

  public void setIdAooResponsabile(String idAooResponsabile) {
    this.idAooResponsabile = idAooResponsabile;
  }

  public String getObjectIdAooResponsabile() {
    return objectIdAooResponsabile;
  }

  public void setObjectIdAooResponsabile(String objectIdAooResponsabile) {
    this.objectIdAooResponsabile = objectIdAooResponsabile;
  }

  public String getDataProtocolloMittente() {
    return dataProtocolloMittente;
  }

  public void setDataProtocolloMittente(String dataProtocolloMittente) {
    this.dataProtocolloMittente = dataProtocolloMittente;
  }

  public String getIdAooProtocollante() {
    return idAooProtocollante;
  }

  public void setIdAooProtocollante(String idAooProtocollante) {
    this.idAooProtocollante = idAooProtocollante;
  }

  public String getObjectIdAooProtocollante() {
    return objectIdAooProtocollante;
  }

  public void setObjectIdAooProtocollante(String objectIdAooProtocollante) {
    this.objectIdAooProtocollante = objectIdAooProtocollante;
  }

  public String getCodiceProtocolloMittente() {
    return codiceProtocolloMittente;
  }

  public void setCodiceProtocolloMittente(String codiceProtocolloMittente) {
    this.codiceProtocolloMittente = codiceProtocolloMittente;
  }

  public String getOggetto() {
    return oggetto;
  }

  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  public String getIdRegistroProtocollo() {
    return idRegistroProtocollo;
  }

  public void setIdRegistroProtocollo(String idRegistroProtocollo) {
    this.idRegistroProtocollo = idRegistroProtocollo;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getCodiceAooProtocollante() {
    return codiceAooProtocollante;
  }

  public void setCodiceAooProtocollante(String codiceAooProtocollante) {
    this.codiceAooProtocollante = codiceAooProtocollante;
  }

  public String getCodiceAooResponsabile() {
    return codiceAooResponsabile;
  }

  public void setCodiceAooResponsabile(String codiceAooResponsabile) {
    this.codiceAooResponsabile = codiceAooResponsabile;
  }

  public String getDbKeyTipoRegistrazione() {
    return dbKeyTipoRegistrazione;
  }

  public void setDbKeyTipoRegistrazione(String dbKeyTipoRegistrazione) {
    this.dbKeyTipoRegistrazione = dbKeyTipoRegistrazione;
  }

  public String getObjectIdClassificazione() {
    return objectIdClassificazione;
  }

  public void setObjectIdClassificazione(String objectIdClassificazione) {
    this.objectIdClassificazione = objectIdClassificazione;
  }

  public Long getAnno() {
    return anno;
  }

  public void setAnno(Long anno) {
    this.anno = anno;
  }

  public Boolean getFlagRiservato() {
    return flagRiservato;
  }

  public void setFlagRiservato(Boolean flagRiservato) {
    this.flagRiservato = flagRiservato;
  }

}
