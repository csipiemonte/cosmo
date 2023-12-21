/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.async.internal;

import java.io.Serializable;
import java.util.function.Function;
import it.csi.cosmo.common.async.model.LongTask;

/**
 *
 */

public interface LongTaskFeedbackBridge {

  <T extends Serializable> void updated(LongTask<T> task, boolean immediate);
  
  <V extends Serializable> V applyStep(Function<LongTask<V>, V> executor, LongTask<V> step);
}
