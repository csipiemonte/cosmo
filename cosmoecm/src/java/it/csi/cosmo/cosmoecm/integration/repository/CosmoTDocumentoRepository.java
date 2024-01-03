/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.integration.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.repository.CosmoTRepository;

/**
 */

public interface CosmoTDocumentoRepository
extends
CosmoTRepository<CosmoTDocumento, Long> {

  CosmoTDocumento findByPraticaIdAndIdDocumentoExt(Long idPratica, String idDocExt);

  List<CosmoTDocumento> findAllByDocumentoPadre(CosmoTDocumento documentoPadre);

  @Deprecated(forRemoval = true)
  @Query("select d from CosmoTDocumento d where d.stato.codice = :codiceStato AND d.dtCancellazione IS NULL")
  List<CosmoTDocumento> findByCodiceStato(@Param("codiceStato") String codiceStato, Pageable page);

}
