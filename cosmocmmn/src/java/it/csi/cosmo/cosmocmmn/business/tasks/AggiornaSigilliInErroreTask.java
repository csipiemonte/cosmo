/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoEcmSigilloElettronicoFeignClient;


public class AggiornaSigilliInErroreTask extends ParentFlowableTask {

  /**
   * parametro fornito in input da process design
   */
  private Expression identificativoMessaggio;
  private Expression identificativoAlias;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    Long idPratica = Long.valueOf(businessKey);

    String exIdentificativoAlias = exIdentificativoAlias(execution);
    String exIdentificativoMessaggio = getIdentificativoMessaggio(execution);
    
    CosmoEcmSigilloElettronicoFeignClient feignClient = this.getBean(CosmoEcmSigilloElettronicoFeignClient.class);
    
    try {
      logger.info(method, "invio aggiornamento sigillo elettronico per i documenti con sigillo in errore per idPratica {}",
          idPratica);
      feignClient.postSigilloElettronicoIdPraticaAggiornaSigilliInErrore(idPratica, exIdentificativoMessaggio, exIdentificativoAlias);
      logger.info(method,
          "invio aggiornamento sigillo elettronico per i documenti con sigillo in errore completato per idPratica {}", idPratica);
    } catch (Exception e) {
      logger.error(method,
          "invio aggiornamento sigillo elettronico per i documenti con sigillo in errorefallito per idPratica " + idPratica, e);
      throw e;
    }

  }

  public String getIdentificativoMessaggio(DelegateExecution execution) {
    return this.requireClassField(execution, identificativoMessaggio, "identificativoMessaggio");
  }

  public String exIdentificativoAlias(DelegateExecution execution) {
    return this.requireClassField(execution, identificativoAlias, "IdentificativoAlias");
  }

}
