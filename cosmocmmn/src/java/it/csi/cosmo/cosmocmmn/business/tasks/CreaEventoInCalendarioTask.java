/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.context.Context;
import org.flowable.task.api.Task;
import it.csi.cosmo.common.dto.search.GenericRicercaParametricaDTO;
import it.csi.cosmo.common.dto.search.StringFilter;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroGruppoTagDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.FiltroRicercaUtentiDTO;
import it.csi.cosmo.cosmoauthorization.dto.rest.Utente;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoAuthorizationUtentiFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;


public class CreaEventoInCalendarioTask extends ParentFlowableTask {

  /**
   * parametri forniti in input da process design
   */
  private Expression nome;
  private Expression descrizione;
  private Expression utente;
  private Expression gruppo;
  private Expression data;
  private Expression jsonMapping;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "Creo evento dalla pratica per businessKey {}", businessKey);

    String nomeField = getNome(execution);
    String descrizioneField = getDescrizione(execution);
    List<String> utenteField = toList(getUtente(execution));
    List<String> gruppoField = toList(getGruppo(execution));
    String dataField = getData(execution);

    String exJsonMapping = getJsonMapping(execution);

    if (exJsonMapping != null || !StringUtils.isBlank(exJsonMapping)) {

      Map<String, Object> mappingResult =
          richiediElaborazioneMappatura(Long.valueOf(businessKey), exJsonMapping);

      nomeField = compila(nomeField, mappingResult);
      descrizioneField = compila(descrizioneField, mappingResult);
      utenteField = asList(compila(utenteField, mappingResult));
      gruppoField = asList(compila(gruppoField, mappingResult));
      dataField = compila(dataField, mappingResult);
    }

    if ((utenteField == null || utenteField.isEmpty())
        && (gruppoField == null || gruppoField.isEmpty())) {
      throw new InternalServerException(
          "Nessun destinatario fornito. Valorizzare almeno uno dei class fields 'utente' e 'gruppo'");
    }

    if (gruppoField != null && !gruppoField.isEmpty()) {
      var utentiDeiGruppi = getUtentiFromGruppi(execution.getTenantId(), gruppoField);
      if (utenteField == null || utenteField.isEmpty()) {
        utenteField = utentiDeiGruppi;
      } else {
        utenteField.addAll(utentiDeiGruppi);
      }
    }

    var taskService = Context.getProcessEngineConfiguration().getTaskService();

    for (var single : utenteField) {

      Task createTask = taskService.newTask();
      createTask.setName(nomeField);
      createTask.setDescription(descrizioneField);
      createTask.setAssignee(single);
      createTask.setTenantId(execution.getTenantId());
      try {
        createTask.setDueDate(new SimpleDateFormat("dd/MM/yyyy").parse(dataField));
      } catch (ParseException e) {
        throw new InternalServerException(
            "Formato della data non valida. Il formato deve essere dd/MM/yyyy");
      }
      try {
        taskService.saveTask(createTask);

      } catch (Exception e) {
        logger.error(method, "errore nella creazione del task per un evento", e);
        throw new InternalServerException(
            "Si e' verificato un errore imprevisto nella creazione del task. Contattare l'assistenza.",
            e);
      }
    }

  }

  public String getNome(DelegateExecution execution) {
    return requireClassField(execution, nome, "nome");
  }

  public void setNome(Expression nome) {
    this.nome = nome;
  }

  public String getUtente(DelegateExecution execution) {
    return getClassField(execution, utente);
  }

  public void setUtente(Expression utente) {
    this.utente = utente;
  }

  public String getGruppo(DelegateExecution execution) {
    return getClassField(execution, gruppo);
  }

  public void setGruppo(Expression gruppo) {
    this.gruppo = gruppo;
  }

  public String getDescrizione(DelegateExecution execution) {
    return requireClassField(execution, descrizione, "descrizione");
  }

  public void setDescrizione(Expression descrizione) {
    this.descrizione = descrizione;
  }

  public String getData(DelegateExecution execution) {
    return requireClassField(execution, data, "data");
  }

  public void setData(Expression data) {
    this.data = data;
  }

  public String getJsonMapping(DelegateExecution execution) {
    return getClassField(execution, jsonMapping);
  }

  public void setJsonMapping(Expression jsonMapping) {
    this.jsonMapping = jsonMapping;
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

  private List<String> asList(List<String> raw) {

    if (raw == null)
      raw = new ArrayList<>();

    return raw.stream().flatMap(e -> Arrays.stream(e.split(","))).map(String::strip)
        .filter(StringUtils::isNotBlank).filter(Objects::nonNull).collect(Collectors.toList());
  }

  private List<String> compila(List<String> raw, Map<String, Object> risultatoElaborazione) {
    if (raw == null || raw.isEmpty()) {
      return raw;
    }
    return raw.stream().map(v -> compila(v, risultatoElaborazione))
        .collect(Collectors.toCollection(ArrayList::new));
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

  private List<String> getUtentiFromGruppi(String tenantId, List<String> gruppi) {

    CosmoAuthorizationUtentiFeignClient utentiFeignClient =
        this.getBean(CosmoAuthorizationUtentiFeignClient.class);

    GenericRicercaParametricaDTO<FiltroRicercaUtentiDTO> getUtentiRequest =
        new GenericRicercaParametricaDTO<>();

    FiltroRicercaUtentiDTO getUtentiFilter = new FiltroRicercaUtentiDTO();

    StringFilter codiceIpaEnteFilter = new StringFilter();
    codiceIpaEnteFilter.setEqualsIgnoreCase(tenantId);
    getUtentiFilter.setCodiceIpaEnte(codiceIpaEnteFilter);

    List<FiltroGruppoTagDTO> gruppiTagFilter = new LinkedList<>();
    gruppi.forEach(single -> {

      FiltroGruppoTagDTO filtro = new FiltroGruppoTagDTO();
      filtro.setNomeGruppo(single);
      gruppiTagFilter.add(filtro);
    });

    getUtentiFilter.setNeiGruppi(gruppiTagFilter);

    getUtentiRequest.setFilter(getUtentiFilter);
    var utentiResponse = utentiFeignClient.getUtenti(ObjectUtils.toJson(getUtentiRequest));

    if (utentiResponse == null || utentiResponse.getUtenti() == null
        || utentiResponse.getUtenti().isEmpty()) {
      return new LinkedList<>();
    }

    return utentiResponse.getUtenti().stream().map(Utente::getCodiceFiscale)
        .collect(Collectors.toList());
  }

}
