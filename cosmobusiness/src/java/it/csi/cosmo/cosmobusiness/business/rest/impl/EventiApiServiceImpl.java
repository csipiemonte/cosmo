/*
 * Copyright CSI-Piemonte - 2023
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package it.csi.cosmo.cosmobusiness.business.rest.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import it.csi.cosmo.cosmobusiness.business.rest.EventiApi;
import it.csi.cosmo.cosmobusiness.business.service.EventiService;
import it.csi.cosmo.cosmobusiness.dto.rest.Evento;
import it.csi.cosmo.cosmobusiness.dto.rest.PaginaEventi;

@SuppressWarnings("unused")
public class EventiApiServiceImpl extends ParentApiImpl implements EventiApi {

  @Autowired
  EventiService s;

  @Override
  public Response getEventi(String nome, String utente, String dtCreazionePrima,
      String dtCreazioneDopo, String dtScadenzaPrima, String dtScadenzaDopo, Boolean sospeso,
      String ente, Integer inizio, Integer dimensionePagina, SecurityContext securityContext) {
    PaginaEventi eventi = s.getEventi(nome, utente, dtCreazionePrima, dtCreazioneDopo, dtScadenzaPrima, dtScadenzaDopo,
        sospeso, ente, inizio, dimensionePagina);
    return Response.ok(eventi).build();
  }

  @Override
  public Response getEventiId(String id, SecurityContext securityContext) {
    Evento eventiId = s.getEventiId(id);
    return Response.ok(eventiId).build();
  }

  @Override
  public Response postEventi(Evento body, SecurityContext securityContext) {
    Evento postEventi = s.postEvento(body);
    return Response.status(201).entity(postEventi).build();
  }

  @Override
  public Response putEventiId(String id, Evento body, SecurityContext securityContext) {
    Evento putEventi = s.putEvento(id, body);
    return Response.ok(putEventi).build();
  }

  @Override
  public Response deleteEventiId(String id, SecurityContext securityContext) {
    s.deleteEvento(id);
    return Response.ok().build();
  }

}
