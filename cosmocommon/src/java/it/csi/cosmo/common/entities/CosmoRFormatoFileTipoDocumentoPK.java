/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the cosmo_r_formato_file_tipo_documento database table.
 *
 */
@Embeddable
public class CosmoRFormatoFileTipoDocumentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="codice_formato_file", insertable=false, updatable=false, unique=true, nullable=false, length=255)
	private String codiceFormatoFile;

	@Column(name="codice_tipo_documento", insertable=false, updatable=false, unique=true, nullable=false, length=100)
	private String codiceTipoDocumento;

	public CosmoRFormatoFileTipoDocumentoPK() {
      // empty constructor
	}
	public String getCodiceFormatoFile() {
		return this.codiceFormatoFile;
	}
	public void setCodiceFormatoFile(String codiceFormatoFile) {
		this.codiceFormatoFile = codiceFormatoFile;
	}
	public String getCodiceTipoDocumento() {
		return this.codiceTipoDocumento;
	}
	public void setCodiceTipoDocumento(String codiceTipoDocumento) {
		this.codiceTipoDocumento = codiceTipoDocumento;
	}

	@Override
  public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CosmoRFormatoFileTipoDocumentoPK)) {
			return false;
		}
		CosmoRFormatoFileTipoDocumentoPK castOther = (CosmoRFormatoFileTipoDocumentoPK)other;
		return
			this.codiceFormatoFile.equals(castOther.codiceFormatoFile)
			&& this.codiceTipoDocumento.equals(castOther.codiceTipoDocumento);
	}

	@Override
  public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.codiceFormatoFile.hashCode();
		hash = hash * prime + this.codiceTipoDocumento.hashCode();

		return hash;
	}
}
