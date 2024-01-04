/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Risultato della verifica di validita' dei documenti firmati digitalmente. <br/>
 *
 * Il Signature e' la struttura dati che descrive la singola electronic signature che in base alla
 * valorizzazione di specifici attributi si distingue in firma, controfirma o marca temporale.
 *
 */
public class IndexSignature {

  private Boolean valida;

  private String certificateAuthority;

  @JsonIgnore
  private byte[] cert;

  private String codiceFiscale;

  private OffsetDateTime dataOra;

  private LocalDateTime dataOraVerifica;

  private String dipartimento;

  private String dnQualifier;

  private IndexSignatureVerificationError errorCode;

  private String fineValidita;

  private String firmatario;

  private String givenName;

  private String surname;

  private String inizioValidita;

  private String nominativoFirmatario;

  private Long numeroControfirme;

  private String organizzazione;

  private String paese;

  private String serialNumber;

  private Boolean timestamped;

  private List<IndexSignature> signature;

  private TipoCertificato tipoCertificato;

  private TipoFirma tipoFirma;

  private IndexSignature(Builder builder) {
    this.valida = builder.valida;
    this.certificateAuthority = builder.certificateAuthority;
    this.cert = builder.cert;
    this.codiceFiscale = builder.codiceFiscale;
    this.dataOra = builder.dataOra;
    this.dataOraVerifica = builder.dataOraVerifica;
    this.dipartimento = builder.dipartimento;
    this.dnQualifier = builder.dnQualifier;
    this.errorCode = builder.errorCode;
    this.fineValidita = builder.fineValidita;
    this.firmatario = builder.firmatario;
    this.givenName = builder.givenName;
    this.surname = builder.surname;
    this.inizioValidita = builder.inizioValidita;
    this.nominativoFirmatario = builder.nominativoFirmatario;
    this.numeroControfirme = builder.numeroControfirme;
    this.organizzazione = builder.organizzazione;
    this.paese = builder.paese;
    this.serialNumber = builder.serialNumber;
    this.timestamped = builder.timestamped;
    this.signature = builder.signature;
    this.tipoCertificato = builder.tipoCertificato;
    this.tipoFirma = builder.tipoFirma;
  }

  public IndexSignature() {
    // NOP
  }

  @Override
  public String toString() {
    return "IndexSignature [" + (valida != null ? "valida=" + valida + ", " : "")
        + (certificateAuthority != null ? "certificateAuthority=" + certificateAuthority + ", "
            : "")
        + (codiceFiscale != null ? "codiceFiscale=" + codiceFiscale + ", " : "")
        + (errorCode != null ? "errorCode=" + errorCode + ", " : "")
        + (tipoCertificato != null ? "tipoCertificato=" + tipoCertificato + ", " : "")
        + (tipoFirma != null ? "tipoFirma=" + tipoFirma : "") + "]";
  }

  public Boolean getValida() {
    return valida;
  }

  public Boolean getTimestamped() {
    return timestamped;
  }

  public String getCertificateAuthority() {
    return certificateAuthority;
  }

  @JsonIgnore
  public byte[] getCert() {
    return cert;
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public OffsetDateTime getDataOra() {
    return dataOra;
  }

  public LocalDateTime getDataOraVerifica() {
    return dataOraVerifica;
  }

  public String getDipartimento() {
    return dipartimento;
  }

  public String getDnQualifier() {
    return dnQualifier;
  }

  public IndexSignatureVerificationError getErrorCode() {
    return errorCode;
  }

  public String getFineValidita() {
    return fineValidita;
  }

  public String getFirmatario() {
    return firmatario;
  }

  public String getGivenName() {
    return givenName;
  }

  public String getSurname() {
    return surname;
  }

  public String getInizioValidita() {
    return inizioValidita;
  }

  public String getNominativoFirmatario() {
    return nominativoFirmatario;
  }

  public Long getNumeroControfirme() {
    return numeroControfirme;
  }

  public String getOrganizzazione() {
    return organizzazione;
  }

  public String getPaese() {
    return paese;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public List<IndexSignature> getSignature() {
    return signature;
  }

  public TipoCertificato getTipoCertificato() {
    return tipoCertificato;
  }

  public TipoFirma getTipoFirma() {
    return tipoFirma;
  }

  /**
   * Creates builder to build {@link IndexSignature}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IndexSignature}.
   */
  public static final class Builder {
    private Boolean valida;
    private String certificateAuthority;
    private byte[] cert;
    private String codiceFiscale;
    private OffsetDateTime dataOra;
    private LocalDateTime dataOraVerifica;
    private String dipartimento;
    private String dnQualifier;
    private IndexSignatureVerificationError errorCode;
    private String fineValidita;
    private String firmatario;
    private String givenName;
    private String surname;
    private String inizioValidita;
    private String nominativoFirmatario;
    private Long numeroControfirme;
    private String organizzazione;
    private String paese;
    private String serialNumber;
    private Boolean timestamped;
    private List<IndexSignature> signature = Collections.emptyList();
    private TipoCertificato tipoCertificato;
    private TipoFirma tipoFirma;

    private Builder() {}

    public Builder withValida(Boolean valida) {
      this.valida = valida;
      return this;
    }

    public Builder withCertificateAuthority(String certificateAuthority) {
      this.certificateAuthority = certificateAuthority;
      return this;
    }

    public Builder withCert(byte[] cert) {
      this.cert = cert;
      return this;
    }

    public Builder withCodiceFiscale(String codiceFiscale) {
      this.codiceFiscale = codiceFiscale;
      return this;
    }

    public Builder withDataOra(OffsetDateTime dataOra) {
      this.dataOra = dataOra;
      return this;
    }

    public Builder withDataOraVerifica(LocalDateTime dataOraVerifica) {
      this.dataOraVerifica = dataOraVerifica;
      return this;
    }

    public Builder withDipartimento(String dipartimento) {
      this.dipartimento = dipartimento;
      return this;
    }

    public Builder withDnQualifier(String dnQualifier) {
      this.dnQualifier = dnQualifier;
      return this;
    }

    public Builder withErrorCode(IndexSignatureVerificationError errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public Builder withFineValidita(String fineValidita) {
      this.fineValidita = fineValidita;
      return this;
    }

    public Builder withFirmatario(String firmatario) {
      this.firmatario = firmatario;
      return this;
    }

    public Builder withGivenName(String givenName) {
      this.givenName = givenName;
      return this;
    }

    public Builder withSurname(String surname) {
      this.surname = surname;
      return this;
    }

    public Builder withInizioValidita(String inizioValidita) {
      this.inizioValidita = inizioValidita;
      return this;
    }

    public Builder withNominativoFirmatario(String nominativoFirmatario) {
      this.nominativoFirmatario = nominativoFirmatario;
      return this;
    }

    public Builder withNumeroControfirme(Long numeroControfirme) {
      this.numeroControfirme = numeroControfirme;
      return this;
    }

    public Builder withOrganizzazione(String organizzazione) {
      this.organizzazione = organizzazione;
      return this;
    }

    public Builder withPaese(String paese) {
      this.paese = paese;
      return this;
    }

    public Builder withSerialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
      return this;
    }

    public Builder withTimestamped(Boolean timestamped) {
      this.timestamped = timestamped;
      return this;
    }

    public Builder withSignature(List<IndexSignature> signature) {
      this.signature = signature;
      return this;
    }

    public Builder withTipoCertificato(TipoCertificato tipoCertificato) {
      this.tipoCertificato = tipoCertificato;
      return this;
    }

    public Builder withTipoFirma(TipoFirma tipoFirma) {
      this.tipoFirma = tipoFirma;
      return this;
    }

    public IndexSignature build() {
      return new IndexSignature(this);
    }
  }

}
