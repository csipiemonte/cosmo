/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.util.Date;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.StatoEfficaciaDefaultImpl;


/**
 *
 */
@ActaModel(className = "StatoDiEfficaciaDecodifica")
public abstract class StatoEfficacia extends EntitaActa {

  public static StatoEfficaciaDefaultImpl.Builder builder() {
    return StatoEfficaciaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "idStatoDiEfficacia")
  protected String idStatoDiEfficacia;

  @ActaProperty(propertyName = "descrizione")
  protected String descrizione;

  @ActaProperty(propertyName = "dataFineValidita")
  protected Date dataFineValidita;

  @ActaProperty(propertyName = "valoreDefault")
  protected Boolean valoreDefault;

  public void setIdStatoEfficacia(String idStatoDiEfficacia) {
    this.idStatoDiEfficacia = idStatoDiEfficacia;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Date getDataFineValidita() {
    return dataFineValidita;
  }

  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  public boolean getValoreDefault() {
    return valoreDefault;
  }

  public void setValoreDefault(Boolean valoreDefault) {
    this.valoreDefault = valoreDefault;
  }

}
