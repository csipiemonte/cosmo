/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.common.config.Constants;
import it.csi.cosmo.cosmoecm.business.rest.FruitoriApi;
import it.csi.cosmo.cosmoecm.business.service.FruitoriService;
import it.csi.cosmo.cosmoecm.dto.ContenutoDocumentoResult;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiFruitoreRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CreaDocumentiLinkFruitoreRequest;
import it.csi.cosmo.cosmoecm.util.FilesUtils;

public class FruitoriApiImpl extends ParentApiImpl implements FruitoriApi {

  @Autowired
  private FruitoriService fruitoriService;

  @Override
  public Response getContenutoFruitore(String idDocumentoExt, SecurityContext securityContext) {
    ContenutoDocumentoResult contenuto = fruitoriService.getContenutoFruitore(idDocumentoExt);
    if (null != contenuto.getLinkDownloadDiretto()) {
    //@formatter:off
      return Response.status(Status.FOUND)
          .header("Location", contenuto.getLinkDownloadDiretto())
          .header(Constants.HEADERS_PREFIX + "DNF", Boolean.TRUE.toString())
          .build();
      //@formatter:on
    } else {
      return FilesUtils.toDownloadResponse(contenuto);
    }
  }

  @Override
  public Response postDocumentoFruitore(CreaDocumentiFruitoreRequest request, String xRequestId,
      String xForwardedFor, SecurityContext securityContext) {

    return Response
        .status(200)
        .entity(fruitoriService.inserisciDocumenti(request))
        .build();
  }

  @Override
  public Response postFruitoriDocumentiLink(String xRequestId, String xForwardedFor,
      CreaDocumentiLinkFruitoreRequest request, SecurityContext securityContext) {

    return Response
        .status(200)
        .entity(fruitoriService.inserisciDocumentiLink(request))
        .build();
  }

}
