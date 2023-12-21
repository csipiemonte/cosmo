/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_pratica_pratica database table.
 *
 */
@Embeddable
public class CosmoRPraticaPraticaPK implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name="id_pratica_da", insertable=false, updatable=false)
  private Long idPraticaDa;

  @Column(name="id_pratica_a", insertable=false, updatable=false)
  private Long idPraticaA;

  @Column(name="codice_tipo_relazione", insertable=false, updatable=false)
  private String codiceTipoRelazione;

  public CosmoRPraticaPraticaPK() {
      // empty constructor
  }
  public Long getIdPraticaDa() {
    return this.idPraticaDa;
  }
  public void setIdPraticaDa(Long idPraticaDa) {
    this.idPraticaDa = idPraticaDa;
  }
  public Long getIdPraticaA() {
    return this.idPraticaA;
  }
  public void setIdPraticaA(Long idPraticaA) {
    this.idPraticaA = idPraticaA;
  }
  public String getCodiceTipoRelazione() {
    return this.codiceTipoRelazione;
  }
  public void setCodiceTipoRelazione(String codiceTipoRelazione) {
    this.codiceTipoRelazione = codiceTipoRelazione;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoRPraticaPraticaPK)) {
      return false;
    }
    CosmoRPraticaPraticaPK castOther = (CosmoRPraticaPraticaPK)other;
    return
        this.idPraticaDa.equals(castOther.idPraticaDa)
        && this.idPraticaA.equals(castOther.idPraticaA)
        && this.codiceTipoRelazione.equals(castOther.codiceTipoRelazione);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.idPraticaDa.hashCode();
    hash = hash * prime + this.idPraticaA.hashCode();
    hash = hash * prime + this.codiceTipoRelazione.hashCode();

    return hash;
  }

    @Override
    public String toString() {
      return "CosmoRPraticaPraticaPK ["
          + (idPraticaDa != null ? "idPraticaDa=" + idPraticaDa + ", " : "")
          + (idPraticaA != null ? "idPraticaA=" + idPraticaA + ", " : "")
          + (codiceTipoRelazione != null ? "codiceTipoRelazione=" + codiceTipoRelazione : "") + "]";
    }
}
