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
 * The persistent class for the cosmo_d_stato_smistamento database table.
 *
 */
@Entity
@Table(name="cosmo_d_stato_smistamento")
@NamedQuery(name="CosmoDStatoSmistamento.findAll", query="SELECT c FROM CosmoDStatoSmistamento c")
public class CosmoDStatoSmistamento extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  private String descrizione;
  //bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoDStatoSmistamento", fetch = FetchType.LAZY)
  private List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos;

  public CosmoDStatoSmistamento() {
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

  public List<CosmoRSmistamentoDocumento> getCosmoRSmistamentoDocumentos() {
    return this.cosmoRSmistamentoDocumentos;
  }

  public void setCosmoRSmistamentoDocumentos(List<CosmoRSmistamentoDocumento> cosmoRSmistamentoDocumentos) {
    this.cosmoRSmistamentoDocumentos = cosmoRSmistamentoDocumentos;
  }

  public CosmoRSmistamentoDocumento addCosmoRSmistamentoDocumento(CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento) {
    getCosmoRSmistamentoDocumentos().add(cosmoRSmistamentoDocumento);
    cosmoRSmistamentoDocumento.setCosmoDStatoSmistamento(this);

    return cosmoRSmistamentoDocumento;
  }

  public CosmoRSmistamentoDocumento removeCosmoRSmistamentoDocumento(CosmoRSmistamentoDocumento cosmoRSmistamentoDocumento) {
    getCosmoRSmistamentoDocumentos().remove(cosmoRSmistamentoDocumento);
    cosmoRSmistamentoDocumento.setCosmoDStatoSmistamento(null);

    return cosmoRSmistamentoDocumento;
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
    CosmoDStatoSmistamento other = (CosmoDStatoSmistamento) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDStatoSmistamento [codice=" + codice + ", descrizione=" + descrizione + "]";
  }

}
