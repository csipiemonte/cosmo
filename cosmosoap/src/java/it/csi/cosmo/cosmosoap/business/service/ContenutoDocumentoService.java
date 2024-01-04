/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmosoap.business.service;

import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;

/**
 *
 */

public interface ContenutoDocumentoService {

  CosmoTContenutoDocumento creaContenutoFirmato(CosmoTContenutoDocumento firmato,
      Entity indexEntity, CosmoDTipoFirma tipoFirma, String nomeFileFirmato,
      CosmoDFormatoFile formatoFileFirmato);

  boolean verificaFirma(CosmoTContenutoDocumento contenuto);

  String generaSha256PerFile(byte[] file);

  String generaSha256PerFile(RetrievedContent file);

  CosmoTContenutoDocumento creaContenutoSigilloElettronico(
      CosmoTContenutoDocumento contenutoOriginale,
      Entity indexEntity);

}
