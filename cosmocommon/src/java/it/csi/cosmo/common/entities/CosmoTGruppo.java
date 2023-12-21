/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_gruppo database table.
 *
 */
@Entity
@Table(name = "cosmo_t_gruppo")
@NamedQuery(name = "CosmoTGruppo.findAll", query = "SELECT c FROM CosmoTGruppo c")
public class CosmoTGruppo extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_GRUPPO_ID_GENERATOR", sequenceName = "COSMO_T_GRUPPO_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_GRUPPO_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice", length = 255, nullable = false)
  private String codice;

  @Column(name = "nome", length = 255, nullable = false)
  private String nome;

  @Column(name = "descrizione", length = 1000, nullable = true)
  private String descrizione;

  // bi-directional many-to-many association to CosmoTUtente
  @JsonIgnoreProperties("gruppo")
  @OneToMany(mappedBy = "gruppo")
  private List<CosmoTUtenteGruppo> associazioniUtenti;

  // bi-directional many-to-one association to CosmoRGruppoTipoPratica
  @JsonIgnoreProperties("cosmoTGruppo")
  @OneToMany(mappedBy = "cosmoTGruppo")
  private List<CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas;

  // bi-directional many-to-one association to CosmoTEnte
  @JsonIgnoreProperties("cosmoTGruppos")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_ente", nullable = false)
  private CosmoTEnte ente;

  // bi-directional many-to-one association to CosmoRPraticaUtenteGruppo
  @JsonIgnoreProperties("cosmoTGruppo")
  @OneToMany(mappedBy = "cosmoTGruppo")
  private List<CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;


  public CosmoTGruppo() {
    // empty constructor
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public List<CosmoTUtenteGruppo> getAssociazioniUtenti() {
    return associazioniUtenti;
  }

  public void setAssociazioniUtenti(List<CosmoTUtenteGruppo> associazioniUtenti) {
    this.associazioniUtenti = associazioniUtenti;
  }

  public CosmoTEnte getEnte() {
    return ente;
  }

  public void setEnte(CosmoTEnte ente) {
    this.ente = ente;
  }

  public List<CosmoRGruppoTipoPratica> getCosmoRGruppoTipoPraticas() {
    return this.cosmoRGruppoTipoPraticas;
  }

  public void setCosmoRGruppoTipoPraticas(List<CosmoRGruppoTipoPratica> cosmoRGruppoTipoPraticas) {
    this.cosmoRGruppoTipoPraticas = cosmoRGruppoTipoPraticas;
  }

  public CosmoRGruppoTipoPratica addCosmoRGruppoTipoPratica(
      CosmoRGruppoTipoPratica cosmoRGruppoTipoPratica) {
    getCosmoRGruppoTipoPraticas().add(cosmoRGruppoTipoPratica);
    cosmoRGruppoTipoPratica.setCosmoTGruppo(this);

    return cosmoRGruppoTipoPratica;
  }

  public CosmoRGruppoTipoPratica removeCosmoRGruppoTipoPratica(
      CosmoRGruppoTipoPratica cosmoRGruppoTipoPratica) {
    getCosmoRGruppoTipoPraticas().remove(cosmoRGruppoTipoPratica);
    cosmoRGruppoTipoPratica.setCosmoTGruppo(null);

    return cosmoRGruppoTipoPratica;
  }

  public List<CosmoRPraticaUtenteGruppo> getCosmoRPraticaUtenteGruppos() {
    return this.cosmoRPraticaUtenteGruppos;
  }

  public void setCosmoRPraticaUtenteGruppos(
      List<CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos) {
    this.cosmoRPraticaUtenteGruppos = cosmoRPraticaUtenteGruppos;
  }

  public CosmoRPraticaUtenteGruppo addCosmoRPraticaUtenteGruppo(
      CosmoRPraticaUtenteGruppo cosmoRPraticaUtenteGruppo) {
    getCosmoRPraticaUtenteGruppos().add(cosmoRPraticaUtenteGruppo);
    cosmoRPraticaUtenteGruppo.setCosmoTGruppo(this);

    return cosmoRPraticaUtenteGruppo;
  }

  public CosmoRPraticaUtenteGruppo removeCosmoRPraticaUtenteGruppo(
      CosmoRPraticaUtenteGruppo cosmoRPraticaUtenteGruppo) {
    getCosmoRPraticaUtenteGruppos().remove(cosmoRPraticaUtenteGruppo);
    cosmoRPraticaUtenteGruppo.setCosmoTGruppo(null);

    return cosmoRPraticaUtenteGruppo;
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
    CosmoTGruppo other = (CosmoTGruppo) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CosmoTGruppo [id=" + id + ", codice=" + codice + ", nome=" + nome + ", descrizione="
        + descrizione + "]";
  }

}
