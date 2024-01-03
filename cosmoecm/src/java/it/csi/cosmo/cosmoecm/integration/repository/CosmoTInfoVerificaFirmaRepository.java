/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.entities.CosmoTInfoVerificaFirma;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 * Spring Data JPA repository per "CosmoTInfoVerificaFirma"
 */

public interface CosmoTInfoVerificaFirmaRepository
extends CosmoTRepository<CosmoTInfoVerificaFirma, Long> {

  void deleteByContenutoDocumentoPadre(CosmoTContenutoDocumento contenutoDocumentoPadre);
}
