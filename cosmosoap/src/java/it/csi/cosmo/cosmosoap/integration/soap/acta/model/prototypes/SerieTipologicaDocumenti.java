/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.acta.model.prototypes;

import java.time.LocalDate;
import java.util.List;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaModel;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.annotations.ActaProperty;
import it.csi.cosmo.cosmosoap.integration.soap.acta.model.impl.SerieTipologicaDocumentiDefaultImpl;


/**
 *
 */
@ActaModel(className = "SerieTipologicaDocumentiPropertiesType")
public abstract class SerieTipologicaDocumenti extends AggregazioneActa {

  public static SerieTipologicaDocumentiDefaultImpl.Builder builder() {
    return SerieTipologicaDocumentiDefaultImpl.builder();
  }

  @ActaProperty
  protected Boolean docAltraClassificazione;

  @ActaProperty
  protected String modalitaCalcoloProgDoc;

  @ActaProperty
  protected String parteFissa;

  protected Boolean registri;

  @ActaProperty
  protected Long idFascicoloStandard;

  @ActaProperty
  protected String stato;

  @ActaProperty
  protected String codice;

  @ActaProperty(propertyName = "obbligoFascStand")
  protected Boolean obbligoFascicoloStandard;

  @ActaProperty(propertyName = "tipologiaNumerazione")
  protected String tipologiaNumerazione;

  @ActaProperty(propertyName = "idAOORespMat")
  protected String idAooResponsabileMateriale;

  @ActaProperty(propertyName = "idStrutturaRespMat")
  protected String idStrutturaResponsabileMateriale;

  @ActaProperty(propertyName = "idNodoRespMat")
  protected String idNodoResponsabileMateriale;

  @ActaProperty
  protected String descrizione;

  @ActaProperty
  protected LocalDate dataCreazione;

  @ActaProperty
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

  @ActaProperty(propertyName = "collocazioneCarteceo")
  protected String collocazioneCarteceo;

  @ActaProperty(propertyName = "paroleChiave")
  protected String paroleChiave;

  @ActaProperty(propertyName = "dataCancellazione")
  protected LocalDate dataCancellazione;

  @ActaProperty(propertyName = "utenteCreazione")
  protected Long idUtenteCreazione;

  @ActaProperty(propertyName = "idMovimentazioniList", itemType = String.class,
      className = "MovimentazionePropertiesType")
  protected List<String> idMovimentazioni;

  @ActaProperty(propertyName = "idAnnotazioniList", itemType = Long.class,
      className = "AnnotazionePropertiesType")
  protected List<Long> idAnnotazioni;

  @ActaProperty(propertyName = "indiceClassificazioneEstesa",
      className = "IndiceClassificazioneEstesaType")
  protected String indiceClassificazioneEstesa;

  @ActaProperty(propertyName = "idDeposito", itemType = String.class)
  protected List<String> idDeposito;

  @ActaProperty(propertyName = "ecmUuidNodo")
  protected String uuidNodo;

  public Boolean getDocAltraClassificazione() {
    return docAltraClassificazione;
  }

  public void setDocAltraClassificazione(Boolean docAltraClassificazione) {
    this.docAltraClassificazione = docAltraClassificazione;
  }

  public String getModalitaCalcoloProgDoc() {
    return modalitaCalcoloProgDoc;
  }

  public void setModalitaCalcoloProgDoc(String modalitaCalcoloProgDoc) {
    this.modalitaCalcoloProgDoc = modalitaCalcoloProgDoc;
  }

  public String getParteFissa() {
    return parteFissa;
  }

  public void setParteFissa(String parteFissa) {
    this.parteFissa = parteFissa;
  }

  public Boolean getRegistri() {
    return registri;
  }

  public void setRegistri(Boolean registri) {
    this.registri = registri;
  }

  public String getUuidNodo() {
    return uuidNodo;
  }

  public void setUuidNodo(String uuidNodo) {
    this.uuidNodo = uuidNodo;
  }

  public Long getIdFascicoloStandard() {
    return idFascicoloStandard;
  }

  public void setIdFascicoloStandard(Long idFascicoloStandard) {
    this.idFascicoloStandard = idFascicoloStandard;
  }

  public String getStato() {
    return stato;
  }

  public void setStato(String stato) {
    this.stato = stato;
  }

  public String getCodice() {
    return codice;
  }

  public void setCodice(String codice) {
    this.codice = codice;
  }

  public Boolean getObbligoFascicoloStandard() {
    return obbligoFascicoloStandard;
  }

  public void setObbligoFascicoloStandard(Boolean obbligoFascicoloStandard) {
    this.obbligoFascicoloStandard = obbligoFascicoloStandard;
  }

  public String getTipologiaNumerazione() {
    return tipologiaNumerazione;
  }

  public void setTipologiaNumerazione(String tipologiaNumerazione) {
    this.tipologiaNumerazione = tipologiaNumerazione;
  }

  public String getIdAooResponsabileMateriale() {
    return idAooResponsabileMateriale;
  }

  public void setIdAooResponsabileMateriale(String idAooResponsabileMateriale) {
    this.idAooResponsabileMateriale = idAooResponsabileMateriale;
  }

  public String getIdStrutturaResponsabileMateriale() {
    return idStrutturaResponsabileMateriale;
  }

  public void setIdStrutturaResponsabileMateriale(String idStrutturaResponsabileMateriale) {
    this.idStrutturaResponsabileMateriale = idStrutturaResponsabileMateriale;
  }

  public String getIdNodoResponsabileMateriale() {
    return idNodoResponsabileMateriale;
  }

  public void setIdNodoResponsabileMateriale(String idNodoResponsabileMateriale) {
    this.idNodoResponsabileMateriale = idNodoResponsabileMateriale;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
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

  public String getCollocazioneCarteceo() {
    return collocazioneCarteceo;
  }

  public void setCollocazioneCarteceo(String collocazioneCarteceo) {
    this.collocazioneCarteceo = collocazioneCarteceo;
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

  public List<String> getIdDeposito() {
    return idDeposito;
  }

  public void setIdDeposito(List<String> idDeposito) {
    this.idDeposito = idDeposito;
  }

  public String getIndiceClassificazioneEstesa() {
    return indiceClassificazioneEstesa;
  }

  public void setIndiceClassificazioneEstesa(String indiceClassificazioneEstesa) {
    this.indiceClassificazioneEstesa = indiceClassificazioneEstesa;
  }

  @Override
  public String toString() {
    final int maxLen = 3;
    return "SerieFascicoliActa [idFascicoloStandard=" + idFascicoloStandard + ", stato=" + stato
        + ", codice=" + codice + ", obbligoFascicoloStandard=" + obbligoFascicoloStandard
        + ", tipologiaNumerazione=" + tipologiaNumerazione + ", idAooResponsabileMateriale="
        + idAooResponsabileMateriale + ", idStrutturaResponsabileMateriale="
        + idStrutturaResponsabileMateriale + ", idNodoResponsabileMateriale="
        + idNodoResponsabileMateriale + ", descrizione=" + descrizione + ", dataCreazione="
        + dataCreazione + ", dataFine=" + dataFine + ", durataConservazioneCorrente="
        + durataConservazioneCorrente + ", durataConservazioneGenerale="
        + durataConservazioneGenerale + ", inArchivioCorrente=" + inArchivioCorrente
        + ", contieneDatiPersonali=" + contieneDatiPersonali + ", contieneDatiSensibili="
        + contieneDatiSensibili + ", contieneDatiRiservati=" + contieneDatiRiservati
        + ", collocazioneCarteceo=" + collocazioneCarteceo + ", paroleChiave=" + paroleChiave
        + ", dataCancellazione=" + dataCancellazione + ", idMovimentazioni="
        + (idMovimentazioni != null
            ? idMovimentazioni.subList(0, Math.min(idMovimentazioni.size(), maxLen))
            : null)
        + ", idUtenteCreazione=" + idUtenteCreazione + ", idAnnotazioni="
        + (idAnnotazioni != null ? idAnnotazioni.subList(0, Math.min(idAnnotazioni.size(), maxLen))
            : null)
        + ", idDeposito="
        + (idDeposito != null ? idDeposito.subList(0, Math.min(idDeposito.size(), maxLen)) : null)
        + ", indiceClassificazioneEstesa=" + indiceClassificazioneEstesa + ", uuidNodo=" + uuidNodo
        + "]";
  }

}
