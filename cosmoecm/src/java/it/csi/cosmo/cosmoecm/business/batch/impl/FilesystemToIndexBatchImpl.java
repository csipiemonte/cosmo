/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.batch.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.entities.CosmoTDocumento;
import it.csi.cosmo.cosmoecm.business.batch.FilesystemToIndexBatch;
import it.csi.cosmo.cosmoecm.business.service.FilesystemToIndexService;
import it.csi.cosmo.cosmoecm.config.ParametriApplicativo;

@Service
public class FilesystemToIndexBatchImpl extends ParentBatchImpl implements FilesystemToIndexBatch {

  private static final int NUM_MAX_RETRIES = 5;

  @Autowired
  private FilesystemToIndexService filesystemToIndexService;

  @Override
  public boolean isEnabled() {
    return configurazioneService != null && configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_FS_TO_INDEX_ENABLE).asBool();
  }

  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public synchronized void execute(BatchExecutionContext context) {
    String method = "execute";

    logger.info(method, "esecuzione batch di trasferimento da filesystem ad Index");

    Integer numMax = configurazioneService
        .requireConfig(ParametriApplicativo.BATCH_FS_TO_INDEX_MAX_DOCUMENTI_PER_ESECUZIONE)
        .asInteger();

    List<CosmoTDocumento> daMigrare =
        filesystemToIndexService.findDaMigrare(numMax, NUM_MAX_RETRIES);

    filesystemToIndexService.migraDocumenti(daMigrare, context);
  }

}
