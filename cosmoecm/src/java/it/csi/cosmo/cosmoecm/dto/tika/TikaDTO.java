/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.dto.tika;

import org.apache.tika.Tika;

/**
 *
 */

public class TikaDTO extends Tika {

  private static TikaDTO instance = null;

  private TikaDTO() {
  }

  public static TikaDTO getInstance() {
    if (instance == null)
      instance = new TikaDTO();

    return instance;
  }

}
