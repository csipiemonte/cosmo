/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.batch.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.flowable.rest.service.api.runtime.process.ExecutionActionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoDStatoCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoDStatoCallbackFruitore_;
import it.csi.cosmo.common.entities.CosmoTCallbackFruitore;
import it.csi.cosmo.common.entities.CosmoTCallbackFruitore_;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.enums.OperazioneFruitore;
import it.csi.cosmo.common.entities.enums.StatoCallbackFruitore;
import it.csi.cosmo.common.entities.proto.CosmoTEntity_;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.batch.InvioCallbackBatch;
import it.csi.cosmo.cosmobusiness.business.service.CallbackService;
import it.csi.cosmo.cosmobusiness.business.service.LockService;
import it.csi.cosmo.cosmobusiness.business.service.impl.LockServiceImpl.LockAcquisitionRequest;
import it.csi.cosmo.cosmobusiness.config.ParametriApplicativo;
import it.csi.cosmo.cosmobusiness.dto.EsitoTentativoInvioCallback;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTCallbackFruitoreRepository;
import it.csi.cosmo.cosmobusiness.integration.rest.CosmoCmmnFeignClient;

@Service
public class InvioCallbackBatchImpl extends ParentBatchImpl implements InvioCallbackBatch {

  /**
   *
   */
  private static final String PARAMETRO_ID_PRATICA = "idPratica";

  /**
   * numero massimo di callback da elaborare per ogni iterazione del batch
   */
  private static final int MAX_ELEMENTS_FOR_EXECUTION = 100;

  /**
   * numero massimo di thread/chiamate parallele per operazioni con possibile esecuzione concorrente
   */
  private static final int MAX_PARALLEL_EXECUTIONS = 10;

  /**
   * numero massimo di thread/chiamate parallele per operazioni con possibile esecuzione concorrente
   */
  private static final long MAX_EXECUTION_TIME = 4 * 60 * 1000L;

  private static final String MESSAGE_EVENT_RECEIVED_ACTION = "messageEventReceived";

  private static final String EVENTO_INVIO_RIUSCITO_SUFFIX = "_OK";

  private static final String EVENTO_INVIO_FALLITO_SUFFIX = "_FALLITO";

  private static final String EVENTO_INVIO_TERMINATO_SUFFIX = "_TERMINATO";
  
  private static final String JOB_LOCK_RESOURCE_CODE = "INVIO_CALLBACK_JOB_LOCK";

  @Autowired
  private CosmoTCallbackFruitoreRepository cosmoTCallbackFruitoreRepository;

  @Autowired
  private CosmoCmmnFeignClient cmmnClient;

  @Autowired
  private CallbackService callbackService;

  @Autowired
  private EntityManager em;
  
  @Autowired
  private LockService lockService;

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_INVIO_CALLBACK_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public synchronized void execute(BatchExecutionContext context) {
    String method = "execute";

    logger.info(method, "esecuzione batch di invio callback");
    
    this.invioCallback(context);
    
    logger.info(method, "termine batch di invio callback");
  }

  private Map<String, List<CosmoTCallbackFruitore>> invioCallback(BatchExecutionContext context) {
  //@formatter:off
    return this.lockService.executeLocking(lock -> invioCallbackLock(lock, context),
        LockAcquisitionRequest.builder()
        .withCodiceRisorsa(JOB_LOCK_RESOURCE_CODE)
        .withRitardoRetry(500L)
        .withTimeout(2000L)
        .withDurata(5 * 60 * 1000L)
        .build()
        );
    //@formatter:on
    
  }
  
  private Map<String, List<CosmoTCallbackFruitore>> invioCallbackLock(CosmoTLock lock, BatchExecutionContext context) {
    
    String method = "invioCallbackLock";
    
    if (lock == null) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di invio callback senza un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE);
    }

    if (lock.cancellato()
        || (lock.getDtScadenza() != null && lock.getDtScadenza().before(new Date()))) {
      throw new ConflictException(
          "Non e' consentito avviare la procedura di invio callback con un lock sulla risorsa "
              + JOB_LOCK_RESOURCE_CODE + " gia' scaduto");
    }
    
    Map<String, List<CosmoTCallbackFruitore>> raggruppati = new HashMap<String, List<CosmoTCallbackFruitore>>();
    
    Page<CosmoTCallbackFruitore> daInviare = getCallbackDaInviare();
    if (daInviare.getNumberOfElements() < 1) {
      return raggruppati;
    }

    if (daInviare.getTotalElements() > daInviare.getNumberOfElements()) {
      // troppi in coda. avviso assistenza
      segnalaPossibileSovraccaricoCoda(daInviare, context);
    }

    raggruppati =
        raggruppaCallbackPerOperazione(daInviare.getContent());
    if (raggruppati.isEmpty()) {
      return raggruppati;
    }

    // crea un executor con N thread dove N = numero di differenti operations da gestire
    ExecutorService executor = Executors.newFixedThreadPool(raggruppati.size());

    raggruppati.entrySet()
        .forEach(entry -> executor.submit(() -> elaboraCodaPerOperazione(entry.getKey(),
            entry.getValue().stream().map(CosmoTCallbackFruitore::getId)
                .collect(Collectors.toCollection(LinkedList::new)),
            context)));

    // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
    executor.shutdown();
    try {
      if (!executor.awaitTermination(MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      logger.warn(method,
          "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
      Thread.currentThread().interrupt();
    }
    return raggruppati;
    
  }

  /**
   * invia segnalazione all'assistenza per verificare possibile sovraccarico della coda
   *
   * @param daInviare
   * @param context
   */
  protected void segnalaPossibileSovraccaricoCoda(Page<CosmoTCallbackFruitore> daInviare,
      BatchExecutionContext context) {

    context.reportWarning(String.format(
        "Ci sono %d messaggi in attesa di elaborazione, la coda ne elabora solamente %d alla volta e potrebbe essere sovraccarica.",
        daInviare.getTotalElements(), MAX_ELEMENTS_FOR_EXECUTION));
  }

  /**
   * elabora una coda di callback in attesa relativi ad una singola operazione
   *
   * delega l'esecuzione ad un esecutore diverso a seconda dell'operazione
   *
   * @param string
   * @param listaIdCallback - ricevo in input gli ID perche' la transazione e' separata dal
   *        chiamante ed e' meglio non usare entities
   */
  protected void elaboraCodaPerOperazione(String operazioneRaw, List<Long> listaIdCallback,
      BatchExecutionContext context) {
    final var method = "elaboraCodaPerOperazione";
    logger.info(method, "elaboro coda per operazione {}", operazioneRaw);

    // elaboro ogni coda in una transazione a se

    OperazioneFruitore operazione = OperazioneFruitore.valueOf(operazioneRaw);
    if (operazione == OperazioneFruitore.RICEZIONE_STATO_PRATICA) {
      elaboraCodaRicezioneStatoPratica(listaIdCallback, context);
    } else if (operazione == OperazioneFruitore.CALLBACK_INVIO_SEGNALE) {
      elaboraCodaRicezioneSegnale(listaIdCallback, context);
    } else if (operazione == OperazioneFruitore.CALLBACK_ESITO_DOC_LINK) {
      elaboraCodaEsitoDocLink(listaIdCallback, context);
    } else {
      throw new InternalServerException("Operazione fruitore " + operazione.name()
          + " non gestita dal batch di scodamento ed invio callback");
    }

    logger.info(method, "fine elaborazione coda per operazione {}", operazioneRaw);
  }

  /**
   * elabora una coda di callback per invio stato pratica
   *
   * @param listaIdCallback - ricevo in input gli ID perche' la transazione e' separata dal
   *        chiamante ed e' meglio non usare entities
   */
  protected void elaboraCodaRicezioneStatoPratica(List<Long> listaIdCallback,
      BatchExecutionContext context) {
    final var method = "elaboraCodaRicezioneStatoPratica";
    logger.info(method, "elaboro coda per invio stato pratica con {} elementi",
        listaIdCallback.size());

    // elaboro ogni coda in una transazione a se
    var r = transactionService.inTransaction(() -> {

      // ottengo lista delle entities ordinate per data inserimento DECRESCENTE
      List<CosmoTCallbackFruitore> entities = cosmoTCallbackFruitoreRepository.findByIdIn(
          listaIdCallback, new Sort(Direction.DESC, CosmoTEntity_.dtInserimento.getName()));

      // raggruppo per ID PRATICA
      Map<String, List<CosmoTCallbackFruitore>> raggruppatePerPratica = entities.stream().collect(
          Collectors.groupingBy(e -> e.getParametri().get(PARAMETRO_ID_PRATICA).toString()));

      // crea un executor con N thread dove N = numero di differenti pratiche da gestire fino ad un
      // massimo di MAX_PARALLEL_EXECUTIONS
      ExecutorService executor = Executors
          .newFixedThreadPool(Math.min(MAX_PARALLEL_EXECUTIONS, raggruppatePerPratica.size()));

      raggruppatePerPratica.entrySet().forEach(entry -> executor.submit(
          () -> elaboraCodaRicezioneStatoPraticaPerSingolaPratica(Long.valueOf(entry.getKey()),
              entry.getValue(), context)));

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        logger.warn(method,
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }
    });

    if (r.failed()) {
      logger.error(method, "errore nell'elaborazione coda per invio stato pratica", r.getError());
    }

    logger.info(method, "fine elaborazione coda per invio stato pratica");
  }

  protected void elaboraCodaRicezioneSegnale(List<Long> listaIdCallback,
      BatchExecutionContext context) {
    final var method = "elaboraCodaRicezioneSegnale";
    logger.info(method, "elaboro coda per invio esito ricezione segnale con {} elementi",
        listaIdCallback.size());

    // elaboro ogni coda in una transazione a se
    var r = transactionService.inTransaction(() -> {

      // ottengo lista delle entities ordinate per data inserimento DECRESCENTE
      List<CosmoTCallbackFruitore> entities = cosmoTCallbackFruitoreRepository.findByIdIn(
          listaIdCallback, new Sort(Direction.DESC, CosmoTEntity_.dtInserimento.getName()));

      // raggruppo per ID PRATICA
      Map<String, List<CosmoTCallbackFruitore>> raggruppatePerPratica = entities.stream().collect(
          Collectors.groupingBy(e -> e.getParametri().get(PARAMETRO_ID_PRATICA).toString()));

      // crea un executor con N thread dove N = numero di differenti pratiche da gestire fino ad un
      // massimo di MAX_PARALLEL_EXECUTIONS
      ExecutorService executor = Executors
          .newFixedThreadPool(Math.min(MAX_PARALLEL_EXECUTIONS, raggruppatePerPratica.size()));

      raggruppatePerPratica.entrySet().forEach(entry -> executor
          .submit(() -> elaboraCodaRicezioneSegnalePerSingolaPratica(Long.valueOf(entry.getKey()),
              entry.getValue(), context)));

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        logger.warn(method,
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }
    });

    if (r.failed()) {
      logger.error(method, "errore nell'elaborazione coda per invio esito segnale", r.getError());
    }

    logger.info(method, "fine elaborazione coda per invio esito segnale");
  }

  /**
   * elaboro una coda di callback per invio stato pratica con un singolo ID pratica applicando la
   * logica di override se necessario
   *
   * @param idPratica
   * @param callbacks
   */
  protected void elaboraCodaRicezioneStatoPraticaPerSingolaPratica(Long idPratica,
      List<CosmoTCallbackFruitore> callbacks, BatchExecutionContext context) {
    final var method = "elaboraCodaRicezioneStatoPraticaPerSingolaPratica";
    logger.info(method, "elaboro coda per invio stato per la pratica {} con {} elementi", idPratica,
        callbacks.size());

    // ordina i callback dal piu' recente al meno recente
    callbacks = callbacks.stream()
        .sorted((c1, c2) -> c2.getDtInserimento().compareTo(c1.getDtInserimento()))
        .collect(Collectors.toCollection(LinkedList::new));

    boolean successo = false;
    boolean fallito = false;
    Set<String> codiciSegnali = new HashSet<>();

    // parto ad elaborare dal meno recente
    while (true) {

      // cerco piu' recente da elaborare
      CosmoTCallbackFruitore daElaborare = null;

      for (CosmoTCallbackFruitore candidate : callbacks) {
        if (candidate.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
            || candidate.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())) {
          daElaborare = candidate;
          break;
        }
      }

      if (daElaborare == null) {
        // nessun candidato da elaborare
        break;
      }

      // salvo il segnale richiesto dal callback da elaborare
      if (!StringUtils.isBlank(daElaborare.getCodiceSegnale())) {
        codiciSegnali.add(daElaborare.getCodiceSegnale().trim());
      }

      // elaboro il candidato corrente
      EsitoTentativoInvioCallback esito =
          elaboraSingoloCallbackInvioStatoPratica(idPratica, daElaborare);

      callbacks.remove(daElaborare);

      // se l'invio e' andato bene, annullo i precedenti invii
      if (esito.getStato() == StatoCallbackFruitore.INVIATO) {
        annullaInviiPrecedentiStatoPratica(callbacks, daElaborare);
        successo = true;
      } else if (esito.getStato() == StatoCallbackFruitore.FALLITO) {
        fallito = true;

        context.reportError(String.format("L'invio del callback %s per la pratica %d e' fallito",
            daElaborare.getId(), idPratica), esito.getErrore());

      } else if (esito.getStato() == StatoCallbackFruitore.RISCHEDULATO) {
        context.reportWarning(String.format(
            "Un tentativo di invio del callback %s per la pratica %d e' fallito. Verranno effettuati altri tentativi.",
            daElaborare.getId(), idPratica), esito.getErrore());
      }
    }

    // se nessun codice segnale e' stato esplicitamente richiesto ne costruisco uno implicito
    if (codiciSegnali.isEmpty()) {
      codiciSegnali.add(OperazioneFruitore.RICEZIONE_STATO_PRATICA.name());
    }

    for (String codiceSegnale : codiciSegnali) {
      if (successo) {
        inviaSegnaleProcessoOSegnala(idPratica, codiceSegnale + EVENTO_INVIO_RIUSCITO_SUFFIX,
            context);
      }
      if (fallito) {
        inviaSegnaleProcessoOSegnala(idPratica, codiceSegnale + EVENTO_INVIO_FALLITO_SUFFIX,
            context);
      }
      if (successo || fallito) {
        inviaSegnaleProcessoOSegnala(idPratica, codiceSegnale + EVENTO_INVIO_TERMINATO_SUFFIX,
            context);
      }
    }

    logger.info(method, "fine elaborazione coda per invio stato pratica {}", idPratica);
  }


  protected void elaboraCodaRicezioneSegnalePerSingolaPratica(Long idPratica,
      List<CosmoTCallbackFruitore> callbacks, BatchExecutionContext context) {
    final var method = "elaboraCodaRicezioneSegnalePerSingolaPratica";
    logger.info(method, "elaboro coda per invio esito segnale per la pratica {} con {} elementi",
        idPratica, callbacks.size());

    // ordina i callback dal piu' recente al meno recente
    callbacks = callbacks.stream()
        .sorted((c1, c2) -> c2.getDtInserimento().compareTo(c1.getDtInserimento()))
        .collect(Collectors.toCollection(LinkedList::new));

    // parto ad elaborare dal meno recente
    while (true) {

      // cerco piu' recente da elaborare
      CosmoTCallbackFruitore daElaborare = null;

      for (CosmoTCallbackFruitore candidate : callbacks) {
        if (candidate.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
            || candidate.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())) {
          daElaborare = candidate;
          break;
        }
      }

      if (daElaborare == null) {
        // nessun candidato da elaborare
        break;
      }

      // elaboro il candidato corrente
      EsitoTentativoInvioCallback esito =
          elaboraSingoloCallbackInvioEsitoSegnale(idPratica, daElaborare);

      callbacks.remove(daElaborare);

      // se l'invio e' andato bene, annullo i precedenti invii
      if (esito.getStato() == StatoCallbackFruitore.INVIATO) {
        context.reportInfo(String.format(
            "L'invio del callback %s per l'esito del segnale per la pratica %d e' riuscito",
            daElaborare.getId(), idPratica));

      } else if (esito.getStato() == StatoCallbackFruitore.FALLITO) {

        context.reportError(String.format(
            "L'invio del callback %s per l'esito del segnale per la pratica %d e' fallito",
            daElaborare.getId(), idPratica), esito.getErrore());

      } else if (esito.getStato() == StatoCallbackFruitore.RISCHEDULATO) {
        context.reportWarning(String.format(
            "Un tentativo di invio del callback per l'esito del segnale %s per la pratica %d e' fallito. Verranno effettuati altri tentativi.",
            daElaborare.getId(), idPratica), esito.getErrore());
      }
    }

    logger.info(method, "fine elaborazione coda per invio esito segnale per pratica {}", idPratica);
  }

  protected void elaboraCodaEsitoDocLinkPerSingolaPratica(Long idPratica,
      List<CosmoTCallbackFruitore> callbacks, BatchExecutionContext context) {
    final var method = "elaboraCodaEsitoDocLinkPerSingolaPratica";
    logger.info(method, "elaboro coda per invio esito importazione documenti tramite link per la pratica {} con {} elementi",
        idPratica, callbacks.size());

    // ordina i callback dal piu' recente al meno recente
    callbacks = callbacks.stream()
        .sorted((c1, c2) -> c2.getDtInserimento().compareTo(c1.getDtInserimento()))
        .collect(Collectors.toCollection(LinkedList::new));

    // parto ad elaborare dal meno recente
    while (true) {

      // cerco piu' recente da elaborare
      CosmoTCallbackFruitore daElaborare = null;

      for (CosmoTCallbackFruitore candidate : callbacks) {
        if (candidate.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
            || candidate.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())) {
          daElaborare = candidate;
          break;
        }
      }

      if (daElaborare == null) {
        // nessun candidato da elaborare
        break;
      }

      // elaboro il candidato corrente
      EsitoTentativoInvioCallback esito =
          elaboraSingoloCallbackInvioEsitoDocLink(idPratica, daElaborare);

      callbacks.remove(daElaborare);

      // se l'invio e' andato bene, annullo i precedenti invii
      if (esito.getStato() == StatoCallbackFruitore.INVIATO) {
        context.reportInfo(String.format(
            "L'invio del callback %s per l'invio dell'esito inserimento documenti tramite link per la pratica %d e' riuscito",
            daElaborare.getId(), idPratica));

      } else if (esito.getStato() == StatoCallbackFruitore.FALLITO) {

        context.reportError(String.format(
            "L'invio del callback %s per l'invio dell'esito inserimento documenti tramite link per la pratica %d e' fallito",
            daElaborare.getId(), idPratica), esito.getErrore());

      } else if (esito.getStato() == StatoCallbackFruitore.RISCHEDULATO) {
        context.reportWarning(String.format(
            "Un tentativo di invio del callback (id tentativo: %s) per l'invio dell'esito inserimento documenti tramite link per la pratica %d e' fallito. Verranno effettuati altri tentativi.",
            daElaborare.getId(), idPratica), esito.getErrore());
      }
    }

    logger.info(method, "fine elaborazione coda per l'invio dell'esito inserimento documenti tramite link per pratica {}", idPratica);
  }

  /**
   * invia un segnale ad un processo oppure segnala errore all'assistenza
   *
   * @param idProcesso
   * @param segnale
   */
  protected void inviaSegnaleProcessoOSegnala(Long idPratica, String segnale,
      BatchExecutionContext context) {
    final var method = "inviaSegnaleProcessoOSegnala";

    try {
      logger.info(method, "invio segnale {} per pratica {}", segnale, idPratica);
      inviaSegnaleProcesso(idPratica, segnale);

    } catch (Exception e) {
      logger.error(method,
          "errore nell'invio del segnale " + segnale + " ai processi per la pratica " + idPratica,
          e);

      context.reportError(
          String.format("Errore nell'invio del segnale %s al processo per la pratica %d: %s",
              segnale, idPratica, e.getMessage()),
          e);
    }
  }

  /**
   * annullo gli invii precedenti per la stessa pratica
   *
   * @param callbacks
   * @param attuale
   */
  protected void annullaInviiPrecedentiStatoPratica(List<CosmoTCallbackFruitore> callbacks,
      CosmoTCallbackFruitore attuale) {
    for (CosmoTCallbackFruitore candidate : callbacks) {
      if (!candidate.getId().equals(attuale.getId())
          && candidate.getDtInserimento().before(attuale.getDtInserimento())
          && candidate.getStato().getCodice().equals(StatoCallbackFruitore.SCHEDULATO.name())
          || candidate.getStato().getCodice().equals(StatoCallbackFruitore.IN_CORSO.name())
          || candidate.getStato().getCodice().equals(StatoCallbackFruitore.RISCHEDULATO.name())) {

        annullaSingoloCallbackInvioStatoPratica(candidate);
      }
    }
  }

  /**
   * tenta l'invio di un singolo callback stato pratica
   *
   * @param idPratica
   * @param callback
   * @return
   */
  protected EsitoTentativoInvioCallback elaboraSingoloCallbackInvioStatoPratica(Long idPratica,
      CosmoTCallbackFruitore callback) {

    final var method = "elaboraSingoloCallbackInvioStatoPratica";
    logger.info(method, "invio stato pratica {} per il callback {}", idPratica, callback.getId());

    // devo staccare l'entity dall entity manager altrimenti la transazione separata nel
    // callbackService non puo' scrivere sulla entity
    em.detach(callback);

    // il metodo del CallbackService che tenta l'invio gestisce anche il cambio di stato e ritorna
    // il nuovo stato del callback
    EsitoTentativoInvioCallback esito =
        callbackService.tentaInvioCallbackSchedulato(callback.getId());

    logger.info(method, "esito invio stato pratica {} per il callback {}: {}", idPratica,
        callback.getId(), esito.getEsito().name());

    logger.info(method, "fine invio stato pratica {} per il callback {}", idPratica,
        callback.getId());

    return esito;
  }

  protected EsitoTentativoInvioCallback elaboraSingoloCallbackInvioEsitoSegnale(Long idPratica,
      CosmoTCallbackFruitore callback) {

    final var method = "elaboraSingoloCallbackInvioEsitoSegnale";
    logger.info(method, "invio esito segnale per pratica {} per il callback {}", idPratica,
        callback.getId());

    // devo staccare l'entity dall entity manager altrimenti la transazione separata nel
    // callbackService non puo' scrivere sulla entity
    em.detach(callback);

    // il metodo del CallbackService che tenta l'invio gestisce anche il cambio di stato e ritorna
    // il nuovo stato del callback
    EsitoTentativoInvioCallback esito =
        callbackService.tentaInvioCallbackSchedulato(callback.getId());

    logger.info(method, "esito invio esito segnale per pratica {} per il callback {}: {}",
        idPratica, callback.getId(), esito.getEsito().name());

    logger.info(method, "fine invio esito segnale per pratica {} per il callback {}", idPratica,
        callback.getId());

    return esito;
  }

  protected EsitoTentativoInvioCallback elaboraSingoloCallbackInvioEsitoDocLink(Long idPratica,
      CosmoTCallbackFruitore callback) {

    final var method = "elaboraSingoloCallbackInvioEsitoDocLink";
    logger.info(method,
        "invio esito importazione documenti tramite link per pratica {} per il callback {}",
        idPratica,
        callback.getId());

    // devo staccare l'entity dall entity manager altrimenti la transazione separata nel
    // callbackService non puo' scrivere sulla entity
    em.detach(callback);

    // il metodo del CallbackService che tenta l'invio gestisce anche il cambio di stato e ritorna
    // il nuovo stato del callback
    EsitoTentativoInvioCallback esito =
        callbackService.tentaInvioCallbackSchedulato(callback.getId());

    logger.info(method,
        "invio esito importazione documenti tramite link per pratica {} per il callback {}: {}",
        idPratica, callback.getId(), esito.getEsito().name());

    logger.info(method,
        "fine invio esiti importazione documenti tramite link per la pratica {} per il callback {}",
        idPratica,
        callback.getId());

    return esito;
  }

  protected void annullaSingoloCallbackInvioStatoPratica(CosmoTCallbackFruitore callback) {

    // devo staccare l'entity dall entity manager altrimenti la transazione separata nel
    // callbackService non puo' scrivere sulla entity
    em.detach(callback);

    callbackService.annullaCallbackSchedulato(callback.getId());

    callback.setStato(cosmoTCallbackFruitoreRepository.reference(CosmoDStatoCallbackFruitore.class,
        StatoCallbackFruitore.ANNULLATO.name()));
  }

  /**
   * ricerca tutti i callback in stato SCHEDULATO o RISCHEDULATO per ognuno tenta o ritenta l'invio
   * e aggiorna lo stato a INVIATO, RISCHEDULATO o FALLITO
   *
   * ordina per data inserimento ASC, limita a MAX_ELEMENTS_FOR_EXECUTION
   *
   * @return la lista dei callback da inviare
   */
  protected Page<CosmoTCallbackFruitore> getCallbackDaInviare() {
    final var method = "getCallbackDaInviare";

    Sort sort = new Sort(Direction.ASC, CosmoTEntity_.dtInserimento.getName());

    Pageable pageRequest = new PageRequest(0, MAX_ELEMENTS_FOR_EXECUTION, sort);
    Page<CosmoTCallbackFruitore> callbacks = cosmoTCallbackFruitoreRepository.findAllNotDeleted(
        (Root<CosmoTCallbackFruitore> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
          var joinOperazione = root.join(CosmoTCallbackFruitore_.stato, JoinType.LEFT);

          return joinOperazione.get(CosmoDStatoCallbackFruitore_.codice).in(Arrays.asList(
              StatoCallbackFruitore.SCHEDULATO.name(), StatoCallbackFruitore.RISCHEDULATO.name()));
        }, pageRequest);

    if (!callbacks.getContent().isEmpty()) {
      logger.info(method, "trovati {} callbacks da inviare su {} in totale",
          callbacks.getNumberOfElements(), callbacks.getTotalElements());
    } else {
      logger.debug(method, "nessun callback da inviare");
    }

    return callbacks;
  }

  /**
   * raggruppa i callback per operazione collegata all'endpoint su cui sono richiesti
   *
   * non garantisce l'ordinamento delle entry nella map ne l'ordinamento delle List che ne sono i
   * values
   *
   * @param input
   * @return Map<String, List<CosmoTCallbackFruitore>>
   */
  protected Map<String, List<CosmoTCallbackFruitore>> raggruppaCallbackPerOperazione(
      Collection<CosmoTCallbackFruitore> input) {
    if (input == null) {
      return Collections.emptyMap();
    }
    return input.stream().collect(
        Collectors.groupingBy(callback -> callback.getEndpoint().getOperazione().getCodice()));
  }

  protected void elaboraCodaEsitoDocLink(List<Long> listaIdCallback,
      BatchExecutionContext context) {
    final var method = "elaboraCodaEsitoDocLink";
    logger.info(method,
        "elaboro coda per invio esito importazione documenti con link con {} elementi",
        listaIdCallback.size());

    // elaboro ogni coda in una transazione a se
    var r = transactionService.inTransaction(() -> {

      // ottengo lista delle entities ordinate per data inserimento DECRESCENTE
      List<CosmoTCallbackFruitore> entities = cosmoTCallbackFruitoreRepository.findByIdIn(
          listaIdCallback, new Sort(Direction.DESC, CosmoTEntity_.dtInserimento.getName()));

      // raggruppo per ID PRATICA
      Map<String, List<CosmoTCallbackFruitore>> raggruppatePerPratica = entities.stream().collect(
          Collectors.groupingBy(e -> e.getParametri().get(PARAMETRO_ID_PRATICA).toString()));

      // crea un executor con N thread dove N = numero di differenti pratiche da gestire fino ad un
      // massimo di MAX_PARALLEL_EXECUTIONS
      ExecutorService executor = Executors
          .newFixedThreadPool(Math.min(MAX_PARALLEL_EXECUTIONS, raggruppatePerPratica.size()));

      raggruppatePerPratica.entrySet().forEach(entry -> executor
          .submit(() -> elaboraCodaEsitoDocLinkPerSingolaPratica(Long.valueOf(entry.getKey()),
              entry.getValue(), context)));

      // attendi che l'esecuzione termini per tutti i task in esecuzione parallela
      executor.shutdown();
      try {
        if (!executor.awaitTermination(MAX_EXECUTION_TIME, TimeUnit.MILLISECONDS)) {
          executor.shutdownNow();
        }
      } catch (InterruptedException e) {
        logger.warn(method,
            "executor did not complete in MAX_EXECUTION_TIME, following executions might be delayed");
        Thread.currentThread().interrupt();
      }
    });

    if (r.failed()) {
      logger.error(method, "errore nell'elaborazione coda per invio esito segnale", r.getError());
    }

    logger.info(method, "fine elaborazione coda per invio esito segnale");
  }

  /**
   * invia segnale di avanzamento a processo
   *
   * @param idPratica
   * @param identificativoEvento
   */
  private void inviaSegnaleProcesso(Long idPratica, String identificativoEvento) {
    String methodName = "avanzaProcesso";
    ValidationUtils.assertNotNull(idPratica, PARAMETRO_ID_PRATICA);
    ValidationUtils.assertNotNull(identificativoEvento, "identificativoEvento");

    // ricerco il processo con businessKey=idPratica
    var processInstanceWrapper = cmmnClient.getProcessInstancesByBusinessKey(idPratica.toString());
    if (processInstanceWrapper == null || processInstanceWrapper.getSize() == null
        || processInstanceWrapper.getSize() < 1L) {
      logger.warn(methodName,
          "nessun processo attivo per l'id pratica {}, ricerco tra le istanze terminate",
          idPratica);
      processInstanceWrapper =
          cmmnClient.getHistoricProcessInstancesByBusinessKey(idPratica.toString());
    }

    if (null != processInstanceWrapper && null != processInstanceWrapper.getData()) {
      if (processInstanceWrapper.getTotal() < 1) {
        // nessuna esecuzione in attesa del segnale.
        logger.warn(methodName, "nessun processo per l'id pratica {}", idPratica);

      } else if (processInstanceWrapper.getTotal() != 1) {
        String errore = String.format(
            "Sono stati trovati %d processi per l'id pratica %d. Deve esisterne soltanto 1 attivo",
            processInstanceWrapper.getTotal(), idPratica);
        logger.error(methodName, errore);
        throw new InternalServerException(errore);
      } else {
        // deve esistere solo un processo attivo per ogni pratica
        String processInstanceId =
            processInstanceWrapper.getData().stream().findFirst().orElseThrow().getId();

        // ricerco la execution afferente al processo
        var executionWrapper = cmmnClient.getExecutions(identificativoEvento, processInstanceId);

        if (null == executionWrapper || executionWrapper.getData() == null
            || executionWrapper.getTotal() == null || executionWrapper.getTotal() < 1) {
          logger.debug(methodName, "Nessuna execution esistente per il processo con id %s",
              processInstanceId);
          return;
        }

        executionWrapper.getData().forEach(execution -> {
          // invio il messaggio al processo con EVENT = identificativoEvento
          var executionActionRequest = new ExecutionActionRequest();
          executionActionRequest.setAction(MESSAGE_EVENT_RECEIVED_ACTION);
          executionActionRequest.setMessageName(identificativoEvento);

          cmmnClient.putExecution(execution.getId(), executionActionRequest);
          logger.info(methodName,
              String.format("Segnale %s correttamente inviato al processo relativo alla pratica %d",
                  identificativoEvento, idPratica));
        });

      }
    } else {
      String errore =
          String.format("Nessun processo esistente per la pratica con id %d", idPratica);
      logger.error(methodName, errore);
      throw new InternalServerException(errore);
    }
  }
}
