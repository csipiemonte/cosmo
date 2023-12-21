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
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import it.csi.cosmo.common.entities.proto.CosmoTEntity;
import it.csi.cosmo.common.entities.proto.CsiLogAuditedEntity;

@Entity
@Table(name = "cosmo_t_credenziali_sigillo_elettronico")
@NamedQuery(name = "CosmoTCredenzialiSigilloElettronico.findAll",
            query = "SELECT c FROM CosmoTCredenzialiSigilloElettronico c")
public class CosmoTCredenzialiSigilloElettronico extends CosmoTEntity
    implements CsiLogAuditedEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public String getPrimaryKeyRepresentation() {
    return String.valueOf(id);
  }

  @Id
  @SequenceGenerator(name = "COSMO_T_CREDENZIALI_SIGILLO_ELETTRONICO_ID_GENERATOR",
      sequenceName = "COSMO_T_CREDENZIALI_SIGILLO_ELETTRONICO_ID_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "COSMO_T_CREDENZIALI_SIGILLO_ELETTRONICO_ID_GENERATOR")
  private Long id;

  private String alias;

  @Column(name = "delegated_domain")
  private String delegatedDomain;

  @Column(name = "delegated_password")
  private String delegatedPassword;

  @Column(name = "delegated_user")
  private String delegatedUser;

  @Column(name = "otp_pwd")
  private String otpPwd;

  @Column(name = "tipo_hsm")
  private String tipoHsm;

  @Column(name = "tipo_otp_auth")
  private String tipoOtpAuth;

  private String utente;

  public CosmoTCredenzialiSigilloElettronico() {
    // empty constructor
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAlias() {
    return this.alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public String getDelegatedDomain() {
    return this.delegatedDomain;
  }

  public void setDelegatedDomain(String delegatedDomain) {
    this.delegatedDomain = delegatedDomain;
  }

  public String getDelegatedPassword() {
    return this.delegatedPassword;
  }

  public void setDelegatedPassword(String delegatedPassword) {
    this.delegatedPassword = delegatedPassword;
  }

  public String getDelegatedUser() {
    return this.delegatedUser;
  }

  public void setDelegatedUser(String delegatedUser) {
    this.delegatedUser = delegatedUser;
  }

  public String getOtpPwd() {
    return this.otpPwd;
  }

  public void setOtpPwd(String otpPwd) {
    this.otpPwd = otpPwd;
  }

  public String getTipoHsm() {
    return this.tipoHsm;
  }

  public void setTipoHsm(String tipoHsm) {
    this.tipoHsm = tipoHsm;
  }

  public String getTipoOtpAuth() {
    return this.tipoOtpAuth;
  }

  public void setTipoOtpAuth(String tipoOtpAuth) {
    this.tipoOtpAuth = tipoOtpAuth;
  }

  public String getUtente() {
    return this.utente;
  }

  public void setUtente(String utente) {
    this.utente = utente;
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
    CosmoTCredenzialiSigilloElettronico other = (CosmoTCredenzialiSigilloElettronico) obj;
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
    return "CosmoTCredenzialiSigilloElettronico [" + (id != null ? "id=" + id + ", " : "")
        + (alias != null ? "alias=" + alias + ", " : "")
        + (delegatedDomain != null ? "delegatedDomain=" + delegatedDomain + ", " : "")
        + (delegatedPassword != null ? "delegatedPassword=" + delegatedPassword + ", " : "")
        + (delegatedUser != null ? "delegatedUser=" + delegatedUser + ", " : "")
        + (otpPwd != null ? "otpPwd=" + otpPwd + ", " : "")
        + (tipoHsm != null ? "tipoHsm=" + tipoHsm + ", " : "")
        + (tipoOtpAuth != null ? "tipoOtpAuth=" + tipoOtpAuth + ", " : "")
        + (utente != null ? "utente=" + utente : "") + "]";
  }




}
