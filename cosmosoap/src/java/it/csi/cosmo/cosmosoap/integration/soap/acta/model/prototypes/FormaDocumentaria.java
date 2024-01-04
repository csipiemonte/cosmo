/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.util.Date;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.FormaDocumentariaDefaultImpl;


/**
 *
 */
@ActaModel(className = "FormaDocumentariaDecodifica")
public abstract class FormaDocumentaria extends EntitaActa {

  public static FormaDocumentariaDefaultImpl.Builder builder() {
    return FormaDocumentariaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "idFormaDocumentaria")
  protected String idFormaDocumentaria;

  @ActaProperty(propertyName = "descrizione")
  protected String descrizione;

  @ActaProperty(propertyName = "firmato")
  protected Boolean firmato;

  @ActaProperty(propertyName = "dataFineValidita")
  protected Date dataFineValidita;

  @ActaProperty(propertyName = "daConservareSostitutiva")
  protected Boolean daConservareSostitutiva;

  @ActaProperty(propertyName = "originale")
  protected Boolean originale;

  @ActaProperty(propertyName = "unico")
  protected Boolean unico;

  @ActaProperty(propertyName = "idVitalRecordCode")
  protected String idVitalRecordCode;

  public String getIdFormaDocumentaria() {
    return idFormaDocumentaria;
  }

  public void setIdFormaDocumentaria(String idFormaDocumentaria) {
    this.idFormaDocumentaria = idFormaDocumentaria;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public boolean getFirmato() {
    return firmato;
  }

  public void setFrimato(Boolean firmato) {
    this.firmato = firmato;
  }

  public Date getDataFineValidita() {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  public boolean getOriginale() {
    return originale;
  }

  public void setOriginale(Boolean originale) {
    this.originale = originale;
  }

  public boolean getUnico() {
    return unico;
  }

  public void setUnico(Boolean unico) {
    this.unico = unico;
  }

  public String getIdVitalRecordCode() {
    return idVitalRecordCode;
  }

  public void setIdVitalRecordCode(String idVitalRecordCode) {
    this.idVitalRecordCode = idVitalRecordCode;
  }

}
