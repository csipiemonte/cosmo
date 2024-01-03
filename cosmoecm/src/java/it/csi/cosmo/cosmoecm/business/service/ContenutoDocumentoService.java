/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.net.URI;
import it.csi.cosmo.common.entities.CosmoDFormatoFile;
import it.csi.cosmo.common.entities.CosmoDTipoDocumento;
import it.csi.cosmo.common.entities.CosmoDTipoFirma;
import it.csi.cosmo.common.entities.CosmoTContenutoDocumento;
import it.csi.cosmo.common.fileshare.model.RetrievedContent;
import it.csi.cosmo.cosmoecm.dto.FileContent;
import it.csi.cosmo.cosmoecm.dto.rest.ContenutiDocumento;
import it.csi.cosmo.cosmosoap.dto.rest.Entity;

/**
 *
 */

public interface ContenutoDocumentoService {

  CosmoTContenutoDocumento creaContenutoTemporaneo(RetrievedContent file);

  CosmoTContenutoDocumento creaContenutoOriginale(CosmoTContenutoDocumento temporaneo,
      Entity indexEntity);

  CosmoTContenutoDocumento creaContenutoSbustato(CosmoTContenutoDocumento temporaneo,
      Entity indexEntity);

  CosmoTContenutoDocumento creaContenutoFirmato(CosmoTContenutoDocumento firmato,
      Entity indexEntity, CosmoDTipoFirma tipoFirma, String nomeFileFirmato,
      CosmoDFormatoFile formatoFileFirmato);

  CosmoDFormatoFile findFormatoByMime(String mime);

  void cancella(CosmoTContenutoDocumento contenuto, boolean cancellaContenutoFisico);

  CosmoTContenutoDocumento getById(Long idDocumento, Long idContenuto);

  FileContent getContenutoFisico(Long idDocumento, Long idContenuto);

  void cancellaById(Long idDocumento, Long idContenuto, boolean cancellaContenutoFisico);

  ContenutiDocumento getByIdDocumento(Long idDocumento);

  boolean verificaFirma(CosmoTContenutoDocumento contenuto);

  URI getLinkDownloadDiretto(Long idDocumento, Long idContenuto, boolean preview);

  boolean isTemporaneo(Long idDocumento, Long idContenuto);

  void validaDimensioneDocumento(CosmoDTipoDocumento tipoDocumento, Long dimensioneDocumento);

  /**
   * @param idDocumento
   * @param idContenuto
   * @param durataMinima
   * @param preview
   * @return
   */
  URI getLinkEsposizionePermanente(Long idDocumento, Long idContenuto, Long durataMinima,
      boolean preview);

  /**
   * @param contenutoTemporaneo
   * @param indexEntity
   * @return CosmoTContenutoDocumento
   */
  CosmoTContenutoDocumento creaContenutoOriginaleDaStreaming(
      CosmoTContenutoDocumento contenutoTemporaneo, Entity indexEntity);

  /**
   * @param idDocumento
   * @param idContenuto
   * @return byte[]
   */
  byte[] getContenutoIndex(Long idDocumento, Long idContenuto);

  String generaSha256PerFile(RetrievedContent file);

  String generaSha256PerFile(byte[] file);

  CosmoTContenutoDocumento creaContenutoFirmatoFea(CosmoTContenutoDocumento contenutoOriginale,
      Entity indexEntity, String nomeFileFirmato);

}
