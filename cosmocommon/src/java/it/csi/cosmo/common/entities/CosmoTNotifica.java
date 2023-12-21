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
 * The persistent class for the cosmo_t_notifica database table.
 *
 */
@Entity
@Table(name = "cosmo_t_notifica")
@NamedQuery(name = "CosmoTNotifica.findAll", query = "SELECT c FROM CosmoTNotifica c")
public class CosmoTNotifica extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_NOTIFICA_ID_GENERATOR",
  sequenceName = "COSMO_T_NOTIFICA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_NOTIFICA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(nullable = false)
  private Timestamp arrivo;

  @Column(length = 100)
  private String descrizione;

  @Column(length = 100)
  private String classe;

  private Timestamp scadenza;

  // bi-directional many-to-one association to CosmoDTipoNotifica
  @ManyToOne
  @JoinColumn(name = "tipo_notifica")
  private CosmoDTipoNotifica cosmoDTipoNotifica;

  @Column(length = 1000)
  private String url;

  @Column(name = "url_descrizione", length = 2147483647)
  private String urlDescrizione;

  // bi-directional many-to-one association to CosmoRNotificaUtenteEnte
  @OneToMany(mappedBy = "cosmoTNotifica")
  private List<CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;

  // bi-directional many-to-one association to CosmoTFruitore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_fruitore")
  private CosmoTFruitore cosmoTFruitore;

  // bi-directional many-to-one association to CosmoTPratica
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_pratica")
  private CosmoTPratica cosmoTPratica;

  public CosmoTNotifica() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getArrivo() {
    return this.arrivo;
  }

  public void setArrivo(Timestamp arrivo) {
    this.arrivo = arrivo;
  }

  public String getDescrizione() {
    return this.descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Timestamp getScadenza() {
    return this.scadenza;
  }

  public void setScadenza(Timestamp scadenza) {
    this.scadenza = scadenza;
  }

  public CosmoDTipoNotifica getCosmoDTipoNotifica() {
    return this.cosmoDTipoNotifica;
  }

  public void setCosmoDTipoNotifica(CosmoDTipoNotifica cosmoDTipoNotifica) {
    this.cosmoDTipoNotifica = cosmoDTipoNotifica;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrlDescrizione() {
    return this.urlDescrizione;
  }

  public void setUrlDescrizione(String urlDescrizione) {
    this.urlDescrizione = urlDescrizione;
  }

  public List<CosmoRNotificaUtenteEnte> getCosmoRNotificaUtenteEntes() {
    return this.cosmoRNotificaUtenteEntes;
  }

  public void setCosmoRNotificaUtenteEntes(
      List<CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes) {
    this.cosmoRNotificaUtenteEntes = cosmoRNotificaUtenteEntes;
  }

  public CosmoRNotificaUtenteEnte addCosmoRNotificaUtenteEnte(
      CosmoRNotificaUtenteEnte cosmoRNotificaUtenteEnte) {
    getCosmoRNotificaUtenteEntes().add(cosmoRNotificaUtenteEnte);
    cosmoRNotificaUtenteEnte.setCosmoTNotifica(this);

    return cosmoRNotificaUtenteEnte;
  }

  public CosmoRNotificaUtenteEnte removeCosmoRNotificaUtenteEnte(
      CosmoRNotificaUtenteEnte cosmoRNotificaUtenteEnte) {
    getCosmoRNotificaUtenteEntes().remove(cosmoRNotificaUtenteEnte);
    cosmoRNotificaUtenteEnte.setCosmoTNotifica(null);

    return cosmoRNotificaUtenteEnte;
  }

  public CosmoTFruitore getCosmoTFruitore() {
    return this.cosmoTFruitore;
  }

  public void setCosmoTFruitore(CosmoTFruitore cosmoTFruitore) {
    this.cosmoTFruitore = cosmoTFruitore;
  }

  public CosmoTPratica getCosmoTPratica() {
    return this.cosmoTPratica;
  }

  public void setCosmoTPratica(CosmoTPratica cosmoTPratica) {
    this.cosmoTPratica = cosmoTPratica;
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
    CosmoTNotifica other = (CosmoTNotifica) obj;
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
    return "CosmoTNotifica [" + (id != null ? "id=" + id + ", " : "")
        + (arrivo != null ? "arrivo=" + arrivo + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "")
        + (scadenza != null ? "scadenza=" + scadenza : "") + "]";
  }

  public String getClasse() {
    return classe;
  }

  public void setClasse(String classe) {
    this.classe = classe;
  }
}
