/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.OffsetDateTime;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.ProtocolloActaDefaultImpl;
import it.doqui.acta.acaris.common.EnumCommonObjectType;


/**
 *
 */
public abstract class ProtocolloActa extends EntitaActa {

  public static ProtocolloActaDefaultImpl.Builder builder() {
    return ProtocolloActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "objectTypeId")
  protected EnumCommonObjectType objectTypeId;

  @ActaProperty(propertyName = "idRegistrazione")
  protected String numRegistrazioneProtocollo;

  @ActaProperty(propertyName = "dataProtocollo")
  protected OffsetDateTime dataRegistrazioneProtocollo;

  @ActaProperty(propertyName = "oggetto")
  protected String oggetto;

  @ActaProperty(propertyName = "entrata")
  protected Boolean entrata;

  @ActaProperty(propertyName = "aooCheRegistra")
  protected String aooCheRegistra;

  @ActaProperty(propertyName = "enteCheRegistra")
  protected String enteCheRegistra;

  @ActaProperty(propertyName = "mittente")
  protected List<String> mittente;

  @ActaProperty(propertyName = "destinatario")
  protected List<String> destinatario;

  @ActaProperty(propertyName = "riservato")
  protected Boolean riservato;

  @ActaProperty(propertyName = "annullato")
  protected Boolean annullato;

  @ActaProperty(propertyName = "uuidRegistrazione")
  protected String uuidRegistrazioneProtocollo;

  public EnumCommonObjectType getObjectTypeId() {
    return objectTypeId;
  }

  public void setObjectTypeId(EnumCommonObjectType objectTypeId) {
    this.objectTypeId = objectTypeId;
  }

  public String getNumRegistrazioneProtocollo() {
    return numRegistrazioneProtocollo;
  }

  public void setNumRegistrazioneProtocollo(String numRegistrazioneProtocollo) {
    this.numRegistrazioneProtocollo = numRegistrazioneProtocollo;
  }

  public OffsetDateTime getDataRegistrazioneProtocollo() {
    return dataRegistrazioneProtocollo;
  }

  public void setDataRegistrazioneProtocollo(OffsetDateTime dataRegistrazioneProtocollo) {
    this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
  }

  public String getOggetto() {
    return oggetto;
  }

  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  public Boolean getEntrata() {
    return entrata;
  }

  public void setEntrata(Boolean entrata) {
    this.entrata = entrata;
  }

  public String getAooCheRegistra() {
    return aooCheRegistra;
  }

  public void setAooCheRegistra(String aooCheRegistra) {
    this.aooCheRegistra = aooCheRegistra;
  }

  public String getEnteCheRegistra() {
    return enteCheRegistra;
  }

  public void setEnteCheRegistra(String enteCheRegistra) {
    this.enteCheRegistra = enteCheRegistra;
  }

  public List<String> getMittente() {
    return mittente;
  }

  public void setMittente(List<String> mittente) {
    this.mittente = mittente;
  }

  public List<String> getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(List<String> destinatario) {
    this.destinatario = destinatario;
  }

  public Boolean getRiservato() {
    return riservato;
  }

  public void setRiservato(Boolean riservato) {
    this.riservato = riservato;
  }

  public Boolean getAnnullato() {
    return annullato;
  }

  public void setAnnullato(Boolean annullato) {
    this.annullato = annullato;
  }

  public String getUuidRegistrazioneProtocollo() {
    return uuidRegistrazioneProtocollo;
  }

  public void setUuidRegistrazioneProtocollo(String uuidRegistrazioneProtocollo) {
    this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
  }

}
