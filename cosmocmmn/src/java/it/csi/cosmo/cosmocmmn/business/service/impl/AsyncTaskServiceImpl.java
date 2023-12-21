/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service.impl;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.async.store.LongTaskStorageAdapter;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;
import it.csi.cosmo.cosmocmmn.business.service.AsyncTaskService;
import it.csi.cosmo.cosmocmmn.integration.mapper.OperazioniAsincroneMapper;
import it.csi.cosmo.cosmocmmn.integration.rest.CosmoBusinessOperazioniAsincroneFeignClient;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

/**
 *
 */

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService, InitializingBean, DisposableBean {

  private final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  private CosmoBusinessOperazioniAsincroneFeignClient cosmoBusinessOperazioniAsincroneFeignClient;

  private LongTaskExecutor executor;

  @Override
  public <T extends Serializable> LongTaskFuture<T> start(String name,
      Function<LongTask<T>, T> handler) {

    LongTaskFuture<T> taskFuture = this.executor.start(name, handler);

    return taskFuture;
  }

  @Override
  public LongTaskExecutor getExecutor() {
    return this.executor;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    //@formatter:off
    this.executor = LongTaskExecutor.builder()
      .withLogger(this.logger)
      .withStorageAdapter(storageAdapter())
      .build();
    //@formatter:on
  }

  @Override
  public void destroy() throws Exception {
    if (this.executor != null) {
      this.executor.close();
    }
  }

  private LongTaskStorageAdapter storageAdapter() {
    return new LongTaskStorageAdapter() {

      @Override
      public void delete(String uuid) {
        cosmoBusinessOperazioniAsincroneFeignClient.deleteOperazioneAsincrona(uuid);
      }

      @Override
      public LongTaskPersistableEntry get(String uuid) {
        OperazioneAsincrona response =
            cosmoBusinessOperazioniAsincroneFeignClient.getOperazioneAsincrona(uuid);

        return OperazioniAsincroneMapper.toEntity(response);
      }

      @Override
      public void save(LongTaskPersistableEntry entity) {

        OperazioneAsincrona payload = OperazioniAsincroneMapper.toDTO(entity);
        cosmoBusinessOperazioniAsincroneFeignClient.putOperazioneAsincrona(payload.getUuid(),
            payload);
      }
    };
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

}
