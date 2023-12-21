/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.common.entities.CosmoLStoricoPratica;

public interface StoricoPraticaService {

  void logEvent(CosmoLStoricoPratica entry);

}
