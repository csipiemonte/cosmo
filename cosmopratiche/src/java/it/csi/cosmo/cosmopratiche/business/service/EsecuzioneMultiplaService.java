/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.service;

import java.util.List;
import it.csi.cosmo.cosmopratiche.dto.rest.AttivitaEseguibileMassivamente;
import it.csi.cosmo.cosmopratiche.dto.rest.RiferimentoAttivita;

/**
 *
 */

public interface EsecuzioneMultiplaService {

  /**
   * @return
   */
  List<AttivitaEseguibileMassivamente> getTaskDisponibiliPerUtenteCorrente();

  /**
   * @param codiceTipoPratica
   * @return
   */
  List<RiferimentoAttivita> getTipologieTaskDisponibiliPerUtenteCorrente(String codiceTipoPratica);

}
