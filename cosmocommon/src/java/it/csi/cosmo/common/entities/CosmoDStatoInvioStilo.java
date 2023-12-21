/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_stato_invio_stilo database table.
 *
 */
@Entity
@Table(name="cosmo_d_stato_invio_stilo")
@NamedQuery(name="CosmoDStatoInvioStilo.findAll", query="SELECT c FROM CosmoDStatoInvioStilo c")
public class CosmoDStatoInvioStilo extends CosmoDEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  private String descrizione;


  //bi-directional many-to-one association to CosmoRInvioStiloDocumento
  @OneToMany(mappedBy="cosmoDStatoInvioStilo")
  private List<CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos;

  public CosmoDStatoInvioStilo() {
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

  public List<CosmoRInvioStiloDocumento> getCosmoRInvioStiloDocumentos() {
    return this.cosmoRInvioStiloDocumentos;
  }

  public void setCosmoRInvioStiloDocumentos(List<CosmoRInvioStiloDocumento> cosmoRInvioStiloDocumentos) {
    this.cosmoRInvioStiloDocumentos = cosmoRInvioStiloDocumentos;
  }

  public CosmoRInvioStiloDocumento addCosmoRInvioStiloDocumento(CosmoRInvioStiloDocumento cosmoRInvioStiloDocumento) {
    getCosmoRInvioStiloDocumentos().add(cosmoRInvioStiloDocumento);
    cosmoRInvioStiloDocumento.setCosmoDStatoInvioStilo(this);

    return cosmoRInvioStiloDocumento;
  }

  public CosmoRInvioStiloDocumento removeCosmoRInvioStiloDocumento(CosmoRInvioStiloDocumento cosmoRInvioStiloDocumento) {
    getCosmoRInvioStiloDocumentos().remove(cosmoRInvioStiloDocumento);
    cosmoRInvioStiloDocumento.setCosmoDStatoInvioStilo(null);

    return cosmoRInvioStiloDocumento;
  }


  @Override
  public int hashCode() {
    return Objects.hash(codice);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoDStatoInvioStilo other = (CosmoDStatoInvioStilo) obj;
    return Objects.equals(codice, other.codice);
  }

  @Override
  public String toString() {
    return "CosmoDStatoInvioStilo [codice=" + codice + ", descrizione=" + descrizione + "]";
  }



}
