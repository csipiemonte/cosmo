/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_c_configurazione_ente database table.
 *
 */
@Embeddable
public class CosmoCConfigurazioneEntePK implements Serializable {
  // default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  private String chiave;

  @Column(name = "id_ente", insertable = false, updatable = false)
  private Long idEnte;

  public CosmoCConfigurazioneEntePK() {}

  public String getChiave() {
    return this.chiave;
  }

  public void setChiave(String chiave) {
    this.chiave = chiave;
  }

  public Long getIdEnte() {
    return this.idEnte;
  }

  public void setIdEnte(Long idEnte) {
    this.idEnte = idEnte;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof CosmoCConfigurazioneEntePK)) {
      return false;
    }
    CosmoCConfigurazioneEntePK castOther = (CosmoCConfigurazioneEntePK) other;
    return this.chiave.equals(castOther.chiave) && this.idEnte.equals(castOther.idEnte);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.chiave.hashCode();
    hash = hash * prime + this.idEnte.hashCode();

    return hash;
  }
}
