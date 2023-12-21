/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_ente_certificatore_ente database table.
 *
 */
@Entity
@Table(name = "cosmo_r_ente_certificatore_ente")
@NamedQuery(name = "CosmoREnteCertificatoreEnte.findAll",
query = "SELECT c FROM CosmoREnteCertificatoreEnte c")
public class CosmoREnteCertificatoreEnte extends CosmoREntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoREnteCertificatoreEntePK id;

  @Column(nullable = false, insertable = false, updatable = false)
  private Long anno;

  @Column(name = "numero_marche_temporali")
  private Long numeroMarcheTemporali;

  // bi-directional many-to-one association to CosmoDEnteCertificatore
  @ManyToOne
  @JoinColumn(name = "codice_ente_certificatore", nullable = false, insertable = false,
  updatable = false)
  private CosmoDEnteCertificatore cosmoDEnteCertificatore;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name = "id_ente", nullable = false, insertable = false, updatable = false)
  private CosmoTEnte cosmoTEnte;

  public CosmoREnteCertificatoreEnte() {
    // NOP
  }

  public CosmoREnteCertificatoreEntePK getId() {
    return this.id;
  }

  public void setId(CosmoREnteCertificatoreEntePK id) {
    this.id = id;
  }


  public Long getAnno() {
    return anno;
  }

  public void setAnno(Long anno) {
    this.anno = anno;
  }

  public Long getNumeroMarcheTemporali() {
    return this.numeroMarcheTemporali;
  }

  public void setNumeroMarcheTemporali(Long numeroMarcheTemporali) {
    this.numeroMarcheTemporali = numeroMarcheTemporali;
  }

  public CosmoDEnteCertificatore getCosmoDEnteCertificatore() {
    return this.cosmoDEnteCertificatore;
  }

  public void setCosmoDEnteCertificatore(CosmoDEnteCertificatore cosmoDEnteCertificatore) {
    this.cosmoDEnteCertificatore = cosmoDEnteCertificatore;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoREnteCertificatoreEnte other = (CosmoREnteCertificatoreEnte) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoREnteCertificatoreEnte [id=" + id + "anno=" + anno + "numeroMarcheTemporali="
        + numeroMarcheTemporali + "]";
  }

}
