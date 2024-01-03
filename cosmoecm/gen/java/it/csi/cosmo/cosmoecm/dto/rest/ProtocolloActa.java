/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class ProtocolloActa  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String objectTypeId = null;
  private String numRegistrazioneProtocollo = null;
  private String oggetto = null;
  private String aooCheRegistra = null;
  private String enteCheRegistra = null;
  private String uuidRegistrazioneProtocollo = null;
  private Boolean entrata = null;
  private Boolean riservato = null;
  private Boolean annullato = null;
  private OffsetDateTime dataRegistrazioneProtocollo = null;

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
  


  // nome originario nello yaml: oggetto 
  public String getOggetto() {
    return oggetto;
  }
  public void setOggetto(String oggetto) {
    this.oggetto = oggetto;
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
  


  // nome originario nello yaml: uuidRegistrazioneProtocollo 
  public String getUuidRegistrazioneProtocollo() {
    return uuidRegistrazioneProtocollo;
  }
  public void setUuidRegistrazioneProtocollo(String uuidRegistrazioneProtocollo) {
    this.uuidRegistrazioneProtocollo = uuidRegistrazioneProtocollo;
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
  


  // nome originario nello yaml: dataRegistrazioneProtocollo 
  public OffsetDateTime getDataRegistrazioneProtocollo() {
    return dataRegistrazioneProtocollo;
  }
  public void setDataRegistrazioneProtocollo(OffsetDateTime dataRegistrazioneProtocollo) {
    this.dataRegistrazioneProtocollo = dataRegistrazioneProtocollo;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProtocolloActa protocolloActa = (ProtocolloActa) o;
    return Objects.equals(id, protocolloActa.id) &&
        Objects.equals(objectTypeId, protocolloActa.objectTypeId) &&
        Objects.equals(numRegistrazioneProtocollo, protocolloActa.numRegistrazioneProtocollo) &&
        Objects.equals(oggetto, protocolloActa.oggetto) &&
        Objects.equals(aooCheRegistra, protocolloActa.aooCheRegistra) &&
        Objects.equals(enteCheRegistra, protocolloActa.enteCheRegistra) &&
        Objects.equals(uuidRegistrazioneProtocollo, protocolloActa.uuidRegistrazioneProtocollo) &&
        Objects.equals(entrata, protocolloActa.entrata) &&
        Objects.equals(riservato, protocolloActa.riservato) &&
        Objects.equals(annullato, protocolloActa.annullato) &&
        Objects.equals(dataRegistrazioneProtocollo, protocolloActa.dataRegistrazioneProtocollo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, objectTypeId, numRegistrazioneProtocollo, oggetto, aooCheRegistra, enteCheRegistra, uuidRegistrazioneProtocollo, entrata, riservato, annullato, dataRegistrazioneProtocollo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProtocolloActa {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    objectTypeId: ").append(toIndentedString(objectTypeId)).append("\n");
    sb.append("    numRegistrazioneProtocollo: ").append(toIndentedString(numRegistrazioneProtocollo)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    aooCheRegistra: ").append(toIndentedString(aooCheRegistra)).append("\n");
    sb.append("    enteCheRegistra: ").append(toIndentedString(enteCheRegistra)).append("\n");
    sb.append("    uuidRegistrazioneProtocollo: ").append(toIndentedString(uuidRegistrazioneProtocollo)).append("\n");
    sb.append("    entrata: ").append(toIndentedString(entrata)).append("\n");
    sb.append("    riservato: ").append(toIndentedString(riservato)).append("\n");
    sb.append("    annullato: ").append(toIndentedString(annullato)).append("\n");
    sb.append("    dataRegistrazioneProtocollo: ").append(toIndentedString(dataRegistrazioneProtocollo)).append("\n");
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

