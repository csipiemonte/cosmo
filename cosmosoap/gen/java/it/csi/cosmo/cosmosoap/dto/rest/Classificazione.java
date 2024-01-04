/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Classificazione  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private String changeToken = null;
  private LocalDate dataInizio = null;
  private LocalDate dataFine = null;
  private String legislatura = null;
  private String numero = null;
  private String stato = null;
  private String codice = null;
  private Boolean cartaceo = null;
  private Boolean docAllegato = null;
  private Boolean docConAllegati = null;
  private Boolean copiaCartacea = null;

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
  public LocalDate getDataInizio() {
    return dataInizio;
  }
  public void setDataInizio(LocalDate dataInizio) {
    this.dataInizio = dataInizio;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFine 
  public LocalDate getDataFine() {
    return dataFine;
  }
  public void setDataFine(LocalDate dataFine) {
    this.dataFine = dataFine;
  }

  /**
   **/
  


  // nome originario nello yaml: legislatura 
  public String getLegislatura() {
    return legislatura;
  }
  public void setLegislatura(String legislatura) {
    this.legislatura = legislatura;
  }

  /**
   **/
  


  // nome originario nello yaml: numero 
  public String getNumero() {
    return numero;
  }
  public void setNumero(String numero) {
    this.numero = numero;
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
  


  // nome originario nello yaml: cartaceo 
  public Boolean isCartaceo() {
    return cartaceo;
  }
  public void setCartaceo(Boolean cartaceo) {
    this.cartaceo = cartaceo;
  }

  /**
   **/
  


  // nome originario nello yaml: docAllegato 
  public Boolean isDocAllegato() {
    return docAllegato;
  }
  public void setDocAllegato(Boolean docAllegato) {
    this.docAllegato = docAllegato;
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
  


  // nome originario nello yaml: copiaCartacea 
  public Boolean isCopiaCartacea() {
    return copiaCartacea;
  }
  public void setCopiaCartacea(Boolean copiaCartacea) {
    this.copiaCartacea = copiaCartacea;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Classificazione classificazione = (Classificazione) o;
    return Objects.equals(id, classificazione.id) &&
        Objects.equals(dbKey, classificazione.dbKey) &&
        Objects.equals(changeToken, classificazione.changeToken) &&
        Objects.equals(dataInizio, classificazione.dataInizio) &&
        Objects.equals(dataFine, classificazione.dataFine) &&
        Objects.equals(legislatura, classificazione.legislatura) &&
        Objects.equals(numero, classificazione.numero) &&
        Objects.equals(stato, classificazione.stato) &&
        Objects.equals(codice, classificazione.codice) &&
        Objects.equals(cartaceo, classificazione.cartaceo) &&
        Objects.equals(docAllegato, classificazione.docAllegato) &&
        Objects.equals(docConAllegati, classificazione.docConAllegati) &&
        Objects.equals(copiaCartacea, classificazione.copiaCartacea);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, dataInizio, dataFine, legislatura, numero, stato, codice, cartaceo, docAllegato, docConAllegati, copiaCartacea);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Classificazione {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    legislatura: ").append(toIndentedString(legislatura)).append("\n");
    sb.append("    numero: ").append(toIndentedString(numero)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    cartaceo: ").append(toIndentedString(cartaceo)).append("\n");
    sb.append("    docAllegato: ").append(toIndentedString(docAllegato)).append("\n");
    sb.append("    docConAllegati: ").append(toIndentedString(docConAllegati)).append("\n");
    sb.append("    copiaCartacea: ").append(toIndentedString(copiaCartacea)).append("\n");
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

