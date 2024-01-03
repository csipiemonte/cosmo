/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import it.csi.cosmo.common.entities.CosmoTFormLogico;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

public interface CosmoTFormLogicoRepository extends CosmoTRepository<CosmoTFormLogico, Long> {

  CosmoTFormLogico findOneByCodice(String codice);


}
