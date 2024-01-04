/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.integration.soap.index2.aspect;

import java.util.Arrays;
import it.csi.cosmo.cosmosoap.integration.soap.index2.model.IndexProperty;

/**
 *
 */

public class SharedAspect {

  @IndexProperty(value = "ecm-sys:sharedLinks", converter = SharedLinkValueConverter.class,
      required = false, readOnly = true)
  protected IndexShare[] sharedLinks;

  public SharedAspect() {
    // NOP
  }

  public IndexShare[] getSharedLinks() {
    return sharedLinks;
  }

  @Override
  public String toString() {
    return "SharedAspect ["
        + (sharedLinks != null ? "sharedLinks=" + Arrays.toString(sharedLinks) : "") + "]";
  }

}
