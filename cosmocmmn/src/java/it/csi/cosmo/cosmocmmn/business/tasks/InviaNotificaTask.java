/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business.tasks;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.cosmobusiness.dto.rest.GetElaborazionePraticaRequest;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessPraticheFeignClient;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;


public class InviaNotificaTask extends ParentFlowableTask {

  private static final boolean DEFAULT_PUSH = true;

  private static final boolean DEFAULT_SINCRONO = false;

  /**
   * parametri forniti in input da process design
   */
  private Expression messaggio;
  private Expression utente;
  private Expression gruppo;
  private Expression classe;
  private Expression push;
  private Expression sincrono;
  private Expression jsonMapping;

  @Override
  public void executeTask(DelegateExecution execution) {
    final var method = "executeTask";

    String businessKey = execution.getProcessInstanceBusinessKey();
    if (StringUtils.isBlank(businessKey)) {
      throw new InternalServerException("Nessuna business key da cui recuperare l'id pratica");
    }

    logger.info(method, "invio notifica per pratica per businessKey {}", businessKey);
    Long idPratica = Long.valueOf(businessKey);

    String messaggioField = getMessaggio(execution);
    List<String> utenteField = toList(getUtente(execution));
    List<String> gruppoField = toList(getGruppo(execution));
    String classeField = getClasse(execution);
    boolean pushField = getPush(execution);
    boolean sincronoField = getSincrono(execution);

    String exJsonMapping = getJsonMapping(execution);

    if (!StringUtils.isBlank(exJsonMapping)) {

      Map<String, Object> mappingResult = richiediElaborazioneMappatura(idPratica, exJsonMapping);

      messaggioField = compila(messaggioField,mappingResult);
      utenteField = asList(compila(utenteField, mappingResult));
      gruppoField = asList(compila(gruppoField, mappingResult));
      classeField = compila(classeField,mappingResult);

      pushField = compilaAsBoolean(getClassField(execution, push), mappingResult, DEFAULT_PUSH);


      sincronoField =
          compilaAsBoolean(getClassField(execution, sincrono), mappingResult, DEFAULT_SINCRONO);



    }

    if ((utenteField == null || utenteField.isEmpty())
        && (gruppoField == null || gruppoField.isEmpty())) {
      throw new InternalServerException(
          "Nessun destinatario fornito. Valorizzare almeno uno dei class fields 'utente' e 'gruppo'");
    }

    CreaNotificaRequest request = new CreaNotificaRequest();
    request.setArrivo(OffsetDateTime.now());
    request.setClasse(classeField);
    request.setIdPratica(idPratica);
    request.setMessaggio(messaggioField);
    request.setPush(pushField);
    request.setCodiceIpaEnte(execution.getTenantId());

    if (utenteField != null && !utenteField.isEmpty()) {
      request.setUtentiDestinatari(utenteField);
    }
    if (gruppoField != null && !gruppoField.isEmpty()) {
      request.setGruppiDestinatari(gruppoField);
    }

    CosmoNotificationsNotificheFeignClient client =
        getBean(CosmoNotificationsNotificheFeignClient.class);

    try {
      CreaNotificheRequest requests = new CreaNotificheRequest();
      requests.setNotifiche(Arrays.asList(request));
      client.postNotifications(requests);
    } catch (Exception e) {
      logger.error(method, "errore nell'invio della notifica dal processo", e);
      if (sincronoField) {
        logger.debug(method, "il processo e' sincrono, rilancio l'eccezione");
        throw e;
      } else {
        logger.debug(method, "il processo non e' sincrono, termino la gestione dell'eccezione");
      }
    }
  }

  public String getMessaggio(DelegateExecution execution) {
    return requireClassField(execution, messaggio, "messaggio");
  }

  public void setMessaggio(Expression messaggio) {
    this.messaggio = messaggio;
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

  public String getClasse(DelegateExecution execution) {
    return getClassField(execution, classe);
  }

  public void setClasse(Expression classe) {
    this.classe = classe;
  }

  public void setPush(Expression push) {
    this.push = push;
  }

  public boolean getPush(DelegateExecution execution) {
    if (push == null) {
      return DEFAULT_PUSH;
    }
    String raw = getClassField(execution, push);
    if (StringUtils.isBlank(raw)) {
      return DEFAULT_PUSH;
    }
    return Boolean.valueOf(raw.strip());
  }

  public boolean getSincrono(DelegateExecution execution) {
    if (sincrono == null) {
      return DEFAULT_SINCRONO;
    }
    String raw = (String) sincrono.getValue(execution);
    if (StringUtils.isBlank(raw)) {
      return DEFAULT_SINCRONO;
    }
    return Boolean.valueOf(raw.strip());
  }

  public void setSincrono(Expression sincrono) {
    this.sincrono = sincrono;
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

  @SuppressWarnings("deprecation")
  private Boolean compilaAsBoolean(String raw, Map<String, Object> risultatoElaborazione,
      Boolean defaultValue) {
    if (risultatoElaborazione == null) {
      risultatoElaborazione = new HashMap<>();
    }

    if (raw == null || StringUtils.isBlank(raw)) {
      return defaultValue;
    }

    if (!raw.contains("${")) {
      return Boolean.valueOf(raw.strip());
    }

    StrSubstitutor sub = new StrSubstitutor(risultatoElaborazione);
    String value = sub.replace(raw);
    if (StringUtils.isBlank(value)) {
      return defaultValue;
    }
    return Boolean.valueOf(value.strip());
  }

  private List<String> asList(List<String> raw) {

    return raw.stream().flatMap(e -> Arrays.stream(e.split(","))).map(String::strip)
        .filter(StringUtils::isNotBlank).collect(Collectors.toList());
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

}
