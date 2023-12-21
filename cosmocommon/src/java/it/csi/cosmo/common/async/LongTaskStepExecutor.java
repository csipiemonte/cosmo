/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.common.async;

import java.io.Serializable;
import java.util.function.Function;
import it.csi.cosmo.common.async.model.LongTask;

public interface LongTaskStepExecutor {

  <T extends Serializable> T apply(Function<LongTask<T>, T> executor, LongTask<T> step);
}
