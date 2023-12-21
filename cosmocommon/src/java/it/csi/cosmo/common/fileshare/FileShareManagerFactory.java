/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.common.fileshare;

/**
 *
 */

public interface FileShareManagerFactory {

  public static DefaultFileShareManagerImpl.Builder defaultMailer() {
    return DefaultFileShareManagerImpl.builder();
  }
}
