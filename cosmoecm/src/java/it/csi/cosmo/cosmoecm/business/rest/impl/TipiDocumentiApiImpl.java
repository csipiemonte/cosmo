/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmoecm.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.TipiDocumentiApi;
import it.csi.cosmo.cosmoecm.business.service.TipoDocumentoService;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

public class TipiDocumentiApiImpl extends ParentApiImpl implements TipiDocumentiApi {

  @Autowired
  private TipoDocumentoService tipoDocumentoService;


  @Override
  public Response getTipiDocumentiAll(String tipoPratica, SecurityContext securityContext) {
    List<TipoDocumento> documenti =
        tipoDocumentoService.getTipiDocumentiAll(tipoPratica);

    return Response.ok(documenti).build();
  }

  @Override
  public Response getTipiDocumenti(String idPratica, String codiceTipoDocPadre,
      SecurityContext securityContext) {

    List<TipoDocumento> documenti =
        tipoDocumentoService.getTipiDocumenti(idPratica, codiceTipoDocPadre);

    return Response.ok(documenti).build();
  }

}
