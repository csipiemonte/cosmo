/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */


package it.csi.cosmo.cosmonotifications.integration.repository;

import it.csi.cosmo.common.entities.CosmoTPratica;
import it.csi.cosmo.common.repository.CosmoTRepository;

public interface CosmoPraticaRepository extends CosmoTRepository<CosmoTPratica, Long> {

  // CosmoTNotifiche findById(Long Id) ;

  // Page<CosmoTNotifica> findAll( Pageable p );


}
