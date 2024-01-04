/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.common.entities.CosmoTLock_;
import it.csi.cosmo.common.entities.CosmoTUtente;
import it.csi.cosmo.common.exception.ConflictException;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ExceptionUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmosoap.business.service.LockService;
import it.csi.cosmo.cosmosoap.business.service.TransactionService;
import it.csi.cosmo.cosmosoap.dto.LockPolicyConfiguration;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTLockRepository;
import it.csi.cosmo.cosmosoap.integration.repository.CosmoTUtenteRepository;
import it.csi.cosmo.cosmosoap.security.SecurityUtils;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class LockServiceImpl implements LockService {

  private CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private TransactionService transactionService;

  @Autowired
  private CosmoTLockRepository cosmoTLockRepository;

  @Autowired
  private CosmoTUtenteRepository cosmoTUtenteRepository;

  @Autowired(required = false)
  private LockPolicyConfiguration lockPolicyConfiguration;

  @Override
  public void reconfigure(LockPolicyConfiguration policy) {
    this.lockPolicyConfiguration = policy;
  }

  @Override
  public <T> T executeLocking(Function<CosmoTLock, T> task, LockAcquisitionRequest locking) {
    final var method = "executeLocking";

    ValidationUtils.require(task, "task");
    ValidationUtils.require(locking, "locking");

    var lockResult = this.acquire(locking);
    if (!lockResult.acquired || lockResult.lock == null) {
      throw new ConflictException(lockResult.reason);
    }
    try {
      return task.apply(lockResult.lock);
    } finally {
      try {
        this.release(lockResult.lock);
      } catch (Throwable errReleasing) { // NOSONAR
        this.logger.warn(method,
            "error occured releasing lock " + lockResult.lock.getCodiceRisorsa(), errReleasing);
      }
    }
  }

  public Optional<CosmoTLock> peek(String resourceCode) {
    ValidationUtils.require(resourceCode, "resourceCode");
    return this.findActiveLock(resourceCode, true);
  }

  @Override
  public LockAcquisitionAttemptResult acquire(LockAcquisitionRequest request) {
    final var method = "acquire";
    LockAcquisitionAttemptResult lastResponse = null;
    Long waitedFor = 0L;
    Long waitStep = request.ritardoRetry;
    if (waitStep == null) {
      waitStep = 1000L;
    }
    var timedOut = false;

    while (!timedOut) {
      lastResponse = this.withOptionalLocalThreadLocking(() -> this.attemptAcquisition(request));
      if (lastResponse.acquired) {
        break;
      }

      if (request.timeout != null && request.timeout > 0L
          && waitedFor + waitStep <= request.timeout) {
        this.logger.debug(method, "waiting {} ms before reattempting lock acquisition", waitStep);
        this.sleep(waitStep);
        waitedFor += waitStep;

      } else {
        this.logger.debug(method, "waiting for lock acquisition timed out after {} ms", waitedFor);
        timedOut = true;
      }
    }

    var out = new LockAcquisitionAttemptResult();
    out.acquired = false;
    out.acquired = lastResponse.acquired;
    out.lockedBySomeoneElse = lastResponse.lockedBySomeoneElse;
    out.renewed = lastResponse.renewed;
    out.reason = lastResponse.reason;
    out.lock = lastResponse.lock;
    out.timedOut = timedOut;
    out.waitedFor = waitedFor;
    return out;

  }

  @Override
  public boolean release(CosmoTLock request) {
    return this.withOptionalLocalThreadLocking(() -> this.attemptRelease(request));
  }

  private <T> T withOptionalLocalThreadLocking(Supplier<T> task) {
    final var method = "withOptionalLocalThreadLocking";

    LockPolicyConfiguration policy = getEffectivePolicy();
    if (policy.synchronizeLocalThreads()) {
      synchronized (LockServiceImpl.class) {
        return task.get();
      }
    } else {
      logger.debug(method, "skipped local thread locking as of policy configuration");
      return task.get();
    }
  }

  /**
   * tento di rilasciare un lock attivo.
   *
   * soft fail in caso di lock gia' scaduto, ConflictException in caso di lock posseduto da altri
   * utenti.
   *
   * @param body
   */
  private boolean attemptRelease(CosmoTLock body) {
    final var method = "attemptLockRelease";
    logger.debug(method, "tento rilascio del lock per la risorsa {} e owner {}",
        body.getCodiceRisorsa(), body.getCodiceOwner());

    Optional<CosmoTLock> lockRaw = findActiveLock(body.getCodiceRisorsa(), false);

    if (lockRaw.isEmpty()) {
      logger.warn(method, "nessun lock attivo per la risorsa {} e owner {} da rilasciare",
          body.getCodiceRisorsa(), body.getCodiceOwner());
      return false;
    }

    sleep(getEffectivePolicy().delayBeforeRelease());

    CosmoTLock lock;
    if (getEffectivePolicy().synchronizeDatabaseRowOnRelease()) {
      lock = cosmoTLockRepository.lock(lockRaw.get().getId());
      if (lock == null) {
        logger.warn(method,
            "race condition nel rilascio del lock attivo per la risorsa {} e owner {}. il lock e' gia' stato rilasciato da un altro thread",
            body.getCodiceRisorsa(), body.getCodiceOwner());
        return false;
      }
    } else {
      logger.debug(method, "skipped database row locking on release as of policy configuration");
      lock = lockRaw.get();
    }

    if (!lock.getCodiceOwner().equals(body.getCodiceOwner())) {
      // lock attivo ma posseduto da altri
      throw new ConflictException(
          "Rilascio di un lock posseduto da un altro utente non consentito");
    }

    // lock attivo e posseduto dall'owner corretto, procedo al rilascio

    try {
      cosmoTLockRepository.delete(lock);
      sleep(getEffectivePolicy().delayBeforeReleaseFlush());
      cosmoTLockRepository.flush();
      return true;
    } catch (Exception e) {
      logger.warn(method, "errore nel rilascio di un lock attivo per la risorsa {} e owner {}: {}",
          body.getCodiceRisorsa(), body.getCodiceOwner(), e.getMessage());
      throw e;
    }
  }

  /**
   * tento di acquisire il lock in una transazione isolata.
   *
   * gestisce applicativamente le race conditions in caso di acquisizione concorrente con altre
   * istanze dell'applicativo
   *
   * @param body
   * @return
   */
  private LockAcquisitionAttemptResult attemptAcquisition(LockAcquisitionRequest body) {
    ValidationUtils.require(body, "body");
    ValidationUtils.require(body.codiceRisorsa, "codiceRisorsa");
    ValidationUtils.require(body.durata, "durata");

    // tento l'acquisizione del lock in una transazione controllata manualmente
    // per poter gestire gli edge cases di race conditions
    var result = transactionService.inTransaction(() -> attemptAcquisitionInsideTransaction(body));

    // lock acquisito e gia' mappato
    if (result.success()) {
      return result.getResult();
    }

    if (result.getError() instanceof org.springframework.dao.DataIntegrityViolationException) {
      // race condition nell'inserimento del lock. il thread non è riuscito ad acquisire il lock
      var out = new LockAcquisitionAttemptResult();
      out.acquired = false;
      out.lock = null;
      out.lockedBySomeoneElse = true;
      out.renewed = false;
      out.reason = "Lock per la risorsa " + body.codiceRisorsa
          + " gia' acquisito da un altro utente durante il tentativo di acquisizione.";
      return out;

    } else if (result.getError() instanceof RuntimeException) {
      throw (RuntimeException) result.getError();
    } else {
      throw new InternalServerException("Errore imprevisto nell'acquisizione del lock",
          result.getError());
    }
  }

  /**
   * tenta di acquisire un lock.
   *
   * se il lock e' gia' acquisito da un altro utente lancia ConflictException.
   *
   * se il lock e' gia' acquisito dallo stesso utente lo rinnova/estende se necessario e lo ritorna.
   *
   * se nessun lock sulla risorsa e' attivo, ne inserisce uno nuovo e lo ritorna.
   *
   * @param body
   * @return
   */
  private LockAcquisitionAttemptResult attemptAcquisitionInsideTransaction(
      LockAcquisitionRequest body) {
    final var method = "attemptAcquisition";

    if (StringUtils.isBlank(body.codiceOwner)) {
      body.codiceOwner = UUID.randomUUID().toString();
    }

    logger.debug(method, "tento acquisizione del lock per la risorsa {} e owner {}",
        body.codiceRisorsa, body.codiceOwner);

    Optional<CosmoTLock> existing = findActiveLock(body.codiceRisorsa, false);

    if (existing.isPresent()) {
      if (existing.get().getCodiceOwner().equals(body.codiceOwner)) {
        // lock already owned, refreshing
        logger.debug(method, "lock per la risorsa {} gia' attivo per l'owner {}",
            body.codiceRisorsa, body.codiceOwner);
        var renewed = renew(existing.get(), body.durata);

        var out = new LockAcquisitionAttemptResult();
        out.acquired = true;
        out.lock = renewed;
        out.lockedBySomeoneElse = false;
        out.renewed = true;
        return out;

      } else {
        // lock owned by another owner
        var otherUser = estraiUtenteDaDatiTecnici(existing.get().getUtenteInserimento());

        var out = new LockAcquisitionAttemptResult();
        out.acquired = false;
        out.lock = null;
        out.lockedBySomeoneElse = true;
        out.renewed = false;
        out.reason = "Lock per la risorsa " + body.codiceRisorsa + " gia' acquisito da "
            + (otherUser.isPresent()
                ? otherUser.get().getNome() + " " + otherUser.get().getCognome()
                    : "un altro utente.");
        return out;
      }
    }

    sleep(getEffectivePolicy().delayBeforeAcquisition());

    // creo nuovo lock
    CosmoTLock newLock = new CosmoTLock();
    newLock.setCodiceOwner(body.codiceOwner);
    newLock.setCodiceRisorsa(body.codiceRisorsa);
    newLock.setDtScadenza(Timestamp.from(Instant.now().plusMillis(body.durata)));

    // puo' lanciare eccezione in caso di race conditions.
    // richiedo il flush per intercettarle il prima possibile.
    newLock = cosmoTLockRepository.save(newLock);

    sleep(getEffectivePolicy().delayBeforeAcquisitionFlush());
    cosmoTLockRepository.flush();

    logger.info(method, "acquisito nuovo lock per la risorsa {} per l'owner {} con id {}",
        body.codiceRisorsa, body.codiceOwner, newLock.getId());

    var out = new LockAcquisitionAttemptResult();
    out.acquired = true;
    out.lock = newLock;
    out.lockedBySomeoneElse = false;
    out.renewed = false;
    return out;
  }

  /**
   * estende la durata di un lock esistente e ancora valido.
   *
   * @param existing
   * @param newDuration
   * @return
   */
  private CosmoTLock renew(CosmoTLock existing, Long newDuration) {
    final var method = "renew";

    Timestamp newExpirationDate = Timestamp.from(Instant.now().plusMillis(newDuration));

    // rinnovo il lock scrivendo su DB solo se la nuova data di scadenza e' successiva alla
    // precedente
    if (existing.getDtScadenza() != null && existing.getDtScadenza().before(newExpirationDate)) {

      logger.info(method,
          "estendo scadenza del lock per la risorsa {} gia' attivo per l'owner {} a {}",
          existing.getCodiceRisorsa(), existing.getCodiceOwner(), newExpirationDate);

      existing.setDtScadenza(newExpirationDate);
      cosmoTLockRepository.save(existing);
    }

    return existing;
  }

  /**
   * ricerca i lock attivi per il codice risorsa specificato.
   *
   * se readOnly = false, i lock scaduti vengono eliminati fisicamente dal database.
   *
   * @param codiceRisorsa
   * @param readOnly
   * @return
   */
  private Optional<CosmoTLock> findActiveLock(String codiceRisorsa, boolean readOnly) {
    final var method = "findActiveLock";

    logger.debug(method, "ricerco i lock attivi per la risorsa {}", codiceRisorsa);
    List<CosmoTLock> lockAttivi =
        cosmoTLockRepository.findNotDeletedByField(CosmoTLock_.codiceRisorsa, codiceRisorsa);

    if (lockAttivi.isEmpty()) {
      logger.debug(method, "nessun lock attivo per la risorsa {}", codiceRisorsa);
      return Optional.empty();
    }

    logger.debug(method, "trovati {} lock per la risorsa {}", lockAttivi.size(), codiceRisorsa);

    Timestamp now = Timestamp.from(Instant.now());
    CosmoTLock valido = null;
    for (var lock : lockAttivi) {
      if (!scaduto(lock, now)) {
        logger.debug(method, "trovato lock attivo per la risorsa {}", codiceRisorsa);
        if (valido == null) {
          valido = lock;
        } else {
          // non *dovrebbe* mai succedere. se succede qualcuno ha dimenticato una unique key sulla
          // tabella.
          throw new InternalServerException("Lock attivi multipli per la risorsa " + codiceRisorsa);
        }
      } else if (!readOnly) {
        // se non sono in modalita' read-only elimino i record scaduti oltre a non considerarli
        processExpiredLock(lock);
      }
    }

    return Optional.ofNullable(valido);
  }

  /**
   * estrae l'utente che corrisponde ad una stringa di tracciamento dei dati tecnici.
   *
   * la stringa puo' essere in formato "APPLICATIVO" oppure "UTENTE@APPLICATIVO", estraggo solo nel
   * secondo caso.
   *
   * @param raw
   * @return
   */
  private Optional<CosmoTUtente> estraiUtenteDaDatiTecnici(String raw) {
    if (StringUtils.isBlank(raw) || !raw.contains("@")) {
      return Optional.empty();
    }
    CosmoTUtente utente = cosmoTUtenteRepository.findByCodiceFiscale(raw.split("\\@")[0]);
    return Optional.ofNullable(utente);
  }

  /**
   * controlla se un record e' scaduto al timestamp specificato.
   *
   * @param entity
   * @param at
   * @return
   */
  private boolean scaduto(CosmoTLock entity, Timestamp at) {
    return entity.getDtScadenza() != null && !entity.getDtScadenza().after(at);
  }

  /**
   * elimina fisicamente un record.
   *
   * esegue un temporaneo update della data cancellazione per evitare errori di integrita' in fase
   * di commit.
   *
   * @param entity
   */
  private void processExpiredLock(CosmoTLock entity) {
    final var method = "processExpiredLock";
    logger.info(method, "elimino fisicamente lock scaduto per la risorsa {} con id {}",
        entity.getCodiceRisorsa(), entity.getId());

    entity.setDtCancellazione(Timestamp.from(Instant.now()));
    entity.setUtenteCancellazione(SecurityUtils.getUtenteCorrente().getCodiceFiscale());
    cosmoTLockRepository.save(entity);

    cosmoTLockRepository.delete(entity);
    cosmoTLockRepository.flush();
  }

  protected void sleep(long duration) {
    if (duration <= 0) {
      return;
    }
    try {
      logger.debug("sleep", "sleeping for {} ms", duration);
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw ExceptionUtils.toChecked(e);
    }
  }

  protected synchronized LockPolicyConfiguration getEffectivePolicy() {
    if (this.lockPolicyConfiguration == null) {
      LockPolicyConfiguration newPolicy = new LockPolicyConfiguration() {

        @Override
        public boolean synchronizeLocalThreads() {
          return true;
        }

        @Override
        public boolean synchronizeDatabaseRowOnRelease() {
          return true;
        }
      };

      this.lockPolicyConfiguration = newPolicy;
    }
    return this.lockPolicyConfiguration;
  }


  public static class LockAcquisitionRequest {
    String codiceRisorsa;
    String codiceOwner;
    Long durata;
    Long timeout;
    Long ritardoRetry;

    private LockAcquisitionRequest(Builder builder) {
      this.codiceRisorsa = builder.codiceRisorsa;
      this.codiceOwner = builder.codiceOwner;
      this.durata = builder.durata;
      this.timeout = builder.timeout;
      this.ritardoRetry = builder.ritardoRetry;
    }

    public LockAcquisitionRequest() {}

    /**
     * Creates builder to build {@link LockAcquisitionRequest}.
     *
     * @return created builder
     */
    public static Builder builder() {
      return new Builder();
    }

    /**
     * Builder to build {@link LockAcquisitionRequest}.
     */
    public static final class Builder {
      private String codiceRisorsa;
      private String codiceOwner;
      private Long durata;
      private Long timeout;
      private Long ritardoRetry;

      private Builder() {}

      public Builder withCodiceRisorsa(String codiceRisorsa) {
        this.codiceRisorsa = codiceRisorsa;
        return this;
      }

      public Builder withCodiceOwner(String codiceOwner) {
        this.codiceOwner = codiceOwner;
        return this;
      }

      public Builder withDurata(Long durata) {
        this.durata = durata;
        return this;
      }

      public Builder withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
      }

      public Builder withRitardoRetry(Long ritardoRetry) {
        this.ritardoRetry = ritardoRetry;
        return this;
      }

      public LockAcquisitionRequest build() {
        return new LockAcquisitionRequest(this);
      }
    }

  }

  public static class LockAcquisitionAttemptResult {
    boolean acquired;
    CosmoTLock lock;
    String reason;
    boolean lockedBySomeoneElse;
    boolean renewed;
    boolean timedOut;
    Long waitedFor;

    public boolean isAcquired() {
      return acquired;
    }

    public CosmoTLock getLock() {
      return lock;
    }

    public String getReason() {
      return reason;
    }

    public boolean isLockedBySomeoneElse() {
      return lockedBySomeoneElse;
    }

    public boolean isRenewed() {
      return renewed;
    }

    public boolean isTimedOut() {
      return timedOut;
    }

    public Long getWaitedFor() {
      return waitedFor;
    }

  }

}
