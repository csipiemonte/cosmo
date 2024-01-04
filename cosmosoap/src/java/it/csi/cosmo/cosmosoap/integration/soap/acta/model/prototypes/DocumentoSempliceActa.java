/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.DocumentoSempliceActaDefaultImpl;
import it.doqui.acta.acaris.documentservice.DocumentoArchivisticoIRC;


/**
 *
 */
@ActaModel(className = "DocumentoSemplicePropertiesType",
    creationPayloadClass = DocumentoArchivisticoIRC.class)
public abstract class DocumentoSempliceActa extends DocumentoActa {

  public static DocumentoSempliceActaDefaultImpl.Builder builder() {
    return DocumentoSempliceActaDefaultImpl.builder();
  }

  @ActaProperty(propertyName = "dataFinePubblicazione", className = "DocumentoDBPropertiesType")
  protected LocalDate dataFinePubblicazione;

  @ActaProperty(propertyName = "indiceClassificazioneEstesa",
      className = "IndiceClassificazioneEstesaType")
  protected String indiceClassificazioneEstesa;

  @ActaProperty(propertyName = "idAnnotazioniList", itemType = String.class,
      className = "AnnotazionePropertiesType")
  protected List<String> idAnnotazioniList;

  @ActaProperty(propertyName = "indiceClassificazione", className = "IndiceClassificazioneType")
  protected String indiceClassificazione;

  @ActaProperty(propertyName = "idProtocolloList", itemType = String.class,
      className = "ProtocolloPropertiesType")
  protected List<String> idProtocolloList;

  public LocalDate getDataFinePubblicazione() {
    return dataFinePubblicazione;
  }

  public void setDataFinePubblicazione(LocalDate dataFinePubblicazione) {
    this.dataFinePubblicazione = dataFinePubblicazione;
  }

  public String getIndiceClassificazioneEstesa() {
    return indiceClassificazioneEstesa;
  }

  public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
    this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
  }

  public List<String> getIdAnnotazioniList() {
    return idAnnotazioniList;
  }

  public void setIdAnnotazioniList(List<String> idAnnotazioniList) {
    this.idAnnotazioniList = idAnnotazioniList;
  }

  public String getIndiceClassificazione() {
    return indiceClassificazione;
  }

  public void setIndiceClassificazione(String indiceClassificazione) {
    this.indiceClassificazione = indiceClassificazione;
  }

  public List<String> getIdProtocolloList() {
    return idProtocolloList;
  }

  public void setIdProtocolloList(List<String> idProtocolloList) {
    this.idProtocolloList = idProtocolloList;
  }

}
