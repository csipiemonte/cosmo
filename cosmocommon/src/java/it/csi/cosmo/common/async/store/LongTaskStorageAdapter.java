/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.store;

import it.csi.cosmo.common.async.model.LongTaskPersistableEntry;

/**
 *
 */

public interface LongTaskStorageAdapter {

  void save(LongTaskPersistableEntry task);

  LongTaskPersistableEntry get(String taskUUID);
  
  void delete(String taskUUID);
}
