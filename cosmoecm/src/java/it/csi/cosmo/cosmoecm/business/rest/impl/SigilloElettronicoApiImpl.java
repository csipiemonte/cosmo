/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package it.csi.cosmo.cosmoecm.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmoecm.business.rest.SigilloElettronicoApi;
import it.csi.cosmo.cosmoecm.business.service.SigilloElettronicoService;
import it.csi.cosmo.cosmoecm.dto.rest.CreaCredenzialiSigilloElettronicoRequest;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronico;
import it.csi.cosmo.cosmoecm.dto.rest.CredenzialiSigilloElettronicoResponse;

/**
 *
 */

public class SigilloElettronicoApiImpl extends ParentApiImpl implements SigilloElettronicoApi {

  @Autowired
  SigilloElettronicoService sigilloElettronicoService;

  @Override
  public Response deleteSigilloElettronicoId(Integer id, SecurityContext securityContext) {
    sigilloElettronicoService.deleteSigilloElettronico(id);
    return Response.noContent().build();
  }

  @Override
  public Response getSigilloElettronicoId(Integer id, SecurityContext securityContext) {
    CredenzialiSigilloElettronico sigilloElettronico =
        sigilloElettronicoService.getSigilloElettronicoId(id);
    return Response.ok(sigilloElettronico).build();
  }

  @Override
  public Response putSigilloElettronicoId(Integer id, CreaCredenzialiSigilloElettronicoRequest body,
      SecurityContext securityContext) {
    CredenzialiSigilloElettronico sigilloElettronico =
        sigilloElettronicoService.updateSigilloElettronico(id, body);
    return Response.ok(sigilloElettronico).build();

  }

  @Override
  public Response postSigilloElettronico(CreaCredenzialiSigilloElettronicoRequest body,
      SecurityContext securityContext) {
    CredenzialiSigilloElettronico sigilloElettronico =
        sigilloElettronicoService.creaSigilloElettronico(body);
    return Response.ok(sigilloElettronico).build();
  }

  @Override
  public Response getSigilloElettronico(String filter, SecurityContext securityContext) {
    CredenzialiSigilloElettronicoResponse sigilloElettronicoResponse =
        sigilloElettronicoService.getSigilloElettronico(filter);
    return Response.ok(sigilloElettronicoResponse).build();
  }

  @Override
  public Response postSigilloElettronicoIdPraticaAggiornaSigilliInErrore(Long idPratica,
      String identificativoEvento, String identificativoAlias, SecurityContext securityContext) {
    sigilloElettronicoService.aggiornaSigilliInErrore(idPratica, identificativoEvento, identificativoAlias);
    return Response.noContent().build();
  }
}
