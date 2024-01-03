/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service.impl;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.LongTaskStepExecutor;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.async.store.AbstractLongTaskDatabaseStore;
import it.csi.cosmo.common.async.store.LongTaskStorageAdapter;
import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona;
import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona_;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoecm.business.service.AsyncTaskService;
import it.csi.cosmo.cosmoecm.business.service.TransactionService;
import it.csi.cosmo.cosmoecm.integration.repository.CosmoTOperazioneAsincronaRepository;
import it.csi.cosmo.cosmoecm.util.logger.LogCategory;
import it.csi.cosmo.cosmoecm.util.logger.LoggerFactory;

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

  private LongTaskExecutor executor;

  @Override
  public <T extends Serializable> LongTaskFuture<T> start(String name,
      Function<LongTask<T>, T> handler) {

    return this.executor.start(name, handler);
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

}
