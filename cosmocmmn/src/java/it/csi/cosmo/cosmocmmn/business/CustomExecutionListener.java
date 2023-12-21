/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmocmmn.business;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

public class CustomExecutionListener implements ExecutionListener {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void notify(DelegateExecution arg0) {
    // nothing to do
  }

}

