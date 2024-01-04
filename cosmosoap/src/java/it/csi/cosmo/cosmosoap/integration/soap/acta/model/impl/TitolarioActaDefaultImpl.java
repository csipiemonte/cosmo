/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.TitolarioActa;

/**
 *
 */

public class TitolarioActaDefaultImpl extends TitolarioActa {

  public TitolarioActaDefaultImpl() {
    // NOP
  }

  private TitolarioActaDefaultImpl(Builder builder) {
    this.id = builder.id;
    this.dbKey = builder.dbKey;
    this.changeToken = builder.changeToken;
    this.dataInizio = builder.dataInizio;
    this.dataFine = builder.dataFine;
    this.stato = builder.stato;
    this.codice = builder.codice;
    this.idProvvedimentoAutorizzatList = builder.idProvvedimentoAutorizzatList;
    this.numeroMaxLivelli = builder.numeroMaxLivelli;
    this.insertContenutiInVociConSottovoci = builder.insertContenutiInVociConSottovoci;
    this.insertDocInFascConSottofasc = builder.insertDocInFascConSottofasc;
    this.descrizione = builder.descrizione;
    this.insertFuoriVolumi = builder.insertFuoriVolumi;
    this.serieMultipleVoce = builder.serieMultipleVoce;
    this.creaAutomaPrimaSerie = builder.creaAutomaPrimaSerie;
    this.scartoSoloConVolumi = builder.scartoSoloConVolumi;
  }

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
    private String stato;
    private String codice;
    private String descrizione;
    private List<Long> idProvvedimentoAutorizzatList;
    private Integer numeroMaxLivelli;
    private Boolean insertContenutiInVociConSottovoci;
    private Boolean insertDocInFascConSottofasc;
    private Boolean insertFuoriVolumi;
    private Boolean serieMultipleVoce;
    private Boolean creaAutomaPrimaSerie;
    private Boolean scartoSoloConVolumi;

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

    public Builder withStato(String stato) {
      this.stato = stato;
      return this;
    }

    public Builder withCodice(String codice) {
      this.codice = codice;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
      return this;
    }

    public Builder withIdProvvedimentoAutorizzatList(List<Long> idProvvedimentoAutorizzatList) {
      this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
      return this;
    }

    public Builder withNumeroMaxLivelli(Integer numeroMaxLivelli) {
      this.numeroMaxLivelli = numeroMaxLivelli;
      return this;
    }

    public Builder withInsertContenutiInVociConSottovoci(Boolean insertContenutiInVociConSottovoci) {
      this.insertContenutiInVociConSottovoci = insertContenutiInVociConSottovoci;
      return this;
    }

    public Builder withInsertDocInFascConSottofasc(Boolean insertDocInFascConSottofasc) {
      this.insertDocInFascConSottofasc = insertDocInFascConSottofasc;
      return this;
    }

    public Builder withInsertFuoriVolumi(Boolean insertFuoriVolumi) {
      this.insertFuoriVolumi = insertFuoriVolumi;
      return this;
    }

    public Builder withSerieMultipleVoce(Boolean serieMultipleVoce) {
      this.serieMultipleVoce = serieMultipleVoce;
      return this;
    }

    public Builder withCreaAutomaPrimaSerie(Boolean creaAutomaPrimaSerie) {
      this.creaAutomaPrimaSerie = creaAutomaPrimaSerie;
      return this;
    }

    public Builder withScartoSoloConVolumi(Boolean scartoSoloConVolumi) {
      this.scartoSoloConVolumi = scartoSoloConVolumi;
      return this;
    }


    public TitolarioActaDefaultImpl build() {
      return new TitolarioActaDefaultImpl(this);
    }
  }

}
