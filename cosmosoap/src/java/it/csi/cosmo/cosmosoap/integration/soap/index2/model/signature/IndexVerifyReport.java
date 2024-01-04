/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.model.signature;

import java.util.Collections;
import java.util.List;

/**
 * Risultato della verifica di validita' dei documenti firmati digitalmente. <br/>
 *
 * Il VerifyReport e' la struttura dati che descrive la busta crittografica xAdES-x ovvero l'insieme
 * di firme semplici, parallele e controfirme con le eventuali marche temporali. <br/>
 *
 * Per ogni firma sono riportati i dati dei certificati X509 coinvolti insieme all'esito delle
 * verifiche.
 */
public class IndexVerifyReport {

  private Boolean valida;

  private IndexVerifyReport child;

  private ConformitaParametriInput conformitaParametriInput;

  private IndexSignatureVerificationError errorCode;

  private FormatoFirma formatoFirma;

  private Integer numCertificatiFirma;

  private Integer numCertificatiMarca;

  private List<IndexSignature> signature;

  private TipoFirma tipoFirma;

  private IndexVerifyReport(Builder builder) {
    this.valida = builder.valida;
    this.child = builder.child;
    this.conformitaParametriInput = builder.conformitaParametriInput;
    this.errorCode = builder.errorCode;
    this.formatoFirma = builder.formatoFirma;
    this.numCertificatiFirma = builder.numCertificatiFirma;
    this.numCertificatiMarca = builder.numCertificatiMarca;
    this.signature = builder.signature;
    this.tipoFirma = builder.tipoFirma;
  }

  public IndexVerifyReport() {
    // NOP
  }

  public Boolean getValida() {
    return valida;
  }

  /**
   * Get il risultato della verifica di validita' della eventuale busta crittografica annidata
   * ovvero siamo nel caso di firma multipla a catena. <br/>
   * Per il formato CADES e' possibile l'annidamento delle buste crittografiche (formato a cipolla)
   * ovvero il signed data e' a sua volta una busta CADES o PADES. <br/>
   * Questo non e' ammissibile per le buste PADES che pertanto non vedranno un VerifyReport
   * annidato.
   */
  public IndexVerifyReport getChild() {
    return child;
  }

  /**
   * Get lo stato di conformita' della busta crittografica ricevuta in input.
   */
  public ConformitaParametriInput getConformitaParametriInput() {
    return conformitaParametriInput;
  }

  /**
   * Get il passo del processo di verifica concluso con errore.<br/>
   * E' restituito NULL se il processo si e' concluso correttamente altrimenti si restituisce un
   * valore da 1 a 7 in caso di fallimento di uno dei sette passi. <br/>
   * L'errore al passo e' segnalato con NON OK in getConformitaParametriInput(). <br/>
   */
  public IndexSignatureVerificationError getErrorCode() {
    return errorCode;
  }

  /**
   * Get l'identificativo del formato di firma digitale.
   */
  public FormatoFirma getFormatoFirma() {
    return formatoFirma;
  }

  /**
   * Get il numero di certificati di firma digitale. <br/>
   * Si tratta del numero di firme parallele o controfirme presenti nella busta.
   */
  public Integer getNumCertificatiFirma() {
    return numCertificatiFirma;
  }

  /**
   * Get il numero di certificati di marcatura temporale. <br/>
   * Si tratta del numero di marche temporali presenti nella busta.
   */
  public Integer getNumCertificatiMarca() {
    return numCertificatiMarca;
  }

  /**
   * Get la lista di firme presenti nel documento comprensivo di eventuali errori di sbustamento.
   * <br/>
   *
   * Si tratta delle firme parallele apposte, per ognuna possono esservi una o piu' controfirme;
   * mentre la marca temporale associata alle firme e' opzionale. La posizione del certificato
   * all'interno della lista delle firme fornisce il progressivo del certificato.
   *
   */
  public List<IndexSignature> getSignature() {
    return signature;
  }

  /**
   * Get la tipologia di firma digitale o marca temporale della struttura dati VerifyReport.
   *
   */
  public TipoFirma getTipoFirma() {
    return tipoFirma;
  }

  @Override
  public String toString() {
    return "IndexVerifyReport [" + (valida != null ? "valida=" + valida + ", " : "")
        + (conformitaParametriInput != null
        ? "conformitaParametriInput=" + conformitaParametriInput + ", "
            : "")
        + (errorCode != null ? "errorCode=" + errorCode + ", " : "")
        + (formatoFirma != null ? "formatoFirma=" + formatoFirma + ", " : "")
        + (tipoFirma != null ? "tipoFirma=" + tipoFirma : "") + "]";
  }

  /**
   * Creates builder to build {@link IndexVerifyReport}.
   *
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link IndexVerifyReport}.
   */
  public static final class Builder {
    private Boolean valida;
    private IndexVerifyReport child;
    private ConformitaParametriInput conformitaParametriInput;
    private IndexSignatureVerificationError errorCode;
    private FormatoFirma formatoFirma;
    private Integer numCertificatiFirma;
    private Integer numCertificatiMarca;
    private List<IndexSignature> signature = Collections.emptyList();
    private TipoFirma tipoFirma;

    private Builder() {}

    public Builder withValida(Boolean valida) {
      this.valida = valida;
      return this;
    }

    public Builder withChild(IndexVerifyReport child) {
      this.child = child;
      return this;
    }

    public Builder withConformitaParametriInput(ConformitaParametriInput conformitaParametriInput) {
      this.conformitaParametriInput = conformitaParametriInput;
      return this;
    }

    public Builder withErrorCode(IndexSignatureVerificationError errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public Builder withFormatoFirma(FormatoFirma formatoFirma) {
      this.formatoFirma = formatoFirma;
      return this;
    }

    public Builder withNumCertificatiFirma(Integer numCertificatiFirma) {
      this.numCertificatiFirma = numCertificatiFirma;
      return this;
    }

    public Builder withNumCertificatiMarca(Integer numCertificatiMarca) {
      this.numCertificatiMarca = numCertificatiMarca;
      return this;
    }

    public Builder withSignature(List<IndexSignature> signature) {
      this.signature = signature;
      return this;
    }

    public Builder withTipoFirma(TipoFirma tipoFirma) {
      this.tipoFirma = tipoFirma;
      return this;
    }

    public IndexVerifyReport build() {
      return new IndexVerifyReport(this);
    }
  }

}
