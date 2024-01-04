/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoFisicoActaDefaultImpl;


/**
 *
 */
@ActaModel(className = "DocumentoFisicoPropertiesType")
public abstract class DocumentoFisicoActa extends EntitaActa {

  public static DocumentoFisicoActaDefaultImpl.Builder builder() {
    return DocumentoFisicoActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "descrizione")
  protected String descrizione;

  @ActaProperty(propertyName = "progressivoPerDocumento")
  protected Integer progressivo;

  @ActaProperty(propertyName = "dataMemorizzazione")
  protected LocalDateTime dataMemorizzazione;

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Integer getProgressivo() {
    return progressivo;
  }

  public void setProgressivo(Integer progressivo) {
    this.progressivo = progressivo;
  }

  public LocalDateTime getDataMemorizzazione() {
    return dataMemorizzazione;
  }

  public void setDataMemorizzazione(LocalDateTime dataMemorizzazione) {
    this.dataMemorizzazione = dataMemorizzazione;
  }

}
