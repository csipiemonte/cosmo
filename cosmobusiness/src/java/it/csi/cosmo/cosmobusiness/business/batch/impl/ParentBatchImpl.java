/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.batch.impl;

import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.cosmobusiness.business.service.ConfigurazioneService;
import it.csi.cosmo.cosmobusiness.business.service.TransactionService;
import it.csi.cosmo.cosmobusiness.dto.TransactionExecutionResult;
import it.csi.cosmo.cosmobusiness.util.logger.LogCategory;
import it.csi.cosmo.cosmobusiness.util.logger.LoggerFactory;

public class ParentBatchImpl {

  protected static final String PATH_SEPARATOR = "/";

  protected CosmoLogger logger =
      LoggerFactory.getLogger(LogCategory.BATCH_LOG_CATEGORY, this.getClass().getSimpleName());

  @Autowired
  protected ConfigurazioneService configurazioneService;

  @Autowired
  protected TransactionService transactionService;

  protected <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task) {
    return transactionService.inTransaction(task);
  }

  protected TransactionExecutionResult<Void> inTransaction(Runnable task) {
    return transactionService.inTransaction(task);
  }

}
