/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoRAttivitaAssegnazione;
import it.csi.cosmo.common.repository.CosmoRRepository;

/**
 *
 */

public interface CosmoRAttivitaAssegnazioneRepository
extends CosmoRRepository<CosmoRAttivitaAssegnazione, Long> {

  List<CosmoRAttivitaAssegnazione> findAllByCosmoTAttivitaIdAndDtFineValIsNullAndUtenteNotNullAndUtenteCodiceFiscaleNotAndUtenteDtCancellazioneNull(
      Long idAttivita, String codiceFiscale);

}
