/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import it.csi.cosmo.common.async.BatchProcessingQueue;
import it.csi.cosmo.common.dto.rest.process.TaskIdentityLinkDTO;
import it.csi.cosmo.common.dto.rest.process.TaskInstanceDTO;
import it.csi.cosmo.common.entities.CosmoDStatoPratica;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.entities.CosmoRPraticaTag;
import it.csi.cosmo.common.entities.CosmoRPraticaTag_;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag;
import it.csi.cosmo.common.entities.CosmoRUtenteGruppoTag_;
import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.entities.CosmoTAttivita_;
import it.csi.cosmo.common.entities.CosmoTEnte;
import it.csi.cosmo.common.entities.CosmoTEnte_;
import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.entities.CosmoTFormLogico_;
import it.csi.cosmo.common.entities.CosmoTFruitore;
import it.csi.cosmo.common.entities.CosmoTGruppo;
import it.csi.cosmo.common.entities.CosmoTGruppo_;
import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.entities.CosmoTTag;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.entities.enums.TipoNotifica;
import it.csi.cosmo.common.exception.BadRequestException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.security.model.UserInfoDTO;
import it.csi.cosmo.common.util.ComplexListComparator;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.ProcessService;
import it.csi.cosmo.cosmobusiness.business.service.StoricoPraticaService;
import it.csi.cosmo.cosmobusiness.config.ErrorMessages;
import it.csi.cosmo.cosmobusiness.dto.rest.AvanzamentoProcessoRequest;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoDStatoPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRAttivitaAssegnazioneRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRPraticaTagRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoRUtenteGruppoTagRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTAttivitaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTFormLogicoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTGruppoRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTPraticaRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTTagRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmobusiness.integration.repository.specifications.CosmoRUtenteGruppoTagSpecifications;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoNotificationsNotificheFeignClient;
import it.csi.cosmo.cosmobusiness.security.SecurityUtils;
import it.csi.cosmo.cosmobusiness.util.CommonUtils;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificaRequest;
import it.csi.cosmo.cosmonotifications.dto.rest.CreaNotificheRequest;

@Service
public class ProcessServiceImpl implements ProcessService, DisposableBean {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  private static final String MESSAGE_EVENT_RECEIVED_ACTION = "messageEventReceived";

  @Autowired
  private CosmoTPraticaRepository cosmoTPraticaRepository;

  @Autowired
  private CosmoTFormLogicoRepository cosmoTFormLogicoRepository;

  @Autowired
  private CosmoTAttivitaRepository cosmoTAttivitaRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired
  private CosmoTGruppoRepository cosmoTGruppoRepository;

  @Autowired
  private CosmoDStatoPraticaRepository cosmoDStatoPraticaRepository;

  @Autowired
  private CosmoRAttivitaAssegnazioneRepository cosmoRAttivitaAssegnazioneRepository;

  @Autowired
  private CosmoNotificationsNotificheFeignClient notificheFeignClient;

  @Autowired
  private StoricoPraticaService storicoPraticaService;

  @Autowired
  private CosmoRPraticaTagRepository cosmoRPraticaTagRepository;

  @Autowired
  private CosmoRUtenteGruppoTagRepository cosmoRUtenteGruppoTagRepository;

  @Autowired
  private CosmoTTagRepository cosmoTTagRepository;

  private BatchProcessingQueue<CreaNotificaRequest> notificationsQueue;

  @Autowired
  private CosmoCmmnFeignClient cmmnFeignClient;


  public ProcessServiceImpl() {

    //@formatter:off
    this.notificationsQueue = BatchProcessingQueue.<CreaNotificaRequest>builder()
        .withLogger(logger)
        .withDispatchInterval(4000)
        .withParallel(false)
        .withFixedDelay(false)
        .withConsumer(this::dequeueNotifications)
        .build();
    //@formatter:on
  }

  @Override
  public void destroy() throws Exception {
    if (this.notificationsQueue != null) {
      this.notificationsQueue.close();
    }
  }

  private void dequeueNotifications(List<CreaNotificaRequest> toSend) {
    if (toSend.isEmpty()) {
      return;
    }

    final var method = "dequeueNotifications";
    logger.debug(method, "sending {} queued notifications", toSend.size());

    try {
      CreaNotificheRequest request = new CreaNotificheRequest();
      List<CreaNotificaRequest> requests = new LinkedList<>();
      toSend.forEach(requests::add);
      request.setNotifiche(requests);

      notificheFeignClient.postNotifications(request);
    } catch (Exception e) {
      logger.error(method, "error sending notifications batch", e);
    }
  }

  @Override
  public CosmoTAttivita importaNuovoTask(CosmoTPratica pratica, TaskInstanceDTO task,
      Map<String, Object> variabili) {
    // creo nuova attivita' che rappresenta il task
    CosmoTAttivita attivitaDaSalvare = new CosmoTAttivita();

    attivitaDaSalvare.setCosmoTPratica(pratica);
    attivitaDaSalvare.setDescrizione(task.getDescription());
    attivitaDaSalvare.setLinkAttivita(linkAttivita(task.getId()));
    attivitaDaSalvare.setNome(task.getName());
    attivitaDaSalvare.setCosmoRAttivitaAssegnaziones(new LinkedList<>());
    attivitaDaSalvare.setFormKey(task.getFormKey());
    setAttivitaFormLogico(attivitaDaSalvare, pratica.getEnte());

    if (!StringUtils.isBlank(task.getParentTaskId())) {
      attivitaDaSalvare.setParent(cosmoTAttivitaRepository
          .findOneNotDeletedByField(CosmoTAttivita_.linkAttivita,
              linkAttivita(task.getParentTaskId()))
          .orElseThrow(() -> new it.csi.cosmo.common.exception.NotFoundException(
              "Attivita' parent non trovata o non attiva")));
    }



    CosmoTAttivita attivita = cosmoTAttivitaRepository.save(attivitaDaSalvare);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_CREATA)
        .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata creata.", attivita.getNome()))
        .withPratica(pratica)
        .withAttivita(attivita)
        .withUtenteCorrente(false)
        .build());
    //@formatter:on


    // gestisco assegnazioni
    aggiornaAssegnazioni(attivita, task, pratica.getEnte(), variabili);

    // invio notifiche
    tentaNotificaCreazioneTask(attivita);

    return attivita;
  }


  private void setAttivitaFormLogico(CosmoTAttivita attivita, CosmoTEnte ente) {
    List<CosmoTFormLogico> forms = cosmoTFormLogicoRepository
        .findNotDeletedByField(CosmoTFormLogico_.codice, attivita.getFormKey());
    CosmoTFormLogico result = forms.stream().filter(
        form -> form.getCosmoTEnte() != null && form.getCosmoTEnte().getId().equals(ente.getId()))
        .findFirst().orElse(
            forms.stream().filter(form -> form.getCosmoTEnte() == null).findFirst().orElse(null));

    if (result == null) {
      throw new NotFoundException(
          "Nessun form logico associato all'attivita' : " + attivita.getFormKey());
    }
    attivita.setFormLogico(result);
  }

  @Override
  public CosmoTAttivita importaNuovaAttivita(CosmoTPratica pratica, CosmoTAttivita attivita,
      CosmoTFruitore fruitore) {

    attivita = cosmoTAttivitaRepository.save(attivita);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_CREATA)
        .withDescrizioneEvento(String.format("L'attivita' \"%s\" e' stata creata.", attivita.getNome()))
        .withPratica(pratica)
        .withAttivita(attivita)
        .withUtenteCorrente(false)
        .withFruitore(fruitore)
        .build());
    //@formatter:on

    // gestisco assegnazioni
    List<CosmoRAttivitaAssegnazione> nuove =
        attivita.getCosmoRAttivitaAssegnaziones().stream().collect(Collectors.toList());
    attivita.getCosmoRAttivitaAssegnaziones().clear();
    aggiornaAssegnazioni(attivita, nuove, pratica.getEnte(), fruitore);

    // invio notifiche
    tentaNotificaCreazioneTask(attivita);

    return attivita;
  }

  @Override
  public void tentaNotificaCreazioneTask(CosmoTAttivita attivita) {
    final var method = "tentaNotificaCreazioneTask";

    attivita.getCosmoRAttivitaAssegnaziones().forEach(assegnazione -> {
      if (Boolean.TRUE.equals(assegnazione.getAssegnatario())
          && assegnazione.getIdUtente() != null) {
        try {
          notificaCreazioneTaskAssegnatariDiretti(attivita, assegnazione);
        } catch (Exception e) {
          logger.error(method,
              "errore nell'invio della notifica di creazione del task all'utente assegnatario", e);
        }
      } else if (assegnazione.getIdGruppo() != null) {
        try {
          notificaCreazioneTaskGruppo(attivita, assegnazione);
        } catch (Exception e) {
          logger.error(method,
              "errore nell'invio della notifica di creazione del task al gruppo assegnatario", e);
        }
      }
    });
  }

  private void tentaNotificaAnnullamentoTask(CosmoTAttivita attivita) {
    final var method = "tentaNotificaAnnullamentoTask";
    Timestamp fiveMinutesAgo = Timestamp.from(OffsetDateTime.now().minusMinutes(5).toInstant());

    attivita.getCosmoRAttivitaAssegnaziones().forEach(assegnazione -> {
      if (assegnazione.getDtFineVal() != null
          && assegnazione.getDtFineVal().before(fiveMinutesAgo)) {
        return;
      }

      if (Boolean.TRUE.equals(assegnazione.getAssegnatario())
          && assegnazione.getIdUtente() != null) {
        try {
          notificaAnnullamentoTaskAssegnatariDiretti(attivita, assegnazione);
        } catch (Exception e) {
          logger.error(method,
              "errore nell'invio della notifica di annullamento del task all'utente assegnatario",
              e);
        }
      } else if (assegnazione.getIdGruppo() != null) {
        try {
          notificaAnnullamentoTaskGruppo(attivita, assegnazione);
        } catch (Exception e) {
          logger.error(method,
              "errore nell'invio della notifica di annullamento del task al gruppo assegnatario",
              e);
        }
      }
    });
  }

  @Override
  public boolean registraCambioStato(CosmoTPratica pratica, String nuovoStatoRaw,
      Instant timestamp, boolean explicit) {
    if (StringUtils.isBlank(nuovoStatoRaw)) {
      return false;
    }

    CosmoDStatoPratica nuovoStato = getStatoPratica(nuovoStatoRaw, pratica.getTipo().getCodice());
    if (pratica.getStato() == null
        || !pratica.getStato().getCodice().equals(nuovoStato.getCodice())) {
      // stato cambiato

      if (explicit) {
        //@formatter:off
        storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
            .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_CAMBIO_STATO)
            .withDescrizioneEvento(String.format("La pratica e' passata in stato \"%s\".",
                nuovoStato.getDescrizione()))
            .withPratica(pratica)
            .build());
        //@formatter:on
      }

      pratica.setDataCambioStato(Timestamp.from(timestamp));
      pratica.setStato(nuovoStato);
      cosmoTPraticaRepository.saveAndFlush(pratica);

      logger.debug("registraCambioStato", "Nuovo stato: {}", nuovoStato.getDescrizione());

      return true;
    }

    return false;
  }

  @Override
  public void aggiornaAttivitaTerminata(CosmoTAttivita attivitaDB) {

    List<CosmoTAttivita> subtasks =
        cosmoTAttivitaRepository.findByField(CosmoTAttivita_.parent, attivitaDB);
    for (CosmoTAttivita subtask : subtasks) {
      aggiornaAttivitaTerminata(subtask);
    }

    if (attivitaDB.nonCancellato()) {
      attivitaDB.setDtCancellazione(Timestamp.from(Instant.now()));
      attivitaDB.setUtenteCancellazione(SecurityUtils.getClientCorrente().getCodice());
      cosmoTAttivitaRepository.save(attivitaDB);
    }

    List<CosmoRAttivitaAssegnazione> assegnazioniDB = cosmoRAttivitaAssegnazioneRepository
        .findByCosmoTAttivitaIdAndDtFineValIsNull(attivitaDB.getId());

    for (CosmoRAttivitaAssegnazione assegnazioneDB : assegnazioniDB) {
      if (assegnazioneDB.valido()) {
        assegnazioneDB.setDtFineVal(Timestamp.from(Instant.now()));
        cosmoRAttivitaAssegnazioneRepository.save(assegnazioneDB);
      }
    }
  }

  @Override
  public void aggiornaAttivitaAnnullata(CosmoTAttivita attivitaDB) {
    aggiornaAttivitaAnnullata(attivitaDB, true, null);
  }

  @Override
  public void aggiornaAttivitaAnnullata(CosmoTAttivita attivitaDB, boolean notifica,
      CosmoTFruitore fruitore) {
    if (attivitaDB == null || attivitaDB.cancellato()) {
      return;
    }

    if (notifica) {
      // invio notifiche
      tentaNotificaAnnullamentoTask(attivitaDB);
    }

    aggiornaAttivitaTerminata(attivitaDB);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_ANNULLATA)
        .withDescrizioneEvento(String.format(
            "L'attivita' \"%s\" della pratica \"%s\" e' stata annullata.",
            attivitaDB.getNome(),
            attivitaDB.getCosmoTPratica().getOggetto()
            ))
        .withPratica(attivitaDB.getCosmoTPratica())
        .withAttivita(attivitaDB)
        .withFruitore(fruitore)
        .build());
    //@formatter:on
  }

  @Override
  public boolean aggiornaAssegnazioni(CosmoTAttivita attivita,
      List<CosmoRAttivitaAssegnazione> assegnazioni, CosmoTEnte ente) {

    return aggiornaAssegnazioni(attivita, assegnazioni, ente, null);
  }

  /*
   * ritorna true se e' stata effettuata qualche modifica
   */
  @Override
  public boolean aggiornaAssegnazioni(CosmoTAttivita attivita,
      List<CosmoRAttivitaAssegnazione> assegnazioni, CosmoTEnte ente, CosmoTFruitore fruitore) {
    final var method = "aggiornaAssegnazioni";

    final AtomicInteger modificheEffettuate = new AtomicInteger(0);

    // confronto le nuove assegnazioni con le vecchie, per lanciare a DB solo gli update
    // che sono strettamente necessari
    ComplexListComparator
    .compareLists(attivita.getCosmoRAttivitaAssegnaziones(), assegnazioni, (vecchia, nuova) -> {
      return vecchia.valido() && vecchia.getAssegnatario().equals(nuova.getAssegnatario())
          && Objects.equals(vecchia.getIdGruppo(), nuova.getIdGruppo())
          && Objects.equals(vecchia.getIdUtente(), nuova.getIdUtente());

    }).onElementsInFirstNotInSecond(daEliminare -> {
      if (daEliminare.getDtFineVal() == null) {
        modificheEffettuate.incrementAndGet();
        daEliminare.setDtFineVal(Timestamp.from(Instant.now()));
        cosmoRAttivitaAssegnazioneRepository.save(daEliminare);
        logger.info(method, "elimino associazione attivita {} - {}", attivita.getId(),
            daEliminare);
      }
    }).onElementsInSecondNotInFirst(daInserire -> {
      modificheEffettuate.incrementAndGet();
      inserisciNuovaAssegnazioneTask(attivita, daInserire, fruitore);
    });

    return modificheEffettuate.get() > 0;
  }

  /*
   * ritorna true se e' stata effettuata qualche modifica
   */
  @Override
  public boolean aggiornaAssegnazioni(CosmoTAttivita attivita, TaskInstanceDTO task,
      CosmoTEnte ente, Map<String, Object> variabili) {

    // costruisco lista nuove assegnazioni
    List<CosmoRAttivitaAssegnazione> assegnazioni = new LinkedList<>();

    if (!StringUtils.isBlank(task.getAssignee())) {
      // esiste un assegnatario diretto
      assegnazioni.add(buildAssegnazioneDiretta(attivita, task.getAssignee()));
    }

    if (areIdentityLinksAssignmentsViaTags(task)) {
      // assegnazione
      assegnazioni
      .addAll(buildAssegnazioniByTags(attivita, task.getIdentityLinks(), ente, variabili));

    } else {
      // se l'identityLink contiene un unico valore ed e' legato all'id di un gruppo, verifico la
      // presenza di tag legati alla pratica
      List<CosmoRPraticaTag> relazioniPraticaTags = new LinkedList<>();
      if (canSearchTagsRelatedToPractice(task)) {
        relazioniPraticaTags = cosmoRPraticaTagRepository
            .findActiveByField(CosmoRPraticaTag_.cosmoTPratica, attivita.getCosmoTPratica());
      }


      if (!CollectionUtils.isEmpty(relazioniPraticaTags)) {
        // assegno da relazione pratica e tags
        assegnazioni.addAll(buildAssegnazioniByRelPraticaTags(
            attivita,
            task.getIdentityLinks().iterator().next(), ente, relazioniPraticaTags));
      } else {
        // assegno da identity links
        task.getIdentityLinks().stream()
        .map(identityLink -> buildAssegnazioneIndiretta(attivita, identityLink, ente))
        .forEach(assegnazioni::add);
      }
    }


    return this.aggiornaAssegnazioni(attivita, assegnazioni, ente);
  }

  private boolean areIdentityLinksAssignmentsViaTags(TaskInstanceDTO task) {
    return !CollectionUtils.isEmpty(task.getIdentityLinks()) && task.getIdentityLinks().stream()
        .allMatch(p -> p.getGroupId().substring(0, 4).equalsIgnoreCase("tag."));
  }

  private void inserisciNuovaAssegnazioneTask(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione daInserire, CosmoTFruitore fruitore) {
    final var method = "inserisciNuovaAssegnazioneTask";

    // inserisci assegnazione su DB
    daInserire = cosmoRAttivitaAssegnazioneRepository.save(daInserire);
    attivita.getCosmoRAttivitaAssegnaziones().add(daInserire);
    logger.info(method, "inserita associazione attivita {} - {}", attivita.getId(), daInserire);

    UserInfoDTO utenteCorrente = SecurityUtils.getUtenteCorrente();

    CosmoTUtente utenteDestinatarioAssociazione =
        daInserire.getIdUtente() != null ? getUtenteById(daInserire.getIdUtente().longValue())
            : null;

    CosmoTGruppo gruppoDestinatarioAssociazione =
        daInserire.getIdGruppo() != null ? getGruppoById(daInserire.getIdGruppo().longValue())
            : null;

    boolean toUtenteCorrente =
        utenteDestinatarioAssociazione != null && utenteDestinatarioAssociazione.getCodiceFiscale()
        .equals(utenteCorrente.getCodiceFiscale());

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.ATTIVITA_ASSEGNATA)
        .withDescrizioneEvento(String.format(
            "L'attivita' \"%s\" e' stata assegnata" + (utenteDestinatarioAssociazione != null ?
                " a " + utenteDestinatarioAssociazione.getNome() + " " + utenteDestinatarioAssociazione.getCognome() + "." :
                  gruppoDestinatarioAssociazione != null ?
                      " al gruppo " + gruppoDestinatarioAssociazione.getDescrizione() + "." : "."),
            attivita.getNome()
            ))
        .withPratica(attivita.getCosmoTPratica())
        .withAttivita(attivita)
        .withUtenteCoinvolto(utenteDestinatarioAssociazione)
        .withGruppoCoinvolto(gruppoDestinatarioAssociazione)
        .withUtenteCorrente(fruitore == null && !toUtenteCorrente)
        .withFruitore(fruitore)
        .build());
    //@formatter:on

    // invia notifica all'assegnatario
    try {
      notificaAssegnazioneTask(attivita, daInserire);
    } catch (Exception e) {
      logger.error(method,
          "errore nell'invio della notifica di assegnazione del task: " + e.getMessage(), e);
    }
  }

  private void notificaCreazioneTaskAssegnatariDiretti(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione assegnazione) {
    CreaNotificaRequest notifica = new CreaNotificaRequest();

    if (assegnazione == null || !assegnazione.valido() || assegnazione.getIdUtente() == null) {
      return;
    }

    CosmoTUtente utente = getUtenteById(assegnazione.getIdUtente().longValue());

    notifica.setUtentiDestinatari(Arrays.asList(utente.getCodiceFiscale()));

    String messaggio = String.format(ErrorMessages.MESSAGGIO_NOTIFICA_CREAZIONE, attivita.getNome(),
        attivita.getCosmoTPratica().getOggetto());
    notifica.setMessaggio(messaggio);
    notifica.setTipoNotifica(TipoNotifica.CREA_TASK.getAzione());

    if (attivita.getCosmoTPratica() != null) {
      notifica.setIdPratica(attivita.getCosmoTPratica().getId());
      notifica.setCodiceIpaEnte(attivita.getCosmoTPratica().getEnte().getCodiceIpa());
    }

    notificationsQueue.submit(notifica);
  }

  private void notificaAnnullamentoTaskAssegnatariDiretti(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione assegnazione) {
    CreaNotificaRequest notifica = new CreaNotificaRequest();

    if (assegnazione == null || assegnazione.getIdUtente() == null) {
      return;
    }

    CosmoTUtente utente = getUtenteById(assegnazione.getIdUtente().longValue());

    notifica.setUtentiDestinatari(Arrays.asList(utente.getCodiceFiscale()));

    String messaggio = String.format(ErrorMessages.MESSAGGIO_NOTIFICA_ANNULLAMENTO,
        attivita.getNome(), attivita.getCosmoTPratica().getOggetto());
    notifica.setMessaggio(messaggio);
    notifica.setTipoNotifica(TipoNotifica.ANNULLA_TASK.getAzione());
    notifica.setClasse("warning");

    if (attivita.getCosmoTPratica() != null) {
      notifica.setIdPratica(attivita.getCosmoTPratica().getId());
      notifica.setCodiceIpaEnte(attivita.getCosmoTPratica().getEnte().getCodiceIpa());
    }

    notificationsQueue.submit(notifica);
  }

  @Override
  public void accodaNotifica(CreaNotificaRequest notifica) {
    notificationsQueue.submit(notifica);
  }

  private void notificaCreazioneTaskGruppo(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione assegnazione) {

    if (assegnazione == null || !assegnazione.valido() || assegnazione.getIdGruppo() == null) {
      return;
    }

    CosmoTPratica pratica = attivita.getCosmoTPratica();
    CosmoTGruppo gruppo = getGruppoById(assegnazione.getIdGruppo().longValue());

    // tralascio il check sui figli cosi' evito una query. un gruppo non dovrebbe mai essere vuoto

    CreaNotificaRequest notifica = new CreaNotificaRequest();

    String messaggio = String.format(ErrorMessages.MESSAGGIO_NOTIFICA_CREAZIONE, attivita.getNome(),
        pratica.getOggetto());

    notifica.setMessaggio(messaggio);
    notifica.setTipoNotifica(TipoNotifica.CREA_TASK.getAzione());

    notifica.setIdPratica(pratica.getId());
    notifica.setCodiceIpaEnte(pratica.getEnte().getCodiceIpa());
    notifica.setGruppiDestinatari(Arrays.asList(gruppo.getCodice()));

    notificationsQueue.submit(notifica);
  }

  private void notificaAnnullamentoTaskGruppo(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione assegnazione) {

    if (assegnazione == null || assegnazione.getIdGruppo() == null) {
      return;
    }

    CosmoTPratica pratica = attivita.getCosmoTPratica();
    CosmoTGruppo gruppo = getGruppoById(assegnazione.getIdGruppo().longValue());

    // tralascio il check sui figli cosi' evito una query. un gruppo non dovrebbe mai essere vuoto

    CreaNotificaRequest notifica = new CreaNotificaRequest();

    String messaggio = String.format(ErrorMessages.MESSAGGIO_NOTIFICA_ANNULLAMENTO,
        attivita.getNome(), pratica.getOggetto());

    notifica.setMessaggio(messaggio);
    notifica.setTipoNotifica(TipoNotifica.ANNULLA_TASK.getAzione());
    notifica.setClasse("warning");

    notifica.setIdPratica(pratica.getId());
    notifica.setCodiceIpaEnte(pratica.getEnte().getCodiceIpa());
    notifica.setGruppiDestinatari(Arrays.asList(gruppo.getCodice()));

    notificationsQueue.submit(notifica);
  }

  private void notificaAssegnazioneTask(CosmoTAttivita attivita,
      CosmoRAttivitaAssegnazione assegnazione) {
    CreaNotificaRequest notifica = new CreaNotificaRequest();

    if (assegnazione == null || !assegnazione.valido() || assegnazione.getIdUtente() == null) {
      return;
    }

    CosmoTPratica pratica = attivita.getCosmoTPratica();
    CosmoTUtente utente = getUtenteById(assegnazione.getIdUtente().longValue());

    notifica.setUtentiDestinatari(Arrays.asList(utente.getCodiceFiscale()));

    String messaggio = String.format(ErrorMessages.MESSAGGIO_NOTIFICA_RIASSEGNAZIONE,
        attivita.getNome(), pratica.getOggetto());
    notifica.setMessaggio(messaggio);
    notifica.setTipoNotifica(TipoNotifica.ASSEGNA_TASK.getAzione());

    notifica.setIdPratica(pratica.getId());
    notifica.setCodiceIpaEnte(pratica.getEnte().getCodiceIpa());

    notificationsQueue.submit(notifica);
  }

  /*
   * HELPERS
   */

  private String linkAttivita(String id) {
    return "tasks/" + id;
  }

  private CosmoRAttivitaAssegnazione buildAssegnazioneIndiretta(CosmoTAttivita attivita,
      TaskIdentityLinkDTO identityLink, CosmoTEnte ente) {
    var assegnazione = new CosmoRAttivitaAssegnazione();
    assegnazione.setCosmoTAttivita(attivita);
    assegnazione.setAssegnatario("assignee".equals(identityLink.getType()));
    assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));

    if (!StringUtils.isBlank(identityLink.getUserId())) {
      assegnazione
      .setIdUtente(getUtenteByCodiceFiscale(identityLink.getUserId()).getId().intValue());
    }

    if (!StringUtils.isBlank(identityLink.getGroupId())) {
      assegnazione
      .setIdGruppo(
          getGruppoByFlowableName(identityLink.getGroupId(), ente.getId()).getId().intValue());
    }
    return assegnazione;
  }

  @Override
  public CosmoRAttivitaAssegnazione buildAssegnazioneDiretta(CosmoTAttivita attivita, String cf) {
    var assegnazione = new CosmoRAttivitaAssegnazione();
    assegnazione.setCosmoTAttivita(attivita);
    assegnazione.setAssegnatario(true);
    assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
    CosmoTUtente utente = getUtenteByCodiceFiscale(cf);
    assegnazione.setIdUtente(utente.getId().intValue());
    return assegnazione;
  }

  @Override
  public CosmoRAttivitaAssegnazione buildAssegnazioneGruppo(CosmoTEnte ente,
      CosmoTAttivita attivita, String codiceGruppo) {
    var assegnazione = new CosmoRAttivitaAssegnazione();
    assegnazione.setCosmoTAttivita(attivita);
    assegnazione.setAssegnatario(false);
    assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
    assegnazione
    .setIdGruppo(getGruppoByFlowableName(codiceGruppo, ente.getId()).getId().intValue());
    return assegnazione;
  }

  private CosmoTGruppo getGruppoByFlowableName(String flowableName, Long idEnte) {
    if (idEnte == null) {
      throw new InternalServerException("Nessun ente associato al processo");
    }

    CosmoTGruppo gruppo =
        cosmoTGruppoRepository.findOneNotDeleted((root, cq, cb) -> {
          var joinEnte = (root.join(CosmoTGruppo_.ente));
          return cb.and(cb.equal(joinEnte.get(CosmoTEnte_.id), idEnte),
              cb.equal(root.get(CosmoTGruppo_.codice), flowableName));
        })
        .orElse(null);
    if (gruppo == null) {
      throw new InternalServerException("Gruppo con flowableName " + flowableName + " non trovato");
    }
    return gruppo;
  }

  private CosmoTGruppo getGruppoById(Long id) {
    CosmoTGruppo gruppo = cosmoTGruppoRepository.findOne(id);
    if (gruppo == null) {
      throw new InternalServerException("Gruppo con id " + id + " non trovato");
    }
    return gruppo;
  }

  private CosmoTUtente getUtenteByCodiceFiscale(String cf) {
    CosmoTUtente utente = cosmoTUtenteRepository.findByCodiceFiscale(cf);
    if (utente == null) {
      throw new InternalServerException("Utente con codice fiscale " + cf + " non trovato");
    }
    return utente;
  }

  private CosmoTUtente getUtenteById(Long id) {
    CosmoTUtente utente = cosmoTUtenteRepository.findOne(id);
    if (utente == null) {
      throw new InternalServerException("Utente con id " + id + " non trovato");
    }
    return utente;
  }

  @Override
  public CosmoDStatoPratica getStatoPratica(String codice, String codiceTipoPratica) {
    ValidationUtils.require(codice, "codice");
    CosmoDStatoPratica stato = cosmoDStatoPraticaRepository.findOneActive(codice).orElseThrow(
        () -> new InternalServerException("StatoPratica con codice " + codice + " non trovato"));

    if (!StringUtils.isBlank(codiceTipoPratica) && stato.getCosmoRStatoTipoPraticas().stream()
        .noneMatch(r -> r.valido() && r.getCosmoDTipoPratica() != null
        && r.getCosmoDTipoPratica().getCodice().equals(codiceTipoPratica))) {

      throw new InternalServerException("Lo stato \"" + codice
          + "\" non e' coerente col tipo pratica \"" + codiceTipoPratica + "\"");
    }
    return stato;
  }

  @Override
  public void terminaPratica(CosmoTPratica pratica, OffsetDateTime timestamp) {

    terminaPratica(pratica, timestamp, null);
  }

  @Override
  public void terminaPratica(CosmoTPratica pratica, OffsetDateTime timestamp,
      CosmoTFruitore fruitore) {
    Timestamp now = Timestamp.from(timestamp.toInstant());


    for (var attivita : pratica.getAttivita()) {
      if (attivita.nonCancellato()) {
        aggiornaAttivitaAnnullata(attivita, false, fruitore);
      }
    }

    pratica.setDataCambioStato(now);
    pratica.setDataFinePratica(now);

    // save and flush necessario per concorrenza
    cosmoTPraticaRepository.saveAndFlush(pratica);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_COMPLETATA)
        .withDescrizioneEvento(String.format("La lavorazione della pratica e' completata.", pratica.getOggetto()))
        .withPratica(pratica)
        .withFruitore(fruitore)
        .build());
    //@formatter:on

    logger.info("terminaPratica", "terminata pratica con id {}", pratica.getId());
  }

  @Override
  public void annullaPratica(CosmoTPratica pratica, OffsetDateTime timestamp) {
    annullaPratica(pratica, timestamp, null);
  }

  @Override
  public void annullaPratica(CosmoTPratica pratica, OffsetDateTime timestamp,
      CosmoTFruitore fruitore) {
    Timestamp now = Timestamp.from(timestamp.toInstant());


    for (var attivita : pratica.getAttivita()) {
      if (attivita.nonCancellato()) {
        aggiornaAttivitaAnnullata(attivita, false, fruitore);
      }
    }

    pratica.setDataCambioStato(now);
    pratica.setDataFinePratica(now);

    // save and flush necessario per concorrenza
    cosmoTPraticaRepository.saveAndFlush(pratica);

    // loggo l'operazione su db
    //@formatter:off
    storicoPraticaService.logEvent(CosmoLStoricoPratica.builder()
        .withCodiceTipoEvento(TipoEventoStoricoPratica.PRATICA_ANNULLATA)
        .withDescrizioneEvento(String.format("La pratica e' stata annullata.", pratica.getOggetto()))
        .withPratica(pratica)
        .withFruitore(fruitore)
        .build());
    //@formatter:on

    // delete logico della pratica
    cosmoTPraticaRepository.deleteLogically(pratica, AuditServiceImpl.getPrincipalCode());

    logger.info("annullaPratica", "annullata pratica con id {}", pratica.getId());
  }

  private List<CosmoRAttivitaAssegnazione> buildAssegnazioniByRelPraticaTags(
      CosmoTAttivita attivita,
      TaskIdentityLinkDTO identityLink, CosmoTEnte ente, List<CosmoRPraticaTag> relPraticaTags) {

    List<CosmoRAttivitaAssegnazione> ret = new LinkedList<>();
    List<Long> userId = new LinkedList<>();

    relPraticaTags.stream().forEach(rpt -> {
      List<CosmoRUtenteGruppoTag> rugts = cosmoRUtenteGruppoTagRepository
          .findActiveByField(CosmoRUtenteGruppoTag_.cosmoTTag, rpt.getCosmoTTag());

      rugts.stream()
      .filter(rugt -> rugt.getCosmoTUtenteGruppo().getIdGruppo()
          .equals(getGruppoByFlowableName(identityLink.getGroupId(), ente.getId()).getId()))
      .forEach(filtered -> userId.add(filtered.getCosmoTUtenteGruppo().getIdUtente()));

    });

    userId.stream().forEach(ui -> {
      var assegnazione = new CosmoRAttivitaAssegnazione();
      assegnazione.setCosmoTAttivita(attivita);
      assegnazione.setAssegnatario(Boolean.TRUE);
      assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
      assegnazione.setIdUtente(ui.intValue());

      ret.add(assegnazione);
    });

    return ret;

  }

  private boolean canSearchTagsRelatedToPractice(TaskInstanceDTO task) {
    return task != null && task.getIdentityLinks().size() == 1
        && !StringUtils.isBlank(task.getIdentityLinks().iterator().next().getGroupId());
  }

  private List<CosmoRAttivitaAssegnazione> buildAssegnazioniByTags(
      CosmoTAttivita attivita, Set<TaskIdentityLinkDTO> identityLinks, CosmoTEnte ente,
      Map<String, Object> variabili) {

    List<CosmoRAttivitaAssegnazione> ret = new LinkedList<>();
    List<Long> usersId = new LinkedList<>();

    identityLinks.stream().forEach(il -> {

      List<String> tokens = Arrays.asList(il.getGroupId().replace("tag.", "").split(".gruppi."));

      if (tokens == null || tokens.isEmpty()) {
        throw new BadRequestException("Formato tag non corretto");
      }

      CosmoTTag tag = cosmoTTagRepository.findOneByCosmoTEnteAndCodiceAndDtCancellazioneIsNull(ente,
          tokens.get(0));

      if (tag == null) {
        throw new NotFoundException("Tag non trovato");
      }


      if(tokens.size()>1) {

        ArrayList<String> res = null;
        try {
          res = (ArrayList<String>) variabili.getOrDefault(tokens.get(1), null);
        } catch (ClassCastException e) {
          throw new BadRequestException("Formato variabile di processo non corretto");
        }

        if (res == null) {
          throw new NotFoundException("Variabile di processo non trovata");
        }

        if (res.isEmpty()) {
          throw new NotFoundException("Nessun gruppo contenuto nella variabile di processo");
        }


        cosmoRUtenteGruppoTagRepository
        .findAll(
            CosmoRUtenteGruppoTagSpecifications.findAllActiveByGruppiAndTag(res, tag.getId()))
        .forEach(elem -> usersId.add(elem.getCosmoTUtenteGruppo().getIdUtente()));



      } else {

        List<CosmoRUtenteGruppoTag> rugts = cosmoRUtenteGruppoTagRepository
            .findActiveByField(CosmoRUtenteGruppoTag_.cosmoTTag, tag);

        rugts.stream().filter(elem -> elem.getCosmoTUtenteGruppo().getDtCancellazione() == null)
        .forEach(filtered -> usersId.add(filtered.getCosmoTUtenteGruppo().getIdUtente()));

      }

    });

    if (usersId.isEmpty()) {
      throw new NotFoundException("Errore in fase di assegnazione: utenti non presenti per il/i tag specificati");
    }

    usersId.stream().distinct().forEach(ui -> {
      var assegnazione = new CosmoRAttivitaAssegnazione();
      assegnazione.setCosmoTAttivita(attivita);
      assegnazione.setAssegnatario(Boolean.TRUE);
      assegnazione.setDtInizioVal(Timestamp.from(Instant.now()));
      assegnazione.setIdUtente(ui.intValue());

      ret.add(assegnazione);
    });

    return ret;

  }

  @Override
  public void avanzaProcesso(AvanzamentoProcessoRequest body) {
    final var methodName = "avanzaProcesso";
    CommonUtils.require(body, "body");
    CommonUtils.require(body.getIdPratica(), "idPratica");
    if (null == body.getIdentificativoEvento() || StringUtils.isBlank(body.getIdentificativoEvento())) {
      String errore = "identificativo evento non trovato";
      logger.error(methodName, errore);
      throw new InternalServerException(errore);
    }

    // ricerco il processo con businessKey=idPratica
    var processInstanceWrapper = cmmnFeignClient.getProcessInstancesByBusinessKey(body.getIdPratica().toString());

    if (null != processInstanceWrapper && null != processInstanceWrapper.getData()) {
      if (processInstanceWrapper.getTotal() != 1) {
        String errore = String.format(
            "Sono stati trovati %d processi per l'id pratica %d. Deve esisterne soltanto 1 attivo",
            processInstanceWrapper.getTotal(), body.getIdPratica());
        logger.error(methodName, errore);
        throw new InternalServerException(errore);
      } else {
        // deve esistere solo un processo attivo per ogni pratica
        String processInstanceId =
            processInstanceWrapper.getData().stream().findFirst().orElseThrow().getId();

        // ricerco la execution afferente al processo
        var executionWrapper =
            cmmnFeignClient.getExecutions(body.getIdentificativoEvento(), processInstanceId);

        if (null == executionWrapper || executionWrapper.getData() == null) {
          String errore = String.format("Nessuna execution esistente per il processo con id %s",
              processInstanceId);
          logger.error(methodName, errore);
          throw new InternalServerException(errore);
        }
        // invio il messaggio al processo con EVENT = identificativoEvento
        var executionActionRequest = new ExecutionActionRequest();
        executionActionRequest.setAction(MESSAGE_EVENT_RECEIVED_ACTION);
        executionActionRequest.setMessageName(body.getIdentificativoEvento());

        executionWrapper.getData().stream().forEach(execution -> {
          cmmnFeignClient.putExecution(execution.getId(), executionActionRequest);
        });

        logger.info(methodName,
            String.format("Segnale %s correttamente inviato al processo relativo alla pratica %d",
                body.getIdentificativoEvento(), body.getIdPratica()));
      }
    } else {
      String errore =
          String.format("Nessun processo esistente per la pratica con id %d", body.getIdPratica());
      logger.error(methodName, errore);
      throw new InternalServerException(errore);
    }
  }
}