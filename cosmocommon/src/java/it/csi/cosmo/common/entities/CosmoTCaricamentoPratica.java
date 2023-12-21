/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_caricamento_pratica database table.
 *
 */
@Entity
@Table(name="cosmo_t_caricamento_pratica")
@NamedQuery(name="CosmoTCaricamentoPratica.findAll", query="SELECT c FROM CosmoTCaricamentoPratica c")
public class CosmoTCaricamentoPratica extends CosmoTEntity implements CsiLogAuditedEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CARICAMENTO_PRATICA_ID_GENERATOR",
      sequenceName = "COSMO_T_CARICAMENTO_PRATICA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COSMO_T_CARICAMENTO_PRATICA_ID_GENERATOR")
  private Long id;

  @Column(name="descrizione_evento")
  private String descrizioneEvento;

  private String errore;

  @Column(name="identificativo_pratica")
  private String identificativoPratica;

  @Column(name="nome_file")
  private String nomeFile;

  @Column(name="path_file")
  private String pathFile;

  //bi-directional many-to-one association to CosmoDStatoCaricamentoPratica
  @ManyToOne
  @JoinColumn(name="codice_stato_caricamento_pratica")
  private CosmoDStatoCaricamentoPratica cosmoDStatoCaricamentoPratica;

  //bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name="id_ente")
  private CosmoTEnte cosmoTEnte;

  //bi-directional many-to-one association to CosmoTPratica
  @ManyToOne
  @JoinColumn(name="id_pratica")
  private CosmoTPratica cosmoTPratica;

  // bi-directional many-to-one association to CosmoTCaricamentoPratica
  @ManyToOne
  @JoinColumn(name = "id_parent")
  private CosmoTCaricamentoPratica cosmoTCaricamentoPratica;

  // bi-directional many-to-one association to CosmoTUtente
  @ManyToOne
  @JoinColumn(name = "id_utente")
  private CosmoTUtente cosmoTUtente;

  // bi-directional many-to-one association to CosmoTCaricamentoPratica
  @OneToMany(mappedBy = "cosmoTCaricamentoPratica")
  private List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;

  public CosmoTCaricamentoPratica() {
    // empty
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescrizioneEvento() {
    return this.descrizioneEvento;
  }

  public void setDescrizioneEvento(String descrizioneEvento) {
    this.descrizioneEvento = descrizioneEvento;
  }

  public String getErrore() {
    return this.errore;
  }

  public void setErrore(String errore) {
    this.errore = errore;
  }

  public String getIdentificativoPratica() {
    return this.identificativoPratica;
  }

  public void setIdentificativoPratica(String identificativoPratica) {
    this.identificativoPratica = identificativoPratica;
  }

  public String getNomeFile() {
    return this.nomeFile;
  }

  public void setNomeFile(String nomeFile) {
    this.nomeFile = nomeFile;
  }

  public String getPathFile() {
    return this.pathFile;
  }

  public void setPathFile(String pathFile) {
    this.pathFile = pathFile;
  }

  public CosmoDStatoCaricamentoPratica getCosmoDStatoCaricamentoPratica() {
    return this.cosmoDStatoCaricamentoPratica;
  }

  public void setCosmoDStatoCaricamentoPratica(CosmoDStatoCaricamentoPratica cosmoDStatoCaricamentoPratica) {
    this.cosmoDStatoCaricamentoPratica = cosmoDStatoCaricamentoPratica;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
  }

  public CosmoTCaricamentoPratica getCosmoTCaricamentoPratica() {
    return this.cosmoTCaricamentoPratica;
  }

  public void setCosmoTCaricamentoPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    this.cosmoTCaricamentoPratica = cosmoTCaricamentoPratica;
  }

  public List<CosmoTCaricamentoPratica> getCosmoTCaricamentoPraticas() {
    return this.cosmoTCaricamentoPraticas;
  }

  public void setCosmoTCaricamentoPraticas(
      List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas) {
    this.cosmoTCaricamentoPraticas = cosmoTCaricamentoPraticas;
  }

  public CosmoTCaricamentoPratica addCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().add(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTCaricamentoPratica(this);

    return cosmoTCaricamentoPratica;
  }

  public CosmoTCaricamentoPratica removeCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().remove(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTCaricamentoPratica(null);

    return cosmoTCaricamentoPratica;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    CosmoTCaricamentoPratica other = (CosmoTCaricamentoPratica) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTCaricamentoPratica [" + (id != null ? "id=" + id + ", " : "")
        + (descrizioneEvento != null ? "descrizioneEvento=" + descrizioneEvento + ", " : "")
        + (errore != null ? "errore=" + errore + ", " : "")
        + (identificativoPratica != null ? "identificativoPratica=" + identificativoPratica + ", "
            : "")
        + (nomeFile != null ? "nomeFile=" + nomeFile + ", " : "")
        + (pathFile != null ? "pathFile=" + pathFile + ", " : "") + "]";
  }

}
