/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class RegistrazioneClassificazioni  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String id = null;
  private String dbKey = null;
  private OffsetDateTime changeToken = null;
  private LocalDate dataProtocollo = null;
  private String stato = null;
  private String objectIdRegistrazione = null;
  private String annoProtocolloMittente = null;
  private String idAooResponsabile = null;
  private String objectIdAooResponsabile = null;
  private String dataProtocolloMittente = null;
  private String idAooProtocollante = null;
  private String objectIdAooProtocollante = null;
  private String codiceProtocolloMittente = null;
  private String oggetto = null;
  private String idRegistroProtocollo = null;
  private String codice = null;
  private String codiceAooProtocollante = null;
  private String codiceAooResponsabile = null;
  private String dbKeyTipoRegistrazione = null;
  private String objectIdClassificazione = null;
  private Long anno = null;
  private Boolean flagRiservato = null;

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
  


  // nome originario nello yaml: dataProtocollo 
  public LocalDate getDataProtocollo() {
    return dataProtocollo;
  }
  public void setDataProtocollo(LocalDate dataProtocollo) {
    this.dataProtocollo = dataProtocollo;
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
  


  // nome originario nello yaml: objectIdRegistrazione 
  public String getObjectIdRegistrazione() {
    return objectIdRegistrazione;
  }
  public void setObjectIdRegistrazione(String objectIdRegistrazione) {
    this.objectIdRegistrazione = objectIdRegistrazione;
  }

  /**
   **/
  


  // nome originario nello yaml: annoProtocolloMittente 
  public String getAnnoProtocolloMittente() {
    return annoProtocolloMittente;
  }
  public void setAnnoProtocolloMittente(String annoProtocolloMittente) {
    this.annoProtocolloMittente = annoProtocolloMittente;
  }

  /**
   **/
  


  // nome originario nello yaml: idAooResponsabile 
  public String getIdAooResponsabile() {
    return idAooResponsabile;
  }
  public void setIdAooResponsabile(String idAooResponsabile) {
    this.idAooResponsabile = idAooResponsabile;
  }

  /**
   **/
  


  // nome originario nello yaml: objectIdAooResponsabile 
  public String getObjectIdAooResponsabile() {
    return objectIdAooResponsabile;
  }
  public void setObjectIdAooResponsabile(String objectIdAooResponsabile) {
    this.objectIdAooResponsabile = objectIdAooResponsabile;
  }

  /**
   **/
  


  // nome originario nello yaml: dataProtocolloMittente 
  public String getDataProtocolloMittente() {
    return dataProtocolloMittente;
  }
  public void setDataProtocolloMittente(String dataProtocolloMittente) {
    this.dataProtocolloMittente = dataProtocolloMittente;
  }

  /**
   **/
  


  // nome originario nello yaml: idAooProtocollante 
  public String getIdAooProtocollante() {
    return idAooProtocollante;
  }
  public void setIdAooProtocollante(String idAooProtocollante) {
    this.idAooProtocollante = idAooProtocollante;
  }

  /**
   **/
  


  // nome originario nello yaml: objectIdAooProtocollante 
  public String getObjectIdAooProtocollante() {
    return objectIdAooProtocollante;
  }
  public void setObjectIdAooProtocollante(String objectIdAooProtocollante) {
    this.objectIdAooProtocollante = objectIdAooProtocollante;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceProtocolloMittente 
  public String getCodiceProtocolloMittente() {
    return codiceProtocolloMittente;
  }
  public void setCodiceProtocolloMittente(String codiceProtocolloMittente) {
    this.codiceProtocolloMittente = codiceProtocolloMittente;
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
  


  // nome originario nello yaml: idRegistroProtocollo 
  public String getIdRegistroProtocollo() {
    return idRegistroProtocollo;
  }
  public void setIdRegistroProtocollo(String idRegistroProtocollo) {
    this.idRegistroProtocollo = idRegistroProtocollo;
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
  


  // nome originario nello yaml: codiceAooProtocollante 
  public String getCodiceAooProtocollante() {
    return codiceAooProtocollante;
  }
  public void setCodiceAooProtocollante(String codiceAooProtocollante) {
    this.codiceAooProtocollante = codiceAooProtocollante;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceAooResponsabile 
  public String getCodiceAooResponsabile() {
    return codiceAooResponsabile;
  }
  public void setCodiceAooResponsabile(String codiceAooResponsabile) {
    this.codiceAooResponsabile = codiceAooResponsabile;
  }

  /**
   **/
  


  // nome originario nello yaml: dbKeyTipoRegistrazione 
  public String getDbKeyTipoRegistrazione() {
    return dbKeyTipoRegistrazione;
  }
  public void setDbKeyTipoRegistrazione(String dbKeyTipoRegistrazione) {
    this.dbKeyTipoRegistrazione = dbKeyTipoRegistrazione;
  }

  /**
   **/
  


  // nome originario nello yaml: objectIdClassificazione 
  public String getObjectIdClassificazione() {
    return objectIdClassificazione;
  }
  public void setObjectIdClassificazione(String objectIdClassificazione) {
    this.objectIdClassificazione = objectIdClassificazione;
  }

  /**
   **/
  


  // nome originario nello yaml: anno 
  public Long getAnno() {
    return anno;
  }
  public void setAnno(Long anno) {
    this.anno = anno;
  }

  /**
   **/
  


  // nome originario nello yaml: flagRiservato 
  public Boolean isFlagRiservato() {
    return flagRiservato;
  }
  public void setFlagRiservato(Boolean flagRiservato) {
    this.flagRiservato = flagRiservato;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrazioneClassificazioni registrazioneClassificazioni = (RegistrazioneClassificazioni) o;
    return Objects.equals(id, registrazioneClassificazioni.id) &&
        Objects.equals(dbKey, registrazioneClassificazioni.dbKey) &&
        Objects.equals(changeToken, registrazioneClassificazioni.changeToken) &&
        Objects.equals(dataProtocollo, registrazioneClassificazioni.dataProtocollo) &&
        Objects.equals(stato, registrazioneClassificazioni.stato) &&
        Objects.equals(objectIdRegistrazione, registrazioneClassificazioni.objectIdRegistrazione) &&
        Objects.equals(annoProtocolloMittente, registrazioneClassificazioni.annoProtocolloMittente) &&
        Objects.equals(idAooResponsabile, registrazioneClassificazioni.idAooResponsabile) &&
        Objects.equals(objectIdAooResponsabile, registrazioneClassificazioni.objectIdAooResponsabile) &&
        Objects.equals(dataProtocolloMittente, registrazioneClassificazioni.dataProtocolloMittente) &&
        Objects.equals(idAooProtocollante, registrazioneClassificazioni.idAooProtocollante) &&
        Objects.equals(objectIdAooProtocollante, registrazioneClassificazioni.objectIdAooProtocollante) &&
        Objects.equals(codiceProtocolloMittente, registrazioneClassificazioni.codiceProtocolloMittente) &&
        Objects.equals(oggetto, registrazioneClassificazioni.oggetto) &&
        Objects.equals(idRegistroProtocollo, registrazioneClassificazioni.idRegistroProtocollo) &&
        Objects.equals(codice, registrazioneClassificazioni.codice) &&
        Objects.equals(codiceAooProtocollante, registrazioneClassificazioni.codiceAooProtocollante) &&
        Objects.equals(codiceAooResponsabile, registrazioneClassificazioni.codiceAooResponsabile) &&
        Objects.equals(dbKeyTipoRegistrazione, registrazioneClassificazioni.dbKeyTipoRegistrazione) &&
        Objects.equals(objectIdClassificazione, registrazioneClassificazioni.objectIdClassificazione) &&
        Objects.equals(anno, registrazioneClassificazioni.anno) &&
        Objects.equals(flagRiservato, registrazioneClassificazioni.flagRiservato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dbKey, changeToken, dataProtocollo, stato, objectIdRegistrazione, annoProtocolloMittente, idAooResponsabile, objectIdAooResponsabile, dataProtocolloMittente, idAooProtocollante, objectIdAooProtocollante, codiceProtocolloMittente, oggetto, idRegistroProtocollo, codice, codiceAooProtocollante, codiceAooResponsabile, dbKeyTipoRegistrazione, objectIdClassificazione, anno, flagRiservato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegistrazioneClassificazioni {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dbKey: ").append(toIndentedString(dbKey)).append("\n");
    sb.append("    changeToken: ").append(toIndentedString(changeToken)).append("\n");
    sb.append("    dataProtocollo: ").append(toIndentedString(dataProtocollo)).append("\n");
    sb.append("    stato: ").append(toIndentedString(stato)).append("\n");
    sb.append("    objectIdRegistrazione: ").append(toIndentedString(objectIdRegistrazione)).append("\n");
    sb.append("    annoProtocolloMittente: ").append(toIndentedString(annoProtocolloMittente)).append("\n");
    sb.append("    idAooResponsabile: ").append(toIndentedString(idAooResponsabile)).append("\n");
    sb.append("    objectIdAooResponsabile: ").append(toIndentedString(objectIdAooResponsabile)).append("\n");
    sb.append("    dataProtocolloMittente: ").append(toIndentedString(dataProtocolloMittente)).append("\n");
    sb.append("    idAooProtocollante: ").append(toIndentedString(idAooProtocollante)).append("\n");
    sb.append("    objectIdAooProtocollante: ").append(toIndentedString(objectIdAooProtocollante)).append("\n");
    sb.append("    codiceProtocolloMittente: ").append(toIndentedString(codiceProtocolloMittente)).append("\n");
    sb.append("    oggetto: ").append(toIndentedString(oggetto)).append("\n");
    sb.append("    idRegistroProtocollo: ").append(toIndentedString(idRegistroProtocollo)).append("\n");
    sb.append("    codice: ").append(toIndentedString(codice)).append("\n");
    sb.append("    codiceAooProtocollante: ").append(toIndentedString(codiceAooProtocollante)).append("\n");
    sb.append("    codiceAooResponsabile: ").append(toIndentedString(codiceAooResponsabile)).append("\n");
    sb.append("    dbKeyTipoRegistrazione: ").append(toIndentedString(dbKeyTipoRegistrazione)).append("\n");
    sb.append("    objectIdClassificazione: ").append(toIndentedString(objectIdClassificazione)).append("\n");
    sb.append("    anno: ").append(toIndentedString(anno)).append("\n");
    sb.append("    flagRiservato: ").append(toIndentedString(flagRiservato)).append("\n");
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

