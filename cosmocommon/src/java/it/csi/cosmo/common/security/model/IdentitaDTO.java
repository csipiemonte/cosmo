/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.security.model;


public class IdentitaDTO implements java.io.Serializable {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String nome;

  private LivelloAutenticazione livelloAutenticazione;

  private String codFiscale;

  private String timestamp;

  private String mac;

  private String idProvider;

  private String rappresentazioneInterna;

  private String cognome;

  public IdentitaDTO() {
    // NOP
  }

  private IdentitaDTO(Builder builder) {
    this.nome = builder.nome;
    this.livelloAutenticazione = builder.livelloAutenticazione;
    this.codFiscale = builder.codFiscale;
    this.timestamp = builder.timestamp;
    this.mac = builder.mac;
    this.idProvider = builder.idProvider;
    this.rappresentazioneInterna = builder.rappresentazioneInterna;
    this.cognome = builder.cognome;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public LivelloAutenticazione getLivelloAutenticazione() {
    return livelloAutenticazione;
  }

  public void setLivelloAutenticazione(LivelloAutenticazione livelloAutenticazione) {
    this.livelloAutenticazione = livelloAutenticazione;
  }

  public String getCodFiscale() {
    return codFiscale;
  }

  public void setCodFiscale(String codFiscale) {
    this.codFiscale = codFiscale;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getIdProvider() {
    return idProvider;
  }

  public void setIdProvider(String idProvider) {
    this.idProvider = idProvider;
  }

  public String getRappresentazioneInterna() {
    return rappresentazioneInterna;
  }

  public void setRappresentazioneInterna(String rappresentazioneInterna) {
    this.rappresentazioneInterna = rappresentazioneInterna;
  }

  public String getCognome() {
    return cognome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  @Override
  public String toString() {
    return "IdentitaDTO [nome=" + nome + ", livelloAutenticazione=" + livelloAutenticazione
        + ", codFiscale=" + codFiscale + ", timestamp=" + timestamp + ", mac=" + mac
        + ", idProvider=" + idProvider + ", rappresentazioneInterna=" + rappresentazioneInterna
        + ", cognome=" + cognome + "]";
  }

  /**
   * Creates builder to build {@link IdentitaDTO}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IdentitaDTO}.
   */
  public static final class Builder {
    private String nome;
    private LivelloAutenticazione livelloAutenticazione;
    private String codFiscale;
    private String timestamp;
    private String mac;
    private String idProvider;
    private String rappresentazioneInterna;
    private String cognome;

    private Builder() {}

    public Builder withNome(String nome) {
      this.nome = nome;
      return this;
    }

    public Builder withLivelloAutenticazione(LivelloAutenticazione livelloAutenticazione) {
      this.livelloAutenticazione = livelloAutenticazione;
      return this;
    }

    public Builder withCodFiscale(String codFiscale) {
      this.codFiscale = codFiscale;
      return this;
    }

    public Builder withTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder withMac(String mac) {
      this.mac = mac;
      return this;
    }

    public Builder withIdProvider(String idProvider) {
      this.idProvider = idProvider;
      return this;
    }

    public Builder withRappresentazioneInterna(String rappresentazioneInterna) {
      this.rappresentazioneInterna = rappresentazioneInterna;
      return this;
    }

    public Builder withCognome(String cognome) {
      this.cognome = cognome;
      return this;
    }

    public IdentitaDTO build() {
      return new IdentitaDTO(this);
    }
  }

}
