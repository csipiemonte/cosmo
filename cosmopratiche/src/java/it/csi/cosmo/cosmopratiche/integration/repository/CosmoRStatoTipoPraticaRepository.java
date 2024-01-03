/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoRStatoTipoPratica;
import it.csi.cosmo.common.repository.CosmoRRepository;

public interface CosmoRStatoTipoPraticaRepository extends CosmoRRepository<CosmoRStatoTipoPratica, String> {

  List<CosmoRStatoTipoPratica> findByCosmoDTipoPraticaCodice(String codiceTipoPratica);
}
