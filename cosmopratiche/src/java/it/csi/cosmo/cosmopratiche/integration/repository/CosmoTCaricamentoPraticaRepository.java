/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTCaricamentoPratica;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 *
 */

public interface CosmoTCaricamentoPraticaRepository
    extends CosmoTRepository<CosmoTCaricamentoPratica, Long> {

  List<CosmoTCaricamentoPratica> findByCosmoDStatoCaricamentoPraticaCodice(String codice);

}
