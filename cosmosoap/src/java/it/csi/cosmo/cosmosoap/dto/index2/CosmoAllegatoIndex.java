/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.dto.index2;

import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.IndexAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.LockableAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.VersionableAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.aspect.WorkingCopyAspect;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexEntity;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexModel;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;


/**
 *
 */
@IndexModel(prefix = "cosmo", type = "allegato")
public class CosmoAllegatoIndex extends IndexEntity {

  @IndexProperty(value = "descDocumentoAll")
  private String descrizione;

  @IndexProperty(value = "tipoDocumentoAll")
  private String tipoDocumento;

  @IndexProperty(value = "idDocumentoCosmoAll")
  private Long idDocumentoCosmo;

  @IndexProperty(value = "idDocParent")
  private Long idDocumentoPadre;

  @IndexAspect
  private LockableAspect lockable;

  @IndexAspect(declared = true)
  private VersionableAspect versionable;

  @IndexAspect
  private WorkingCopyAspect workingCopy;

  public CosmoAllegatoIndex() {
    // NOP
  }

  private CosmoAllegatoIndex(Builder builder) {
    filename = builder.filename;
    mimeType = builder.mimeType;
    encoding = builder.encoding;
    content = builder.content;
    descrizione = builder.descrizione;
    idDocumentoCosmo = builder.numeroDocumento;
    tipoDocumento = builder.tipoDocumento;
    idDocumentoPadre = builder.numeroDocumentoPadre;
  }

  public WorkingCopyAspect getWorkingCopy() {
    return workingCopy;
  }

  public void setWorkingCopy(WorkingCopyAspect workingCopy) {
    this.workingCopy = workingCopy;
  }

  public VersionableAspect getVersionable() {
    return versionable;
  }

  public void setVersionable(VersionableAspect versionable) {
    this.versionable = versionable;
  }

  public LockableAspect getLockable() {
    return lockable;
  }

  public void setLockable(LockableAspect lockable) {
    this.lockable = lockable;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Long getIdDocumentoCosmo() {
    return idDocumentoCosmo;
  }

  public void setIdDocumentoCosmo(Long idDocumentoCosmo) {
    this.idDocumentoCosmo = idDocumentoCosmo;
  }

  public Long getIdDocumentoPadre() {
    return idDocumentoPadre;
  }

  public void setIdDocumentoPadre(Long idDocumentoPadre) {
    this.idDocumentoPadre = idDocumentoPadre;
  }

  /**
   * Creates builder to build {@link CosmoAllegatoIndex}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoAllegatoIndex}.
   */
  public static final class Builder {

    private String filename;

    private String mimeType;

    private String encoding;

    private byte[] content;

    private String descrizione;

    private String tipoDocumento;

    private Long numeroDocumento;

    private Long numeroDocumentoPadre;

    private Builder() {}

    public Builder withFilename(String filename) {
      this.filename = filename;
      return this;
    }

    public Builder withTipoDocumento(String tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
      return this;
    }

    public Builder withMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder withEncoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    public Builder withContent(byte[] content) {
      this.content = content;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withNumeroDocumento(Long numeroDocumento) {
      this.numeroDocumento = numeroDocumento;
      return this;
    }

    public Builder withNumeroDocumentoPadre(Long numeroDocumentoPadre) {
      this.numeroDocumentoPadre = numeroDocumentoPadre;
      return this;
    }

    public CosmoAllegatoIndex build() {
      return new CosmoAllegatoIndex(this);
    }
  }

  @Override
  public String toString() {
    return "CosmoAllegatoIndex [descrizione=" + descrizione
        + ", tipoDocumento=" + tipoDocumento + ", idDocumentoCosmo=" + idDocumentoCosmo
        + ", idDocumentoPadre=" + idDocumentoPadre + ", filename=" + filename + ", uid=" + uid
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((idDocumentoCosmo == null) ? 0 : idDocumentoCosmo.hashCode());
    result = prime * result + ((tipoDocumento == null) ? 0 : tipoDocumento.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoAllegatoIndex other = (CosmoAllegatoIndex) obj;

    if (idDocumentoCosmo == null) {
      if (other.idDocumentoCosmo != null) {
        return false;
      }
    } else if (!idDocumentoCosmo.equals(other.idDocumentoCosmo)) {
      return false;
    }
    if (tipoDocumento == null) {
      if (other.tipoDocumento != null) {
        return false;
      }
    } else if (!tipoDocumento.equals(other.tipoDocumento)) {
      return false;
    }
    return true;
  }

}
