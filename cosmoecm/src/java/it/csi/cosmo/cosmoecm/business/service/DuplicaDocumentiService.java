/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.rest.Documenti;
import it.csi.cosmo.cosmoecm.dto.rest.RelazioniDocumentoDuplicato;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;

/**
 *
 */

public interface DuplicaDocumentiService {

  RiferimentoOperazioneAsincrona duplicaDocumenti(Long idPraticaDa, Long idPraticaA);

  Documenti preparaDuplicazione(Long idPraticaDa, Long idPraticaA, Boolean returnDocs);

  RelazioniDocumentoDuplicato duplicaDocumento(Long idPraticaA, Long idDocumentoDaDuplicare,
      RelazioniDocumentoDuplicato docDaDuplicareDocNuovo);
}
