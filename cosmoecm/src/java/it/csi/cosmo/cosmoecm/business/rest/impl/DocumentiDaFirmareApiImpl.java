/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.DocumentiDaFirmareApi;
import it.csi.cosmo.cosmoecm.business.service.DocumentoService;
import it.csi.cosmo.cosmoecm.dto.rest.DocumentiResponse;

public class DocumentiDaFirmareApiImpl extends ParentApiImpl implements DocumentiDaFirmareApi {

  @Autowired
  private DocumentoService documentoService;

  @Override
  public Response getDocumentiFirmabili(String filter, String filterDocDaFirmare, Boolean export,
      SecurityContext securityContext) {
    DocumentiResponse documenti =
        documentoService.getDocumentiDaFirmare(filter, filterDocDaFirmare, export);

    if (documenti != null) {
      return Response.ok(documenti).build();
    } else {
      return Response.noContent().build();
    }
  }
}
