/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoauthorization.business.service;

import java.math.BigDecimal;
import java.util.List;
import it.csi.cosmo.cosmoauthorization.dto.rest.ConfigurazioneEnte;

/**
 * Servizio per la gestione della configurazione dell'ente
 */
public interface ConfigurazioneEnteService {

  ConfigurazioneEnte getConfigurazioneEnte(BigDecimal idEnte, String chiave);

  List<ConfigurazioneEnte> getConfigurazioniEnte(BigDecimal idEnte);

  ConfigurazioneEnte postConfigurazioneEnte(BigDecimal idEnte, ConfigurazioneEnte configurazione);

  ConfigurazioneEnte putConfigurazioneEnte(BigDecimal idEnte, String chiave,
      ConfigurazioneEnte configurazione);

  void deleteConfigurazioneEnte(BigDecimal idEnte, String chiave);

}
