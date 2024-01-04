/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service.impl;

import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmosoap.business.service.TransactionService;
import it.csi.cosmo.cosmosoap.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmosoap.util.logger.LogCategory;
import it.csi.cosmo.cosmosoap.util.logger.LoggerFactory;

/**
 *
 */
@Service
public class TransactionServiceImpl implements TransactionService {

  private static final String CLASS_NAME = TransactionServiceImpl.class.getSimpleName();

  @Autowired
  protected PlatformTransactionManager transactionManager;

  private static final CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BUSINESS_LOG_CATEGORY, CLASS_NAME);

  @SuppressWarnings("unchecked")
  @Override
  public <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task) {
    final var method = "inTransaction";

    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
    transactionTemplate.setTimeout(30 * 60);
    transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);

    return transactionTemplate.execute(status -> {
      logger.trace(method, "[tx] transaction opened");
      try {
        var result = task.get();
        logger.trace(method, "[tx] transaction committed");
        return (TransactionExecutionResult<T>) TransactionExecutionResult.builder()
            .withResult(result).withSuccess(true).build();

      } catch (Throwable t) { // NOSONAR
        status.setRollbackOnly();
        logger.debug(method, "[tx] transaction ROLLBACK");
        logger.error(method, "Errore nella transazione isolata", t);
        return (TransactionExecutionResult<T>) TransactionExecutionResult.builder().withError(t)
            .withSuccess(false).build();
      }
    });
  }

  protected void rethrow(Throwable t) {
    if (t instanceof RuntimeException) {
      throw (RuntimeException) t;
    } else {
      throw new InternalServerException(t.getMessage(), t);
    }
  }

  @Override
  public TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return this.inTransaction(() -> {
      task.run();
      return null;
    });
  }

  @Override
  public <T> T inTransactionOrThrow(Supplier<T> task) {
    TransactionExecutionResult<T> result = this.inTransaction(task);
    if (result.failed()) {
      rethrow(result.getError());
    }
    return result.getResult();
  }

  @Override
  public void inTransactionOrThrow(Runnable task) {
    TransactionExecutionResult<Void> result = this.inTransaction(task);
    if (result.failed()) {
      rethrow(result.getError());
    }
  }

}
