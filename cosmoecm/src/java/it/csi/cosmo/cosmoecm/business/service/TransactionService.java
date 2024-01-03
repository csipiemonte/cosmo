/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.function.Supplier;
import it.csi.cosmo.cosmoecm.dto.TransactionExecutionResult;

/**
 * Servizio per la gestione manuale delle transazioni
 */
public interface TransactionService {

  <T> T inTransactionOrThrow(Supplier<T> task);

  <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task);

  void inTransactionOrThrow(Runnable task);

  TransactionExecutionResult<Void> inTransaction(Runnable task);
}
