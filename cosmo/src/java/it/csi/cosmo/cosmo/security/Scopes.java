/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmo.security;

/**
 * Enumerazione contenente tutti gli Scopes gestiti dall'applicativo
 */
public abstract class Scopes {

  private Scopes() {
    // private
  }

  //@formatter:off
  public static final String MONITORING = "MONITORING";
  public static final String DISCOVERY_REGISTER = "DISCOVERY_REGISTER";
  public static final String DISCOVERY_FETCH = "DISCOVERY_FETCH";
  public static final String ORCHESTRATOR = "ORCHESTRATOR";
  public static final String PUSH_NOTIFICATIONS = "PUSH_NOTIFICATIONS";
  public static final String PUSH_EVENTS = "PUSH_EVENTS";
  public static final String PROCESS_EVENTS = "PROCESS_EVENTS";
  public static final String DEPLOY_PROCESS = "DEPLOY_PROCESS";
  //@formatter:on

}
