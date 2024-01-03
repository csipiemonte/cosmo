/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import it.csi.cosmo.cosmoecm.dto.FileContent;

/**
 *
 */

public interface FileService {

  /**
   * Metodo che consente il download di un documento sia su file system che su index
   *
   * @param id del documento da scaricare
   * @return il file associato al documento il cui id e' il parametro di input
   */
  FileContent downloadDocumento(String id);

  /**
   * Metodo che elimina il file dal filesystem
   *
   * @param uuid e' l'identificativo necessario per determinare il file da eliminare
   */
  void deleteFileOnFileSystem(String uuid);

  /**
   * Metodo che consente la preview di un documento che non ha la firma o che e' stato sbustato
   *
   * @param id del documento di cui si vuole la preview
   * @return il file associato al documento il cui id e' il parametro di input
   */
  FileContent previewDocumento(String id);

}
