/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.business.service.MailService;
import it.csi.cosmo.cosmocmmn.business.service.impl.MailServiceImpl;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;


public class InviaMailAssistenzaTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression text;
  private Expression subject;
  private Expression jsonMapping;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    MailService mailService = this.getBean(MailServiceImpl.class);

    String exSubject = this.requireClassField(execution, this.subject, "subject");
    String exText = this.requireClassField(execution, this.text, "text");

    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {
      // call mapping
      Long idPratica = Long.valueOf(execution.getProcessInstanceBusinessKey());
      Map<String, Object> mappingResult = richiediElaborazioneMappatura(idPratica, exJsonMapping);

      // replace mapping in fields
      exSubject = compila(exSubject, mappingResult);
      exText = compila(exText, mappingResult);
    }

    try {
      logger.info(method, "invio email ad assistenza con subject: {}", exSubject);
      mailService.inviaMailAssistenza(exSubject, exText);
      logger.info(method, "invio email ad assistenza completato");
    } catch (Exception e) {
      logger.error(method, "invio email ad assistenza fallito", e);
      throw e;
    }
  }

  @SuppressWarnings("deprecation")
  private String compila(String raw, Map<String, Object> risultatoElaborazione) {
    if (risultatoElaborazione == null) {
      risultatoElaborazione = new HashMap<>();
    }

    if (raw == null || !raw.contains("${")) {
      return raw;
    }

    StrSubstitutor sub = new StrSubstitutor(risultatoElaborazione);
    return sub.replace(raw);
  }

  private Map<String, Object> richiediElaborazioneMappatura(Long idPratica, String mappatura) {

    GetElaborazionePraticaRequest requestElaborazione = new GetElaborazionePraticaRequest();
    requestElaborazione.setMappatura(mappatura);

    logger.debug("richiediElaborazioneMappatura",
        "richiedo elaborazione mappa parametri per invio mail relativa alla pratica: {}",
        mappatura);

    CosmoBusinessPraticheFeignClient client = this.getBean(CosmoBusinessPraticheFeignClient.class);

    @SuppressWarnings("unchecked")
    Map<String, Object> datiElaborati =
        (Map<String, Object>) client.postPraticheIdElaborazione(idPratica, requestElaborazione);

    return datiElaborati;
  }

  private String getJsonMapping(DelegateExecution execution) {
    return getClassField(execution, jsonMapping);
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
  }

  public void setText(Expression text) {
    this.text = text;
  }

  public void setSubject(Expression subject) {
    this.subject = subject;
  }
}
