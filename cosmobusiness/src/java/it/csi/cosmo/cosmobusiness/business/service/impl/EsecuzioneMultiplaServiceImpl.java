/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import it.csi.cosmo.common.async.internal.ContextAwareCallable;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.EsecuzioneMultiplaService;
import it.csi.cosmo.cosmobusiness.business.service.IstanzaFormLogiciService;
import it.csi.cosmo.cosmobusiness.business.service.LavorazionePraticaService;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaApprovaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaVariabiliProcessoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.dto.rest.Task;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTIstanzaFunzionalitaFormLogicoRepository;

@Service
public class EsecuzioneMultiplaServiceImpl implements EsecuzioneMultiplaService {

  private static final int MAX_PARALLEL_EXECUTIONS = 3;

  private static final String CONFIG_APPROVAZIONE_NOTE_APPROVAZIONE_KEY =
      "variabileNoteApprovazione";

  private static final String CONFIG_APPROVAZIONE_RISULTATO_APPROVAZIONE_KEY =
      "variabileRisultatoApprovazione";

  private static final String CONFIG_APPROVAZIONE_KEY = "O_APPROVAZIONE";

  @Autowired
  private AsyncTaskService asyncTaskService;

  @Autowired
  private LockService lockService;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTIstanzaFunzionalitaFormLogicoRepository cosmoTIstanzaFunzionalitaFormLogicoRepository;

  @Autowired
  private IstanzaFormLogiciService istanzaFormLogiciService;

  @Autowired
  private LavorazionePraticaService lavorazionePraticaService;

  @Autowired
  private ConfigurazioneService configurazioneService;

  @Override
  public RiferimentoOperazioneAsincrona postEsecuzioneMultiplaApprova(
      EsecuzioneMultiplaApprovaRequest request) {
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    Integer numMax = configurazioneService
        .requireConfig(ParametriApplicativo.NUM_MAX_APPROVAZIONI_PARALLELE).asInteger();
    if (numMax != null && request.getTasks().size() > numMax) {
      throw new BadRequestException("Troppe elaborazioni richieste. Il massimo consentito e' di "
          + numMax + " approvazioni alla volta.");
    }

    var resultCollector = new ConcurrentHashMap<Long, EsitoAttivitaEseguibileMassivamente>();

    var async = asyncTaskService
        .start(request.isApprovazione() ? "Approvazione multipla" : "Rifiuto multiplo", task -> {

          var executor = Executors
              .newFixedThreadPool(Math.min(request.getTasks().size(), MAX_PARALLEL_EXECUTIONS));

          // submit di subtask all'executor per l'esecuzione parallela
          for (AttivitaEseguibileMassivamente t : request.getTasks()) {
            var callable = new ContextAwareCallable<Object>(() -> task.step(
                t.getAttivita().getNome() + " (" + t.getPratica().getOggetto() + ")", step -> {
                  EsitoAttivitaEseguibileMassivamente esito =
                      new EsitoAttivitaEseguibileMassivamente();
                  esito.task = t;
                  try {
                    salvataggioVariabiliMassivo(request, null, t, step);
                    esito.successo = true;
                  } catch (Throwable terr) { // NOSONAR
                    esito.successo = false;
                    esito.errore = terr;
                  } finally {
                    resultCollector.put(t.getAttivita().getId().longValue(), esito);
                  }
                  return null;
                }), getCurrentRequestAttributes());
            executor.submit(callable);
          }

          // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
          executor.shutdown();
          try {
            if (!executor.awaitTermination(Math.max(300, request.getTasks().size() * 60),
                TimeUnit.SECONDS)) {
              executor.shutdownNow();
            }
          } catch (InterruptedException e) {
            task.warn(
                "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
            Thread.currentThread().interrupt();
          }

          // verifica risultati da resultCollector
          return ObjectUtils.toJson(resultCollector.values());
        });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(async.getTaskId());
    return output;
  }


  @Override
  public RiferimentoOperazioneAsincrona postEsecuzioneMultiplaVariabiliProcesso(
      EsecuzioneMultiplaVariabiliProcessoRequest request) {
    ValidationUtils.require(request, "request");
    ValidationUtils.validaAnnotations(request);

    Integer numMax = configurazioneService
        .requireConfig(ParametriApplicativo.NUM_MAX_APPROVAZIONI_PARALLELE).asInteger();
    if (numMax != null && request.getTasks().size() > numMax) {
      throw new BadRequestException(
          "Troppe elaborazioni richieste. Il massimo numero di form compilabili consentito e' di "
              + numMax + " form alla volta.");
    }

    var resultCollector = new ConcurrentHashMap<Long, EsitoAttivitaEseguibileMassivamente>();

    var async = asyncTaskService.start("Salvataggio variabili di processo multiplo", task -> {

      var executor = Executors
          .newFixedThreadPool(Math.min(request.getTasks().size(), MAX_PARALLEL_EXECUTIONS));

      // submit di subtask all'executor per l'esecuzione parallela
      for (AttivitaEseguibileMassivamente t : request.getTasks()) {
        var callable = new ContextAwareCallable<Object>(() -> task
            .step(t.getAttivita().getNome() + " (" + t.getPratica().getOggetto() + ")", step -> {
              EsitoAttivitaEseguibileMassivamente esito = new EsitoAttivitaEseguibileMassivamente();
              esito.task = t;
              try {
                salvataggioVariabiliMassivo(null, request, t, step);
                esito.successo = true;
              } catch (Throwable terr) { // NOSONAR
                esito.successo = false;
                esito.errore = terr;
              } finally {
                resultCollector.put(t.getAttivita().getId().longValue(), esito);
              }
              return null;
            }), getCurrentRequestAttributes());
        executor.submit(callable);
      }

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(Math.max(300, request.getTasks().size() * 60),
            TimeUnit.SECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        task.warn(
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }

      // verifica risultati da resultCollector
      return ObjectUtils.toJson(resultCollector.values());
    });

    var output = new RiferimentoOperazioneAsincrona();
    output.setUuid(async.getTaskId());
    return output;
  }


  private RequestAttributes getCurrentRequestAttributes() {
    try {
      return RequestContextHolder.currentRequestAttributes();
    } catch (IllegalStateException e) {
      return null;
    }
  }

  private void salvataggioVariabiliMassivo(EsecuzioneMultiplaApprovaRequest approvaRequest,
      EsecuzioneMultiplaVariabiliProcessoRequest variabiliProcessoRequest,
      AttivitaEseguibileMassivamente task, LongTask<Serializable> step) {

    var attivita = cosmoTAttivitaRepository.findOne(task.getAttivita().getId().longValue());

    String lockResourceCode = "@task(" + attivita.getTaskId() + ")";

    String lockOwner = UUID.randomUUID().toString();

    CosmoTLock[] locks = {null};

    step.step("Acquisizione del lock", substep -> {

      var lockRequest = new LockAcquisitionRequest();
      lockRequest.codiceOwner = lockOwner;
      lockRequest.codiceRisorsa = lockResourceCode;
      lockRequest.durata = 300000L;
      lockRequest.ritardoRetry = 250L;
      lockRequest.timeout = 5000L;

      var lockAcquisitionResult = lockService.acquire(lockRequest);

      if (lockAcquisitionResult.acquired) {
        locks[0] = lockAcquisitionResult.lock;
      } else {
        throw new ConflictException(lockAcquisitionResult.reason);
      }

      substep.sleep(500);
      return null;
    });

    try {
      step.step("Elaborazione del task", substep -> {
        salvataggioVariabiliMassivoInLock(approvaRequest, variabiliProcessoRequest, task, attivita,
            substep);
      });
    } finally {
      step.step("Rilascio del lock", substep -> {
        lockService.release(locks[0]);
      });
    }
  }

  private void salvataggioVariabiliMassivoInLock(
      EsecuzioneMultiplaApprovaRequest approvazioneRequest,
      EsecuzioneMultiplaVariabiliProcessoRequest variabiliProcessoRequest,
      AttivitaEseguibileMassivamente task, CosmoTAttivita attivita, LongTask<Serializable> step) {

    Long idIstanza = task.getFunzionalita().getId();

    // leggo la configurazione per sapere in che variabili salvare il risultato

    var istanza = cosmoTIstanzaFunzionalitaFormLogicoRepository.findOne(idIstanza);

    var parametri = istanzaFormLogiciService.getValoriParametri(istanza);

    var lavorazioneRequest = new Task();
    List<Object> variables = new ArrayList<>();
    lavorazioneRequest.setVariables(variables);

    Boolean mandareAvantiProcesso = null;

    if (approvazioneRequest != null) {
      var outputApprovazione = parametri.get(CONFIG_APPROVAZIONE_KEY).asObject();
      variables.add(setVariable(
          outputApprovazione.get(CONFIG_APPROVAZIONE_RISULTATO_APPROVAZIONE_KEY).asText(),
          String.valueOf(approvazioneRequest.isApprovazione())));
      variables.add(
          setVariable(outputApprovazione.get(CONFIG_APPROVAZIONE_NOTE_APPROVAZIONE_KEY).asText(),
              approvazioneRequest.getNote().strip()));

      mandareAvantiProcesso = approvazioneRequest.isMandareAvantiProcesso();

    } else if (variabiliProcessoRequest != null) {
      variabiliProcessoRequest.getVariabiliProcesso().forEach(
          variabile -> variables.add(setVariable(variabile.getName(), variabile.getValue())));

      mandareAvantiProcesso = variabiliProcessoRequest.isMandareAvantiProcesso();
    }

    if (Boolean.FALSE.equals(mandareAvantiProcesso)) {
      lavorazionePraticaService.eseguiLavorazioneAttivita(task.getPratica().getId().longValue(),
          attivita, lavorazioneRequest, false, step);
    } else {
      //@formatter:off
      lavorazionePraticaService.eseguiConfermaAttivita(
          task.getPratica().getId().longValue(),
          attivita,
          lavorazioneRequest,
          false,
          step
          );
      //@formatter:on
    }

  }


  protected Object setVariable(String name, Object value) {
    var output = new HashMap<String, Object>();
    output.put("name", name);
    output.put("value", value);
    return output;
  }

  public static class EsitoAttivitaEseguibileMassivamente {
    AttivitaEseguibileMassivamente task;
    Boolean successo;
    Throwable errore;

    public AttivitaEseguibileMassivamente getTask() {
      return task;
    }

    public Boolean getSuccesso() {
      return successo;
    }

    public Throwable getErrore() {
      return errore;
    }

  }

}
