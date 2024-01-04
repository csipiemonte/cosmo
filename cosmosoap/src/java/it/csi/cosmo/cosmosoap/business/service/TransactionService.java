/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import java.util.function.Supplier;
import it.csi.cosmo.cosmosoap.dto.TransactionExecutionResult;

/**
 *
 */

public interface TransactionService {

  <T> T inTransactionOrThrow(Supplier<T> task);

  <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task);

  void inTransactionOrThrow(Runnable task);

  TransactionExecutionResult<Void> inTransaction(Runnable task);

}
