/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.batch.impl;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmoauthorization.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmoauthorization.business.service.MailService;
import it.csi.cosmo.cosmoauthorization.business.service.TransactionService;
import it.csi.cosmo.cosmoauthorization.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmoauthorization.util.logger.LogCategory;
import it.csi.cosmo.cosmoauthorization.util.logger.LoggerFactory;

public class ParentBatchImpl {

  protected static final String PATH_SEPARATOR = "/";

  protected CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BATCH_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  protected ConfigurazioneService configurazioneService;

  @Autowired
  protected MailService mailService;

  @Autowired
  protected TransactionService transactionService;

  @Deprecated(forRemoval = true)
  protected Boolean inTransaction(TransactionTemplate transactionTemplate,
      TransactionCallback<Boolean> callable) {
    final var method = "inTransaction";
    try {
      return transactionTemplate.execute(callable::doInTransaction);
    } catch (Throwable t) { // NOSONAR
      logger.error(method, "Errore nel commit della transazione", t);
      return Boolean.FALSE;
    }
  }

  protected <T> T require(T raw, String descrizione) {
    if (raw == null) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' nullo");
    }
    if (raw instanceof String && StringUtils.isBlank((String) raw)) {
      throw new IllegalArgumentException("Il campo " + descrizione + " e' richiesto ma e' vuoto");
    }
    return raw;
  }

  protected <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task) {
    return transactionService.inTransaction(task);
  }

  protected TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return transactionService.inTransaction(task);
  }

  protected <T> TransactionExecutionResult<T> attempt(Callable<T> task) {
    try {
      T result = task.call();
      return TransactionExecutionResult.<T>builder().withResult(result).withSuccess(true).build();
    } catch (Exception e) {
      return TransactionExecutionResult.<T>builder().withError(e).withSuccess(false).build();
    }
  }

}
