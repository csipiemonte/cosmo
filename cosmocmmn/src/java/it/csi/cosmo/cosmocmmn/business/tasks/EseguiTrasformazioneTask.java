/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.Map;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;


public class EseguiTrasformazioneTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression trasformazione;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String exTrasf = getTrasformazione(execution);

    Long idPratica = Long.valueOf(execution.getProcessInstanceBusinessKey());
    Map<String, Object> mappingResult = richiediElaborazioneMappatura(idPratica, exTrasf);

    logger.info(method, "eseguo trasformazione per pratica {} ...", idPratica);

    for (var outputVariable : mappingResult.entrySet()) {

      logger.debug(method, "importo variabile da mappatura: {} = {}", outputVariable.getKey(),
          outputVariable.getValue());

      setVariable(execution, outputVariable.getKey(), outputVariable.getValue());
    }
  }

  private Map<String, Object> richiediElaborazioneMappatura(Long idPratica, String mappatura) {

    GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
    requestElaborazione.setMappatura(mappatura);

    logger.debug("richiediElaborazioneMappatura",
        "richiedo elaborazione relativa alla pratica: {}",
        mappatura);

    CosmoBusinessPraticheFeignClient client = this.getBean(CosmoBusinessPraticheFeignClient.class);

    @SuppressWarnings("unchecked")
    Map<String, Object> datiElaborati =
        (Map<String, Object>) client.postPraticheIdElaborazione(idPratica, requestElaborazione);

    return datiElaborati;
  }

  private String getTrasformazione(DelegateExecution execution) {
    return getClassField(execution, trasformazione);
  }

  public void setTrasformazione(Expression trasformazione) {
    this.trasformazione = trasformazione;
  }

}
