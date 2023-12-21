/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the cosmo_r_invio_stilo_documento database table.
 * 
 */
@Embeddable
public class CosmoRInvioStiloDocumentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_ud")
	private Long idUd;

	@Column(name="id_documento", insertable=false, updatable=false)
	private Long idDocumento;

	public CosmoRInvioStiloDocumentoPK() {
	}
	public Long getIdUd() {
		return this.idUd;
	}
	public void setIdUd(Long idUd) {
		this.idUd = idUd;
	}
	public Long getIdDocumento() {
		return this.idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CosmoRInvioStiloDocumentoPK)) {
			return false;
		}
		CosmoRInvioStiloDocumentoPK castOther = (CosmoRInvioStiloDocumentoPK)other;
		return 
			this.idUd.equals(castOther.idUd)
			&& this.idDocumento.equals(castOther.idDocumento);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idUd.hashCode();
		hash = hash * prime + this.idDocumento.hashCode();
		
		return hash;
	}
}
