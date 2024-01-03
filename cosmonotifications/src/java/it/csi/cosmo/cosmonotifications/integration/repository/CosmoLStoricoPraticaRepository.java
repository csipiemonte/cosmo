/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmonotifications.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoLStoricoPratica;
import it.csi.cosmo.common.entities.enums.TipoEventoStoricoPratica;
import it.csi.cosmo.common.repository.CosmoLRepository;

/**
 * Spring Data JPA repository per "CosmoLStoricoPratica"
 */

public interface CosmoLStoricoPraticaRepository
extends CosmoLRepository<CosmoLStoricoPratica, Long> {
  List<CosmoLStoricoPratica> findAllByPraticaIdAndCodiceTipoEventoAndUtenteCodiceFiscaleNotAndUtenteDtCancellazioneNull(
      Long id, TipoEventoStoricoPratica codiceTipoEvento, String codiceFiscale);
}
