/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.FormatiFileApi;
import it.csi.cosmo.cosmoecm.business.service.FormatoFileService;
import it.csi.cosmo.cosmoecm.dto.rest.FormatoFileResponse;
import it.csi.cosmo.cosmoecm.dto.rest.RaggruppamentoFormatiFileResponse;

public class FormatiFileApiImpl extends ParentApiImpl implements FormatiFileApi {

  @Autowired
  private FormatoFileService formatoFileService;


  @Override
  public Response getFormatiFile(String filter, SecurityContext securityContext) {
    FormatoFileResponse formatiFile = formatoFileService.getFormatiFile(filter);
    return Response.ok(formatiFile).build();
  }



  @Override
  public Response getFormatiFileGrouped(String filter, SecurityContext securityContext) {
    RaggruppamentoFormatiFileResponse formatiFile = formatoFileService.getFormatiFileRaggruppati(filter);
    return Response.ok(formatiFile).build();
  }

}
