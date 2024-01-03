/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTCredenzialiSigilloElettronico;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTSigilloElettronico"
 */

public interface CosmoTCredenzialiSigilloElettronicoRepository
    extends CosmoTRepository<CosmoTCredenzialiSigilloElettronico, Long> {


}
