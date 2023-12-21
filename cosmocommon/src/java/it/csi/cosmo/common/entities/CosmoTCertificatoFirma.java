/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;


/**
 * The persistent class for the cosmo_t_certificato_firma database table.
 *
 */
@Entity
@Table(name = "cosmo_t_certificato_firma")
@NamedQuery(name = "CosmoTcertificatoFirma.findAll",
query = "SELECT c FROM CosmoTCertificatoFirma c")
public class CosmoTCertificatoFirma extends CosmoTEntity implements CsiLogAuditedEntity {
  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CERTIFICATO_FIRMA_ID_GENERATOR",
  sequenceName = "COSMO_T_CERTIFICATO_FIRMA_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
  generator = "COSMO_T_CERTIFICATO_FIRMA_ID_GENERATOR")
  @Column(unique = true, nullable = false)
  private Long id;

  @Column(length = 100, nullable = false)
  private String descrizione;

  @Column(length = 100, nullable = false)
  private String pin;

  @Column(length = 100, nullable = true)
  private String password;

  @Column(name = "dt_scadenza")
  protected String dtScadenza;

  @Column(length = 100, nullable = false)
  private String username;

  //bi-directional many-to-one association to CosmoDEnteCertificatore
  @ManyToOne
  @JoinColumn(name="ente_certificatore")
  private CosmoDEnteCertificatore cosmoDEnteCertificatore;

  //bi-directional many-to-one association to CosmoDProfiloFeq
  @ManyToOne
  @JoinColumn(name="profilo_feq")
  private CosmoDProfiloFeq cosmoDProfiloFeq;

  //bi-directional many-to-one association to CosmoDSceltaMarcaTemporale
  @ManyToOne
  @JoinColumn(name="scelta_temporale")
  private CosmoDSceltaMarcaTemporale cosmoDSceltaMarcaTemporale;

  //bi-directional many-to-one association to CosmoDTipoCredenzialiFirma
  @ManyToOne
  @JoinColumn(name="tipo_credenziali_firma")
  private CosmoDTipoCredenzialiFirma cosmoDTipoCredenzialiFirma;

  //bi-directional many-to-one association to CosmoDTipoOtp
  @ManyToOne
  @JoinColumn(name="tipo_otp")
  private CosmoDTipoOtp cosmoDTipoOtp;

  //bi-directional many-to-one association to CosmoTUtente
  @ManyToOne
  @JoinColumn(name="id_utente")
  private CosmoTUtente cosmoTUtente;
  
  @Column(name = "ultimo_utilizzato", nullable = false)
  private Boolean ultimoUtilizzato;

  public CosmoTCertificatoFirma() {
    // empty
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

  public String getPin() {
    return this.pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDtScadenza() {
    return this.dtScadenza;
  }

  public void setDtScadenza(String dtScadenza) {
    this.dtScadenza = dtScadenza;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public CosmoDEnteCertificatore getCosmoDEnteCertificatore() {
    return this.cosmoDEnteCertificatore;
  }

  public void setCosmoDEnteCertificatore(CosmoDEnteCertificatore cosmoDEnteCertificatore) {
    this.cosmoDEnteCertificatore = cosmoDEnteCertificatore;
  }

  public CosmoDProfiloFeq getCosmoDProfiloFeq() {
    return this.cosmoDProfiloFeq;
  }

  public void setCosmoDProfiloFeq(CosmoDProfiloFeq cosmoDProfiloFeq) {
    this.cosmoDProfiloFeq = cosmoDProfiloFeq;
  }

  public CosmoDSceltaMarcaTemporale getCosmoDSceltaMarcaTemporale() {
    return this.cosmoDSceltaMarcaTemporale;
  }

  public void setCosmoDSceltaMarcaTemporale(CosmoDSceltaMarcaTemporale cosmoDSceltaMarcaTemporale) {
    this.cosmoDSceltaMarcaTemporale = cosmoDSceltaMarcaTemporale;
  }

  public CosmoDTipoCredenzialiFirma getCosmoDTipoCredenzialiFirma() {
    return this.cosmoDTipoCredenzialiFirma;
  }

  public void setCosmoDTipoCredenzialiFirma(CosmoDTipoCredenzialiFirma cosmoDTipoCredenzialiFirma) {
    this.cosmoDTipoCredenzialiFirma = cosmoDTipoCredenzialiFirma;
  }

  public CosmoDTipoOtp getCosmoDTipoOtp() {
    return this.cosmoDTipoOtp;
  }

  public void setCosmoDTipoOtp(CosmoDTipoOtp cosmoDTipoOtp) {
    this.cosmoDTipoOtp = cosmoDTipoOtp;
  }

  public CosmoTUtente getCosmoTUtente() {
    return this.cosmoTUtente;
  }

  public void setCosmoTUtente(CosmoTUtente cosmoTUtente) {
    this.cosmoTUtente = cosmoTUtente;
  }

  public Boolean getUltimoUtilizzato() {
    return ultimoUtilizzato;
  }

  public void setUltimoUtilizzato(Boolean ultimoUtilizzato) {
    this.ultimoUtilizzato = ultimoUtilizzato;
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
    CosmoTCertificatoFirma other = (CosmoTCertificatoFirma) obj;
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
    return "CosmoTCertificatoFirma [" + (id != null ? "id=" + id + ", " : "")
        + (descrizione != null ? "descrizione=" + descrizione + ", " : "") + "]";
  }



}
