/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmobusiness.testbed.rest;

import org.apache.commons.lang3.NotImplementedException;


/**
 *
 */

public abstract class ParentTestClient {

  protected void notMocked() {
    throw new NotImplementedException("Il mock per questa chiamata non e' stato implementato. "
        + "Stai eseguendo un integration test, quindi i feign client non sono abilitati "
        + "e sono sostituiti da implementazioni mockate nel package "
        + this.getClass().getPackageName()
        + ". Assicurati che un mock del servizio chiamato sia implementato nella classe corrispondente.");
  }
}
