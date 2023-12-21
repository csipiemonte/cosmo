/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.store;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;

/**
 *
 */

public abstract class AbstractLongTaskDatabaseStore implements LongTaskStorageAdapter {

  protected CosmoLogger logger;

  public AbstractLongTaskDatabaseStore(CosmoLogger logger) {
    this.logger = logger;
  }

  protected abstract CosmoTOperazioneAsincrona persist(CosmoTOperazioneAsincrona entity);

  protected abstract CosmoTOperazioneAsincrona getByUUID(String uuid);

  protected abstract void deleteByUUID(String uuid);

  @Override
  public void save(LongTaskPersistableEntry task) {
    final var method = "save";
    if (logger.isTraceEnabled()) {
      logger.trace(method, "request for saving task {} on DB: {}", task.getUuid(),
          ObjectUtils.represent(task));
    } else {
      logger.debug(method, "request for saving task {} on DB", task.getUuid());
    }

    var entity = toEntity(task);
    var saved = persist(entity);

    logger.debug(method, "persisted task {} with id {}", saved.getUuid(), saved.getId());
  }

  @Override
  public LongTaskPersistableEntry get(String taskUUID) {
    final var method = "get";
    logger.debug(method, "request for getting task {} from DB", taskUUID);

    return fromEntity(getByUUID(taskUUID));
  }

  @Override
  public void delete(String taskUUID) {
    final var method = "delete";
    logger.debug(method, "request for deleting task {} from DB", taskUUID);

    deleteByUUID(taskUUID);
  }

  protected CosmoTOperazioneAsincrona toEntity(LongTaskPersistableEntry task) {
    if (task == null) {
      return null;
    }

    CosmoTOperazioneAsincrona entity = new CosmoTOperazioneAsincrona();

    entity.setDataAvvio(toTimestamp(task.getStartedAt()));
    entity.setDataFine(toTimestamp(task.getFinishedAt()));
    entity.setStato(task.getStatus());
    entity.setUuid(task.getUuid());
    entity.setMetadati(task);
    entity.setVersione(task.getVersion());

    return entity;
  }

  protected Timestamp toTimestamp(OffsetDateTime timestamp) {
    if (timestamp == null) {
      return null;
    }
    return Timestamp.from(timestamp.toInstant());
  }

  protected LongTaskPersistableEntry fromEntity(CosmoTOperazioneAsincrona entity) {
    if (entity == null) {
      return null;
    }

    LongTaskPersistableEntry deserialized = entity.getMetadati();



    return deserialized;
  }

}
