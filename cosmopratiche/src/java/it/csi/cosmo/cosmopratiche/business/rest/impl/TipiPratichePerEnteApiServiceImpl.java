/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmopratiche.business.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmopratiche.business.rest.TipiPratichePerEnteApi;
import it.csi.cosmo.cosmopratiche.business.service.TipiPraticheService;
import it.csi.cosmo.cosmopratiche.dto.rest.TipoPratica;


public class TipiPratichePerEnteApiServiceImpl extends ParentApiImpl implements TipiPratichePerEnteApi {

  @Autowired
  TipiPraticheService p;


  @Override
  public Response getTipiPratichePerEnte(Integer idEnte, Boolean creazionePratica, Boolean conEnte,
      SecurityContext securityContext) {
    List<TipoPratica> tipoPratica = p.getTipiPraticaPerEnte(idEnte, creazionePratica, conEnte);
    return Response.ok(tipoPratica).build();
  }


}
