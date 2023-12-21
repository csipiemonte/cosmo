/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.process.model;

import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTIstanzaFunzionalitaFormLogico;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.InternalServerException;

/**
 *
 *
 */
public abstract class FunzionalitaFormLogicoHookContext {

  protected CosmoTPratica pratica;
  protected CosmoTAttivita attivita;
  protected String taskId;
  protected TaskResponse task;
  protected CosmoTFormLogico formLogico;
  protected CosmoTIstanzaFunzionalitaFormLogico istanza;
  protected CosmoTUtente utente;
  protected Map<String, Object> parametriIstanza;

  public Optional<Object> getParametro(String chiave) {
    Object v = parametriIstanza != null ? parametriIstanza.getOrDefault(chiave, null) : null;
    return Optional.ofNullable(v);
  }

  public Object requireParametro(String chiave) {
    Object v = getParametro(chiave).orElse(null);
    if (v == null || (v instanceof String && StringUtils.isBlank((String) v))) {
      throw new InternalServerException("Parametro \"" + chiave + "\" non fornito o non valido");
    }
    return v;
  }

  public CosmoTIstanzaFunzionalitaFormLogico getIstanza() {
    return istanza;
  }

  public Map<String, Object> getParametriIstanza() {
    return parametriIstanza;
  }

  public CosmoTUtente getUtente() {
    return utente;
  }

  public CosmoTFormLogico getFormLogico() {
    return formLogico;
  }

  public CosmoTPratica getPratica() {
    return pratica;
  }

  public CosmoTAttivita getAttivita() {
    return attivita;
  }

  public String getTaskId() {
    return taskId;
  }

  public TaskResponse getTask() {
    return task;
  }
}
