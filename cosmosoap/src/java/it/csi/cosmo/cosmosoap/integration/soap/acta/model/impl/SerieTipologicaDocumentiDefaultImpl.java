/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.annotation.Generated;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.SerieTipologicaDocumenti;


/**
 *
 */
public class SerieTipologicaDocumentiDefaultImpl extends SerieTipologicaDocumenti {

  public SerieTipologicaDocumentiDefaultImpl() {
    // NOP
  }

  @Generated("SparkTools")
  private SerieTipologicaDocumentiDefaultImpl(Builder builder) {
    id = builder.id;
    dbKey = builder.dbKey;
    changeToken = builder.changeToken;
    idFascicoloStandard = builder.idFascicoloStandard;
    stato = builder.stato;
    codice = builder.codice;
    obbligoFascicoloStandard = builder.obbligoFascicoloStandard;
    tipologiaNumerazione = builder.tipologiaNumerazione;
    idAooResponsabileMateriale = builder.idAooResponsabileMateriale;
    idStrutturaResponsabileMateriale = builder.idStrutturaResponsabileMateriale;
    idNodoResponsabileMateriale = builder.idNodoResponsabileMateriale;
    descrizione = builder.descrizione;
    dataCreazione = builder.dataCreazione;
    dataFine = builder.dataFine;
    durataConservazioneCorrente = builder.durataConservazioneCorrente;
    durataConservazioneGenerale = builder.durataConservazioneGenerale;
    inArchivioCorrente = builder.inArchivioCorrente;
    contieneDatiPersonali = builder.contieneDatiPersonali;
    contieneDatiSensibili = builder.contieneDatiSensibili;
    contieneDatiRiservati = builder.contieneDatiRiservati;
    collocazioneCarteceo = builder.collocazioneCarteceo;
    paroleChiave = builder.paroleChiave;
    dataCancellazione = builder.dataCancellazione;
    idUtenteCreazione = builder.idUtenteCreazione;
    idMovimentazioni = builder.idMovimentazioni;
    idAnnotazioni = builder.idAnnotazioni;
    indiceClassificazioneEstesa = builder.indiceClassificazioneEstesa;
    idDeposito = builder.idDeposito;
    uuidNodo = builder.uuidNodo;
    docAltraClassificazione = builder.docAltraClassificazione;
    modalitaCalcoloProgDoc = builder.modalitaCalcoloProgDoc;
    parteFissa = builder.parteFissa;
    registri = builder.registri;
  }

  @Override
  public String toString() {
    return "SerieFascicoliActaDefaultImpl [wrapping = " + super.toString() + "]";
  }

  /**
   * Creates builder to build {@link SerieTipologicaDocumentiDefaultImpl}.
   *
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link SerieTipologicaDocumentiDefaultImpl}.
   */
  @Generated("SparkTools")
  public static final class Builder {

    private String id;

    private String dbKey;

    private LocalDateTime changeToken;

    private Long idFascicoloStandard;

    private String stato;

    private String codice;

    private Boolean obbligoFascicoloStandard;

    private String tipologiaNumerazione;

    private String idAooResponsabileMateriale;

    private String idStrutturaResponsabileMateriale;

    private String idNodoResponsabileMateriale;

    private String descrizione;

    private LocalDate dataCreazione;

    private LocalDate dataFine;

    private Integer durataConservazioneCorrente;

    private Integer durataConservazioneGenerale;

    private Boolean inArchivioCorrente;

    private Boolean contieneDatiPersonali;

    private Boolean contieneDatiSensibili;

    private Boolean contieneDatiRiservati;

    private String collocazioneCarteceo;

    private String paroleChiave;

    private LocalDate dataCancellazione;

    private Long idUtenteCreazione;

    private List<String> idMovimentazioni = Collections.emptyList();

    private List<Long> idAnnotazioni = Collections.emptyList();

    private String indiceClassificazioneEstesa;

    private List<String> idDeposito = Collections.emptyList();

    private String uuidNodo;

    private Boolean docAltraClassificazione;

    private String modalitaCalcoloProgDoc;

    private String parteFissa;

    private Boolean registri;

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

    public Builder withIdFascicoloStandard(Long idFascicoloStandard) {
      this.idFascicoloStandard = idFascicoloStandard;
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

    public Builder withObbligoFascicoloStandard(Boolean obbligoFascicoloStandard) {
      this.obbligoFascicoloStandard = obbligoFascicoloStandard;
      return this;
    }

    public Builder withTipologiaNumerazione(String tipologiaNumerazione) {
      this.tipologiaNumerazione = tipologiaNumerazione;
      return this;
    }

    public Builder withIdAooResponsabileMateriale(String idAooResponsabileMateriale) {
      this.idAooResponsabileMateriale = idAooResponsabileMateriale;
      return this;
    }

    public Builder withIdStrutturaResponsabileMateriale(String idStrutturaResponsabileMateriale) {
      this.idStrutturaResponsabileMateriale = idStrutturaResponsabileMateriale;
      return this;
    }

    public Builder withIdNodoResponsabileMateriale(String idNodoResponsabileMateriale) {
      this.idNodoResponsabileMateriale = idNodoResponsabileMateriale;
      return this;
    }

    public Builder withDescrizione(String descrizione) {
      this.descrizione = descrizione;
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

    public Builder withCollocazioneCarteceo(String collocazioneCarteceo) {
      this.collocazioneCarteceo = collocazioneCarteceo;
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

    public Builder withIdUtenteCreazione(Long idUtenteCreazione) {
      this.idUtenteCreazione = idUtenteCreazione;
      return this;
    }

    public Builder withIdMovimentazioni(List<String> idMovimentazioni) {
      this.idMovimentazioni = idMovimentazioni;
      return this;
    }

    public Builder withIdAnnotazioni(List<Long> idAnnotazioni) {
      this.idAnnotazioni = idAnnotazioni;
      return this;
    }

    public Builder withIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
      this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
      return this;
    }

    public Builder withIdDeposito(List<String> idDeposito) {
      this.idDeposito = idDeposito;
      return this;
    }

    public Builder withUuidNodo(String uuidNodo) {
      this.uuidNodo = uuidNodo;
      return this;
    }

    public Builder withDocAltraClassificazione(Boolean docAltraClassificazione) {
      this.docAltraClassificazione = docAltraClassificazione;
      return this;
    }

    public Builder withModalitaCalcoloProgDoc(String modalitaCalcoloProgDoc) {
      this.modalitaCalcoloProgDoc = modalitaCalcoloProgDoc;
      return this;
    }

    public Builder withParteFissa(String parteFissa) {
      this.parteFissa = parteFissa;
      return this;
    }

    public Builder withRegistri(Boolean registri) {
      this.registri = registri;
      return this;
    }

    public SerieTipologicaDocumentiDefaultImpl build() {
      return new SerieTipologicaDocumentiDefaultImpl(this);
    }
  }

}
