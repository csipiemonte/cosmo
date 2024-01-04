/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmosoap.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmosoap.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmosoap.dto.rest.DocumentiTask;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class DoSignFirmaMassivaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String alias = null;
  private String otp = null;
  private String pin = null;
  private String uuidTransaction = null;
  private String codiceCa = null;
  private String codiceTsa = null;
  private String profiloFEQ = null;
  private Boolean marcaTemporale = null;
  private Boolean notificaFirma = null;
  private String note = null;
  private Boolean processCarryOn = false;
  private List<AttivitaEseguibileMassivamente> tasks = new ArrayList<>();
  private List<DocumentiTask> documentiDaFirmare = new ArrayList<>();
  private String codiceEnteCertificatore = null;
  private String password = null;
  private String provider = null;

  /**
   **/
  


  // nome originario nello yaml: alias 
  @NotNull
  public String getAlias() {
    return alias;
  }
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   **/
  


  // nome originario nello yaml: otp 
  @NotNull
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
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
  


  // nome originario nello yaml: uuidTransaction 
  @NotNull
  public String getUuidTransaction() {
    return uuidTransaction;
  }
  public void setUuidTransaction(String uuidTransaction) {
    this.uuidTransaction = uuidTransaction;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceCa 
  public String getCodiceCa() {
    return codiceCa;
  }
  public void setCodiceCa(String codiceCa) {
    this.codiceCa = codiceCa;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceTsa 
  public String getCodiceTsa() {
    return codiceTsa;
  }
  public void setCodiceTsa(String codiceTsa) {
    this.codiceTsa = codiceTsa;
  }

  /**
   **/
  


  // nome originario nello yaml: profiloFEQ 
  @NotNull
  public String getProfiloFEQ() {
    return profiloFEQ;
  }
  public void setProfiloFEQ(String profiloFEQ) {
    this.profiloFEQ = profiloFEQ;
  }

  /**
   **/
  


  // nome originario nello yaml: marcaTemporale 
  @NotNull
  public Boolean isMarcaTemporale() {
    return marcaTemporale;
  }
  public void setMarcaTemporale(Boolean marcaTemporale) {
    this.marcaTemporale = marcaTemporale;
  }

  /**
   **/
  


  // nome originario nello yaml: notificaFirma 
  @NotNull
  public Boolean isNotificaFirma() {
    return notificaFirma;
  }
  public void setNotificaFirma(Boolean notificaFirma) {
    this.notificaFirma = notificaFirma;
  }

  /**
   **/
  


  // nome originario nello yaml: note 
  public String getNote() {
    return note;
  }
  public void setNote(String note) {
    this.note = note;
  }

  /**
   **/
  


  // nome originario nello yaml: processCarryOn 
  @NotNull
  public Boolean isProcessCarryOn() {
    return processCarryOn;
  }
  public void setProcessCarryOn(Boolean processCarryOn) {
    this.processCarryOn = processCarryOn;
  }

  /**
   **/
  


  // nome originario nello yaml: tasks 
  @NotNull
  public List<AttivitaEseguibileMassivamente> getTasks() {
    return tasks;
  }
  public void setTasks(List<AttivitaEseguibileMassivamente> tasks) {
    this.tasks = tasks;
  }

  /**
   **/
  


  // nome originario nello yaml: documentiDaFirmare 
  public List<DocumentiTask> getDocumentiDaFirmare() {
    return documentiDaFirmare;
  }
  public void setDocumentiDaFirmare(List<DocumentiTask> documentiDaFirmare) {
    this.documentiDaFirmare = documentiDaFirmare;
  }

  /**
   **/
  


  // nome originario nello yaml: codiceEnteCertificatore 
  @NotNull
  public String getCodiceEnteCertificatore() {
    return codiceEnteCertificatore;
  }
  public void setCodiceEnteCertificatore(String codiceEnteCertificatore) {
    this.codiceEnteCertificatore = codiceEnteCertificatore;
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
  


  // nome originario nello yaml: provider 
  public String getProvider() {
    return provider;
  }
  public void setProvider(String provider) {
    this.provider = provider;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DoSignFirmaMassivaRequest doSignFirmaMassivaRequest = (DoSignFirmaMassivaRequest) o;
    return Objects.equals(alias, doSignFirmaMassivaRequest.alias) &&
        Objects.equals(otp, doSignFirmaMassivaRequest.otp) &&
        Objects.equals(pin, doSignFirmaMassivaRequest.pin) &&
        Objects.equals(uuidTransaction, doSignFirmaMassivaRequest.uuidTransaction) &&
        Objects.equals(codiceCa, doSignFirmaMassivaRequest.codiceCa) &&
        Objects.equals(codiceTsa, doSignFirmaMassivaRequest.codiceTsa) &&
        Objects.equals(profiloFEQ, doSignFirmaMassivaRequest.profiloFEQ) &&
        Objects.equals(marcaTemporale, doSignFirmaMassivaRequest.marcaTemporale) &&
        Objects.equals(notificaFirma, doSignFirmaMassivaRequest.notificaFirma) &&
        Objects.equals(note, doSignFirmaMassivaRequest.note) &&
        Objects.equals(processCarryOn, doSignFirmaMassivaRequest.processCarryOn) &&
        Objects.equals(tasks, doSignFirmaMassivaRequest.tasks) &&
        Objects.equals(documentiDaFirmare, doSignFirmaMassivaRequest.documentiDaFirmare) &&
        Objects.equals(codiceEnteCertificatore, doSignFirmaMassivaRequest.codiceEnteCertificatore) &&
        Objects.equals(password, doSignFirmaMassivaRequest.password) &&
        Objects.equals(provider, doSignFirmaMassivaRequest.provider);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias, otp, pin, uuidTransaction, codiceCa, codiceTsa, profiloFEQ, marcaTemporale, notificaFirma, note, processCarryOn, tasks, documentiDaFirmare, codiceEnteCertificatore, password, provider);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DoSignFirmaMassivaRequest {\n");
    
    sb.append("    alias: ").append(toIndentedString(alias)).append("\n");
    sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    uuidTransaction: ").append(toIndentedString(uuidTransaction)).append("\n");
    sb.append("    codiceCa: ").append(toIndentedString(codiceCa)).append("\n");
    sb.append("    codiceTsa: ").append(toIndentedString(codiceTsa)).append("\n");
    sb.append("    profiloFEQ: ").append(toIndentedString(profiloFEQ)).append("\n");
    sb.append("    marcaTemporale: ").append(toIndentedString(marcaTemporale)).append("\n");
    sb.append("    notificaFirma: ").append(toIndentedString(notificaFirma)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    processCarryOn: ").append(toIndentedString(processCarryOn)).append("\n");
    sb.append("    tasks: ").append(toIndentedString(tasks)).append("\n");
    sb.append("    documentiDaFirmare: ").append(toIndentedString(documentiDaFirmare)).append("\n");
    sb.append("    codiceEnteCertificatore: ").append(toIndentedString(codiceEnteCertificatore)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    provider: ").append(toIndentedString(provider)).append("\n");
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

