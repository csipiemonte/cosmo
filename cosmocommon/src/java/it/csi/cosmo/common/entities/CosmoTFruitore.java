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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Where;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_fruitore database table.
 *
 */
@Entity
@Table(name = "cosmo_t_fruitore")
@NamedQuery(name = "CosmoTFruitore.findAll", query = "SELECT c FROM CosmoTFruitore c")
public class CosmoTFruitore extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_FRUITORE_ID_GENERATOR",
  sequenceName = "COSMO_T_FRUITORE_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_FRUITORE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "api_manager_id", unique = true, nullable = false, length = 50)
  private String apiManagerId;

  @Column(name = "nome_app", length = 50)
  private String nomeApp;

  @Column(length = 255)
  private String url;

  // bi-directional many-to-one association to CosmoTNotifica
  @OneToMany(mappedBy = "cosmoTFruitore")
  private List<CosmoTNotifica> cosmoTNotificas;

  // bi-directional many-to-one association to CosmoTPratica
  @OneToMany(mappedBy = "fruitore")
  private List<CosmoTPratica> cosmoTPraticas;

  // bi-directional many-to-many association to CosmoDAutorizzazioneFruitore
  @ManyToMany
  @JoinTable(name = "cosmo_r_fruitore_autorizzazione",
  joinColumns = {@JoinColumn(name = "id_fruitore")},
  inverseJoinColumns = {@JoinColumn(name = "codice")})
  private List<CosmoDAutorizzazioneFruitore> cosmoDAutorizzazioneFruitores;

  // bi-directional many-to-one association to CosmoRFruitoreEnte
  @OneToMany(mappedBy = "cosmoTFruitore")
  private List<CosmoRFruitoreEnte> cosmoRFruitoreEntes;

  // bi-directional many-to-one association to CosmoTNotifica
  @OneToMany(mappedBy = "fruitore")
  @Where(clause = "dt_cancellazione is NULL")
  private List<CosmoTSchemaAutenticazioneFruitore> schemiAutenticazione;

  // bi-directional many-to-one association to CosmoTNotifica
  @OneToMany(mappedBy = "fruitore")
  @Where(clause = "dt_cancellazione is NULL")
  private List<CosmoTEndpointFruitore> endpoints;

  public CosmoTFruitore() {
    // empty constructor
  }

  public List<CosmoTSchemaAutenticazioneFruitore> getSchemiAutenticazione() {
    return schemiAutenticazione;
  }

  public void setSchemiAutenticazione(
      List<CosmoTSchemaAutenticazioneFruitore> schemiAutenticazione) {
    this.schemiAutenticazione = schemiAutenticazione;
  }

  public List<CosmoTEndpointFruitore> getEndpoints() {
    return endpoints;
  }

  public void setEndpoints(List<CosmoTEndpointFruitore> endpoints) {
    this.endpoints = endpoints;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getApiManagerId() {
    return this.apiManagerId;
  }

  public void setApiManagerId(String apiManagerId) {
    this.apiManagerId = apiManagerId;
  }

  public String getNomeApp() {
    return this.nomeApp;
  }

  public void setNomeApp(String nomeApp) {
    this.nomeApp = nomeApp;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<CosmoTNotifica> getCosmoTNotificas() {
    return this.cosmoTNotificas;
  }

  public void setCosmoTNotificas(List<CosmoTNotifica> cosmoTNotificas) {
    this.cosmoTNotificas = cosmoTNotificas;
  }

  public CosmoTNotifica addCosmoTNotifica(CosmoTNotifica cosmoTNotifica) {
    getCosmoTNotificas().add(cosmoTNotifica);
    cosmoTNotifica.setCosmoTFruitore(this);

    return cosmoTNotifica;
  }

  public CosmoTNotifica removeCosmoTNotifica(CosmoTNotifica cosmoTNotifica) {
    getCosmoTNotificas().remove(cosmoTNotifica);
    cosmoTNotifica.setCosmoTFruitore(null);

    return cosmoTNotifica;
  }

  public List<CosmoTPratica> getCosmoTPraticas() {
    return this.cosmoTPraticas;
  }

  public void setCosmoTPraticas(List<CosmoTPratica> cosmoTPraticas) {
    this.cosmoTPraticas = cosmoTPraticas;
  }

  public CosmoTPratica addCosmoTPratica(CosmoTPratica cosmoTPratica) {
    getCosmoTPraticas().add(cosmoTPratica);
    cosmoTPratica.setFruitore(this);

    return cosmoTPratica;
  }

  public CosmoTPratica removeCosmoTPratica(CosmoTPratica cosmoTPratica) {
    getCosmoTPraticas().remove(cosmoTPratica);
    cosmoTPratica.setFruitore(null);

    return cosmoTPratica;
  }

  public List<CosmoDAutorizzazioneFruitore> getCosmoDAutorizzazioneFruitores() {
    return this.cosmoDAutorizzazioneFruitores;
  }

  public void setCosmoDAutorizzazioneFruitores(
      List<CosmoDAutorizzazioneFruitore> cosmoDAutorizzazioneFruitores) {
    this.cosmoDAutorizzazioneFruitores = cosmoDAutorizzazioneFruitores;
  }

  public List<CosmoRFruitoreEnte> getCosmoRFruitoreEntes() {
    return this.cosmoRFruitoreEntes;
  }

  public void setCosmoRFruitoreEntes(List<CosmoRFruitoreEnte> cosmoRFruitoreEntes) {
    this.cosmoRFruitoreEntes = cosmoRFruitoreEntes;
  }

  public CosmoRFruitoreEnte addCosmoRFruitoreEnte(CosmoRFruitoreEnte cosmoRFruitoreEnte) {
    getCosmoRFruitoreEntes().add(cosmoRFruitoreEnte);
    cosmoRFruitoreEnte.setCosmoTFruitore(this);

    return cosmoRFruitoreEnte;
  }

  public CosmoRFruitoreEnte removeCosmoRFruitoreEnte(CosmoRFruitoreEnte cosmoRFruitoreEnte) {
    getCosmoRFruitoreEntes().remove(cosmoRFruitoreEnte);
    cosmoRFruitoreEnte.setCosmoTFruitore(null);

    return cosmoRFruitoreEnte;
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
    CosmoTFruitore other = (CosmoTFruitore) obj;
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
    return "CosmoTFruitore [" + (id != null ? "id=" + id + ", " : "")
        + (apiManagerId != null ? "apiManagerId=" + apiManagerId + ", " : "")
        + (nomeApp != null ? "nomeApp=" + nomeApp + ", " : "")
        + "]";
  }

}
