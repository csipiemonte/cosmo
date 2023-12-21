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
 * The persistent class for the cosmo_d_filtro_campo database table.
 *
 */
@Entity
@Table(name="cosmo_d_filtro_campo")
@NamedQuery(name="CosmoDFiltroCampo.findAll", query="SELECT c FROM CosmoDFiltroCampo c")
public class CosmoDFiltroCampo extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 25)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  // bi-directional many-to-one association to CosmoTCommento
  @OneToMany(mappedBy = "filtroCampo", fetch = FetchType.LAZY)
  private List<CosmoTVariabileProcesso> cosmoTVariabileProcessos;



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




  public List<CosmoTVariabileProcesso> getCosmoTVariabileProcessos() {
    return this.cosmoTVariabileProcessos;
  }

  public void setCosmoTVariabileProcessos(List<CosmoTVariabileProcesso> cosmoTVariabileProcessos) {
    this.cosmoTVariabileProcessos = cosmoTVariabileProcessos;
  }

  public CosmoTVariabileProcesso addCosmoTVariabileProcesso(CosmoTVariabileProcesso cosmoTVariabileProcesso) {
    getCosmoTVariabileProcessos().add(cosmoTVariabileProcesso);
    cosmoTVariabileProcesso.setFiltroCampo(this);

    return cosmoTVariabileProcesso;
  }

  public CosmoTVariabileProcesso removeCosmoTVariabileProcesso(CosmoTVariabileProcesso cosmoTVariabileProcesso) {
    getCosmoTVariabileProcessos().remove(cosmoTVariabileProcesso);
    cosmoTVariabileProcesso.setFiltroCampo(null);

    return cosmoTVariabileProcesso;
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
    CosmoDFiltroCampo other = (CosmoDFiltroCampo) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDFiltroCampo [descrizione=" + descrizione + ", codice=" + codice + "]";
  }


}
