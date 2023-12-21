/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.dto.rest;

import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import it.csi.cosmo.cosmobusiness.dto.rest.Funzionalita;
import it.csi.cosmo.cosmobusiness.dto.rest.Pratica;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import java.util.List;
import java.io.Serializable;
import javax.validation.constraints.*;

public class FormTask  implements Serializable {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [implicit-camel-case] 
  private static final long serialVersionUID = 1L;
  
  private Pratica pratica = null;
  private Task task = null;
  private List<Funzionalita> funzionalita = new ArrayList<>();

  /**
   **/
  


  // nome originario nello yaml: pratica 
  public Pratica getPratica() {
    return pratica;
  }
  public void setPratica(Pratica pratica) {
    this.pratica = pratica;
  }

  /**
   **/
  


  // nome originario nello yaml: task 
  public Task getTask() {
    return task;
  }
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   **/
  


  // nome originario nello yaml: funzionalita 
  public List<Funzionalita> getFunzionalita() {
    return funzionalita;
  }
  public void setFunzionalita(List<Funzionalita> funzionalita) {
    this.funzionalita = funzionalita;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FormTask formTask = (FormTask) o;
    return Objects.equals(pratica, formTask.pratica) &&
        Objects.equals(task, formTask.task) &&
        Objects.equals(funzionalita, formTask.funzionalita);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pratica, task, funzionalita);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FormTask {\n");
    
    sb.append("    pratica: ").append(toIndentedString(pratica)).append("\n");
    sb.append("    task: ").append(toIndentedString(task)).append("\n");
    sb.append("    funzionalita: ").append(toIndentedString(funzionalita)).append("\n");
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

