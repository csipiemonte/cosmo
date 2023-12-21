/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobe.integration.apimgr.model;

/**
 * Puo' avere valore APPLICATION o APPLICATION_USER per identificare se si riferisce all'asserzione
 * generata per un applicazione con l'utente finale o per un'applicazione senza utente
 *
 *
 *
 */
public enum ApiMgrJWTAssertionUserType {
 APPLICATION, APPLICATION_USER, Application_User, Application, application, application_user
}
