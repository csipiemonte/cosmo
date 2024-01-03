/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.service;

import java.util.List;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.common.entities.CosmoTLock;
import it.csi.cosmo.cosmoecm.dto.index2.RisultatoMigrazioneDocumento;

public interface FilesystemToIndexService {

  /**
   * @param numMax
   * @param maxRetries
   * @return
   */
  List<CosmoTDocumento> findDaMigrare(int numMax, int maxRetries);

  /**
   * @param daMigrare
   * @return
   */
  List<RisultatoMigrazioneDocumento> migraDocumenti(List<CosmoTDocumento> daMigrare);

  List<RisultatoMigrazioneDocumento> migraDocumenti(List<CosmoTDocumento> daMigrare,
      BatchExecutionContext batchContext);

  /**
   * @param daMigrare
   * @return
   */
  RisultatoMigrazioneDocumento migraDocumento(CosmoTDocumento daMigrare);

  /**
   * @param lock
   * @param daMigrare
   * @param batchContext
   * @return
   */
  List<RisultatoMigrazioneDocumento> migraDocumentiInsideLock(CosmoTLock lock,
      List<CosmoTDocumento> daMigrare, BatchExecutionContext batchContext);

}
