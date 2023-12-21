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
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_utente database table.
 *
 */
@Entity
@Table(name = "cosmo_t_utente")
@NamedQuery(name = "CosmoTUtente.findAll", query = "SELECT c FROM CosmoTUtente c")
public class CosmoTUtente extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_UTENTE_ID_GENERATOR", sequenceName = "COSMO_T_UTENTE_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_UTENTE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_fiscale", nullable = false, length = 30)
  private String codiceFiscale;

  @Column(nullable = false, length = 255)
  private String cognome;

  @Column(nullable = false, length = 255)
  private String nome;

  // bi-directional many-to-one association to CosmoTPreferenzeUtente
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoTPreferenzeUtente> cosmoTPreferenzeUtentes;

  // bi-directional many-to-one association to CosmoRNotificaUtenteEnte
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;

  // bi-directional many-to-one association to CosmoRUtenteEnte
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoRUtenteEnte> cosmoRUtenteEntes;

  // bi-directional many-to-one association to CosmoRUtenteProfilo
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoRUtenteProfilo> cosmoRUtenteProfilos;

  // bi-directional many-to-one association to CosmoTCertificatoFirma
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas;

  // bi-directional many-to-one association to CosmoRPraticaUtenteGruppo
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoRPraticaUtenteGruppo> cosmoRPraticaUtenteGruppos;

  // bi-directional many-to-one association to CosmoRUtenteFunzionalitaApplicazioneEsterna
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas;

  // bi-directional many-to-many association to CosmoTGruppo
  @ManyToMany
  @JoinTable(name = "cosmo_t_utente_gruppo",
  joinColumns = {@JoinColumn(name = "id_utente", nullable = false)},
  inverseJoinColumns = {@JoinColumn(name = "id_gruppo", nullable = false)})
  private List<CosmoTGruppo> cosmoTGruppos;

  // bi-directional many-to-one association to CosmoTUtenteGruppo
  @OneToMany(mappedBy = "utente")
  private List<CosmoTUtenteGruppo> cosmoTUtenteGruppos;

  // bi-directional many-to-one association to CosmoTCaricamentoPratica
  @OneToMany(mappedBy = "cosmoTUtente")
  private List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;

  public CosmoTUtente() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCodiceFiscale() {
    return this.codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public String getCognome() {
    return this.cognome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public List<CosmoTPreferenzeUtente> getCosmoTPreferenzeUtentes() {
    return this.cosmoTPreferenzeUtentes;
  }

  public void setCosmoTPreferenzeUtentes(List<CosmoTPreferenzeUtente> cosmoTPreferenzeUtentes) {
    this.cosmoTPreferenzeUtentes = cosmoTPreferenzeUtentes;
  }

  public CosmoTPreferenzeUtente addCosmoTPreferenzeUtente(
      CosmoTPreferenzeUtente cosmoTPreferenzeUtente) {
    getCosmoTPreferenzeUtentes().add(cosmoTPreferenzeUtente);
    cosmoTPreferenzeUtente.setCosmoTUtente(this);

    return cosmoTPreferenzeUtente;
  }

  public CosmoTPreferenzeUtente removeCosmoTPreferenzeUtente(
      CosmoTPreferenzeUtente cosmoTPreferenzeUtente) {
    getCosmoTPreferenzeUtentes().remove(cosmoTPreferenzeUtente);
    cosmoTPreferenzeUtente.setCosmoTUtente(null);

    return cosmoTPreferenzeUtente;
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
    cosmoRNotificaUtenteEnte.setCosmoTUtente(this);

    return cosmoRNotificaUtenteEnte;
  }

  public CosmoRNotificaUtenteEnte removeCosmoRNotificaUtenteEnte(
      CosmoRNotificaUtenteEnte cosmoRNotificaUtenteEnte) {
    getCosmoRNotificaUtenteEntes().remove(cosmoRNotificaUtenteEnte);
    cosmoRNotificaUtenteEnte.setCosmoTUtente(null);

    return cosmoRNotificaUtenteEnte;
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
    cosmoRPraticaUtenteGruppo.setCosmoTUtente(this);

    return cosmoRPraticaUtenteGruppo;
  }

  public CosmoRPraticaUtenteGruppo removeCosmoRPraticaUtenteGruppo(
      CosmoRPraticaUtenteGruppo cosmoRPraticaUtenteGruppo) {
    getCosmoRPraticaUtenteGruppos().remove(cosmoRPraticaUtenteGruppo);
    cosmoRPraticaUtenteGruppo.setCosmoTUtente(null);

    return cosmoRPraticaUtenteGruppo;
  }

  public List<CosmoRUtenteEnte> getCosmoRUtenteEntes() {
    return this.cosmoRUtenteEntes;
  }

  public void setCosmoRUtenteEntes(List<CosmoRUtenteEnte> cosmoRUtenteEntes) {
    this.cosmoRUtenteEntes = cosmoRUtenteEntes;
  }

  public CosmoRUtenteEnte addCosmoRUtenteEnte(CosmoRUtenteEnte cosmoRUtenteEnte) {
    getCosmoRUtenteEntes().add(cosmoRUtenteEnte);
    cosmoRUtenteEnte.setCosmoTUtente(this);

    return cosmoRUtenteEnte;
  }

  public CosmoRUtenteEnte removeCosmoRUtenteEnte(CosmoRUtenteEnte cosmoRUtenteEnte) {
    getCosmoRUtenteEntes().remove(cosmoRUtenteEnte);
    cosmoRUtenteEnte.setCosmoTUtente(null);

    return cosmoRUtenteEnte;
  }

  public List<CosmoTCertificatoFirma> getCosmoTCertificatoFirmas() {
    return this.cosmoTCertificatoFirmas;
  }

  public void setCosmoTCertificatoFirmas(List<CosmoTCertificatoFirma> cosmoTCertificatoFirmas) {
    this.cosmoTCertificatoFirmas = cosmoTCertificatoFirmas;
  }

  public CosmoTCertificatoFirma addCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().add(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoTUtente(this);

    return cosmoTCertificatoFirma;
  }

  public CosmoTCertificatoFirma removeCosmoTCertificatoFirma(
      CosmoTCertificatoFirma cosmoTCertificatoFirma) {
    getCosmoTCertificatoFirmas().remove(cosmoTCertificatoFirma);
    cosmoTCertificatoFirma.setCosmoTUtente(null);

    return cosmoTCertificatoFirma;
  }

  public List<CosmoTGruppo> getCosmoTGruppos() {
    return this.cosmoTGruppos;
  }

  public void setCosmoTGruppos(List<CosmoTGruppo> cosmoTGruppos) {
    this.cosmoTGruppos = cosmoTGruppos;
  }

  public List<CosmoRUtenteProfilo> getCosmoRUtenteProfilos() {
    return this.cosmoRUtenteProfilos;
  }

  public void setCosmoRUtenteProfilos(List<CosmoRUtenteProfilo> cosmoRUtenteProfilos) {
    this.cosmoRUtenteProfilos = cosmoRUtenteProfilos;
  }

  public CosmoRUtenteProfilo addCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().add(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTUtente(this);

    return cosmoRUtenteProfilo;
  }

  public CosmoRUtenteProfilo removeCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().remove(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTUtente(null);

    return cosmoRUtenteProfilo;
  }

  public List<CosmoRUtenteFunzionalitaApplicazioneEsterna> getCosmoRUtenteFunzionalitaApplicazioneEsternas() {
    return this.cosmoRUtenteFunzionalitaApplicazioneEsternas;
  }

  public void setCosmoRUtenteFunzionalitaApplicazioneEsternas(
      List<CosmoRUtenteFunzionalitaApplicazioneEsterna> cosmoRUtenteFunzionalitaApplicazioneEsternas) {
    this.cosmoRUtenteFunzionalitaApplicazioneEsternas =
        cosmoRUtenteFunzionalitaApplicazioneEsternas;
  }

  public CosmoRUtenteFunzionalitaApplicazioneEsterna addCosmoRUtenteFunzionalitaApplicazioneEsterna(
      CosmoRUtenteFunzionalitaApplicazioneEsterna cosmoRUtenteFunzionalitaApplicazioneEsterna) {
    getCosmoRUtenteFunzionalitaApplicazioneEsternas()
    .add(cosmoRUtenteFunzionalitaApplicazioneEsterna);
    cosmoRUtenteFunzionalitaApplicazioneEsterna.setCosmoTUtente(this);

    return cosmoRUtenteFunzionalitaApplicazioneEsterna;
  }

  public CosmoRUtenteFunzionalitaApplicazioneEsterna removeCosmoRUtenteFunzionalitaApplicazioneEsterna(
      CosmoRUtenteFunzionalitaApplicazioneEsterna cosmoRUtenteFunzionalitaApplicazioneEsterna) {
    getCosmoRUtenteFunzionalitaApplicazioneEsternas()
    .remove(cosmoRUtenteFunzionalitaApplicazioneEsterna);
    cosmoRUtenteFunzionalitaApplicazioneEsterna.setCosmoTUtente(null);

    return cosmoRUtenteFunzionalitaApplicazioneEsterna;
  }


  public List<CosmoTUtenteGruppo> getCosmoTUtenteGruppos() {
    return this.cosmoTUtenteGruppos;
  }

  public void setCosmoTUtenteGruppos(List<CosmoTUtenteGruppo> cosmoTUtenteGruppos) {
    this.cosmoTUtenteGruppos = cosmoTUtenteGruppos;
  }

  public CosmoTUtenteGruppo addCosmoTUtenteGruppo(CosmoTUtenteGruppo cosmoTUtenteGruppo) {
    getCosmoTUtenteGruppos().add(cosmoTUtenteGruppo);
    cosmoTUtenteGruppo.setUtente(this);

    return cosmoTUtenteGruppo;
  }

  public CosmoTUtenteGruppo removeCosmoTUtenteGruppo(CosmoTUtenteGruppo cosmoTUtenteGruppo) {
    getCosmoTUtenteGruppos().remove(cosmoTUtenteGruppo);
    cosmoTUtenteGruppo.setUtente(null);

    return cosmoTUtenteGruppo;
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
    cosmoTCaricamentoPratica.setCosmoTUtente(this);

    return cosmoTCaricamentoPratica;
  }

  public CosmoTCaricamentoPratica removeCosmoTCaricamentoPratica(CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().remove(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTUtente(null);

    return cosmoTCaricamentoPratica;

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
    CosmoTUtente other = (CosmoTUtente) obj;
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
    return "CosmoTUtente [" + (id != null ? "id=" + id + ", " : "")
        + (codiceFiscale != null ? "codiceFiscale=" + codiceFiscale + ", " : "")
        + (cognome != null ? "cognome=" + cognome + ", " : "")
        + (nome != null ? "nome=" + nome + ", " : "") + "]";
  }
}
