/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.model;

/**
 *
 */

/**
 *
 *
 */
public interface LongTaskAwaitCallbacks {

  void completed(LongTaskPersistableEntry task);

  void failed(LongTaskPersistableEntry task);

  void timedOut(LongTaskPersistableEntry task);

  default void updated(LongTaskPersistableEntry task) {}

  default void finalizing(LongTaskPersistableEntry task) {}
}
