/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.function.Function;
import it.csi.cosmo.common.async.LongTaskExecutor;
import it.csi.cosmo.common.async.model.LongTask;
import it.csi.cosmo.common.async.model.LongTaskFuture;
import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;
import it.csi.cosmo.cosmobusiness.dto.rest.CreaOperazioneAsincronaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.OperazioneAsincrona;

/**
 * Servizio per la gestione dei task asincroni
 */
public interface AsyncTaskService {

  <T extends Serializable> LongTaskFuture<T> start(String name, Function<LongTask<T>, T> handler);

  LongTaskExecutor getExecutor();

  OperazioneAsincrona getOperazioneAsincrona(String uuid);

  OperazioneAsincrona postOperazioneAsincrona(CreaOperazioneAsincronaRequest body);

  OperazioneAsincrona putOperazioneAsincrona(String uuid, OperazioneAsincrona body);

  void deleteOperazioneAsincrona(String uuid);

  Future<LongTaskPersistableEntry> watcher(String uuid, Long timeoutSeconds);

  LongTaskPersistableEntry wait(String uuid, Long timeoutSeconds);

}
