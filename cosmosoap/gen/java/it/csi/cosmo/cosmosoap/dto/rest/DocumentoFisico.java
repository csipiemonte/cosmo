/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DocumentoFisico  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private OffsetDateTime changeToken = null;
  private String descrizione = null;
  private Integer progressivo = null;
  private OffsetDateTime dataMemorizzazione = null;

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
  


  // nome originario nello yaml: descrizione 
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: progressivo 
  public Integer getProgressivo() {
    return progressivo;
  }
  public void setProgressivo(Integer progressivo) {
    this.progressivo = progressivo;
  }

  /**
   **/
  


  // nome originario nello yaml: dataMemorizzazione 
  public OffsetDateTime getDataMemorizzazione() {
    return dataMemorizzazione;
  }
  public void setDataMemorizzazione(OffsetDateTime dataMemorizzazione) {
    this.dataMemorizzazione = dataMemorizzazione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentoFisico documentoFisico = (DocumentoFisico) o;
    return Objects.equals(id, documentoFisico.id) &&
        Objects.equals(dbKey, documentoFisico.dbKey) &&
        Objects.equals(changeToken, documentoFisico.changeToken) &&
        Objects.equals(descrizione, documentoFisico.descrizione) &&
        Objects.equals(progressivo, documentoFisico.progressivo) &&
        Objects.equals(dataMemorizzazione, documentoFisico.dataMemorizzazione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, descrizione, progressivo, dataMemorizzazione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DocumentoFisico {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    progressivo: ").append(toIndentedString(progressivo)).append("\n");
    sb.append("    dataMemorizzazione: ").append(toIndentedString(dataMemorizzazione)).append("\n");
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

