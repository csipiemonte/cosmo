/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoREntity;

/**
 *
 */

/**
 * The persistent class for the cosmo_r_sigillo_documento database table.
 *
 */
@Entity
@Table(name="cosmo_r_sigillo_documento")
@NamedQuery(name="CosmoRSigilloDocumento.findAll", query="SELECT c FROM CosmoRSigilloDocumento c")
public class CosmoRSigilloDocumento extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_SIGILLO_DOCUMENTO_ID_GENERATOR",
  sequenceName = "COSMO_R_SIGILLO_DOCUMENTO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_R_SIGILLO_DOCUMENTO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_esito_sigillo")
  private String codiceEsitoSigillo;

  @Column(name = "messaggio_esito_sigillo")
  private String messaggioEsitoSigillo;

  // bi-directional many-to-one association to CosmoDStatoSigilloElettronico
  @ManyToOne
  @JoinColumn(name = "codice_stato_sigillo")
  private CosmoDStatoSigilloElettronico cosmoDStatoSigilloElettronico;

  // bi-directional many-to-one association to CosmoTSigilloElettronico
  @ManyToOne
  @JoinColumn(name = "id_sigillo")
  private CosmoTSigilloElettronico cosmoTSigilloElettronico;

  // bi-directional many-to-one association to CosmoTDocumento
  @ManyToOne
  @JoinColumn(name = "id_documento")
  private CosmoTDocumento cosmoTDocumento;

  public CosmoRSigilloDocumento() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceEsitoSigillo() {
    return this.codiceEsitoSigillo;
  }

  public void setCodiceEsitoSigillo(String codiceEsitoSigillo) {
    this.codiceEsitoSigillo = codiceEsitoSigillo;
  }

  public String getMessaggioEsitoSigillo() {
    return this.messaggioEsitoSigillo;
  }

  public void setMessaggioEsitoSigillo(String messaggioEsitoSigillo) {
    this.messaggioEsitoSigillo = messaggioEsitoSigillo;
  }

  public CosmoDStatoSigilloElettronico getCosmoDStatoSigilloElettronico() {
    return this.cosmoDStatoSigilloElettronico;
  }

  public void setCosmoDStatoSigilloElettronico(
      CosmoDStatoSigilloElettronico cosmoDStatoSigilloElettronico) {
    this.cosmoDStatoSigilloElettronico = cosmoDStatoSigilloElettronico;
  }

  public CosmoTSigilloElettronico getCosmoTSigilloElettronico() {
    return this.cosmoTSigilloElettronico;
  }

  public void setCosmoTSigilloElettronico(CosmoTSigilloElettronico cosmoTSigilloElettronico) {
    this.cosmoTSigilloElettronico = cosmoTSigilloElettronico;
  }

  public CosmoTDocumento getCosmoTDocumento() {
    return this.cosmoTDocumento;
  }

  public void setCosmoTDocumento(CosmoTDocumento cosmoTDocumento) {
    this.cosmoTDocumento = cosmoTDocumento;
  }

}
