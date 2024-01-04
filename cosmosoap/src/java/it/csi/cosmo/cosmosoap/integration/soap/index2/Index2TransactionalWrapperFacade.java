/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmosoap.integration.soap.index2;

import java.util.concurrent.Callable;
import it.csi.cosmo.cosmosoap.integration.soap.index2.exceptions.Index2RollbackFailedException;


/**
 *
 */

public interface Index2TransactionalWrapperFacade extends Index2WrapperFacade {

  <T> T withTransaction ( Callable<T> callable );

  void withTransaction ( Runnable runnable );

  void startTransaction ();

  void commit ();

  void rollback () throws Index2RollbackFailedException;

}
