/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaRequest;
import it.csi.cosmo.cosmosoap.dto.rest.GetStatoRichiestaResponse;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoRequest;
import it.csi.cosmo.cosmosoap.dto.rest.SmistaDocumentoResponse;

/**
 *
 */

public interface StardasService {

  SmistaDocumentoResponse smistaDocumento(SmistaDocumentoRequest requestSmistamento);

  GetStatoRichiestaResponse getStatoRichiesta(GetStatoRichiestaRequest statoRichiestaRequest);
}
