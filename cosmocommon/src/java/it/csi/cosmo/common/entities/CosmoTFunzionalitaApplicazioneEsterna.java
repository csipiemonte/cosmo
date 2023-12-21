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
 * The persistent class for the cosmo_t_funzionalita_applicazione_esterna database table.
 *
 */
@Entity
@Table(name="cosmo_t_funzionalita_applicazione_esterna")
@NamedQuery(name="CosmoTFunzionalitaApplicazioneEsterna.findAll", query="SELECT c FROM CosmoTFunzionalitaApplicazioneEsterna c")
public class CosmoTFunzionalitaApplicazioneEsterna extends CosmoTEntity
implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }


  @Id
  @SequenceGenerator(name = "COSMO_T_FUNZIONALITA_APPLICAZIONE_ESTERNA_ID_GENERATOR",
  allocationSize = 1, sequenceName = "COSMO_T_FUNZIONALITA_APPLICAZIONE_ESTERNA_ID_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_FUNZIONALITA_APPLICAZIONE_ESTERNA_ID_GENERATOR")

  private Long id;

  @Column(nullable = false, length = 255)
  private String descrizione;

  @Column(nullable = false)
  private Boolean principale;

  @Column(nullable = false)
  private String url;

  //bi-directional many-to-one association to CosmoRUtenteFunzionalitaApplicazioneEsterna
  @OneToMany(mappedBy="cosmoTFunzionalitaApplicazioneEsterna")
  private List<CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas;

  // bi-directional many-to-one association to CosmoREnteApplicazioneEsterna
  @ManyToOne
  @JoinColumn(name = "id_ente_applicazione_esterna")
  private CosmoREnteApplicazioneEsterna cosmoREnteApplicazioneEsterna;

  // bi-directional many-to-one association to CosmoREnteFunzionalitaApplicazioneEsterna
  @OneToMany(mappedBy = "cosmoTFunzionalitaApplicazioneEsterna")
  private List<CosmoREnteFunzionalitaApplicazioneEsterna> cosmoREnteFunzionalitaApplicazioneEsternas;


  public CosmoTFunzionalitaApplicazioneEsterna() {
    // NOP
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Boolean getPrincipale() {
    return this.principale;
  }

  public void setPrincipale(Boolean principale) {
    this.principale = principale;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<CosmoRUtenteFunzionalitaApplicazioneEsterna> getCosmoRUtenteFunzionalitaApplicazioneEsternas() {
    return this.cosmoRUtenteFunzionalitaApplicazioneEsternas;
  }

  public void setCosmoRUtenteFunzionalitaApplicazioneEsternas(List<CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas) {
    this.cosmoRUtenteFunzionalitaApplicazioneEsternas = cosmoRUtenteFunzionalitaApplicazioneEsternas;
  }

  public CosmoRUtenteFunzionalitaApplicazioneEsterna addCosmoRUtenteFunzionalitaApplicazioneEsterna(CosmoRUtenteFunzionalitaApplicazioneEsterna cosmoRUtenteFunzionalitaApplicazioneEsterna) {
    getCosmoRUtenteFunzionalitaApplicazioneEsternas().add(cosmoRUtenteFunzionalitaApplicazioneEsterna);
    cosmoRUtenteFunzionalitaApplicazioneEsterna.setCosmoTFunzionalitaApplicazioneEsterna(this);

    return cosmoRUtenteFunzionalitaApplicazioneEsterna;
  }

  public CosmoRUtenteFunzionalitaApplicazioneEsterna removeCosmoRUtenteFunzionalitaApplicazioneEsterna(CosmoRUtenteFunzionalitaApplicazioneEsterna cosmoRUtenteFunzionalitaApplicazioneEsterna) {
    getCosmoRUtenteFunzionalitaApplicazioneEsternas().remove(cosmoRUtenteFunzionalitaApplicazioneEsterna);
    cosmoRUtenteFunzionalitaApplicazioneEsterna.setCosmoTFunzionalitaApplicazioneEsterna(null);

    return cosmoRUtenteFunzionalitaApplicazioneEsterna;
  }

  public CosmoREnteApplicazioneEsterna getCosmoREnteApplicazioneEsterna() {
    return this.cosmoREnteApplicazioneEsterna;
  }

  public void setCosmoREnteApplicazioneEsterna(
      CosmoREnteApplicazioneEsterna cosmoREnteApplicazioneEsterna) {
    this.cosmoREnteApplicazioneEsterna = cosmoREnteApplicazioneEsterna;
  }

  public List<CosmoREnteFunzionalitaApplicazioneEsterna> getCosmoREnteFunzionalitaApplicazioneEsternas() {
    return this.cosmoREnteFunzionalitaApplicazioneEsternas;
  }

  public void setCosmoREnteFunzionalitaApplicazioneEsternas(
      List<CosmoREnteFunzionalitaApplicazioneEsterna> cosmoREnteFunzionalitaApplicazioneEsternas) {
    this.cosmoREnteFunzionalitaApplicazioneEsternas = cosmoREnteFunzionalitaApplicazioneEsternas;
  }

  public CosmoREnteFunzionalitaApplicazioneEsterna addCosmoREnteFunzionalitaApplicazioneEsterna(
      CosmoREnteFunzionalitaApplicazioneEsterna cosmoREnteFunzionalitaApplicazioneEsterna) {
    getCosmoREnteFunzionalitaApplicazioneEsternas().add(cosmoREnteFunzionalitaApplicazioneEsterna);
    cosmoREnteFunzionalitaApplicazioneEsterna.setCosmoTFunzionalitaApplicazioneEsterna(this);

    return cosmoREnteFunzionalitaApplicazioneEsterna;
  }

  public CosmoREnteFunzionalitaApplicazioneEsterna removeCosmoREnteFunzionalitaApplicazioneEsterna(
      CosmoREnteFunzionalitaApplicazioneEsterna cosmoREnteFunzionalitaApplicazioneEsterna) {
    getCosmoREnteFunzionalitaApplicazioneEsternas()
        .remove(cosmoREnteFunzionalitaApplicazioneEsterna);
    cosmoREnteFunzionalitaApplicazioneEsterna.setCosmoTFunzionalitaApplicazioneEsterna(null);

    return cosmoREnteFunzionalitaApplicazioneEsterna;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
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
    CosmoTFunzionalitaApplicazioneEsterna other = (CosmoTFunzionalitaApplicazioneEsterna) obj;
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
    return "CosmoTFunzionalitaApplicazioneEsterna [" + (id != null ? "id=" + id + ", " : "")
        + (principale != null ? "principale=" + principale + ", " : "")
        + (url != null ? "uuidNodo=" + url : "") + "]";
  }
}
