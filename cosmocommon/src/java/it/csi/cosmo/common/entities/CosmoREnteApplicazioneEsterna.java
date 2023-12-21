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
import it.csi.cosmo.common.entities.proto.CosmoREntity;


/**
 * The persistent class for the cosmo_r_ente_applicazione_esterna database table.
 *
 */
@Entity
@Table(name="cosmo_r_ente_applicazione_esterna")
@NamedQuery(name="CosmoREnteApplicazioneEsterna.findAll", query="SELECT c FROM CosmoREnteApplicazioneEsterna c")
public class CosmoREnteApplicazioneEsterna extends CosmoREntity {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_R_ENTE_APPLICAZIONE_ESTERNA_ID_GENERATOR",
  sequenceName = "COSMO_R_ENTE_APPLICAZIONE_ESTERNA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_R_ENTE_APPLICAZIONE_ESTERNA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  //bi-directional many-to-one association to CosmoTApplicazioneEsterna
  @ManyToOne
  @JoinColumn(name="id_applicazione_esterna")
  private CosmoTApplicazioneEsterna cosmoTApplicazioneEsterna;

  //bi-directional many-to-one association to CosmoTEnte
  @ManyToOne
  @JoinColumn(name="id_ente")
  private CosmoTEnte cosmoTEnte;

  //bi-directional many-to-one association to CosmoTFunzionalitaApplicazioneEsterna
  @OneToMany(mappedBy="cosmoREnteApplicazioneEsterna")
  private List<CosmoTFunzionalitaApplicazioneEsterna> cosmoTFunzionalitaApplicazioneEsternas;

  public CosmoREnteApplicazioneEsterna() {
    // NOP
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CosmoTApplicazioneEsterna getCosmoTApplicazioneEsterna() {
    return this.cosmoTApplicazioneEsterna;
  }

  public void setCosmoTApplicazioneEsterna(CosmoTApplicazioneEsterna cosmoTApplicazioneEsterna) {
    this.cosmoTApplicazioneEsterna = cosmoTApplicazioneEsterna;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public List<CosmoTFunzionalitaApplicazioneEsterna> getCosmoTFunzionalitaApplicazioneEsternas() {
    return this.cosmoTFunzionalitaApplicazioneEsternas;
  }

  public void setCosmoTFunzionalitaApplicazioneEsternas(List<CosmoTFunzionalitaApplicazioneEsterna> cosmoTFunzionalitaApplicazioneEsternas) {
    this.cosmoTFunzionalitaApplicazioneEsternas = cosmoTFunzionalitaApplicazioneEsternas;
  }

  public CosmoTFunzionalitaApplicazioneEsterna addCosmoTFunzionalitaApplicazioneEsterna(CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna) {
    getCosmoTFunzionalitaApplicazioneEsternas().add(cosmoTFunzionalitaApplicazioneEsterna);
    cosmoTFunzionalitaApplicazioneEsterna.setCosmoREnteApplicazioneEsterna(this);

    return cosmoTFunzionalitaApplicazioneEsterna;
  }

  public CosmoTFunzionalitaApplicazioneEsterna removeCosmoTFunzionalitaApplicazioneEsterna(CosmoTFunzionalitaApplicazioneEsterna cosmoTFunzionalitaApplicazioneEsterna) {
    getCosmoTFunzionalitaApplicazioneEsternas().remove(cosmoTFunzionalitaApplicazioneEsterna);
    cosmoTFunzionalitaApplicazioneEsterna.setCosmoREnteApplicazioneEsterna(null);

    return cosmoTFunzionalitaApplicazioneEsterna;
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CosmoREnteApplicazioneEsterna other = (CosmoREnteApplicazioneEsterna) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoREnteApplicazioneEsterna [id=" + id + "]";
  }
}
