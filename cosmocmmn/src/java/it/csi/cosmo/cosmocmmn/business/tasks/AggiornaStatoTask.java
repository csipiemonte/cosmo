/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.dto.rest.process.ProcessEngineEventDTO;
import it.csi.cosmo.common.dto.rest.process.ProcessInstanceDTO;
import it.csi.cosmo.cosmocmmn.business.PraticheFlowableEventListener;
import it.csi.cosmo.cosmocmmn.business.service.EventSenderService;
import it.csi.cosmo.cosmocmmn.business.service.impl.EventSenderServiceImpl;
import it.csi.cosmo.cosmocmmn.security.SecurityUtils;
import it.csi.cosmo.cosmocmmn.util.listener.SpringApplicationContextHelper;


public class AggiornaStatoTask extends ParentFlowableTask {

  /**
   * parametro fornito in input da process design
   */
  private Expression stato;
  private Expression defer;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    setLogEventEnabled(false);

    String param = getStato(execution);

    String previous = (String) execution.getVariable(VARIABLE_STATO);

    if (previous == null || !param.equals(previous)) {
      if (previous != null) {
        execution.setVariable(VARIABLE_STATO_PRECEDENTE, previous);
      }

      logger.info(method, "task status update: {} -> {}", previous, param);

      execution.setVariable(VARIABLE_STATO, param);
    }

    var varDataAggStato = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    execution.setVariable(VARIABLE_DATA_AGGIORNAMENTO_STATO, varDataAggStato);

    execution.setVariable(VARIABLE_UTENTE_AGGIORNAMENTO_STATO,
        SecurityUtils.getUtenteCorrente().getCodiceFiscale());

    execution.setVariable(VARIABLE_CLIENT_AGGIORNAMENTO_STATO,
        SecurityUtils.getClientCorrente().getCodice());

    Boolean deferVal = getDefer(execution);
    if (!Boolean.TRUE.equals(deferVal)) {
      logger.info(method, "sending status update to cosmo: {} -> {}", previous, param);

      ProcessEngineEventDTO newEvent = new ProcessEngineEventDTO();
      newEvent.setMessageType(PraticheFlowableEventListener.STATE_CHANGE);
      newEvent.setTimestamp(OffsetDateTime.now());

      var mappedProcess = new ProcessInstanceDTO();
      mappedProcess.setBusinessKey(execution.getProcessInstanceBusinessKey());
      mappedProcess.setTenantId(execution.getTenantId());
      mappedProcess.setId(execution.getProcessInstanceId());
      newEvent.setProcess(mappedProcess);

      mappedProcess.setVariables(new HashMap<>());
      mappedProcess.getVariables().put(VARIABLE_STATO, param);
      mappedProcess.getVariables().put(VARIABLE_DATA_AGGIORNAMENTO_STATO, varDataAggStato);
      mappedProcess.getVariables().put(VARIABLE_UTENTE_AGGIORNAMENTO_STATO,
          SecurityUtils.getUtenteCorrente().getCodiceFiscale());
      mappedProcess.getVariables().put(VARIABLE_CLIENT_AGGIORNAMENTO_STATO,
          SecurityUtils.getClientCorrente().getCodice());

      var senderService =
          (EventSenderService) SpringApplicationContextHelper.getBean(EventSenderServiceImpl.class);

      senderService.sendEventToCosmo(newEvent);

      logger.info(method, "sent status update to cosmo: {} -> {}", previous, param);
    }
  }

  public String getStato(DelegateExecution execution) {
    return (String) stato.getValue(execution);
  }

  public Boolean getDefer(DelegateExecution execution) {
    // return Boolean.valueOf((String) defer.getValue(execution));
    return Boolean.FALSE;
  }

  public void setStato(Expression stato) {
    this.stato = stato;
  }

  public void setDefer(Expression defer) {
    this.defer = defer;
  }

}
