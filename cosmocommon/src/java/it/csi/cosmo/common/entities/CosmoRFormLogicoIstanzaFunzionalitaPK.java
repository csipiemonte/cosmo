/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the cosmo_r_form_logico_istanza_funzionalita database table.
 * 
 */
@Embeddable
public class CosmoRFormLogicoIstanzaFunzionalitaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="id_form_logico", insertable=false, updatable=false)
	private Long idFormLogico;

	@Column(name="id_istanza_funzionalita", insertable=false, updatable=false)
	private Long idIstanzaFunzionalita;

	public CosmoRFormLogicoIstanzaFunzionalitaPK() {
	}
	public Long getIdFormLogico() {
		return this.idFormLogico;
	}
	public void setIdFormLogico(Long idFormLogico) {
		this.idFormLogico = idFormLogico;
	}
	public Long getIdIstanzaFunzionalita() {
		return this.idIstanzaFunzionalita;
	}
	public void setIdIstanzaFunzionalita(Long idIstanzaFunzionalita) {
		this.idIstanzaFunzionalita = idIstanzaFunzionalita;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CosmoRFormLogicoIstanzaFunzionalitaPK)) {
			return false;
		}
		CosmoRFormLogicoIstanzaFunzionalitaPK castOther = (CosmoRFormLogicoIstanzaFunzionalitaPK)other;
		return 
			this.idFormLogico.equals(castOther.idFormLogico)
			&& this.idIstanzaFunzionalita.equals(castOther.idIstanzaFunzionalita);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.idFormLogico.hashCode();
		hash = hash * prime + this.idIstanzaFunzionalita.hashCode();
		
		return hash;
	}
}
