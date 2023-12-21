/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_formato_file_tipo_documento database table.
 *
 */
@Entity
@Table(name="cosmo_r_formato_file_tipo_documento")
@NamedQuery(name="CosmoRFormatoFileTipoDocumento.findAll", query="SELECT c FROM CosmoRFormatoFileTipoDocumento c")
public class CosmoRFormatoFileTipoDocumento extends CosmoREntity {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CosmoRFormatoFileTipoDocumentoPK id;

    // bi-directional many-to-one association to CosmoTEnte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_formato_file", nullable = false, insertable = false, updatable = false)
    private CosmoDFormatoFile cosmoDFormatoFile;

    // bi-directional many-to-one association to CosmoTFruitore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_tipo_documento", nullable = false, insertable = false, updatable = false)
    private CosmoDTipoDocumento cosmoDTipoDocumento;


    public CosmoRFormatoFileTipoDocumento() {
      // empty constructor
    }

    public CosmoRFormatoFileTipoDocumentoPK getId() {
        return this.id;
    }

    public void setId(CosmoRFormatoFileTipoDocumentoPK id) {
        this.id = id;
    }

    public CosmoDFormatoFile getCosmoDFormatoFile() {
      return cosmoDFormatoFile;
    }

    public void setCosmoDFormatoFile(CosmoDFormatoFile cosmoDFormatoFile) {
      this.cosmoDFormatoFile = cosmoDFormatoFile;
    }

    public CosmoDTipoDocumento getCosmoDTipoDocumento() {
      return cosmoDTipoDocumento;
    }

    public void setCosmoDTipoDocumento(CosmoDTipoDocumento cosmoDTipoDocumento) {
      this.cosmoDTipoDocumento = cosmoDTipoDocumento;
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
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      CosmoRFormatoFileTipoDocumento other = (CosmoRFormatoFileTipoDocumento) obj;
      if (id == null) {
        if (other.id != null) {
          return false;
        }
      } else if (!id.equals(other.id)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "CosmoRFormatoFileTipoDocumento [" + (id != null ? "id=" + id + ", " : "") + "]";
    }

}
