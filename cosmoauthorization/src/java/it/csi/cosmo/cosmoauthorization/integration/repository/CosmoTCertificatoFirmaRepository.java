/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoauthorization.integration.repository;

import java.util.List;
import it.csi.cosmo.common.entities.CosmoTCertificatoFirma;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTCertificatoFirma"
 */

public interface CosmoTCertificatoFirmaRepository
extends CosmoTRepository<CosmoTCertificatoFirma, Long> {

  List<CosmoTCertificatoFirma> findAllByCosmoTUtenteCodiceFiscaleAndDtCancellazioneIsNull(
      String codiceFiscale);

}
