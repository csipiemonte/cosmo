/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tipo_contenuto_firmato database table.
 *
 */
@Entity
@Table(name="cosmo_d_tipo_contenuto_firmato")
@NamedQuery(name="CosmoDTipoContenutoFirmato.findAll", query="SELECT c FROM CosmoDTipoContenutoFirmato c")
public class CosmoDTipoContenutoFirmato extends CosmoDEntity {
	private static final long serialVersionUID = 1L;

	@Id
    @Column(nullable = false, length = 100)
	private String codice;

	private String descrizione;

	public CosmoDTipoContenutoFirmato() {
      // empty constructor
	}

	public String getCodice() {
		return this.codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getDescrizione() {
		return this.descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((codice == null) ? 0 : codice.hashCode());
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
      CosmoDTipoContenutoFirmato other = (CosmoDTipoContenutoFirmato) obj;
      if (codice == null) {
        if (other.codice != null)
          return false;
      } else if (!codice.equals(other.codice))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "CosmoDTipoContenutoFirmato [descrizione=" + descrizione + ", codice=" + codice + "]";
    }

}
