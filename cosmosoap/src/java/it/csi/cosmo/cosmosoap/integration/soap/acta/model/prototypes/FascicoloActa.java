/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;


/**
 *
 */
public abstract class FascicoloActa extends AggregazioneActa {

  @ActaProperty(propertyName = "numero")
  protected String numero;

  @ActaProperty(propertyName = "oggetto")
  protected String oggetto;

  @ActaProperty(propertyName = "soggetto")
  protected String soggetto;

  @ActaProperty(propertyName = "idVitalRecordCode")
  protected String vitalRecordCode;

  @ActaProperty(propertyName = "stato")
  protected String stato;

  @ActaProperty(propertyName = "idAOORespMat")
  protected Long idAOOResponsabileMateriale;

  @ActaProperty(propertyName = "idStrutturaRespMat")
  protected Long idStrutturaResponsabileMateriale;

  @ActaProperty(propertyName = "idNodoRespMat")
  protected Long idNodoResponsabileMateriale;

  @ActaProperty(propertyName = "codice")
  protected String codice;

  @ActaProperty(propertyName = "descrizione")
  protected String descrizione;

  @ActaProperty(propertyName = "collocazioneCarteceo")
  protected String collocazioneCarteceo;

  @ActaProperty(propertyName = "ecmUuidNodo")
  protected String ecmUuidNodo;

  @ActaProperty(propertyName = "uuid")
  protected String uuid;

  @ActaProperty(propertyName = "idMovimentazioniList", itemType = String.class,
      className = "MovimentazionePropertiesType")
  protected List<String> idMovimentazioni;

  @ActaProperty(propertyName = "utenteCreazione")
  protected Long idUtenteCreazione;

  @ActaProperty(propertyName = "idAnnotazioniList", itemType = Long.class,
      className = "AnnotazionePropertiesType")
  protected List<Long> idAnnotazioni;

  @ActaProperty(propertyName = "idDeposito")
  protected String idDeposito;

  @ActaProperty(propertyName = "indiceClassificazioneEstesa")
  protected String indiceClassificazioneEstesa;

  @ActaProperty(propertyName = "dataCreazione")
  protected LocalDate dataCreazione;

  @ActaProperty(propertyName = "dataFine")
  protected LocalDate dataFine;

  @ActaProperty(propertyName = "conservazioneCorrente")
  protected Integer durataConservazioneCorrente;

  @ActaProperty(propertyName = "conservazioneGenerale")
  protected Integer durataConservazioneGenerale;

  @ActaProperty(propertyName = "archivioCorrente")
  protected Boolean inArchivioCorrente;

  @ActaProperty(propertyName = "datiPersonali")
  protected Boolean contieneDatiPersonali;

  @ActaProperty(propertyName = "datiSensibili")
  protected Boolean contieneDatiSensibili;

  @ActaProperty(propertyName = "datiRiservati")
  protected Boolean contieneDatiRiservati;

  @ActaProperty(propertyName = "paroleChiave")
  protected String paroleChiave;

  @ActaProperty(propertyName = "dataCancellazione")
  protected LocalDate dataCancellazione;

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getOggetto() {
    return oggetto;
  }

  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
  }

  public String getSoggetto() {
    return soggetto;
  }

  public void setSoggetto(String soggetto) {
    this.soggetto = soggetto;
  }

  public String getVitalRecordCode() {
    return vitalRecordCode;
  }

  public void setVitalRecordCode(String vitalRecordCode) {
    this.vitalRecordCode = vitalRecordCode;
  }

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public Long getIdAOOResponsabileMateriale() {
    return idAOOResponsabileMateriale;
  }

  public void setIdAOOResponsabileMateriale(Long idAOOResponsabileMateriale) {
    this.idAOOResponsabileMateriale = idAOOResponsabileMateriale;
  }

  public void setIdStrutturaResponsabileMateriale(Long idStrutturaResponsabileMateriale) {
    this.idStrutturaResponsabileMateriale = idStrutturaResponsabileMateriale;
  }

  public void setIdNodoResponsabileMateriale(Long idNodoResponsabileMateriale) {
    this.idNodoResponsabileMateriale = idNodoResponsabileMateriale;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getCollocazioneCarteceo() {
    return collocazioneCarteceo;
  }

  public void setCollocazioneCarteceo(String collocazioneCarteceo) {
    this.collocazioneCarteceo = collocazioneCarteceo;
  }

  public String getEcmUuidNodo() {
    return ecmUuidNodo;
  }

  public void setEcmUuidNodo(String ecmUuidNodo) {
    this.ecmUuidNodo = ecmUuidNodo;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public List<String> getIdMovimentazioni() {
    return idMovimentazioni;
  }

  public void setIdMovimentazioni(List<String> idMovimentazioni) {
    this.idMovimentazioni = idMovimentazioni;
  }

  public Long getIdUtenteCreazione() {
    return idUtenteCreazione;
  }

  public void setIdUtenteCreazione(Long idUtenteCreazione) {
    this.idUtenteCreazione = idUtenteCreazione;
  }

  public List<Long> getIdAnnotazioni() {
    return idAnnotazioni;
  }

  public void setIdAnnotazioni(List<Long> idAnnotazioni) {
    this.idAnnotazioni = idAnnotazioni;
  }

  public void setIdDeposito(String idDeposito) {
    this.idDeposito = idDeposito;
  }

  public String getIndiceClassificazioneEstesa() {
    return indiceClassificazioneEstesa;
  }

  public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
    this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
  }

  public LocalDate getDataCreazione() {
    return dataCreazione;
  }

  public void setDataCreazione(LocalDate dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  public LocalDate getDataFine() {
    return dataFine;
  }

  public void setDataFine(LocalDate dataFine) {
    this.dataFine = dataFine;
  }

  public Integer getDurataConservazioneCorrente() {
    return durataConservazioneCorrente;
  }

  public void setDurataConservazioneCorrente(Integer durataConservazioneCorrente) {
    this.durataConservazioneCorrente = durataConservazioneCorrente;
  }

  public Integer getDurataConservazioneGenerale() {
    return durataConservazioneGenerale;
  }

  public void setDurataConservazioneGenerale(Integer durataConservazioneGenerale) {
    this.durataConservazioneGenerale = durataConservazioneGenerale;
  }

  public Boolean getInArchivioCorrente() {
    return inArchivioCorrente;
  }

  public void setInArchivioCorrente(Boolean inArchivioCorrente) {
    this.inArchivioCorrente = inArchivioCorrente;
  }

  public Boolean getContieneDatiPersonali() {
    return contieneDatiPersonali;
  }

  public void setContieneDatiPersonali(Boolean contieneDatiPersonali) {
    this.contieneDatiPersonali = contieneDatiPersonali;
  }

  public Boolean getContieneDatiSensibili() {
    return contieneDatiSensibili;
  }

  public void setContieneDatiSensibili(Boolean contieneDatiSensibili) {
    this.contieneDatiSensibili = contieneDatiSensibili;
  }

  public Boolean getContieneDatiRiservati() {
    return contieneDatiRiservati;
  }

  public void setContieneDatiRiservati(Boolean contieneDatiRiservati) {
    this.contieneDatiRiservati = contieneDatiRiservati;
  }

  public String getParoleChiave() {
    return paroleChiave;
  }

  public void setParoleChiave(String paroleChiave) {
    this.paroleChiave = paroleChiave;
  }

  public LocalDate getDataCancellazione() {
    return dataCancellazione;
  }

  public void setDataCancellazione(LocalDate dataCancellazione) {
    this.dataCancellazione = dataCancellazione;
  }

}
