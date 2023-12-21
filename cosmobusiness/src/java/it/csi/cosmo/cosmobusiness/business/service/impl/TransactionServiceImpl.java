/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service.impl;

import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import it.csi.cosmo.common.exception.InternalServerException;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

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
      String transactionId = UUID.randomUUID().toString();
      logger.debug(method, "[tx] transaction opened {}", transactionId);
      try {
        var result = task.get();
        logger.debug(method, "[tx] transaction committed {}", transactionId);
        return (TransactionExecutionResult<T>) TransactionExecutionResult
            .builder().withResult(result).withSuccess(true).build();

      } catch (Throwable t) { // NOSONAR
        status.setRollbackOnly();
        logger.debug(method, "[tx] transaction ROLLBACK {}", transactionId);
        logger.error(method, "Errore nella transazione isolata " + transactionId, t);

        return (TransactionExecutionResult<T>) TransactionExecutionResult.builder().withError(t)
            .withSuccess(false).build();
      } finally {
        logger.debug(method, "[tx] transaction closed {}", transactionId);
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
