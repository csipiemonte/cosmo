/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.InviaChiamataEsternaRequest;
import it.csi.cosmo.cosmocmmn.business.service.AsyncTaskService;
import it.csi.cosmo.cosmocmmn.business.service.DocumentiService;
import it.csi.cosmo.cosmocmmn.business.service.impl.AsyncTaskServiceImpl;
import it.csi.cosmo.cosmocmmn.business.service.impl.DocumentiServiceImpl;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;


public class ChiamataEsternaTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression codiceEndpoint;
  private Expression url;
  private Expression metodo;
  private Expression mappaturaQuery;
  private Expression mappaturaUrl;
  private Expression mappaturaRequestBody;
  private Expression mappaturaHeaders;
  private Expression mappaturaOutput;
  private Expression codiceFruitore;
  private Expression restituisceJson;
  private Expression codiciTipiDocumentoIndex;
  private Expression jsonMapping;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "eseguo chiamata esterna per businessKey {}", businessKey);

    Long idPratica = Long.valueOf(businessKey);

    var allDocIndex = getAllDocumentiIndex(execution);
    var exTipiDocumento = getCodiciTipiDocumentoIndex(execution);

    if (allDocIndex || (exTipiDocumento != null && !exTipiDocumento.isEmpty())) {

      logger.info(method,
          "verifico stato acquisizione documenti per businessKey {} prima di inviare chiamata esterna",
          businessKey);

      DocumentiService service = getBean(DocumentiServiceImpl.class);

      try {
        if (allDocIndex) {
          service.verificaStatoAcquisizioneDocumentiIndex(idPratica);
        } else {
          service.verificaStatoAcquisizioneDocumentiIndex(idPratica, exTipiDocumento);
        }

      } catch (Exception e) {
        logger.error(method, e.getMessage());
        throw new BpmnError("DocumentiNonPronti", e.getMessage());
      }
    }

    InviaChiamataEsternaRequest request = new InviaChiamataEsternaRequest();
    request.setCodiceEndpoint(getCodiceEndpoint(execution));
    request.setMappaturaHeaders(getMappaturaHeaders(execution));
    request.setMappaturaOutput(getMappaturaOutput(execution));
    request.setMappaturaQuery(getMappaturaQuery(execution));
    request.setMappaturaRequestBody(getMappaturaRequestBody(execution));
    request.setMappaturaUrl(getMappaturaUrl(execution));
    request.setMetodo(getMetodo(execution));
    request.setUrl(getUrl(execution));
    request.setCodiceFruitore(getCodiceFruitore(execution));
    request.setRestituisceJson(getRestituisceJson(execution));

    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {

      // call mapping
      Map<String, Object> mappingResult = richiediElaborazioneMappatura(idPratica, exJsonMapping);

      request.setCodiceFruitore(compila(getCodiceFruitore(execution), mappingResult));
      request.setCodiceEndpoint(compila(getCodiceEndpoint(execution), mappingResult));
    }

    var client = getBean(CosmoBusinessPraticheFeignClient.class);
    AsyncTaskService asyncTaskService = getBean(AsyncTaskServiceImpl.class);

    // invia chiamata
    logger.info(method, "invio chiamata esterna");
    if (logger.isDebugEnabled()) {
      logger.debug(method, "payload invio chiamata esterna: {}", ObjectUtils.represent(request));
    }


    var response = client.postPraticaChiamataEstermaInvia(idPratica, request);

    if (logger.isDebugEnabled()) {
      logger.info(method, "response invio chiamata esterna:  {}",
          ObjectUtils.represent(response));
    } else {
      logger.info(method,
          "response invio chiamata esterna: avviata operazione asincrona {}", response.getUuid());
    }

    // attendi task async

    logger.info(method, "attesa del task asincrono {} ...", response.getUuid());
    var remoteAsyncTaskResult = asyncTaskService.wait(response.getUuid(), 15 * 60L);

    if (logger.isDebugEnabled()) {
      logger.info(method, "risultato task asincrono:  {}",
          ObjectUtils.represent(remoteAsyncTaskResult));
    } else {
      logger.info(method, "terminato task asincrono {}", response.getUuid());
    }

    // preleva mappatura dell'output se richiesto
    if (!StringUtils.isBlank(request.getMappaturaOutput())) {
      Map<String, Object> outputVariables = getVariabiliFromAsyncTaskOutput(remoteAsyncTaskResult);
      for (var outputVariable : outputVariables.entrySet()) {
        logger.debug(method, "importo variabile da output: {} = {}", outputVariable.getKey(),
            outputVariable.getValue());

        setVariable(execution, outputVariable.getKey(), outputVariable.getValue());
      }
    }

  }

  private Map<String, Object> getVariabiliFromAsyncTaskOutput(LongTaskPersistableEntry taskResult) {
    final var method = "getVariabiliFromAsyncTaskOutput";
    final var mappedOutputKey = "mappedOutput";

    logger.info(method, "importo risultato della mappatura dell'output");

    var firstDecode = ObjectUtils.fromJson(taskResult.getResult(), String.class);
    @SuppressWarnings("unchecked")
    Map<String, Object> asyncResult = ObjectUtils.fromJson(firstDecode, Map.class);

    if (!asyncResult.containsKey(mappedOutputKey)) {
      throw new InternalServerException("Risultato della mappatura dell'output non trovato");
    }

    var mappedOutput = asyncResult.get(mappedOutputKey);
    if (!(mappedOutput instanceof Map)) {
      throw new InternalServerException(
          "Risultato della mappatura dell'output non coerente: e' necessario mappare ad un oggetto");
    }

    @SuppressWarnings("unchecked")
    var mappedMapOutput = (Map<String, Object>) mappedOutput;
    Map<String, Object> output = new HashMap<>();

    for (var mappedField : mappedMapOutput.entrySet()) {
      output.put(mappedField.getKey(), mappedField.getValue());
    }

    return output;
  }

  public Set<String> getCodiciTipiDocumentoIndex(DelegateExecution execution) {
    String raw = getClassField(execution, codiciTipiDocumentoIndex);

    return toSet(raw);
  }

  public boolean getAllDocumentiIndex(DelegateExecution execution) {
    String raw = getClassField(execution, codiciTipiDocumentoIndex);

    return !StringUtils.isBlank(raw) && "*".equals(raw.strip());
  }

  public String getCodiceEndpoint(DelegateExecution execution) {
    return getClassField(execution, codiceEndpoint);
  }

  public String getUrl(DelegateExecution execution) {
    return getClassField(execution, url);
  }

  public String getMetodo(DelegateExecution execution) {
    return getClassField(execution, metodo);
  }

  public String getMappaturaQuery(DelegateExecution execution) {
    return getClassField(execution, mappaturaQuery);
  }

  public String getMappaturaUrl(DelegateExecution execution) {
    return getClassField(execution, mappaturaUrl);
  }

  public String getMappaturaRequestBody(DelegateExecution execution) {
    return getClassField(execution, mappaturaRequestBody);
  }

  public String getMappaturaHeaders(DelegateExecution execution) {
    return getClassField(execution, mappaturaHeaders);
  }

  public String getMappaturaOutput(DelegateExecution execution) {
    return getClassField(execution, mappaturaOutput);
  }

  public String getCodiceFruitore(DelegateExecution execution) {
    return getClassField(execution, codiceFruitore);
  }

  public Boolean getRestituisceJson(DelegateExecution execution) {
    return getClassFieldAsBoolean(execution, restituisceJson);
  }

  public String getJsonMapping(DelegateExecution execution) {
    return getClassField(execution, jsonMapping);
  }


  public void setCodiceEndpoint(Expression codiceEndpoint) {
    this.codiceEndpoint = codiceEndpoint;
  }

  public void setUrl(Expression url) {
    this.url = url;
  }

  public void setMetodo(Expression metodo) {
    this.metodo = metodo;
  }

  public void setMappaturaOutput(Expression mappaturaOutput) {
    this.mappaturaOutput = mappaturaOutput;
  }

  public void setMappaturaQuery(Expression mappaturaQuery) {
    this.mappaturaQuery = mappaturaQuery;
  }

  public void setMappaturaUrl(Expression mappaturaUrl) {
    this.mappaturaUrl = mappaturaUrl;
  }

  public void setMappaturaRequestBody(Expression mappaturaRequestBody) {
    this.mappaturaRequestBody = mappaturaRequestBody;
  }

  public void setMappaturaHeaders(Expression mappaturaHeaders) {
    this.mappaturaHeaders = mappaturaHeaders;
  }

  public void setCodiceFruitore(Expression codiceFruitore) {
    this.codiceFruitore = codiceFruitore;
  }

  public void setRestituisceJson(Expression restituisceJson) {
    this.restituisceJson = restituisceJson;
  }

  public void setCodiciTipiDocumentoIndex(Expression codiciTipiDocumentoIndex) {
    this.codiciTipiDocumentoIndex = codiciTipiDocumentoIndex;
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
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

}
