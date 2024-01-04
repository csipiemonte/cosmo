/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaPayloadProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.doqui.acta.acaris.archive.EnumDocPrimarioType;
import it.doqui.acta.acaris.archive.EnumTipoDocumentoType;
import it.doqui.acta.acaris.archive.GruppoAllegatiPropertiesType;
import it.doqui.acta.acaris.common.ObjectIdType;
import it.doqui.acta.acaris.documentservice.EnumTipoDocumentoArchivistico;


/**
 *
 */
public abstract class DocumentoActa extends EntitaActa {

  @ActaProperty(propertyName = "codBarre")
  protected String codiceBarre;

  @ActaProperty(propertyName = "tipoDocFisico")
  protected EnumTipoDocumentoType tipoDocFisico;

  @ActaProperty(propertyName = "composizione")
  protected EnumDocPrimarioType composizione;

  @ActaProperty(propertyName = "multiplo")
  protected Boolean multiplo;

  @ActaProperty(propertyName = "registrato")
  protected Boolean registrato;

  @ActaProperty(propertyName = "modificabile")
  protected Boolean modificabile;

  @ActaProperty(propertyName = "definitivo")
  protected Boolean definitivo;

  @ActaProperty(propertyName = "origineInterna")
  protected Boolean origineInterna;

  @ActaProperty(propertyName = "analogico")
  protected Boolean analogico;

  @ActaProperty(propertyName = "rappresentazioneDigitale")
  protected Boolean rappresentazioneDigitale;

  @ActaProperty(propertyName = "daConservare")
  protected Boolean daConservare;

  @ActaProperty(propertyName = "prontoPerConservazione")
  protected Boolean prontoPerConservazione;

  @ActaProperty(propertyName = "daConservareDopoIl")
  protected LocalDate daConservareDopoIl;

  @ActaProperty(propertyName = "daConservarePrimaDel")
  protected LocalDate daConservarePrimaDel;

  @ActaProperty(propertyName = "conservato")
  protected Boolean conservato;

  @ActaProperty(propertyName = "datiPersonali")
  protected Boolean datiPersonali;

  @ActaProperty(propertyName = "datiSensibili")
  protected Boolean datiSensibili;

  @ActaProperty(propertyName = "datiRiservati")
  protected Boolean datiRiservati;

  @ActaProperty(propertyName = "dataCreazione")
  protected LocalDate dataCreazione;

  @ActaProperty(propertyName = "paroleChiave")
  protected String paroleChiave;

  @ActaProperty(propertyName = "modSMSQuarantena")
  protected Boolean modSMSQuarantena;

  @ActaProperty(propertyName = "congelato")
  protected Boolean congelato;

  @ActaProperty(propertyName = "referenziabile")
  protected Boolean referenziabile;

  @ActaProperty(propertyName = "autoreGiuridico", itemType = String.class)
  protected List<String> autoreGiuridico;

  @ActaProperty(propertyName = "autoreFisico", itemType = String.class)
  protected List<String> autoreFisico;

  @ActaProperty(propertyName = "scrittore", itemType = String.class)
  protected List<String> scrittore;

  @ActaProperty(propertyName = "originatore", itemType = String.class)
  protected List<String> originatore;

  @ActaProperty(propertyName = "destinatarioGiuridico", itemType = String.class)
  protected List<String> destinatarioGiuridico;

  @ActaProperty(propertyName = "destinatarioFisico", itemType = String.class)
  protected List<String> destinatarioFisico;

  @ActaProperty(propertyName = "oggetto")
  protected String oggetto;

  @ActaProperty(propertyName = "dataDocTopica")
  protected String dataDocTopica;

  @ActaProperty(propertyName = "dataDocCronica")
  protected LocalDate dataDocCronica;

  @ActaProperty(propertyName = "numRepertorio")
  protected String numRepertorio;

  @ActaProperty(propertyName = "docConAllegati")
  protected Boolean docConAllegati;

  @ActaProperty(propertyName = "docAutenticato")
  protected Boolean docAutenticato;

  @ActaProperty(propertyName = "docAutenticatoCopiaAutenticata")
  protected Boolean docAutenticatoCopiaAutenticata;

  @ActaProperty(propertyName = "idStatoDiEfficacia")
  protected String idStatoDiEfficacia;

  @ActaProperty(propertyName = "idFormaDocumentaria")
  protected String idFormaDocumentaria;

  @ActaProperty(propertyName = "idVitalRecordCode")
  protected String vitalRecordCode;

  @ActaProperty(propertyName = "idCorrente")
  protected String idCorrente;

  @ActaProperty(propertyName = "soggettoProduttore")
  protected List<String> soggettoProduttore;

  @ActaProperty(propertyName = "docAutentico")
  protected Boolean docAutentico;

  @ActaProperty(propertyName = "docAutenticatoFirmaAutenticata")
  protected Boolean docAutenticatoFirmaAutenticata;

  @ActaProperty(propertyName = "applicativoAlimentante")
  protected String applicativoAlimentante;

  // Properties per contenuto

  @ActaPayloadProperty(manual = true)
  protected EnumTipoDocumentoArchivistico tipoDocumento;

  @ActaPayloadProperty(manual = true)
  protected Boolean allegato;

  @ActaPayloadProperty(manual = true)
  protected String fileName;

  @ActaPayloadProperty(manual = true)
  protected String mimeType;

  @ActaPayloadProperty(manual = true)
  protected byte[] content;

  @ActaPayloadProperty(manual = true)
  protected Boolean copiaCartacea;

  @ActaPayloadProperty(manual = true)
  protected String collocazioneCartacea;

  @ActaPayloadProperty(manual = true)
  protected Boolean cartaceo;

  @ActaPayloadProperty(manual = true)
  protected LocalDate dataMemorizzazione;

  @ActaPayloadProperty(manual = true)
  protected Integer progressivoPerDocumento;

  @ActaPayloadProperty(manual = true)
  protected Boolean contenutoFisicoModificabile;

  @ActaPayloadProperty(manual = true)
  protected Boolean contenutoFisicoSbustamento;

  @ActaPayloadProperty(manual = true)
  protected Boolean contenutoFisicoWorkingCopy;

  @ActaPayloadProperty(manual = true)
  protected GruppoAllegatiPropertiesType gruppoAllegati;

  @ActaPayloadProperty(manual = true)
  protected ObjectIdType classificazionePrincipale;

  public String getApplicativoAlimentante() {
    return applicativoAlimentante;
  }

  public EnumTipoDocumentoArchivistico getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(EnumTipoDocumentoArchivistico tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public Boolean getDocAutentico() {
    return docAutentico;
  }

  public void setDocAutentico(Boolean docAutentico) {
    this.docAutentico = docAutentico;
  }

  public Boolean getAllegato() {
    return allegato;
  }

  public void setAllegato(Boolean allegato) {
    this.allegato = allegato;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public Boolean getCopiaCartacea() {
    return copiaCartacea;
  }

  public void setCopiaCartacea(Boolean copiaCartacea) {
    this.copiaCartacea = copiaCartacea;
  }

  public String getCollocazioneCartacea() {
    return collocazioneCartacea;
  }

  public void setCollocazioneCartacea(String collocazioneCartacea) {
    this.collocazioneCartacea = collocazioneCartacea;
  }

  public Boolean getCartaceo() {
    return cartaceo;
  }

  public void setCartaceo(Boolean cartaceo) {
    this.cartaceo = cartaceo;
  }

  public LocalDate getDataMemorizzazione() {
    return dataMemorizzazione;
  }

  public void setDataMemorizzazione(LocalDate dataMemorizzazione) {
    this.dataMemorizzazione = dataMemorizzazione;
  }

  public Integer getProgressivoPerDocumento() {
    return progressivoPerDocumento;
  }

  public void setProgressivoPerDocumento(Integer progressivoPerDocumento) {
    this.progressivoPerDocumento = progressivoPerDocumento;
  }

  public Boolean getContenutoFisicoModificabile() {
    return contenutoFisicoModificabile;
  }

  public void setContenutoFisicoModificabile(Boolean contenutoFisicoModificabile) {
    this.contenutoFisicoModificabile = contenutoFisicoModificabile;
  }

  public Boolean getContenutoFisicoSbustamento() {
    return contenutoFisicoSbustamento;
  }

  public void setContenutoFisicoSbustamento(Boolean contenutoFisicoSbustamento) {
    this.contenutoFisicoSbustamento = contenutoFisicoSbustamento;
  }

  public Boolean getContenutoFisicoWorkingCopy() {
    return contenutoFisicoWorkingCopy;
  }

  public void setContenutoFisicoWorkingCopy(Boolean contenutoFisicoWorkingCopy) {
    this.contenutoFisicoWorkingCopy = contenutoFisicoWorkingCopy;
  }

  public String getCodiceBarre() {
    return codiceBarre;
  }

  public void setCodiceBarre(String codiceBarre) {
    this.codiceBarre = codiceBarre;
  }

  public EnumTipoDocumentoType getTipoDocFisico() {
    return tipoDocFisico;
  }

  public void setTipoDocFisico(EnumTipoDocumentoType tipoDocFisico) {
    this.tipoDocFisico = tipoDocFisico;
  }

  public EnumDocPrimarioType getComposizione() {
    return composizione;
  }

  public void setComposizione(EnumDocPrimarioType composizione) {
    this.composizione = composizione;
  }

  public Boolean getMultiplo() {
    return multiplo;
  }

  public void setMultiplo(Boolean multiplo) {
    this.multiplo = multiplo;
  }

  public Boolean getRegistrato() {
    return registrato;
  }

  public void setRegistrato(Boolean registrato) {
    this.registrato = registrato;
  }

  public Boolean getModificabile() {
    return modificabile;
  }

  public void setModificabile(Boolean modificabile) {
    this.modificabile = modificabile;
  }

  public Boolean getDefinitivo() {
    return definitivo;
  }

  public void setDefinitivo(Boolean definitivo) {
    this.definitivo = definitivo;
  }

  public Boolean getOrigineInterna() {
    return origineInterna;
  }

  public void setOrigineInterna(Boolean origineInterna) {
    this.origineInterna = origineInterna;
  }

  public Boolean getAnalogico() {
    return analogico;
  }

  public void setAnalogico(Boolean analogico) {
    this.analogico = analogico;
  }

  public Boolean getRappresentazioneDigitale() {
    return rappresentazioneDigitale;
  }

  public void setRappresentazioneDigitale(Boolean rappresentazioneDigitale) {
    this.rappresentazioneDigitale = rappresentazioneDigitale;
  }

  public Boolean getDaConservare() {
    return daConservare;
  }

  public void setDaConservare(Boolean daConservare) {
    this.daConservare = daConservare;
  }

  public Boolean getProntoPerConservazione() {
    return prontoPerConservazione;
  }

  public void setProntoPerConservazione(Boolean prontoPerConservazione) {
    this.prontoPerConservazione = prontoPerConservazione;
  }

  public LocalDate getDaConservareDopoIl() {
    return daConservareDopoIl;
  }

  public void setDaConservareDopoIl(LocalDate daConservareDopoIl) {
    this.daConservareDopoIl = daConservareDopoIl;
  }

  public LocalDate getDaConservarePrimaDel() {
    return daConservarePrimaDel;
  }

  public void setDaConservarePrimaDel(LocalDate daConservarePrimaDel) {
    this.daConservarePrimaDel = daConservarePrimaDel;
  }

  public Boolean getConservato() {
    return conservato;
  }

  public void setConservato(Boolean conservato) {
    this.conservato = conservato;
  }

  public Boolean getDatiPersonali() {
    return datiPersonali;
  }

  public void setDatiPersonali(Boolean datiPersonali) {
    this.datiPersonali = datiPersonali;
  }

  public Boolean getDatiSensibili() {
    return datiSensibili;
  }

  public void setDatiSensibili(Boolean datiSensibili) {
    this.datiSensibili = datiSensibili;
  }

  public Boolean getDatiRiservati() {
    return datiRiservati;
  }

  public void setDatiRiservati(Boolean datiRiservati) {
    this.datiRiservati = datiRiservati;
  }

  public LocalDate getDataCreazione() {
    return dataCreazione;
  }

  public void setDataCreazione(LocalDate dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public String getParoleChiave() {
    return paroleChiave;
  }

  public void setParoleChiave(String paroleChiave) {
    this.paroleChiave = paroleChiave;
  }

  public Boolean getModSMSQuarantena() {
    return modSMSQuarantena;
  }

  public void setModSMSQuarantena(Boolean modSMSQuarantena) {
    this.modSMSQuarantena = modSMSQuarantena;
  }

  public Boolean getCongelato() {
    return congelato;
  }

  public void setCongelato(Boolean congelato) {
    this.congelato = congelato;
  }

  public Boolean getReferenziabile() {
    return referenziabile;
  }

  public void setReferenziabile(Boolean referenziabile) {
    this.referenziabile = referenziabile;
  }

  public List<String> getAutoreGiuridico() {
    return autoreGiuridico;
  }

  public void setAutoreGiuridico(List<String> autoreGiuridico) {
    this.autoreGiuridico = autoreGiuridico;
  }

  public List<String> getAutoreFisico() {
    return autoreFisico;
  }

  public void setAutoreFisico(List<String> autoreFisico) {
    this.autoreFisico = autoreFisico;
  }

  public List<String> getScrittore() {
    return scrittore;
  }

  public void setScrittore(List<String> scrittore) {
    this.scrittore = scrittore;
  }

  public List<String> getOriginatore() {
    return originatore;
  }

  public void setOriginatore(List<String> originatore) {
    this.originatore = originatore;
  }

  public List<String> getDestinatarioGiuridico() {
    return destinatarioGiuridico;
  }

  public void setDestinatarioGiuridico(List<String> destinatarioGiuridico) {
    this.destinatarioGiuridico = destinatarioGiuridico;
  }

  public List<String> getDestinatarioFisico() {
    return destinatarioFisico;
  }

  public void setDestinatarioFisico(List<String> destinatarioFisico) {
    this.destinatarioFisico = destinatarioFisico;
  }

  public String getOggetto() {
    return oggetto;
  }

  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  public String getDataDocTopica() {
    return dataDocTopica;
  }

  public void setDataDocTopica(String dataDocTopica) {
    this.dataDocTopica = dataDocTopica;
  }

  public LocalDate getDataDocCronica() {
    return dataDocCronica;
  }

  public void setDataDocCronica(LocalDate dataDocCronica) {
    this.dataDocCronica = dataDocCronica;
  }

  public String getNumRepertorio() {
    return numRepertorio;
  }

  public void setNumRepertorio(String numRepertorio) {
    this.numRepertorio = numRepertorio;
  }

  public Boolean getDocConAllegati() {
    return docConAllegati;
  }

  public void setDocConAllegati(Boolean docConAllegati) {
    this.docConAllegati = docConAllegati;
  }

  public Boolean getDocAutenticato() {
    return docAutenticato;
  }

  public void setDocAutenticato(Boolean docAutenticato) {
    this.docAutenticato = docAutenticato;
  }

  public Boolean getDocAutenticatoFirmaAutenticata() {
    return docAutenticatoFirmaAutenticata;
  }

  public void setDocAutenticatoFirmaAutenticata(Boolean docAutenticatoFirmaAutenticata) {
    this.docAutenticatoFirmaAutenticata = docAutenticatoFirmaAutenticata;
  }

  public Boolean getDocAutenticatoCopiaAutenticata() {
    return docAutenticatoCopiaAutenticata;
  }

  public void setDocAutenticatoCopiaAutenticata(Boolean docAutenticatoCopiaAutenticata) {
    this.docAutenticatoCopiaAutenticata = docAutenticatoCopiaAutenticata;
  }

  public String getIdStatoDiEfficacia() {
    return idStatoDiEfficacia;
  }

  public void setIdStatoDiEfficacia(String idStatoDiEfficacia) {
    this.idStatoDiEfficacia = idStatoDiEfficacia;
  }

  public String getIdFormaDocumentaria() {
    return idFormaDocumentaria;
  }

  public void setIdFormaDocumentaria(String idFormaDocumentaria) {
    this.idFormaDocumentaria = idFormaDocumentaria;
  }

  public String getVitalRecordCode() {
    return vitalRecordCode;
  }

  public void setVitalRecordCode(String vitalRecordCode) {
    this.vitalRecordCode = vitalRecordCode;
  }

  public String getIdCorrente() {
    return idCorrente;
  }

  public void setIdCorrente(String idCorrente) {
    this.idCorrente = idCorrente;
  }

  public List<String> getSoggettoProduttore() {
    return soggettoProduttore;
  }

  public void setSoggettoProduttore(List<String> soggettoProduttore) {
    this.soggettoProduttore = soggettoProduttore;
  }

  public String getApplicativoALimentante() {
    return applicativoAlimentante;
  }

  public void setApplicativoAlimentante(String applicativoAlimentante) {
    this.applicativoAlimentante = applicativoAlimentante;
  }

  public GruppoAllegatiPropertiesType getGruppoAllegati() {
    return gruppoAllegati;
  }

  public void setGruppoAllegati(GruppoAllegatiPropertiesType gruppoAllegati) {
    this.gruppoAllegati = gruppoAllegati;
  }

  public ObjectIdType getClassificazionePrincipale() {
    return classificazionePrincipale;
  }

  public void setClassificazionePrincipale(ObjectIdType classificazionePrincipale) {
    this.classificazionePrincipale = classificazionePrincipale;
  }

}
