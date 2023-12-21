/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_tab_dettaglio database table.
 *
 */
@Entity
@Table(name="cosmo_d_tab_dettaglio")
@NamedQuery(name="CosmoDTabDettaglio.findAll", query="SELECT c FROM CosmoDTabDettaglio c")
public class CosmoDTabDettaglio extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  private String descrizione;

  //bi-directional many-to-one association to CosmoRTabDettaglioTipoPratica
  @OneToMany(mappedBy="cosmoDTabDettaglio")
  private List<CosmoRTabDettaglioTipoPratica> cosmoRTabDettaglioTipoPraticas;

  public CosmoDTabDettaglio() {
    // empty
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

  public List<CosmoRTabDettaglioTipoPratica> getCosmoRTabDettaglioTipoPraticas() {
    return this.cosmoRTabDettaglioTipoPraticas;
  }

  public void setCosmoRTabDettaglioTipoPraticas(List<CosmoRTabDettaglioTipoPratica> cosmoRTabDettaglioTipoPraticas) {
    this.cosmoRTabDettaglioTipoPraticas = cosmoRTabDettaglioTipoPraticas;
  }

  public CosmoRTabDettaglioTipoPratica addCosmoRTabDettaglioTipoPratica(CosmoRTabDettaglioTipoPratica cosmoRTabDettaglioTipoPratica) {
    getCosmoRTabDettaglioTipoPraticas().add(cosmoRTabDettaglioTipoPratica);
    cosmoRTabDettaglioTipoPratica.setCosmoDTabDettaglio(this);

    return cosmoRTabDettaglioTipoPratica;
  }

  public CosmoRTabDettaglioTipoPratica removeCosmoRTabDettaglioTipoPratica(CosmoRTabDettaglioTipoPratica cosmoRTabDettaglioTipoPratica) {
    getCosmoRTabDettaglioTipoPraticas().remove(cosmoRTabDettaglioTipoPratica);
    cosmoRTabDettaglioTipoPratica.setCosmoDTabDettaglio(null);

    return cosmoRTabDettaglioTipoPratica;
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
    CosmoDTabDettaglio other = (CosmoDTabDettaglio) obj;
    if (codice == null) {
      if (other.codice != null)
        return false;
    } else if (!codice.equals(other.codice))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoDTabDettaglio [codice=" + codice + ", descrizione=" + descrizione + "]";
  }


}
