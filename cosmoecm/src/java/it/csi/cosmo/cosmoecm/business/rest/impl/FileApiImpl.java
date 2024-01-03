/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.FileApi;
import it.csi.cosmo.cosmoecm.business.service.FileService;

public class FileApiImpl extends ParentApiImpl implements FileApi {

  @Autowired
  FileService fileService;

  @Override
  public Response deleteFileUuid(String uuid, SecurityContext securityContext) {
    fileService.deleteFileOnFileSystem(uuid);
    return Response.noContent().build();
  }


}
