/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto;

/**
 *
 *
 */
public interface LockPolicyConfiguration {

  boolean synchronizeLocalThreads();

  boolean synchronizeDatabaseRowOnRelease();

  default long delayBeforeAcquisition() {
    return 0L;
  }

  default long delayBeforeAcquisitionFlush() {
    return 0L;
  }

  default long delayBeforeRelease() {
    return 0L;
  }

  default long delayBeforeReleaseFlush() {
    return 0L;
  }
}
