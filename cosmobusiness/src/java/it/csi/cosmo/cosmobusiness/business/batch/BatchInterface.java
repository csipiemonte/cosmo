/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.batch;

import it.csi.cosmo.common.batch.model.BatchExecutionContext;

/**
 *
 */

public interface BatchInterface {

  public boolean isEnabled();

  public void execute(BatchExecutionContext context);
}
