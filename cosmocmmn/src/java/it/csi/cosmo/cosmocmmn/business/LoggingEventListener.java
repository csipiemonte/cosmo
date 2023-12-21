/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business;

import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import it.csi.cosmo.common.logger.CosmoLogger;
import it.csi.cosmo.common.util.ObjectUtils;
import it.csi.cosmo.cosmocmmn.util.logger.LogCategory;
import it.csi.cosmo.cosmocmmn.util.logger.LoggerFactory;

public class LoggingEventListener implements FlowableEventListener {

  private final CosmoLogger LOGGER =
      LoggerFactory.getLogger(LogCategory.PROCESS_LOG_CATEGORY, this.getClass().getSimpleName());

  @Override
  public void onEvent(FlowableEvent ev) {
    if (!LOGGER.isDebugEnabled()) {
      return;
    }

    try {
      LOGGER.info("event", "< " + ev.getType() + " - " + ObjectUtils.toJson(ev));
    } catch (Exception e) {
      LOGGER.info("event", "< " + ev.getType() + " - " + ev.toString() + " ("
          + e.getClass().getSimpleName() + " - " + e.getMessage() + ")");
    }
  }

  @Override
  public boolean isFireOnTransactionLifecycleEvent() {
    return false;
  }

  @Override
  public boolean isFailOnException() {
    return false;
  }

  @Override
  public String getOnTransaction() {
    return null;
  }
}
