/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.test.cosmo.cosmoecm.testbed.service;

import java.io.IOException;
import it.csi.cosmo.common.entities.CosmoTPratica;

/**
 *
 */

public interface PraticaTestbedService {

  CosmoTPratica salvaPratica() throws IOException;

}
