/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.engine.variable.RestVariable;
import org.flowable.rest.service.api.runtime.process.ProcessInstanceResponse;
import org.flowable.rest.service.api.runtime.task.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.dto.rest.process.TaskIdentityLinkDTO;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ProcessRecoveryService;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;

@Service
public class ProcessRecoveryServiceImpl implements ProcessRecoveryService {

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoCmmnFeignClient cosmoCmmnClient;

  @Autowired
  private ProcessService processService;

  private static final String RECOVERY_ACTOR = "RECOVERY";

  @Override
  public boolean recover(Long idPratica, UserInfoDTO actor) {
    ValidationUtils.require(idPratica, "idPratica");

    var pratica = this.cosmoTPraticaRepository.findOne(idPratica);
    if (pratica == null) {
      throw new NotFoundException("Pratica non trovata");
    }

    var processInstanceId = pratica.getProcessInstanceId();
    if (StringUtils.isBlank(processInstanceId)) {
      throw new NotFoundException("Nessun processo assegnato alla pratica");
    }

    var processo = cosmoCmmnClient.getProcessInstance(processInstanceId);
    var tasks = cosmoCmmnClient.getTasksByProcessInstanceId(processInstanceId).getData();

    var variabili = Arrays.stream(cosmoCmmnClient.getProcessInstanceVariables(processInstanceId))
        .collect(Collectors.toList());

    Map<String, Object> variabiliMap = new HashMap<>();
    variabili.stream().forEach(elem -> variabiliMap.put(elem.getName(), elem.getValue()));
    boolean updated = false;

    if (recoverPraticaEntity(pratica, processo, variabili)) {
      updated = true;
    }

    if (recoverAttivitaEntities(pratica, tasks, variabiliMap)) {
      updated = true;
    }

    if (updated) {
      notificaRecovery(pratica, actor);
    }

    return updated;
  }

  protected void notificaRecovery(CosmoTPratica pratica, UserInfoDTO actor) {
    if (!Boolean.FALSE.equals(actor.getAnonimo())) {
      return;
    }

    CreaNotificaRequest request = new CreaNotificaRequest();
    request.setClasse("warning");
    request.setIdPratica(pratica.getId());
    request.setPush(true);
    request.setUtentiDestinatari(Arrays.asList(actor.getCodiceFiscale()));
    request.setTipoNotifica(TipoNotifica.RIPRISTINO_PRATICA.getAzione());

    request.setMessaggio(String.format(
        "La pratica %s e' stata ripristinata in seguito ad un errore.", pratica.getOggetto()));

    if (actor.getEnte() != null) {
      request.setCodiceIpaEnte(actor.getEnte().getTenantId());
    }

    processService.accodaNotifica(request);
  }

  /*
   * ritorna true se e' stata effettuata qualche modifica
   */
  protected boolean recoverPraticaEntity(CosmoTPratica target, ProcessInstanceResponse source,
      List<RestVariable> variables) {
    var now = Instant.now();

    String stato = variables.stream().filter(c -> c.getName().equals("stato")).findFirst()
        .map(v -> (String) v.getValue()).orElse(null);

    boolean updated = processService.registraCambioStato(target, stato, now, false);

    boolean targetEnded = target.getDataFinePratica() != null;
    boolean sourceEnded = source.isEnded();

    if (targetEnded && !sourceEnded) {
      updated = true;
      target.setDataCambioStato(Timestamp.from(now));
      target.setDataFinePratica(null);
      cosmoTPraticaRepository.saveAndFlush(target);

    } else if (!targetEnded && sourceEnded) {
      updated = true;
      target.setDataCambioStato(Timestamp.from(now));
      target.setDataFinePratica(Timestamp.from(now));
    }

    target.setUtenteUltimaModifica(RECOVERY_ACTOR);
    target.setDtUltimaModifica(Timestamp.from(now));
    cosmoTPraticaRepository.saveAndFlush(target);

    return updated;
  }

  /*
   * ritorna true se e' stata effettuata qualche modifica
   */
  private boolean recoverAttivitaEntities(CosmoTPratica pratica,
      List<TaskResponse> tasks, Map<String, Object> variabili) {

    var now = Instant.now();

    var listDiff =
        ComplexListComparator.compareLists(pratica.getAttivita(), tasks, (attivita, task) -> {
          String idTaskFromAtt = attivita.getTaskId();
          return task.getId() != null && idTaskFromAtt != null
              && task.getId().equals(idTaskFromAtt);
        });

    final AtomicInteger modificheEffettuate = new AtomicInteger(0);

    listDiff.onElementsInFirstNotInSecond(attivita -> {
      if (attivita.cancellato()) {
        return;
      }

      modificheEffettuate.incrementAndGet();

      // non esiste task collegato, termino l'attivita
      processService.aggiornaAttivitaTerminata(attivita);

      attivita.setUtenteUltimaModifica(RECOVERY_ACTOR);
      attivita.setDtUltimaModifica(Timestamp.from(now));
      cosmoTAttivitaRepository.saveAndFlush(attivita);

      cosmoTAttivitaRepository.deleteLogically(attivita, RECOVERY_ACTOR);
    });

    listDiff.onElementsInSecondNotInFirst(task -> {
      modificheEffettuate.incrementAndGet();

      // task non presente su db, creo l'attivita
      var nuovaAttivita = processService.importaNuovoTask(pratica, toTaskInstance(task), variabili);

      nuovaAttivita.setUtenteUltimaModifica(RECOVERY_ACTOR);
      nuovaAttivita.setDtUltimaModifica(Timestamp.from(now));
      cosmoTAttivitaRepository.saveAndFlush(nuovaAttivita);
    });

    listDiff.onElementsInBoth((attivita, task) -> {
      boolean somethingDone = false;

      // controlla coerenza attivazione
      if (attivita.cancellato()) {
        // cancellata su DB ma attiva in remoto, ripristino
        somethingDone = true;

        attivita.setDtCancellazione(null);
        attivita.setUtenteCancellazione(null);
        attivita.setUtenteUltimaModifica(RECOVERY_ACTOR);
        attivita.setDtUltimaModifica(Timestamp.from(now));
        cosmoTAttivitaRepository.saveAndFlush(attivita);
      }

      // gestisco assegnazioni
      if (processService.aggiornaAssegnazioni(attivita, toTaskInstance(task), pratica.getEnte(),
          variabili)) {
        somethingDone = true;
      }

      // marco attore per le modifiche se ho cambiato qualcosa
      if (somethingDone) {
        modificheEffettuate.incrementAndGet();

        attivita.setUtenteUltimaModifica(RECOVERY_ACTOR);
        attivita.setDtUltimaModifica(Timestamp.from(now));
        cosmoTAttivitaRepository.saveAndFlush(attivita);
      }
    });

    return modificheEffettuate.get() > 0;
  }

  /*
   * TODO WARNING: LOGICA DUPLICATA DA COSMOCMMN EVENT DISPATCHER
   */
  private TaskInstanceDTO toTaskInstance(TaskResponse t) {
    var mappedTask = new TaskInstanceDTO();
    mappedTask.setId(t.getId());
    mappedTask.setName(t.getName());
    mappedTask.setDescription(t.getDescription());
    mappedTask.setParentTaskId(t.getParentTaskId());
    mappedTask.setAssignee(t.getAssignee());

    var identityLinks = cosmoCmmnClient.getIdentityLinksByTaskId(t.getId());
    if (identityLinks != null) {
      mappedTask.setIdentityLinks(Arrays.stream(identityLinks).map(link -> {
        var out = new TaskIdentityLinkDTO();
        out.setType(link.getType());
        out.setUserId(link.getUser());
        out.setGroupId(link.getGroup());
        return out;
      }).collect(Collectors.toCollection(HashSet::new)));
    }

    return mappedTask;
  }

}
