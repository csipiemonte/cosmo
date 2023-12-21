/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmobusiness.business.service;

import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaApprovaRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.EsecuzioneMultiplaVariabiliProcessoRequest;
import it.csi.cosmo.cosmobusiness.dto.rest.RiferimentoOperazioneAsincrona;

public interface EsecuzioneMultiplaService {

  RiferimentoOperazioneAsincrona postEsecuzioneMultiplaApprova(
      EsecuzioneMultiplaApprovaRequest request);

  RiferimentoOperazioneAsincrona postEsecuzioneMultiplaVariabiliProcesso(
      EsecuzioneMultiplaVariabiliProcessoRequest request);

}
