/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tipo_commento database table.
 *
 */
@Entity
@Table(name = "cosmo_d_tipo_commento")
@NamedQuery(name = "CosmoDTipoCommento.findAll", query = "SELECT c FROM CosmoDTipoCommento c")
public class CosmoDTipoCommento extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 10)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  // bi-directional many-to-one association to CosmoTCommento
  @OneToMany(mappedBy = "tipo", fetch = FetchType.LAZY)
  private List<CosmoTCommento> cosmoTCommentos;


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

  public List<CosmoTCommento> getCosmoTCommentos() {
    return this.cosmoTCommentos;
  }

  public void setCosmoTCommentos(List<CosmoTCommento> cosmoTCommentos) {
    this.cosmoTCommentos = cosmoTCommentos;
  }

  public CosmoTCommento addCosmoTCommento(CosmoTCommento cosmoTCommento) {
    getCosmoTCommentos().add(cosmoTCommento);
    cosmoTCommento.setTipo(this);

    return cosmoTCommento;
  }

  public CosmoTCommento removeCosmoTCommento(CosmoTCommento cosmoTCommento) {
    getCosmoTCommentos().remove(cosmoTCommento);
    cosmoTCommento.setTipo(null);

    return cosmoTCommento;
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
    CosmoDTipoCommento other = (CosmoDTipoCommento) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDTipoCommento [descrizione=" + descrizione + ", codice=" + codice + "]";
  }


}

