/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.jasper;

import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.cosmoecm.integration.jasper.model.ExportFormat;


public class RisultatoGenerazioneReport {

  private CosmoTPratica pratica;
  private byte[] compilato;
  private ExportFormat formato;
  private String mimeType;
  private String estensione;

  public RisultatoGenerazioneReport() {
    // Auto-generated constructor stub
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public void setPratica(CosmoTPratica pratica) {
    this.pratica = pratica;
  }

  public byte[] getCompilato() {
    return compilato;
  }

  public void setCompilato(byte[] compilato) {
    this.compilato = compilato;
  }

  public ExportFormat getFormato() {
    return formato;
  }

  public void setFormato(ExportFormat formato) {
    this.formato = formato;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public String getEstensione() {
    return estensione;
  }

  public void setEstensione(String estensione) {
    this.estensione = estensione;
  }

}
