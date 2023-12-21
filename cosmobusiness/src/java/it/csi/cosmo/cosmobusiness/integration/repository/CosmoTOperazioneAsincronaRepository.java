/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import it.csi.cosmo.common.entities.CosmoTOperazioneAsincrona;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTOperazioneAsincrona"
 */

public interface CosmoTOperazioneAsincronaRepository
    extends CosmoTRepository<CosmoTOperazioneAsincrona, Long> {


}
