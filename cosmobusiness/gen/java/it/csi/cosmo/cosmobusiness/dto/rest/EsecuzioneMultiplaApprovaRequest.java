/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import io.swagger.annotations.ApiModel;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaEseguibileMassivamente;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class EsecuzioneMultiplaApprovaRequest  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private String note = null;
  private Boolean approvazione = null;
  private List<AttivitaEseguibileMassivamente> tasks = new ArrayList<>();
  private Boolean mandareAvantiProcesso = null;

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
  


  // nome originario nello yaml: approvazione 
  @NotNull
  public Boolean isApprovazione() {
    return approvazione;
  }
  public void setApprovazione(Boolean approvazione) {
    this.approvazione = approvazione;
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
    EsecuzioneMultiplaApprovaRequest esecuzioneMultiplaApprovaRequest = (EsecuzioneMultiplaApprovaRequest) o;
    return Objects.equals(note, esecuzioneMultiplaApprovaRequest.note) &&
        Objects.equals(approvazione, esecuzioneMultiplaApprovaRequest.approvazione) &&
        Objects.equals(tasks, esecuzioneMultiplaApprovaRequest.tasks) &&
        Objects.equals(mandareAvantiProcesso, esecuzioneMultiplaApprovaRequest.mandareAvantiProcesso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(note, approvazione, tasks, mandareAvantiProcesso);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EsecuzioneMultiplaApprovaRequest {\n");
    
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
    sb.append("    approvazione: ").append(toIndentedString(approvazione)).append("\n");
    sb.append("    tasks: ").append(toIndentedString(tasks)).append("\n");
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

