/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * The persistent class for the cosmo_r_attivita_assegnazione database table.
 *
 */
@Entity
@Table(name = "cosmo_r_attivita_assegnazione")
@NamedQuery(name = "CosmoRAttivitaAssegnazione.findAll",
    query = "SELECT c FROM CosmoRAttivitaAssegnazione c")
public class CosmoRAttivitaAssegnazione extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_ATTIVITA_ASSEGNAZIONE_ID_GENERATOR",
      sequenceName = "COSMO_R_ATTIVITA_ASSEGNAZIONE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_R_ATTIVITA_ASSEGNAZIONE_ID_GENERATOR")
  private Long id;

  private Boolean assegnatario;

  @Column(name = "id_gruppo")
  private Integer idGruppo;

  @Column(name = "id_utente")
  private Integer idUtente;

  // bi-directional many-to-one association to CosmoTAttivita
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_attivita")
  private CosmoTAttivita cosmoTAttivita;

  // bi-directional many-to-one association to CosmoTGruppo
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_gruppo", insertable = false, updatable = false)
  private CosmoTGruppo gruppo;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_utente", insertable = false, updatable = false)
  private CosmoTUtente utente;

  public CosmoRAttivitaAssegnazione() {
    // empty for JPA
  }

  public CosmoTGruppo getGruppo() {
    return gruppo;
  }

  public void setGruppo(CosmoTGruppo gruppo) {
    this.gruppo = gruppo;
  }

  public CosmoTUtente getUtente() {
    return utente;
  }

  public void setUtente(CosmoTUtente utente) {
    this.utente = utente;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Boolean getAssegnatario() {
    return this.assegnatario;
  }

  public void setAssegnatario(Boolean assegnatario) {
    this.assegnatario = assegnatario;
  }

  public Integer getIdGruppo() {
    return this.idGruppo;
  }

  public void setIdGruppo(Integer idGruppo) {
    this.idGruppo = idGruppo;
  }

  public Integer getIdUtente() {
    return this.idUtente;
  }

  public void setIdUtente(Integer idUtente) {
    this.idUtente = idUtente;
  }

  public CosmoTAttivita getCosmoTAttivita() {
    return this.cosmoTAttivita;
  }

  public void setCosmoTAttivita(CosmoTAttivita cosmoTAttivita) {
    this.cosmoTAttivita = cosmoTAttivita;
  }

}
