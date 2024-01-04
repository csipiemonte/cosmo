/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Generated;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FascicoloActa;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.FascicoloRealeAnnualeActa;

/**
 *
 */
public class FascicoloRealeAnnualeActaDefaultImpl extends FascicoloRealeAnnualeActa {

  public FascicoloRealeAnnualeActaDefaultImpl() {
    // NOP
  }

  @Generated("SparkTools")
  private FascicoloRealeAnnualeActaDefaultImpl(Builder builder) {
    numero = builder.numero;
    oggetto = builder.oggetto;
    soggetto = builder.soggetto;
    vitalRecordCode = builder.vitalRecordCode;
    stato = builder.stato;
    idAOOResponsabileMateriale = builder.idAOOResponsabileMateriale;
    idStrutturaResponsabileMateriale = builder.idStrutturaResponsabileMateriale;
    idNodoResponsabileMateriale = builder.idNodoResponsabileMateriale;
    codice = builder.codice;
    descrizione = builder.descrizione;
    collocazioneCarteceo = builder.collocazioneCarteceo;
    ecmUuidNodo = builder.ecmUuidNodo;
    uuid = builder.uuid;
    idMovimentazioni = builder.idMovimentazioni;
    idUtenteCreazione = builder.idUtenteCreazione;
    idAnnotazioni = builder.idAnnotazioni;
    idDeposito = builder.idDeposito;
    indiceClassificazioneEstesa = builder.indiceClassificazioneEstesa;
    dataCreazione = builder.dataCreazione;
    dataFine = builder.dataFine;
    durataConservazioneCorrente = builder.durataConservazioneCorrente;
    durataConservazioneGenerale = builder.durataConservazioneGenerale;
    inArchivioCorrente = builder.inArchivioCorrente;
    contieneDatiPersonali = builder.contieneDatiPersonali;
    contieneDatiSensibili = builder.contieneDatiSensibili;
    contieneDatiRiservati = builder.contieneDatiRiservati;
    paroleChiave = builder.paroleChiave;
    dataCancellazione = builder.dataCancellazione;
    anno = builder.anno;
    numeroInterno = builder.numeroInterno;
  }

  /**
   * Creates builder to build {@link FascicoloActa}.
   * 
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link FascicoloActa}.
   */
  @Generated("SparkTools")
  public static final class Builder {

    private String numero;

    private String oggetto;

    private String soggetto;

    private String vitalRecordCode;

    private String stato;

    private Long idAOOResponsabileMateriale;

    private Long idStrutturaResponsabileMateriale;

    private Long idNodoResponsabileMateriale;

    private String codice;

    private String descrizione;

    private String collocazioneCarteceo;

    private String ecmUuidNodo;

    private String uuid;

    private List<String> idMovimentazioni = Collections.emptyList();

    private Long idUtenteCreazione;

    private List<Long> idAnnotazioni = Collections.emptyList();

    private String idDeposito;

    private String indiceClassificazioneEstesa;

    private LocalDate dataCreazione;

    private LocalDate dataFine;

    private Integer durataConservazioneCorrente;

    private Integer durataConservazioneGenerale;

    private Boolean inArchivioCorrente;

    private Boolean contieneDatiPersonali;

    private Boolean contieneDatiSensibili;

    private Boolean contieneDatiRiservati;

    private String paroleChiave;

    private LocalDate dataCancellazione;

    private Integer anno;

    private String numeroInterno;

    private Builder() {}

    public Builder withAnno(Integer anno) {
      this.anno = anno;
      return this;
    }

    public Builder withNumeroInterno(String numeroInterno) {
      this.numeroInterno = numeroInterno;
      return this;
    }

    public Builder withNumero(String numero) {
      this.numero = numero;
      return this;
    }

    public Builder withOggetto(String oggetto) {
      this.oggetto = oggetto;
      return this;
    }

    public Builder withSoggetto(String soggetto) {
      this.soggetto = soggetto;
      return this;
    }

    public Builder withVitalRecordCode(String vitalRecordCode) {
      this.vitalRecordCode = vitalRecordCode;
      return this;
    }

    public Builder withStato(String stato) {
      this.stato = stato;
      return this;
    }

    public Builder withIdAOOResponsabileMateriale(Long idAOOResponsabileMateriale) {
      this.idAOOResponsabileMateriale = idAOOResponsabileMateriale;
      return this;
    }

    public Builder withIdStrutturaResponsabileMateriale(Long idStrutturaResponsabileMateriale) {
      this.idStrutturaResponsabileMateriale = idStrutturaResponsabileMateriale;
      return this;
    }

    public Builder withIdNodoResponsabileMateriale(Long idNodoResponsabileMateriale) {
      this.idNodoResponsabileMateriale = idNodoResponsabileMateriale;
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

    public Builder withCollocazioneCarteceo(String collocazioneCarteceo) {
      this.collocazioneCarteceo = collocazioneCarteceo;
      return this;
    }

    public Builder withEcmUuidNodo(String ecmUuidNodo) {
      this.ecmUuidNodo = ecmUuidNodo;
      return this;
    }

    public Builder withUuid(String uuid) {
      this.uuid = uuid;
      return this;
    }

    public Builder withIdMovimentazioni(List<String> idMovimentazioni) {
      this.idMovimentazioni = idMovimentazioni;
      return this;
    }

    public Builder withIdUtenteCreazione(Long idUtenteCreazione) {
      this.idUtenteCreazione = idUtenteCreazione;
      return this;
    }

    public Builder withIdAnnotazioni(List<Long> idAnnotazioni) {
      this.idAnnotazioni = idAnnotazioni;
      return this;
    }

    public Builder withIdDeposito(String idDeposito) {
      this.idDeposito = idDeposito;
      return this;
    }

    public Builder withIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
      this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
      return this;
    }

    public Builder withDataCreazione(LocalDate dataCreazione) {
      this.dataCreazione = dataCreazione;
      return this;
    }

    public Builder withDataFine(LocalDate dataFine) {
      this.dataFine = dataFine;
      return this;
    }

    public Builder withDurataConservazioneCorrente(Integer durataConservazioneCorrente) {
      this.durataConservazioneCorrente = durataConservazioneCorrente;
      return this;
    }

    public Builder withDurataConservazioneGenerale(Integer durataConservazioneGenerale) {
      this.durataConservazioneGenerale = durataConservazioneGenerale;
      return this;
    }

    public Builder withInArchivioCorrente(Boolean inArchivioCorrente) {
      this.inArchivioCorrente = inArchivioCorrente;
      return this;
    }

    public Builder withContieneDatiPersonali(Boolean contieneDatiPersonali) {
      this.contieneDatiPersonali = contieneDatiPersonali;
      return this;
    }

    public Builder withContieneDatiSensibili(Boolean contieneDatiSensibili) {
      this.contieneDatiSensibili = contieneDatiSensibili;
      return this;
    }

    public Builder withContieneDatiRiservati(Boolean contieneDatiRiservati) {
      this.contieneDatiRiservati = contieneDatiRiservati;
      return this;
    }

    public Builder withParoleChiave(String paroleChiave) {
      this.paroleChiave = paroleChiave;
      return this;
    }

    public Builder withDataCancellazione(LocalDate dataCancellazione) {
      this.dataCancellazione = dataCancellazione;
      return this;
    }

    public FascicoloRealeAnnualeActaDefaultImpl build() {
      return new FascicoloRealeAnnualeActaDefaultImpl(this);
    }
  }

  @Override
  public String toString() {
    return "FascicoloRealeAnnualeActaDefaultImpl [wrapping = " + super.toString() + "]";
  }

}
