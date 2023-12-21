/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.business.service;

import it.csi.cosmo.cosmoauthorization.dto.rest.AutorizzazioniFruitoreResponse;

/**
 *
 */

public interface AutorizzazioniFruitoreService {

  AutorizzazioniFruitoreResponse getAutorizzazioniFruitore(String filter);

}
