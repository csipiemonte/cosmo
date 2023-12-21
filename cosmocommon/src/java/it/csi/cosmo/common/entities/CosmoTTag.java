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
 * The persistent class for the cosmo_t_tag database table.
 *
 */
@Entity
@Table(name = "cosmo_t_tag")
@NamedQuery(name = "CosmoTTag.findAll", query = "SELECT c FROM CosmoTTag c")
public class CosmoTTag extends CosmoTEntity implements CsiLogAuditedEntity {

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "COSMO_T_TAG_ID_GENERATOR", sequenceName = "COSMO_T_TAG_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_TAG_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(nullable = false, length = 30)
  private String codice;

  @Column(length = 100)
  private String descrizione;

  // bi-directional many-to-one association to CosmoTEnte
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte cosmoTEnte;

  // bi-directional many-to-one association to CosmoDTipoTag
  @ManyToOne
  @JoinColumn(name = "codice_tipo_tag", nullable = false)
  private CosmoDTipoTag cosmoDTipoTag;

  // bi-directional many-to-one association to CosmoRPraticaTag
  @OneToMany(mappedBy = "cosmoTTag")
  private List<CosmoRPraticaTag> cosmoRPraticaTags;

  // bi-directional many-to-one association to CosmoRUtenteGruppoTag
  @OneToMany(mappedBy = "cosmoTTag")
  private List<CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags;

  public CosmoTTag() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @Override
  public Timestamp getDtCancellazione() {
    return this.dtCancellazione;
  }

  @Override
  public void setDtCancellazione(Timestamp dtCancellazione) {
    this.dtCancellazione = dtCancellazione;
  }

  @Override
  public Timestamp getDtInserimento() {
    return this.dtInserimento;
  }

  @Override
  public void setDtInserimento(Timestamp dtInserimento) {
    this.dtInserimento = dtInserimento;
  }

  @Override
  public Timestamp getDtUltimaModifica() {
    return this.dtUltimaModifica;
  }

  @Override
  public void setDtUltimaModifica(Timestamp dtUltimaModifica) {
    this.dtUltimaModifica = dtUltimaModifica;
  }

  @Override
  public String getUtenteCancellazione() {
    return this.utenteCancellazione;
  }

  @Override
  public void setUtenteCancellazione(String utenteCancellazione) {
    this.utenteCancellazione = utenteCancellazione;
  }

  @Override
  public String getUtenteInserimento() {
    return this.utenteInserimento;
  }

  @Override
  public void setUtenteInserimento(String utenteInserimento) {
    this.utenteInserimento = utenteInserimento;
  }

  @Override
  public String getUtenteUltimaModifica() {
    return this.utenteUltimaModifica;
  }

  @Override
  public void setUtenteUltimaModifica(String utenteUltimaModifica) {
    this.utenteUltimaModifica = utenteUltimaModifica;
  }

  public CosmoTEnte getCosmoTEnte() {
    return this.cosmoTEnte;
  }

  public void setCosmoTEnte(CosmoTEnte cosmoTEnte) {
    this.cosmoTEnte = cosmoTEnte;
  }

  public CosmoDTipoTag getCosmoDTipoTag() {
    return this.cosmoDTipoTag;
  }

  public void setCosmoDTipoTag(CosmoDTipoTag cosmoDTipoTag) {
    this.cosmoDTipoTag = cosmoDTipoTag;
  }

  public List<CosmoRPraticaTag> getCosmoRPraticaTags() {
    return this.cosmoRPraticaTags;
  }

  public void setCosmoRPraticaTags(List<CosmoRPraticaTag> cosmoRPraticaTags) {
    this.cosmoRPraticaTags = cosmoRPraticaTags;
  }

  public CosmoRPraticaTag addCosmoRPraticaTag(CosmoRPraticaTag cosmoRPraticaTag) {
    getCosmoRPraticaTags().add(cosmoRPraticaTag);
    cosmoRPraticaTag.setCosmoTTag(this);

    return cosmoRPraticaTag;
  }

  public CosmoRPraticaTag removeCosmoRPraticaTag(CosmoRPraticaTag cosmoRPraticaTag) {
    getCosmoRPraticaTags().remove(cosmoRPraticaTag);
    cosmoRPraticaTag.setCosmoTTag(null);

    return cosmoRPraticaTag;
  }

  public List<CosmoRUtenteGruppoTag> getCosmoRUtenteGruppoTags() {
    return this.cosmoRUtenteGruppoTags;
  }

  public void setCosmoRUtenteGruppoTags(List<CosmoRUtenteGruppoTag> cosmoRUtenteGruppoTags) {
    this.cosmoRUtenteGruppoTags = cosmoRUtenteGruppoTags;
  }

  public CosmoRUtenteGruppoTag addCosmoRUtenteGruppoTag(
      CosmoRUtenteGruppoTag cosmoRUtenteGruppoTag) {
    getCosmoRUtenteGruppoTags().add(cosmoRUtenteGruppoTag);
    cosmoRUtenteGruppoTag.setCosmoTTag(this);

    return cosmoRUtenteGruppoTag;
  }

  public CosmoRUtenteGruppoTag removeCosmoRUtenteGruppoTag(
      CosmoRUtenteGruppoTag cosmoRUtenteGruppoTag) {
    getCosmoRUtenteGruppoTags().remove(cosmoRUtenteGruppoTag);
    cosmoRUtenteGruppoTag.setCosmoTTag(null);

    return cosmoRUtenteGruppoTag;
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
    CosmoTTag other = (CosmoTTag) obj;
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
    return "CosmoTTag [" + (id != null ? "id=" + id + ", " : "")
        + "codice=" + codice + ","
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + "]";
  }

}
