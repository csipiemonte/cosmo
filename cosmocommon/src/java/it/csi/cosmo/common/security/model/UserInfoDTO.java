/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Classe contenente le informazioni di profilazione sull'utente corrente.
 *
 * Nota: per evitare manipolazioni indesiderate, questa classe e' immutabile e puo' essere costruita
 * attraverso l'apposito Builder.
 */
public class UserInfoDTO implements Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Long id;

  private IdentitaDTO identita;

  private Boolean anonimo;

  private String nome;

  private String cognome;

  private String codiceFiscale;

  private String email;

  private String telefono;

  private EnteDTO ente;

  private ProfiloDTO profilo;

  private List<GruppoDTO> gruppi = new LinkedList<>();

  private List<PreferenzeUtenteDTO> preferenze = new LinkedList<>();

  private OffsetDateTime fineValidita;

  private Boolean accessoDiretto;

  private UserInfoDTO(Builder builder) {
    this.id = builder.id;
    this.identita = builder.identita;
    this.anonimo = builder.anonimo;
    this.nome = builder.nome;
    this.cognome = builder.cognome;
    this.codiceFiscale = builder.codiceFiscale;
    this.preferenze = builder.preferenze;
    this.email = builder.email;
    this.telefono = builder.telefono;
    this.ente = builder.ente;
    this.profilo = builder.profilo;
    this.gruppi = builder.gruppi;
    this.fineValidita = builder.fineValidita;
    this.accessoDiretto = builder.accessoDiretto;
  }

  public UserInfoDTO() {
    // NOP
  }

  public String getHashIdentita() {
    return getCodiceFiscale() + (getEnte() != null ? ".E" + getEnte().getId() : "")
        + (getProfilo() != null ? ".P" + getProfilo().getId() : "");
  }

  public boolean hasAuthority(String requiredAuthority) {
    if (requiredAuthority == null) {
      throw new InvalidParameterException();
    }
    return profilo != null && profilo.getUseCases() != null
        && profilo.getUseCases().stream().anyMatch(o -> o.getCodice().equals(requiredAuthority));
  }

  public boolean hasAuthorities(Collection<String> requiredAuthorities) {
    if (requiredAuthorities == null) {
      throw new InvalidParameterException();
    }
    return requiredAuthorities.stream().allMatch(this::hasAuthority);
  }

  public boolean hasAnyAuthority(Collection<String> requiredAuthorities) {
    if (requiredAuthorities == null) {
      throw new InvalidParameterException();
    }
    return requiredAuthorities.stream().anyMatch(this::hasAuthority);
  }

  public boolean hasAuthorities(String... requiredAuthorities) {
    if (requiredAuthorities == null) {
      throw new InvalidParameterException();
    }
    return hasAuthorities(Arrays.asList(requiredAuthorities));
  }

  public boolean hasAnyAuthority(String... requiredAuthorities) {
    if (requiredAuthorities == null) {
      throw new InvalidParameterException();
    }
    return hasAnyAuthority(Arrays.asList(requiredAuthorities));
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OffsetDateTime getFineValidita() {
    return fineValidita;
  }

  public void setFineValidita(OffsetDateTime fineValidita) {
    this.fineValidita = fineValidita;
  }

  public IdentitaDTO getIdentita() {
    return identita;
  }

  public void setIdentita(IdentitaDTO identita) {
    this.identita = identita;
  }

  public Boolean getAnonimo() {
    return anonimo;
  }

  public void setAnonimo(Boolean anonimo) {
    this.anonimo = anonimo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCognome() {
    return cognome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public List<PreferenzeUtenteDTO> getPreferenze() {
    return preferenze;
  }

  public void setPreferenze(List<PreferenzeUtenteDTO> preferenze) {
    this.preferenze = preferenze;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public EnteDTO getEnte() {
    return ente;
  }

  public void setEnte(EnteDTO ente) {
    this.ente = ente;
  }

  public ProfiloDTO getProfilo() {
    return profilo;
  }

  public void setProfilo(ProfiloDTO profilo) {
    this.profilo = profilo;
  }

  public List<GruppoDTO> getGruppi() {
    return gruppi;
  }

  public void setGruppi(List<GruppoDTO> gruppi) {
    this.gruppi = gruppi;
  }

  public Boolean getAccessoDiretto() {
    return accessoDiretto;
  }

  public void setAccessoDiretto(Boolean accessoDiretto) {
    this.accessoDiretto = accessoDiretto;
  }

  /**
   * Creates builder to build {@link UserInfoDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link UserInfoDTO}.
   */
  public static final class Builder {
    private Long id;
    private IdentitaDTO identita;
    private Boolean anonimo;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private List<PreferenzeUtenteDTO> preferenze = Collections.emptyList();
    private String email;
    private String telefono;
    private EnteDTO ente;
    private ProfiloDTO profilo;
    private List<GruppoDTO> gruppi = Collections.emptyList();
    private OffsetDateTime fineValidita;
    private Boolean accessoDiretto;

    private Builder() {}

    public Builder withId(Long id) {
      this.id = id;
      return this;
    }

    public Builder withFineValidita(OffsetDateTime fineValidita) {
      this.fineValidita = fineValidita;
      return this;
    }

    public Builder withIdentita(IdentitaDTO identita) {
      this.identita = identita;
      return this;
    }

    public Builder withAnonimo(Boolean anonimo) {
      this.anonimo = anonimo;
      return this;
    }

    public Builder withNome(String nome) {
      this.nome = nome;
      return this;
    }

    public Builder withCognome(String cognome) {
      this.cognome = cognome;
      return this;
    }

    public Builder withCodiceFiscale(String codiceFiscale) {
      this.codiceFiscale = codiceFiscale;
      return this;
    }

    public Builder withPreferenze(List<PreferenzeUtenteDTO> preferenze) {
      this.preferenze = preferenze;
      return this;
    }

    public Builder withEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder withTelefono(String telefono) {
      this.telefono = telefono;
      return this;
    }

    public Builder withEnte(EnteDTO ente) {
      this.ente = ente;
      return this;
    }

    public Builder withProfilo(ProfiloDTO profilo) {
      this.profilo = profilo;
      return this;
    }

    public Builder withGruppi(List<GruppoDTO> gruppi) {
      this.gruppi = gruppi;
      return this;
    }

    public UserInfoDTO build() {
      return new UserInfoDTO(this);
    }

    public Builder withAccessoDiretto(Boolean accessoDiretto) {
      this.accessoDiretto = accessoDiretto;
      return this;
    }
  }

}
