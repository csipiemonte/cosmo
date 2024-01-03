/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.FirmaFeaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public interface FeaService {

  RiferimentoOperazioneAsincrona firma(FirmaFeaRequest request);

}
