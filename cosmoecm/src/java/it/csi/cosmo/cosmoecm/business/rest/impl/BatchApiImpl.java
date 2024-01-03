/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import java.time.LocalDateTime;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.batch.model.BatchExecutionContext;
import it.csi.cosmo.common.security.model.Secured;
import it.csi.cosmo.cosmoecm.business.batch.FilesystemToIndexBatch;
import it.csi.cosmo.cosmoecm.business.rest.BatchApi;


public class BatchApiImpl extends ParentApiImpl implements BatchApi {

  @Autowired
  public FilesystemToIndexBatch filesystemToIndexBatch;

  @Secured(testOnly = true)
  @Override
  public Response launchFilesystem2IndexBatch() {

    filesystemToIndexBatch.execute(BatchExecutionContext.builder()
        .withBatchName("Filesystem2IndexManual").withStartedAt(LocalDateTime.now()).build());

    return Response.ok("launched").build();
  }

}
