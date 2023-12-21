/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import java.sql.Timestamp;
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
 *
 */
@Entity
@Table(name="cosmo_d_stato_sigillo_elettronico")
@NamedQuery(name="CosmoDStatoSigilloElettronico.findAll", query="SELECT c FROM CosmoDStatoSigilloElettronico c")
public class CosmoDStatoSigilloElettronico extends CosmoDEntity {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String codice;

  private String descrizione;

  // bi-directional many-to-one association to CosmoRSmistamentoDocumento
  @OneToMany(mappedBy = "cosmoDStatoSigilloElettronico", fetch = FetchType.LAZY)
  private List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos;

  public CosmoDStatoSigilloElettronico() {}

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
  public Timestamp getDtFineVal() {
    return this.dtFineVal;
  }

  @Override
  public void setDtFineVal(Timestamp dtFineVal) {
    this.dtFineVal = dtFineVal;
  }

  @Override
  public Timestamp getDtInizioVal() {
    return this.dtInizioVal;
  }

  @Override
  public void setDtInizioVal(Timestamp dtInizioVal) {
    this.dtInizioVal = dtInizioVal;
  }

  public List<CosmoRSigilloDocumento> getCosmoRSigilloDocumentos() {
    return this.cosmoRSigilloDocumentos;
  }

  public void setCosmoRSigilloDocumentos(List<CosmoRSigilloDocumento> cosmoRSigilloDocumentos) {
    this.cosmoRSigilloDocumentos = cosmoRSigilloDocumentos;
  }

  public CosmoRSigilloDocumento addCosmoRSigilloDocumento(
      CosmoRSigilloDocumento cosmoRSigilloDocumento) {
    getCosmoRSigilloDocumentos().add(cosmoRSigilloDocumento);
    cosmoRSigilloDocumento.setCosmoDStatoSigilloElettronico(this);

    return cosmoRSigilloDocumento;
  }

  public CosmoRSigilloDocumento removeCosmoRSigilloDocumento(
      CosmoRSigilloDocumento cosmoRSigilloDocumento) {
    getCosmoRSigilloDocumentos().remove(cosmoRSigilloDocumento);
    cosmoRSigilloDocumento.setCosmoDStatoSigilloElettronico(null);

    return cosmoRSigilloDocumento;
  }

}
