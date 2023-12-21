/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail.model;

/**
 *
 */

public enum MailStatus {

  PREPARED, SENT, REQUEUED, FAILED, SUPPRESSED_BY_DEBOUNCE

}
