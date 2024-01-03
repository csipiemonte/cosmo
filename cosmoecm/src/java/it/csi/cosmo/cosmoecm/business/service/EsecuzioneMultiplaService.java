/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.EsecuzioneMultiplaRifiutoFirmaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public interface EsecuzioneMultiplaService {

  void postEsecuzioneMultiplaFirma(
      EsecuzioneMultiplaFirmaRequest request);

  RiferimentoOperazioneAsincrona postEsecuzioneMultiplaRifiutoFirma(
      EsecuzioneMultiplaRifiutoFirmaRequest request);

}
