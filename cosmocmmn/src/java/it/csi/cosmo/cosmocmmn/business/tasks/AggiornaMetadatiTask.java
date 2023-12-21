/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoPraticheMetadatiFeignClient;


public class AggiornaMetadatiTask extends ParentFlowableTask {

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    Long idPratica = Long.valueOf(businessKey);

    CosmoPraticheMetadatiFeignClient feignClient =
        this.getBean(CosmoPraticheMetadatiFeignClient.class);

    try {
      logger.info(method, "invio richiesta aggiornamento metadati per idPratica {}", idPratica);
      feignClient.putMetadatiIdPraticaVariabiliProcesso(businessKey);
      logger.info(method, "invio richiesta aggiornamento metadati completato per idPratica {}",
          idPratica);
    } catch (Exception e) {
      logger.error(method,
          "invio richiesta aggiornamento metadati fallito per idPratica " + idPratica,
          e);
      throw e;
    }
  }

}
