/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.integration.soap.acta.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import it.doqui.acta.acaris.management.VitalRecordCodeType;


/**
 * Classe di contesto per l'integrazione con i servizi di DoQui ACTA.
 *
 */
public class ActaClientContext implements Serializable {

  private static final long serialVersionUID = 1L;

  private String repository;

  private String utente;

  private LocalDateTime scadenzaUtente;

  private Long aoo;

  private Long struttura;

  private Long nodo;

  private String codiceProtocollista;

  private String applicationInfo;

  private Map<ActaVitalRecordCodes, VitalRecordCodeType> vrcMap = new HashMap<>();

  public Map<ActaVitalRecordCodes, VitalRecordCodeType> getVrcMap() {
    return vrcMap;
  }

  public void setVrcMap(Map<ActaVitalRecordCodes, VitalRecordCodeType> vrcMap) {
    this.vrcMap = vrcMap;
  }

  public String getApplicationInfo() {
    return applicationInfo;
  }

  public void setApplicationInfo(String applicationInfo) {
    this.applicationInfo = applicationInfo;
  }

  public String getCodiceProtocollista() {
    return codiceProtocollista;
  }

  public void setCodiceProtocollista(String codiceProtocollista) {
    this.codiceProtocollista = codiceProtocollista;
  }

  public LocalDateTime getScadenzaUtente() {
    return scadenzaUtente;
  }

  public void setScadenzaUtente(LocalDateTime scadenzaUtente) {
    this.scadenzaUtente = scadenzaUtente;
  }

  public String getRepository() {
    return repository;
  }

  public void setRepository(String repository) {
    this.repository = repository;
  }

  public String getUtente() {
    return utente;
  }

  public void setUtente(String utente) {
    this.utente = utente;
  }

  public Long getAoo() {
    return aoo;
  }

  public void setAoo(Long aoo) {
    this.aoo = aoo;
  }

  public Long getStruttura() {
    return struttura;
  }

  public void setStruttura(Long struttura) {
    this.struttura = struttura;
  }

  public Long getNodo() {
    return nodo;
  }

  public void setNodo(Long nodo) {
    this.nodo = nodo;
  }

  public void setVitalRecordCodes(VitalRecordCodeType[] vitalRecordCodes) {
    for (VitalRecordCodeType vr : vitalRecordCodes) {
      vrcMap.put(ActaVitalRecordCodes.fromDescription(vr.getDescrizione()), vr);
    }
  }

}
