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
@IndexModel(prefix = "cosmo", type = "documento")
public class CosmoDocumentoIndex extends IndexEntity {

  @IndexProperty(value = "descDocumento")
  private String descrizione;

  @IndexProperty
  private String tipoDocumento;

  @IndexProperty
  private Long idDocumentoCosmo;

  @IndexAspect
  private LockableAspect lockable;

  @IndexAspect(declared = true)
  private VersionableAspect versionable;

  @IndexAspect
  private WorkingCopyAspect workingCopy;

  public CosmoDocumentoIndex() {
    // NOP
  }

  private CosmoDocumentoIndex(Builder builder) {
    filename = builder.filename;
    mimeType = builder.mimeType;
    encoding = builder.encoding;
    content = builder.content;
    descrizione = builder.descrizione;
    idDocumentoCosmo = builder.numeroDocumento;
    tipoDocumento = builder.tipoDocumento;
  }

  public WorkingCopyAspect getWorkingCopy() {
    return workingCopy;
  }

  public VersionableAspect getVersionable() {
    return versionable;
  }

  public LockableAspect getLockable() {
    return lockable;
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

  /**
   * Creates builder to build {@link CosmoDocumentoIndex}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link CosmoDocumentoIndex}.
   */
  public static final class Builder {

    private String filename;

    private String mimeType;

    private String encoding;

    private byte[] content;

    private String descrizione;

    private String tipoDocumento;

    private Long numeroDocumento;

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

    public CosmoDocumentoIndex build() {
      return new CosmoDocumentoIndex(this);
    }
  }

  @Override
  public String toString() {
    return "CosmoDocumentoIndex [descrizione=" + descrizione + ", tipoDocumento=" + tipoDocumento
        + ", idDocumentoCosmo=" + idDocumentoCosmo + ", filename=" + filename + ", uid=" + uid
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
    CosmoDocumentoIndex other = (CosmoDocumentoIndex) obj;

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
