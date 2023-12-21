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
 * The persistent class for the cosmo_t_ente database table.
 *
 */
@Entity
@Table(name = "cosmo_t_ente")
@NamedQuery(name = "CosmoTEnte.findAll", query = "SELECT c FROM CosmoTEnte c")
public class CosmoTEnte extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_ENTE_ID_GENERATOR", sequenceName = "COSMO_T_ENTE_ID_SEQ",
  allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COSMO_T_ENTE_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(name = "codice_ipa", length = 10, nullable = false)
  private String codiceIpa;

  @Column(name = "codice_fiscale", length = 16)
  private String codiceFiscale;

  // @Lob
  @Column(name = "logo")
  private byte[] logo;

  @Column(nullable = false, length = 255)
  private String nome;

  // bi-directional many-to-one association to CosmoCConfigurazioneEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoCConfigurazioneEnte> cosmoCConfigurazioneEntes;


  // bi-directional many-to-one association to CosmoTPreferenzeEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoTPreferenzeEnte> cosmoTPreferenzeEntes;

  // bi-directional many-to-one association to CosmoRUtenteEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoRUtenteEnte> cosmoRUtenteEntes;

  // bi-directional many-to-one association to CosmoRUtenteProfilo
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoRUtenteProfilo> cosmoRUtenteProfilos;

  // bi-directional many-to-one association to CosmoTGruppo
  @OneToMany(mappedBy = "ente")
  private List<CosmoTGruppo> cosmoTGruppos;

  // bi-directional many-to-one association to CosmoRFruitoreEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoRFruitoreEnte> cosmoRFruitoreEntes;

  // bi-directional many-to-one association to CosmoTPratica
  @OneToMany(mappedBy = "ente")
  private List<CosmoTPratica> cosmoTPraticas;

  // bi-directional many-to-one association to CosmoREnteCertificatoreEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes;

  // bi-directional many-to-one association to CosmoREnteApplicazioneEsterna
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoREnteApplicazioneEsterna> cosmoREnteApplicazioneEsternas;

  // bi-directional many-to-one association to CosmoREnteFunzionalitaApplicazioneEsterna
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoREnteFunzionalitaApplicazioneEsterna> cosmoREnteFunzionalitaApplicazioneEsternas;

  // bi-directional many-to-one association to CosmoRNotificaUtenteEnte
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoRNotificaUtenteEnte> cosmoRNotificaUtenteEntes;

  // bi-directional many-to-one association to CosmoTFormLogico
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoTFormLogico> cosmoTFormLogicos;

  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas;

  // bi-directional many-to-one association to CosmoTOtp
  @OneToMany(mappedBy = "cosmoTEnte")
  private List<CosmoTOtp> cosmoTOtps;

  public CosmoTEnte() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public byte[] getLogo() {
    return this.logo;
  }

  public void setLogo(byte[] logo) {
    this.logo = logo;
  }

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCodiceIpa() {
    return this.codiceIpa;
  }

  public void setCodiceIpa(String codiceIpa) {
    this.codiceIpa = codiceIpa;
  }

  public List<CosmoTPreferenzeEnte> getCosmoTPreferenzeEntes() {
    return this.cosmoTPreferenzeEntes;
  }

  public void setCosmoTPreferenzeEntes(List<CosmoTPreferenzeEnte> cosmoTPreferenzeEntes) {
    this.cosmoTPreferenzeEntes = cosmoTPreferenzeEntes;
  }

  public CosmoTPreferenzeEnte addCosmoTPreferenzeEnte(CosmoTPreferenzeEnte cosmoTPreferenzeEnte) {
    getCosmoTPreferenzeEntes().add(cosmoTPreferenzeEnte);
    cosmoTPreferenzeEnte.setCosmoTEnte(this);

    return cosmoTPreferenzeEnte;
  }

  public CosmoTPreferenzeEnte removeCosmoTPreferenzeEnte(
      CosmoTPreferenzeEnte cosmoTPreferenzeEnte) {
    getCosmoTPreferenzeEntes().remove(cosmoTPreferenzeEnte);
    cosmoTPreferenzeEnte.setCosmoTEnte(null);

    return cosmoTPreferenzeEnte;
  }

  public List<CosmoRUtenteEnte> getCosmoRUtenteEntes() {
    return this.cosmoRUtenteEntes;
  }

  public void setCosmoRUtenteEntes(List<CosmoRUtenteEnte> cosmoRUtenteEntes) {
    this.cosmoRUtenteEntes = cosmoRUtenteEntes;
  }

  public CosmoRUtenteEnte addCosmoRUtenteEnte(CosmoRUtenteEnte cosmoRUtenteEnte) {
    getCosmoRUtenteEntes().add(cosmoRUtenteEnte);
    cosmoRUtenteEnte.setCosmoTEnte(this);

    return cosmoRUtenteEnte;
  }

  public CosmoRUtenteEnte removeCosmoRUtenteEnte(CosmoRUtenteEnte cosmoRUtenteEnte) {
    getCosmoRUtenteEntes().remove(cosmoRUtenteEnte);
    cosmoRUtenteEnte.setCosmoTEnte(null);

    return cosmoRUtenteEnte;
  }

  public List<CosmoRUtenteProfilo> getCosmoRUtenteProfilos() {
    return this.cosmoRUtenteProfilos;
  }

  public void setCosmoRUtenteProfilos(List<CosmoRUtenteProfilo> cosmoRUtenteProfilos) {
    this.cosmoRUtenteProfilos = cosmoRUtenteProfilos;
  }

  public CosmoRUtenteProfilo addCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().add(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTEnte(this);

    return cosmoRUtenteProfilo;
  }

  public CosmoRUtenteProfilo removeCosmoRUtenteProfilo(CosmoRUtenteProfilo cosmoRUtenteProfilo) {
    getCosmoRUtenteProfilos().remove(cosmoRUtenteProfilo);
    cosmoRUtenteProfilo.setCosmoTEnte(null);

    return cosmoRUtenteProfilo;
  }

  public List<CosmoTGruppo> getCosmoTGruppos() {
    return this.cosmoTGruppos;
  }

  public void setCosmoTGruppos(List<CosmoTGruppo> cosmoTGruppos) {
    this.cosmoTGruppos = cosmoTGruppos;
  }

  public CosmoTGruppo addCosmoTGruppo(CosmoTGruppo cosmoTGruppo) {
    getCosmoTGruppos().add(cosmoTGruppo);
    cosmoTGruppo.setEnte(this);

    return cosmoTGruppo;
  }

  public CosmoTGruppo removeCosmoTGruppo(CosmoTGruppo cosmoTGruppo) {
    getCosmoTGruppos().remove(cosmoTGruppo);
    cosmoTGruppo.setEnte(null);

    return cosmoTGruppo;
  }

  public List<CosmoRFruitoreEnte> getCosmoRFruitoreEntes() {
    return this.cosmoRFruitoreEntes;
  }

  public void setCosmoRFruitoreEntes(List<CosmoRFruitoreEnte> cosmoRFruitoreEntes) {
    this.cosmoRFruitoreEntes = cosmoRFruitoreEntes;
  }

  public CosmoRFruitoreEnte addCosmoRFruitoreEnte(CosmoRFruitoreEnte cosmoRFruitoreEnte) {
    getCosmoRFruitoreEntes().add(cosmoRFruitoreEnte);
    cosmoRFruitoreEnte.setCosmoTEnte(this);

    return cosmoRFruitoreEnte;
  }

  public CosmoRFruitoreEnte removeCosmoRFruitoreEnte(CosmoRFruitoreEnte cosmoRFruitoreEnte) {
    getCosmoRFruitoreEntes().remove(cosmoRFruitoreEnte);
    cosmoRFruitoreEnte.setCosmoTEnte(null);

    return cosmoRFruitoreEnte;
  }

  public List<CosmoTPratica> getCosmoTPraticas() {
    return this.cosmoTPraticas;
  }

  public void setCosmoTPraticas(List<CosmoTPratica> cosmoTPraticas) {
    this.cosmoTPraticas = cosmoTPraticas;
  }

  public List<CosmoREnteCertificatoreEnte> getCosmoREnteCertificatoreEntes() {
    return this.cosmoREnteCertificatoreEntes;
  }

  public void setCosmoREnteCertificatoreEntes(
      List<CosmoREnteCertificatoreEnte> cosmoREnteCertificatoreEntes) {
    this.cosmoREnteCertificatoreEntes = cosmoREnteCertificatoreEntes;
  }

  public CosmoREnteCertificatoreEnte addCosmoREnteCertificatoreEnte(
      CosmoREnteCertificatoreEnte cosmoREnteCertificatoreEnte) {
    getCosmoREnteCertificatoreEntes().add(cosmoREnteCertificatoreEnte);
    cosmoREnteCertificatoreEnte.setCosmoTEnte(this);

    return cosmoREnteCertificatoreEnte;
  }

  public CosmoREnteCertificatoreEnte removeCosmoREnteCertificatoreEnte(
      CosmoREnteCertificatoreEnte cosmoREnteCertificatoreEnte) {
    getCosmoREnteCertificatoreEntes().remove(cosmoREnteCertificatoreEnte);
    cosmoREnteCertificatoreEnte.setCosmoTEnte(null);

    return cosmoREnteCertificatoreEnte;
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
    cosmoREnteApplicazioneEsterna.setCosmoTEnte(this);

    return cosmoREnteApplicazioneEsterna;
  }

  public CosmoREnteApplicazioneEsterna removeCosmoREnteApplicazioneEsterna(
      CosmoREnteApplicazioneEsterna cosmoREnteApplicazioneEsterna) {
    getCosmoREnteApplicazioneEsternas().remove(cosmoREnteApplicazioneEsterna);
    cosmoREnteApplicazioneEsterna.setCosmoTEnte(null);

    return cosmoREnteApplicazioneEsterna;
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
    cosmoREnteFunzionalitaApplicazioneEsterna.setCosmoTEnte(this);

    return cosmoREnteFunzionalitaApplicazioneEsterna;
  }

  public CosmoREnteFunzionalitaApplicazioneEsterna removeCosmoREnteFunzionalitaApplicazioneEsterna(
      CosmoREnteFunzionalitaApplicazioneEsterna cosmoREnteFunzionalitaApplicazioneEsterna) {
    getCosmoREnteFunzionalitaApplicazioneEsternas()
    .remove(cosmoREnteFunzionalitaApplicazioneEsterna);
    cosmoREnteFunzionalitaApplicazioneEsterna.setCosmoTEnte(null);

    return cosmoREnteFunzionalitaApplicazioneEsterna;
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
    cosmoRNotificaUtenteEnte.setCosmoTEnte(this);

    return cosmoRNotificaUtenteEnte;
  }

  public CosmoRNotificaUtenteEnte removeCosmoRNotificaUtenteEnte(
      CosmoRNotificaUtenteEnte cosmoRNotificaUtenteEnte) {
    getCosmoRNotificaUtenteEntes().remove(cosmoRNotificaUtenteEnte);
    cosmoRNotificaUtenteEnte.setCosmoTEnte(null);

    return cosmoRNotificaUtenteEnte;
  }

  public List<CosmoTFormLogico> getCosmoTFormLogicos() {
    return this.cosmoTFormLogicos;
  }

  public void setCosmoTFormLogicos(List<CosmoTFormLogico> cosmoTFormLogicos) {
    this.cosmoTFormLogicos = cosmoTFormLogicos;
  }

  public CosmoTFormLogico addCosmoTFormLogico(CosmoTFormLogico cosmoTFormLogico) {
    getCosmoTFormLogicos().add(cosmoTFormLogico);
    cosmoTFormLogico.setCosmoTEnte(this);

    return cosmoTFormLogico;
  }

  public CosmoTFormLogico removeCosmoTFormLogico(CosmoTFormLogico cosmoTFormLogico) {
    getCosmoTFormLogicos().remove(cosmoTFormLogico);
    cosmoTFormLogico.setCosmoTEnte(null);

    return cosmoTFormLogico;
  }



  public List<CosmoTCaricamentoPratica> getCosmoTCaricamentoPraticas() {
    return cosmoTCaricamentoPraticas;
  }

  public void setCosmoTCaricamentoPraticas(
      List<CosmoTCaricamentoPratica> cosmoTCaricamentoPraticas) {
    this.cosmoTCaricamentoPraticas = cosmoTCaricamentoPraticas;
  }

  public CosmoTCaricamentoPratica addCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().add(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTEnte(this);

    return cosmoTCaricamentoPratica;
  }

  public CosmoTCaricamentoPratica removeCosmoTCaricamentoPratica(
      CosmoTCaricamentoPratica cosmoTCaricamentoPratica) {
    getCosmoTCaricamentoPraticas().remove(cosmoTCaricamentoPratica);
    cosmoTCaricamentoPratica.setCosmoTEnte(null);

    return cosmoTCaricamentoPratica;
  }



  public List<CosmoCConfigurazioneEnte> getCosmoCConfigurazioneEntes() {
    return this.cosmoCConfigurazioneEntes;
  }

  public void setCosmoCConfigurazioneEntes(
      List<CosmoCConfigurazioneEnte> cosmoCConfigurazioneEntes) {
    this.cosmoCConfigurazioneEntes = cosmoCConfigurazioneEntes;
  }

  public CosmoCConfigurazioneEnte addCosmoCConfigurazioneEnte(
      CosmoCConfigurazioneEnte cosmoCConfigurazioneEnte) {
    getCosmoCConfigurazioneEntes().add(cosmoCConfigurazioneEnte);
    cosmoCConfigurazioneEnte.setCosmoTEnte(this);

    return cosmoCConfigurazioneEnte;
  }


  public List<CosmoTOtp> getCosmoTOtps() {
    return this.cosmoTOtps;
  }

  public void setCosmoTOtps(List<CosmoTOtp> cosmoTOtps) {
    this.cosmoTOtps = cosmoTOtps;
  }

  public CosmoTOtp addCosmoTOtp(CosmoTOtp cosmoTOtp) {
    getCosmoTOtps().add(cosmoTOtp);
    cosmoTOtp.setCosmoTEnte(this);

    return cosmoTOtp;
  }

  public CosmoTOtp removeCosmoTOtp(CosmoTOtp cosmoTOtp) {
    getCosmoTOtps().remove(cosmoTOtp);
    cosmoTOtp.setCosmoTEnte(null);

    return cosmoTOtp;
  }

  public CosmoCConfigurazioneEnte removeCosmoCConfigurazioneEnte(
      CosmoCConfigurazioneEnte cosmoCConfigurazioneEnte) {
    getCosmoCConfigurazioneEntes().remove(cosmoCConfigurazioneEnte);
    cosmoCConfigurazioneEnte.setCosmoTEnte(null);

    return cosmoCConfigurazioneEnte;
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
    CosmoTEnte other = (CosmoTEnte) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }


  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  @Override
  public String toString() {
    return "CosmoTEnte [id=" + id + ", codiceIpa=" + codiceIpa + ", codiceFiscale=" + codiceFiscale
        + ", nome=" + nome + "]";
  }


}
