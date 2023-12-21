/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmocmmn.business.service;

import java.io.Serializable;
import java.util.function.Function;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;

/**
 * Servizio per la gestione dei task asincroni
 */
public interface AsyncTaskService {

  <T extends Serializable> LongTaskFuture<T> start(String name, Function<LongTask<T>, T> handler);

  LongTaskExecutor getExecutor();

  LongTaskPersistableEntry wait(String uuid, Long timeoutSeconds);
}
