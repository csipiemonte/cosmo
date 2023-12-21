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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_applicazione_esterna database table.
 *
 */
@Entity
@Table(name="cosmo_t_applicazione_esterna")
@NamedQuery(name="CosmoTApplicazioneEsterna.findAll", query="SELECT c FROM CosmoTApplicazioneEsterna c")
public class CosmoTApplicazioneEsterna extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;


  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_APPLICAZIONE_ESTERNA_ID_GENERATOR", allocationSize = 1,
  sequenceName = "COSMO_T_APPLICAZIONE_ESTERNA_ID_SEQ")
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_APPLICAZIONE_ESTERNA_ID_GENERATOR")
  private Long id;

  @Column(nullable = false, length = 255)
  private String descrizione;

  @Column(nullable = false)
  private byte[] icona;

  // bi-directional many-to-one association to CosmoREnteApplicazioneEsterna
  @OneToMany(mappedBy="cosmoTApplicazioneEsterna")
  private List<CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsternas;

  public CosmoTApplicazioneEsterna() {
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

  public byte[] getIcona() {
    return this.icona;
  }

  public void setIcona(byte[] icona) {
    this.icona = icona;
  }

  public List<CosmoREnteApplicazioneEsterna> getCosmoREnteApplicazioneEsternas() {
    return this.cosmoREnteApplicazioneEsternas;
  }

  public void setCosmoREnteApplicazioneEsternas(
      List<CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsternas) {
    this.cosmoREnteApplicazioneEsternas = cosmoREnteApplicazioneEsternas;
  }

  public CosmoREnteApplicazioneEsterna addCosmoREnteApplicazioneEsterna(
      CosmoREnteApplicazioneEsterna cosmoREnteApplicazioneEsterna) {
    getCosmoREnteApplicazioneEsternas().add(cosmoREnteApplicazioneEsterna);
    cosmoREnteApplicazioneEsterna.setCosmoTApplicazioneEsterna(this);

    return cosmoREnteApplicazioneEsterna;
  }

  public CosmoREnteApplicazioneEsterna removeCosmoREnteApplicazioneEsterna(
      CosmoREnteApplicazioneEsterna cosmoREnteApplicazioneEsterna) {
    getCosmoREnteApplicazioneEsternas().remove(cosmoREnteApplicazioneEsterna);
    cosmoREnteApplicazioneEsterna.setCosmoTApplicazioneEsterna(null);

    return cosmoREnteApplicazioneEsterna;
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
    CosmoTApplicazioneEsterna other = (CosmoTApplicazioneEsterna) obj;
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
    return "CosmoTApplicazioneEsterna [" + (id != null ? "id=" + id + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione : "") + "]";
  }

}
