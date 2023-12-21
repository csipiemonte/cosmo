/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.GetPayloadAttivazioneSistemaEsternoResponse;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;

public interface AttivazioneSistemaEsternoService {

  /**
   * @param idPratica
   * @param idAttivita
   * @return
   */
  RiferimentoOperazioneAsincrona inviaAttivazioneSistemaEsterno(Long idPratica, Long idAttivita);

  /**
   * @param idPratica
   * @param idAttivita
   * @return
   */
  GetPayloadAttivazioneSistemaEsternoResponse getPayloadAttivazioneSistemaEsterno(Long idPratica,
      Long idAttivita);

}
