/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmoauthorization.dto.rest.EnteCertificatore;
import it.csi.cosmo.cosmoauthorization.dto.rest.ProfiloFEQ;
import it.csi.cosmo.cosmoauthorization.dto.rest.SceltaMarcaTemporale;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoCredenzialiFirma;
import it.csi.cosmo.cosmoauthorization.dto.rest.TipoOTP;
import java.time.OffsetDateTime;
import java.io.Serializable;
import javax.validation.constraints.*;

public class CertificatoFirma  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Long id = null;
  private String descrizione = null;
  private String username = null;
  private String pin = null;
  private OffsetDateTime dataScadenza = null;
  private EnteCertificatore enteCertificatore = null;
  private ProfiloFEQ profiloFEQ = null;
  private SceltaMarcaTemporale sceltaMarcaTemporale = null;
  private TipoCredenzialiFirma tipoCredenzialiFirma = null;
  private TipoOTP tipoOTP = null;
  private String password = null;
  private Boolean ultimoUtilizzato = null;

  /**
   **/
  


  // nome originario nello yaml: id 
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   **/
  


  // nome originario nello yaml: descrizione 
  @NotNull
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  /**
   **/
  


  // nome originario nello yaml: username 
  @NotNull
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  


  // nome originario nello yaml: pin 
  @NotNull
  public String getPin() {
    return pin;
  }
  public void setPin(String pin) {
    this.pin = pin;
  }

  /**
   **/
  


  // nome originario nello yaml: dataScadenza 
  public OffsetDateTime getDataScadenza() {
    return dataScadenza;
  }
  public void setDataScadenza(OffsetDateTime dataScadenza) {
    this.dataScadenza = dataScadenza;
  }

  /**
   **/
  


  // nome originario nello yaml: enteCertificatore 
  @NotNull
  public EnteCertificatore getEnteCertificatore() {
    return enteCertificatore;
  }
  public void setEnteCertificatore(EnteCertificatore enteCertificatore) {
    this.enteCertificatore = enteCertificatore;
  }

  /**
   **/
  


  // nome originario nello yaml: profiloFEQ 
  @NotNull
  public ProfiloFEQ getProfiloFEQ() {
    return profiloFEQ;
  }
  public void setProfiloFEQ(ProfiloFEQ profiloFEQ) {
    this.profiloFEQ = profiloFEQ;
  }

  /**
   **/
  


  // nome originario nello yaml: sceltaMarcaTemporale 
  @NotNull
  public SceltaMarcaTemporale getSceltaMarcaTemporale() {
    return sceltaMarcaTemporale;
  }
  public void setSceltaMarcaTemporale(SceltaMarcaTemporale sceltaMarcaTemporale) {
    this.sceltaMarcaTemporale = sceltaMarcaTemporale;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoCredenzialiFirma 
  @NotNull
  public TipoCredenzialiFirma getTipoCredenzialiFirma() {
    return tipoCredenzialiFirma;
  }
  public void setTipoCredenzialiFirma(TipoCredenzialiFirma tipoCredenzialiFirma) {
    this.tipoCredenzialiFirma = tipoCredenzialiFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: tipoOTP 
  @NotNull
  public TipoOTP getTipoOTP() {
    return tipoOTP;
  }
  public void setTipoOTP(TipoOTP tipoOTP) {
    this.tipoOTP = tipoOTP;
  }

  /**
   **/
  


  // nome originario nello yaml: password 
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  


  // nome originario nello yaml: ultimoUtilizzato 
  public Boolean isUltimoUtilizzato() {
    return ultimoUtilizzato;
  }
  public void setUltimoUtilizzato(Boolean ultimoUtilizzato) {
    this.ultimoUtilizzato = ultimoUtilizzato;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertificatoFirma certificatoFirma = (CertificatoFirma) o;
    return Objects.equals(id, certificatoFirma.id) &&
        Objects.equals(descrizione, certificatoFirma.descrizione) &&
        Objects.equals(username, certificatoFirma.username) &&
        Objects.equals(pin, certificatoFirma.pin) &&
        Objects.equals(dataScadenza, certificatoFirma.dataScadenza) &&
        Objects.equals(enteCertificatore, certificatoFirma.enteCertificatore) &&
        Objects.equals(profiloFEQ, certificatoFirma.profiloFEQ) &&
        Objects.equals(sceltaMarcaTemporale, certificatoFirma.sceltaMarcaTemporale) &&
        Objects.equals(tipoCredenzialiFirma, certificatoFirma.tipoCredenzialiFirma) &&
        Objects.equals(tipoOTP, certificatoFirma.tipoOTP) &&
        Objects.equals(password, certificatoFirma.password) &&
        Objects.equals(ultimoUtilizzato, certificatoFirma.ultimoUtilizzato);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, descrizione, username, pin, dataScadenza, enteCertificatore, profiloFEQ, sceltaMarcaTemporale, tipoCredenzialiFirma, tipoOTP, password, ultimoUtilizzato);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CertificatoFirma {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    dataScadenza: ").append(toIndentedString(dataScadenza)).append("\n");
    sb.append("    enteCertificatore: ").append(toIndentedString(enteCertificatore)).append("\n");
    sb.append("    profiloFEQ: ").append(toIndentedString(profiloFEQ)).append("\n");
    sb.append("    sceltaMarcaTemporale: ").append(toIndentedString(sceltaMarcaTemporale)).append("\n");
    sb.append("    tipoCredenzialiFirma: ").append(toIndentedString(tipoCredenzialiFirma)).append("\n");
    sb.append("    tipoOTP: ").append(toIndentedString(tipoOTP)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    ultimoUtilizzato: ").append(toIndentedString(ultimoUtilizzato)).append("\n");
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

