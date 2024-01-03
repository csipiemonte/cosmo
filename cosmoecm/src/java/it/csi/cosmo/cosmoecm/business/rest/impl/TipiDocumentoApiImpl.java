/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.TipiDocumentoApi;
import it.csi.cosmo.cosmoecm.business.service.TipoDocumentoService;
import it.csi.cosmo.cosmoecm.dto.rest.TipoDocumento;

/**
 *
 */

public class TipiDocumentoApiImpl extends ParentApiImpl implements TipiDocumentoApi {

  @Autowired
  private TipoDocumentoService tipoDocumentoService;


  @Override
  public Response getTipiDocumento(List<String> codici, SecurityContext securityContext) {
    List<TipoDocumento> documenti =
        tipoDocumentoService.getTipiDocumento(codici);

    return Response.ok(documenti).build();
  }


  @Override
  public Response queryTipiDocumento(List<String> codici, Boolean addFormatoFile,
      String codicePadre, String codiceTipoPratica, SecurityContext securityContext) {
    List<TipoDocumento> documenti =
        tipoDocumentoService.getTipiDocumento(codici, addFormatoFile, codicePadre,
            codiceTipoPratica);

    return Response.ok(documenti).build();
  }

}
