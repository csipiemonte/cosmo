/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.process.model;

/**
 *
 *
 */
public interface FunzionalitaFormLogicoHooks {

  void beforeComplete(BeforeCompleteHookContext context);

  void afterComplete(AfterCompleteHookContext context);

  boolean failSafe();
}
