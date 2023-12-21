/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the cosmo_r_istanza_form_logico_parametro_valore database table.
 * 
 */
@Embeddable
public class CosmoRIstanzaFormLogicoParametroValorePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_istanza", insertable=false, updatable=false)
	private Long idIstanza;

	@Column(name="codice_chiave_parametro", insertable=false, updatable=false)
	private String codiceChiaveParametro;

	public CosmoRIstanzaFormLogicoParametroValorePK() {
	}
	public Long getIdIstanza() {
		return this.idIstanza;
	}
	public void setIdIstanza(Long idIstanza) {
		this.idIstanza = idIstanza;
	}
	public String getCodiceChiaveParametro() {
		return this.codiceChiaveParametro;
	}
	public void setCodiceChiaveParametro(String codiceChiaveParametro) {
		this.codiceChiaveParametro = codiceChiaveParametro;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CosmoRIstanzaFormLogicoParametroValorePK)) {
			return false;
		}
		CosmoRIstanzaFormLogicoParametroValorePK castOther = (CosmoRIstanzaFormLogicoParametroValorePK)other;
		return 
			this.idIstanza.equals(castOther.idIstanza)
			&& this.codiceChiaveParametro.equals(castOther.codiceChiaveParametro);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idIstanza.hashCode();
		hash = hash * prime + this.codiceChiaveParametro.hashCode();
		
		return hash;
	}
}
