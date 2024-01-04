/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.Generated;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes.DocumentoSempliceActa;
import it.doqui.acta.acaris.archive.EnumDocPrimarioType;
import it.doqui.acta.acaris.archive.EnumTipoDocumentoType;
import it.doqui.acta.acaris.archive.GruppoAllegatiPropertiesType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.documentservice.EnumTipoDocumentoArchivistico;


/**
 *
 */
public class DocumentoSempliceActaDefaultImpl extends DocumentoSempliceActa {


  @Generated("SparkTools")
  private DocumentoSempliceActaDefaultImpl(Builder builder) {
    id = builder.id;
    dbKey = builder.dbKey;
    codiceBarre = builder.codiceBarre;
    tipoDocFisico = builder.tipoDocFisico;
    composizione = builder.composizione;
    multiplo = builder.multiplo;
    registrato = builder.registrato;
    modificabile = builder.modificabile;
    definitivo = builder.definitivo;
    origineInterna = builder.origineInterna;
    analogico = builder.analogico;
    rappresentazioneDigitale = builder.rappresentazioneDigitale;
    daConservare = builder.daConservare;
    prontoPerConservazione = builder.prontoPerConservazione;
    daConservareDopoIl = builder.daConservareDopoIl;
    daConservarePrimaDel = builder.daConservarePrimaDel;
    conservato = builder.conservato;
    datiPersonali = builder.datiPersonali;
    datiSensibili = builder.datiSensibili;
    datiRiservati = builder.datiRiservati;
    dataCreazione = builder.dataCreazione;
    paroleChiave = builder.paroleChiave;
    modSMSQuarantena = builder.modSMSQuarantena;
    congelato = builder.congelato;
    referenziabile = builder.referenziabile;
    autoreGiuridico = builder.autoreGiuridico;
    autoreFisico = builder.autoreFisico;
    scrittore = builder.scrittore;
    originatore = builder.originatore;
    destinatarioGiuridico = builder.destinatarioGiuridico;
    destinatarioFisico = builder.destinatarioFisico;
    oggetto = builder.oggetto;
    dataDocTopica = builder.dataDocTopica;
    dataDocCronica = builder.dataDocCronica;
    numRepertorio = builder.numRepertorio;
    docConAllegati = builder.docConAllegati;
    docAutenticato = builder.docAutenticato;
    docAutenticatoFirmaAutenticata = builder.docAutenticatoFirmaAutenticata;
    docAutenticatoCopiaAutenticata = builder.docAutenticatoCopiaAutenticata;
    idStatoDiEfficacia = builder.idStatoDiEfficacia;
    idFormaDocumentaria = builder.idFormaDocumentaria;
    vitalRecordCode = builder.vitalRecordCode;
    dataFinePubblicazione = builder.dataFinePubblicazione;
    idCorrente = builder.idCorrente;
    soggettoProduttore = builder.soggettoProduttore;
    indiceClassificazioneEstesa = builder.indiceClassificazioneEstesa;
    idAnnotazioniList = builder.idAnnotazioniList;
    indiceClassificazione = builder.indiceClassificazione;
    idProtocolloList = builder.idProtocolloList;
    tipoDocumento = builder.tipoDocumento;
    allegato = builder.allegato;
    fileName = builder.fileName;
    mimeType = builder.mimeType;
    content = builder.content;
    copiaCartacea = builder.copiaCartacea;
    collocazioneCartacea = builder.collocazioneCartacea;
    cartaceo = builder.cartaceo;
    dataMemorizzazione = builder.dataMemorizzazione;
    progressivoPerDocumento = builder.progressivoPerDocumento;
    contenutoFisicoModificabile = builder.contenutoFisicoModificabile;
    contenutoFisicoSbustamento = builder.contenutoFisicoSbustamento;
    contenutoFisicoWorkingCopy = builder.contenutoFisicoWorkingCopy;
    applicativoAlimentante = builder.applicativoAlimentante;
    gruppoAllegati = builder.gruppoAllegati;
    classificazionePrincipale = builder.classificazionePrincipale;
  }

  public DocumentoSempliceActaDefaultImpl() {
    // NOP
  }

  /**
   * Creates builder to build {@link DocumentoSempliceActaDefaultImpl}.
   *
   * @return created builder
   */
  @Generated("SparkTools")
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link DocumentoSempliceActaDefaultImpl}.
   */
  @Generated("SparkTools")
  public static final class Builder {

    private static final List<String> DEFAULT_USER =
        Arrays.asList("Utente di sistema COSMO", "COSMO");

    private EnumDocPrimarioType composizione = EnumDocPrimarioType.DOCUMENTO_SINGOLO;

    private String id;

    private String dbKey;

    private String codiceBarre;

    private EnumTipoDocumentoType tipoDocFisico = EnumTipoDocumentoType.SEMPLICE;

    private Boolean multiplo = false;

    private Boolean registrato = false;

    private Boolean modificabile = true;

    private Boolean definitivo = false;

    private Boolean origineInterna = true;

    private Boolean analogico = false;

    private Boolean rappresentazioneDigitale = true;

    private Boolean daConservare = false;

    private Boolean prontoPerConservazione = false;

    private LocalDate daConservareDopoIl;

    private LocalDate daConservarePrimaDel;

    private Boolean conservato;

    private Boolean datiPersonali = true;

    private Boolean datiSensibili = true;

    private Boolean datiRiservati = true;

    private LocalDate dataCreazione = LocalDate.now();

    private String paroleChiave = "";

    private Boolean modSMSQuarantena;

    private Boolean congelato;

    private Boolean referenziabile;

    private List<String> autoreGiuridico = DEFAULT_USER;

    private List<String> autoreFisico = DEFAULT_USER;

    private List<String> scrittore = DEFAULT_USER;

    private List<String> originatore = DEFAULT_USER;

    private List<String> destinatarioGiuridico = DEFAULT_USER;

    private List<String> destinatarioFisico = DEFAULT_USER;

    private String oggetto;

    private String dataDocTopica;

    private LocalDate dataDocCronica;

    private String numRepertorio;

    private Boolean docConAllegati = false;

    private Boolean docAutenticato = false;

    private Boolean docAutenticatoFirmaAutenticata = false;

    private Boolean docAutenticatoCopiaAutenticata = false;

    private String idStatoDiEfficacia;

    private String idFormaDocumentaria;

    private String vitalRecordCode;

    private LocalDate dataFinePubblicazione;

    private String idCorrente;

    private List<String> soggettoProduttore;

    private String indiceClassificazioneEstesa;

    private List<String> idAnnotazioniList = Collections.emptyList();

    private String indiceClassificazione;

    private List<String> idProtocolloList = Collections.emptyList();

    private EnumTipoDocumentoArchivistico tipoDocumento;

    private Boolean allegato;

    private String fileName;

    private String mimeType;

    private byte[] content;

    private Boolean copiaCartacea;

    private String collocazioneCartacea;

    private Boolean cartaceo;

    private LocalDate dataMemorizzazione;

    private Integer progressivoPerDocumento;

    private Boolean contenutoFisicoModificabile;

    private Boolean contenutoFisicoSbustamento;

    private Boolean contenutoFisicoWorkingCopy;

    private String applicativoAlimentante;

    private GruppoAllegatiPropertiesType gruppoAllegati;

    private ObjectIdType classificazionePrincipale;

    private Builder() {}

    public Builder withId(String id) {
      this.id = id;
      return this;
    }

    public Builder withDbKey(String dbKey) {
      this.dbKey = dbKey;
      return this;
    }

    public Builder withCodiceBarre(String codiceBarre) {
      this.codiceBarre = codiceBarre;
      return this;
    }

    public Builder withTipoDocFisico(EnumTipoDocumentoType tipoDocFisico) {
      this.tipoDocFisico = tipoDocFisico;
      return this;
    }

    public Builder withComposizione(EnumDocPrimarioType composizione) {
      this.composizione = composizione;
      return this;
    }

    public Builder withMultiplo(Boolean multiplo) {
      this.multiplo = multiplo;
      return this;
    }

    public Builder withRegistrato(Boolean registrato) {
      this.registrato = registrato;
      return this;
    }

    public Builder withModificabile(Boolean modificabile) {
      this.modificabile = modificabile;
      return this;
    }

    public Builder withDefinitivo(Boolean definitivo) {
      this.definitivo = definitivo;
      return this;
    }

    public Builder withOrigineInterna(Boolean origineInterna) {
      this.origineInterna = origineInterna;
      return this;
    }

    public Builder withAnalogico(Boolean analogico) {
      this.analogico = analogico;
      return this;
    }

    public Builder withRappresentazioneDigitale(Boolean rappresentazioneDigitale) {
      this.rappresentazioneDigitale = rappresentazioneDigitale;
      return this;
    }

    public Builder withDaConservare(Boolean daConservare) {
      this.daConservare = daConservare;
      return this;
    }

    public Builder withProntoPerConservazione(Boolean prontoPerConservazione) {
      this.prontoPerConservazione = prontoPerConservazione;
      return this;
    }

    public Builder withDaConservareDopoIl(LocalDate daConservareDopoIl) {
      this.daConservareDopoIl = daConservareDopoIl;
      return this;
    }

    public Builder withDaConservarePrimaDel(LocalDate daConservarePrimaDel) {
      this.daConservarePrimaDel = daConservarePrimaDel;
      return this;
    }

    public Builder withConservato(Boolean conservato) {
      this.conservato = conservato;
      return this;
    }

    public Builder withDatiPersonali(Boolean datiPersonali) {
      this.datiPersonali = datiPersonali;
      return this;
    }

    public Builder withDatiSensibili(Boolean datiSensibili) {
      this.datiSensibili = datiSensibili;
      return this;
    }

    public Builder withDatiRiservati(Boolean datiRiservati) {
      this.datiRiservati = datiRiservati;
      return this;
    }

    public Builder withDataCreazione(LocalDate dataCreazione) {
      this.dataCreazione = dataCreazione;
      return this;
    }

    public Builder withParoleChiave(String paroleChiave) {
      this.paroleChiave = paroleChiave;
      return this;
    }

    public Builder withModSMSQuarantena(Boolean modSMSQuarantena) {
      this.modSMSQuarantena = modSMSQuarantena;
      return this;
    }

    public Builder withCongelato(Boolean congelato) {
      this.congelato = congelato;
      return this;
    }

    public Builder withReferenziabile(Boolean referenziabile) {
      this.referenziabile = referenziabile;
      return this;
    }

    public Builder withAutoreGiuridico(List<String> autoreGiuridico) {
      this.autoreGiuridico = autoreGiuridico;
      return this;
    }

    public Builder withAutoreFisico(List<String> autoreFisico) {
      this.autoreFisico = autoreFisico;
      return this;
    }

    public Builder withScrittore(List<String> scrittore) {
      this.scrittore = scrittore;
      return this;
    }

    public Builder withOriginatore(List<String> originatore) {
      this.originatore = originatore;
      return this;
    }

    public Builder withDestinatarioGiuridico(List<String> destinatarioGiuridico) {
      this.destinatarioGiuridico = destinatarioGiuridico;
      return this;
    }

    public Builder withDestinatarioFisico(List<String> destinatarioFisico) {
      this.destinatarioFisico = destinatarioFisico;
      return this;
    }

    public Builder withOggetto(String oggetto) {
      this.oggetto = oggetto;
      return this;
    }

    public Builder withDataDocTopica(String dataDocTopica) {
      this.dataDocTopica = dataDocTopica;
      return this;
    }

    public Builder withDataDocCronica(LocalDate dataDocCronica) {
      this.dataDocCronica = dataDocCronica;
      return this;
    }

    public Builder withNumRepertorio(String numRepertorio) {
      this.numRepertorio = numRepertorio;
      return this;
    }

    public Builder withDocConAllegati(Boolean docConAllegati) {
      this.docConAllegati = docConAllegati;
      return this;
    }

    public Builder withDocAutenticato(Boolean docAutenticato) {
      this.docAutenticato = docAutenticato;
      return this;
    }

    public Builder withDocAutenticatoFirmaAutenticata(Boolean docAutenticatoFirmaAutenticata) {
      this.docAutenticatoFirmaAutenticata = docAutenticatoFirmaAutenticata;
      return this;
    }

    public Builder withDocAutenticatoCopiaAutenticata(Boolean docAutenticatoCopiaAutenticata) {
      this.docAutenticatoCopiaAutenticata = docAutenticatoCopiaAutenticata;
      return this;
    }

    public Builder withIdStatoDiEfficacia(String idStatoDiEfficacia) {
      this.idStatoDiEfficacia = idStatoDiEfficacia;
      return this;
    }

    public Builder withIdFormaDocumentaria(String idFormaDocumentaria) {
      this.idFormaDocumentaria = idFormaDocumentaria;
      return this;
    }

    public Builder withVitalRecordCode(String vitalRecordCode) {
      this.vitalRecordCode = vitalRecordCode;
      return this;
    }

    public Builder withDataFinePubblicazione(LocalDate dataFinePubblicazione) {
      this.dataFinePubblicazione = dataFinePubblicazione;
      return this;
    }

    public Builder withIdCorrente(String idCorrente) {
      this.idCorrente = idCorrente;
      return this;
    }

    public Builder withSoggettoProduttore(List<String> soggettoProduttore) {
      this.soggettoProduttore = soggettoProduttore;
      return this;
    }

    public Builder withIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
      this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
      return this;
    }

    public Builder withIdAnnotazioniList(List<String> idAnnotazioniList) {
      this.idAnnotazioniList = idAnnotazioniList;
      return this;
    }

    public Builder withIndiceClassificazione(String indiceClassificazione) {
      this.indiceClassificazione = indiceClassificazione;
      return this;
    }

    public Builder withIdProtocolloList(List<String> idProtocolloList) {
      this.idProtocolloList = idProtocolloList;
      return this;
    }

    public Builder withTipoDocumento(EnumTipoDocumentoArchivistico tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
      return this;
    }

    public Builder withAllegato(Boolean allegato) {
      this.allegato = allegato;
      return this;
    }

    public Builder withFileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder withMimeType(String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    public Builder withContent(byte[] content) {
      this.content = content;
      return this;
    }

    public Builder withCopiaCartacea(Boolean copiaCartacea) {
      this.copiaCartacea = copiaCartacea;
      return this;
    }

    public Builder withCollocazioneCartacea(String collocazioneCartacea) {
      this.collocazioneCartacea = collocazioneCartacea;
      return this;
    }

    public Builder withCartaceo(Boolean cartaceo) {
      this.cartaceo = cartaceo;
      return this;
    }

    public Builder withDataMemorizzazione(LocalDate dataMemorizzazione) {
      this.dataMemorizzazione = dataMemorizzazione;
      return this;
    }

    public Builder withProgressivoPerDocumento(Integer progressivoPerDocumento) {
      this.progressivoPerDocumento = progressivoPerDocumento;
      return this;
    }

    public Builder withContenutoFisicoModificabile(Boolean contenutoFisicoModificabile) {
      this.contenutoFisicoModificabile = contenutoFisicoModificabile;
      return this;
    }

    public Builder withContenutoFisicoSbustamento(Boolean contenutoFisicoSbustamento) {
      this.contenutoFisicoSbustamento = contenutoFisicoSbustamento;
      return this;
    }

    public Builder withContenutoFisicoWorkingCopy(Boolean contenutoFisicoWorkingCopy) {
      this.contenutoFisicoWorkingCopy = contenutoFisicoWorkingCopy;
      return this;
    }

    public Builder withApplicativoAlimentante(String applicativoAlimentante) {
      this.applicativoAlimentante = applicativoAlimentante;
      return this;
    }

    public Builder withGruppoAllegati(GruppoAllegatiPropertiesType gruppoAllegati) {
      this.gruppoAllegati = gruppoAllegati;
      return this;
    }

    public Builder withClassificazionePrincipale(ObjectIdType classificazionePrincipale) {
      this.classificazionePrincipale = classificazionePrincipale;
      return this;
    }

    public DocumentoSempliceActaDefaultImpl build() {
      return new DocumentoSempliceActaDefaultImpl(this);
    }
  }

}
