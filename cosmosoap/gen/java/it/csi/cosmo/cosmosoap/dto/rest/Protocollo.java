/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class Protocollo  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private OffsetDateTime changeToken = null;
  private String objectTypeId = null;
  private String numRegistrazioneProtocollo = null;
  private OffsetDateTime dataRegistrazioneProtocollo = null;
  private String oggetto = null;
  private Boolean entrata = null;
  private String aooCheRegistra = null;
  private String enteCheRegistra = null;
  private List<String> mittente = new ArrayList<>();
  private List<String> destinatario = new ArrayList<>();
  private Boolean riservato = null;
  private Boolean annullato = null;
  private String uuidRegistrazioneProtocollo = null;

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
  public OffsetDateTime getChangeToken() {
    return changeToken;
  }
  public void setChangeToken(OffsetDateTime changeToken) {
    this.changeToken = changeToken;
  }

  /**
   **/
  


  // nome originario nello yaml: objectTypeId 
  public String getObjectTypeId() {
    return objectTypeId;
  }
  public void setObjectTypeId(String objectTypeId) {
    this.objectTypeId = objectTypeId;
  }

  /**
   **/
  


  // nome originario nello yaml: numRegistrazioneProtocollo 
  public String getNumRegistrazioneProtocollo() {
    return numRegistrazioneProtocollo;
  }
  public void setNumRegistrazioneProtocollo(String numRegistrazioneProtocollo) {
    this.numRegistrazioneProtocollo = numRegistrazioneProtocollo;
  }

  /**
   **/
  


  // nome originario nello yaml: dataRegistrazioneProtocollo 
  public OffsetDateTime getDataRegistrazioneProtocollo() {
    return dataRegistrazioneProtocollo;
  }
  public void setDataRegistrazioneProtocollo(OffsetDateTime dataRegistrazioneProtocollo) {
    this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
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
  


  // nome originario nello yaml: entrata 
  public Boolean isEntrata() {
    return entrata;
  }
  public void setEntrata(Boolean entrata) {
    this.entrata = entrata;
  }

  /**
   **/
  


  // nome originario nello yaml: aooCheRegistra 
  public String getAooCheRegistra() {
    return aooCheRegistra;
  }
  public void setAooCheRegistra(String aooCheRegistra) {
    this.aooCheRegistra = aooCheRegistra;
  }

  /**
   **/
  


  // nome originario nello yaml: enteCheRegistra 
  public String getEnteCheRegistra() {
    return enteCheRegistra;
  }
  public void setEnteCheRegistra(String enteCheRegistra) {
    this.enteCheRegistra = enteCheRegistra;
  }

  /**
   **/
  


  // nome originario nello yaml: mittente 
  public List<String> getMittente() {
    return mittente;
  }
  public void setMittente(List<String> mittente) {
    this.mittente = mittente;
  }

  /**
   **/
  


  // nome originario nello yaml: destinatario 
  public List<String> getDestinatario() {
    return destinatario;
  }
  public void setDestinatario(List<String> destinatario) {
    this.destinatario = destinatario;
  }

  /**
   **/
  


  // nome originario nello yaml: riservato 
  public Boolean isRiservato() {
    return riservato;
  }
  public void setRiservato(Boolean riservato) {
    this.riservato = riservato;
  }

  /**
   **/
  


  // nome originario nello yaml: annullato 
  public Boolean isAnnullato() {
    return annullato;
  }
  public void setAnnullato(Boolean annullato) {
    this.annullato = annullato;
  }

  /**
   **/
  


  // nome originario nello yaml: uuidRegistrazioneProtocollo 
  public String getUuidRegistrazioneProtocollo() {
    return uuidRegistrazioneProtocollo;
  }
  public void setUuidRegistrazioneProtocollo(String uuidRegistrazioneProtocollo) {
    this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Protocollo protocollo = (Protocollo) o;
    return Objects.equals(id, protocollo.id) &&
        Objects.equals(dbKey, protocollo.dbKey) &&
        Objects.equals(changeToken, protocollo.changeToken) &&
        Objects.equals(objectTypeId, protocollo.objectTypeId) &&
        Objects.equals(numRegistrazioneProtocollo, protocollo.numRegistrazioneProtocollo) &&
        Objects.equals(dataRegistrazioneProtocollo, protocollo.dataRegistrazioneProtocollo) &&
        Objects.equals(oggetto, protocollo.oggetto) &&
        Objects.equals(entrata, protocollo.entrata) &&
        Objects.equals(aooCheRegistra, protocollo.aooCheRegistra) &&
        Objects.equals(enteCheRegistra, protocollo.enteCheRegistra) &&
        Objects.equals(mittente, protocollo.mittente) &&
        Objects.equals(destinatario, protocollo.destinatario) &&
        Objects.equals(riservato, protocollo.riservato) &&
        Objects.equals(annullato, protocollo.annullato) &&
        Objects.equals(uuidRegistrazioneProtocollo, protocollo.uuidRegistrazioneProtocollo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, objectTypeId, numRegistrazioneProtocollo, dataRegistrazioneProtocollo, oggetto, entrata, aooCheRegistra, enteCheRegistra, mittente, destinatario, riservato, annullato, uuidRegistrazioneProtocollo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Protocollo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    objectTypeId: ").append(toIndentedString(objectTypeId)).append("\n");
    sb.append("    numRegistrazioneProtocollo: ").append(toIndentedString(numRegistrazioneProtocollo)).append("\n");
    sb.append("    dataRegistrazioneProtocollo: ").append(toIndentedString(dataRegistrazioneProtocollo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    entrata: ").append(toIndentedString(entrata)).append("\n");
    sb.append("    aooCheRegistra: ").append(toIndentedString(aooCheRegistra)).append("\n");
    sb.append("    enteCheRegistra: ").append(toIndentedString(enteCheRegistra)).append("\n");
    sb.append("    mittente: ").append(toIndentedString(mittente)).append("\n");
    sb.append("    destinatario: ").append(toIndentedString(destinatario)).append("\n");
    sb.append("    riservato: ").append(toIndentedString(riservato)).append("\n");
    sb.append("    annullato: ").append(toIndentedString(annullato)).append("\n");
    sb.append("    uuidRegistrazioneProtocollo: ").append(toIndentedString(uuidRegistrazioneProtocollo)).append("\n");
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

