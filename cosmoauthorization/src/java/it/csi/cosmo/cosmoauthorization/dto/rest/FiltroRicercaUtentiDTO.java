/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.List;
import java.util.Objects;
import it.csi.cosmo.common.dto.search.LongFilter;
import it.csi.cosmo.common.dto.search.StringFilter;

/**
 *
 */

public class FiltroRicercaUtentiDTO {

  private LongFilter id;
  private StringFilter email;
  private StringFilter nome;
  private StringFilter cognome;
  private LongFilter idEnte;
  private List<StringFilter> profili;
  private StringFilter codiceFiscale;
  private StringFilter codiceIpaEnte;
  private StringFilter codiceFiscaleEnte;
  private StringFilter fullText;
  private StringFilter fullName;
  private List<FiltroGruppoTagDTO> neiGruppi;
  private List<String> neiTag;

  public StringFilter getCodiceIpaEnte() {
    return codiceIpaEnte;
  }

  public void setCodiceIpaEnte(StringFilter codiceIpaEnte) {
    this.codiceIpaEnte = codiceIpaEnte;
  }

  public StringFilter getCodiceFiscaleEnte() {
    return codiceFiscaleEnte;
  }

  public void setCodiceFiscaleEnte(StringFilter codiceFiscaleEnte) {
    this.codiceFiscaleEnte = codiceFiscaleEnte;
  }

  public LongFilter getId() {
    return id;
  }

  public void setId(LongFilter id) {
    this.id = id;
  }

  public StringFilter getEmail() {
    return email;
  }

  public void setEmail(StringFilter email) {
    this.email = email;
  }

  public StringFilter getNome() {
    return nome;
  }

  public void setNome(StringFilter nome) {
    this.nome = nome;
  }

  public StringFilter getCognome() {
    return cognome;
  }

  public void setCognome(StringFilter cognome) {
    this.cognome = cognome;
  }

  public LongFilter getIdEnte() {
    return idEnte;
  }

  public void setIdEnte(LongFilter idEnte) {
    this.idEnte = idEnte;
  }

  public StringFilter getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(StringFilter codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public StringFilter getFullText() {
    return fullText;
  }

  public void setFullText(StringFilter fullText) {
    this.fullText = fullText;
  }

  public List<FiltroGruppoTagDTO> getNeiGruppi() {
    return neiGruppi;
  }

  public void setNeiGruppi(List<FiltroGruppoTagDTO> neiGruppi) {
    this.neiGruppi = neiGruppi;
  }

  public List<StringFilter> getProfili() {
    return profili;
  }

  public void setProfili(List<StringFilter> profili) {
    this.profili = profili;
  }


  public StringFilter getFullName() {
    return fullName;
  }

  public void setFullName(StringFilter fullName) {
    this.fullName = fullName;
  }

  public List<String> getNeiTag() {
    return neiTag;
  }

  public void setNeiTag(List<String> neiTag) {
    this.neiTag = neiTag;
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscale, codiceFiscaleEnte, codiceIpaEnte, cognome, email, fullName,
        fullText, id, idEnte, neiGruppi, nome, profili);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FiltroRicercaUtentiDTO other = (FiltroRicercaUtentiDTO) obj;
    return Objects.equals(codiceFiscale, other.codiceFiscale)
        && Objects.equals(codiceFiscaleEnte, other.codiceFiscaleEnte)
        && Objects.equals(codiceIpaEnte, other.codiceIpaEnte)
        && Objects.equals(cognome, other.cognome) && Objects.equals(email, other.email)
        && Objects.equals(fullName, other.fullName) && Objects.equals(fullText, other.fullText)
        && Objects.equals(id, other.id) && Objects.equals(idEnte, other.idEnte)
        && Objects.equals(neiGruppi, other.neiGruppi) && Objects.equals(nome, other.nome)
        && Objects.equals(profili, other.profili);
  }

  @Override
  public String toString() {
    return "FiltroRicercaUtentiDTO [id=" + id + ", email=" + email + ", nome=" + nome + ", cognome="
        + cognome + ", idEnte=" + idEnte + ", profili=" + profili + ", codiceFiscale="
        + codiceFiscale + ", codiceIpaEnte=" + codiceIpaEnte + ", codiceFiscaleEnte="
        + codiceFiscaleEnte + ", fullText=" + fullText + ", fullName=" + fullName + ", neiGruppi="
        + neiGruppi + "]";
  }


}
