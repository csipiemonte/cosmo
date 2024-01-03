/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.business.service;

import java.util.function.Supplier;
import it.csi.cosmo.cosmonotifications.dto.TransactionExecutionResult;

/**
 * Servizio per la gestione manuale delle transazioni
 */
public interface TransactionService {

  <T> TransactionExecutionResult<T> inTransaction(Supplier<T> task);

  TransactionExecutionResult<Void> inTransaction(Runnable task);
}
