/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_formato_file_profilo_feq_tipo_firma database table.
 *
 */
@Embeddable
public class CosmoRFormatoFileProfiloFeqTipoFirmaPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="codice_formato_file", insertable=false, updatable=false)
  private String codiceFormatoFile;

  @Column(name="codice_profilo_feq", insertable=false, updatable=false)
  private String codiceProfiloFeq;

  @Column(name="codice_tipo_firma", insertable=false, updatable=false)
  private String codiceTipoFirma;

  public CosmoRFormatoFileProfiloFeqTipoFirmaPK() {
      // NOP
  }

  public String getCodiceFormatoFile() {
    return this.codiceFormatoFile;
  }

  public void setCodiceFormatoFile(String codiceFormatoFile) {
    this.codiceFormatoFile = codiceFormatoFile;
  }

  public String getCodiceProfiloFeq() {
    return this.codiceProfiloFeq;
  }
  public void setCodiceProfiloFeq(String codiceProfiloFeq) {
    this.codiceProfiloFeq = codiceProfiloFeq;
  }

  public String getCodiceTipoFirma() {
    return this.codiceTipoFirma;
  }

  public void setCodiceTipoFirma(String codiceTipoFirma) {
    this.codiceTipoFirma = codiceTipoFirma;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRFormatoFileProfiloFeqTipoFirmaPK)) {
      return false;
    }
    CosmoRFormatoFileProfiloFeqTipoFirmaPK castOther = (CosmoRFormatoFileProfiloFeqTipoFirmaPK)other;
    return
        this.codiceFormatoFile.equals(castOther.codiceFormatoFile)
        && this.codiceProfiloFeq.equals(castOther.codiceProfiloFeq)
        && this.codiceTipoFirma.equals(castOther.codiceTipoFirma);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.codiceFormatoFile.hashCode();
    hash = hash * prime + this.codiceProfiloFeq.hashCode();
    hash = hash * prime + this.codiceTipoFirma.hashCode();

    return hash;
  }
}
