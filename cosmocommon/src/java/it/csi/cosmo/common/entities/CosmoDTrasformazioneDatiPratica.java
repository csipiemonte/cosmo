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
import it.csi.cosmo.common.entities.proto.CosmoDEntity;


/**
 * The persistent class for the cosmo_d_trasformazione_dati_pratica database table.
 *
 */
@Entity
@Table(name = "cosmo_d_trasformazione_dati_pratica")
@NamedQuery(name = "CosmoDTrasformazioneDatiPratica.findAll",
    query = "SELECT c FROM CosmoDTrasformazioneDatiPratica c")
public class CosmoDTrasformazioneDatiPratica extends CosmoDEntity {

  private static final long serialVersionUID = 1L;

  @SequenceGenerator(name = "COSMO_D_TRASFORMAZIONE_DATI_PRATICA_ID_GENERATOR",
      sequenceName = "COSMO_D_TRASFORMAZIONE_DATI_PRATICA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_D_TRASFORMAZIONE_DATI_PRATICA_ID_GENERATOR")
  @Id
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false, name = "codice_tipo_pratica", length = 255, insertable = false,
      updatable = false)
  private String codiceTipoPratica;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codice_tipo_pratica")
  private CosmoDTipoPratica tipoPratica;

  @Column(nullable = false)
  private String definizione;

  @Column(nullable = false, length = 500)
  private String descrizione;

  @Column(nullable = true)
  private Boolean obbligatoria;

  @Column(nullable = true)
  private Integer ordine;

  @Column(nullable = false, name = "codice_fase", length = 255)
  private String codiceFase;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceTipoPratica() {
    return codiceTipoPratica;
  }

  public void setCodiceTipoPratica(String codiceTipoPratica) {
    this.codiceTipoPratica = codiceTipoPratica;
  }

  public CosmoDTipoPratica getTipoPratica() {
    return tipoPratica;
  }

  public void setTipoPratica(CosmoDTipoPratica tipoPratica) {
    this.tipoPratica = tipoPratica;
  }

  public String getDefinizione() {
    return definizione;
  }

  public void setDefinizione(String definizione) {
    this.definizione = definizione;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public Boolean getObbligatoria() {
    return obbligatoria;
  }

  public void setObbligatoria(Boolean obbligatoria) {
    this.obbligatoria = obbligatoria;
  }

  public Integer getOrdine() {
    return ordine;
  }

  public void setOrdine(Integer ordine) {
    this.ordine = ordine;
  }

  public String getCodiceFase() {
    return codiceFase;
  }

  public void setCodiceFase(String codiceFase) {
    this.codiceFase = codiceFase;
  }

  @Override
  public String toString() {
    return "CosmoDTrasformazioneDatiPratica [id=" + id + ", codiceTipoPratica=" + codiceTipoPratica
        + ", descrizione=" + descrizione + ", obbligatoria=" + obbligatoria + ", ordine=" + ordine
        + ", codiceFase=" + codiceFase + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((codiceFase == null) ? 0 : codiceFase.hashCode());
    result = prime * result + ((codiceTipoPratica == null) ? 0 : codiceTipoPratica.hashCode());
    result = prime * result + ((definizione == null) ? 0 : definizione.hashCode());
    result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((obbligatoria == null) ? 0 : obbligatoria.hashCode());
    result = prime * result + ((ordine == null) ? 0 : ordine.hashCode());
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
    CosmoDTrasformazioneDatiPratica other = (CosmoDTrasformazioneDatiPratica) obj;
    if (codiceFase == null) {
      if (other.codiceFase != null)
        return false;
    } else if (!codiceFase.equals(other.codiceFase))
      return false;
    if (codiceTipoPratica == null) {
      if (other.codiceTipoPratica != null)
        return false;
    } else if (!codiceTipoPratica.equals(other.codiceTipoPratica))
      return false;
    if (definizione == null) {
      if (other.definizione != null)
        return false;
    } else if (!definizione.equals(other.definizione))
      return false;
    if (descrizione == null) {
      if (other.descrizione != null)
        return false;
    } else if (!descrizione.equals(other.descrizione))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (obbligatoria == null) {
      if (other.obbligatoria != null)
        return false;
    } else if (!obbligatoria.equals(other.obbligatoria))
      return false;
    if (ordine == null) {
      if (other.ordine != null)
        return false;
    } else if (!ordine.equals(other.ordine))
      return false;
    return true;
  }

}
