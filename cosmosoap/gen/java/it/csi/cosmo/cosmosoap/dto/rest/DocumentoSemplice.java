/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.GruppoAllegatiProperties;
import it.csi.cosmo.cosmosoap.dto.rest.ObjectId;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentoSemplice  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private String codiceBarre = null;
  private OffsetDateTime changeToken = null;
  private String tipoDocFisico = null;
  private String composizione = null;
  private Boolean multiplo = null;
  private Boolean registrato = null;
  private Boolean modificabile = null;
  private Boolean definitivo = null;
  private Boolean origineInterna = null;
  private Boolean analogico = null;
  private Boolean rappresentazioneDigitale = null;
  private Boolean daConservare = null;
  private Boolean prontoPerConservazione = null;
  private LocalDate daConservareDopoIl = null;
  private LocalDate daConservarePrimaDel = null;
  private Boolean conservato = null;
  private Boolean datiPersonali = null;
  private Boolean datiSensibili = null;
  private Boolean datiRiservati = null;
  private LocalDate dataCreazione = null;
  private String paroleChiave = null;
  private Boolean modSMSQuarantena = null;
  private Boolean congelato = null;
  private Boolean referenziabile = null;
  private List<String> autoreGiuridico = new ArrayList<>();
  private List<String> autoreFisico = new ArrayList<>();
  private List<String> scrittore = new ArrayList<>();
  private List<String> originatore = new ArrayList<>();
  private List<String> destinatarioGiuridico = new ArrayList<>();
  private List<String> destinatarioFisico = new ArrayList<>();
  private String oggetto = null;
  private String dataDocTopica = null;
  private LocalDate dataDocCronica = null;
  private String numRepertorio = null;
  private Boolean docConAllegati = null;
  private Boolean docAutenticato = null;
  private Boolean docAutenticatoCopiaAutenticata = null;
  private String idStatoDiEfficacia = null;
  private String idFormaDocumentaria = null;
  private String vitalRecordCode = null;
  private String idCorrente = null;
  private List<String> soggettoProduttore = new ArrayList<>();
  private Boolean docAutentico = null;
  private Boolean docAutenticatoFirmaAutenticata = null;
  private String applicativoAlimentante = null;
  private String tipoDocumento = null;
  private Boolean allegato = null;
  private String fileName = null;
  private String mimeType = null;
  private byte[] content = null;
  private Boolean copiaCartacea = null;
  private String collocazioneCartacea = null;
  private Boolean cartaceo = null;
  private LocalDate dataMemorizzazione = null;
  private Integer progressivoPerDocumento = null;
  private Boolean contenutoFisicoModificabile = null;
  private Boolean contenutoFisicoSbustamento = null;
  private Boolean contenutoFisicoWorkingCopy = null;
  private GruppoAllegatiProperties gruppoAllegati = null;
  private ObjectId classificazionePrincipale = null;
  private LocalDate dataFinePubblicazione = null;
  private String indiceClassificazioneEstesa = null;
  private List<String> idAnnotazioniList = new ArrayList<>();
  private String indiceClassificazione = null;
  private List<String> idProtocolloList = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: id 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: dbKey 
  public String getDbKey() {
    return dbKey;
  }
  public void setDbKey(String dbKey) {
    this.dbKey = dbKey;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceBarre 
  public String getCodiceBarre() {
    return codiceBarre;
  }
  public void setCodiceBarre(String codiceBarre) {
    this.codiceBarre = codiceBarre;
  }

  /**
   **/
  


  // nome originario nello yaml: changeToken 
  public OffsetDateTime getChangeToken() {
    return changeToken;
  }
  public void setChangeToken(OffsetDateTime changeToken) {
    this.changeToken = changeToken;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoDocFisico 
  public String getTipoDocFisico() {
    return tipoDocFisico;
  }
  public void setTipoDocFisico(String tipoDocFisico) {
    this.tipoDocFisico = tipoDocFisico;
  }

  /**
   **/
  


  // nome originario nello yaml: composizione 
  public String getComposizione() {
    return composizione;
  }
  public void setComposizione(String composizione) {
    this.composizione = composizione;
  }

  /**
   **/
  


  // nome originario nello yaml: multiplo 
  public Boolean isMultiplo() {
    return multiplo;
  }
  public void setMultiplo(Boolean multiplo) {
    this.multiplo = multiplo;
  }

  /**
   **/
  


  // nome originario nello yaml: registrato 
  public Boolean isRegistrato() {
    return registrato;
  }
  public void setRegistrato(Boolean registrato) {
    this.registrato = registrato;
  }

  /**
   **/
  


  // nome originario nello yaml: modificabile 
  public Boolean isModificabile() {
    return modificabile;
  }
  public void setModificabile(Boolean modificabile) {
    this.modificabile = modificabile;
  }

  /**
   **/
  


  // nome originario nello yaml: definitivo 
  public Boolean isDefinitivo() {
    return definitivo;
  }
  public void setDefinitivo(Boolean definitivo) {
    this.definitivo = definitivo;
  }

  /**
   **/
  


  // nome originario nello yaml: origineInterna 
  public Boolean isOrigineInterna() {
    return origineInterna;
  }
  public void setOrigineInterna(Boolean origineInterna) {
    this.origineInterna = origineInterna;
  }

  /**
   **/
  


  // nome originario nello yaml: analogico 
  public Boolean isAnalogico() {
    return analogico;
  }
  public void setAnalogico(Boolean analogico) {
    this.analogico = analogico;
  }

  /**
   **/
  


  // nome originario nello yaml: rappresentazioneDigitale 
  public Boolean isRappresentazioneDigitale() {
    return rappresentazioneDigitale;
  }
  public void setRappresentazioneDigitale(Boolean rappresentazioneDigitale) {
    this.rappresentazioneDigitale = rappresentazioneDigitale;
  }

  /**
   **/
  


  // nome originario nello yaml: daConservare 
  public Boolean isDaConservare() {
    return daConservare;
  }
  public void setDaConservare(Boolean daConservare) {
    this.daConservare = daConservare;
  }

  /**
   **/
  


  // nome originario nello yaml: prontoPerConservazione 
  public Boolean isProntoPerConservazione() {
    return prontoPerConservazione;
  }
  public void setProntoPerConservazione(Boolean prontoPerConservazione) {
    this.prontoPerConservazione = prontoPerConservazione;
  }

  /**
   **/
  


  // nome originario nello yaml: daConservareDopoIl 
  public LocalDate getDaConservareDopoIl() {
    return daConservareDopoIl;
  }
  public void setDaConservareDopoIl(LocalDate daConservareDopoIl) {
    this.daConservareDopoIl = daConservareDopoIl;
  }

  /**
   **/
  


  // nome originario nello yaml: daConservarePrimaDel 
  public LocalDate getDaConservarePrimaDel() {
    return daConservarePrimaDel;
  }
  public void setDaConservarePrimaDel(LocalDate daConservarePrimaDel) {
    this.daConservarePrimaDel = daConservarePrimaDel;
  }

  /**
   **/
  


  // nome originario nello yaml: conservato 
  public Boolean isConservato() {
    return conservato;
  }
  public void setConservato(Boolean conservato) {
    this.conservato = conservato;
  }

  /**
   **/
  


  // nome originario nello yaml: datiPersonali 
  public Boolean isDatiPersonali() {
    return datiPersonali;
  }
  public void setDatiPersonali(Boolean datiPersonali) {
    this.datiPersonali = datiPersonali;
  }

  /**
   **/
  


  // nome originario nello yaml: datiSensibili 
  public Boolean isDatiSensibili() {
    return datiSensibili;
  }
  public void setDatiSensibili(Boolean datiSensibili) {
    this.datiSensibili = datiSensibili;
  }

  /**
   **/
  


  // nome originario nello yaml: datiRiservati 
  public Boolean isDatiRiservati() {
    return datiRiservati;
  }
  public void setDatiRiservati(Boolean datiRiservati) {
    this.datiRiservati = datiRiservati;
  }

  /**
   **/
  


  // nome originario nello yaml: dataCreazione 
  public LocalDate getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(LocalDate dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   **/
  


  // nome originario nello yaml: paroleChiave 
  public String getParoleChiave() {
    return paroleChiave;
  }
  public void setParoleChiave(String paroleChiave) {
    this.paroleChiave = paroleChiave;
  }

  /**
   **/
  


  // nome originario nello yaml: modSMSQuarantena 
  public Boolean isModSMSQuarantena() {
    return modSMSQuarantena;
  }
  public void setModSMSQuarantena(Boolean modSMSQuarantena) {
    this.modSMSQuarantena = modSMSQuarantena;
  }

  /**
   **/
  


  // nome originario nello yaml: congelato 
  public Boolean isCongelato() {
    return congelato;
  }
  public void setCongelato(Boolean congelato) {
    this.congelato = congelato;
  }

  /**
   **/
  


  // nome originario nello yaml: referenziabile 
  public Boolean isReferenziabile() {
    return referenziabile;
  }
  public void setReferenziabile(Boolean referenziabile) {
    this.referenziabile = referenziabile;
  }

  /**
   **/
  


  // nome originario nello yaml: autoreGiuridico 
  public List<String> getAutoreGiuridico() {
    return autoreGiuridico;
  }
  public void setAutoreGiuridico(List<String> autoreGiuridico) {
    this.autoreGiuridico = autoreGiuridico;
  }

  /**
   **/
  


  // nome originario nello yaml: autoreFisico 
  public List<String> getAutoreFisico() {
    return autoreFisico;
  }
  public void setAutoreFisico(List<String> autoreFisico) {
    this.autoreFisico = autoreFisico;
  }

  /**
   **/
  


  // nome originario nello yaml: scrittore 
  public List<String> getScrittore() {
    return scrittore;
  }
  public void setScrittore(List<String> scrittore) {
    this.scrittore = scrittore;
  }

  /**
   **/
  


  // nome originario nello yaml: originatore 
  public List<String> getOriginatore() {
    return originatore;
  }
  public void setOriginatore(List<String> originatore) {
    this.originatore = originatore;
  }

  /**
   **/
  


  // nome originario nello yaml: destinatarioGiuridico 
  public List<String> getDestinatarioGiuridico() {
    return destinatarioGiuridico;
  }
  public void setDestinatarioGiuridico(List<String> destinatarioGiuridico) {
    this.destinatarioGiuridico = destinatarioGiuridico;
  }

  /**
   **/
  


  // nome originario nello yaml: destinatarioFisico 
  public List<String> getDestinatarioFisico() {
    return destinatarioFisico;
  }
  public void setDestinatarioFisico(List<String> destinatarioFisico) {
    this.destinatarioFisico = destinatarioFisico;
  }

  /**
   **/
  


  // nome originario nello yaml: oggetto 
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  /**
   **/
  


  // nome originario nello yaml: dataDocTopica 
  public String getDataDocTopica() {
    return dataDocTopica;
  }
  public void setDataDocTopica(String dataDocTopica) {
    this.dataDocTopica = dataDocTopica;
  }

  /**
   **/
  


  // nome originario nello yaml: dataDocCronica 
  public LocalDate getDataDocCronica() {
    return dataDocCronica;
  }
  public void setDataDocCronica(LocalDate dataDocCronica) {
    this.dataDocCronica = dataDocCronica;
  }

  /**
   **/
  


  // nome originario nello yaml: numRepertorio 
  public String getNumRepertorio() {
    return numRepertorio;
  }
  public void setNumRepertorio(String numRepertorio) {
    this.numRepertorio = numRepertorio;
  }

  /**
   **/
  


  // nome originario nello yaml: docConAllegati 
  public Boolean isDocConAllegati() {
    return docConAllegati;
  }
  public void setDocConAllegati(Boolean docConAllegati) {
    this.docConAllegati = docConAllegati;
  }

  /**
   **/
  


  // nome originario nello yaml: docAutenticato 
  public Boolean isDocAutenticato() {
    return docAutenticato;
  }
  public void setDocAutenticato(Boolean docAutenticato) {
    this.docAutenticato = docAutenticato;
  }

  /**
   **/
  


  // nome originario nello yaml: docAutenticatoCopiaAutenticata 
  public Boolean isDocAutenticatoCopiaAutenticata() {
    return docAutenticatoCopiaAutenticata;
  }
  public void setDocAutenticatoCopiaAutenticata(Boolean docAutenticatoCopiaAutenticata) {
    this.docAutenticatoCopiaAutenticata = docAutenticatoCopiaAutenticata;
  }

  /**
   **/
  


  // nome originario nello yaml: idStatoDiEfficacia 
  public String getIdStatoDiEfficacia() {
    return idStatoDiEfficacia;
  }
  public void setIdStatoDiEfficacia(String idStatoDiEfficacia) {
    this.idStatoDiEfficacia = idStatoDiEfficacia;
  }

  /**
   **/
  


  // nome originario nello yaml: idFormaDocumentaria 
  public String getIdFormaDocumentaria() {
    return idFormaDocumentaria;
  }
  public void setIdFormaDocumentaria(String idFormaDocumentaria) {
    this.idFormaDocumentaria = idFormaDocumentaria;
  }

  /**
   **/
  


  // nome originario nello yaml: vitalRecordCode 
  public String getVitalRecordCode() {
    return vitalRecordCode;
  }
  public void setVitalRecordCode(String vitalRecordCode) {
    this.vitalRecordCode = vitalRecordCode;
  }

  /**
   **/
  


  // nome originario nello yaml: idCorrente 
  public String getIdCorrente() {
    return idCorrente;
  }
  public void setIdCorrente(String idCorrente) {
    this.idCorrente = idCorrente;
  }

  /**
   **/
  


  // nome originario nello yaml: soggettoProduttore 
  public List<String> getSoggettoProduttore() {
    return soggettoProduttore;
  }
  public void setSoggettoProduttore(List<String> soggettoProduttore) {
    this.soggettoProduttore = soggettoProduttore;
  }

  /**
   **/
  


  // nome originario nello yaml: docAutentico 
  public Boolean isDocAutentico() {
    return docAutentico;
  }
  public void setDocAutentico(Boolean docAutentico) {
    this.docAutentico = docAutentico;
  }

  /**
   **/
  


  // nome originario nello yaml: docAutenticatoFirmaAutenticata 
  public Boolean isDocAutenticatoFirmaAutenticata() {
    return docAutenticatoFirmaAutenticata;
  }
  public void setDocAutenticatoFirmaAutenticata(Boolean docAutenticatoFirmaAutenticata) {
    this.docAutenticatoFirmaAutenticata = docAutenticatoFirmaAutenticata;
  }

  /**
   **/
  


  // nome originario nello yaml: applicativoAlimentante 
  public String getApplicativoAlimentante() {
    return applicativoAlimentante;
  }
  public void setApplicativoAlimentante(String applicativoAlimentante) {
    this.applicativoAlimentante = applicativoAlimentante;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoDocumento 
  public String getTipoDocumento() {
    return tipoDocumento;
  }
  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: allegato 
  public Boolean isAllegato() {
    return allegato;
  }
  public void setAllegato(Boolean allegato) {
    this.allegato = allegato;
  }

  /**
   **/
  


  // nome originario nello yaml: fileName 
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   **/
  


  // nome originario nello yaml: mimeType 
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   **/
  


  // nome originario nello yaml: content 
  @Pattern(regexp="^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
  public byte[] getContent() {
    return content;
  }
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   **/
  


  // nome originario nello yaml: copiaCartacea 
  public Boolean isCopiaCartacea() {
    return copiaCartacea;
  }
  public void setCopiaCartacea(Boolean copiaCartacea) {
    this.copiaCartacea = copiaCartacea;
  }

  /**
   **/
  


  // nome originario nello yaml: collocazioneCartacea 
  public String getCollocazioneCartacea() {
    return collocazioneCartacea;
  }
  public void setCollocazioneCartacea(String collocazioneCartacea) {
    this.collocazioneCartacea = collocazioneCartacea;
  }

  /**
   **/
  


  // nome originario nello yaml: cartaceo 
  public Boolean isCartaceo() {
    return cartaceo;
  }
  public void setCartaceo(Boolean cartaceo) {
    this.cartaceo = cartaceo;
  }

  /**
   **/
  


  // nome originario nello yaml: dataMemorizzazione 
  public LocalDate getDataMemorizzazione() {
    return dataMemorizzazione;
  }
  public void setDataMemorizzazione(LocalDate dataMemorizzazione) {
    this.dataMemorizzazione = dataMemorizzazione;
  }

  /**
   **/
  


  // nome originario nello yaml: progressivoPerDocumento 
  public Integer getProgressivoPerDocumento() {
    return progressivoPerDocumento;
  }
  public void setProgressivoPerDocumento(Integer progressivoPerDocumento) {
    this.progressivoPerDocumento = progressivoPerDocumento;
  }

  /**
   **/
  


  // nome originario nello yaml: contenutoFisicoModificabile 
  public Boolean isContenutoFisicoModificabile() {
    return contenutoFisicoModificabile;
  }
  public void setContenutoFisicoModificabile(Boolean contenutoFisicoModificabile) {
    this.contenutoFisicoModificabile = contenutoFisicoModificabile;
  }

  /**
   **/
  


  // nome originario nello yaml: contenutoFisicoSbustamento 
  public Boolean isContenutoFisicoSbustamento() {
    return contenutoFisicoSbustamento;
  }
  public void setContenutoFisicoSbustamento(Boolean contenutoFisicoSbustamento) {
    this.contenutoFisicoSbustamento = contenutoFisicoSbustamento;
  }

  /**
   **/
  


  // nome originario nello yaml: contenutoFisicoWorkingCopy 
  public Boolean isContenutoFisicoWorkingCopy() {
    return contenutoFisicoWorkingCopy;
  }
  public void setContenutoFisicoWorkingCopy(Boolean contenutoFisicoWorkingCopy) {
    this.contenutoFisicoWorkingCopy = contenutoFisicoWorkingCopy;
  }

  /**
   **/
  


  // nome originario nello yaml: gruppoAllegati 
  public GruppoAllegatiProperties getGruppoAllegati() {
    return gruppoAllegati;
  }
  public void setGruppoAllegati(GruppoAllegatiProperties gruppoAllegati) {
    this.gruppoAllegati = gruppoAllegati;
  }

  /**
   **/
  


  // nome originario nello yaml: classificazionePrincipale 
  public ObjectId getClassificazionePrincipale() {
    return classificazionePrincipale;
  }
  public void setClassificazionePrincipale(ObjectId classificazionePrincipale) {
    this.classificazionePrincipale = classificazionePrincipale;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFinePubblicazione 
  public LocalDate getDataFinePubblicazione() {
    return dataFinePubblicazione;
  }
  public void setDataFinePubblicazione(LocalDate dataFinePubblicazione) {
    this.dataFinePubblicazione = dataFinePubblicazione;
  }

  /**
   **/
  


  // nome originario nello yaml: indiceClassificazioneEstesa 
  public String getIndiceClassificazioneEstesa() {
    return indiceClassificazioneEstesa;
  }
  public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
    this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
  }

  /**
   **/
  


  // nome originario nello yaml: idAnnotazioniList 
  public List<String> getIdAnnotazioniList() {
    return idAnnotazioniList;
  }
  public void setIdAnnotazioniList(List<String> idAnnotazioniList) {
    this.idAnnotazioniList = idAnnotazioniList;
  }

  /**
   **/
  


  // nome originario nello yaml: indiceClassificazione 
  public String getIndiceClassificazione() {
    return indiceClassificazione;
  }
  public void setIndiceClassificazione(String indiceClassificazione) {
    this.indiceClassificazione = indiceClassificazione;
  }

  /**
   **/
  


  // nome originario nello yaml: idProtocolloList 
  public List<String> getIdProtocolloList() {
    return idProtocolloList;
  }
  public void setIdProtocolloList(List<String> idProtocolloList) {
    this.idProtocolloList = idProtocolloList;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoSemplice documentoSemplice = (DocumentoSemplice) o;
    return Objects.equals(id, documentoSemplice.id) &&
        Objects.equals(dbKey, documentoSemplice.dbKey) &&
        Objects.equals(codiceBarre, documentoSemplice.codiceBarre) &&
        Objects.equals(changeToken, documentoSemplice.changeToken) &&
        Objects.equals(tipoDocFisico, documentoSemplice.tipoDocFisico) &&
        Objects.equals(composizione, documentoSemplice.composizione) &&
        Objects.equals(multiplo, documentoSemplice.multiplo) &&
        Objects.equals(registrato, documentoSemplice.registrato) &&
        Objects.equals(modificabile, documentoSemplice.modificabile) &&
        Objects.equals(definitivo, documentoSemplice.definitivo) &&
        Objects.equals(origineInterna, documentoSemplice.origineInterna) &&
        Objects.equals(analogico, documentoSemplice.analogico) &&
        Objects.equals(rappresentazioneDigitale, documentoSemplice.rappresentazioneDigitale) &&
        Objects.equals(daConservare, documentoSemplice.daConservare) &&
        Objects.equals(prontoPerConservazione, documentoSemplice.prontoPerConservazione) &&
        Objects.equals(daConservareDopoIl, documentoSemplice.daConservareDopoIl) &&
        Objects.equals(daConservarePrimaDel, documentoSemplice.daConservarePrimaDel) &&
        Objects.equals(conservato, documentoSemplice.conservato) &&
        Objects.equals(datiPersonali, documentoSemplice.datiPersonali) &&
        Objects.equals(datiSensibili, documentoSemplice.datiSensibili) &&
        Objects.equals(datiRiservati, documentoSemplice.datiRiservati) &&
        Objects.equals(dataCreazione, documentoSemplice.dataCreazione) &&
        Objects.equals(paroleChiave, documentoSemplice.paroleChiave) &&
        Objects.equals(modSMSQuarantena, documentoSemplice.modSMSQuarantena) &&
        Objects.equals(congelato, documentoSemplice.congelato) &&
        Objects.equals(referenziabile, documentoSemplice.referenziabile) &&
        Objects.equals(autoreGiuridico, documentoSemplice.autoreGiuridico) &&
        Objects.equals(autoreFisico, documentoSemplice.autoreFisico) &&
        Objects.equals(scrittore, documentoSemplice.scrittore) &&
        Objects.equals(originatore, documentoSemplice.originatore) &&
        Objects.equals(destinatarioGiuridico, documentoSemplice.destinatarioGiuridico) &&
        Objects.equals(destinatarioFisico, documentoSemplice.destinatarioFisico) &&
        Objects.equals(oggetto, documentoSemplice.oggetto) &&
        Objects.equals(dataDocTopica, documentoSemplice.dataDocTopica) &&
        Objects.equals(dataDocCronica, documentoSemplice.dataDocCronica) &&
        Objects.equals(numRepertorio, documentoSemplice.numRepertorio) &&
        Objects.equals(docConAllegati, documentoSemplice.docConAllegati) &&
        Objects.equals(docAutenticato, documentoSemplice.docAutenticato) &&
        Objects.equals(docAutenticatoCopiaAutenticata, documentoSemplice.docAutenticatoCopiaAutenticata) &&
        Objects.equals(idStatoDiEfficacia, documentoSemplice.idStatoDiEfficacia) &&
        Objects.equals(idFormaDocumentaria, documentoSemplice.idFormaDocumentaria) &&
        Objects.equals(vitalRecordCode, documentoSemplice.vitalRecordCode) &&
        Objects.equals(idCorrente, documentoSemplice.idCorrente) &&
        Objects.equals(soggettoProduttore, documentoSemplice.soggettoProduttore) &&
        Objects.equals(docAutentico, documentoSemplice.docAutentico) &&
        Objects.equals(docAutenticatoFirmaAutenticata, documentoSemplice.docAutenticatoFirmaAutenticata) &&
        Objects.equals(applicativoAlimentante, documentoSemplice.applicativoAlimentante) &&
        Objects.equals(tipoDocumento, documentoSemplice.tipoDocumento) &&
        Objects.equals(allegato, documentoSemplice.allegato) &&
        Objects.equals(fileName, documentoSemplice.fileName) &&
        Objects.equals(mimeType, documentoSemplice.mimeType) &&
        Objects.equals(content, documentoSemplice.content) &&
        Objects.equals(copiaCartacea, documentoSemplice.copiaCartacea) &&
        Objects.equals(collocazioneCartacea, documentoSemplice.collocazioneCartacea) &&
        Objects.equals(cartaceo, documentoSemplice.cartaceo) &&
        Objects.equals(dataMemorizzazione, documentoSemplice.dataMemorizzazione) &&
        Objects.equals(progressivoPerDocumento, documentoSemplice.progressivoPerDocumento) &&
        Objects.equals(contenutoFisicoModificabile, documentoSemplice.contenutoFisicoModificabile) &&
        Objects.equals(contenutoFisicoSbustamento, documentoSemplice.contenutoFisicoSbustamento) &&
        Objects.equals(contenutoFisicoWorkingCopy, documentoSemplice.contenutoFisicoWorkingCopy) &&
        Objects.equals(gruppoAllegati, documentoSemplice.gruppoAllegati) &&
        Objects.equals(classificazionePrincipale, documentoSemplice.classificazionePrincipale) &&
        Objects.equals(dataFinePubblicazione, documentoSemplice.dataFinePubblicazione) &&
        Objects.equals(indiceClassificazioneEstesa, documentoSemplice.indiceClassificazioneEstesa) &&
        Objects.equals(idAnnotazioniList, documentoSemplice.idAnnotazioniList) &&
        Objects.equals(indiceClassificazione, documentoSemplice.indiceClassificazione) &&
        Objects.equals(idProtocolloList, documentoSemplice.idProtocolloList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, codiceBarre, changeToken, tipoDocFisico, composizione, multiplo, registrato, modificabile, definitivo, origineInterna, analogico, rappresentazioneDigitale, daConservare, prontoPerConservazione, daConservareDopoIl, daConservarePrimaDel, conservato, datiPersonali, datiSensibili, datiRiservati, dataCreazione, paroleChiave, modSMSQuarantena, congelato, referenziabile, autoreGiuridico, autoreFisico, scrittore, originatore, destinatarioGiuridico, destinatarioFisico, oggetto, dataDocTopica, dataDocCronica, numRepertorio, docConAllegati, docAutenticato, docAutenticatoCopiaAutenticata, idStatoDiEfficacia, idFormaDocumentaria, vitalRecordCode, idCorrente, soggettoProduttore, docAutentico, docAutenticatoFirmaAutenticata, applicativoAlimentante, tipoDocumento, allegato, fileName, mimeType, content, copiaCartacea, collocazioneCartacea, cartaceo, dataMemorizzazione, progressivoPerDocumento, contenutoFisicoModificabile, contenutoFisicoSbustamento, contenutoFisicoWorkingCopy, gruppoAllegati, classificazionePrincipale, dataFinePubblicazione, indiceClassificazioneEstesa, idAnnotazioniList, indiceClassificazione, idProtocolloList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoSemplice {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    codiceBarre: ").append(toIndentedString(codiceBarre)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    tipoDocFisico: ").append(toIndentedString(tipoDocFisico)).append("\n");
    sb.append("    composizione: ").append(toIndentedString(composizione)).append("\n");
    sb.append("    multiplo: ").append(toIndentedString(multiplo)).append("\n");
    sb.append("    registrato: ").append(toIndentedString(registrato)).append("\n");
    sb.append("    modificabile: ").append(toIndentedString(modificabile)).append("\n");
    sb.append("    definitivo: ").append(toIndentedString(definitivo)).append("\n");
    sb.append("    origineInterna: ").append(toIndentedString(origineInterna)).append("\n");
    sb.append("    analogico: ").append(toIndentedString(analogico)).append("\n");
    sb.append("    rappresentazioneDigitale: ").append(toIndentedString(rappresentazioneDigitale)).append("\n");
    sb.append("    daConservare: ").append(toIndentedString(daConservare)).append("\n");
    sb.append("    prontoPerConservazione: ").append(toIndentedString(prontoPerConservazione)).append("\n");
    sb.append("    daConservareDopoIl: ").append(toIndentedString(daConservareDopoIl)).append("\n");
    sb.append("    daConservarePrimaDel: ").append(toIndentedString(daConservarePrimaDel)).append("\n");
    sb.append("    conservato: ").append(toIndentedString(conservato)).append("\n");
    sb.append("    datiPersonali: ").append(toIndentedString(datiPersonali)).append("\n");
    sb.append("    datiSensibili: ").append(toIndentedString(datiSensibili)).append("\n");
    sb.append("    datiRiservati: ").append(toIndentedString(datiRiservati)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    paroleChiave: ").append(toIndentedString(paroleChiave)).append("\n");
    sb.append("    modSMSQuarantena: ").append(toIndentedString(modSMSQuarantena)).append("\n");
    sb.append("    congelato: ").append(toIndentedString(congelato)).append("\n");
    sb.append("    referenziabile: ").append(toIndentedString(referenziabile)).append("\n");
    sb.append("    autoreGiuridico: ").append(toIndentedString(autoreGiuridico)).append("\n");
    sb.append("    autoreFisico: ").append(toIndentedString(autoreFisico)).append("\n");
    sb.append("    scrittore: ").append(toIndentedString(scrittore)).append("\n");
    sb.append("    originatore: ").append(toIndentedString(originatore)).append("\n");
    sb.append("    destinatarioGiuridico: ").append(toIndentedString(destinatarioGiuridico)).append("\n");
    sb.append("    destinatarioFisico: ").append(toIndentedString(destinatarioFisico)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    dataDocTopica: ").append(toIndentedString(dataDocTopica)).append("\n");
    sb.append("    dataDocCronica: ").append(toIndentedString(dataDocCronica)).append("\n");
    sb.append("    numRepertorio: ").append(toIndentedString(numRepertorio)).append("\n");
    sb.append("    docConAllegati: ").append(toIndentedString(docConAllegati)).append("\n");
    sb.append("    docAutenticato: ").append(toIndentedString(docAutenticato)).append("\n");
    sb.append("    docAutenticatoCopiaAutenticata: ").append(toIndentedString(docAutenticatoCopiaAutenticata)).append("\n");
    sb.append("    idStatoDiEfficacia: ").append(toIndentedString(idStatoDiEfficacia)).append("\n");
    sb.append("    idFormaDocumentaria: ").append(toIndentedString(idFormaDocumentaria)).append("\n");
    sb.append("    vitalRecordCode: ").append(toIndentedString(vitalRecordCode)).append("\n");
    sb.append("    idCorrente: ").append(toIndentedString(idCorrente)).append("\n");
    sb.append("    soggettoProduttore: ").append(toIndentedString(soggettoProduttore)).append("\n");
    sb.append("    docAutentico: ").append(toIndentedString(docAutentico)).append("\n");
    sb.append("    docAutenticatoFirmaAutenticata: ").append(toIndentedString(docAutenticatoFirmaAutenticata)).append("\n");
    sb.append("    applicativoAlimentante: ").append(toIndentedString(applicativoAlimentante)).append("\n");
    sb.append("    tipoDocumento: ").append(toIndentedString(tipoDocumento)).append("\n");
    sb.append("    allegato: ").append(toIndentedString(allegato)).append("\n");
    sb.append("    fileName: ").append(toIndentedString(fileName)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    copiaCartacea: ").append(toIndentedString(copiaCartacea)).append("\n");
    sb.append("    collocazioneCartacea: ").append(toIndentedString(collocazioneCartacea)).append("\n");
    sb.append("    cartaceo: ").append(toIndentedString(cartaceo)).append("\n");
    sb.append("    dataMemorizzazione: ").append(toIndentedString(dataMemorizzazione)).append("\n");
    sb.append("    progressivoPerDocumento: ").append(toIndentedString(progressivoPerDocumento)).append("\n");
    sb.append("    contenutoFisicoModificabile: ").append(toIndentedString(contenutoFisicoModificabile)).append("\n");
    sb.append("    contenutoFisicoSbustamento: ").append(toIndentedString(contenutoFisicoSbustamento)).append("\n");
    sb.append("    contenutoFisicoWorkingCopy: ").append(toIndentedString(contenutoFisicoWorkingCopy)).append("\n");
    sb.append("    gruppoAllegati: ").append(toIndentedString(gruppoAllegati)).append("\n");
    sb.append("    classificazionePrincipale: ").append(toIndentedString(classificazionePrincipale)).append("\n");
    sb.append("    dataFinePubblicazione: ").append(toIndentedString(dataFinePubblicazione)).append("\n");
    sb.append("    indiceClassificazioneEstesa: ").append(toIndentedString(indiceClassificazioneEstesa)).append("\n");
    sb.append("    idAnnotazioniList: ").append(toIndentedString(idAnnotazioniList)).append("\n");
    sb.append("    indiceClassificazione: ").append(toIndentedString(indiceClassificazione)).append("\n");
    sb.append("    idProtocolloList: ").append(toIndentedString(idProtocolloList)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

