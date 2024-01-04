/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.ClassificazioneActa;


/**
 *
 */
public class ClassificazioneActaDefaultImpl extends ClassificazioneActa {

  private ClassificazioneActaDefaultImpl(Builder builder) {
    this.id = builder.id;
    this.dbKey = builder.dbKey;
    this.changeToken = builder.changeToken;
    this.dataInizio = builder.dataInizio;
    this.dataFine = builder.dataFine;
    this.legislatura = builder.legislatura;
    this.numero = builder.numero;
    this.stato = builder.stato;
    this.codice = builder.codice;
    this.cartaceo = builder.cartaceo;
    this.docAllegato = builder.docAllegato;
    this.docConAllegati = builder.docConAllegati;
    this.copiaCartacea = builder.copiaCartacea;
  }

  public ClassificazioneActaDefaultImpl() {}

  /**
   * Creates builder to build {@link ClassificazioneActaDefaultImpl}.
   * 
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link ClassificazioneActaDefaultImpl}.
   */
  public static final class Builder {
    private String id;
    private String dbKey;
    private LocalDateTime changeToken;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private String legislatura;
    private String numero;
    private String stato;
    private String codice;
    private Boolean cartaceo;
    private Boolean docAllegato;
    private Boolean docConAllegati;
    private Boolean copiaCartacea;

    private Builder() {}

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withDbKey(String dbKey) {
      this.dbKey = dbKey;
      return this;
    }

    public Builder withChangeToken(LocalDateTime changeToken) {
      this.changeToken = changeToken;
      return this;
    }

    public Builder withDataInizio(LocalDate dataInizio) {
      this.dataInizio = dataInizio;
      return this;
    }

    public Builder withDataFine(LocalDate dataFine) {
      this.dataFine = dataFine;
      return this;
    }

    public Builder withLegislatura(String legislatura) {
      this.legislatura = legislatura;
      return this;
    }

    public Builder withNumero(String numero) {
      this.numero = numero;
      return this;
    }

    public Builder withStato(String stato) {
      this.stato = stato;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withCartaceo(Boolean cartaceo) {
      this.cartaceo = cartaceo;
      return this;
    }

    public Builder withDocAllegato(Boolean docAllegato) {
      this.docAllegato = docAllegato;
      return this;
    }

    public Builder withDocConAllegati(Boolean docConAllegati) {
      this.docConAllegati = docConAllegati;
      return this;
    }

    public Builder withCopiaCartacea(Boolean copiaCartacea) {
      this.copiaCartacea = copiaCartacea;
      return this;
    }

    public ClassificazioneActaDefaultImpl build() {
      return new ClassificazioneActaDefaultImpl(this);
    }
  }

}
