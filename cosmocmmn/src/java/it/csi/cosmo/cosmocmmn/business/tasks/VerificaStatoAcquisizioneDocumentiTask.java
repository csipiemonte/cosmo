/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmocmmn.business.service.DocumentiService;
import it.csi.cosmo.cosmocmmn.business.service.impl.DocumentiServiceImpl;


public class VerificaStatoAcquisizioneDocumentiTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression tipiDocumento;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "eseguo verifica stato acquisizione documenti per businessKey {}",
        businessKey);

    Long idPratica = Long.valueOf(businessKey);

    var exTipiDocumento = getTipiDocumento(execution);

    DocumentiService service = getBean(DocumentiServiceImpl.class);

    try {
      service.verificaStatoAcquisizioneDocumentiIndex(idPratica, exTipiDocumento);

    } catch (Exception e) {

      logger.error(method, e.getMessage());
      throw new BpmnError("DocumentiNonPronti", e.getMessage());
    }
  }

  public void setTipiDocumento(Expression tipiDocumento) {
    this.tipiDocumento = tipiDocumento;
  }

  private Set<String> getTipiDocumento(DelegateExecution execution) {
    return toSet(getClassField(execution, tipiDocumento));
  }

}
