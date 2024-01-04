/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.AllowedChildObjectTypeIds;
import it.csi.cosmo.cosmosoap.dto.rest.ChangeToken;
import it.csi.cosmo.cosmosoap.dto.rest.ObjectId;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class GruppoAllegatiProperties  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private ObjectId objectId = null;
  private ChangeToken changeToken = null;
  private String objectTypeId = null;
  private ObjectId parentId = null;
  private AllowedChildObjectTypeIds allowedChildObjectTypeIds = null;
  private Integer numeroAllegati = 0;
  private OffsetDateTime dataInizio = null;
  private OffsetDateTime dataFine = null;
  private ObjectId classificazionePrincipale = null;

  /**
   **/
  


  // nome originario nello yaml: objectId 
  @NotNull
  public ObjectId getObjectId() {
    return objectId;
  }
  public void setObjectId(ObjectId objectId) {
    this.objectId = objectId;
  }

  /**
   **/
  


  // nome originario nello yaml: changeToken 
  @NotNull
  public ChangeToken getChangeToken() {
    return changeToken;
  }
  public void setChangeToken(ChangeToken changeToken) {
    this.changeToken = changeToken;
  }

  /**
   **/
  


  // nome originario nello yaml: objectTypeId 
  @NotNull
  public String getObjectTypeId() {
    return objectTypeId;
  }
  public void setObjectTypeId(String objectTypeId) {
    this.objectTypeId = objectTypeId;
  }

  /**
   **/
  


  // nome originario nello yaml: parentId 
  @NotNull
  public ObjectId getParentId() {
    return parentId;
  }
  public void setParentId(ObjectId parentId) {
    this.parentId = parentId;
  }

  /**
   **/
  


  // nome originario nello yaml: allowedChildObjectTypeIds 
  @NotNull
  public AllowedChildObjectTypeIds getAllowedChildObjectTypeIds() {
    return allowedChildObjectTypeIds;
  }
  public void setAllowedChildObjectTypeIds(AllowedChildObjectTypeIds allowedChildObjectTypeIds) {
    this.allowedChildObjectTypeIds = allowedChildObjectTypeIds;
  }

  /**
   **/
  


  // nome originario nello yaml: numeroAllegati 
  @NotNull
  public Integer getNumeroAllegati() {
    return numeroAllegati;
  }
  public void setNumeroAllegati(Integer numeroAllegati) {
    this.numeroAllegati = numeroAllegati;
  }

  /**
   **/
  


  // nome originario nello yaml: dataInizio 
  @NotNull
  public OffsetDateTime getDataInizio() {
    return dataInizio;
  }
  public void setDataInizio(OffsetDateTime dataInizio) {
    this.dataInizio = dataInizio;
  }

  /**
   **/
  


  // nome originario nello yaml: dataFine 
  @NotNull
  public OffsetDateTime getDataFine() {
    return dataFine;
  }
  public void setDataFine(OffsetDateTime dataFine) {
    this.dataFine = dataFine;
  }

  /**
   **/
  


  // nome originario nello yaml: classificazionePrincipale 
  @NotNull
  public ObjectId getClassificazionePrincipale() {
    return classificazionePrincipale;
  }
  public void setClassificazionePrincipale(ObjectId classificazionePrincipale) {
    this.classificazionePrincipale = classificazionePrincipale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GruppoAllegatiProperties gruppoAllegatiProperties = (GruppoAllegatiProperties) o;
    return Objects.equals(objectId, gruppoAllegatiProperties.objectId) &&
        Objects.equals(changeToken, gruppoAllegatiProperties.changeToken) &&
        Objects.equals(objectTypeId, gruppoAllegatiProperties.objectTypeId) &&
        Objects.equals(parentId, gruppoAllegatiProperties.parentId) &&
        Objects.equals(allowedChildObjectTypeIds, gruppoAllegatiProperties.allowedChildObjectTypeIds) &&
        Objects.equals(numeroAllegati, gruppoAllegatiProperties.numeroAllegati) &&
        Objects.equals(dataInizio, gruppoAllegatiProperties.dataInizio) &&
        Objects.equals(dataFine, gruppoAllegatiProperties.dataFine) &&
        Objects.equals(classificazionePrincipale, gruppoAllegatiProperties.classificazionePrincipale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(objectId, changeToken, objectTypeId, parentId, allowedChildObjectTypeIds, numeroAllegati, dataInizio, dataFine, classificazionePrincipale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GruppoAllegatiProperties {\n");
    
    sb.append("    objectId: ").append(toIndentedString(objectId)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    objectTypeId: ").append(toIndentedString(objectTypeId)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    allowedChildObjectTypeIds: ").append(toIndentedString(allowedChildObjectTypeIds)).append("\n");
    sb.append("    numeroAllegati: ").append(toIndentedString(numeroAllegati)).append("\n");
    sb.append("    dataInizio: ").append(toIndentedString(dataInizio)).append("\n");
    sb.append("    dataFine: ").append(toIndentedString(dataFine)).append("\n");
    sb.append("    classificazionePrincipale: ").append(toIndentedString(classificazionePrincipale)).append("\n");
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

