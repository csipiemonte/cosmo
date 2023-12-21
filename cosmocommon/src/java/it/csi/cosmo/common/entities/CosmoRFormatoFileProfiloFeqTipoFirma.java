/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_formato_file_profilo_feq_tipo_firma database table.
 *
 */
@Entity
@Table(name="cosmo_r_formato_file_profilo_feq_tipo_firma")
@NamedQuery(name="CosmoRFormatoFileProfiloFeqTipoFirma.findAll", query="SELECT c FROM CosmoRFormatoFileProfiloFeqTipoFirma c")
public class CosmoRFormatoFileProfiloFeqTipoFirma extends CosmoREntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private CosmoRFormatoFileProfiloFeqTipoFirmaPK id;

  //bi-directional many-to-one association to CosmoDFormatoFile
  @ManyToOne
  @JoinColumn(name = "codice_formato_file", nullable = false, insertable = false, updatable = false)
  private CosmoDFormatoFile cosmoDFormatoFile;

  //bi-directional many-to-one association to CosmoDProfiloFeq
  @ManyToOne
  @JoinColumn(name = "codice_profilo_feq", nullable = false, insertable = false, updatable = false)
  private CosmoDProfiloFeq cosmoDProfiloFeq;

  //bi-directional many-to-one association to CosmoDTipoFirma
  @ManyToOne
  @JoinColumn(name = "codice_tipo_firma", nullable = false, insertable = false, updatable = false)
  private CosmoDTipoFirma cosmoDTipoFirma;

  public CosmoRFormatoFileProfiloFeqTipoFirma() {
    // NOP
  }

  public CosmoRFormatoFileProfiloFeqTipoFirmaPK getId() {
    return this.id;
  }

  public void setId(CosmoRFormatoFileProfiloFeqTipoFirmaPK id) {
    this.id = id;
  }

  public CosmoDFormatoFile getCosmoDFormatoFile() {
    return this.cosmoDFormatoFile;
  }

  public void setCosmoDFormatoFile(CosmoDFormatoFile cosmoDFormatoFile) {
    this.cosmoDFormatoFile = cosmoDFormatoFile;
  }

  public CosmoDProfiloFeq getCosmoDProfiloFeq() {
    return this.cosmoDProfiloFeq;
  }

  public void setCosmoDProfiloFeq(CosmoDProfiloFeq cosmoDProfiloFeq) {
    this.cosmoDProfiloFeq = cosmoDProfiloFeq;
  }

  public CosmoDTipoFirma getCosmoDTipoFirma() {
    return this.cosmoDTipoFirma;
  }

  public void setCosmoDTipoFirma(CosmoDTipoFirma cosmoDTipoFirma) {
    this.cosmoDTipoFirma = cosmoDTipoFirma;
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
    CosmoRFormatoFileProfiloFeqTipoFirma other = (CosmoRFormatoFileProfiloFeqTipoFirma) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoRFormatoFileProfiloFeqTipoFirma [id=" + id + "]";
  }
}
