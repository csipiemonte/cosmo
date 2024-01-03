/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

public class FiltroSigilloElettronicoDTO {

  private LongFilter id;
  private StringFilter alias;
  private StringFilter delegatedDomain;
  private StringFilter delegatedPassword;
  private StringFilter delegatedUser;
  private StringFilter otpPwd;
  private StringFilter tipoHsm;
  private StringFilter tipoOtpAuth;
  private StringFilter utente;
  private StringFilter fulltext;

  public LongFilter getId() {
    return id;
  }
  public void setId(LongFilter id) {
    this.id = id;
  }
  public StringFilter getAlias() {
    return alias;
  }
  public void setAlias(StringFilter alias) {
    this.alias = alias;
  }
  public StringFilter getDelegatedDomain() {
    return delegatedDomain;
  }
  public void setDelegatedDomain(StringFilter delegatedDomain) {
    this.delegatedDomain = delegatedDomain;
  }
  public StringFilter getDelegatedPassword() {
    return delegatedPassword;
  }
  public void setDelegatedPassword(StringFilter delegatedPassword) {
    this.delegatedPassword = delegatedPassword;
  }
  public StringFilter getDelegatedUser() {
    return delegatedUser;
  }
  public void setDelegatedUser(StringFilter delegatedUser) {
    this.delegatedUser = delegatedUser;
  }
  public StringFilter getOtpPwd() {
    return otpPwd;
  }
  public void setOtpPwd(StringFilter otpPwd) {
    this.otpPwd = otpPwd;
  }
  public StringFilter getTipoHsm() {
    return tipoHsm;
  }
  public void setTipoHsm(StringFilter tipoHsm) {
    this.tipoHsm = tipoHsm;
  }
  public StringFilter getTipoOtpAuth() {
    return tipoOtpAuth;
  }
  public void setTipoOtpAuth(StringFilter tipoOtpAuth) {
    this.tipoOtpAuth = tipoOtpAuth;
  }
  public StringFilter getUtente() {
    return utente;
  }
  public void setUtente(StringFilter utente) {
    this.utente = utente;
  }
  public StringFilter getFulltext() {
    return fulltext;
  }
  public void setFulltext(StringFilter fulltext) {
    this.fulltext = fulltext;
  }
}
