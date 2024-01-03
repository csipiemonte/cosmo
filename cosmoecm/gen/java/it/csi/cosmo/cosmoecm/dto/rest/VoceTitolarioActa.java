/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VoceTitolarioActa  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private String changeToken = null;
  private String dataInizio = null;
  private String dataFine = null;
  private String codice = null;
  private String descrizione = null;
  private String stato = null;
  private Boolean presenzaFascTemp = null;
  private String dataAggiornamento = null;
  private List<Integer> idProvvedimentoAutorizzatList = new ArrayList<>();
  private String descrBreve = null;
  private Boolean presenzaFascicoloRealeAnnualeNV = null;
  private Boolean insertSottoVociGestCont = null;
  private Boolean presenzaSerieDoc = null;
  private String paroleChiave = null;
  private Boolean presenzaFascStand = null;
  private Integer conservazioneGenerale = null;
  private Boolean presenzaFascRealeLiberoNV = null;
  private String indiceClassificazioneEstesa = null;
  private Boolean presenzaSerieDoss = null;
  private Boolean presenzaFascRealeEreditatoNV = null;
  private Boolean presenzaFascRealeContinuoNV = null;
  private Integer conservazioneCorrente = null;

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
  


  // nome originario nello yaml: changeToken 
  public String getChangeToken() {
    return changeToken;
  }
  public void setChangeToken(String changeToken) {
    this.changeToken = changeToken;
  }

  /**
   **/
  


  // nome originario nello yaml: dataInizio 
  public String getDataInizio() {
    return dataInizio;
  }
  public void setDataInizio(String dataInizio) {
    this.dataInizio = dataInizio;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFine 
  public String getDataFine() {
    return dataFine;
  }
  public void setDataFine(String dataFine) {
    this.dataFine = dataFine;
  }

  /**
   **/
  


  // nome originario nello yaml: codice 
  public String getCodice() {
    return codice;
  }
  public void setCodice(String codice) {
    this.codice = codice;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: stato 
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaFascTemp 
  public Boolean isPresenzaFascTemp() {
    return presenzaFascTemp;
  }
  public void setPresenzaFascTemp(Boolean presenzaFascTemp) {
    this.presenzaFascTemp = presenzaFascTemp;
  }

  /**
   **/
  


  // nome originario nello yaml: dataAggiornamento 
  public String getDataAggiornamento() {
    return dataAggiornamento;
  }
  public void setDataAggiornamento(String dataAggiornamento) {
    this.dataAggiornamento = dataAggiornamento;
  }

  /**
   **/
  


  // nome originario nello yaml: idProvvedimentoAutorizzatList 
  public List<Integer> getIdProvvedimentoAutorizzatList() {
    return idProvvedimentoAutorizzatList;
  }
  public void setIdProvvedimentoAutorizzatList(List<Integer> idProvvedimentoAutorizzatList) {
    this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
  }

  /**
   **/
  


  // nome originario nello yaml: descrBreve 
  public String getDescrBreve() {
    return descrBreve;
  }
  public void setDescrBreve(String descrBreve) {
    this.descrBreve = descrBreve;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaFascicoloRealeAnnualeNV 
  public Boolean isPresenzaFascicoloRealeAnnualeNV() {
    return presenzaFascicoloRealeAnnualeNV;
  }
  public void setPresenzaFascicoloRealeAnnualeNV(Boolean presenzaFascicoloRealeAnnualeNV) {
    this.presenzaFascicoloRealeAnnualeNV = presenzaFascicoloRealeAnnualeNV;
  }

  /**
   **/
  


  // nome originario nello yaml: insertSottoVociGestCont 
  public Boolean isInsertSottoVociGestCont() {
    return insertSottoVociGestCont;
  }
  public void setInsertSottoVociGestCont(Boolean insertSottoVociGestCont) {
    this.insertSottoVociGestCont = insertSottoVociGestCont;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaSerieDoc 
  public Boolean isPresenzaSerieDoc() {
    return presenzaSerieDoc;
  }
  public void setPresenzaSerieDoc(Boolean presenzaSerieDoc) {
    this.presenzaSerieDoc = presenzaSerieDoc;
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
  


  // nome originario nello yaml: presenzaFascStand 
  public Boolean isPresenzaFascStand() {
    return presenzaFascStand;
  }
  public void setPresenzaFascStand(Boolean presenzaFascStand) {
    this.presenzaFascStand = presenzaFascStand;
  }

  /**
   **/
  


  // nome originario nello yaml: conservazioneGenerale 
  public Integer getConservazioneGenerale() {
    return conservazioneGenerale;
  }
  public void setConservazioneGenerale(Integer conservazioneGenerale) {
    this.conservazioneGenerale = conservazioneGenerale;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaFascRealeLiberoNV 
  public Boolean isPresenzaFascRealeLiberoNV() {
    return presenzaFascRealeLiberoNV;
  }
  public void setPresenzaFascRealeLiberoNV(Boolean presenzaFascRealeLiberoNV) {
    this.presenzaFascRealeLiberoNV = presenzaFascRealeLiberoNV;
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
  


  // nome originario nello yaml: presenzaSerieDoss 
  public Boolean isPresenzaSerieDoss() {
    return presenzaSerieDoss;
  }
  public void setPresenzaSerieDoss(Boolean presenzaSerieDoss) {
    this.presenzaSerieDoss = presenzaSerieDoss;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaFascRealeEreditatoNV 
  public Boolean isPresenzaFascRealeEreditatoNV() {
    return presenzaFascRealeEreditatoNV;
  }
  public void setPresenzaFascRealeEreditatoNV(Boolean presenzaFascRealeEreditatoNV) {
    this.presenzaFascRealeEreditatoNV = presenzaFascRealeEreditatoNV;
  }

  /**
   **/
  


  // nome originario nello yaml: presenzaFascRealeContinuoNV 
  public Boolean isPresenzaFascRealeContinuoNV() {
    return presenzaFascRealeContinuoNV;
  }
  public void setPresenzaFascRealeContinuoNV(Boolean presenzaFascRealeContinuoNV) {
    this.presenzaFascRealeContinuoNV = presenzaFascRealeContinuoNV;
  }

  /**
   **/
  


  // nome originario nello yaml: conservazioneCorrente 
  public Integer getConservazioneCorrente() {
    return conservazioneCorrente;
  }
  public void setConservazioneCorrente(Integer conservazioneCorrente) {
    this.conservazioneCorrente = conservazioneCorrente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoceTitolarioActa voceTitolarioActa = (VoceTitolarioActa) o;
    return Objects.equals(id, voceTitolarioActa.id) &&
        Objects.equals(dbKey, voceTitolarioActa.dbKey) &&
        Objects.equals(changeToken, voceTitolarioActa.changeToken) &&
        Objects.equals(dataInizio, voceTitolarioActa.dataInizio) &&
        Objects.equals(dataFine, voceTitolarioActa.dataFine) &&
        Objects.equals(codice, voceTitolarioActa.codice) &&
        Objects.equals(descrizione, voceTitolarioActa.descrizione) &&
        Objects.equals(stato, voceTitolarioActa.stato) &&
        Objects.equals(presenzaFascTemp, voceTitolarioActa.presenzaFascTemp) &&
        Objects.equals(dataAggiornamento, voceTitolarioActa.dataAggiornamento) &&
        Objects.equals(idProvvedimentoAutorizzatList, voceTitolarioActa.idProvvedimentoAutorizzatList) &&
        Objects.equals(descrBreve, voceTitolarioActa.descrBreve) &&
        Objects.equals(presenzaFascicoloRealeAnnualeNV, voceTitolarioActa.presenzaFascicoloRealeAnnualeNV) &&
        Objects.equals(insertSottoVociGestCont, voceTitolarioActa.insertSottoVociGestCont) &&
        Objects.equals(presenzaSerieDoc, voceTitolarioActa.presenzaSerieDoc) &&
        Objects.equals(paroleChiave, voceTitolarioActa.paroleChiave) &&
        Objects.equals(presenzaFascStand, voceTitolarioActa.presenzaFascStand) &&
        Objects.equals(conservazioneGenerale, voceTitolarioActa.conservazioneGenerale) &&
        Objects.equals(presenzaFascRealeLiberoNV, voceTitolarioActa.presenzaFascRealeLiberoNV) &&
        Objects.equals(indiceClassificazioneEstesa, voceTitolarioActa.indiceClassificazioneEstesa) &&
        Objects.equals(presenzaSerieDoss, voceTitolarioActa.presenzaSerieDoss) &&
        Objects.equals(presenzaFascRealeEreditatoNV, voceTitolarioActa.presenzaFascRealeEreditatoNV) &&
        Objects.equals(presenzaFascRealeContinuoNV, voceTitolarioActa.presenzaFascRealeContinuoNV) &&
        Objects.equals(conservazioneCorrente, voceTitolarioActa.conservazioneCorrente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, dataInizio, dataFine, codice, descrizione, stato, presenzaFascTemp, dataAggiornamento, idProvvedimentoAutorizzatList, descrBreve, presenzaFascicoloRealeAnnualeNV, insertSottoVociGestCont, presenzaSerieDoc, paroleChiave, presenzaFascStand, conservazioneGenerale, presenzaFascRealeLiberoNV, indiceClassificazioneEstesa, presenzaSerieDoss, presenzaFascRealeEreditatoNV, presenzaFascRealeContinuoNV, conservazioneCorrente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoceTitolarioActa {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    presenzaFascTemp: ").append(toIndentedString(presenzaFascTemp)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    idProvvedimentoAutorizzatList: ").append(toIndentedString(idProvvedimentoAutorizzatList)).append("\n");
    sb.append("    descrBreve: ").append(toIndentedString(descrBreve)).append("\n");
    sb.append("    presenzaFascicoloRealeAnnualeNV: ").append(toIndentedString(presenzaFascicoloRealeAnnualeNV)).append("\n");
    sb.append("    insertSottoVociGestCont: ").append(toIndentedString(insertSottoVociGestCont)).append("\n");
    sb.append("    presenzaSerieDoc: ").append(toIndentedString(presenzaSerieDoc)).append("\n");
    sb.append("    paroleChiave: ").append(toIndentedString(paroleChiave)).append("\n");
    sb.append("    presenzaFascStand: ").append(toIndentedString(presenzaFascStand)).append("\n");
    sb.append("    conservazioneGenerale: ").append(toIndentedString(conservazioneGenerale)).append("\n");
    sb.append("    presenzaFascRealeLiberoNV: ").append(toIndentedString(presenzaFascRealeLiberoNV)).append("\n");
    sb.append("    indiceClassificazioneEstesa: ").append(toIndentedString(indiceClassificazioneEstesa)).append("\n");
    sb.append("    presenzaSerieDoss: ").append(toIndentedString(presenzaSerieDoss)).append("\n");
    sb.append("    presenzaFascRealeEreditatoNV: ").append(toIndentedString(presenzaFascRealeEreditatoNV)).append("\n");
    sb.append("    presenzaFascRealeContinuoNV: ").append(toIndentedString(presenzaFascRealeContinuoNV)).append("\n");
    sb.append("    conservazioneCorrente: ").append(toIndentedString(conservazioneCorrente)).append("\n");
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

