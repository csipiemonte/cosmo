/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class VoceTitolario  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private String changeToken = null;
  private String dataInizio = null;
  private String dataFIne = null;
  private String codice = null;
  private String descrizione = null;
  private String stato = null;
  private Boolean presenzaFascTemp = null;
  private String dataAggiornamento = null;
  private List<Long> idProvvedimentoAutorizzatList = new ArrayList<>();
  private String descrBreve = null;
  private Boolean presenzaFascRealeAnnualeNV = null;
  private Boolean insertSottoVociGestCont = null;
  private Boolean presenzaSerieDoc = null;
  private String paroleChiave = null;
  private Boolean presenzaFascStand = null;
  private Boolean presenzaSerieFasc = null;
  private Integer conservazioneGenerale = null;
  private Boolean presenzaFascRealeLiberoNV = null;
  private Boolean presenzaFascRealeLegislaturaNV = null;
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
  


  // nome originario nello yaml: dataFIne 
  public String getDataFIne() {
    return dataFIne;
  }
  public void setDataFIne(String dataFIne) {
    this.dataFIne = dataFIne;
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
  public List<Long> getIdProvvedimentoAutorizzatList() {
    return idProvvedimentoAutorizzatList;
  }
  public void setIdProvvedimentoAutorizzatList(List<Long> idProvvedimentoAutorizzatList) {
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
  


  // nome originario nello yaml: presenzaFascRealeAnnualeNV 
  public Boolean isPresenzaFascRealeAnnualeNV() {
    return presenzaFascRealeAnnualeNV;
  }
  public void setPresenzaFascRealeAnnualeNV(Boolean presenzaFascRealeAnnualeNV) {
    this.presenzaFascRealeAnnualeNV = presenzaFascRealeAnnualeNV;
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
  


  // nome originario nello yaml: presenzaSerieFasc 
  public Boolean isPresenzaSerieFasc() {
    return presenzaSerieFasc;
  }
  public void setPresenzaSerieFasc(Boolean presenzaSerieFasc) {
    this.presenzaSerieFasc = presenzaSerieFasc;
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
  


  // nome originario nello yaml: presenzaFascRealeLegislaturaNV 
  public Boolean isPresenzaFascRealeLegislaturaNV() {
    return presenzaFascRealeLegislaturaNV;
  }
  public void setPresenzaFascRealeLegislaturaNV(Boolean presenzaFascRealeLegislaturaNV) {
    this.presenzaFascRealeLegislaturaNV = presenzaFascRealeLegislaturaNV;
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
    VoceTitolario voceTitolario = (VoceTitolario) o;
    return Objects.equals(id, voceTitolario.id) &&
        Objects.equals(dbKey, voceTitolario.dbKey) &&
        Objects.equals(changeToken, voceTitolario.changeToken) &&
        Objects.equals(dataInizio, voceTitolario.dataInizio) &&
        Objects.equals(dataFIne, voceTitolario.dataFIne) &&
        Objects.equals(codice, voceTitolario.codice) &&
        Objects.equals(descrizione, voceTitolario.descrizione) &&
        Objects.equals(stato, voceTitolario.stato) &&
        Objects.equals(presenzaFascTemp, voceTitolario.presenzaFascTemp) &&
        Objects.equals(dataAggiornamento, voceTitolario.dataAggiornamento) &&
        Objects.equals(idProvvedimentoAutorizzatList, voceTitolario.idProvvedimentoAutorizzatList) &&
        Objects.equals(descrBreve, voceTitolario.descrBreve) &&
        Objects.equals(presenzaFascRealeAnnualeNV, voceTitolario.presenzaFascRealeAnnualeNV) &&
        Objects.equals(insertSottoVociGestCont, voceTitolario.insertSottoVociGestCont) &&
        Objects.equals(presenzaSerieDoc, voceTitolario.presenzaSerieDoc) &&
        Objects.equals(paroleChiave, voceTitolario.paroleChiave) &&
        Objects.equals(presenzaFascStand, voceTitolario.presenzaFascStand) &&
        Objects.equals(presenzaSerieFasc, voceTitolario.presenzaSerieFasc) &&
        Objects.equals(conservazioneGenerale, voceTitolario.conservazioneGenerale) &&
        Objects.equals(presenzaFascRealeLiberoNV, voceTitolario.presenzaFascRealeLiberoNV) &&
        Objects.equals(presenzaFascRealeLegislaturaNV, voceTitolario.presenzaFascRealeLegislaturaNV) &&
        Objects.equals(indiceClassificazioneEstesa, voceTitolario.indiceClassificazioneEstesa) &&
        Objects.equals(presenzaSerieDoss, voceTitolario.presenzaSerieDoss) &&
        Objects.equals(presenzaFascRealeEreditatoNV, voceTitolario.presenzaFascRealeEreditatoNV) &&
        Objects.equals(presenzaFascRealeContinuoNV, voceTitolario.presenzaFascRealeContinuoNV) &&
        Objects.equals(conservazioneCorrente, voceTitolario.conservazioneCorrente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, dataInizio, dataFIne, codice, descrizione, stato, presenzaFascTemp, dataAggiornamento, idProvvedimentoAutorizzatList, descrBreve, presenzaFascRealeAnnualeNV, insertSottoVociGestCont, presenzaSerieDoc, paroleChiave, presenzaFascStand, presenzaSerieFasc, conservazioneGenerale, presenzaFascRealeLiberoNV, presenzaFascRealeLegislaturaNV, indiceClassificazioneEstesa, presenzaSerieDoss, presenzaFascRealeEreditatoNV, presenzaFascRealeContinuoNV, conservazioneCorrente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoceTitolario {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFIne: ").append(toIndentedString(dataFIne)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    presenzaFascTemp: ").append(toIndentedString(presenzaFascTemp)).append("\n");
    sb.append("    dataAggiornamento: ").append(toIndentedString(dataAggiornamento)).append("\n");
    sb.append("    idProvvedimentoAutorizzatList: ").append(toIndentedString(idProvvedimentoAutorizzatList)).append("\n");
    sb.append("    descrBreve: ").append(toIndentedString(descrBreve)).append("\n");
    sb.append("    presenzaFascRealeAnnualeNV: ").append(toIndentedString(presenzaFascRealeAnnualeNV)).append("\n");
    sb.append("    insertSottoVociGestCont: ").append(toIndentedString(insertSottoVociGestCont)).append("\n");
    sb.append("    presenzaSerieDoc: ").append(toIndentedString(presenzaSerieDoc)).append("\n");
    sb.append("    paroleChiave: ").append(toIndentedString(paroleChiave)).append("\n");
    sb.append("    presenzaFascStand: ").append(toIndentedString(presenzaFascStand)).append("\n");
    sb.append("    presenzaSerieFasc: ").append(toIndentedString(presenzaSerieFasc)).append("\n");
    sb.append("    conservazioneGenerale: ").append(toIndentedString(conservazioneGenerale)).append("\n");
    sb.append("    presenzaFascRealeLiberoNV: ").append(toIndentedString(presenzaFascRealeLiberoNV)).append("\n");
    sb.append("    presenzaFascRealeLegislaturaNV: ").append(toIndentedString(presenzaFascRealeLegislaturaNV)).append("\n");
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

