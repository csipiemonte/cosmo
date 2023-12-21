/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_ente_certificatore_ente database table.
 *
 */
@Embeddable
public class CosmoREnteCertificatoreEntePK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="codice_ente_certificatore", insertable=false, updatable=false)
  private String codiceEnteCertificatore;

  @Column(name="id_ente", insertable=false, updatable=false)
  private Long idEnte;

  @Column(insertable = false, updatable = false)
  private Long anno;

  public CosmoREnteCertificatoreEntePK() {
    // NOP
  }
  public String getCodiceEnteCertificatore() {
    return this.codiceEnteCertificatore;
  }
  public void setCodiceEnteCertificatore(String codiceEnteCertificatore) {
    this.codiceEnteCertificatore = codiceEnteCertificatore;
  }
  public Long getIdEnte() {
    return this.idEnte;
  }
  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }
  public Long getAnno() {
    return this.anno;
  }
  public void setAnno(Long anno) {
    this.anno = anno;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoREnteCertificatoreEntePK)) {
      return false;
    }
    CosmoREnteCertificatoreEntePK castOther = (CosmoREnteCertificatoreEntePK)other;
    return
        this.codiceEnteCertificatore.equals(castOther.codiceEnteCertificatore)
        && this.idEnte.equals(castOther.idEnte)
        && this.anno.equals(castOther.anno);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.codiceEnteCertificatore.hashCode();
    hash = hash * prime + this.idEnte.hashCode();
    hash = hash * prime + this.anno.hashCode();

    return hash;
  }
}
