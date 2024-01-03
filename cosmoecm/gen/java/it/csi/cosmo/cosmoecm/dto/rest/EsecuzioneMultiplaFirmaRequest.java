/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmoecm.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmoecm.dto.rest.CertificatoFirma;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiTask;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsecuzioneMultiplaFirmaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private CertificatoFirma certificato = null;
  private String otp = null;
  private List<AttivitaEseguibileMassivamente> tasks = new ArrayList<>();
  private List<DocumentiTask> documentiTask = new ArrayList<>();
  private String note = null;
  private Boolean lockMgmt = true;
  private Boolean mandareAvantiProcesso = null;

  /**
   **/
  


  // nome originario nello yaml: certificato 
  public CertificatoFirma getCertificato() {
    return certificato;
  }
  public void setCertificato(CertificatoFirma certificato) {
    this.certificato = certificato;
  }

  /**
   **/
  


  // nome originario nello yaml: otp 
  public String getOtp() {
    return otp;
  }
  public void setOtp(String otp) {
    this.otp = otp;
  }

  /**
   **/
  


  // nome originario nello yaml: tasks 
  public List<AttivitaEseguibileMassivamente> getTasks() {
    return tasks;
  }
  public void setTasks(List<AttivitaEseguibileMassivamente> tasks) {
    this.tasks = tasks;
  }

  /**
   **/
  


  // nome originario nello yaml: documentiTask 
  public List<DocumentiTask> getDocumentiTask() {
    return documentiTask;
  }
  public void setDocumentiTask(List<DocumentiTask> documentiTask) {
    this.documentiTask = documentiTask;
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
  


  // nome originario nello yaml: lockMgmt 
  public Boolean isLockMgmt() {
    return lockMgmt;
  }
  public void setLockMgmt(Boolean lockMgmt) {
    this.lockMgmt = lockMgmt;
  }

  /**
   **/
  


  // nome originario nello yaml: mandareAvantiProcesso 
  @NotNull
  public Boolean isMandareAvantiProcesso() {
    return mandareAvantiProcesso;
  }
  public void setMandareAvantiProcesso(Boolean mandareAvantiProcesso) {
    this.mandareAvantiProcesso = mandareAvantiProcesso;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EsecuzioneMultiplaFirmaRequest esecuzioneMultiplaFirmaRequest = (EsecuzioneMultiplaFirmaRequest) o;
    return Objects.equals(certificato, esecuzioneMultiplaFirmaRequest.certificato) &&
        Objects.equals(otp, esecuzioneMultiplaFirmaRequest.otp) &&
        Objects.equals(tasks, esecuzioneMultiplaFirmaRequest.tasks) &&
        Objects.equals(documentiTask, esecuzioneMultiplaFirmaRequest.documentiTask) &&
        Objects.equals(note, esecuzioneMultiplaFirmaRequest.note) &&
        Objects.equals(lockMgmt, esecuzioneMultiplaFirmaRequest.lockMgmt) &&
        Objects.equals(mandareAvantiProcesso, esecuzioneMultiplaFirmaRequest.mandareAvantiProcesso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(certificato, otp, tasks, documentiTask, note, lockMgmt, mandareAvantiProcesso);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsecuzioneMultiplaFirmaRequest {\n");
    
    sb.append("    certificato: ").append(toIndentedString(certificato)).append("\n");
    sb.append("    otp: ").append(toIndentedString(otp)).append("\n");
    sb.append("    tasks: ").append(toIndentedString(tasks)).append("\n");
    sb.append("    documentiTask: ").append(toIndentedString(documentiTask)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    lockMgmt: ").append(toIndentedString(lockMgmt)).append("\n");
    sb.append("    mandareAvantiProcesso: ").append(toIndentedString(mandareAvantiProcesso)).append("\n");
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

