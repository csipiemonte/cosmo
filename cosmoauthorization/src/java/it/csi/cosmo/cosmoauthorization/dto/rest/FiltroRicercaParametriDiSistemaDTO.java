/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import it.csi.cosmo.common.dto.search.RecursiveFilterSpecification;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaParametriDiSistemaDTO
		implements RecursiveFilterSpecification<FiltroRicercaParametriDiSistemaDTO> {

	private StringFilter chiave;
	private StringFilter valore;
	private StringFilter descrizione;
	private StringFilter fullText;
	private FiltroRicercaParametriDiSistemaDTO[] and;
	private FiltroRicercaParametriDiSistemaDTO[] or;

	public StringFilter getFullText() {
		return fullText;
	}

	public void setFullText(StringFilter fullText) {
		this.fullText = fullText;
	}

	public StringFilter getChiave() {
		return chiave;
	}

	public void setChiave(StringFilter chiave) {
		this.chiave = chiave;
	}

	public StringFilter getValore() {
		return valore;
	}

	public void setValore(StringFilter valore) {
		this.valore = valore;
	}

	public StringFilter getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(StringFilter descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public FiltroRicercaParametriDiSistemaDTO[] getAnd() {
		return and;
	}

	public void setAnd(FiltroRicercaParametriDiSistemaDTO[] and) {
		this.and = and;
	}

	@Override
	public FiltroRicercaParametriDiSistemaDTO[] getOr() {
		return or;
	}

	public void setOr(FiltroRicercaParametriDiSistemaDTO[] or) {
		this.or = or;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chiave == null) ? 0 : chiave.hashCode());
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((valore == null) ? 0 : valore.hashCode());
		result = prime * result + ((fullText == null) ? 0 : fullText.hashCode());
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
		FiltroRicercaParametriDiSistemaDTO other = (FiltroRicercaParametriDiSistemaDTO) obj;
		if (chiave == null) {
			if (other.chiave != null)
				return false;
		} else if (!chiave.equals(other.chiave))
			return false;
		if (valore == null) {
			if (other.valore != null)
				return false;
		} else if (!valore.equals(other.valore))
			return false;
		if (descrizione == null) {
			if (other.descrizione != null)
				return false;
		} else if (!descrizione.equals(other.descrizione))
			return false;
		if (fullText == null) {
			if (other.fullText != null)
				return false;
		} else if (!fullText.equals(other.fullText))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FiltroRicercaEntiDTO [chiave=" + chiave + ","
				+ " valore=" + valore + ", descrizione=" + descrizione+ ", fullText=" + fullText +"]";
	}

}
