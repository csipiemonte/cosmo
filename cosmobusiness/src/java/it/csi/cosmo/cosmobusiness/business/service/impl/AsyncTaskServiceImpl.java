/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.LongTaskStepExecutor;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTask.LongTaskStatus;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.async.store.AbstractLongTaskDatabaseStore;
import it.csi.cosmo.common.async.store.LongTaskStorageAdapter;
import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona;
import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona_;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.exception.NotFoundException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.common.util.ValidationUtils;
import it.csi.cosmo.cosmobusiness.business.service.AsyncTaskService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmobusiness.integration.mapper.OperazioniAsincroneMapper;
import it.csi.cosmo.cosmobusiness.integration.repository.CosmoTOperazioneAsincronaRepository;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

/**
 *
 */

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService, InitializingBean, DisposableBean {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  protected TransactionService transactionService;

  @Autowired
  protected CosmoTOperazioneAsincronaRepository cosmoTOperazioneAsincronaRepository;

  @Autowired
  protected OperazioniAsincroneMapper operazioniAsincroneMapper;

  private LongTaskExecutor executor;

  @Override
  public <T extends Serializable> LongTaskFuture<T> start(String name,
      Function<LongTask<T>, T> handler) {

    LongTaskFuture<T> taskFuture = this.executor.start(name, handler);

    return taskFuture;
  }

  @Override
  public Future<LongTaskPersistableEntry> watcher(String uuid, Long timeoutSeconds) {
    return this.executor.watch(uuid, timeoutSeconds);
  }

  @Override
  public LongTaskPersistableEntry wait(String uuid, Long timeoutSeconds) {
    try {
      return this.executor.watch(uuid, timeoutSeconds).get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new InternalServerException("Attesa del task asincrono interrotta forzatamente", e);
    } catch (ExecutionException e) {
      if (null == e.getCause()) {
        throw new InternalServerException("Il task asincrono di cui si era in attesa e' fallito",
            e);
      }
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      } else {
        throw new InternalServerException("Il task asincrono di cui si era in attesa e' fallito",
            e.getCause());
      }
    }
  }

  @Override
  public LongTaskExecutor getExecutor() {
    return this.executor;
  }

  @Transactional
  @Override
  public void deleteOperazioneAsincrona(String uuid) {
    this.executor.deleteDelayed(uuid);
  }

  @Transactional(readOnly = true)
  @Override
  public OperazioneAsincrona getOperazioneAsincrona(String uuid) {
    var entity = this.executor.get(uuid);
    if (entity == null) {
      throw new NotFoundException("Operazione con uuid " + uuid + " non trovata");
    }
    return operazioniAsincroneMapper.toDTO(entity);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    //@formatter:off
    this.executor = LongTaskExecutor.builder()
      .withLogger(this.logger)
      .withStorageAdapter(storageAdapter())
      .withTaskExecutor(stepExecutor())
      .build();
    //@formatter:on
  }

  @Override
  public void destroy() throws Exception {
    if (this.executor != null) {
      this.executor.close();
    }
  }

  private LongTaskStepExecutor stepExecutor() {
    return new LongTaskStepExecutor() {
      @Override
      public <T extends Serializable> T apply(Function<LongTask<T>, T> executor, LongTask<T> task) {
        if (task.getParent() == null) {
          // no transaction for root task
          return executor.apply(task);
        } else {
          return transactionService.inTransactionOrThrow(() -> executor.apply(task));
        }
      }
    };
  }

  private LongTaskStorageAdapter storageAdapter() {
    return new AbstractLongTaskDatabaseStore(logger) {

      @Override
      protected CosmoTOperazioneAsincrona persist(CosmoTOperazioneAsincrona entity) {
        var res = transactionService.inTransaction(() -> {

          var existing = cosmoTOperazioneAsincronaRepository
              .findOneByField(CosmoTOperazioneAsincrona_.uuid, entity.getUuid());
          if (existing.isPresent()) {
            entity.setId(existing.get().getId());
            entity.setDtInserimento(existing.get().getDtInserimento());
            entity.setUtenteInserimento(existing.get().getUtenteInserimento());
            entity.setDtUltimaModifica(existing.get().getDtUltimaModifica());
            entity.setUtenteUltimaModifica(existing.get().getUtenteUltimaModifica());
          }
          return cosmoTOperazioneAsincronaRepository.saveAndFlush(entity);
        });
        if (res.success()) {
          return res.getResult();
        }
        throw new InternalServerException(res.getError().getMessage(), res.getError());
      }

      @Override
      protected CosmoTOperazioneAsincrona getByUUID(String uuid) {
        var res = transactionService.inTransaction(() -> cosmoTOperazioneAsincronaRepository
            .findOneByField(CosmoTOperazioneAsincrona_.uuid, uuid));
        if (res.success()) {
          return res.getResult().orElse(null);
        }
        throw new InternalServerException(res.getError().getMessage(), res.getError());
      }

      @Override
      protected void deleteByUUID(String uuid) {
        var res = transactionService.inTransaction(() -> cosmoTOperazioneAsincronaRepository
            .deleteByField(CosmoTOperazioneAsincrona_.uuid, uuid));
        if (res.failed()) {
          throw new InternalServerException(res.getError().getMessage(), res.getError());
        }
      }
    };
  }

  @Transactional
  @Override
  public OperazioneAsincrona postOperazioneAsincrona(CreaOperazioneAsincronaRequest body) {
    ValidationUtils.validaAnnotations(body);

    CosmoTOperazioneAsincrona entity = new CosmoTOperazioneAsincrona();
    entity.setDataAvvio(Timestamp.from(body.getDataAvvio().toInstant()));
    entity.setStato(LongTaskStatus.valueOf(body.getStato()));
    entity.setUuid(UUID.randomUUID().toString());
    entity.setVersione(body.getVersione());

    LongTaskPersistableEntry metadati = new LongTaskPersistableEntry();

    metadati.setUuid(entity.getUuid());
    metadati.setName(body.getNome());
    metadati.setStartedAt(body.getDataAvvio());
    metadati.setStatus(entity.getStato());
    metadati.setVersion(entity.getVersione());

    entity.setMetadati(metadati);

    cosmoTOperazioneAsincronaRepository.save(entity);

    return operazioniAsincroneMapper.toDTO(metadati);
  }

  @Transactional
  @Override
  public OperazioneAsincrona putOperazioneAsincrona(String uuid, OperazioneAsincrona body) {
    ValidationUtils.validaAnnotations(body);
    CosmoTOperazioneAsincrona entity = cosmoTOperazioneAsincronaRepository
        .findOneByField(CosmoTOperazioneAsincrona_.uuid, uuid).orElse(null);

    if (entity == null) {
      entity = new CosmoTOperazioneAsincrona();
    }

    entity.setDataAvvio(Timestamp.from(body.getDataAvvio().toInstant()));
    entity.setStato(LongTaskStatus.valueOf(body.getStato()));
    entity.setUuid(body.getUuid());
    entity.setVersione(body.getVersione());
    entity.setDataFine(
        body.getDataFine() != null ? Timestamp.from(body.getDataFine().toInstant()) : null);

    LongTaskPersistableEntry metadati = map(body);
    entity.setMetadati(metadati);

    cosmoTOperazioneAsincronaRepository.save(entity);

    return operazioniAsincroneMapper.toDTO(metadati);
  }

  private LongTaskPersistableEntry map(OperazioneAsincrona entity) {
    LongTaskPersistableEntry metadati = new LongTaskPersistableEntry();

    metadati.setUuid(entity.getUuid());
    metadati.setName(entity.getNome());
    metadati.setStartedAt(entity.getDataAvvio());
    metadati.setStatus(LongTaskStatus.valueOf(entity.getStato()));
    metadati.setVersion(entity.getVersione());

    metadati.setErrorDetails(entity.getDettagliErrore());
    metadati.setErrorMessage(entity.getErrore());
    metadati.setFinishedAt(entity.getDataFine());
    metadati.setMessages(entity.getMessaggi() != null ? entity.getMessaggi().stream()
        .map(operazioniAsincroneMapper::toEntity).collect(Collectors.toCollection(LinkedList::new))
        : null);
    metadati.setResult(ObjectUtils.toJson(entity.getRisultato()));
    metadati.setSteps(entity.getSteps() != null ? entity.getSteps().stream().map(this::map)
        .collect(Collectors.toCollection(LinkedList::new)) : null);

    return metadati;
  }
}
