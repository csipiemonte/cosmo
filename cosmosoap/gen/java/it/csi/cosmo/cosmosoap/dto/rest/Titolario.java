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

public class Titolario  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private String changeToken = null;
  private String dataInizio = null;
  private String dataFine = null;
  private String stato = null;
  private String codice = null;
  private List<Long> idProvvedimentoAutorizzatList = new ArrayList<>();
  private Integer numeroMaxLivelli = null;
  private Boolean insertContenutiInVociConSottovoci = null;
  private Boolean insertDocInFascConSottofasc = null;
  private String descrizione = null;
  private Boolean insertFuoriVolumi = null;
  private Boolean serieMultipleVoce = null;
  private Boolean creaAutomaPrimaSerie = null;
  private Boolean scartoSoloConVolumi = null;

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
  


  // nome originario nello yaml: stato 
  public String getStato() {
    return stato;
  }
  public void setStato(String stato) {
    this.stato = stato;
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
  


  // nome originario nello yaml: idProvvedimentoAutorizzatList 
  public List<Long> getIdProvvedimentoAutorizzatList() {
    return idProvvedimentoAutorizzatList;
  }
  public void setIdProvvedimentoAutorizzatList(List<Long> idProvvedimentoAutorizzatList) {
    this.idProvvedimentoAutorizzatList = idProvvedimentoAutorizzatList;
  }

  /**
   **/
  


  // nome originario nello yaml: numeroMaxLivelli 
  public Integer getNumeroMaxLivelli() {
    return numeroMaxLivelli;
  }
  public void setNumeroMaxLivelli(Integer numeroMaxLivelli) {
    this.numeroMaxLivelli = numeroMaxLivelli;
  }

  /**
   **/
  


  // nome originario nello yaml: insertContenutiInVociConSottovoci 
  public Boolean isInsertContenutiInVociConSottovoci() {
    return insertContenutiInVociConSottovoci;
  }
  public void setInsertContenutiInVociConSottovoci(Boolean insertContenutiInVociConSottovoci) {
    this.insertContenutiInVociConSottovoci = insertContenutiInVociConSottovoci;
  }

  /**
   **/
  


  // nome originario nello yaml: insertDocInFascConSottofasc 
  public Boolean isInsertDocInFascConSottofasc() {
    return insertDocInFascConSottofasc;
  }
  public void setInsertDocInFascConSottofasc(Boolean insertDocInFascConSottofasc) {
    this.insertDocInFascConSottofasc = insertDocInFascConSottofasc;
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
  


  // nome originario nello yaml: insertFuoriVolumi 
  public Boolean isInsertFuoriVolumi() {
    return insertFuoriVolumi;
  }
  public void setInsertFuoriVolumi(Boolean insertFuoriVolumi) {
    this.insertFuoriVolumi = insertFuoriVolumi;
  }

  /**
   **/
  


  // nome originario nello yaml: serieMultipleVoce 
  public Boolean isSerieMultipleVoce() {
    return serieMultipleVoce;
  }
  public void setSerieMultipleVoce(Boolean serieMultipleVoce) {
    this.serieMultipleVoce = serieMultipleVoce;
  }

  /**
   **/
  


  // nome originario nello yaml: creaAutomaPrimaSerie 
  public Boolean isCreaAutomaPrimaSerie() {
    return creaAutomaPrimaSerie;
  }
  public void setCreaAutomaPrimaSerie(Boolean creaAutomaPrimaSerie) {
    this.creaAutomaPrimaSerie = creaAutomaPrimaSerie;
  }

  /**
   **/
  


  // nome originario nello yaml: scartoSoloConVolumi 
  public Boolean isScartoSoloConVolumi() {
    return scartoSoloConVolumi;
  }
  public void setScartoSoloConVolumi(Boolean scartoSoloConVolumi) {
    this.scartoSoloConVolumi = scartoSoloConVolumi;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Titolario titolario = (Titolario) o;
    return Objects.equals(id, titolario.id) &&
        Objects.equals(dbKey, titolario.dbKey) &&
        Objects.equals(changeToken, titolario.changeToken) &&
        Objects.equals(dataInizio, titolario.dataInizio) &&
        Objects.equals(dataFine, titolario.dataFine) &&
        Objects.equals(stato, titolario.stato) &&
        Objects.equals(codice, titolario.codice) &&
        Objects.equals(idProvvedimentoAutorizzatList, titolario.idProvvedimentoAutorizzatList) &&
        Objects.equals(numeroMaxLivelli, titolario.numeroMaxLivelli) &&
        Objects.equals(insertContenutiInVociConSottovoci, titolario.insertContenutiInVociConSottovoci) &&
        Objects.equals(insertDocInFascConSottofasc, titolario.insertDocInFascConSottofasc) &&
        Objects.equals(descrizione, titolario.descrizione) &&
        Objects.equals(insertFuoriVolumi, titolario.insertFuoriVolumi) &&
        Objects.equals(serieMultipleVoce, titolario.serieMultipleVoce) &&
        Objects.equals(creaAutomaPrimaSerie, titolario.creaAutomaPrimaSerie) &&
        Objects.equals(scartoSoloConVolumi, titolario.scartoSoloConVolumi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, dataInizio, dataFine, stato, codice, idProvvedimentoAutorizzatList, numeroMaxLivelli, insertContenutiInVociConSottovoci, insertDocInFascConSottofasc, descrizione, insertFuoriVolumi, serieMultipleVoce, creaAutomaPrimaSerie, scartoSoloConVolumi);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Titolario {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    idProvvedimentoAutorizzatList: ").append(toIndentedString(idProvvedimentoAutorizzatList)).append("\n");
    sb.append("    numeroMaxLivelli: ").append(toIndentedString(numeroMaxLivelli)).append("\n");
    sb.append("    insertContenutiInVociConSottovoci: ").append(toIndentedString(insertContenutiInVociConSottovoci)).append("\n");
    sb.append("    insertDocInFascConSottofasc: ").append(toIndentedString(insertDocInFascConSottofasc)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    insertFuoriVolumi: ").append(toIndentedString(insertFuoriVolumi)).append("\n");
    sb.append("    serieMultipleVoce: ").append(toIndentedString(serieMultipleVoce)).append("\n");
    sb.append("    creaAutomaPrimaSerie: ").append(toIndentedString(creaAutomaPrimaSerie)).append("\n");
    sb.append("    scartoSoloConVolumi: ").append(toIndentedString(scartoSoloConVolumi)).append("\n");
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

