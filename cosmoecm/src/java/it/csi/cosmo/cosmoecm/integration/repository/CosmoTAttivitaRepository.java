/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTAttivita;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoTAttivitaRepository extends CosmoTRepository<CosmoTAttivita, Long> {

  CosmoTAttivita findByCosmoTPraticaIdAndLinkAttivita(Long idPratica, String linkAttivita);

}
