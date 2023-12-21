/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.integration.repository;

import java.sql.Timestamp;
import it.csi.cosmo.common.entities.CosmoCConfigurazione;
import it.csi.cosmo.common.repository.CosmoCRepository;

/**
 * Spring Data JPA repository per "CosmoCConfigurazione"
 */

public interface CosmoCConfigurazioneRepository
    extends CosmoCRepository<CosmoCConfigurazione, String> {


  CosmoCConfigurazione findOneByChiaveAndDtInizioValBeforeAndDtFineValAfterOrDtFineValNull(
      String chiave, Timestamp inizioValidita, Timestamp fineValidita);
}
