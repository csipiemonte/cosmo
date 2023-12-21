/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.process.model;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;

/**
 *
 *
 */
public class AfterCompleteHookContext extends FunzionalitaFormLogicoHookContext {

  protected Task request;
  protected TaskResponse taskBefore;

  private AfterCompleteHookContext(Builder builder) {
    this.pratica = builder.pratica;
    this.attivita = builder.attivita;
    this.taskId = builder.taskId;
    this.task = builder.task;
    this.formLogico = builder.formLogico;
    this.istanza = builder.istanza;
    this.utente = builder.utente;
    this.parametriIstanza = builder.parametriIstanza;
    this.request = builder.request;
    this.taskBefore = builder.taskBefore;
  }

  /**
   *
   */
  public AfterCompleteHookContext() {
    // Auto-generated constructor stub
  }

  @SuppressWarnings("unchecked")
  public Optional<Object> getVariabileRequest(String chiave) {
    if (StringUtils.isBlank(chiave)) {
      throw new InvalidParameterException();
    }

    return request != null && request.getVariables() != null ? request.getVariables().stream()
        .filter(
            v -> ((String) ((Map<String, Object>) v).get("name")).equalsIgnoreCase(chiave.trim()))
        .findFirst().map(v -> ((String) ((Map<String, Object>) v).get("value"))) : Optional.empty();
  }

  public Object requireVariabileRequest(String chiave) {
    Object v = getVariabileRequest(chiave).orElse(null);
    if (v == null || (v instanceof String && StringUtils.isBlank((String) v))) {
      throw new InternalServerException(
          "Variabile della request \"" + chiave + "\" non presente o non valida");
    }
    return v;
  }

  public Optional<Object> getVariabileTask(String chiave) {
    if (StringUtils.isBlank(chiave)) {
      throw new InvalidParameterException();
    }
    return task != null && task.getVariables() != null
        ? task.getVariables().stream().filter(v -> v.getName().equalsIgnoreCase(chiave.trim()))
            .findFirst().map(RestVariable::getValue)
            : Optional.empty();
  }

  public Object requireVariabileTask(String chiave) {
    Object v = getVariabileTask(chiave).orElse(null);
    if (v == null || (v instanceof String && StringUtils.isBlank((String) v))) {
      throw new InternalServerException(
          "Variabile di processo \"" + chiave + "\" non presente o non valida");
    }
    return v;
  }

  public Optional<Object> getVariabileTaskBefore(String chiave) {
    if (StringUtils.isBlank(chiave)) {
      throw new InvalidParameterException();
    }
    return taskBefore != null && taskBefore.getVariables() != null ? taskBefore.getVariables()
        .stream().filter(v -> v.getName().equalsIgnoreCase(chiave.trim())).findFirst()
        .map(RestVariable::getValue) : Optional.empty();
  }

  public Object requireVariabileTaskBefore(String chiave) {
    Object v = getVariabileTaskBefore(chiave).orElse(null);
    if (v == null || (v instanceof String && StringUtils.isBlank((String) v))) {
      throw new InternalServerException(
          "Variabile di processo \"" + chiave + "\" non presente o non valida");
    }
    return v;
  }

  @SuppressWarnings("unchecked")
  public Optional<Object> getVariabileInput(String chiave) {
    if (StringUtils.isBlank(chiave)) {
      throw new InvalidParameterException();
    }
    return request != null && request.getVariables() != null ? request.getVariables().stream()
        .filter(
            v -> ((String) ((Map<String, Object>) v).get("name")).equalsIgnoreCase(chiave.trim()))
        .findFirst().map(v -> ((String) ((Map<String, Object>) v).get("value"))) : Optional.empty();
  }

  public Object requireVariabileInput(String chiave) {
    Object v = getVariabileInput(chiave).orElse(null);
    if (v == null || (v instanceof String && StringUtils.isBlank((String) v))) {
      throw new InternalServerException(
          "Variabile di input \"" + chiave + "\" non presente o non valida");
    }
    return v;
  }

  public Task getRequest() {
    return request;
  }

  public TaskResponse getTaskBefore() {
    return taskBefore;
  }

  /**
   * Creates builder to build {@link AfterCompleteHookContext}.
   * @return created builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Builder to build {@link AfterCompleteHookContext}.
   */
  public static final class Builder {
    private CosmoTPratica pratica;
    private CosmoTAttivita attivita;
    private String taskId;
    private TaskResponse task;
    private CosmoTFormLogico formLogico;
    private CosmoTIstanzaFunzionalitaFormLogico istanza;
    private CosmoTUtente utente;
    private Map<String, Object> parametriIstanza = Collections.emptyMap();
    private Task request;
    private TaskResponse taskBefore;

    private Builder() {}

    public Builder withPratica(CosmoTPratica pratica) {
      this.pratica = pratica;
      return this;
    }

    public Builder withAttivita(CosmoTAttivita attivita) {
      this.attivita = attivita;
      return this;
    }

    public Builder withTaskId(String taskId) {
      this.taskId = taskId;
      return this;
    }

    public Builder withTask(TaskResponse task) {
      this.task = task;
      return this;
    }

    public Builder withFormLogico(CosmoTFormLogico formLogico) {
      this.formLogico = formLogico;
      return this;
    }

    public Builder withIstanza(CosmoTIstanzaFunzionalitaFormLogico istanza) {
      this.istanza = istanza;
      return this;
    }

    public Builder withUtente(CosmoTUtente utente) {
      this.utente = utente;
      return this;
    }

    public Builder withParametriIstanza(Map<String, Object> parametriIstanza) {
      this.parametriIstanza = parametriIstanza;
      return this;
    }

    public Builder withRequest(Task request) {
      this.request = request;
      return this;
    }

    public Builder withTaskBefore(TaskResponse taskBefore) {
      this.taskBefore = taskBefore;
      return this;
    }

    public AfterCompleteHookContext build() {
      return new AfterCompleteHookContext(this);
    }
  }

}
