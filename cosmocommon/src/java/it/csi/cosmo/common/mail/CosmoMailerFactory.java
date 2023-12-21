/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.mail;

/**
 *
 */

public interface CosmoMailerFactory {

  public static DefaultCosmoMailerImpl.IServerStage defaultMailer() {
    return DefaultCosmoMailerImpl.builder();
  }
}
