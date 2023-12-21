/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import com.fasterxml.jackson.databind.JsonNode;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoPratichePraticheFeignClient;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;
import it.csi.cosmo.cosmopratiche.dto.rest.StoricoPraticaRequest;

public abstract class ParentFlowableTask implements JavaDelegate {

  protected static final String VARIABLE_STATO = "stato";
  protected static final String VARIABLE_STATO_PRECEDENTE = "statoPrecedente";
  protected static final String VARIABLE_CLIENT_AGGIORNAMENTO_STATO = "clientAggiornamentoStato";
  protected static final String VARIABLE_UTENTE_AGGIORNAMENTO_STATO = "utenteAggiornamentoStato";
  protected static final String VARIABLE_DATA_AGGIORNAMENTO_STATO = "dataAggiornamentoStato";
  protected static final String VARIABLE_DATA_ULTIMO_INVIO_STATO = "dataUltimoInvioStato";
  private boolean logEventEnabled = true;

  protected final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  /**
   *
   */
  public ParentFlowableTask() {
    // Auto-generated constructor stub
  }


  protected void setVariable(DelegateExecution execution, String key, Object value) {
    if (value instanceof Map<?, ?> || value instanceof Collection<?>) {
      // import as json
      value = ObjectUtils.getDataMapper().convertValue(value, JsonNode.class);
    }
    execution.setVariable(key, value);
  }

  @SuppressWarnings("unchecked")
  protected <T> T getBean(Class<T> beanClass) {
    return (T) SpringApplicationContextHelper.getBean(beanClass);
  }

  @Override
  public void execute(DelegateExecution execution) {
    final var method = "execute";
    String taskID = this.getClass().getSimpleName() + "-" + UUID.randomUUID().toString();

    logger.info(method, "avvio service task " + taskID);
    logger.beginForClass(this.getClass().getName(), method);

    try {
      this.executeTask(execution);
      logger.info(method, "service task " + taskID + " terminato senza errori");
    } catch (Exception e) {
      logger.error(method, "exception throwed running task " + taskID, e);
      if (isFailOnException()) {
        if (e instanceof BpmnError) { // NOSONAR
          throw e;
        }
        throw new BpmnError(this.getClass().getSimpleName() + "Error", e.getMessage());
      }
    } finally {
      if (isLogEventEnabled()) {
        this.logEvent(execution);
      }

      logger.endForClass(this.getClass().getName(), method);
      logger.info(method, "terminato service task " + taskID);
    }
  }

  public abstract void executeTask(DelegateExecution execution);

  protected void logEvent(DelegateExecution execution) {
    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }
    Long idPratica = Long.valueOf(businessKey);
    CosmoPratichePraticheFeignClient feignClientPratiche =
        this.getBean(CosmoPratichePraticheFeignClient.class);
    StoricoPraticaRequest spr = getStoricoPraticaRequest(idPratica, execution.getCurrentFlowElement().getName());

    feignClientPratiche.postPraticheIdPraticaStorico(idPratica.intValue(), spr);
  }

  protected boolean isFailOnException() {
    return true;
  }

  protected String getClassField(DelegateExecution execution, Expression field) {
    if (field == null) {
      return null;
    }
    String raw = (String) field.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return raw.strip();
  }

  protected Boolean getClassFieldAsBoolean(DelegateExecution execution, Expression field) {
    if (field == null) {
      return null;
    }
    String raw = (String) field.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return null;
    }
    return Boolean.valueOf(raw);
  }

  protected String requireClassField(DelegateExecution execution, Expression field, String name) {
    String raw = getClassField(execution, field);
    if (StringUtils.isBlank(raw)) {
      throw new InternalServerException("Valorizzare il class field '" + name + "'");
    }
    return raw;
  }

  protected List<String> toList(String raw) {
    if (StringUtils.isBlank(raw)) {
      return Collections.emptyList();
    }
    return Arrays.stream(raw.split(",")).map(String::strip).filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }

  protected Set<String> toSet(String raw) {
    if (StringUtils.isBlank(raw)) {
      return Collections.emptySet();
    }
    return Arrays.stream(raw.split(",")).map(String::strip).filter(StringUtils::isNotBlank)
        .collect(Collectors.toSet());
  }

  protected StoricoPraticaRequest getStoricoPraticaRequest(Long idPratica, String eventName) {
    CosmoPratichePraticheFeignClient feignClientPratiche =
        this.getBean(CosmoPratichePraticheFeignClient.class);
    StoricoPraticaRequest spr = new StoricoPraticaRequest();
    spr.setIdPratica(idPratica);
    spr.setCodiceTipoEvento(TipoEventoStoricoPratica.EVENTO_AUTOMATICO.name());
    spr.setDescrizioneEvento(
        eventName != null ? eventName : getDefaultEventName());
    return spr;
  }

  protected String getDefaultEventName() {
    return "Evento generico";
  }

  protected boolean isLogEventEnabled() {
    return this.logEventEnabled;
  }

  protected void setLogEventEnabled(boolean logEventEnabled) {
    this.logEventEnabled = logEventEnabled;
  }
}
