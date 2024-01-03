/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentoFisicoActa;
import it.csi.cosmo.cosmoecm.dto.rest.IdentitaUtenteResponse;
import it.csi.cosmo.cosmoecm.dto.rest.ImportaDocumentiActaRequest;
import it.csi.cosmo.cosmoecm.dto.rest.RiferimentoOperazioneAsincrona;
import it.csi.cosmo.cosmoecm.dto.rest.TitolarioActa;
import it.csi.cosmo.cosmoecm.dto.rest.VociTitolarioActa;

/**
 *
 */

public interface ActaService {

  RiferimentoOperazioneAsincrona findDocumentiSemplici(String identita, String filter);

  RiferimentoOperazioneAsincrona importaDocumentiActa(String identita,
      ImportaDocumentiActaRequest body);

  IdentitaUtenteResponse findIdentitaDisponibili();

  List<DocumentoFisicoActa> findDocumentiFisiciByIdDocumentoSemplice(String idIdentita, String id);

  String findIdByIndiceClassificazioneEstesaAggregazione(String identita, String indiceClassificazioneEsteso);

  TitolarioActa getTitolarioActa(String idIdentita, String codice);

  VociTitolarioActa ricercaAlberaturaVociTitolario(String idIdentita, String chiaveTitolario,
      String chiavePadre, String filter);
}
